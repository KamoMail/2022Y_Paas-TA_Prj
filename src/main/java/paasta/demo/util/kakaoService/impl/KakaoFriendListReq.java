package paasta.demo.util.kakaoService.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import paasta.demo.util.kakaoService.IKakaoFriendListReq;
import paasta.demo.util.kakaoService.comm.IKakaoInfo;
import paasta.demo.util.kakaoService.comm.KakaoServiceLog;

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
public class KakaoFriendListReq extends KakaoServiceLog implements IKakaoFriendListReq, IKakaoInfo{

	// => 엑세스 토큰으로 사용자의 친구 목록을 가져오는 메서드
	@Override
	public String requestFriendList(String accessToken, int friendNum) throws IOException{
		log.info(this.getClass().getName() + "...request Kakao Freind List Start");
		
		String returnMessageLog = "친구목록 불러오기 성공";
		String inPutAccessToken = accessToken; 
		URL url = null; 
		HttpURLConnection conn = null;

		try {
			url = new URL(RquestFreindListURL); // 카카오서버 접속 URL 카카오 서버 주소
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			//--------------------------------------요청에 필요한 Header에 포함될 내용 헤더에 포함----------------------------
			log.info("...accessTokem : " + inPutAccessToken); 
			conn.setRequestProperty("Authorization", "Bearer " + inPutAccessToken);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			//---------------------------------------------------데이터를 보내는 작업부분------------------------------------
			conn.setDoOutput(true);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream())); 
			StringBuilder sb = new StringBuilder();			
			sb.append("&limit=" + friendNum); // => 즐겨찾기에 추가된 친구를 불러온다.
			bw.write(sb.toString());
			bw.flush(); // 결과물 버퍼에서 보내기
			bw.close(); // 리소스 닫기
			//--------------------------------------------응답코드 받는 곳-----------------------------------------------------
			int responseCode = conn.getResponseCode();
			log.info("...responseCode : " + responseCode); // => 2xx면 성공, 4xx면 실패
			if(responseCode == 403) {
				returnMessageLog = "친구목록 불러오기 실패....";
			}
			//------------------------------------------------------------------------------------------------------------
		} catch(IOException e) {
			returnMessageLog = "친구목록 불러오기 실패....";
			log.info("Error : " + e.toString());
		} finally {
			log.info(this.getClass().getName() + "...Req Process End");
		}
		log.info(this.getClass().getName() + "...request Kakao Freind List End");
		return returnMessageLog;
	}
	

}
