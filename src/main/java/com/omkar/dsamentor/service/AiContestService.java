
package com.omkar.dsamentor.service;

import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.omkar.dsamentor.model.ContestQuestion;
import com.omkar.dsamentor.model.DSAProblem;
import com.omkar.dsamentor.model.User;
import com.omkar.dsamentor.repository.DsaProblemRepository;
import com.omkar.dsamentor.repository.UserProblemProgressRepository;

public class AiContestService {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final int CONTEST_SIZE = 3;

    // These identifiers match the levels used by the current application.
    private static final List<String> DSA_LEVELS = List.of(
            "NEW",
            "BEGINEER",
            "BEGINEER1",
            "INTERMEDIATE",
            "INTERMEDIATE1",
            "INTERMEDIATE2",
            "INTERMEDIATE3",
            "MASTER"
    );

    public static List<ContestQuestion> generateContest(User user) throws SQLException, IOException, InterruptedException {

        if (user == null || user.getDsaLevel() == null || user.getDsaLevel().isBlank()) {
            throw new IllegalArgumentException(
                    "A logged-in user with a DSA level is required.");
        }

        // 1. Retrieve all recorded solved-question IDs for this user.
        Set<Integer> savedIds=UserProblemProgressRepository.getProblemIdForCheckUnsolvedProblemsByUser(user.getUsername());

        Set<Integer> solvedIds = savedIds == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(savedIds);

        // 2. Collect question metadata across the known DSA levels.
        // Only metadata is used here; no candidate question list is sent
        // to Groq for contest selection.
        Map<Integer, DSAProblem> questionMetadata=loadMetadataAcrossLevels();

        // Each question ID is counted at most once per category.
        Map<String, Set<Integer>> dataStructureCounts=new LinkedHashMap<>();
        Map<String, Set<Integer>> patternCounts=new LinkedHashMap<>();
        Map<String, Set<Integer>> difficultyCounts=new LinkedHashMap<>();


        // 3. Build learner statistics from solved IDs with metadata.
        for (Integer id : solvedIds) {
            DSAProblem problem = questionMetadata.get(id);

            // The total count still includes this ID even if its metadata
            // is unavailable. Its category counts cannot be inferred.
            if (problem == null) {
                continue;
            }

            // In AlgoPilot, topic represents the data structure/practice area.
            String dataStructure = safe(problem.getTopic());

            addCount(dataStructureCounts, dataStructure, id);

            // Count the patterns associated with this solved question.
            String patternValue = safe(problem.getPattern());

            String[] patterns = patternValue.split("\\s*[,;|+]\\s*");

            for (String pattern : patterns) {

                String normalizedPattern = normalizePattern(pattern);

                addCount(patternCounts, normalizedPattern, id);
            }

            String difficulty = normalizeDifficulty(problem.getDifficulty());
            addCount(difficultyCounts, difficulty, id);
        }

        // 4. Prepare the exact learner profile sent to Groq.
        StringBuilder learnerProfile = new StringBuilder();

        learnerProfile.append("LEARNER PROFILE\n\n");

        learnerProfile.append("CURRENT DSA LEVEL: ")
                .append(user.getDsaLevel())
                .append("\n\n");

        learnerProfile.append(
                "TOTAL RECORDED SOLVED QUESTION COUNT "
                + "(ALL DIFFICULTIES): ")
                .append(solvedIds.size())
                .append("\n\n");

        learnerProfile.append(
                "RECORDED SOLVED QUESTIONS BY DIFFICULTY:\n");

        learnerProfile.append("Easy: ")
                .append(count(difficultyCounts, "Easy"))
                .append('\n');

        learnerProfile.append("Medium: ")
                .append(count(difficultyCounts, "Medium"))
                .append('\n');

        learnerProfile.append("Hard: ")
                .append(count(difficultyCounts, "Hard"))
                .append("\n\n");

        learnerProfile.append("RECORDED PRACTICE DATA STRUCTURES:\n");
        appendCounts(learnerProfile, dataStructureCounts);

        learnerProfile.append("\nRECORDED SOLVED PATTERNS:\n");
        appendCounts(learnerProfile, patternCounts);

        learnerProfile.append(
                "\nNOTE: Category counts include only solved IDs "
                + "whose question metadata was found.\n");

        // 5. Give Groq the profile, not a question catalogue.
        String systemPrompt = """
                You are an AI contest designer for a DSA learning platform.

                Generate exactly three DISTINCT, established DSA questions
                using the learner profile provided in the user message.
                You are NOT given a question catalogue. Choose questions
                independently from your existing knowledge.

                PERSONALIZATION:
                - Respect the user's current DSA level.
                - Use the recorded data structures and patterns to
                personalize the contest.
                - Use practiced patterns for reinforcement and introduce
                suitable challenges at the learner's level.
                - Avoid three questions testing the same idea when possible.
                - These are recorded learning activities, not independently
                verified evidence of mastery.

                RETURN ONLY THESE FIVE FIELDS PER QUESTION:
                - title
                - platform
                - url
                - difficulty
                - estimatedMinutes

                Do not return topic, pattern, dataStructures, problemNumber,
                hints, explanations, solution code, or other fields.

                PLATFORM AND URL:
                - Prefer established LeetCode or GeeksforGeeks questions.
                - Use the correct platform name.
                - Return a canonical HTTPS URL only when reasonably confident.
                - If the URL is uncertain, return null.
                - Never claim that a URL was opened or verified.

                TIME ESTIMATES:
                - Provide a realistic independent-solving estimate in minutes.
                - Easy: usually 15-25 minutes.
                - Medium: usually 25-45 minutes.
                - Hard: usually 40-75 minutes.
                - estimatedMinutes must be an integer from 5 to 120.

                OUTPUT FORMAT:
                Return valid JSON only:

                {
                  "questions": [
                    {
                      "title": "Problem title",
                      "platform": "LeetCode",
                      "url": "https://leetcode.com/problems/example/",
                      "difficulty": "Medium",
                      "estimatedMinutes": 30
                    }
                  ]
                }

                The example illustrates the format only.
                Return exactly three distinct questions.
                Do not add Markdown fences or explanatory prose.
                """;

        String userPrompt = learnerProfile + """
                Generate a personalized contest of exactly three distinct
                questions using this learner profile. Return only the
                five requested fields for each question.
                """;

        // Uses the temperature overload in our existing GroqApiClient.
        String response = GroqApiClient.generateText(systemPrompt,userPrompt,0.8);

        // 6. Parse and validate Groq's response.
        JsonNode root = parseJsonObject(response);
        JsonNode questionsNode = root.path("questions");

        if (!questionsNode.isArray() || questionsNode.size() != CONTEST_SIZE) {
            throw new IOException("AI did not return exactly three contest questions.");
        }

        List<ContestQuestion> contest = new ArrayList<>();
        Set<String> uniqueQuestions = new LinkedHashSet<>();

        for (JsonNode node : questionsNode) {

            String title = text(node, "title");
            String platform = normalizePlatform(text(node, "platform"));
            String difficulty = normalizeDifficulty(
                    text(node, "difficulty"));

            if (title.isBlank()) {
                throw new IOException("AI returned a question without a title.");
            }

            if ("Unknown".equals(difficulty)) {
                throw new IOException("AI returned an invalid question difficulty.");
            }

            JsonNode timeNode=node.path("estimatedMinutes");

            if (!timeNode.canConvertToInt()) {
                throw new IOException("AI returned an invalid estimatedMinutes value.");
            }

            int estimatedMinutes=timeNode.asInt();

            if (estimatedMinutes<5 || estimatedMinutes>120) {
                throw new IOException("AI returned an unreasonable time estimate.");
            }

            String url = validateUrl(text(node, "url"));

            // Detect duplicate URLs or duplicate platform/title pairs.
            String uniqueKey = url != null
                    ? url.toLowerCase(Locale.ROOT)
                    : platform + ":"
                            + title.toLowerCase(Locale.ROOT);

            if (!uniqueQuestions.add(uniqueKey)) {
                throw new IOException("AI returned duplicate contest questions.");
            }

            // Constructor overload described below stores the AI estimate.
            contest.add(new ContestQuestion(
                    title,
                    platform,
                    url,
                    null,
                    null,
                    null,
                    difficulty,
                    0,
                    estimatedMinutes));
        }

        return contest;
    }

