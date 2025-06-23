package com.project.bookommendbe.service;

import com.project.bookommendbe.util.EmailConfig;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final EmailConfig javaMailSender;

    @Autowired
    public MailService(EmailConfig emailConfig) {
        this.javaMailSender = emailConfig;
    }


    /*
    // 파일 첨부 예시
    public void sendEmailWithAttachment(String to, String subject, String text, String pathToAttachment) throws MessagingException, jakarta.mail.MessagingException {
        MimeMessage message = emailConfig.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        //FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
        //helper.addAttachment("첨부파일명", file);
        mailSender.send(message);
    }*/


}