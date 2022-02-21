package paasta.demo.service;

import paasta.demo.dto.MailDTO;

public interface IMailService {

	int doSendMail(String adminEmail, String password, MailDTO pDTO); // => 이메일 보내는 서비스
}
