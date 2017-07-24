package com.smart.dao.hibernate;

import com.smart.domain.SchoolEvent;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */
@Repository
public class SchoolEventHibernateDao extends BaseDao{
    public void addSchoolEvent(SchoolEvent schoolEvent){
        getHibernateTemplate().save(schoolEvent);
    }
}
