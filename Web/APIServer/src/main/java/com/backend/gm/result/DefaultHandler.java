package com.backend.gm.result;

import com.backend.bean.Server;
import com.backend.gm.send.GMSendErrorType;
import com.backend.gm.send.GMSendException;
import com.backend.utils.JsonUtils;
import com.backend.utils.TypeReference;
import org.nutz.log.Log;
import org.nutz.log.Logs;

import java.util.HashMap;

public class DefaultHandler implements ResultHandler<String> {

    private static final Log LOGGER = Logs.get();

    @Override
    public String hand(Server server, String result) throws GMSendException {
        try {
            return getResult(result);
        } catch (Exception e) {
            LOGGER.error("gm 解析返回值[" + result + "]失败 ", e);
            throw new GMSendException(GMSendErrorType.PARSE, e);
        }
    }

    protected String getResult(String result) {
        return JsonUtils.parseObject(result, new TypeReference<HashMap<String,String>>(){}).get("gmResult");
    }

    @Override
    public String hand(Server server, Exception e) {
        LOGGER.error("gm 执行失败 " + e.toString());
        return "-99";
    }

}
