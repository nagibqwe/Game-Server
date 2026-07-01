package com.game.background;
import game.core.net.Config.ServerConfig;
import game.core.script.ScriptManager;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

public class BackGroundReLoadScripts {

    private final static Logger logger = LogManager.getLogger(BackGroundReLoadScripts.class);

    public static String reLoadScripts(HttpRequest httpRequest) {
        
        QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
        List<String> param = qsd.parameters().get("secret_key");
        if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
            logger.error("后台刷新脚本失败 私密key错误");
            return "failed";
        }
        Map<Integer, String> scriptNames = new ConcurrentHashMap<>();
        scriptNames.put(1, "common.LoginUrl");

        boolean isSuccess = false;
        try {
            isSuccess = ScriptManager.getInstance().reload(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (isSuccess) {
            return "ok";
        } else {
            return "failed";
        }
    }

}
