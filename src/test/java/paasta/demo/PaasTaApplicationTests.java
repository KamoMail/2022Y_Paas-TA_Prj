package paasta.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import paasta.demo.dto.redis.redisSchema;
import paasta.demo.persistance.mapper.redis.IRedisRepository;
import paasta.demo.util.kakaoService.IKakaoFriendListReq;
import paasta.demo.util.kakaoService.impl.KakaoFriendListReq;

@SpringBootTest
class PaasTaApplicationTests {
	static String RquestFreindListURL = "";
	static String accessTokenTest = "";
	
	@Autowired
	private IRedisRepository redis;
	
	@Test
    void 레디스테스트() throws IOException{
		// given
		redisSchema rs = new redisSchema();
		// when 
		rs.setName("kim");
		rs.setTestData("testData2");
		redis.save(rs);
		Optional<redisSchema> res = redis.findById("test2");
    	// then
		//assertThat(rs, "친구목록 불러오기 성공");
    }
	
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
