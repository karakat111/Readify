package ui;

import models.*;
import service.*;

import java.util.List;
import java.util.Scanner;

public class ConsoleApp {
    private Scanner scanner = new Scanner(System.in);
    private BookService bookService;
    private ReservationService reservationService;
    private OrderService orderService;
    private CategoryService categoryService;
    private UserService userService;
    private ReviewService reviewService;
    private CouponService couponService;
    private AchievementService achievementService;
    private User currentUser;

    public ConsoleApp(BookService bookService, OrderService orderService,
                      ReservationService reservationService, CategoryService categoryService,
                      UserService userService, ReviewService reviewService,
                      CouponService couponService, AchievementService achievementService) {
        this.bookService = bookService;
        this.reservationService = reservationService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.reviewService = reviewService;
        this.couponService = couponService;
        this.achievementService = achievementService;
    }

    public void start() {
        System.out.println("=== Welcome to Readify ===");
        authMenu();
    }

    private void authMenu() {
        while (true) {
            System.out.println("\n=== READIFY ===");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    if (login()) {
                        mainMenu();
                    }
                }
                case "2" -> register();
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private boolean login() {
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        currentUser = userService.login(email, password);
        if (currentUser == null) {
            System.out.println("Invalid email or password!");
            return false;
        }

        System.out.println("✓ Logged in as " + currentUser.getUsername() + " (" + currentUser.getRole() + ")");
        return true;
    }

    private void register() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();


        try {
            currentUser = userService.register(username, email, password);
            if (currentUser != null) {
                System.out.println("✓ Registration successful! You are now logged in as USER.");
                System.out.println("(Admins and Managers are added manually via database)");
                mainMenu();
            } else {
                System.out.println("✗ Registration failed. Please try again.");
            }
        } catch (RuntimeException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void mainMenu() {
        while (true) {
            System.out.println("\n=== MENU ===");
            System.out.println("1. List Books");
            System.out.println("2. Rent Book");
            System.out.println("3. Reserve Book");
            System.out.println("4. Add Book");
            System.out.println("5. Review Book");
            System.out.println("6. View Book Reviews");
            System.out.println("7. View Achievements");
            if (currentUser.isAdminOrManager()) {
                System.out.println("8. Create Coupon");
            }
            System.out.println("0. Logout");
            System.out.print("Choose option: ");

            String choice = scanner.nextLine();

            if (choice.equals("0")) {
                System.out.println("✓ Logged out successfully!");
                currentUser = null;
                return;
            }

            switch (choice) {
                case "1" -> listBooks();
                case "2" -> rentBook();
                case "3" -> reserveBook();
                case "4" -> addBook();
                case "5" -> reviewBook();
                case "6" -> viewBookReviews();
                case "7" -> viewAchievements();
                case "8" -> {
                    if (currentUser.isAdminOrManager()) createCoupon();
                    else System.out.println("✗ Access denied.");
                }
                default -> System.out.println("✗ Invalid option");
            }
        }
    }

    private void listBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        System.out.println("\n=== AVAILABLE BOOKS ===");
        books.forEach(book -> {
            double avgRating = reviewService.getAverageRating(book.getId());
            System.out.println(book + " | Rating: " + String.format("%.1f", avgRating) + "/5");
        });
    }

    private void rentBook() {
        if (!currentUser.canRent()) {
            System.out.println("✗ No permission to rent books.");
            return;
        }

        System.out.print("Enter Book ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Book book = bookService.getBookById(id);

        if (book == null) {
            System.out.println("✗ Book not found!");
            return;
        }

        System.out.print("Days to rent: ");
        int days = Integer.parseInt(scanner.nextLine());

        try {
            Rental rental = reservationService.rentBook(currentUser, book, days);
            if (rental != null) {
                System.out.println("✓ Book rented successfully!");
                System.out.println("Rented at: " + rental.getRentedAt());
            }
        } catch (RuntimeException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void reserveBook() {
        if (!currentUser.canReserve()) {
            System.out.println("✗ No permission to reserve books.");
            return;
        }

        System.out.print("Enter Book ID: ");
        long id = Long.parseLong(scanner.nextLine());
        Book book = bookService.getBookById(id);

        if (book == null) {
            System.out.println("✗ Book not found!");
            return;
        }


        try {
            reservationService.reserveBook(currentUser, book);
            System.out.println("✓ Book reserved successfully!");
        } catch (RuntimeException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void addBook() {
        if (!currentUser.isAdminOrManager()) {
            System.out.println("✗ No permission to add books.");
            return;
        }

        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Category: ");
        String categoryName = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());

        Category category = categoryService.getCategoryByName(categoryName);
        if (category == null) {
            category = categoryService.addCategory(categoryName);
        }

        bookService.addBook(title, author, category, price, stock);
        System.out.println("✓ Book added successfully!");
    }

    private void reviewBook() {
        System.out.print("Enter Book ID to review: ");
        long bookId = Long.parseLong(scanner.nextLine());
        Book book = bookService.getBookById(bookId);

        if (book == null) {
            System.out.println("✗ Book not found!");
            return;
        }

        System.out.print("Rating (1-5): ");
        int rating = Integer.parseInt(scanner.nextLine());
        System.out.print("Comment: ");
        String comment = scanner.nextLine();

        try {
            Review review = reviewService.addReview(currentUser, book, rating, comment);
            if (review != null) {
                System.out.println("✓ Review added successfully!");
            } else {
                System.out.println("✗ Failed to add review.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void viewBookReviews() {
        System.out.print("Enter Book ID: ");
        long bookId = Long.parseLong(scanner.nextLine());

        List<Review> reviews = reviewService.getBookReviews(bookId);
        if (reviews.isEmpty()) {
            System.out.println("No reviews for this book yet.");
            return;
        }

        double avgRating = reviewService.getAverageRating(bookId);
        System.out.println("\n=== REVIEWS (Average: " + String.format("%.1f", avgRating) + "/5) ===");
        reviews.forEach(System.out::println);
    }

    private void viewAchievements() {
        // Get user's rentals
        List<Rental> userRentals = reservationService.getUserRentals(currentUser.getId());

        // Dummy data for orders and reviews (replace with real counts)
        int ordersCount = 5;
        int rentalsCount = userRentals.size();
        int reviewsCount = 2;

        List<Achievement> achievements = achievementService.checkAchievements(
                currentUser, ordersCount, rentalsCount, reviewsCount, userRentals
        );

        if (achievements.isEmpty()) {
            System.out.println("No achievements yet. Keep reading!");
            return;
        }

        System.out.println("\n=== YOUR ACHIEVEMENTS ===");
        achievements.forEach(System.out::println);

        int totalStars = achievements.stream().mapToInt(Achievement::getStars).sum();
        System.out.println("\n⭐️ Total Stars: " + totalStars);
    }

    private void createCoupon() {
        System.out.print("Coupon Code: ");
        String code = scanner.nextLine();
        System.out.print("Discount (%): ");
        double discount = Double.parseDouble(scanner.nextLine());


        try {
            Coupon coupon = couponService.createCoupon(code, discount);
            if (coupon != null) {
                System.out.println("✓ Coupon created: " + coupon);
            } else {
                System.out.println("✗ Failed to create coupon.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
