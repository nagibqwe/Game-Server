package common.godbook;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.FunctionVariable;
import com.game.bi.manager.BIDefine;
import com.game.godbook.log.AmuletActiveLog;
import com.game.godbook.log.AmuletConditionFinishLog;
import com.game.godbook.script.IGodBookScript;
import com.game.godbook.struct.Amulet;
import com.game.godbook.struct.ConditionInfo;
import com.game.hook.manager.PlayerHookManager;
import com.game.manager.Manager;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.message.GodBook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

public class GodBookScript implements IGodBookScript {

    private static final Logger logger = LogManager.getLogger(GodBookScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GodBookBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initAmulet(Player player) {
       //for (Cfg_Amulet_Bean bean : Cfg_Amulet_Container.GetInstance().getValuees()) {
       //    if (bean.getOpen_condition() == null || bean.getOpen_condition().size() <= 0) {
       //        Amulet amulet = new Amulet(bean.getId());
       //        for (Cfg_AmuletCondition_Bean conditionBean : Cfg_AmuletCondition_Container.GetInstance().getValuees()) {
       //            if (conditionBean.getAmuletId() != bean.getId()) {
       //                continue;
       //            }
       //            ConditionInfo info = new ConditionInfo(conditionBean.getId());
       //            int progress = Manager.controlManager.deal().getFuncProgress(player, conditionBean.getAccomplishments());
       //            boolean complete = Manager.controlManager.deal().checkFuncProgress(player, conditionBean.getAccomplishments());
       //            info.setProgress(progress);
       //            info.setStatus(complete ? ConditionInfo.HAS_COMPLETED : ConditionInfo.UNDER_WAY);
       //            amulet.getAmuletInfo().put(info.getId(), info);
       //        }
       //        player.getGodBookInfo().put(amulet.getId(), amulet);
       //    }
       //}
        //sendAllGodBookInfo(player);
    }

    @Override
    public void onReqActiveAmulet(Player player, GodBook.ReqActiveAmulet messInfo) {
      // int amuletId = messInfo.getAmuletId();
      // Amulet amulet = player.getGodBookInfo().get(amuletId);
      // if (amulet == null || amulet.isActivated()) {
      //     return;
      // }

      // Cfg_Amulet_Bean bean = Cfg_Amulet_Container.GetInstance().getValueByKey(amuletId);
      // if (bean == null) {
      //     return;
      // }

      // //是否任务都已完成
      // boolean complete = amulet.getAmuletInfo().values().stream().allMatch(n -> n.getStatus() == ConditionInfo.HAS_DRAW);
      // if (!complete) {
      //     return;
      // }

      // //是否达到激活条件
      // if (!Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())) {
      //     return;
      // }

      // //激活
      // amulet.setActivated(true);
      // for (int i = 0; i < bean.getActive_skill().size(); i ++) {
      //     if (bean.getActive_skill().get(i).get(0) == player.getCareer()) {
      //         Manager.skillManager.addSkill(player, bean.getActive_skill().get(i).get(1));
      //         break;
      //     }
      // }

      // //符咒经验加成
      // if (bean.getExp_type() > 0) {
      //     Manager.playerHookManager.deal().checkPlayerRateChange(player, PlayerHookManager.AmuletRate);
      // }
      // Manager.controlManager.operate(player, FunctionVariable.Getamulet, 0);
      // sendAllGodBookInfo(player);

      // AmuletActiveLog activeLog = new AmuletActiveLog();
      // activeLog.setPlayer(player);
      // activeLog.setAmuletId(amuletId);
      // LogService.getInstance().execute(activeLog);
      // Manager.biManager.getScript().biGrow(player, 4, 0, BIDefine.GrowActive, 0, amuletId, amuletId);
    }

    @Override
    public void onReqGetReward(Player player, GodBook.ReqGetReward messInfo) {
      // int conditionId = messInfo.getConditonId();
      // Cfg_AmuletCondition_Bean conditionBean = Cfg_AmuletCondition_Container.GetInstance().getValueByKey(conditionId);
      // if (conditionBean == null) {
      //     return;
      // }
      // int amuletId = conditionBean.getAmuletId();
      // Amulet amulet = player.getGodBookInfo().get(amuletId);
      // if (amulet == null) {
      //     logger.error("玩家不存在此符咒信息：" + amuletId);
      //     return;
      // }
      // ConditionInfo conditionInfo = amulet.getAmuletInfo().get(conditionId);
      // if (conditionInfo.getStatus() != ConditionInfo.HAS_COMPLETED) {
      //     logger.error("符咒任务条件未完成(0)或已领取(2): " + conditionInfo.getStatus());
      //     return;
      // }
      // conditionInfo.setStatus(ConditionInfo.HAS_DRAW);

      // //重新计算属性
      // Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.AmuletTask);

      // //返回消息
      // GodBook.ResGetReward.Builder builder = GodBook.ResGetReward.newBuilder();
      // builder.setId(conditionId);
      // MessageUtils.send_to_player(player, GodBook.ResGetReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());

      // AmuletConditionFinishLog finishLog = new AmuletConditionFinishLog();
      // finishLog.setPlayer(player);
      // finishLog.setConditionId(conditionId);
      // LogService.getInstance().execute(finishLog);
    }

    @Override
    public void checkUpdateAmulet(Player player, int type) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GodBook)) {
            return;
        }

        //符咒开启检查
        checkOpenAmulet(player);

        //检查符咒任务条件进度改变
        checkAmuletConditionChange(player);

    }

    /**
     * 检查符咒开启
     */
    private void checkOpenAmulet(Player player) {
      //  boolean open = false;
      //  for (Cfg_Amulet_Bean bean : Cfg_Amulet_Container.GetInstance().getValuees()) {
      //      if (player.getGodBookInfo().containsKey(bean.getId())) {
      //          continue;
      //      }
      //      if (bean.getOpen_condition() == null) {
      //          continue;
      //      }
      //      if (Manager.controlManager.deal().checkFuncProgress(player, bean.getOpen_condition())) {
      //          open = true;
      //          Amulet amulet = new Amulet(bean.getId());
      //          for (Cfg_AmuletCondition_Bean conditionBean :  Cfg_AmuletCondition_Container.GetInstance().getValuees()) {
      //              if (conditionBean.getAmuletId() != amulet.getId()) {
      //                  continue;
      //              }
      //              amulet.getAmuletInfo().put(conditionBean.getId(), new ConditionInfo(conditionBean.getId()));
      //          }
      //          player.getGodBookInfo().put(amulet.getId(), amulet);
      //      }
      //  }
//
      //  if (open) {
      //      sendGodBookInfo(player);
      //  }
    }

    /**
     * 检查符咒任务条件进度改变
     */
    private void checkAmuletConditionChange(Player player) {
       //ConcurrentHashMap<Integer, Amulet> godBookInfo = player.getGodBookInfo();
       //for (Cfg_AmuletCondition_Bean bean : Cfg_AmuletCondition_Container.GetInstance().getValuees()) {
       //    int amuletId = bean.getAmuletId();
       //    //未激活
       //    if (!godBookInfo.containsKey(amuletId)) {
       //        continue;
       //    }
       //    ConditionInfo conditionInfo = godBookInfo.get(amuletId).getAmuletInfo().get(bean.getId());
       //    if (conditionInfo == null) {
       //        logger.error("不太可能走到这，除非配置表新增了");
       //        conditionInfo = new ConditionInfo(bean.getId());
       //        godBookInfo.get(amuletId).getAmuletInfo().put(bean.getId(), new ConditionInfo(bean.getId()));
       //    }

       //    //已完成或已领取
       //    if (conditionInfo.getStatus() == ConditionInfo.HAS_DRAW || conditionInfo.getStatus() == ConditionInfo.HAS_COMPLETED) {
       //        continue;
       //    }

       //    //检查完成状态和进度
       //    boolean complete = Manager.controlManager.deal().checkFuncProgress(player, bean.getAccomplishments());
       //    int progress = Manager.controlManager.deal().getFuncProgress(player, bean.getAccomplishments());

       //    //刷新进度
       //    refreshConditionProgress(player, conditionInfo, complete, progress);
       //}
    }

    /**
     * 刷新进度
     */
    private void refreshConditionProgress(Player player, ConditionInfo conditionInfo, boolean complete, int progress) {
        //进度改变处理
        if (progress > conditionInfo.getProgress()) {
            conditionInfo.setProgress(progress);
            if (conditionInfo.getStatus() == ConditionInfo.UNDER_WAY && complete) {
                GodBook.ResUpdateCondition.Builder builder = GodBook.ResUpdateCondition.newBuilder();
                builder.setId(conditionInfo.getId());
                MessageUtils.send_to_player(player, GodBook.ResUpdateCondition.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            }
        }
        conditionInfo.setStatus(complete ? ConditionInfo.HAS_COMPLETED : ConditionInfo.UNDER_WAY);
    }

    @Override
    public void sendGodBookInfo(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.GodBook)) {
            return;
        }
        sendAllGodBookInfo(player);
    }

    private void sendAllGodBookInfo(Player player) {
        //发送天书面板信息
        GodBook.ResBookInfo.Builder builder = GodBook.ResBookInfo.newBuilder();
        for (Amulet amulet : player.getGodBookInfo().values()) {
            GodBook.amuletInfo.Builder amuletBuilder = GodBook.amuletInfo.newBuilder();
            amuletBuilder.setId(amulet.getId());
            amuletBuilder.setStatus(amulet.isActivated());
            for (ConditionInfo info : amulet.getAmuletInfo().values()) {
                GodBook.conditionInfo.Builder conditionBuilder = GodBook.conditionInfo.newBuilder();
                conditionBuilder.setId(info.getId());
                conditionBuilder.setProgress(info.getProgress());
                conditionBuilder.setStatus(info.getStatus());
                amuletBuilder.addList(conditionBuilder);
            }
            builder.addAmulets(amuletBuilder);
        }
        MessageUtils.send_to_player(player, GodBook.ResBookInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取符咒经验加成
     */
    @Override
    public int getAmuletExpRate(Player player) {
        int expRate = 0;
       // for (Amulet amulet : player.getGodBookInfo().values()) {
       //     if (amulet.isActivated()) {
       //         Cfg_Amulet_Bean bean = CfgManager.getCfg_Amulet_Container().getValueByKey(amulet.getId());
       //         if (bean != null && bean.getExp_type() > 0) {
       //             expRate += bean.getExp_type();
       //         }
       //     }
       // }
        return expRate;
    }

}
