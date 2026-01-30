package com.js.dao;

import java.util.Map;

import com.js.dto.MemberDto;

public interface MemberDao {
	String selectPwById(String id);		// 아이디로 비밀번호 찾기
	
	int selectNicknameCount(String nickname);	// 닉네임 중복 확인
	int selectIdCount(String id);		// 아이디 중복 확인
	void insertMember(MemberDto dto);	// 회원가입
	
	Map<String, String> selectProfile(String id);	// 메뉴바 회원 정보 조회
	void updateProfile(String nickname, String profileImg, String id);	// 메뉴바 회원 정보 수정
	
	String selectNicknameById(String id);	// 아이디로 닉네임 찾기
	
	String selectIdByNaverToken(String naverToken);	// 네이버 토큰 값으로 아이디 찾기
	void updateNaverToken(String naverToken, String id);	// 네이버 토큰 값 추가
	
	String selectIdByEmail(String email);	// 이메일로 아이디 찾기
	void updatePw(String pw, String id);	// 비밀번호 초기화
}
