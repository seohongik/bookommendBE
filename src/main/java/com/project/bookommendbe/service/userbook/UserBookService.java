package com.project.bookommendbe.service.userbook;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserBookService {

    private final UserBookRepository userBookRepository;

    UserBookService(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }


    // 처리할 서비스 로직 [S]
    public List<UserBook> findUserBooksByUser(User user) {
        return userBookRepository.findUserBooksByUser(user);
    }

    public void updateMyBookPageCount(User user, Long userBookId, Map<String, String> request) {

        //Optional<UserBook>  userBook = userBookDAO.find(UserBookEnum.FIND_USER_BOOK_BY_ID_AND_USER, user , null, null,  userBookId );
        Optional<UserBook> userBook = userBookRepository.findUserBookByIdAndUser(userBookId, user);

        if(userBook.isPresent()) {
            int pageCount = Integer.parseInt(request.get("pageCount"));
            int fromPage = Integer.parseInt(request.get("fromPage"));
            userBook.get().setPageCount(pageCount);
            userBook.get().setFromPage(fromPage);
            userBookRepository.save(userBook.get());
        }
    }


    public Optional<UserBook> saveReadBookPageCountAndStatus(User user, RecordAndReviewSaveVO saveRequest) {
       // Optional<UserBook> userBook = userBookDAO.find(UserBookEnum.FIND_USER_BOOK_BY_ID_AND_USER, user , null, null, saveRequest.getUserBookId());
        Optional<UserBook> userBook = userBookRepository.findUserBookByIdAndUser(saveRequest.getUserBookId(), user);
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

    public boolean existsUserBookByUserAndBookIsbn(User user, String bookIsbn) {
        return userBookRepository.existsUserBookByUserAndBookIsbn(user, bookIsbn);
    }


    public void saveMyBook(boolean isOwnBook, Optional<Book> book, Optional<User> user) {
        if(!isOwnBook) {
            UserBook userBook = new UserBook();
            userBook.setBook(book.get());
            userBook.setUser(user.get());
            userBook.setBookIsbn(book.get().getBookIsbn());
            userBookRepository.save(userBook);
        }
    }

    public Optional<UserBook> getUserBookListTimeLine(User user, String bookIsbn) {
        return userBookRepository.findUserBookByUserAndBookIsbn(user, bookIsbn);

    }
}

