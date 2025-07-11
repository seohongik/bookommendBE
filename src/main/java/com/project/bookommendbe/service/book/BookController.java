package com.project.bookommendbe.service.book;

import com.project.bookommendbe.dto.BookVO;
import com.project.bookommendbe.dto.UserBookSaveVO;
import com.project.bookommendbe.dto.api.library.Result;
import com.project.bookommendbe.dto.api.naver.Channel;
import com.project.bookommendbe.entity.Book;
import com.project.bookommendbe.service.RestTempService;
import com.project.bookommendbe.service.userbook.UserBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Slf4j
public class BookController {

    private final UserBookService userBookService;
    @Value("${naver.clientId}")
    private String clientId;

    @Value("${naver.secretId}")
    private String secretId;

    @Value("${library.key}")
    private String libraryKey;


    private final RestTempService restTempService;
    private final BookService bookService;


    @Autowired
    public BookController(RestTempService restTempService, BookService bookService, UserBookService userBookService) {
        this.restTempService = restTempService;
        this.bookService = bookService;
        this.userBookService = userBookService;
    }


    @GetMapping(value = "/r1/searchBookInfo" )
    @ResponseBody
    public List<BookVO> requestUserBook(@RequestParam MultiValueMap<String, String> paramMap) throws MalformedURLException {

        List<BookVO> showableBooks = new ArrayList<>();
        showableBooks=bookService.findSavedBook(showableBooks, paramMap);
        log.info("!!!!!!!!!!!!!!!!!!!!beforeAPi");
        if (showableBooks.isEmpty()) {

            String url = "https://openapi.naver.com";
            String path  ="/v1/search/book.json";

            Map<String,String> headersItem = new ConcurrentHashMap<>();
            headersItem.put("X-Naver-Client-Id", clientId);
            headersItem.put("X-Naver-Client-Secret", secretId);

            ResponseEntity<Channel> response = restTempService.response(url,path, paramMap, headersItem, HttpMethod.GET,Channel.class);
            bookService.saveApiNAVERBooks(response.getBody().getItems());
            showableBooks = bookService.findSavedBook(showableBooks, paramMap);
            log.info("!!!!!!!!!!!!!!!!!!!!afterAPi");
        }
        return showableBooks;
    }



    @GetMapping("/c1/userBook") // uri 바꿔야함
    public String insertUserBookAndBookCategoryBy(@ModelAttribute UserBookSaveVO request, RedirectAttributes redirectAttributes) throws MalformedURLException {

        log.error("insertUserBookAndBookCategoryBy:{}",request);
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<>();

        String url = "https://www.nl.go.kr";
        String path = "/seoji/SearchApi.do";
        paramMap.put("cert_key", Collections.singletonList(libraryKey));
        paramMap.put("result_style", Collections.singletonList("json"));
        paramMap.put("isbn", Collections.singletonList(request.getBookIsbn()));
        paramMap.put("page_no", Collections.singletonList("1"));
        paramMap.put("page_size", Collections.singletonList("10"));

        ResponseEntity<Result> response = restTempService.response(url, path, paramMap, null, HttpMethod.GET, Result.class);

        if(response.getBody().getDocs()!=null) {
            bookService.saveApiCategoryBooks(response.getBody().getDocs(), request.getBookIsbn());
        }
        redirectAttributes.addFlashAttribute("request", request);

        return "redirect:/p1/saveUserBook";

    }
}
