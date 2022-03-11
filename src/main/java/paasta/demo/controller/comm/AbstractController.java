package paasta.demo.controller.comm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author 최별규
 * @version 1.0 로그를 사용하기 위한 추상클래스
 * */
public abstract class AbstractController {

	// <Log4J> 로그 파일 생성 및 로그 출력을 위한 log4j 프레임워크의 자바 객체
	protected Logger log = LogManager.getLogger("MySpringPRJ Controller");

}
