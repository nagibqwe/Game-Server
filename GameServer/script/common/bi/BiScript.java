package common.bi;

import com.game.backpack.structs.ItemCoinType;
import com.game.bi.base.BI;
import com.game.bi.inter.IBiScript;
import com.game.bi.manager.BIFileLog;
import com.game.bi.manager.BIString;
import com.game.bi.struct.*;
import com.game.db.bean.RankPlayer;
import com.game.manager.Manager;
import com.game.marriage.struct.Marriage;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.QuitGameDefine;
import com.game.ranklist.manager.RankListManager;
import com.game.recharge.structs.RechargeDefine;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.task.structs.Task;
import game.core.net.Config.ServerConfig;
import game.core.util.*;
import game.message.BIMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/12/30 15:12.
 * @author: tc
 */
public class BiScript implements IBiScript {
    // 是否再单独备份一份BI
    private final boolean isBak = false;

    private final Logger log = LogManager.getLogger(BiScript.class);
    private final Logger biBakLog = LogManager.getLogger("BILog");
    private final Logger biSysNetLog = LogManager.getLogger("BILog2");
//    private final Logger biLog = LogManager.getLogger("BILog2");

    @Override
    public int getId() {
        return ScriptEnum.BIScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void onReqBiDevice(Player player, BIMessage.ReqBiDevice biDevice) {
        biUpdateBase(player);

        BI bi = player.getBi();
        bi.setApp_id(biDevice.getDevice().getAppId());
        bi.setRole_id(player.getId());
        bi.setRole_name(player.getName());
        bi.setRole_sex(player.getSex());
        bi.setRole_prof(player.getCareer());
        bi.setRole_level(player.getLevel());
        bi.setRole_vip_level(player.getVipLv());
        bi.setRole_combat(player.getFightPoint());
        bi.setUser_ip(player.getLoginIP());

        bi.setChannel_id(biDevice.getDevice().getChannelId());
        bi.setSource_id(biDevice.getDevice().getSourceId());
        bi.setDevice_id(biDevice.getDevice().getDeviceId());
        bi.setPlatform(biDevice.getDevice().getPlatform());
        bi.setApp_version(biDevice.getDevice().getAppVersion());

        bi.setMerchant(biDevice.getDevice().getMerchant());
        bi.setNet_type(biDevice.getDevice().getNetType());
        bi.setOs(biDevice.getDevice().getOs());
        bi.setOs_version(biDevice.getDevice().getOsVersion());
        bi.setScreen(biDevice.getDevice().getScreen());
        bi.setServerName(biDevice.getDevice().getServerName());

        bi.setCpUserId(biDevice.getDevice().getCpuserid());
        bi.setCpUserName(biDevice.getDevice().getCpuserName());
        bi.setCpGameName(biDevice.getDevice().getCpgameName());
        bi.setCpPlatFormGameName(biDevice.getDevice().getCpgameName());
        bi.setCpdid(biDevice.getDevice().getCpdid());
        bi.setCpdevice_name(biDevice.getDevice().getCpdeviceName());
        bi.setCpplatformId(biDevice.getDevice().getCpplatformId());
        bi.setCpgameId(biDevice.getDevice().getCpgameId());
        bi.setGame_version(ServerConfig.getVersion());
        bi.setMap_id(player.getCurGps().getModelId() == 0 ? "" : String.valueOf(player.getCurGps().getModelId()));

        log.info("bi数据:" + player.getBi().toString2());

//        if (EntityState.ReConnect.compare(player.getState()))
//            biLogin(player, 2);
//        else
//            biLogin(player, 1);

        Manager.biManager.get4399Script().loginBiTo4399(player);
    }

    private BI syncPlayerInfo(Player player) {
        if (player == null) {
            return null;
        }
        BI bi = player.getBi();
        if (bi.getRole_level() != player.getLevel()) {
            bi.setRole_level(player.getLevel());
        }
        if (bi.getRole_vip_level() != player.getVipLv()) {
            bi.setRole_vip_level(player.getVipLv());
        }
        if (bi.getRole_combat() != player.getFightPoint()) {
            bi.setRole_combat(player.getFightPoint());
        }
        int mapId = player.getCurGps().getModelId();
        if(mapId==0){
            bi.setMap_id("");
        }else if (!bi.getMap_id().equals(String.valueOf(mapId))) {
            bi.setMap_id(String.valueOf(mapId));
        }
        return bi;
    }

    @Override
    public void onReqBi(Player player, BIMessage.ReqBi bi) {
        if (noSave()) return;
        if (bi.getBiName().equals("newbie_guide")) {
            // 新手引导数据
            int id = 0;
            int status = 0;
            for (BIMessage.ValMap valMap : bi.getValMapsList()) {
                if (valMap.getKey().equals("id")) {
                    id = StringUtils.parseInt(valMap.getValue(), 0);
                } else if (valMap.getKey().equals("status")) {
                    status = StringUtils.parseInt(valMap.getValue(), 0);
                }
            }
            BINewbie_guide biNewbieGuide = new BINewbie_guide(String.valueOf(id), String.valueOf(status));
            addLog(BIString.str(syncPlayerInfo(player), biNewbieGuide));
        }
    }

    @Override
    public void onReqUiBi(Player player, BIMessage.ReqUiBi uiBi) {
        Player client = Manager.playerManager.getPlayerCache(uiBi.getRoleId());
        if (client == null) {
            return;
        }

        for (BIMessage.UIData data : uiBi.getUiDataList()) {
//            UiBiLog uiBiLog = new UiBiLog();
//            uiBiLog.setUiID(data.getId());
//            uiBiLog.setTime(data.getTime());
//            uiBiLog.setPlayer(client);
//            LogService.getInstance().execute(uiBiLog);

            Manager.biManager.getScript().biClientClick(client, data.getId());
        }
    }

    @Override
    public void init() {
//        if (isBak) {
//            OutputStreamWriter writer = getWriter(Manager.biManager.getBiBak());
//            if (writer == null) {
//                log.error("bi bak file init failed");
//                System.exit(13);
//                return;
//            }
//        }
//
//        OutputStreamWriter biWriter = getWriter(Manager.biManager.getBiSync());
//        if (biWriter == null) {
//            log.error("bi file init failed");
//            System.exit(13);
//        }
    }

    @Override
    public void save() {
//        OutputStreamWriter writer = null;
//        if (isBak) {
//            writer = getWriter(Manager.biManager.getBiBak());
//            if (writer == null) return;
//        }
//
//        OutputStreamWriter biWriter = getWriter(Manager.biManager.getBiSync());
//        if (biWriter == null) return;
//
//        try {
//            boolean update = false;
//            while (!Manager.biManager.getQueue().isEmpty()) {
//                String string = Manager.biManager.getQueue().poll();
//                if (string == null)
//                    continue;
//
//                if (isBak) writer.write(string);
//                biWriter.write(string);
//                update = true;
//
//                biLog.info(string);
//            }
//
//            if (update) {
//                if (isBak) writer.flush();
//                biWriter.flush();
//            }
//        } catch (IOException e) {
//            log.error("save bi error: " + e.getMessage());
//        }
    }

    @Override
    public void biUpdateBase(Player player) {
        BI bi = player.getBi();
        bi.setRole_id(player.getId());
        bi.setRole_level(player.getLevel());
        bi.setRole_name(dealStr(player.getName()));
        bi.setRole_combat(player.getFightPoint());
        bi.setRole_vip_level(player.getVipLv());
        bi.setRole_sex(player.getSex());
        bi.setRole_prof(player.getCareer());
        bi.setAccount_id(player.getUuid());
//        bi.setServer_id(ServerConfig.getServerId());
        bi.setServer_id(player.getCreateServerId());
        bi.setZone_id(String.valueOf(ServerConfig.getLsId()));
        bi.setUser_ip(player.getLoginIP());
        bi.setGame_version(ServerConfig.getVersion());
        bi.setMap_id(player.getCurGps().getModelId() == 0 ? "" : String.valueOf(player.getCurGps().getModelId()));
        bi.updateLastModifyTime();
    }

    @Override
    public void biCreate(Player player, BIMessage.Device device, int career, int sex, String ip) {
        biUpdateBase(player);

        BI bi = initBIInfo(player, device);

        if (noSave()) return;
        Manager.biManager.get4399Script().createPlayer(player);
        Manager.biManager.get4399Script().roleBiTo4399(player);

        BICreate_role biCreate = new BICreate_role();
        addLog(BIString.str(bi, biCreate));
    }

    private BI initBIInfo(Player player, BIMessage.Device device) {
        BI bi = player.getBi();
        bi.setApp_id(device.getAppId());
        bi.setRole_id(player.getId());
        bi.setRole_name(player.getName());
        bi.setRole_level(player.getLevel());
        bi.setRole_combat(player.getFightPoint());
        bi.setRole_sex(player.getSex());
        bi.setRole_prof(player.getCareer());
        bi.setUser_ip(player.getLoginIP());

        bi.setChannel_id(device.getChannelId());
        bi.setSource_id(device.getSourceId());
        bi.setDevice_id(device.getDeviceId());
        bi.setPlatform(device.getPlatform() > 0 ? device.getPlatform() : 4);
        bi.setApp_version(device.getAppVersion());

        bi.setMerchant(device.getMerchant());
        bi.setNet_type(device.getNetType());
        bi.setOs(device.getOs());
        bi.setOs_version(device.getOsVersion());
        bi.setScreen(device.getScreen());
        bi.setServerName(device.getServerName());

        bi.setCpUserId(device.getCpuserid());
        bi.setCpUserName(device.getCpuserName());
        bi.setCpGameName(device.getCpgameName());
        bi.setCpPlatFormGameName(device.getCpgameName());
        bi.setCpdid(device.getCpdid());
        bi.setCpdevice_name(device.getCpdeviceName());
        bi.setCpplatformId(device.getCpplatformId());
        bi.setCpgameId(device.getCpgameId());
        bi.setGame_version(ServerConfig.getVersion());
        bi.setMap_id(player.getCurGps().getModelId() == 0 ? "" : String.valueOf(player.getCurGps().getModelId()));
        return bi;
    }

    private String dealStr(String str) {
        if (str == null || str.equals(""))
            return str;

        return str.replace('|', '/').replaceAll("\n|\r", "");
    }

    /**
     * 临时处理多语言名字传值
     * @param str
     * @return
     */
    private String dealName(String str) {
        if (str == null || str.equals("") || !str.contains("2&_"))
            return str;

        return str.replace("2&_", "");
    }

    private OutputStreamWriter getWriter(BIFileLog biFileLog) {
//        long now = TimeUtils.Time();
//        if (TimeUtils.isSameDay(now, biFileLog.getLastBakTime()) && biFileLog.getWriter() != null)
//            return biFileLog.getWriter();
//
//        biFileLog.setLastBakTime(now);
//        String filePath = biFileLog.getPath() + File.separator + biFileLog.getPrefix()
//                + ServerConfig.getServerId() + "_"
//                + TimeUtils.format2string(now, "yyyy-MM-dd") + biFileLog.getSuffix();
//        biFileLog.setFilePath(filePath);
//        try {
//            if (biFileLog.getWriter() != null)
//                biFileLog.getWriter().close();
//
//            File dir = new File(biFileLog.getPath());
//            if (!dir.exists()) dir.mkdirs();
//
//            File file = new File(filePath);
//            FileOutputStream fos = null;
//            if (!file.exists()) {
//                file.createNewFile();
//                fos = new FileOutputStream(file);
//            } else {
//                fos = new FileOutputStream(file, true);
//            }
//            OutputStreamWriter writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
//            biFileLog.setWriter(writer);
//            return writer;
//        } catch (Exception e) {
//            log.error("open bi file failed: " + e.getMessage());
//        }
        return null;
    }

    private boolean noSave() {
//        return GameServer.getInstance().IsFightServer();
        return false;
    }

    private void addLog(String string) {
        if (string == null || string.equals(""))
            return;

        // 0关闭所有日志
        if (ServerConfig.getBiConfig().getOpenNet() == 0)
            return;

        // 1仅开启文本日志
        if (ServerConfig.getBiConfig().getOpenNet() == 1) {
            //测试BI写文件消耗
//            long sTime = TimeUtils.Time();
//            //模拟同时操作次数
//            int count = 500;
//            for (int i = 0; i < count; i++) {
            biBakLog.info(string);
//            }
//            log.error("BI写文件"+count+"条,耗时："+(TimeUtils.Time()-sTime)+"ms");
            return;
        }

        // 2仅开启网络日志
        if (ServerConfig.getBiConfig().getOpenNet() == 2) {
            biSysNetLog.info(string);
            return;
        }

        // 开启所有日志
//        Manager.biManager.addQueue(string);
        biSysNetLog.info(string);
        biBakLog.info(string);
    }

    @Override
    public void example(Player player) {
//        BILogin biLogin = new BILogin(1, 999, "127.0.0.1", "1.0.0", "1.1.1", "中国移动", "5G");
//        String string = BIString.str(player.getBi(), biLogin);
//        addLog(string);
//        log.info(string);
    }

    @Override
    public void biLogin(Player player, int status, int is_retrieve) {
        if (noSave()) return;
        BI bi = player.getBi();
        if (bi == null)
            return;

        BILogin biLogin = new BILogin(bi.getNet_type(), String.valueOf(status), String.valueOf(is_retrieve));
        addLog(BIString.str(bi, biLogin));
    }

    @Override
    public void biLogout(Player player, int reason, int is_retrieve) {
        if (noSave()) return;
        if (player == null) return;

        BILogout biLogout = new BILogout(TimeUtils.NowToString(),
                String.valueOf(player.getLastLoginLevel()),
                String.valueOf(player.getLastVipLevel()),
                String.valueOf((TimeUtils.Time() - player.getLastLoginTime()) / 1000),
                String.valueOf(getLogoutReason(reason)),
                String.valueOf(Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GemCoin)),
                "",
                String.valueOf(Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GoldCoin)),
                String.valueOf(Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.BindMoney)),
                "",
                String.valueOf(player.getOpenServerGrowUp().getPoint()),
                String.valueOf(is_retrieve));
        addLog(BIString.str(player.getBi(), biLogout));
    }

    private int getLogoutReason(int reason) {
        if (QuitGameDefine.Normal == reason) {
            return 1;
        } else if (QuitGameDefine.RepeatLogin == reason) {
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public void biOlineNum(List<Player> playerList) {
        if (noSave()) return;
        BIOnline biOlnum = new BIOnline();
        biOlnum.setEvent_time(TimeUtils.NowToStringWithZone());
        biOlnum.setZone_id(String.valueOf(ServerConfig.getLsId()));
        biOlnum.setServer_id(String.valueOf(ServerConfig.getServerId()));
        biOlnum.setOnline_num(String.valueOf(playerList.size()));
        biOlnum.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
        biOlnum.setAccount_list(getAccountList(playerList));
        addLog(BIString.str(null, biOlnum));
    }

    private String getAccountList(List<Player> playerList) {
        if (playerList.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Player player : playerList) {
            sb.append(player.getBi().getAccount_id()).append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public void biPay(Player player, int goodId, String orderId, String goodsName, int orderAmount, int status, int diamondAmount, int src, String moneyType) {
        if (noSave()) return;
        BIPay biPay = new BIPay(
                "",
                "SDK",
                orderId,
                String.valueOf(getRechargeType(src)),
                String.valueOf(status),
                (moneyType==null||moneyType.equals(""))?"CNY":moneyType,
                String.valueOf(orderAmount),
                String.valueOf(diamondAmount),
                String.valueOf(goodId),
                goodsName,
                "1"
        );
        addLog(BIString.str(player.getBi(), biPay));
    }

    private int getRechargeType(int src) {
        if (RechargeDefine.SRC_INTERNAL == src) {
            return 0;
        } else if (RechargeDefine.SRC_NORMAL == src) {
            return 1;
        } else {
            return 99;
        }
    }

    @Override
    public void biMall(Player player, int product_type, int mall_type, int item_id, int item_num, int money_type, int amount, int price, int location) {
        if (noSave()) return;
        BIMall biMall = new BIMall(
                String.valueOf(mall_type),
                String.valueOf(product_type),
                String.valueOf(item_id),
                String.valueOf(item_num),
                String.valueOf(money_type),
                String.valueOf(amount),
                String.valueOf(price),
                String.valueOf(location)
        );
        addLog(BIString.str(player.getBi(), biMall));
    }

    @Override
    public void biMarry(Player player, long targetId, String targetName, int opt) {
//        if (noSave()) return;
//        BIMarry biMarry = new BIMarry(
//                String.valueOf(targetId),
//                targetName,
//                String.valueOf(opt));
//        addLog(BIString.str(player.getBi(), biMarry));
    }

    @Override
    public void biRoomResult(Player player, int stage_id) {
//        if (noSave()) return;
//        BIRoom_result biRoom_result = new BIRoom_result(String.valueOf(stage_id));
//        addLog(BIString.str(player.getBi(), biRoom_result));
    }

    @Override
    public void biDraw(Player player, int draw_id, int act_type) {
//        if (noSave()) return;
//        BIDraw biDraw = new BIDraw(String.valueOf(draw_id), String.valueOf(act_type));
//        addLog(BIString.str(player.getBi(), biDraw));
    }

    @Override
    public void biItem(Player player, long uid, int item_type, int item_level, int item_id, String item_name, long item_num, long after_num, int op_type, long op_id, int change_type, int scene) {
        if (noSave()) return;
        BIItem biItem = new BIItem(
                String.valueOf(item_id),
                String.valueOf(uid),
                dealName(dealStr(item_name)),
                String.valueOf(item_type),
                String.valueOf(change_type),
                String.valueOf(item_num),
                String.valueOf(Math.abs(change_type == 0?after_num + item_num:after_num - item_num)),
                String.valueOf(after_num),
                String.valueOf(op_type),
                "",
                String.valueOf(op_id),
                "",
                "");
        addLog(BIString.str(player.getBi(), biItem));
    }

    @Override
    public void biExp(Player player, String ip, int old_level, int after_level, long exp_num, long after_exp, int op_type, long op_id) {
//        if (noSave()) return;
//        BIExp biExp = new BIExp(ip,old_level,after_level,exp_num,after_exp,String.valueOf(op_type), String.valueOf(op_id));
//        addLog(BIString.str(player.getBi(), biExp));
    }

    @Override
    public void biMoney(Player player, String ip, int money_type, int amount, long before_num, long after_num, int op_type, long op_id, int change_type, int scene) {
        if (noSave()) return;
        BIMoney biMoney = new BIMoney(
                String.valueOf(money_type),
                String.valueOf(change_type),
                String.valueOf(amount),
                String.valueOf(before_num),
                String.valueOf(after_num),
                String.valueOf(op_type),
                "",
                String.valueOf(op_id),
                "",
                "");
        addLog(BIString.str(player.getBi(), biMoney));
    }

    @Override
    public void biResource(Player player, int change_type, int money_type, BigInteger amount, BigInteger before_num, BigInteger after_num, int beforeLevel, int afterLevel, int op_type, long op_id) {
        if (noSave()) return;
        //测试时的代码---排除经验start
//        if(money_type == 8){//经验
//            return;
//        }
        //测试时的代码---排除经验end
        BIResource biResource = new BIResource(
                String.valueOf(money_type),
                String.valueOf(change_type),
                String.valueOf(amount),
                before_num == null ? "" : before_num.toString(),
                after_num == null ? "" : after_num.toString(),
                String.valueOf(op_type),
                "",
                String.valueOf(op_id),
                "",
                "");
        addLog(BIString.str(player.getBi(), biResource));
    }

    @Override
    public void biGuildMember(Player player, long guild_id, int guild_level, int change_type, int status, int member, long guildPower) {
        if (noSave()) return;
        BIGuild_member biGuild_member = new BIGuild_member(String.valueOf(guild_id), String.valueOf(guild_level), String.valueOf(change_type), String.valueOf(status), String.valueOf(member), String.valueOf(guildPower));
        addLog(BIString.str(player.getBi(), biGuild_member));
    }

    @Override
    public void biGuildUpgrade(Player player, long guild_id, String guild_name, int level_type, int before_guild_level, int later_guild_level) {
        if (noSave()) return;
        BIGuild_upgrade biGuildUpgrade = new BIGuild_upgrade(String.valueOf(guild_id), guild_name, String.valueOf(level_type), String.valueOf(before_guild_level), String.valueOf(later_guild_level));
        addLog(BIString.str(player.getBi(), biGuildUpgrade));
    }

    @Override
    public void biInstance(Player player, int instance_id, int instance_type, String instance_name, String instance_subName, int instance_level,
                           int instance_status, Integer instance_result, String instance_diff, String floor) {
        BIInstance biInstance = new BIInstance(
                String.valueOf(instance_id),
                instance_name,
                String.valueOf(instance_type),
                String.valueOf(instance_subName),
                String.valueOf(instance_level),
                instance_diff,
                String.valueOf(instance_status),
                instance_result == null ? "" : instance_result.toString(),
                String.valueOf(player.getTeamId()),
                "",
                floor);
        addLog(BIString.str(player.getBi(), biInstance));
    }

    @Override
    public void biChat(Player player, int chat_source, int sys_flag, String chat_txt, long obj_account_id, long obj_role_id, String object_name, String object_ip, String object_device_id) {
        if (noSave()) return;
        BIChat biChat = new BIChat(
                String.valueOf(chat_source),
                String.valueOf(sys_flag),
                dealStr(chat_txt),
                String.valueOf(obj_account_id),
                String.valueOf(obj_role_id),
                object_name,
                object_ip,
                object_device_id);
        addLog(BIString.str(player.getBi(), biChat));
    }

    public void biRole_info(Player player) {
        if (noSave()) return;
        String createtime = DateFormatUtils.format(new Date((long) player.getCreateTime() * 1000L), "yyyy-MM-dd HH:mm:ss");
        String lastlogintime = DateFormatUtils.format(new Date(player.getLastLoginTime()), "yyyy-MM-dd HH:mm:ss");

//        String oldMapID = player.getOld().getModelId() + "";
        String curTaskID = String.valueOf(player.getCurMainTaskId());
        long diamond_res = player.getGold().getReaminGold();
        long bmoney_balance = player.getCurrencys().get(ItemCoinType.GoldCoin);
        long gold_res = player.getCurrencys().get(ItemCoinType.BindMoney);
        long nowExp = player.getCurrencys().get(ItemCoinType.EXP);

        String marryPartnerId = "";
        String marry_partner_name = "";
        String marry_partner_time = "";
        String marryStatus = "0";
        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
        if (marriage != null) {
            marryStatus = "1";
            PlayerWorldInfo marryTarget = Manager.marriageManager.getMarryTarget(player);
            marryPartnerId = String.valueOf(marryTarget.getRoleid());
            marry_partner_name = marryTarget.getRolename();
            marry_partner_time = String.valueOf(marriage.getMarriageTime());
        }
        BIRole_info biRole_info = new BIRole_info(
                createtime,
                lastlogintime,
                String.valueOf(player.getAccunonlinetime()),
                String.valueOf(nowExp),
                TimeUtils.NowToString(),
                curTaskID,
                String.valueOf(diamond_res),
                "",
                String.valueOf(bmoney_balance),
                String.valueOf(gold_res),
                "",
                marryStatus,
                marryPartnerId,
                marry_partner_name,
                marry_partner_time,
                String.valueOf(player.getVipPearl().getState() + 1)
        );
        addLog(BIString.str(player.getBi(), biRole_info));
    }

    @Override
    public void biTask(Player player, int task_id, int task_type, int task_status, String task_name, int task_subtype, int task_level) {
        if (noSave()) return;
        BITask biTask = new BITask(
                String.valueOf(task_id),
                dealStr(task_name),
                String.valueOf(task_type),
                String.valueOf(task_status),
                "",
                String.valueOf(task_subtype),
                getTaskLevel(task_type, task_level));

        BI bi = player.getBi();
        if (bi.getRole_id() <= 0) {
            biUpdateBase(player);
        }

        addLog(BIString.str(player.getBi(), biTask));
    }

    private String getTaskLevel(int task_type, int task_level) {
        if(task_type == Task.GUILD_TASK){
            switch (task_level){
                case 1:
                    return "C";
                case 2:
                    return "B";
                case 3:
                    return "A";
                case 4:
                    return "S";
                default:
                    return "";
            }
        }
        return "";
    }

    @Override
    public void biScene(Player player, int scene, String scene_name, int type, int stay_time) {
//        if (noSave()) return;
//        BIScene biScene = new BIScene(scene, scene_name, type, stay_time);
//        addLog(BIString.str(player.getBi(), biScene));
        biUpdateBase(player);
    }

    @Override
    public void biGrow(Player player, int grow_type, int grow_subtype, int act_type, int before_target_id, int after_target_id, int grow_id) {
        if (noSave()) return;
//        long oldFightPoint = player.getOldFightPoint();
//        long newFightPoint = player.getFightPoint();
//        long changeFightPoint = newFightPoint - oldFightPoint;
        long newFightPoint = getFightPointFromRank(player, grow_type);
        BIGrow biGrow = new BIGrow(
                String.valueOf(grow_id),
                "",
                String.valueOf(grow_type),
                String.valueOf(grow_subtype),
                String.valueOf(act_type),
                "1",
                "1",
                "",
                String.valueOf(before_target_id),
                String.valueOf(after_target_id),
                "",
                "",
                String.valueOf(newFightPoint));
        addLog(BIString.str(player.getBi(), biGrow));
        //仅仅本地测试用
//        LogService.getInstance().execute(new RoleGrowLog(player.getId(), GrowType.get(grow_type, grow_subtype,act_type,BiType.grow), JsonUtils.toJSONString(biGrow)));
    }

    private long getFightPointFromRank(Player player, int grow_type) {
        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if(rankPlayer != null){
            switch (grow_type){
                //坐骑
                case 1:
                case 9:
                case 12:
                    return rankPlayer.getHorseFightPoint();
                    //坐骑脉轮
                case 26:
                    return rankPlayer.getHorseEquipPower();
                case 20:
                    return rankPlayer.getFlySwordPower();
                case 22:
                    return rankPlayer.getImmortalsoulPower();
                //法宝
                case 3:
                case 14:
                case 15:
                case 42:
                    return rankPlayer.getMagicWeaponDamage();
                case 7:
                case 11:
                case 24:
                case 25:
                    return rankPlayer.getPetFightPower();
                case 17:
                    return rankPlayer.getMonsterFightPower();
                case 18:
                    return rankPlayer.getMarryChildPower();
                case 23:
                    return rankPlayer.getHolyEquipFightPower();
                case 27:
                    return rankPlayer.getSoulFight();
                case 21:
                    return rankPlayer.getSpiritFightPower();
                case 38:
                    return rankPlayer.getDevilSoulPower();
                case 13:
                case 2:
                case 10:
                    return rankPlayer.getWingFightPoint();
                case 39:
                case 40:
                case 41:
                    return rankPlayer.getWeaponFightPower();
            }
        }
        return 0;
    }

    @Override
    public void biBoss(Player player, Integer instance_id, String instance_name, Integer instance_type, Integer instance_level, int boss_type, int boss_id, String boss_name, int level, long dps, int dps_rank, long guild_id, int sub_type, int guild_rank) {
//        if (noSave()) return;
//        BIBoss biBoss = new BIBoss(scene, boss_type, boss_id, boss_name, dps, dps_rank, guild_id);
        BIMonster_kill biMonsterKill = new BIMonster_kill(
                instance_id == null ? "" : instance_id.toString(),
                instance_name,
                instance_type == null ? "" : instance_type.toString(),
                instance_level == null ? "" : instance_level.toString(),
                String.valueOf(boss_id),
                boss_name,
                String.valueOf(boss_type),
                String.valueOf(level),
                String.valueOf(dps),
                String.valueOf(dps_rank),
                String.valueOf(guild_id),
                String.valueOf(sub_type),
                guild_rank == 0 ? "" : String.valueOf(guild_rank));
        addLog(BIString.str(player.getBi(), biMonsterKill));
    }

    @Override
    public void biDeath(Player player, Integer instance_id, String instance_name, Integer instance_type, Integer instance_level, int killer_type, long killer_id, String killer_name, int killer_level, long killer_combat, int res_type) {
        if (noSave()) return;
        BIDeath biDeath = new BIDeath(
                instance_id == null ? "" : instance_id.toString(),
                instance_name,
                instance_type == null ? "" : instance_type.toString(),
                instance_level == null ? "" : instance_level.toString(),
                String.valueOf(killer_type),
                String.valueOf(killer_id),
                String.valueOf(killer_name),
                String.valueOf(killer_level),
                String.valueOf(killer_combat));
        addLog(BIString.str(player.getBi(), biDeath));
    }

    @Override
    public void biEquip(Player player, int act_type, int equip_type, int part, int item_id, String item_name, int star, int lev, int col, int str, int bind, int suit, int gem_num, int gem_pos, int gem_set, int gem_rating, int gem_id) {
        if (noSave()) return;
        BIEquip biEquip = new BIEquip(String.valueOf(item_id), item_name, String.valueOf(equip_type), String.valueOf(part), String.valueOf(act_type), String.valueOf(star), String.valueOf(lev), String.valueOf(col), String.valueOf(str), String.valueOf(bind), String.valueOf(suit), String.valueOf(gem_id), String.valueOf(gem_num), String.valueOf(gem_pos), String.valueOf(gem_set), String.valueOf(gem_rating), "", "", "", "", "", "");
        addLog(BIString.str(player.getBi(), biEquip));
        //仅仅本地测试用
//        LogService.getInstance().execute(new RoleGrowLog(player.getId(), GrowType.get(equip_type, 0,act_type, BiType.equip),JsonUtils.toJSONString(biEquip)));
    }

    @Override
    public void biEquip(Player player, int act_type, int equip_type, int part, int item_id, String item_name, int star, int lev, int col, int str, int bind, int suit, int gem_num, int gem_pos, int gem_set, int gem_rating, int gem_id,
                        Integer lock_num,
                        Integer lock_type,
                        Integer change_score,
                        Integer before_score,
                        Integer after_score,
                        Integer refine_level) {
        if (noSave()) return;
        BIEquip biEquip = new BIEquip(String.valueOf(item_id), item_name, String.valueOf(equip_type), String.valueOf(part), String.valueOf(act_type), String.valueOf(star), String.valueOf(lev), String.valueOf(col), String.valueOf(str), String.valueOf(bind), String.valueOf(suit), String.valueOf(gem_id), String.valueOf(gem_num), String.valueOf(gem_pos), String.valueOf(gem_set), String.valueOf(gem_rating),
                lock_num == null ? "" : lock_num.toString(),
                lock_type == null ? "" : lock_type.toString(),
                change_score == null ? "" : change_score.toString(),
                before_score == null ? "" : before_score.toString(),
                after_score == null ? "" : after_score.toString(),
                refine_level == null ? "" : refine_level.toString());
        addLog(BIString.str(player.getBi(), biEquip));
        //仅仅本地测试用
//        LogService.getInstance().execute(new RoleGrowLog(player.getId(), GrowType.get(equip_type, 0,act_type, BiType.equip),JsonUtils.toJSONString(biEquip)));
    }

    @Override
    public void biRealm(Player player, int up_type, int act_type, int stage, int taskId, String progress) {
        if (noSave()) return;
        BIRealm biRealm = new BIRealm(String.valueOf(up_type),
                String.valueOf(act_type),
                String.valueOf(taskId),
                progress);
        addLog(BIString.str(player.getBi(), biRealm));
    }

    @Override
    public void biActivity(Player player, int reason, int activity_type, int id) {
        if (noSave()) return;
        BIActivity biActivity = new BIActivity(
                String.valueOf(reason),
                "",
                getActivityId(reason, id),
                "",
                String.valueOf(activity_type),
                "",
                "",
                "",
                "",
                "4",
                "",
                ""
        );
        addLog(BIString.str(player.getBi(), biActivity));
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int itemChangeReason){
        biActivity(player, activity.getId(), activity.getName(), activity.getSubId(), activity.getSubName(), activity.getType(), itemChangeReason, 0, "", 0, 0);
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int itemChangeReason, int subReward){
        biActivity(player, activity.getId(), activity.getName(), activity.getSubId(), activity.getSubName(), activity.getType(), itemChangeReason, subReward, "", 0, 0);
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int itemChangeReason, int subReward, String rewardName){
        biActivity(player, activity.getId(), activity.getName(), activity.getSubId(), activity.getSubName(), activity.getType(), itemChangeReason, subReward, rewardName, 0, 0);
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int subId, String subName, int itemChangeReason, int round, int level){
        biActivity(player, activity.getId(), activity.getName(), subId, subName, activity.getType(), itemChangeReason, 0, "", round, level);
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int subId, String subName, int itemChangeReason, int subReward, String rewardName, int round, int level){
        biActivity(player, activity.getId(), activity.getName(), subId, subName, activity.getType(), itemChangeReason, subReward, rewardName, round, level);
    }

    @Override
    public void biActivity(Player player, BIActiityTypeEnum activity, int itemChangeReason, int round, int level){
        biActivity(player, activity.getId(), activity.getName(), activity.getSubId(), activity.getSubName(), activity.getType(), itemChangeReason, 0, "", round, level);
    }

    private void biActivity(Player player, int id, String name, int subId, String subName, int type, int itemChangeReason, int reward, String rewardName,int round, int level){
        if (noSave()) return;
        BIActivity biActivity = new BIActivity(
                String.valueOf(id),
                name == null ? "" : name,
                getActivityId(id, subId),
                subName == null ? "" : subName,
                String.valueOf(type),
                String.valueOf(itemChangeReason),
                "",
                String.valueOf(reward),
                rewardName,
                "4",
                String.valueOf(round),
                String.valueOf(level)
        );
        addLog(BIString.str(player.getBi(), biActivity));
    }

    private String getActivityId(int reason, int id){
        StringBuilder sb = new StringBuilder();
        sb.append(reason).append("_").append(id);
        return sb.toString();
    }

    @Override
    public void biAuction(long role_id, int role_level, String role_name, long targetId, int role_type, int op_type, int item_id, int item_type, int item_colour, int item_lev, int item_star, long item_no, String item_name, int item_num, int bid_price, int fixed_price, long guild_id) {
        if (noSave()) return;
        BIAuction biAuction = new BIAuction();
        biAuction.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
        biAuction.setEvent_time(TimeUtils.NowToStringWithZone());
        biAuction.setZone_id(String.valueOf(ServerConfig.getLsId()));
        biAuction.setServer_id(String.valueOf(ServerConfig.getServerId()));
        biAuction.setRole_id(String.valueOf(role_id));
        biAuction.setRole_level(String.valueOf(role_level));
        biAuction.setRole_name(role_name);
//        biAuction.setRole_type(role_type);
        biAuction.setTarget_id(String.valueOf(targetId));
        biAuction.setTarget_type(String.valueOf(role_type));
        biAuction.setAuction_op_type(String.valueOf(op_type));
        biAuction.setItem_id(String.valueOf(item_id));
        biAuction.setItem_type(String.valueOf(item_type));
        biAuction.setItem_colour(String.valueOf(item_colour));
        biAuction.setItem_lev(String.valueOf(item_lev));
        biAuction.setItem_star(String.valueOf(item_star));
        biAuction.setItem_no(String.valueOf(item_no));
        biAuction.setItem_name(String.valueOf(item_name));
        biAuction.setItem_num(String.valueOf(item_num));
        biAuction.setBid_price(String.valueOf(bid_price));
        biAuction.setFixed_price(String.valueOf(fixed_price));
        biAuction.setGuild_id(String.valueOf(guild_id));
        addLog(BIString.str(null, biAuction));
    }

    @Override
    public void biRank(int targetType, long role_id, String role_name, int rank_type, int ranking, String rank_value) {
//        if (noSave()) return;
//        BIRank biRank = new BIRank();
//        biRank.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
//        biRank.setEvent_time(TimeUtils.NowToString());
//        biRank.setZone_id(String.valueOf(ServerConfig.getLsId()));
//        biRank.setServer_id(String.valueOf(ServerConfig.getServerId()));
//        biRank.setTarget_type(targetType);
//        biRank.setTarget_id(role_id);
//        biRank.setTarget_name(role_name);
//        biRank.setRank_type(rank_type);
//        biRank.setRanking(ranking);
//        biRank.setRank_value(rank_value);
//        addLog(BIString.str(null, biRank));
    }

    @Override
    public void biGuildWar(long guild_id, String guild_name, int guild_level, int rate_level, int type, int camp, int round) {
        if (noSave()) return;
        BIGuild_war biGuildWar = new BIGuild_war();
        biGuildWar.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
        biGuildWar.setEvent_time(TimeUtils.NowToStringWithZone());
        biGuildWar.setZone_id(String.valueOf(ServerConfig.getLsId()));
        biGuildWar.setServer_id(String.valueOf(ServerConfig.getServerId()));
        biGuildWar.setGuild_id(String.valueOf(guild_id));
        biGuildWar.setGuild_name(guild_name);
        biGuildWar.setGuild_level(String.valueOf(guild_level));
        biGuildWar.setRate_level(String.valueOf(rate_level));
        biGuildWar.setType(String.valueOf(type));
        biGuildWar.setCamp(String.valueOf(camp));
        biGuildWar.setRound(String.valueOf(round));
        addLog(BIString.str(null, biGuildWar));
    }

    @Override
    public void biVehicle(Player player, int scene, int buff_id) {
//        if (noSave()) return;
//        BIVehicle biVehicle = new BIVehicle(scene, buff_id);
//        addLog(BIString.str(player.getBi(), biVehicle));
    }

    @Override
    public void biEvent(Player player, int event_type, int change_value, int after_value) {
//        if (noSave()) return;
//        BIEvent biEvent = new BIEvent();
//        biEvent.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
//        biEvent.setEvent_time(TimeUtils.NowToString());
//        biEvent.setZone_id(String.valueOf(ServerConfig.getLsId()));
//        biEvent.setServer_id(String.valueOf(ServerConfig.getServerId()));
//        biEvent.setGame_version(ServerConfig.getVersion());
//
//        biEvent.setEvent_type(event_type);
//        biEvent.setChange_value(change_value);
//        biEvent.setAfter_value(after_value);

//        addLog(BIString.str(null, biEvent));
    }

    @Override
    public void biGuildMoney(long guild_id, String guild_name, int guild_level, long money) {
//        if (noSave()) return;
//        BIGuild_money biGuildMoney = new BIGuild_money();
//        biGuildMoney.setLog_id(String.valueOf(IDConfigUtil.getLogId()));
//        biGuildMoney.setEvent_time(TimeUtils.NowToString());
//        biGuildMoney.setZone_id(String.valueOf(ServerConfig.getLsId()));
//        biGuildMoney.setServer_id(String.valueOf(ServerConfig.getServerId()));
//        biGuildMoney.setGuild_id(guild_id);
//        biGuildMoney.setGuild_name(guild_name);
//        biGuildMoney.setGuild_level(guild_level);
//        biGuildMoney.setMoney(money);
//        addLog(BIString.str(null, biGuildMoney));
    }

    /**
     * 前端 bi 数据
     *
     * @param player
     * @param id
     */
    @Override
    public void biClientClick(Player player, long id) {
//        if (noSave()) return;
//        BIClient_click biClient_click = new BIClient_click(String.valueOf(id));

//        addLog(BIString.str(player.getBi(), biClient_click));
    }

    @Override
    public void BiServer_op(int type){
        try{
            BIServer_op biServer_op = new BIServer_op();
            biServer_op.setServer_id(String.valueOf(GameServer.getInstance().getServerId()));
            biServer_op.setServer_name(GameServer.getInstance().getServerName());
            biServer_op.setServer_op_type(String.valueOf(type));
            biServer_op.setGame_version("");
            biServer_op.setEvent_time(TimeUtils.NowToStringWithZone());
            biServer_op.setServer_open_day(String.valueOf(TimeUtils.getOpenServerDay()));

            biServer_op.setServer_open_time(new SimpleDateFormat("Z").format(new Date()) + " " + ServerConfig.getServerOpenTime());
            biServer_op.setZone_id(String.valueOf(ServerConfig.getLsId()));
            biServer_op.setZone_name("");
            addLog(BIString.str(biServer_op));
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void biSign(Player player, int type, int rewardType, int count, int totalCount) {
        if (noSave()) return;
        BISign sign = new BISign(
                type == 0 ? "" : String.valueOf(type),
                String.valueOf(rewardType),
                "",
                String.valueOf(count),
                "",
                String.valueOf(totalCount));
        addLog(BIString.str(player.getBi(), sign));
    }

    @Override
    public void biWorldExpedition(
            Integer expedition_id,
            Integer city_id,
            Integer city_type,
            Integer add_score,
            Integer server_score,
            Integer city_owner_id) {
        if (noSave()) return;
        BIWorld_expedition sign = new BIWorld_expedition(expedition_id == null ? "" : expedition_id.toString(),
                city_id == null ? "" : city_id.toString(),
                city_type == null ? "" : city_type.toString(),
                add_score == null ? "" : add_score.toString(),
                server_score == null ? "" : server_score.toString(),
                city_owner_id == null ? "" : city_owner_id.toString());
        addLog(BIString.str(new BI(), sign));
    }

    public static void main(String[] args) {
        System.out.println(new SimpleDateFormat("Z").format(new Date()));
    }
}
