package com.js.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.dao.MyTripDao;
import com.js.dto.UpdateMyPlaceDto;
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

@Service
public class MyTripServiceImpl implements MyTripService {
	@Autowired
	MyTripDao mtDao;

	/**
	 * getMemberDetail(String id) : 회원 정보 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원 정보 dto(아이디, 닉네임, 프로필 사진)
	 */
	@Override
	public MemberDto getMemberDetail(String id) {
		return mtDao.selectMemberDetail(id);
	}
	
	/**
	 * getPlanDetail(int planIdx) : <내 여행> 상세 정보
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 여행의 상세 정보(도시명, 출발날짜, 여행일수)
	 */
	@Override
	public List<PlanDetailDto> getPlanDetail(int planIdx) {
		return mtDao.selectPlanDetail(planIdx);
	}
	
	/**
	 * getUpcomingPlanList(String id) : 다가오는 여행 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 다가오는 여행 리스트(현재 날짜를 기준으로 최근순)
	 */
	@Override
	public List<UpcomingPlanListDto> getUpcomingPlanList(String id) {
		return mtDao.selectUpcomingPlanList(id);
	}
	
	/**
	 * updatePlanDate(int planIdx, String startDate, int days) : <내 여행> 여행 날짜 수정
	 * 파라미터 planIdx : 수정할 여행 idx
	 * 파라미터 startDate : 수정된 여행 시작 날짜
	 * 파라미터 days : 수정된 여행 일수
	 */
	@Override
	public void updatePlanDate(int planIdx, String startDate, int days) {
		mtDao.updatePlanDate(startDate, days, planIdx);
	}

	/**
	 * deletePlan(int planIdx) : <내 여행> 삭제
	 * 파라미터 planIdx : 삭제할 여행의 idx
	 */
	@Override
	public void deletePlan(int planIdx) {
		mtDao.deletePlan(planIdx);
	}

	/**
	 * getCityList() : <내 여행> 일정 추가_도시 리스트 조회
	 * 리턴값 : 도시 리스트(나라명, 도시명, 가까운 도시 정보, 도시 사진)
	 */
	@Override
	public List<CityListDto> getCityList() {
		return mtDao.selectCityList();
	}
	
	
	
	/**
	 * getLastDay(int planIdx) : <내 여행 상세> 여행 마지막 일 조회
	 * 파라미터 planIdx : 조회하려는 여행 idx
	 * 리턴값 : 여행의 마지막 일(1박 2일 -> 2)
	 */
	@Override
	public int getLastDay(int planIdx) {
		List<PlanDetailPlaceDto> list1 = mtDao.selectPlanDetailPlace(planIdx);
		int maxDay = 0;
		for(PlanDetailPlaceDto dto : list1) {
			if(dto.getDay() > maxDay) maxDay = dto.getDay();
		}
		return maxDay;
	}
	
	/**
	 * getPlanDetailPlace(int planIdx) : <내 여행 상세> 장소 리스트
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 idx의 여행 장소 리스트
	 */
	@Override
	public List<PlanDetailPlaceDto> getPlanDetailPlace(int planIdx) {
		List<PlanDetailPlaceDto> listRet = mtDao.selectPlanDetailPlace(planIdx);
		
		for(int i=0; i<=listRet.size()-1; i++) {
			int planPlaceIdx = listRet.get(i).getPlanPlaceIdx();
			List<PlanDetailPlaceMemoDto> listMemo = mtDao.selectPlanDetailPlaceMemo(planPlaceIdx);
			listRet.get(i).setListMemo(listMemo);
		}
		
		return listRet;
	}
	
	/**
	 * deletePlanPlace(int planPlaceIdx) : <내 여행 상세> 여행 장소 삭제
	 * 파라미터 planPlaceIdx : 삭제할 여행 장소 idx
	 */
	@Override
	public void deletePlanPlace(int planPlaceIdx) {
		int deleteOrder = mtDao.selectPlaceOrder(planPlaceIdx);
		mtDao.updatePlaceOrder(deleteOrder);
		mtDao.deletePlanPlace(planPlaceIdx); 
	}

	/**
	 * getPlanDetailPlaceMemo(int planPlaceIdx) : <내 여행 상세>의 장소 메모 리스트
	 * 파라미터 planPlaceIdx : 조회하려는 여행의 장소 idx
	 * 리턴값 : 해당 장소 idx의 메모 리스트
	 */
	@Override
	public List<PlanDetailPlaceMemoDto> getPlanDetailPlaceMemo(int planPlaceIdx) {
		return mtDao.selectPlanDetailPlaceMemo(planPlaceIdx);
	}

