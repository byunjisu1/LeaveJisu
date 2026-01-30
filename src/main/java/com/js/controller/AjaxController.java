package com.js.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.js.dto.BlogCommentListDto;
import com.js.dto.FlightDto;
import com.js.dto.InsertPlaceMemoDto;
import com.js.dto.MemberDto;
import com.js.dto.ReviewListDto;
import com.js.dto.UpdateMyPlaceDto;
import com.js.service.MemberService;
import com.js.service.MyTripService;
import com.js.service.TripInfoService;
import com.js.util.MailSender;

@RestController
public class AjaxController {
	@Autowired
	MemberService mSvc;
	@Autowired
	MyTripService mtSvc;
	@Autowired
	TripInfoService tiSvc;
	
	// return : 사용가능이면 (result:true), 중복이면 (result:false)
	@RequestMapping("/nickname_check")
	public Map<String, Boolean> nicknameCheck(@RequestBody Map<String, String> reqMap) {
		Map<String, Boolean> retMap = new HashMap<>();
		
		String nickname = reqMap.get("nickname");
		int count = mSvc.getNicknameCount(nickname);
		
		if(count == 0) {
			retMap.put("result", true);  //"사용 가능한 닉네임입니다.");
		} else {
			retMap.put("result", false);  //"중복된 닉네임입니다. \n다시 입력하세요.");
		}
		
		return retMap;
	}
	
	// return : 사용가능이면 (result:true), 중복이면 (result:false)
	@RequestMapping("/id_check")
	public Map<String, Boolean> idCheck(@RequestBody Map<String, String> reqMap) {
		Map<String, Boolean> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		int count = mSvc.getIdCount(id);
		
		if(count == 0) {
			retMap.put("result", true);  //"사용 가능한 아이디입니다.");
		} else {
			retMap.put("result", false);  //"중복된 닉네임입니다. \n다시 입력하세요.");
		}
		
		return retMap;
	}
	
	// return : 전송완료 (result:true)
	@RequestMapping("/send_email")
	public Map<String, Boolean> sendEmail(@RequestBody Map<String, String> reqMap, HttpSession session) {
		Map<String, Boolean> retMap = new HashMap<>();
		
		String email = reqMap.get("email");
		
		String authCode = "";
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < 5; i++) {
	        int index = random.nextInt(charSet.length());
	        sb.append(charSet.charAt(index));
	    }
	    authCode = sb.toString();
	    
	    MailSender.sendMail(email, "떠나지수 회원가입 인증번호", "<span style='font-weight: bold;'>떠나지수 이메일 인증번호 : </span>" + authCode);
	    session.setAttribute("authCode", authCode);
	    
	    retMap.put("result", true);
		
