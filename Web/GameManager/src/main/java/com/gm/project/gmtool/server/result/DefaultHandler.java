package com.gm.project.gmtool.server.result;

import com.gm.project.gmtool.server.send.GMSendErrorType;
import com.gm.project.gmtool.server.send.GMSendException;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class DefaultHandler implements ResultHandler<String> {

    private final Logger LOGGER = LoggerFactory.getLogger(DefaultHandler.class);
    @Override
    public String hand(TServer server, String result) throws GMSendException {
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
    public String hand(TServer server, Exception e) {
        LOGGER.error("gm 执行失败 " + e.toString());
        return "-99";
    }

}
