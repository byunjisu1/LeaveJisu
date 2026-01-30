<%@page import="java.util.ArrayList"%>
<%@page import="com.js.dto.BlogPlaceMapDto"%>
<%@page import="com.js.dto.BlogCommentListDto"%>
<%@page import="com.js.dto.BlogDetailPlaceDto"%>
<%@page import="com.js.dto.BlogDetailDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	BlogDetailDto blog = (BlogDetailDto)request.getAttribute("listBlog");
	List<BlogCommentListDto> listBlogComment = (List<BlogCommentListDto>)request.getAttribute("listBlogComment");
	List<BlogPlaceMapDto> listBlogPlaceMap = (List<BlogPlaceMapDto>)request.getAttribute("listBlogPlaceMap");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_블로그</title>
	<link rel="stylesheet" href="resources/css/Blog.css"/>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY" defer></script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		$(function() {
			moment.locale('ko');
			
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			$("#day").click(function() {	// day 보기 팝업
				$("#black_filter").css("display", "block");
				$("#popup_day").css("display", "block");
			});
			$("#course").click(function() {	// 여행 코스 보기
				let blogIdx = $(this).data("blog_idx");
				location.href="PlanCourse?blog_idx=" + blogIdx;
			});
			$(".content").click(function() {
				location.href="PlaceDetail";
			});
			$("#add_btn").click(function() {	/* 내 일정으로 담기 */
				$("#popup_registertravel").css("display", "block");
				
				// 팝업 열자마자 화면에 오늘 날짜 표시
				$("#start_date").text(moment().format('YYYY.MM.DD(ddd)'));
				$("#end_date").text(moment().add(7, 'days').format('YYYY.MM.DD(ddd)'));
				
				// 팝업이 표시된 후 daterangepicker를 강제로 열기
		        setTimeout(function() {
		            $('input[name="daterangepicker"]').click();
		        }, 100);
			});
			$('input[name="daterangepicker"]').daterangepicker({	/* date picker */
				// 달력이 팝업 안에 고정되도록 설정
			    parentEl: "#popup_registertravel",
			    alwaysShowCalendars: true,	// 달력을 항상 펼쳐둠
			    autoApply: true,	// 날짜 클릭 시 바로 적용
			    autoUpdateInput: true,
		        opens: 'center',	// 팝업 내부에 고정
		        
		        startDate: moment(),	// 달력이 처음 열릴 때 선택되어 있을 날짜 지정 (오늘 날짜)
		        endDate: moment().add(7, 'days'),
		        
		        locale: {	// input의 value와 동일한 포맷 지정
		            format: 'MM/DD/YYYY', 
		            separator: " - ",
		            daysOfWeek: ["일", "월", "화", "수", "목", "금", "토"],
		            monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"]
		        }
				}, function(start, end, label) {	// 날짜 범위를 선택하고 'Apply' 버튼을 누르거나 선택이 완료되었을 때 실행되는 콜백(Callback) 함수
					$("#start_date").text(start.format('YYYY.MM.DD(ddd)'));
					$("#end_date").text(end.format('YYYY.MM.DD(ddd)'));
			});
			// 달력이 닫히려고 할 때 바로 다시 열기
		    $('input[name="daterangepicker"]').on('hide.daterangepicker', function(ev, picker) {
		        // 약간의 시차를 두어 다시 열리게 함
		        setTimeout(function() {
		            picker.show();
		        }, 10);
		    });
		    $("#popup_registertravel > div:first-child > div:first-child, #black_filter").click(function() {	/* 'x' */
				$("#popup_registertravel").css("display", "none");
			});
		    $("#popup_registertravel #btn_modify_date").click(function() {	/* 등록 완료 파란 버튼 */
		    	$('#loading').css("display", "block");
				let startText = $("#start_date").text().substring(0, 10).replaceAll(".", "-");
				let endText = $("#end_date").text().substring(0, 10).replaceAll(".", "-");
				
				let startDate = moment(startText, "YYYY-MM-DD");
			    let endDate = moment(endText, "YYYY-MM-DD");
			    let days = endDate.diff(startDate, 'days') + 1; // 일수 계산 (당일 포함)
			    
			 	let placeDataList = [];
			 	$(".blog_day_place > div").each(function() {
			        let day = $(this).data("day");
			        let pIdx = $(this).data("place_idx");
			        let cIdx = $(this).data("city_idx");
			        
			        placeDataList.push({
			            day: parseInt(day),
			            placeIdx: pIdx,
			            cityIdx : cIdx
			        });
			    });
			 	
			 	let requestData = {
			 		id: "<%=id%>",
			 		startDate: startDate.format('YYYY-MM-DD'),
			 		days: days,
			 		placeList: placeDataList
			 	};
				
			 	fetch('insert_blog_plan', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify(requestData)
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$('#loading').css("display", "none");
					$("#black_filter").css("display", "none");
					$("#popup_registertravel").css("display", "none");
					alert("내 일정으로 담았습니다. \n담은 일정은 프로필 내 '내 여행'에서 다시 확인하실 수 있습니다.");
				})
				.catch(error => {
					$('#loading').css("display", "none");
				    alert("에러");
				});
			});
			$(".good").click(function() {	// 댓글 추천
				if($(this).hasClass("on")) {
					let blogCommentIdx = $(this).closest(".comment").data("blog_comment_idx");
					let blogIdx = $("#course").data("blog_idx");
					let dollar_this = $(this);
					
					fetch('delete_comment_good', {
					    method: 'POST',
					    headers: { 'Content-Type': 'application/json; charset=utf-8' },
					    body: JSON.stringify({ blogCommentIdx: blogCommentIdx, blogIdx: blogIdx })
					})
					.then(response => {
					    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
					    return response.json();
					})
					.then(data => {
						// 화면 바꿔주기
						$(dollar_this).removeClass("on");
						$(dollar_this).siblings(".goodN").text("· 좋아요 " + data.commentGoodN);
					})
					.catch(error => {
					    alert("에러");
					});
			
				} else {
					let id = "<%=id%>";
					let blogCommentIdx = $(this).closest(".comment").data("blog_comment_idx");
					let blogIdx = $("#course").data("blog_idx");
					let dollar_this = $(this);
					
					fetch('insert_comment_good', {
					    method: 'POST',
					    headers: { 'Content-Type': 'application/json; charset=utf-8' },
					    body: JSON.stringify({ id: id, blogCommentIdx: blogCommentIdx, blogIdx: blogIdx })
					})
					.then(response => {
					    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
					    return response.json();
					})
					.then(data => {
						// 화면 바꿔주기
						$(dollar_this).addClass("on");
						$(dollar_this).siblings(".goodN").text("· 좋아요 " + data.commentGoodN);
					})
					.catch(error => {
					    alert("에러");
					});
				}
			});
			$("#upload").keyup(function() {	/* 댓글 입력 시, 등록 버튼 활성화 */
				let length = $("#upload").val().length;
				if(length >= 1) {
					$("#upload_btn").addClass("on");
				}
			});
			$("#upload_btn").click(function() {	/* 댓글 등록 버튼 */
				let blogIdx = <%=request.getParameter("blog_idx")%>;
				let id = "<%=id%>";
				let content = $("#upload").val();
				
				fetch('insert_blog_comment', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ blogIdx: blogIdx, id: id, content: content })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					let newComment = `
						<div class="comment" data-blog_comment_idx=\${data.dto.blogCommentIdx}>
							<img class="fl" src="/upload/\${data.dto.profileImg}"/>
							<div class="fl">
								<div>\${data.dto.nickname}</div>
								<div>\${data.dto.content}</div>
								<div class="option">
									<div class="good \${data.dto.commentGood}=1 ? "on" : "" "></div>
									<div class="goodN">· 좋아요 \${data.dto.commentGoodN}</div>
								</div>
							</div>
							<div class="comment_option fl"></div>
							<div style="clear:both;"></div>
							<div class="line"></div>
						</div>
					`;
					
					$(".comment_list").append(newComment);
					
					$("#upload").val("");
					alert("댓글이 등록되었습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(document).on("click", ".comment_option", function() {	// 댓글 수정/삭제 팝업
				let blogCommentIdx = $(this).closest(".comment").data("blog_comment_idx");
				$("#popup_option").attr("data-blog_comment_idx", blogCommentIdx);
				$(".comment_modify").attr("data-blog_comment_idx", blogCommentIdx);
				$("#black_filter").css("display", "block");
				$("#popup_option").css("display", "block");
			});
			$("#modify").click(function() {	// 내 댓글 수정하기 버튼
				let blogCommentIdx = $(".comment_modify").data("blog_comment_idx");
				let currentContent = $(".comment[data-blog_comment_idx='" + blogCommentIdx + "']").find("div:nth-child(2) > div:nth-child(2)").text();
				
				$("#black_filter").css("display", "none");
				$("#popup_option").css("display", "none");
				
				// 댓글 입력창이 수정창으로 바뀌어야 함
				$(".upload").css("display", "none");
				$(".comment_modify").css("display", "block");
				
				$("#modify_upload").val(currentContent);	// 원래의 내용을 input에 넣어주기
			});
			$("#modify_x").click(function() {	// 댓글 수정 'x' 버튼
				$(".upload").css("display", "flex");
				$(".comment_modify").css("display", "none");
			});
			$("#modify_btn").click(function() {	// 댓글 수정 '수정' 버튼
				let blogCommentIdx = $(".comment_modify").data("blog_comment_idx");
				let content = $("#modify_upload").val();
				
				fetch('update_blog_comment', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ content: content, blogCommentIdx: blogCommentIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$(".comment").each(function(idx, item) {
						if($(item).data("blog_comment_idx")!=blogCommentIdx) return;
						$(item).find("div:nth-child(2) > div:nth-child(2)").text(content);
						alert("댓글이 수정되었습니다.");
						$(".upload").css("display", "flex");
						$(".comment_modify").css("display", "none");
					})
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#delete").click(function() {	// 내 댓글 삭제하기 버튼
				$("#black_filter").css("display", "none");
				$("#popup_option").css("display", "none");
				
				let blogCommentIdx = $("#popup_option").attr("data-blog_comment_idx");
				
				fetch('delete_blog_comment', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ blogCommentIdx: blogCommentIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$(".comment").each(function(idx, item) {
						if($(item).data("blog_comment_idx")!=blogCommentIdx) return;
						$(item).closest(".comment").remove();
						alert("댓글이 삭제되었습니다.");
					})
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#popup_day > div:nth-child(2) > div").click(function() {	// 해당 day로 이동
				$("#black_filter").css("display", "none");
				$("#popup_day").css("display", "none");
				let d = $(this).text().replace(" ","").toLowerCase();
				$('body, html').animate({ scrollTop: $("."+d).offset().top }, 1000);
			});
			$("#other_blog").click(function() {
				location.href="RecommendPlan#blog_content";
			});
			$("#black_filter").click(function() {
				$("#popup_option").css("display", "none");
				$("#popup_day").css("display", "none");
				$("#black_filter").css("display", "none");
			});
			
			initMap();
		});
		
		function initMap() {
			let map;
			let arr;
			let sumLat;
			let sumLng;
			let avgLat;
			let avgLng;
			let centerPosition;
			let cnt;

		<%int lastDay = Integer.parseInt(blog.getDays().substring(3, 4));
			for(int day=1; day<=lastDay; day++) {%>
			
		   	map = new google.maps.Map(document.getElementById("map<%=day%>"), {
		    	center: { lat: 48.861761, lng: 2.353266 }, // 서울 중심 좌표
		    	zoom: 12,
		   	});
		   
		   	arr = [
			<%for(BlogPlaceMapDto dto : listBlogPlaceMap) {
					if(dto.getDay() != day) continue;%>
	   			  { label: "<%=dto.getPlaceOrder()%>", name: "<%=dto.getName()%>", lat: <%=dto.getLatitude()%>, lng: <%=dto.getLongitude()%> },
	   		<%
				}
	   		%>
   			];
			
   			arr.forEach(({ label, name, lat, lng }) => {
    			const marker = new google.maps.Marker({
	    			position: { lat, lng },
	    			label,
	    			map,
    			});
       			const infoWindow = new google.maps.InfoWindow();
	   			marker.addListener("click", () => {
		   			infoWindow.setContent(name);
		   			infoWindow.open(map, marker);
	   			});
    		});  

   			sumLat = 0.0;
   			sumLng = 0.0;
   			cnt = 0;
   			arr.forEach(({ lat, lng }) => {
   				if(!(lat==0 && lng==0)) cnt++; 
   				sumLat += lat;
   				sumLng += lng;
   			});
   			avgLat = sumLat / cnt;
   			avgLng = sumLng / cnt;
   			
   			centerPosition = { lat: avgLat, lng: avgLng };
   			
   			map.panTo(centerPosition);
   			
		<% } %>
		
		}
		
	</script>
