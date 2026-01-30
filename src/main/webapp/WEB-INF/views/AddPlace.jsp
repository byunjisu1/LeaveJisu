<%@page import="com.js.dto.AddPlaceDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
	List<AddPlaceDto> listAddMyPlace = (List<AddPlaceDto>)request.getAttribute("listAddMyPlace");
	List<AddPlaceDto> listAddRecommendPlace = (List<AddPlaceDto>)request.getAttribute("listAddRecommendPlace");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>장소선택</title>
	<link rel="stylesheet" href="resources/css/AddPlace.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY" defer></script>
	<script>
		$(function() {
			initMap();
			
			$("#places > span").click(function() {
				$("#places > span").removeClass("on");
				$(this).addClass("on");
				let idx = $(this).index(); // 0 : 추천장소, 1: 찜한장소
				if(idx==0) {
					//추천장소
					$("#recommend_place_items").css("display","block");
					$("#myplace_items").css("display","none");
					updateMap(recommendData);
				}else { //찜한장소
					$("#recommend_place_items").css("display","none");
					$("#myplace_items").css("display","block");
					updateMap(myPlaceData);
				}
			});
			$("#finish_choose").html('<div id="choose">장소를 선택해주세요</div>');
		    let selectedPlaces = [];
			$(".select_btn").click(function() {	/* 장소 선택 */
				let item = $(this).closest(".recommend_place_item, .myplace_item");
		        let placeName = item.find(".place_name").text();
		        let placeImg = item.find(".place_img").attr("src");
		        let placeIdx = item.find(".place_idx").val();

		        // 이미 선택된 장소인지 확인 (중복 방지)
		        if (selectedPlaces.includes(placeName)) {
		            alert("이미 선택된 장소입니다.");
		            return;
		        }

		        // 배열에 추가
		        selectedPlaces.push(placeName);

		        // 하단 바(finish_choose)에 장소 아이콘 추가
		        let placeHtml = `
		        	<div class="finish_place fl" data-name="\${placeName}">
						<div class="mypickplace" data-place_idx="\${placeIdx}">
							<img src="\${placeImg}"/>
							<div>\${placeName}</div>
							<img class="delete_place" src="resources/img/itemx.png"/>
						</div>
					</div>
		        `;
		        
		        // #choose 버튼을 제외한 앞부분에 쌓이도록 함
		        $("#choose").before(placeHtml);

		        // 하단 버튼 문구 업데이트
		        updateSelectButton(selectedPlaces);
			});
			// #finish_choose 에서 .delete_place 를 click 했을 때 실행. (동적-지금 있든 나중에 생기든 이름표(클래스)만 맞으면 다 챙긴다)
			$("#finish_choose").on("click", ".delete_place", function() {	/* 도시 선택 삭제 */
			    let deletePlace = $(this).closest(".finish_place").attr("data-name");

			    // 2. 배열(selectedPlaces)에서 해당 이름 제거
			    selectedPlaces = selectedPlaces.filter(name => name !== deletePlace);
			    
			    let place = $(this).closest(".finish_place");
			    
			    // 장소 아이콘 삭제
			    place.remove();

			    // 4. 예외 처리: 만약 장소를 다 지워서 배열이 비었다면?
			    if (selectedPlaces.length === 0) {
			        $("#finish_choose").html('<div id="choose">장소를 선택해주세요</div>');
			    } else {
			        updateSelectButton(selectedPlaces);
			    }
			});
			$("#finish_choose").on("click", "#choose", function() {
				if(selectedPlaces.length === 0) {
					alert("장소를 선택해주세요.");
					return;
				}
				
				const urlParams = new URLSearchParams(window.location.search);
			    let planIdx = urlParams.get("plan_idx");
			    let day = urlParams.get("day");
			    
			    
			 	// 1. 가상의 폼 생성
			    let $form = $('<form action="insertPlanPlaces" method="POST"></form>');
			 	// 2. 데이터 담기
			    $form.append(`<input type="hidden" name="planIdx" value="\${planIdx}">`);
			    $form.append(`<input type="hidden" name="day" value="\${day}">`);
			 	// 3. 선택된 여러 개의 장소 IDX 담기
			    $(".finish_place .mypickplace").each(function() {
			        let pIdx = $(this).data("place_idx");
			        $form.append(`<input type="hidden" name="placeIdxList" value="\${pIdx}">`);
			    });
			 	
			    $("body").append($form);
			    $form.submit();
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
		    <% for(AddPlaceDto dto : listAddRecommendPlace) { %>
		    { label: "<%=dto.getName().substring(0, 1)%>", name: "<%=dto.getName()%>", lat: <%=dto.getLatitude()%>, lng: <%=dto.getLongitude()%> },
		    <% } %>
		];
		const myPlaceData = [
		    <% for(AddPlaceDto dto : listAddMyPlace) { %>
		    { label: "<%=dto.getName().substring(0, 1)%>", name: "<%=dto.getName()%>", lat: <%=dto.getLatitude()%>, lng: <%=dto.getLongitude()%> },
		    <% } %>
		];
		
		function initMap() {
		   	map = new google.maps.Map(document.getElementById("map"), {
		    	center: { lat: 48.861761, lng: 2.353266 }, // 서울 중심 좌표
		    	zoom: 12,
		   	});	
		   	updateMap(recommendData);	// 초기 실행 시 추천장소 표시
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
		function updateSelectButton(list) {
	        if(list.length === 0) {
	            $("#choose").text("장소를 선택해주세요");
	        } else if(list.length === 1) {
	            $("#choose").text(`\${list[0]} 선택완료`);
	        } else {
	            $("#choose").text(`\${list[0]} 외 \${list.length - 1}개 선택완료`);
	        }
	    }
	</script>
</head>
<body>
	<div id="map_container">
		<div id="map"></div>
	</div>
	<div id="chooseplace">
		<div></div>
		<div id="places">
			<span class="on">추천 장소</span>
			<span>찜한 장소</span>
		</div>
	</div>
	<div id="recommend_place_items">
		<% for(AddPlaceDto dto : listAddRecommendPlace) { %>
			<div class="recommend_place_item">
				<input type="hidden" class="place_idx" value="<%=dto.getPlaceIdx()%>"/>
				<img class="place_img" src="<%=dto.getPlaceImg()%>"/>
				<span class="place_name"><%=dto.getName()%></span>
				<span><%=dto.getCategory()%> . <%=dto.getCityName()%></span>
				<span class="select_btn">선택</span>
			</div>
		<% } %>
	</div>
	<div id="myplace_items" style="display: none;">
		<% for(AddPlaceDto dto : listAddMyPlace) { %>
			<div class="myplace_item">
				<input type="hidden" class="place_idx" value="<%=dto.getPlaceIdx()%>"/>	
				<img class="place_img" src="<%=dto.getPlaceImg()%>"/>
				<span class="place_name"><%=dto.getName()%></span>
				<span><%=dto.getCategory()%> . <%=dto.getCityName()%></span>
				<span class="select_btn">선택</span>
			</div>
		<% } %>
	</div>
	
	<div id="finish_choose">
		<div id="choose">1개의 장소 담기</div>
	</div>
</body>
</html>