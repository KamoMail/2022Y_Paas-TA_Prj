package paasta.demo.service;

// 로그인에 성공하면 엑세스토큰을 활용하여 자신 혹은 친구에게 톡을 보내는 인터페이스 입니다.
/*
 * 만약 친구에게 톡을 보내려면 나에게 톡 보내기 성공 후 카카오사의 승인을 받아야 해당 서비스를 이용할 수 있습니다.
 * */
public interface IKakaoMessageService {
	
	public String sendFriendKakaoTalk(); // => 친구에게 메시지 보내기
	
	public String sendMyKakaoTalk(String accessToken, String message, String moveUrl); // 카카오 메시지 나에게 보내기
}
