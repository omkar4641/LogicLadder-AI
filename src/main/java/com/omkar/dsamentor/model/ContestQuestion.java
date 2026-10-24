
package com.omkar.dsamentor.model;

public class ContestQuestion {

    private final String title;
    private final String platform;
    private final String url;
    private final String dataStructures;
    private final String difficulty;
    private final int problemNumber;
    private final int allocatedMinutes;

    public ContestQuestion(String title, String platform, String url, String difficulty, int estimatedMinutes) {

        this(
                title,
                platform,
                url,
                null,
                null,
                null,
                difficulty,
                0,
                estimatedMinutes);
    }

    public ContestQuestion(String title, String platform, String url, String topic, String patterns,
            String dataStructures, String difficulty, int problemNumber, int estimatedMinutes) {
        if (estimatedMinutes < 5 || estimatedMinutes > 120) {
            throw new IllegalArgumentException(
                    "Estimated time must be between 5 and 120 minutes.");
        }

        this.title = title;
        this.platform = platform;
        this.url = url;
        this.dataStructures = dataStructures;
        this.difficulty = difficulty;
        this.problemNumber = problemNumber;
        this.allocatedMinutes = estimatedMinutes;
    }

    public String getTitle() {
        return title;
    }

    public String getPlatform() {
        return platform;
    }

    public String getUrl() {
        return url;
    }

    public String getDataStructures() {
        return dataStructures;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public int getAllocatedMinutes() {
        return allocatedMinutes;
    }
}
