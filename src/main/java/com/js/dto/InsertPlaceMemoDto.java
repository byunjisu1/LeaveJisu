package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsertPlaceMemoDto {
	private Integer memoIdx;	// 메모 idx
	private int planPlaceIdx;	// 일정 장소 idx
	private String content;		// 메모 내용
}
