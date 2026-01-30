<%@page import="com.js.dto.MyPlaceListDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	List<MyPlaceListDto> listMyPlace = (List<MyPlaceListDto>)request.getAttribute("listMyPlace");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>메뉴바 - 프로필내 내 저장</title>
	<link rel="stylesheet" href="resources/css/MySaved.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function(){
			$("#header > img:first-child").click(function()	{	/* 뒤로가기 */
				history.back();	
			});
			$(".myplace").click(function() {	/* 장소 상세 페이지 */
				let placeIdx = $(this).closest(".place").data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$(".writememo").click(function(){	/* 메모 작성 팝업 */
				let placeIdx = $(this).closest(".place").data("place_idx");
				$("#popup_creatememo").attr("data-place_idx", placeIdx);
				
				let presentMemo = $(this).find("span").text();
				if(presentMemo === "메모 남기기") {
			        $("#memo").val("");
			    } else {
			        $("#memo").val(presentMemo);
			    }
				
				$("#popup_creatememo").css("display","block");
				$("#black_filter").css("display","block");
			})
			$("#popup_creatememo > div:nth-child(3) > div:first-child").click(function() {	/* 취소 */	
				$("#popup_creatememo").css("display","none");
				$("#black_filter").css("display","none");
			});
			$("#popup_creatememo > div:nth-child(3) > div:nth-child(2)").click(function() {	/* 확인 */				
				let memo = $("#memo").val();
				let placeIdx = $("#popup_creatememo").data("place_idx");
				
				fetch('update_place_memo', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: "<%=id%>", placeIdx: placeIdx, memo: memo })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$("#popup_creatememo").css("display","none");
					$("#black_filter").css("display","none");
					// 화면 바꿔주기
					$(".place").each(function(idx, item) {
						if($(item).data("place_idx")!=placeIdx) return;
						$(item).find(".writememo span").text(memo);
					});
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(".heart").click(function() {
				let placeIdx = $(this).closest(".place").data("place_idx");
				let dollar_this = $(this);
				
				fetch('delete_myplace', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: "<%=id%>", placeIdx: placeIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$(dollar_this).closest(".place").remove();
					alert("삭제되었습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display","block");
			});
		});
	</script>
</head>
<body>
	<div id="header">
		<img class="fl" src="resources/img/left_arrow.png"/>
		<span class="fl">내 저장</span>
		<div id="bell" class="fr" style="top: 20px; left: -96px;"></div>
		<img id="menu" class="fr" src="https://assets.triple.guide/images/ico_navi_menu@4x.png"/>
		<div style="clear:both;"></div>
	</div>
	<% if(listMyPlace.size() == 0) { %>
		<div style="font-size: 20px; margin-left: 85px; margin-top: 80px; color: darkgray;">저장된 장소가 없습니다. <br/>장소상세페이지에서 장소를 찜하세요.</div>
	<% } %>
	<% for(MyPlaceListDto dto : listMyPlace) { %>
		<div class="place" data-place_idx="<%=dto.getPlaceIdx()%>">
			<div class="main">
				<div class="myplace fl">
					<h3><%=dto.getName()%></h3>
					<span><%=dto.getIntro()%></span>
					<div>
						<% 
						   int starCount = (int)Math.round(dto.getStarScore());
						   for(int i=1; i<=starCount; i++) { 
						%>
								<img src="https://assets.triple.guide/images/img-review-star-full@4x.png"/>
						<% } %>
						<% for(int i=1; i<=(5-starCount); i++) { %>
								<img src="https://assets.triple.guide/images/img-review-star-empty@4x.png"/>
						<% } %>
						<span>(<%=dto.getReviewCnt()%>)</span>
					</div>
					<span><%=dto.getCategory()%> · <%=dto.getCityName()%></span>
				</div>
				<div class="myplacepic fr">
					<img class="fr" src="<%=dto.getPlaceImg()%>"/>
					<img class="heart" src="resources/img/redheart.png"/>
				</div>
				<div style="clear:both;"></div>
			</div>
			<div class="writememo">
				<img src="resources/img/bluememo.png"/>
				<span <%=dto.getContent()!=null ? "class='on'" : "" %>><%=dto.getContent()!=null ? dto.getContent() : "메모 남기기"%></span>
			</div> 
		</div>
	<% } %>
	
	<div id="popup_creatememo" style="display:none;" >
		<div class="fl">메모</div>
		<textarea class="fl" id="memo" type="text" placeholder="잊기 쉬운 정보들을 메모해보세요."></textarea>
		<div>
			<div>취소</div>
			<div>확인</div>
		</div>
		<div style="clear:both;"></div>
	</div>
	
	<div id="black_filter" style="display:none;"></div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>