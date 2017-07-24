package com.smart.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;

/**
 * Created by Administrator on 2017/7/22.
 */
@Repository
public class CheckBox {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
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
    public String updateCheckBox(String eventId){
        Connection connection = getConn();
        String sqlFindCheck = "SELECT is_check FROM t_event WHERE event_id = ? ";
        int findIsCheck = 0;
        int isCheck = 0;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlFindCheck);
            preparedStatement.setString(1,eventId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                findIsCheck = resultSet.getInt(1);
            }
        }catch (SQLException e){
        }
        if(findIsCheck == 0){
            isCheck = 1;
        }else{
            isCheck = 0;
        }
        String sqlUpdate = "UPDATE t_event set is_check = ? WHERE event_id = ? ";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(sqlUpdate);
            preparedStatement.setInt(1,isCheck);
            preparedStatement.setString(2,eventId);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
        }
        return "yes";
    }
//    public String say(String name) {
//        System.out.println("name:"+name);
//        return "name:"+name;
//    }
}
