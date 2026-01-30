package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CityListDto {
	private int cityIdx;		// 도시 idx
	private String country;		// 나라
	private String cityName;	// 도시명
	private String nearCity;	// 근처 도시
	private String cityImg;		// 도시 이미지
	
}
