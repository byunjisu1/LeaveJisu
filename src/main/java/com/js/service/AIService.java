package com.js.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.dao.TripInfoDao;

@Service
public class AIService {
	@Autowired
	TripInfoService tiSvc;	// 여행 정보 서비스
	@Autowired
	TripInfoDao tiDao;		// 여행 정보 dao
	
	private final String API_URL = "__";
	private final String API_KEY = "__";
	// [3] 여행 일정 추천 _ 클릭이력 기반 (ChatGPT API)
	public String[] recommendApiByClick(String loginId) {
		// 1. 결과 받아오기
		List<Map<String, Object>> listClickHistory = tiDao.selectClickHistoryOneWeekByMemberId(loginId);
		// 클릭 이력이 10개 이하인 경우 처리
	    if (listClickHistory == null || listClickHistory.size() < 10) {
	        return new String[] { "INSUFFICIENT_DATA" };
	    }
		List<Map<String, Object>> listPlaces = tiDao.selectAllPlaceList();
		List<String> listPlacesToTransfer = new ArrayList<>();
		
		for(Map<String, Object> place : listPlaces) {
			int placeIdx = Integer.parseInt(place.get("PLACE_IDX")+"");
			String placeName = (String)place.get("NAME");
			String cityName = (String)place.get("CITY_NAME");
			listPlacesToTransfer.add(placeIdx + ":" + placeName + "(" + cityName + ")");
		}
		
		Map<String, Object> mapResult = null;
		try {
			mapResult = itineraryPlanApiByClick(listPlacesToTransfer, listClickHistory);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 2. DB에 반영하기
		tiDao.deleteAIRecommendPlaceListByMemberId(loginId);
		for(Map<String, Object> map1 : (List<Map<String,Object>>)mapResult.get("plan")) {
			//System.out.println(map1);
			int day = (int)map1.get("day");
			int order = (int)map1.get("order");
			int placeIdx = (int)map1.get("place_idx");
			tiDao.insertAIRecommendPlace(loginId, day, order, placeIdx);
		}
		
		return new String[] {
			(String)mapResult.get("cityName"),
			(Integer)(mapResult.get("days"))+""
		};
	}
	
	// [3] 여행 일정 추천 _ 클릭이력 기반 (ChatGPT API) 내부 호출.
	private Map<String, Object> itineraryPlanApiByClick(List<String> listPlacesToTransfer, List<Map<String, Object>> listClickHistory) throws Exception {
		Map<String, Object> mapResult = new HashMap<>();
		
		String result = callItineraryPlanGptByClick(listPlacesToTransfer, listClickHistory);
	    ObjectMapper objectMapper = new ObjectMapper();
	    
	    // 1단계: 전체 응답 파싱
	    JsonNode rootNode = objectMapper.readTree(result);
	    // 2단계: content 추출 (JSON 모드이므로 content 자체가 JSON 문자열임)
	    String contentString = rootNode.path("choices").get(0).path("message").path("content").asText();
	    JsonNode resJson = objectMapper.readTree(contentString);

	    String recommendedCity = resJson.path("recommended_city").asText();
	    int totalDays = resJson.path("total_days").asInt();
	    
	    // 2. 일정 리스트 추출
	    JsonNode itineraryArray = resJson.path("itinerary");
	    List<Map<String, Object>> listItinerary = objectMapper.readValue(
	        itineraryArray.toString(), 
	        new TypeReference<List<Map<String, Object>>>() {}
	    );
	    mapResult.put("cityName", recommendedCity);
	    mapResult.put("days", totalDays);
	    mapResult.put("plan", listItinerary);
	    
		return mapResult;
	}

	// [3] 여행 일정 추천 _ 클릭이력 기반 (ChatGPT API) 내부 호출.
	private String callItineraryPlanGptByClick(List<String> listPlacesToTransfer, List<Map<String, Object>> listClickHistory) {
	    RestTemplate restTemplate = new RestTemplate();

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(API_KEY);

	    // 프롬프트 구성
	    String prompt = String.format(
    		"너는 데이터 기반의 스마트 여행 플래너야. 제공된 [사용자 클릭 히스토리]와 [가용 장소 목록]을 분석하여 최적의 여행 계획을 세워줘.\n\n" +
		    "[사용자 클릭 히스토리]\n%s\n" +
		    "(설명: 사용자가 관심을 보인 장소의 place_idx와 클릭 횟수야. 클릭수가 높을수록 선호도가 높아.)\n\n" +
		    "[가용 장소 목록]\n%s\n" +
		    "(설명: 추천 가능한 장소들의 상세 정보야.)\n\n" +
		    "**미션 및 제약사항**:\n" +
		    "1. **도시 및 기간 결정**: 클릭 히스토리를 바탕으로 사용자가 가장 관심 있어 하는 '도시'를 딱 1개 선정하고, 그 도시만을 충분히 즐길 수 있는 적절한 '여행 일수'를 결정해. (선정된 장소의 도시가 딱 1개여야 함)\n" +
		    "2. **장소 선정**: 반드시 제공된 [가용 장소 목록]의 'place_idx'만 사용해. 클릭수가 높은 장소를 우선적으로 일정에 배치해.\n" +
		    "3. **일정 밀도**: 하루에 약 5~6곳의 장소를 방문하는 스케줄로 짜줘.\n" +
		    "4. **결과 형식**: 반드시 아래의 JSON 구조로만 응답해 (다른 설명 텍스트 금지).\n\n" +
		    "**JSON 구조 예시**:\n" +
		    "{\n" +
		    "  \"recommended_city\": \"선정된 도시명\",\n" +
		    "  \"total_days\": 3,\n" +
		    "  \"itinerary\": [\n" +
		    "    {\"day\": 1, \"order\": 1, \"place_idx\": 101},\n" +
		    "    {\"day\": 1, \"order\": 2, \"place_idx\": 105},\n" +
		    "    ...\n" +
		    "  ]\n" +
		    "}",
	        listPlacesToTransfer.toString(),
	        listClickHistory.toString()
	    );

	    Map<String, Object> message = Map.of(
	        "role", "user",
	        "content", prompt
	    );

	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("model", "gpt-4o");
	    requestBody.put("messages", List.of(message));
	    requestBody.put("response_format", Map.of("type", "json_object")); // JSON 모드 활성화
	    requestBody.put("max_tokens", 2000);

	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

	    try {
	        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
	        return response.getBody();
	    } catch (Exception e) {
	        return "[]"; // 에러 발생 시 빈 배열 반환
	    }
	}
	
	
	// [2] 여행 일정 추천 _ 옵션 선택 (ChatGPT API)  
	public void recommendApiByOption(String loginId, String cityName, int days, String with, String style, String travelTempo) {
		// 1. 결과 받아오기
		List<Map<String, Object>> listMapPlaces = tiSvc.getPlaceListByCityName(cityName);
		Map<String, String> mapOptionsUserSelected = new HashMap<>();
		mapOptionsUserSelected.put("도시명", cityName);
		mapOptionsUserSelected.put("여행기간", (days-1)+"박"+days+"일");
		mapOptionsUserSelected.put("동행여부", with);
		mapOptionsUserSelected.put("선호여행스타일", style);
		mapOptionsUserSelected.put("선호여행일정", travelTempo);
	
		List<Map<String, Object>> listMapResult = null;
		try {
			listMapResult = itineraryPlanApiByOption(listMapPlaces, mapOptionsUserSelected);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// 2. DB에 반영하기
		tiDao.deleteAIRecommendPlaceListByMemberId(loginId);
		for(Map<String, Object> map1 : listMapResult) {
			int day = (int)map1.get("day");
			int order = (int)map1.get("order");
			int placeIdx = (int)map1.get("place_idx");
			tiDao.insertAIRecommendPlace(loginId, day, order, placeIdx);
		}
	}
	
	// [2] 여행 일정 추천 _ 옵션 선택 (ChatGPT API) 내부 호출
	private List<Map<String, Object>> itineraryPlanApiByOption(List<Map<String, Object>> listMapPlaces, Map<String, String> options) throws Exception {
	    String result = callItineraryPlanGptByOption(listMapPlaces, options);
	    ObjectMapper objectMapper = new ObjectMapper();

	    // 1단계: 전체 응답 파싱
	    JsonNode rootNode = objectMapper.readTree(result);
	    // 2단계: content 추출 (JSON 모드이므로 content 자체가 JSON 문자열임)
	    String contentString = rootNode.path("choices").get(0).path("message").path("content").asText();

		//contentString은 현재 "{ \"itinerary\": [...] }" 형태입니다.
		JsonNode contentNode = objectMapper.readTree(contentString);
		
		// 'itinerary' 키에 해당하는 배열만 꺼내서 List로 변환합니다.
		JsonNode itineraryArray = contentNode.path("itinerary");
		
		return objectMapper.readValue(itineraryArray.toString(), new TypeReference<List<Map<String, Object>>>() {});
	}

	// [2] 여행 일정 추천 _ 옵션 선택 (ChatGPT API) 내부 호출.
	private String callItineraryPlanGptByOption(List<Map<String, Object>> listMapPlaces, Map<String, String> options) {
	    RestTemplate restTemplate = new RestTemplate();

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(API_KEY);

	    // 프롬프트 구성
	    String prompt = String.format(
	        "너는 전문 여행 가이드야. 아래 제공된 [장소 목록] 내에서만 선택하여 [사용자 취향]에 맞는 여행 일정을 짜줘.\n\n" +
	        "[장소 목록]\n%s\n\n" +
	        "[사용자 취향]\n%s\n\n" +
	        "**주의사항**:\n" +
	        "1. 반드시 제공된 'place_idx'만 사용해.\n" +
	        "2. '선호여행일정'이 'marching(빼곡한)'이면 하루에 7-8곳을, 'airy(널널한)'면 하루에 4-5곳을 배치해.\n" +
	        "3. 응답은 반드시 다른 텍스트 없이 JSON Array 형식으로만 답해줘.\n" +
	        "4. 형식 : { \"itinerary\": [{\"day\": 1, \"order\": 1, \"place_idx\": 101}, ...] }",
	        listMapPlaces.toString(),
	        options.toString()
	    );

	    Map<String, Object> message = Map.of(
	        "role", "user",
	        "content", prompt
	    );

	    Map<String, Object> requestBody = new HashMap<>();
	    requestBody.put("model", "gpt-4o");
	    requestBody.put("messages", List.of(message));
	    requestBody.put("response_format", Map.of("type", "json_object")); // JSON 모드 활성화
	    requestBody.put("max_tokens", 2000);

	    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

	    try {
	        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
	        return response.getBody();
	    } catch (Exception e) {
	        return "[]"; // 에러 발생 시 빈 배열 반환
	    }
	}
	
    
    // [1] 짐싸기 테스트.
	public static void mainPackingAnalysis(String[] args) throws Exception {
		byte[] fileContent = Files.readAllBytes(Paths.get("d:/Temp/upload/스.png"));
		String base64Image = Base64.getEncoder().encodeToString(fileContent);
		Map<String, Object> mapResult = new AIService().packingApi(base64Image, "베네수엘라", 25);
		System.out.println("present:");
		for(String item : (String[])mapResult.get("present")) { System.out.print(item + " "); }
		System.out.println();
		
		System.out.println("missing:");
		for(String item : (String[])mapResult.get("missing")) { System.out.print(item + " "); }
		System.out.println();
		
		System.out.println("advice:" + mapResult.get("advice"));
	}

	// [1] 짐싸기 (ChatGPT API)
	// 짐싸기 (ChatGPT API) packingApi -> analyzeSuitcase -> 
	public Map<String, Object> packingApi(String imageUrl, String cityName, int days) throws Exception {
		Map<String, Object> mapRet = new HashMap<>();

		String result = new AIService().analyzeSuitcase(imageUrl, cityName, days);
		ObjectMapper objectMapper = new ObjectMapper();

        // 1단계: 전체 응답 문자열을 JsonNode로 변환
        JsonNode rootNode = objectMapper.readTree(result);

        // 2단계: choices -> 0번째 -> message -> content 추출
        // 이 content는 "{ \"present_items\": ... }" 형태의 문자열입니다.
        String contentString = rootNode.path("choices").get(0).path("message").path("content").asText();

        // 3단계: content 안의 문자열을 다시 JSON 객체로 변환
        JsonNode resultJson = objectMapper.readTree(contentString);

        // 최종 확인
        String[] arrayPresent = objectMapper.readValue(resultJson.path("present_items").toString(), String[].class);
        String[] arrayMissing = objectMapper.readValue(resultJson.path("missing_items").toString(), String[].class);
        String advice = resultJson.path("advice").asText();
        
        mapRet.put("present", arrayPresent);
        mapRet.put("missing", arrayMissing);
        mapRet.put("advice", advice);
        
        return mapRet;
	}

	// [1] 짐싸기 (ChatGPT API) 내부 호출.
    private String analyzeSuitcase(String base64Image, String city, int days) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(API_KEY);

        // 2. 메시지 구성 (이미지 포함)
        List<Map<String, Object>> contentList = new ArrayList<>();

        String additional = "";
        if(city!=null) {
	        additional = city + " " + days + "박 " + (days+1) + "일. ";
        } else {
        	additional = "파리 4박 5일. ";	// 기본값
        }
        // 텍스트 프롬프트: JSON 형식을 엄격히 요구함
        contentList.add(Map.of(
            "type", "text", 
            "text", "이 사진은 내 캐리어 내부야. " + additional + "여행을 위해 부족한 짐이 무엇인지 분석해서 다음 JSON 형식으로만 답해줘(단, 한글로 답할 것, 그리고 되도록 많이 답할 것): " +
                    "{ \"present_items\": [이미 있는 물건들], \"missing_items\": [부족한 물건들], \"advice\": \"한줄 조언\" }"
        ));
        
        // 이미지 데이터
        String imageUrl = base64Image;
        if(!base64Image.startsWith("http")) {
        	imageUrl = "data:image/jpeg;base64," + imageUrl; 
        }
        contentList.add(Map.of(
            "type", "image_url",
            "image_url", Map.of("url", imageUrl)
        ));

        Map<String, Object> message = Map.of(
            "role", "user",
            "content", contentList
        );

        // 3. 요청 바디 구성 (JSON 모드 사용)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", List.of(message));
        requestBody.put("response_format", Map.of("type", "json_object")); // 중요: JSON 응답 강제
        requestBody.put("max_tokens", 500);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, entity, String.class);
            return response.getBody(); // 분석된 JSON 문자열 반환
        } catch (Exception e) {
            return "{\"error\": \"" + e.getMessage() + "\"}";
        }
    }
}
