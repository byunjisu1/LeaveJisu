package com.js.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.js.dao.MyTripDao;
import com.js.dao.TripInfoDao;
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
import com.js.util.ApiAirline;

@Service
public class TripInfoServiceImpl implements TripInfoService {
	@Autowired
	TripInfoDao tiDao;
	@Autowired
	MyTripDao mtDao;

	/**
	 * getCityListRecommend() : <추천 일정> 도시 리스트
	 * 리턴값 : 추천 일정 도시 정보 dto 리스트
	 */
	@Override
	public List<CityListDto> getCityListRecommend() {
		return tiDao.selectCityList();
	}

	/**
	 * getRecommendPlanDetailList(int cityIdx) : <추천 일정> 일정 컨셉 내용 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 해당 도시의 추천 일정 컨셉 상세 내용 dto 리스트
	 */
	@Override
	public List<RecommendPlanDetailDto> getRecommendPlanDetailList(int cityIdx) {
		List<RecommendPlanDetailDto> listPlan = tiDao.selectConceptList(cityIdx);
		for(int i=0; i<listPlan.size(); i++) {
			int recommendPlanIdx = listPlan.get(i).getRecommendPlanIdx();
			listPlan.get(i).setListPlace(tiDao.selectRecommendPlaceList(recommendPlanIdx));
		}
		return listPlan;
	}
	
	/**
	 * getPlaceTop10(int cityIdx) : <추천 일정> TOP 10 장소 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 해당 도시의 TOP 10 장소 정보 dto 리스트
	 */
	@Override
	public List<PlaceDto> getPlaceTop10(int cityIdx) {
		return tiDao.selectTop10(cityIdx);
	}
	
	/**
	 * getCityNameByIdx(int cityIdx) : <추천 일정 전체> 도시 idx로 도시명 찾기
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 조회된 도시명
	 */
	@Override
	public String getCityNameByIdx(int cityIdx) {
		return tiDao.selectCityNameByIdx(cityIdx);
	}

	/**
	 * getRecommendReviewList(int cityIdx, String id, String orderBy) : <추천 일정 전체> 도시 리뷰 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 파라미터 id : 해당 회원의 리뷰 추천 여부
	 * 파라미터 orderBy : 정렬 방법(order:null(="추천순") 또는 "최신순")
	 * 리턴값 : 해당 도시 리뷰 정보 dto 리스트
	 */
	@Override
	public List<ReviewListDto> getRecommendReviewList(int cityIdx, String id, String orderBy) {
		return tiDao.selectRecommendReviewList(cityIdx, id, orderBy);
	}
	
	/**
	 * getRecommendReviewImgList(int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 사진 리스트
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : 해당 도시 리뷰의 사진 리스트
	 */
	@Override
	public List<String> getRecommendReviewImgList(int recommendReviewIdx) {
		return tiDao.selectRecommendReviewImgList(recommendReviewIdx);
	}
	
	/**
	 * insertRecommendReview(int cityIdx, String id, String content, String tripDate) : <추천 일정 전체> 도시 리뷰 작성
	 * 파라미터 cityIdx : 작성할 도시 idx
	 * 파라미터 id : 리뷰 작성자 아이디
	 * 파라미터 content : 리뷰 작성 내용
	 * 파라미터 tripDate : 여행한 날짜
	 * 리턴값 : 생성된 도시 리뷰 idx
	 */
	@Override
	public int insertRecommendReview(int cityIdx, String id, String content, String tripDate) {
		return tiDao.insertRecommendReview(cityIdx, id, content, tripDate);
	}
	
	/**
	 * insertRecommendReviewPic(int recommendReviewIdx, String reviewImg) : <추천 일정 전체> 도시 리뷰 사진 추가
	 * 파라미터 recommendReviewIdx : 추가할 도시 리뷰 idx
	 * 파라미터 reviewImg : 추가할 리뷰 사진
	 */
	@Override
	public void insertRecommendReviewPic(int recommendReviewIdx, String reviewImg) {
		tiDao.insertRecommendReviewPic(recommendReviewIdx, reviewImg);
	}
	
	/**
	 * deleteRecommendPlanReviewGood(String id, int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천 취소
	 * 파라미터 id : 추천 취소하는 회원의 아이디
	 * 파라미터 recommendReviewIdx : 추천 취소할 도시 리뷰 idx
	 */
	@Override
	public void deleteRecommendPlanReviewGood(String id, int recommendReviewIdx) {
		tiDao.deleteRecommendPlanReviewGood(id, recommendReviewIdx);
	}

	/**
	 * getWriterIdByIdx(int recommendReviewIdx) : <내 알림> 도시 리뷰 idx로 작성자 아이디 찾기
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : 해당 리뷰의 작성자 아이디
	 */
	@Override
	public String getWriterIdByIdx(int recommendReviewIdx) {
		return tiDao.selectWriterIdByIdx(recommendReviewIdx);
	}
	
