package models;

public class Review {
    private Long id;
    private User user;
    private Book book;
    private int rating; // 1-5
    private String comment;

    public Review(Long id, User user, Book book, int rating, String comment) {
        if (rating < 1 || rating > 5) throw new IllegalArgumentException("Rating must be 1-5");
        this.id = id;
        this.user = user;
        this.book = book;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Book getBook() { return book; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }

    @Override
    public String toString() {
        return String.format("%s rated %s: %d/5 - \"%s\"",
                user.getUsername(), book.getTitle(), rating, comment);
    }
}
