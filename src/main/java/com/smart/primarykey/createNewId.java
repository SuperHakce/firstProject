package com.smart.primarykey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/23.
 */
@Repository
public class createNewId {
    private PrimarKey primarKey;
    @Autowired
    private void setPrimarKey(PrimarKey primarKey){
        this.primarKey = primarKey;
    }
    public String createPrimaryID(){
        String primaryID = null;
        String msg="";
        Date date = new Date();
        //SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss.SSS");
        SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMddHHmmssSSS");
        //String robotID = getSchoolRandomString(5);
        String robotID = "000001";
        String randomID = getSchoolRandomString(7);
        msg += robotID + sdf.format(date) + randomID;
        return primarKey.testPrimaryKey(msg);
    }
    /**
     * 生成随机字符串
     * @param length
     * @return
     */
    private static String getSchoolRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdef0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
