<%@page import="java.util.List"%>
<%@page import="com.js.dto.PlanCourseDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)request.getAttribute("loginId");
	List<PlanCourseDto> listCourse = (List<PlanCourseDto>)request.getAttribute("listCourse");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_여행코스</title>
	<link rel="stylesheet" href="resources/css/PlanCourse.css"/>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY&callback=initMap" async defer></script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		$(function() {
			moment.locale('ko');
			
			$("#x").click(function() {
				history.back();
			});
			$(".place").click(function() {
				let placeIdx = $(this).data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$("#add_plan").click(function() {	/* 내 일정으로 담기 */
				$("#black_filter").css("display", "block");
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
				$("#black_filter").css("display", "none");
			});
		    $("#popup_registertravel #btn_modify_date").click(function() {	/* 등록 완료 파란 버튼 */
				let startText = $("#start_date").text().substring(0, 10).replaceAll(".", "-");
				let endText = $("#end_date").text().substring(0, 10).replaceAll(".", "-");
				
				let startDate = moment(startText, "YYYY-MM-DD");
			    let endDate = moment(endText, "YYYY-MM-DD");
			    let days = endDate.diff(startDate, 'days') + 1; // 일수 계산 (당일 포함)
			    
			 	let placeDataList = [];
			 	$(".plan > div").each(function() {
			        let day = $(this).closest(".plan").prevAll(".title_content:first").find("div:nth-child(2)").text().replace("일차", "");
			        let pIdx = $(this).data("place_idx");
			        
			        placeDataList.push({
			            day: parseInt(day),
			            placeIdx: pIdx
			        });
			    });
			 	
			 	let requestData = {
			 		id: "<%=id%>",
			 		startDate: startDate.format('YYYY-MM-DD'),
			 		days: days,
			 		cityIdx: new URLSearchParams(window.location.search).get("city_idx"),
			 		placeList: placeDataList
			 	};
				
			 	fetch('insert_plan', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify(requestData)
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$("#popup_registertravel").css("display", "none");
					$("#black_filter").css("display", "none");
					alert("내 일정으로 담았습니다. \n담은 일정은 프로필 내 '내 여행'에서 다시 확인하실 수 있습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
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
			let mapDiv;
			let mapContainer;

			<%
				int mapDay = 0;
				for(PlanCourseDto dto : listCourse) {
					if(mapDay == dto.getDay()) continue;
			%>
			
		   	arr = [
			<%
				for(PlanCourseDto dto2 : listCourse) {
				if(dto.getDay() != dto2.getDay()) continue;
				mapDay = dto.getDay();
			%>
	   			  { label: "<%=dto2.getPlaceOrder()%>", name: "<%=dto2.getName()%>", lat: <%=dto2.getLatitude()%>, lng: <%=dto2.getLongitude()%> },
	   		<%
				}
	   		%>
   			];
			
		 	// 장소가 없는 경우 처리
		    mapDiv = document.getElementById("map<%=dto.getDay()%>");
		    if (mapDiv) {
	            mapContainer = mapDiv.closest('.map');

			    if (arr.length === 0) {
			        if (mapContainer) {
			            mapContainer.style.display = "none"; // 부모 컨테이너까지 숨김
			        } else {
			            mapDiv.style.display = "none"; // 부모가 없으면 지도 영역만 숨김
			        }
			    } else {	// 장소가 있는 경우 지도 생성
			    	map = new google.maps.Map(document.getElementById("map<%=dto.getDay()%>"), {
				    	center: { lat: 37.5665, lng: 126.9780 }, // 서울 중심 좌표
				    	zoom: 12,
				   	});
				 	
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
		    }
   			
		<% } %>
		
		}
	</script>
</head>
<body>
	<div id="header">
		<img src="https://assets.triple.guide/images/img_intro_logo_dark.svg"/>
		<div id="x" class="fr">X</div>
		<div style="clear:both;"></div>
	</div>
	
	<% 
		int day = 0;
		for(PlanCourseDto dto : listCourse) {
			if(day == dto.getDay()) continue;
	%>
		<div class="day">
			<div class="map">
				<div id="map<%=dto.getDay()%>"></div>
			</div>
			<div class="day_main">
				<div>Day <%=dto.getDay()%></div>
				<div>
					<% for(PlanCourseDto dto2 : listCourse) {
						if(dto.getDay() != dto2.getDay()) continue;
						day = dto.getDay();
						String mint = "rgb(38, 206, 194)";	// 숙소
						String red = "rgb(255, 97, 105)";	// 음식점 or 카페/디저트
						String purple = "rgb(151, 95, 254)";	// 관광명소
					%>
						<div class="place" data-place_idx="<%=dto.getPlaceIdx()%>">
							<div class="number">
								<div style="background-color: <%=dto2.getCategory().equals("숙소") ? mint : (dto2.getCategory().equals("관광명소") ? purple : red)%>"><%=dto2.getPlaceOrder()%></div>
								<div></div>
							</div>
							<div class="place_detail">
								<div><%=dto2.getName()%></div>
								<div><%=dto2.getCategory()%></div>
							</div>
						</div>
					<% } %>
				</div>
			</div>
		</div>
	<% } %>
	<div id="add_plan">
		<img width="18" height="18" src="https://assets.triple.guide/images/ico_lounge_download_white@4x.png" alt="">
		<div>내 일정으로 담기</div>
	</div>
	
	<div id="black_filter" style="display:none;"></div>
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
</body>
</html>