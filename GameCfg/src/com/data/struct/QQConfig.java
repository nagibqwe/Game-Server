package com.data.struct;

import game.core.util.CodedUtil;
import game.core.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;

/**
 * @Desc TODO
 * @Date 2022/1/4 10:14
 * @Auth ZUncle
 */
public enum QQConfig {
    AppId("appid"),
    Sign("sig"),
    OpenId("openid"),
    OpenKey("openkey"),
    Pf("pf"),
    PfKey("pfkey"),
    Fgi("fgi"),//渠道
    Zoneid("zoneid"),//支付分区ID
    ;
    final String key;

    QQConfig(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    //TODO 生成密钥
    public static String createSign(SortedMap<String, Object> params, String API_KEY) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            String v = "";
            try {
                v = (String) entry.getValue();
            } catch (Exception e) {
                // TODO: handle exception
                v = entry.getValue() + "";
            }
            if (StringUtils.isEmpty(v)) {
                continue;
            }
            if ("sign".equals(k) || "key".equals(k)) {
                continue;
            }

            sb.append(k).append("=").append(v).append("&");
        }
        sb.append("key=").append(API_KEY);
        return CodedUtil.HMAC_SHA1(sb.toString()).toUpperCase();
    }
}
