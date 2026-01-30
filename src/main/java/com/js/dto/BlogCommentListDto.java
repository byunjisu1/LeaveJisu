package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogCommentListDto {
	private int blogCommentIdx;		// 블로그 댓글 idx
	private int blogIdx;			// 블로그 idx
	private String id;				// id
	private String nickname;		// 회원 닉네임
	private String profileImg;		// 회원 프로필 이미지
	private String content;			// 댓글 내용
	private int commentGood;		// 댓글 추천 여부
	private int commentGoodN;		// 댓글 추천 개수
}
