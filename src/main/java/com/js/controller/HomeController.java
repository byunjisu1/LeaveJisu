package com.js.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.js.dto.AddPlaceDto;
import com.js.dto.AiRecommendPlaceDto;
import com.js.dto.AirportListDto;
import com.js.dto.BlogCommentListDto;
import com.js.dto.BlogDetailDto;
import com.js.dto.BlogListDto;
import com.js.dto.BlogPlaceMapDto;
import com.js.dto.CityListDto;
import com.js.dto.MemberDto;
import com.js.dto.MenuBarProfileDto;
import com.js.dto.MyAiPackDto;
import com.js.dto.MyBellListDto;
import com.js.dto.MyPlaceListDto;
import com.js.dto.MyReviewListDto;
import com.js.dto.PlaceDto;
import com.js.dto.PlaceReviewDto;
import com.js.dto.PlanCourseDto;
import com.js.dto.PlanDetailDto;
import com.js.dto.PlanDetailPlaceDto;
import com.js.dto.PlanPlaceMapDto;
import com.js.dto.RecommendPlanDetailDto;
import com.js.dto.ReviewListDto;
import com.js.dto.UpcomingPlanListDto;
import com.js.service.AIService;
import com.js.service.MemberService;
import com.js.service.MyTripService;
import com.js.service.TripInfoService;

@Controller
public class HomeController {
	@Autowired
	TripInfoService tiSvc;	// 여행 정보 서비스
	@Autowired
	MemberService mSvc;		// 회원 정보 서비스
	@Autowired
	MyTripService mtSvc;	// 내 여행 서비스
	@Autowired
	AIService aiSvc;		// AI 서비스

	// 루트 -> 메인페이지로.
	@RequestMapping("/")
	public String home() {
		return "forward:/Main";
	}
	
	// 메인페이지
	@RequestMapping("/Main")
	public String main(HttpSession session, Model model, @ModelAttribute("msg") String msg) {
		String id = (String)session.getAttribute("loginId");
		
		if(id != null) {
			Map<String, String> profileMap = mSvc.getProfile(id);
			List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
			List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
			
			model.addAttribute("profileMap", profileMap);
			model.addAttribute("listMenuBarProfile", listMenuBarProfile);
			model.addAttribute("listMyBell", listMyBell);
		}
		
		return "Main";
	}
	
	// 로그인페이지로 이동.
	@RequestMapping("/Login")
	public String login(@ModelAttribute("msg") String msg) {
		
		return "Login";
	}
	
	// 로그아웃 처리.
	@RequestMapping("/Logout")
	public String logout(HttpSession session, RedirectAttributes rttr) {
		session.removeAttribute("loginId");
		rttr.addFlashAttribute("msg", "로그아웃 되었습니다.");
		return "redirect:/Login";
	}
	
	// 로그인 처리.
	@RequestMapping("/LoginAction")
	public String loginAction(String id, String pw, HttpSession session, RedirectAttributes rttr) {
		boolean result = mSvc.checkLogin(id, pw);
		
		if(result) {
			session.setAttribute("loginId", id);
			rttr.addFlashAttribute("msg", "로그인되었습니다.");
			return "redirect:/";
		}
		rttr.addFlashAttribute("msg", "잘못된 로그인 정보입니다.");
		return "redirect:/Login";
	}
	
	// 회원가입 페이지로 이동.
	@RequestMapping("/Join")
	public String join(HttpSession session, Model model) {
		return "Join";
	}
	
	// 장소 상세 페이지로 이동.
	@RequestMapping("/PlaceDetail")
	public String placeDetail(@RequestParam("place_idx") Integer placeIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		PlaceDto placeDto = mtSvc.getPlaceDetail(id, placeIdx);
		List<PlaceReviewDto> listPlaceReview = mtSvc.getPlaceReviewList(id, placeIdx);
		for(PlaceReviewDto dto : listPlaceReview) {
			dto.setListReviewImg(mtSvc.getPlaceReviewImg(dto.getReviewIdx()));
		}
		List<UpcomingPlanListDto> listUpcomingPlan = mtSvc.getUpcomingPlanList(id);
		
		model.addAttribute("placeDto", placeDto);
		model.addAttribute("listPlaceReview", listPlaceReview);
		model.addAttribute("listUpcomingPlan", listUpcomingPlan);
		
		return "PlaceDetail";
	}
	