	/**
	 * insertPlaceMemo(InsertPlaceMemoDto dto) : <내 여행 상세>의 장소 메모 추가
	 * 파라미터 InsertPlaceMemoDto : 추가할 메모(메모 idx, 장소 idx, 내용)
	 */
	@Override
	public int insertPlaceMemo(InsertPlaceMemoDto dto) {
		return mtDao.insertPlaceMemo(dto);
	}
	
	/**
	 * deletePlaceMemo(int memoIdx) : <내 여행 상세>의 장소 메모 삭제
	 * 파라미터 memoIdx : 삭제할 메모의 idx
	 */
	@Override
	public void deletePlaceMemo(int memoIdx) {
		mtDao.deletePlaceMemo(memoIdx);
	}

	/**
	 * getPlanPlaceMap(int planIdx) : <내 여행 상세>의 장소 위치 리스트
	 * 파라미터 planIdx : 조회하려는 여행의 idx
	 * 리턴값 : 해당 여행의 장소 위치 리스트(일자, 장소명, 위도, 경도)
	 */
	@Override
	public List<PlanPlaceMapDto> getPlanPlaceMap(int planIdx) {
		return mtDao.selectPlanPlaceMap(planIdx);
	}

	/**
	 * getAddMyPlace(String id) : <내 여행 상세> 장소 추가_내 저장 장소 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원의 저장된 장소 정보 dto 리스트
	 */
	@Override
	public List<AddPlaceDto> getAddMyPlace(String id) {
		return mtDao.selectAddMyPlace(id);
	}

	/**
	 * getAddRecommendPlace(int planIdx) : <내 여행 상세> 장소 추가_추천 장소 리스트
	 * 파라미터 planIdx : 해당 여행 idx
	 * 리턴값 : 등록한 여행 도시의 장소 정보 dto 리스트
	 */
	@Override
	public List<AddPlaceDto> getAddRecommendPlace(int planIdx) {
		return mtDao.selectAddRecommendPlace(planIdx);
	}

	
	
	/**
	 * getMyPlace(String id) : <내 저장> 장소 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 회원의 저장된 장소 리스트(장소 정보, 별점, 해당 장소 리뷰 개수)
	 */
	@Override
	public List<MyPlaceListDto> getMyPlace(String id) {
		return mtDao.selectMyPlaceList(id);
	}

	/**
	 * updateMyPlaceMemo(UpdateMyPlaceDto dto) : <내 저장> 장소 메모 추가
	 * 파라미터 UpdateMyPlaceDto : 해당 장소의 메모 정보(회원 아이디, 찜 날짜, 메모 내용)
	 */
	@Override
	public void updateMyPlaceMemo(UpdateMyPlaceDto dto) {
		mtDao.updateMyPlaceMemo(dto);
	}

	/**
	 * deleteMyPlace(String id, int placeIdx) : <내 저장> 장소 삭제
	 * 파라미터 id : 삭제할 회원의 아이디
	 * 파라미터 placeIdx : 삭제할 장소의 idx
	 */
	@Override
	public void deleteMyPlace(String id, int placeIdx) {
		mtDao.deleteMyPlace(id, placeIdx);
	}
	
	/**
	 * insertMyPlace(String id, int placeIdx) : <내 저장> 장소 추가
	 * 파라미터 id : 추가할 회원의 아이디
	 * 파라미터 placeIdx : 추가할 장소의 idx
	 */
	@Override
	public void insertMyPlace(String id, int placeIdx) {
		mtDao.insertMyPlace(id, placeIdx);
	}

	

	/**
	 * getMyReview(String id) : <내 리뷰> 장소 리뷰 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 내 장소 리뷰 리스트(도시명, 장소 정보, 리뷰 정보, 리뷰 좋아요 여부, 좋아요 수, 리뷰 사진 리스트)
	 */
	@Override
	public List<MyReviewListDto> getMyReview(String id) {
		return mtDao.selectMyReview(id);
	}
	
	/**
	 * getPlaceReviewImg(int reviewIdx) : <내 리뷰> 장소 리뷰 사진 조회
	 * 파라미터 reviewIdx : 조회할 장소 리뷰 idx
	 * 리턴값 : 조회된 해당 리뷰의 사진 리스트
	 */
	@Override
	public List<String> getPlaceReviewImg(int reviewIdx) {
		return mtDao.selectPlaceReviewImg(reviewIdx);
	}
	
