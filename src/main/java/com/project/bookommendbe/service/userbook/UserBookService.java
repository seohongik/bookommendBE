package com.project.bookommendbe.service.userbook;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.dto.UserBookVO;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserBookService extends UserBookServiceSuper{


    protected UserBookRepository userBookRepository;

    @Autowired
    UserBookService(UserBookRepository userBookRepository) {
        super(userBookRepository);
        this.userBookRepository = userBookRepository;
    }

    @Override
    public List<UserBook> getUserBooksByUserOpen(Optional<User> user) {
        return userBookRepository.findUserBooksByUser(user.get());
    }

    @Override
    public List<UserBook> findAllOpen() {
        return userBookRepository.findAll();
    }

    void updateMyBookPageCount(User user, Long userBookId, Map<String, String> request) {

        Optional<UserBook> userBook = super.findUserBookByIdAndUser(userBookId, user);

        if(userBook.isPresent()) {
            int pageCount = Integer.parseInt(request.get("pageCount"));
            int fromPage = Integer.parseInt(request.get("fromPage"));
            userBook.get().setPageCount(pageCount);
            userBook.get().setFromPage(fromPage);
            userBookRepository.save(userBook.get());
        }
    }


    @Override
    public Optional<UserBook> saveReadBookPageCountAndStatusOpen(User user, RecordAndReviewSaveVO saveRequest) {
        Optional<UserBook> userBook = super.findUserBookByIdAndUser(saveRequest.getUserBookId(), user);
        userBook.get().setFromPage(saveRequest.getRecord().getFromPage());

        if(saveRequest.getRecord().getFromPage()==0) {
            userBook.get().setStartedAt(LocalDateTime.now().toString());
        }else  if(saveRequest.getRecord().getFromPage()>0) {
        }
        if (saveRequest.getRecord().getFromPage()== saveRequest.getRecord().getToPage()) {
            userBook.get().setFinishedAt(LocalDateTime.now().toString());
        }
        userBookRepository.save(userBook.get());
        return userBook;
    }

    @Override
    public void saveCategoryBookOpen(Book book) {
        System.out.println("UserBookService.saveCategoryBookOpen");
        userBookRepository.saveAll(book.getUserBooks());
    }


    void saveMyBook(Optional<Book> book, Optional<User> user) {
        boolean isOwnBook=super.existsUserBookByUserAndBookIsbn(user.get(), book.get().getBookIsbn());
        if(!isOwnBook) {
            UserBook userBook = new UserBook();
            userBook.setBook(book.get());
            userBook.setUser(user.get());
            userBook.setBookIsbn(book.get().getBookIsbn());
            userBookRepository.save(userBook);
        }
    }

    public Optional<UserBook> getUserBookListTimeLineOpen(User user, String bookIsbn) {
        return super.findUserBookByUserAndBookIsbn(user, bookIsbn);
    }


    List<UserBook> getUserBooksByUser(User user) {
        return super.findUserBooksByUser(user);
    }


    List<UserBookVO> getReadingUserBookList(List<UserBook> userBooks) {

        List<UserBookVO> userBookReadings= new ArrayList<>();
        for (UserBook userBook : userBooks) {

            Book book = userBook.getBook();

            UserBookVO userBookReadVO = new UserBookVO();
            userBookReadVO.setBookIsbn(userBook.getBookIsbn());
            userBookReadVO.setTitle(book.getTitle());
            userBookReadVO.setAuthor(book.getAuthor());
            userBookReadVO.setPublisher(book.getPublisher());
            userBookReadVO.setCoverImageUrl(book.getCoverImageUrl());
            userBookReadVO.setPublishedDate(book.getPublishedDate());
            userBookReadVO.setDescription(book.getDescription());
            userBookReadVO.setBookId(book.getId());
            userBookReadVO.setUserBookId(userBook.getId());
            userBookReadVO.setUserId(userBook.getUser().getId());
            userBookReadVO.setPageCount(userBook.getPageCount());
            userBookReadVO.setFromPage(userBook.getFromPage());
            userBookReadings.add(userBookReadVO);

        }
        return userBookReadings;
    }


}