	// 내 여행 페이지로 이동.
	@RequestMapping("/MyTrip")
	public String myTrip(Model model, HttpSession session) {
		String id = (String)session.getAttribute("loginId");
		
		MemberDto memberDto = mtSvc.getMemberDetail(id);
		List<UpcomingPlanListDto> listUpcomingPlan = mtSvc.getUpcomingPlanList(id);
		List<MyReviewListDto> listMyReview = mtSvc.getMyReview(id);
		List<MyReviewListDto> listMyCityReview = mtSvc.getMyCityReview(id);
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("memberDto", memberDto);
		model.addAttribute("listUpcomingPlan", listUpcomingPlan);
		model.addAttribute("listMyReview", listMyReview);
		model.addAttribute("listMyCityReview", listMyCityReview);
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "MyTrip";
	}
	
	// 내 여행 상세 페이지로 이동.
	@RequestMapping("/MyTripDetail")
	public String myTripDetail(@RequestParam("plan_idx") Integer planIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		List<PlanDetailPlaceDto> listPlanDetailPlace = mtSvc.getPlanDetailPlace(planIdx);
		int lastDay = mtSvc.getLastDay(planIdx);		
		List<PlanDetailDto> listPlanDetail = mtSvc.getPlanDetail(planIdx);
		List<PlanPlaceMapDto> listPlanPlaceMap = mtSvc.getPlanPlaceMap(planIdx);
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("listPlanDetailPlace", listPlanDetailPlace);
		model.addAttribute("lastDay", lastDay);
		model.addAttribute("listPlanDetail", listPlanDetail);
		model.addAttribute("listPlanPlaceMap", listPlanPlaceMap);
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "MyTripDetail";
	}
	
	// 일정 추가 처리.
	@PostMapping("/insertPlan")
	public String insertPlan(@RequestParam("startDate") String startDate, @RequestParam("days") int days, @RequestParam("cityIdxList") List<Integer> cityIdxList, HttpSession session) {
		String id = (String)session.getAttribute("loginId");
		
		int planIdx = mtSvc.insertPlan(id, startDate, days);
		int order = 1;
		for(int cityIdx : cityIdxList) {
			mtSvc.insertPlanCity(planIdx, order++, cityIdx);
		}
		
		return "redirect:/MyTripDetail?plan_idx=" + planIdx;
	}
	
	// 장소 추가 페이지로 이동.
	@RequestMapping("/AddPlace")
	public String addPlace(@RequestParam("plan_idx") Integer planIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		List<AddPlaceDto> listAddMyPlace = mtSvc.getAddMyPlace(id);
		List<AddPlaceDto> listAddRecommendPlace = mtSvc.getAddRecommendPlace(planIdx);
		
		model.addAttribute("listAddMyPlace", listAddMyPlace);
		model.addAttribute("listAddRecommendPlace", listAddRecommendPlace);
		
		return "AddPlace";
	}
	
	// 일정 장소 추가 처리.
	@PostMapping("/insertPlanPlaces")
	public String insertPlanPlaces(@RequestParam("planIdx") int planIdx, @RequestParam("day") int day, @RequestParam("placeIdxList") List<Integer> placeIdxList) {
		for(int pIdx : placeIdxList) {
			mtSvc.insertPlanPlace(planIdx, day, pIdx);
		}
		
		return "redirect:/MyTripDetail?plan_idx=" + planIdx;
	}
	
	// 내 저장 페이지로 이동.
	@RequestMapping("/MySaved")
	public String mySaved(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		List<MyPlaceListDto> listMyPlace = mtSvc.getMyPlace(id);
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyPlace", listMyPlace);
		model.addAttribute("listMyBell", listMyBell);
		
		return "MySaved";
	}
	
	// 일정 추가 페이지로 이동.
	@RequestMapping("/AddPlan")
	public String addPlan(Model model) {
		List<CityListDto> listCity = mtSvc.getCityList();
		
		model.addAttribute("listCity", listCity);
		return "AddPlan";
	}
	
