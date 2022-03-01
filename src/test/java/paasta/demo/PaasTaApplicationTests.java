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
import paasta.demo.service.IMailService;
import paasta.demo.service.impl.MailService;

@SpringBootTest
class PaasTaApplicationTests {
	
	@Autowired
	private IRedisRepository redis;
	
	@Autowired
	private IKakaoFriendListReqService kakaoFriendListReq;
	
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
	// 버퍼 사용하면 안되는 이유 찾기
	@Test
    void 메시지보내기() throws Exception{
		// given
		String accessTokenTest = "hRsV";
		String user_email = "me94om";
		// when 
		JsonArray res = kakaoFriendListReq.requestFriendList(accessTokenTest, 5);
		int result = kakaoFriendListReq.insertFriendList(res, user_email);
    	// then
		assertEquals(result, 1);
    }
	
	//@Test
	void 매퍼테스트() throws Exception{
		
	}

}
