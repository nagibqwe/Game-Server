package com.kits.project.serverList.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.utils.DateUtils;
import com.kits.common.utils.LogUtil;
import com.kits.project.serverList.domain.NoticeList;
import com.kits.project.serverListConfig.notice.domain.SdkNotice;
import com.kits.project.serverListConfig.notice.service.ISdkNoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class QueryNotice {
    @Autowired
    private ISdkNoticeService sdkNoticeService;

    protected final Logger logger = LoggerFactory.getLogger(QueryNotice.class);

    @GetMapping("/queryNotice")
    protected void queryServerList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long currentTimeMillis = System.currentTimeMillis();
        response.setContentType("application/json;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String chn_id = request.getParameter("chn_id");

        //判断参数
        if (null == chn_id || !isNum(chn_id) || chn_id.equals("")){//必须传的参数
            JSON result = getResult(16);
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
            return;
        }
        try {
            ConcurrentHashMap<Long, SdkNotice> noticeHashMap = sdkNoticeService.getNoticeHashMap();
            Date dateNow = DateUtils.getNowDate();

            HashMap<String, Object> dataMap = new HashMap<String, Object>();
            HashMap<String, Object> noticesMap = new HashMap<String, Object>();
            List<NoticeList> notices = new ArrayList<>();

            for (Map.Entry<Long, SdkNotice> entry : noticeHashMap.entrySet()) {
                SdkNotice sdkNotice = entry.getValue();
                if (!isContains(sdkNotice,chn_id)){
                    continue;
                }
                if (sdkNotice.getStatus() != 1){
                    continue;
                }
                if (DateUtils.dateDiff(dateNow,sdkNotice.getEndTime()) < 0){
                    continue;
                }
                if (DateUtils.dateDiff(dateNow,sdkNotice.getStartTime()) > 0){
                    continue;
                }
                notices = getNoticeLists(notices,sdkNotice);
            }
            noticesMap.put("notices",notices);
            dataMap.put("data",noticesMap);
            dataMap.put("state",1);//成功
            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(dataMap)));
            response.getWriter().flush();
            } catch (Exception e){
                e.printStackTrace();
            }
            LogUtil.info("获取公告耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
    }

    /**
     * 判断字符串是否可以转换为数字
     * @return
     */
    private boolean isNum(String str){
        return str.matches("[0-9]+");
    }

    private JSON getResult(Object error) {

        HashMap<String, Object> errorMap = new HashMap<String, Object>();

        errorMap.put("state",error);
        return (JSON) JSONObject.toJSON(errorMap);
    }

    //组装客户端需要的数据字段名
    private List<NoticeList> getNoticeLists(List<NoticeList> noticeLists,SdkNotice sdkNotice){
        NoticeList noticeList = new NoticeList();
        noticeList.setAuto(sdkNotice.getAuto());
        noticeList.setContent(sdkNotice.getNoticeContent());
        noticeList.setTitle(sdkNotice.getNoticeName());
        noticeList.setType(sdkNotice.getNoticeType());
        noticeLists.add(noticeList);
        return noticeLists;
    }

    /**
     * 判断是否包含请求时发过来的渠道ID
     * @param sdkNotice
     * @param channelId
     * @return
     */
    private boolean isContains(SdkNotice sdkNotice, String channelId){
        boolean flag = false;
        String channelIdStr = sdkNotice.getChannel();
        if (channelIdStr != null){
            String[] channelIdArr = channelIdStr.split(",");
            for (String channelid:channelIdArr){
                if (!channelid.equals("") && (Integer.parseInt(channelid) == Integer.parseInt(channelId))){
                    flag = true;
                }else {
                    continue;
                }
            }
        }
        return flag;
    }
}