    private static Map<Integer, DSAProblem> loadMetadataAcrossLevels()
            throws SQLException {

        Map<Integer, DSAProblem> allQuestions = new LinkedHashMap<>();

        for (String level : DSA_LEVELS) {

            List<DSAProblem> problems=DsaProblemRepository.getEligibleProblems(level);

            if (problems == null) {
                continue;
            }

            for (DSAProblem problem : problems) {

                if (problem != null) {
                    allQuestions.putIfAbsent(
                            problem.getProblemID(),
                            problem);
                }
            }
        }

        return allQuestions;
    }



    private static void addCount(Map<String, Set<Integer>> counts,String label,int questionId) {
        if (label == null
                || label.isBlank()
                || label.equals("Not available")
                || label.equals("Unknown")) {
            return;
        }

        counts.computeIfAbsent(
                label,
                key -> new HashSet<>()).add(questionId);
    }

    private static int count(
            Map<String, Set<Integer>> counts,
            String label) {

        Set<Integer> ids = counts.get(label);

        return ids == null ? 0 : ids.size();
    }





    private static void appendCounts(StringBuilder profile,Map<String, Set<Integer>> counts) {
        if (counts.isEmpty()) {
            profile.append("- No recorded metadata available.\n");
            return;
        }

        for (Map.Entry<String, Set<Integer>> entry : counts.entrySet()) {

            profile.append("- ")
                    .append(entry.getKey())
                    .append(": ")
                    .append(entry.getValue().size())
                    .append(" questions\n");
        }
    }



