package models;

public class Rental {
    private Long id;
    private User user;
    private Book book;
    private int days;

    public Rental(Long id, User user, Book book, int days) {
        this.id = id; this.user = user; this.book = book; this.days = days;
    }

    public User getUser() { return user; }
    public Book getBook() { return book; }
    public int getDays() { return days; }
}