	/**
	 * getMyCityReview(String id) : <내 리뷰> 도시 리뷰 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 내 도시 리뷰 리스트(도시명, 장소 정보, 리뷰 정보, 리뷰 좋아요 여부, 좋아요 수, 리뷰 사진 리스트)
	 */
	@Override
	public List<MyReviewListDto> getMyCityReview(String id) {
		return mtDao.selectMyCityReview(id);
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
		return mtDao.insertReview(placeIdx, id, content, star, tripDate);
	}

	/**
	 * insertReviewPic(int reviewIdx, String reviewImg) : 리뷰 사진 추가
	 * 파라미터 reviewIdx : 추가할 리뷰 idx
	 * 파라미터 reviewImg : 추가할 사진
	 */
	@Override
	public void insertReviewPic(int reviewIdx, String reviewImg) {
		mtDao.insertReviewPic(reviewIdx, reviewImg);
	}
	
	/**
	 * deleteMyReview(int reviewIdx) : <내 리뷰> 리뷰 삭제
	 * 파라미터 reviewIdx : 삭제할 리뷰의 idx
	 */
	@Override
	public void deleteMyReview(int reviewIdx) {
		mtDao.deleteMyReview(reviewIdx);
	}

	/**
	 * getStarComment(int star) : <내 리뷰> 별점 코멘트 조회
	 * 파라미터 star : 별점 개수
	 * 리턴값 : 해당 별점 개수에 대한 코멘트 내용
	 */
	@Override
	public String getStarComment(int star) {
		return mtDao.selectStarComment(star);
	}
	
	
	
	/**
	 * getPlaceDetail(String id, int placeIdx) : <장소 상세> 장소 정보 조회
	 * 파라미터 id : 해당 회원의 장소 찜 여부
	 * 파라미터 placeIdx : 해당 장소의 idx
	 * 리턴값 : 장소 정보 dto
	 */
	@Override
	public PlaceDto getPlaceDetail(String id, int placeIdx) {
		mtDao.insertClick(id, placeIdx);
		return mtDao.selectPlaceDetail(id, placeIdx);
	}

	/**
	 * getPlaceReviewList(String id, int placeIdx) : <장소 상세> 리뷰 내용 조회
	 * 파라미터 id : 해당 회원의 리뷰 찜 여부
	 * 파라미터 placeIdx : 해당 장소의 idx
	 * 리턴값 : 해당 장소에 대한 리뷰 정보 리스트 dto
	 */
	@Override
	public List<PlaceReviewDto> getPlaceReviewList(String id, int placeIdx) {
		List<PlaceReviewDto> listRet = mtDao.selectPlaceReview(id, placeIdx);
		
		for(int i=0; i<=listRet.size()-1; i++) {
			int reviewIdx = listRet.get(i).getReviewIdx();
			List<String> listPlaceReviewImg = mtDao.selectPlaceReviewImg(reviewIdx);
			listRet.get(i).setListReviewImg(listPlaceReviewImg);
		}
		return listRet;
	}

	/**
	 * deletePlaceReviewGood(String id, int reviewIdx) : <장소 상세> 장소 리뷰 추천 취소
	 * 파라미터 id : 해당 회원의 아이디
	 * 파라미터 reviewIdx : 취소할 리뷰의 idx
	 */
	@Override
	public void deletePlaceReviewGood(String id, int reviewIdx) {
		mtDao.deletePlaceReviewGood(id, reviewIdx);
	}

	/**
	 * insertPlaceReviewGood(String id, int reviewIdx) : <장소 상세> 장소 리뷰 추천
	 * 파라미터 id : 해당 회원의 아이디
	 * 파라미터 reviewIdx : 추천할 리뷰의 idx
	 */
	@Override
	public void insertPlaceReviewGood(String id, int reviewIdx) {
		String writerId = mtDao.selectWriterIdByReviewIdx(reviewIdx);
		mtDao.insertBell(reviewIdx, null, id, writerId);
		mtDao.insertPlaceReviewGood(id, reviewIdx);
	}

	/**
	 * getPlaceReviewGoodN(int reviewIdx) : <장소 상세> 리뷰 추천 개수
	 * 파라미터 reviewIdx : 해당 리뷰의 idx
	 * 리턴값 : 해당 리뷰의 추천 개수
	 */
	@Override
	public int getPlaceReviewGoodN(int reviewIdx) {
		return mtDao.selectPlaceReviewGoodN(reviewIdx);
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
		return mtDao.insertPlan(id, startDate, days);
	}
	
