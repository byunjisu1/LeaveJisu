<%@page import="java.util.Comparator"%>
<%@page import="com.js.dto.FlightDto"%>
<%@page import="com.js.dto.AirportListDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	List<AirportListDto> listAirport = (List<AirportListDto>)request.getAttribute("listAirport");
	List<FlightDto> listFlight = (List<FlightDto>)request.getAttribute("listFlight");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_항공권</title>
	<link rel="stylesheet" href="resources/css/AirlineTicket.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		$(function() {
			$("#menu").click(function() {
				$("#black_filter").css("display", "block");
				$("#menu_open").css("display", "block");
			});
			$("#departure, #destination").click(function() {
				$("#black_filter").css("display", "block");
				$("#popup_air").css("display", "block");
				
				// 클릭한 대상에 따라 활성화 표시
		        if (this.id === "departure") {
		            $("#departure_box").addClass("active_date_box");
		            $("#destination_box").removeClass("active_date_box");
		        } else {
		            $("#destination_box").addClass("active_date_box");
		            $("#departure_box").removeClass("active_date_box");
		        }
			});
		    $(document).on("click", "#popup_air .air_list", function() {	/* 공항 리스트 데이터 동기화 */
		        let airportName = $(this).find(".airportName").text();
		        let airportId = $(this).find(".airportId").text(); 
		        let country = $(this).find(".country > span").text();              

		        if ($("#departure_box").hasClass("active_date_box")) {	// 출발지 변경
		            $("#departure .option").text(airportId.replace('(', '').replace(')', ''));	/* 메인화면 */
		            
		            // 팝업 상단(popup_air) 헤더 변경
		           updateUI("#departure_box", airportName, airportId, country);

		            // 날짜 팝업 상단의 출발지 정보도 동기화
		           updateUI("#popup_air_day .air_day_header > div:nth-child(1)", airportName, airportId, country);
		        } else {	// 도착지 변경
		            $("#destination .option").text(airportId.replace('(', '').replace(')', ''));

		            // 팝업 상단(popup_air) 헤더 변경
		            updateUI("#destination_box", airportName, airportId, country);
		            
		         	// 날짜 팝업 상단의 도착지 정보도 동기화
		            updateUI("#popup_air_day .air_day_header > div:nth-child(2)", airportName, airportId, country);
		        }

		        $("#black_filter").css("display", "none");
		        $("#popup_air").css("display", "none");
		    });
			
		    let todayStr = moment().format('YYYY-MM-DD');
		    let nextWeekStr = moment().add(7, 'days').format('YYYY-MM-DD');

		    // 페이지 로드 시 메인 화면 날짜, 팝업 상단 날짜 초기화(오늘 날짜)
		    $("#departure_date .option").text(todayStr);
		    $("#destination_date .option").text(nextWeekStr);
		    $(".air_day_header .start_date").text(todayStr);
		    $(".air_day_header .end_date").text(nextWeekStr);
			
		    $("#departure_date").click(function() {	/* 출발 날짜 */
		        openDatePopup();
		        $("#start_date_box").addClass("active_date_box");		/* 파란 테두리 */
		        $("#end_date_box").removeClass("active_date_box");
		    });

		    $("#destination_date").click(function() {	/* 도착 날짜 */
		        openDatePopup();
		        $("#end_date_box").addClass("active_date_box");
		        $("#start_date_box").removeClass("active_date_box");
		    });
			$('input[name="daterangepicker"]').daterangepicker({	/* date picker */
				// 달력이 팝업 안에 고정되도록 설정
			    parentEl: "#popup_air_day",
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
					$(".air_day_header .start_date").text(start.format('YYYY-MM-DD'));
				    $(".air_day_header .end_date").text(end.format('YYYY-MM-DD'));
				    
				    $("#departure_date .option").text(start.format('YYYY-MM-DD'));
				    $("#destination_date .option").text(end.format('YYYY-MM-DD'));
			});
			// 달력이 닫히려고 할 때 바로 다시 열기
		    $('input[name="daterangepicker"]').on('hide.daterangepicker', function(ev, picker) {
		        // 약간의 시차를 두어 다시 열리게 함
		        setTimeout(function() {
		            picker.show();
		        }, 10);
		    });

			$("#btn_minus").click(function() {
				let num = $("#num").text();
				$("#num").text(Number(num) - 1);
				if($("#num").text() < 1) { $("#num").text(1); }
			});
			$("#btn_plus").click(function() {
				let num = $("#num").text();
				$("#num").text(Number(num) + 1);
				if($("#num").text() > 8) { $("#num").text(8); }
			});
			
			$("#end_date_box > div:nth-child(3), #destination_box > div:nth-child(3), #black_filter").click(function() {	/* 팝업 닫기 */
				$("#black_filter").css("display", "none");
				$("#popup_air").css("display", "none");
				$("#popup_air_day").css("display", "none");
			});
			
			$("#search").click(function() {	/* 검색 */
				$('#loading').show();
				$("#departure_list").empty().append("<div>가는 편 목록</div>");
			    $("#destination_list").empty().append("<div style='margin-top: 30px;'>오는 편 목록</div>");
			    
				// 가는 거 (항공편)
				let departure = $("#departure > .option").text();
				let destination = $("#destination > .option").text();
				let departureDate = $("#departure_date > .option").text();
				let pNum = $(".p_num > #num").text();
				
				fetch('departure_ticket', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ 
						departure:departure,
						destination:destination,
						departureDate:departureDate,
						pNum:pNum
				    })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(listResponse => {
					console.log(listResponse);
					
					listResponse.forEach(function(item, idx) {
						let str = `
							<div class="air">
								<div>
							`;
						if(idx==0) {	
							str+='# 최저가 추천';
						}
						str+=`
								</div>	
								<div>	<!-- 출발 항공편 -->
									<div class="logo fl">
										<img src="\${item.airlineLogo}"/>
									</div>
									<div class="air_info fl">
										<div>\${item.airlineName}</div>
										<div>
											<div>\${item.airportIdDeparture}</div>
											<div>\${item.timeDeparture}</div>
											<div>-</div>
											<div>\${item.airportIdDestination}</div>
											<div>\${item.timeDestination}</div>
										</div>
									</div>
									<div class="air_hour_price fr">
										<div>\${item.transitType} <span style="color: rgba(58, 58, 58, 0.3);">\${item.flightTime}</span></div>
										<div>\${item.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}원</div>
									</div>
									<div style="clear:both;"></div>
								</div>
							</div>
						`;
						
						$("#departure_list").append(str);
					});
					
					// 오는 거 (항공편)
					departure = $("#destination > .option").text();
					destination = $("#departure > .option").text();
					departureDate = $("#destination_date > .option").text();
					pNum = $(".p_num > #num").text();

					return fetch('departure_ticket', {
					    method: 'POST',
					    headers: { 'Content-Type': 'application/json; charset=utf-8' },
					    body: JSON.stringify({ 
							departure:departure,
							destination:destination,
							departureDate:departureDate,
							pNum:pNum
					    })
					})
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(listResponse => {
					console.log(listResponse);  
					
					listResponse.forEach(function(item, idx) {
						let str = `
							<div class="air">
								<div>
							`;
						if(idx==0) {	
							str+='# 최저가 추천';
						}
						str+=`
								</div>	
								<div>	<!-- 도착 항공편 -->
									<div class="logo fl">
										<img src="\${item.airlineLogo}"/>
									</div>
									<div class="air_info fl">
										<div>\${item.airlineName}</div>
										<div>
											<div>\${item.airportIdDeparture}</div>
											<div>\${item.timeDeparture}</div>
											<div>-</div>
											<div>\${item.airportIdDestination}</div>
											<div>\${item.timeDestination}</div>
										</div>
									</div>
									<div class="air_hour_price fr">
										<div>\${item.transitType} <span style="color: rgba(58, 58, 58, 0.3);">\${item.flightTime}</span></div>
										<div>\${item.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")}원</div>
									</div>
									<div style="clear:both;"></div>
								</div>
							</div>
						`;
						
						$("#loading").hide();
						$("#air_header, #air_container").css("display", "block");
						$("#destination_list").append(str);
					});
				})
				.catch(error => {
					$("#loading").hide();
				    alert("에러");
				});
			});
		});
		// 출발 날짜 / 도착 날짜 공통 팝업
	    function openDatePopup() {
	    	$("#black_filter").css("display", "block");
			$("#popup_air_day").css("display", "block");
	        
	        // 현재 메인 화면의 날짜를 팝업 상단에 동기화
	        $(".air_day_header .start_date").text($("#departure_date .option").text());
	        $(".air_day_header .end_date").text($("#destination_date .option").text());
	        
	        setTimeout(function() {
	            $('input[name="daterangepicker"]').click();
	        }, 100);
	    }		
		// 출발지 / 도착지 화면 업데이트
	    function updateUI(containerSelector, name, id, country) {
	        const $target = $(containerSelector);
	        $target.find(".target_name").text(name);
	        $target.find(".target_id").text(id);
	        $target.find(".target_country").text(country);
	    }
	</script>
