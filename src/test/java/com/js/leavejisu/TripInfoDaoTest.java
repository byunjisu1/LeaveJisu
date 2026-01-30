package com.js.leavejisu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.js.dao.TripInfoDao;
import com.js.dto.AiRecommendPlaceDto;
import com.js.dto.AirportListDto;
import com.js.dto.BlogCommentListDto;
import com.js.dto.BlogDetailDto;
import com.js.dto.BlogDetailPlaceDto;
import com.js.dto.BlogListDto;
import com.js.dto.BlogPlaceMapDto;
import com.js.dto.CityListDto;
import com.js.dto.FlightDto;
import com.js.dto.MyAiPackDto;
import com.js.dto.PlaceDto;
import com.js.dto.PlanCourseDto;
import com.js.dto.RecommendPlanDetailDto;
import com.js.dto.RecommendPlanPlaceDto;
import com.js.dto.ReviewListDto;
import com.js.util.ApiAirline;
import com.js.util.ApiExplorer;
import com.js.util.XmlParserUtil;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class TripInfoDaoTest {
	@Autowired
	TripInfoDao tiDao;
	
	// selectCityList
	@Test
	public void testSelectCityList() throws Exception {
		// 1. Given : 없음.
		
		// 2. When
		List<CityListDto> listCity = tiDao.selectCityList();
		
		// 3. Then
		assertNotNull("listCity 리스트가 null이 아니어야 함", listCity);
		for(CityListDto dto : listCity) {
			System.out.println(dto.getCityIdx() + " / " + dto.getCityName() + " / " + dto.getCityImg());
		}
	}
	/*
	// selectTop10
	@Test
	public void testSelectPlaceTop10() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		List<PlaceDto> listPlaceTop10 = tiDao.selectTop10(cityIdx);
		
		// Then
		assertNotNull("listPlaceTop10 리스트가 null이 아니어야 함", listPlaceTop10);
		for(PlaceDto dto : listPlaceTop10) {
			System.out.println(dto.getCityIdx() + " / " + dto.getPlaceIdx() + " / " + dto.getName());
		}
	}
	
	// selectConceptList
	@Test
	public void testSelectConceptList() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		List<RecommendPlanDetailDto> listConcept = tiDao.selectConceptList(cityIdx);
		
		// Then
		assertNotNull("listConcept 리스트가 null이 아니어야 함", listConcept);
		for(RecommendPlanDetailDto dto : listConcept) {
			System.out.println(dto.getDay() + " 일차 : " + dto.getConcept() + " / " + dto.getContent());
		}
	}
	
	// selectCityNameByIdx
	@Test
	public void testSelectCityNameByIdx() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		String cityName = tiDao.selectCityNameByIdx(cityIdx);
		
		// Then
		System.out.println("조회된 도시명 : " + cityName);
	}
	
	// selectRecommendPlaceList
	@Test
	public void testSelectRecommendPlaceList() throws Exception {
		// Given
		int recommendPlanIdx = 13;
		
		// When
		List<RecommendPlanPlaceDto> listRecommendPlace = tiDao.selectRecommendPlaceList(recommendPlanIdx);
		
		// Then
		assertNotNull("listRecommendPlace 리스트가 null이 아니어야 함", listRecommendPlace);
		for(RecommendPlanPlaceDto dto : listRecommendPlace) {
			System.out.println(dto.getPlaceOrder() + " / " + dto.getPlaceName() + " / " + dto.getIntro());
		}
	}
	
	// selectBlogList
	@Test
	public void testSelectBlogList() throws Exception {
		// Given : x
		
		// When
		List<BlogListDto> listBlog = tiDao.selectBlogList();
		
		// Then
		assertNotNull("listBlog 리스트가 null이 아니어야 함", listBlog);
		for(BlogListDto dto : listBlog) {
			System.out.println(dto.getNickname() + " / " + dto.getBlogIdx() + " / " + dto.getTitle() + " / " + dto.getIntro());
		}
	}
	
	// selectBlogImgList
	@Test
	public void testSelectBlogImgList() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<String> blogImg = tiDao.selectBlogImgList(blogIdx);
		
		// Then
		System.out.println(blogImg);
	}
	
	// selectRecommendReviewList
	@Test
	public void testSelectRecommendReviewList() throws Exception {
		// Given
		int cityIdx = 16;
		String id = "jj5678";
		
		// When
		List<ReviewListDto> listReview = tiDao.selectRecommendReviewList(cityIdx, id, null);
		
		// Then
		assertNotNull("listReview 리스트가 null이 아니어야 함", listReview);
		for(ReviewListDto dto : listReview) {
			System.out.println(dto.getNickname() + " : " + dto.getContent() + " / 작성 날짜 : " + dto.getReviewDate() + " / 리뷰 추천 수 : " + dto.getReviewGoodN());
		}
	}
	
	// selectRecommendReviewImgList
	@Test
	public void testSelectRecommendReviewImgList() throws Exception {
		// Given
		int recommendReviewIdx = 32;
		
		// When
		List<String> listReviewImg = tiDao.selectRecommendReviewImgList(recommendReviewIdx);
		
		// Then
		assertNotNull("listReviewImg 리스트가 null이 아니어야 함", listReviewImg);
		for(String s : listReviewImg) {
			System.out.println(s);
		}
	}
	
	// insertRecommendReview
	@Test
	@Transactional
	public void testInsertRecommendReview() throws Exception {
		// Given
		int cityIdx = 16;
		String id = "YG1017";
		String content = "내용1";
		String tripDate = "202601";
		
		// When
		int recommendReviewIdx = tiDao.insertRecommendReview(cityIdx, id, content, tripDate);
		
		// Then
		System.out.println(recommendReviewIdx);
	}
	
	// insertRecommendReviewPic
	@Test
	@Transactional
	public void testInsertRecommendReviewPic() throws Exception {
		// Given
		int recommendReviewIdx = 1;
		String reviewImg = "ac.jpg";
		
		// When
		tiDao.insertRecommendReviewPic(recommendReviewIdx, reviewImg);
		
		// Then
	}
	
	// selectBlogDetail
	@Test
	public void testSelectBlogDetail() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		BlogDetailDto listBlog = tiDao.selectBlogDetail(blogIdx);
		
		// Then
		assertNotNull("listBlog 리스트가 null이 아니어야 함", listBlog);
		System.out.println(listBlog.getTitle() + " / " + listBlog.getIntro());
	}
	
	// selectBlogDetailPlace
	@Test
	public void testSelectBlogDetailPlace() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<BlogDetailPlaceDto> listBlog = tiDao.selectBlogDetailPlace(blogIdx);
		
		// Then
		assertNotNull("listBlog 리스트가 null이 아니어야 함", listBlog);
		for(BlogDetailPlaceDto dto : listBlog) {
			System.out.println(dto.getDay() + "일차 " + dto.getPlaceName() + " / " + dto.getPlaceOrder() + " / " + dto.getContent());
		}
	}
	
	// selectPlanCourse
	@Test
	public void testSelectPlanCourse() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<PlanCourseDto> listCourse = tiDao.selectPlanCourse(blogIdx);
		
		// Then
		assertNotNull("listCourse 리스트가 null이 아니어야 함", listCourse);
		for(PlanCourseDto dto : listCourse) {
			System.out.println(dto.getDay() + "일차 " + dto.getPlaceOrder() + "번 : " + dto.getName());
		}
	}
	
	// selectBlogCommentList
	@Test
	public void testSelectBlogCommentList() throws Exception {
		// Given
		String id = "YG1017";
		int blogIdx = 4;
		
		// When
		List<BlogCommentListDto> listBlogComment = tiDao.selectBlogCommentList(id, blogIdx);
		
		// Then
		assertNotNull("listBlogComment 리스트가 null이 아니어야 함", listBlogComment);
		for(BlogCommentListDto dto : listBlogComment) {
			System.out.println(dto.getNickname() + " : " + dto.getContent() + " 좋아요 여부 : " + dto.getCommentGood() + " / 좋아요 수 : " + dto.getCommentGoodN());
		}
	}
	
	// selectBlogCommentByIdx
	@Test
	public void testSelectBlogCommentByIdx() throws Exception {
		// Given
		String id = "YG1017";
		int blogCommentIdx = 4;
		
		// When
		BlogCommentListDto listCommentByIdx = tiDao.selectBlogCommentByIdx(id, blogCommentIdx);
		
		// Then
		System.out.println(listCommentByIdx.getNickname() + " : " + listCommentByIdx.getContent());
	}
	
	// selectBlogPlaceMapList
	@Test
	public void testSelectBlogPlaceMapList() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<BlogPlaceMapDto> listPlaceMap = tiDao.selectBlogPlaceMap(blogIdx);
		
		// Then
		assertNotNull("listPlaceMap 리스트가 null이 아니어야 함", listPlaceMap);
		for(BlogPlaceMapDto dto : listPlaceMap) {
			System.out.println(dto.getDay() + "일차, " + dto.getPlaceOrder() + "번 장소 : " + dto.getName() + " / " + dto.getLatitude() + ", " + dto.getLongitude());
		}
	}
	
	// deleteBlogCommentGood
	@Test
	@Transactional
	public void testDeleteBlogCommentGood() throws Exception {
		// Given
		String id = "YG1017";
		int blogCommentIdx = 3;
		
		// When
		tiDao.deleteBlogCommentGood(id, blogCommentIdx);
	}
	
	// insertBlogCommentGood
	@Test
	@Transactional
	public void testInsertBlogCommentGood() throws Exception {
		// Given
		int blogCommentIdx = 1;
		String id = "YG1017";
		
		// When
		tiDao.insertBlogCommentGood(id, blogCommentIdx);
	}
	
	// selectBlogCommentN
	@Test
	public void testSelectBlogCommentN() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<Map<String, Object>> listCommentN = tiDao.getBlogCommentN(blogIdx);
		
		// Then
		assertNotNull("listCommentN 리스트가 null이 아니어야 함", listCommentN);
		for(Map<String, Object> map : listCommentN) {
	        System.out.println("전체 데이터: " + map); 
		}
	}
	
	// insertBlogComment
	@Test
	@Transactional
	public void testInsertBlogComment() throws Exception {
		// Given
		int blogIdx = 4;
		String id = "YG1017";
		String content = "짱";
		
		// When
		tiDao.insertBlogComment(blogIdx, id, content);
	}
	
	// deleteBlogComment
	@Test
	@Transactional
	public void testDeleteBlogComment() throws Exception {
		// Given
		int blogCommentIdx = 2;
		
		// When
		tiDao.deleteBlogComment(blogCommentIdx);
	}
	
	// updateBlogComment
	@Test
	@Transactional
	public void testUpdateBlogComment() throws Exception {
		// Given
		String content = "수정@";
		int blogCommentIdx = 2;
		
		// When
		tiDao.updateBlogComment(content, blogCommentIdx);
	}
	
	// deleteRecommendPlanReviewGood
	@Test
	@Transactional
	public void testDeleteRecommendPlanReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int recommendReviewIdx = 2;
		
		// When
		tiDao.deleteRecommendPlanReviewGood(id, recommendReviewIdx);
	}
	
	// insertRecommendPlanReviewGood
	@Test
	@Transactional
	public void testInsertRecommendPlanReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int recommendReviewIdx = 1;
		
		// When
		tiDao.insertRecommendPlanReviewGood(id, recommendReviewIdx);
	}
	
	// selectRecommendReviewGoodN
	@Test
	public void testSelectRecommendReviewGoodN() throws Exception {
		// Given
		int recommendReviewIdx = 2;
		
		// When
		int reviewGoodN = tiDao.getRecommendReviewGoodN(recommendReviewIdx);
		
		// Then
		System.out.println(reviewGoodN);
	}

	// selectAirportList
	@Test
	public void testSelectAirportList() throws Exception {
		// Given : x
		
		// When
		List<AirportListDto> listAirport = tiDao.selectAirportList();
		
		// Then
		assertNotNull("listAirport 리스트가 null이 아니어야 함", listAirport);
		for(AirportListDto dto : listAirport) {
			System.out.println(dto.getAirportId() + " / " + dto.getAirportName());
		}
	}

	// setAirlineLogo
	@Test
	public void testSelectAirlineId() throws Exception {
		// Given : x
		
		// When
		List<String> listAirlineId = tiDao.selectAirlineIdList();
		
		// Then
		assertNotNull("listAirport 리스트가 null이 아니어야 함", listAirlineId);
		for(String id : listAirlineId) {
			try {
				System.out.println("ID : " + id);
				String xml = ApiExplorer.getAirlineLogoImageUrl(id);
				String imgUrl = XmlParserUtil.getAirlineImage(xml);
				tiDao.setAirlineLogo(id, imgUrl);
			} catch(Exception e) { System.out.println("."); }
		}
	}

    public final String[] AIRLINE_CODES = {
	        "KE", "OZ", "7C", "LJ", "TW", "BX", "RS", "JL", "NH", "ZG", 
	        "MM", "GK", "IJ", "UA", "DL", "AA", "AF", "BA", "LH", "SQ", 
	        "CX", "QF", "NZ", "VJ", "VN", "QH", "FD", "SL", "TG", "TR", 
	        "MH", "AK", "GA", "ID", "CA", "CZ", "MU", "FM", "ZH", "HX", 
	        "UO", "BR", "CI", "IT", "KL", "IB", "AZ", "SK", "OS", "LX", 
	        "LO", "TK", "PC", "W6", "U2", "FR", "WS", "AC", "B6", "F9", 
	        "NK", "EK", "EY", "QR", "SV", "WY"
    };
    public final String[] AIRLINE_NAMES = {
        "대한항공", "아시아나항공", "제주항공", "진에어", "티웨이항공", "에어부산", "에어서울", "일본항공 (JAL)", "ANA 전일본공수", "ZIPAIR", 
        "Peach Aviation", "Jetstar Japan", "Spring Japan", "United Airlines", "Delta Air Lines", "American Airlines", "Air France", "British Airways", "Lufthansa", "Singapore Airlines", 
        "Cathay Pacific", "Qantas", "Air New Zealand", "VietJet Air", "Vietnam Airlines", "Bamboo Airways", "Thai AirAsia", "Thai Lion Air", "Thai Airways", "Scoot", 
        "Malaysia Airlines", "AirAsia", "Garuda Indonesia", "Batik Air", "Air China", "China Southern Airlines", "China Eastern Airlines", "Shanghai Airlines", "Shenzhen Airlines", "Hong Kong Airlines", 
        "Hong Kong Express", "EVA Air", "China Airlines", "Tigerair Taiwan", "KLM 네덜란드항공", "이베리아 항공", "ITA Airways", "Scandinavian Airlines", "Austrian Airlines", "Swiss International Air Lines", 
        "LOT Polish Airlines", "Turkish Airlines", "Pegasus Airlines", "Wizz Air", "easyJet", "Ryanair", "WestJet", "Air Canada", "JetBlue Airways", "Frontier Airlines", 
        "Spirit Airlines", "Emirates", "Etihad Airways", "Qatar Airways", "Saudia Airlines", "Oman Air"
    };
    private String codeToName(String code) {
    	for(int i=0; i<=AIRLINE_CODES.length; i++) {
    		if(code!=null && code.equals(AIRLINE_CODES[i])) {
    			return AIRLINE_NAMES[i];
    		}
    	}
    	return null;
    }

	@Test
	public void testSelectAirlineTicket() throws Exception {
		// Given : x
		String dep = "ICN";
		String dest = "NRT";
		String startDate = "2026-01-20";
		int nop = 1;
		
		// When
		List<FlightDto> listFlight = ApiAirline.getFlightList(tiDao, dep, dest, startDate, nop);
		
		// Then
		assertNotNull("listFlight 리스트가 null이 아니어야 함", listFlight);
		for(FlightDto dto : listFlight) {
			try {
				System.out.println("ID : " + dto.getAirlineId() + " / " + dto.getPrice() + "원 " + dto.getTransitType() + " / " + dto.getAirportIdDeparture() + " -> " + dto.getAirportIdDestination());
			} catch(Exception e) { System.out.println("."); }
		}
	}

	// selectMyAiPackList
	@Test
	public void testSelectMyAiPackList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		MyAiPackDto MyAiPack = tiDao.selectMyAiPackList(id);
		
		// Then
		assertNotNull("MyAiPack이 null이 아니어야 함", MyAiPack);
		System.out.println(MyAiPack.getPresentList() + "/" + MyAiPack.getMissingList() + ", 조언 : " + MyAiPack.getAdvice());
	}
	
	// selectWriterIdByRecommendReviewIdx
	@Test
	public void testSelectWriterIdByReviewIdx() throws Exception {
		// Given
		int recommendReviewIdx = 1;
		
		// When
		String writerId = tiDao.selectWriterIdByIdx(recommendReviewIdx);
		
		// Then
		assertEquals("아이디가 같아야 함", writerId, "ss1234");
		System.out.println("writerId : " + writerId);
		
	}
	
	// deleteMyCityReview
	@Test
	@Transactional
	public void testDeleteMyCityReview() throws Exception {
		// Given
		int recommendReviewIdx = 1;
		
		// When
		tiDao.deleteMyCityReview(recommendReviewIdx);
		
		// Then
	}
	
	// selectCityImgByIdx
	@Test
	public void testSelectCityImgByIdx() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		String img = tiDao.selectCityImgByIdx(cityIdx);
		
		// Then
		System.out.println(img);
	}
	
	// select(All)PlaceListByCityName
	@Test
	public void testSelectPlaceListByCityName() throws Exception {
		// Given
		String cityName = "파리";
		
		// When
		List<Map<String, Object>> listResult = tiDao.selectAllPlaceList();
		
		// Then
		assertNotNull("listResult 리스트가 null이 아니어야 함", listResult);
		for(Map<String, Object> mapEl : listResult) {
			int placeIdx = Integer.parseInt(mapEl.get("PLACE_IDX")+"");
			String name = (String)mapEl.get("NAME");
			String cityNameDB = (String)mapEl.get("CITY_NAME");
			System.out.println(placeIdx + " : " + name + "(" + cityNameDB + ")");
		}
	}
	
	// selectAiRecommendPlaceList
	@Test
	public void testSelectAiRecommendPlaceList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<AiRecommendPlaceDto> listAiPlace = tiDao.selectAiRecommendPlaceList(id);
		
		// Then
		assertNotNull("listAiPlace 리스트가 null이 아니어야 함", listAiPlace);
		for(AiRecommendPlaceDto dto : listAiPlace) {
			System.out.println(dto.getCategory() + dto.getName() + dto.getPlaceImg());
		}
	}
	
	// selectClickHistoryOneWeekByMemberId
	@Test
	public void testSelectClickHistoryOneWeekByMemberId() throws Exception {
		// Given
		String loginId = "YG1017";
		
		// When
		List<Map<String, Object>> listResult = tiDao.selectClickHistoryOneWeekByMemberId(loginId);
		
		// Then
		assertNotNull("listResult 리스트가 null이 아니어야 함", listResult);
		for(Map<String, Object> mapEl : listResult) {
			String placeIdx = (String)(mapEl.get("PLACE_IDX")+"");
			String name = (String)mapEl.get("NAME");
			String clickCnt = (String)(mapEl.get("CLICK_CNT")+"");
			System.out.println(placeIdx + " : " + name + " : " + clickCnt);
		}
	}
	*/
}
