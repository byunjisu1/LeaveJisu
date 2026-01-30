package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecommendPlanPlaceDto {
	private int placeIdx;		// 장소 idx
	private String placeName;	// 장소명
	private String placeImg;	// 장소 이미지
	private String intro;		// 장소 소개
	private String category;	// 장소 카테고리
	private String cityName;	// 도시명
	private String location;	// 장소 위치
	private Double latitude;	// 장소 위도
	private Double longitude;	// 장소 경도
	private int placeOrder;		// 장소 순서
	private String placeTime;	// 장소 방문 시간
}
