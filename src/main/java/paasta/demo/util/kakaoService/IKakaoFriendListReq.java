package paasta.demo.util.kakaoService;

import java.io.IOException;

public interface IKakaoFriendListReq {
	
	public String requestFriendList(String accessToken, int friendNum) throws Exception; // => 엑세스 토큰으로 친구 목록 불러오기
}
