package com.project.bookommendbe.service.user;

import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.entity.User;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUserById(long id) {
       // return userDAO.find(UserEnum.FIND_USER_BY_ID, null,null,null,id);
        return userRepository.findUserById(id);
    }

    public void create(UserVO userVO) throws NoSuchAlgorithmException {
        User user = new User();
        user.setUsername(userVO.getUsername());
        user.setPassword(encodingInformation(userVO.getPassword()));
        user.setEmail(userVO.getEmail());
        user.setGender(userVO.getGender());
        user.setDateOfBirth(userVO.getDateOfBirth());
        user.setConfirmPassword(encodingInformation(userVO.getConfirmPassword()));
        user.setPhoneNumber(encodingInformation(String.valueOf(userVO.getPhoneNumber())));
        user.setSignUpId(userVO.getSignUpId());
        user.setPhoneNumberTypical(userVO.getPhoneNumber());

        userRepository.save(user);
    }


    public Optional<User> findUserByEmailAndPhoneNumber(UserVO userVO) throws NoSuchAlgorithmException {

        String[] param = {userVO.getEmail(), encodingInformation(userVO.getPhoneNumber())};
        Optional<User> user=userRepository.findUserByEmailAndPhoneNumber(param[0], param[1]);
        return user;

    }

    public Optional<User> findUserByEmailAndPassword(UserVO loginVO) throws NoSuchAlgorithmException {
        String[] param = {loginVO.getEmail(), encodingInformation(loginVO.getPassword())};
        //Optional<User> user=userDAO.find(UserEnum.FIND_USER_BY_EMAIL_AND_PASSWORD,null, null, param, null);
        Optional<User> user=userRepository.findUserByEmailAndPassword(param[0], param[1]);
        if(user.isPresent()){
            return user;
        }else {
            return Optional.empty();
        }
    }

    public Optional<User> findUserBySignUpIdAndPhoneNumber(UserVO userFindVO) throws NoSuchAlgorithmException {
        String[] param = {userFindVO.getSignUpId(),encodingInformation(userFindVO.getPhoneNumber())};
        Optional<User> user=userRepository.findUserBySignUpIdAndPhoneNumber(param[0], param[1]);

        if(user.isPresent()){
            return user;
        }else {
            return Optional.empty();
        }
    }


    // 처리할 서비스 로직 [S]
    private String  encodingInformation(String information) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(information.getBytes());
        return DatatypeConverter.printHexBinary(md.digest());
    }

    public void updatePassword(UserVO uservo) throws NoSuchAlgorithmException {

        Optional<User> user = userRepository.findUserByEmailAndPhoneNumberAndPasswordAuthNumber(uservo.getEmail(), encodingInformation(uservo.getPhoneNumber()), uservo.getAuthNumber());
        if(user.isPresent()) {
            user.get().setPassword(encodingInformation(uservo.getPassword()));
            user.get().setConfirmPassword(encodingInformation(uservo.getConfirmPassword()));
            userRepository.save(user.get());
        }
    }


    public void changePasswordEmptyAndMakeAuthNumber(UserVO userVO,int authNumber) throws NoSuchAlgorithmException {
        Optional<User> user=userRepository.findUserByEmailAndPhoneNumber(userVO.getEmail(), encodingInformation(userVO.getPhoneNumber()));
        if(user.isPresent()) {
            user.get().setPassword("");
            user.get().setConfirmPassword("");
            user.get().setPasswordAuthNumber(authNumber);
            userRepository.save(user.get());
        }
    }

    public boolean verify(UserVO userVO) throws NoSuchAlgorithmException {

        Optional<User> user=userRepository.findUserByEmailAndPhoneNumberAndPasswordAuthNumber(userVO.getEmail(), encodingInformation(userVO.getPhoneNumber()), userVO.getAuthNumber());
        return user.isPresent();
    }
}



