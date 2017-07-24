package com.smart.service;

import java.text.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

import com.smart.dao.Page;
import com.smart.domain.SchoolEvent;
import com.smart.thread.RunnableTest;
import com.smart.web.LoginController;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.smart.domain.User;
import static org.testng.Assert.*;

@ContextConfiguration(locations = {"classpath*:/smart-context.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public class UserServiceTest extends AbstractTransactionalTestNGSpringContextTests {
    private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(UserServiceTest.class);
    @Autowired
    private UserService userService;

    private void setUserService(UserService userService){
        this.userService = userService;
    }

    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    private static String getSchoolRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 幂等性模拟
     */
    @Rollback(false)
    //@Test
    public void testIdempotentForEvent(){
        RunnableTest runnableTest = new RunnableTest();
        Thread thread = new Thread(runnableTest);
        thread.start();
    }
    /**
     * First：批量增加测试
     */
    @Rollback(false)
    @Test
    public void testAddEventByList(){
        Random random = new Random();
        List<SchoolEvent> schoolEventList = new ArrayList<SchoolEvent>();
        for(int i = 1;i <= 110;i ++){
            SchoolEvent schoolEvent = new SchoolEvent();
            schoolEvent.setEventId(userService.createNewID());
            logger.debug(userService.createNewID());
            schoolEvent.setSchoolName(getSchoolRandomString(10));
            schoolEvent.setSchoolCode(random.nextInt(10000));
            schoolEvent.setEventText(getSchoolRandomString(10));
            schoolEvent.setIsCheck(0);
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                schoolEvent.setStartDate(simpleDateFormat.parse("1992-05-09"));
                schoolEvent.setEndDate(simpleDateFormat.parse("2013-04-09"));
            }catch(ParseException e){
                logger.debug("Date Error");
            }
            schoolEventList.add(schoolEvent);
        }
        userService.addEventByList(schoolEventList);
    }

    /**
     * Second：批量增加自定义异常类回滚测试
     */
    @Rollback(false)
    //@Test
    public void testAddEventByListForException(){
        Random random = new Random();
        List<SchoolEvent> schoolEventList = new ArrayList<SchoolEvent>();
        for(int i = 1;i <= 100;i ++){
            SchoolEvent schoolEvent = new SchoolEvent();
            schoolEvent.setSchoolName(getSchoolRandomString(10));
            if(i == 50){
                schoolEvent.setSchoolName("PSPS");
            }else{
                schoolEvent.setSchoolName(getSchoolRandomString(10));
            }
            schoolEvent.setSchoolCode(random.nextInt(10000));
            schoolEvent.setEventText(getSchoolRandomString(10));
            schoolEvent.setIsCheck(0);
            try{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                schoolEvent.setStartDate(simpleDateFormat.parse("1992-05-09"));
                schoolEvent.setEndDate(simpleDateFormat.parse("2013-04-09"));
            }catch(ParseException e){
                logger.debug("Date Error");
            }
            schoolEventList.add(schoolEvent);
        }
        userService.addEventByList(schoolEventList);
    }

    /**
     * Third：分页查询测试
     */
    @Rollback(false)
    //@Test
    public void findEventByPage(){
        Random random = new Random();
        int pageNo = random.nextInt(15);
        int pageSize = random.nextInt(20);
        String eventId = getSchoolRandomString(2);
        Page page = userService.findEventByPage(pageNo,pageSize);
        List<SchoolEvent> schoolEvents = new ArrayList<SchoolEvent>();
        schoolEvents = page.getData();
    }

    /**
     * Fourth批量删除
     */
    @Rollback(false)
    //@Test
    public void deleteEventGroup(){
        String eventId = getSchoolRandomString(1);
        userService.deleteEventGroup(eventId);
    }

    /**
     * 30位
     * Test testPrimaryKey
     */
    @Rollback(false)
   //@Test
    public void createNewID(){
        String ID = userService.createNewID();
        logger.debug(ID);
    }
    @Rollback(false)
    //@Test
    public void testAddSchool(){
        Date date = new Date();
        SchoolEvent schoolEvent = new SchoolEvent();
        UUID uuid = UUID.randomUUID();
        schoolEvent.setEventId(uuid.toString());
        schoolEvent.setSchoolCode(1190);
        schoolEvent.setSchoolName("POPO");
        schoolEvent.setEventText("PPPPPP");
        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            schoolEvent.setStartDate(sdf.parse("1992-04-19"));
            schoolEvent.setEndDate(sdf.parse("2007-09-10"));
        }catch (ParseException e)
        {
            logger.debug("Add SchoolEvent To Mysql");
        }
        userService.addEvent(schoolEvent);
    }

    //@Test
    public void testHasMatchUser() {
        boolean b1 = userService.hasMatchUser("admin", "123456");
        boolean b2 = userService.hasMatchUser("admin", "1111");
        assertTrue(b1);
        assertTrue(!b2);
    }

    //@Test
    public void testFindUserByUserName()throws Exception{
        for(int i =0; i< 100;i++) {
            User user = userService.findUserByUserName("admin");
            assertEquals(user.getUserName(), "admin");
        }

    }

    //@Test
    public void testAddLoginLog() {
        User user = userService.findUserByUserName("admin");
        user.setUserId(1);
        user.setUserName("admin");
        user.setLastIp("000.000.000.000");
        user.setLastVisit(new Date());
        userService.loginSuccess(user);
    }
}
