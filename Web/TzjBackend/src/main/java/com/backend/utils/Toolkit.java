package com.backend.utils;

import com.backend.bean.APILog;
import com.backend.bean.User;
import org.nutz.lang.Lang;
import org.nutz.lang.random.R;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Map;


public class Toolkit {

    public static final Log log = Logs.get();

    public static final String sign = "sign";

    private static final String Iv = "\0\0\0\0\0\0\0\0";
    private static final String Transformation = "DESede/CBC/PKCS5Padding";

    public static boolean checkCaptcha(String expected, String actual) {
        if (expected == null || actual == null || actual.length() == 0 || actual.length() > 24)
            return false;
        return actual.equalsIgnoreCase(expected);
    }

    public static String passwordEncode(String password, String slat) {
        String str = slat + password + slat + password.substring(4);
        return Lang.digest("SHA-512", str);
    }

    public static String _3DES_encode(byte[] key, byte[] data) {
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
        try {
            Cipher c1 = Cipher.getInstance(Transformation);
            c1.init(Cipher.ENCRYPT_MODE, deskey, iv);
            byte[] re = c1.doFinal(data);
            return Lang.fixedHexString(re);
        } catch (Exception e) {
            log.info("3DES FAIL?", e);
            e.printStackTrace();
        }
        return null;
    }

    public static String _3DES_decode(byte[] key, byte[] data) {
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        IvParameterSpec iv = new IvParameterSpec(Iv.getBytes());
        try {
            Cipher c1 = Cipher.getInstance(Transformation);
            c1.init(Cipher.DECRYPT_MODE, deskey, iv);
            byte[] re = c1.doFinal(data);
            return new String(re);
        } catch (Exception e) {
            log.debug("BAD 3DES decode", e);
        }
        return null;
    }

    public static NutMap kv2map(String kv) {
        NutMap re = new NutMap();
        if (kv == null || kv.length() == 0 || !kv.contains("=")) {
            return re;
        }
        String[] tmps = kv.split(",");
        for (String tmp : tmps) {
            if (!tmp.contains("=")) {
                continue;
            }
            String[] tmps2 = tmp.split("=", 2);
            re.put(tmps2[0], tmps2[1]);
        }
        return re;
    }

    public static String randomPasswd(User usr) {
        String passwd = R.sg(10).next();
        String slat = R.sg(48).next();
        usr.setSalt(slat);
        usr.setPassword(passwordEncode(passwd, slat));
        return passwd;
    }

    public static byte[] hexstr2bytearray(String str) {
        byte[] re = new byte[str.length() / 2];
        for (int i = 0; i < re.length; i++) {
            int r = Integer.parseInt(str.substring(i*2, i*2+2), 16);
            re[i] = (byte)r;
        }
        return re;
    }

    private static String getSignStr(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = paramMap.entrySet().iterator();

        StringBuilder signStr = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String[]> entry = iterator.next();
            if (sign.equalsIgnoreCase(entry.getKey())) {
                continue;
            }
            if (signStr.length() > 1) {
                signStr.append("&");
            }
            signStr.append(entry.getKey()).append("=");
            for (String str : entry.getValue()) {
                signStr.append(str);
            }
        }
        return signStr.toString();
    }

    private static String getSign(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        StringBuilder signStr = new StringBuilder();
        for(String str : paramMap.get(sign)) {
            signStr.append(str);
        }
        return signStr.toString();
    }

    public static boolean checkSign(HttpServletRequest request) {
        String sign = getSign(request);
        String signString = getSignStr(request);

        String md5 = Lang.md5(signString + ServerKeyUtil.GetRequestKey());

        log.error("md5= " + md5 + " sign=" + sign);
        return md5.equalsIgnoreCase(sign);
    }

    public static NutMap outResult(boolean isOk) {
        return new NutMap().setv("ok", isOk);
    }

    public static NutMap outResult(boolean isOk, String msg) {
    	return new NutMap().setv("ok", isOk).setv("msg", msg);
    }

    public static NutMap outResult(boolean isOk, Object data) {
        return new NutMap().setv("ok", isOk).setv("data", data);
    }

    public static NutMap outResult(boolean isOk, String msg, Object data) {
        return new NutMap().setv("ok", isOk).setv("msg", msg).setv("data", data);
    }

    /**
     * 对外接口返回示例
     */
    public static NutMap result(HttpServletRequest request, int code, String msg) {
        NutMap result = new NutMap().setv("Code", code).setv("msg", msg);
        APILog apiLog = new APILog();
        apiLog.setIp(getIp(request));
        apiLog.setUrl(request.getRequestURI());
        apiLog.setParams(getSignStr(request));
        apiLog.setResult(result.toString());
        apiLog.setTime(System.currentTimeMillis() / 1000);
        apiLog.setType(1);
        APILogUtil.getInstance().log(apiLog);
        return result;
    }

    /**
     * 对外接口返回示例
     */
    public static NutMap result(HttpServletRequest request, int ret, String msg, String desc, String data) {
        NutMap result = new NutMap().setv("ret", ret).setv("msg", msg).setv("desc", desc).setv("data", data);
        APILog apiLog = new APILog();
        apiLog.setIp(getIp(request));
        apiLog.setUrl(request.getRequestURI());
        apiLog.setParams(getSignStr(request));
        apiLog.setResult(result.toString());
        apiLog.setTime(System.currentTimeMillis() / 1000);
        apiLog.setType(2);
        APILogUtil.getInstance().log(apiLog);
        return result;
    }

    public static String getIp(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

}
