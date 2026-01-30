package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanCourseDto {
	private int placeIdx;		// 장소 idx
	private String name;		// 장소명
	private Double latitude;	// 장소 위도
	private Double longitude;	// 장소 경도
	private int placeOrder;		// 장소 순서
	private int day;			// 여행 일자
	private String category;	// 장소 카테고리
}
