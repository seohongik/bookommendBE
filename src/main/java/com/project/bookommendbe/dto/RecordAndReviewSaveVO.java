package com.project.bookommendbe.dto;


import lombok.Data;

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
public class RecordAndReviewSaveVO {

    private Long userId;
    private Long userBookId;
    private int rating;

    private RecordVO record;

}


