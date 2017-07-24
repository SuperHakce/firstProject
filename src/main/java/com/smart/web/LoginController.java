package com.smart.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;


import com.smart.dao.Page;
import com.smart.domain.SchoolEvent;
import com.smart.domain.UserLearningHistory;
import com.smart.thread.RunnableTest;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.smart.domain.User;
import com.smart.service.UserService;

@RestController
public class LoginController{
	private UserService userService;
	private Logger logger = Logger.getLogger(LoginController.class);
	@RequestMapping(value = "/index.html")
	public String loginPage(){
		logger.debug("heqingjiang");
		logger.error("HEQINGJIANG");
		return "login";
	}
	
	@RequestMapping(value = "/loginCheck.html")
	public ModelAndView loginCheck(HttpServletRequest request,LoginCommand loginCommand){
		ModelAndView modelAndView = new ModelAndView();
		boolean isValidUser =  userService.hasMatchUser(loginCommand.getUserName(), loginCommand.getPassword());
		if (!isValidUser) {
			return new ModelAndView("login", "error", "用户名或密码错误。");
		} else {
			User user = userService.findUserByUserName(loginCommand.getUserName());
			user.setLastIp(request.getLocalAddr());
			user.setLastVisit(new Date());
			userService.loginSuccess(user);
			modelAndView.setViewName("main");
			modelAndView.addObject("user",user);
			List<UserLearningHistory> userLearningHistories = userService.loginSuccessFindUserHistory(user.getUserName());
			modelAndView.addObject("histories",userLearningHistories);
			return modelAndView;
		}
	}

