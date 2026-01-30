package com.js.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailDto {
	private String nickname;						// 회원 닉네임
	private String profileImg;						// 회원 프로필 이미지
	private int blogIdx;							// 블로그 idx
	private String title;							// 블로그 제목
	private String days;							// 여행 기간
	private String imgUrl;							// 블로그 대표 사진 
	private String intro;							// 블로그 소개 문구
	private List<BlogDetailPlaceDto> listBlogPlace;	// 블로그 장소 dto 리스트
}
