package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private String id;			// 회원 id
	private String pw;			// 회원 pw
	private String nickname;	// 회원 닉네임
	private String email;		// 회원 이메일
	private String profileImg;	// 회원 프로필 이미지
}
