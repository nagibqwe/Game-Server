package com.backend.utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http 工具封装类
 */
public class HttpConnectionUtils {

    /**
     * 发送get请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, String> params) {

        String result = "";
        try {
            if(params!=null) {
                StringBuilder sbParams = new StringBuilder();
                for (Entry<String, String> entry : params.entrySet()) {
                    sbParams.append(entry.getKey());
                    sbParams.append("=");
                    sbParams.append(entry.getValue());
                    sbParams.append("&");
                }

                if (sbParams.length() > 0) {
                    sbParams.deleteCharAt(sbParams.lastIndexOf("&"));
                    if (url.indexOf("?") > 0) {
                        url = url + "&" + sbParams.toString();
                    } else {
                        url = url + "?" + sbParams.toString();
                    }
                }
            }

            URL restServiceURL = new URL(url);
            HttpURLConnection httpConnection = (HttpURLConnection) restServiceURL.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("Charsert", "UTF-8"); //设置请求编码
            httpConnection.setRequestProperty("Accept", "*/*");
            httpConnection.setReadTimeout(3000);
            httpConnection.setConnectTimeout(3000);

            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpConnection.setRequestProperty(entry.getKey(), entry.getValue().toString());
                }
            }

            if (httpConnection.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error code : "
                        + httpConnection.getResponseCode() + " url:" + url);
            }

            InputStream inputStream = httpConnection.getInputStream();
            result = readAll(inputStream);
            inputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String post(String url, Map<String, String> params) {
        return postForm(url, null, params);
    }

    /**
     * 发送post form请求
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) {

        String result = "";

        try {
            StringBuilder sbParams = new StringBuilder();
            for (Entry<String, String> entry : params.entrySet()) {
                sbParams.append(entry.getKey());
                sbParams.append("=");
                sbParams.append(entry.getValue().toString());
                sbParams.append("&");
            }
            if (sbParams.length() > 0) {
                sbParams.deleteCharAt(sbParams.lastIndexOf("&"));
            }

            URL reqUrl = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) reqUrl.openConnection();

            //设置参数
            httpConn.setDoOutput(true);        //需要输出
            httpConn.setDoInput(true);        //需要输入
            httpConn.setUseCaches(false);    //不允许缓存
            httpConn.setRequestMethod("POST");        //设置POST方式连接

            //设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.setRequestProperty("accept", "*/*");

            if (headers != null) {
                for (Entry<String, String> entry : headers.entrySet()) {
                    httpConn.setRequestProperty(entry.getKey(), entry.getValue().toString());
                }
            }

            //建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
            //dos.writeBytes(sbParams.toString()); 这个方法会有中文乱码
            dos.write(sbParams.toString().getBytes("utf-8")); //解决中文乱码
            dos.flush();
            dos.close();

            if (httpConn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP POST form Request Failed with Error code : "
                        + httpConn.getResponseCode() + " url:" + url + " form:" + params.toString());
            }

            InputStream inputStream = httpConn.getInputStream();
            result = readAll(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 发送json请求
     *
     * @param url
     * @param headers
     * @param jsonData
     * @return
     */
    public static String postJSON(String url, Map<String, Object> headers, String jsonData) {

        String result = "";

        try {

            URL reqUrl = new URL(url);
            HttpURLConnection httpConn = (HttpURLConnection) reqUrl.openConnection();

            //设置参数
            httpConn.setDoOutput(true);        //需要输出
            httpConn.setDoInput(true);        //需要输入
            httpConn.setUseCaches(false);    //不允许缓存
            httpConn.setRequestMethod("POST");        //设置POST方式连接

            //设置请求属性
            httpConn.setRequestProperty("Content-Type", "application/json");
            httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            httpConn.setRequestProperty("Charset", "UTF-8");
            httpConn.setRequestProperty("accept", "*/*");

            if (headers != null) {
                for (Entry<String, Object> entry : headers.entrySet()) {
                    httpConn.setRequestProperty(entry.getKey(), entry.getValue().toString());
                }
            }

            //建立输入流，向指向的URL传入参数
            DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream());
            //dos.writeBytes(jsonData); 这个方法会有中文乱码,使用下面的方法解决
            dos.write(jsonData.getBytes("utf-8"));
            dos.flush();
            dos.close();

            if (httpConn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP POST json Request Failed with Error code : "
                        + httpConn.getResponseCode() + " url:" + url + " json:" + jsonData);
            }

            InputStream inputStream = httpConn.getInputStream();
            result = readAll(inputStream);
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 读取字节流
     *
     * @param inputStream
     * @return
     */
    private static String readAll(InputStream inputStream) {

        StringBuilder builder = new StringBuilder();

        try {
            byte[] b = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(b)) != -1) {
                builder.append(new String(b, 0, length,"UTF-8"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return builder.toString();
    }


    /**
     * formupload方式提交数据
     *
     * @param urlPath
     * @param textMap
     * @param fileMap
     * @return
     */
//    public static String formUpload(String urlPath, Map<String, String> textMap, Map<String, String> fileMap) {
//        String res = "";
//        HttpURLConnection conn = null;
//        String BOUNDARY = "----lwm12345boundary"; //boundary就是request头和上传文件内容的分隔符
//        try {
//            URL url = new URL(urlPath);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(30000);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//
//            OutputStream out = new DataOutputStream(conn.getOutputStream());
//            // text
//            if (textMap != null) {
//                StringBuffer strBuf = new StringBuffer();
//                Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Entry<String, String> entry = iter.next();
//                    String inputName = (String) entry.getKey();
//                    String inputValue = (String) entry.getValue();
//                    if (inputValue == null) {
//                        continue;
//                    }
//                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
//                    strBuf.append(inputValue);
//                }
//                out.write(strBuf.toString().getBytes("utf-8"));
//            }
//
//            // file
//            if (fileMap != null) {
//                Iterator<Entry<String, String>> iter = fileMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Entry<String, String> entry = iter.next();
//                    String inputName = (String) entry.getKey();
//                    String inputValue = (String) entry.getValue();
//                    if (inputValue == null) {
//                        continue;
//                    }
//                    File file = new File(inputValue);
//                    String filename = file.getName();
//
//                    String contentType = MimeTypeUtils.getType(filename);
//
//                    StringBuffer strBuf = new StringBuffer();
//                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + filename + "\"\r\n");
//                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
//
//                    out.write(strBuf.toString().getBytes("utf-8"));
//
//                    DataInputStream in = new DataInputStream(new FileInputStream(file));
//                    int bytes = 0;
//                    byte[] bufferOut = new byte[1024];
//                    while ((bytes = in.read(bufferOut)) != -1) {
//                        out.write(bufferOut, 0, bytes);
//                    }
//                    in.close();
//                }
//            }
//
//            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//            out.write(endData);
//            out.flush();
//            out.close();
//
//            // 读取返回数据
//            StringBuffer strBuf = new StringBuffer();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                strBuf.append(line).append("\n");
//            }
//            res = strBuf.toString();
//            reader.close();
//            reader = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//                conn = null;
//            }
//        }
//        return res;
//    }

    /**
     * 发送下载文件请求
     *
     * @param urlPath
     * @param downloadDir
     * @return
     */
    public static File downloadFile(String urlPath, String downloadDir) {
        File file = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("POST");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();

            // 文件大小
            int fileLength = httpURLConnection.getContentLength();

            // 文件名
            String filePathUrl = httpURLConnection.getURL().getFile();
            String fileFullName = filePathUrl.substring(filePathUrl.lastIndexOf(File.separatorChar) + 1);

            System.out.println("file length---->" + fileLength);

            URLConnection con = url.openConnection();

            BufferedInputStream bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = downloadDir + File.separatorChar + fileFullName;
            file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
                // 打印下载百分比
                // System.out.println("下载了-------> " + len * 100 / fileLength +
                // "%\n");
            }
            bin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return file;
        }

    }

    /**
     * formupload方式提交转发文件流
     *
     * @param urlPath
     * @param textMap
     * @param fileMap
     * @return
     */
//    public static String formUpload2(String urlPath, Map<String, String> textMap, Map<String, InputStream> fileMap) {
//        String res = "";
//        HttpURLConnection conn = null;
//        String BOUNDARY = "----lwm12345boundary"; //boundary就是request头和上传文件内容的分隔符
//        try {
//            URL url = new URL(urlPath);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(5000);
//            conn.setReadTimeout(30000);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Connection", "Keep-Alive");
//            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//
//            OutputStream out = new DataOutputStream(conn.getOutputStream());
//            // text
//            if (textMap != null) {
//                StringBuffer strBuf = new StringBuffer();
//                Iterator<Entry<String, String>> iter = textMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Entry<String, String> entry = iter.next();
//                    String inputName = (String) entry.getKey();
//                    String inputValue = (String) entry.getValue();
//                    if (inputValue == null) {
//                        continue;
//                    }
//                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
//                    strBuf.append(inputValue);
//                }
//                out.write(strBuf.toString().getBytes("utf-8"));
//            }
//
//            // file
//            if (fileMap != null) {
//                Iterator<Entry<String, InputStream>> iter = fileMap.entrySet().iterator();
//                while (iter.hasNext()) {
//                    Entry<String, InputStream> entry = iter.next();
//                    String inputName = (String) entry.getKey();
//                    InputStream fileStream =  entry.getValue();
//
//
//                    String contentType = MimeTypeUtils.getType(inputName);
//
//                    StringBuffer strBuf = new StringBuffer();
//                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + inputName + "\"\r\n");
//                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
//
//                    out.write(strBuf.toString().getBytes("utf-8"));
//
//                    DataInputStream in = new DataInputStream(fileStream);
//                    int bytes = 0;
//                    byte[] bufferOut = new byte[1024];
//                    while ((bytes = in.read(bufferOut)) != -1) {
//                        out.write(bufferOut, 0, bytes);
//                    }
//                    in.close();
//                }
//            }
//
//            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//            out.write(endData);
//            out.flush();
//            out.close();
//
//            // 读取返回数据
//            StringBuffer strBuf = new StringBuffer();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                strBuf.append(line).append("\n");
//            }
//            res = strBuf.toString();
//            reader.close();
//            reader = null;
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (conn != null) {
//                conn.disconnect();
//                conn = null;
//            }
//        }
//        return res;
//    }

}