</head>
<body>
	<div id="header">
		<div id="bell" class="fr" style="top: 13px; left: -85px;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	<div id="main">
		<div>
			<div><%=blog.getDays()%> 여행기</div>
		</div>
		<img src="<%=blog.getImgUrl()%>"/>
		<div><%=blog.getTitle()%></div>
		<div><%=blog.getIntro()%></div>
		<div class="box">
			<div id="day" class="fl">
				<svg width="17" height="17" viewBox="0 0 17 17" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M6.43251 3.64307C6.11692 3.64307 5.86108 3.8989 5.86108 4.2145C5.86108 4.53009 6.11692 4.78592 6.43251 4.78592H14.6785C14.9941 4.78592 15.25 4.53009 15.25 4.2145C15.25 3.8989 14.9941 3.64307 14.6785 3.64307H6.43251ZM5.86108 8.50021C5.86108 8.18462 6.11692 7.92878 6.43251 7.92878H14.6785C14.9941 7.92878 15.25 8.18462 15.25 8.50021C15.25 8.8158 14.9941 9.07164 14.6785 9.07164H6.43251C6.11692 9.07164 5.86108 8.8158 5.86108 8.50021ZM5.86108 12.7859C5.86108 12.4703 6.11692 12.2145 6.43251 12.2145H14.6785C14.9941 12.2145 15.25 12.4703 15.25 12.7859C15.25 13.1015 14.9941 13.3574 14.6785 13.3574H6.43251C6.11692 13.3574 5.86108 13.1015 5.86108 12.7859Z" fill="#3A3A3A"></path><path d="M5.96108 4.2145C5.96108 3.95413 6.17215 3.74307 6.43251 3.74307V3.54307C6.06169 3.54307 5.76108 3.84368 5.76108 4.2145H5.96108ZM6.43251 4.68592C6.17215 4.68592 5.96108 4.47486 5.96108 4.2145H5.76108C5.76108 4.58531 6.06169 4.88592 6.43251 4.88592V4.68592ZM14.6785 4.68592H6.43251V4.88592H14.6785V4.68592ZM15.15 4.2145C15.15 4.47486 14.9389 4.68592 14.6785 4.68592V4.88592C15.0494 4.88592 15.35 4.58531 15.35 4.2145H15.15ZM14.6785 3.74307C14.9389 3.74307 15.15 3.95413 15.15 4.2145H15.35C15.35 3.84368 15.0494 3.54307 14.6785 3.54307V3.74307ZM6.43251 3.74307H14.6785V3.54307H6.43251V3.74307ZM6.43251 7.82878C6.06169 7.82878 5.76108 8.12939 5.76108 8.50021H5.96108C5.96108 8.23985 6.17215 8.02878 6.43251 8.02878V7.82878ZM14.6785 7.82878H6.43251V8.02878H14.6785V7.82878ZM15.35 8.50021C15.35 8.12939 15.0494 7.82878 14.6785 7.82878V8.02878C14.9389 8.02878 15.15 8.23985 15.15 8.50021H15.35ZM14.6785 9.17164C15.0494 9.17164 15.35 8.87103 15.35 8.50021H15.15C15.15 8.76057 14.9389 8.97164 14.6785 8.97164V9.17164ZM6.43251 9.17164H14.6785V8.97164H6.43251V9.17164ZM5.76108 8.50021C5.76108 8.87103 6.06169 9.17164 6.43251 9.17164V8.97164C6.17215 8.97164 5.96108 8.76057 5.96108 8.50021H5.76108ZM6.43251 12.1145C6.06169 12.1145 5.76108 12.4151 5.76108 12.7859H5.96108C5.96108 12.5256 6.17215 12.3145 6.43251 12.3145V12.1145ZM14.6785 12.1145H6.43251V12.3145H14.6785V12.1145ZM15.35 12.7859C15.35 12.4151 15.0494 12.1145 14.6785 12.1145V12.3145C14.9389 12.3145 15.15 12.5256 15.15 12.7859H15.35ZM14.6785 13.4574C15.0494 13.4574 15.35 13.1567 15.35 12.7859H15.15C15.15 13.0463 14.9389 13.2574 14.6785 13.2574V13.4574ZM6.43251 13.4574H14.6785V13.2574H6.43251V13.4574ZM5.76108 12.7859C5.76108 13.1567 6.06169 13.4574 6.43251 13.4574V13.2574C6.17215 13.2574 5.96108 13.0463 5.96108 12.7859H5.76108Z" fill="#3A3A3A"></path><ellipse cx="2.97222" cy="4.21429" rx="0.722222" ry="0.714286" fill="#3A3A3A" stroke="#3A3A3A" stroke-width="0.3"></ellipse><ellipse cx="2.97222" cy="8.49993" rx="0.722222" ry="0.714286" fill="#3A3A3A" stroke="#3A3A3A" stroke-width="0.3"></ellipse><ellipse cx="2.97222" cy="12.7856" rx="0.722222" ry="0.714286" fill="#3A3A3A" stroke="#3A3A3A" stroke-width="0.3"></ellipse></svg>
				<div>Day 보기</div>
			</div>
			<div id="course" class="fl" data-blog_idx="<%=blog.getBlogIdx()%>">
				<svg width="17" height="17" viewBox="0 0 17 17" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M7.5083 5.0498H10.8283C11.7562 5.0498 12.5083 5.80196 12.5083 6.72979V6.72979C12.5083 7.65762 11.7562 8.40977 10.8284 8.40977H4.32832C2.77087 8.40977 1.5083 9.67234 1.5083 11.2298V11.2298C1.5083 12.7872 2.77086 14.0498 4.32832 14.0498H10.2083" stroke="#3A3A3A" stroke-width="1.34" stroke-linecap="round" stroke-linejoin="round"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M13.2683 14.3561L12.2301 13.3252C11.5552 12.655 11.5183 11.5383 12.2167 10.8924C12.8545 10.3022 13.8455 10.3022 14.4832 10.8924C15.1813 11.5383 15.1448 12.655 14.4699 13.3252L13.432 14.3561C13.3865 14.4009 13.3131 14.4009 13.2683 14.3561Z" stroke="#3A3A3A" stroke-width="1.34"></path><path fill-rule="evenodd" clip-rule="evenodd" d="M4.26829 5.51624L3.23009 4.48535C2.55518 3.81521 2.51835 2.69849 3.21672 2.05252C3.85449 1.46231 4.84546 1.46231 5.48323 2.05252C6.18131 2.69849 6.14477 3.81521 5.46987 4.48535L4.43196 5.51624C4.38651 5.56107 4.31314 5.56107 4.26829 5.51624Z" stroke="#3A3A3A" stroke-width="1.34"></path></svg>
				<div>여행 코스 보기</div>
			</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<div class="blog_day_content">
	
		<% 
		for(int i=1; i<=lastDay; i++) { 
		%>
			<div class="day<%=i%>">
				<div class="separator"></div>
				<div class="day_header">
					<div>Day <%=i%></div>
				</div>
				<div class="map" id="map<%=i%>">
				</div>
				<div class="blog_day_place">
					<% 
					for(BlogDetailPlaceDto dto : blog.getListBlogPlace()) { 
						if(i!=dto.getDay()) continue;
						String content = dto.getContent() == null ? "" : dto.getContent().replace("\n", "<br/>");
						String mint = "rgb(38, 206, 194)";	// 숙소
						String red = "rgb(255, 97, 105)";	// 음식점 or 카페/디저트
						String purple = "rgb(151, 95, 254)";	// 관광명소
					%>
						<div data-place_idx="<%=dto.getPlaceIdx()%>" data-day="<%=dto.getDay()%>" data-city_idx="<%=dto.getCityIdx()%>">
							<div class="number fl">
								<div style="background-color: <%=dto.getCategory().equals("숙소") ? mint : (dto.getCategory().equals("관광명소") ? purple : red)%>;"><%=dto.getPlaceOrder()%></div>
							</div>
							<div class="content fl">
								<div><%=dto.getPlaceName()%></div>
								<div class="fl"><%=dto.getCategory()%></div>
								<div style="clear:both;"></div>
								<% if(dto.getPlaceStar() != 0) { %>
									<div>
										<% for(int j=1; j<=dto.getPlaceStar(); j++) { %>
												<span class="star"></span>
										<% } %>
										<% for(int k=1; k<=(5-dto.getPlaceStar()); k++) { %>
												<span class="no_star"></span>
										<% } %>
									</div>
								<% } else { %>
									<div style="height: 0px"></div>
								<% } %>
								<% if(dto.getContent() != null) { %>
									<div>
										<div><%=content%></div>
									</div>
								<% } %>
							</div>
							<%
								if(dto.getPlaceImg() != null) {
							%>
									<img src="<%=dto.getPlaceImg()%>"/>
							<% } %>
							<div style="clear:both;"></div>
						</div>
					<% } %>
				</div>
				
			</div>
		<% } %>
	<div class="blog_plan_box">
		<div>
			<div id="add_btn">
				<img width="18" height="18" src="https://assets.triple.guide/images/ico_lounge_download_white@4x.png" alt="">
				<div>내 일정으로 담기</div>
			</div>
			<div id="other_blog">
				<div>다른 여행 블로그 보러 가기</div>
			</div>
		</div>
		<div></div>
		<div>
			<img src="/upload/<%=blog.getProfileImg()%>" alt="<%=blog.getNickname()%>" class="content-elements__SquareImage-sc-9d4245e9-4 xBJSW">
			<div><%=blog.getNickname()%></div>
		</div>
	</div>
	<div class="separator" style="margin-bottom: 20px;"></div>
	
	<div class="blog_comment">
		<div class="comment_list">
			<% for(BlogCommentListDto dto : listBlogComment) { %>
				<div class="comment" data-blog_comment_idx=<%=dto.getBlogCommentIdx()%>>
					<img class="fl" src="/upload/<%=dto.getProfileImg()%>"/>
					<div class="fl">
						<div><%=dto.getNickname()%></div>
						<div><%=dto.getContent()%></div>
						<div class="option">
							<div class="good <%=dto.getCommentGood()>=1 ? "on" : ""%>"></div>
							<div class="goodN">· 좋아요 <%=dto.getCommentGoodN()%></div>
						</div>
					</div>
					<% if(id.equals(dto.getId())) { %>
						<div class="comment_option fl"></div>
					<% } %>
					<div style="clear:both;"></div>
					<div class="line"></div>
				</div>
			<% } %>
		</div>
		<div class="upload">
			<input type="text" id="upload" placeholder="댓글을 입력하세요."/>
			<div id="upload_btn">등록</div>
		</div>
		<div class="comment_modify" style="display: none;">
			<div>
				<div>댓글 수정 중</div>
				<div id="modify_x">x</div>
			</div>
			<div>
				<input type="text" id="modify_upload" placeholder=""/>
				<div id="modify_btn">수정</div>
			</div>
		</div>
		<div class="separator"></div>
	</div>
	
	<div id="black_filter" style="display: none;"></div>
	<div id="popup_day" style="display: none;">
		<div>Day 보기</div>
		<div>
			<% for(int i=1; i<=lastDay; i++) { %>
				<div>Day <%=i%></div>
			<% } %>
		</div>
	</div>
	<div id="popup_option" style="display: none;">
		<div>내 댓글</div>
		<div>
			<div id="modify">수정하기</div>
			<div id="delete">삭제하기</div>
		</div>
	</div>
	<div id="popup_registertravel" style="display: none;">
		<div>
			<div class="fl"></div>
			<div style="clear:both;"></div>
		</div>
		<div>여행일정 등록</div>
		<div>일정에 따른 날씨예보, 여행 정보를 알려드립니다.</div>
		<input type="text" name="daterangepicker" value="01/01/2026 - 01/01/2026" style="display: none;"/>
		<div id="btn_modify_date">
			<span id="start_date"></span> - <span id="end_date"></span> / 등록 완료
		</div>
	</div>
	<div id="loading" style="display: none;">
		<img src="resources/img/loading.gif" class="loading">
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>