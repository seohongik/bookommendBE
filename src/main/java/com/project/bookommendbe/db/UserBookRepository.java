package com.project.bookommendbe.db;


import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    UserBook findByUser(User user);

    boolean existsByBook(Book book);

    List<UserBook> findUserBooksByUser(User user);

    List<UserBook> findUserBooksByUserId(long userId);

    Optional<UserBook> findUserBookByUserIdAndId(Long userId, Long userBookId);

    UserBook findUserBookByUserIdAndBookIsbn(long userId, String bookIsbn);
}
