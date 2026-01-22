package models;

public class OrderItem {
    private Book book;
    private int quantity;

    public OrderItem(Book book, int quantity) {
        if(book == null) throw new IllegalArgumentException("Book cannot be null");
        if(quantity <= 0) throw new IllegalArgumentException("Quantity must be > 0");
        this.book = book;
        this.quantity = quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }

    @Override
    public String toString() {
        return String.format("%s x %d | Price: %.2f", book.getTitle(), quantity, book.getPrice());
    }
}
