//package com.kits.project.serverList.controller;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.kits.common.dbclient.DBClient;
//import com.kits.common.dbclient.DBServerMgr;
//import com.kits.common.utils.LogUtil;
//import com.kits.project.serverList.dao.QueryServerListDao;
//import com.kits.project.serverList.domain.ServerList;
//import com.kits.common.utils.LogUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
////@WebServlet(name = "QueryServerListDao",urlPatterns = "/queryServerList2",loadOnStartup = 1)
//public class QueryServerList {
//    protected final Logger logger = LoggerFactory.getLogger(QueryServerList.class);
//    private static final String FAILED = "failed";
//    private static final String SUCCESS = "success";
//    private static final int OPEN_STATE_OPEN = 1;//已开服
//    private static final int SERVER_STATUS_OPEN = 1;//状态为开放中
//    private static final int SERVER_STATUS_KEEP = 2;//状态为维护中
//
//    @GetMapping("/queryServerList2")
//    protected void queryServerList2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        long currentTimeMillis = System.currentTimeMillis();
//        response.setContentType("application/json;charset=UTF-8");
//        request.setCharacterEncoding("UTF-8");
//        String chn_id = request.getParameter("chn_id");
//        String user_id = request.getParameter("user_id");
//        String sec_id = request.getParameter("sec_id");
//        String client_version = request.getParameter("client_version");
//
//        //判断参数
//        if (null == chn_id || !isNum(chn_id)){//必须传的参数
//            JSON result = getResult(100,FAILED,null);
//            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
//            return;
//        }
//        if (null == user_id || user_id.equals("")){//不是白名单
////            JSON result = getResult(101,FAILED,null);
////            response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
//        }
//        if (null == sec_id || sec_id.equals("")){
//            sec_id="";
//        }else {
//            sec_id+=",";
//        }
//        if (null == client_version || client_version.equals("")){
//
//        }
//
//        boolean isWhite = false;//是否为白名单
//        try {
//            DBClient dbClient =DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.GM);
//            List<Map<String, Object>> channelList = QueryServerListDao.selectSdkChannelList(dbClient, Integer.parseInt(chn_id));
//            HashMap map = new HashMap();
//            if (null != channelList && channelList.size() > 0){
//                List<Map<String, Object>> sdkServerLists = QueryServerListDao.selectSdkServerListByChannelId(dbClient,Integer.parseInt(chn_id));
//                List<Map<String, Object>> sdkWhite = QueryServerListDao.selectWhiteByWhiteName(dbClient,user_id);
//                if (null != sdkWhite && sdkWhite.size() > 0){
//                    isWhite = true;
//                }
//                if ( null != sdkServerLists && sdkServerLists.size() > 0){
//                    Map<String, Object> sdkServerList = sdkServerLists.get(0);//只会有一条数据
//                    Long serverListId = (Long) sdkServerList.get("id");
//                    map.put("serverListId",sdkServerList.get("id"));
//
//                    HashMap<String, Object> data  =  new HashMap<String, Object>();
//                    //组装登录服信息
//                    HashMap<String, Object> loginServerMap = new HashMap<String, Object>();
//                    loginServerMap.put("svr_host",sdkServerList.get("login_server_ip"));
//                    loginServerMap.put("svr_name",sdkServerList.get("login_server_name"));
//                    loginServerMap.put("svr_port",sdkServerList.get("login_server_port"));
//                    data.put("lg_server",loginServerMap);
//
//                    List<Map<String, Object>> serverAndExtras = QueryServerListDao.selectServerExtraBySceIdAddAppVersion(dbClient,serverListId,isWhite,client_version,sec_id,OPEN_STATE_OPEN,SERVER_STATUS_OPEN,SERVER_STATUS_KEEP);
//                    List<ServerList> serverLists = new ArrayList<>();
//                    if (null != serverAndExtras && serverAndExtras.size() > 0){
//                        serverLists = getServerLists(serverAndExtras);
//                    }
//                    data.put("servers",serverLists);//白名单全部服务器可见
//                    HashMap<String, Object> result  =  new HashMap<String, Object>();
//                    result.put("data",data);
//                    try {
//                        response.getWriter().write(String.valueOf((JSON) JSONObject.toJSON(result)));
//                        response.getWriter().flush();
//                    } catch (Exception e) {
//                        logger.error(e.getMessage(),e);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        LogUtil.info("获取服务器列表耗时:" +(System.currentTimeMillis() - currentTimeMillis)+ "ms");
//    }
//    private JSON getResult(Object error, Object status, Object results) {
//
//        HashMap<String, Object> errorMap = new HashMap<String, Object>();
//
//        errorMap.put("error",error);
//        errorMap.put("status",status);
//        errorMap.put("results",results);
//
//        return (JSON) JSONObject.toJSON(errorMap);
//    }
//    /**
//     * 判断字符串是否可以转换为数字
//     * @return
//     */
//    private boolean isNum(String str){
//        return str.matches("[0-9]+");
//    }
//
//
//    /**
//     * 转换为String数组<br>
//     *
//     * @param str 被转换的值
//     * @return 结果
//     */
//    public static String[] toStrArray(String str)
//    {
//        return toStrArray(",", str);
//    }
//
//    /**
//     * 转换为String数组<br>
//     *
//     * @param split 分隔符
//     * @param split 被转换的值
//     * @return 结果
//     */
//    public static String[] toStrArray(String split, String str)
//    {
//        return str.split(split);
//    }
//    //组装客户端需要的数据字段名
//    private List<ServerList> getServerLists(List<Map<String, Object>> serverAndExtras){
//        List<ServerList> serverLists = new ArrayList<>();
//        for (Map<String, Object> server : serverAndExtras) {
//            ServerList serverList = new ServerList();
//            for (Map.Entry<String, Object> s : server.entrySet()) {
//                if (s.getKey().equals("server_id")){
//                    serverList.setSvr_id(Integer.parseInt(s.getValue().toString()));
//                }else if (s.getKey().equals("server_name")){
//                    serverList.setSvr_name((String)s.getValue());
//                }else if (s.getKey().equals("server_ip")){
//                    serverList.setSvr_host((String)s.getValue());
//                }else if (s.getKey().equals("server_port")){
//                    serverList.setSvr_port(Integer.parseInt(s.getValue().toString()));
//                }else if (s.getKey().equals("sort_id")){
//                    serverList.setSvr_sort(Integer.parseInt(s.getValue().toString()));
//                }else if (s.getKey().equals("server_status")){
//                    serverList.setSvr_status(Integer.parseInt(s.getValue().toString()));
//                }else if (s.getKey().equals("group_type")){
//                    serverList.setGroup_type(Integer.parseInt(s.getValue().toString()));
//                }else if (s.getKey().equals("server_label")){
//                    serverList.setSvr_label((String)s.getValue());
//                }
//            }
//            serverLists.add(serverList);
//        }
//
//        return serverLists;
//    }
//}
