<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.format.DateTimeFormatter"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Map"%>
<%@page import="com.js.dto.MenuBarProfileDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty msg}">
    <script>
        alert("${msg}");
    </script>
</c:if>
	    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>메인화면_메뉴바</title>
	<link rel="stylesheet" href="resources/css/Main.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function() {
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			let loginId = "${sessionScope.loginId}";
			
			if(!loginId) { // 로그인 안 된 경우
	            $("#profile").css("display", "none");
	            $("#btn_login").css("display", "block");
	            $("#bell").css("display", "none"); 
	        } else {
	            $("#profile").css("display", "block");
	            $("#btn_login").css("display", "none");
	            $("#bell").css("display", "block");
	        }
		});
	</script>
</head>
<body>
	<div id="header">
		<img src="https://triple.guide/intro/static/images/img-main-logo-white@3x.png"/>
		<div id="bell" class="fr" style="background: url(resources/img/white_bell.png) center / 100%;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	<div id ="main">
		<h2>계획부터 예약까지,여행이 쉬워지는</h2>
		<h1>나를 아는 여행 앱</h1>
		<h1>떠나지수</h1>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>