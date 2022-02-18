package paasta.demo.persistance.mapper.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import paasta.demo.dto.mongo.mongoSchema;

// 몽고 JPA 사용을 위한 인터페이스 생성, JPA를 쓰려면 MongoRepository를 상속받은 인터페이스가 있어야 한다.
// MongoRepository<T, ID> T는 타입 ID는 PK값 타입 
@Repository
public interface IMongoRepository extends MongoRepository<mongoSchema, String>{

	List<mongoSchema> findByName(String name); // => SQL로 치면 select
	
}
