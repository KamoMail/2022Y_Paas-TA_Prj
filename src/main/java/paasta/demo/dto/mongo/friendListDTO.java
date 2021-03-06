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
	
	// 테이블에 있는 컬럼이름이랑 같아야 한다. 보통 테이블은 대문자 DTO는 소문자로 입력한다.(헷갈리지 않게 하지만 스펠링은 같다)
	@Id
	private String id; //=> 몽고 아이디
	
	private String user_email; // => 친구를 등록하는 사용자 이메일
	
	private String friend_kid; // => 카카오에서 부여한 사용자 id 값
	
	private String friend_name; //=> 카카오 사용자 이름(닉네임)
	
	private String friend_uuid; //=> 사용자 고유 ID, 나중에 메시지 발송시 활용
	
	private String exists_yn; //=> uuid 중복확인 을 위한 변수 Y, N으로 리턴 된다.
	
}
