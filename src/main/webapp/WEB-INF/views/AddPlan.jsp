<%@page import="com.js.dto.CityListDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<CityListDto> listCity = (List<CityListDto>)request.getAttribute("listCity");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>여행지역 설정</title>
	<link rel="stylesheet" href="resources/css/AddPlan.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />

	<script>
		$(function(){ 
			moment.locale('ko');
			
			$("#header > img:nth-child(1)").click(function() {	/* 뒤로가기 */
				history.back();
			});
			$("#category > span").click(function() {	// 해당 city로 이동
				let categoryName = $(this).text().trim();
			
		        $("#category span").removeClass("on");
		        $(this).addClass("on");

		        // 2. 스크롤 이동 로직
		        if (categoryName === "전체") {
		            $('body, html').stop().animate({ scrollTop: 0 }, 800);
		        } else {
		            let target = $("." + categoryName);
		            if (target.length > 0) {
		                let offsetTop = target.offset().top - 70; // 헤더 높이 제외
		                $('body, html').stop().animate({ scrollTop: offsetTop }, 800);
		            }
		        }
			});
		    let selectedCities = [];
			$(".select_btn").click(function() {	/* 도시 선택 */
		        let cityName = $(this).closest(".item").find(".city_name").text();
		        let cityImg = $(this).closest(".item").find(".city_img").attr("src");
		        let cityIdx = $(this).closest(".item").data("city_idx");

		        // 이미 선택된 도시인지 확인 (중복 방지)
		        if (selectedCities.includes(cityName)) {
		            alert("이미 선택된 도시입니다.");
		            return;
		        }
		        
		     	// 두 번째 도시부터는 화살표를 먼저 추가
		        if (selectedCities.length > 0) {
		            let arrowHtml = `<img class="arrow fl" src="resources/img/right_arrow.png"/>`;
		            $("#mychoose").before(arrowHtml);
		        }

		        // 배열에 추가
		        selectedCities.push(cityName);

		        // 하단 바(choosecity)에 도시 아이콘 추가
		        let cityHtml = `
		            <div class="city fl" data-name="\${cityName}" data-city_idx="\${cityIdx}">
		                <img src="\${cityImg}"/>
	                	<img class="delete_city" src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRvP3CBqD6yBMp35flTRbrZlfgVXJdmzb_feQ&s"/>
		                <p>\${cityName}</p>
		            </div>
		        `;
		        
		        // #mychoose 버튼을 제외한 앞부분에 쌓이도록 함
		        $("#mychoose").before(cityHtml);

		        // 하단 버튼 문구 업데이트
		        updateSelectButton(selectedCities);
			});
			// #choosecity 에서 .delete_city 를 click 했을 때 실행. (동적-지금 있든 나중에 생기든 이름표(클래스)만 맞으면 다 챙긴다)
			$("#choosecity").on("click", ".delete_city", function() {	/* 도시 선택 삭제 */
			    let deleteCity = $(this).closest(".city").attr("data-name");

			    // 2. 배열(selectedCities)에서 해당 이름 제거
			    selectedCities = selectedCities.filter(name => name !== deleteCity);
			    
			    let city = $(this).closest(".city");

			    // 3. 화면 정리
			    // 도시 아이콘 바로 앞에 화살표가 있다면 화살표부터 삭제
			    if (city.prev().hasClass("arrow")) {
			    	city.prev().remove(); 
			    } else if (city.next().hasClass("arrow")) {
			        // 첫 번째 요소를 지울 땐 뒤에 있는 화살표를 지워야 함
			        city.next().remove();
			    }
			    
			    // 도시 아이콘 삭제
			    city.remove();

			    // 4. 예외 처리: 만약 도시를 다 지워서 배열이 비었다면?
			    if (selectedCities.length === 0) {
			        $("#choosecity").html('<div id="mychoose">도시를 선택해주세요</div>');
			    } else {
			        updateSelectButton(selectedCities);
			    }
			});
			$("#mychoose").click(function() {	/* 선택완료 버튼 -> 일정 등록 팝업 */
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
			$("#popup_registertravel > div:first-child > div:first-child").click(function() {	/* 'x' */
				$("#popup_registertravel").css("display", "none");
			});
			$("#popup_registertravel #btn_modify_date").click(function() {	/* 등록 완료 파란 버튼 */
				if(selectedCities.length === 0) {
					alert("선택된 도시가 없습니다.");
					return;
				}
				let startText = $("#start_date").text().substring(0, 10).replaceAll(".", "-");
				let endText = $("#end_date").text().substring(0, 10).replaceAll(".", "-");
				
				let startMoment = moment(startText, "YYYY-MM-DD");
			    let endMoment = moment(endText, "YYYY-MM-DD");
			    let days = endMoment.diff(startMoment, 'days') + 1; // 일수 계산 (당일 포함)
			    
			 	// 1. 가상의 폼 생성
			    let $form = $('<form action="insertPlan" method="POST"></form>');
			 	// 2. 데이터 담기
			    $form.append(`<input type="hidden" name="startDate" value="\${startMoment.format('YYYY-MM-DD')}">`);
			    $form.append(`<input type="hidden" name="days" value="\${days}">`);
			 	// 3. 선택된 여러 개의 도시 IDX 담기
			    $("#choosecity .city").each(function() {
			        let cityIdx = $(this).data("city_idx");
			        $form.append(`<input type="hidden" name="cityIdxList" value="\${cityIdx}">`);
			    });
			 	
			    $("body").append($form);
			    $form.submit();
			});
		});
		
		function updateSelectButton(list) {
	        if(list.length === 0) {
	            $("#mychoose").text("도시를 선택해주세요");
	        } else if(list.length === 1) {
	            $("#mychoose").text(`\${list[0]} 선택완료`);
	        } else {
	            $("#mychoose").text(`\${list[0]} 외 \${list.length - 1}개 선택완료`);
	        }
	    }
	</script>
</head> 	  
<body>
	<div id="header">
		<img src="resources/img/left_arrow.png" alt="왼쪽 화살표"/>
	</div>

	<div id="category">
		<span class="on">전체</span>
		<span>일본</span>
		<span>오세아니아</span>
		<span>유럽</span>
		<span>아메리카</span>
		<span>아시아</span>
	</div>
	<div id="content">
		<%
		String currentCountry = "";
		for(CityListDto dto : listCity) { 
		%>
			<% 
				if(!currentCountry.equals(dto.getCountry())) { 
			%>
				<div class="title <%=dto.getCountry()%>"><%=dto.getCountry()%></div>
			<% 
				currentCountry = dto.getCountry();
				} 
			%>
			<div class="item" data-city_idx="<%=dto.getCityIdx()%>">
				<img class="fl city_img" src="<%=dto.getCityImg()%>"/>
				<div class="fl">
					<p class="city_name"><%=dto.getCityName()%></p>
					<span><%=dto.getNearCity()%></span>
				</div>
				<span class="fr select_btn">선택</span>
				<div style="clear:both;"></div>
			</div>
		<% } %>
		
		<div id="choosecity">
			<div id="mychoose">도시를 선택해주세요</div>
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
</body>
</html>