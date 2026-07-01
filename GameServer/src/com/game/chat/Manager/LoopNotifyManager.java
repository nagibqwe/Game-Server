package com.game.chat.Manager;

import com.game.utils.MessageUtils;
import game.core.util.TimeUtils;
import game.message.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循环公告管理类
 *
 * @author luosv
 * Created on 2018/2/5 0005.
 */
public class LoopNotifyManager {

    private static final Logger LOGGER = LogManager.getLogger(LoopNotifyManager.class);

    //循环最大次数
    private static final int LOOP_MAX_NUM = 3;
    //循环时间间隔(毫秒)
    private static final long TIME_INTERVAL = 5 * 60 * 1000;
    //需要循环的消息<languageId, msg>
    private final ConcurrentHashMap<Integer, ChatMessage.PersonalNotice.Builder> loopMsg = new ConcurrentHashMap<>();
    //消息循环次数记录<languageId, loopNum>
    private final ConcurrentHashMap<Integer, Integer> loopNum = new ConcurrentHashMap<>();
    //消息上次循环时间记录<languageId， loopTime>
    private final ConcurrentHashMap<Integer, Long> loopTime = new ConcurrentHashMap<>();

    public static LoopNotifyManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void addLoopMsg(int languageId, ChatMessage.PersonalNotice.Builder msg) {
        loopMsg.remove(languageId);
        loopMsg.put(languageId, msg);
        loopNum.remove(languageId);
        loopNum.put(languageId, 1);
        loopTime.remove(languageId);
        loopTime.put(languageId, TimeUtils.Time());
    }

    private void updateLoopMsg(int languageId) {
        if (loopMsg.containsKey(languageId)) {
            if (loopNum.containsKey(languageId)) {
                loopNum.put(languageId, loopNum.get(languageId) + 1);
            }
            if (loopTime.containsKey(languageId)) {
                loopTime.put(languageId, TimeUtils.Time());
            }
        }
    }

    private void removeLoopMsg(int languageId) {
        loopMsg.remove(languageId);
        loopNum.remove(languageId);
        loopTime.remove(languageId);
    }

    public void loopNotifyTick() {
        for (Map.Entry<Integer, ChatMessage.PersonalNotice.Builder> entry : loopMsg.entrySet()) {
            if (!loopNum.containsKey(entry.getKey()) || !loopTime.containsKey(entry.getKey())) {
                removeLoopMsg(entry.getKey());
                continue;
            }
            if (loopNum.get(entry.getKey()) >= LOOP_MAX_NUM) {
                removeLoopMsg(entry.getKey());
                continue;
            }
            if (loopTime.get(entry.getKey()) + TIME_INTERVAL > TimeUtils.Time()) {
                continue;
            }
            MessageUtils.send_to_all_player(ChatMessage.PersonalNotice.MsgID.eMsgID_VALUE, entry.getValue().build().toByteArray());
            updateLoopMsg(entry.getKey());
        }
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        LoopNotifyManager processor;

        Singleton() {
            this.processor = new LoopNotifyManager();
        }

        LoopNotifyManager getProcessor() {
            return processor;
        }

    }

}
