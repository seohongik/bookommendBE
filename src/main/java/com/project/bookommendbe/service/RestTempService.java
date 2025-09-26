package com.project.bookommendbe.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestTempService{

    public  <T>ResponseEntity<T> response(String url,  String path ,  MultiValueMap<String, String> paramMap, Map<String,String> headerItem, HttpMethod httpMethod , Class<?> tClass) throws MalformedURLException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers=null;
        if(headerItem!=null) {
             headers= makeHeader(headerItem);
        }
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        return (ResponseEntity<T>) restTemplate.exchange(makeUri(url,path, paramMap), httpMethod, requestEntity, tClass);
    }

    String makeUri(String url,String path, MultiValueMap<String, String> params ) throws MalformedURLException {
        URL newUri = new URL(url);
        //for (String key : paramMap.keySet()) {
        String uriString =UriComponentsBuilder.newInstance()
                    .scheme(newUri.getProtocol())
                    .host(newUri.getHost())
                    .path(path)
                    .queryParams(params)
                    .build().toString();
       // }
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


