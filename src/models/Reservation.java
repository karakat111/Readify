package models;

public class Reservation {
    private Long id;
    private User user;
    private Book book;

    public Reservation(Long id, User user, Book book) {
        this.id = id; this.user = user; this.book = book;
    }

    public User getUser() { return user; }
    public Book getBook() { return book; }
}
