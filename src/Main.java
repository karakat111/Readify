import service.BookService;
import service.OrderService;
import service.ReservationService;
import service.CategoryService;
import service.UserService;
import ui.ConsoleApp;

public class Main {
    public static void main(String[] args) {
        BookService bookService = new BookService();
        OrderService orderService = new OrderService();
        ReservationService reservationService = new ReservationService();
        CategoryService categoryService = new CategoryService();
        UserService userService = new UserService();

        ConsoleApp app = new ConsoleApp(bookService, orderService, reservationService, categoryService,userService);
        app.start();
    }
}
