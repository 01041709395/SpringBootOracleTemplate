package kopo.poly.controller;

import jakarta.servlet.http.HttpServletRequest;
import kopo.poly.dto.MailDTO;
import kopo.poly.dto.MsgDTO;
import kopo.poly.service.IMailService;
import kopo.poly.service.impl.IMailService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RequestMapping(value = "/mail")
@RequiredArgsConstructor
@Controller

public class MailController {
    private final IMailService mailService; // 메일 발송을 위한 서비스 객체를 사용하기

    /**
     * 메일 발송하기 폼
     */
    @GetMapping(value = "mailForm")
    public String mailFrom() {

        //로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info("{}mailForm Start!", this.getClass().getName());

        return "mail/mailForm";
    }

    /**
     * 메일 발송하기
     */
    @ResponseBody
    @PostMapping(value = "sendMail")
    public MsgDTO sendMail(HttpServletRequest request) {

        // 로그 찍기 (추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
        log.info("{}.SendMail Start!", this.getClass().getName());

        String msg; // 발송 결과 메세지

        // 웹 URL로부터 전달받는 값들
        String toMail = CmmUtil.nvl(request.getParameter("toMail")); //받는 사람
        String title = CmmUtil.nvl(request.getParameter("title")); // 메일 제목
        String contents = CmmUtil.nvl(request.getParameter("contents")); // 메일 내용
        /*
         *
         * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어왔는지 파악해야함 반드시 작성할 것
         *
         */
        log.info("toMail : {} / title : {} / contents : {}", toMail, title, contents);

        // 메일 발송할 정보 넣기 위한 DTO 객체 생성하기
        MailDTO pDTO = new MailDTO();

        // 웹에서 받은 값 DTO에 넣기
        pDTO.setToMail(toMail); // 받는 사람 DTO에 저장
        pDTO.setTitle(title); // 메일 제목 DTO에 저장
        pDTO.setContents(contents); // 메일 내용 DTO에 저장

        // 메일 발송하기
        int res = mailService.doSendMail(pDTO);

        if (res == 1) { //메일발송 성공
            msg = "메일 발송하였습니다.";
        } else { //메일발송 실패
            msg = "메일 발송 실패하였습니다.";
        }
        log.info(msg);

        //결과 메세지 전달하기
        MsgDTO dto = new MsgDTO();
        dto.setMsg(msg);

        // 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
        log.info("{}.sendMail End!", this.getClass().getName());

        return dto;
    }
}