
package com.omkar.dsamentor.service;

import java.io.IOException;

import com.omkar.dsamentor.model.DSAProblem;
import com.omkar.dsamentor.model.User;

public class AiDsaMentorService {

    public enum AssistanceOption {
        EXPLAIN_AND_DRY_RUN,
        BRUTE_FORCE_APPROACH,
        OPTIMAL_APPROACH
    }

    

    public static String getAssistance(User user,DSAProblem problem,AssistanceOption option) throws IOException, InterruptedException {
        if (user == null || problem == null || option == null) {
            throw new IllegalArgumentException(
                    "User, problem, and assistance option are required.");
        }

        // General instructions for every AI assistance mode.
        String systemPrompt = """
                You are the AI DSA Mentor in a Java console application.
                Teach like a patient tutor who helps students understand
                data structures and algorithms through examples.

                CONSOLE FORMATTING RULES:
                - Use plain text and ASCII characters.
                - Use clear section headings and consistent separators.
                - Use aligned ASCII tables for dry runs and complexity.
                - Keep table columns readable in a typical terminal.
                - Use short paragraphs but explain the reasoning completely.
                - Avoid Markdown headings, bold markers, backticks,
                Markdown tables, and fenced code blocks.
                - Avoid excessive blank lines and repeated explanations.

                TEACHING RULES:
                - Adapt explanations to the user's current DSA level.
                - Explain why each important decision is made.
                - Explain relevant data structures and algorithmic patterns.
                - Never provide complete solution code.
                - Do not invent problem constraints or examples.
                - Do not claim that an example is official unless its source
                is known.
                - The supplied URL is context only; do not claim you opened it.
                - If the title is ambiguous, state the uncertainty rather
                than confidently explaining a different problem.
                - Focus only on the assistance mode requested by the user.

                Produce a structured teaching explanation, not a brief
                summary containing only technique names and complexities.
                """;

        // Problem information passed to Groq.
        String context = """
                USER'S CURRENT DSA LEVEL: %s
                PROBLEM ID: %d
                PROBLEM TITLE: %s
                PLATFORM: %s
                TOPIC: %s
                DIFFICULTY: %s
                PROBLEM URL: %s
                """.formatted(
                safe(user.getDsaLevel()),
                problem.getProblemID(),
                safe(problem.getTitle()),
                safe(problem.getPlatform()),
                safe(problem.getTopic()),
                safe(problem.getDifficulty()),
                safe(problem.getProURL()));

        String instructions;

        switch (option) {

            // OPTION 1: Explain the question and dry-run an example.
            case EXPLAIN_AND_DRY_RUN:
                instructions = """
                        TASK: EXPLAIN THE QUESTION AND DRY-RUN AN EXAMPLE

                        Aim for approximately 200-300 words when appropriate.

                        SECTION 1 - UNDERSTAND THE QUESTION
                        Explain what the problem asks in simple language.
                        Describe the input, expected output, and important
                        requirements that can be established reliably.

                        SECTION 2 - EXAMPLE
                        Show a small, valid example and its expected result.
                        If the original example is unavailable, label the
                        example as illustrative.

                        SECTION 3 - DETAILED DRY RUN
                        Explain the example step by step.
                        Introduce important variables before the dry run.
                        Show important comparisons, decisions, and state
                        changes in an ASCII table.

                        For an array problem, show how the array changes
                        after meaningful operations. Explain which indices
                        are accessed and why each action occurs.

                        Do not use unexplained arrows or pointer values.
                        Do not introduce brute-force or optimal approaches.
                        Do not provide solution code.

                        SECTION 4 - KEY TAKEAWAY
                        Finish with the most important observation the
                        user should understand about the question.
                        """;
                break;

            // OPTION 2: Teach only the brute-force approach.
            case BRUTE_FORCE_APPROACH:
                instructions = """
                        TASK: TEACH THE BRUTE-FORCE APPROACH ONLY

                        Aim for approximately 350-500 words when needed.
                        Explain one approach in depth rather than comparing
                        multiple approaches superficially.

                        SECTION 1 - CORE IDEA
                        Explain the straightforward way to solve the problem.
                        Describe the steps in simple language and explain
                        why this method works.

                        SECTION 2 - DATA STRUCTURE AND PATTERN
                        Identify the data structure and algorithmic pattern
                        used, if applicable. Explain what each important
                        variable represents and how it helps solve the task.

                        SECTION 3 - DETAILED DRY RUN
                        Select a small, valid example.

                        Start with:
                        Input: [actual example]
                        Goal : [expected result]

                        Explain the initial state before showing the trace.
                        Use an aligned ASCII table with columns appropriate
                        for the chosen algorithm.

                        For each important step, explain:
                        - Which values or indices are examined.
                        - Which comparison or condition is evaluated.
                        - Why the algorithm takes that action.
                        - How the array or other state changes.
                        - What happens in the next step.

                        If the approach repeatedly shifts array elements,
                        demonstrate how the remaining elements move.
                        If it uses nested loops, explain what each loop does
                        and where repeated work occurs.

                        Do not force array shifting, nested loops, or any
                        particular data structure onto unrelated problems.

                        SECTION 4 - COMPLEXITY
                        Show a compact ASCII table:

                        +----------------------+----------------+
                        | Complexity Type      | Cost           |
                        +----------------------+----------------+
                        | Time                 | Actual O(...)  |
                        | Auxiliary Space      | Actual O(...)  |
                        +----------------------+----------------+

                        Replace the placeholders with correct complexities
                        for the actual brute-force algorithm. Briefly
                        explain why the time complexity has that cost.

                        SECTION 5 - LIMITATION
                        Explain the main weakness of this approach.
                        Connect the limitation to the example when possible.
                        Do not explain or reveal the complete optimal approach.

                        Do not provide complete solution code or long
                        pseudocode. Keep the response focused on brute force.
                        """;
                break;

            // OPTION 3: Teach only the optimal approach.
            case OPTIMAL_APPROACH:
                instructions = """
                        TASK: TEACH THE OPTIMAL APPROACH ONLY

                        Aim for approximately 350-500 words when needed.
                        The main goal is to help the student understand
                        why the algorithm works, not merely name a pattern.

                        SECTION 1 - KEY OBSERVATION
                        Explain the important property of the problem that
                        makes an efficient solution possible.

                        SECTION 2 - DATA STRUCTURE AND PATTERN
                        Identify the suitable data structure and/or pattern.
                        Explain what each important variable or pointer
                        represents and why it is required.

                        SECTION 3 - HOW THE ALGORITHM WORKS
                        Describe the algorithm in clear, logical steps.
                        Explain the purpose of each important comparison,
                        pointer movement, update, or decision.

                        Explain why the approach is correct and how it
                        avoids unnecessary operations. You may briefly
                        relate the improvement to repeated work that a
                        straightforward approach would perform, but do not
                        turn this into a second full algorithm tutorial.

                        SECTION 4 - DETAILED DRY RUN
                        Select a small, valid example.

                        Start with:
                        Input: [actual example]
                        Goal : [expected result]

                        Explain the initial values of important variables.
                        Then show a compact, readable ASCII trace.

                        The trace should show:
                        - The current values and indices being examined.
                        - The comparison or condition being evaluated.
                        - The action taken and the reason for it.
                        - Updated pointers, variables, or array state.

                        For array modification problems, show the array
                        after meaningful writes. Distinguish the valid
                        result from unused trailing positions when relevant.

                        Do not merely list pointer values or arrows.
                        Explain why each important state change occurs.

                        SECTION 5 - COMPLEXITY
                        Show a compact ASCII table:

                        +----------------------+----------------+
                        | Complexity Type      | Cost           |
                        +----------------------+----------------+
                        | Time                 | Actual O(...)  |
                        | Auxiliary Space      | Actual O(...)  |
                        +----------------------+----------------+

                        Use correct complexities for the identified
                        algorithm and briefly explain why they apply.

                        SECTION 6 - WHY THIS APPROACH WORKS WELL
                        Finish with a short explanation connecting the
                        key observation, the chosen data structure or
                        pattern, and the efficiency of the algorithm.

                        Do not force a particular technique onto a
                        problem where it does not apply.
                        Do not provide complete solution code or long
                        pseudocode. Keep the response focused on the
                        selected optimal approach.
                        """;
                break;

            default:
                throw new IllegalArgumentException("Unsupported AI assistance option.");
        }

        // Call the Groq client that we already implemented.
        String response = GroqApiClient.generateText(systemPrompt,context + "\n" + instructions,0.2);

        if (response == null || response.isBlank()) {
            return "No explanation was returned. Please try again.";
        }

        // Remove common Markdown markers while keeping ASCII tables.
        return formatForConsole(response);
    }

    private static String safe(String value) {
        return value == null || value.isBlank()
                ? "Not available"
                : value.trim();
    }

    private static String formatForConsole(String response) {
        return response
                .replaceAll("(?m)^#{1,6}\\s*", "")
                .replace("**", "")
                .replace("__", "")
                .replace("`", "")
                .replaceAll("(?m)^\\s*-{3,}\\s*$", "")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
    }
}
