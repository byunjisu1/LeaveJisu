package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPlaceMapDto {
	private int day;			// 일자
	private int placeOrder;		// 장소 순서
	private String name;		// 장소명
	private double latitude;	// 장소 위도
	private double longitude;	// 장소 경도
}
