package com.smart.exception;

import com.smart.domain.SchoolEvent;

/**
 * Created by Administrator on 2017/7/20.
 */

/**
 * 自定义异常，检查学校名字是否为空
 */
public class schoolNameNotNull{
    public String checkSchoolName(String schoolName) throws exception{
        if(schoolName.equals("PSPS")){
            throw new exception("School Name cannot be PSPS");
        }
        return schoolName;
    }
}