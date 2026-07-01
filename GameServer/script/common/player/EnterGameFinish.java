package common.player;


import com.game.friend.struct.Relation;
import com.game.manager.Manager;
import com.game.player.script.IEnterGameFinish;
import com.game.player.structs.Player;
import com.game.roleLog.RoleUpdateLogService;
import com.game.roleLog.UpdateThread;
import com.game.roleLog.script.IRoleUpdateScript;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import game.core.dblog.ColumnInfo;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.*;

/**
 * @author admin
 */
public class EnterGameFinish implements IScript, IRoleUpdateScript, IEnterGameFinish {

    private static final Logger logger = LogManager.getLogger(EnterGameFinish.class);

    @Override
    public int getId() {
        return ScriptEnum.PlayerEnterGameFinishBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void EnterGameFinish(Player player) {
        try {
            //日常数据
            Manager.dailyActiveManager.deal().sendDailyActivePanelInfo(player);

            Manager.playerHookManager.deal().onReqHookSetInfoHandler(player);
            //离线挂机检查
            Manager.playerHookManager.deal().stopOfflineHook(player);

            //天书信息
            Manager.godBookManager.deal().sendGodBookInfo(player);

            //成就数据
            Manager.achievementManager.deal().sendAchievementInfo(player);

            //称号数据
            Manager.titleManager.deal().sendTitleInfo(player);

            //发送好友数据
            Manager.friendManager.deal().getRelationList(player, Relation.RelationType_Friend.getValue());

            //设置反馈信息
            Manager.settingManager.deal().playerOnline(player);

            //有奖问答
            Manager.questionnaireManager.deal().onlineDeal(player);

            //福地boss
            Manager.guildActivityManager.deal().openAllBossPanel(player);
            Manager.guildActivityManager.deal().checkFudiBossRedPoint(player);

            Manager.luckyDrawManager.deal().onPlayerOnline(player);

            //屏蔽字
            Manager.chatManager.deal().sendForbidWord(player);

            //仙盟boss红点
            Manager.guildActivityManager.deal().syncGuildBossOCTime(player);

            //发送充值数据
            Manager.rechargeManager.playerOnLine(player);

            //发送心跳数据
            Manager.heartManager.sendSpeedUpCheckMsg(player);

            sendOpenServerTime(player);

//            manager.guildsManager.playerOnLine(player);

            //升级红点提示
            //Manager.playerManager.deal(ScriptEnum.ChangeJobBaseScript).checkUpLevelLimit(player);

            //境界任务
            Manager.stateVipManager.playerOnLine(player);

            //上线发送标签库信息
            Manager.activityManager.deal().sendActivityTagInfo(player);
            //登录加载运营活动信息
            Manager.activityManager.deal().playerOnline(player);

            //开服狂欢
            Manager.openServerAcManager.deal().initRevel(player);
            Manager.openServerAcManager.deal().checkTimeOver(player);
            Manager.openServerAcManager.deal().onReqFirstKillPanel(player, true);
            Manager.openServerAcManager.deal().sendNewServerActInfo(player);

            //成长之路
            Manager.openServerAcManager.deal().initGrowUp(player);

            //开服活动
            Manager.openServerAcManager.deal().initOpenServerSpec(player);

            //开服活动预告
            Manager.openServerAcManager.deal().loginFreeDailyReward(player);

            //假拍卖行
            Manager.auctionManager.manager().auctionOnline(player);

            //实名认证
            Manager.playerManager.certifyScript().playerOnline(player);

            //礼物赠送记录
            Manager.playerManager.managerExt().onReqGetGiftLog(player, 1);

            //V4返利
            Manager.openServerAcManager.v4Rebate().onLineInit(player);

            //充值返利
            Manager.rechargeRebateManager.deal().rechargeRebate(player);

            //挂机找回时间
            Manager.playerHookManager.deal().onlineSendHookFindTime(player);

        } catch (Exception ex) {
            //manager.playerManager.quitGame(player.getIosession(), PlayerManager.QuitGame_GM);
            logger.error(ex, ex);
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("EnterGameFinish:" + ex, Arrays.toString(ex.getStackTrace()));
        }
    }

    private void sendOpenServerTime(Player player) {
        PlayerMessage.ResOpenServerTime.Builder builder = PlayerMessage.ResOpenServerTime.newBuilder();
        builder.setTime(TimeUtils.getOpenServerTime());
        MessageUtils.send_to_player(player, PlayerMessage.ResOpenServerTime.MsgID.eMsgID_VALUE, builder.build().toByteArray());

    }

    @Override
    public void OndealSave(String Threadname, Connection connection, List<Player> updates, int fieldNum) {
        if (updates.size() > 0) {
            String buildUpdateSqlExpress = buildUpdateSqlExpress();
            String buildInsert = buildInsertSqlExpress();
            try {
                PreparedStatement insertStmt = connection.prepareStatement(buildInsert);
                PreparedStatement updateStmt = connection.prepareStatement(buildUpdateSqlExpress);
                for (Player player : updates) {
                    UpdateThread.buildParam(player, updateStmt);
                    //TODO 在新增系统的时候修改此处的值，改值为目前系统值+1；
                    updateStmt.setLong(fieldNum + 1, player.getId());
                    if (updateStmt.executeUpdate() == 0) {
                        UpdateThread.buildParam(player, insertStmt);
                        logger.error("角色" + player.getName() + "(" + player.getId() + ")执行更新失败重置插入标记");
                        if (insertStmt.executeUpdate() == 0) {
                            logger.error("角色" + player.getName() + "(" + player.getId() + ")执行插入时重置插入标记");
                            RoleUpdateLogService.getInstance().updateRoleDate(player.getId());
                        }
                    }
                }
                if (updates.size() > 0) {
                    logger.error("保存数据线程" + Threadname + " : 执行更新" + updates.size() + "条");
                }
            } catch (Exception e) {
                logger.error("执行更新的时玩家出错了", e);
            }
        }

    }

    private String buildUpdateSqlExpress() {
        return "update rolestate set " + buildExpress() + " where roleId=?";
    }

    private String buildInsertSqlExpress() {
        return "insert into rolestate set " + buildExpress();
    }

    private String buildExpress() {
        StringBuilder sb = new StringBuilder();
        List<ColumnInfo> buildFields = UpdateThread.buildFields();
        for (ColumnInfo info : buildFields) {
            sb.append(info.getName()).append("=?,");
        }
        String express = sb.toString();
        if (express.endsWith(",")) {
            express = express.substring(0, express.length() - 1);
        }
        return express;
    }

    @Override
    public void OndealCreateSave(String Threadname, Connection connection, Player pp, int fieldNum) {
        if (pp != null) {
            String buildUpdateSqlExpress = buildUpdateSqlExpress();
            String buildInsert = buildInsertSqlExpress();
            try {
                PreparedStatement insertStmt = connection.prepareStatement(buildInsert);
                PreparedStatement updateStmt = connection.prepareStatement(buildUpdateSqlExpress);
                UpdateThread.buildParam(pp, updateStmt);
                //TODO 在新增系统的时候修改此处的值，改值为目前系统值+1；
                updateStmt.setLong(fieldNum + 1, pp.getId());
                if (updateStmt.executeUpdate() == 0) {
                    UpdateThread.buildParam(pp, insertStmt);
                    logger.error("角色" + pp.getName() + "(" + pp.getId() + ")执行更新失败");
                    if (insertStmt.executeUpdate() == 0) {
                        logger.error("角色" + pp.getName() + "(" + pp.getId() + ")重置插入标记");
                        RoleUpdateLogService.getInstance().updateRoleDate(pp.getId());
                    }
                }
                logger.error("线程:[" + Threadname + "]执行 保存玩家" + pp.nameIdString());
            } catch (Exception e) {
                logger.error("执行更新的时玩家出错了", e);
            }
        }
    }
}
