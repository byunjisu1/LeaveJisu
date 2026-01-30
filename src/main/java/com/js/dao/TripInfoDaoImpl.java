package com.js.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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

@Repository
public class TripInfoDaoImpl implements TripInfoDao {
	@Autowired
	SqlSession sqlSession;
	
	/**
	 * selectCityList() : <추천 일정> 도시 리스트
	 * 리턴값 : 도시 정보 dto 리스트(도시명, 도시 사진)
	 */
	@Override
	public List<CityListDto> selectCityList() {
		return sqlSession.selectList("TripInfoMapper.getCityList");
	}
	
	/**
	 * selectConceptList(int cityIdx) : <추천 일정> 일정 컨셉 내용 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 해당 도시 추천 일정 컨셉 내용 dto 리스트(컨셉, 일차, 내용, 장소 리스트)
	 */
	@Override
	public List<RecommendPlanDetailDto> selectConceptList(int cityIdx) {
		return sqlSession.selectList("TripInfoMapper.getConcept", cityIdx);
	}

	/**
	 * selectRecommendPlaceList(int recommendPlanIdx) : <추천 일정> 일정 장소 리스트
	 * 파라미터 recommendPlanIdx : 추천 일정 idx
	 * 리턴값 : 장소 정보 dto 리스트
	 */
	@Override
	public List<RecommendPlanPlaceDto> selectRecommendPlaceList(int recommendPlanIdx) {
		return sqlSession.selectList("TripInfoMapper.getRecommendPlaceList", recommendPlanIdx);
	}

	/**
	 * selectTop10(int cityIdx) : <추천 일정> TOP 10 장소 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 해당 도시의 추천 장소 10개 리스트
	 */
	@Override
	public List<PlaceDto> selectTop10(int cityIdx) {
		return sqlSession.selectList("TripInfoMapper.getRecommendPlaceTop10", cityIdx);
	}
	
	/**
	 * selectCityNameByIdx(int cityIdx) : <추천 일정 전체> 도시 idx로 도시명 찾기
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 조회된 도시명
	 */
	@Override
	public String selectCityNameByIdx(int cityIdx) {
		return sqlSession.selectOne("TripInfoMapper.getCityNameByIdx", cityIdx);
	}