	/**
	 * insertRecommendPlanReviewGood(String id, int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천
	 * 파라미터 id : 추천하는 회원의 아이디
	 * 파라미터 recommendReviewIdx : 추천할 도시 리뷰 idx
	 * 추천을 누르면 해당 작성자에게 <내 알림> 추가
	 */
	@Override
	public void insertRecommendPlanReviewGood(String id, int recommendReviewIdx) {
		String writerId = tiDao.selectWriterIdByIdx(recommendReviewIdx);
		mtDao.insertBell(null, recommendReviewIdx, id, writerId);
		tiDao.insertRecommendPlanReviewGood(id, recommendReviewIdx);
	}
	
	/**
	 * getRecommendReviewGoodN(int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천 수
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : 해당 리뷰의 추천 개수
	 */
	@Override
	public int getRecommendReviewGoodN(int recommendReviewIdx) {
		return tiDao.getRecommendReviewGoodN(recommendReviewIdx);
	}
	
	
	
	/**
	 * getBlogList() : <추천 일정> 블로그 리스트
	 * 리턴값 : 블로그 정보 dto 리스트(블로그 상세 정보 + 블로그 이미지)
	 */
	@Override
	public List<BlogListDto> getBlogList() {
		List<BlogListDto> listBlog = tiDao.selectBlogList();
		for(int i=0; i<listBlog.size(); i++) {
			int blogIdx = listBlog.get(i).getBlogIdx();
			listBlog.get(i).setBlogImgUrlList(tiDao.selectBlogImgList(blogIdx));
		}
		return listBlog;
	}

	/**
	 * getBlogDetail(int blogIdx) : <블로그> 블로그 상세 정보 조회
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 상세 정보 dto
	 */
	@Override
	public BlogDetailDto getBlogDetail(int blogIdx) {
		BlogDetailDto listBlog = tiDao.selectBlogDetail(blogIdx);
		listBlog.setListBlogPlace(tiDao.selectBlogDetailPlace(listBlog.getBlogIdx()));
		return listBlog;
	}

	/**
	 * getPlanCourse(int blogIdx) : <블로그> 여행 코스 목록
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 여행 코스 dto 리스트
	 */
	@Override
	public List<PlanCourseDto> getPlanCourse(int blogIdx) {
		return tiDao.selectPlanCourse(blogIdx);
	}

	/**
	 * getBlogPlaceMapList(int blogIdx) : <블로그> 여행 코스 장소 위치 리스트
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 여행 코스 장소 위치 dto 리스트
	 */
	@Override
	public List<BlogPlaceMapDto> getBlogPlaceMapList(int blogIdx) {
		return tiDao.selectBlogPlaceMap(blogIdx);
	}
	
	/**
	 * getBlogCommentList(String id, int blogIdx) : <블로그> 댓글 목록 리스트
	 * 파라미터 id : 해당 회원의 댓글 추천 여부
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 댓글 목록 dto 리스트
	 */
	@Override
	public List<BlogCommentListDto> getBlogCommentList(String id, int blogIdx) {
		return tiDao.selectBlogCommentList(id, blogIdx);
	}
	
	/**
	 * getBlogCommentByIdx(String id, int blogCommentIdx) : <블로그> 댓글 정보 조회
	 * 파라미터 id : 해당 회원의 댓글 추천 여부
	 * 파라미터 blogCommentIdx : 조회할 블로그 댓글 idx
	 * 리턴값 : 해당 블로그 댓글의 상세 정보 dto
	 */
	@Override
	public BlogCommentListDto getBlogCommentByIdx(String id, int blogCommentIdx) {
		return tiDao.selectBlogCommentByIdx(id, blogCommentIdx);
	}

	/**
	 * deleteBlogCommentGood(String id, int blogCommentIdx) : <블로그> 댓글 추천 취소
	 * 파라미터 id : 추천 취소하는 회원의 아이디
	 * 파라미터 blogCommentIdx : 취소할 블로그 댓글 idx
	 */
	@Override
	public void deleteBlogCommentGood(String id, int blogCommentIdx) {
		tiDao.deleteBlogCommentGood(id, blogCommentIdx);
	}

	/**
	 * insertBlogCommentGood(String id, int blogCommentIdx) : <블로그> 댓글 추천
	 * 파라미터 id : 추천하는 회원의 아이디
	 * 파라미터 blogCommentIdx : 추천할 블로그 댓글 idx
	 */
	@Override
	public void insertBlogCommentGood(String id, int blogCommentIdx) {
		tiDao.insertBlogCommentGood(id, blogCommentIdx);
	}

	/**
	 * getCommentN(int blogIdx) : <블로그> 댓글 추천 수
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 댓글 목록 추천 수 리스트(댓글 idx, 추천수)
	 */
	@Override
	public List<Map<String, Object>> getCommentN(int blogIdx) {
		return tiDao.getBlogCommentN(blogIdx);
	}

