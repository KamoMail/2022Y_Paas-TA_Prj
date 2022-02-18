package paasta.demo.dto.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/**
 * @author 최별규
 * @version 1. 몽고 테스트용 스키마(DTO)
 */
// DTO랑 같은 것 테스트 스키마
@Document(collection = "test") // 컬렉션 이름
@Data //=> 게터 세터와 toString 을 할 수 있는 롬복
public class mongoSchema {
	
	@Id
	private String id; //=> 몽고 아이디
	
	private String name; //=> 작성자 이름
	
	private String data; //=> 내용
}
