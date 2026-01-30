package com.js.service;

import java.util.List;
import java.util.Map;

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

public interface MyTripService {
	MemberDto getMemberDetail(String id);	// 회원 정보 조회
	List<PlanDetailDto> getPlanDetail(int planIdx);	// <내 여행> 여행 상세 정보
	List<UpcomingPlanListDto> getUpcomingPlanList(String id);	// 다가오는 여행 리스트 조회
	void updatePlanDate(int planIdx, String startDate, int days);	// <내 여행> 여행 날짜 수정
	void deletePlan(int planIdx);	// <내 여행> 여행 삭제
	List<CityListDto> getCityList();	// <내 여행> 도시 리스트 조회
	
	int getLastDay(int planIdx);	// <내 여행 상세> 여행 마지막 일 조회
	List<PlanDetailPlaceDto> getPlanDetailPlace(int planIdx);	// <내 여행 상세> 여행 장소 리스트
	void deletePlanPlace(int planPlaceIdx);	// <내 여행 상세> 여행 장소 삭제
	List<PlanDetailPlaceMemoDto> getPlanDetailPlaceMemo(int planPlaceIdx);	// <내 여행 상세> 장소 메모 리스트
	int insertPlaceMemo(InsertPlaceMemoDto dto);	// <내 여행 상세> 장소 메모 추가
	void deletePlaceMemo(int memoIdx);	// <내 여행 상세> 장소 메모 삭제
	List<PlanPlaceMapDto> getPlanPlaceMap(int planIdx);	// <내 여행 상세> 여행 장소 위치 리스트
	List<AddPlaceDto> getAddMyPlace(String id);	// <내 여행 상세> 장소 추가_내 저장 장소 목록
	List<AddPlaceDto> getAddRecommendPlace(int planIdx);	// <내 여행 상세> 장소 추가_추천 장소 목록
	
	List<MyPlaceListDto> getMyPlace(String id);	// <내 저장> 찜 장소 리스트 조회
	void updateMyPlaceMemo(UpdateMyPlaceDto dto);	// <내 저장> 장소 메모 변경
	void deleteMyPlace(String id, int placeIdx);	// <내 저장> 장소 저장 취소
	void insertMyPlace(String id, int placeIdx);	// <내 저장> 장소 저장
	
	List<MyReviewListDto> getMyReview(String id);	// <내 리뷰> 내 장소 리뷰 조회
	List<String> getPlaceReviewImg(int reviewIdx);	// <내 리뷰> 장소 리뷰 사진 조회
	List<MyReviewListDto> getMyCityReview(String id);	// <내 리뷰> 도시 리뷰 조회
	int insertReview(int placeIdx, String id, String content, int star, String tripDate);	// <내 리뷰> 리뷰 추가
	void insertReviewPic(int reviewIdx, String reviewImg);	// <내 리뷰> 리뷰 사진 추가
	void deleteMyReview(int reviewIdx);	// <내 리뷰> 리뷰 삭제
	String getStarComment(int star);	// <내 리뷰> 별점 코멘트
	
	PlaceDto getPlaceDetail(String id, int placeIdx);	// <장소 상세> 장소 상세 정보 조회
	List<PlaceReviewDto> getPlaceReviewList(String id, int placeIdx);	// <장소 상세> 장소 리뷰 리스트 조회
	void deletePlaceReviewGood(String id, int reviewIdx);	// <장소 상세> 장소 리뷰 추천 취소
	void insertPlaceReviewGood(String id, int reviewIdx);	// <장소 상세> 장소 리뷰 추천
	int getPlaceReviewGoodN(int reviewIdx);	// <장소 상세> 장소 리뷰 추천 개수
	
	int insertPlan(String id, String startDate, int days);	// 여행 일정 추가
	void insertPlanCity(int planIdx, int planOrder, int cityIdx);	// 여행 일정 속 도시 추가
	void insertPlanPlace(int planIdx, int day, int placeIdx);	// 여행 일정 속 장소 추가
	int getCityIdxByName(String name);	// 도시명으로 도시 idx 찾기
	
	List<MenuBarProfileDto> getMenuBarProfile(String id);	// <메뉴바> 회원 정보 조회
	
	List<MyBellListDto> getMyBellList(String id);	// <내 알림> 알림 목록 조회
	void deleteAllMyBells(String id);	// <내 알림> 알림 모두 삭제
	void deleteMyBell(int bellIdx);	// <내 알림> 알림 1개 삭제
	String getWriterIdByReviewIdx(int reviewIdx);	// <내 알림> 리뷰 작성자 아이디 조회 
	int insertMyBell(Integer reviewIdx, Integer recommendReviewIdx, String id, String memberId);	// <내 알림> 알림 추가
	
	void insertAiPackImg(String id, String filename);	// <AI 짐싸기> 파일 업로드
	Map<String, Object> getAiPackCityDays(String id);	// <AI 짐싸기> 다가오는 여행 일정의 도시명과 여행 일수 조회
	void updateAiPack(String present, String missing, String advice, String id);	// <AI 짐싸기> API 내용 업데이트
}
