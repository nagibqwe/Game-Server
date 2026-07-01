package common.title;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Title_Bean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.structs.SynToFightType;
import com.game.structs.ItemChangeAction;
import com.game.title.log.TitleChangeLog;
import com.game.title.script.ITitleScript;
import com.game.title.structs.TitleData;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.TitleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TitleScript implements ITitleScript {

    private static final Logger logger = LogManager.getLogger(TitleScript.class);

    // 1、代表境界
    public static final int TYPE_STATE = 1;
    // 2、代表成就
    public static final int TYPE_CHENGJIU = 2;
    // 3、代表节日
    public static final int TYPE_JIERI = 3;
    // 4、代表仙缘
    public static final int TYPE_XIANYUAN = 4;
    // 5、代表仙盟福地限时称号
    public static final int TYPE_FUDI = 5;
    // 6、代表运营活动
    public static final int TYPE_ACTIVITY = 6;
    // 7、代表仙盟福地永久称号
    public static final int TYPE_FUDI_FOREVER = 7;

    @Override
    public int getId() {
        return ScriptEnum.TitleBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void sendTitleInfo(Player player) {
        TitleData data = player.getTitleData();
        TitleMessage.ResTitleInfo.Builder builder = TitleMessage.ResTitleInfo.newBuilder();
        TitleMessage.title.Builder wear = TitleMessage.title.newBuilder();
        if (!data.getTitleList().containsKey(data.getWearId())) {
            data.setWearId(0);
        }
        wear.setId(data.getWearId());
        wear.setRemainTime(data.getTitleList().getOrDefault(data.getWearId(), 0));
        builder.setWear(wear);
        for (Map.Entry<Integer, Integer> entry : data.getTitleList().entrySet()) {
            TitleMessage.title.Builder title = TitleMessage.title.newBuilder();
            title.setId(entry.getKey());
            title.setRemainTime(entry.getValue());
            builder.addList(title);
        }
        MessageUtils.send_to_player(player, TitleMessage.ResTitleInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void titleTimeOutCheck(Player player) {
        TitleData data = player.getTitleData();
        Iterator<Map.Entry<Integer, Integer>> iterator = data.getTitleList().entrySet().iterator();
        boolean changed = false;
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> entry = iterator.next();
            if (entry.getValue() == 0) {
                continue;
            }
            if (entry.getValue() <= TimeUtils.Time() / 1000) {
                //穿戴的称号过期了，直接走卸下
                if (entry.getKey() == data.getWearId()) {
                    onReqDownTitle(player, data.getWearId());
                }
                iterator.remove();
                changed = true;
            }
        }

        if (changed) {
            //重新计算属性
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);
            sendTitleInfo(player);
        }
    }

    /**
     * 卸载称号
     *
     * @param player
     * @param id
     */
    @Override
    public void uninstallTitle(Player player, int id) {
        TitleData data = player.getTitleData();
        if (!data.getTitleList().containsKey(id))
            return;

        if (data.getWearId() == id)
            onReqDownTitle(player, id);

        data.getTitleList().remove(id);

        // 重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);
        sendTitleInfo(player);
    }

    /**
     * 卸裁某种类型的称号
     *
     * @param player
     * @param type
     */
    @Override
    public void uninstallTitleByType(Player player, int type) {
        TitleData data = player.getTitleData();
        Iterator<Map.Entry<Integer, Integer>> it = data.getTitleList().entrySet().iterator();
        boolean change = false;
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();

            Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(entry.getKey());
            if (bean == null) continue;
            if (bean.getType() != type) continue;

            onReqDownTitle(player, entry.getKey());
            it.remove();
            change = true;
        }
        if (change) {
            // 重新计算属性
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);
            Manager.playerManager.manager().syncPlayerWorldInfo(player, false);
        }
        if (player.isOnline()) {
            sendTitleInfo(player);
        }
    }

    @Override
    public void useTitleItem(Player player, int title, int num, int reason) {
        Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(title);
        if (bean == null) {
            logger.error("Cfg_TitleBean配置表不存在：" + title);
            return;
        }
        int deadline = 0;
        TitleData data = player.getTitleData();
        if (data.getTitleList().containsKey(bean.getId())) {
            int lastTime = data.getTitleList().get(bean.getId());
            if (lastTime > 0) {
                deadline = lastTime + bean.getTime() * num;
                //已经激活并且不是永久的增加过期时间
                data.getTitleList().put(bean.getId(), deadline);
                writeTitleActiveLog(player, title, title, num, 1, lastTime, deadline);
            }
        } else {
            deadline = bean.getTime() == 0 ? 0 : (int) (TimeUtils.Time() / 1000) + bean.getTime() * num;
            data.getTitleList().put(bean.getId(), deadline);
            //重新计算属性
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);
            writeTitleActiveLog(player, title, title, num, 0, -1, deadline);
        }
        //返回称号激活消息
        sendTitleActiveInfo(player, title, deadline);

        Manager.controlManager.operate(player, FunctionVariable.Playertitle, title);

        Manager.biManager.getScript().biRealm(player, 1, 3, 0, bean.getId(), "");
        long itemId = IDConfigUtil.getId();
        Manager.backpackManager.manager().writeItemLog(player, itemId, bean.getId(), 0,
                1, reason, ItemChangeAction.ADD, IDConfigUtil.getLogId(), 0, 0, -1);
        Manager.backpackManager.manager().writeItemLog(player, itemId, bean.getId(), 1,
                0, ItemChangeReason.ActiveTitleDec, ItemChangeAction.REMOVE, IDConfigUtil.getLogId(), 0, 0, -1);
    }

    @Override
    public void onReqActiveTitle(Player player, int id, boolean cost) {
        TitleData data = player.getTitleData();
        if (data.getTitleList().containsKey(id)) {
//            logger.error("称号已激活！id={}", id);
            return;
        }
        Cfg_Title_Bean bean = CfgManager.getCfg_Title_Container().getValueByKey(id);
        if (bean == null) {
            logger.info("Cfg_TitleBean配置表不存在 title={} player={}", id, player);
            return;
        }
        if (cost) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, id, 1, ItemChangeReason.ActiveTitleDec, IDConfigUtil.getLogId())) {
                logger.info("称号物品不足 title={} player={}", id, player);
                return;
            }
        }
        int deadline = bean.getTime() == 0 ? 0 : (int) (TimeUtils.Time() / 1000) + bean.getTime();
        data.getTitleList().put(id, deadline);
        //重新计算属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.TITLE);

        //返回激活消息
        sendTitleActiveInfo(player, id, deadline);

        Manager.controlManager.operate(player, FunctionVariable.Playertitle, id);

        logger.info("称号激活 title={} player={}", id, player);

        //称号激活日志
        writeTitleActiveLog(player, id, id, 1, 0, -1, deadline);
        Manager.biManager.getScript().biRealm(player, 1, 3, 0, bean.getId(), "");
        long itemId = IDConfigUtil.getId();
        Manager.backpackManager.manager().writeItemLog(player, itemId, bean.getId(), 1,
                0, ItemChangeReason.ActiveTitleDec, ItemChangeAction.REMOVE, IDConfigUtil.getLogId(), 0, 0, -1);
    }

    /**
     * 返回称号激活的信息
     */
    private void sendTitleActiveInfo(Player player, int id, int deadline) {
        TitleMessage.ResActiveTitleResult.Builder builder = TitleMessage.ResActiveTitleResult.newBuilder();
        TitleMessage.title.Builder title = TitleMessage.title.newBuilder();
        title.setId(id);
        title.setRemainTime(deadline);
        builder.setInfo(title);
        MessageUtils.send_to_player(player, TitleMessage.ResActiveTitleResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onReqWearTitle(Player player, int id) {
        TitleData data = player.getTitleData();
        if (data.getWearId() == id) {
            return;
        }
        if (!data.getTitleList().containsKey(id)) {
            return;
        }
        data.setWearId(id);

        TitleMessage.ResWearTitle.Builder builder = TitleMessage.ResWearTitle.newBuilder();
        TitleMessage.title.Builder title = TitleMessage.title.newBuilder();
        title.setId(id);
        title.setRemainTime(data.getTitleList().get(id));
        builder.setInfo(title);
        MessageUtils.send_to_player(player, TitleMessage.ResWearTitle.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        broadTitleChange(player, id);
    }

    @Override
    public void onReqDownTitle(Player player, int id) {
        TitleData data = player.getTitleData();
        if (data.getWearId() != id) {
            return;
        }
        data.setWearId(0);

        TitleMessage.ResDownTitle.Builder builder = TitleMessage.ResDownTitle.newBuilder();
        builder.setId(id);
        MessageUtils.send_to_player(player, TitleMessage.ResDownTitle.MsgID.eMsgID_VALUE, builder.build().toByteArray());

        broadTitleChange(player, 0);
    }

    /**
     * 广播玩家穿戴称号改变消息
     */
    private void broadTitleChange(Player player, int id) {
        TitleMessage.ResBroadWearTitle.Builder broad = TitleMessage.ResBroadWearTitle.newBuilder();
        broad.setRoleId(player.getId());
        broad.setId(id);
        if (player.playerCrossData.isToFightServer()) {
            Map<Integer, Object> map = new HashMap<>();
            map.put(1, id);
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.TitleChange, map, TitleMessage.ResBroadWearTitle.MsgID.eMsgID_VALUE, broad.build().toByteString());
        } else {
            MessageUtils.send_to_roundPlayer(player, TitleMessage.ResBroadWearTitle.MsgID.eMsgID_VALUE, broad.build().toByteArray());
        }
    }

    /**
     * 称号改变日志
     */
    private void writeTitleActiveLog(Player player, int titleId, int itemId, int num,
                                     int operateType, int lastExpirationTime, int nowExpirationTime) {
        TitleChangeLog changeLog = new TitleChangeLog();
        changeLog.setPlayer(player);
        changeLog.setTitleId(titleId);
        changeLog.setItem(itemId + "_" + num);
        changeLog.setOperateType(operateType);
        changeLog.setLastExpirationTime(lastExpirationTime);
        changeLog.setNowExpirationTime(nowExpirationTime);
        LogService.getInstance().execute(changeLog);
    }
}
