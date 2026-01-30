package com.js.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.js.dto.AddPlaceDto;
import com.js.dto.AddPlanDto;
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

@Repository
public class MyTripDaoImpl implements MyTripDao {
	@Autowired
	SqlSession sqlSession;

	/**
	 * selectMemberDetail(String id) : 회원 정보 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원 정보 dto(아이디, 닉네임, 프로필 사진)
	 */
	@Override
	public MemberDto selectMemberDetail(String id) {
		return sqlSession.selectOne("MyTripMapper.getMemberDetail", id);
	}
	
	/**
	 * selectPlanDetail(int planIdx) : <내 여행> 상세 정보
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 여행의 상세 정보(도시명, 출발날짜, 여행일수)
	 */
	@Override
	public List<PlanDetailDto> selectPlanDetail(int planIdx) {
		return sqlSession.selectList("MyTripMapper.getPlanDetail", planIdx);
	}
	
	/**
	 * selectUpcomingPlanList(String id) : 다가오는 여행 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 다가오는 여행 리스트(현재 날짜를 기준으로 최근순)
	 */
	@Override
	public List<UpcomingPlanListDto> selectUpcomingPlanList(String id) {
		return sqlSession.selectList("MyTripMapper.upcomingPlan", id);
	}
	
	/**
	 * updatePlanDate(String startDate, int days, int planIdx) : <내 여행> 여행 날짜 수정
	 * 파라미터 startDate : 수정된 여행 시작 날짜
	 * 파라미터 days : 수정된 여행 일수
	 * 파라미터 planIdx : 수정할 여행 idx
	 */
	@Override
	public void updatePlanDate(String startDate, int days, int planIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("startDate", startDate);
		map1.put("days", days);
		map1.put("planIdx", planIdx);
		sqlSession.update("MyTripMapper.updatePlanDate", map1);
	}

	/**
	 * deletePlan(int planIdx) : <내 여행> 삭제
	 * 파라미터 planIdx : 삭제할 여행의 idx
	 */
	@Override
	public void deletePlan(int planIdx) {
		sqlSession.delete("MyTripMapper.deletePlan", planIdx);
	}
	
	/**
	 * selectCityList() : <내 여행> 일정 추가_도시 리스트 조회
	 * 리턴값 : 도시 리스트(나라명, 도시명, 가까운 도시, 도시 사진)
	 */
	@Override
	public List<CityListDto> selectCityList() {
		return sqlSession.selectList("MyTripMapper.getCityList");
	}
	
	
	
	/**
	 * selectPlanDetailPlace(int planIdx) : <내 여행 상세> 장소 리스트
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 idx의 여행 장소 리스트
	 */
	@Override
	public List<PlanDetailPlaceDto> selectPlanDetailPlace(int planIdx) {
		return sqlSession.selectList("MyTripMapper.getPlanDetailPlace", planIdx);
	}
	
	/**
	 * deletePlanPlace(int planPlaceIdx) : <내 여행 상세> 여행 장소 삭제
	 * 파라미터 planPlaceIdx : 삭제할 여행 장소 idx
	 */
	@Override
	public void deletePlanPlace(int planPlaceIdx) {
		sqlSession.delete("MyTripMapper.deletePlanPlace", planPlaceIdx);
	}
	
	/**
	 * selectPlaceOrder(int planPlaceIdx) : <내 여행 상세> 여행 장소 순서 조회
	 * 파라미터 planPlaceIdx : 조회할 여행 장소 idx
	 * 리턴값 : DB에서 조회된 여행 장소의 순서
	 */
	@Override
	public int selectPlaceOrder(int planPlaceIdx) {
		return sqlSession.selectOne("MyTripMapper.getPlaceOrder", planPlaceIdx);
	}

	/**
	 * updatePlaceOrder(int deleteOrder) : <내 여행 상세> 여행 장소 삭제 시, 새로운 순서 부여
	 * 파라미터 deleteOrder : 삭제한 여행 장소의 순서
	 */
	@Override
	public void updatePlaceOrder(int deleteOrder) {
		sqlSession.insert("MyTripMapper.updatePlaceOrder", deleteOrder);
	}