    private static String normalizePattern(String value) {

        if (value == null || value.isBlank()) {
            return "Not available";
        }

        String pattern = value.trim().toLowerCase(Locale.ROOT);

        if (pattern.contains("2 pointer")
                || pattern.contains("two pointer")) {
            return "Two Pointers";
        }

        if (pattern.contains("sliding window")) {
            return "Sliding Window";
        }

        if (pattern.contains("binary search")) {
            return "Binary Search";
        }

        if (pattern.contains("prefix sum")) {
            return "Prefix Sum";
        }

        if (pattern.contains("fast and slow")
                || pattern.contains("fast/slow")) {
            return "Fast and Slow Pointers";
        }

        if (pattern.contains("backtracking")) {
            return "Backtracking";
        }

        if (pattern.contains("dynamic programming")) {
            return "Dynamic Programming";
        }

        if (pattern.contains("monotonic stack")) {
            return "Monotonic Stack";
        }

        if (pattern.contains("hash")) {
            return "Hashing";
        }

        // Preserve unknown pattern names rather than inventing a category.
        return value.trim();
    }




    private static String normalizeDifficulty(String value) {

        if (value == null) {
            return "Unknown";
        }

        return switch (value.trim().toLowerCase(Locale.ROOT)) {
            case "easy" -> "Easy";
            case "medium" -> "Medium";
            case "hard" -> "Hard";
            default -> "Unknown";
        };
    }

    private static String normalizePlatform(String value)
            throws IOException {

        String platform = value.toLowerCase(Locale.ROOT);

        if (platform.contains("leetcode")) {
            return "LeetCode";
        }

        if (platform.contains("geeksforgeeks")
                || platform.equals("gfg")) {
            return "GeeksforGeeks";
        }

        throw new IOException(
                "AI returned an unsupported platform: " + value);
    }

    private static String text(JsonNode node, String field) {

        JsonNode value = node.path(field);

        return value.isTextual()
                ? value.asText().trim()
                : "";
    }





    private static JsonNode parseJsonObject(String response)
            throws IOException {

        if (response == null || response.isBlank()) {
            throw new IOException("AI returned an empty response.");
        }

        int firstBrace = response.indexOf('{');
        int lastBrace = response.lastIndexOf('}');

        if (firstBrace < 0 || lastBrace < firstBrace) {
            throw new IOException(
                    "AI response did not contain a JSON object.");
        }

        JsonNode root = MAPPER.readTree(
                response.substring(firstBrace, lastBrace + 1));

        if (root == null || !root.isObject()) {
            throw new IOException("AI returned invalid JSON.");
        }

        return root;
    }


    

    private static String validateUrl(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            URI uri = URI.create(value.trim());

            if (!"https".equalsIgnoreCase(uri.getScheme())
                    || uri.getHost() == null) {
                return null;
            }

            String host = uri.getHost().toLowerCase(Locale.ROOT);

            boolean allowedHost =
                    host.equals("leetcode.com")
                    || host.endsWith(".leetcode.com")
                    || host.equals("geeksforgeeks.org")
                    || host.endsWith(".geeksforgeeks.org");

            return allowedHost ? uri.toString() : null;

        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static String safe(String value) {
        return value == null || value.isBlank()
                ? "Not available"
                : value.trim();
    }
}
