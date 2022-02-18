package paasta.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import paasta.demo.controller.comm.AbstractController;
import paasta.demo.dto.mongo.mongoSchema;
import paasta.demo.persistance.mapper.mongo.IMongoRepository;

@RestController // => JSON 형태로 나오는 몽고 리턴을 확인하기 위한 비동기 컨트롤러
public class MongoTestController extends AbstractController{
	
	@Autowired
	private final IMongoRepository mongoRepository = null; // => JPA 사용을 위한 선언
	
	// 인서트 테스트용
	@GetMapping("/mongoTest")
	public String mongoTest() {
		log.info(this.getClass().getName() + ".testMongoStart");
		mongoSchema mongoDb = new mongoSchema();
		mongoDb.setName("choi"); // 데이터 삽입
		log.info("name : " + mongoDb.getName());
		
		mongoDb.setData("testData"); // 데이터 삽입
		log.info("data : " + mongoDb.getData());
		
		log.info(mongoRepository.save(mongoDb).getId());
		log.info(this.getClass().getName() + ".testMongoEnd");
		return "";
	}
	// 셀렉트 테스트용
	@GetMapping("/mongoTestSelect")
	public List<mongoSchema> mongoSelect() {
		mongoSchema mongoDb = new mongoSchema();
		mongoDb.setName("choi"); // 데이터 삽입
		String name = mongoDb.getName();
		return mongoRepository.findByName(name);
	}

}
