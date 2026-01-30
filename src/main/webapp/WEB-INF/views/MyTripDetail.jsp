<%@page import="com.js.dto.PlanPlaceMapDto"%>
<%@page import="com.js.dto.PlanDetailDto"%>
<%@page import="com.js.dto.PlanDetailPlaceMemoDto"%>
<%@page import="com.js.dto.PlanDetailPlaceDto"%>
<%@page import="com.js.util.CalculateDate"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<PlanDetailPlaceDto> listPlanDetailPlace = (List<PlanDetailPlaceDto>)request.getAttribute("listPlanDetailPlace");
	List<PlanDetailPlaceMemoDto> listPlanDetailPlaceMemo = (List<PlanDetailPlaceMemoDto>)request.getAttribute("listPlanDetailPlaceMemo");
	int lastDay = (int)request.getAttribute("lastDay");
	List<PlanDetailDto> listPlanDetail = (List<PlanDetailDto>)request.getAttribute("listPlanDetail");
	String date = CalculateDate.formatDateWithDots(listPlanDetail.get(0).getStartDate().split(" ")[0]);
	String lastDate = CalculateDate.calculateDate(listPlanDetail.get(0).getStartDate().split(" ")[0], listPlanDetail.get(0).getDays());
	List<String> allDates = CalculateDate.getDatesBetween(listPlanDetail.get(0).getStartDate().split(" ")[0], listPlanDetail.get(0).getDays());
	List<PlanPlaceMapDto> listPlanPlaceMap = (List<PlanPlaceMapDto>)request.getAttribute("listPlanPlaceMap");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>프로필내 내 여행2(여행 상세 일정 페이지)</title>
	<link rel="stylesheet" href="resources/css/MyTripDetail.css"/>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY" defer></script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<script>
	$(function(){
		// 메모 추가
		$("#popup_creatememo > div:nth-child(3) > div:nth-child(2)").click(function() {
			let memo = $("#memo").val();
			let planPlaceIdx = $(this).closest("#popup_creatememo").data("plan_place_idx");
			
			fetch('insert_memo', {
			    method: 'POST',
			    headers: { 'Content-Type': 'application/json; charset=utf-8' },
			    body: JSON.stringify({ "memo": memo, "planPlaceIdx" : planPlaceIdx })
			})
			.then(response => {
			    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
			    return response.json();
			})
			.then(data => {
				let planPlaceIdx = $(".item.on > div:nth-child(2)").data("plan_place_idx");
				let theElement = $(".item.on");
				while(true) {
					if(theElement.next().hasClass("memoitem"))
						theElement = theElement.next();
					else if(theElement.next().hasClass("item") || theElement.next().hasClass("additem")) 
						break;
				}
				let strInsert = `
					<div class="memoitem" data-memo_idx="\${data}">
						<span class="fl"></span>
						<div class="fl">\${memo}</div>
						<div style="clear:both;"></div>
						<div class="btn_x">x</div>
					</div>
				`;
				theElement.after(strInsert);
				
				$("#memo").val("");
				$("#popup_creatememo").removeData("plan_place_idx");
				$("#popup_creatememo").css("display", "none");
			    $("#black_filter").css("display", "none");
			})
			.catch(error => {
			    alert("에러");
			});
		});
		
		$("#header > img:first-child").click(function() {	/* 뒤로가기 */
			location.href="MyTrip";
		});
		$("#menu").click(function() {	/* 삼지창 */
			$("#menu_open").css("display","block");
			$("#black_filter").css("display","block");
		});
		$(".additem > div:first-child").click(function() {	/* 장소 추가 */
			const params = new URLSearchParams(window.location.search);
			let planIdx = params.get("plan_idx");
			
			let day = $(this).closest(".additem").prevAll(".dayplan").first().data("day");
			location.href="AddPlace?plan_idx=" + planIdx + "&day=" + day;
		});
		$(".item").click(function() {	// 장소 선택하면 item.on
			if($(this).hasClass("on")==false) {
				$(".item").removeClass("on");
				$(this).addClass("on");
			} else {
				$(this).removeClass("on");
			}
		});
		$(".additem > div:nth-child(2)").click(function(){	/* 메모 추가 */
			let planPlaceIdx = $(".item.on").data("plan_place_idx");
			$("#popup_creatememo").attr("data-plan_place_idx", planPlaceIdx);
			$("#popup_creatememo").css("display","block");
			$("#black_filter").css("display","block");
		});
		$("#popup_creatememo > div:nth-child(3) > div:first-child").click(function(){	/* 메모 작성 취소 */
			$("#popup_creatememo").css("display","none");
			$("#black_filter").css("display","none");
		});
		$("#popup_creatememo > div:nth-child(3) > div:nth-child(2)").click(function(){	/* 메모 작성 확인 */
			$("#popup_creatememo").css("display","none");
			$("#black_filter").css("display","none");
		});
		$("#popup_creatememo").on('scroll touchmove mousewheel', function(e) {	/* 이벤트핸들러 전파 X */
		    e.preventDefault();
		    e.stopPropagation();
		    return false;
		});
		$(".item > .btn_x").click(function() {	/* 장소 삭제 */
			let planPlaceIdx = $(this).closest(".item").data("plan_place_idx");
			let dollar_this = $(this);
		
			if(confirm("장소를 삭제하시겠습니까?")) {
				fetch('delete_plan_place', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ planPlaceIdx : planPlaceIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					let removeItem = $(dollar_this).closest(".item");
					removeItem.add(removeItem.next(".memoitem")).remove();
					// add() : 기존 선택자에 새로운 요소 합치기. next(".memoitem")이 존재하지 않으면 $item만 지워짐
					
					location.reload();
				})
				.catch(error => {
				    alert("에러");
				});				
			}
		});
		$(document).on("click", ".memoitem > .btn_x", function() {	/* 메모 삭제 */
			let memoIdx = $(this).closest(".memoitem").data("memo_idx");
			let dollar_this = $(this);
			
			if(confirm("메모를 삭제하시겠습니까?")) {
				fetch('delete_memo', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ memoIdx: memoIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$(dollar_this).closest(".memoitem").remove();
				})
				.catch(error => {
				    alert("에러");
				});
			}
		});
		
		initMap();
		syncHeight();
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

	   	map = new google.maps.Map(document.getElementById("map"), {
	    	center: { lat: 37.5720865, lng: 126.9854332 }, // 서울 중심 좌표
	    	zoom: 12,
	   	});
	   
	   	arr = [
		<%
			for(PlanPlaceMapDto dto : listPlanPlaceMap) {
		%>
   			  { label: "<%=dto.getDay()%>-<%=dto.getPlaceOrder()%>", name: "<%=dto.getName()%>", lat: <%=dto.getLatitude()%>, lng: <%=dto.getLongitude()%> },
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
		
		// 데이터가 있을 때만 실행
	    if (cnt > 0) {
	        avgLat = sumLat / cnt;
	        avgLng = sumLng / cnt;
	        
	        centerPosition = { lat: avgLat, lng: avgLng };
	        map.panTo(centerPosition);
	    } else {
	    	alert("표시할 장소가 없어 기본 위치로 설정됩니다. \n장소를 추가해주세요");
	    }
	}
	
	function syncHeight() {
        // #content의 실제 계산된 높이를 가져옴
        let contentHeight = $("#content").outerHeight();
        
        // body의 높이를 그만큼 강제로 지정
        $("body").css("min-height", contentHeight + "px");
    }
	
</script>
<body>
	<div id="header">
		<img class="fl" src="resources/img/left_arrow.png"/>
		<div id="bell" class="fr" style="top: 20px; left: -90px;"></div>
		<img id="menu" class="fr" src="https://assets.triple.guide/images/ico_navi_menu@4x.png"/>
		<div style="clear:both;"></div>
	</div>
	<div id="content">
		<div id="vertical"></div>
		<div id= "main">
			<p>
				<% 
					String cityName = "";
					for(int i=0; i<listPlanDetail.size(); i++) {
						String currentName = listPlanDetail.get(i).getCityName();
				%>
					<% if(!cityName.equals(currentName)) {
						if(i > 0) {
					%>
							~
						<% } %>
							<span><%=currentName%></span>
					<% 
						cityName = currentName;
						}
					%>
				<% } %>
				여행
			</p>
			<span><%=date%> - <%=lastDate%></span>
		</div>
		<%
		for(int i=1; i<=listPlanDetail.get(0).getDays(); i++) {
			String currentDate = allDates.get(i-1);
		%>
			<div class="dayplan" data-day="<%=i%>">
				<span>day <%=i%></span>
				<span><%=currentDate%></span>
			</div>
			
			<%
			for(PlanDetailPlaceDto dto2 : listPlanDetailPlace) {
				if(i != dto2.getDay()) continue;
			%>
				<div class="item" data-plan_place_idx=<%=dto2.getPlanPlaceIdx()%>>
					<span class="purple fl"><%=dto2.getPlaceOrder()%></span>
					<div class="fl">
						<h3><%=dto2.getName()%></h3>
						<p><%=dto2.getCategory()%></p>
					</div>
					<div style="clear:both;"></div>
					<div class="btn_x">x</div>
				</div>
				<%
				for(PlanDetailPlaceMemoDto memoDto : dto2.getListMemo()) {
				%>
					<div class="memoitem" data-memo_idx="<%=memoDto.getMemoIdx()%>">
						<span class="fl"></span>
						<div class="fl"><%=memoDto.getContent()%></div>
						<div style="clear:both;"></div>
						<div class="btn_x">x</div>
					</div>
				<% } %>
			<% } %>
			
			<div class="additem">
				<div>장소 추가</div>
				<div>메모 추가</div>
			</div>
		<% } %>
		
	</div>
	<div class="map_wrapper">
		<div id="map"></div>
	</div>
	
	<div id="black_filter" style="display:none;"></div>
	
	<div id= "popup_creatememo" style="display:none;">
		<div>메모</div>
		<textarea id="memo" placeholder="잊기 쉬운 정보들을 메모해보세요."></textarea>
		<div>
			<div>취소</div>
			<div>확인</div>
		</div>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>