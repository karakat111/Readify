import models.Book;
import models.User;
import models.Role;
import service.*;
import ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {

        // Создаём пользователей
        User user = new User();
        user.setId(1L);
        user.setUsername("Ivan");
        user.setRole(Role.CUSTOMER);

        // Сервисы
        BookService bookService = new BookService();
        OrderService orderService = new OrderService();
        ReservationService reservationService = new ReservationService();
        RentalService rentalService = new RentalService();

        // Добавляем книги
        Book b1 = new Book();
        b1.setTitle("Java для начинающих");
        b1.setAuthor("Иван Иванов");
        b1.setGenre("Программирование");
        b1.setPrice(1000);
        b1.setStock(5);
        bookService.addBook(b1);

        Book b2 = new Book();
        b2.setTitle("Гарри Поттер");
        b2.setAuthor("Джоан Роулинг");
        b2.setGenre("Фэнтези");
        b2.setPrice(1200);
        b2.setStock(3);
        bookService.addBook(b2);

        // Консольный интерфейс
        ConsoleUI ui = new ConsoleUI(bookService, orderService, reservationService, rentalService, user);
        ui.start();
    }
}
