package kopo.poly.service.impl;

import jakarta.mail.internet.MimeMessage;
import kopo.poly.dto.MailDTO;
import kopo.poly.service.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender mailSender;
    @Value("${Spring.mail.username}")
    private String frommail;

}
@Override
public int doSendMail(MailDTO pDTO) {
    //로그 찍기 (추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
    log.info("{}.doSendMail start!", this.getClass().getName());

    // 메일 발송 성공여부(발송 성공 : 1 / 발송 실패 : 0)
    int res = 1;

    // 전달 받은 DTO부터 데이터 가져오기(DTO 객체가 메모리에 올라가지 않아 Null이 발생할 수 있기 때문에 에러방지 차원으로 if문 사용
    if (pDTO == null) {
        pDTO = new MailDTO();
    }

    String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는사람
    String title = CmmUtil.nvl(pDTO.getTitle()); // 메일제목
    String contents = CmmUtil.nvl(pDTO.getContents()); //메일내용

    log.info("toMail : {} / title : {} / contents : {}", toMail, title, contents);

    //메일 발송 메세지 구조 (파일 첨부 가능)
    MimeMessage message = MailSender.createMimeMessage();

    //메일 발송 메세지 구조를 쉽게 생성하게 도와주는 객체
    MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

    try {
        messageHelper.setTo(toMail); //받는사람
        messageHelper.setFrom(fromMail); //보내는 사람
        messageHelper.setSubject(title); //메일 제목
        messageHelper.setText(contents); //메일 내용

        MailSender.send(message);
    } catch (Exception e) { // 모든 에러 다잡기
        res = 0;
        log.info("[ERROR] doSendMail : {}", e);
    }
    // 로그 찍기 (추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
    log.info("{}.doSendMail end!", this.getClass().getName());
    return res;
}
