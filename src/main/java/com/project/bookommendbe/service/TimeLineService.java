package com.project.bookommendbe.service;

import com.project.bookommendbe.dto.TimelineVO;
import com.project.bookommendbe.entity.*;
import com.project.bookommendbe.service.recordAndReview.record.RecordService;
import com.project.bookommendbe.service.recordAndReview.review.ReviewService;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.userbook.UserBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TimeLineService {

    private final UserService userService;
    private final UserBookService userBookService;
    private final ReviewService reviewService;
    private final RecordService recordService;


    public List<TimelineVO>  makeTimeLine(Map<String, String > pramMap) {

        List<TimelineVO> timelines = new ArrayList<>();
        long userId = Long.parseLong(pramMap.get("userId"));
        String date = pramMap.get("date");

        Optional<User> user = userService.getUserByIdOpen(userId);
        List<ReadingRecord> readingRecords =recordService.findRecordByUserAndDateOpen(user.get(),date);
        List<Review> reviews =reviewService.findReviewsByUserAndReviewDateOpen(user, date);

        for (ReadingRecord readingRecord : readingRecords) {

            Optional<UserBook> userBookOptional = userBookService.getUserBookListTimeLineOpen(user.get(), readingRecord.getBookIsbn());

            UserBook userBook = userBookOptional.get();
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
        return timelines;
    }
}
