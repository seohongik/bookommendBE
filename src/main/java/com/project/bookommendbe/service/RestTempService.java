package com.project.bookommendbe.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Service
public class RestTempService{

    public  <T>ResponseEntity<T> response(String url,  String path ,  Map<String, String> paramMap, Map<String,String> headerItem, HttpMethod httpMethod , Class<?> tClass) throws MalformedURLException {

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Object> requestEntity = new HttpEntity<>(makeHeader(headerItem));
        return (ResponseEntity<T>) restTemplate.exchange(makeUri(url,path, paramMap), httpMethod, requestEntity, tClass);
    }

    String makeUri(String url,String path, Map<String, String> paramMap ) throws MalformedURLException {
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


    HttpHeaders makeHeader(Map<String,String> headerItem) throws MalformedURLException {

        HttpHeaders headers = new HttpHeaders();
        for (String key : headerItem.keySet()) {
            headers.add(key, headerItem.get(key));
        }
        return headers;
    }

}


