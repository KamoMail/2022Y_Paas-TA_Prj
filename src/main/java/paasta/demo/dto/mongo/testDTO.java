package paasta.demo.dto.mongo;

/**
 * @author 최별규
 * @version 1. 몽고 친구 저장 스키마(DTO)
 */
public class testDTO {
	
	public String getKid() {
		return kid;
	}

	public void setKid(String kid) {
		this.kid = kid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getExists_yn() {
		return exists_yn;
	}

	public void setExists_yn(String exists_yn) {
		this.exists_yn = exists_yn;
	}

	private String kid; // => 카카오에서 부여한 사용자 id 값
	
	private String name; //=> 카카오 사용자 이름(닉네임)
	
	private String uuid; //=> 사용자 고유 ID, 나중에 메시지 발송시 활용
	
	private String exists_yn; //=> uuid 중복확인 을 위한 변수 Y, N으로 리턴 된다.
	
}
