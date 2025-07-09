package com.project.bookommendbe.service.book;

import com.project.bookommendbe.dto.BookVO;
import com.project.bookommendbe.dto.UserBookVO;
import com.project.bookommendbe.dto.api.library.Doc;
import com.project.bookommendbe.dto.api.naver.Item;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.BookCategory;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.userbook.UserBookService;
import com.project.bookommendbe.service.userbook.UserBookServiceSuper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Slf4j
@Service
public class BookService extends BookServiceSuper {

    private final BookRepository bookRepository;
    private final UserBookService userBookService;

    @Autowired
    public BookService(BookRepository bookRepository, UserBookService userBookService) {
        super(bookRepository);
        this.bookRepository = bookRepository;
        this.userBookService = userBookService;
    }

    @Override
    public Optional<Book> findBookByBookIsbnOpen(String isbn) {
        return bookRepository.findBookByBookIsbn(isbn);
    }


    // 처리할 서비스 로직 [S]
    List<BookVO> findSavedBook(List<BookVO> showableBooks, MultiValueMap<String, String> paramMap) {
        List<Book> books=super.findBooksBySplitTitleContaining(split(paramMap.get("query").toString()));
        if (books != null && !books.isEmpty()) {
            makeDisplayBooks(showableBooks, books);
        }
        return showableBooks;

    }

    void saveApiNAVERBooks(List<Item> items) {
        for (Item item : items) {
            Optional<Book> isOwnIsbnBook = super.findBookByBookIsbn(item.getIsbn());
            if (isOwnIsbnBook.isEmpty() || !isOwnIsbnBook.get().getBookIsbn().equals(item.getIsbn())) {
                Book saveBook = new Book();
                saveBook.setCoverImageUrl(item.getImage());
                saveBook.setAuthor(item.getAuthor());
                saveBook.setBookIsbn(item.getIsbn());
                saveBook.setTitle(item.getTitle());
                saveBook.setDescription(item.getDescription());
                saveBook.setPublisher(item.getPublisher());
                saveBook.setSplitTitle(split(item.getTitle()));
                saveBook.setPublishedDate(item.getPubdate());
                saveBook.setPublishedDate(item.getPubdate());
                saveBook.setDiscount(String.valueOf(item.getDiscount()));
                bookRepository.save(saveBook);
            }
        }
    }

    void saveApiCategoryBooks(List<Doc> docs, String bookIsbn) {

        Optional<Book> book=bookRepository.findBookByBookIsbn(bookIsbn);
        if(book.isPresent()) {
            for (Doc doc : docs) {
                book.get().setBookCategory(BookCategory.fromCode(doc.getSubject()));
                bookRepository.save(book.get());

            }
        }
    }

    /*
    Optional<Book> getBookByIsbn(String bookIsbn) {
        Optional<Book> book = bookRepository.findBookByBookIsbn(bookIsbn);
        return book;
    }*/

    void  makeDisplayBooks(List<BookVO> showableBook, List<Book> apiBooks) {
        for (Book item : apiBooks) {
            BookVO showBook = new BookVO();
            showBook.setId(item.getId());
            showBook.setCoverImageUrl(item.getCoverImageUrl());
            showBook.setAuthor(item.getAuthor());
            showBook.setBookIsbn(item.getBookIsbn());
            showBook.setTitle(item.getTitle());
            showBook.setDescription(item.getDescription());
            showBook.setPublisher(item.getPublisher());
            showBook.setPublishedDate(item.getPublishedDate());
            showableBook.add(showBook);
        }
    }

    private String split(String query) {
        query=query.replaceAll("[^가-힣]", "");

        String[] chosungs = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ" , "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String[] jungsungs = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
        String[] jongsungs = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String result = "";

        for (int i = 0; i < query.length(); i++) {
            int keywordUniBase = query.charAt(i) - 44032;
            char chosung = (char)(keywordUniBase / 28 / 21);
            char jungsung = (char)(keywordUniBase / 28 % 21);
            char jongsung = (char)(keywordUniBase % 28);
            result += chosungs[chosung] + jungsungs[jungsung] + jongsungs[jongsung];
        }
        return result;
    }

}



