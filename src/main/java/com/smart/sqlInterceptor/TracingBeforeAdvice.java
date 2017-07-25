package com.smart.sqlInterceptor;

import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Administrator on 2017/7/25.
 */
public class TracingBeforeAdvice implements MethodBeforeAdvice {
    private Logger logger = Logger.getLogger(TracingBeforeAdvice.class);
    public void before(Method m,Object[] args,Object target) throws Throwable{
        if(args != null){
            if(args.length > 0){
                String sqlPre = args[0].toString();
                ArrayList<String> arrayList = new ArrayList<String>();
                for(int i = 0;i < args.length;i ++)
                    if(args[i].getClass().isArray()){
                        for(int j = 0;j < Array.getLength(args[i]);j ++){
                            arrayList.add(Array.get(args[i], j).toString());
                        }
                    }
                int indexFlag = 0;
                StringBuffer stringBuffer = new StringBuffer();
                for(int k = 0;k < sqlPre.length(); k ++){
                    char p = sqlPre.charAt(k);
                    if(p != '?'){
                        stringBuffer.append(p);
                    }else{
                        stringBuffer.append(arrayList.get(indexFlag));
                        indexFlag ++;
                    }
                }
                logger.debug(stringBuffer);
            }
        }
    }
}
