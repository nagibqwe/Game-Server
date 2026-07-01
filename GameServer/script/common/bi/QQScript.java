package common.bi;

import com.data.struct.QQConfig;
import com.game.bi.biqq.*;
import com.game.bi.inter.IQQScript;
import com.game.bi.manager.BIManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.qq.structs.QQUic;
import com.game.qq.structs.QQUicMessage;
import com.game.qq.structs.QQUicStr;
import com.game.register.structs.UserInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.http.HttpAsyncClient;
import game.core.net.Config.ServerConfig;
import game.core.util.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 14:21
 */
public class QQScript implements IQQScript {

    final Logger logger = LogManager.getLogger(QQScript.class);

    @Override
    public int getId() {
        return ScriptEnum.QQScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }


    /**
     * 检查屏蔽字
     *
     * @param player
     * @param content
     * @param uic
     */
    void uic(Player player, String content, QQUic uic) throws UnsupportedEncodingException {

        UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());

        SortedMap<String, Object> params = new TreeMap<>();
        params.put(QQConfig.OpenId.getKey(), userInfo.getExtension().get(QQConfig.OpenId.getKey()));
        params.put(QQConfig.OpenKey.getKey(), userInfo.getExtension().get(QQConfig.OpenKey.getKey()));
        params.put(QQConfig.AppId.getKey(), ServerConfig.getQqConfig().getAppId());
        String sign = QQConfig.createSign(params, ServerConfig.getQqConfig().getAppKey());
        params.put(QQConfig.Sign.getKey(), sign);

        List<QQUicStr> check = new ArrayList<>();
        check.add(new QQUicStr(content, uic.getType()));
        HttpEntity entity = new StringEntity(JsonUtils.toJSONString(check));

        String query = linkQuery(params);
        String url = ServerConfig.getQqConfig().getUicFilterUrl() + "?" + query;

        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type", "application/json;charset=utf-8");
        post.setEntity(entity);

