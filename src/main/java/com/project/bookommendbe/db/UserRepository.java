package com.project.bookommendbe.db;

import com.project.bookommendbe.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(long id);

    Optional<User> findUserByEmailAndPassword(@Email(message = "이메일을 입력해주세요") String email, String password);

    Optional<User> findUserBySignUpIdAndPhoneNumber(@NotNull(message="아이디를 입력해주세요") String signUpId, String phoneNumber);

    Optional<User> findUserByEmailAndPhoneNumber(@Email(message = "이메일을 입력해주세요") String email, String phoneNumber);

    Optional<User> findUserByPhoneNumberAndPasswordAuthNumber(String s, int authNumber);
}
