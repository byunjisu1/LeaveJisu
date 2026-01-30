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
public class RecommendPlanDetailDto {
	private int recommendPlanIdx;							// 추천 일정 idx
	private String concept;									// 추천 일정 컨셉
	private int day;										// 여행 일자
	private String content;									// 추천 일정 내용
	private List<RecommendPlanPlaceDto> listPlace;			// 장소 리스트
}
