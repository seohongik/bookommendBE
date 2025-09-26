package com.project.bookommendbe.service.book;

import com.project.bookommendbe.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BookServiceSuper {
    protected final BookRepository bookRepository;
    public BookServiceSuper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }
    public abstract Optional<Book> findBookByBookIsbnOpen(String isbn);
}
