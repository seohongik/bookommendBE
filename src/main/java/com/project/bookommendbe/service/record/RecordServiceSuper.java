package com.project.bookommendbe.service.record;

import com.project.bookommendbe.entity.ReadingRecord;
import com.project.bookommendbe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class RecordServiceSuper {


    protected RecordRepository recordRepository;

    @Autowired
    protected RecordServiceSuper(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    List<ReadingRecord> findReadingRecordsByUserAndDate(User user, String date){
        return recordRepository.findReadingRecordsByUserAndDate(user, date);
    }
}
