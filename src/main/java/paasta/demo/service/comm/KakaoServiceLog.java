package paasta.demo.service.comm;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// 카카로 로그을 위한 추상클래스
public abstract class KakaoServiceLog {

	// 카카오 관련된 로그를 찍기위한 로그 객체 생성
	protected Logger log = LogManager.getLogger("KakaoService Start");

}
