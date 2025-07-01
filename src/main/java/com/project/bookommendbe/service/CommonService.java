package com.project.bookommendbe.service;

import java.util.List;
import java.util.Optional;

public interface CommonService<T>  {

    public Optional<T> find(String methodName , Object object , String bookIsbn, Long ... ids);

    public List<T> findAll(String methodName, Object object, String bookIsbn, Long... ids);

    public void save(String methodName, T t);

    public void update(String methodName , T t);

    public void delete(String methodName , T t);

}

