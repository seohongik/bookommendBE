package com.project.bookommendbe.main;

import com.project.bookommendbe.db.*;
import com.project.bookommendbe.dto.TimelineVO;
import com.project.bookommendbe.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class MainController {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReadingRecordRepository readingRecordRepository;
    private final UserBookRepository userBookRepository;

    @Autowired
    public MainController(UserRepository userRepository, ReviewRepository reviewRepository, ReadingRecordRepository readingRecordRepository, UserBookRepository userBookRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.readingRecordRepository = readingRecordRepository;
        this.userBookRepository = userBookRepository;
    }

    @GetMapping("/r1/timeline")
    public List<TimelineVO> timeline(@RequestParam Map<String, String> pramMap) {

        System.out.println(pramMap);
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

}