</head>
<body>
	<div id="header">
		<img class="fl" src="https://assets.triple.guide/images/img_intro_logo_dark.svg"/>
		<div id="bell" class="fr" style="top: 13px; left: -85px;"></div>
		<div id="menu" class="fr"></div>
		<div style="clear:both;"></div>
	</div>
	<div id="header2">
		<div>쉽고 빠른 항공권 조회</div>
	</div>
	<div id="option_container">
		<div id="departure" class="fl">
			<div>
				<img src="https://triple.guide/air/static/images/img-hub-departure@3x.png"/>
				<div>출발지</div>
			</div>
			<div class="option">ICN</div>
		</div>
		<div id="destination" class="fl">
			<div>
				<img src="https://triple.guide/air/static/images/img-hub-arrival@3x.png"/>
				<div>도착지</div>
			</div>
			<div class="option">NRT</div>
		</div>
		<div id="departure_date" class="fl">
			<div>
				<img src="https://triple.guide/air/static/images/img-hub-date@3x.png"/>
				<div>출발 날짜</div>
			</div>
			<div class="option">2025-12-25</div>
		</div>
		<div id="destination_date" class="fl">
			<div>
				<img src="https://triple.guide/air/static/images/img-hub-date@3x.png"/>
				<div>도착 날짜</div>
			</div>
			<div class="option">2025-12-27</div>
		</div>
		<div class="fl">
			<div>
				<img src="https://triple.guide/air/static/images/img-hub-man@3x.png"/>
				<div>인원수</div>
			</div>
			<div class="p_num">
				<div id="btn_minus">-</div>
				<div id="num">1</div>
				<div id="btn_plus">+</div>
			</div>
		</div>
		<div class="clear:both;"></div>
	</div>
	<div id="search">검색</div>
	<div id="loading" style="display: none;">
		<img src="resources/img/loading.gif" class="loading" style="width:50px;">
	</div>
	
	<div id="air_header" style="display: none;">항공편 목록</div>
	<div id="air_container" style="display: none;">
		<div id="departure_list">
		</div>
		<div id="destination_list">
		</div>
	</div>
	
	<div id="black_filter" style="display: none;"></div>
	
	<div id="popup_air" style="display: none;">
		<div class="air_start_header">
			<div id="departure_box" border-top-left-radius: 20px;">
				<div class="title">출발지</div>
				<div class="air_list">
					<img class="fl" src="resources/img/airplane.png"/>
					<div class="fl">
						<div class="target_name fl" style="margin-right: 3px;">인천 국제</div>
						<div class="target_id fl">(ICN)</div>
						<div class="country"><span style="color: rgb(150, 150, 150);">대한민국</span></div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div id="destination_box">
				<div class="title">도착지</div>
				<div class="air_list">
					<img class="fl" src="resources/img/airplane.png"/>
					<div class="fl">
						<div class="target_name fl" style="margin-right: 3px;">도쿄 나리타</div>
						<div class="target_id fl">(NRT)</div>
						<div class="country"><span style="color: rgb(150, 150, 150);">일본</span></div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
		<div>
			<% for(AirportListDto dto : listAirport) { %>
				<div class="air_list">
					<img class="fl" src="resources/img/airplane.png"/>
					<div class="fl">
						<div class="airportName fl" style="margin-right: 3px;"><%=dto.getAirportName()%></div>
						<div class="airportId fl">(<%=dto.getAirportId()%>)</div>
						<div class="country"><span style="color: rgb(150, 150, 150);"><%=dto.getCountry()%></span></div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			<% } %>
		</div>
	</div>
	
	<div id="popup_air_day" style="display: none;">
		<div class="air_day_header">
			<div style="border-top-left-radius: 20px;">
				<div class="title">출발지</div>
				<div class="air_list">
					<img class="fl" src="resources/img/airplane.png"/>
					<div class="fl">
						<div class="target_name fl" style="margin-right: 3px;">인천 국제</div>
						<div class="target_id fl">(ICN)</div>
						<div><span class="target_country" style="color: rgb(150, 150, 150);">대한민국</span></div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div class="title">도착지</div>
				<div class="air_list">
					<img class="fl" src="resources/img/airplane.png"/>
					<div class="fl">
						<div class="target_name fl" style="margin-right: 3px;">도쿄 나리타</div>
						<div class="target_id fl">(NRT)</div>
						<div><span class="target_country" style="color: rgb(150, 150, 150);">일본</span></div>
						<div style="clear:both;"></div>
					</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div id="start_date_box">
				<div class="title">출발 날짜</div>
				<div class="start_date">2025-12-25</div>
			</div>
			<div id="end_date_box" style="border-top-right-radius: 20px;">
				<div class="title">도착 날짜</div>
				<div class="end_date">2025-12-27</div>
				<div>X</div>
			</div>
		</div>
		<div>
			<input type="text" name="daterangepicker" value="01/01/2026 - 01/01/2026" style="display: none;"/>
		</div>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>