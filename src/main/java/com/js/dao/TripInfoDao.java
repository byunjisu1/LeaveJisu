package com.js.dao;

import java.util.List;
import java.util.Map;

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
import com.js.dto.PlaceIdxNameDto;
import com.js.dto.PlanCourseDto;
import com.js.dto.RecommendPlanDetailDto;
import com.js.dto.RecommendPlanPlaceDto;
import com.js.dto.ReviewListDto;

public interface TripInfoDao {
	List<CityListDto> selectCityList();		// <추천 일정> 도시 리스트
	List<RecommendPlanDetailDto> selectConceptList(int cityIdx);	// <추천 일정> 일정 컨셉 내용 리스트
	List<RecommendPlanPlaceDto> selectRecommendPlaceList(int recommendPlanIdx);	// <추천 일정> 일정 장소 리스트
	List<PlaceDto> selectTop10(int cityIdx);	// <추천 일정> TOP 10 장소 리스트
	String selectCityNameByIdx(int cityIdx);	// <추천 일정 전체> 도시 idx로 도시 이름 찾기
	List<ReviewListDto> selectRecommendReviewList(int cityIdx, String id, String orderBy);  // <추천 일정 전체> 도시 리뷰 리스트
	List<String> selectRecommendReviewImgList(int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 사진 리스트
	int insertRecommendReview(int cityIdx, String id, String content, String tripDate);	// <추천 일정 전체> 도시 리뷰 작성
	void insertRecommendReviewPic(int recommendReviewIdx, String reviewImg);	// <추천 일정 전체> 도시 리뷰 사진 추가
	void deleteRecommendPlanReviewGood(String id, int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천 취소
	void insertRecommendPlanReviewGood(String id, int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천
	int getRecommendReviewGoodN(int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천 수
	List<BlogListDto> selectBlogList();	// <추천 일정> 블로그 리스트
	List<String> selectBlogImgList(int blogIdx);	// <추천 일정> 블로그 이미지 리스트
	
	BlogDetailDto selectBlogDetail(int blogIdx);	// <블로그> 블로그 정보 조회
	List<BlogDetailPlaceDto> selectBlogDetailPlace(int blogIdx);	// <블로그> 블로그 장소 목록
	List<PlanCourseDto> selectPlanCourse(int blogIdx);	// <블로그> 여행 코스 목록
	List<BlogPlaceMapDto> selectBlogPlaceMap(int blogIdx);	// <블로그> 여행 코스 장소 위치 리스트
	List<BlogCommentListDto> selectBlogCommentList(String id, int blogIdx);	// <블로그> 블로그 댓글 목록
	BlogCommentListDto selectBlogCommentByIdx(String id, int blogCommentIdx);	// <블로그> 블로그 댓글 정보
	void deleteBlogCommentGood(String id, int blogCommentIdx);	// <블로그> 댓글 추천 취소
	void insertBlogCommentGood(String id, int blogCommentIdx);	// <블로그> 댓글 추천
	List<Map<String, Object>> getBlogCommentN(int blogIdx);	// <블로그> 댓글 추천 수
	int insertBlogComment(int blogIdx, String id, String content);	// <블로그> 댓글 작성
	void deleteBlogComment(int blogCommentIdx);	// <블로그> 댓글 삭제
	void updateBlogComment(String content, int blogCommentIdx);	// <블로그> 댓글 수정
	
	List<AirportListDto> selectAirportList();	// <항공편> 공항 목록
	List<String> selectAirlineIdList();   //  <항공편> 항공사 목록
	void setAirlineLogo(String id, String imgUrl);  //  <항공편> 항공사 로고 이미지 업데이트
	Map<String,String> getAirlineLogoNameById(String airlineId);	// <항공편> 항공사 아이디로 로고 이미지, 이름 찾기
	
	MyAiPackDto selectMyAiPackList(String id);	// <AI 짐싸기> 내 AI 짐싸기 기존 정보 조회
	
	String selectCityImgByIdx(int cityIdx);	// <AI 추천일정> 도시 idx로 도시 이미지 찾기
	List<AiRecommendPlaceDto> selectAiRecommendPlaceList(String id);	// <AI 추천일정> AI 추천 일정 장소 리스트 조회
	List<Map<String,Object>> selectPlaceListByCityName(String cityName);	// <AI 추천일정> Chat GPT API에게 줄 해당 도시별 장소 목록
	List<Map<String,Object>> selectAllPlaceList();	// <AI 추천일정> Chat GPT API에게 줄 전체 장소 목록
	void deleteAIRecommendPlaceListByMemberId(String id);	// <AI 추천일정> 해당 아이디의 AI 추천 일정 정보 삭제
	void insertAIRecommendPlace(String id, int day, int placeOrder, int placeIdx);	// <AI 추천일정> Chat GPT API 추천 일정 정보 추가
	List<Map<String,Object>> selectClickHistoryOneWeekByMemberId(String id);	// <AI 추천일정> 해당 아이디의 최근 일주일 간 클릭 이력
	
	String selectWriterIdByIdx(int recommendReviewIdx);	// <내 리뷰> 도시 리뷰 idx로 작성자 아이디 찾기
	void deleteMyCityReview(int recommendReviewIdx);	// <내 리뷰> 내 도시 리뷰 삭제
}
