package com.js.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {
	// to : 받는 사람
	// subject : 제목
	// content : 내용
	public static void sendMail(String to, String subject, String content) {
		
		// [1] 이메일 관련 전역 변수 설정
		String host = "smtp.naver.com";					// ★ SMTP 서버명 작성
		String port = "587";							// ★ POP3/SMTP 465  IMAP/SMTP 587
		final String id = "__";							// ★ 송신자이메일주소
		final String pw = "__";							// ★ 2차인증 O ( 앱비밀번호 )
		
		// [2-1] 이메일 환경 설정 ( 공통 )
		Properties props = new Properties();
		props.put("mail.smtp.host", host);						// SMTP 호스트
		props.put("mail.smtp.port", port);						// SMTP 포트
		props.put("mail.smtp.auth", "true");					// 인증 허용
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");		// SSL/TLS 버전 호환 설정
		props.put("mail.smtp.ssl.enable", "false");				// 자동적으로 보안 채널을 생성하여 메일을 전송 [ SSL/TLS ]
		props.put("mail.smtp.ssl.trust", host);					// 인증 서 관련 오류 발생
		props.put("mail.debug", "true");						// 디버그 활성화 여부
		props.put("mail.smtp.socketFactory.fallback", "false"); // 도메인 이름을 SSL 속성 변경함
		
		// [2-2] 이메일 환경 설정 ( 465 인 경우 SSL )
		if("465".equals(port)) {
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");	// SSL FACTORY CLASS
			props.put("mail.smtp.socketFactory.port", "465");
			
			System.out.println("[system.out] 1-1");
			
		// [2-3] 이메일 환경 설정 ( 587 인 경우 TLS )
		}else if("587".equals(port)) {
			// starttls 역할 : javamail 에 Tls 모드를 시작하라고 명시적으로 요청 → starttls 확인전까지 평문으로 보냄 → starttls true 이면 보안 관련채널 생성
			props.put("mail.smtp.starttls.enable", "true");			// 보안 관련 채널 생성해서 인증서 확인 등의 작업 [ Starttls ]
			props.put("mail.smtp.socketFactory.port", "587");
			
			System.out.println("[system.out] 1-2");
		}
		
		try {
			// [3] 로그인 실시
			Session session = Session.getDefaultInstance(props, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(id, pw);
				}
			});
			
			System.out.println("[system.out] 2");
			
			// [4] 메시지 내용 보내기 설정
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(id));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			System.out.println("[system.out] 3");
			
			message.setSubject(subject);	// ★ 메일제목
			message.setContent(content, "text/html; charset=utf-8");	// ★ 메일내용
			
			// [5] 메세지 발송 프로세스
			message.setSentDate(new java.util.Date());
			System.out.println("[system.out] 4");
			Transport.send(message);
			System.out.println("[system.out] 메일 발송 성공");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
