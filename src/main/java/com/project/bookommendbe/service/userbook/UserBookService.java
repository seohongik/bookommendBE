package com.project.bookommendbe.service.userbook;


import com.project.bookommendbe.db.UserBookRepository;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.CommonService;

import java.util.List;
import java.util.Optional;

public abstract class UserBookService implements CommonService<UserBook> {


    public abstract Optional<UserBook> find(String methodName , Object object , String bookIsbn, Long ... ids);

    public abstract List<UserBook> findAll(String methodName, Object object, String bookIsbn, Long... ids);
}
