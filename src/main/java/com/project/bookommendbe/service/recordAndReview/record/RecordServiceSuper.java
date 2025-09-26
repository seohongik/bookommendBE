package com.project.bookommendbe.service.recordAndReview.record;

import com.project.bookommendbe.entity.ReadingRecord;
import com.project.bookommendbe.entity.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class RecordServiceSuper {
    protected final RecordRepository recordRepository;
    public RecordServiceSuper(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }
    public abstract List<ReadingRecord> findRecordByUserAndDateOpen(User user, String date);
}
