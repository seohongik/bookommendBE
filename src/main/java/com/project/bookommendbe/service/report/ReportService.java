package com.project.bookommendbe.service.report;

import com.project.bookommendbe.dto.ReportVO;
import com.project.bookommendbe.dto.UserVO;
import com.project.bookommendbe.entity.Report;
import com.project.bookommendbe.entity.User;
import com.project.bookommendbe.entity.UserBook;
import com.project.bookommendbe.service.user.UserService;
import com.project.bookommendbe.service.user.UserServiceSuper;
import com.project.bookommendbe.service.userbook.UserBookService;
import com.project.bookommendbe.service.userbook.UserBookServiceSuper;
import jakarta.xml.bind.DatatypeConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class ReportService extends ReportServiceSuper {

    protected final ReportRepository reportRepository;
    protected final UserService userService;
    protected final UserBookService userBookService;
    @Autowired
    ReportService(ReportRepository reportRepository, UserService userService, UserBookService userBookService) {
        super(reportRepository);
        this.reportRepository = reportRepository;
        this.userService = userService;
        this.userBookService = userBookService;

    }

    public Report saveUserReportBy(ReportVO saveRequest) {
        //TODO 로직 작성
        Optional<User> user=userService.getUserByIdOpen(saveRequest.getUserId());
        UserBook userBook=userBookService.getUserBooksByIdAndUserIdOpen(saveRequest.getUserBookId(),saveRequest.getUserId());
        Report report = new Report();
        report.setUser(user.get());
        report.setUserBook(userBook);
        report.setReportContent(saveRequest.getReportContent());
        report.setReportTitle(saveRequest.getReportTitle());
        saveRequest.setImageUrl(userBook.getBook().getCoverImageUrl());
        saveRequest.setDescription(userBook.getBook().getDescription());

        report.setYear(saveRequest.getYear());
        report.setMonth(saveRequest.getMonth());
        report.setCreatedAt(LocalDateTime.now());
        report.setReportDate(LocalDate.now());
        reportRepository.save(report);

        try {
            String userName = "seohong-ig";
            getHtmlContentIndex(userName,user, saveRequest);
            getHtmlDetail(saveRequest, userName, user.get());
        }catch (IOException e){
            e.printStackTrace();
        }


        return report;
    }

    public String getHtmlContentIndex(String userName, Optional<User> user,ReportVO saveRequest) throws IOException {
        StringBuilder html = new StringBuilder();

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"ko\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>서홍익 북 리포트</title>\n");
        html.append("    <style>\n");
        html.append("        body {\n");
        html.append("            font-family: sans-serif;\n");
        html.append("            margin: 0;\n");
        html.append("            padding: 0;\n");
        html.append("            line-height: 1.6;\n");
        html.append("        }\n");
        html.append("        header {\n");
        html.append("            background-color: #333;\n");
        html.append("            color: #fff;\n");
        html.append("            padding: 1rem;\n");
        html.append("            text-align: center;\n");
        html.append("        }\n");
        html.append("        header h1 {\n");
        html.append("            margin: 0;\n");
        html.append("        }\n");
        html.append("        nav {\n");
        html.append("            background-color: #555;\n");
        html.append("            padding: 0.5rem;\n");
        html.append("        }\n");
        html.append("        nav ul {\n");
        html.append("            list-style-type: none;\n");
        html.append("            margin: 0;\n");
        html.append("            padding: 0;\n");
        html.append("            display: flex;\n");
        html.append("            justify-content: center;\n");
        html.append("        }\n");
        html.append("        nav li {\n");
        html.append("            margin: 0 1rem;\n");
        html.append("        }\n");
        html.append("        nav a {\n");
        html.append("            color: #fff;\n");
        html.append("            text-decoration: none;\n");
        html.append("            font-weight: bold;\n");
        html.append("        }\n");
        html.append("        .container {\n");
        html.append("            display: flex;\n");
        html.append("            min-height: calc(100vh - 120px); /* 푸터 공간 고려 */\n");
        html.append("        }\n");
        html.append("        main {\n");
        html.append("            flex: 3; /* 메인 콘텐츠가 더 많은 공간 차지 */\n");
        html.append("            padding: 2rem;\n");
        html.append("        }\n");
        html.append("        aside {\n");
        html.append("            flex: 1; /* 사이드바가 적은 공간 차지 */\n");
        html.append("            background-color: #f4f4f4;\n");
        html.append("            padding: 2rem;\n");
        html.append("        }\n");
        html.append("        footer {\n");
        html.append("            background-color: #333;\n");
        html.append("            color: #fff;\n");
        html.append("            text-align: center;\n");
        html.append("            padding: 1rem;\n");
        html.append("            position: relative;\n");
        html.append("            bottom: 0;\n");
        html.append("            width: 100%;\n");
        html.append("        }\n");
        html.append("        iframe{\n");
        html.append("             border: none;\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("\n");
        html.append("    <header>\n");
        html.append("        <h1>북 코멘드 리포트</h1>\n");
        html.append("        <p>환영합니다!</p>\n");
        html.append("    </header>\n");
        html.append("\n");
        html.append("    <nav>\n");
        html.append("        <ul>\n");
        html.append("            <li><a href=\"#\">독서 동기</a></li>\n");
        html.append("            <li><a href=\"#\">독후감</a></li>\n");
        html.append("            <li><a href=\"#\">타임 라인</a></li>\n");
        html.append("        </ul>\n");
        html.append("    </nav>\n");
        html.append("\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <main>\n");
        // 따옴표 이스케이프 처리에 유의
        html.append("            <iframe id='content' src=\"\" width=\"100%\" height=\"100%\"></iframe>\n");
        html.append("        </main>\n");

            html.append("        <aside>\n");
            html.append("            <div>\n");
            html.append("                <div>\n");
            for (UserBook userBook : userBookService.getUserBooksByUserOpen(user)) {
                html.append("                    <a class=\"report\" id=\"").append(userBook.getId()).append("\">").append(userBook.getBook().getAuthor()).append(" / ").append(userBook.getBook().getTitle()).append("<br><br><br>").append("</a>\n");
            }
            html.append("                </div>\n");
            html.append("            </div>\n");
            html.append("        </aside>\n");
            html.append("    </div>\n");
            html.append("\n");

        html.append("    <footer>\n");
        html.append("        <p>&copy; 2025 모든 권리 보유.</p>\n");
        html.append("        <p>서홍익 개인 공간.</p>\n");
        html.append("    </footer>\n");
        html.append("\n");
        html.append("</body>\n");
        html.append("<script>\n");
        // Java 문자열 내의 스크립트 코드는 그대로 유지

        Set<String> yearSet = new HashSet<>();
        for (Report report : reportRepository.findAllByUserOrderByCreatedAt(user.get())) {
            yearSet.add(report.getYear());
        }
        html.append("    document.querySelectorAll(\".report\").forEach(function(item,index){\n");
        html.append("        item.addEventListener(\"click\",function(){\n");
        for (String year : yearSet) {
            html.append("            document.querySelector(\"#content\").src = '../").append(year).append("/'+(item.id)+\".html\";\n");
        }
        html.append("            \n");
        html.append("        })\n");
        html.append("    });\n");

        html.append("</script>\n");
        html.append("</html>\n");

        File file = new File("/Users/"+userName+"/book-reporter/index.html");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(html.toString());
        fileWriter.close();

        // 완성된 HTML 문자열 반환
        return html.toString();
    }

    public void getHtmlDetail(ReportVO report,String userName,User user) throws IOException {
        // 원본 HTML의 일부 (section 태그)
        // StringBuilder 객체를 생성합니다.
        StringBuilder htmlBuilder = new StringBuilder();

        // 1. <!DOCTYPE html> 및 <html>, <head> 부분 추가
        htmlBuilder.append("<!DOCTYPE html>\n")
                .append("<html lang=\"ko\">\n")
                .append("<head>\n")
                .append("    <meta charset=\"UTF-8\">\n")
                .append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
                .append("    <!-- Tailwind CSS CDN 로드 -->\n")
                .append("    <script src=\"https://cdn.tailwindcss.com\"></script>\n")
                .append("    <style>\n")
                .append("        /* 기본 폰트 설정 */\n")
                .append("        body {\n")
                .append("            font-family: 'Inter', sans-serif;\n")
                .append("        }\n")
                .append("        /* 이미지 높이 조정 (Tailwind의 h-90 클래스가 표준이 아니므로 사용자 정의) */\n")
                .append("        .h-90 {\n")
                .append("            height: 22.5rem; /* 360px */\n")
                .append("        }\n")
                .append("    </style>\n")
                .append("</head>\n");

        // 2. <body> 시작 및 컨테이너 <div> 추가
        htmlBuilder.append("<body class=\"bg-gray-100 p-8 min-h-screen\">\n")
                .append("\n")
                .append("    <div class=\"max-w-4xl mx-auto bg-white shadow-2xl rounded-xl p-8 sm:p-12 lg:p-16\">\n")
                .append("        \n");

        // 3. <header> 섹션 추가
        htmlBuilder.append("        <header class=\"text-center mb-16\">\n")
                .append("            <h1 class=\"text-5xl font-extrabold text-gray-900 mb-2\">이방인 (L'Étranger)</h1>\n")
                .append("            <p class=\"text-xl text-gray-600 font-medium\">"+report.getReportTitle()+"</p>\n")
                .append("        </header>\n")
                .append("\n");

        // 4. <section> 시작
        htmlBuilder.append("        <section>\n");

        // 5. 책 표지 이미지 블록 추가 (bookCoverHtml)
        htmlBuilder.append("            <!-- 추가된 책 표지 이미지 블록 -->\n")
                .append("            <div class=\"flex flex-col items-center justify-center p-8 bg-gray-50 rounded-xl mb-12\">\n")
                .append("                <h2 class=\"text-3xl font-bold text-gray-900 mb-6\">책 표지</h2>\n")
                .append("                <img \n")
                .append("                    src="+report.getImageUrl()+"/")
                .append("                    class=\"w-60 h-90 object-cover shadow-2xl rounded-lg transform transition duration-500 hover:scale-105\"\n")
                .append("                    onerror=\"this.onerror=null; this.src='https://placehold.co/300x450/4b5563/e5e7eb?text=Image+Unavailable'\"\n")
                .append("                />\n")
                .append("                <p class=\"mt-4 text-sm text-gray-500\">"+ report.getReportTitle()+ "</p>\n")
                .append("            </div>\n");

        // 6. 첫 번째 <article> (내용 분석 및 감상) 시작
        htmlBuilder.append("            \n")
                .append("            <article class=\"border-t-2 border-gray-200 pt-8 mt-12\">\n")
                .append("                <h3 class=\"text-2xl font-semibold text-gray-800 mb-4\">줄거리</h3>\n")
                .append("                <p class=\"text-lg leading-relaxed text-gray-700\">\n")
                .append("                  "+report.getDescription()+"\n")
                .append("                </p>\n");

        // 7. 울림이 있는 한줄 (인용구) 블록 추가 (quoteHtml)

        htmlBuilder.append("                <!-- 추가된 '울림이 있는 한줄' -->\n")
                    .append("                <p class=\"mt-6 border-l-4 border-gray-300 pl-4 italic text-gray-600\">\n")
                    .append("                 "+report.getSympathyLine1()+"\n")
                    .append("                </p>\n")
                    .append("                <p class=\"mt-6 border-l-4 border-gray-300 pl-4 italic text-gray-600\">\n")
                    .append("                 "+report.getSympathyLine2()+"\n")
                    .append("                </p>\n")
                    .append("                <p class=\"mt-6 border-l-4 border-gray-300 pl-4 italic text-gray-600\">\n")
                    .append("                 "+report.getSympathyLine3()+"\n")
                    .append("                </p>\n");

        // 8. 첫 번째 <article> 끝
        htmlBuilder.append("            </article> \n");

        // 9. 두 번째 <article> (깨달음과 느낀 점) 추가
        htmlBuilder.append("            <article class=\"border-t-2 border-gray-200 pt-8 mt-12\">\n")
                .append("                <h3 class=\"text-2xl font-semibold text-gray-800 mb-4\">깨달음과 느낀 점</h3>\n")
                .append("                <p class=\"text-lg leading-relaxed text-gray-700\">\n")
                .append("                 "+report.getReportContent()+"\n")
                .append("                </p>\n")
                .append("            </article>\n");

        // 10. <section> 및 <div>, <body>, <html> 닫기
        htmlBuilder.append("        </section>\n")
                .append("\n")
                .append("    </div>\n")
                .append("</body>\n")
                .append("</html>");

        // 11. 최종 결과 출력
        System.out.println("--- StringBuilder.append()로 완성된 HTML ---");
        System.out.println(htmlBuilder.toString());


        // 6. 결과 출력
        File folder = new File("/Users/"+userName+"/book-reporter/"+ report.getYear());

        //report.getId()+".html"

        if(!folder.exists()){
            folder.mkdirs();
        }else {
            File file = new File(folder+"/"+report.getUserBookId()+".html");
            if (file.exists()) {
                if (file.delete()) {
                    log.info("파일 업데이트");
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(htmlBuilder.toString());
            fileWriter.close();
        }

    }
}
