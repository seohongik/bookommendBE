package com.project.bookommendbe.db;

import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.entity.User;
import jakarta.validation.constraints.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(long id);
    User findUserBySignUpIdAndPassword(String signUpId, String password);

    Optional<User> findUserByEmailAndPassword(@Email(message = "이메일을 입력해주세요") String email, String s);
}