	/**
	 * selectPlanDetailPlaceMemo(int planPlaceIdx) : <내 여행 상세>의 장소 메모 리스트
	 * 파라미터 planPlaceIdx : 조회하려는 여행의 장소 idx
	 * 리턴값 : 해당 장소 idx의 메모 리스트
	 */
	@Override
	public List<PlanDetailPlaceMemoDto> selectPlanDetailPlaceMemo(int planPlaceIdx) {
		return sqlSession.selectList("MyTripMapper.getPlanDetailPlaceMemo", planPlaceIdx);
	}

	/**
	 * insertPlaceMemo(InsertPlaceMemoDto dto) : <내 여행 상세>의 장소 메모 추가
	 * 파라미터 InsertPlaceMemoDto : 추가할 메모(메모 idx, 장소 idx, 내용)
	 */
	@Override
	public int insertPlaceMemo(InsertPlaceMemoDto dto) {
		sqlSession.insert("MyTripMapper.insertPlaceMemo", dto);
		return dto.getMemoIdx();
	}
	
	/**
	 * deletePlaceMemo(int memoIdx) : <내 여행 상세>의 장소 메모 삭제
	 * 파라미터 memoIdx : 삭제할 메모의 idx
	 */
	@Override
	public void deletePlaceMemo(int memoIdx) {
		sqlSession.delete("MyTripMapper.deletePlaceMemo", memoIdx);
	}
	
	/**
	 * selectPlanPlaceMap(int planIdx) : <내 여행 상세>의 장소 위치 리스트
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 여행의 장소 위치 리스트(일자, 장소명, 위도, 경도)
	 */
	@Override
	public List<PlanPlaceMapDto> selectPlanPlaceMap(int planIdx) {
		return sqlSession.selectList("MyTripMapper.getPlanPlaceMap", planIdx);
	}
	
	/**
	 * selectAddMyPlace(String id) : <내 여행 상세> 장소 추가_내 저장 장소 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원의 저장된 장소 정보 dto 리스트
	 */
	@Override
	public List<AddPlaceDto> selectAddMyPlace(String id) {
		return sqlSession.selectList("MyTripMapper.addMyPlace", id);
	}

	/**
	 * selectAddRecommendPlace(int planIdx) : <내 여행 상세> 장소 추가_추천 장소 리스트
	 * 파라미터 planIdx : 해당 여행 idx
	 * 리턴값 : 등록한 여행 도시의 장소 정보 dto 리스트
	 */
	@Override
	public List<AddPlaceDto> selectAddRecommendPlace(int planIdx) {
		return sqlSession.selectList("MyTripMapper.addRecommendPlace", planIdx);
	}
	
	

	/**
	 * selectMyPlaceList(String id) : <내 저장> 장소 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원의 저장된 장소 리스트 (장소 정보, 별점, 해당 장소 리뷰 개수)
	 */
	@Override
	public List<MyPlaceListDto> selectMyPlaceList(String id) {
		return sqlSession.selectList("MyTripMapper.getMyPlaceList", id);
	}

	/**
	 * updateMyPlaceMemo(UpdateMyPlaceDto dto) : <내 저장> 장소 메모 추가
	 * 파라미터 UpdateMyPlaceDto : 해당 장소의 메모 정보(회원 아이디, 찜 날짜, 메모 내용)
	 */
	@Override
	public void updateMyPlaceMemo(UpdateMyPlaceDto dto) {
		sqlSession.update("MyTripMapper.updateMyPlaceMemo", dto);
	}

	/**
	 * deleteMyPlace(String id, int placeIdx) : <내 저장> 장소 삭제
	 * 파라미터 id : 삭제할 회원의 아이디
	 * 파라미터 placeIdx : 삭제할 장소의 idx
	 */
	@Override
	public void deleteMyPlace(String id, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("placeIdx", placeIdx);
		sqlSession.delete("MyTripMapper.deleteMyPlace", map1);
	}
	
	/**
	 * insertMyPlace(String id, int placeIdx) : <내 저장> 장소 추가
	 * 파라미터 id : 추가할 회원의 아이디
	 * 파라미터 placeIdx : 추가할 장소의 idx
	 */
	@Override
	public void insertMyPlace(String id, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("placeIdx", placeIdx);
		sqlSession.delete("MyTripMapper.insertMyPlace", map1);
	}
	
	

