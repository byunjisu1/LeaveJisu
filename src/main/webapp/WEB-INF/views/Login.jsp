<%@page import="java.math.BigInteger"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="java.security.SecureRandom"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    String clientId = "gmbnSIrgIhi0INbUtzLL";//애플리케이션 클라이언트 아이디값";
    String redirectURI = URLEncoder.encode("http://localhost:9090/LeaveJisu/NaverLoginAction", "UTF-8");
    SecureRandom random = new SecureRandom();
    String state = new BigInteger(130, random).toString();
    String apiURL = "https://nid.naver.com/oauth2.0/authorize?response_type=code";
    apiURL += "&client_id=" + clientId;
    apiURL += "&redirect_uri=" + redirectURI;
    apiURL += "&state=" + state;
    session.setAttribute("state", state);
 %>

<c:if test="${not empty sessionScope.msg}">
    <script>
        alert("${sessionScope.msg}");
    </script>
    <% session.removeAttribute("msg"); %>
</c:if>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>로그인</title>
	<link rel="stylesheet" href="resources/css/Login.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function(){
			$("#search > .searchid").click(function() {	/* 아이디 찾기 */	
				$("#black_filter").css("display","block");
				$("#popup_searchid").css("display","block");
			});
			$("#popup_searchid > button:nth-child(3)").click(function() {	/* 검색 */
				let email = $(this).closest("#popup_searchid").find("input[type='text']").val();
			
				fetch('search_id', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ email: email })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) {
				    	alert("검색된 아이디는 " + data.id + "입니다. \n로그인을 진행해주세요.");
				    	location.href="Login";
				    } else {
				    	alert("검색된 아이디가 없습니다. \n이메일을 다시 입력하시거나 회원가입을 진행해주세요.");
				    	location.href="Login";
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#popup_searchid > div:nth-child(4)").click(function() {	/* 'x' */
				$("#black_filter").css("display","none");
				$("#popup_searchid").css("display","none");
			});
			$("#search > .searchpassword").click(function() {	/* 비밀번호 찾기 */
				$("#black_filter").css("display","block");
				$("#popup_searchpassword").css("display","block");
			});
			$("#popup_searchpassword > button:nth-child(4)").click(function() {	/* 검색 */
				let id = $("#input_id").val();
				let email = $("#input_email").val();
				alert(id + ", " + email);
				if(id==null || email==null) {
					alert("아이디와 이메일을 모두 입력해주세요.");
				}
			
				fetch('search_pw', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: id, email: email })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) {
				    	alert("새 비밀번호가 이메일로 전송되었습니다. \n확인 후 로그인을 진행해주세요.");
				    	location.href="Login";
				    } else {
				    	alert("아이디 또는 이메일에 일치하는 비밀번호가 없습니다. \n아이디와 이메일을 다시 입력해주세요.");
				    	location.href="Login";
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#popup_searchpassword > div:nth-child(5)").click(function() {	/* 'x' */	
				$("#black_filter").css("display","none");
				$("#popup_searchpassword").css("display","none");
			});
			$("#join").click(function(){
				location.href="Join";
			});
		});
	</script>	
	<style>
	    
	</style>
</head>
<body>
	<div id="loginlogo">L O G I N</div>
	<div id="container">
		<div id="naverlogin">
			<a href="<%=apiURL%>" class="naver-btn">
			    <div class="naver-logo-symbol"></div>
			    <span>네이버로 로그인</span>
			</a>
		</div>
		<form action="LoginAction" method="post">
			<div id="idpassword">
				<div id="id">ID
					<input type="text" name="id"/>
				</div>
				<div id="password">PASSWORD
					<input type="password" name="pw"/>
				</div>
			</div>
			<div id="login">
				<button>LOGIN</button>
			</div>
		</form>		
		<div id="search">
			<div class="searchid">- SEARCH ID</div>
			<div class="searchpassword">- SEARCH PASSWORD</div>
		</div>
		<div id="join">
			<span>JOIN MEMBER</span>
		</div>
	</div>
	<div id="black_filter" style= "display:none;"></div>
	<div id="popup_searchid" style= "display:none;">
		<span>이메일로 아이디 찾기</span>
		<button>
			<input type="text"/>
		</button>
		<button>검색</button>
		<div class="x fr"></div>
		<div style="clear:both;"></div>
	</div>
 	<div id="popup_searchpassword" style= "display:none;">
 		<div>비밀번호 찾기</div>
 		<div>
			<span>아이디</span>
			<button>
				<input type="text" id="input_id"/>
			</button>
		</div>
		<div>
			<span>이메일</span>
			<button>
				<input type="text" id="input_email"/>
			</button>
		</div>
		<button>검색</button>
		<div class="x fr"></div>
	</div>
</body>
</html>