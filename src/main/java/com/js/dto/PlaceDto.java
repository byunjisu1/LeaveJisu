package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDto {
	private int placeIdx;			// 장소 idx
	private String international;	// 국내 or 해외
	private int cityIdx;			// 도시 idx
	private String category;		// 도시 카테고리
	private String name;			// 장소명
	private String intro;			// 장소 소개
	private String location;		// 장소 위치
	private String placeImg;		// 장소 이미지
	private String phone;			// 장소 전화번호
	private String time;			// 오픈 시간
	private String close;			// 마감 시간
	private double latitude;		// 장소 위도
	private double longitude;		// 장소 경도
	private String cityName;  		// 도시명
	private Double starScore;  		// 별점
	private int reviewCnt;     		// 리뷰개수
	private int heartCnt;      		// 찜개수
	private int myHeart;			// 찜여부
}
