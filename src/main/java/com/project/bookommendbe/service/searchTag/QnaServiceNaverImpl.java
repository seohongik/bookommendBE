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
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service("naver")
public class QnaServiceNaverImpl implements QnaService {

    /*public Map<String, String> getQna(String title, String body) {
        return Map.of();
    }*/

    @Override
    public Map<String, String> getQna(String title, String body) {

        //String title = "뉴욕3부작";
        //String body = "내용이 뭐지?";
        //String title = "슈퍼맨";
        //String body = "내용이 뭐지?";

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
            // 불법이라 쫄려서 안씀
            driver.get("https://search.naver.com/search.naver?query=" + bookTitleBuilder.toString());

            WebElement webBody = driver.findElement(By.tagName("body"));
            // 예시: 지식백과 내용이 들어 있는 div 찾기 (클래스 이름은 상황에 따라 달라질 수 있음)
            List<WebElement> descs = webBody.findElements(By.className("detail_box"));

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

        return resultMap;
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
}
