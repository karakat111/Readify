package ui;

import models.Book;
import models.User;
import service.*;

import java.util.*;

public class ConsoleUI {
    private BookService bookService;
    private OrderService orderService;
    private ReservationService reservationService;
    private RentalService rentalService;
    private User currentUser;

    private Scanner scanner = new Scanner(System.in);

    public ConsoleUI(BookService bookService,
                     OrderService orderService,
                     ReservationService reservationService,
                     RentalService rentalService,
                     User currentUser) {
        this.bookService = bookService;
        this.orderService = orderService;
        this.reservationService = reservationService;
        this.rentalService = rentalService;
        this.currentUser = currentUser;
    }

    public void start() {
        boolean running = true;
        while (running) {
            System.out.println("\n=== Readify Menu ===");
            System.out.println("1. Просмотреть книги");
            System.out.println("2. Купить книгу");
            System.out.println("3. Забронировать книгу");
            System.out.println("4. Взять книгу в прокат");
            System.out.println("0. Выйти");
            System.out.print("Выберите: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showBooks();
                case 2 -> buyBook();
                case 3 -> reserveBook();
                case 4 -> rentBook();
                case 0 -> running = false;
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void showBooks() {
        System.out.println("\nДоступные книги:");
        for (Book b : bookService.getAllBooks()) {
            System.out.println(b.getId() + ". " + b.getTitle() + " | Автор: " + b.getAuthor() +
                    " | Жанр: " + b.getGenre() + " | Цена: " + b.getPrice() +
                    " | Остаток: " + b.getStock());
        }
    }

    private void buyBook() {
        System.out.print("Введите ID книги для покупки: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        Book book = bookService.findById(id);
        if (book == null) {
            System.out.println("Книга не найдена!");
            return;
        }
        if (book.getStock() < 1) {
            System.out.println("Книга закончилась!");
            return;
        }
        Map<Book, Integer> map = new HashMap<>();
        map.put(book, 1);
        orderService.createOrder(currentUser, map);
        System.out.println("Книга куплена!");
    }

    private void reserveBook() {
        System.out.print("Введите ID книги для брони: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        Book book = bookService.findById(id);
        if (book == null || book.getStock() < 1) {
            System.out.println("Книга недоступна!");
            return;
        }
        reservationService.reserveBook(currentUser, book, 3);
        System.out.println("Книга забронирована на 3 дня!");
    }

    private void rentBook() {
        System.out.print("Введите ID книги для проката: ");
        Long id = scanner.nextLong();
        scanner.nextLine();
        Book book = bookService.findById(id);
        if (book == null || book.getStock() < 1) {
            System.out.println("Книга недоступна!");
            return;
        }
        rentalService.rentBook(currentUser, book, 7);
        System.out.println("Книга взята в прокат на 7 дней!");
    }
}