	/**
	 * selectMyReview(String id) : <내 리뷰> 장소 리뷰 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 내 장소 리뷰 리스트(도시명, 장소 정보, 리뷰 정보, 리뷰 좋아요 여부, 좋아요 수, 리뷰 사진 리스트)
	 */
	@Override
	public List<MyReviewListDto> selectMyReview(String id) {
		return sqlSession.selectList("MyTripMapper.getMyReview", id);
	}
	
	/**
	 * selectPlaceReviewImg(int reviewIdx) : <내 리뷰> 장소 리뷰 사진 조회
	 * 파라미터 reviewIdx : 조회할 장소 리뷰 idx
	 * 리턴값 : 조회된 해당 리뷰의 사진 리스트
	 */
	@Override
	public List<String> selectPlaceReviewImg(int reviewIdx) {
		return sqlSession.selectList("MyTripMapper.getPlaceReviewImg", reviewIdx);
	}
	
	/**
	 * selectMyCityReview(String id) : <내 리뷰> 도시 리뷰 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 내 도시 리뷰 리스트(도시명, 장소 정보, 리뷰 정보, 리뷰 좋아요 여부, 좋아요 수, 리뷰 사진 리스트)
	 */
	@Override
	public List<MyReviewListDto> selectMyCityReview(String id) {
		return sqlSession.selectList("MyTripMapper.getMyCityReview", id);
	}

