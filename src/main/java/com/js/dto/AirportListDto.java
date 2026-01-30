package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AirportListDto {
	private String airportId;		// 공항 id
	private String airportName;		// 공항명
	private String country;			// 나라
}
