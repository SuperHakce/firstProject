package com.smart.dao;

import com.smart.domain.UserLearningHistory;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2017/7/16.
 */
@Repository
public class UserLeaningHistoryDao {
    private JdbcTemplate jdbcTemplate;

    /**
     * 查询学历信息
     * @param userName
     * @return
     */
    public List<UserLearningHistory> getHistoryByName(final String userName){
        String sqlHistory = " SELECT history_id,user_id,user_name,school_name,start_date,end_date " +
                " FROM t_history WHERE user_name = ? ";
        final List<UserLearningHistory> historys = new ArrayList();
        jdbcTemplate.query(sqlHistory, new Object[]{userName}, new RowCallbackHandler() {

            public void processRow(ResultSet resultSet) throws SQLException {
                UserLearningHistory userLearningHistory = new UserLearningHistory();
                userLearningHistory.setHistoryId(resultSet.getInt("history_id"));
                userLearningHistory.setUserId(resultSet.getInt("user_id"));
                userLearningHistory.setUserName(resultSet.getString("user_name"));
                userLearningHistory.setSchoolName(resultSet.getString("school_name"));
                userLearningHistory.setStartDate(resultSet.getDate("start_date"));
                userLearningHistory.setEndDate(resultSet.getDate("end_date"));
                historys.add(userLearningHistory);
            }
        });
        if(historys.isEmpty() == true){
            return historys;
        }else{
            return historys;
        }
    }

    /**
     * 新增用户学历信息
     * @param userLearningHistory
     */
    public void addHistoryToMysql(UserLearningHistory userLearningHistory){
        String sqlAddHistory = " insert into t_history(user_id,user_name,school_name," +
                "start_date,end_date)values(?,?,?,?,?) ";
        Object args[] = {userLearningHistory.getUserId(),userLearningHistory.getUserName(),
        userLearningHistory.getSchoolName(),userLearningHistory.getStartDate(),
        userLearningHistory.getEndDate()};
        jdbcTemplate.update(sqlAddHistory,args);
    }

    /**
     * 更新学历信息
     * @param userLearningHistory
     */
    public void updateHistoryByHistoryId(UserLearningHistory userLearningHistory){
        String sqlModifyHistory = " UPDATE t_history SET  user_id = ?,user_name = ?, " +
                "school_name = ?,start_date = ?,end_date = ? WHERE history_Id = ? ";
        Object args[] = {userLearningHistory.getUserId(),userLearningHistory.getUserName(),
                userLearningHistory.getSchoolName(),userLearningHistory.getStartDate(),
                userLearningHistory.getEndDate(),userLearningHistory.getHistoryId()};
        jdbcTemplate.update(sqlModifyHistory,args);
    }

    /**
     * 删除学历信息
     * @param userLearningHistory
     */
    public void deleteHistoryByHistoryId(UserLearningHistory userLearningHistory){
        String sqlDeleteHistory = " DELETE FROM t_history WHERE history_Id = ? ";
        Object args[] = {userLearningHistory.getHistoryId()};
        jdbcTemplate.update(sqlDeleteHistory,args);
    }

    /**
     * 模糊查询
     * @param schoolName
     * @return
     */
    public List<UserLearningHistory> getHistoryByFindBySchoolName(final String schoolName,final  String userName){
        String sqlFindByUserName = " SELECT history_id,user_id,user_name,school_name,start_date,end_date " +
                " FROM t_history WHERE school_name LIKE ? AND user_name = ? ";
        String schoolNameAdd = "%" + schoolName + "%";
        final List<UserLearningHistory> historys = new ArrayList();
        jdbcTemplate.query(sqlFindByUserName, new Object[]{schoolNameAdd,userName}, new RowCallbackHandler() {

            public void processRow(ResultSet resultSet) throws SQLException {
                UserLearningHistory userLearningHistory = new UserLearningHistory();
                userLearningHistory.setHistoryId(resultSet.getInt("history_id"));
                userLearningHistory.setUserId(resultSet.getInt("user_id"));
                userLearningHistory.setUserName(resultSet.getString("user_name"));
                userLearningHistory.setSchoolName(resultSet.getString("school_name"));
                userLearningHistory.setStartDate(resultSet.getDate("start_date"));
                userLearningHistory.setEndDate(resultSet.getDate("end_date"));
                historys.add(userLearningHistory);
            }
        });
        if(historys.isEmpty() == true){
            return historys;
        }else{
            return historys;
        }
    }
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
}
