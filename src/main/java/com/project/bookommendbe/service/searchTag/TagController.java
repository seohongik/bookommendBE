package com.project.bookommendbe.service.searchTag;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class TagController {

    // 서비스 로직으로 뺄꺼도 DB에도 넣고 entity도 만들 예정
    @GetMapping("/r1/tag")
    public List<String> getTags(@RequestParam String bookTitleQuery, @RequestParam String queryBody) {

        List<String> querys = getKeyword(queryBody);

        // 1. bookTitleQuery 단어 분리
        String[] splitBookTitle = bookTitleQuery.split(" ");
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

        String bookTitle = bookTitleBuilder.toString();
        try {
            Deque<String> findWord = new ArrayDeque<>();
            Deque<String> queries = new ArrayDeque<>();

            List<String> splitList = new ArrayList<>();

            queries.addFirst(bookTitle);
            for (String query : querys) {
                splitList.add(query);
                findWord.addFirst(query);
            }


            List<String> bodys = new ArrayList<>();
            // URL에서 문서 가져오기 (기본 GET 요청)
            while (!queries.isEmpty()) {
                Document doc = Jsoup.connect("https://search.naver.com/search.naver?query=" + queries.poll()).get();
                Elements elements = doc.getElementsByTag("a");
                //Elements elements = doc.getAllElements();

                for (Element e : elements) {
                    bodys.add(e.text());
                }

            }

            Map<String, Double> similarityMap = new LinkedHashMap<>();

            for (String word : findWord) {
                for (Character fw : word.toCharArray()) {
                    for (String body : bodys) {
                        double similarity = computeCosineSimilarity(word, body);
                        for (Character bw : body.toCharArray()) {
                            if (fw.equals(bw)) {
                                similarity += 0.1;
                            }
                        }
                        similarityMap.put(body, similarity);
                    }
                }
            }

            List<Map.Entry<String, Double>> list = new LinkedList<>(similarityMap.entrySet());
            // 2. Value 기준으로 정렬 (오름차순)
            Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
            //System.out.println("list = " + list);

            int max = 5;
            int count = 0;

            StringBuffer sb = new StringBuffer();

            for (Map.Entry<String, Double> entry : list) {

                if (count <= max) {
                    sb.append(entry.getKey());
                }
                count++;

            }
            List<String> result = getKeyword(sb.toString());

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

    private List<String> getKeyword(String text){
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

        List<Token> tokenList = komoran.analyze(text).getTokenList();

        List<String> keywords = tokenList.stream()
                .filter(token -> token.getPos().startsWith("NN"))  // 명사만
                .map(Token::getMorph)
                .distinct()  // 중복 제거
                .collect(Collectors.toList());

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

}
