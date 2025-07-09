package com.project.bookommendbe.service.book;


import com.project.bookommendbe.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface BookRepository extends JpaRepository<Book, Long> {


    Optional<Book> findBookByBookIsbn(String isbn);

    List<Book> findBooksBySplitTitleContaining(String splitTitle);


}
