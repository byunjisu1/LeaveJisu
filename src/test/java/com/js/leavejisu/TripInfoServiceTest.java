package com.js.leavejisu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.js.dto.AiRecommendPlaceDto;
import com.js.dto.AirportListDto;
import com.js.dto.BlogCommentListDto;
import com.js.dto.BlogDetailDto;
import com.js.dto.BlogDetailPlaceDto;
import com.js.dto.BlogListDto;
import com.js.dto.BlogPlaceMapDto;
import com.js.dto.CityListDto;
import com.js.dto.MyAiPackDto;
import com.js.dto.PlaceDto;
import com.js.dto.PlanCourseDto;
import com.js.dto.RecommendPlanDetailDto;
import com.js.dto.RecommendPlanPlaceDto;
import com.js.dto.ReviewListDto;
import com.js.service.AIService;
import com.js.service.TripInfoService;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class TripInfoServiceTest {
	@Autowired
	TripInfoService tiSvc;
	@Autowired
	AIService aiSvc;
	
	// getCityList
	@Test
	public void testGetCityListRecommend() throws Exception {
		// 1. Given : 없음.
		
		// 2. When
		List<CityListDto> listCity = tiSvc.getCityListRecommend();
		
		// 3. Then
		assertNotNull("listCity 리스트가 null이 아니어야 함", listCity);
		for(CityListDto dto : listCity) {
			System.out.println(dto.getCityIdx() + " / " + dto.getCityName() + " / " + dto.getCityImg());
		}
	}
	/*
	// getPlaceTop10
	@Test
	public void testGetPlaceTop10() throws Exception {
		// Given
		int cityIdx = 16;  // 파리
		
		// When
		List<PlaceDto> listPlace = tiSvc.getPlaceTop10(cityIdx);
		
		// Then
		assertNotNull("listPlace 리스트가 null이 아니어야 함", listPlace);
		for(PlaceDto dto : listPlace) {
			System.out.println(dto.getCityIdx() + " / " + dto.getPlaceIdx() + " / " + dto.getName());
		}
	}

	// (getRecommendPlanDetailList = getConceptList + getRecommendPlaceList)
	@Test
	public void getRecommendPlanDetailList() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		List<RecommendPlanDetailDto> listRecommendPlan = tiSvc.getRecommendPlanDetailList(cityIdx);

		// Then
		assertNotNull("lisRecommendPlan 리스트가 null이 아니어야 함", listRecommendPlan);
		for(RecommendPlanDetailDto dto : listRecommendPlan) {
			System.out.println(dto.getDay() + "일차 : \nconcept : " + dto.getConcept() + "\ncontent : " + dto.getContent());
			for(RecommendPlanPlaceDto place : dto.getListPlace()) {
				System.out.println(place.getPlaceOrder() + " / " + place.getPlaceName() + " / " + place.getIntro());
			}
		}
	}
		
	// getBlogList
	@Test
	public void testGetBlogList() throws Exception {
		// Given : x
		
		// When
		List<BlogListDto> listBlog = tiSvc.getBlogList();
		
		// Then
		assertNotNull("listBlog 리스트가 null이 아니어야 함", listBlog);
		for(BlogListDto dto : listBlog) {
			System.out.println(dto.getNickname() + " / " + dto.getTitle() + " / " + dto.getIntro());
			for(String imgList : dto.getBlogImgUrlList()) {
				System.out.println(imgList);
			}
		}
	}
	
	// getCityNameByIdx
	@Test
	public void testGetCityNameByIdx() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		String cityName = tiSvc.getCityNameByIdx(cityIdx);
		
		// Then
		System.out.println("조회된 도시명 : " + cityName);
	}
	
	// getRecommendReviewList
	@Test
	public void testGetRecommendReviewList() throws Exception {
		// Given
		int cityIdx = 16;
		String id = "jj5678";
		
		// When
		List<ReviewListDto> listReview = tiSvc.getRecommendReviewList(cityIdx, id, null);
		
		// Then
		assertNotNull("listReview 리스트가 null이 아니어야 함", listReview);
		for(ReviewListDto dto : listReview) {
			System.out.println(dto.getNickname() + " : " + dto.getContent() + " / 작성 날짜 : " + dto.getReviewDate() + " / 리뷰 추천 수 : " + dto.getReviewGoodN());
		}
	}
	
	// getRecommendReviewImgList
	@Test
	public void testGetRecommendReviewImgList() throws Exception {
		// Given
		int recommendReviewIdx = 32;
		
		// When
		List<String> listReviewImg = tiSvc.getRecommendReviewImgList(recommendReviewIdx);
		
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
		int recommendReviewIdx = tiSvc.insertRecommendReview(cityIdx, id, content, tripDate);
		
		// Then
		System.out.println(recommendReviewIdx);
	}
	
	// insertRecommendReviewPic
	@Test
	@Transactional
	public void testInsertRecommendReviewPic() throws Exception {
		// Given
		int recommendReviewIdx = 1;
		String reviewImg = "acfe.jpg";
		
		// When
		tiSvc.insertRecommendReviewPic(recommendReviewIdx, reviewImg);
		
		// Then
	}
	
	// getBlogDetail
	@Test
	public void testGetBlogDetail() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		BlogDetailDto listBlog = tiSvc.getBlogDetail(blogIdx);
		
		// Then
		assertNotNull("listBlog 리스트가 null이 아니어야 함", listBlog);
		System.out.println(listBlog.getTitle() + " / " + listBlog.getIntro());
		for(BlogDetailPlaceDto placeDto : listBlog.getListBlogPlace()) {
			System.out.println(placeDto.getDay() + "일차 " + placeDto.getPlaceName() + " / " + placeDto.getContent());
		}
	}
	
	// getPlanCourse
	@Test
	public void testGetPlanCourse() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<PlanCourseDto> listCourse = tiSvc.getPlanCourse(blogIdx);
		
		// Then
		assertNotNull("listCourse 리스트가 null이 아니어야 함", listCourse);
		for(PlanCourseDto dto : listCourse) {
			System.out.println(dto.getDay() + "일차 " + dto.getPlaceOrder() + "번 : " + dto.getName());
		}
	}
	
	// getBlogCommentList
	@Test
	public void testGetBlogCommentList() throws Exception {
		// Given
		String id = "YG1017";
		int blogIdx = 4;
		
		// When
		List<BlogCommentListDto> listBlogComment = tiSvc.getBlogCommentList(id, blogIdx);
		
		// Then
		assertNotNull("listBlogComment 리스트가 null이 아니어야 함", listBlogComment);
		for(BlogCommentListDto dto : listBlogComment) {
			System.out.println(dto.getNickname() + " : " + dto.getContent() + " 좋아요 여부 : " + dto.getCommentGood() + " / 좋아요 수 : " + dto.getCommentGoodN());
		}
	}
	
	// getBlogCommentByIdx
	@Test
	public void testGetBlogCommentByIdx() throws Exception {
		// Given
		String id = "YG1017";
		int blogCommentIdx = 4;
		
		// When
		BlogCommentListDto listCommentByIdx = tiSvc.getBlogCommentByIdx(id, blogCommentIdx);
		
		// Then
		System.out.println(listCommentByIdx.getNickname() + " : " + listCommentByIdx.getContent());
	}
	
	// getBlogPlaceMapList
	@Test
	public void testGetBlogPlaceMapList() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<BlogPlaceMapDto> listPlaceMap = tiSvc.getBlogPlaceMapList(blogIdx);
		
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
		tiSvc.deleteBlogCommentGood(id, blogCommentIdx);
	}
	
	// insertBlogCommentGood
	@Test
	@Transactional
	public void testInsertBlogCommentGood() throws Exception {
		// Given
		int blogCommentIdx = 1;
		String id = "YG1017";
		
		// When
		tiSvc.insertBlogCommentGood(id, blogCommentIdx);
	}
	
	// getAirportList
	@Test
	public void testGetAirportList() throws Exception {
		// Given : x
		
		// When
		List<AirportListDto> listAirport = tiSvc.getAirportList();
		
		// Then
		assertNotNull("listAirport 리스트가 null이 아니어야 함", listAirport);
		for(AirportListDto dto : listAirport) {
			System.out.println(dto.getAirportId() + " / " + dto.getAirportName());
		}
	}
	
	// getBlogCommentN
	@Test
	public void testGetBlogCommentN() throws Exception {
		// Given
		int blogIdx = 4;
		
		// When
		List<Map<String, Object>> listCommentN = tiSvc.getCommentN(blogIdx);
		
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
		tiSvc.insertBlogComment(blogIdx, id, content);
	}
	
	// deleteBlogComment
	@Test
	@Transactional
	public void testDeleteBlogComment() throws Exception {
		// Given
		int blogCommentIdx = 2;
		
		// When
		tiSvc.deleteBlogComment(blogCommentIdx);
	}
	
	// updateBlogComment
	@Test
	@Transactional
	public void testUpdateBlogComment() throws Exception {
		// Given
		String content = "수정@";
		int blogCommentIdx = 2;
		
		// When
		tiSvc.updateBlogComment(content, blogCommentIdx);
	}
	
	// deleteRecommendPlanReviewGood
	@Test
	@Transactional
	public void testDeleteRecommendPlanReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int recommendReviewIdx = 2;
		
		// When
		tiSvc.deleteRecommendPlanReviewGood(id, recommendReviewIdx);
	}
	
	// insertRecommendPlanReviewGood
	@Test
	@Transactional
	public void testInsertRecommendPlanReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int recommendReviewIdx = 1;
		
		// When
		tiSvc.insertRecommendPlanReviewGood(id, recommendReviewIdx);
	}
	
	// getRecommendReviewGoodN
	@Test
	public void testGetRecommendReviewGoodN() throws Exception {
		// Given
		int recommendReviewIdx = 2;
		
		// When
		int reviewGoodN = tiSvc.getRecommendReviewGoodN(recommendReviewIdx);
		
		// Then
		System.out.println(reviewGoodN);
	}
	
	// getMyAiPackList
	@Test
	public void testGetMyAiPackList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		MyAiPackDto MyAiPack = tiSvc.getMyAiPack(id);
		
		// Then
		assertNotNull("MyAiPack이 null이 아니어야 함", MyAiPack);
		System.out.println(MyAiPack.getPresentList() + "/" + MyAiPack.getMissingList() + ", 조언 : " + MyAiPack.getAdvice());
	}
	
	// getWriterIdByRecommendReviewIdx
	@Test
	public void testSelectWriterIdByReviewIdx() throws Exception {
		// Given
		int recommendReviewIdx = 1;
		
		// When
		String writerId = tiSvc.getWriterIdByIdx(recommendReviewIdx);
		
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
		tiSvc.deleteMyCityReview(recommendReviewIdx);
		
		// Then
	}
	
	// getCityImgByIdx
	@Test
	public void testGetCityImgByIdx() throws Exception {
		// Given
		int cityIdx = 16;
		
		// When
		String img = tiSvc.getCityImgByIdx(cityIdx);
		
		// Then
		System.out.println(img);
	}
	
	// getPlaceListByCityName
	@Test
	public void testGetPlaceLisByCityName() throws Exception {
		// Given
		String cityName = "파리";
		
		// When
		List<Map<String, Object>> listResult = tiSvc.getPlaceListByCityName(cityName);
		
		// Then
		assertNotNull("listResult 리스트가 null이 아니어야 함", listResult);
		for(Map<String, Object> mapEl : listResult) {
			int placeIdx = Integer.parseInt(mapEl.get("PLACE_IDX")+"");
			String name = (String)mapEl.get("NAME");
			System.out.println(placeIdx + " : " + name);
		}
	}
	
	// getAiRecommendPlaceList
	@Test
	public void testGetAiRecommendPlaceList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<AiRecommendPlaceDto> listAiPlace = tiSvc.getAiRecommendPlaceList(id);
		
		// Then
		assertNotNull("listAiPlace 리스트가 null이 아니어야 함", listAiPlace);
		for(AiRecommendPlaceDto dto : listAiPlace) {
			System.out.println(dto.getCategory() + dto.getName() + dto.getPlaceImg());
		}
	}
	
	@Test
	@Transactional
	public void testAIRecommendLeft() throws Exception {   // 옵션 선택
		// Given
		String loginId = "YG1017";
		String cityName = "파리";
		int days = 3;
		String with = "혼자/친구와";
		String style= "체험 액티비티/자연과 함께";
		String travelTempo = "빼곡한 일정";
		
		// When
		aiSvc.recommendApiByOption(loginId, cityName, days, with, style, travelTempo);
		
		// Then
	}

	@Test
	public void testAIRecommendRight() throws Exception {  // 클릭 이력
		// Given
		String loginId = "YG1017";
		
		// When
		aiSvc.recommendApiByClick(loginId);
		
		// Then
	}
	*/
}
