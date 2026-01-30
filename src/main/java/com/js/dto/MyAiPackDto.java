package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyAiPackDto {
	private String packImg;		// 캐리어 이미지
	private String presentList;	// 현재 짐 목록
	private String missingList;	// 잊은 짐 목록
	private String advice;		// 조언
}
