    import service.*;
    import ui.ConsoleApp;

    public class Main {
        public static void main(String[] args) {
            BookService bookService = new BookService();
            OrderService orderService = new OrderService();
            ReservationService reservationService = new ReservationService();
            CategoryService categoryService = new CategoryService();
            UserService userService = new UserService();
            ReviewService reviewService = new ReviewService();
            CouponService couponService = new CouponService();
            AchievementService achievementService = new AchievementService();

            ConsoleApp app = new ConsoleApp(
                    bookService,
                    orderService,
                    reservationService,
                    categoryService,
                    userService,
                    reviewService,
                    couponService,
                    achievementService
            );
            app.start();
        }
    }
