package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMyPlaceDto {
	private String id;			// 회원 아이디
	private int placeIdx;		// 장소 idx
	private String likeDate;	// 찜 날짜
	private String content;		// 장소 메모
}
