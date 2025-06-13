package com.project.bookommendbe.db;


import com.project.bookommendbe.account.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {


    Optional<Book> findBookByBookIsbn(String isbn);

    List<Book> findBooksBySplitTitleContaining(String ownSplitTitle);

    List<Book> findBooksByTitleContaining(String title);
}
