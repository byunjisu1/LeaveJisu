package com.js.leavejisu;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.js.dao.MemberDao;
import com.js.dto.MemberDto;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class MemberDaoTest {
	@Autowired
	MemberDao mDao;

	// selectPwById (로그인 성공 여부)
	@Test
	public void testSelectPwById() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		String pw = mDao.selectPwById(id);
		
		// Then
		assertNotNull(pw);
		System.out.println("id : " + id + ", pw : " + pw);
	}

	/*
	// selectNicknameCount
	@Test
	public void testSelectNicknameCount() throws Exception {
		// Given
		String nickname = "쿼카";
		
		// When
		int nicknameCount = mDao.selectNicknameCount(nickname);
		
		// Then
		System.out.println(nicknameCount);
	}
	
	// selectIdCount
	@Test
	public void testSelectIdCount() throws Exception {
		// Given
		String id = "jj5678";
		
		// When
		int idCount = mDao.selectIdCount(id);
		
		// Then
		System.out.println(idCount);
	}
	
	// insertMember
	@Test
	@Transactional
	public void testInsertMember() throws Exception {
		// Given
		MemberDto dto = new MemberDto("dkdk", "dkdkdk", "아1", "dkdkdk@naver.com", null);
		
		// When
		mDao.insertMember(dto);
		
		// Then : x
	}
	
	// selectMenuBarProfile
	@Test
	public void testSelectMenuBarProfile() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		Map<String, String> profileMap = mDao.selectProfile(id);
		
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
		String profileImg = "a";
		
		// When
		mDao.updateProfile(nickname, profileImg, id);
		
		// Then
	}
	
	// selectNicknameById
	@Test
	public void testSelectNicknameById() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		String nickname = mDao.selectNicknameById(id);
		
		// Then
		assertNotNull("조회한 닉네임은 null이 아니어야 함", nickname);
		System.out.println(id + "'s nickname : " + nickname);
	}
	
	// selectIdByNaverToken
	@Test
	public void testSelectIdByNaverToken() throws Exception {
		// Given
		String naverToken = "aa";
		
		// When
		String id = mDao.selectIdByNaverToken(naverToken);
		
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
		mDao.updateNaverToken(naverToken, id);
		
		// Then
	}
	
	// selectIdByEmail
	@Test
	public void testSelectIdByEmail() throws Exception {
		// Given
		String email = "YG1017@naver.com";
		
		// When
		String id = mDao.selectIdByEmail(email);
		
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
		mDao.updatePw(pw, id);
		
		// Then
	}
	*/
}
