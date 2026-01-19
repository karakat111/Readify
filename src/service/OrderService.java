package service;

import models.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class OrderService {
    private List<Order> orders = new ArrayList<>();
    private Long orderIdCounter = 1L;

    public Order createOrder(User user, Map<Book, Integer> books) {
        if (books.isEmpty()) throw new RuntimeException("No books to order");

        double totalPrice = 0;
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            if (book.getStock() < quantity)
                throw new RuntimeException("Not enough books: " + book.getTitle());

            totalPrice += book.getPrice() * quantity;
        }

        Order order = new Order();
        order.setId(orderIdCounter++);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());
        order.setTotalPrice(totalPrice);

        List<OrderItem> items = new ArrayList<>();
        for (Map.Entry<Book, Integer> entry : books.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            OrderItem item = new OrderItem();
            item.setBook(book);
            item.setQuantity(quantity);
            items.add(item);

            book.setStock(book.getStock() - quantity);
        }
        order.setItems(items);
        orders.add(order);
        return order;
    }

    public List<Order> findOrdersByUser(User user) {
        List<Order> result = new ArrayList<>();
        for (Order o : orders) {
            if (o.getUser().equals(user)) result.add(o);
        }
        return result;
    }
}
