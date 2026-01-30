package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyBellListDto {
	private int bellIdx;					// 알림 idx
	private Integer reviewIdx;				// 장소 리뷰 idx
	private Integer recommendReviewIdx;		// 도시 리뷰 idx
	private String clickId;					// 클릭 아이디
	private String nickname;				// 회원 닉네임
	private String id;						// 회원 아이디
}
