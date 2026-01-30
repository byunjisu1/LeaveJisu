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
public class PlanDetailPlaceDto {
	private int planPlaceIdx;						// 여행 일정 장소 idx
	private int planIdx;							// 여행 일정 idx
	private int day;								// 여행 일자
	private String name;							// 장소명
	private String category;						// 장소 카테고리
	private int placeOrder;							// 장소 순서
	private int placeIdx;							// 장소 idx
	private List<PlanDetailPlaceMemoDto> listMemo;	// 장소 메모 리스트
}
