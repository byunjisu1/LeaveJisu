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
public class PlaceReviewDto {
	private int reviewIdx;				// 장소 리뷰 idx
	private String nickname;			// 회원 닉네임
	private String profileImg;			// 회원 프로필 이미지
	private String content;				// 리뷰 내용
	private int star;					// 별점
	private String reviewDate;			// 리뷰 작성일
	private String tripDate;			// 여행 방문일
	private int reviewGood;				// 리뷰 추천 여부
	private int reviewGoodN;			// 리뷰 추천 개수
	private List<String> listReviewImg;	// 리뷰 이미지 리스트
}
