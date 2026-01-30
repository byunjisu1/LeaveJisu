package com.js.leavejisu;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.js.dto.MemberDto;
import com.js.service.MemberService;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class MemberServiceTest {
	@Autowired
	MemberService mSvc;
	
	// getPwById (로그인 성공 여부)
	@Test
	public void testCheckLogin() throws Exception {
		// 1. Given
		String id = "YG1017";
		String pw = "1017";
		
		// 2. When
		boolean result = mSvc.checkLogin(id, pw);
		
		// 3. Then
		System.out.println("id : " + id + ", pw : " + pw + " = " + result);
		//assertTrue("로그인 실패", result);		// result가 true가 아니면 '빨간 막대' & 로그인 실패
	}
	/*
	// getNicknameCount
	@Test
	public void testGetNicknameCount() throws Exception {
		// Given
		String nickname = "쿼카";
		
		// When
		int nicknameCount = mSvc.getNicknameCount(nickname);
		
		// Then
		System.out.println(nicknameCount);
	}
	
	// getIdCount
	@Test
	public void testGetIdCount() throws Exception {
		// Given
		String id = "jj5678";
		
		// When
		int idCount = mSvc.getIdCount(id);
		
		// Then
		System.out.println(idCount);
	}
	
	// insertMember
	@Test
	@Transactional
	public void testInsertMember() throws Exception {
		// Given
		MemberDto dto = new MemberDto("ghgh", "ghghg", "아2", "ghghh@naver.com", null);
		
		// When
		mSvc.insertMember(dto);
		
		// Then : x
	}
	
	// getMenuBarProfile
	@Test
	public void testGetMenuBarProfile() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		Map<String, String> profileMap = mSvc.getProfile(id);
		
		// Then
		System.out.println(profileMap);
	}
	
	// updateNickname
	@Test
	@Transactional
	public void testUpdateNickname() throws Exception {
		// Given
		String nickname = "쿼카";
		String id = "YG1017";
		String profileImg = "a.jpg";
		
		// When
		mSvc.updateProfile(nickname, profileImg, id);	
		
		// Then
	}
	
	// getIdByNaverToken
	@Test
	public void testGetIdByNaverToken() throws Exception {
		// Given
		String naverToken = "aa";
		
		// When
		String id = mSvc.getIdByNaverToken(naverToken);
		
		// Then
		System.out.println("id : " + id);
	}
	
	// updateNaverToken
	@Test
	@Transactional
	public void testUpdateNaverToken() throws Exception {
		// Given
		String naverToken = "a";
		String id = "jj5678";
		
		// When
		mSvc.updateNaverToken(naverToken, id);
		
		// Then
	}
	
	// getIdByEmail
	@Test
	public void testGetIdByEmail() throws Exception {
		// Given
		String email = "YG1017@naver.com";
		
		// When
		String id = mSvc.getIdByEmail(email);
		
		// Then
		System.out.println("검색된 id : " + id);
	}
	
	// updatePw
	@Test
	@Transactional
	public void testUpdatePw() throws Exception {
		// Given
		String pw = "111";
		String id = "YG1017";
		
		// When
		mSvc.updatePw(pw, id);
		
		// Then
	}
	*/
}
