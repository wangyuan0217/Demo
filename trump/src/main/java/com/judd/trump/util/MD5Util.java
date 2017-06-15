package com.judd.trump.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author 王元_Trump
 * @time 2017/3/14 17:19
 * @desc MD5
 */
public class MD5Util {
    /**
     * MD5加密 字符串 32位加密
     *
     * @param encry
     * @return
     */
    public static String encryption(String encry) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(encry.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            re_md5 = buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5.toLowerCase();
    }
}
