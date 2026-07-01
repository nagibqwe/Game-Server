/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;


import com.alibaba.druid.util.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;

/**
 *
 * @author Administrator
 */
public class CodedUtil
{
    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(CodedUtil.class);

    private static char hexDigits[] =
    {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static String decodeBase64(String b64string) throws Exception
    {
        return new String(Base64.base64ToByteArray(b64string), Charset.forName("utf-8"));
    }

    public static String encodeBase64(String stringsrc)
    {
        try
        {
            return Base64.byteArrayToBase64(stringsrc.getBytes("utf-8"));
        }
        catch (UnsupportedEncodingException e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
    public static String encrypt(String str, String algorithm){
        try
        {
            byte[] strTemp = str.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance(algorithm);
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char c[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++)
            {
                byte byte0 = md[i];
                c[k++] = hexDigits[byte0 >>> 4 & 0xf];
                c[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(c);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String Md5(String s)
    {
        return encrypt(s, "MD5");
    }
    public static String HMAC_SHA1(String s)
    {
        return encrypt(s, "HMAC-SHA1");
    }
}
