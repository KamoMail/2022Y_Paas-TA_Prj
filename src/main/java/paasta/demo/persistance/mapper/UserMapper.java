package paasta.demo.persistance.mapper;


import org.apache.ibatis.annotations.Mapper;

import paasta.demo.dto.UserDTO;

@Mapper
public interface UserMapper {

	UserDTO getUserInfo(UserDTO uDTO); // => 사용자 정보 가져오기
	
	UserDTO getUserInfoKakao(UserDTO uDTO); // => 이메일로 사용자 정보 가져오기
	
}
