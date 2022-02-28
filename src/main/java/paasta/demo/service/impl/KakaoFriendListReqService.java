package paasta.demo.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import paasta.demo.dto.mongo.friendListDTO;
import paasta.demo.dto.mongo.testDTO;
import paasta.demo.persistance.mapper.KakaoMessageMapper;
import paasta.demo.service.IKakaoFriendListReqService;
import paasta.demo.service.comm.IKakaoInfoService;
import paasta.demo.service.comm.KakaoServiceLog;

/**
 * @author 최별규
 * @version 1.0 친구 정보 불러오기 인터페이스
 * 친구 정보 제공 조건
 * 1) 친구인 사용자가 앱과 연결된 상태일 것
 * 2) 앱 연결시 카카오 서비스 내 친구 목록 제공에 동의할 것
 * 3) 숨김 또는 차단 친구가 아닐 것
 * 이 모든 상황을 만족하는 친구만 목록을 불러올 수 있음
 * tip : 10분간의 응답 캐시가 있기 떄문에 실시간 친구목록 변경이 되질 않는다.
 */
@Service("KakaoFriendListReq")
public class KakaoFriendListReqService extends KakaoServiceLog implements IKakaoFriendListReqService, IKakaoInfoService{

	@Autowired
	private KakaoMessageMapper kakaoFriendMapper;
	
	// => 엑세스 토큰으로 사용자의 친구 목록을 가져오는 메서드
	@SuppressWarnings({ "null", "deprecation" })
	@Override
	public JsonArray requestFriendList(String accessToken, int friendNum) throws Exception{
		log.info(this.getClass().getName() + "...request Kakao Freind List Start");
		
		JsonArray jsonArray = new JsonArray();
		String requestURL = RquestFreindListURL + "?&limit=" + friendNum;
		String inPutAccessToken = "Bearer " + accessToken; 
		//String strFreindNum = String.valueOf(friendNum);
		
		URL url = null; 
		HttpsURLConnection conn;

		try {
			url = new URL(requestURL); // 카카오서버 접속 URL 카카오 서버 주소
			conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			//--------------------------------------요청에 필요한 Header에 포함될 내용 헤더에 포함----------------------------
			conn.setRequestProperty("Authorization", inPutAccessToken);
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
			// 가끔 유저 에이전트 정보가 자바로 찍혀서 403이 될 수 있으니 반드시 헤더에 추가해줄 것
			conn.setDoOutput(true);
			conn.connect(); // => 버퍼를 사용해도 해결하도록 할 것....
			//-----------------------------------데이터를 보내고 작업부분<이슈 발생해서 주석처리>------------------------------------
			//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); 
			//StringBuilder sb = new StringBuilder();	// 파라미터 추가를 위한 문자열 빌더(속도 Up)		
			//sb.append("&limit="); // => 즐겨찾기에 추가된 친구를 불러온다. key
			//sb.append(strFreindNum); // => 즐겨찾기에 추가된 친구를 불러온다. value
			//bw.write(sb.toString());
			//bw.flush(); // 결과물 버퍼에서 보내기		
			log.info("...Request Ok ...... Response Work Start");
			//---------------------------------------------------데이터를 받는 작업부분---------------------------------------
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String br_line = "";
			String result = "";
			
			while ((br_line = br.readLine()) != null) { // 데이터가 null 이 아닐때 까지 계속해서 데이터를 불러오고
				result += br_line;
			}
			log.info("....result : " + result);
			//--------------------------------------------응답코드 받는 곳-----------------------------------------------------
			int responseCode = conn.getResponseCode();
			log.info("...responseCode : " + responseCode); // => 2xx면 성공, 4xx면 실패
			//----------------------------------------------JSON 파싱 수행---------------------------------------------------
            JsonParser parser = new JsonParser(); 
            JsonObject element = (JsonObject) parser.parse(result); // (제이슨오브젝트 상속 받음)넘어온 데이터에서 제이슨 파싱을 한 후 값을 넣음
            jsonArray = (JsonArray) element.get("elements");
            log.info("jsonArray : " + jsonArray);    
            
		} catch(IOException e) {
			log.info("Error : " + e.toString());
		} finally {
			if(jsonArray == null) {
				jsonArray.add("null");
				log.info("...Error : " + jsonArray);
			}

			log.info(this.getClass().getName() + "...Req Process End");
		}
		log.info(this.getClass().getName() + "...request Kakao Freind List End");
		return jsonArray;
	}
	// => 친구 목록 불러온 다음 저장을 수행하는 메서드(몽고 디비와 mySQL에 저장함)
	@Override
	public int insertFriendList(JsonArray paramObject) throws Exception {
		log.info(this.getClass().getName() + "...Kakao Freind Insert To DataBase Start");
		int res = 0; // 성공하면 1로 변하도록 함

		log.info("...FriendList array Size : " + paramObject.size());
		
		friendListDTO tempDTO; // => DTO 형식으로 임시 저장할 변수
		friendListDTO rDTO; // => 데이터 중복을 위한 변수
		//------------------------------------UUID 중복확인한 후 데이터베이스에 친구 목록 저장하는 로직-------------------------------------
		for(int i = 0; i < paramObject.size(); i++) {
			tempDTO = new friendListDTO();
			rDTO  = new friendListDTO();

			JsonElement element = paramObject.get(i);
			tempDTO.setFriend_uuid(element.getAsJsonObject().get("uuid").getAsString());
			// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ERROR >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
			rDTO = kakaoFriendMapper.getUuidExists(tempDTO); // => 널 발생
			log.info(".......null > " + tempDTO.getFriend_uuid());
			//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< ERROR <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
			log.info("Y? N? " + rDTO.getExists_yn().toString());
			if(rDTO.getExists_yn().equals("N")) {// => 만약 중복이 아니면 N을 반환하기 때문에 if 스코프 안에 로직이 동작함
				tempDTO.setKid(element.getAsJsonObject().get("id").getAsString());
				tempDTO.setName(element.getAsJsonObject().get("profile_nickname").getAsString());
				res = kakaoFriendMapper.insertFriendList(tempDTO);
				tempDTO = null;
				rDTO = null;
				element = null;
			}
		}
		//-------------------------------------------------------------------------------------------------------------------
		log.info(this.getClass().getName() + "...Kakao Freind Insert To DataBase End");
		return res;
	}
	

}
