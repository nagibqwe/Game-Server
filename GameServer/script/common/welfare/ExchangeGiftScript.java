package common.welfare;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.db.bean.ActiveCodeBean;
import com.game.db.dao.ActiveCodeDao;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import com.game.utils.Symbol;
import com.game.welfare.script.IExchangeGiftScript;
import com.game.welfare.struct.ActiveCodeLog;
import game.core.dblog.LogService;
import game.core.util.DateFormatUtils;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.WelfareMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExchangeGiftScript implements IExchangeGiftScript {
    private static final Logger log = LogManager.getLogger(ExchangeGiftScript.class);
    private final ActiveCodeDao dao = new ActiveCodeDao();

    /**
     * 兑换礼包
     *
     * @param player
     * @param activeCode
     */
    @Override
    public void onReqExchangeGift(Player player, String activeCode) {
        ActiveCodeBean activeCodeBean = dao.select(activeCode);
        //判断激活码是否存在
        if (activeCodeBean == null) {
            sendResult(player, activeCode, 1, "不存在");
            return;
        }
        if (activeCodeBean.getDeleteTime() > 0) {
            sendResult(player, activeCode, 1, "不存在");
            return;
        }
        //判断该激活码是否使用
        if (activeCodeBean.getGet_player_id() != 0) {
            sendResult(player, activeCode, 2, "已被使用");
            return;
        }
        //判断激活码时效是否过期
        if (!checkValid(activeCodeBean)) {
            sendResult(player, activeCode, 3, "使用时间不对");
            return;
        }
        //判断是否是正确的服务器使用
        if (!checkServerID(player.getCreateServerId(), activeCodeBean.getValide_server_id_list())) {
            sendResult(player, activeCode, 4, "不支持该服务器使用");
            return;
        }
        //判断该激活码是否支持该平台
        if (!checkPlateform(player.getPlatformName(), activeCodeBean.getPlateform_name_big())) {
            sendResult(player, activeCode, 5, "不支持该平台玩家");
            return;
        }

        //检查同一批次的激活码是否使用过（普通码角色同一批次只能使用一次）
        if (activeCodeBean.getParam() == 0
                && dao.getBatchCount(player.getId(), activeCodeBean.getBatch()) > 0) {
            sendResult(player, activeCode, 6, "玩家已经使用过该批次的激活码 Batch:" + activeCodeBean.getBatch());
            return;
        }

        if (activeCodeBean.getParam() == 1) {//万能码
            for (String code : player.getUsedActiveCodeList()) {
                if (code.equalsIgnoreCase(activeCode)) {
                    sendResult(player, activeCode, 7, "该类型万能激活码玩家已使用");
                    return;
                }
            }
            player.getUsedActiveCodeList().add(activeCode);
        } else if(activeCodeBean.getParam() == 2){//多次码
            activeCodeBean.setGet_account_id(player.getUserId());
            activeCodeBean.setGet_plateform_aid(player.getName()); // 这里记的玩家名
            activeCodeBean.setGet_plateform_name(player.getPlatformName());
            activeCodeBean.setGet_player_id(player.getId());
            activeCodeBean.setGet_server_id(GameServer.getInstance().getServerId());
            if (dao.update(activeCodeBean) != 1) {
                sendResult(player, activeCode, 8, "激活码使用失败");
                return;
            }
        } else {//0 普通码
            activeCodeBean.setGet_account_id(player.getUserId());
            activeCodeBean.setGet_plateform_aid(player.getName()); // 这里记的玩家名
            activeCodeBean.setGet_plateform_name(player.getPlatformName());
            activeCodeBean.setGet_player_id(player.getId());
            activeCodeBean.setGet_server_id(GameServer.getInstance().getServerId());
            if (dao.update(activeCodeBean) != 1) {
                sendResult(player, activeCode, 8, "激活码使用失败");
                return;
            }
        }

        long actionId = IDConfigUtil.getLogId();
        String strItem = activeCodeBean.getItemList();

        // 给物品
        List<Item> items = Item.createItems(strItem, Symbol.FENHAO_REG, Symbol.DOUHAO_REG);
        Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.System
                , String.valueOf(MessageString.ActiveCodeGetContent)
                , items
                , ItemChangeReason.ActiveCodeGetContentGain, actionId);

        // 加log
        log.info(player.getName() + "(" + player.getId() + ")使用了激活码：" + activeCode + " ， 获得:" + activeCodeBean.getItemList() + " , 物品数量队列大小:" + items.size());
        writeActiveCodeLog(player, activeCodeBean, actionId);
        sendResult(player, activeCode, 0, "");

        //BI
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.WELFARE_ExchangeGift, ItemChangeReason.ActiveCodeGetContentGain);
    }

    /**
     * 玩家上线
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player) {

    }

    /**
     * 请求某福利子项数据
     *
     * @param player
     */
    @Override
    public void freshDataNtf(Player player) {

    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.ExchangeGiftBaseScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    private void writeActiveCodeLog(Player player, ActiveCodeBean activeCodeBean, long actionId) {
        try {
            ActiveCodeLog activeCodeLog = new ActiveCodeLog();
            activeCodeLog.setActionId(actionId);
            activeCodeLog.setActiveCode(activeCodeBean.getCode());
            activeCodeLog.setItemList(activeCodeBean.getItemList());
            activeCodeLog.setPlatformName(player.getPlatformName());
            activeCodeLog.setRoleId(player.getId());
            activeCodeLog.setSid(player.getCreateServerId());
            activeCodeLog.setUserId(player.getUserId());
            LogService.getInstance().execute(activeCodeLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //激活码是否是正确的使用时间
    private boolean checkValid(ActiveCodeBean activeCodeBean) {
        try {
            long beginTime = 0;
            long nowTime = TimeUtils.Time();
            long endTime = 0;
            Date date;
            if (activeCodeBean.getValide_time_begin() != null) {
                date = DateFormatUtils.parse(activeCodeBean.getValide_time_begin(), "yyyy-MM-dd HH:mm:ss");
                beginTime = date.getTime();
            }
            if (activeCodeBean.getValide_time_end() != null) {
                date = DateFormatUtils.parse(activeCodeBean.getValide_time_end(), "yyyy-MM-dd HH:mm:ss");
                endTime = date.getTime();
            }
            return nowTime >= beginTime && nowTime < endTime;
        } catch (ParseException ex) {
            log.error("激活码时效设置错误", ex);
        }
        return false;
    }

    //判断是否是正确的服务器,serverIdList格式为[1,2,3]
    private boolean checkServerID(int serverID, String serverIdList) {
        if (serverIdList == null) {
            return true;
        }
        if (serverIdList.equals("[]")) {
            return true;
        }
        if (serverIdList.equals("null")) {
            return true;
        }
        int length = serverIdList.length();
        if (length < 3) {
            return false;
        }

        List<Integer> idList = JsonUtils.parseArray(serverIdList, Integer.class);
        for (Integer idStr : idList) {
            if (idStr == null) {
                continue;
            }
            if (idStr == serverID) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPlateform(String plateform, String limit) {
        if (limit == null)
            return true;
        if (limit.equals("0"))
            return true;
        if (limit.equals("null")) {
            return true;
        }
        if (limit.equals(""))
            return true;

        return limit.equals(plateform);
    }

    private void sendResult(Player player, String activeCode, int result, String param) {
        WelfareMessage.ResExchangeGift.Builder msg = WelfareMessage.ResExchangeGift.newBuilder();
        msg.setErrorNo(result);
        MessageUtils.send_to_player(player, WelfareMessage.ResExchangeGift.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        if (result != 0)
            log.error("玩家:" + player.getId() + " Name:" + player.getName() + " 使用激活码:" + activeCode + " 失败desc:" + param + " reason:" + result);
    }
}
