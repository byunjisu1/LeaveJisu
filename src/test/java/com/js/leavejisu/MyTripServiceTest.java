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

import com.js.dto.AddPlaceDto;
import com.js.dto.CityListDto;
import com.js.dto.InsertPlaceMemoDto;
import com.js.dto.MemberDto;
import com.js.dto.MenuBarProfileDto;
import com.js.dto.MyBellListDto;
import com.js.dto.MyPlaceListDto;
import com.js.dto.MyReviewListDto;
import com.js.dto.PlaceDto;
import com.js.dto.PlaceReviewDto;
import com.js.dto.PlanDetailDto;
import com.js.dto.PlanDetailPlaceDto;
import com.js.dto.PlanDetailPlaceMemoDto;
import com.js.dto.PlanPlaceMapDto;
import com.js.dto.UpcomingPlanListDto;
import com.js.dto.UpdateMyPlaceDto;
import com.js.service.MyTripService;

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class MyTripServiceTest {
	@Autowired
	MyTripService mtSvc;
	
	// getPlanDetailPlace
	@Test
	public void testGetPlanDetailPlace() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanDetailPlaceDto> listPlanPlace = mtSvc.getPlanDetailPlace(planIdx);
		
		// Then
		assertNotNull("listPlanPlace 리스트가 null이 아니어야 함", listPlanPlace);
		for(PlanDetailPlaceDto dto : listPlanPlace) {
			System.out.println(dto.getDay() + "일차, " + dto.getPlaceOrder() + "번째 장소 : " + dto.getName());
		}
	}
	/*
	// getPlanDetailPlaceMemo
	@Test
	public void testGetPlanDetailPlaceMemo() throws Exception {
		// Given
		int planPlaceIdx = 2;
		
		// When
		List<PlanDetailPlaceMemoDto> listPlanPlaceMemo = mtSvc.getPlanDetailPlaceMemo(planPlaceIdx);
		
		// Then
		assertNotNull("listPlanPlace 리스트가 null이 아니어야 함", listPlanPlaceMemo);
		for(PlanDetailPlaceMemoDto dto : listPlanPlaceMemo) {
			System.out.println(dto.getContent());
		}
	}
	
	// getPlanDetail
	@Test
	public void testGetPlanDetail() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanDetailDto> listPlanDetail = mtSvc.getPlanDetail(planIdx);
		
		// Then
		assertNotNull("listPlanDetail 리스트가 null이 아니어야 함", listPlanDetail);
		for(PlanDetailDto dto : listPlanDetail) {
			System.out.println(dto.getCityName() + ", " + dto.getStartDate());
		}
	}
	
	// getPlanPlaceMap
	@Test
	public void testGetPlanPlaceMap() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanPlaceMapDto> listPlanPlaceMap = mtSvc.getPlanPlaceMap(planIdx);
		
		// Then
		assertNotNull("listPlanPlaceMap 리스트가 null이 아니어야 함", listPlanPlaceMap);
		for(PlanPlaceMapDto dto : listPlanPlaceMap) {
			System.out.println(dto.getDay() + ", " + dto.getName() + " : " + dto.getLatitude() + " , " + dto.getLongitude());
		}
	}
	
	// insertPlaceMemo
	@Test
	@Transactional
	public void testInsertPlaceMemo() throws Exception {
		// Given
		InsertPlaceMemoDto dto = new InsertPlaceMemoDto(0, 3, "테스트 추가임");
		
		// When
		int memoIdx = mtSvc.insertPlaceMemo(dto);
		
		// Then
		System.out.println(memoIdx);
	}
	
	// deletePlaceMemo
	@Test
	@Transactional
	public void testDeletePlaceMemo() throws Exception {
		// Given
		int memoIdx = 1;
		
		// When
		mtSvc.deletePlaceMemo(memoIdx);
		
		// Then
	}
	
	// getMemberDetail
	@Test
	public void testGetMemberDetail() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		MemberDto memberDto = mtSvc.getMemberDetail(id);
		
		// Then
		assertEquals("조회된 데이터의 id는 조회하려는 id와 같아야 함", id, memberDto.getId());
		System.out.println(memberDto.getNickname() + ", " + memberDto.getProfileImg());
	}
	
	// getMyPlaceList
	@Test
	public void testGetMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyPlaceListDto> listMyPlace = mtSvc.getMyPlace(id);
		
		// Then
		assertNotNull("listMyPlace 리스트가 null이 아니어야 함", listMyPlace);
		for(MyPlaceListDto dto : listMyPlace) {
			System.out.println(dto.getName() + ", " + dto.getPlaceImg());
		}
	}
	
	// insertMyPlaceMemo
	@Test
	@Transactional
	public void testInsertMyPlaceMemo() throws Exception {
		// Given
		UpdateMyPlaceDto dto = new UpdateMyPlaceDto("YG1017", 30, null, null);
		
		// When
		mtSvc.updateMyPlaceMemo(dto);
		
		// Then
	}
	
	// deleteMyPlace
	@Test
	@Transactional
	public void testDeleteMyPlace() throws Exception {
		// Given
		String memberId = "YG1017";
		int placeIdx = 6;
		List<MyPlaceListDto> listBefore = mtSvc.getMyPlace(memberId);
				
		// When
		mtSvc.deleteMyPlace(memberId, placeIdx);

		// Then
		List<MyPlaceListDto> listAfter = mtSvc.getMyPlace(memberId);
		assertEquals("삭제 후 요소가 딱 하나 감소해야 함.", listBefore.size()-1, listAfter.size());
	}
	
	// insertMyPlace test
	@Test
	@Transactional
	public void testInsertMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 6;
		List<MyPlaceListDto> listBefore = mtSvc.getMyPlace(id);
				
		// When
		mtSvc.insertMyPlace(id, placeIdx);

		// Then
		List<MyPlaceListDto> listAfter = mtSvc.getMyPlace(id);
		assertEquals("추가 후 요소가 딱 하나 증가해야 함.", listBefore.size()+1, listAfter.size());
	}
	
	// getUpcomingPlanList
	@Test
	public void testGetUpcomingPlanList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<UpcomingPlanListDto> listUpcomingPlan = mtSvc.getUpcomingPlanList(id);
		
		// Then
		assertNotNull("listUpcomingPlan 리스트가 null이 아니어야 함", listUpcomingPlan);
		for(UpcomingPlanListDto dto : listUpcomingPlan) {
			System.out.println(dto.getStartDate() + ", " + dto.getCityName());
		}
	}
	
	// updatePlanDate
	@Test
	@Transactional
	public void testUpdatePlanDate() throws Exception {
		// Given
		int planIdx = 1;
		String startDate = "2026/01/09";
		int days = 5;
		
		// When
		mtSvc.updatePlanDate(planIdx, startDate, days);
		
		// Then
	}
	
	// deletePlan
	@Test
	@Transactional
	public void testDeletePlan() throws Exception {
		// Given
		int planIdx = 2;
				
		// When
		mtSvc.deletePlan(planIdx);

		// Then
	}
	
	// getCityList
	@Test
	public void testGetCityList() throws Exception {
		// Given : x
		
		// When
		List<CityListDto> listCity = mtSvc.getCityList();
		
		// Then
		assertNotNull("listCity 리스트는 null이 아니어야 함", listCity);
		for(CityListDto dto : listCity) {
			System.out.println(dto.getCityName() + " : " + dto.getCountry());
		}
	}
	
	// getMyReviewList
	@Test
	public void testGetMyReviewList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyReviewListDto> listMyReview = mtSvc.getMyReview(id);
		
		// Then
		assertNotNull("listMyReview 리스트는 null이 아니어야 함", listMyReview);
		for(MyReviewListDto dto : listMyReview) {
			System.out.println(dto.getContent() + " : " + dto.getName());
		}
	}
	
	// getMyCityReviewList
	@Test
	public void testGetMyCityReviewList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyReviewListDto> listMyCityReview = mtSvc.getMyCityReview(id);
		
		// Then
		assertNotNull("listMyCityReview 리스트는 null이 아니어야 함", listMyCityReview);
		for(MyReviewListDto dto : listMyCityReview) {
			System.out.println(dto.getContent() + " : " + dto.getCityName());
		}
	}
	
	// insertReview
	@Test
	@Transactional
	public void testInsertReview() throws Exception {
		// Given
		int placeIdx = 1;
		String id = "YG1017";
		String content = "내용1";
		int star = 1;
		String tripDate = "202601";
		
		// When
		int reviewIdx = mtSvc.insertReview(placeIdx, id, content, star, tripDate);
		
		// Then
		System.out.println(reviewIdx);
	}
	
	
	// insertReviewPic
	@Test
	@Transactional
	public void testInsertReviewPic() throws Exception {
		// Given
		int reviewIdx = 1;
		String reviewImg = "ac.jpg";
		
		// When
		mtSvc.insertReviewPic(reviewIdx, reviewImg);
		
		// Then
	}
	
	// getStarComment
	@Test
	public void testGetStarComment() throws Exception {
		// Given
		int star = 1;
		
		// When
		String comment = mtSvc.getStarComment(star);
		
		// Then
		System.out.println(comment);
	}
	
	// getPlaceDetail
	@Test
	public void testGetPlaceDetail() throws Exception {
		// Given
		int placeIdx = 1;
		String id = "YG1017";
		
		// When
		PlaceDto dto = mtSvc.getPlaceDetail(id, placeIdx);
		
		// Then
		System.out.println(dto.getCityName() + ", " + dto.getName());
	}
	
	// getPlaceReview
	@Test
	public void testGetPlaceReview() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 6;
		
		// When
		List<PlaceReviewDto> listPlaceReview = mtSvc.getPlaceReviewList(id, placeIdx);
		
		// Then
		assertNotNull("listPlaceReview 리스트가 null이 아니어야 함", listPlaceReview);
		for(PlaceReviewDto dto : listPlaceReview) {
			System.out.println(dto.getNickname() + " : " + dto.getContent());
		}
	}
	
	// getPlaceReviewImg
	@Test
	public void testGetPlaceReviewImg() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		List<String> listPlaceReviewImg = mtSvc.getPlaceReviewImg(reviewIdx);
		
		// Then
		assertNotNull("listPlaceReviewImg 리스트가 null이 아니어야 함", listPlaceReviewImg);
		for(String s : listPlaceReviewImg) {
			System.out.println(s);
		}
	}
	
	// deletePlaceReviewGood test
	@Test
	@Transactional
	public void testDeletePlaceReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int reviewIdx = 1;
				
		// When
		mtSvc.deletePlaceReviewGood(id, reviewIdx);

		// Then
	}
	
	// insertPlaceReviewGood test
	@Test
	@Transactional
	public void testInsertPlaceReviewGood() throws Exception {
		// Given
		String id = "YG1017";
		int reviewIdx = 1;
				
		// When
		mtSvc.insertPlaceReviewGood(id, reviewIdx);

		// Then
	}
	
	// getPlaceReviewGoodN
	@Test
	public void testGetPlaceReviewGoodN() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		int reviewGoodN = mtSvc.getPlaceReviewGoodN(reviewIdx);
		
		// Then
		System.out.println(reviewGoodN);
	}
	
	// deleteMyReview
	@Test
	@Transactional
	public void testDeleteMyReview() throws Exception {
		// Given
		int reviewIdx = 2;
				
		// When
		mtSvc.deleteMyReview(reviewIdx);

		// Then
	}
	
	// getAddMyPlace
	@Test
	public void testGetAddMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<AddPlaceDto> listAddMyPlace = mtSvc.getAddMyPlace(id);
		
		// Then
		assertNotNull("listAddMyPlace 리스트가 null이 아니어야 함", listAddMyPlace);
		for(AddPlaceDto dto : listAddMyPlace) {
			System.out.println(dto.getName() + ", " + dto.getCityName());
		}
	}
	
	// getAddMyPlace
	@Test
	public void testGetAddRecommendPlace() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<AddPlaceDto> listAddRecommendPlace = mtSvc.getAddRecommendPlace(planIdx);
		
		// Then
		assertNotNull("listAddRecommendPlace 리스트가 null이 아니어야 함", listAddRecommendPlace);
		for(AddPlaceDto dto : listAddRecommendPlace) {
			System.out.println(dto.getName() + ", " + dto.getCityName());
		}
	}
	
	// insertPlan
	@Test
	@Transactional
	public void testInsertPlan() throws Exception {
		// Given
		String id = "YG1017";
		String startDate = "2026/01/12";
		int days = 8;
				
		// When
		mtSvc.insertPlan(id, startDate, days);

		// Then
	}
	
	// insertPlanCity
	@Test
	@Transactional
	public void testInsertPlanCity() throws Exception {
		// Given
		int planIdx = 1;
		int planOrder = 4;
		int cityIdx = 4;
				
		// When
		mtSvc.insertPlanCity(planIdx, planOrder, cityIdx);

		// Then
	}
	
	// insertPlanPlace
	@Test
	@Transactional
	public void testInsertPlanPlace() throws Exception {
		// Given
		int planIdx = 1;
		int day = 1;
		int placeIdx = 22;
		
		List<PlanDetailPlaceDto> listBefore = mtSvc.getPlanDetailPlace(planIdx);
				
		// When
		mtSvc.insertPlanPlace(planIdx, day, placeIdx);

		// Then
		List<PlanDetailPlaceDto> listAfter = mtSvc.getPlanDetailPlace(planIdx);
		assertEquals("추가 후 요소가 딱 하나 증가해야 함.", listBefore.size()+1, listAfter.size());
	}
	
	// getCityIdxByName
	@Test
	public void testGetCityIdxByName() throws Exception {
		// Given
		String name = "도쿄";
		
		//When
		int cityIdx = mtSvc.getCityIdxByName(name);
		
		// Then
		System.out.println("cityIdx : " + cityIdx);
	}
	
	// deletePlanPlace
	@Test
	@Transactional
	public void testDeletePlanPlace() throws Exception {
		// Given
		int planPlaceIdx = 9;
		
		// When
		mtSvc.deletePlanPlace(planPlaceIdx);

		// Then
	}
	
	// getMenuBarProfile
	@Test
	public void testGetMenuBarProfile() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MenuBarProfileDto> listProfile = mtSvc.getMenuBarProfile(id);
		
		// Then
		assertNotNull("listProfile 리스트가 null이 아니어야 함", listProfile);
		for(MenuBarProfileDto dto : listProfile) {
			System.out.println(dto.getCityName() + " 여행 : " + dto.getStartDate());
		}
	}
	
	// getMyBellList
	@Test
	public void testGetMyBellList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		// Then
		assertNotNull("listMyBell 리스트가 null이 아니어야 함", listMyBell);
		for(MyBellListDto dto : listMyBell) {
			System.out.println(dto.getClickId() + "님이 내 리뷰 " + dto.getReviewIdx() + "에 좋아요를 눌렀습니다.");
		}
	}
	
	// deleteAllMyBells
	@Test
	@Transactional
	public void testDeleteAllMyBells() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		mtSvc.deleteAllMyBells(id);

		// Then
	}
	
	// deleteMyBell
	@Test
	@Transactional
	public void testDeleteMyBell() throws Exception {
		// Given
		int bellIdx = 1;
		
		// When
		mtSvc.deleteMyBell(bellIdx);

		// Then
	}
	
	// insertBell
	@Test
	@Transactional
	public void testInsertBell() throws Exception {
		// Given
		Integer reviewIdx = null;
		Integer recommendReviewIdx = 1;
		String id = "jj5678";
		String memberId = "YG1017";
		
		// When
		int newBellIdx = mtSvc.insertMyBell(reviewIdx, recommendReviewIdx, id, memberId);
		
		// Then
		System.out.println("newBellIdx : " + newBellIdx);
	}
	
	// getWriterIdByReviewIdx
	@Test
	public void testGetWriterByReviewIdx() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		String writer = mtSvc.getWriterIdByReviewIdx(reviewIdx);
		
		// Then
		System.out.println(writer);
	}
	
	// insertAiPackImg
	@Test
	@Transactional
	public void testInsertAiPackImg() throws Exception {
		// Given
		String id = "YG1017";
		String filename = "테스트.jpg";
		
		// When
		mtSvc.insertAiPackImg(id, filename);
		
		// Then
	}
	
	// updateAiPack
	@Test
	@Transactional
	public void testUpdateAiPack() throws Exception {
		// Given
		String present = "현재 있는 거 ";
		String missing = "안 챙긴 거";
		String advice = "잘 챙겨";
		String id = "YG1017";
		
		// When
		mtSvc.updateAiPack(present, missing, advice, id);
		
		// Then
	}
	
	// getAiPackCityDays
	@Test
	public void testGetCityDays() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		Map<String, Object> map1 = mtSvc.getAiPackCityDays(id);
		
		// Then
		System.out.println(map1);
	}
	*/
}
