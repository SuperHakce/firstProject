package com.smart.dao;

import com.smart.domain.LoginLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcAccessor;
import org.springframework.stereotype.Repository;


/**
 * Created by Administrator on 2017/7/14.
 */
@Repository
public class LoginLogDao {
    private JdbcTemplate jdbcTemplate;
    //向 t_login_log 插入登录信息
    private final static String INSERT_LOGIN_LOG_SQL = "INSERT INTO t_login_log(user_id,ip,login_datetime) VALUES(?,?,?)";

    /**
     * 往 t_login_log 插入登录信息
     * @param loginLog
     */
    public void insertLoginLog(LoginLog loginLog){
        Object[] args = { loginLog.getUserId(),loginLog.getIp(),loginLog.getLoginDate() };
        int s = jdbcTemplate.update(INSERT_LOGIN_LOG_SQL,args);
    }

    @Autowired
    private void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
