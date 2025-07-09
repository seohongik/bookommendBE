package com.project.bookommendbe.service.record;

import com.project.bookommendbe.dto.RecordAndReviewSaveVO;
import com.project.bookommendbe.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService extends RecordServiceSuper{

    protected RecordRepository recordRepository;

    protected RecordService(RecordRepository recordRepository) {
        super(recordRepository);
    }


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
        return recordRepository.findReadingRecordsByUserAndDate(user, date);
    }

}



