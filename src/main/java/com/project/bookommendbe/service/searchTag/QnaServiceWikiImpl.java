package com.project.bookommendbe.service.searchTag;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.bonigarcia.wdm.WebDriverManager;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jspecify.annotations.Nullable;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.DoubleBuffer;
import java.util.*;
import java.util.stream.Collectors;

// 이것만 합법임
@Service("wiki")
public class QnaServiceWikiImpl implements QnaService {


    @Override
    public Map<String, String> getQna(String title, String body) {

        //String title = "뉴욕3부작";
        //String body = "내용이 뭐지?";
        //String title = "슈퍼맨";
        //String body = "내용이 뭐지?";

        Map<WebElement, Double> detailPageMap = new LinkedHashMap<>();

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

        Map<String, String> result = new HashMap<>();
        try {


            try {
                Document document = Jsoup.connect("https://ko.wikipedia.org/wiki/" + title).get();
                List<Element> descs = document.getElementsByTag("p");

                descs.forEach((key)->{

                    Elements parsed =key.getElementsByTag("a");
                    querys.forEach(query-> {
                        if (parsed.text().contains(query)) {
                            result.put("https://ko.wikipedia.org/wiki/" + query,parsed.text());
                        }
                    });
                });
            }catch (HttpStatusException e) {

                e.printStackTrace();
                Document document = Jsoup.connect("https://ko.wikipedia.org/w/index.php?search=" + title).get();
                List<Element> descs = document.getElementsByTag("li");
                Map<Element, Double> bodyMap = new HashMap<>();
                for (Element el : descs) {
                    double similarity=0.0;
                    for (String query:querys){
                   
                        similarity += calculateJaccardSimilarity(query, el.text());
                        similarity += computeCosineSimilarity(query, el.text());
                 

                    }
                    similarity= calculateNounSimilarity(querys,el.text());
                    bodyMap.put(el, similarity);
                }

                List<Map.Entry<Element, Double>> list = new LinkedList<>(bodyMap.entrySet());
                list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                for (Map.Entry<Element, Double> entry : list) {
                    Elements elements = entry.getKey().getElementsByClass("mw-search-result-heading");

                    Elements elementInner = entry.getKey().getElementsByClass("searchresult");

                    //if(elements.text().contains(title)){

                    elementInner.forEach((key)->{

                        String keyStr = key.getElementsByTag("span").text();

                        if(keyStr.contains(title)){
                            System.out.println("key = " + key);
                            System.out.println("elements = " + elements.text());
                            result.put("https://ko.wikipedia.org/wiki/"+elements.text(), key.text());
                        }

                    });
                    
                }

                List<String> alinkList = new ArrayList<>(result.keySet());

                Map<String,Double> staticsMap = new LinkedHashMap<>();

                Document document2 = Jsoup.connect(alinkList.get(0)).get();
                List<Element> descs2 = document2.getElementsByTag("p");

                List<String> allList=descs2.stream().map(Element::text).collect(Collectors.toList());
                allList.forEach((key)->{

                    double similarity=0.0;
                    for (String query:querys){
                        similarity += calculateJaccardSimilarity(query, key);
                        similarity += computeCosineSimilarity(query, key);
                    }
                    similarity= calculateNounSimilarity(querys,key);

                    staticsMap.put(key, similarity);

                });
                List<Map.Entry<String, Double>> staticsList = new LinkedList<>(staticsMap.entrySet());
                staticsList.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

                result.put(alinkList.get(0), staticsList.get(0).getKey());


            }

            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    private String split(String query) {
        query=query.replaceAll("[^가-힣]", "");

        String[] chosungs = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ" , "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String[] jungsungs = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
        String[] jongsungs = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String result = "";

        for (int i = 0; i < query.length(); i++) {
            int keywordUniBase = query.charAt(i) - 44032;
            char chosung = (char)(keywordUniBase / 28 / 21);
            char jungsung = (char)(keywordUniBase / 28 % 21);
            char jongsung = (char)(keywordUniBase % 28);
            result += chosungs[chosung] + jungsungs[jungsung] + jongsungs[jongsung];
        }
        return result;
    }

    private List<String> splitBody(String query) {

        List<String> body = new ArrayList<>();
        query=query.replaceAll("[^가-힣]", "");

        String result="";

        String[] chosungs = {"ㄱ", "ㄲ", "ㄴ", "ㄷ", "ㄸ", "ㄹ", "ㅁ", "ㅂ", "ㅃ", "ㅅ", "ㅆ", "ㅇ" , "ㅈ", "ㅉ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};
        String[] jungsungs = {"ㅏ", "ㅐ", "ㅑ", "ㅒ", "ㅓ", "ㅔ", "ㅕ", "ㅖ", "ㅗ", "ㅘ", "ㅙ", "ㅚ", "ㅛ", "ㅜ", "ㅝ", "ㅞ", "ㅟ", "ㅠ", "ㅡ", "ㅢ", "ㅣ"};
        String[] jongsungs = {"", "ㄱ", "ㄲ", "ㄳ", "ㄴ", "ㄵ", "ㄶ", "ㄷ", "ㄹ", "ㄺ", "ㄻ", "ㄼ", "ㄽ", "ㄾ", "ㄿ", "ㅀ", "ㅁ", "ㅂ", "ㅄ", "ㅅ", "ㅆ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ"};

        for (int i = 0; i < query.length(); i++) {
            int keywordUniBase = query.charAt(i) - 44032;
            char chosung = (char)(keywordUniBase / 28 / 21);
            char jungsung = (char)(keywordUniBase / 28 % 21);
            char jongsung = (char)(keywordUniBase % 28);
            result += chosungs[chosung] + jungsungs[jungsung] + jongsungs[jongsung];
        }
        body.add(result);

        return body;
    }

    private List<String> getKeyword(String text){
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        List<Token> tokenList = komoran.analyze(text).getTokenList();

        List<String> keywords = tokenList.stream()
                .filter(token -> token.getPos().startsWith("NN"))  // 명사만
                .map(Token::getMorph)
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

        System.out.println("핵심 단어: " + keywords);
        return keywords;
    }

    private double computeCosineSimilarity(String a, String b) {
        Set<String> wordsA = new HashSet<>(Arrays.asList(a.split(" ")));
        Set<String> wordsB = new HashSet<>(Arrays.asList(b.split(" ")));

        Set<String> allWords = new HashSet<>();
        allWords.addAll(wordsA);
        allWords.addAll(wordsB);

        int[] vecA = new int[allWords.size()];
        int[] vecB = new int[allWords.size()];

        int idx = 0;
        for (String word : allWords) {
            vecA[idx] = wordsA.contains(word) ? 1 : 0;
            vecB[idx] = wordsB.contains(word) ? 1 : 0;
            idx++;
        }

        int dot = 0, normA = 0, normB = 0;
        for (int i = 0; i < vecA.length; i++) {
            dot += vecA[i] * vecB[i];
            normA += vecA[i] * vecA[i];
            normB += vecB[i] * vecB[i];
        }

        return dot / (Math.sqrt(normA) * Math.sqrt(normB) + 1e-10);
    }


    // 문자열을 단어 집합으로 분리 (공백 기준)
    private  Set<String> tokenize(String text) {
        text = text.toLowerCase().replaceAll("[^a-zA-Z가-힣0-9\\s]", "");
        return new HashSet<>(Arrays.asList(text.split("\\s+")));
    }

    // Jaccard 유사도 계산
    private  double calculateJaccardSimilarity(String word, String sentence) {
        Set<String> set1 = tokenize(word);
        Set<String> set2 = tokenize(sentence);

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        if (union.isEmpty()) return 0.0;

        return (double) intersection.size() / union.size();
    }

    public  double calculateNounSimilarity(List<String> nounList, String sentence) {
        if (nounList == null || nounList.isEmpty() || sentence == null) {
            return 0.0;
        }

        int matchCount = 0;

        for (String noun : nounList) {
            if (sentence.contains(noun)) {
                matchCount++;
            }
        }

        // 유사도 = 포함된 명사 수 / 전체 명사 수
        return (double) matchCount / nounList.size();
    }
}