		return retMap;
	}
	
	// return : 인증번호가 일치하면 (result:true), 일치하지 않으면 (result:false)
	@RequestMapping("/code_check")
	public Map<String, Boolean> codeCheck(@RequestBody Map<String, String> reqMap, HttpSession session) {
		Map<String, Boolean> retMap = new HashMap<>();
		
		String code = reqMap.get("code");
		String authCode = (String)session.getAttribute("authCode");
		
		String nickname = reqMap.get("nickname");
		String id = reqMap.get("id");
		String pw = reqMap.get("pw");
		String email = reqMap.get("email");
		
		if(code.equals(authCode)) {
			mSvc.insertMember(new MemberDto(id, pw, nickname, email, null));
			session.setAttribute("loginId", id);
			
			retMap.put("result", true);  // 회원가입 완료
		} else {
			retMap.put("result", false);  // 인증번호 불일치
		}
		
		return retMap;
	}
	
	// <내 여행> 상세 페이지 속 장소 메모 추가
	@RequestMapping("/insert_memo")
	public int insertMemo(@RequestBody Map<String, String> reqMap) {
		String memo = reqMap.get("memo");
		int planPlaceIdx = Integer.parseInt(reqMap.get("planPlaceIdx"));
		
		InsertPlaceMemoDto dto = new InsertPlaceMemoDto(0, planPlaceIdx, memo);
		
		int memoIdx = mtSvc.insertPlaceMemo(dto);
		
		return memoIdx;
	}
	
	// <내 여행> 상세 페이지 속 장소 메모 삭제
	@RequestMapping("/delete_memo")
	public Map<String, String> deleteMemo(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int memoIdx = Integer.parseInt(reqMap.get("memoIdx"));
		
		mtSvc.deletePlaceMemo(memoIdx);
		
		return retMap;
	}
	
	// <내 여행> 상세 페이지 속 장소 삭제
	@RequestMapping("/delete_plan_place")
	public Map<String, String> deletePlanPlace(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int planPlaceIdx = Integer.parseInt(reqMap.get("planPlaceIdx"));
		
		mtSvc.deletePlanPlace(planPlaceIdx);
		
		return retMap;
	}
	
	// <내 저장> 속 장소 메모 업데이트
	@RequestMapping("/update_place_memo")
	public Map<String, String> updatePlaceMemo(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		int placeIdx = Integer.parseInt(reqMap.get("placeIdx"));
		String memo = reqMap.get("memo");
		
		UpdateMyPlaceDto dto = new UpdateMyPlaceDto(id, placeIdx, null, memo);
		
		mtSvc.updateMyPlaceMemo(dto);
		
		return retMap;
	}
	
	// <내 저장, 장소 상세> 속 장소 찜 삭제
	@RequestMapping("/delete_myplace")
	public Map<String, String> deleteMyPlace(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		int placeIdx = Integer.parseInt(reqMap.get("placeIdx"));
	
		mtSvc.deleteMyPlace(id, placeIdx);
		
		return retMap;
	}
	
	// <장소 상세 페이지> 속 장소 찜 추가
	@RequestMapping("/insert_myplace")
	public Map<String, String> insertMyPlace(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		int placeIdx = Integer.parseInt(reqMap.get("placeIdx"));
	
		mtSvc.insertMyPlace(id, placeIdx);
		
		return retMap;
	}
	
	// <내 여행> 속 다가오는 여행 날짜 업데이트
	@RequestMapping("/update_plan_date")
	public Map<String, String> updatePlanDate(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int planIdx = Integer.parseInt(reqMap.get("planIdx"));
		String startDate = reqMap.get("startDate");
		int days = Integer.parseInt(reqMap.get("days"));
		
		mtSvc.updatePlanDate(planIdx, startDate, days);
		
		return retMap;
	}
	
	// <내 여행> 속 다가오는 여행 삭제
	@RequestMapping("/delete_plan")
	public Map<String, String> deletePlan(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int planIdx = Integer.parseInt(reqMap.get("planIdx"));
		
		mtSvc.deletePlan(planIdx);
		
		return retMap;
	}
	
	// <블로그> 댓글 추천 삭제
	@RequestMapping("/delete_comment_good")
	public Map<String, Object> deleteCommentGood(@RequestBody Map<String, String> reqMap, HttpSession session) {
		Map<String, Object> retMap = new HashMap<>();
		
		int blogCommentIdx = Integer.parseInt(reqMap.get("blogCommentIdx"));
		int blogIdx = Integer.parseInt(reqMap.get("blogIdx"));
		String id = (String)session.getAttribute("loginId");
		
		tiSvc.deleteBlogCommentGood(id, blogCommentIdx);
		
		List<Map<String, Object>> listComment = tiSvc.getCommentN(blogIdx);
		retMap.put("commentGoodN", 0);	// 초기값
		for(Map<String, Object> map : listComment) {
			Integer currentIdx = Integer.parseInt(String.valueOf(map.get("BLOGCOMMENTIDX")));
			if(currentIdx == blogCommentIdx) {
				int goodN = Integer.parseInt(String.valueOf(map.get("COMMENTGOODN")));
				System.out.println(goodN);
				retMap.put("commentGoodN", goodN);
				break;
			}
		}
		
		return retMap;
	}
	
	// <블로그> 댓글 추천
	@RequestMapping("/insert_comment_good")
	public Map<String, Object> insertCommentGood(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		int blogCommentIdx = Integer.parseInt(reqMap.get("blogCommentIdx"));
		int blogIdx = Integer.parseInt(reqMap.get("blogIdx"));
		
		tiSvc.insertBlogCommentGood(id, blogCommentIdx);
		
		List<Map<String, Object>> listComment = tiSvc.getCommentN(blogIdx);
		retMap.put("commentGoodN", 0);	// 초기값
		for(Map<String, Object> map : listComment) {
			Integer currentIdx = Integer.parseInt(String.valueOf(map.get("BLOGCOMMENTIDX")));
			if(currentIdx == blogCommentIdx) {
				int goodN = Integer.parseInt(String.valueOf(map.get("COMMENTGOODN")));
				System.out.println(goodN);
				retMap.put("commentGoodN", goodN);
				break;
			}
		}
		
		return retMap;
	}
	
	// <블로그> 댓글 추가
	@RequestMapping("/insert_blog_comment")
	public Map<String, Object> insertBlogComment(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		int blogIdx = Integer.parseInt(reqMap.get("blogIdx"));
		String id = reqMap.get("id");
		String content = reqMap.get("content");
		
		int blogCommentIdx = tiSvc.insertBlogComment(blogIdx, id, content);
		
		BlogCommentListDto dto = tiSvc.getBlogCommentByIdx(id, blogCommentIdx);
		
		retMap.put("dto", dto);
		
		return retMap;
	}
	
	// <블로그> 댓글 삭제
	@RequestMapping("/delete_blog_comment")
	public Map<String, String> deleteBlogComment(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int blogCommentIdx = Integer.parseInt(reqMap.get("blogCommentIdx"));
		
		tiSvc.deleteBlogComment(blogCommentIdx);
		
		return retMap;
	}
	
	// <블로그> 댓글 수정
	@RequestMapping("/update_blog_comment")
	public Map<String, String> updateBlogComment(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		String content = reqMap.get("content");
		int blogCommentIdx = Integer.parseInt(reqMap.get("blogCommentIdx"));
		
		tiSvc.updateBlogComment(content, blogCommentIdx);
		
		return retMap;
	}
	
	// <추천일정> 리뷰 정렬
	@RequestMapping("/recommend_review_sort")
	public List<ReviewListDto> recommentReviewSort(@RequestBody Map<String, String> reqMap, HttpSession session) {
		String id = (String)session.getAttribute("loginId");
		
		int cityIdx = Integer.parseInt(reqMap.get("cityIdx"));
		String sort = reqMap.get("sort");  // "추천순" 또는 "최신순"
		
		return tiSvc.getRecommendReviewList(cityIdx, id, sort);
	}
	
	// 도시 리뷰 추천/삭제 토글
	@RequestMapping("/toggle_city_review_good")
	public int toggleCityReviewGood(@RequestBody Map<String, String> reqMap) {
		String id = reqMap.get("id");
		String action = reqMap.get("action");
		int reviewIdx = Integer.parseInt(reqMap.get("reviewIdx"));
		
		if("on".equals(action)) {
			tiSvc.insertRecommendPlanReviewGood(id, reviewIdx);
		} else {
			tiSvc.deleteRecommendPlanReviewGood(id, reviewIdx);
		}
		
		return tiSvc.getRecommendReviewGoodN(reviewIdx);
	}
	
	// 장소 리뷰 추천/삭제 토글
	@RequestMapping("/toggle_place_review_good")
	public int togglePlaceReviewGood(@RequestBody Map<String, String> reqMap) {
		String id = reqMap.get("id");
		String action = reqMap.get("action");
		int reviewIdx = Integer.parseInt(reqMap.get("reviewIdx"));
		
		if("on".equals(action)) {
			mtSvc.insertPlaceReviewGood(id, reviewIdx); // <장소 상세 페이지> 리뷰 추천
		} else {
			mtSvc.deletePlaceReviewGood(id, reviewIdx); // <장소 상세 페이지> 리뷰 추천 삭제
		}
		
		return mtSvc.getPlaceReviewGoodN(reviewIdx);
	}
	
	// <내 리뷰> 속 리뷰 삭제 (장소, 도시)
	@RequestMapping("/delete_review")
	public Map<String, String> deleteReview(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int reviewIdx = Integer.parseInt(reqMap.get("reviewIdx"));
		String idxType = reqMap.get("idxType");
		
		if(idxType.equals("p")) {
			mtSvc.deleteMyReview(reviewIdx);
		} else {
			tiSvc.deleteMyCityReview(reviewIdx);
		}
		
		return retMap;
	}
	
	// 추천일정 일정 담기
	@RequestMapping("/insert_plan")
	public Map<String, Object> insertPlan(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		String id = (String)reqMap.get("id");
		String startDate = (String)reqMap.get("startDate");
		System.out.println(startDate);
		int days = (int)reqMap.get("days");
		int cityIdx = 0;
		
		String city = (String)reqMap.get("city");
		if(city != null) {
			cityIdx = mtSvc.getCityIdxByName(city);
		} else if(reqMap.get("cityIdx") != null) {
			cityIdx = Integer.parseInt((String)reqMap.get("cityIdx"));
		}
		
		List<Map<String, Object>> placeList = (List<Map<String, Object>>) reqMap.get("placeList");
		
		int planIdx = mtSvc.insertPlan(id, startDate, days);
		mtSvc.insertPlanCity(planIdx, 1, cityIdx);
		
		for(Map<String, Object> place : placeList) {
			int day = (int)place.get("day");
			int placeIdx = (int)place.get("placeIdx");
			mtSvc.insertPlanPlace(planIdx, day, placeIdx);
		}
		
		return retMap;
	}
	
	// 블로그 일정 담기
	@RequestMapping("/insert_blog_plan")
	public Map<String, Object> insertBlogPlan(@RequestBody Map<String, Object> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		String id = (String)reqMap.get("id");
		String startDate = (String)reqMap.get("startDate");
		int days = (int)reqMap.get("days");
		
		List<Map<String, Object>> placeList = (List<Map<String, Object>>) reqMap.get("placeList");
		
		int planIdx = mtSvc.insertPlan(id, startDate, days);
		
		HashSet<Integer> hset = new HashSet<>();
		for(Map<String, Object> place : placeList) {
			int day = (int)place.get("day");
			int placeIdx = (int)place.get("placeIdx");
			int cityIdx = (int)place.get("cityIdx");
			hset.add(cityIdx);
		
			mtSvc.insertPlanPlace(planIdx, day, placeIdx);
		}
		int order = 1;
		for(int h : hset) {
			mtSvc.insertPlanCity(planIdx, order++, h);
		}
		
		return retMap;
	}
	

	// <장소 상세 페이지> day 팝업
	@RequestMapping("/upcomingtrip_lastDay")
	public int upcomingtripLastDay(@RequestBody Map<String, String> reqMap) {
		int planIdx = Integer.parseInt(reqMap.get("planIdx"));
		
		int lastDay = mtSvc.getLastDay(planIdx);
		
		return lastDay;
	}
	
	// <장소 상세 페이지> 일정 추가
	@RequestMapping("/insert_plan_place")
	public Map<String, Object> insertPlanPlace(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		int planIdx = Integer.parseInt(reqMap.get("planIdx"));
		int day = Integer.parseInt(reqMap.get("day"));
		int placeIdx = Integer.parseInt(reqMap.get("placeIdx"));
		
		mtSvc.insertPlanPlace(planIdx, day, placeIdx);
		
		return retMap;
	}
	
	// 출발 항공편 조회
	@RequestMapping("/departure_ticket")
	public List<FlightDto> departureTicket(@RequestBody Map<String, String> reqMap) {
		String departure = (String)reqMap.get("departure");
		String destination = (String)reqMap.get("destination");
		String departureDate = (String)reqMap.get("departureDate");
		int pNum = Integer.parseInt(reqMap.get("pNum"));

		List<FlightDto> listFlight = tiSvc.getFlightList(departure, destination, departureDate, pNum);
		
		return listFlight;
	}
	
	// 메뉴바 프로필 정보 수정
	@PostMapping("/update_profile")
	@ResponseBody
	public Map<String, Object> updateProfile(@RequestParam(value="nickname", required=false) String nickname,
	        @RequestParam(value="profile_img", required=false) MultipartFile file,
	        HttpSession session) throws IllegalStateException, IOException {
		Map<String, Object> retMap = new HashMap<>();
		
		String id = (String)session.getAttribute("loginId");
		
		// 업로드 받은 파일 저장.
		// 1. upload 폴더 지정 (폴더가 없으면 생성)
		String path = "D:/Temp/upload";
		
		File f = new File(path);	// 파일시스템 관련 처리 --> File 객체
		if(!f.exists()) f.mkdirs();
		
		String filename = "";

		// 2. 폴더 지정하고, 지정한 폴더에 저장 --> 파일 정보(ex. 파일명)을 얻음.
		if(file != null && !file.isEmpty()) {
			filename = file.getOriginalFilename();	// 원본파일명
			File savedFile = new File(path, filename);	// 스프링에서는, 'rename policy' 대신에 저장할 파일명을 직접 지정함.
			file.transferTo(savedFile);	// 실제로 파일을 서버에 저장함.
			
			System.out.println("서버에서 받은 파일(들) : " + file.getOriginalFilename());
			
			mSvc.updateProfile(nickname, filename, id);
		} else {
			mSvc.updateProfile(nickname, null, id);
		}
		
	    if(filename != null) {
	        retMap.put("newProfileUrl", "/upload/" + filename);
	    }
		
		return retMap;
	}
	
	// 알림 모두 삭제
	@RequestMapping("/delete_all_bells")
	public Map<String, Object> deleteAllMyBells(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		String id = reqMap.get("id");
		
		mtSvc.deleteAllMyBells(id);
		
		return retMap;
	}

	// 알림 하나 삭제
	@RequestMapping("/delete_bells")
	public Map<String, Object> deleteMyBell(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		int bellIdx = Integer.parseInt(reqMap.get("bellIdx"));
		
		mtSvc.deleteMyBell(bellIdx);
		
		return retMap;
	}
	
	// 도시 리뷰 추가
	@PostMapping("/insert_recommend_review")
	@ResponseBody
	public Map<String, String> insertRecommendReview(@RequestParam("cityIdx") int cityIdx,
		    @RequestParam("id") String id,
		    @RequestParam("content") String content,
		    @RequestParam("tripDate") String tripDate,
		    @RequestParam(value="review_imgs", required=false) List<MultipartFile> files) throws IllegalStateException, IOException {
		
		Map<String, String> mapRet = new HashMap<>();
		
		int recommendReviewIdx = tiSvc.insertRecommendReview(cityIdx, id, content, tripDate);
		
		// 업로드 받은 파일 저장.
		// 1. upload 폴더 지정 (폴더가 없으면 생성)
		String path = "D:/Temp/upload";
		
		File f = new File(path);	// 파일시스템 관련 처리 --> File 객체
		if(!f.exists())
			f.mkdirs();

		for(MultipartFile file : files) {
			// 2. 폴더 지정하고, 지정한 폴더에 저장 --> 파일 정보(ex. 파일명)을 얻음.
			if(!file.isEmpty()) {
				String filename = file.getOriginalFilename();	// 원본파일명
				File savedFile = new File(path, filename);	// 스프링에서는, 'rename policy' 대신에 저장할 파일명을 직접 지정함.
				
				file.transferTo(savedFile);	// 실제로 파일을 서버에 저장함.
				
				// System.out.println("서버에서 받은 파일(들) : " + file.getOriginalFilename());
				
				tiSvc.insertRecommendReviewPic(recommendReviewIdx, file.getOriginalFilename());
			}
		}
	
		return mapRet;
	}
	
	// 장소 리뷰 추가
	@PostMapping("/insert_place_review")
	@ResponseBody
	public Map<String, String> insertPlaceReview(@RequestParam("placeIdx") int placeIdx,
		    @RequestParam("id") String id,
		    @RequestParam("content") String content,
		    @RequestParam("star") int star,
		    @RequestParam("tripDate") String tripDate,
		    @RequestParam(value="review_imgs", required=false) List<MultipartFile> files) throws IllegalStateException, IOException {
		
		Map<String, String> mapRet = new HashMap<>();
		
		int reviewIdx = mtSvc.insertReview(placeIdx, id, content, star, tripDate);
		
		// 업로드 받은 파일 저장.
		// 1. upload 폴더 지정 (폴더가 없으면 생성)
		String path = "D:/Temp/upload";
		
		if (files == null) return mapRet;
		
		File f = new File(path);	// 파일시스템 관련 처리 --> File 객체
		if(!f.exists())
			f.mkdirs();

		for(MultipartFile file : files) {
			// 2. 폴더 지정하고, 지정한 폴더에 저장 --> 파일 정보(ex. 파일명)을 얻음.
			if(!file.isEmpty()) {
				String filename = file.getOriginalFilename();	// 원본파일명
				File savedFile = new File(path, filename);	// 스프링에서는, 'rename policy' 대신에 저장할 파일명을 직접 지정함.
				
				file.transferTo(savedFile);	// 실제로 파일을 서버에 저장함.
				
				System.out.println("서버에서 받은 파일(들) : " + file.getOriginalFilename());
				
				mtSvc.insertReviewPic(reviewIdx, file.getOriginalFilename());
			}
		}
	
		return mapRet;
	}
	
	// 별점 코멘트
	@RequestMapping("/get_star_comment")
	public Map<String, String> getStarComment(@RequestBody Map<String, String> reqMap) {
		Map<String, String> retMap = new HashMap<>();
		
		int star = Integer.parseInt(reqMap.get("star"));
		String comment = mtSvc.getStarComment(star);
		retMap.put("comment", comment);
		
		return retMap;
	}
	
	// 아이디 찾기
	@RequestMapping("/search_id")
	public Map<String, Object> searchId(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		String email = reqMap.get("email");
		
		String id = mSvc.getIdByEmail(email);
		
		if(id != null) {
			retMap.put("result", true);
			retMap.put("id", id);
		} else {
			retMap.put("result", false);
		}
		
		return retMap;
	}
	
	// 비밀번호 찾기
	@RequestMapping("/search_pw")
	public Map<String, Object> searchPw(@RequestBody Map<String, String> reqMap) {
		Map<String, Object> retMap = new HashMap<>();
		
		String id = reqMap.get("id");
		String email = reqMap.get("email");
		
		// 아이디와 이메일이 올바른지 체크
		if(!id.equals(mSvc.getIdByEmail(email))) {
			retMap.put("result", false);
			return retMap;
		}
		
		// 새 비밀번호 생성
		String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();
	    for (int i = 0; i < 5; i++) {
	        int index = random.nextInt(charSet.length());
	        sb.append(charSet.charAt(index));
	    }
	    String newPw = sb.toString();
	    
	    mSvc.updatePw(newPw, id);
	    MailSender.sendMail(email, "떠나지수 새로운 비밀번호", "<span style='font-weight: bold;'>떠나지수 새로운 비밀번호 : </span>" + newPw);
	    retMap.put("result", true);
		
		return retMap;
	}
}