        HttpAsyncClient.instance.getClient().execute(post, new FutureCallback<HttpResponse>() {

            @Override
            public void completed(HttpResponse response) {
                try {
                    String res = EntityUtils.toString(response.getEntity());
                    QQUicMessage message = JsonUtils.toJavaObject(res, QQUicMessage.class);
                    switch (message.getRet()) {
                        case 0:
                            break;
                        default:
                            logger.error("错误");
                    }
                } catch (Exception e) {
                    logger.error(e, e);
                }

            }

            @Override
            public void failed(Exception e) {

            }

            @Override
            public void cancelled() {

            }
        });
    }

    String linkQuery(SortedMap<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append(k).append("=").append(v).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public void log(Player player, QQLogType type) {
        try {
            if (ServerConfig.getQqConfig().getOpenLog() != 1) {
                return;
            }
            Title title = new Title();
            title.setApp_id(ServerConfig.getQqConfig().getAppId());
            title.setApp_name(ServerConfig.getQqConfig().getAppName());
            title.setRetry_times(0);
            title.setSeq_id("" + IDConfigUtil.getLogId());
            ReqMessage data = new ReqMessage();
            data.setTitle(title);
            List<ReqData> datas = new ArrayList<>();
            ReqDataDetail dataDetail = new ReqDataDetail();
            dataDetail.setIversion("1.0.0");
            dataDetail.setAppid(ServerConfig.getQqConfig().getAppId());
            dataDetail.setUserip(player.getLoginIP());
            dataDetail.setSvrip(ServerConfig.getGameServerIp());
            dataDetail.setTime((int) (System.currentTimeMillis() / 1000));
            dataDetail.setDomain(10);
            dataDetail.setIworldid(GameServer.getInstance().getServerId());
            dataDetail.setOptype(3);
            dataDetail.setActionid(type.getType());

            UserInfo userInfo = Manager.registerManager.getUserInfo(player.getUserId());
            if (userInfo == null) {
                logger.warn("BiQQ userInfo is null uid:{}", player.getUserId());
                return;
            }
            dataDetail.setOpuid(player.getUserId());
            String openId = userInfo.getExtension().get(QQConfig.OpenId.getKey());
            dataDetail.setOpopenid(openId);
//        dataDetail.setTouid();
//        dataDetail.setToopenid();
            dataDetail.setLevel(1);
            dataDetail.setSource("qqgame");
            if (type == QQLogType.logout) {
                dataDetail.setOnlinetime((int) ((TimeUtils.Time() - player.getLastLoginTime()) / 1000));
            }

            datas.add(new ReqData(dataDetail));
            data.setData(datas);

            send(data);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    /**
     * 发送请求
     *
     * @param data
     */
    private void send(ReqMessage data) {
        try {
            //设置当前发送时间
            data.getTitle().setTimestamp((int) (System.currentTimeMillis() / 1000));
            //要发送的body
            String jsonData = JsonUtils.toJSONString(data);

            String sign = CodedUtil.Md5(jsonData + ServerConfig.getQqConfig().getAppKey());
            HttpPost post = new HttpPost(ServerConfig.getQqConfig().getLogUrl());
            post.setHeader("Content-Type", "application/json");
            post.setHeader("signature", sign);
            post.setHeader("version", "1.0");

            HttpEntity entity = new StringEntity(jsonData);
            post.setEntity(entity);
            HttpAsyncClient.instance.getClient().execute(post, new FutureCallback<HttpResponse>() {
                @Override
                public void completed(HttpResponse httpResponse) {
                    try {
                        String res = EntityUtils.toString(httpResponse.getEntity());
                        ResMessage resMessage = JsonUtils.toJavaObject(res, ResMessage.class);
                        int code = resMessage.getData().getCode();
                        if (code != 200) {
                            //异常处理
                            if (code < 1000) {
                                //标准http错误，需要重试
                                int retry = data.getTitle().retryTimes();
                                retry++;
                                if (retry > 5) {
                                    logger.warn("BiQQ 数据上报重试{}次失败，取消重试,data:{}", retry, jsonData);
                                    return;
                                }
                                data.getTitle().setRetry_times(retry);
                                int delayTime = getDelayTime(retry);
                                data.getTitle().setTimestamp(delayTime + (int) (System.currentTimeMillis() / 1000));
                                BIManager.getInstance().addCommand(() -> {
                                    BIManager.getInstance().getBiqqReq().add(data);
                                });
                            } else {
                                //数据处理错误，打印日志
                                logger.error("BiQQ 数据上报错误 code:{},msg:{}", code, jsonData);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void failed(Exception e) {
                    //标准http错误，需要重试
                    int retry = data.getTitle().retryTimes();
                    retry++;
                    if (retry > 5) {
                        logger.warn("BiQQ 数据上报重试{}次失败，取消重试,data:{}", retry, jsonData);
                        return;
                    } else {
                        logger.error("BiQQ 数据上报请求异常，第{}次重试", retry, e);
                    }
                    data.getTitle().setRetry_times(retry);
                    int delayTime = getDelayTime(retry);
                    data.getTitle().setTimestamp(delayTime + (int) (System.currentTimeMillis() / 1000));
                    BIManager.getInstance().addCommand(() -> {
                        BIManager.getInstance().getBiqqReq().add(data);
                    });
                }

                @Override
                public void cancelled() {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private int getDelayTime(int retry) {
        if (retry < 1) {
            retry = 1;
        }
        double v = Math.pow(2, retry);
        return (int) v;
    }

    @Override
    public void retry() {
        List<ReqMessage> ls = BIManager.getInstance().getBiqqReq();
        if (ls.size() > 100) {
            logger.warn("BiQQ retry list size:{}", ls.size());
        }
        int time = (int) (System.currentTimeMillis() / 1000);
        Iterator<ReqMessage> it = ls.iterator();
        while (it.hasNext()) {
            ReqMessage req = it.next();
            if (req.getTitle().timestamp() < time) {
                send(req);
                it.remove();
            }
        }
    }

}
