package paasta.demo.dto.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/**
 * @author 최별규
 * @version 1. 몽고 친구 저장 스키마(DTO)
 */
@Document(collection = "friendList") // 컬렉션 이름
@Data //=> 게터 세터와 toString 을 할 수 있는 롬복
public class friendListDTO {
	
	@Id
	private String id; //=> 몽고 아이디
	
	private String kid; // => 카카오에서 부여한 사용자 id 값
	
	private String name; //=> 카카오 사용자 이름(닉네임)
	
	private String uuid; //=> 사용자 고유 ID, 나중에 메시지 발송시 활용
	
}
