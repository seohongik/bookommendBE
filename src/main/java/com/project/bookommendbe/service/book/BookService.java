package com.project.bookommendbe.service.book;

import com.project.bookommendbe.dto.BookVO;
import com.project.bookommendbe.dto.UserBookVO;
import com.project.bookommendbe.dto.api.library.Doc;
import com.project.bookommendbe.dto.api.naver.Item;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.BookCategory;
import com.project.bookommendbe.entity.UserBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository =bookRepository;
    }


    // 처리할 서비스 로직 [S]

    public List<BookVO> findSavedBook(List<BookVO> showableBooks, Map<String, String> paramMap) {

        //List<Book> books = bookDAO.findAll(BookEnum.FIND_BOOKS_BY_SPLIT_TITLE_CONTAINING, paramMap.get("query"));

        List<Book> books=bookRepository.findBooksBySplitTitleContaining(paramMap.get("query"));

        if (books != null && !books.isEmpty()) {
            makeDisplayBooks(showableBooks, books);
            return showableBooks;
        }

        return null;

    }


    public void saveApiNAVERBooks(List<Item> items) {

            for (Item item : items) {
                //Optional<Book> isOwnIsbnBook = b.find(BookEnum.FIND_BOOK_BY_BOOK_ISBN, null, item.getIsbn(), null, null);

                Optional<Book> isOwnIsbnBook = bookRepository.findBookByBookIsbn(item.getIsbn());

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

    public Optional<Book> saveApiCategoryBooks(List<Doc> docs, String bookIsbn) {

        Optional<Book> book = bookRepository.findBookByBookIsbn(bookIsbn);
        for (Doc doc : docs) {
            book.get().setBookCategory(BookCategory.fromCode(doc.getSubject()));
            bookRepository.save(book.get());
        }
        return Optional.of(book.get());
    }

    private void  makeDisplayBooks(List<BookVO> showableBook, List<Book> apiBooks) {
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

    public List<UserBookVO> getReadingUserBookList(List<UserBook> userBooks) {

        List<UserBookVO> userBookReads = new ArrayList<>();
        for (UserBook userBook : userBooks) {

           // Optional<Book> book = bookDAO.find(BookEnum.FIND_BOOK_BY_BOOK_ISBN,null, userBook.getBookIsbn(), null,null);

            Optional<Book> book = bookRepository.findBookByBookIsbn(userBook.getBookIsbn());

            if(book.isPresent()) {
                UserBookVO userBookReadVO = new UserBookVO();
                userBookReadVO.setBookIsbn(userBook.getBookIsbn());
                userBookReadVO.setTitle(book.get().getTitle());
                userBookReadVO.setAuthor(book.get().getAuthor());
                userBookReadVO.setPublisher(book.get().getPublisher());
                userBookReadVO.setCoverImageUrl(book.get().getCoverImageUrl());
                userBookReadVO.setPublishedDate(book.get().getPublishedDate());
                userBookReadVO.setDescription(book.get().getDescription());
                userBookReadVO.setBookId(book.get().getId());
                userBookReadVO.setUserBookId(userBook.getId());
                userBookReadVO.setUserId(userBook.getUser().getId());
                userBookReadVO.setPageCount(userBook.getPageCount());
                userBookReadVO.setFromPage(userBook.getFromPage());

                userBookReads.add(userBookReadVO);

            }
        }
        return userBookReads;
    }

    // 처리할 서비스 로직 [E]

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



