package kr.hossam.myshop.helpers;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MailHelper {
    /**
     * 메일 발송을 위한 JavaMailSender 객체
     *
     * --> import org.springframework.mail.javamail.JavaMailSender;
     */
    private final JavaMailSender javaMailSender;

    /**
     * 보내는 사람의 이름과 이메일 주소 (환경설정 파일에서 읽어옴)
     */
    @Value("${mailhelper.sender.name}")
    private String senderName;

    /**
     * 보내는 사람의 이메일 주소 (환경설정 파일에서 읽어옴)
     */
    @Value("${mailhelper.sender.email}")
    private String senderEmail;

    /**
     * 메일을 발송한다.
     *
     * @param receiverName - 수신자 이름
     * @param receiverEmail - 수신자 이메일 주소
     * @param subject  - 제목
     * @param content  - 내용
     *
     * @throws MessagingException
     */
    // --> import javax.mail.MessagingException;
    public void sendMail(String receiverName, String receiverEmail, String subject, String content) throws Exception {
        log.debug("----------------------------------------------------");
        log.debug(String.format("RecvName: %s", receiverName));
        log.debug(String.format("RecvEmail: %s", receiverEmail));
        log.debug(String.format("Subject: %s", subject));
        // 본문 내용의 경우 출력량이 많아서 주석 처리
        //log.debug(String.format("Content: %s", content));
        log.debug("----------------------------------------------------");

        // --> import jakarta.mail.internet.MimeMessage;
        MimeMessage message = javaMailSender.createMimeMessage();
        // --> import org.springframework.mail.javamail.MimeMessageHelper;
        MimeMessageHelper helper = new MimeMessageHelper(message);

        // 제목, 내용, 수신자 설정
        try {
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(new InternetAddress(receiverEmail, receiverName, StandardCharsets.UTF_8.name()));
            helper.setFrom(new InternetAddress(senderEmail, senderName, StandardCharsets.UTF_8.name()));

            // 메일 보내기
            javaMailSender.send(message);
        } catch (MessagingException e) {
            log.error("메일 발송 정보 설정 실패", e);
            throw e;
        } catch (UnsupportedEncodingException e) {
            log.error("지원하지 않는 인코딩", e);
            throw e;
        } catch (Exception e) {
            log.error("알 수 없는 오류", e);
            throw e;
        }
    }

    /**
     * 메일을 발송한다.(첨부파일 없음)
     *
     * @param receiverEmail - 수신자 이메일 주소
     * @param subject  - 제목
     * @param content  - 내용
     * @throws MessagingException
     */
    public void sendMail(String receiverEmail, String subject, String content) throws Exception {
        this.sendMail(null, receiverEmail, subject, content);
    }
}