	/**
	 * insertBlogComment(int blogIdx, String id, String content) : <블로그> 댓글 작성
	 * 파라미터 blogIdx : 작성할 블로그 idx
	 * 파라미터 id : 댓글 작성자 아이디
	 * 파라미터 content : 댓글 작성 내용
	 * 리턴값 : 생성된 댓글 idx
	 */
	@Override
	public int insertBlogComment(int blogIdx, String id, String content) {
		return tiDao.insertBlogComment(blogIdx, id, content);
	}

	/**
	 * deleteBlogComment(int blogCommentIdx) : <블로그> 댓글 삭제
	 * 파라미터 blogCommentIdx : 삭제할 블로그 댓글 idx
	 */
	@Override
	public void deleteBlogComment(int blogCommentIdx) {
		tiDao.deleteBlogComment(blogCommentIdx);
	}

	/**
	 * updateBlogComment(String content, int blogCommentIdx) : <블로그> 댓글 수정
	 * 파라미터 content : 수정한 댓글 내용
	 * 파라미터 blogCommentIdx : 수정할 블로그 댓글 idx
	 */
	@Override
	public void updateBlogComment(String content, int blogCommentIdx) {
		tiDao.updateBlogComment(content, blogCommentIdx);
	}

	
	
	/**
	 * getAirportList() : <항공편> 공항 목록
	 * 리턴값 : 공항 정보 dto 리스트
	 */
	@Override
	public List<AirportListDto> getAirportList() {
		return tiDao.selectAirportList();
	}

	/** getFlightList() : 항공편 목록 조회
	 * 	파라미터 departure : 출발지 (ex. ICN)
	 *  파라미터 destination : 도착지
	 *  파라미터 startDate : 출발 날짜 (ex. 2026-01-20)
	 *  파라미터 nop : number of people(인원수)
	 *  리턴값 : 해당 날짜에 맞는 항공편 목록 dto 리스트
	 */
	@Override
	public List<FlightDto> getFlightList(String departure, String destination, String startDate, int nop) {
		return ApiAirline.getFlightList(tiDao, departure, destination, startDate, nop);
	}

	/**
	 * getAirlineLogoById(String airlineId) : <항공편> 항공사 아이디로 항공사 로고 이미지 찾기
	 * 파라미터 airlineId : 조회할 항공사 아이디
	 * 리턴값 : DB에서 조회된 항공사 로고 이미지
	 */
	@Override
	public String getAirlineLogoById(String airlineId) {
		Map<String,String> mapLogoName = tiDao.getAirlineLogoNameById(airlineId);
		return mapLogoName.get("airline_logo");
	}

	/**
	 * getAirlineNameById(String airlineId) : <항공편> 항공사 아이디로 항공사 이름 찾기
	 * 파라미터 airlineId : 조회할 항공사 아이디
	 * 리턴값 : DB에서 조회된 항공사 이름
	 */
	@Override
	public String getAirlineNameById(String airlineId) {
		Map<String,String> mapLogoName = tiDao.getAirlineLogoNameById(airlineId);
		return mapLogoName.get("airline_name");
	}

	
	
	/**
	 * getMyAiPack(String id) : <AI 짐싸기> 내 AI 짐싸기 기존 정보 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 AI 짐싸기 기존 정보 dto
	 */
	@Override
	public MyAiPackDto getMyAiPack(String id) {
		return tiDao.selectMyAiPackList(id);
	}

	

	/**
	 * deleteMyCityReview(int recommendReviewIdx) : <내 리뷰> 내 도시 리뷰 삭제
	 * 파라미터 recommendReviewIdx : 삭제할 도시 리뷰 idx
	 */
	@Override
	public void deleteMyCityReview(int recommendReviewIdx) {
		tiDao.deleteMyCityReview(recommendReviewIdx);
	}

	

	/**
	 * getCityImgByIdx(int cityIdx) : <AI 추천일정> 도시 idx로 도시 이미지 찾기
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : DB에서 조회된 해당 도시의 이미지
	 */
	@Override
	public String getCityImgByIdx(int cityIdx) {
		return tiDao.selectCityImgByIdx(cityIdx);
	}

	/**
	 * getAiRecommendPlaceList(String id) : <AI 추천일정> AI 추천 일정 장소 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 AI 추천 일정 장소 정보 dto 리스트
	 */
	@Override
	public List<AiRecommendPlaceDto> getAiRecommendPlaceList(String id) {
		return tiDao.selectAiRecommendPlaceList(id);
	}

	/**
	 * getPlaceListByCityName(String cityName) : <AI 추천일정> Chat GPT API에게 줄 해당 도시별 장소 목록
	 * 파라미터 cityName : 조회할 도시명
	 * 리턴값 : 해당 도시의 장소 목록 리스트(장소 idx, 장소명)
	 */
	@Override
	public List<Map<String, Object>> getPlaceListByCityName(String cityName) {
		return tiDao.selectPlaceListByCityName(cityName);
	}
}