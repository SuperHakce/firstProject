package com.smart.service;

import com.smart.dao.*;
import com.smart.dao.hibernate.HistoryHibernateDao;
import com.smart.domain.LoginLog;
import com.smart.domain.SchoolEvent;
import com.smart.domain.User;
import com.smart.domain.UserLearningHistory;
import com.smart.primarykey.PrimarKey;
import com.smart.primarykey.createNewId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */
@Service//把 UserService 注解定义为一个 Spring 服务层 Bean
public class UserService {
    private LoginLogDao loginLogDao;
    private UserDao userDao;
    private UserLeaningHistoryDao userLeaningHistoryDao;
    private SchoolEventDao schoolEventDao;
    private PrimarKey primarKey;
    private createNewId createNewIds;
    private HistoryHibernateDao historyHibernateDao;//Test By Heqingjiang

    /**
     * LoginLogDao 已经通过 Spring 注解的方式注解为 Bean
     * @param loginLogDao
     */
    @Autowired
    public void setLoginLogDao(LoginLogDao loginLogDao){
        this.loginLogDao = loginLogDao;
    }

    @Autowired
    public void setSchoolEventDao(SchoolEventDao schoolEventDao){
        this.schoolEventDao = schoolEventDao;
    }

    @Autowired
    public void setPrimarKey(PrimarKey primarKey){
        this.primarKey = primarKey;
    }

    @Autowired
    public void setCreateNewIds(createNewId createNewIds){
        this.createNewIds = createNewIds;
    }

    /**
     * UserDao 已经通过 Spring 注解的方式注解为 Bean
     * @param userDao
     */
    @Autowired
    public void setUserDao(UserDao userDao){
        this.userDao = userDao;
    }

    @Autowired
    public void setUserLeaningHistoryDao(UserLeaningHistoryDao userLeaningHistoryDao){
        this.userLeaningHistoryDao = userLeaningHistoryDao;
    }

    /**
     * 调用 UserDao 的 getMatchCount 方法查询用户匹配结果
     * @param username
     * @param password
     * @return
     */
    public boolean hasMatchUser(String username,String password){
        int matchCount = userDao.getMatchCount(username,password);
        return matchCount > 0;
    }

    @Autowired
    public void setHistoryHibernateDao(HistoryHibernateDao historyHibernateDao){//Test By Heqingjiang
        this.historyHibernateDao = historyHibernateDao;//Test By Heqingjiang
    }//Test By Heqingjiang

    /**
     * 通过用户名字查询用户信息并且返回 User
     * @param username
     * @return
     */
    public User findUserByUserName(String username){
        return userDao.findUserByUserName(username);
    }

    /**
     * 新增用户学历信息
     * @param userLearningHistory
     */
    public void addHistory(UserLearningHistory userLearningHistory){
        userLeaningHistoryDao.addHistoryToMysql(userLearningHistory);
    }
    /**
     * 修改用户学历信息
     * @param userLearningHistory
     */
    public void modifyHistory(UserLearningHistory userLearningHistory){
        userLeaningHistoryDao.updateHistoryByHistoryId(userLearningHistory);
    }

    /**
     * 删除用户学历信息
     * @param userLearningHistory
     */
    public void deleteHistory(UserLearningHistory userLearningHistory){
        userLeaningHistoryDao.deleteHistoryByHistoryId(userLearningHistory);
    }

    /**
     *  登录成功处理
     * @param user
     */
    @Transactional
    public void loginSuccess(User user){

        /*UserLearningHistory userLearningHistory = new UserLearningHistory();//Test By Heqingjiang
        userLearningHistory.setUserId(10);//Test By Heqingjiang
        userLearningHistory.setSchoolName("HEU");//Test By Heqingjiang
        userLearningHistory.setUserName("admin");//Test By Heqingjiang
        userLearningHistory.setStartDate("19920419");//Test By Heqingjiang
        userLearningHistory.setEndDate("20170901");//Test By Heqingjiang
        historyHibernateDao.addUserLearningHistory(userLearningHistory);*///Test By Heqingjiang
        user.setCredits(5 + user.getCredits());
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(user.getLastVisit());
        userDao.updateLoginInfo(user);
        loginLogDao.insertLoginLog(loginLog);
    }

    /**
     * 调出用户的学习历史
     * @param userName
     */
    @Transactional
    public List<UserLearningHistory> loginSuccessFindUserHistory(String userName){
        return userLeaningHistoryDao.getHistoryByName(userName);
    }

    /**
     * 根据学校名字模糊查询
     * @param schoolName
     * @return
     */
    public List<UserLearningHistory> findHistoryBySchoolName(String schoolName,String userName){
        return userLeaningHistoryDao.getHistoryByFindBySchoolName(schoolName,userName);
    }

    /**
     * 增加 SchoolEvent
     * @param schoolEvent
     */
    @Transactional
    public void addEvent(SchoolEvent schoolEvent){
        schoolEventDao.addSchoolEventToMysql(schoolEvent);
    }

    /**
     * 批量增加 SchoolEvent
     */
    @Transactional
    public void addEventByList(List<SchoolEvent> schoolEvents){
        schoolEventDao.addSchoolEventByListToMysql(schoolEvents);
    }
    /**
     * JSP分页
     * @param pageNo
     * @return
     */
    public Page findEventByPage(int pageNo,int pageSize){
        return schoolEventDao.findSchoolEventByPage(pageNo,pageSize);
    }

    public Page findEventByPage(int pageNo,String searchName,int pageSize){
        return schoolEventDao.findSchoolEventByPage(pageNo,searchName,pageSize);
    }

    /**
     * 根据用户姓名模糊查询与其学历相关的历史事件（t_user -> t_history -> t_event）
     * @param userName
     * @return
     */
    public Page findEventByUserName(String userName){
        return schoolEventDao.findEventByUserName(userName);
    }

//    public int testIdempotentForEvent(String method,String eventId,int change,String ticket){
//        return schoolEventDao.testIdempotentForEvent(method,eventId,change,ticket);
//    }

    public void testIdempotentForEvent(int number){
        RunnableTest runnableTest = new RunnableTest();
        Thread thread = new Thread(runnableTest);
        thread.start();
    }

    public void deleteEventGroup(String eventId){
        schoolEventDao.deleteEventGroup(eventId);
    }

    public void invertCheck(String name){
        schoolEventDao.invertCheck(name);
    }
    public void cancelCheckAllAndCheckAll(int who,String name){
        schoolEventDao.cancelCheckAllAndCheckAll(who,name);
    }
    public void invertCheckThisPage(int pageNo,String searchName,int pageSize){
        schoolEventDao.invertCheckThisPage(pageNo,searchName,pageSize);
    }
    public void cancelCheckAllAndCheckAllThisPage(int pageNo,String searchName,int pageSize,int who){
        schoolEventDao.cancelCheckAllAndCheckAllThisPage(pageNo,searchName,pageSize,who);
    }
    public void deleteAllChecked(){
        schoolEventDao.deleteAllChecked();
    }

    public String createNewID(){
        return createNewIds.createPrimaryID();
    }
}
