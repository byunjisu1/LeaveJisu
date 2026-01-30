package com.js.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {
	private String airlineId;  				// 항공사 id  
	private double price;   				// 가격
	private String flightTime;  			// 비행 시간
	private String airportIdDeparture;  	// 출발 공항 id
	private String timeDeparture;  			// 출발 시간
	private String airportIdDestination; 	// 도착 공항 id
	private String timeDestination;  		// 도착 시간
	private String transitType;  			// 경유 정보
	private String airlineName;				// 항공사명
	private String airlineLogo;				// 항공사 로고
}
