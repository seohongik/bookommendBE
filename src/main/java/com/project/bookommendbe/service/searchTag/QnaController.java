package com.project.bookommendbe.service.searchTag;

import io.github.bonigarcia.wdm.WebDriverManager;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class QnaController {

    private QnaService qnaService;

    @Autowired
    public void setQnaService(@Qualifier("QnaServiceWikiImpl") QnaService qnaService) {
        this.qnaService = qnaService;
    }

    @GetMapping("/r1/qna")
    public Map<String,String> qna(@RequestParam  String title, @RequestParam  String body ) {
        //String title = "뉴욕3부작";
        //String body = "내용이 뭐지?";
        //String title = "슈퍼맨";
        //String body = "내용이 뭐지?";

        /*
        Map<String, String> resultMap = new LinkedHashMap<>();
        List<String> querys = getKeyword(body);

        // 1. bookTitleQuery 단어 분리
        String[] splitBookTitle = title.split(" ");
        StringBuilder bookTitleBuilder = new StringBuilder();

        // 2. splitBookTitle + querys 전체 합쳐서 하나의 리스트로
        List<String> totalWords = new ArrayList<>();
        Collections.addAll(totalWords, splitBookTitle);
        totalWords.addAll(querys);

        // 3. 중간에만 + 붙이기
        for (int i = 0; i < totalWords.size(); i++) {
            bookTitleBuilder.append(totalWords.get(i));
            if (i != totalWords.size() - 1) {
                bookTitleBuilder.append("+");
            }
        }

        ChromeOptions options = new ChromeOptions();

        ChromeDriver driver = new ChromeDriver(options);
        try {
            WebDriverManager.chromedriver().setup();
            driver.get("https://search.naver.com/search.naver?query=" + bookTitleBuilder.toString());

            WebElement webBody = driver.findElement(By.tagName("body"));
            // 예시: 지식백과 내용이 들어 있는 div 찾기 (클래스 이름은 상황에 따라 달라질 수 있음)
            List<WebElement> descs = webBody.findElements(By.className("detail_box"));
            descs.addAll(webBody.findElements(By.tagName("span")));

            Map<String, Double> bodyMap = new HashMap<>();
            Map<String, String>  linkMap= new HashMap<>();

            for (String query:querys){
                for (WebElement el : descs) {
                    double similarity = calculateJaccardSimilarity(query, el.getText());
                    similarity += computeCosineSimilarity(query, el.getText());
                    Document doc = Jsoup.parse(el.getAttribute("innerHTML"));
                    Element parsed = doc.body();

                    Document parsedDoc = Jsoup.parse(parsed.toString());
                    Elements texts = parsedDoc.select("a[href]");

                    for (Element text : texts) {
                        bodyMap.put(text.text(), similarity);
                        linkMap.put(text.text(), text.attr("href"));
                    }
                }
            }

            List<Map.Entry<String, Double>> list = new LinkedList<>(bodyMap.entrySet());
            list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

            if(!list.isEmpty()) {

                for (String query:querys) {
                    for (Map.Entry<String, Double> entry : list) {
                        if (split(entry.getKey()).contains(split(title)) || split(entry.getKey()).contains(split(query)) && "".equals(entry.getKey())) {
                            resultMap.put(linkMap.get(entry.getKey()), entry.getKey());
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 커스텀 에러 만들어서 리턴
        }finally {
            driver.quit();
        }

        return resultMap;*/

        return qnaService.getQna(title, body);
        
    }

    

}