<%@page import="com.js.dto.ReviewListDto"%>
<%@page import="java.util.List"%>
<%@page import="com.js.dto.RecommendPlanPlaceDto"%>
<%@page import="com.js.dto.RecommendPlanDetailDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	Integer cityIdx = (Integer)request.getAttribute("cityIdx");
	String cityName = (String)request.getAttribute("cityName");
	List<RecommendPlanDetailDto> listRecommendPlan = (List<RecommendPlanDetailDto>)request.getAttribute("listRecommendPlan");
	List<ReviewListDto> listReview = (List<ReviewListDto>)request.getAttribute("listReview");
%>
<%!
	String dateToString(String strDate) {
		String year = strDate.split(" ")[0].substring(0,4);
		String month = strDate.split(" ")[0].substring(5,7);
		String day = strDate.split(" ")[0].substring(8,10);
		return year + "." + Integer.parseInt(month) + "." + Integer.parseInt(day);
	}

%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_추천일정_전체</title>
	<link rel="stylesheet" href="resources/css/RecommendPlanAll.css"/>
	
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<!-- Date Range Picker -->
	<script type="text/javascript" src="https://cdn.jsdelivr.net/jquery/latest/jquery.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/moment@2.29.4/min/moment-with-locales.min.js"></script>
	<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.min.js"></script>
	<link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/npm/daterangepicker/daterangepicker.css" />

	<!-- Swiper.js -->	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />
	<script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>

	<script>
		$(function() {
			moment.locale('ko');
			
			$("#x").click(function() {
				const params = new URLSearchParams(window.location.search);	// 주소의 파라미터 가져오기
				let cityIdx = params.get("city_idx");
				location.href="RecommendPlan?city_idx=" + cityIdx;
			});
			$(".plan > div").click(function() {	// 장소 상세 페이지
				let placeIdx = $(this).data("place_idx");
				location.href="PlaceDetail?place_idx=" + placeIdx;
			});
			$("#review > #review_header > img").click(function() {	/* 리뷰 작성 */
				$("#black_filter").css("display", "block");
				$("#popup_addReview").css("display", "block");
			});
			$("#popup_addReview > #header > img:first-child").click(function() {	/* 리뷰 작성창 닫기 */
				$("#black_filter").css("display", "none");
				$("#popup_addReview").css("display", "none");
			});
			$("#btn_add_photo").click(function() {
		        $("#review_file").click();
		    });
			// 파일 선택
	        let inputFileList = [];   // 리뷰 이미지 업로드 선택한 파일 목록
		    $("#review_file").on("change", function(e) {
		        var files = e.target.files;
		        var filesArr = Array.prototype.slice.call(files);
		        
		     	// 새로 선택한 파일들을 합쳤을 때 5장이 넘는지 먼저 체크
		        if (inputFileList.length + filesArr.length > 5) {
		            alert("이미지는 최대 5개까지 가능합니다.");
		            $(this).val(""); // 선택 초기화
		            return;
		        }
		        
		        filesArr.forEach(function(f) {
		            if (!f.type.match("image.*")) return; // 이미지 아닐 경우 패스

		            inputFileList.push(f); // 배열에 추가

		            // ⭐ FileReader를 이용해 화면에 미리보기 출력
		            var reader = new FileReader();
		            reader.onload = function(e) {
		                var html = `
		                    <div class="preview_item" style="position:relative;">
		                        <img src="\${e.target.result}" style="width:80px; height:80px; border-radius:5px; object-fit:cover;"/>
		                        <span class="remove_img" style="position:absolute; top:0; right:0; cursor:pointer; background:rgba(0,0,0,0.5); color:#fff; padding:0 5px;">x</span>
		                    </div>`;
		                $("#preview_zone").append(html);
		            };
		            reader.readAsDataURL(f);
		        });
		        
		        $(this).val(""); // 같은 파일 재선택 가능하게 초기화
		    });

		    // 미리보기 사진 삭제 기능
		    $(document).on("click", ".remove_img", function() {
		        var index = $(this).parent().index();
		        inputFileList.splice(index, 1); // 배열에서 삭제
		        $(this).parent().remove(); // 화면에서 삭제
		    });
			$("#popup_addReview > #header > span:nth-child(2)").click(function() {	/* 리뷰 작성 완료 버튼 */
				let content = $("#review_content > textarea").val();
				let tripDate = $("#review_content > #tripwhen > span").text().replace(/[^0-9]/g, "");
								
				let formData = new FormData();
				
				formData.append("cityIdx", '<%=request.getParameter("city_idx")%>');
			    formData.append("id", '<%=id%>');
			    formData.append("content", content);
			    formData.append("tripDate", tripDate);
				for (let i = 0; i < inputFileList.length; i++) {
					formData.append("review_imgs", inputFileList[i]);  // 배열에서 이미지들을 꺼내 폼 객체에 담는다.
				}
				
				fetch('insert_recommend_review', {
				    method: 'POST',
				    body: formData
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					alert("리뷰가 등록되었습니다.");
					$("#popup_addReview").css("display", "none");
					$("#black_filter").css("display", "none");
					location.reload();
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("select[name='review_sort']").change(function() {	/* 리뷰 정렬 */
				let selected = $(this).val();   // most_recent most_liked
				
				if(selected=='most_recent') selected='최신순';
				else if(selected=='most_liked') selected='추천순';
				
				fetch('recommend_review_sort', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ cityIdx:'<%=request.getParameter("city_idx")%>', sort:selected })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					console.log(data);
					$("#review_container").empty();
					data.forEach(function(el) {
						const tripYear = el.tripDate.substring(0,4);
						const tripMonth = el.tripDate.substring(4);

						let strAppend = `
							<div class="review_content">
								<div>
									<img src="\${el.profileImg}"/>
									<div>\${el.nickname}</div>
								</div>
								<div>\${tripYear}년 \${tripMonth}월 여행</div>
								<div>\${el.content}</div>
								<div>
									<div class="fl">
										<div class="\${el.reviewGood==1 ? 'on' : ''}"></div>
										<div>\${el.reviewGoodN}</div>
									</div>
									<div class="fr">\${el.reviewDate.split(" ")[0]}</div>
									<div style="clear:both;"></div>
								</div>
								<div class="line"></div>
							</div>
						`;
						$("#review_container").append(strAppend);
					});
					
					alert("성공");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$(".good").click(function() {	/* 리뷰 추천 */
				let recommendReviewIdx = $(this).closest(".review_content").data("recommend_review_idx");
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
				    	action: action,
				    	reviewIdx: recommendReviewIdx 
			    	})
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					if(action=="on") {
						$(dollar_this).addClass("on");
						$(dollar_this).siblings(".goodN").text(data);
						
						// WebSocket.
						webSocket.send(JSON.stringify({ 
					    	id: "<%=id%>", 
					    	type: "c", 
					    	action: action,
					    	reviewIdx: reviewIdx 
					    }));
						
					} else {
						$(dollar_this).removeClass("on");
						$(dollar_this).siblings(".goodN").text(data);
					}
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#plan_add").click(function() {	/* 내 일정으로 담기 */
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
		    	$('#loading').css("display", "block");
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
					$('#loading').css("display", "none");
					$("#popup_registertravel").css("display", "none");
					$("#black_filter").css("display", "none");
					alert("내 일정으로 담았습니다. \n담은 일정은 프로필 내 '내 여행'에서 다시 확인하실 수 있습니다.");
				})
				.catch(error => {
					$('#loading').css("display", "none");
				    alert("에러");
				});
			});
			
		    const swiper = new Swiper('.swiper', {
				// Optional parameters
				direction: 'horizontal',
				loop: true,
				
				slidesPerView: 2,
				spaceBetween: 20,
				autoplay: {
					delay: 3000
				},
				
				// Navigation arrows
				navigation: {
					nextEl: '.swiper-button-next',
					prevEl: '.swiper-button-prev',
				},
				
				scrollbar: {
					el: '.swiper-scrollbar',
				}
	    	});
		});    // the end of $(function() {}).
		
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
			let mapContainer

			<%
				for(RecommendPlanDetailDto dto : listRecommendPlan) {
			%>
		   
		   	arr = [
			<%
				for(RecommendPlanPlaceDto placeDto : dto.getListPlace()) {
			%>
	   			  { label: "<%=placeDto.getPlaceOrder()%>", name: "<%=placeDto.getPlaceName()%>", lat: <%=placeDto.getLatitude()%>, lng: <%=placeDto.getLongitude()%> },
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
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY&callback=initMap" async defer></script>
<body>
	<div id="header">
		<div id="x" class="fr">x</div>
		<div style="clear:both;"></div>
	</div>
	<div id="small_title">
		<div><%=cityName%></div>
		<img src="https://assets.triple.guide/images/ico_arrow.svg"/>
		<div>가이드</div>
	</div>
	<%
		for(RecommendPlanDetailDto dto : listRecommendPlan) { 
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
				<div id="map<%=dto.getDay()%>"></div>
			</div>
			<div class="plan">
				<%
				for(RecommendPlanPlaceDto placeDto : dto.getListPlace()) {
				%>
					<div data-place_idx="<%=placeDto.getPlaceIdx()%>">
						<div class="number fl">
							<div><%=placeDto.getPlaceOrder()%></div>
							<div><%=placeDto.getPlaceTime()%></div>
						</div>
						<div class="place_content fl">
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
			<div class="separator"></div>
		<% } %>
	
	<div id="review">
		<div id="review_header">
			<div class="fl">리뷰 <span style="color: rgb(54, 143, 255);"><%=listReview.size()%></span></div>
			<img class="fr" src="https://assets.triple.guide/images/btn-com-write@2x.png"/>
			<div style="clear:both;"></div>
		</div>
		<div>
			<select name="review_sort">
				<option value="most_liked">추천순</option>
				<option value="most_recent">최신순</option>
			</select>
		</div>
		<div id="review_container">
			<% 
			for(ReviewListDto dto : listReview) {
				String tripYear = dto.getTripDate().substring(0, 4);
				String tripMonth = dto.getTripDate().substring(4);
			%>
				<div class="review_content" data-recommend_review_idx="<%=dto.getRecommendReviewIdx()%>">
					<div>
						<img src="/upload/<%=dto.getProfileImg()%>"/>
						<div><%=dto.getNickname()%></div>
					</div>
					<div><%=tripYear%>년 <%=tripMonth%>월 여행</div>
					<div><%=dto.getContent()%></div>
					<% if(dto.getListReviewImg() != null) { %>
						<div class="review_img swiper">
							<div class="swiper-wrapper">
								<% for(String s : dto.getListReviewImg()) { %>
									<div class="swiper-slide">
										<img src="/upload/<%=s%>"/>
									</div>
								<% } %>
							</div>
						</div>
					<% } %>
					<div class="review_good">
						<div class="fl">
							<div class="good <%=dto.getReviewGood()==1 ? "on" : ""%>"></div>
							<div class="goodN"><%=dto.getReviewGoodN()%></div>
						</div>
						<div class="fr"><%=dateToString(dto.getReviewDate())%></div>
						<div style="clear:both;"></div>
					</div>
					<div class="line"></div>
				</div>
			<% } %>
		</div>
	</div>
	<div id="plan_add">
		<img src="https://triple.guide/trips/static/icons/ico-save-schedule.svg"/>
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
	<div id="popup_addReview" style="display: none;">
		<div id="header">
			<img class="fl" src="resources/img/popup_x.png"/>
			<span class="fr">완료</span>
		</div>
		<div id="review_content">
			<div id="tripwhen">
				<img src="resources/img/graycalendar.png"/>
				<span id="chose">언제 다녀오셨어요? (선택)</span>
			</div>
			<textarea type="text" placeholder=" 직접 경험한 솔직한 리뷰를 남겨주세요. &#13;&#10; 사진은 5장까지 첨부할 수 있어요."></textarea>
			<div id="picturearea">
				<div>
					<input type="file" id="review_file" name="review_img" multiple="multiple" style="display: none;"/>
					<img id="btn_add_photo" src="resources/img/addphoto.png"/>
				</div>
				<div id="preview_zone" style="display: flex; gap: 10px; margin-top: 10px;"></div>
			</div>
		</div>
	</div>
	
	<div id="loading" style="display: none;">
		<img src="resources/img/loading.gif" class="loading">
	</div>
	
	<!-- Better Picker -->
	<script src="resources/js/picker.min.js"></script>
	<script src="resources/js/picker-main.js"></script>
</body>
</html>