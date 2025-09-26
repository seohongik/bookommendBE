package com.project.bookommendbe.util;

import com.project.bookommendbe.entity.User;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.security.NoSuchAlgorithmException;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${spring.mail.host}")
    private String host;
    @Value("${spring.mail.port}")
    private int port;
    @Value("${spring.mail.username}")
    private String username;
    @Value("${spring.mail.password}")
    private String password;
    public void javaMailSender(String to, String subject, String text) throws MessagingException {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setJavaMailProperties(getMailProperties());
        Session session = Session.getInstance(getMailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("SMTP SSL 테스트 메일");
        message.setText("이 메시지는 SMTP SSL을 사용하여 전송되었습니다.");
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true); // 두 번째 인자는 HTML 여부
        // 메일 전송
        Transport.send(message);
        System.out.println("메일 전송 성공!");
    }
    @Bean
    public Properties getMailProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.fallback", "false"); // SSL 실패 시 일반 소켓 사용 안 함
        return props;
    }
    
    public void sendEmail(User user, int authNumber) throws jakarta.mail.MessagingException, NoSuchAlgorithmException {
        String text = "<html>" +
                "<body>" +
                "<h3>이메일 인증을 위해 아래 링크를 클릭해주세요.</h3>" +
                "<h1>"+"bookommend의 비밀번호를 재설정 하기위한 인증번호를 보냅니다."+"</h1>"+
                "<div>"+authNumber+"</div>" +
                "</body>" +
                "</html>";
        javaMailSender(user.getEmail(), "bookommend의 비밀번호를 재설정 하기위한 링크를 보냅니다.", text);
        //mailService.sendEmail(user.getEmail(),"비밀번호를 재설정 하기위한 링크를 보냅니다.",emailContent);
    }

}
