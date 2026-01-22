package models;

import java.util.List;

public class Order {
    private Long id;
    private User user;
    private List<OrderItem> items;

    public Order(Long id, User user, List<OrderItem> items) {
        if(user == null) throw new IllegalArgumentException("User cannot be null");
        if(items == null || items.isEmpty()) throw new IllegalArgumentException("Order must have items");
        this.id = id;
        this.user = user;
        this.items = items;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public List<OrderItem> getItems() { return items; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id)
                .append("\nBuyer: ").append(user.getUsername())
                .append("\nItems:\n");
        items.forEach(item -> sb.append(" - ").append(item).append("\n"));
        double total = items.stream()
                .mapToDouble(i -> i.getBook().getPrice() * i.getQuantity())
                .sum();
        sb.append("Total: ").append(total);
        return sb.toString();
    }
}
