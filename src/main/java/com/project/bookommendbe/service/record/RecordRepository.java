package com.project.bookommendbe.service.record;


import com.project.bookommendbe.entity.ReadingRecord;
import com.project.bookommendbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface RecordRepository extends JpaRepository<ReadingRecord, Long> {

    List<ReadingRecord> findReadingRecordsByUserAndDate(User user, String date);
}
