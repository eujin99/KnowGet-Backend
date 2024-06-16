// package com.knowget.knowgetbackend.domain.user.controller;// package com.knowget.knowget.domain.user.controller;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
// import com.knowget.knowgetbackend.domain.user.dto.EmailCheckDTO;
// import com.knowget.knowgetbackend.domain.user.dto.EmailRequestDTO;
// import com.knowget.knowgetbackend.domain.user.service.MailService;
//
// import jakarta.validation.Valid;
// import lombok.RequiredArgsConstructor;
//
// @RestController
// @RequestMapping("/auth")
// @RequiredArgsConstructor
// public class MailController {
//
// 	private final MailService mailService;
//
// 	/**
// 	 * 사용자로부터 입력받은 이메일 주소로 인증코드를 전송
// 	 *
// 	 * @param emailRequestDTO 사용자가 입력한 이메일 주소
// 	 * @return ResponseEntity<Void>
// 	 * @author Jihwan
// 	 */
// 	@PostMapping("/send")
// 	public ResponseEntity<Void> sendMail(@RequestBody @Valid EmailRequestDTO emailRequestDTO) {
// 		mailService.setMail(emailRequestDTO.getEmail());
// 		return new ResponseEntity<>(HttpStatus.OK);
// 	}
//
// 	/**
// 	 * 사용자로부터 인증번호를 입력받아, 전송된 인증번호와 일치한지 확인
// 	 *
// 	 * @param emailCheckDTO 사용자가 입력한 이메일 주소와 8자리 인증코드
// 	 * @return 인증 성공여부에 따른 결과 메시지
// 	 * @author Jihwan
// 	 */
// 	@PostMapping("/check")
// 	public ResponseEntity<Boolean> checkAuthCode(@RequestBody @Valid EmailCheckDTO emailCheckDTO) {
// 		boolean result = mailService.checkAuthCode(emailCheckDTO.getEmail(), emailCheckDTO.getAuthCode());
// 		return new ResponseEntity<>(result, HttpStatus.OK);
// 	}
//
// }
