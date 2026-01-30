<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_맞춤일정_옵션선택</title>
	<link rel="stylesheet" href="resources/css/AiPlanOption.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function() {
			$(".back").click(function() {
				let parentId = $(this).parent().parent().attr("id");
				$(this).parent().parent().css("display", "none");
				
				let nextId = "option" + (Number(parentId.charAt(6)) - 1);
				$("#" + nextId).css("display", "block");
			});
			$(".next").click(function() {	// 다음 옵션으로 넘어가기
				let parentId = $(this).parent().attr("id");
				if(parentId == undefined) return;  // 마지막 페이지인 경우.
				$(this).parent().css("display", "none");
				
				let nextId = "option" + (Number(parentId.charAt(6)) + 1);
				$("#" + nextId).css("display", "block");
			});
			$(".city").click(function() {	// 옵션1 선택
				if($(this).hasClass("on")) {
					$(this).removeClass("on");
				} else {
					$(".city").removeClass("on");	// on 되어있는 거 전부 삭제
					$(this).addClass("on");
					let cityName = $(this).text();
					$("#input_option1").val(cityName);
				}
				let cnt = $("#option1 .city.on").length;
				if(cnt>=1) {
					$("#option1 .next").addClass("on");
				} else {
					$("#option1 .next").removeClass("on");
				}
			});
			$(".days").click(function() {	// 옵션2 선택
				if($(this).hasClass("on")) {
					$(this).removeClass("on");
				} else {
					$(".days").removeClass("on");
					$(this).addClass("on");
					let days = $(this).text();
					$("#input_option2").val(days);
				}
				let cnt = $("#option2 .days.on").length;
				if(cnt>=1) {
					$("#option2 .next").addClass("on");
				} else {
					$("#option2 .next").removeClass("on");
				}
			});
			$(".with").click(function() {	// 옵션3 다중선택
				if($(this).hasClass("on")) {
					$(this).removeClass("on");
				} else {
					$(this).addClass("on");
					$("#input_option3").val("");
					$(".with.on").each(function(idx, item) {
						let with1 = $(item).text();
						$("#input_option3").val($("#input_option3").val() + "," + with1);
					});
				}
				let cnt = $("#option3 .with.on").length;
				if(cnt>=1) {
					$("#option3 .next").addClass("on");
				} else {
					$("#option3 .next").removeClass("on");
				}
			});
			$(".t_style").click(function() {	// 옵션4 다중선택
				if($(this).hasClass("on")) {
					$(this).removeClass("on");
				} else {
					$(this).addClass("on");
					$("#input_option4").val("");
					$(".t_style.on").each(function(idx, item) {
						let t_style = $(item).text();
						$("#input_option4").val($("#input_option4").val() + "," + t_style);
					});
				}
				let cnt = $("#option4 .t_style.on").length;
				if(cnt>=1) {
					$("#option4 .next").addClass("on");
				} else {
					$("#option4 .next").removeClass("on");
				}
			});
			$(".preference_plan").click(function() {	// 옵션5 선택
				if($(this).hasClass("on")) {
					$(this).removeClass("on");
				} else {
					$(".preference_plan").removeClass("on");
					$(this).addClass("on");
					let preference_plan = $(this).text();
					$("#input_option5").val(preference_plan);
				}
				let cnt = $("#option5 .preference_plan.on").length;
				if(cnt>=1) {
					$("#option5 .next").addClass("on");
				} else {
					$("#option5 .next").removeClass("on");
				}
			});
			$("input[type='submit']").click(function() {
				$("#option5").css('display', 'none');
				$("#option_ready").css('display', 'block');
			});
		});
	</script>
	
