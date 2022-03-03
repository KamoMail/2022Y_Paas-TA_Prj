package paasta.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.gson.JsonArray;

import paasta.demo.dto.MailDTO;
import paasta.demo.dto.redis.redisSchema;
import paasta.demo.persistance.mapper.redis.IRedisRepository;
import paasta.demo.service.IKakaoFriendListReqService;
import paasta.demo.service.IKakaoMessageService;
import paasta.demo.service.IMailService;
import paasta.demo.service.impl.MailService;

@SpringBootTest
class PaasTaApplicationTests {
	
	@Autowired
	private IRedisRepository redis;
	
	@Autowired
	private IKakaoFriendListReqService kakaoFriendListReq;
	
	@Autowired
	private IKakaoMessageService KakaoMessageService;
	
	//@Test
	void 메일테스트() throws Exception{
		// given
		IMailService ms = new MailService();
		MailDTO pDTO = new MailDTO();
		pDTO.setTitle("test");
		pDTO.setToMail(" ");
		pDTO.setContents("testMail 입니다.");
		// when
		int res = ms.doSendMail("{testAdminEmail}", "{testPwd}", pDTO);
		// then
		assertEquals(res, 1);
	}
	//@Test
    void 레디스테스트() throws IOException{ 
		// given
		redisSchema rs = new redisSchema();
		// when 
		rs.setName("kim");
		rs.setTestData("testData2");
		redis.save(rs);
		Optional<redisSchema> res = redis.findById("test2");
    	// then
		System.out.println("res : " + res);
		assertEquals(res, "친구목록 불러오기 성공");
    }
	//@Test
    void 친구목록가쟈와저장() throws Exception{
		// given
		String accessTokenTest = "lsJbG1vf5mBRBnHPLjbHw5jsI5uht_dh-YXxHwo9dRsAAAF_SDsKXA";
		String user_email = "me940728@naver.com";
		// when 
		JsonArray res = kakaoFriendListReq.requestFriendList(accessTokenTest, 5);
		int result = kakaoFriendListReq.insertFriendList(res, user_email);
    	// then
		assertEquals(result, 1);
    }
	@Test
	void 메시지전송() throws Exception{
		// given
		String user_email = "me940728@naver.com";
		String accessTokenTest = "lsJbG1vf5mBRBnHPLjbHw5jsI5uht_dh-YXxHwo9dRsAAAF_SDsKXA";
		// when
		String res = KakaoMessageService.sendFriendKakaoTalk(accessTokenTest, user_email);
		// then
		assertEquals(res, "이상무");
	}

}
