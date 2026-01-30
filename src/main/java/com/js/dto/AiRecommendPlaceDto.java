package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiRecommendPlaceDto {
	private int day;			// 여행 일자
	private int placeIdx;		// 장소 idx
	private int placeOrder;		// 장소 순서
	private String category;	// 장소 카테고리
	private String name;		// 장소명
	private String intro;		// 장소 소개
	private String placeImg;	// 장소 이미지
	private Double latitude;	// 장소 위도
	private Double longitude;	// 장소 경도
}
