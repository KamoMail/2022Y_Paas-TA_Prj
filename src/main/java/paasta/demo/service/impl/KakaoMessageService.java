package paasta.demo.service.impl;


import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

import paasta.demo.dto.mongo.friendListDTO;
import paasta.demo.persistance.mapper.KakaoMessageMapper;
import paasta.demo.service.IKakaoMessageService;
import paasta.demo.service.comm.IKakaoInfoService;
import paasta.demo.service.comm.KakaoServiceLog;
/* @Auth 최별규
 * @Version 1.0
 * 카카오 엑세스 토큰을 통해 메시지를 보내는 클래스입니다.
 * ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2022.02.12 |  최별규     |  초안 작성
 * 메시지는 엑세스 토큰이 있어야만 발송이 가능합니다.
 * */
@Service("KakaoMessageService")
public class KakaoMessageService extends KakaoServiceLog implements IKakaoMessageService, IKakaoInfoService{
	
	@Autowired
	private KakaoMessageMapper kakaoFriendMapper;
	
// => 카카오 로그인 성공 후 나에게 톡을 보내는 기능
	@Override
	public String sendMyKakaoTalk(String accessToken, String message, String moveUrl) {
		log.info(this.getClass().getName() + "...send My Kakao Talk Start!!!");
		String returnMessageLog = "카카오톡 보내기 성공";
		// 메시지 입력 부
		String contents = message;
		String web_link = moveUrl;
		String mobile_web_link = moveUrl;
		//-----------------------------------link 키 value는 웹과 모바일로 나누어져 있어 맵에다 담았음---------------------------
		HashMap<String, String> linkMap = new HashMap<String, String>();
		linkMap.put("web_url", web_link);
		linkMap.put("mobile_web_url", mobile_web_link);
		//--------------------------------------제이슨 형식의 데이터 생성을 위한 작업-------------------------------------------- 
		JSONObject dataOne = new JSONObject();
		dataOne.put("object_type", "text");
		dataOne.put("text", contents);
		dataOne.put("link", linkMap);
		dataOne.put("button_title", "사이트이동");
		//----------------------------------------------접속을 위한 객체 선언부----------------------------------------------
		URL url = null; 
		HttpURLConnection conn = null;
		JSONObject template_object = null; // 가장 바깥 쪽의 JSON 최종 결과물 담을 곳임
		try {
			url = new URL(SEND_TALK_URL); // 카카오 접속 URL
			conn = (HttpURLConnection) url.openConnection();
			template_object = new JSONObject();
			template_object.put("template_object", dataOne); // 최종 JSON으로 만들기
			
			log.info("....template_object  : " + template_object.toString());
			
			conn.setRequestMethod("POST");
			//--------------------------------------요청에 필요한 Header에 포함될 내용 헤더에 포함----------------------------
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			// conn.setRequestProperty("template_object", template_object.toString());
			//---------------------------------------------------데이터를 보내는 작업부분------------------------------------
			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); // json 버퍼스트림으로 넣기
			StringBuilder sb = new StringBuilder();			
			sb.append("&template_object=");
			sb.append(dataOne);
			bw.write(sb.toString()); // 형변환하여 쓰기
			bw.flush(); // 결과물 버퍼에서 보내기
			bw.close(); // 리소스 닫기
			//--------------------------------------------응답코드 받는 곳-----------------------------------------------------
			int responseCode = conn.getResponseCode();
			log.info("responseCode : " + responseCode);
			//------------------------------------------------------------------------------------------------------------------
		} catch(IOException e) {
			log.info(e);
		} finally {
			log.info(this.getClass().getName() + "...send My Kakao Talk End");
		}
		return returnMessageLog;
	}
// => 친구에게 메시지 보내는 메서드
	@SuppressWarnings("null")
	@Override
	public String sendFriendKakaoTalk(String accessToken, String user_email) throws Exception {
		log.info(this.getClass().getName() + "...send Message Fot My Friend Start");
		String res = "NotToMessage";
		String testMessage = "최별규입니다. 그냥 무시하세요 [범중, 상인. 종은, 흥수, 하윤]";
		String web_link = "nullData";
		String mobile_web_link = "nullData";
		//-----------------------------------메시지를 보낼 사용자 정보 가져오는 로직-------------------------------------------------------
		friendListDTO pDTO = new friendListDTO();
		pDTO.setUser_email(user_email);
		
		List<friendListDTO> rList = new ArrayList<friendListDTO>();
		rList = kakaoFriendMapper.getFriendInfo(pDTO);
		
		String data = rList
				.stream()
				.map(d -> d.getFriend_uuid())
				.reduce((e, d) -> e + "," + d).toString();

		String [] dataArrays = data
				.replaceAll("\\[", "")
				.replaceAll("\\]", "")
				.substring(8) // => 앞에 사용하지 않는 문자열 제외
				.split(",");
		log.info("....UUID_Arrays : " + Arrays.toString(dataArrays));
		JSONArray receiver_uuids = new JSONArray();
		
		for(String e : dataArrays) {
			receiver_uuids.put(e);
		}

		//Stream<String> st = (Stream<String>)receiver_uuids;
		//st.forEach(e -> log.info("....receiver_uuids : " + e));
		
		int count = 0; // => 5개씩 끊을 변수
		//----------------------------------------------------------------------------------------------------------------------
		//-----------------------------------link 키 value는 웹과 모바일로 나누어져 있어 맵에다 담았음---------------------------------------
		HashMap<String, String> linkMap = new HashMap<String, String>();
		linkMap.put("web_url", web_link);
		linkMap.put("mobile_web_url", mobile_web_link);
		// --------------------------------------제이슨 형식의 데이터 생성을 위한 작업--------------------------------------------
		JSONObject template_object = new JSONObject();
		template_object.put("object_type", "text");
		template_object.put("text", testMessage);
		template_object.put("link", linkMap);
		template_object.put("button_title", "사이트이동[null]");
		// ----------------------------------------------접속을 위한 객체선언부----------------------------------------------
		URL url = null;
		HttpURLConnection conn = null;
		try {
			url = new URL(SEND_TALK_FRIEND_URL); // 카카오 접속 URL
			conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			// --------------------------------------요청에 필요한 Header에 포함될 내용 헤더에포함----------------------------
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
			// ---------------------------------------------------데이터를 보내는작업부분------------------------------------
			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); // json 버퍼스트림으로 넣기
			StringBuilder sb = new StringBuilder();
			sb.append("&receiver_uuids=");
			sb.append(receiver_uuids); // => 친구 UUID 가져와서 5명씩 문자열 배열로
			sb.append("&template_object=");
			sb.append(template_object);
			log.info("....URL : " + sb.toString());
			bw.write(sb.toString()); // 형변환하여 쓰기
			bw.flush(); // 결과물 버퍼에서 보내기
			bw.close(); // 리소스 닫기
			// --------------------------------------------------응답코드 받는곳-----------------------------------------------------
			int responseCode = conn.getResponseCode();
			log.info("responseCode : " + responseCode);
			// ------------------------------------------------------------------------------------------------------------------
		} catch (IOException e) {
			log.info(e);
		} finally {
			log.info(this.getClass().getName() + "...send My Kakao Talk End");
		}
		log.info(this.getClass().getName() + "...send Message Fot My Friend End");
		return res;
	}
	

}