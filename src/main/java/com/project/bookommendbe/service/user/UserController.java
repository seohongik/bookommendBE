package com.project.bookommendbe.service.user;

import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.service.TimeLineService;
import com.project.bookommendbe.util.AuthNumber;
import com.project.bookommendbe.util.EmailConfig;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity userCreate( @Valid @RequestBody UserVO userVO, BindingResult bindingResult) throws NoSuchAlgorithmException, UserException {


        UserException userCreate = userService.create(userVO);
        if(!bindingResult.hasErrors()) {
            StringBuilder sb = new StringBuilder();
            if(userCreate==null) {
                Map<String, String> map = new HashMap<>();
                map.put("msg", "회원가입 완료");
                return makeResponse(sb, map);
            }else {
                try {
                    throw userCreate;
                }catch (UserException e) {
                    return isError(bindingResult, new StringBuilder(e.getMessage()));
                }
            }
        }else {
            return isError(bindingResult, new StringBuilder("회원가입 실패"));
        }

    }


    @GetMapping("/r1/login")
    public ResponseEntity login(@Valid @ModelAttribute LoginVO loginVO, BindingResult bindingResult) throws NoSuchAlgorithmException {

        StringBuilder sb = new StringBuilder();

        if(bindingResult.hasErrors()) {
            return isError(bindingResult, sb);
        }

        UserVO userVO = new UserVO();
        userVO.setEmail(loginVO.getEmail());
        userVO.setPassword(loginVO.getPassword());
        Optional<User> user=userService.findUserByEmailAndPassword(userVO);

        Map<String,String> map=new HashMap<>();
        if(user.isPresent()) {
            map.put("id", String.valueOf(user.get().getId()));
            return makeResponse(sb,map);
        }

        return makeResponse(new StringBuilder("정보가 없습니다."), new HashMap<>());

    }

    @GetMapping("/r1/isUserId")
    public ResponseEntity isUserId(@Valid @ModelAttribute UserFindIdVO userFindVO, BindingResult bindingResult) throws NoSuchAlgorithmException, jakarta.mail.MessagingException {

        StringBuilder sb = new StringBuilder();

        if(bindingResult.hasErrors()) {
            return isError(bindingResult, sb);
        }

        UserVO userVO = new UserVO();
        userVO.setSignUpId(userFindVO.getSignUpId());
        userVO.setPhoneNumber(String.valueOf(userFindVO.getPhoneNumber()));

        Optional<User> user=userService.findUserBySignUpIdAndPhoneNumber(userVO);
        if(user.isPresent()) {
            Map<String, String> map=new HashMap<>();
            map.put("email", user.get().getEmail());
            return makeResponse(new StringBuilder(), map);
        }else {
            return makeResponse(new StringBuilder("정보가 없습니다."), new HashMap<>());
        }

    }

    @GetMapping("/r1/isUserPw")
    public ResponseEntity isUserPw(@Valid @ModelAttribute UserFindPwVO userFindVO, BindingResult bindingResult) throws NoSuchAlgorithmException, jakarta.mail.MessagingException {

        StringBuilder sb = new StringBuilder();

        if(bindingResult.hasErrors()) {
            return isError(bindingResult, sb);
        }

        UserVO userVO = new UserVO();
        userVO.setEmail(userFindVO.getEmail());
        userVO.setPhoneNumber(String.valueOf(userFindVO.getPhoneNumber()));
        Optional<User> user=userService.findUserByEmailAndPhoneNumber(userVO);


        if(user.isPresent()) {
            //StringBuilder stringBuilder = new StringBuilder();
            int authNumber = new AuthNumber().getNumber();
            Map<String, String> map = new HashMap<>();
            map.put("flag", "true");
            map.put("message", "이메일을 확인해주세요");
            userService.changePasswordEmptyAndMakeAuthNumber(userVO, authNumber);
            emailConfig.sendEmail(user.get(),  authNumber);
            return makeResponse(sb, map);
        }else {
            return makeResponse(new StringBuilder("정보가 없습니다"), new HashMap<>());
        }

    }

    @GetMapping("/r1/verify")
    public ResponseEntity verifyEmail(@RequestParam("authNumber") int authNumber,@Valid @ModelAttribute UserFindPwVO userFindVO ) throws NoSuchAlgorithmException {
        UserVO userVO = new UserVO();
        userVO.setPhoneNumber(userFindVO.getPhoneNumber());
        userVO.setEmail(userFindVO.getEmail());
        userVO.setAuthNumber(authNumber);

        StringBuilder sb = new StringBuilder();
        boolean verified=userService.verify(userVO);
        if(verified) {
            Map<String, String> map = new HashMap<>();
            map.put("authNumber", String.valueOf(authNumber));
            return makeResponse(sb, map);
        }else {
            return makeResponse(new StringBuilder("정보가 없습니다"), new HashMap<>());
        }
    }


    @PutMapping("/u1/password/verified")
    public void updatePasswordUser(@RequestBody UserVO userVO) throws NoSuchAlgorithmException {
        log.error(userVO.toString());
        userService.updatePassword(userVO);
    }

    private ResponseEntity makeResponse( StringBuilder message, Map<String,String> value) {


        if(!value.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(value);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
    }

    private ResponseEntity isError(BindingResult bindingResult, StringBuilder sb) {

        bindingResult.getAllErrors().forEach(objectError -> {
            String msg = objectError.getDefaultMessage();
            sb.append(msg).append("\n").append("\n");
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());

    }

}
