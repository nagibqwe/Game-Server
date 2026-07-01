/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kits.project.photocheck.photodata.utils;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 */
public class HttpUtils
{
    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(HttpUtils.class);

    public static int sendPost(String urladdress, String param)
    {
        StringBuilder sb = new StringBuilder();
        return sendPost(urladdress, param, sb, null, null);
    }

    public static int sendPost(String urladdress, String param, StringBuilder sb)
    {
        return sendPost(urladdress, param, sb, null, null);
    }

    /**
     *
     * @param urladdress http://www.baidu.com/s
     * @param param "aa=22&bb=33"
     * @param sb 返回的结果字符串
     * @param requestPropertyKey
     * @param requestPropertyValue
     * @return 返回连接状态码
     */
    public static int sendPost(String urladdress, String param, StringBuilder sb, String requestPropertyKey, String requestPropertyValue)
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
            int code = 405;
            try
            {
                code = uc.getResponseCode();
//                logger.error("返回结果状态值:" + uc.getResponseCode());
//                String responseMessage = uc.getResponseMessage();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream())))
                {
                    while (reader.ready())
                    {
                        sb.append(reader.readLine());
                    }
                }
            }
            catch (Exception e)
            {
                logger.error(e, e);
            }
            return code;
        }
        catch (Exception e)
        {
            logger.error("异常" + urladdress, e);
            if (e instanceof ConnectException)
            {
                logger.error(e, e);
            }
            else
            {
                logger.error(e, e);
            }
        }
        finally
        {
            try
            {
                if (uc != null && uc.getInputStream() != null)
                {
                    // uc.disconnect();//释放资源，并有可能影响到持久连接
                    uc.getInputStream().close();// 只释放实例资源，不会影响持久连接
                }
            }
            catch (IOException ex)
            {
                logger.error(ex, ex);
            }
        }
        return 404;
    }

    /**
     * @param sb 返回内容
     * @example http://www.baidu.com/s?wd=秦美人&a2=33
     * @param urladdress 请求地址
     * @return 返回状态值值
     * @throws Exception
     */
    public static int sendGet(String urladdress, StringBuilder sb) throws Exception
    {
        int state = 404;
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
            state = uc.getResponseCode();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(uc.getInputStream())))
            {
                while (reader.ready())
                {
                    sb.append(reader.readLine());
                }
            }

            return state;
        }
        catch (Exception e)
        {
            logger.debug("发送出错" + urladdress);
            if (e instanceof ConnectException)
            {
                logger.error(e, e);
            }
            else
            {
                logger.error(e, e);
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
        return state;
    }

    public static void main(String[] args)
    {
        StringBuilder sb = new StringBuilder();
//        String ss = PhotoFileUtils.bytesToHexString(PhotoFileUtils.image2byte("C:/Users/Administrator/Desktop/veer-305358648.jpg"));
//        String ss = PhotoFileUtils.imageToBase64Str("C:/Users/Administrator/Desktop/veer-305358648.jpg");
        int code = sendPost("http://localhost:8081/photocheck/photodata/upload", createUrlParam("extName", "png","photoId","64E8851B11B64101076D4C665C2751A113"), sb);
        System.out.println("返回code =" + code + ", content =" + sb.toString());
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
            logger.error(e, e);
        }
        return sb.toString();
    }
}
