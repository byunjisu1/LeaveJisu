<%@page import="com.js.dto.AiRecommendPlaceDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	String city = (String)session.getAttribute("cityName");
	int days = (int)session.getAttribute("days");
	session.removeAttribute("cityName");
	session.removeAttribute("days");
	List<AiRecommendPlaceDto> listAiPlace = (List<AiRecommendPlaceDto>)request.getAttribute("listAiPlace");
%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_맞춤일정_결과</title>
	<link rel="stylesheet" href="resources/css/AiPlan.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Google Maps -->
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY" defer></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		$(function() {
			moment.locale('ko');
			initMap();
			
			$(document).ready(function() {
			    $(".day_box > div").first().addClass("on");
			});
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			$(".day_box > div").click(function() {
				$(".day_box > div").removeClass("on");
				$(this).addClass("on");
				
				for(let i=0; i<=<%=days%>; i++) {
					$("#day" + i).css("display", "none");
				}
				
				let day = Number($(this).text().substring(4));
				$("#day" + day).css("display", "block");
				
				// 해당 Day의 마커만 지도에 표시하기
			    const filteredData = recommendData.filter(item => item.day === day);
			    updateMap(filteredData);
			});
			$(".plan_box").click(function() {	/* 장소 상세 페이지 */
				let placeIdx = $(this).closest(".day_place").data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$("#add_btn").click(function() {	/* 내 일정으로 담기 */
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
			 	$(".day_place").each(function() {
			 		let day = $(this).closest("[id^='day']").attr("id");	// 위로 올라가며 id가 day로 시작하는 가장 가까운 부모의 id 값
			 		day = parseInt(day.replace("day", ""));
			        let pIdx = $(this).data("place_idx");
			        
			        placeDataList.push({
			            day: day,
			            placeIdx: pIdx
			        });
			    });
			 	
			 	let requestData = {
			 		id: "<%=id%>",
			 		startDate: startDate.format('YYYY-MM-DD'),
			 		days: <%=days%>,
			 		city: "<%=city%>",
			 		placeList: placeDataList
			 	};
				
			 	console.log("전송 데이터:", JSON.stringify(requestData));
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
			$("#again").click(function() {	/* 새 추천 받기 */
				location.href="AiPlanHome";
			});
		});
		
		let map;
		let arr;
		let sumLat;
		let sumLng;
		let avgLat;
		let avgLng;
		let centerPosition;
		let cnt;
		let markers = [];
		
		const recommendData = [
		    <% for(AiRecommendPlaceDto dto : listAiPlace) { %>
		    { day: <%=dto.getDay()%>, label: "<%=dto.getPlaceOrder()%>", name: "<%=dto.getName()%>", lat: <%=dto.getLatitude()%>, lng: <%=dto.getLongitude()%> },
		    <% } %>
		];
		
		function initMap() {
		   	map = new google.maps.Map(document.getElementById("map"), {
		    	center: { lat: 48.861761, lng: 2.353266 }, // 서울 중심 좌표
		    	zoom: 5,
		   	});
		 	// 초기 로딩 시 Day 1 데이터만 표시
		    const firstDayData = recommendData.filter(item => item.day === 1);
		    updateMap(firstDayData);
		}
		
		function updateMap(data) {
		   // 기존 마커들 삭제
		   markers.forEach(marker => marker.setMap(null));
		   markers = [];
		   
		   if(!data || data.length === 0) return;	// 데이터가 없을 경우 종료
			
   			data.forEach(({ label, name, lat, lng }) => {
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
	   			
			   markers.push(marker);
    		});
		   

   			sumLat = 0.0;
   			sumLng = 0.0;
   			cnt = 0;
   			data.forEach(({ lat, lng }) => {
   				if(!(lat==0 && lng==0)) cnt++; 
   				sumLat += lat;
   				sumLng += lng;
   			});
   			avgLat = sumLat / cnt;
   			avgLng = sumLng / cnt;
   			
   			centerPosition = { lat: avgLat, lng: avgLng };
   			
   			map.panTo(centerPosition);
		
		}
	</script>
</head>
<%
	String strBaak = "";
	if(days==1)
		strBaak = "당일치기";
	else
		strBaak = (days-1) + "박 " + days + "일";
%>
<body>
	<div id="header">
		<div id="bell" class="fr" style="top: 22px; left: -77px;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	<div id="header2">
		<img src="https://media.triple.guide/triple-cms/c_limit,f_auto,h_1024,w_1024/255ba179-2542-4701-8b23-d2d26daa4e13.jpeg"/>
		<div><%=city%>, <%=strBaak%><br><span style="color: rgb(54, 143, 255)">AI 추천일정</span>입니다.</div>
		<div>트리플이 알려준 맞춤일정으로 여행을 떠나보세요.</div>
	</div>
	<div id="container">
		<div class="map">
			<div id="map"></div>
		</div>
		<div class="day_box">
			<% for(int i=1; i<=days; i++) { %>
				<div class="fl">Day <%=i%></div>
			<% } %>
			<div style="clear:both;"></div>
		</div>
		<div class="plan_container">
			<%
				int day = 1;
			%>
				<div id="day<%=day%>">
			<%
				for(int i=0; i<=listAiPlace.size()-1; i++) {
					AiRecommendPlaceDto dto = listAiPlace.get(i);
					if(dto.getDay() > day) {
			%>
				</div>
				<div id="day<%=++day%>" style="display:none;">
			<%			
					}
					String mint = "rgb(38, 206, 194)";	// 숙소
					String red = "rgb(255, 97, 105)";	// 음식점 or 카페/디저트
					String purple = "rgb(151, 95, 254)";	// 관광명소
			%>
			<%
					String dayStyle="";
					boolean lastDailySchedule = false;  // 해당 Day의 마지막 스케줄이면 true.
					if(i==listAiPlace.size()-1 || day < listAiPlace.get(i+1).getDay() ) {
						lastDailySchedule = true;
					}
					if(lastDailySchedule)
						dayStyle = "border-left: none;";
			%>
					<div class="day_place" style="<%=dayStyle%>" data-place_idx="<%=dto.getPlaceIdx()%>">
						<div style="background-color: <%=dto.getCategory().equals("숙소") ? mint : (dto.getCategory().equals("관광명소") ? purple : red)%>;"><%=dto.getPlaceOrder()%></div>
						<div class="plan_box">
							<img class="fl" src="<%=dto.getPlaceImg()%>"/>
							<div class="fl">
								<div><%=dto.getName()%></div>
								<div><%=dto.getCategory()%></div>
							</div>
							<div style="clear:both;"></div>
							<div class="plan_line"></div>
							<div>
								<div>소개</div>
								<div><%=dto.getIntro()%></div>
							</div>
						</div>
					</div>
			<% 
				} 
			%>
				</div>
		</div>
	</div>
			
	<div id="like">
		<img src="https://triple.guide/trips/static/images/promotion/customized-schedule/img-heart.png"/>
		<div>추천일정이 마음에 드세요?</div>
		<div>추천받은 일정을 내 일정으로 담으면 언제든 확인하고 편집할 수 있어요!</div>
	</div>
	<div id="box">
		<div id="add_btn" style="background-color: rgb(54, 143, 255); color: rgb(255, 255, 255);">
			<img src="https://triple.guide/trips/static/icons/ico-save-schedule.svg"/>
			<div>내 일정으로 담기</div>
		</div>
		<div id="again">
			<img src="https://triple.guide/trips/static/icons/ico-retry.svg"/>
			<div>다시하기</div>
		</div>
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
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>