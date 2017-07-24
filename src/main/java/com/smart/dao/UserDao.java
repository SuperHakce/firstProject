package com.smart.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;
import com.smart.domain.User;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Administrator on 2017/7/13.
 */
@Repository//通过@Repository 注解定义 Dao
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private final static String MATCH_COUNT_SQL = " SELECT count(*) FROM t_user " +
            " WHERE user_name = ? and password = ? ";
    private  final static String UPDATE_LOGIN_INFO_SQL = " UPDATE t_user SET " +
            " last_visit=?,last_ip=?,credits=?  WHERE user_id =?";

    /**
     * 验证用户信息，返回1正确，返回0错误
     * @param userName
     * @param userPassword
     * @return
     */
    public int getMatchCount(String userName,String userPassword){
        return jdbcTemplate.queryForObject(MATCH_COUNT_SQL,new Object[]{userName,userPassword},Integer.class);
    }

    /**
     * 通过用户名查找用户信息
     * @param userName
     * @return
     */
    public User findUserByUserName(final String userName) {
        String sqlStr = " SELECT user_id,user_name,credits " + " FROM t_user WHERE user_name =? ";
        final User user = new User();
        jdbcTemplate.query(sqlStr, new Object[] { userName },
                new RowCallbackHandler() {
                    public void processRow(ResultSet rs) throws SQLException {
                        user.setUserId(rs.getInt("user_id"));
                        user.setUserName(userName);
                        user.setCredits(rs.getInt("credits"));
                    }
    });
        return user;
    }

    /**
     * 根据userId更新用户登录信息，增加积分，更新登录IP，最后登录时间
     * @param user
     */
    public void updateLoginInfo(User user) {
        int s = jdbcTemplate.update(UPDATE_LOGIN_INFO_SQL, new Object[] { user.getLastVisit(),
                user.getLastIp(),user.getCredits(),user.getUserId()});
    }

    /**
     * 自动注入，jdbcTemplcate bean 在 Spring 配置文件中定义
     * @param jdbcTemplate
     */
    @Autowired//自动注入 JdbcTemplate 的 Beans
    public void getJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
