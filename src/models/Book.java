package models;

public class Book {
    private Long id;
    private String title;
    private String author;
    private Category category;
    private double price;
    private int stock;

    public Book(Long id, String title, String author, Category category, double price, int stock) {
        if(title == null || title.isBlank()) throw new IllegalArgumentException("Title is required");
        if(price < 0 || stock < 0) throw new IllegalArgumentException("Price and stock cannot be negative");
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Category getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s by %s | Category: %s | Price: %.2f | Stock: %d",
                id, title, author, category.getName(), price, stock);
    }
}