	/**
	 * insertPlanCity(int planIdx, int planOrder, int cityIdx) : 여행 일정 속 도시 추가
	 * 파라미터 planIdx : 추가할 여행 idx
	 * 파라미터 planOrder : 도시 순서
	 * 파라미터 cityIdx : 추가할 도시 idx
	 */
	@Override
	public void insertPlanCity(int planIdx, int planOrder, int cityIdx) {
		mtDao.insertPlanCity(planIdx, planOrder, cityIdx);
	}
	
	/**
	 * insertPlanPlace(int planIdx, int day, int placeIdx) : 여행 일정 속 장소 추가
	 * 파라미터 planIdx : 추가할 여행 idx
	 * 파라미터 day : 장소를 추가할 일자
	 * 파라미터 placeIdx : 추가할 장소 idx
	 */
	@Override
	public void insertPlanPlace(int planIdx, int day, int placeIdx) {
		mtDao.insertPlanPlace(planIdx, day, placeIdx);
	}
	
	/**
	 * getCityIdxByName(String name) : 도시명으로 도시 idx 찾기
	 * 파라미터 name : 조회할 도시 이름
	 * 리턴값 : DB에서 조회된 도시 idx
	 */
	@Override
	public int getCityIdxByName(String name) {
		return mtDao.selectCityIdxByName(name);
	}

	

	/**
	 * getMenuBarProfile(String id) : 메뉴바 다가오는 여행 상세 정보 리스트
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : DB에서 조회된 다가오는 여행 상세 정보 dto 리스트
	 */
	@Override
	public List<MenuBarProfileDto> getMenuBarProfile(String id) {
		return mtDao.selectMenuBarProfile(id);
	}
	
	

	/**
	 * getMyBellList(String id) : <내 알림> 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : DB에서 조회된 해당 회원의 알림 정보 dto 리스트
	 */
	@Override
	public List<MyBellListDto> getMyBellList(String id) {
		return mtDao.selectMyBellList(id);
	}

	/**
	 * deleteAllMyBells(String id) : <내 알림> 모두 삭제
	 * 파라미터 id : 삭제할 회원의 아이디
	 */
	@Override
	public void deleteAllMyBells(String id) {
		mtDao.deleteAllMyBells(id);
	}

	/**
	 * deleteMyBell(int bellIdx) : <내 알림> 1개 삭제
	 * 파라미터 bellIdx : 삭제할 알림 idx
	 */
	@Override
	public void deleteMyBell(int bellIdx) {
		mtDao.deleteMyBell(bellIdx);
	}

	/**
	 * getWriterIdByReviewIdx(int reviewIdx) : <내 알림> 리뷰 작성자 아이디 조회
	 * 파라미터 reviewIdx : 조회할 리뷰 idx
	 * 리턴값 : 해당 리뷰의 작성자 아이디
	 */
	@Override
	public String getWriterIdByReviewIdx(int reviewIdx) {
		return mtDao.selectWriterIdByReviewIdx(reviewIdx);
	}

	/**
	 * insertMyBell(Integer reviewIdx, Integer recommendReviewIdx, String id, String memberId) : <내 알림> 알림 추가
	 * 파라미터 reviewIdx : 알림에 추가할 장소 리뷰 idx
	 * 파라미터 recommendReviewIdx : 알림에 추가할 도시 리뷰 idx
	 * 파라미터 id : 리뷰에 좋아요를 누른 회원의 아이디
	 * 파라미터 memberId : 리뷰 작성자 아이디
	 * 리턴값 : 생성된 알림 idx
	 */
	@Override
	public int insertMyBell(Integer reviewIdx, Integer recommendReviewIdx, String id, String memberId) {
		return mtDao.insertBell(reviewIdx, recommendReviewIdx, id, memberId);
	}

	
	
	/**
	 * insertAiPackImg(String id, String filename) : <AI 짐싸기> 파일 업로드
	 * 파라미터 id : 업로드할 회원의 아이디
	 * 파라미터 filename : 업로드할 파일(사진)
	 */
	@Override
	public void insertAiPackImg(String id, String filename) {
		mtDao.insertAiPackImg(id, filename);
	}

	/**
	 * getAiPackCityDays(String id) : <AI 짐싸기> 다가오는 여행 일정의 도시명과 일수 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 다가오는 여행 일정 1개의 도시명과 여행 일수
	 */
	@Override
	public Map<String, Object> getAiPackCityDays(String id) {
		return mtDao.selectAiPackCityDays(id);
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
		mtDao.updateAiPack(present, missing, advice, id);
	}
}
