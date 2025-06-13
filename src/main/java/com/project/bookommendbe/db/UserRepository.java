package com.project.bookommendbe.db;

import com.project.bookommendbe.account.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findUserById(long id);
    User findUserBySignUpIdAndPassword(String signUpId, String password);

}
