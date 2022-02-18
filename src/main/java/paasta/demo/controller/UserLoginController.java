package paasta.demo.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import paasta.demo.controller.comm.CommController;
import paasta.demo.dto.UserDTO;
import paasta.demo.service.IUserInfoService;
import paasta.demo.util.CmmUtil;
import paasta.demo.util.kakaoService.IKakaoFriendListReq;
import paasta.demo.util.kakaoService.IKakaoLogin;

@Controller
public class UserLoginController extends CommController{
	
	@Resource(name = "UserService")
	private IUserInfoService userService;
	
	@Resource(name = "KakaoLoginService")
	private IKakaoLogin kakao;
	
	@Resource(name = "KakaoFriendListReq")
	private IKakaoFriendListReq kakaoFriend;
	
	//=> 로그인 페이지 리턴
	@GetMapping(value = "/loginPage")
	public String loginPage() throws Exception{
		log.info(this.getClass().getName() + ".Logim Page load Start");
		log.info(this.getClass().getName() + ".Logim Page load End");
		return "/user/UserLoginPage";
	}
	//=> 로그인 처리 프로세스 POST
	@PostMapping(value = "/loginPage/loginProc")
	public String loginProc(HttpServletRequest request, ModelMap model) throws Exception{
		log.info(this.getClass().getName() + ".Logim Process Start");
		
		String user_id = CmmUtil.nvl(request.getParameter("user_id"));
		log.info("user_id : " + user_id);
		
		String password = CmmUtil.nvl(request.getParameter("password"));;
		log.info("password : " + password);
		
		int res = userService.getUserInfo(user_id, password);
		log.info("Login Sucess ? : " + res);
		
		String strRes = resLoginCheck(res, model);
		log.info("redirecting : " + strRes);

		log.info(this.getClass().getName() + ".Logim Process End");
		return "/redirect";
	}
	// => 카카오 로그인 인증코드 발급화면 매핑
	@RequestMapping(value="/kakaoGetAuthToken")
	public String kakaoLoginProc(ModelMap model) throws Exception {
		log.info(this.getClass().getName() + "kakaoLogin Start!");
		
		String forKakao = kakao.getAuthCode(); // 카카오 접속을 위한 실행
		log.info(forKakao);
		
		String msg = "카카오 로그인 시도";
		String url = forKakao;
		
		model.addAttribute("msg", msg);
		model.addAttribute("url", url); // 완성된 호출 url을 jsp에서 처리하도록 리다이렉팅
		
		log.info(this.getClass().getName() + ".kakaoLogin End");
		return "/redirect";
	}
	// => 카카오 로그인 후 엑세스 토큰 발급
	@GetMapping(value = "/kakaoLogin", produces = "apllication/json")
	public String kakaoAuthCodeProc(@RequestParam("code") String code, HttpServletRequest request,
			HttpServletResponse response, ModelMap model, HttpSession session) throws Exception {
		log.info(this.getClass().getName() + "kakaoAuthCodeProc Start!!!");
		String kakaoToken = kakao.getAccessToken(code);
		log.info("엑세스 토큰 : " + kakaoToken);
		
		Map<String, Object> result = kakao.getUserInfo(kakaoToken);
		String kakaoEmail = result.get("kakaoEmail").toString();
		log.info("카카오 이메일 : " + kakaoEmail);

		UserDTO pDTO = new UserDTO(); // 값 전달 용
		UserDTO rDTO = new UserDTO(); // 값 받아오기 용

		pDTO.setUser_email(kakaoEmail);
		rDTO = userService.kakaoLoginProc(pDTO); // userService에서 MAPPER를 연결하여 값 받아옴
		
		String strRes = userDTOCheck(rDTO, kakaoToken, session, model);
		log.info("성공유무 : " + strRes);
		log.info(this.getClass().getName() + "kakaoAuthCodeProc End");
		return "/redirect";
	}
	//=> 카카오 친구목록 불러오기 Test
	@GetMapping(value = "/kakaoFriendList")
	public String kakaoFriendListRequest(HttpServletRequest request, HttpSession session) throws Exception{
		log.info(this.getClass().getName() + "kakao Request Friend List Start");
		String accessToken = session.getAttribute("kakaoToken").toString();
		log.info(kakaoFriend.requestFriendList(accessToken, 5));
		log.info(this.getClass().getName() + "kakao Request Friend List End");
		return "";
	}

}
