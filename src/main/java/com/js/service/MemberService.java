package com.js.service;

import java.util.Map;

import com.js.dto.MemberDto;

public interface MemberService {
	boolean checkLogin(String id, String pw);	// 로그인 성공 여부
	
	int getNicknameCount(String nickname);	// 닉네임 중복 확인
	int getIdCount(String id);	// 아이디 중복 확인
	void insertMember(MemberDto dto);	// 회원가입
	
	Map<String, String> getProfile(String id);	// 메뉴바 회원 정보
	void updateProfile(String nickname, String profileImg, String id);	// 메뉴바 회원 정보 수정
	
	String getNicknameByMemberId(String id);	// 아이디로 닉네임 찾기
	
	String getIdByNaverToken(String naverToken);	// 네이버 토큰 값으로 아이디 찾기
	void updateNaverToken(String naverToken, String id);	// 네이버 토큰 값 추가
	
	String getIdByEmail(String email);	// 이메일로 아이디 찾기
	void updatePw(String pw, String id);	// 비밀번호 초기화
}
