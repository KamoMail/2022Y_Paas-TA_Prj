package paasta.demo.dto.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;

/* 레디스 사용을 위한 DTO 생성
 * @RedisHash의 의미?
- 해당 클래스의 엔티티들이 수많이 저장될 텐데, 이들만을 보관하는 Hash Key를 설정.
- 이 해쉬 공간 속에서 각 엔티티들이 hash_id (@id에 맵핑되는 값)을 가지게 된다.
- 결과적으로 삼중 해쉬맵을 사용한다고 이해하면 된다.
- HashMap<String, HashMap<String, Account>>로 이해
- 첫번째 String은 @RedisHash의 이름
- 두번째 String은 hash_id
- Account는 key:value 형태로 값을 저장하고 있으며, getter로 value를 가져올 수 있음
- (필드 이름이 key가 된다)
 * */
@Data
@RedisHash(value = "test2") // => 레디스 사용하려면 해당 어노테이션 사용 key는 test2 여기서 인자 값으로 시간을 설정할 수 있다.
public class redisSchema {
	
	@Id
	private String id;
	
	private String name;
	
	private String testData;
}
