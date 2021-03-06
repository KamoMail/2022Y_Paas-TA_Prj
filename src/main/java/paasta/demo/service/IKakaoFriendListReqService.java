package paasta.demo.service;

import com.google.gson.JsonArray;

public interface IKakaoFriendListReqService {
	
	public int insertFriendList(JsonArray paramObject, String user_email) throws Exception; // => 친구 목록 불러온 다음 저장하는 로직
	
	public JsonArray requestFriendList(String accessToken, int friendNum) throws Exception; // => 엑세스 토큰으로 친구 목록 불러오기
}
