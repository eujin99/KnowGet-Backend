// package com.knowget.knowgetbackend.domain.user.service;
//
// import java.util.Random;
//
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.knowget.knowgetbackend.global.config.redis.RedisUtil;
//
// import jakarta.mail.MessagingException;
// import jakarta.mail.internet.MimeMessage;
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class MailService {
//
// 	private final JavaMailSender javaMailSender;
// 	private final RedisUtil redisUtil;
// 	private static String authCode;
//
// 	/**
// 	 * 인증코드 생성 및 이메일 전송
// 	 *
// 	 * @param emailTo 인증메일을 보낼 이메일 주소
// 	 * @author Jihwan
// 	 */
// 	@Transactional
// 	public void setMail(String emailTo) {
// 		createAuthCode();
// 		String emailFrom = "knowget.official@gmail.com";
// 		String title = "본인확인 인증 이메일 입니다.";
// 		String body = "";
// 		body += "<div style='margin:100px;'>";
// 		body += "<h1> 안녕하세요 너겟 입니다! </h1>";
// 		body += "<br>";
// 		body += "<p>아래 코드를 정확하게 입력해주세요<p>";
// 		body += "<br>";
// 		body += "<p>감사합니다!<p>";
// 		body += "<br>";
// 		body += "<div align='center' style='border:1px solid black; font-family:verdana';>";
// 		body += "<h3 style='color:blue;'>요청하신 인증 코드입니다.</h3>";
// 		body += "<div style='font-size:130%'>";
// 		body += "CODE : <strong>";
// 		body += authCode + "</strong><div><br/> ";
// 		body += "</div>";
// 		sendMail(emailFrom, emailTo, title, body);
// 	}
//
// 	/**
// 	 * 임의의 8자리 인증코드 생성
// 	 *
// 	 * @author Jihwan
// 	 */
// 	public static void createAuthCode() {
// 		StringBuilder key = new StringBuilder();
// 		Random rand = new Random();
//
// 		for (int i = 0; i < 8; i++) { // 인증코드 8자리
// 			int index = rand.nextInt(3); // 0~2 까지 랜덤
//
// 			switch (index) {
// 				case 0:
// 					key.append((char)(rand.nextInt(26) + 97));
// 					//  a ~ z (e.g., 1 + 97 = 98 => (char)98 = 'b')
// 					break;
// 				case 1:
// 					key.append((char)(rand.nextInt(26) + 65));
// 					//  A ~ Z (e.g., 1 + 65 = 66 => (char)66 = 'B')
// 					break;
// 				case 2:
// 					key.append((rand.nextInt(10)));
// 					// 0 ~ 9
// 					break;
// 			}
// 		}
//
// 		authCode = key.toString();
// 	}
//
// 	/**
// 	 * 설정해둔 세부사항 정보를 가지고 인증메일을 전송
// 	 *
// 	 * @param emailFrom 보내는 이 이메일 주소
// 	 * @param emailTo   받는 이 이메일 주소
// 	 * @param title     메일 제목
// 	 * @param body      메일 내용
// 	 * @author Jihwan
// 	 */
// 	@Transactional
// 	public void sendMail(String emailFrom, String emailTo, String title, String body) {
// 		MimeMessage message = javaMailSender.createMimeMessage();
// 		try {
// 			MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
// 			helper.setFrom(emailFrom);
// 			helper.setTo(emailTo);
// 			helper.setSubject(title);
// 			helper.setText(body, true);
// 			javaMailSender.send(message);
// 		} catch (MessagingException e) {
// 			e.printStackTrace();
// 		}
// 		redisUtil.setDataExpire(authCode, emailTo, 60 * 5L);
// 	}
//
// 	/**
// 	 * 사용자가 입력한 인증코드가 전송된 인증코드와 일치하는지 확인
// 	 *
// 	 * @param email    사용자가 인증번호를 받은 이메일 주소
// 	 * @param authCode 사용자가 해당 이메일로 받아서 입력한 인증코드
// 	 * @return 사용자가 입력한 인증코드와 전송됐었던 인증코드가 일치하는지 여부
// 	 * @author Jihwan
// 	 */
// 	@Transactional(readOnly = true)
// 	public boolean checkAuthCode(String email, String authCode) {
// 		if (redisUtil.getData(authCode) == null) {
// 			return false;
// 		} else {
// 			return redisUtil.getData(authCode).equals(email);
// 		}
// 	}
//
// }
