package com.smart.primarykey;

import org.springframework.stereotype.Repository;
import sun.awt.image.PixelConverter;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by Administrator on 2017/7/23.
 */
@Repository
public class PrimarKey {
//    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
//    '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
//            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
//            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
//            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
//            'Z' ,'!', '@', '#', '$', '%', '&', '*', '(', ')', '?', '~', '^', '-', '+', '=', '.', ':', '}', '{',
//            '[', ']', ';', '`', '"' , '|', '<', '>', '_'};//90

    final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
            'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y',
            'Z'};//62

    final static char[] digitsSixteen = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    final static Map<Integer,Character> digitMap = new HashMap<Integer,Character>();

    final static Map<Character, Integer> digitMapSixteen = new HashMap<Character, Integer>();

    static {
        for (int i = 0; i < digits.length; i++) {
            digitMap.put((int) i,digits[i]);
        }
        for (int i = 0; i < digitsSixteen.length; i++) {
            digitMapSixteen.put(digitsSixteen[i], (int) i);
        }
    }

    /**
     * 16 进制转 62 进制
     * @param bigIntegerOne
     * @return
     */
    public static String toLastIndex(BigInteger bigIntegerOne){
        String lastString = "";
        BigInteger bigIntegerZero = new BigInteger("0");
        BigInteger bigIntegerDivide = bigIntegerOne;
        BigInteger bigIntegerRemain = bigIntegerOne;
        BigInteger bigInteger62 = new BigInteger("62");
        BigInteger bigIntegerTwo = bigIntegerOne;
        while(bigIntegerTwo.compareTo(bigIntegerZero) != 0){
            bigIntegerDivide = bigIntegerTwo.divide(bigInteger62);
            bigIntegerRemain = bigIntegerTwo.remainder(bigInteger62);
            bigIntegerTwo = bigIntegerDivide;
            int index = bigIntegerRemain.intValue();
            if(index > 0)
                index -= 1;
            char c = digitMap.get(index);
            lastString = lastString + "" + c;
        }
        int lengthS = lastString.length();
        int lengthP = 20 - lengthS;
        String addS = getSchoolRandomString(lengthP);
        return lastString + addS;
    }
    /**
     * 16 进制字符串转 long
     * @param primaryKey
     * @return
     */
    public static BigInteger toNumber(String primaryKey) {
        //String str = new BigInteger(primaryKey, 16).toString(10);
        BigInteger bigIntegerResult = new BigInteger("0");
        int length = primaryKey.length();
        char[] s = primaryKey.toCharArray();
        for(int i = 0;i < length;i ++){
            char number = s[i];
            long a = digitMapSixteen.get(number);
            String w = String.valueOf(a);
            BigInteger bigInteger = new BigInteger(w);
            BigInteger bigInteger16 = new BigInteger("16");
            for(int j = 1;j <= i;j ++){
                BigInteger bigIntegerS = bigInteger;
                bigInteger = bigIntegerS.multiply(bigInteger16);
            }
            BigInteger bigIntegerTwo = bigIntegerResult;
            bigIntegerResult = bigIntegerTwo.add(bigInteger);
            BigInteger bigIntegerThree = bigIntegerResult;
        }
        return bigIntegerResult;
    }
    public String testPrimaryKey(String s){
        BigInteger bigInteger = toNumber(s);
        String lastIndex = toLastIndex(bigInteger);
        return lastIndex;
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
}