	/**
	 * 增加学校事件信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "addSchoolEvent.html")
	public ModelAndView addSchoolEvent(HttpServletRequest request){
		ModelAndView modelAndView = new ModelAndView();
		SchoolEvent schoolEvent = new SchoolEvent();
		UUID uuid = UUID.randomUUID();
		schoolEvent.setEventId(uuid.toString());
		schoolEvent.setSchoolCode(Integer.parseInt(request.getParameter("schoolCode")));
		schoolEvent.setSchoolName(request.getParameter("schoolName"));
		schoolEvent.setEventText(request.getParameter("eventText"));
		Date startDate = new Date();
		Date endDate = new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sdf.parse(request.getParameter("startDate"));
			endDate = sdf.parse(request.getParameter("endDate"));
	    }catch (ParseException e)
		   {
			     System.out.println(e.getMessage());
		   }
		schoolEvent.setStartDate(startDate);
		schoolEvent.setEndDate(endDate);
		schoolEvent.setIsCheck(0);
		userService.addEvent(schoolEvent);
		int pageNo = 1;
		String name = "";
		Page pagedTopic = userService.findEventByPage(pageNo,name,10);
		modelAndView.addObject("pagedTopic",pagedTopic);
		modelAndView.setViewName("schoolEvent");
		return modelAndView;
	}

	@RequestMapping(value = "/addHistory.html")
	public ModelAndView addHistory(HttpServletRequest request){
		UserLearningHistory userLearningHistory = new UserLearningHistory();
		userLearningHistory.setUserId(Integer.parseInt(request.getParameter("userId")));
		userLearningHistory.setUserName(request.getParameter("userName"));
		userLearningHistory.setSchoolName(request.getParameter("schoolName"));
		Date startDate = new Date();
		Date endDate = new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sdf.parse(request.getParameter("startDate"));
			endDate = sdf.parse(request.getParameter("endDate"));
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		userLearningHistory.setStartDate(startDate);
		userLearningHistory.setEndDate(endDate);
		userService.addHistory(userLearningHistory);
		User user = userService.findUserByUserName(userLearningHistory.getUserName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		List<UserLearningHistory> userLearningHistories = userService.loginSuccessFindUserHistory(user.getUserName());
		modelAndView.addObject("user",user);
		modelAndView.addObject("histories",userLearningHistories);
		return modelAndView;
	}

	@RequestMapping(value = "/modifyHistory.html")
	public ModelAndView modifyHistory(HttpServletRequest request){
		UserLearningHistory userLearningHistory = new UserLearningHistory();
		userLearningHistory.setHistoryId(Integer.parseInt(request.getParameter("historyId")));
		userLearningHistory.setUserId(Integer.parseInt(request.getParameter("userId")));
		userLearningHistory.setUserName(request.getParameter("userName"));
		userLearningHistory.setSchoolName(request.getParameter("schoolName"));
		Date startDate = new Date();
		Date endDate = new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sdf.parse(request.getParameter("startDate"));
			endDate = sdf.parse(request.getParameter("endDate"));
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		userLearningHistory.setStartDate(startDate);
		userLearningHistory.setEndDate(endDate);
		userService.modifyHistory(userLearningHistory);
		User user = userService.findUserByUserName(userLearningHistory.getUserName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		List<UserLearningHistory> userLearningHistories = userService.loginSuccessFindUserHistory(user.getUserName());
		modelAndView.addObject("user",user);
		modelAndView.addObject("histories",userLearningHistories);
		return modelAndView;
	}

	@RequestMapping(value = "/deleteHistory.html")
	public ModelAndView deleteHistory(HttpServletRequest request){
		UserLearningHistory userLearningHistory = new UserLearningHistory();
		userLearningHistory.setHistoryId(Integer.parseInt(request.getParameter("historyId")));
		userLearningHistory.setUserId(Integer.parseInt(request.getParameter("userId")));
		userLearningHistory.setUserName(request.getParameter("userName"));
		userLearningHistory.setSchoolName(request.getParameter("schoolName"));
		Date startDate = new Date();
		Date endDate = new Date();
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			startDate = sdf.parse(request.getParameter("startDate"));
			endDate = sdf.parse(request.getParameter("endDate"));
		}catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		userLearningHistory.setStartDate(startDate);
		userLearningHistory.setEndDate(endDate);
		userService.deleteHistory(userLearningHistory);
		User user = userService.findUserByUserName(userLearningHistory.getUserName());
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		List<UserLearningHistory> userLearningHistories = userService.loginSuccessFindUserHistory(user.getUserName());
		modelAndView.addObject("user",user);
		modelAndView.addObject("histories",userLearningHistories);
		return modelAndView;
	}

	/**
	 * 模糊查询学校名字
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findBySchoolName.html")
	public ModelAndView findHistoryBySchoolName(HttpServletRequest request){
		User user = userService.findUserByUserName("admin");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("main");
		List<UserLearningHistory> userLearningHistories = userService.findHistoryBySchoolName(request.getParameter("findBySchoolName"),user.getUserName());
		modelAndView.addObject("user",user);
		modelAndView.addObject("histories", userLearningHistories);
		return modelAndView;
	}

	/**
	 * 根据事件ID模糊查询事件
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/findEventByUserName.html")
	public ModelAndView findEventByUserName(HttpServletRequest request){
		ModelAndView modelAndView =new ModelAndView();
		String setSearchName = request.getParameter("findEventByUserName");
		String setPageSize = request.getParameter("setPageSize");
		String setPageNo = request.getParameter("setPage");
		int pageNo = 1;
		if(request.getParameter("checkAll") != null && request.getParameter("checkAll").equals("Check All")){
			userService.cancelCheckAllAndCheckAll(0);
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("cancelCheckAll") != null && request.getParameter("cancelCheckAll").equals("Cancel Check All")){
			userService.cancelCheckAllAndCheckAll(1);
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("invertCheck") != null && request.getParameter("invertCheck").equals("Invert Check")){
			userService.invertCheck();
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("checkAllThisPage") != null && request.getParameter("checkAllThisPage").equals("Check All ThisPage")){
			userService.cancelCheckAllAndCheckAllThisPage(Integer.parseInt(setPageNo), setSearchName, Integer.parseInt(setPageSize), 0);
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("cancelCheckAllThisPage") != null && request.getParameter("cancelCheckAllThisPage").equals("Cancel Check All ThisPage")){
			userService.cancelCheckAllAndCheckAllThisPage(Integer.parseInt(setPageNo),setSearchName,Integer.parseInt(setPageSize),1);
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("invertCheckThisPage") != null && request.getParameter("invertCheckThisPage").equals("Invert Check ThisPage")){
			userService.invertCheckThisPage(Integer.parseInt(setPageNo),setSearchName,Integer.parseInt(setPageSize));
			pageNo = Integer.parseInt(setPageNo);
		}
		if(request.getParameter("deleteAllChecked") != null && request.getParameter("deleteAllChecked").equals("Delete All Checked")){
			userService.deleteAllChecked();
		}
		int pageSize = 15;
		String searchName = null;
		if(setSearchName == null){
			searchName = "";
		}else{
			searchName = setSearchName.replaceAll(" ","");
		}
		if(setPageSize != null && setPageSize != "" && setPageSize != " "){
			String replacePageSize = setPageSize.replaceAll(" ","");
			pageSize = Integer.parseInt(replacePageSize);
		}
		Page pagedTopic = userService.findEventByPage(pageNo,searchName,pageSize);
		pagedTopic.setSearchName(searchName);
		modelAndView.addObject("pagedTopic",pagedTopic);
		modelAndView.setViewName("schoolEvent");
		return modelAndView;
	}
	/**
	 * JSP 分页
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/changePage.html")
	public ModelAndView changePageSchoolEvent(@RequestParam(value = "pageNo", required = false) Integer pageNo,HttpServletRequest request) {
		ModelAndView modelAndView =new ModelAndView();
		String setSearchName = request.getParameter("findEventByUserName");
		String setPageSize = request.getParameter("setPageSize");
		String checkBoxList = request.getParameter("checkBoxList");
		int pageSize = 15;
		String searchName = null;
		pageNo = pageNo==null?1:pageNo;
		if(setSearchName == null){
			searchName = "";
		}else{
			searchName = setSearchName.replaceAll(" ","");
		}
		if(setPageSize != null && setPageSize != "" && setPageSize != " "){
			String replacePageSize = setPageSize.replaceAll(" ","");
			pageSize = Integer.parseInt(replacePageSize);
		}
		Page pagedTopic = userService.findEventByPage(pageNo,searchName,pageSize);
		pagedTopic.setSearchName(searchName);
		modelAndView.addObject("pagedTopic",pagedTopic);
		modelAndView.setViewName("schoolEvent");
		return modelAndView;
	}
	@RequestMapping(value = "/showPage.html")
	public ModelAndView showPage(HttpServletRequest request){
		ModelAndView modelAndView =new ModelAndView();
		String setSearchName = request.getParameter("findEventByUserName");
		String setPageSize = request.getParameter("setPageSize");
		String setPageNo = request.getParameter("showPage");
		int pageSize = 15;
		String searchName = null;
		int pageNo = 1;
		if(setSearchName == null){
			searchName = "";
		}else{
			searchName = setSearchName.replaceAll(" ","");
		}
		if(setPageSize != null && setPageSize != "" && setPageSize != " "){
			String replacePageSize = setPageSize.replaceAll(" ","");
			pageSize = Integer.parseInt(replacePageSize);
		}
		if(setPageNo != null && setPageNo != "" && setPageNo != " "){
			String replacePageNo = setPageNo.replaceAll(" ","");
			pageNo = Integer.parseInt(replacePageNo);
		}
		Page pagedTopic = userService.findEventByPage(pageNo,searchName,pageSize);
		pagedTopic.setSearchName(searchName);
		modelAndView.addObject("pagedTopic",pagedTopic);
		modelAndView.setViewName("schoolEvent");
		return modelAndView;
	}

	/**
	 *，模拟幂等性
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/testIdempotentForEvent.html")
	public ModelAndView testIdempotentForEvent(HttpServletRequest request){
		RunnableTest runnableTest1 = new RunnableTest();
		Thread thread1 = new Thread(runnableTest1);
		RunnableTest runnableTest2 = new RunnableTest();
		Thread thread2 = new Thread(runnableTest1);
		RunnableTest runnableTest3 = new RunnableTest();
		Thread thread3 = new Thread(runnableTest1);
		RunnableTest runnableTest4 = new RunnableTest();
		Thread thread4 = new Thread(runnableTest1);
		RunnableTest runnableTest5 = new RunnableTest();
		Thread thread5 = new Thread(runnableTest1);
		RunnableTest runnableTest6 = new RunnableTest();
		Thread thread6 = new Thread(runnableTest1);
		thread1.start();thread2.start();thread3.start();thread4.start();
		thread5.start();thread6.start();
		User user = userService.findUserByUserName("admin");
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("user",user);
		modelAndView.setViewName("main");
		return modelAndView;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
