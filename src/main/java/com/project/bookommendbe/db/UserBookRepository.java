package com.project.bookommendbe.db;


import com.project.bookommendbe.account.User;
import com.project.bookommendbe.account.UserBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserBookRepository extends JpaRepository<UserBook, Long> {

    UserBook findByUser(User user);

}
