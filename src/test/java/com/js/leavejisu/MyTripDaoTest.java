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

import com.js.dao.MyTripDao;
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

@RunWith(SpringJUnit4ClassRunner.class)	// "이 테스트는 스프링과 함께 실행하겠다"라고 스프링에게 알림. 만약에 이게 없다면 @Autowired 는 작동하지 않음.
@ContextConfiguration(locations= {"file:src/main/webapp/WEB-INF/spring/root-context.xml"})	// 테스트 필요한 설정 파일은 root-context.xml 이다
public class MyTripDaoTest {
	@Autowired
	MyTripDao mtDao;
	
	// selectPlanDetailPlace
	@Test
	public void testSelectPlanDetailPlace() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanDetailPlaceDto> listPlanPlace = mtDao.selectPlanDetailPlace(planIdx);
		
		// Then
		assertNotNull("listPlanPlace 리스트가 null이 아니어야 함", listPlanPlace);
		for(PlanDetailPlaceDto dto : listPlanPlace) {
			System.out.println(dto.getDay() + "일차, " + dto.getPlaceOrder() + "번째 장소 : " + dto.getName());
		}
	}
	/*
	// selectPlanDetailPlaceMemo
	@Test
	public void testSelectPlanDetailPlaceMemo() throws Exception {
		// Given
		int planPlaceIdx = 2;
		
		// When
		List<PlanDetailPlaceMemoDto> listPlanPlaceMemo = mtDao.selectPlanDetailPlaceMemo(planPlaceIdx);
		
		// Then
		assertNotNull("listPlanPlace 리스트가 null이 아니어야 함", listPlanPlaceMemo);
		for(PlanDetailPlaceMemoDto dto : listPlanPlaceMemo) {
			System.out.println(dto.getContent());
		}
	}
	
	// selectPlanDetail
	@Test
	public void testSelectPlanDetail() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanDetailDto> listPlanDetail = mtDao.selectPlanDetail(planIdx);
		
		// Then
		assertNotNull("listPlanDetail 리스트가 null이 아니어야 함", listPlanDetail);
		for(PlanDetailDto dto : listPlanDetail) {
			System.out.println(dto.getCityName() + ", " + dto.getStartDate());
		}
	}
	
	// selectPlanPlaceMap
	@Test
	public void testSelectPlanPlaceMap() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<PlanPlaceMapDto> listPlanPlaceMap = mtDao.selectPlanPlaceMap(planIdx);
		
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
		InsertPlaceMemoDto dto = new InsertPlaceMemoDto(0, 2, "테스트 추가임");
		
		// When
		int memoIdx = mtDao.insertPlaceMemo(dto);
		
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
		mtDao.deletePlaceMemo(memoIdx);
		
		// Then
	}
	
	// selectMemberDetail
	@Test
	public void testSelectMemberDetail() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		MemberDto listMemberDetail = mtDao.selectMemberDetail(id);
		
		// Then
		System.out.println(listMemberDetail.getNickname() + ", " + listMemberDetail.getProfileImg());
	}
	
	// selectMyPlaceList
	@Test
	public void testSelectMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyPlaceListDto> listMyPlace = mtDao.selectMyPlaceList(id);
		
		// Then
		assertNotNull("listMyPlace 리스트가 null이 아니어야 함", listMyPlace);
		for(MyPlaceListDto dto : listMyPlace) {
			System.out.println(dto.getName() + ", " + dto.getPlaceImg());
		}
	}
	
	// updateMyPlaceMemo
	@Test
	public void testUpdateMemo() throws Exception {
		// Given
		UpdateMyPlaceDto dto = new UpdateMyPlaceDto("YG1017", 25, null, null);
		
		// When
		mtDao.updateMyPlaceMemo(dto);
		
		// Then
	}
	
	// deleteMyPlace test
	@Test
	@Transactional
	public void testDeleteMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 6;
		List<MyPlaceListDto> listBefore = mtDao.selectMyPlaceList(id);
				
		// When
		mtDao.deleteMyPlace(id, placeIdx);

		// Then
		List<MyPlaceListDto> listAfter = mtDao.selectMyPlaceList(id);
		assertEquals("삭제 후 요소가 딱 하나 감소해야 함.", listBefore.size()-1, listAfter.size());
	}
	
	// insertMyPlace test
	@Test
	@Transactional
	public void testInsertMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 6;
		List<MyPlaceListDto> listBefore = mtDao.selectMyPlaceList(id);
				
		// When
		mtDao.insertMyPlace(id, placeIdx);

		// Then
		List<MyPlaceListDto> listAfter = mtDao.selectMyPlaceList(id);
		assertEquals("추가 후 요소가 딱 하나 증가해야 함.", listBefore.size()+1, listAfter.size());
	}
	
	// upcomingPlanList
	@Test
	public void testSelectUpcomingPlanList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<UpcomingPlanListDto> listUpcomingPlan = mtDao.selectUpcomingPlanList(id);
		
		// Then
		assertNotNull("listUpcomingPlan 리스트가 null이 아니어야 함", listUpcomingPlan);
		for(UpcomingPlanListDto dto : listUpcomingPlan) {
			System.out.println(dto.getStartDate() + ", " + dto.getCityName());
		}
	}
	
	// updatePlanDate
	@Test
	public void testUpdatePlanDate() throws Exception {
		// Given
		int planIdx = 1;
		String startDate = "2026/01/09";
		int days = 5;
		
		// When
		mtDao.updatePlanDate(startDate, days, planIdx);
		
		// Then
	}
	
	// deletePlan
	@Test
	@Transactional
	public void testDeletePlan() throws Exception {
		// Given
		int planIdx = 2;
				
		// When
		mtDao.deletePlan(planIdx);

		// Then
	}
	
	// selectCityList
	@Test
	public void testSelectCityList() throws Exception {
		// Given : x
		
		// When
		List<CityListDto> listCity = mtDao.selectCityList();
		
		// Then
		assertNotNull("listCity 리스트는 null이 아니어야 함", listCity);
		for(CityListDto dto : listCity) {
			System.out.println(dto.getCityName() + " : " + dto.getCountry());
		}
	}
	
	// selectMyReviewList
	@Test
	public void testSelectMyReviewList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyReviewListDto> listMyReview = mtDao.selectMyReview(id);
		
		// Then
		assertNotNull("listMyReview 리스트는 null이 아니어야 함", listMyReview);
		for(MyReviewListDto dto : listMyReview) {
			System.out.println(dto.getContent() + " : " + dto.getName());
		}
	}
	
	// selectMyCityReviewList
	@Test
	public void testSelectMyCityReviewList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyReviewListDto> listMyCityReview = mtDao.selectMyCityReview(id);
		
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
		int reviewIdx = mtDao.insertReview(placeIdx, id, content, star, tripDate);
		
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
		mtDao.insertReviewPic(reviewIdx, reviewImg);
		
		// Then
	}
	
	// selectStarComment
	@Test
	public void testSelectStarComment() throws Exception {
		// Given
		int star = 1;
		
		// When
		String comment = mtDao.selectStarComment(star);
		
		// Then
		System.out.println(comment);
	}
	
	// selectPlaceDetail
	@Test
	public void testSelectPlaceDetail() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 1;
		
		// When
		PlaceDto dto = mtDao.selectPlaceDetail(id, placeIdx);
		
		// Then
		System.out.println(dto.getCityName() + ", " + dto.getName() + " : " + dto.getMyHeart());
	}
	
	// selectPlaceReview
	@Test
	public void testSelectPlaceReview() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 6;
		
		// When
		List<PlaceReviewDto> listPlaceReview = mtDao.selectPlaceReview(id, placeIdx);
		
		// Then
		assertNotNull("listPlaceReview 리스트가 null이 아니어야 함", listPlaceReview);
		for(PlaceReviewDto dto : listPlaceReview) {
			System.out.println(dto.getNickname() + " : " + dto.getContent());
		}
	}
	
	// selectPlaceReviewImg
	@Test
	public void testSelectPlaceReviewImg() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		List<String> listPlaceReviewImg = mtDao.selectPlaceReviewImg(reviewIdx);
		
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
		mtDao.deletePlaceReviewGood(id, reviewIdx);

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
		mtDao.insertPlaceReviewGood(id, reviewIdx);

		// Then
	}
	
	// selectPlaceReviewGoodN
	@Test
	public void testSelectPlaceReviewGoodN() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		int reviewGoodN = mtDao.selectPlaceReviewGoodN(reviewIdx);
		
		// Then
		System.out.println(reviewGoodN);
	}
	
	// insertClick
	@Test
	@Transactional
	public void testInsertClick() throws Exception {
		// Given
		String id = "YG1017";
		int placeIdx = 5;
		
		// When
		mtDao.insertClick(id, placeIdx);
		
		// Then
	}
	
	// deleteMyReview
	@Test
	@Transactional
	public void testDeleteMyReview() throws Exception {
		// Given
		int reviewIdx = 2;
				
		// When
		mtDao.deleteMyReview(reviewIdx);

		// Then
	}
	
	// selectAddMyPlace
	@Test
	public void testSelectAddMyPlace() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<AddPlaceDto> listAddMyPlace = mtDao.selectAddMyPlace(id);
		
		// Then
		assertNotNull("listAddMyPlace 리스트가 null이 아니어야 함", listAddMyPlace);
		for(AddPlaceDto dto : listAddMyPlace) {
			System.out.println(dto.getName() + ", " + dto.getCityName());
		}
	}
	
	// selectAddMyPlace
	@Test
	public void testSelectAddRecommendPlace() throws Exception {
		// Given
		int planIdx = 1;
		
		// When
		List<AddPlaceDto> listAddRecommendPlace = mtDao.selectAddRecommendPlace(planIdx);
		
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
		mtDao.insertPlan(id, startDate, days);

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
		mtDao.insertPlanCity(planIdx, planOrder, cityIdx);

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
		
		List<PlanDetailPlaceDto> listBefore = mtDao.selectPlanDetailPlace(planIdx);
				
		// When
		mtDao.insertPlanPlace(planIdx, day, placeIdx);

		// Then
		List<PlanDetailPlaceDto> listAfter = mtDao.selectPlanDetailPlace(planIdx);
		assertEquals("추가 후 요소가 딱 하나 증가해야 함.", listBefore.size()+1, listAfter.size());
	}
	
	// selectCityIdxByName
	@Test
	public void testSelectCityIdxByName() throws Exception {
		// Given
		String name = "도쿄";
		
		//When
		int cityIdx = mtDao.selectCityIdxByName(name);
		
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
		mtDao.deletePlanPlace(planPlaceIdx);

		// Then
	}
	
	// selectPlaceOrder
	@Test
	public void testSelectPlaceOrder() throws Exception {
		// Given
		int planPlaceIdx = 2;
		
		// When
		int placeOrder = mtDao.selectPlaceOrder(planPlaceIdx);
		
		// Then
		System.out.println(placeOrder);
	}
	
	// updatePlaceOrder
	@Test
	@Transactional
	public void testUpdatePlaceOrder() throws Exception {
		// Given
		int deleteOrder = 1;
		
		// When
		mtDao.updatePlaceOrder(deleteOrder);
		
		// Then
	}
	
	// selectMenuBarProfile
	@Test
	public void testSelectMenuBarProfile() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MenuBarProfileDto> listProfile = mtDao.selectMenuBarProfile(id);
		
		// Then
		assertNotNull("listProfile 리스트가 null이 아니어야 함", listProfile);
		for(MenuBarProfileDto dto : listProfile) {
			System.out.println(dto.getCityName() + " 여행 : " + dto.getStartDate());
		}
	}
	
	// selectMyBellList
	@Test
	public void testSelectMyBellList() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		List<MyBellListDto> listMyBell = mtDao.selectMyBellList(id);
		
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
		mtDao.deleteAllMyBells(id);

		// Then
	}
	
	// deleteMyBell
	@Test
	@Transactional
	public void testDeleteMyBell() throws Exception {
		// Given
		int bellIdx = 1;
		
		// When
		mtDao.deleteMyBell(bellIdx);

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
		int newBellIdx = mtDao.insertBell(reviewIdx, recommendReviewIdx, id, memberId);
		
		// Then
		System.out.println("newBellIdx : " + newBellIdx);
	}
	
	// selectWriterIdByReviewIdx
	@Test
	public void testSelectWriterIdByReviewIdx() throws Exception {
		// Given
		int reviewIdx = 1;
		
		// When
		String writerId = mtDao.selectWriterIdByReviewIdx(reviewIdx);
		
		// Then
		assertEquals("아이디가 같아야 함", writerId, "ss1234");
		System.out.println("writerId : " + writerId);
	}
	
	// insertAiPackImg
	@Test
	@Transactional
	public void testInsertAiPackImg() throws Exception {
		// Given
		String id = "YG1017";
		String filename = "테스트.jpg";
		
		// When
		mtDao.insertAiPackImg(id, filename);
		
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
		mtDao.updateAiPack(present, missing, advice, id);
		
		// Then
	}
	
	// selectAiPackCityDays
	@Test
	public void testSelectCityDays() throws Exception {
		// Given
		String id = "YG1017";
		
		// When
		Map<String, Object> map1 = mtDao.selectAiPackCityDays(id);
		
		// Then
		System.out.println(map1);
	}
	*/
}