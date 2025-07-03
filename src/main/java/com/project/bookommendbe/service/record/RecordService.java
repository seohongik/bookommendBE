package com.project.bookommendbe.service.record;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }


    // 처리할 서비스 로직 [S]

    public void saveMyRecord(Optional<UserBook> userBook, RecordAndReviewSaveVO saveRequest ) {

        ReadingRecord readingRecord = new ReadingRecord();
        readingRecord.setUserBook(userBook.get());
        readingRecord.setUser(userBook.get().getUser());
        readingRecord.setBookIsbn(saveRequest.getRecord().getBookIsbn());
        readingRecord.setToPage(saveRequest.getRecord().getToPage());
        readingRecord.setFromPage(saveRequest.getRecord().getFromPage());
        readingRecord.setDate(saveRequest.getRecord().getDate());
        readingRecord.setComment(saveRequest.getRecord().getComment());
        readingRecord.setOpinion(saveRequest.getRecord().getOpinion());
        readingRecord.setTime(saveRequest.getRecord().getTime());
        readingRecord.setPercent(saveRequest.getRecord().getPercent());
        readingRecord.setReadAmountCount(saveRequest.getRecord().getReadAmountCount());

        if(saveRequest.getRecord().getFromPage()==0) {
            readingRecord.setStatus(String.valueOf(ReadingStatus.TO_READ));
        }else  if(saveRequest.getRecord().getFromPage()>0) {
            readingRecord.setStatus(String.valueOf(ReadingStatus.READING));
        }
        if (saveRequest.getRecord().getFromPage()== saveRequest.getRecord().getToPage()) {
            readingRecord.setStatus(String.valueOf(ReadingStatus.COMPLETED));
        }
        recordRepository.save(readingRecord);
    }

    public List<ReadingRecord> findRecordByUserAndDate(User user, String date) {
        //return recordRepository.findAll(RecordEnum.FIND_READING_RECORDS_BY_USER_AND_DATE, user, date);
        return recordRepository.findReadingRecordsByUserAndDate(user, date);
    }

    /*
    @Override
    public List<String> findBookIsbn(List<ReadingRecord> readingRecords) {

        List<String> bookIsbns = new ArrayList<>();

        for (ReadingRecord readingRecord : readingRecords) {
            bookIsbns.add(readingRecord.getBookIsbn());
        }
        return bookIsbns;
    }*/


}



