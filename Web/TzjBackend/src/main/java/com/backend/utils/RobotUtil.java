//package com.backend.utils;
//
//import com.dingtalk.api.DefaultDingTalkClient;
//import com.dingtalk.api.DingTalkClient;
//import com.dingtalk.api.request.OapiRobotSendRequest;
//import com.dingtalk.api.response.OapiRobotSendResponse;
//import com.taobao.api.ApiException;
//import org.nutz.json.Json;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class RobotUtil {
//
//    public static String pushMessage(String text) throws ApiException {
//        DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/robot/send");
//        String accessToken = ServerKeyUtil.getKey("DingTalkAccessToken");
//        OapiRobotSendRequest req = new OapiRobotSendRequest();
//        Map<String, String> content = new HashMap<>();
//        content.put("content", text);
//        req.setText(Json.toJson(content));
//        req.setMsgtype("text");
//        OapiRobotSendResponse response = client.execute(req, accessToken);
//        return response.toString();
//    }
//}
