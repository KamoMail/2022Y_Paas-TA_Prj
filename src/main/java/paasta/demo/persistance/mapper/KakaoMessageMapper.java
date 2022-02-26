package paasta.demo.persistance.mapper;


import org.apache.ibatis.annotations.Mapper;

import paasta.demo.dto.mongo.friendListDTO;

@Mapper
public interface KakaoMessageMapper {

	friendListDTO getUuidExists(friendListDTO pDTO) throws Exception; //=> UUID 중복을 확인 하는 메서드
	
	int insertFriendList(friendListDTO pDTO) throws Exception; // => 중복이 없다면 친구 정보를 저장하는 메서드
	
}
