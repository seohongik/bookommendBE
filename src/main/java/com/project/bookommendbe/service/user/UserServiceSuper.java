package com.project.bookommendbe.service.user;

import com.project.bookommendbe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public abstract class UserServiceSuper {

    protected final UserRepository userRepository;

    public UserServiceSuper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Optional<User> findUserByEmailAndPassword( String email, String password){
        return userRepository.findUserByEmailAndPassword(email, password);
    };

    Optional<User> findUserBySignUpIdAndPhoneNumber( String signUpId, String phoneNumber){
        return userRepository.findUserBySignUpIdAndPhoneNumber(signUpId, phoneNumber);
    };

    Optional<User> findUserByEmailAndPhoneNumber( String email, String phoneNumber){
        return userRepository.findUserByEmailAndPhoneNumber(email, phoneNumber);
    };

    Optional<User> findUserByEmailAndPhoneNumberAndPasswordAuthNumber(String email, String phoneNumber, int authNumber){
        return userRepository.findUserByEmailAndPhoneNumberAndPasswordAuthNumber(email, phoneNumber, authNumber);
    };

    public abstract Optional<User> getUserByIdOpen(Long id);


}
