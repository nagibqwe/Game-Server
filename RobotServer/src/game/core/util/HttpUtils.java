/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Administrator
 */
public class HttpUtils
{
    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(HttpUtils.class);

    public static String sendPost(String urladdress, String param) throws Exception
    {
        return sendPost(urladdress, param, null,null);
    }

    /**
     *
     * @param urladdress http://www.baidu.com/s
     * @param param "aa=22&bb=33"
     * @return
     * @throws Exception
     */
    public static String sendPost(String urladdress, String param, String requestPropertyKey,String requestPropertyValue) throws Exception
    {
        HttpURLConnection uc = null;
        try
        {
            URL url = new URL(urladdress);

            uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setInstanceFollowRedirects(true); // 不允许重定向
            uc.setRequestMethod("POST");
            uc.setConnectTimeout(5000); // 五秒连接超时
            uc.setReadTimeout(5000); // 5秒返回超时
            if (requestPropertyKey != null)
            {
                uc.setRequestProperty(requestPropertyKey, requestPropertyValue);
            }
            uc.getOutputStream().write(param.getBytes());
            uc.connect();
            String lines = "";
            try
            {
                String responseMessage = uc.getResponseMessage();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                while (reader.ready())
                {
                    lines += reader.readLine();
                }
                reader.close();
            }
            catch (Exception e)
            {
                logger.info(e, e);
            }
            return lines;
        }
        catch (Exception e)
        {
            logger.error("异常" + urladdress, e);
            if (e instanceof ConnectException)
            {
                logger.info(e);
            }
            else
            {
                logger.info(e, e);
            }
        }
        finally
        {
            if (uc != null && uc.getInputStream() != null)
            {
                // uc.disconnect();//释放资源，并有可能影响到持久连接
                uc.getInputStream().close();// 只释放实例资源，不会影响持久连接
            }
        }
        return "";
    }

    /**
     * @example http://www.baidu.com/s?wd=秦美人&a2=33
     * @param urladdress
     * @return
     * @throws Exception
     */
    public static String sendGet(String urladdress) throws Exception
    {
        HttpURLConnection uc = null;
        try
        {
            URL url = new URL(urladdress);
            uc = (HttpURLConnection) url.openConnection();
            uc.setInstanceFollowRedirects(false); // 不允许重定向
            uc.setRequestMethod("GET");
            uc.setConnectTimeout(5000); // 10秒超时
            uc.setReadTimeout(5000); // 10秒超时
            uc.connect();
            int t = uc.getResponseCode();
            String responseMessage = uc.getResponseMessage();
            BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String lines = "";
            while (reader.ready())
            {
                lines += reader.readLine();
            }
            reader.close();

            return lines;
        }
        catch (Exception e)
        {
            logger.debug("发送出错" + urladdress);
            if (e instanceof ConnectException)
            {
                logger.info(e);
            }
            else
            {
                logger.info(e, e);
            }
        }
        finally
        {
            if (uc != null && uc.getInputStream() != null)
            {
                // uc.disconnect();//释放资源，并有可能影响到持久连接
                uc.getInputStream().close();// 只释放实例资源，不会影响持久连接
            }
        }
        return "";
    }

    public static void main(String[] args)
    {
        try
        {
            String Str = sendPost("http://eve.funcell123.com/config/1/szdc", createUrlParam("access_token", "sfergrth"));
            System.out.println(Str);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static String createUrlParam(Object... param)
    {
        StringBuilder sb = new StringBuilder();
        boolean isfirst = true;
        try
        {
            if (param != null && param.length > 1)
            {
                for (int i = 0; i < param.length; i += 2)
                {
                    if (param[i + 1] != null)
                    {
                        if (!isfirst)
                        {
                            sb.append('&');
                        }
                        sb.append(param[i]);
                        sb.append('=');
                        String value = param[i + 1].toString();
                        value = java.net.URLEncoder.encode(value, "utf-8");
                        sb.append(value);
                        isfirst = false;
                    }
                }
            }
        }
        catch (Exception e)
        {
            logger.info(e, e);
        }
        return sb.toString();
    }
}
