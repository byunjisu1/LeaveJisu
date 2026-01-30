package com.js.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.js.dto.MemberDto;

@Repository
public class MemberDaoImpl implements MemberDao {
	@Autowired
	SqlSession sqlSession;
	
	/**
	 * selectPwById(String id) : 아이디로 비밀번호 찾기
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 리턴값 : DB에서 조회된 비밀번호
	 */
	@Override
	public String selectPwById(String id) {
		return sqlSession.selectOne("MemberMapper.getPwById", id);
	}
	
	/**
	 * selectNicknameCount(String nickname) : 닉네임 중복 확인(유효성 검사)
	 * 파라미터 nickname : 입력한 닉네임
	 * 리턴값 : DB에서 해당 닉네임의 개수
	 */
	@Override
	public int selectNicknameCount(String nickname) {
		return sqlSession.selectOne("MemberMapper.getNicknameCount", nickname);
	}
	
	/**
	 * selectIdCount(String id) : 아이디 중복 확인(유효성 검사)
	 * 파라미터 id : 입력한 아이디
	 * 리턴값 : DB에서 해당 아이디의 개수
	 */
	@Override
	public int selectIdCount(String id) {
		return sqlSession.selectOne("MemberMapper.getIdCount", id);
	}
	
	/**
	 * insertMember(MemberDto dto) : 회원가입
	 * 파라미터 MemberDto dto : 회원 정보 dto
	 */
	@Override
	public void insertMember(MemberDto dto) {
		sqlSession.insert("MemberMapper.insertMember", dto);
	}
	
	/**
	 * selectProfile(String id) : 메뉴바 회원 정보 조회
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 리턴값 : 닉네임, 프로필 이미지
	 */
	@Override
	public Map<String, String> selectProfile(String id) {
		return sqlSession.selectOne("MemberMapper.getMenuBarProfile", id);
	}
	
	/**
	 * updateProfile(String nickname, String profileImg, String id) : 메뉴바 회원 정보 수정
	 * 파라미터 id : 변경하려는 회원의 아이디
	 * 파라미터 nickname : 변경된 닉네임
	 * 파라미터 profileImg : 변경된 이미지
	 */
	@Override
	public void updateProfile(String nickname, String profileImg, String id) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("nickname", nickname);
		map1.put("profileImg", profileImg);
		map1.put("id", id);
		sqlSession.update("MemberMapper.updateProfile", map1);
	}
	
	/**
	 * selectNicknameById(String id) : 아이디로 닉네임 찾기
	 * 파라미터 id : 조회하려는 회원의 아이디
	 * 리턴값 : DB에서 조회된 회원의 닉네임
	 */
	@Override
	public String selectNicknameById(String id) {
		return sqlSession.selectOne("MemberMapper.getNicknameById", id);
	}

	/**
	 * selectIdByNaverToken(String naverToken) : 네이버토큰으로 아이디 찾기(소셜로그인)
	 * 파라미터 naverToken : 조회하려는 회원의 네이버 토큰 값
	 * 리턴값 : DB에서 조회된 회원의 아이디
	 */
	@Override
	public String selectIdByNaverToken(String naverToken) {
		return sqlSession.selectOne("MemberMapper.getIdByNaverToken", naverToken);
	}
	
	/**
	 * updateNaverToken(String naverToken, String id) : 네이버 토큰 값 업데이트(소셜로그인)
	 * 파라미터 naverToken : 새로 받은 회원의 네이버 토큰 값
	 * 파라미터 id : 네이버 토큰 값을 업데이트 할 회원의 아이디
	 */
	@Override
	public void updateNaverToken(String naverToken, String id) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("naverToken", naverToken);
		map1.put("id", id);
		sqlSession.update("MemberMapper.updateNaverToken", map1);
	}
	
	/**
	 * selectIdByEmail(String email) : 이메일로 아이디 찾기
	 * 파라미터 email : 조회하려는 회원의 이메일
	 * 리턴값 : DB에서 조회된 회원의 아이디
	 */
	@Override
	public String selectIdByEmail(String email) {
		return sqlSession.selectOne("MemberMapper.getIdByEmail", email);
	}
	
	/**
	 * updatePw(String pw, String id) : 비밀번호 초기화
	 * 파라미터 pw : 새로운 난수 발생 비밀번호
	 * 파라미터 id : 새로운 비밀번호를 생성한 회원의 아이디
	 */
	@Override
	public void updatePw(String pw, String id) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("pw", pw);
		map1.put("id", id);
		sqlSession.update("MemberMapper.updatePw", map1);
	}
}
