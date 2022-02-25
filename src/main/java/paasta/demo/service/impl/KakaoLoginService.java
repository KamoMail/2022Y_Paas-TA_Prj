package paasta.demo.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import paasta.demo.service.IKakaoLoginService;
import paasta.demo.service.comm.IKakaoInfoService;
import paasta.demo.service.comm.KakaoServiceLog;

/* @Auth 최별규
 * @Version 1.0
 * 카카오 로그인을 위한 기능을 모아놓은 클래스 입니다.
 * ____________________________________________________________________________________
 * |   작성일     |   작성자    |                          내용                        |
 * |------------------------------------------------------------------------------------
 * | 2022.02.12 |  최별규     |  초안 작성
 * 카카오 로그인 순서 :  OAUTH2(인증코드 -> 접근 토큰 -> 사용자 정보 가져오기 -> 로그인)
 * */
@Service("KakaoLoginService")
public class KakaoLoginService extends KakaoServiceLog implements IKakaoInfoService, IKakaoLoginService{
	// => 카카오 서버로부터 인증코드를 발급받는 메서드
	@Override
	public String getAuthCode() {
		log.info(this.getClass().getName() + "...getAuthCode For Kakao Server Start");
		String authCode = SampleRequest + RESTAPI_KEY + "&redirect_uri=" + Redirect_URI + "&response_type=code"; // 최종으로 카카오로 보낼 주소
		log.info(this.getClass().getName() + "...getAuthCode For Kakao Server End");
		return authCode;
	}
	// => 인증코드를 활용하여 엑세스토큰을 발급하는 메서드
	@Override
	public String getAccessToken(String authCode) {
		log.info(this.getClass().getName() + "...getAccessToken Start For KakaoServer");
		String accessToken = "";
		try {
			log.info(this.getClass().getName() + "...getAccessToken Try");
			URL url = new URL(AccessTokenReqURL); // URL로 보내야 하니깐 URL 객체 생성
			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // HttpURLConnection은 혼자 갹체를 생성하여 사용하지 못하고, 반드시 url 객체로 생성된 값을 
			conn.setRequestMethod("POST");									   // 리턴 받아 형변환 하여 사용하여야 한다.
			conn.setDoOutput(true);											   // 요청 방식은 post, url 연결을 출력용으로 사용하기 때문에 true 그렇지 않으면 false
			
            // buffer 스트림 객체 값 셋팅 후 요청 버퍼에 적제
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder(); // 스트링끼지 합치는 연산은 객체를 합쳐서 새로만드는 연산을 함 빌더는 기존 객체에 붙히는 연산으로 부하 적음 
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=" + RESTAPI_KEY);  //앱 KEY VALUE
            sb.append("&redirect_uri=" + Redirect_URI); // 앱 CALLBACK 경로
            sb.append("&code=" + authCode);
            bw.write(sb.toString()); // 스트링 빌더로 데이터를 한곳에 적재 한 후
            log.info("보내기 : " + sb.toString());
            bw.flush(); // 버퍼에 담긴 값들을 실제로 넘김(버퍼를 비움)
            
            //  RETURN 값 result 변수에 저장
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream())); // 리턴 한 값 받음
            String br_line = ""; 
            String result = "";

            while ((br_line = br.readLine()) != null) { // 데이터가 null 이 아닐때 까지 계속해서 데이터를 불러오고
                result += br_line;
            }
            
            // 데이터를 다 불러 왔으면 
            JsonParser parser = new JsonParser(); // 제이슨을 파싱하여 오브젝트로 변환해준다. => 문자열 쪼개기로 하면 되지 않겠나? 데이터가 적으면 상관 없지만 많다면?
            log.info("parser : " + parser);
            JsonElement element = parser.parse(result); // (제이슨오브젝트 상속 받음)넘어온 데이터에서 제이슨 파싱을 한 후 값을 넣어준다. 
            log.info("Element : " + element);
     
            // 토큰 값 저장 및 리턴
            accessToken = element.getAsJsonObject().get("access_token").getAsString(); // 
            //refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
            br.close();
            
            log.info(this.getClass().getName() + "...getAccessToken Try ----> END");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
			log.info("토큰 발급 종료");
		}
		log.info(this.getClass().getName() + "...getAccessToken End For KakaoServer");
		return accessToken;
	}
	// => 엑세스 토큰으로 유저 정보 가져오기
	@Override
	public Map<String, Object> getUserInfo(String accessToken) {
		log.info(this.getClass().getName() + "...getUserInfo For KakaoServer Start");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		try {
			URL url = new URL(UserInfoReqURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			//요청에 필요한 Header에 포함될 내용
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			
			int responseCode = conn.getResponseCode();

			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String br_line = "";
			String result = "";

			while ((br_line = br.readLine()) != null) {
				result += br_line;
			}

			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(result);

			// JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject(); // 카카오 설명서 참고
			// String nickname = properties.getAsJsonObject().get("nickname").getAsString();
			// String profile_image = properties.getAsJsonObject().get("profile_image").getAsString();
			JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
			String kakaoEmail = kakaoAccount.getAsJsonObject().get("email").getAsString();

			resultMap.put("kakaoEmail", kakaoEmail);
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			log.info(this.getClass().getName() + "...getUserInfo For KakaoServer End");
		}
		return resultMap;
	}
	
	

}