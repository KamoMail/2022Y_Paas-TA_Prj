package paasta.demo.dto;

import lombok.Data;

@Data
public class MailDTO {
	
	private String adminEmail; // => 메일을 보내기 위한 관리자 이메일
	
	private String password; // => 메일을 보내기 위한 관리자 비밀번호
	
	private String toMail; // => 받는 사람 이메일
	
	private String title; // => 메일 제목
	
	private String contents; // => 메일 내용

}
