/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.util;


import com.alibaba.druid.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 *
 * @author Administrator
 */
public class CodedUtil
{
    private static char hexDigits[] =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String decodeBase64(String b64string) throws Exception
    {
        return new String(Base64.base64ToByteArray(b64string));
    }

    public static String encodeBase64(String stringsrc)
    {
        try
        {
            return Base64.byteArrayToBase64(stringsrc.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static String Md5(String s)
    {
        try
        {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
