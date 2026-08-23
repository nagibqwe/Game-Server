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
 * Скрипт обработки и отправки ошибок
 *
 * @author admin
 */
public class ErrorLogReport implements IScript, IErrorReportScript {

    private static final Logger log = LogManager.getLogger(ErrorLogReport.class);

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
            int no = 3;//Другие ошибки
            if (eelog.getErrorType().equalsIgnoreCase("DBERROR")) {//Ошибка операции с БД
                no = 1;
            } else if (eelog.getErrorType().equalsIgnoreCase("confignotfound")) {//Ошибка получения конфигурации
                no = 2;
            } else if (eelog.getErrorType().equalsIgnoreCase("playerAttMaxWarning")) {
                no = 4;
            }
            //Отправка при сборе
            sendLog(no, eelog.getErrorType(), eelog.getErrorValue(), 1);
        } else {//Отчёт об ошибках предметов
            ErrorLog itemlog = elog.getLog();
            if (CfgManager.getCfg_Item_warning_Container().getValueByKey(itemlog.getType()) == null) {
                return;
            }
            Cfg_Item_warning_Bean bean = CfgManager.getCfg_Item_warning_Container().getValueByKey(itemlog.getType());

            //Нет в таблице сбора
            if (bean == null) {
                return;
            }

            dealItemLog(elog.getKey(), itemlog.getType(), itemlog.getValue(), bean.getPlayerLimit());
            //Обработка общих данных сервера
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
            list.put(itemId, itemlog);
        }

        value = itemlog.getLastValue();
        itemlog.setTimes(itemlog.getTimes() + 1);
        //Сброс текущего значения
        itemlog.setLastValue(itemlog.getLastValue() + itemlog.getValue());

        long count = itemlog.getLastValue();

        //Отправка при превышении порога
        if (count >= limit) {
            itemlog.setTimes(itemlog.getTimes() + 1);

            Long now = TimeUtils.Time();
            //Если прошло менее 10 минут с последней отправки, пропускаем
            if (now - itemlog.getLastSendTime() < 10 * 60 * 1000) {
                return;
            }

            itemlog.setLastSendTime(now);
            sendLog(itemlog.getType(), key + " ID предмета:" + itemlog.getType(), "Текущее количество: " + count + " Предыдущее значение: " + value, itemlog.getValue());
        }
    }

    /**
     * Отправка информации об ошибке
     *
     * @param i           Код ошибки
     * @param type        Тип ошибки
     * @param errorValue  Текст ошибки
     * @param lastValue   Значение счётчика
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

        log.error("Тип ошибки:"+type+" контекст=" + content + ", код=" + code + ", результат = " + result.toString());
    }

    /**
     * Удаление кэша предметов игрока
     *
     * @param roleId ID персонажа
     */
    @Override
    public void removeErrorLog(long roleId) {
        String key = "" + roleId;
        //Удаление кэша игрока
        if (ErrorLogThread.getErrorlog().containsKey(key)) {
            ErrorLogThread.getErrorlog().remove(key);
        }

        //Очистка кэша ежедневных накоплений при очистке кэша игрока
        if (Manager.commercializeManager.getDailyAccRechargeRecord().containsKey(roleId)) {
            Manager.commercializeManager.getDailyAccRechargeRecord().remove(roleId);
        }
    }

}