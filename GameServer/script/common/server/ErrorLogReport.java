/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.server;
import com.data.CfgManager;
import com.data.bean.Cfg_Item_warning_Bean;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.script.IErrorReportScript;
import com.game.server.structs.ErrorExceptionLog;
import com.game.server.structs.ErrorInfo;
import com.game.server.structs.ErrorLog;
import com.game.server.thread.ErrorLogThread;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.CodedUtil;
import game.core.util.HttpUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 错误处理汇报脚本
 *
 * @author admin
 */
public class ErrorLogReport implements IScript, IErrorReportScript {

    private static final Logger log = LogManager.getLogger(ErrorLogReport.class);

    //物品的数量超过了这个值后
//    private static long MAX_DEFINE = 10000;

    @Override
    public int getId() {
        return ScriptEnum.ErrorLogReportBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void addlog(ErrorInfo errorInfo) {
        ErrorInfo elog = errorInfo;
        if (elog.getLog() instanceof ErrorExceptionLog) {
            ErrorExceptionLog eelog = (ErrorExceptionLog) elog.getLog();
            int no = 3;//其它错误异常
            if (eelog.getErrorType().equalsIgnoreCase("DBERROR")) {//数据操作异常
                no = 1;
            } else if (eelog.getErrorType().equalsIgnoreCase("confignotfound")) {//配置文件获取异常
                no = 2;
            } else if (eelog.getErrorType().equalsIgnoreCase("playerAttMaxWarning")) {
                no = 4;
            }
            //收集就发送
            sendLog(no, eelog.getErrorType(), eelog.getErrorValue(), 1);
        } else {//此处是物品的错误报告
            ErrorLog itemlog = elog.getLog();
            if (CfgManager.getCfg_Item_warning_Container().getValueByKey(itemlog.getType()) == null) {
                return;
            }
            Cfg_Item_warning_Bean bean = CfgManager.getCfg_Item_warning_Container().getValueByKey(itemlog.getType());

            //不在收集的要求数据表中
            if (bean == null) {
                return;
            }

            dealItemLog(elog.getKey(), itemlog.getType(), itemlog.getValue(), bean.getPlayerLimit());
            //服务器总次数据处理
            String serverKey = "Server_" + itemlog.getType();
            dealItemLog(serverKey, itemlog.getType(), itemlog.getValue(), bean.getServerLimit());
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
                itemlog.setLastValue(0L);
                itemlog.setTimes(0);
            }
        } else {
            itemlog = new ErrorLog();
            itemlog.setType(itemId);
            itemlog.setValue(value);
            itemlog.setLastValue(0L);
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

        int serverId = GameServer.getInstance().getServerId();
        String plat = GameServer.getInstance().getServerPlatform();
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

        log.error("errorType:"+type+"context=" + content + ",code=" + code + ", result = " + result.toString());
    }

    /**
     * 移除玩家的物品缓存信息
     *
     * @param roleId 角色ID
     */
    @Override
    public void removeErrorLog(long roleId) {
        String key = "" + roleId;
        //移除玩家的缓存数据
        if (ErrorLogThread.getErrorlog().containsKey(key)) {
            ErrorLogThread.getErrorlog().remove(key);
        }

        //玩家缓存清理时也清理掉缓存的rewardKey
//        if (Manager.activityManager.getRoleRewards().containsKey(roleId)) {
//            Manager.activityManager.getRoleRewards().remove(roleId);
//        }

        //清理每日累充数据
        if (Manager.commercializeManager.getDailyAccRechargeRecord().containsKey(roleId)) {
            Manager.commercializeManager.getDailyAccRechargeRecord().remove(roleId);
        }

        //处理七天排名
        //manager.newServerActivityManager.deal().playerDeleteRoleDeal(roleId);
    }

}
