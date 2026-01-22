package ui;

import models.Book;
import models.Category;
import models.User;
import service.BookService;
import service.OrderService;
import service.ReservationService;
import service.CategoryService;
import service.UserService;

import java.util.Scanner;

public class ConsoleApp {
    private Scanner scanner = new Scanner(System.in);
    private BookService bookService;
    private ReservationService reservationService;
    private OrderService orderService;
    private CategoryService categoryService;
    private UserService userService;
    private User currentUser;

    public ConsoleApp(BookService bookService, OrderService orderService,
                      ReservationService reservationService, CategoryService categoryService,
                      UserService userService) {
        this.bookService = bookService;
        this.reservationService = reservationService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public void start() {
        System.out.println("=== Welcome to Readify ===");
        login();
        mainMenu();
    }

    private void login() {
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter role (ADMIN, MANAGER, USER):");
        String role = scanner.nextLine().toUpperCase();

        // Add or get user from DB
        currentUser = userService.addOrGetUser(username, role);
        System.out.println("Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n1.List books 2.Rent 3.Reserve 4.Add Book 5.Exit");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> bookService.getAllBooks().forEach(System.out::println);
                case "2" -> rentBook();
                case "3" -> reserveBook();
                case "4" -> addBook();
                case "5" -> {
                    System.out.println("Bye!");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void rentBook() {
        if (!currentUser.canRent()) {
            System.out.println("No permission to rent books.");
            return;
        }

        System.out.println("Enter Book ID:");
        long id = Long.parseLong(scanner.nextLine());
        Book book = bookService.getBookById(id);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("Days to rent:");
        int days = Integer.parseInt(scanner.nextLine());

        try {
            reservationService.rentBook(currentUser, book, days);
            System.out.println("Book rented!");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void reserveBook() {
        if (!currentUser.canReserve()) {
            System.out.println("No permission to reserve books.");
            return;
        }

        System.out.println("Enter Book ID:");
        long id = Long.parseLong(scanner.nextLine());
        Book book = bookService.getBookById(id);

        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        try {
            reservationService.reserveBook(currentUser, book);
            System.out.println("Book reserved!");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addBook() {
        if (!currentUser.isAdminOrManager()) {
            System.out.println("No permission to add books.");
            return;
        }

        System.out.println("Title:");
        String title = scanner.nextLine();
        System.out.println("Author:");
        String author = scanner.nextLine();
        System.out.println("Category:");
        String categoryName = scanner.nextLine();
        System.out.println("Price:");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.println("Stock:");
        int stock = Integer.parseInt(scanner.nextLine());

        // Check if category exists in DB
        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            category = categoryService.addCategory(categoryName);
        }

        bookService.addBook(title, author, category, price, stock);
        System.out.println("Book added!");
    }
}
