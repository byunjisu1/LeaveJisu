<%@page import="com.js.dto.RecommendPlanDetailDto"%>
<%@page import="com.js.dto.BlogListDto"%>
<%@page import="com.js.dto.RecommendPlanPlaceDto"%>
<%@page import="com.js.dto.PlaceDto"%>
<%@page import="com.js.dto.CityListDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<CityListDto> listCityRecommend = (List<CityListDto>)request.getAttribute("listCityRecommend");
	Integer cityIdx = (Integer)request.getAttribute("cityIdx");
	List<PlaceDto> listPlaceTop10 = (List<PlaceDto>)request.getAttribute("listPlaceTop10");
	List<RecommendPlanDetailDto> listRecommendPlan = (List<RecommendPlanDetailDto>)request.getAttribute("listRecommendPlan");
	List<BlogListDto> listBlog = (List<BlogListDto>)request.getAttribute("listBlog");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_떠나지수_추천일정</title>
	<link rel="stylesheet" href="resources/css/RecommendPlan.css"/>
	<link rel="stylesheet" href="resources/css/Top10.css"/>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY&callback=initMap" async defer></script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Swiper.js -->	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />
	<script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
	
	<script>
		$(function() {
			$("#menu").click(function() {
				// 사이드 바
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			$(".city").click(function() {	// 각 도시별 추천 일정 화면
				let cityIdx = $(this).data("city_idx");
				location.href="RecommendPlan?city_idx=" + cityIdx;
			});
			$(".place_content").click(function() {	// 장소 상세 페이지
				let placeIdx = $(this).closest(".place_content").data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$("#add").click(function() {	// 나머지 일정 자세히 보기
				const params = new URLSearchParams(window.location.search);	// 주소의 파라미터 가져오기
				let cityIdx = params.get("city_idx");
				if(cityIdx == null) cityIdx = 16;	// 파리(기본값)
				location.href="RecommendPlanAll?city_idx=" + cityIdx;
			});
			$(".top10items").click(function() {	// TOP10 장소 상세 페이지 이동
				let placeIdx = $(this).closest(".top10items").data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$(".blog_container").click(function() {	// 해당 블로그 상세 페이지
				let blogIdx = $(this).data("blog_idx");
				location.href="Blog?blog_idx=" + blogIdx;
			});
			$("#back_home").click(function() {	// 트리플 홈으로
				location.href="Main";
			});
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
		   
		   	arr = [
			<%
				int count = 1;
				for(RecommendPlanDetailDto dto : listRecommendPlan) { 
					if(count++ == 2) break;
					for(RecommendPlanPlaceDto placeDto : dto.getListPlace()) {
			%>
	   			  { label: "<%=placeDto.getPlaceOrder()%>", name: "<%=placeDto.getPlaceName()%>", lat: <%=placeDto.getLatitude()%>, lng: <%=placeDto.getLongitude()%> },
	   		<%
					}
				}
	   		%>
			];
		   	
			// 장소가 없는 경우 처리
		    const mapDiv = document.getElementById("map");
		    const mapContainer = mapDiv.closest('.map');

		    if (arr.length === 0) {
		        if (mapContainer) {
		            mapContainer.style.display = "none"; // 부모 컨테이너까지 숨김
		        } else {
		            mapDiv.style.display = "none"; // 부모가 없으면 지도 영역만 숨김
		        }
		        return;
		    }
		    
		 	// 장소가 있는 경우 지도 생성
		   	map = new google.maps.Map(document.getElementById("map"), {
		    	center: { lat: 37.5665, lng: 126.9780 }, // 서울 중심 좌표
		    	zoom: 12,
		   	});
			
		 	// 마커 표시
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
		}
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
	</script>
</head>
<body>
	<div id="header">
		<img src="https://assets.triple.guide/images/img_intro_logo_dark.svg"/>
		<div id="bell" class="fr" style="top: 13px; left: -85px;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	
	<div id="city_list">
		<div>떠나지수 추천 일정 👍🏻</div>
		<div class="city_box">
			<%
			for(CityListDto dto : listCityRecommend) {
			%>
				<div class='city <%=(dto.getCityIdx()==cityIdx ? "on" : "")%> fl' data-city_idx="<%=dto.getCityIdx()%>">
					<div style="background: url(<%=dto.getCityImg()%>) 0% 0% / cover no-repeat;"></div>
					<div><%=dto.getCityName()%></div>
				</div>
			<%
			}
			%>
			<div style="clear:both;"></div>
		</div>
	</div>
	
		<%
		int cnt = 1;
		for(RecommendPlanDetailDto dto : listRecommendPlan) { 
				if(cnt++ == 2)
					break;
				String content = dto.getContent().replace("\n", "<br/>");
				String contentLineOne = content.substring(0, content.indexOf("<br/>"));
				String contentLineOneAfter = content.substring(content.indexOf("<br/>"));
		%>
			<div class="title_content">
				<div><%=dto.getConcept()%></div>
				<div><%=dto.getDay()%>일차</div>
				<p>
					<strong><%=contentLineOne%></strong>
					<%=contentLineOneAfter%>
				</p>
			</div>
			<div class="map">
				<div id="map"></div>
			</div>
			<div class="plan">
				<%
				for(RecommendPlanPlaceDto placeDto : dto.getListPlace()) {
				%>
					<div>
						<div class="number fl">
							<div><%=placeDto.getPlaceOrder()%></div>
							<div><%=placeDto.getPlaceTime()%></div>
						</div>
						<div class="place_content fl" data-place_idx=<%=placeDto.getPlaceIdx()%>>
							<img class="fl" src="<%=placeDto.getPlaceImg()%>"/>
							<div class="fl">
								<div><%=placeDto.getPlaceName()%></div>
								<div><%=placeDto.getIntro()%></div>
								<div><%=placeDto.getCategory()%></div>
							</div>
						</div>
					</div>
				<% } %>
			</div>
		<% } %>
		
	<div id="add">나머지 일정 자세히 보기</div>
	<div class="separator"></div>
	
	<%----------------------------------------------------------------------%>
	<div id="content">
		<div class="title">지금 가장 HOT🔥한 방문지 TOP 10</div>
		<div class="subtitle">사람들이 많이 클릭한 관광지・맛집</div>
		<div class="top10">
			<div class="fl">
				<% 
					for(int i=0; i<=4; i++) { 
						PlaceDto dto = listPlaceTop10.get(i);
				%>
					<div class="top10items" data-place_idx="<%=dto.getPlaceIdx()%>">
						<div class="fl">
						<%=i+1%>
						</div>
						<div class="fl">
							<div><%=dto.getName() %></div>
							<div><%=dto.getIntro() %></div>
							<div class="div_star_heart">
								<span>
									<svg width="14" height="14" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M7.27952 1.06957L8.78211 4.79878L12.6289 5.14531C12.8957 5.16947 13.0037 5.51855 12.8011 5.70231L9.88389 8.35313L10.7583 12.2966C10.8189 12.5707 10.5357 12.7862 10.3065 12.6406L7.00059 10.55L3.69465 12.6406C3.46484 12.7856 3.18227 12.57 3.2429 12.2966L4.11729 8.35313L1.19882 5.70167C0.996295 5.51791 1.10423 5.16884 1.37164 5.14468L5.21846 4.79814L6.72106 1.06957C6.82535 0.810144 7.17523 0.810144 7.27952 1.06957Z" fill="#FFDB0C"></path></svg>
									<span><%=dto.getStarScore()%></span>
									<span>(<%=dto.getReviewCnt()%>)·</span>
								</span>
								<span>
									<svg width="14" height="13" viewBox="0 0 14 13" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M9.41934 1.11108C8.1735 1.11108 7.39242 1.92142 6.99989 2.48619C6.60736 1.92142 5.82664 1.11108 4.58081 1.11108C3.07786 1.11108 1.94434 2.32911 1.94434 3.944C1.94434 7.03872 5.15425 9.1375 6.87856 10.2653C6.91539 10.2891 6.95764 10.3014 6.99989 10.3014C7.04214 10.3014 7.08439 10.2891 7.12086 10.2653C8.84553 9.1375 12.0554 7.03872 12.0554 3.944C12.0554 2.32911 10.9223 1.11108 9.41934 1.11108Z" fill="#FD2E69"></path><path d="M9.41934 1.11108C8.1735 1.11108 7.39242 1.92142 6.99989 2.4862C6.60736 1.92142 5.82664 1.11108 4.58081 1.11108C3.07786 1.11108 1.94434 2.32911 1.94434 3.944C1.94434 7.03872 5.15425 9.1375 6.87856 10.2653C6.91539 10.2891 6.95764 10.3014 6.99989 10.3014C7.04214 10.3014 7.08439 10.2891 7.12086 10.2653C8.84553 9.1375 12.0554 7.03872 12.0554 3.944C12.0554 2.32911 10.9223 1.11108 9.41934 1.11108" stroke="#FD2E69" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
									<span><%=dto.getHeartCnt()%></span>
								</span>
							</div>
							<div><%=dto.getCategory() %> · <%=dto.getCityName()%></div>
						
						</div>
						<img class="fr" src="<%=dto.getPlaceImg()%>"/>
						<div style="clear:both;"></div>
					</div>
				<% } %>				
			</div>
			<div class="fr">
				<% 
					for(int i=5; i<=9; i++) { 
						PlaceDto dto = listPlaceTop10.get(i);
				%>
					<div class="top10items" data-place_idx="<%=dto.getPlaceIdx()%>">
						<div class="fl">
						<%=i+1%>
						</div>
						<div class="fl">
							<div><%=dto.getName() %></div>
							<div><%=dto.getIntro() %></div>
							<div class="div_star_heart">
								<span>
									<svg width="14" height="14" viewBox="0 0 14 14" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M7.27952 1.06957L8.78211 4.79878L12.6289 5.14531C12.8957 5.16947 13.0037 5.51855 12.8011 5.70231L9.88389 8.35313L10.7583 12.2966C10.8189 12.5707 10.5357 12.7862 10.3065 12.6406L7.00059 10.55L3.69465 12.6406C3.46484 12.7856 3.18227 12.57 3.2429 12.2966L4.11729 8.35313L1.19882 5.70167C0.996295 5.51791 1.10423 5.16884 1.37164 5.14468L5.21846 4.79814L6.72106 1.06957C6.82535 0.810144 7.17523 0.810144 7.27952 1.06957Z" fill="#FFDB0C"></path></svg>
									<span><%=dto.getStarScore()%></span>
									<span>(<%=dto.getReviewCnt()%>)·</span>
								</span>
								<span>
									<svg width="14" height="13" viewBox="0 0 14 13" fill="none" xmlns="http://www.w3.org/2000/svg"><path fill-rule="evenodd" clip-rule="evenodd" d="M9.41934 1.11108C8.1735 1.11108 7.39242 1.92142 6.99989 2.48619C6.60736 1.92142 5.82664 1.11108 4.58081 1.11108C3.07786 1.11108 1.94434 2.32911 1.94434 3.944C1.94434 7.03872 5.15425 9.1375 6.87856 10.2653C6.91539 10.2891 6.95764 10.3014 6.99989 10.3014C7.04214 10.3014 7.08439 10.2891 7.12086 10.2653C8.84553 9.1375 12.0554 7.03872 12.0554 3.944C12.0554 2.32911 10.9223 1.11108 9.41934 1.11108Z" fill="#FD2E69"></path><path d="M9.41934 1.11108C8.1735 1.11108 7.39242 1.92142 6.99989 2.4862C6.60736 1.92142 5.82664 1.11108 4.58081 1.11108C3.07786 1.11108 1.94434 2.32911 1.94434 3.944C1.94434 7.03872 5.15425 9.1375 6.87856 10.2653C6.91539 10.2891 6.95764 10.3014 6.99989 10.3014C7.04214 10.3014 7.08439 10.2891 7.12086 10.2653C8.84553 9.1375 12.0554 7.03872 12.0554 3.944C12.0554 2.32911 10.9223 1.11108 9.41934 1.11108" stroke="#FD2E69" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path></svg>
									<span><%=dto.getHeartCnt()%></span>
								</span>
							</div>
							<div><%=dto.getCategory() %> · <%=dto.getCityName()%></div>
						
						</div>
						<img class="fr" src="<%=dto.getPlaceImg()%>"/>
						<div style="clear:both;"></div>
					</div>
				<% } %>				
			</div>
			<div style="clear:both;"></div>		
		</div>
	</div>
	<%----------------------------------------------------------------------%>
	
	<div class="separator"></div>
	<div id="blog_content">
		<div class="blog_main_title">해외 실시간 여행기 🧭</div>
		<div class="blog_subtitle">직접 다녀온 추천 일정과 여행 꿀팁 확인하기</div>
		<div class="blog_group">
			<% for(BlogListDto blogDto : listBlog) { %>
			<div class="fl">
				<div class="blog_pic swiper">
					<div class="swiper-wrapper">
						<% for(String blogImg : blogDto.getBlogImgUrlList()) { %>
							<img class="swiper-slide" src="<%=blogImg%>"/>
						<% } %>
					</div>
				</div>
				<div class="blog_container" data-blog_idx="<%=blogDto.getBlogIdx()%>">
					<div style="background-image: url(/upload/<%=blogDto.getProfileImg()%>)"></div>
					<div><%=blogDto.getNickname()%>님의 일정 ・ <%=blogDto.getDays()%></div>
					<div><%=blogDto.getTitle()%></div>
					<div><%=blogDto.getIntro()%></div>
				</div>
			</div>
			<% } %>
			<div style="clear:both;"></div>
		</div>
	</div>
	<div id="back_home">트리플 홈으로 가기</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>