	// 추천 일정 페이지로 이동.
	@RequestMapping("/RecommendPlan")
	public String recommendPlan(@RequestParam(value="city_idx", required=false) Integer cityIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		if(cityIdx == null) {
			cityIdx = 16;	// 파리(기본값)
		}
		
		List<CityListDto> listCityRecommend = tiSvc.getCityListRecommend();
		List<PlaceDto> listPlaceTop10 = tiSvc.getPlaceTop10(cityIdx);
		List<RecommendPlanDetailDto> listRecommendPlan = tiSvc.getRecommendPlanDetailList(cityIdx);
		List<BlogListDto> listBlog = tiSvc.getBlogList();
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("listCityRecommend", listCityRecommend);
		model.addAttribute("cityIdx", cityIdx);
		model.addAttribute("listPlaceTop10", listPlaceTop10);
		model.addAttribute("listRecommendPlan", listRecommendPlan);
		model.addAttribute("listBlog", listBlog);
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "RecommendPlan";
	}
	
	// 추천 일정 전체 페이지로 이동.
	@RequestMapping("/RecommendPlanAll")
	public String recommendPlanAll(@RequestParam("city_idx") Integer cityIdx, Integer recommendPlanIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		String cityName = tiSvc.getCityNameByIdx(cityIdx);
		List<RecommendPlanDetailDto> listRecommendPlan = tiSvc.getRecommendPlanDetailList(cityIdx);
		List<ReviewListDto> listReview = tiSvc.getRecommendReviewList(cityIdx, id, null);
		for(ReviewListDto dto : listReview) {
			dto.setListReviewImg(tiSvc.getRecommendReviewImgList(dto.getRecommendReviewIdx()));
		}
		
		model.addAttribute("cityIdx", cityIdx);
		model.addAttribute("cityName", cityName);
		model.addAttribute("listRecommendPlan", listRecommendPlan);
		model.addAttribute("listReview", listReview);
		
		return "RecommendPlanAll";
	}
	
	// 블로그 페이지로 이동.
	@RequestMapping("/Blog")
	public String blog(@RequestParam("blog_idx") Integer blogIdx, HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		BlogDetailDto blog = tiSvc.getBlogDetail(blogIdx);
		List<BlogCommentListDto> listBlogComment = tiSvc.getBlogCommentList(id, blogIdx);
		List<BlogPlaceMapDto> listBlogPlaceMap = tiSvc.getBlogPlaceMapList(blogIdx);
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("listBlog", blog);
		model.addAttribute("listBlogComment", listBlogComment);
		model.addAttribute("listBlogPlaceMap", listBlogPlaceMap);
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "Blog";
	}
	
	// 여행 코스 페이지로 이동.
	@RequestMapping("/PlanCourse")
	public String planCourse(@RequestParam("blog_idx") Integer blogIdx, Model model) {
		List<PlanCourseDto> listCourse = tiSvc.getPlanCourse(blogIdx);
		
		model.addAttribute("listCourse", listCourse);
		
		return "PlanCourse";
	}
	
	// 항공권 페이지로 이동.
	@RequestMapping("/AirlineTicket")
	public String airlineTicket(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		List<AirportListDto> listAirport = tiSvc.getAirportList();
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("listAirport", listAirport);
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);		
		model.addAttribute("listMyBell", listMyBell);
		
