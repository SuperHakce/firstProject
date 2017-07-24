package com.smart.thread;

import com.mysql.jdbc.*;
import com.smart.domain.SchoolEvent;
import com.smart.service.UserService;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



/**
 * Created by Administrator on 2017/7/20.
 */
public class RunnableTest implements Runnable{
    private UserService userService;
    private JdbcTemplate jdbcTemplate;
    Logger logger = Logger.getLogger(RunnableTest.class);
    private Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/sampledb";
        String username = "root";
        String password = "123456";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
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
        ThreadHelp threadHelp = new ThreadHelp();
        Connection connection = threadHelp.getConn();
        String sqlFindTicket = "SELECT * FROM t_ticket WHERE ticket_id = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFindTicket);
            preparedStatement.setString(1,ticket);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                return true;
            }
        }catch (SQLException e){

        }
        return false;
    }
    public int testIdempotentForEvent(String method,String eventId,int change, final String ticket){
        boolean ticketIsUsed = ticketIsUsed(ticket);
        if(ticketIsUsed == true){
            String findSchoolCode = "SELECT school_code FROM t_event WHERE event_id = ? ";
            int schoolCode = 0;
            try{
                ThreadHelp threadHelp = new ThreadHelp();
                Connection connection = threadHelp.getConn();
                PreparedStatement preparedStatement = connection.prepareStatement(findSchoolCode);
                preparedStatement.setString(1,eventId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    schoolCode = resultSet.getInt(1);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return schoolCode;
        }else {
                int schoolCode = 0;
                ThreadHelp threadHelp = new ThreadHelp();
                Connection connection = threadHelp.getConn();
                String updateTicket = "INSERT INTO t_ticket(ticket_id)VALUES(?) ";
                try {
                    if (ticketIsUsed(ticket) == false) {
                        PreparedStatement preparedStatement = connection.prepareStatement(updateTicket);
                        preparedStatement.setString(1, ticket);
                        preparedStatement.executeUpdate();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String findSchoolCode = "SELECT school_code FROM t_event WHERE event_id = ? ";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(findSchoolCode);
                    preparedStatement.setString(1, eventId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        schoolCode = resultSet.getInt(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String updateSchoolCode = "UPDATE t_event SET school_code = ? WHERE event_id = ? ";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(updateSchoolCode);
                    preparedStatement.setInt(1, schoolCode + change);
                    preparedStatement.setString(2, eventId);
                    preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return schoolCode + change;
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
        logger.debug("------Thread Start------");
        SchoolEvent schoolEvent = new SchoolEvent();
        String method = "add";
        schoolEvent.setEventId("010980990mark");
        int change = 100;
        String ticket = create_ticket(method,schoolEvent.getEventId(),change);
        //int schoolCodeNow = userService.testIdempotentForEvent(method,schoolEvent.getEventId(), change, ticket);
        synchronized (this){
            int schoolCodeNow = testIdempotentForEvent(method,schoolEvent.getEventId(),change,ticket);
            logger.debug("-----------schoolCode = " + schoolCodeNow);
        }
    }
}
