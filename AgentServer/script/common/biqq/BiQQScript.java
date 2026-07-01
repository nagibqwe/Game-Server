package common.biqq;

import com.game.biqq.*;
import com.game.thread.ThreadPoolManager;
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
import java.util.concurrent.TimeUnit;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/17 14:21
 */
public class BiQQScript implements IBiQQScript {

    final Logger log = LogManager.getLogger(BiQQScript.class);

    @Override
    public int getId() {
        return 3;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void logRegister(long userId, String loginIp) {
        try{
            if(ServerConfig.getQqConfig().getOpenLog() != 1){
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
            dataDetail.setUserip(loginIp);
            dataDetail.setSvrip(ServerConfig.getGameServerIp());
            dataDetail.setTime((int)(System.currentTimeMillis()/1000));
            dataDetail.setDomain(10);
            dataDetail.setIworldid(0);
            dataDetail.setOptype(3);
            dataDetail.setActionid(QQLogType.register.getType());

            dataDetail.setOpuid(userId);
//        dataDetail.setOpopenid(openId);
//        dataDetail.setTouid();
//        dataDetail.setToopenid();
            dataDetail.setLevel(1);
            dataDetail.setSource("qqgame");

            datas.add(new ReqData(dataDetail));
            data.setData(datas);

            send(data);
        }catch (Exception e){
            log.error("", e);
        }
    }

    /**
     * 发送请求
     * @param data
     */
    private void send(ReqMessage data){
        try {
            //设置当前发送时间
            data.getTitle().setTimestamp((int)System.currentTimeMillis()/1000);
            //要发送的body
            String jsonData = JsonUtils.toJSONString(data);

            String sign = CodedUtil.Md5(jsonData + ServerConfig.getQqConfig().getAppKey());
            HttpPost post = new HttpPost(ServerConfig.getQqConfig().getLogUrl());
            post.setHeader("Content-Type", "application/json");
            post.setHeader("signature",sign);
            post.setHeader("version","1.0");

            HttpEntity entity = new StringEntity(jsonData);
            post.setEntity(entity);
            HttpAsyncClient.instance.getClient().execute(post, new FutureCallback<HttpResponse>(){
                @Override
                public void completed(HttpResponse httpResponse) {
                    try {
                        String res = EntityUtils.toString(httpResponse.getEntity());
                        ResMessage resMessage = JsonUtils.toJavaObject(res, ResMessage.class);
                        int code = resMessage.getData().getCode();
                        if(code != 200){
                            //异常处理
                            if(code < 1000){
                                //标准http错误，需要重试
                                int retry = data.getTitle().retryTimes();
                                retry++;
                                if(retry > 5){
                                    log.warn("BiQQ 数据上报重试{}次失败，取消重试,data:{}", retry, jsonData);
                                    return;
                                }
                                data.getTitle().setRetry_times(retry);
                                int delayTime = getDelayTime(retry);
                                data.getTitle().setTimestamp(delayTime + (int)(System.currentTimeMillis()/1000));
                                ThreadPoolManager.getInstance().getExecutorService().schedule(()->{
                                    send(data);
                                }, delayTime, TimeUnit.SECONDS);
                            }else{
                                //数据处理错误，打印日志
                                log.error("BiQQ 数据上报错误 code:{},msg:{}", code, jsonData);
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
                    if(retry > 5){
                        log.warn("BiQQ 数据上报重试{}次异常，取消重试,data:{}", retry, jsonData);
                        return;
                    }else{
                        log.error("BiQQ 数据上报请求异常,第{}次重试", retry, e);
                    }
                    data.getTitle().setRetry_times(retry);
                    int delayTime = getDelayTime(retry);
                    data.getTitle().setTimestamp(delayTime + (int)(System.currentTimeMillis()/1000));
                    ThreadPoolManager.getInstance().getExecutorService().schedule(()->{
                        send(data);
                    }, delayTime, TimeUnit.SECONDS);
                }

                @Override
                public void cancelled() {
                    log.warn("BiQQ 数据上报取消,data:{}", jsonData);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private int getDelayTime(int retry) {
        if(retry < 1){
            retry = 1;
        }
        double v = Math.pow(2, retry);
        return (int) v;
    }

}
