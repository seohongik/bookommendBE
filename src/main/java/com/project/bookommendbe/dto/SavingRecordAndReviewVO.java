package com.project.bookommendbe.dto;


import com.project.bookommendbe.entity.RatingEnum;
import com.project.bookommendbe.entity.Review;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 'userId':1,
 'userBookId':1,
 'record':{
 'date':props.date,
 'fromPage': pageAmountCount,
 'toPage': allPage,
 'pagesRead':(allPage-pageAmountCount),
 'percent' : ((pageAmountCount / allPage) * 100),
 'opinion' :opinion,
 'comment' :comment,
 'time' : formatTime()
 },
 'rating':{
 'rating': rating
 }
 */

@Data
public class SavingRecordAndReviewVO {

    private Long userId;
    private Long userBookId;
    private int rating;

    private RecordVO record;

}


