<%@page import="com.js.util.CalculateDate"%>
<%@page import="com.js.dto.UpcomingPlanListDto"%>
<%@page import="com.js.dto.PlaceReviewDto"%>
<%@page import="java.util.List"%>
<%@page import="com.js.dto.PlaceDto"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String id = (String)session.getAttribute("loginId");
	PlaceDto placeDto = (PlaceDto)request.getAttribute("placeDto");
	List<PlaceReviewDto> listPlaceReview = (List<PlaceReviewDto>)request.getAttribute("listPlaceReview");
	List<UpcomingPlanListDto> listUpcomingPlan = (List<UpcomingPlanListDto>)request.getAttribute("listUpcomingPlan");
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>장소상세정보</title>
	<link rel="stylesheet" href="resources/css/PlaceDetail.css"/>
	<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC1z3AiKNcplK5-6Zz0tHzarT2HjnwkHbY&libraries=marker&callback=initMap" defer></script>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.css" />
	<script src="https://cdn.jsdelivr.net/npm/swiper@12/swiper-bundle.min.js"></script>
	
	<script>
		$(function() {
			$("#x").click(function() {
				history.back();
			});
			$("#myheart").click(function() {	/* 장소 찜 해제 */
				const urlParams = new URLSearchParams(window.location.search);
			    let placeIdx = urlParams.get('place_idx');
				let dollar_this = $(this);
				
				fetch('delete_myplace', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: "<%=id%>", placeIdx: placeIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$("#myheart").css("display", "none");
					$("#noheart").css("display", "block");
					alert("장소 저장이 취소되었습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#noheart").click(function() {	/* 장소 찜 추가 */
				const urlParams = new URLSearchParams(window.location.search);
			    let placeIdx = urlParams.get('place_idx');
			    
			    fetch('insert_myplace', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ id: "<%=id%>", placeIdx: placeIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					// 화면 바꿔주기
					$("#noheart").css("display", "none");
					$("#myheart").css("display", "block");
					alert("장소가 저장되었습니다.");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#addplan").click(function() {	/* 일정추가 */
				$("#black_filter").css("display", "block");
				$("#popup_upcomingtrip").css("display", "block");
			});
			$("#popup_upcomingtrip > #popup_header > div, #black_filter").click(function() {	/* 'x' */
				$("#black_filter").css("display", "none");
				$("#popup_upcomingtrip").css("display", "none");
			});
			$("#popup_upcomingtrip .my_plan").click(function() {
				let planIdx = $(this).data("plan_idx");
				$("#popup_day > div:nth-child(2)").attr("data-plan_idx", planIdx);
				
				fetch('upcomingtrip_lastDay', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ planIdx: planIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					console.log(data);
					for(let i=1; i<=data; i++) {
						let str = `
							<div>Day \${i}</div>
						`;
						$("#popup_day > div:nth-child(2)").append(str);
					}

					$("#popup_upcomingtrip").css("display", "none");
					$("#popup_day").css("display", "block");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#popup_day").on("click", "div:nth-child(2) > div", function() {	/* 일정 day까지 선택 완료 */
				let planIdx = $(this).parent().data("plan_idx");
				let day = $(this).text().substring(4);
				let placeIdx = new URLSearchParams(window.location.search).get("place_idx");
				
				fetch('insert_plan_place', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ planIdx: planIdx, day: day, placeIdx: placeIdx })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
					alert("선택하신 여행 일정에 해당 장소가 추가되었습니다.");
					$("#black_filter").css("display", "none");
					$("#popup_day").css("display", "none");
				})
				.catch(error => {
				    alert("에러");
				});
			});
			$("#writereview").click(function() {	/* 리뷰 작성 */
				$("#black_filter").css("display", "block");
				$("#popup_addReview").css("display", "block");
			});
			$("#popup_addReview > #header > img:first-child").click(function() {	/* 리뷰 작성창 닫기 */
				$("#black_filter").css("display", "none");
				$("#popup_addReview").css("display", "none");
			});
			let selectedStar = 0;
			$(".star").click(function() {
				selectedStar = $(this).data("value");
				
				// 별 색상 변경 (클릭한 곳까지 채우기)
			    $(".star").each(function() {
			        if ($(this).data("value") <= selectedStar) {
			            $(this).attr("src", "resources/img/yellow_star.png");
			        } else {
			            $(this).attr("src", "resources/img/empty_star.png");
			        }
			    });
				
			 	// 실시간으로 DB 문구 가져오기 (AJAX)
			    fetch('get_star_comment', {
			        method: 'POST',
			        headers: { 'Content-Type': 'application/json; charset=utf-8' },
			        body: JSON.stringify({ star: selectedStar })
			    })
			    .then(response => {
				    if (!response.ok) throw new Error('응답 실패');
				    return response.json();
				})
			    .then(data => {
			        $("#star_comment").text(data.comment).css("color", "rgb(58, 58, 58)");
			    })
			    .catch(error => {
			    	 alert("에러");
			    });
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
			$("#popup_addReview > #header > span:nth-child(3)").click(function() {	/* 리뷰 작성 완료 버튼 */
				let content = $("#review_content > textarea").val();
				let tripDate = $("#review_content > #tripwhen > span").text().replace(/[^0-9]/g, "");

				// 유효성 검사: 별점을 선택하지 않았을 경우 알림
			    if (selectedStar === 0) {
			        alert("별점을 선택해주세요!");
			        return;
			    }
			    
				let formData = new FormData();
				
				formData.append("placeIdx", '<%=request.getParameter("place_idx")%>');
			    formData.append("id", '<%=id%>');
			    formData.append("content", content);
			    formData.append("star", selectedStar);
			    formData.append("tripDate", tripDate);
				for (let i = 0; i < inputFileList.length; i++) {
					formData.append("review_imgs", inputFileList[i]);  // 배열에서 이미지들을 꺼내 폼 객체에 담는다.
				}
				
				fetch('insert_place_review', {
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
			$(".good").click(function() {	// 리뷰 추천
				let reviewIdx = $(this).closest(".reviewcontent").data("review_idx");
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
						$(dollar_this).closest(".recommend").find(".goodN").text(data);
						
						// WebSocket.
						webSocket.send(JSON.stringify({ 
					    	id: "<%=id%>", 
					    	type: "p", 
					    	action: action,
					    	reviewIdx: reviewIdx 
					    }));
						
					} else {
						$(dollar_this).removeClass("on");
						$(dollar_this).closest(".recommend").find(".goodN").text(data);
					}
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			const swiper = new Swiper('.swiper', {
				// Optional parameters
				direction: 'horizontal',
				loop: true,
				
				// If we need pagination
				pagination: {
					el: '.swiper-pagination',
				},
				
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
				
				// And if we need scrollbar
				scrollbar: {
					el: '.swiper-scrollbar',
				}
	    	});

		});  // the end of $(function() {}).
		
		function initMap() {
			let map;
			let arr;
			let sumLat;
			let sumLng;
			let avgLat;
			let avgLng;
			let centerPosition;
			
		   	map = new google.maps.Map(document.getElementById("map"), {
		    	center: { lat: 48.861761, lng: 2.353266 }, // 서울 중심 좌표
		    	zoom: 12,
		   	});
		   
		   	arr = [
	   			  { label: "", name: "<%=placeDto.getName()%>", lat: <%=placeDto.getLatitude()%>, lng: <%=placeDto.getLongitude()%> }
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
		}
	</script>
</head>
<body>
	<div id="header">
		<img src="https://assets.triple.guide/images/img_intro_logo_dark.svg"/>
		<div id="x" class="fr">x</div>
		<div style="clear:both;"></div>
	</div>
	<div id="header2">
		<h1><%=placeDto.getName()%></h1>
		<div id="star_heart">
			<div id="star" class="fl">
				<% 
				   int starCount = (int)Math.round(placeDto.getStarScore());
				   for(int i=1; i<=starCount; i++) { 
				%>
						<img src="https://assets.triple.guide/images/img-review-star-full@4x.png"/>
				<% } %>
				<% for(int i=1; i<=(5-starCount); i++) { %>
						<img src="https://assets.triple.guide/images/img-review-star-empty@4x.png"/>
				<% } %>
				
				<span><%=placeDto.getReviewCnt()%></span>
			</div>
			<div id="heart" class="fl">
				<img src="resources/img/redheart.png"/>
				<span><%=placeDto.getHeartCnt()%></span>
			</div>
			<div style="clear:both;"></div>
		</div>			
		<div><%=placeDto.getCategory() %> · <%=placeDto.getCityName()%></div>
	</div>
	<img src="<%=placeDto.getPlaceImg()%>"/>
	
	<div id="items">
		<div id="items_container">
			<div id="myheart" style="display: <%=placeDto.getMyHeart()==1 ? "block" : "none"%>">
				<img src="resources/img/redheart.png"/>
				<span>저장취소</span>
			</div>
			<div id="noheart" style="display: <%=placeDto.getMyHeart()==1 ? "none" : "block"%>">
				<img src="resources/img/heart.png"/>
				<span>저장하기</span>
			</div>
			<div id="addplan">
				<img src="resources/img/addplan.png"/>
				<span id="add_btn">일정추가</span>
			</div>
			<div id="writereview">
				<img src="resources/img/star.png"/>
				<span>리뷰쓰기</span>
			</div>
		</div>
	</div>
	
	<div id="basic_info">
		<div>기본정보</div>
		<div id="map"></div>
		<div id="place_info">
			<div id="address">
				<span>주소</span>
				<span><%=placeDto.getLocation()%></span>
			</div>
			<div id="phone">
				<span>전화</span>
				<span><%=placeDto.getPhone()!=null ? placeDto.getPhone() : "-"%></span>
			</div>
		</div>
		<div id="store_open_info">
			<div>
				<div>이용가능시간</div>
				<div><%=placeDto.getTime()!=null ? placeDto.getTime() : "-"%></div>
			</div>
			<div>
				<div style="margin-top: 15px;">휴무일</div>
				<div><%=placeDto.getClose()!=null ? placeDto.getClose() : "-"%></div>
			</div>
		</div>
	</div>
	
	<div id="review_header">
		<span>리뷰</span>
		<span><%=listPlaceReview.size()%></span>
	</div>
	
	<% for(PlaceReviewDto dto : listPlaceReview) { %>
		<div class="reviewcontent" data-review_idx="<%=dto.getReviewIdx()%>">
			<div class="review_content_header">
				<img src="/upload/<%=dto.getProfileImg()%>"/>
				<div><%=dto.getNickname()%></div>
			</div>
			<div class="grade">
				<% for(int i=1; i<=dto.getStar(); i++) { %>
						<img src="https://assets.triple.guide/images/img-review-star-full@4x.png"/>
				<% } %>
				<% for(int i=1; i<=(5-dto.getStar()); i++) { %>
						<img src="https://assets.triple.guide/images/img-review-star-empty@4x.png"/>
				<% } %>
			</div>
			<div><%=dto.getContent()%></div>
			<% if(dto.getListReviewImg() != null && dto.getListReviewImg().size() > 0) { %>
				<div class="review_pics swiper">
					<div class="swiper-wrapper">
						<% for(String s : dto.getListReviewImg()) { %>
							<div class="swiper-slide">
								<img src="/upload/<%=s%>"/>
							</div>
						<% } %>
					</div>
				</div>
			<% } %>
			<div class="recommend">
				<div class="good <%=dto.getReviewGood()==1 ? "on" : ""%>"></div>
				<div class="goodN"><%=dto.getReviewGoodN()%></div>
				<span class="fr"><%=dto.getReviewDate().substring(0, 11)%></span>
				<div style="clear:both;"></div>
			</div>
		</div>
	<% } %>

	<div id="black_filter" style="display: none;"></div>
	<div id="popup_upcomingtrip" style= "display:none;">
 		<div id="popup_header">
 			<div>X</div>
 		</div>
 		<div>다가오는 여행</div>
 		<div id="my_plans">
 			<% 
 				for(UpcomingPlanListDto dto : listUpcomingPlan) {
 					String startDate = CalculateDate.formatDateWithDots(dto.getStartDate().split(" ")[0]);
 					String endDate = CalculateDate.calculateDate(dto.getStartDate().split(" ")[0], dto.getDays());
 			%>
			 		<div class="my_plan" data-plan_idx="<%=dto.getPlanIdx()%>">
			 			<div class="citypic" style="background: url(<%=dto.getCityImg()%>) 0% 0% / cover no-repeat;"></div>
			 			<div class="words">
				 			<div><%=dto.getCityName()%> 여행</div>
				 			<div><%=startDate%> - <%=endDate%></div>
			 			</div>
			 		</div>
	 		<% } %>
 		</div>
	</div>
	<div id="popup_day" style="display: none;">
		<div>Day 보기</div>
		<div>

		</div>
	</div>

	<div id="popup_check" style= "display:none;">
		<div>일정에 추가되었습니다.</div>
		<div>닫기</div>
	</div>
	
	<div id="popup_addReview" style="display: none;">
		<div id="header">
			<img class="fl" src="resources/img/popup_x.png"/>
			<p><%=placeDto.getName()%></p>
			<span class="fr">완료</span>
		</div>
		<div id="star_area">
			<div id="stargrade">
				<img class="star" src="resources/img/empty_star.png" data-value="1"/>
				<img class="star" src="resources/img/empty_star.png" data-value="2"/>
				<img class="star" src="resources/img/empty_star.png" data-value="3"/>
				<img class="star" src="resources/img/empty_star.png" data-value="4"/>
				<img class="star" src="resources/img/empty_star.png" data-value="5"/>
			</div>
			<div id="star_comment">별점을 선택해주세요!</div>
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
	
	<!-- Better Picker -->
	<script src="resources/js/picker.min.js"></script>
	<script src="resources/js/picker-main.js"></script>
</body>
</html>