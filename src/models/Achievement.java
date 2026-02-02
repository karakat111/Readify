package models;

public class Achievement {
    private String title;
    private String description;
    private int stars;

    public Achievement(String title, String description, int stars) {
        this.title = title;
        this.description = description;
        this.stars = stars;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStars() { return stars; }

    @Override
    public String toString() {
        return String.format("⭐️ %s | %s | Stars: %d", title, description, stars);
    }
}
