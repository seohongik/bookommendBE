package com.project.bookommendbe.service.userbook;

import com.project.bookommendbe.db.UserBookRepository;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserBookServiceImpl extends UserBookService  {

    UserBookRepository userBookRepository;

    @Autowired
    public UserBookServiceImpl(UserBookRepository userBookRepository) {
        this.userBookRepository = userBookRepository;
    }

    @Override
    public Optional<UserBook> find(String methodName , Object object , String bookIsbn, Long ... ids) {

        if(UserBookServiceEnum.findUserBookByUser.getMethodName().equals(methodName) ) {
            if(object instanceof User user) {
                return userBookRepository.findUserBookByUser(user);
            }
        }else  if(UserBookServiceEnum.findUserBookByIdAndUser.getMethodName().equals(methodName) ) {

            if(object instanceof User user) {
                return userBookRepository.findUserBookByIdAndUser(ids[0], user);
            }
        }else if(UserBookServiceEnum.findUserBookByUserAndBookIsbn.getMethodName().equals(methodName) ) {
            if(object instanceof User user) {
                return userBookRepository.findUserBookByUserAndBookIsbn(user, bookIsbn);
            }
        }

        return null;
    }

    @Override
    public List<UserBook> findAll(String methodName , Object object , String bookIsbn, Long ... ids) {

        if(UserBookServiceEnum.findUserBooksByUser.getMethodName().equals(methodName) ) {
            if(object instanceof User user) {
                return userBookRepository.findUserBooksByUser(user);
            }
        }

        return List.of();
    }

    @Override
    public void save(String methodName, UserBook userBook) {
        userBookRepository.save(userBook);
    }

    @Override
    public void update(String methodName, UserBook userBook) {
        userBookRepository.save(userBook);
    }

    @Override
    public void delete(String methodName, UserBook userBook) {
        userBookRepository.delete(userBook);
    }


    // 처리할 서비스 로직 [S]








    // 처리할 서비스 로직 [E]




    private enum UserBookServiceEnum {

        findUserBookByUser("findUserBookByUser"),
        findUserBooksByUser("findUserBooksByUser"),
        findUserBooksByUserId("findUserBooksByUserId"),
        findUserBookByIdAndUser("findUserBookByIdAndUser"),
        findUserBookByUserAndBookIsbn("findUserBookByUserAndBookIsbn"),
        existsUserBookByUserAndBookIsbn("existsUserBookByUserAndBookIsbn");


        String methodName;
        public String getMethodName() {
            return methodName;
        }
        UserBookServiceEnum(String methodName) {
            this.methodName = methodName;
        }
    }
}
