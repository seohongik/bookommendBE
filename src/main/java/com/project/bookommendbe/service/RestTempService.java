package com.project.bookommendbe.service;

import com.project.bookommendbe.dto.BookVO;
import com.project.bookommendbe.dto.api.naver.Channel;
import com.project.bookommendbe.dto.api.naver.Item;
import com.project.bookommendbe.entity.Book;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class RestTempService<T> {


    public  ResponseEntity<T> response(String url, String path ,  Map<String, String> paramMap, Map<String,String> headerItem, HttpMethod httpMethod , Class<T> tClass) throws MalformedURLException {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<T> requestEntity = new HttpEntity<>(makeHeader(headerItem));
        return  restTemplate.exchange(makeUri(url,path, paramMap), httpMethod, requestEntity, tClass);
    }

    private String makeUri(String url,String path, Map<String, String> paramMap ) throws MalformedURLException {
        URL newUri = new URL(url);
        String uriString = "";
        for (String key : paramMap.keySet()) {
            uriString =UriComponentsBuilder.newInstance()
                    .scheme(newUri.getProtocol())
                    .host(newUri.getHost())
                    .path(path)
                    .queryParam(key, paramMap.get(key))
                    .queryParam(key, paramMap.get(key))
                    .queryParam(key, paramMap.get(key))
                    .build().toString();

        }
        return uriString;

    }

    private HttpHeaders makeHeader(Map<String,String> headerItem) throws MalformedURLException {

        HttpHeaders headers = new HttpHeaders();
        for (String key : headerItem.keySet()) {
            headers.add(key, headerItem.get(key));
        }
        return headers;
    }

}


