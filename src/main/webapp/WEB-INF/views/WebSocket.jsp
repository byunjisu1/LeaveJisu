<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script>
	const webSocket = new WebSocket("ws://13.209.208.223:9090/LeaveJisu/broadcasting");
	
	webSocket.onopen = function(e) { console.log("websocket 접속되었음."); };
	webSocket.onerror = function(e) { console.log("에러"); };
	
</script>