<%@page import="com.js.dto.ReviewListDto"%>
<%@page import="com.js.dto.MyReviewListDto"%>
<%@page import="com.js.util.CalculateDate"%>
<%@page import="com.js.dto.UpcomingPlanListDto"%>
<%@page import="com.js.dto.MemberDto"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	MemberDto memberDto = (MemberDto)request.getAttribute("memberDto");
	List<UpcomingPlanListDto> listUpcomingPlan = (List<UpcomingPlanListDto>)request.getAttribute("listUpcomingPlan");
	List<MyReviewListDto> listMyReview = (List<MyReviewListDto>)request.getAttribute("listMyReview");
	List<MyReviewListDto> listMyCityReview = (List<MyReviewListDto>)request.getAttribute("listMyCityReview");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>프로필내 내 여행_내 여행 페이지</title>
	<link rel="stylesheet" href="resources/css/MyTrip.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>

	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/momentjs/latest/moment.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />
	
	<script>
		let picker;
	
		function getDuration(startDate, endDate) {	// 여행 기간 구하기
		    // 1. "YYYY/MM/DD" 문자열을 Date 객체로 변환
		    const start = new Date(startDate);
		    const end = new Date(endDate);
	
		    // 2. 두 날짜의 밀리초 차이 계산
		    const diffInMs = end - start;
	
		    // 3. 밀리초를 일(day) 단위로 변환
		    // (1000ms * 60s * 60m * 24h = 86,400,000ms)
		    const diffInDays = diffInMs / (1000 * 60 * 60 * 24);
	
		    return Math.floor(diffInDays+1);  // 당일 포함
		}
		
		$(function() {
			let planCount = $(".myplan").length;
		    $("#category > p > span").text(planCount);
			$("#menu > img:nth-child(2)").click(function() {
				$("#menu_open").css("display","block");
				$("#black_filter").css("display","block");
			});
			$(document).on("click", "#category > p", function() {
				$("#category > p").removeClass("on");
				$(this).addClass("on");
				
				let idx = $(this).index(); // 0 : 내 여행, 1: 리뷰
				if(idx==0) {
					//내 여행
					$("#createtrip").css("display","block");
					$("#upcomingtrip").css("display","block");
					$("#city").css("display","none");
					$("#myreview").css("display","none");
					$(".bottom").css("display","none");
				}else { //리뷰
					$("#createtrip").css("display","none");
					$("#upcomingtrip").css("display","none");
					$("#city").css("display","block");
					$("#myreview").css("display","block");
					$(".bottom").css("display","flex");
				}
			});
			const urlParams = new URLSearchParams(window.location.search);
		    const tab = urlParams.get('tab');
		    const reviewIdx = urlParams.get('reviewIdx');
		    const idxType = urlParams.get('idxType'); // 'p' (장소) 또는 'c' (도시)

		    if (tab === 'review') {
		    	// 리뷰 탭으로 들어왔을 때
		        $("#category > p").eq(1).trigger("click");
		    	
		     	// 해당 리뷰로 스크롤 이동
		        if (reviewIdx && idxType) {
		            setTimeout(function() {
		                let $target;

		                // idxType에 따라 찾는 영역 분기
		                if (idxType === 'p') {
		                    // 장소 리뷰 영역에서 찾기
		                    $target = $("#place_review .review[data-review_idx='" + reviewIdx + "']");
		                } else if (idxType === 'c') {
		                    // 도시 리뷰 영역에서 찾기
		                    $target = $("#city_review .review[data-review_idx='" + reviewIdx + "']");
		                }

		                // 3. 대상이 존재하면 스크롤 및 효과 적용
		                if ($target && $target.length > 0) {
		                    $('html, body').animate({
		                        scrollTop: $target.offset().top - 120
		                    }, 600);

		                    // 시각적 강조 (CSS 클래스 추가)
		                    $target.addClass("highlight_review");
		                    
		                    setTimeout(function() {
		                        $target.removeClass("highlight_review");
		                    }, 3000);
		                }
		            }, 500); // 탭 전환 애니메이션 시간을 고려해 약간 넉넉히 대기
		        }
		    }
			$("#createtrip").click(function() {	/* 여행 일정 만들기 */
				$("#popup_createplan").css("display","block");
				$("#black_filter").css("display","block");
			});
			$("#popup_createplan > div:nth-child(2) > div:nth-child(3)").click(function() {	/* 직접 일정 만들기 */
				$("#popup_createplan").css("display","none");
				$("#black_filter").css("display","none");
				location.href="AddPlan";
			});
			$("#popup_createplan > div:nth-child(2) > div:nth-child(4)").click(function() {	/* AI 일정 추천받기 */
				$("#popup_createplan").css("display","none");
				$("#black_filter").css("display","none");
				location.href="AiPlanHome";
			});
			$("#popup_createplan > div:nth-child(1) > div:nth-child(1)").click(function() {	/* X */
				$("#popup_createplan").css("display","none");
				$("#black_filter").css("display","none");
			});
			$(".myplan").click(function()	{	/* 내 여행 상세 페이지 */
				let planIdx = $(this).data("plan_idx");
				location.href="MyTripDetail?plan_idx=" + planIdx;
			});
			
			$('input[name="daterangepicker"]').daterangepicker({
				// 달력이 팝업 안에 고정되도록 설정
			    parentEl: "#popup_changedate", 
			    alwaysShowCalendars: true,
			    autoApply: true,
				}, function(start, end, label) {
					$("#popup_changedate").attr("startDate", start.format('YYYY/MM/DD'));
					$("#popup_changedate").attr("days", getDuration(start, end));
					
					let arrDays = ['', '월', '화', '수', '목', '금', '토', '일'];
					$("#start_date").text(start.format('YYYY.MM.DD') + "(" + arrDays[start.format('E')] + ")");
					$("#end_date").text(end.format('YYYY.MM.DD') + "(" + arrDays[end.format('E')] + ")");
			});
			$('input[name="daterangepicker"]').on('hide.daterangepicker', function(ev, picker) {
			    if($("#popup_changedate").is(":visible")) {
			        // 루프 방지를 위해 아주 약간의 시간차를 두고 다시 띄움
			        setTimeout(function() {
			            picker.show();
			        }, 1);
			    }
			});
			
			$(".edit_btn").click(function() {	/* 여행 날짜 수정 팝업 */
				let planIdx = $(this).closest(".myplan").data("plan_idx");
				$("#popup_changedate").attr("data-plan_idx", planIdx);
				$("#popup_changedate").css("display","block");
				$("#black_filter").css("display","block");
				
				const startDate = $(this).closest(".myplan").find("div:nth-child(2) > div:nth-child(2)").text().substring(0, 13);
				const endDate = $(this).closest(".myplan").find("div:nth-child(2) > div:nth-child(2)").text().substring(16);
				$("#start_date").text(startDate);	// 등록 버튼의 일자에 값 입히기
				$("#end_date").text(endDate);
				
				//alert(startDate + " / " + endDate);    // 2026.01.15(목) / 2026.01.18(일)
				const y1 = startDate.substring(0, 4);
				const m1 = startDate.substring(5, 7);
				const d1 = startDate.substring(8, 10);
				const y2 = endDate.substring(0, 4);
				const m2 = endDate.substring(5, 7);
				const d2 = endDate.substring(8, 10);
				
				var drp = $('input[name="daterangepicker"]').data('daterangepicker');
				drp.setStartDate(moment(`\${y1}/\${m1}/\${d1}`));
				drp.setEndDate(moment(`\${y2}/\${m2}/\${d2}`));
				drp.updateView(); // 캘린더 화면을 초기화한 날짜로 강제 이동 및 갱신
				
				// 팝업 열릴 때 바로 달력 표시
			    $('input[name="daterangepicker"]').data('daterangepicker').show();
			});
			$("#popup_changedate > div:first-child > div:first-child").click(function() { /* 'x' */
				$("#popup_changedate").css("display","none");
				$("#black_filter").css("display","none");
			});
			$("#popup_changedate > div:nth-child(5)").click(function() {	/* 완료 파란 버튼 */
				const planIdx = $("#popup_changedate").data("plan_idx");
				const startDate = $("#popup_changedate").attr("startDate");
				const days = $("#popup_changedate").attr("days");
				
				fetch('update_plan_date', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ planIdx: planIdx, startDate: startDate, days: days })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$(".myplan").each(function(idx, item) {
						if($(item).data("plan_idx")!=planIdx) return;
						
				        const week = ['일', '월', '화', '수', '목', '금', '토'];

				    	 // 2. startDate("2026/01/21")를 "/" 기준으로 쪼개서 숫자로 변환
				        const parts = startDate.split("/");
				        const year = parseInt(parts[0]);
				        const month = parseInt(parts[1]) - 1; // 월은 0부터 시작하므로 1을 뺌
				        const day = parseInt(parts[2]);

				        // 3. 시작 날짜 객체 생성 및 포맷팅
				        const sObj = new Date(year, month, day);
				        const sYear = sObj.getFullYear();
				        const sMonth = String(sObj.getMonth() + 1).padStart(2, '0');
				        const sDate = String(sObj.getDate()).padStart(2, '0');
				        const sDay = week[sObj.getDay()];
				        const sDateDisp = sYear + "." + sMonth + "." + sDate + "(" + sDay + ")";

				        // 4. 종료 날짜 계산 (시작일 + 9일 - 1일 = 8일 뒤)
				        const eObj = new Date(year, month, day);
				        eObj.setDate(eObj.getDate() + (parseInt(days) - 1));

				        const eYear = eObj.getFullYear();
				        const eMonth = String(eObj.getMonth() + 1).padStart(2, '0');
				        const eDate = String(eObj.getDate()).padStart(2, '0');
				        const eDay = week[eObj.getDay()];
				        const eDateDisp = eYear + "." + eMonth + "." + eDate + "(" + eDay + ")";

				        // 5. 최종 문자열 결합
				        const dateRange = sDateDisp + " - " + eDateDisp;

				        // 6. HTML 화면에 텍스트 주입
				        $(item).find("> div:nth-child(2) > div:nth-child(2)").text(dateRange);
					});
				})
				.catch(error => {
				    alert("에러");
				});
				
				$("#popup_changedate").css("display","none");
				$("#black_filter").css("display","none");
			});
			$(".trash_btn").click(function() {	/* 여행 삭제 팝업 */
				let planIdx = $(this).closest(".myplan").data("plan_idx");
				$("#popup_deleteplan").attr("data-plan_idx", planIdx);
				
				$("#popup_deleteplan").css("display","block");
				$("#black_filter").css("display","block");
			});
			$("#popup_deleteplan > div:nth-child(2) > div:first-child").click(function() {	/* 취소 버튼 */
				$("#popup_deleteplan").css("display","none");
				$("#black_filter").css("display","none");
			});
			$(document).on("click", "#popup_deleteplan > div:nth-child(2) > div:nth-child(2)", function() {	/* 확인 버튼 */
				let planIdx = $("#popup_deleteplan").data("plan_idx");
				let dollar_this = $(this);
				
				fetch('delete_plan', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ planIdx: planIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					$(".myplan").each(function(idx, item) {
						if($(item).data("plan_idx")!=planIdx) return;
						$(item).closest(".myplan").remove();
						alert("내 여행이 삭제되었습니다.");
					})
				})
				.catch(error => {
				    alert("에러");
				});
				
				$("#popup_deleteplan").css("display","none");
				$("#black_filter").css("display","none");
			});

			$("#myreview .content > p:nth-child(1)").click(function() {	/* 장소 상세 페이지 이동 */
				let placeIdx = $(this).closest(".content").data("place_idx");
				alert(placeIdx);
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$(".p_good").click(function() {	// 장소 리뷰 추천
				let reviewIdx = $(this).closest(".bottom").data("review_idx");
				let dollar_this = $(this);
				let action = "";  // "on" 또는 "off"
				
				if($(this).hasClass("on")) {
					action = "off";
				} else {
					action = "on";
				}
				
				fetch('toggle_place_review_good', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ 
				    	id: "<%=id%>", 
				    	type: "p", 
				    	action: action,
				    	reviewIdx: reviewIdx 
				    })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					if(action=="on") {
						$(dollar_this).addClass("on");
						$(dollar_this).closest(".bottom").find(".p_goodN").text(data);
						
						// WebSocket.
						webSocket.send(JSON.stringify({ 
					    	id: "<%=id%>", 
					    	type: "c", 
					    	action: action,
					    	reviewIdx: reviewIdx 
					    }));
						
					} else {
						$(dollar_this).removeClass("on");
						$(dollar_this).closest(".bottom").find(".p_goodN").text(data);
					}
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(".c_good").click(function() {	/* 도시 리뷰 추천 */
				let reviewIdx = $(this).closest(".bottom").data("review_idx");
				let dollar_this = $(this);
				let action = "";  // "on" 또는 "off"
				
				if($(this).hasClass("on")) {
					action = "off";
				} else {
					action = "on";
				}
				
				fetch('toggle_city_review_good', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ 
				    	id: "<%=id%>",
				    	type: "c",
				    	action: action,
				    	reviewIdx: reviewIdx 
			    	})
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					if(action=="on") {
						$(dollar_this).addClass("on");
						$(dollar_this).closest(".bottom").find(".c_goodN").text(data);
						
						// WebSocket.
						webSocket.send(JSON.stringify({ 
					    	id: "<%=id%>", 
					    	type: "c", 
					    	action: action,
					    	reviewIdx: reviewIdx 
					    }));
						
					} else {
						$(dollar_this).removeClass("on");
						$(dollar_this).closest(".bottom").find(".c_goodN").text(data);
					}
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(document).on("click", ".place_delete", function() {	/* 장소 리뷰 삭제 팝업 */
				let reviewIdx = $(this).closest(".bottom").data("review_idx");
				$("#popup_deletereview").attr("data-review_idx", reviewIdx);
				$("#popup_deletereview").attr("data-idx_type", "p");
				
				$("#popup_deletereview").css("display","block");
				$("#black_filter").css("display","block");
			});
			$(document).on("click", ".city_delete", function() {	/* 도시 리뷰 삭제 팝업 */
				let reviewIdx = $(this).closest(".bottom").data("review_idx");
				$("#popup_deletereview").attr("data-review_idx", reviewIdx);
				$("#popup_deletereview").attr("data-idx_type", "c");
				
				$("#popup_deletereview").css("display","block");
				$("#black_filter").css("display","block");
			});
			$("#popup_deletereview > div:nth-child(2) > div:nth-child(1)").click(function() {	/* 삭제 취소 버튼 */
				$("#popup_deletereview").css("display","none");
				$("#black_filter").css("display","none");
			});
			$(document).on("click", "#popup_deletereview > div:nth-child(2) > div:nth-child(2)", function() {	/* 삭제 확인 버튼 */
				let reviewIdx = $("#popup_deletereview").attr("data-review_idx");
				let idxType = $("#popup_deletereview").attr("data-idx_type");
				
				// idxType에 따라 검색 범위를 좁혀서 타겟팅
			    let searchArea = (idxType === 'p') ? "#place_review" : "#city_review";
			    let targetReview = $(searchArea).find(".review[data-review_idx='" + reviewIdx + "']");
				
				fetch('delete_review', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ reviewIdx: reviewIdx, idxType: idxType })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// [도시 헤더 처리] 삭제하려는 리뷰가 도시 이름을 가지고 있는 경우 (첫 번째 리뷰인 경우)
			        let cityHeader = targetReview.find(".city");
					
			        if (cityHeader.is(":visible") && cityHeader.length > 0) {
			            let nextReview = targetReview.next(".review");
			            
			            if (nextReview.length > 0) {	// 다음 리뷰가 존재하고, 그 리뷰가 여전히 같은 도시에 속해 있는지 확인
			            	let cityName = cityHeader.find("span").text();
			            	let nextCityHeader = nextReview.find(".city");
			            	
			            	if (nextCityHeader.length > 0) {
			                    nextCityHeader.show(); // 숨겨져 있던 다음 리뷰의 도시명을 보여줌
			                } else {
			                	nextReview.prepend(`<div class="city" style="display:block;"><span>\${cityName}</span></div>`);
			                }
			            }
			        }
		            targetReview.remove();
		            
		         	// 남은 리뷰 개수 체크
		            if ($(searchArea).find(".review").length === 0) {
		                let emptyMsg = '<div style="margin: 10px; color: darkslateblue; text-align: center;">등록된 리뷰가 없습니다.</div>';
		                $(searchArea).append(emptyMsg);
		            }
			        
			        alert("리뷰가 삭제되었습니다.");
			        $("#popup_deletereview").css("display","none");
			        $("#black_filter").css("display","none");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			
		});  // end of $(function(){}).
		
	</script>
</head>
<body>
	<div id="menu">
		<div id="bell" class="fr" style="top: 20px; left: -80px;"></div>
		<img class="fr" src="https://assets.triple.guide/images/ico_navi_menu@4x.png"/>
		<div style="clear:both;"></div>
	</div>
	<div id="mytrip_profile">
		<img src="/upload/<%=memberDto.getProfileImg()%>"/>
		<p><%=memberDto.getNickname()%></p>
	</div>
	<div id="main">
		<div id="category">
			<p class="on">내 여행 
				<span></span>
			</p>
			<p>내 리뷰</p>
		</div>
		<div id="createtrip">
			<img src="https://img.icons8.com/ios11/512/228BE6/plus.png"/>
			<div>
				<div>여행 일정 만들기</div>
				<div>새로운 여행을 떠나보세요</div>
			</div>
		</div>
		<div id="upcomingtrip">
			<div>
				<span>다가오는 여행</span>
			</div>
			<div id="mytripplan">
				<% if(listUpcomingPlan.size() == 0) { %>
					<div style="margin-left: 10px; color: cornflowerblue;">등록된 여행 일정이 없습니다.</div>
				<% } %>
				<% 
					int planIdx = 0;
					for(UpcomingPlanListDto dto : listUpcomingPlan) {
						if(planIdx != dto.getPlanIdx()) {
							planIdx = dto.getPlanIdx();
							String pureStartDate = dto.getStartDate().split(" ")[0];
							String startDate = CalculateDate.formatDateWithDots(dto.getStartDate().split(" ")[0]);
							String lastDate = CalculateDate.calculateDate(pureStartDate, dto.getDays());				
				%>
							<div class="myplan" data-plan_idx="<%=dto.getPlanIdx()%>">
									<img class="citypic" src="<%=dto.getCityImg()%>"/>
									<div>
										<div><%=dto.getCityName()%> 여행</div>
										<div><%=startDate%> - <%=lastDate%></div>
									</div>
								<div class="fr">
									<img class="edit_btn" src="resources/img/edit.png"/>
									<img class="trash_btn" src="resources/img/trash.png"/>
								</div>
								<div style="clear:both;"></div>
							</div>
				<% 
						}
					}
				%>
			</div>
		</div>
	</div>
	
	<div id="myreview" style="display:none;">
		<div id="place_review">
			<div>내 장소 리뷰</div>
			<% if(listMyReview.size() == 0) { %>
				<div style="margin: 10px; color: darkslateblue; text-align: center;">등록된 리뷰가 없습니다.</div>
			<% } %>
			<% 
				String currentCity = "";
				for(MyReviewListDto dto : listMyReview) { 
			%>
				<div class="review" data-review_idx="<%=dto.getReviewIdx()%>">
				<% 
					if(!currentCity.equals(dto.getCityName())) { 
				%>
					<div class="city" style="display:block;">
						<span><%=dto.getCityName()%></span>
					</div>
				<% 
					currentCity = dto.getCityName();
					} 
				%>
					<div>
						<div class="place_content" data-place_idx="<%=dto.getPlaceIdx()%>">
							<p><%=dto.getName()%></p>
							<span><%=dto.getCategory()%></span>
							<div class="star" class="fl">
								<% 
								   for(int i=1; i<=dto.getStar(); i++) { 
								%>
										<img src="https://assets.triple.guide/images/img-review-star-full@4x.png"/>
								<% } %>
								<% for(int i=1; i<=(5-dto.getStar()); i++) { %>
										<img src="https://assets.triple.guide/images/img-review-star-empty@4x.png"/>
								<% } %>
							</div>
							<p><%=dto.getContent()%></p>
							<span><%=dto.getReviewDate().substring(0, 11)%></span>
							<div style="clear:both;"></div>
						</div>
						<div class="bottom" style="display:none;" data-review_idx="<%=dto.getReviewIdx()%>">
							<div class="p_good <%=dto.getReviewGood()==1 ? "on" : ""%>"></div>
							<div class="p_goodN"><%=dto.getReviewGoodN()%></div>
							<img class="place_delete" src="resources/img/trash.png"/>
						</div>
					</div>
				</div>
			<% } %>
		</div>
		<div id="city_review">
			<div>내 도시 리뷰</div>
			<% if(listMyCityReview.size() == 0) { %>
				<div style="margin: 10px; color: darkslateblue; text-align: center;">등록된 리뷰가 없습니다.</div>
			<% } %>
			<% 
				String currentCity2 = "";
				for(MyReviewListDto dto : listMyCityReview) { 
			%>
				<div class="review" data-review_idx="<%=dto.getRecommendReviewIdx()%>">
				<% 
					if(!currentCity2.equals(dto.getCityName())) { 
				%>
					<div class="city" style="display:block;">
						<span><%=dto.getCityName()%></span>
					</div>
				<% 
					currentCity2 = dto.getCityName();
					} 
				%>
					<div>
						<div class="city_content">
							<p><%=dto.getContent()%></p>
							<span style="top: 2px;"><%=dto.getReviewDate().substring(0, 11)%></span>
						</div>
						<div class="bottom" style="display:none;" data-review_idx="<%=dto.getRecommendReviewIdx()%>">
							<div class="c_good <%=dto.getReviewGood()==1 ? "on" : ""%>"></div>
							<div class="c_goodN"><%=dto.getReviewGoodN()%></div>
							<img class="city_delete" src="resources/img/trash.png"/>
						</div>
					</div>
				</div>
			<% } %>
		</div>
	</div>
	
	<div id="popup_createplan" style="display:none;">
		<div>
			<div class="x fr"></div>
			<div style="clear:both;"></div>
		</div>
		<div>
			<div></div>
			<div class="fl">일정 생성</div>
			<div>직접 일정 만들기</div>
			<div>AI 일정 추천받기</div>
		</div>
	</div>
	<div id="popup_deleteplan" style="display:none;">
		<div>일정에 등록된 장소들 및 체크리스트가 삭제됩니다.<br>일정을 삭제하겠습니까?</div>
		<div>
			<div>취소</div>
			<div>확인</div>
		</div>
	</div>
	<div id="popup_changedate" style="display:none;">
		<div>
			<div class="fl"></div>
			<div style="clear:both;"></div>
		</div>
		<div>여행날짜 수정</div>
		<div>일정에 따른 날씨예보, 여행 정보를 알려드립니다.</div>
		<input type="text" name="daterangepicker" value="01/01/2026 - 01/01/2026" style="display:none;"/>
		<div id="btn_modify_date">
			<span id="start_date"></span> - <span id="end_date"></span> / 등록 완료
		</div>
	</div>
	
	<div id="popup_deletereview" style="display:none;">
		<div>작성한 리뷰가 삭제됩니다.<br>리뷰를 삭제하시겠습니까?</div>
		<div>
			<div class="fl">취소</div>
			<div class="fr">확인</div>
			<div style="clear:both;"></div>
		</div>
	</div>
	<div id="black_filter" style="display:none;"></div>
	
	<%--
	<!-- Better Picker -->
	<script src="resources/js/picker.min.js"></script>
	<script src="resources/js/picker-main.js"></script>
	 --%>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>