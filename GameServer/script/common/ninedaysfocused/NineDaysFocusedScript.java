package common.ninedaysfocused;

import com.data.MessageString;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.ninedaysfocused.manager.NineDaysFocusedManager;
import com.game.ninedaysfocused.script.INineDaysFocused;
import com.game.ninedaysfocused.structs.NineDaysTask;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.data.ItemChangeReason;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.NineDaysFocusedMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


/**
 * Created by ClC on 2019/7/22.
 */
public class NineDaysFocusedScript implements IScript ,INineDaysFocused {

    private static final Logger LOG = LogManager.getLogger(NineDaysFocusedScript.class);

    @Override
    public int getId() {
        return ScriptEnum.NineDaysFocusedBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 报名匹配
     */
    @Override
    public void OnReqNineDaysFocusedEnter(Player player) {



        if ( player.playerCrossData.isToFightServer()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EXISTINCLONE);//("您已经在副本中。"));
            return;
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        if (map.getId() > 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.EXISTINCLONE);//("您已经在副本中。"));
            return;
        }
        //是否是战斗时间
        if (!isBattleTime()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CrossJJC_NotBattleTime);
            LOG.info("活动未开启");
            return;
        }

        NineDaysFocusedMessage.G2PReqApplyNieDaysFocused.Builder msg = NineDaysFocusedMessage.G2PReqApplyNieDaysFocused.newBuilder();
        NineDaysFocusedMessage.CrossHero.Builder hero = NineDaysFocusedMessage.CrossHero.newBuilder();

        hero.setName(player.getName());
        hero.setRoleId(player.getId());
        hero.setServerName(player.getShowSid());
        hero.setCareer(player.getCareer());
        hero.setDegree(player.getXsGrade());
        hero.setLv(player.getLevel());
        hero.setFightPoint((int)player.getFightPoint());
        Equip weapon = Manager.equipManager.getEquipByType(player, 1);
        hero.setWeaponId(weapon == null ? 0 : weapon.getItemModelId());
        hero.setWingId(player.getWing().getCurrentModelId());
        hero.setEquipMinStar(Manager.equipManager.getClothesStar(player));
        hero.setFashionBodyId(0);
        hero.setFashionBodyId(player.getNewFashionData().getBodyID());
        hero.setFashionWeaponId(player.getNewFashionData().getWeaponID());


        MessageUtils.send_to_public(NineDaysFocusedMessage.G2PReqApplyNieDaysFocused.MsgID.eMsgID_VALUE, hero.build().toByteArray());


        //发送公共服匹配

    }

    /**
     * 取消报名匹配
     */
    public void OnReqNineDaysFocusedCancelEnter(Player player) {
        //发送公共服匹配
    }

    public void onStart() {
        NineDaysFocusedManager.getInstance().setIsOpen(true);
    }

    public void onOver() {
        NineDaysFocusedManager.getInstance().setIsOpen(false);
    }

    //活动是否开启
    private boolean isBattleTime() {
        if (NineDaysFocusedManager.getInstance().getIsOpen()) {
            return false;
        }
        return true;
    }


    public void getTaskReward(Player player ,int taskID)
    {

         NineDaysTask task = player.getPersonalData().getTaskList().get(taskID);
         if (task.getIsGet())
         {
             LOG.info("已经领取了改任务 " + task.getTaskID());
             return;
         }
         if (task.getAlreadyStage()<task.getTargetStage()){
             LOG.info("目标未完成 " + task.getTaskID());
             return;
         }
        task.setGet(true);

        //发奖  刷新任务

    }
    /**
     * 刷新奖励数据
     */
    public void  onF2GSynchroRewardData(NineDaysFocusedMessage.F2GSynchrodata data)
    {

        Player player = PlayerManager.getInstance().getPlayerCache(data.getRoleId());
        if (player == null)
        {
            LOG.info("玩家不在线");
            return;
        }
        player.getPersonalData().setGatherRewardNum(data.getGatherRewardNum());
        player.getPersonalData().setBossRewardNum(data.getBossRewardNum());

        List<List<Integer>> itemDrops = Manager.dropManager.deal().getItemDrops(player, data.getDropId());
        List<Item> list = Item.createItems(itemDrops, 1);
        long actionId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().addItems(player, list, ItemChangeReason.DropByGatherGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System,
                    MessageString.BAG_FULL_MAIL_TITLE, MessageString.BAG_FULL_MAIL_CONTENT, list, ItemChangeReason.DropByGatherGet, actionId);
        }
    }
    /**
     * 刷新任务数据
     */
    public void  onF2GSynchroTaskData(NineDaysFocusedMessage.F2GSynchrotask data)
    {
        Player player = PlayerManager.getInstance().getPlayerCache(data.getRoleId());
        if (player == null)
        {
            LOG.info("玩家不在线");
            return;
        }
        NineDaysTask task =  player.getPersonalData().getTaskList().get(data.getTaskdata().getTaskID());
        task.setAlreadyStage(data.getTaskdata().getAlreadyStage());
        task.setTargetStage(data.getTaskdata().getTargetStage());
    }
}
