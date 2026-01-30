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
public class MyReviewListDto {
	private String cityName;			// 도시명
	private String name;				// 장소명
	private String category;			// 장소 카테고리
	private int reviewIdx;				// 장소 리뷰 idx
	private int recommendReviewIdx;		// 도시 리뷰 idx
	private int placeIdx;				// 장소 idx
	private String content;				// 리뷰 내용
	private int star;					// 별점
	private String reviewDate;			// 리뷰 작성일
	private String tripDate;			// 장소 방문일
	private int reviewGood;				// 리뷰 추천 여부
	private int reviewGoodN;			// 리뷰 추천 개수
	private List<String> listReviewImg;	// 리뷰 이미지 리스트
}
