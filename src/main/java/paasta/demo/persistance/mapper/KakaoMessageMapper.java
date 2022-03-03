package paasta.demo.persistance.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import paasta.demo.dto.mongo.friendListDTO;

@Mapper
public interface KakaoMessageMapper {
	
	friendListDTO getUuidExists(friendListDTO pDTO) throws Exception; //=> UUID 중복을 확인 하는 메서드
	
	int insertFriendList(friendListDTO pDTO) throws Exception; // => 중복이 없다면 친구 정보를 저장하는 메서드
	
	List<friendListDTO> getFriendInfo(friendListDTO pDTO) throws Exception; // => 친구 정보 가져오는 메서드
	
}