	/**
	 * insertReview(int placeIdx, String id, String content, int star, String tripDate) : 리뷰 추가
	 * 파라미터 id : 리뷰를 추가할 회원의 아이디
	 * 파라미터 placeIdx : 추가할 장소의 idx
	 * 파라미터 content : 리뷰 내용
	 * 파라미터 star : 별점
	 * 파라미터 tripDate : 여행 다녀온 날짜
	 * 리턴값 : 생성된 리뷰 idx
	 */
	@Override
	public int insertReview(int placeIdx, String id, String content, int star, String tripDate) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("placeIdx", placeIdx);
		map1.put("id", id);
		map1.put("content", content);
		map1.put("star", star);
		map1.put("tripDate", tripDate);
		sqlSession.insert("MyTripMapper.insertReview", map1);
		return (int)map1.get("reviewIdx");
	}
	
	/**
	 * insertReviewPic(int reviewIdx, String reviewImg) : 리뷰 사진 추가
	 * 파라미터 reviewIdx : 추가할 리뷰 idx
	 * 파라미터 reviewImg : 추가할 사진
	 */
	@Override
	public void insertReviewPic(int reviewIdx, String reviewImg) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("reviewIdx", reviewIdx);
		map1.put("reviewImg", reviewImg);
		sqlSession.insert("MyTripMapper.insertReviewPic", map1);
	}
	
	/**
	 * deleteMyReview(int reviewIdx) : <내 리뷰> 리뷰 삭제
	 * 파라미터 reviewIdx : 삭제할 리뷰의 idx
	 */
	@Override
	public void deleteMyReview(int reviewIdx) {
		sqlSession.delete("MyTripMapper.deleteMyReview", reviewIdx);
	}

	/**
	 * selectStarComment(int star) : <내 리뷰> 별점 코멘트 조회
	 * 파라미터 star : 별점 개수
	 * 리턴값 : 해당 별점 개수에 대한 코멘트 내용
	 */
	@Override
	public String selectStarComment(int star) {
		return sqlSession.selectOne("MyTripMapper.getStarComment", star);
	}

	
	
	/**
	 * selectPlaceDetail(String id, int placeIdx) : <장소 상세> 장소 정보 조회
	 * 파라미터 id : 해당 회원의 장소 찜 여부
	 * 파라미터 placeIdx : 해당 장소의 idx
	 * 리턴값 : 장소 정보 dto
	 */
	@Override
	public PlaceDto selectPlaceDetail(String id, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("placeIdx", placeIdx);
		return sqlSession.selectOne("MyTripMapper.getPlaceDetail", map1);
	}

	/**
	 * selectPlaceReview(String id, int placeIdx) : <장소 상세> 리뷰 내용 조회
	 * 파라미터 id : 해당 회원의 리뷰 찜 여부
	 * 파라미터 placeIdx : 해당 장소의 idx
	 * 리턴값 : 해당 장소에 대한 리뷰 정보 리스트 dto
	 */
	@Override
	public List<PlaceReviewDto> selectPlaceReview(String id, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("placeIdx", placeIdx);
		return sqlSession.selectList("MyTripMapper.getPlaceReview", map1);
	}

	/**
	 * deletePlaceReviewGood(String id, int reviewIdx) : <장소 상세> 장소 리뷰 추천 취소
	 * 파라미터 id : 해당 회원의 아이디
	 * 파라미터 reviewIdx : 취소할 리뷰의 idx
	 */
	@Override
	public void deletePlaceReviewGood(String id, int reviewIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("reviewIdx", reviewIdx);
		sqlSession.delete("MyTripMapper.deletePlaceReviewGood", map1);
	}

	/**
	 * insertPlaceReviewGood(String id, int reviewIdx) : <장소 상세> 장소 리뷰 추천
	 * 파라미터 id : 해당 회원의 아이디
	 * 파라미터 reviewIdx : 추천할 리뷰의 idx
	 */
	@Override
	public void insertPlaceReviewGood(String id, int reviewIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("reviewIdx", reviewIdx);
		sqlSession.insert("MyTripMapper.insertPlaceReviewGood", map1);
	}

	/**
	 * selectPlaceReviewGoodN(int reviewIdx) : <장소 상세> 리뷰 추천 개수
	 * 파라미터 reviewIdx : 해당 리뷰의 idx
	 * 리턴값 : 해당 리뷰의 추천 개수
	 */
	@Override
	public int selectPlaceReviewGoodN(int reviewIdx) {
		return sqlSession.selectOne("MyTripMapper.getPlaceReviewGoodN",reviewIdx);
	}
	
	/**
	 * insertClick(String id, int placeIdx) : 장소 클릭 이력 추가
	 * 파라미터 id : 장소를 클릭한 회원의 아이디
	 * 파라미터 placeIdx : 해당 장소의 idx
	 */
	@Override
	public void insertClick(String id, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("placeIdx", placeIdx);
		sqlSession.insert("MyTripMapper.insertClick", map1);
	}

	

	/**
	 * insertPlan(String id, String startDate, int days) : 여행 일정 추가
	 * 파라미터 id : 추가할 회원의 아이디
	 * 파라미터 startDate : 여행 시작 날짜
	 * 파라미터 days : 여행 일수
	 * 리턴값 : 생성된 여행 idx
	 */
	@Override
	public int insertPlan(String id, String startDate, int days) {
		AddPlanDto dto = new AddPlanDto();
		dto.setId(id);
		dto.setStartDate(startDate);
		dto.setDays(days);
		sqlSession.insert("MyTripMapper.insertPlan", dto);
		return dto.getPlanIdx();
	}

	/**
	 * insertPlanCity(int planIdx, int planOrder, int cityIdx) : 여행 일정 속 도시 추가
	 * 파라미터 planIdx : 추가할 여행 idx
	 * 파라미터 planOrder : 도시 순서
	 * 파라미터 cityIdx : 추가할 도시 idx
	 */
	@Override
	public void insertPlanCity(int planIdx, int planOrder, int cityIdx) {
		Map<String, Integer> map1 = new HashMap<>();
		map1.put("planIdx", planIdx);
		map1.put("planOrder", planOrder);
		map1.put("cityIdx", cityIdx);
		sqlSession.insert("MyTripMapper.insertPlanCity", map1);
	}
	
	/**
	 * insertPlanPlace(int planIdx, int day, int placeIdx) : 여행 일정 속 장소 추가
	 * 파라미터 planIdx : 추가할 여행 idx
	 * 파라미터 day : 장소를 추가할 일자
	 * 파라미터 placeIdx : 추가할 장소 idx
	 */
	@Override
	public void insertPlanPlace(int planIdx, int day, int placeIdx) {
		Map<String, Integer> map1 = new HashMap<>();
		map1.put("planIdx", planIdx);
		map1.put("day", day);
		map1.put("placeIdx", placeIdx);
		sqlSession.insert("MyTripMapper.insertPlanPlace", map1);
	}
	
	/**
	 * selectCityIdxByName(String name) : 도시명으로 도시 idx 찾기
	 * 파라미터 name : 조회할 도시 이름
	 * 리턴값 : DB에서 조회된 도시 idx
	 */
	@Override
	public int selectCityIdxByName(String name) {
		return sqlSession.selectOne("MyTripMapper.getCityIdxByName", name);
	}
	
	

	/**
	 * selectMenuBarProfile(String id) : 메뉴바 다가오는 여행 상세 정보 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : DB에서 조회된 다가오는 여행 상세 정보 dto 리스트
	 */
	@Override
	public List<MenuBarProfileDto> selectMenuBarProfile(String id) {
		return sqlSession.selectList("MyTripMapper.selectMenuBarProfile", id);
	}

	
	
	/**
	 * selectMyBellList(String id) : <내 알림> 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : DB에서 조회된 해당 회원의 알림 정보 dto 리스트
	 */
	@Override
	public List<MyBellListDto> selectMyBellList(String id) {
		return sqlSession.selectList("MyTripMapper.selectMyBell", id);
	}

	/**
	 * deleteAllMyBells(String id) : <내 알림> 모두 삭제
	 * 파라미터 id : 삭제할 회원의 아이디
	 */
	@Override
	public void deleteAllMyBells(String id) {
		sqlSession.delete("MyTripMapper.deleteAllMyBells", id);
	}

	/**
	 * deleteMyBell(int bellIdx) : <내 알림> 1개 삭제
	 * 파라미터 bellIdx : 삭제할 알림 idx
	 */
	@Override
	public void deleteMyBell(int bellIdx) {
		sqlSession.delete("MyTripMapper.deleteMyBell", bellIdx);
	}
	
	/**
	 * selectWriterIdByReviewIdx(int reviewIdx) : <내 알림> 리뷰 작성자 아이디 조회
	 * 파라미터 reviewIdx : 조회할 리뷰 idx
	 * 리턴값 : 해당 리뷰의 작성자 아이디
	 */
	@Override
	public String selectWriterIdByReviewIdx(int reviewIdx) {
		return sqlSession.selectOne("MyTripMapper.selectWriterIdByReviewIdx", reviewIdx);
	}

	/**
	 * insertBell(Integer reviewIdx, Integer recommendReviewIdx, String id, String memberId) : <내 알림> 알림 추가
	 * 파라미터 reviewIdx : 알림에 추가할 장소 리뷰 idx
	 * 파라미터 recommendReviewIdx : 알림에 추가할 도시 리뷰 idx
	 * 파라미터 id : 리뷰에 좋아요를 누른 회원의 아이디
	 * 파라미터 memberId : 리뷰 작성자 아이디
	 * 리턴값 : 생성된 알림 idx
	 */
	@Override
	public int insertBell(Integer reviewIdx, Integer recommendReviewIdx, String id, String memberId) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("reviewIdx", reviewIdx);
		map1.put("recommendReviewIdx", recommendReviewIdx);
		map1.put("id", id);
		map1.put("memberId", memberId);
		sqlSession.insert("MyTripMapper.insertBell", map1);
		
		return (int)map1.get("bellIdx");
	}

	

	/**
	 * insertAiPackImg(String id, String filename) : <AI 짐싸기> 파일 업로드
	 * 파라미터 id : 업로드할 회원의 아이디
	 * 파라미터 filename : 업로드할 파일(사진)
	 */
	@Override
	public void insertAiPackImg(String id, String filename) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("filename", filename);
		sqlSession.insert("MyTripMapper.insertAiPackImg", map1);
	}
	
	/**
	 * selectAiPackCityDays(String id) : <AI 짐싸기> 다가오는 여행 일정의 도시명과 일수 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 다가오는 여행 일정 1개의 도시명과 여행 일수
	 */
	@Override
	public Map<String, Object> selectAiPackCityDays(String id) {
		return sqlSession.selectOne("MyTripMapper.getAiPackCityDays", id);
	}

	/**
	 * updateAiPack(String present, String missing, String advice, String id) : <AI 짐싸기> API 내용 업데이트
	 * 파라미터 present : Chat GPT API로부터 받은 현재 짐 목록
	 * 파라미터 missing : Chat GPT API로부터 받은 잊은 짐 목록
	 * 파라미터 advice : Chat GPT API로부터 받은 조언
	 * 파라미터 id : 업데이트할 회원의 아이디
	 */
	@Override
	public void updateAiPack(String present, String missing, String advice, String id) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("present", present);
		map1.put("missing", missing);
		map1.put("advice", advice);
		map1.put("id", id);
		sqlSession.update("MyTripMapper.updateAiPack", map1);
	}
}
