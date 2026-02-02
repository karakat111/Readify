package models;

import java.time.LocalDateTime;

public class Rental {
    private Long id;
    private User user;
    private Book book;
    private int days;
    private LocalDateTime rentedAt;

    public Rental(Long id, User user, Book book, int days, LocalDateTime rentedAt) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.days = days;
        this.rentedAt = rentedAt;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Book getBook() { return book; }
    public int getDays() { return days; }
    public LocalDateTime getRentedAt() { return rentedAt; }
}
