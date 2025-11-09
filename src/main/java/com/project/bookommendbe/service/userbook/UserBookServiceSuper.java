package com.project.bookommendbe.service.userbook;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class UserBookServiceSuper {

    protected UserBookRepository userBookRepository;
    public UserBookServiceSuper(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }
    List<UserBook> findUserBooksByUser(User user){
        log.info("findUserBooksByUser:{}",userBookRepository.findUserBooksByUser(user));
        return userBookRepository.findUserBooksByUser(user);
    };

    Optional<UserBook> findUserBookByIdAndUser(Long userBookId, User user){
        return userBookRepository.findUserBookByIdAndUser(userBookId, user);
    };
    Optional<UserBook> findUserBookByUserAndBookIsbn(User user, String bookIsbn){
        return userBookRepository.findUserBookByUserAndBookIsbn(user, bookIsbn);
    };
    boolean existsUserBookByUserAndBookIsbn(User userId, String bookIsbn){
        return userBookRepository.existsUserBookByUserAndBookIsbn(userId, bookIsbn);
    };
    public abstract List<UserBook> getUserBooksByUserOpen(Optional<User>  user);
    public abstract List<UserBook> findAllOpen();
    public abstract  Optional<UserBook> saveReadBookPageCountAndStatusOpen(User user, RecordAndReviewSaveVO saveRequest);
    public abstract void saveCategoryBookOpen(Book book);

    public UserBook getUserBooksByIdAndUserIdOpen(Long id, Long userId) {
        return userBookRepository.findUserBookByIdAndUserId(id, userId);
    }
}
