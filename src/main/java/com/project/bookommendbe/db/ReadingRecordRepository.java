package com.project.bookommendbe.db;


import com.project.bookommendbe.account.ReadingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadingRecordRepository extends JpaRepository<ReadingRecord, Long> {

}
