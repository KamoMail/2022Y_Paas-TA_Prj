package paasta.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import paasta.demo.util.kakaoService.IKakaoFriendListReq;
import paasta.demo.util.kakaoService.impl.KakaoFriendListReq;

@SpringBootTest
class PaasTaApplicationTests {
	static String RquestFreindListURL = "";
	static String accessTokenTest = "";
    
	@Test
    void 왜안될까() throws IOException{
		// given
		IKakaoFriendListReq kf = new KakaoFriendListReq();
		// when 
		String res = kf.requestFriendList(accessTokenTest, 5);
    	// then
		assertEquals(res, "친구목록 불러오기 성공");
    }

}
