package com.backend.utils;

import com.backend.manager.GameInfoManager;
import com.backend.manager.RechargeItemManager;
import org.apache.commons.codec.digest.DigestUtils;
import org.nutz.log.Log;
import org.nutz.log.Logs;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值相关工具类
 */
public class RechargeUtil {

    private static final String DEFAULT_CHARSET = "utf-8";

    private static final Log logger = Logs.get();

    public static String encode(Object value){
        try {
            return URLEncoder.encode(value.toString(),"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    // 校验sign
    public static boolean isSignOK(String signStr, String sign) {
        String newSign = "";
        try {
            newSign = DigestUtils.md5Hex(signStr.getBytes(DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        String newSign = MD5Util.MD5(signStr);
        boolean result = newSign.toLowerCase().equals(sign.toLowerCase());
        if(!result){
            logger.error("md5校验失败,前面:"+sign+",newSign:"+newSign+",待签名字符串:"+signStr);
        }
        return result;
    }

    public static String buildMd5String(String... args){
        if(args!=null && args.length>0)
        {
            Map<String,Object> paramer = new HashMap<>();
            for(int i = 0;i<args.length/2;i++){
                String key = args[i*2];
                String value =  args[i*2+1];
                if(StringUtils.isEmpty(value)){
                    continue;
                }
                paramer.put(key,value);
            }
            return buildMd5String(paramer);
        }
        return "";
    }
    /**
     * 构建md5加密窜
     * @param paramer
     * @return
     */
    public static String buildMd5String(Map<String,Object> paramer){
        List<String> keyList = new ArrayList<>();
        for(String key : paramer.keySet()){
            keyList.add(key);
        }
        keyList.sort(null);
        StringBuilder sb = new StringBuilder();
        //排序
        for(int i = 0;i<keyList.size();i++){
            if(i == 0){
                sb.append(keyList.get(i)+"=").append(paramer.get(keyList.get(i)));
            }else {
                sb.append("&"+keyList.get(i)+"=").append(paramer.get(keyList.get(i)));
            }
        }
//        sb.append("&secretkey=").append(RechargeItemManager.getInstance().getSecretkey());
        sb.append(GameInfoManager.getInstance().getGameInfo().getRechargeSecretkey());
        return sb.toString();
    }
}
