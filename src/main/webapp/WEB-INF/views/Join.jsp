<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>TRIPLE_회원가입</title>
	<link rel="stylesheet" href="resources/css/Join.css"/>
	<script src="https://code.jquery.com/jquery-3.7.1.js" integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4=" crossorigin="anonymous"></script>
	<script>
		$(function() {
			$("#x").click(function() {
				history.back();
			});
			// 닉네임 중복 검사
			$(".nickname_check").click(function() {
				let nicknameData = $("#input_nickname").val();
				if(/^[가-힣a-zA-Z]{2,6}$/.test(nicknameData) == false) {
					alert("닉네임이 올바르지 않습니다. \n다시 입력해주세요.");
					return false;
				}
				
				fetch('nickname_check', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ "nickname": nicknameData })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) { // result: true -> 사용가능
				        alert("사용 가능한 닉네임입니다.");
						$("#input_nickname").prop('readonly', true);
				    } else { // result: false -> 중복상황
				        alert("중복된 닉네임입니다. \n다시 입력하세요.");
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			// 아이디 중복 검사
			$(".id_check").click(function() {
				let idData = $("#input_id").val();
				if(/^[a-z0-9]{4,16}$/.test(idData) == false) {
					alert("아이디가 올바르지 않습니다. \n다시 입력해주세요.");
					return false;
				}
				
				fetch('id_check', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ "id": idData })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) { // result: true -> 사용가능
				        alert("사용 가능한 아이디입니다.");
						$("#input_id").prop('readonly', true);
				    } else { // result: false -> 중복상황
				        alert("중복된 아이디입니다. \n다시 입력하세요.");
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			// 이메일 인증번호 발송
			$(".email").click(function() {	
				let emailData = $("#input_email").val();
				if(/^[A-Za-z0-9\.\-_]+@([a-z0-9\-]+\.)+[A-Za-z]{2,6}$/.test(emailData) == false) {
					alert("이메일 양식이 올바르지 않습니다. \n다시 입력해주세요.")
					return false;
				}
				emailData = emailData.toLowerCase();
				
				fetch('send_email', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ "email": emailData })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) {
				        alert("인증번호가 발송되었습니다.");
				        $(".email_check").addClass("on");
				    } else {
				        alert("이메일 인증번호가 발송되지 않았습니다.");
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			// 이메일 인증번호 확인
			$(".email_check").click(function() {
				let emailCode = $("#input_code").val();
				
				let nickname = $("#input_nickname").val();
				let id = $("#input_id").val();
				let pw = $("#input_pw").val();
				let email = $("#input_email").val();
				
				fetch('code_check', {
				    method: 'POST',
				    headers: { 'Content-Type': 'application/json; charset=utf-8' },
				    body: JSON.stringify({ 
				    	"code": emailCode, 
				    	"nickname": nickname, 
				    	"id": id, 
				    	"pw": pw,
				    	"email": email 
				    })
				})
				.then(response => {
				    if (!response.ok) { throw new Error('네트워크 에러! 관리자에게 문의하세요.') }
				    return response.json();
				})
				.then(data => {
				    if (data.result) {
				        alert("회원가입이 완료되었습니다.\n일반 로그인 또는 네이버 로그인을 진행해주세요.");
				        location.href = "Login";
				    } else {
				        alert("인증번호가 일치하지 않습니다. \n이메일 인증번호를 다시 입력하세요.");
				    }
				})
				.catch(error => {
				    alert("에러");
				});
			});
			
			// 유효성 검사
			$("#input_nickname").keyup(function() {	
				let nickname = $("#input_nickname").val();
				if(/^[가-힣a-zA-Z]{2,6}$/.test(nickname) == false) {
					$(this).parents(".box_container").find(".alert").css("display", "block");
					return false;
				} else {
					$(this).parents(".box_container").find(".alert").css("display", "none");
				}
			});
			$("#input_id").keyup(function() {
				let id = $("#input_id").val();
				if(/^[a-z0-9]{4,16}$/.test(id) == false) {
					$(this).parents(".box_container").find(".alert").css("display", "block");
					return false;
				} else {
					$(this).parents(".box_container").find(".alert").css("display", "none");
				}
			});
			$("#input_pw").keyup(function() {
				let pw = $("#input_pw").val();
				if(/^[a-zA-Z0-9]{4,16}$/.test(pw) == false) {
					$(this).parents(".box_container").find(".alert").css("display", "block");
					return false;
				} else {
					$(this).parents(".box_container").find(".alert").css("display", "none");
				}
			});
			$("#input_email").keyup(function() {
				let email = $("#input_email").val();
				if(/^[a-z0-9\.\-_]+@([a-z0-9\-]+\.)+[a-z]{2,6}$/.test(email) == false) {
					$(this).parents(".box_container").find(".alert").css("display", "block");
					return false;
				} else {
					$(this).parents(".box_container").find(".alert").css("display", "none");
					if($("#input_nickname").prop('readonly') 
							&& $("#input_id").prop('readonly') 
							&& /^[a-zA-Z0-9]{4,16}$/.test($("#input_pw").val())) {
						$(".email").addClass("on");
					} else {
						$(".email").removeClass("on");
					}
				}
			});
		});
	</script>
</head>
<body>
	<div id="header">
		<h2>JOIN MEMBER</h2>
		<div id="x" class="fr">X</div>
	</div>
	<div id="header2">
		<div class="fl">기본정보</div>
		<div class="fr">필수입력사항</div>
		<div class="fr">*</div>
		<div style="clear:both;"></div>
	</div>
	<div id="content">
		<form>
			<div class="box_container">
				<div>
					<div>
						<span id="nickname" class="fl">닉네임</span>
						<span class="fl">*</span>
						<div style="clear:both;"></div>
					</div>
					<input type="text" id="input_nickname" name="nickname"
					value=""
					/>
					<span>(한글, 영문 2~6자)</span>
					<div class="nickname_check">중복 확인</div>
				</div>
				<div class="alert" style="display: none;">닉네임은 한글, 영문자 2~6자로 입력하세요.</div>
			</div>
			<div class="box_container">
				<div>
					<div>
						<span id="id" class="fl">아이디</span>
						<span class="fl">*</span>
						<div style="clear:both;"></div>
					</div>
					<input type="text" id="input_id" name="id"
					value=""
					/>
					<span>(영문 소문자/숫자 4~16자)</span>
					<div class="id_check">중복 확인</div>
				</div>
				<div class="alert" style="display: none;">아이디는 영문 소문자, 숫자 4~16자로 입력하세요.</div>
			</div>
			<div class="box_container">
				<div>
					<div>
						<span id="pw" class="fl">비밀번호</span>
						<span class="fl">*</span>
						<div style="clear:both;"></div>
					</div>
					<input type="password" id="input_pw" name="pw"
					value=""
					/>
					<span>(영문 대소문자/숫자 4~16자)</span>
				</div>
				<div class="alert" style="display: none;">비밀번호는 영문 대소문자, 숫자 4~16자로 입력하세요.</div>
			</div>
			<div class="box_container">
				<div>
					<div>
						<span id="email" class="fl">이메일</span>
						<span class="fl">*</span>
						<div style="clear:both;"></div>
					</div>
					<input type="text" id="input_email" name="email"
					value=""
					/>
					<div class="email">이메일 인증번호 발송</div>
				</div>
				<div class="alert" style="display: none;">이메일 형식(ex. aaaa@naver.com)으로 입력하세요.</div>
			</div>
			<div class="box_container">
				<div>
					<div>
						<span id="code_check" class="fl">인증번호 입력</span>
						<span class="fl">*</span>
						<div style="clear:both;"></div>
					</div>
					<input type="text" id="input_code"/>
					<div class="email_check">이메일 인증번호 확인</div>
				</div>
			</div>
		</form>
	</div>
</body>
</html>