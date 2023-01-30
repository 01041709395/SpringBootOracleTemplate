package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;

public interface IMailService {

    // 메일 발송
    int doSendMail(MailDTO pDTO);
}
