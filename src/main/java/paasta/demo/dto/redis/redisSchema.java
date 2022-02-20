package paasta.demo.dto.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

/* 레디스 사용을 위한 DTO 생성
 * */
@Data
@RedisHash(value = "test2", timeToLive = 30) // => value는 키 값을 의미 timeToLive는 만료 시간을 의미
public class redisSchema {
	
	@Id
	private String id;
	
	private String name;
	
	private String testData;
}
