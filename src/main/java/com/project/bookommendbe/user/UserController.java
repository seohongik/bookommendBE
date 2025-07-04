package com.project.bookommendbe.user;

import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.service.TimeLineService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.util.EmailConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {


    private final TimeLineService timeLineService;
    private final UserService userService;
    private final EmailConfig emailConfig;


    @GetMapping("/r1/timeline")
    public List<TimelineVO> timeline(@RequestParam Map<String, String> pramMap) {
        return  timeLineService.makeTimeLine(pramMap);
    }

    @PostMapping("/c1/user")
    public ResponseEntity userCreate( @Valid @RequestBody UserVO userVO, BindingResult bindingResult) throws NoSuchAlgorithmException {

        StringBuilder sb = new StringBuilder();
        isError(bindingResult, sb);


        Optional<User> user = userService.create(userVO);
        return makeResponse(user,  new StringBuilder("정보를 찾을 수 없음 관리자에게 연락주세요"),null);
    }

    @PutMapping("/u1/user")
    public ResponseEntity user(@Valid @RequestBody UserUpdateVO updateVO, BindingResult bindingResult) throws NoSuchAlgorithmException {
        StringBuilder sb = new StringBuilder();
        isError(bindingResult, sb);


        UserVO userVO = new UserVO();
        userVO.setEmail(updateVO.getEmail());
        userVO.setPhoneNumber(updateVO.getPhoneNumber());

        Optional<User> user=userService.findUserByEmailAndPhoneNumber(userVO);
        return makeResponse(user,  new StringBuilder("정보를 찾을 수 없음 관리자에게 연락주세요"),null);

    }

    @GetMapping("/r1/login")
    public ResponseEntity login(@Valid @ModelAttribute LoginVO loginVO, BindingResult bindingResult) throws NoSuchAlgorithmException {

        StringBuilder sb = new StringBuilder();
        isError(bindingResult, sb);


        UserVO userVO = new UserVO();
        userVO.setEmail(loginVO.getEmail());
        userVO.setPassword(loginVO.getPassword());
        Optional<User> user=userService.findUserByEmailAndPassword(userVO);

        Map<String,String> map=new HashMap<>();
        map.put("id", String.valueOf(user.get().getId()));

        return makeResponse(user,new StringBuilder("user not found"),map);

    }

    @GetMapping("/r1/isUser")
    public ResponseEntity passwordOrID(@ModelAttribute UserFindVO userFindVO, BindingResult bindingResult) throws NoSuchAlgorithmException, jakarta.mail.MessagingException {

        if(userFindVO.getItem().equals("1")) {
            UserVO userVO = new UserVO();
            userVO.setSignUpId(userFindVO.getSignUpId());
            userVO.setPhoneNumber(String.valueOf(userFindVO.getPhoneNumber()));

            Optional<User> user=userService.findUserBySignUpIdAndPhoneNumber(userVO);
            Map<String,String> map=new HashMap<>();
            map.put("email", user.get().getEmail());

            return makeResponse(user,new StringBuilder("정보를 찾지 못했습니다."), map);

        }else if(userFindVO.getItem().equals("2")){
            UserVO userVO = new UserVO();
            userVO.setEmail(userFindVO.getEmail());
            userVO.setPhoneNumber(String.valueOf(userFindVO.getPhoneNumber()));
            Optional<User> user=userService.findUserByEmailAndPhoneNumber(userVO);

            Map<String, String> map = new HashMap<>();
            map.put("flag", "true");
            map.put("message", "이메일을 확인해주세요");
            if(user.isPresent()) {
                emailConfig.sendEmail(user.get());
            }
            return makeResponse(user,new StringBuilder("user not found"), map);

        }

        return makeResponse(null, new StringBuilder("입력값을 다시 확인 해 주세요 "), null);

    }

    @GetMapping("/r1/verify")
    public ResponseEntity verifyEmail(@RequestParam("authNumber") int authNumber, @RequestParam String phoneNumber) throws NoSuchAlgorithmException {
        UserVO userVO = new UserVO();
        userVO.setAuthNumber(authNumber);
        userVO.setPhoneNumber(phoneNumber);
        Optional<User> user=userService.findUserByPhoneNumberAndPasswordAuthNumber(userVO);

        if(user.isPresent()) {
            user.get().setPassword("");
            user.get().setConfirmPassword("");
        }
        return makeResponse(user,  new StringBuilder("인증 실패"),null);
    }

    private ResponseEntity makeResponse(Optional<User> user, StringBuilder message, Map<String,String> value) {

        if(user.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(value);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    private void isError(BindingResult bindingResult, StringBuilder sb) {

        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String msg = objectError.getDefaultMessage();
                sb.append(field.getField()+"\n"+msg+"\n" );
            });
        }

    }

}
