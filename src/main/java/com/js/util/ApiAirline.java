package com.js.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.js.dao.TripInfoDao;
import com.js.dao.TripInfoDaoImpl;
import com.js.dto.FlightDto;

public class ApiAirline {

    // 항공사 코드 매핑
    private static final Map<String, String> AIRLINE_MAP = new HashMap<>();

    // 매핑된 항공사 이름 반환
    public static String getAirlineName(String code) {
        return AIRLINE_MAP.getOrDefault(code, code + " (미등록 항공사)");
    }

    // 공항 코드 매핑
    private static final Map<String, String> AIRPORT_MAP = new HashMap<>();

    public static String getAirportName(String code) {
        return AIRPORT_MAP.getOrDefault(code, code + " (미등록 공항)");
    }
    
    public static List<FlightDto> getFlightList(TripInfoDao tiDao, String dep, String dest, String startDate, int nop) {
    	List<FlightDto> listRet = null;
        try {
            String token = getAccessToken(
                "__",
                "__"
            );

            JSONArray data = searchFlights(
                token,
                dep,	// 출발지
                dest,	// 도착지
                startDate,	// 출발날짜
                nop,	// 인원수
                "KRW"
            );

            listRet = getAllFlightsWithDetails(tiDao, data);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listRet;
    }

    // Access Token 발급
    public static String getAccessToken(String apiKey, String apiSecret) throws Exception {
        URL url = new URL("__");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        String params = "grant_type=client_credentials&client_id=" + apiKey + "&client_secret=" + apiSecret;

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            response.append(line);

        reader.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getString("access_token");
    }

    // 항공편 검색 (Flight Offers Search API)
    public static JSONArray searchFlights(String token, String origin, String dest, String date, int adults, String currency) throws Exception {

        String urlStr = "__"
                + "originLocationCode=" + origin
                + "&destinationLocationCode=" + dest
                + "&departureDate=" + date
                + "&adults=" + adults
                + "&currencyCode=" + currency;

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();

        // 에러 시 상세 메시지 출력
        if (status != 200) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            StringBuilder errorMsg = new StringBuilder();
            String line;

            while ((line = errorReader.readLine()) != null)
                errorMsg.append(line);

            errorReader.close();

            throw new RuntimeException("API ERROR: " + errorMsg.toString());
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null)
            response.append(line);

        reader.close();

        JSONObject json = new JSONObject(response.toString());
        return json.getJSONArray("data");
    }
    
    // 항공편 출력 (최저가 포함)
    public static List<FlightDto> getAllFlightsWithDetails(TripInfoDao tiDao, JSONArray data) {
    	List<FlightDto> listRet = new ArrayList<>();
    	
        for (int i = 0; i < data.length(); i++) {
            JSONObject offer = data.getJSONObject(i);
            double price = offer.getJSONObject("price").getDouble("grandTotal");

            JSONArray itineraries = offer.getJSONArray("itineraries");
            JSONObject itinerary = itineraries.getJSONObject(0);

            String totalDuration = itinerary.getString("duration");   // 예: PT2H30M

            JSONArray segments = itinerary.getJSONArray("segments");

            // 첫 구간 = 출발 정보
            JSONObject firstSeg = segments.getJSONObject(0);
            JSONObject dep = firstSeg.getJSONObject("departure");

            // 마지막 구간 = 도착 정보
            JSONObject lastSeg = segments.getJSONObject(segments.length() - 1);
            JSONObject arr = lastSeg.getJSONObject("arrival");

            String airlineCode = firstSeg.getString("carrierCode");
            String airlineName = getAirlineName(airlineCode);

            String airlineId = airlineCode;  // KE
			String flightTime = formatDuration(totalDuration);  // 2시간 25분
			String airportIdDeparture = dep.getString("iataCode") ;  // ICN
			String timeDeparture = dep.getString("at").replace("T", " ");   // 2026-01-20 15:300:00
			String airportIdDestination = arr.getString("iataCode");  // NRT
			String timeDestination = arr.getString("at").replace("T", " ");  // 2026-01-20 17:55:00
			String transitType = null;  // 경유 정보 "직항" "경유 1" 등.
			if(segments.length() == 1) { transitType = "직항"; }
			else { transitType = "경유 " + (segments.length()-1) + "회"; }
			
			// DB 조회 결과를 변수에 담기
		    Map<String, String> airlineMap = tiDao.getAirlineLogoNameById(airlineId);
		    String logo = "";
		    String name = "";

		    if (airlineMap != null) {
		        logo = airlineMap.get("AIRLINE_LOGO");
		        name = airlineMap.get("AIRLINE_NAME");
		    } else {
		        // 데이터가 없을 경우 기본값 설정
		        logo = "default_logo.png";
		        name = airlineId; // 항공사 이름 대신 코드라도 출력
		    }
			
			FlightDto fDto = new FlightDto(airlineId, price, flightTime, airportIdDeparture, timeDeparture, airportIdDestination, timeDestination, transitType, name, logo);
			listRet.add(fDto);
        }  // end of for()
        return listRet;

    }

    // Duration 변환 함수 (PT2H30M → 2시간 30분)
    public static String formatDuration(String iso) {
        iso = iso.replace("PT", "");
        int hours = 0, minutes = 0;

        if (iso.contains("H")) {
            hours = Integer.parseInt(iso.substring(0, iso.indexOf("H")));
            iso = iso.substring(iso.indexOf("H") + 1);
        }
        if (iso.contains("M")) {
            minutes = Integer.parseInt(iso.substring(0, iso.indexOf("M")));
        }
        return hours + "시간 " + minutes + "분";
    }
    
    // 경유 시간 계산 함수 (경유 소요시간)
    public static String calcLayover(JSONObject seg1, JSONObject seg2) {
        String arrTime = seg1.getJSONObject("arrival").getString("at"); 
        String depTime = seg2.getJSONObject("departure").getString("at"); 

        java.time.LocalDateTime t1 = java.time.LocalDateTime.parse(arrTime);
        java.time.LocalDateTime t2 = java.time.LocalDateTime.parse(depTime);

        java.time.Duration diff = java.time.Duration.between(t1, t2);

        long hours = diff.toHours();
        long minutes = diff.toMinutes() % 60;

        return hours + "시간 " + minutes + "분";
    }
}
