package paasta.demo.persistance.mapper.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import paasta.demo.dto.mongo.friendListDTO;

/*
 * @author 최별규
 * @version 1.1 친구 목록 저장 레포지토리
 * */

@Repository
public interface IFriendListRepository extends MongoRepository<friendListDTO, String>{

	List<friendListDTO> findByName(String uuid); // UUID로 사용자 조회
	
}
