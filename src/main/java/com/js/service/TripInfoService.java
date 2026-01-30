package com.js.service;

import java.util.List;
import java.util.Map;

import com.js.dto.AiRecommendPlaceDto;
import com.js.dto.AirportListDto;
import com.js.dto.BlogCommentListDto;
import com.js.dto.BlogDetailDto;
import com.js.dto.BlogListDto;
import com.js.dto.BlogPlaceMapDto;
import com.js.dto.CityListDto;
import com.js.dto.FlightDto;
import com.js.dto.MyAiPackDto;
import com.js.dto.PlaceDto;
import com.js.dto.PlanCourseDto;
import com.js.dto.RecommendPlanDetailDto;
import com.js.dto.ReviewListDto;

public interface TripInfoService {
	List<CityListDto> getCityListRecommend();	// <추천 일정> 도시 리스트
	List<RecommendPlanDetailDto> getRecommendPlanDetailList(int cityIdx);	// <추천 일정> 일정 컨셉 내용 리스트
	List<PlaceDto> getPlaceTop10(int cityIdx);	// <추천 일정> TOP 10 장소 리스트
	String getCityNameByIdx(int cityIdx);	// <추천 일정 전체> 도시 idx로 도시명 찾기
	List<ReviewListDto> getRecommendReviewList(int cityIdx, String id, String orderBy);	// <추천 일정 전체> 도시 리뷰 리스트
	List<String> getRecommendReviewImgList(int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 사진 리스트
	int insertRecommendReview(int cityIdx, String id, String content, String tripDate);	// <추천 일정 전체> 도시 리뷰 작성
	void insertRecommendReviewPic(int recommendReviewIdx, String reviewImg);	// <추천 일정 전체> 도시 리뷰 사진 추가
	void deleteRecommendPlanReviewGood(String id, int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천 취소
	String getWriterIdByIdx(int recommendReviewIdx);	// <내 알림> 도시 리뷰 idx로 작성자 아이디 찾기
	void insertRecommendPlanReviewGood(String id, int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천
	int getRecommendReviewGoodN(int recommendReviewIdx);	// <추천 일정 전체> 도시 리뷰 추천 수 
	
	List<BlogListDto> getBlogList();	// <추천 일정> 블로그 리스트
	BlogDetailDto getBlogDetail(int blogIdx);	// <블로그> 블로그 정보 조회
	List<PlanCourseDto> getPlanCourse(int blogIdx);	// <블로그> 블로그 여행 코스 목록
	List<BlogPlaceMapDto> getBlogPlaceMapList(int blogIdx);	// <블로그> 여행 코스 장소 위치 리스트
	List<BlogCommentListDto> getBlogCommentList(String id, int blogIdx);	// <블로그> 댓글 목록 리스트
	BlogCommentListDto getBlogCommentByIdx(String id, int blogCommentIdx);	// <블로그> 블로그 댓글 정보
	void deleteBlogCommentGood(String id, int blogCommentIdx);	// <블로그> 댓글 추천 취소
	void insertBlogCommentGood(String id, int blogCommentIdx);	// <블로그> 댓글 추천
	List<Map<String, Object>> getCommentN(int blogIdx);	// <블로그> 댓글 추천 수
	int insertBlogComment(int blogIdx, String id, String content);	// <블로그> 댓글 작성
	void deleteBlogComment(int blogCommentIdx);	// <블로그> 댓글 삭제
	void updateBlogComment(String content, int blogCommentIdx);	// <블로그> 댓글 수정

	List<AirportListDto> getAirportList();	// <항공편> 공항 목록
	List<FlightDto> getFlightList(String departure, String destination, String startDate, int nop);	// <항공편> 항공편 목록 조회
	String getAirlineLogoById(String airlineId);	// <항공편> 항공사 아이디로 항공사 로고 이미지 찾기
	String getAirlineNameById(String airlineId);	// <항공편> 항공사 아이디로 항공사 이름 찾기

	MyAiPackDto getMyAiPack(String id);	// <AI 짐싸기> 내 AI 짐싸기 기존 정보 조회
	
	void deleteMyCityReview(int recommendReviewIdx);	// <내 리뷰> 내 도시 리뷰 삭제
	
	String getCityImgByIdx(int cityIdx);	// <AI 추천일정> 도시 idx로 도시 이미지 찾기
	List<AiRecommendPlaceDto> getAiRecommendPlaceList(String id);	// <AI 추천일정> AI 추천 일정 장소 리스트 조회
	List<Map<String,Object>> getPlaceListByCityName(String cityName);	//  <AI 추천일정> Chat GPT API에게 줄 해당 도시별 장소 목록
}
