<%@page import="com.js.dao.MemberDaoImpl"%>
<%@page import="com.js.dao.MemberDao"%>
<%@page import="com.js.service.MemberServiceImpl"%>
<%@page import="com.js.service.MemberService"%>
<%@page import="com.js.service.MyTripServiceImpl"%>
<%@page import="com.js.service.MyTripService"%>
<%@page import="com.js.dao.MyTripDaoImpl"%>
<%@page import="com.js.dao.MyTripDao"%>
<%@page import="com.js.dto.MyBellListDto"%>
<%@page import="java.time.temporal.ChronoUnit"%>
<%@page import="java.util.List"%>
<%@page import="com.js.dto.MenuBarProfileDto"%>
<%@page import="java.time.LocalDate"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<MenuBarProfileDto> listMenuBarProfile = (List<MenuBarProfileDto>)request.getAttribute("listMenuBarProfile");
	Map<String, String> profileMap = (Map<String, String>)request.getAttribute("profileMap");
	List<MyBellListDto> listMyBell = (List<MyBellListDto>)request.getAttribute("listMyBell");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="resources/css/MenuBar.css"/>
	
	<!-- Swiper.js -->	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />
	<script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
	
	<script>
		$(function(){
			let bellCount = <%= listMyBell!=null ? listMyBell.size() : 0 %>;
			if(bellCount>0) { $("#bell").addClass("new"); }
			else { $("#bell").removeClass("new"); }
			
			$("#btn_login").click(function(){
				location.href="Login";
			});
			$("#menu_open > div:first-child > div").click(function() {	/* 'X' */
				$("#menu_open").css("display","none");
				$("#black_filter").css("display","none");
			});
			let originalNickname = "";
			let isImageChanged = false;
			$("#profile > .setting").click(function() {	/* 톱니바퀴 */	
				originalNickname = $("#nickname").text().trim();
				isImageChanged = false;	// 초기화
				$("#popup_changeprofile input[type='text']").val(originalNickname);
				$("#popup_changeprofile button").removeClass("on");
				
				$("#black_filter").css("display", "block");
				$("#popup_changeprofile").css("display","block");
			});
			$("#btn_img_change").click(function() {
		        $("#profile_file").click();
		    });
		    $("#profile_file").on("change", function(e) {
		        if (this.files && this.files[0]) {
		            let reader = new FileReader();
		            reader.onload = function(e) {
		                $("#popup_changeprofile img").eq(0).attr("src", e.target.result);
		            }
		            reader.readAsDataURL(this.files[0]);
		            
		            isImageChanged = true;
		            checkChanges();
		        }
		    });
			$("#popup_changeprofile input[type='text']").on("keyup", function() {	/* 수정완료 버튼 활성화 */
				checkChanges();
			});
			// 수정완료 버튼 활성화 체크 함수
		    function checkChanges() {
		        let currentNickname = $("#popup_changeprofile input[type='text']").val().trim();
		        let isNicknameValid = /^[가-힣a-zA-Z]{2,6}$/.test(currentNickname);
		        let isNicknameChanged = (currentNickname !== originalNickname);

		        // 닉네임이 유효하면서 (닉네임이 변했거나 OR 사진이 변했거나)
		        if (isNicknameValid && (isNicknameChanged || isImageChanged)) {
		            $("#popup_changeprofile button").addClass("on");
		        } else {
		            $("#popup_changeprofile button").removeClass("on");
		        }
		    }
			$("#popup_changeprofile button").click(function() {	/* 닉네임 수정 */
				if(!$(this).hasClass("on")) return;
			
				let nickname = $("#popup_changeprofile input[type='text']").val();
				let fileInput = document.getElementById('profile_file');
				
				// 사진과 닉네임을 함께 전송하기 위해 FormData 사용
		        let formData = new FormData();
		        formData.append("nickname", nickname);
		        if(fileInput.files[0]) {
		            formData.append("profile_img", fileInput.files[0]);
		        }
		        
				fetch('update_profile', {
				    method: 'POST',
				    body: formData
				})
				.then(response => {
			        if (!response.ok) throw new Error('서버 통신 에러');
			        return response.json();
			    })
				.then(data => {
					$("#nickname").text(nickname);
					if(data.newProfileUrl) {
		                $("#profile > img:nth-child(3)").attr("src", data.newProfileUrl);
		            }
					$("#popup_changeprofile").css("display","none");
					
					alert("프로필이 수정되었습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#popup_changeprofile .x").click(function() {	/* 프로필 수정 팝업 닫기 */
		        $("#popup_changeprofile").css("display", "none");
		    });
			$(".upcoming_plan").click(function() {	/* 다가오는 여행 */
				let planIdx = $(this).closest(".upcoming_plan").data("plan_idx");
				if(planIdx != undefined) {
					location.href="MyTripDetail?plan_idx=" + planIdx;
				} else {
					location.href="MyTrip";
				}
			});
			$("#profile > div:nth-child(5) > div:nth-child(1)").click(function() {	/* 내 여행 */
				location.href="MyTrip";
			});
			$("#profile > div:nth-child(5) > div:nth-child(2)").click(function() {	/* 내 저장 */
				location.href="MySaved";				
			});
			$("#profile > div:nth-child(5) > div:nth-child(3)").click(function() {	/* 내 리뷰 */
				location.href="MyTrip?tab=review";
			});
			$('#menuitems > div:nth-child(1)').click(function() {	/* AI 짐싸기 리스트 */
				location.href="AiPackingList";
			});
			$('#menuitems > div:nth-child(2)').click(function() {	/* 여행 정보 */
				location.href="RecommendPlan";
			});
			$('#menuitems > div:nth-child(3)').click(function() {	/* AI 추천 맞춤일정 */
				location.href="AiPlanHome";
			});
			$('#menuitems > div:nth-child(4)').click(function() {	/* 여행상품 */
				location.href="AirlineTicket";
			});
			$("#logout").click(function(){
				location.href="Logout";
			});
			$(document).on("click", "#bell", function() {
			    $("#black_filter").css("display", "block");
			    $("#popup_bell").css("display", "block");
			});
			
			$("#popup_bell > #bell_header > div:nth-child(1)").click(function() {	/* 'x' */
				$("#black_filter").css("display", "none");
				$("#popup_bell").css("display", "none");
			});
			$("#popup_bell #all_check_btn").click(function() {	/* 모두 확인 */
				fetch('delete_all_bells', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: "<%=session.getAttribute("loginId")%>" })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$("#bell").removeClass("new");
					$("#contents").empty();
					$("#contents").append("<div class='empty_bell'>새로운 알림이 없습니다.</div>");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(document).on("click", "#popup_bell .content", function() {	/* 알림 1개 확인 */
				let bellIdx = $(this).data("bell_idx");
				let idxType = $(this).data("review_idx").substring(0, 1);
				let reviewIdx = $(this).data("review_idx").substring(1);
				
				fetch('delete_bells', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ bellIdx: bellIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 해당 리뷰로 이동
					location.href="MyTrip?tab=review&&idxType=" + idxType + "&&reviewIdx=" + reviewIdx;
					
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			const swiper = new Swiper('.swiper', {
				direction: 'horizontal',
				loop: false,	// 무한 반복 여부
				
				slidesPerView: 1,
				spaceBetween: 20,
				autoplay: {
					delay: 5000,
					disableOnInteraction: false,	// 사용자가 직접 넘겨도 자동 재생 계속 작동.
				},
	    	});
		});
		
		webSocket.onmessage = function(e) {
			//alert("받은 메시지 : " + e.data);    // e.data : JSON 형식으로 되어 있는 문자열.
			console.log(JSON.parse(e.data));  // JSON.parse() : 문자열 -> JS객체 변환. (단, 문자열이 JSON형식일 것.)
			
			let bellCount = <%= listMyBell!=null ? listMyBell.size() : 0 %>;
			if(bellCount>0) { $("#bell").addClass("new"); }
			else { $("#bell").removeClass("new"); }
			
			const data = JSON.parse(e.data);
			let str = `
				<div class="content" data-bell_idx="\${data.bellIdx}" data-review_idx="${data.type+data.reviewIdx}">
					· \${data.nickname}님이 내 리뷰에 좋아요를 눌렀습니다.
				</div>
			`;
			$("#contents").prepend(str);
		}
	</script>
</head>
<body>
	<div id="black_filter" style="display:none;"></div>
	<div id="menu_open" style="display:none;">
		<div>
			<div class="x"></div>
		</div>
		<% if(profileMap == null) { %>
			<div id="btn_login"><b>로그인/회원가입</b></div> 
		<% } else { %>
			<div id="profile">
				<img class="setting" src="https://cdn-icons-png.flaticon.com/256/97/97777.png"/>
				<span id="nickname"><%=profileMap.get("NICKNAME")%></span>
				<img src="<%=profileMap.get("PROFILE_IMG")!=null ? "/upload/" + profileMap.get("PROFILE_IMG") : "resources/img/default_profile.png"%>"/>
				<% if(listMenuBarProfile != null && !listMenuBarProfile.isEmpty()) { %>
					<div class="swiper">
       					<div class="swiper-wrapper">	
					<%
						LocalDate today = LocalDate.now();
						for(MenuBarProfileDto dto : listMenuBarProfile) {
							String datePart = dto.getStartDate().substring(0, 10);
				            LocalDate start = LocalDate.parse(datePart);
				            long diff = ChronoUnit.DAYS.between(today, start);
				            String dDayResult = (diff == 0) ? "D-Day" : "D-" + diff;
					%>
							<div class="upcoming_plan swiper-slide" data-plan_idx="<%=dto.getPlanIdx()%>">
								<img src="<%=dto.getCityImg()%>"/>
								<span><%=dto.getCityName()%> 일정</span>
								<span> ㅣ </span>
								<span><%=dDayResult%></span>
							</div>
					<% } %>
						</div>
					</div>
				<% 
				} else {
				%>
					<div class="upcoming_plan no_data">
			            <span>다가오는 일정이 없습니다.</span>
			        </div>
		        <% } %>
				<div id="my_category">
					<div>
						<img src="resources/img/location.png"/>
						<p>내 여행</p>
					</div>
					<div>
						<img src="resources/img/heart.png"/>
						<p>내 저장</p>
					</div>
					<div>
						<img src="resources/img/star.png"/>
						<p>내 리뷰</p>
					</div>
				</div>
			</div>
		<% } %>
			<div id="menuitems">
				<div>AI 짐싸기 리스트</div>
				<div>여행정보</div>
				<div>AI 추천 맞춤일정</div>
				<div>여행상품</div>
			</div>
			
			<% if(profileMap != null) { %>
				<div id="logout">로그아웃</div>
			<% } %>
	</div>
	<% if(profileMap != null) { %>
		
		<div id="popup_changeprofile" style="display:none;">
			<div class="x fr"></div>
			<div>
				<img src="<%=profileMap.get("PROFILE_IMG")!=null ? "/upload/" + profileMap.get("PROFILE_IMG") : "resources/img/default_profile.png"%>"/>
				<img id="btn_img_change" src="https://cdn-icons-png.freepik.com/256/8680/8680018.png?semt=ais_white_label"/>
				<input type="file" id="profile_file" name="profile_img" multiple="multiple" style="display: none;"/>
			</div>
			<div>
				<input type="text"/>
				<img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRabVqM7cLoneBbK6VYUO2Q1Ll3bdPAGhlJN1JZ6KJlG5IM4gMwmBp6cxM3iN37StSlsdM&usqp=CAU"/>
			</div>
			<div><button>수정완료</button></div>
		</div>
		
		<div id="popup_bell" style="display: none;">
			<div id="bell_header">
				<div class="fl">x</div>
				<button id="all_check_btn" class="fr">모두 확인</button>
				<div style="clear: both;"></div>
			</div>
			<div id="contents" >
				<% if(listMyBell == null || listMyBell.isEmpty()) { %>
					<div class='empty_bell'>새로운 알림이 없습니다.</div>
				<% 
					} else {
						for(MyBellListDto dto : listMyBell) { 
				%>
						<div class="content" data-bell_idx="<%=dto.getBellIdx()%>" data-review_idx="<%=dto.getReviewIdx()!=null ? "p"+dto.getReviewIdx() : "c"+dto.getRecommendReviewIdx()%>">
							· <%=dto.getNickname()%>님이 내 리뷰에 좋아요를 눌렀습니다.
						</div>
						<% } %>
				<% } %>
			</div>
		</div>
	<% } %>
</body>
</html>