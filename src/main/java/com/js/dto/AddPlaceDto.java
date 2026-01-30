package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddPlaceDto {
	private int planIdx;		// 여행 일정 idx
	private String name;		// 장소명
	private String category;	// 장소 카테고리
	private String placeImg;	// 장소 이미지
	private String cityName;	// 도시명
	private double latitude;	// 장소 위도
	private double longitude;	// 장소 경도
	private int placeIdx;		// 장소 idx
}
