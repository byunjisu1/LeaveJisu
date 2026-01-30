package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyPlaceListDto {
	private int placeIdx;		// 장소 idx
	private String category;	// 장소 카테고리
	private String cityName;	// 도시명
	private String name;		// 장소명
	private String intro;		// 장소 소개
	private String placeImg;	// 장소 이미지
	private String content;		// 장소 정보
	private double starScore;	// 장소 별점
	private int reviewCnt;		// 리뷰 개수
}
