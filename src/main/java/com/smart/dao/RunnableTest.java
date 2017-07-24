package com.smart.dao;

import com.smart.domain.SchoolEvent;
import com.smart.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/20.
 */
@Repository
public class RunnableTest implements Runnable{
    private UserService userService;
    private SchoolEventDao schoolEventDao;
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setSchoolEventDao(SchoolEventDao schoolEventDao){
        this.schoolEventDao = schoolEventDao;
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }
    public boolean ticketIsUsed(String ticket){
        String sqlFindTicket = "SELECT * FROM t_ticket WHERE ticket_id = ? ";
        String isNow = jdbcTemplate.queryForObject(sqlFindTicket,String.class,ticket);
        if(isNow.equals(ticket)){
            return true;
        }else{
            return false;
        }
    }
    public int testIdempotentForEvent(String method,String eventId,int change, final String ticket){
        boolean ticketIsUsed = ticketIsUsed(ticket);
        if(ticketIsUsed == true){
            int schoolCode = jdbcTemplate.queryForObject("SELECT school_code FROM t_event WHERE event_id = ? ",int.class,eventId);
            return schoolCode;
        }else{
            //原子操作
            jdbcTemplate.update("INSERT INTO t_ticket(ticket_id)VALUES(?) ",ticket);
            int schoolCode = jdbcTemplate.queryForObject("SELECT school_code FROM t_event WHERE event_id = ? ",int.class,eventId);
            jdbcTemplate.update("UPDATE t_event SET school_code = ? WHERE event_id = ? ",schoolCode - change,eventId);
            return schoolCode - change;
        }
    }
    private static String create_ticket(String method,String eventId,int change){
        String random = getSchoolRandomString(5);//该随便字符串模拟唯一订单号
        String ticket = method + eventId + change + "testId";
        return ticket;
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
    public void run(){
        SchoolEvent schoolEvent = new SchoolEvent();
        String method = "add";
        schoolEvent.setEventId("010980990mark");
        int change = 100;
        String ticket = create_ticket(method,schoolEvent.getEventId(),change);
        //int schoolCodeNow = userService.testIdempotentForEvent(method,schoolEvent.getEventId(), change, ticket);
        int schoolCodeNow = schoolEventDao.testIdempotentForEvent(method, schoolEvent.getEventId(), change, ticket);
    }
}
