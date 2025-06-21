package com.project.bookommendbe.main;

import com.project.bookommendbe.db.*;
import com.project.bookommendbe.dto.*;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.service.MailService;
import com.project.bookommendbe.util.EmailConfig;
import jakarta.validation.Valid;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@RestController
@Slf4j
public class MainController {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingRecordRepository readingRecordRepository;
    private final UserBookRepository userBookRepository;
    private final EmailConfig emailConfig;

    @Autowired
    public MainController(UserRepository userRepository, ReviewRepository reviewRepository, ReadingRecordRepository readingRecordRepository, UserBookRepository userBookRepository, EmailConfig emailConfig) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.readingRecordRepository = readingRecordRepository;
        this.userBookRepository = userBookRepository;
        this.emailConfig = emailConfig;
    }

    @GetMapping("/r1/timeline")
    public List<TimelineVO> timeline(@RequestParam Map<String, String> pramMap) {

        long userId = Long.parseLong(pramMap.get("userId"));
        String date = pramMap.get("date");

        User user = userRepository.findUserById(userId);
        List<ReadingRecord> readingRecords =readingRecordRepository.findReadingRecordsByUserAndDate(user,date);
        List<Review> reviews =reviewRepository.findReviewsByUserAndReviewDate(user, date);

        List<TimelineVO> timelines = new ArrayList<>();

        for (ReadingRecord readingRecord : readingRecords) {

            UserBook userBook=userBookRepository.findUserBookByUserIdAndBookIsbn(userId,readingRecord.getBookIsbn());

            if(userBook.getBook().getBookIsbn().equals(readingRecord.getBookIsbn())) {

                TimelineVO timelineVO = new TimelineVO();

                timelineVO.setTitle(userBook.getBook().getTitle());
                timelineVO.setAuthor(userBook.getBook().getAuthor());
                timelineVO.setDate(readingRecord.getDate());
                timelineVO.setOpinion(new String(Character.toChars(0x1F913)) + readingRecord.getOpinion());
                timelineVO.setComment(new String(Character.toChars(0x1F914)) + readingRecord.getComment());
                timelineVO.setPercent(readingRecord.getPercent() + new String(Character.toChars(0x1F4C8)));
                timelineVO.setBookIsbn(readingRecord.getBookIsbn());
                timelineVO.setBetweenPage(readingRecord.getBetweenPage());
                timelineVO.setStatus(ReadingStatus.valueOf(readingRecord.getStatus()));
                timelineVO.setFromPage(readingRecord.getFromPage() + "\uD83D\uDCD6");
                timelineVO.setToPage(readingRecord.getToPage() + "\uD83D\uDCD6");
                timelineVO.setReadAmountCount(readingRecord.getReadAmountCount());
                timelineVO.setTime(readingRecord.getTime());
                timelines.add(timelineVO);
            }
        }

        for (TimelineVO timelineVO : timelines) {

            for (Review review : reviews) {

                if (review.getBook().getBookIsbn().equals(timelineVO.getBookIsbn())) {

                    timelineVO.setCreatedAt(review.getCreatedAt());
                    if (review.getRating().equals(RatingEnum.ONE)) {
                        timelineVO.setRating("나의 별 " + "\u2B50");
                    } else if (review.getRating().equals(RatingEnum.TWO)) {
                        timelineVO.setRating("나의 별 " + "\u2B50" + "\u2B50");
                    } else if (review.getRating().equals(RatingEnum.THREE)) {
                        timelineVO.setRating("나의 별 " + "\u2B50" + "\u2B50" + "\u2B50");
                    } else if (review.getRating().equals(RatingEnum.FOUR)) {
                        timelineVO.setRating("나의 별 " + "\u2B50" + "\u2B50" + "\u2B50" + "\u2B50");
                    } else if (review.getRating().equals(RatingEnum.FIVE)) {
                        timelineVO.setRating("나의 별 " + "\u2B50" + "\u2B50" + "\u2B50" + "\u2B50" + "\u2B50");
                    }

                }
            }
        }
        log.info("timeline :::: {}",timelines);
        return timelines;
    }

    @PostMapping("/c1/user")
    public ResponseEntity user( @Valid @RequestBody UserVO userVO, BindingResult bindingResult) throws NoSuchAlgorithmException {

        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String msg = objectError.getDefaultMessage();
                System.out.println("field : "+field.getField());
                System.out.println(msg);

                sb.append("field  :" +field.getField());
                sb.append("\n");
                sb.append("message :"+msg);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

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

        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping("/u1/user")
    public ResponseEntity user(@Valid @RequestBody UserUpdateVO userUpdateVO, BindingResult bindingResult) throws NoSuchAlgorithmException {


        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String msg = objectError.getDefaultMessage();
                System.out.println("field : "+field.getField());
                System.out.println(msg);

                sb.append("field  :" +field.getField());
                sb.append("\n");
                sb.append("message :"+msg);
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        Optional<User> user=userRepository.findUserByEmailAndPhoneNumber(userUpdateVO.getEmail(), encodingInformation(userUpdateVO.getPhoneNumber()));

        if(user.isPresent()){
            user.get().setPassword(encodingInformation(userUpdateVO.getPassword()));
            user.get().setConfirmPassword(encodingInformation(userUpdateVO.getConfirmPassword()));
            userRepository.save(user.get());
            return ResponseEntity.status(HttpStatus.CREATED).body("변경 성공");
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보를 찾을 수 없음 관리자에게 연락주세요");
        }


    }

    @GetMapping("/r1/login")
    public ResponseEntity login(@Valid @ModelAttribute LoginVO loginVO, BindingResult bindingResult) throws NoSuchAlgorithmException {

        log.info("login :::: {}",loginVO);
        StringBuilder sb = new StringBuilder();
        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String msg = objectError.getDefaultMessage();
                System.out.println("field : "+field.getField());
                System.out.println(msg);

                sb.append(field.getField()+"\n"+msg+"\n" );
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        Optional<User> user=userRepository.findUserByEmailAndPassword(loginVO.getEmail(),encodingInformation(loginVO.getPassword()));

        if(user.isPresent()){
            Map<String,Long> map=new HashMap<>();
            map.put("id", user.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }else {
            sb.append("user not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb);
        }

    }

    @GetMapping("/r1/isUser")
    public ResponseEntity passwordOrID(@ModelAttribute UserFindVO userFindVO, BindingResult bindingResult) throws NoSuchAlgorithmException, jakarta.mail.MessagingException {

        log.info("userFindVO :::: {}",userFindVO);
        StringBuilder sb = new StringBuilder();
        if(bindingResult.hasErrors()){

            bindingResult.getAllErrors().forEach(objectError -> {
                FieldError field = (FieldError) objectError;
                String msg = objectError.getDefaultMessage();
                System.out.println("field : "+field.getField());
                System.out.println(msg);

                sb.append(field.getField()+"\n"+msg+"\n" );
            });

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
        }

        if(userFindVO.getItem().equals("1")) {

            Optional<User> user=userRepository.findUserBySignUpIdAndPhoneNumber(userFindVO.getSignUpId(),encodingInformation(userFindVO.getPhoneNumber()));

            if(user.isPresent()) {
                userFindVO.setEmail(user.get().getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(userFindVO);
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보를 찾지 못했습니다.");
            }

        }else if(userFindVO.getItem().equals("2")) {
            Optional<User> user=userRepository.findUserByEmailAndPhoneNumber(userFindVO.getEmail(),encodingInformation(userFindVO.getPhoneNumber()));

            if(user.isPresent()&& user.get().getPassword().equals("")){

                Map<String,Object > map=new HashMap<>();
                map.put("message","비밀번호를 다시 설정 해주세요");
                map.put("flag",true);
                return ResponseEntity.status(HttpStatus.OK).body(map);
            }
            if(user.isPresent()) {
                sendEmail(user.get());
                return ResponseEntity.status(HttpStatus.OK).body("이메일을 확인해주세요");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("정보를 찾지 못했습니다.");
            }
        }else {
            sb.append("입력값을 다시 확인 해 주세요 ");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb);
        }

    }


    private void sendEmail(User user) throws jakarta.mail.MessagingException {
        String uuid = UUID.randomUUID().toString();
        user.setUuid (UUID.fromString(uuid));
        userRepository.save(user);
        String text = "<html>" +
                "<body>" +
                "<h3>이메일 인증을 위해 아래 링크를 클릭해주세요.</h3>" +
                "<a href='http://localhost:8080/verify?uuid="+user.getUuid()+ "&id="+user.getId()+"'>이메일 인증하기</a>" +
                "</body>" +
                "</html>";
        emailConfig.javaMailSender(user.getEmail(), "비밀번호를 재설정 하기위한 링크를 보냅니다.", text);
        //mailService.sendEmail(user.getEmail(),"비밀번호를 재설정 하기위한 링크를 보냅니다.",emailContent);
    }



    private String  encodingInformation(String information) throws  NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(information.getBytes());
        return DatatypeConverter.printHexBinary(md.digest());


    }

}
