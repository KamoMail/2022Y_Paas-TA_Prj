package paasta.demo.util.kakaoService;

import java.util.Map;

// 카카오 로그인을 위한 인터페이스 입니다.
// 인증코드로 엑세스 토큰을 발급받은 후 사용자 정보를 가져올 수 있습니다.
public interface IKakaoLogin {
	
	String getAuthCode() throws Exception; // => 카카오 로그인 시도를 위한 인증 코드 받는 메서드
	
	public String getAccessToken(String authCode) throws Exception; // => 인증코드를 통해 엑세스 토큰을 발급하는 메서드
	
	public Map<String, Object> getUserInfo(String accessToken) throws Exception; // => 엑세스 토큰으로 유저 정보 가져오기

}
