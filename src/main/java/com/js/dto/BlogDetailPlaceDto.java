package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogDetailPlaceDto {
	private int blogIdx;		// 블로그 idx
	private int day;			// 여행 일자
	private int placeIdx;		// 장소 idx
	private int placeOrder;		// 장소 순서
	private String content;		// 블로그 내용
	private String placeImg;	// 장소 이미지
	private int placeStar;		// 장소 별점
	private String placeName;	// 장소명
	private String category;	// 장소 카테고리
	private int cityIdx;		// 도시 idx
}
