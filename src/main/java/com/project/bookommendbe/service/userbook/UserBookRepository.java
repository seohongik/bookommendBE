package com.project.bookommendbe.service.userbook;


import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserBookRepository extends JpaRepository<UserBook, Long> {

    Optional<UserBook> findUserBookByUser(User user);

    List<UserBook> findUserBooksByUser(User user);

    Optional<UserBook> findUserBookByIdAndUser(Long userBookId, User user);

    Optional<UserBook> findUserBookByUserAndBookIsbn(User user, String bookIsbn);

    boolean existsUserBookByUserAndBookIsbn(User userId, String bookIsbn);

}
