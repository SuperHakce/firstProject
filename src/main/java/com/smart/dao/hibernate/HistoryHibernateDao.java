package com.smart.dao.hibernate;

import com.smart.domain.UserLearningHistory;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/7/16.
 */
@Repository
public class HistoryHibernateDao extends BaseDao{

    public void addUserLearningHistory(UserLearningHistory userLearningHistory) {
        getHibernateTemplate().save(userLearningHistory);
    }

    public List findUserLearningHistoryByUName(String queryString,String userName){
        return getHibernateTemplate().find(queryString,userName);
    }

}
