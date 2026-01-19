package service;

import models.Book;
import models.Reservation;
import models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Reservation> reservations = new ArrayList<>();
    private Long reservationIdCounter = 1L;

    public Reservation reserveBook(User user, Book book, int days) {
        if (book.getStock() < 1) throw new RuntimeException("The book is not available for booking");

        Reservation res = new Reservation();
        res.setId(reservationIdCounter++);
        res.setUser(user);
        res.setBook(book);
        res.setReservedUntil(LocalDate.now().plusDays(days));
        res.setStatus("RESERVED");

        book.setStock(book.getStock() - 1);

        reservations.add(res);
        return res;
    }

    public List<Reservation> getReservationsByUser(User user) {
        List<Reservation> result = new ArrayList<>();
        for (Reservation r : reservations) {
            if (r.getUser().equals(user)) result.add(r);
        }
        return result;
    }
}