</head>
<body>
	<div id="option1">	<!-- 옵션1 -->
		<div class="header">
			<div class="back fl" style="display: none"></div>
			<div class="fr">1/5</div>
			<div style="clear:both;"></div>
		</div>
		<div class="header2">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_earth-emoji.svg);"></div>
			<div>떠나고 싶은 도시는?</div>
			<div>도시 1곳을 선택해주세요.</div>
		</div>
		<div class="city_list">
			<div>
				<div>일본</div>
				<div>
					<div class="fl city">도쿄</div>
					<div class="fl city">후쿠오카</div>
					<div class="fl city">오사카</div>
					<div class="fl city">가고시마</div>
					<div class="fl city">시즈오카</div>
					<div class="fl city">나고야</div>
					<div class="fl city">삿포로</div>
					<div class="fl city">오키나와</div>
					<div class="fl city">나가사키</div>
					<div class="fl city">다카마쓰</div>
					<div class="fl city">마쓰야마</div>
					<div class="fl city">구마모토</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div>아시아</div>
				<div>
					<div class="fl city">나트랑</div>
					<div class="fl city">두바이</div>
					<div class="fl city">가오슝</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div>유럽</div>
				<div>
					<div class="fl city">그라나다</div>
					<div class="fl city">파리</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div>아메리카</div>
				<div>
					<div class="fl city">밴쿠버</div>
					<div class="fl city">칸쿤</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div>오세아니아</div>
				<div>
					<div class="fl city">시드니</div>
					<div style="clear:both;"></div>
				</div>
			</div>
			<div>
				<div>국내</div>
				<div>
					<div class="fl city">서울</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
		<div class="next">다음</div>
	</div>
	
	<div id="option2" style="display:none;">	<!-- 옵션2 -->
		<div class="header">
			<div class="back fl"></div>
			<div class="fr">2/5</div>
			<div id="menu" class="fr"></div>
			<div style="clear:both;"></div>
		</div>
		<div class="header2">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_schedule-emoji.svg);"></div>
			<div>여행 기간은?</div>
			<div>원하는 기간을 선택해 주세요.</div>
		</div>
		<div class="city_list">
			<div>
				<div></div>
				<div>
					<div class="fl days">당일치기</div>
					<div class="fl days">1박 2일</div>
					<div class="fl days">2박 3일</div>
					<div class="fl days">3박 4일</div>
					<div class="fl days">4박 5일</div>
					<div class="fl days">5박 6일</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
		<div class="next">다음</div>
	</div>
	
	<div id="option3" style="display:none;">	<!-- 옵션3 -->
		<div class="header">
			<div class="back fl"></div>
			<div class="fr">3/5</div>
			<div id="menu" class="fr"></div>
			<div style="clear:both;"></div>
		</div>
		<div class="header2">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_smile-emoji.svg);"></div>
			<div>누구와 떠나나요?</div>
			<div>다중 선택이 가능해요.</div>
		</div>
		<div class="city_list">
			<div>
				<div></div>
				<div>
					<div class="fl with">혼자</div>
					<div class="fl with">친구와</div>
					<div class="fl with">연인과</div>
					<div class="fl with">배우자와</div>
					<div class="fl with">아이와</div>
					<div class="fl with">부모님과</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
		<div class="next">다음</div>
	</div>
	
	<div id="option4" style="display:none;">	<!-- 옵션4 -->
		<div class="header">
			<div class="back fl"></div>
			<div class="fr">4/5</div>
			<div id="menu" class="fr"></div>
			<div style="clear:both;"></div>
		</div>
		<div class="header2">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_camera-emoji.svg);"></div>
			<div>내가 선호하는<br>여행 스타일은?</div>
			<div>다중 선택이 가능해요.</div>
		</div>
		<div class="city_list">
			<div>
				<div></div>
				<div>
					<div class="fl t_style">체험·액티비티</div>
					<div class="fl t_style">SNS 핫플레이스</div>
					<div class="fl t_style">자연과 함께</div>
					<div class="fl t_style">유명 관광지는 필수</div>
					<div class="fl t_style">여유롭게 힐링</div>
					<div class="fl t_style">문화·예술·역사</div>
					<div class="fl t_style">여행지 느낌 물씬</div>
					<div class="fl t_style">쇼핑은 열정적으로</div>
					<div class="fl t_style">관광보다 먹방</div>
					<div style="clear:both;"></div>
				</div>
			</div>
		</div>
		<div class="next">다음</div>
	</div>
	
	<div id="option5" style="display:none;">	<!-- 옵션5 -->
		<div class="header">
			<div class="back fl"></div>
			<div class="fr">5/5</div>
			<div id="menu" class="fr"></div>
			<div style="clear:both;"></div>
		</div>
		<div class="header2">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_map-emoji.svg);"></div>
			<div>선호하는<br>여행 일정은?</div>
			<div>선택해주신 스타일로 일정을 만들어드려요.</div>
		</div>
		<form action="AiPlanOptionAction" method="post">
			<div class="city_list">
				<div>
					<div></div>
					<div>
						<div class="preference_plan" style="width: 708px;">빼곡한 일정 선호</div>
						<div class="preference_plan" style="width: 708px;">널널한 일정 선호</div>
						<div></div>
					</div>
				</div>
			</div>
			<input type="hidden" id="input_option1" name="option1"/>
			<input type="hidden" id="input_option2" name="option2"/>
			<input type="hidden" id="input_option3" name="option3"/>
			<input type="hidden" id="input_option4" name="option4"/>
			<input type="hidden" id="input_option5" name="option5"/>
			<input type="submit" value="다음" class="next"/>
		</form>
	</div>
	
	<div id="option_ready" style="display:none;">	<!-- 준비중 -->
		<div class="header2" style="margin-top: 320px;">
			<div style="background: url(https://triple.guide/trips/static/icons/ico_map-emoji.svg);"></div>
			<div>여행 스타일에 맞는<br>맞춤 일정을<br>준비중입니다.</div>
			<div>잠시만 기다려 주세요.</div>
		</div>
		<div id="separator"></div>
	</div>
	<%@ include file="WebSocket.jsp" %>
	<%@ include file="MenuBar.jsp" %>
</body>
</html>