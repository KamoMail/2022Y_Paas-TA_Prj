package paasta.demo.persistance.mapper.redis;

import org.springframework.data.repository.CrudRepository;

import paasta.demo.dto.redis.redisSchema;

// 매개변수 타입으로는 redisSchema를 받고 키 타입은 스츠링이다.
public interface IRedisRepository extends CrudRepository<redisSchema, String>{
	/* 해당 클래스를 상속 받으면 save를 통해 저장을 하고,
	 * findById를 하면 조회를
	 * count는 키의 갯수
	 * delete는 삭제를 한다.
	 * 물론 오버라이딩도 가능하다.
	*/ 
}
