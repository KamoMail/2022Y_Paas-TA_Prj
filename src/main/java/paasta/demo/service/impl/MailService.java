package paasta.demo.service.impl;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

import paasta.demo.dto.MailDTO;
import paasta.demo.service.IMailService;
import paasta.demo.service.comm.AbstractService;
import paasta.demo.util.CmmUtil;

@Service("MailService")
public class MailService extends AbstractService implements IMailService{

	final String host = "smtp.naver.com";     // => 엔드 유저 
	final String port = "587"; // google은 465, naver는 587
	
	// 메일 보내기
	@Override
	public int doSendMail(String adminEmail, String password, MailDTO pDTO) {
		log.info(this.getClass().getName()+ "...do.sendMail start!!");
		
		String toMail = CmmUtil.nvl(pDTO.getToMail()); // 받는 사람
		log.info(this.getClass().getName()+ "...받는 사람 이메일 : " + toMail);
		log.info(this.getClass().getName()+ "...관리자 email : " + adminEmail);

		//메일 방송 성공여부 확인용 변수
		int res = 1;
		
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // javax 외부 라이브러리에 보내는 사람 정보 설정
		props.put("mail.smtp.auth", "true"); // 인증여부 false는 인증 안함 무조건 true 하기
		//props.put("mail.smtp.port", port); // 포트 연결
		
		//--------------------------네이버 SMTP 서버 인증 처리 로직---------------------------
		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(adminEmail, password);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(adminEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			//메일 제목
			message.setSubject(CmmUtil.nvl(pDTO.getTitle()));
			// 메일 내용
			message.setText(CmmUtil.nvl(pDTO.getContents()));
			// 메일 발송
			Transport.send(message);
		} catch (MessagingException e) {
			res = 0; // 메일발송이 실패하기 때문에 0으로 변경
			log.info("[EROOR]" + this.getClass() + "...doSendMail : " + e);
		} catch (Exception e) {
			res = 1;
			log.info("[EROOR]" + this.getClass() + "...doSendMail : " + e);
		}
		log.info(this.getClass() + "...doSendMail end!!");
		return res;
	}
	

}
