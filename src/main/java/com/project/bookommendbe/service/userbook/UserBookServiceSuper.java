package com.project.bookommendbe.service.userbook;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;

import java.util.List;
import java.util.Optional;

public abstract class UserBookServiceSuper {

    protected UserBookRepository userBookRepository;
    public UserBookServiceSuper(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }
    List<UserBook> findUserBooksByUser(User user){
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
}
