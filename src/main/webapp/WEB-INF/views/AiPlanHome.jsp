<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_맞춤일정_메인</title>
	<link rel="stylesheet" href="resources/css/AiPlanHome.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function() {
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			$("#preference").click(function() {
				location.href="AiPlanOption";
			});
			$("#auto").click(function() {
				$("#option_ready").css("display", "block");
				location.href="AiPlanAutoRecommend";
			});
		});
	</script>
</head>
<body>
	<div id="header">
		<div id="bell" class="fr" style="top: 5px; left: -77px;"></div>
		<div id="menu" class="fr"></div>
	</div>
	<div id="header2">
		<div>취향에 맞게 일정을<br>추천해 드려요!</div>
		<div>순식간에 여행 준비 끝</div>
	</div>
	<div id="main_img">
		<img src="https://triple.guide/trips/static/images/promotion/customized-schedule/img_main-visual@4x.png"/>
	</div>
	<div id="button">
		<div id="preference">내 취향 맞춤 추천 받기</div>
		<div id="auto">자동 추천 받기</div>
	</div>
	
	<div id="option_ready" style="display:none;">	<!-- 준비중 -->
		<div class="header2" style="margin-top: 320px;">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_map-emoji.svg);"></div>
			<div>여행 스타일에 맞는<br>맞춤 일정을<br>준비중입니다.</div>
			<div>잠시만 기다려 주세요.</div>
		</div>
		<div id="separator"></div>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>