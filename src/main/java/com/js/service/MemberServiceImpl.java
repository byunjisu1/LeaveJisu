package com.js.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.dao.MemberDao;
import com.js.dto.MemberDto;

@Service
public class MemberServiceImpl implements MemberService {
	@Autowired
	MemberDao mDao;

	/**
	 * checkLogin(String id, String pw) : 로그인 성공 여부
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 파라미터 pw : 조회하려는 회원의 비밀번호
	 * 리턴값 : 로그인 성공 - true / 로그인 실패 - false
	 */
	@Override
	public boolean checkLogin(String id, String pw) {
		String pwFromDb = mDao.selectPwById(id);
		if(pw!=null && pw.equals(pwFromDb)) {
			return true;	// 로그인 성공
		}
		return false;	// 로그인 실패
	}
	
	/**
	 * getNicknameCount(String nickname) : 닉네임 중복 확인(유효성 검사)
	 * 파라미터 nickname : 입력한 닉네임
	 * 리턴값 : DB에서 조회된 닉네임의 개수
	 */
	@Override
	public int getNicknameCount(String nickname) {
		return mDao.selectNicknameCount(nickname);
	}

	/**
	 * getIdCount(String id) : 아이디 중복 확인(유효성 검사)
	 * 파라미터 id : 입력한 아이디
	 * 리턴값 : DB에서 조회된 아이디의 개수
	 */
	@Override
	public int getIdCount(String id) {
		return mDao.selectIdCount(id);
	}

	/**
	 * insertMember(MemberDto dto) : 회원가입
	 * 파라미터 MemberDto dto : 회원 정보 dto
	 */
	@Override
	public void insertMember(MemberDto dto) {
		mDao.insertMember(dto);
	}

	/**
	 * getProfile(String id) : 메뉴바 회원 정보 조회
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 리턴 값 : 회원의 닉네임, 프로필 사진
	 */
	@Override
	public Map<String, String> getProfile(String id) {
		return mDao.selectProfile(id);
	}

	/**
	 * updateProfile(String nickname, String profileImg, String id) : 메뉴바 회원 정보 수정
	 * 파라미터 nickname : 새로운 닉네임
	 * 파라미터 profileImg : 새로운 프로필 사진
	 * 파라미터 id : 변경하려는 회원의 아이디
	 */
	@Override
	public void updateProfile(String nickname, String profileImg, String id) {
		mDao.updateProfile(nickname, profileImg, id);;
	}

	/**
	 * getNicknameByMemberId(String id) : 아이디로 닉네임 찾기
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 리턴값 : DB에서 조회된 회원의 닉네임
	 */
	@Override
	public String getNicknameByMemberId(String id) {
		return mDao.selectNicknameById(id);
	}

	/**
	 * getIdByNaverToken(String naverToken) : 네이버 토큰 값으로 아이디 찾기(소셜로그인)
	 * 파라미터 naverToken : 조회하려는 회원의 네이버 토큰 값
	 * 리턴값 : DB에서 조회된 회원의 아이디
	 */
	@Override
	public String getIdByNaverToken(String naverToken) {
		return mDao.selectIdByNaverToken(naverToken);
	}

	/**
	 * updateNaverToken(String naverToken, String id) : 네이버 토큰 값 추가
	 * 파라미터 naverToken : 새로운 네이버 토큰 값
	 * 파라미터 id : 추가할 회원의 아이디
	 */
	@Override
	public void updateNaverToken(String naverToken, String id) {
		mDao.updateNaverToken(naverToken, id);
	}

	/**
	 * getIdByEmail(String email) : 이메일로 아이디 찾기
	 * 파라미터 email : 조회하려는 회원의 이메일
	 * 리턴값 : DB에서 조회된 회원의 아이디
	 */
	@Override
	public String getIdByEmail(String email) {
		return mDao.selectIdByEmail(email);
	}

	/**
	 * updatePw(String pw, String id) : 비밀번호 초기화
	 * 파라미터 pw : 새로운 난수 발생 비밀번호
	 * 파라미터 id : 비밀번호를 업데이트할 회원의 아이디
	 */
	@Override
	public void updatePw(String pw, String id) {
		mDao.updatePw(pw, id);
	}
}