	/**
	 * selectRecommendReviewList(int cityIdx, String id, String orderBy) : <추천 일정 전체> 도시 리뷰 리스트
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 파라미터 id : 리뷰 좋아요 여부
	 * 파라미터 orderBy : 정렬 방법(order:null(="추천순") 또는 "최신순")
	 * 리턴값 : 해당 도시 리뷰 목록 리스트
	 */
	@Override
	public List<ReviewListDto> selectRecommendReviewList(int cityIdx, String id, String orderBy) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("cityIdx", cityIdx);
		map1.put("id", id);
		if(orderBy==null || orderBy.equals("추천순")) {
			return sqlSession.selectList("TripInfoMapper.getRecommendPlanReview", map1);
		}
		// "최신순"인 경우
		return sqlSession.selectList("TripInfoMapper.getRecommendPlanReviewOrderRecent", map1); 
	}
	
	/**
	 * selectRecommendReviewImgList(int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 사진 리스트
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : DB에서 조회된 해당 리뷰의 사진 목록
	 */
	@Override
	public List<String> selectRecommendReviewImgList(int recommendReviewIdx) {
		return sqlSession.selectList("TripInfoMapper.getRecommendReviewImgList", recommendReviewIdx);
	}
	
	/**
	 * insertRecommendReview(int cityIdx, String id, String content, String tripDate) : <추천 일정 전체> 도시 리뷰 작성
	 * 파라미터 cityIdx : 추가할 도시 idx
	 * 파라미터 id : 리뷰 작성자 id
	 * 파라미터 content : 리뷰 작성 내용
	 * 파라미터 tripDate : 여행 다녀온 날짜
	 * 리턴값 : 새로 생성된 도시 리뷰 idx
	 */
	@Override
	public int insertRecommendReview(int cityIdx, String id, String content, String tripDate) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("cityIdx", cityIdx);
		map1.put("id", id);
		map1.put("content", content);
		map1.put("tripDate", tripDate);
		sqlSession.insert("TripInfoMapper.insertRecommendReview", map1);
		return (int)map1.get("recommendReviewIdx");
	}

	/**
	 * insertRecommendReviewPic(int recommendReviewIdx, String reviewImg) : <추천 일정 전체> 도시 리뷰 사진 추가
	 * 파라미터 recommendReviewIdx : 추가할 도시 리뷰 idx
	 * 파라미터 reviewImg : 추가할 리뷰 사진
	 */
	@Override
	public void insertRecommendReviewPic(int recommendReviewIdx, String reviewImg) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("recommendReviewIdx", recommendReviewIdx);
		map1.put("reviewImg", reviewImg);
		sqlSession.insert("TripInfoMapper.insertRecommendReviewPic", map1);   
	}

	/**
	 * deleteRecommendPlanReviewGood(String id, int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천 취소
	 * 파라미터 id : 해당 회원 아이디
	 * 파라미터 recommendReviewIdx : 취소할 리뷰 idx
	 */
	@Override
	public void deleteRecommendPlanReviewGood(String id, int recommendReviewIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("recommendReviewIdx", recommendReviewIdx);
		sqlSession.delete("TripInfoMapper.deleteRecommendPlanReviewGood", map1);
	}

	/**
	 * insertRecommendPlanReviewGood(String id, int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천
	 * 파라미터 id : 해당 회원 아이디
	 * 파라미터 recommendReviewIdx : 추천할 리뷰 idx
	 */
	@Override
	public void insertRecommendPlanReviewGood(String id, int recommendReviewIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("recommendReviewIdx", recommendReviewIdx);
		sqlSession.insert("TripInfoMapper.insertRecommendPlanReviewGood", map1);
	}

	/**
	 * getRecommendReviewGoodN(int recommendReviewIdx) : <추천 일정 전체> 도시 리뷰 추천 수
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : 해당 리뷰의 추천 수
	 */
	@Override
	public int getRecommendReviewGoodN(int recommendReviewIdx) {
		return sqlSession.selectOne("TripInfoMapper.getRecommendReviewCommentN", recommendReviewIdx);
	}

	/**
	 * selectBlogList() : <추천 일정> 블로그 리스트
	 * 리턴값 : 블로그 목록 리스트
	 */
	@Override
	public List<BlogListDto> selectBlogList() {
		return sqlSession.selectList("TripInfoMapper.getBlogList");
	}

	/**
	 * selectBlogImgList(int blogIdx) : <추천 일정> 블로그 이미지 리스트
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 이미지 리스트
	 */
	@Override
	public List<String> selectBlogImgList(int blogIdx) {
		return sqlSession.selectList("TripInfoMapper.getBlogImg", blogIdx);
	}
	
	
	
	/**
	 * selectBlogDetail(int blogIdx) : <블로그> 블로그 정보 조회
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 블로그 정보 dto(작성자 정보, 제목, 소개 내용, 이미지, 장소 리스트)
	 */
	@Override
	public BlogDetailDto selectBlogDetail(int blogIdx) {
		return sqlSession.selectOne("TripInfoMapper.getBlogDetail", blogIdx);
	}

	/**
	 * selectBlogDetailPlace(int blogIdx) : <블로그> 블로그 장소 목록
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 장소 정보 dto 리스트
	 */
	@Override
	public List<BlogDetailPlaceDto> selectBlogDetailPlace(int blogIdx) {
		return sqlSession.selectList("TripInfoMapper.getBlogDetailPlace", blogIdx);
	}

	/**
	 * selectPlanCourse(int blogIdx) : <블로그> 여행 코스 목록
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 여행 코스 dto 리스트
	 */
	@Override
	public List<PlanCourseDto> selectPlanCourse(int blogIdx) {
		return sqlSession.selectList("TripInfoMapper.getPlanCourse", blogIdx);
	}

	/**
	 * selectBlogPlaceMap(int blogIdx) : <블로그> 여행 코스 장소 위치 리스트
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 여행 장소 위치 dto 리스트
	 */
	@Override
	public List<BlogPlaceMapDto> selectBlogPlaceMap(int blogIdx) {
		return sqlSession.selectList("TripInfoMapper.getBlogPlaceMap", blogIdx);
	}
	
	/**
	 * selectBlogCommentList(String id, int blogIdx) : <블로그> 블로그 댓글 목록
	 * 파라미터 id : 댓글 추천 여부
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 댓글 목록 dto 리스트
	 */
	@Override
	public List<BlogCommentListDto> selectBlogCommentList(String id, int blogIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("blogIdx", blogIdx);
		return sqlSession.selectList("TripInfoMapper.getBlogCommentList", map1);
	}
	
	/**
	 * selectBlogCommentByIdx(String id, int blogCommentIdx) : <블로그> 블로그 댓글 1개 정보
	 * 파라미터 id : 해당 회원의 댓글 추천 여부
	 * 파라미터 blogCommentIdx : 조회할 블로그 댓글 idx
	 * 리턴값 : 해당 idx의 댓글 정보 조회
	 */
	@Override
	public BlogCommentListDto selectBlogCommentByIdx(String id, int blogCommentIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("blogCommentIdx", blogCommentIdx);
		return sqlSession.selectOne("TripInfoMapper.getBlogCommentByIdx", map1);	
	}

	/**
	 * deleteBlogCommentGood(String id, int blogCommentIdx) : <블로그> 댓글 추천 취소
	 * 파라미터 id : 추천 취소하는 회원의 아이디
	 * 파라미터 blogCommentIdx : 추천 취소할 블로그 댓글 idx
	 */
	@Override
	public void deleteBlogCommentGood(String id, int blogCommentIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("blogCommentIdx", blogCommentIdx);
		sqlSession.delete("TripInfoMapper.deleteBlogCommentGood", map1);
	}

	/**
	 * insertBlogCommentGood(String id, int blogCommentIdx) : <블로그> 댓글 추천
	 * 파라미터 id : 추천하는 회원의 아이디
	 * 파라미터 blogCommentIdx : 추천할 블로그 댓글 idx
	 */
	@Override
	public void insertBlogCommentGood(String id, int blogCommentIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("blogCommentIdx", blogCommentIdx);
		sqlSession.insert("TripInfoMapper.insertBlogCommentGood", map1);
	}

	/**
	 * getBlogCommentN(int blogIdx) : <블로그> 댓글 추천 수
	 * 파라미터 blogIdx : 조회할 블로그 idx
	 * 리턴값 : 해당 블로그의 댓글 별 추천 수 리스트
	 */
	@Override
	public List<Map<String, Object>> getBlogCommentN(int blogIdx) {
		return sqlSession.selectList("TripInfoMapper.getBlogCommentN", blogIdx);
	}

	/**
	 * insertBlogComment(int blogIdx, String id, String content) : <블로그> 댓글 작성
	 * 파라미터 blogIdx : 댓글 작성할 블로그 idx
	 * 파라미터 id : 댓글 작성하는 회원의 아이디
	 * 파라미터 content : 댓글 내용
	 * 리턴값 : 생성된 블로그 댓글 idx
	 */
	@Override
	public int insertBlogComment(int blogIdx, String id, String content) {
		BlogCommentListDto dto = new BlogCommentListDto();
		dto.setId(id);
		dto.setBlogIdx(blogIdx);
		dto.setContent(content);
		sqlSession.insert("TripInfoMapper.insertBlogComment", dto);
		int generatedBlogCommentIdx = dto.getBlogCommentIdx();
		return generatedBlogCommentIdx;
	}

	/**
	 * deleteBlogComment(int blogCommentIdx) : <블로그> 댓글 삭제
	 * 파라미터 blogCommentIdx : 삭제할 블로그 댓글 idx
	 */
	@Override
	public void deleteBlogComment(int blogCommentIdx) {
		sqlSession.insert("TripInfoMapper.deleteBlogComment", blogCommentIdx);
	}

	/**
	 * updateBlogComment(String content, int blogCommentIdx) : <블로그> 댓글 수정
	 * 파라미터 content : 수정한 댓글 내용
	 * 파라미터 blogCommentIdx : 수정할 블로그 댓글 idx
	 */
	@Override
	public void updateBlogComment(String content, int blogCommentIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("content", content);
		map1.put("blogCommentIdx", blogCommentIdx);
		sqlSession.insert("TripInfoMapper.updateBlogComment", map1);
	}

	
	
	/**
	 * selectAirportList() : <항공편> 공항 목록
	 * 리턴값 : 공항 정보 dto 리스트
	 */
	@Override
	public List<AirportListDto> selectAirportList() {
		return sqlSession.selectList("TripInfoMapper.getAirportList");
	}

	/**
	 * selectAirlineIdList() : <항공편> 항공사 목록
	 * 리턴값 : 항공사 정보 리스트
	 */
	@Override
	public List<String> selectAirlineIdList() {
		return sqlSession.selectList("TripInfoMapper.getAirlineIdList");
	}

	/**
	 * setAirlineLogo(String id, String imgUrl) : <항공편> 항공사 로고 이미지 업데이트
	 * 파라미터 id : 항공사 아이디
	 * 파라미터 imgUrl : 항공사 로고 이미지
	 */
	@Override
	public void setAirlineLogo(String id, String imgUrl) {
		Map<String, String> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("imgUrl", imgUrl);
		sqlSession.update("TripInfoMapper.setAirlineLogo", map1);
	}

	/**
	 * getAirlineLogoNameById(String airlineId) : <항공편> 항공사 아이디로 로고 이미지, 이름 찾기
	 * 파라미터 airlineId : 조회할 항공사 아이디
	 * 리턴값 : 해당 항공사의 로고 이미지, 항공사 이름
	 */
	@Override
	public Map<String, String> getAirlineLogoNameById(String airlineId) {
		return sqlSession.selectOne("TripInfoMapper.getAirlineLogoNameById", airlineId);
	}

	

	/**
	 * selectMyAiPackList(String id) : <AI 짐싸기> 내 AI 짐싸기 기존 정보 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : DB에서 조회된 AI 짐싸기 기존 정보 dto(사진, 현재 짐 목록, 잊은 짐 목록, 조언)
	 */
	@Override
	public MyAiPackDto selectMyAiPackList(String id) {
		return sqlSession.selectOne("TripInfoMapper.getMyAiPack", id);
	}

	

	/**
	 * selectCityImgByIdx(int cityIdx) : <AI 추천일정> 도시 idx로 도시 이미지 찾기
	 * 파라미터 cityIdx : 조회할 도시 idx
	 * 리턴값 : 해당 도시의 이미지
	 */
	@Override
	public String selectCityImgByIdx(int cityIdx) {
		return sqlSession.selectOne("TripInfoMapper.getCityImgByIdx", cityIdx);
	}
	
	/**
	 * selectAiRecommendPlaceList(String id) : <AI 추천일정> AI 추천 일정 장소 리스트 조회
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : AI 추천 일정 장소 정보 dto 리스트
	 */
	@Override
	public List<AiRecommendPlaceDto> selectAiRecommendPlaceList(String id) {
		return sqlSession.selectList("TripInfoMapper.getMyAiRecommendPlace", id);
	}

	/**
	 * selectPlaceListByCityName(String cityName) : <AI 추천일정> Chat GPT API에게 줄 해당 도시별 장소 목록
	 * 파라미터 cityName : 조회할 도시명
	 * 리턴값 : DB에서 조회된 해당 도시의 장소 리스트(장소 idx, 장소명)
	 */
	@Override
	public List<Map<String, Object>> selectPlaceListByCityName(String cityName) {
		return sqlSession.selectList("TripInfoMapper.getMyDBPlaceList", cityName);
	}

	/**
	 * selectAllPlaceList() : <AI 추천일정> Chat GPT API에게 줄 전체 장소 목록
	 * 리턴값 : DB에서 조회된 모든 장소 리스트(장소 idx, 장소명)
	 */
	@Override
	public List<Map<String, Object>> selectAllPlaceList() {
		return sqlSession.selectList("TripInfoMapper.getAllMyDBPlaceList");
	}

	/**
	 * deleteAIRecommendPlaceListByMemberId(String id) : <AI 추천일정> 해당 아이디의 AI 추천 일정 정보 삭제
	 * 파라미터 id : 삭제할 회원의 아이디
	 */
	@Override
	public void deleteAIRecommendPlaceListByMemberId(String id) {
		sqlSession.delete("TripInfoMapper.deleteAIRecommendPlaceList", id);
	}

	/**
	 * insertAIRecommendPlace(String id, int day, int placeOrder, int placeIdx) : <AI 추천일정> Chat GPT API 추천 일정 정보 추가
	 * 파라미터 id : 추가할 회원의 아이디
	 * 파라미터 day : 추가할 일차
	 * 파라미터 placeOrder : 추가할 장소 순서
	 * 파라미터 placeIdx : 추가할 장소 idx
	 */
	@Override
	public void insertAIRecommendPlace(String id, int day, int placeOrder, int placeIdx) {
		Map<String, Object> map1 = new HashMap<>();
		map1.put("id", id);
		map1.put("day", day);
		map1.put("placeOrder", placeOrder);
		map1.put("placeIdx", placeIdx);
		sqlSession.insert("TripInfoMapper.insertAIRecommendPlace", map1);
	}

	/**
	 * selectClickHistoryOneWeekByMemberId(String id) : <AI 추천일정> 해당 아이디의 최근 일주일 간 클릭 이력
	 * 파라미터 id : 조회할 회원의 아이디
	 * 리턴값 : 해당 회원의 최근 일주일 간 클릭 이력 내역 리스트(장소 idx, 클릭 횟수)
	 */
	@Override
	public List<Map<String, Object>> selectClickHistoryOneWeekByMemberId(String id) {
		return sqlSession.selectList("TripInfoMapper.getClickHistoryLastOneWeekByMemberId", id);
	}
	
	
	
	/**
	 * selectWriterIdByIdx(int recommendReviewIdx) : <내 리뷰> 도시 리뷰 idx로 작성자 아이디 찾기
	 * 파라미터 recommendReviewIdx : 조회할 도시 리뷰 idx
	 * 리턴값 : 해당 리뷰의 작성자 아이디
	 */
	@Override
	public String selectWriterIdByIdx(int recommendReviewIdx) {
		return sqlSession.selectOne("TripInfoMapper.getWriterIdByRecommendReviewIdx", recommendReviewIdx);
	}
	
	/**
	 * deleteMyCityReview(int recommendReviewIdx) : <내 리뷰> 내 도시 리뷰 삭제
	 * 파라미터 recommendReviewIdx : 삭제할 도시 리뷰 idx
	 */
	@Override
	public void deleteMyCityReview(int recommendReviewIdx) {
		sqlSession.delete("TripInfoMapper.deleteMyCityReview", recommendReviewIdx);
	}
}