		return "AirlineTicket";
	}
	
	// AI짐싸기 페이지로 이동.
	@RequestMapping("/AiPackingList")
	public String aiPackingList(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		MyAiPackDto myAiPack = tiSvc.getMyAiPack(id);
		
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		model.addAttribute("myAiPack", myAiPack);
		
		return "AiPackingList";
	}
	
	// AI 짐싸기 파일 업로드 처리.
	@RequestMapping("/UploadAiPacking")
	public String uploadAiPacking(@RequestParam("file1") MultipartFile file, HttpSession session, Model model) throws Exception {
		String loginId = (String)session.getAttribute("loginId");
		
		// 1. upload 폴더 생성
		// String path = application.getRealPath("upload");	// upload 폴더의 절대경로(C:\ 로 시작) 얻기
		// System.out.println("절대경로 : " + path);
		String path = "D:/Temp/upload";
		
		// 폴더가 없으면 생성 :
		File f = new File(path);	// 파일시스템 관련 처리 --> File 객체
		if(!f.exists())
			f.mkdirs();
		
		// 2. 폴더 지정하고, 지정한 폴더에 저장 --> 파일 정보(ex. 파일명)을 얻음.
		if(!file.isEmpty()) {
			String filename = file.getOriginalFilename();	// 원본파일명
			File savedFile = new File(path, filename);	// 스프링에서는, 'rename policy' 대신에 저장할 파일명을 직접 지정함.
			
			file.transferTo(savedFile);	// 실제로 파일을 서버에 저장함.
			
			mtSvc.insertAiPackImg(loginId, filename);
			
			// ChatGPT API AI서비스 실행 ---> list, list, advice를 update.
			
			Map<String, Object> map1 = mtSvc.getAiPackCityDays(loginId);
			String nation = (String)map1.get("CITY_NAME");
			int days = ((Number)map1.get("DAYS")).intValue();

			byte[] fileContent = Files.readAllBytes(Paths.get("d:/Temp/upload/" + filename));
			String base64Image = Base64.getEncoder().encodeToString(fileContent);
			Map<String, Object> mapResult = new AIService().packingApi(base64Image, nation, days);
			System.out.println("present:");
			for(String item : (String[])mapResult.get("present")) { System.out.print(item + ", "); }
			System.out.println();
			
			System.out.println("missing:");
			for(String item : (String[])mapResult.get("missing")) { System.out.print(item + ", "); }
			System.out.println();
			
			System.out.println("advice:" + mapResult.get("advice"));
			
			StringBuffer presentList = new StringBuffer("");
			for(String item : (String[])mapResult.get("present")) { presentList.append(item + ","); }
			if (presentList.length() > 0) { presentList.setLength(presentList.length() - 1); }
			StringBuffer missingList = new StringBuffer("");
			for(String item : (String[]) mapResult.get("missing")) { missingList.append(item + ","); }
			if (missingList.length() > 0) { missingList.setLength(missingList.length() - 1); }
			String present = presentList.toString();
			String missing = missingList.toString();
			String advice = (String)mapResult.get("advice");
			if(present==null || "".equals(present)) present=" ";
			if(missing==null || "".equals(missing)) missing=" ";
			if(advice==null || "".equals(advice)) advice=" ";
			
			mtSvc.updateAiPack(present, missing, advice, loginId);
		}
		
		return "forward://AiPackingList";
	}
	
	// AI 추천 일정 메인 홈 페이지로 이동.
	@RequestMapping("/AiPlanHome")
	public String aiPlanHome(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "AiPlanHome";
	}
	
	// AI 추천 일정 페이지로 이동.
	@RequestMapping("/AiPlan")
	public String aiPlan(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		List<AiRecommendPlaceDto> listAiPlace = tiSvc.getAiRecommendPlaceList(id);
		
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		model.addAttribute("listAiPlace", listAiPlace);
		
		return "AiPlan";
	}
	
	// AI 추천 일정 옵션 선택 페이지로 이동.
	@RequestMapping("/AiPlanOption")
	public String aiPlanOption(HttpSession session, Model model) {
		String id = (String)session.getAttribute("loginId");
		
		Map<String, String> profileMap = mSvc.getProfile(id);
		List<MenuBarProfileDto> listMenuBarProfile = mtSvc.getMenuBarProfile(id);
		List<MyBellListDto> listMyBell = mtSvc.getMyBellList(id);
		
		model.addAttribute("profileMap", profileMap);
		model.addAttribute("listMenuBarProfile", listMenuBarProfile);
		model.addAttribute("listMyBell", listMyBell);
		
		return "AiPlanOption";
	}
	
	// AI 추천 일정 옵션 선택 처리.
	@RequestMapping("/AiPlanOptionAction")
	public String aiPlanOptionAction(String option1, String option2, String option3, String option4, String option5, HttpSession session) {
		String id = (String)session.getAttribute("loginId");
		
		System.out.println("option1 : " + option1);  	// 파리
		System.out.println("option2 : " + option2);		// 2박 3일
		System.out.println("option3 : " + option3);		// 혼자
		System.out.println("option4 : " + option4);		// SNS 핫플레이스
		System.out.println("option5 : " + option5);	 	// 널널한 일정
		
		session.setAttribute("cityName", option1);
		int option2num = 0;
		if("당일치기".equals(option2)) option2num = 1;
		else {
			option2 = option2.split(" ")[1].replace("일", "");
			option2num = Integer.parseInt(option2);
		}
		session.setAttribute("days", option2num);
		aiSvc.recommendApiByOption(id, option1, option2num, option3, option4, option5);
		return "forward:/AiPlan";
	}
	
	// AI 자동 추천 처리.
	@RequestMapping("/AiPlanAutoRecommend")
	public String aiPlanAutoRecommend(HttpSession session, HttpServletResponse response) throws IOException {
		String id = (String)session.getAttribute("loginId");
		
		String[] arr = aiSvc.recommendApiByClick(id);
		// 데이터 부족 시 처리
	    if (arr.length == 1 && "INSUFFICIENT_DATA".equals(arr[0])) {
	        response.setContentType("text/html; charset=UTF-8");
	        PrintWriter out = response.getWriter();
	        
	        out.println("<script>");
	        out.println("alert('최근 일주일간 장소 클릭 이력이 10개 이상이어야 자동 추천이 가능합니다.\\n장소들을 구경하고 다시 시도해주세요!\\n(원하는 장소를 클릭하면 해당 장소 상세 페이지로 이동합니다.)');");
	        out.println("location.href='AiPlanHome';"); 
	        out.println("</script>");
	        
	        out.flush();
	        return null; 
	    }
	    
		String cityName = arr[0];
		int days = Integer.parseInt(arr[1]);
		
		session.setAttribute("cityName", cityName);
		session.setAttribute("days", days);
		return "forward:/AiPlan";
	}
	
	// 소셜 로그인(네이버 아이디로 로그인) 처리.
	@RequestMapping("/NaverLoginAction")
	public String naverLoginAction(HttpServletRequest request, HttpSession session, RedirectAttributes rttr) throws UnsupportedEncodingException {
		String clientId = "__";//애플리케이션 클라이언트 아이디값";
	    String clientSecret = "__";//애플리케이션 클라이언트 시크릿값";
	    String code = request.getParameter("code");
	    String state = request.getParameter("state");
	    String redirectURI = URLEncoder.encode("http://localhost:9090/LeaveJisu/NaverLoginAction", "UTF-8");
	    String apiURL;
	    apiURL = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&";
	    apiURL += "client_id=" + clientId;
	    apiURL += "&client_secret=" + clientSecret;
	    apiURL += "&redirect_uri=" + redirectURI;
	    apiURL += "&code=" + code;
	    apiURL += "&state=" + state;
	    System.out.println("apiURL="+apiURL);
	    try {
	      URL url = new URL(apiURL);
	      HttpURLConnection con = (HttpURLConnection)url.openConnection();
	      con.setRequestMethod("GET");
	      int responseCode = con.getResponseCode();
	      BufferedReader br;
	      System.out.print("responseCode="+responseCode);
	      if(responseCode==200) { // 정상 호출
	        br = new BufferedReader(new InputStreamReader(con.getInputStream()));
	      } else {  // 에러 발생
	        br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
	      }
	      String inputLine;
	      StringBuffer res = new StringBuffer();
	      while ((inputLine = br.readLine()) != null) {
	        res.append(inputLine);
	      }
	      br.close();
	      if(responseCode==200) {
	    	  ObjectMapper objectMapper = new ObjectMapper();
	    	  JsonNode rootNode = objectMapper.readTree(res.toString());

	    	  // "access_token" 필드의 값을 String으로 추출
	    	  String accessToken = rootNode.path("access_token").asText();
	    	  String loginId = mSvc.getIdByNaverToken(accessToken);
	    	  if(loginId != null) {
	    		  session.setAttribute("loginId", loginId);
	    		  return "redirect:/Main";
	    	  } else {
	    		  loginId = (String)session.getAttribute("loginId"); 
	    		  if(loginId!=null) {
	    			  mSvc.updateNaverToken(accessToken, loginId);
	    			  return "redirect:/Main";
	    		  }
	    		  rttr.addFlashAttribute("msg", "회원가입 후 네이버 로그인 해주세요.");
	    		  return "redirect:/Join";
	    	  }
	      }
	    } catch (Exception e) {
	      System.out.println(e);
	    }
	    return "forward:/Main";
	}
}
