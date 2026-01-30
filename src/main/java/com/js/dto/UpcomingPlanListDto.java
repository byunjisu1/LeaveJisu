package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpcomingPlanListDto {	
	private int planIdx;		// 여행 일정 idx
	private String startDate;	// 여행 시작 날짜
	private int days;			// 여행 기간
	private String cityName;	// 도시명
	private String cityImg;		// 도시 이미지
}
