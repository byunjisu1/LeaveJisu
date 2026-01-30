<%@page import="com.js.dto.MyAiPackDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	MyAiPackDto myAiPack = (MyAiPackDto)request.getAttribute("myAiPack");
	String[] presentList = new String[0];
	String[] missingList = new String[0];
	
	if (myAiPack != null) {
		presentList = myAiPack.getPresentList().split(",");
		missingList = myAiPack.getMissingList().split(",");
    }
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>AI짐싸기리스트</title>
	<link rel="stylesheet" href="resources/css/AiPackingList.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function() {
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			
			$("#btn_upload").click(function(e) {
				e.preventDefault();
				$("#real_file").click();
			});
			$("#real_file").on("change", function() {
				$(this).parent().submit();
			})
		});
	</script>
</head>
<body>
	<div id="header">
		<div id="bell" class="fr" style="top: 23px; left: -92px;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	<div id="header2">AI 짐싸기 리스트</div>
	<div id="picarea">
		<div id="pic">
			<img src="<%=(myAiPack != null && myAiPack.getPackImg()!=null) ? "/upload/" + myAiPack.getPackImg() : "resources/img/addphoto.png"%>"/>
		</div>
		<form action="UploadAiPacking" method="post" enctype="multipart/form-data">
			<button id="btn_upload">Upload Photo</button>
			<input id="real_file" type="file" name="file1"/>
		</form>
	</div>
	<div id="main">
		<div id="present">
			<% if(myAiPack != null && presentList != null) { %>
			<div>이미 있는 물건</div>
				<% for(String s : presentList) { %>
					<div class="item">
						<img class="fl" src="resources/img/blackcheck.png"/>
						<span><%=s%></span>
					</div>
				<% } %>
			<% } %>
		</div>
		<div id="missing">
			<% if(myAiPack != null && missingList != null) { %>
			<div>잊은 물건</div>
				<% for(String s : missingList) { %>
					<div class="item">
						<img class="fl" src="resources/img/blackcheck.png"/>
						<span><%=s%></span>
					</div>
				<% } %>
			<% } %>
		</div>
	</div>
	<div class="advice">
		<div>★ AI 조언 ★</div>
		<div><%=(myAiPack != null && myAiPack.getAdvice() != null) ? myAiPack.getAdvice() : "데이터가 없습니다. 사진을 먼저 업로드 해주세요."%></div>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>