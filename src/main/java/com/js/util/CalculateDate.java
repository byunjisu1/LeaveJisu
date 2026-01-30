package com.js.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CalculateDate {
	/**
	 * formatDateWithDots(String dateStr)
	 * @param dateStr 시작날짜 DB 의 startDate
	 * @return 시작날짜 (yyyy.MM.dd(요일) 형식)
	 */
	public static String formatDateWithDots(String dateStr) {
	    // 1. 하이픈(-) 형식을 읽어오기 위한 설정
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    
	    // 2. 입력받은 "2025-10-08"을 날짜 객체로 변환
	    LocalDate date = LocalDate.parse(dateStr, inputFormatter);
	    
	    // 3. 요일 추출 (한글 요일)
	    String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
	    
	    // 4. 점(.) 형식으로 포맷팅 (마지막 dd 뒤에 점을 넣지 않음)
	    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
	    
	    // 5. 최종 결과: "2025.10.08(수)"
	    return String.format("%s(%s)", formattedDate, dayOfWeek);
	}
	
	/**
	 * calculateDate(String dateStr, int n)
	 * @param dateStr 시작날짜 (yyyy-MM-dd 형식)
	 * @param n 일수
	 * @return 마지막날짜 (yyyy.MM.dd(요일) 형식)
	 */
	public static String calculateDate(String dateStr, int n) {
		// 1. 입력 형식을 하이픈(-)으로 고정
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        // 2. 파싱 (이제 "2026-01-15"를 정확히 인식합니다)
        LocalDate date = LocalDate.parse(dateStr, inputFormatter);
        
        // 3. n일 후 계산
        LocalDate futureDate = date.plusDays(n-1);
        
        // 4. 요일 구하기 (한글로 출력하기 위해 Locale.KOREAN 설정)
        String dayOfWeek = futureDate.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
        
        // 5. 원하는 형식으로 포맷팅하여 리턴
        // 2025.10.08(요일) 형식으로 다시 포맷팅
        String formattedDate = futureDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        
        return String.format("%s(%s)", formattedDate, dayOfWeek);
    }
	
	public static List<String> getDatesBetween(String dateStr, int days) {
	    List<String> dateList = new ArrayList<>();
	    // 입력 형식이 '2025.10.05' 이므로 패턴 설정
	    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    // 출력 형식이 '10.05(일)' 이므로 패턴 설정
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM.dd");

	    LocalDate startDate = LocalDate.parse(dateStr, inputFormatter);

	    for (int i = 0; i < days; i++) {
	        LocalDate current = startDate.plusDays(i);
	        String dayOfWeek = current.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.KOREAN);
	        String formatted = current.format(outputFormatter);
	        
	        // 최종 형태: "10.05(일)"
	        dateList.add(formatted + "(" + dayOfWeek + ")");
	    }
	    return dateList;
	}
}
