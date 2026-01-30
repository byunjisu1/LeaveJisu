package com.js.websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.service.MemberService;
import com.js.service.MyTripService;
import com.js.service.TripInfoService;

@Component
public class BroadSocket extends TextWebSocketHandler {
	@Autowired
	private MemberService mSvc;		// 회원 정보 서비스
	@Autowired
	private MyTripService mtSvc;	// 내 여행 서비스
	@Autowired
	private TripInfoService tiSvc;	// 여행 정보 서비스
	
	public static Set<WebSocketSession> clients = Collections.synchronizedSet(new HashSet<>());
	
	// 클라이언트 접속 시
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		clients.add(session);
		
		Map<String, Object> mapAttributes = session.getAttributes();
		System.out.println("웹소켓 새로운 접속자 로그인아이디 = " + mapAttributes.get("loginId") + "(현재 접속자 수 : " + clients.size() + ")");
	}
	
	// 클라이언트로부터 메세지 도착 시
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		
		Map<String, Object> data = objectMapper.readValue(message.getPayload(), Map.class);
		System.out.println("BroadSocket) 받은 데이터 : " + data);
		
		String nickname = mSvc.getNicknameByMemberId((String)data.get("id"));
	
		String targetMemberId = "";
		int bellIdx = -1;
		
		if(((String)data.get("type")).equals("P")) {	// 장소 리뷰
			targetMemberId = mtSvc.getWriterIdByReviewIdx((int)data.get("reviewIdx"));	// 리뷰 작성자 id
			bellIdx = mtSvc.insertMyBell((Integer)data.get("reviewIdx"), null, (String)data.get("id"), targetMemberId);
		} else {	// 도시 리뷰
			targetMemberId = tiSvc.getWriterIdByIdx((int)data.get("reviewIdx"));
			bellIdx = mtSvc.insertMyBell(null, (Integer)data.get("reviewIdx"), (String)data.get("id"), targetMemberId);
		}
		
		Map<String, Object> response = Map.of(
			"nickname", nickname,
			"type", data.get("type"),
			"bellIdx", bellIdx,
			"reviewIdx", data.get("reviewIdx")
        );
		String jsonResponse = objectMapper.writeValueAsString(response);
		for(WebSocketSession client : clients) {
			String connectMemberId = (String)client.getAttributes().get("loginId");
			if(targetMemberId.equals(connectMemberId)) {
		        client.sendMessage(new TextMessage(jsonResponse));
			}
		}
	}
	
	// 클라이언트 연결 종료 시
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		clients.remove(session);
	}
}
