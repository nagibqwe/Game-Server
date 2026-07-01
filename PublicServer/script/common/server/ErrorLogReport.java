/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.server;

import com.game.script.ScriptEnum;
import com.game.server.errorlog.ErrorExceptionLog;
import com.game.server.errorlog.ErrorInfo;
import com.game.server.errorlog.ErrorLog;
import com.game.server.errorlog.ErrorLogThread;
import com.game.server.script.IErrorLogScript;
import game.core.net.Config.ServerConfig;
import game.core.util.CodedUtil;
import game.core.util.HttpUtils;
import game.core.util.TimeUtils;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 错误处理汇报脚本
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ErrorLogReport implements IErrorLogScript {

    private static final Logger log = LogManager.getLogger(ErrorLogReport.class);

    //物品的数量超过了这个值后
    private static long MAX_DEFINE = 10000;

    @Override
    public int getId() {
        return ScriptEnum.ErrorLogReport;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void addlog(ErrorInfo elog) {
        if (elog.getLog() instanceof ErrorExceptionLog) {
            ErrorExceptionLog eelog = (ErrorExceptionLog) elog.getLog();
            int no = 3;//其它错误异常
            if (eelog.getErrorType().equalsIgnoreCase("DBERROR")) {//数据操作异常
                no = 1;
            } else if (eelog.getErrorType().equalsIgnoreCase("confignotfound")) {//配置文件获取异常
                no = 2;
            }
            //收集就发送
            sendLog(no, eelog.getErrorType(), eelog.getErrorValue(), 1);
        } else {//此处是物品的错误报告
            ErrorLog itemlog = elog.getLog();
//            Cfg_item_warningBean bean = Manager.gameDataManager.Cfg_item_warningContainer.GetValueByKey(itemlog.getType());
//
//            //不在收集的要求数据表中
//            if (bean == null) {
//                return false;
//            }
//
//            dealItemLog(elog.getKey(), itemlog.getType(), itemlog.getValue(), bean.getPlayerLimit());
//            //服务器总次数据处理
//            String serverKey = "Server_" + itemlog.getType();
//            dealItemLog(serverKey, itemlog.getType(), itemlog.getValue(), bean.getServerLimit());
        }
    }

    private void dealItemLog(String key, int itemId, long value, long limit) {
        if (limit < 1) {
            return;
        }

        ConcurrentHashMap<Integer, ErrorLog> list;
        if (ErrorLogThread.getErrorlog().containsKey(key)) {
            list = ErrorLogThread.getErrorlog().get(key);
            //ErrorLogThread.getErrorlog().put("item"+ elog.getKey(), elog.getLog());
        } else {
            list = new ConcurrentHashMap<>();
            ErrorLogThread.getErrorlog().put(key, list);
        }
        ErrorLog itemlog;

        if (list.containsKey(itemId)) {
            itemlog = list.get(itemId);
            itemlog.setValue(value);
            if (itemlog.getLastValue() == null) {
                itemlog.setLastValue(0l);
                itemlog.setTimes(0);
            }
        } else {
            itemlog = new ErrorLog();
            itemlog.setType(itemId);
            itemlog.setValue(value);
            itemlog.setLastValue(0l);
            itemlog.setTimes(0);
            itemlog.setLastSendTime(0);
            list.put(itemId, itemlog);//没有就加入进去
        }

        value = itemlog.getLastValue();
        itemlog.setTimes(itemlog.getTimes() + 1);
        //重置一下当前值
        itemlog.setLastValue(itemlog.getLastValue() + itemlog.getValue());

        long count = itemlog.getLastValue();

        //异常标准发送
        if (count >= limit) { //不处理经验的增长
            itemlog.setTimes(itemlog.getTimes() + 1);

            Long now = TimeUtils.Time();
            //如果小于上一次发送的时间则发送
            if (now - itemlog.getLastSendTime() < 10 * 60 * 1000) {
                return;
            }

            itemlog.setLastSendTime(now);
            sendLog(itemlog.getType(), key + "的物品id:" + itemlog.getType(), "本次产生的数量值为:" + count + " 原来量：" + value, itemlog.getValue());
        }
    }

    /**
     * 发送错误日志信息
     *
     * @param i 错误编号
     * @param type 错误类型
     * @param errorValue 错误信息
     * @param lastValue 计量值
     */
    private void sendLog(int i, String type, String errorValue, long lastValue) {

        int serverId = ServerConfig.getServerId();
        String plat = ServerConfig.getServerPlatform();
        String mkey = type;
        String content = errorValue;
        long last = lastValue;

        StringBuilder sb = new StringBuilder();
        sb.append("serverId=").append(serverId).append("platform").append(plat).append("type=").append(i).append("mKey=").append(mkey).append("content=").append(content).append("lastValue=").append(last);
        sb.append("lsbGameKey201512121419");

        String md5 = CodedUtil.Md5(sb.toString()).toLowerCase();

        sb = new StringBuilder();
        sb.append("serverId=").append(serverId).append("&platform=").append(plat).append("&type=").append(i).append("&mKey=").append(mkey).append("&content=").append(content).append("&lastValue=").append(last);
        sb.append("&sign=").append(md5);

        String httpurl = ServerConfig.getErrorLogUrl();

        StringBuilder result = new StringBuilder();
        int code = HttpUtils.sendPost(httpurl + "/error/addlog", sb.toString(), result);

        log.info("context=" + content + ",code=" + code + ", result = " + result.toString());
    }

    /**
     * 移除玩家的物品缓存信息
     *
     * @param roleId 角色ID
     */
    public void removeErrorLog(Object roleId) {
        String key = "" + (long) roleId;
        //移除玩家的缓存数据
        if (ErrorLogThread.getErrorlog().containsKey(key)) {
            ErrorLogThread.getErrorlog().remove(key);
        }
    }
}
