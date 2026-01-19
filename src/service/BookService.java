package service;

import models.Book;
import java.util.ArrayList;
import java.util.List;
public class BookService {
    private List<Book> books = new ArrayList<>();
    private Long bookIdCounter = 1L;

    public Book addBook(Book book) {
        book.setId(bookIdCounter++);
        books.add(book);
        return book;
    }

    public List<Book> getAllBooks() { return books; }

    public Book findById(Long id) {
        for (Book b : books) {
            if (b.getId().equals(id)) return b;
        }
        return null;
    }
}
