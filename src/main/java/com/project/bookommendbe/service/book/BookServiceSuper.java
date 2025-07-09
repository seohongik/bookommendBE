package com.project.bookommendbe.service.book;

import com.project.bookommendbe.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public abstract class BookServiceSuper {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceSuper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    Optional<Book> findBookByBookIsbn(String isbn){
      return   bookRepository.findBookByBookIsbn(isbn);
    }

    List<Book> findBooksBySplitTitleContaining(String splitTitle){
      return   bookRepository.findBooksBySplitTitleContaining(splitTitle);
    }

    protected void save(Book saveBook) {
        bookRepository.save(saveBook);
    }

    public abstract Optional<Book> findBookByBookIsbnOpen(String isbn);


}
