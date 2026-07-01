package common.backgrand;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Changejob_Bean;
import com.data.script.ScriptConfigManager;
import com.data.struct.ReadIntegerArrayEs;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.backgrand.manager.BackGrandServer;
import com.game.backgrand.script.IBackCommandScript;
import com.game.backgrand.structs.GmHelp;
import com.game.backgrand.structs.ParmDec;
import com.game.backgrand.structs.RankData;
import com.game.backgrand.structs.RankListSort;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.control.structs.FuncOpenData;
import com.game.count.structs.VariantType;
import com.game.db.bean.*;
import com.game.db.dao.roleDao;
import com.game.gm.log.BackGMCmdLog;
import com.game.mail.manager.MailManager;
import com.game.manager.Manager;
import com.game.player.log.TranRoleLog;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.QuitGameDefine;
import com.game.ranklist.script.IRankScript;
import com.game.recharge.structs.Recharge;
import com.game.recharge.structs.RechargeDefine;
import com.game.recharge.structs.RechargeItemInfo;
import com.game.register.structs.UserInfo;
import com.game.register.structs.UserRoleInfo;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.filter.ClientMsgAdapter;
import com.game.server.thread.SaveServer;
import com.game.task.structs.MainTask;
import com.game.task.structs.Task;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import com.game.welfare.manager.WelfareManager;
import com.game.welfare.script.IUpdateNoticeScript;
import game.core.dblog.LogService;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.*;
import game.message.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 后台命令的脚本处理
 */
public class BackCommandScript implements IScript, IBackCommandScript {

    private static final Logger log = LogManager.getLogger(BackCommandScript.class);

    private final static String SIGN_KEY = "2016629_V1.2.4";

    // 成功 九零 一起 玩 www.90 17 5.c om
    public static final boolean SUCCESS = true;
    // 失败
    public static final boolean FAILURE = false;

    private static final HashMap<String, Long> DO_WAIT = new HashMap<>();

    private static final roleDao ROLE_DAO = new roleDao();


    @Override
    public int getId() {
        return ScriptEnum.BackCommandBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 处理后台过来的子命令
     *
     * @param cmd 命令包含参数
     */
    @Override
    public void dealBackCommand(Channel session, Map<String, Object> cmd) {
        String result = invokeOperatingGM(cmd);
        log.error("http 后台请求处理完成result " + result);
        BackGrandServer.Send(session, result);
        session.close();
    }

    private static String result(boolean isOk, String msg, Object data) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("ok", isOk);
        map.put("msg", msg);
        map.put("data", data);
        return JsonUtils.toJSONString(map);
    }


    private String invokeOperatingGM(Map<String, Object> cmd) {
        // 命令
        if (cmd == null || cmd.size() < 1) {
            log.error("请输入正确的gm指令");
            return result(FAILURE, "gm参数异常!!请输入正确的gm指令", "");
        } else {
            log.info("收到运营GM:" + cmd);
        }

        // gm指令名称，对应类的方法名
        String gmName = cmd.get("cmd").toString();
        // 方法类型
        MethodType gmMethodType = MethodType.methodType(String.class, Map.class);
        // 方法掉用器
        MethodHandle gmHandle;
        String errorMsg;
        // 返回gm执行结果消息
        String resInvokeGMCmdMsg;
        try {
            gmHandle = MethodHandles.lookup().findVirtual(getClass(), gmName, gmMethodType);
            resInvokeGMCmdMsg = (String) gmHandle.invoke(this, cmd);
            // 运营gm日志
            writeOperationLog(cmd, resInvokeGMCmdMsg);
        } catch (IllegalAccessException e) {
            errorMsg = "gm指令:" + gmName + "非法访问方法";
            log.error(errorMsg, e);
            resInvokeGMCmdMsg = result(FAILURE, errorMsg, "");
        } catch (NoSuchMethodException e) {
            errorMsg = "gm指令:" + gmName + "不存在";
            log.error(errorMsg, e);
            resInvokeGMCmdMsg = result(FAILURE, errorMsg, "");
        } catch (Throwable e) {
            errorMsg = "gm指令:" + gmName + "运行错误:";
            log.error(errorMsg, e);
            resInvokeGMCmdMsg = result(FAILURE, errorMsg + e.toString(), "");
        }
        return resInvokeGMCmdMsg;
    }

    private void writeOperationLog(Map<String, Object> cmd, String resInvokeGMCmdMsg) {
        BackGMCmdLog operatingGMCmdLog = new BackGMCmdLog();
        String operationuser = "未知";
        if (cmd.containsKey("operationuser")) {
            operationuser = cmd.get("operationuser").toString();
        }
        operatingGMCmdLog.setBackUser(operationuser);
        operatingGMCmdLog.setCmd(cmd.toString());
        if ("gmGetShopInfo".equalsIgnoreCase(cmd.get("cmd").toString())) {
            resInvokeGMCmdMsg = "";
        }
        if ("gmGetKeyWords".equalsIgnoreCase(cmd.get("cmd").toString())) {
            resInvokeGMCmdMsg = "";
        }
        operatingGMCmdLog.setResult(resInvokeGMCmdMsg.trim());
        LogService.getInstance().execute(operatingGMCmdLog);
    }

    @GmHelp(gmDesc = "发布公告", parms = {@ParmDec(pramFiled = "params", pramDesc = "脚本ID")})
    public String gmPublishAnnounce(Map<String, Object> cmdMap) {
        int type = Integer.parseInt(cmdMap.get("type").toString());
        String content = cmdMap.get("content").toString();
        Notify notify = Notify.MARQUEE;
        if (type != 1) {
            notify = Notify.CHAT;
        }
        MessageUtils.notify_allOnlinePlayer(notify, MessageString.WANGNENGTISHI, content);
        return result(true, "发送成功", "");
    }

    @GmHelp(gmDesc = "发布运营活动幸运值", parms = {@ParmDec(pramFiled = "totalLuckyValue", pramDesc = "幸运值")})
    public String gmPublishLuckyValue(Map<String, Object> cmdMap) {
        int totalLuckyValue = Integer.parseInt(cmdMap.get("totalLuckyValue").toString());
        Manager.activityManager.setTotalLuckyValue(totalLuckyValue);
        ServerParamUtil.saveTotalLuckyValue();
        log.info("Gm 后台设置活动幸运值 value={}", totalLuckyValue);
        return result(true, "设置成功", "");
    }

    @GmHelp(gmDesc = "热更新脚本", parms = {@ParmDec(pramFiled = "params", pramDesc = "脚本ID")})
    public String gmReloadScript(Map<String, Object> cmdMap) {
        boolean success = true;
        List<Integer> successList = new ArrayList<>();
        List<Integer> failedList = new ArrayList<>();
        String[] scripts = cmdMap.get("params").toString().split(",");
        for (String scriptIdStr : scripts) {
            int scriptId = Integer.parseInt(scriptIdStr);
            try {
                success = ScriptManager.getInstance().reload(scriptId);
                if (success) {
                    successList.add(scriptId);
                } else {
                    failedList.add(scriptId);
                }
            } catch (Exception e) {
                log.error(e, e);
                e.printStackTrace();
                success = false;
                failedList.add(scriptId);
            }
        }
        return result(success, "更新脚本, 成功列表：" + Arrays.toString(successList.toArray())
                + ",失败列表：" + Arrays.toString(failedList.toArray()), "");
    }

    @GmHelp(gmDesc = "热更新脚本", parms = {@ParmDec(pramFiled = "params", pramDesc = "配置表名字")})
    public String gmReloadConfig(Map<String, Object> cmdMap) {
        boolean success = true;
        List<String> failedList = new ArrayList<>();
        List<String> successList = new ArrayList<>();
        String[] configNameList = cmdMap.get("params").toString().split(",");
        for (String configName : configNameList) {
            try {

                //TODO 添加全脚本名支持
                configName = configName.replace(".java", "").replace("Cfg_", "").replace("_Load", "");

                char oldChar = configName.charAt(0);
                char newChar = (oldChar + "").toUpperCase().charAt(0);
                String replace = configName.replaceFirst(oldChar + "", newChar + "");
                String reloadName = "config.Cfg_" + replace + "_Load";
                success = ScriptConfigManager.GetInstance().reloadConfigScript(reloadName);

                if (success) {
                    switch (configName.toLowerCase()) {
                        case "item":
                        case "equip":
                            ScriptConfigManager.GetInstance().reloadCofigItem();
                            break;
                        case "functionstart":
                            Manager.controlManager.deal().reload();
                            break;
                        case "shop_maket":
                            Manager.shopManager.reset();
                            break;
                        default:
                            break;
                    }
                    successList.add(configName);
                } else {
                    failedList.add(configName);
                }
            } catch (Exception e) {
                log.error(e, e);
                e.printStackTrace();
                success = false;
                failedList.add(configName);
            }
        }
        return result(success, "更新配置表,成功列表：" + Arrays.toString(successList.toArray())
                + "失败列表" + Arrays.toString(failedList.toArray()), "");
    }

    @GmHelp(gmDesc = "删除活动", parms = {@ParmDec(pramFiled = "id", pramDesc = "活动ID")})
    public String gmActivityDeleteMess(Map<String, Object> cmdMap) {
        int type = Integer.parseInt(cmdMap.get("type").toString());
        log.error("收到后台删除活动，actId=" + type);
        //先判断是否已删除过了
//        if (!Manager.activityManager.getConfigDao().isActivityDeleted(type)) {
//            return result(FAILURE, "失败", "此服该活动已删除过了");
//        }
        //数据库中删除成功了才通知到GameServer删除缓存中的
        if (Manager.activityManager.getConfigDao().delete(DbSqlName.ACTIVITYCONFIG_DELETE.getName(), type) == 1) {
            Manager.activityManager.deal().batchDelActivity(type);
        } else {
            result(FAILURE, type + "活动删除失败(活动数据库中删除失败)!", "");
        }
        return result(SUCCESS, type + "活动删除成功！", "");
    }

    @GmHelp(gmDesc = "批量删除活动", parms = {@ParmDec(pramFiled = "data", pramDesc = "一批活动id(列表)")})
    public String gmBatchDeleteActMess(Map<String, Object> cmdMap) {
        String actIdData = cmdMap.get("actIds").toString();
        String actTypeData = cmdMap.get("actTypes").toString();
        log.error("收到后台请求批量删除活动,Id:" + actIdData + ",type:" + actTypeData);

        List<Integer> delIdList = JsonUtils.parseArray(actIdData, Integer.class);
        List<Integer> delActList = JsonUtils.parseArray(actTypeData, Integer.class);
        //保存删除失败的活动id列表
        List<Integer> delFailList = new ArrayList<>();
        for (int i = 0; i < delIdList.size(); i++) {
            if (Manager.activityManager.getConfigDao().delete(DbSqlName.ACTIVITYCONFIG_DELETE.getName(), delIdList.get(i)) == 1) {
                Manager.activityManager.deal().batchDelActivity(delActList.get(i));
            } else {
                delFailList.add(delActList.get(i));
            }
        }
        if (!delFailList.isEmpty()) {
            return result(FAILURE, "", delFailList.toString());
        } else {
            return result(SUCCESS, "批量删除成功", "");
        }
    }

    @GmHelp(gmDesc = "批量发布活动", parms = {@ParmDec(pramFiled = "data", pramDesc = "活动信息(列表)")})
    public String gmBatchSendActMess(Map<String, Object> cmdMap) {
        log.error("收到后台请求批量发布活动");
        List<HashMap<String, Object>> actList = (List<HashMap<String, Object>>) cmdMap.get("data");
        List<ActivityConfigBean> activityBeans = new ArrayList<>();
        for (Map<String, Object> actMap : actList) {
            ActivityConfigBean actBean = initActBean(actMap);
            activityBeans.add(actBean);
        }
        List<Integer> failedList = Manager.activityManager.deal().w2gBatchSyncActivity(activityBeans);
        for (ActivityConfigBean actBean : activityBeans) {
            if (failedList.contains(actBean.getId())) {
                continue;
            }
            if (Manager.activityManager.getConfigDao().isExitActivity(actBean.getId())) {
                Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_UPDATE.getName(), actBean);
            } else {
                Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_INSERT.getName(), actBean);
            }
        }
        if (failedList.isEmpty()) {
            return result(SUCCESS, "批量发布成功", "");
        } else {
            return result(FAILURE, "", failedList.toString());
        }
    }

    /**
     * 根据后台发来的活动数据map生成活动Bean
     *
     * @param map 后台发来的活动数据
     * @return 活动Bean
     */
    private ActivityConfigBean initActBean(Map<String, Object> map) {
        ActivityConfigBean actBean = new ActivityConfigBean();
        try {
            int timeType = (int) map.get("timeType");
            int openServerOffsetBegin = (int) map.get("openServerOffsetBegin");
            if (openServerOffsetBegin < 1) {
                openServerOffsetBegin = 1;
            }
            int openServerOffset = (int) map.get("openServerOffset");
            if (openServerOffset < 1) {
                openServerOffset = 1;
            }
            int openServerRecordOffsetBegin = (int) map.get("openServerRecordOffsetBegin");
            if (openServerRecordOffsetBegin < 0) {
                openServerRecordOffsetBegin = 0;
            }
            int openServerRecordOffset = (int) map.get("openServerRecordOffset");
            if (openServerRecordOffset < 0) {
                openServerRecordOffset = 0;
            }
            actBean.setId((int) map.get("id"));
            actBean.setMinLv((int) map.get("minLv"));
            actBean.setMaxLv((int) map.get("maxLv"));

            //TODO 活动唯一类型特殊处理   活动逻辑类型*1000+节日类型
            actBean.setType(((int) map.get("type")) * 1000 + ((int) map.get("subType")));
            actBean.setTag(Byte.valueOf(map.get("tag").toString()));
            actBean.setSort(Byte.valueOf(map.get("sort").toString()));
            actBean.setName(map.get("name").toString());
            if (timeType == 0) {
                actBean.setBeginTime(DateFormatUtils.parse(map.get("beginTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                actBean.setEndTime(DateFormatUtils.parse(map.get("endTime").toString(), "yyyy-MM-dd HH:mm").getTime());

                actBean.setStartRecordTime(map.get("startRecordTime").toString().equals("") ? actBean.getBeginTime() : DateFormatUtils.parse(map.get("startRecordTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                actBean.setEndRecordTime(map.get("endRecordTime").toString().equals("") ? actBean.getEndTime() : DateFormatUtils.parse(map.get("endRecordTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                if (actBean.getStartRecordTime() <= 0) {
                    actBean.setStartRecordTime(actBean.getBeginTime());
                }
                if (actBean.getEndRecordTime() <= 0) {
                    actBean.setEndRecordTime(actBean.getEndTime());
                }

            } else if (timeType == 1) {
                long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
                actBean.setBeginTime(openTime + ((openServerOffsetBegin - 1) * 24 * 3600 * 1000l));
                actBean.setEndTime(actBean.getBeginTime() + (openServerOffset * 24 * 3600 * 1000l));

                if (openServerRecordOffsetBegin <= 0) {
                    actBean.setStartRecordTime(actBean.getBeginTime());
                } else {
                    actBean.setStartRecordTime(openTime + ((openServerRecordOffsetBegin - 1) * 24 * 3600 * 1000l));
                }

                if (openServerRecordOffset <= 0) {
                    actBean.setEndRecordTime(actBean.getEndTime());
                } else {
                    actBean.setEndRecordTime(actBean.getStartRecordTime() + (openServerRecordOffset * 24 * 3600 * 1000l));
                }

            }

            actBean.setCustom(map.get("custom").toString());
            actBean.setState(Byte.valueOf(map.get("cover").toString()));
            actBean.setIsOpenServer(Byte.valueOf(map.get("isOpenServer").toString()));
        } catch (ParseException e) {
            log.error("ParseException!" + e);
        }
        return actBean;
    }

    /**
     * 根据后台发来的活动数据map生成活动Bean
     *
     * @param map 后台发来的活动数据
     * @return 活动Bean
     */
    private ActivityConfigBean initActivityBean(Map<String, Object> map) {
        ActivityConfigBean actBean = new ActivityConfigBean();
        try {
            actBean.setId((int) map.get("id"));
            actBean.setType((int) map.get("type"));
            actBean.setMinLv((int) map.get("minLv"));
            actBean.setMaxLv((int) map.get("maxLv"));
            actBean.setTag((Byte) map.get("tag"));
            actBean.setSort((Byte) map.get("sort"));
            actBean.setName(map.get("name").toString());
            int timeType = (int) map.get("timeType");
            int openServerOffsetBegin = (int) map.get("openServerOffsetBegin");
            int openServerOffset = (int) map.get("openServerOffset");
            int openServerRecordOffsetBegin = (int) map.get("openServerRecordOffsetBegin");
            int openServerRecordOffset = (int) map.get("openServerRecordOffset");

            if (timeType == 0) {
                actBean.setBeginTime(DateFormatUtils.parse(map.get("beginTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                actBean.setEndTime(DateFormatUtils.parse(map.get("endTime").toString(), "yyyy-MM-dd HH:mm").getTime());

                actBean.setStartRecordTime(map.get("startRecordTime").toString().equals("") ? actBean.getBeginTime() : DateFormatUtils.parse(map.get("startRecordTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                actBean.setEndRecordTime(map.get("endRecordTime").toString().equals("") ? actBean.getEndTime() : DateFormatUtils.parse(map.get("endRecordTime").toString(), "yyyy-MM-dd HH:mm").getTime());
                if (actBean.getStartRecordTime() <= 0) {
                    actBean.setStartRecordTime(actBean.getBeginTime());
                }
                if (actBean.getEndRecordTime() <= 0) {
                    actBean.setEndRecordTime(actBean.getEndTime());
                }

            } else if (timeType == 1) {
                long openTime = TimeUtils.getBeginTime(TimeUtils.getOpenServerTime());
                actBean.setBeginTime(openTime + (openServerOffsetBegin * 24 * 3600 * 1000l));
                actBean.setEndTime(actBean.getBeginTime() + (openServerOffset * 24 * 3600 * 1000l));

                actBean.setStartRecordTime(openTime + (openServerRecordOffsetBegin * 24 * 3600 * 1000l));
                actBean.setEndRecordTime(actBean.getBeginTime() + (openServerRecordOffset * 24 * 3600 * 1000l));
            }

            actBean.setCustom(map.get("custom").toString());
            actBean.setState((Byte) map.get("cover"));
            actBean.setIsOpenServer(Byte.valueOf(map.get("isOpenServer").toString()));
        } catch (ParseException e) {
            log.error("ParseException!" + e);
        }
        return actBean;
    }

    @Override
    @GmHelp(gmDesc = "发送单个活动", parms = {@ParmDec(pramFiled = "data", pramDesc = "活动信息")})
    public String gmActivitySendMess(Map<String, Object> cmdMap) {
        log.info("收到后台发布活动，actId=" + cmdMap.get("id"));
        ActivityConfigBean actBean = initActivityBean(cmdMap);
        boolean result = Manager.activityManager.deal().w2gSyncActivity(actBean);
        if (Manager.activityManager.getConfigDao().isExitActivity(actBean.getId())) {
            //允许重新发布活动的
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_UPDATE.getName(), actBean);
        } else {
            Manager.activityManager.getConfigDao().insert(DbSqlName.ACTIVITYCONFIG_INSERT.getName(), actBean);
        }

        return result(result, result ? "发布成功！" : "发布失败！", "");
    }


    @GmHelp(gmDesc = "扣元宝和月卡功能",
            parms = {@ParmDec(pramFiled = "roleId", pramDesc = "角色ID"),
                    @ParmDec(pramFiled = "num", pramDesc = "扣除数量"),
                    @ParmDec(pramFiled = "type", pramDesc = "类型"),
                    @ParmDec(pramFiled = "sign", pramDesc = "签名")})
    public String gmDealPlayerMoney(Map<String, Object> cmdMap) {
        return result(FAILURE, "暂不支持该操作", "");
    }

    @GmHelp(gmDesc = "扣除道具",
            parms = {@ParmDec(pramFiled = "roleId", pramDesc = "角色ID"),
                    @ParmDec(pramFiled = "itemId", pramDesc = "道具Id"),
                    @ParmDec(pramFiled = "itemNum", pramDesc = "道具数量"),
                    @ParmDec(pramFiled = "isMail", pramDesc = "是否发送邮件"),
                    @ParmDec(pramFiled = "isBind", pramDesc = "是否绑定"),
                    @ParmDec(pramFiled = "mailTitle", pramDesc = "邮件标题"),
                    @ParmDec(pramFiled = "mailContent", pramDesc = "邮件内容"),
                    @ParmDec(pramFiled = "sendUser", pramDesc = "发送者"),
            })
    public String gmDeductItemOpt(Map<String, Object> cmdMap) {
        long roleId = Long.parseLong(cmdMap.get("roleId").toString());
        int itemId = Integer.parseInt(cmdMap.get("itemId").toString());
        int itemNum = Integer.parseInt(cmdMap.get("itemNum").toString());
        int isBind = Integer.parseInt(cmdMap.get("isBind").toString());
        int isMail = Integer.parseInt(cmdMap.get("isMail").toString());
        String mailTitle = cmdMap.get("mailTitle").toString();
        String mailContent = cmdMap.get("mailContent").toString();
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            return result(FAILURE, "玩家不在线", "0");
        } else {
            long actionId = IDConfigUtil.getLogId();
            //判断物品是否为货币，如果是货币，扣除为0
            boolean isCoinItem = Manager.currencyManager.manager().isCoinItem(itemId);
            if (!isCoinItem) {
                int backItemCount = Manager.backpackManager.manager().getItemNum(player, itemId);
                if (backItemCount < itemNum) {
                    itemNum = backItemCount;
                }
                Manager.backpackManager.manager().onRemoveItem(player, itemId, itemNum, isBind == 1, ItemChangeReason.GMDeductItemDec, actionId);
            } else {
                long coinCount = getPlayerCoinNum(player, itemId);
                if (coinCount < itemNum) {
                    itemNum = (int) coinCount;
                }
                Manager.currencyManager.manager().onDecItemCoin(player, itemNum, ItemChangeReason.GMDeductItemDec, actionId, itemId);
            }
            if (isMail == 1) {
                Manager.mailManager.sendMailToPlayer(player.getId(), 2, MessageString.System, mailTitle, mailContent);
            }
            //强实际扣除的数量返回
            return result(SUCCESS, "扣除成功", itemNum + "");
        }
    }

    private long getPlayerCoinNum(Player player, int coinId) {
        return Manager.currencyManager.manager().getCurrencyNum(player, coinId);
    }

    @GmHelp(gmDesc = "排行榜数据获取", parms = {@ParmDec(pramFiled = "rankKind", pramDesc = "排行榜类型")})
    public String gmQueryRankList(Map<String, Object> cmdMap) {
        int rankKind = Integer.parseInt(cmdMap.get("rankKind").toString());
        log.error("收到后台查询排行榜，rankKind=" + rankKind);
        //根据rankKind获取其排行榜信息数据
        try {
            IRankScript rankClass = Manager.rankListManager.getRankScript(rankKind);
            if (rankClass == null) {
                return result(FAILURE, "后台查询排行榜传递的rankKind错误！", "");
            }

            List<RankListMessage.RankInfo.Builder> infoList = rankClass.getRankInfo();
            if (infoList == null) {
                return result(FAILURE, "rankList == null！", "");

            } else {
                List<RankData> rankList = new ArrayList<>();
                for (RankListMessage.RankInfo.Builder info : infoList) {
                    RankData rank = new RankData();
                    rank.setRank(info.getRank());
                    rank.setRoleId(info.getRoleId());
                    rank.setRoleName(info.getRoleName());
                    rank.setRankData(info.getRankData());
                    rankList.add(rank);
                }
                rankList.sort(new RankListSort());
                return result(SUCCESS, "", JsonUtils.toJSONString(rankList));
            }
        } catch (Exception exception) {
            log.error(exception);
        }
        return result(FAILURE, "查询失败", "");
    }


    @GmHelp(gmDesc = "请求请求开关游戏功能")
    public String gmGetFuncOpenList(Map<String, Object> cmdMap) {
        List<FuncOpenData> funcList = Manager.controlManager.deal().getBackFuncList();
        return result(SUCCESS, "", funcList);
    }

    /**
     * 请求开关游戏功能
     */
    @GmHelp(gmDesc = "请求开关游戏功能", parms = {@ParmDec(pramFiled = "data", pramDesc = "{功能id:功能状态}列表"),})
    public String gmSwitchFunction(Map<String, Object> cmdMap) {
        log.error("收到后台请求开关游戏功能");
        List<Map<String, Integer>> dataList = (List<Map<String, Integer>>) cmdMap.get("data");
        List<FuncOpenData> switchFuncList = new ArrayList<>();
        FuncOpenData funcOpenData;
        for (Map<String, Integer> dataMap : dataList) {
            for (Map.Entry<String, Integer> entry : dataMap.entrySet()) {
                funcOpenData = new FuncOpenData();
                funcOpenData.setId(Integer.parseInt(entry.getKey()));
                funcOpenData.setOpenState(entry.getValue());
                switchFuncList.add(funcOpenData);
            }
        }
        boolean switchResult = Manager.controlManager.deal().changeBackFuncs(switchFuncList);
        return result(switchResult, "", "");
    }

    /**
     * 设置玩家头像
     */
    @GmHelp(gmDesc = "设置玩家头像", parms = {@ParmDec(pramFiled = "params", pramDesc = "{roleId:'',state:''}")})
    public String gmsetroleiconstate(Map<String, Object> cmdMap) {
        boolean setSuccess = false;
        StringBuilder mes = new StringBuilder();
        String pValue = cmdMap.get("params").toString();
        String value = pValue.trim();
        String[] tmp = value.split(",");
        if (tmp.length < 2) {
            log.error("GM后台玩家的头像状态 : gmSetRoleiconstate执行失败， 没有该命令！ param=" + pValue);
            return result(FAILURE, "失败", "");
        }
        try {
            long roleId = Long.parseLong(tmp[0]);
            int state = Integer.parseInt(tmp[1]);
            roleBean role = new roleBean();
            role.setRoleid(roleId);
            role.setUseIconState(state);
            int updatestate = ROLE_DAO.updateIconStateByRoleId(role);

            mes.append("更新玩家的头像操作结果：").append(updatestate > 0);
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (player != null) {
                player.setUseIconState(state);
                if (state == 0) {
                    Manager.countManager.addVariant(player, VariantType.UP_CUSTOM_AVATAR, 1);
                    if (!"tw".equalsIgnoreCase(ServerConfig.getLangType())) {
                        MessageUtils.notify_Chat_To_AllPlayer(player, ChatChannel.CHATCHANNEL_WORLD, MessageString.ICONUPDATEWORLD);//发送全服世界频道
                    }
                }
                log.info("state : " + state);
                if (state == 1) {
                    if ("tw".equalsIgnoreCase(ServerConfig.getLangType())) {
                        MessageUtils.notify_Chat_To_AllPlayer(player, ChatChannel.CHATCHANNEL_WORLD, MessageString.ICONUPDATEWORLD);//发送全服世界频道
                    }
                }
            }
            setSuccess = updatestate > 0;
        } catch (Exception e) {
            mes.append("更新玩家的头像操作结果：出异常了！");
        }
        if (mes.length() < 1) {
            return result(setSuccess, setSuccess ? "成功" : "失败", "");
        } else {
            return result(setSuccess, mes.toString(), "");
        }
    }

    @GmHelp(gmDesc = "生成gm文档")
    @Override
    public String gmBuildGmDoc(Map<String, Object> cmdMap) {
        Class cl1 = BackCommandScript.class;
        Method[] methods = cl1.getDeclaredMethods();
        StringBuilder sb = new StringBuilder();
        int line = 1;
        for (Method method : methods) {
            GmHelp apiOperation = method.getAnnotation(GmHelp.class);
            String name = method.getName();
            if (!name.contains("gm")) {
                continue;
            }
            if (apiOperation == null) {
                sb.append(line).append(". 方法未注解").append("=[").append(name).append("]\n");
            } else {
                sb.append(line).append(". ").append(apiOperation.gmDesc()).append("=[").append(name).append("]\n");
                sb.append("{命令:'test',数值参数:123,数值参数:444,字符串参数:'物品名字,怪物名字'}\n");
                sb.append("cmd:").append(name).append("\n");
                ParmDec[] parms = apiOperation.parms();
                for (ParmDec parm : parms) {
                    if (StringUtil.isNullOrEmpty(parm.pramFiled())) break;
                    sb.append(parm.pramFiled()).append(":'***'    //");
                    sb.append(parm.pramDesc()).append("\n");
                }
                sb.append("\n\n");
            }
            line++;
        }
        FileOutputStream fileoutputStream = null;
        OutputStreamWriter outputStreamwrite = null;
        BufferedWriter bufferedwrite = null;
        try {
            File file = new File("config/gm.txt");
            fileoutputStream = new FileOutputStream(file);
            outputStreamwrite = new OutputStreamWriter(fileoutputStream, StandardCharsets.UTF_8);
            bufferedwrite = new BufferedWriter(outputStreamwrite);
            bufferedwrite.write(sb.toString());
            bufferedwrite.flush();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("生成GM文档失败！！！！", e);
        } finally {
            try {
                if (fileoutputStream != null) {
                    fileoutputStream.close();
                }
                if (outputStreamwrite != null) {
                    outputStreamwrite.close();
                }
                if (bufferedwrite != null) {
                    bufferedwrite.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result(SUCCESS, "生成文档成功", "");
    }

    @GmHelp(gmDesc = "获取屏蔽关键字")
    public String gmGetKeyWords(Map<String, Object> cmdMap) {
        String resultStr = JsonUtils.toJSONString(Manager.chatManager.getForbidWords());
        return result(SUCCESS, resultStr, "");
    }

    @GmHelp(gmDesc = "添加屏蔽关键字", parms = {@ParmDec(pramFiled = "params", pramDesc = "{word:关键字,type:类型}"),})
    public String gmAddKeyWord(Map<String, Object> cmdMap) {
        String type = cmdMap.get("type").toString();
        String word = cmdMap.get("keyword").toString();
        ForbidWordBean bean = new ForbidWordBean();
        bean.setType(Integer.parseInt(type));
        bean.setWord(word);
        int id = Manager.chatManager.getWordDao().insert(bean);
        boolean isOk = false;
        if (id > 0) {
            Manager.chatManager.getForbidWords().put(bean.getId(), bean);
            ChatMessage.ResForbidWord.Builder builder = ChatMessage.ResForbidWord.newBuilder();
            builder.setType(1);
            builder.addWordList(bean.getWord());
            MessageUtils.send_to_all_player(ChatMessage.ResForbidWord.MsgID.eMsgID_VALUE, builder.build().toByteArray());
            //返回信息
            isOk = true;
        }
        return result(isOk, isOk ? "添加字成功" : "添加字失败", "");
    }

    @GmHelp(gmDesc = "删除屏蔽关键字")
    public String gmDeleteKeyWords(Map<String, Object> cmdMap) {
        int id = Integer.parseInt(cmdMap.get("id").toString());
        int result = Manager.chatManager.getWordDao().delete(id);
        if (result > 0) {
            ForbidWordBean bean = Manager.chatManager.getForbidWords().remove(id);
            ChatMessage.ResForbidWord.Builder builder = ChatMessage.ResForbidWord.newBuilder();
            builder.setType(2);
            builder.addWordList(bean.getWord());
            MessageUtils.send_to_all_player(ChatMessage.ResForbidWord.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
        return result(SUCCESS, result > 0 ? "删除成功" : "删除失败", "");
    }

    @GmHelp(gmDesc = "更新聊天黑名单")
    public String gmLoadChatBlackList(Map<String, Object> cmdMap) {
        Manager.chatManager.loadChatBlackList();
        return result(true, "加载成功", "");
    }

    @GmHelp(gmDesc = "更新聊天替换关键字")
    public String gmLoadChatWord(Map<String, Object> cmdMap) {
        Manager.chatManager.loadChatWord();
        return result(true, "加载成功", "");
    }

    @GmHelp(gmDesc = "踢人下线", parms = {@ParmDec(pramFiled = "roleId", pramDesc = "目标玩家Id"),})
    public String gmKickPlayer(Map<String, Object> cmdMap) {
        long roleId = Long.parseLong(cmdMap.get("roleId").toString());
        List<Player> players = new ArrayList<>();
        if (roleId == 0) {
            players = Manager.playerManager.getOnLines();
        } else {
            Player player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null) {
                return result(false, "玩家不在线", "");
            }
            players.add(player);
        }
        for (Player player : players) {
            player.setReconnectTime(0);
            Manager.playerManager.iQuitGame().QuitGame(player.getIosession(), QuitGameDefine.GM, false,true);
        }
        return result(true, "操作成功", "");
    }

    @GmHelp(gmDesc = "玩家禁言")
    public String gmForbidChat(Map<String, Object> cmdMap) {
        Manager.chatManager.loadForbidChat();
        return result(true, "加载完成", "");
    }

    @GmHelp(gmDesc = "发送邮件")
    public String gmSendMail(Map<String, Object> cmdMap) {
        boolean isAllServerMail = cmdMap.get("roleIds").equals("all");
        String title = cmdMap.get("title").toString();
        String content = cmdMap.get("content").toString();
        ReadIntegerArrayEs items = new ReadIntegerArrayEs(cmdMap.get("items").toString(), "}", ",");
        List<Item> itemList = Item.createItems(items);
        if (isAllServerMail) {
            for (Long roleId : Manager.playerManager.getAllPlayerWorldInfo().keySet()) {
                MailManager.getInstance().sendMailToPlayer(roleId, 2, MessageString.System, title, content, itemList, ItemChangeReason.GMGet);
            }
        } else {
            String[] roleIdStrArray = cmdMap.get("roleIds").toString().split(",");
            for (String roleIdStr : roleIdStrArray) {
                long roleId = Long.parseLong(roleIdStr);
                MailManager.getInstance().sendMailToPlayer(roleId, 2, MessageString.System, title, content, itemList, ItemChangeReason.GMGet);
            }
        }
        return result(true, "发送成功", "");
    }

    @GmHelp(gmDesc = "发送更新公告")
    public String gmSendUpdateNotice(Map<String, Object> cmdMap) {
        IUpdateNoticeScript script = (IUpdateNoticeScript) WelfareManager.getInstance().getScript(WelfareMessage.WelfareType.UpdateNotice);
        if (script != null) {
            //公告内容
            String text = cmdMap.get("content").toString();
            //物品信息
            String items = cmdMap.get("items").toString();
            //是否清理领取信息
            String reset = cmdMap.get("resetReceives").toString();
            //log.error("更新文本:"+text);
            script.refreshNotice(text, items, reset.equals("1"));
        }
        return result(true, "发送更新公告成功", "");
    }

    @GmHelp(gmDesc = "设置gm玩家")
    public String setGM(Map<String, Object> cmdMap) {
        long roleId = Long.parseLong(cmdMap.get("params").toString());
        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return result(FAILURE, "未找到玩家", "");
        }
        player.setGM(!player.isGM());
        log.info("玩家【" + player.getName() + "】设置gm：" + player.isGM());
        return result(SUCCESS, "成功", "");
    }

    @GmHelp(gmDesc = "后台测试连接")
    public String gmTest(Map<String, Object> cmdMap) {
        Manager.dailyActiveManager.getDailyOpenState().put(207, 3);
        return result(SUCCESS, "成功", "");
    }
	
	@GmHelp(gmDesc = "后台清理掉线玩家")
    public String clearChannel(Map<String, Object> cmdMap){

        Object[] objs = ClientMsgAdapter.getChannels().toArray();
        for (Object obj : objs) {
            if (obj == null) {
                continue;
            }
            Channel ioSession = (Channel) obj;
            ByteBuf sendbuf;
            try {
                synchronized (ioSession) {

                    if (ioSession.unsafe() == null) {
                        log.info(ioSession + "发送队列时， 连接已经断开了！2");
                        continue;
                    }
                    if (!ioSession.isActive()) {
                        log.info(ioSession + "发送队列时， 连接已经断开了！4");
                        SessionUtils.release(ioSession);
                        continue;
                    }
                    if (ioSession.unsafe().outboundBuffer() == null) {
                        log.info(ioSession + "发送队列时， 连接已经断开了！3");
                        //ioSession.attr(SEND_BUF).set(null);
                        SessionUtils.release(ioSession);
                        continue;
                    }
                    if (!ioSession.isWritable()) {
                        SessionUtils.release(ioSession);
                        ClientMsgAdapter.getChannels().remove(ioSession);
                        log.info(ioSession + "发送队列时，暂时不可写！ size=" + ioSession.unsafe().outboundBuffer().totalPendingWriteBytes());
                    }
                }
            } catch (Exception e) {
                log.info(e.toString());
            }
        }
        return result(SUCCESS, "成功", "");
    }

    @GmHelp(gmDesc = "设置服务器开服时间")
    public String dealOpsTime(Map<String, Object> cmdMap) throws ParseException {
        if (cmdMap.get("time") == null) {
            return result(SUCCESS, "", ServerConfig.getServerOpenTime());
        }
        String time = cmdMap.get("time").toString();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String newOpsTime = sdf.format(sdf.parse(time));
        ServerConfig.setOpenTime(newOpsTime);
        Manager.biManager.getScript().BiServer_op(103);
        ServerParamUtil.serverOpenTime = newOpsTime;
        ServerParamUtil.saveServerOpenTime();

        Manager.fallingSkyManager.deal().onOpenTimeChange();//设置开服时间后天禁令 重置到第一轮

        CrossServerMessage.G2PServerOpentimeChange.Builder msg = CrossServerMessage.G2PServerOpentimeChange.newBuilder();
        msg.setServerId(ServerConfig.getServerId());
        msg.addAllServerIdsList(ServerConfig.getServerIdList());
        msg.setServeropentime(newOpsTime);
        msg.setPlat(ServerConfig.getServerPlatform());
        MessageUtils.send_to_public(CrossServerMessage.G2PServerOpentimeChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return result(SUCCESS, "服务器开服时间设置成功：" + ServerConfig.getServerOpenTime(), "");
    }

    @GmHelp(gmDesc = "设置刷新服务器开服时间")
    public String refreshOpenTime(){
        CrossServerMessage.G2PServerOpentimeChange.Builder msg = CrossServerMessage.G2PServerOpentimeChange.newBuilder();
        msg.setServerId(ServerConfig.getServerId());
        msg.addAllServerIdsList(ServerConfig.getServerIdList());
        msg.setServeropentime(ServerConfig.getServerOpenTime());
        msg.setPlat(ServerConfig.getServerPlatform());
        MessageUtils.send_to_public(CrossServerMessage.G2PServerOpentimeChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return result(SUCCESS, "刷新开服时间成功：" + ServerConfig.getServerOpenTime(), "");
    }

    @GmHelp(gmDesc = "设置游戏服时间")
    public String setGameTime(Map<String, Object> cmdMap) throws ParseException {
        if (!ServerConfig.isTestServer()) {
            return result(FAILURE, "服务器时间设置失败，必须是测试服", "");
        }
        int skip = Integer.parseInt(cmdMap.get("params").toString());
        if (skip > 0) {
            TimeUtils.setTime(TimeUtils.Time() + skip * 60 * 1000);
        }
        return result(SUCCESS, "服务器时间设置成功：" + TimeUtils.NowToString(), "");
    }

    @GmHelp(gmDesc = "设置游戏服时间")
    public String setTime(Map<String, Object> cmdMap) throws ParseException {
        if (!ServerConfig.isTestServer()) {
            return result(FAILURE, "服务器时间设置失败，必须是测试服", "");
        }
        String date = cmdMap.get("params").toString();
        long setTime = TimeUtils.Time();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        Date d = sdf.parse(date);
        if (setTime > d.getTime()) {
            return result(FAILURE, "服务器时间设置失败", "");
        }
        setTime = d.getTime() - d.getTime() % 60000 + setTime % 60000;
        TimeUtils.setTime(setTime);
        return result(SUCCESS, "服务器时间设置成功：" + TimeUtils.NowToString(), "");
    }

    @GmHelp(gmDesc = "设置服务器注册限制人数")
    public String setRegisterLimitNum(Map<String, Object> cmdMap) {
        String num = cmdMap.get("num").toString();
        if (num == null || num.isEmpty() || "0".equals(num)) {
            return result(SUCCESS, "", ServerParamUtil.registerNumLimit);
        }
        ServerParamUtil.registerNumLimit = Integer.parseInt(num);
        ServerParamUtil.saveServerRegisterLimit();
        return result(SUCCESS, "服务器注册限制人数设置成功：" + ServerParamUtil.registerNumLimit, "");
    }

    @GmHelp(gmDesc = "转移角色")
    public String tranRole(Map<String, Object> cmdMap) {
        String roleIdStr = cmdMap.get("tranRoleId").toString();
        String userIdStr = cmdMap.get("tranUserId").toString();

        if (roleIdStr == null || userIdStr == null || roleIdStr.isEmpty() || userIdStr.isEmpty()) {
            return result(FAILURE, "转移参数填写错误", "");
        }

        long userId = Long.parseLong(userIdStr);
        long roleId = Long.parseLong(roleIdStr);

        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(roleId);
        if (playerWorldInfo == null) {
            return result(FAILURE, "转移角色不存在", "");
        }

        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return result(FAILURE, "转移角色不存在", "");
        }

        if (player.getUserId() == userId) {
            return result(FAILURE, "账号ID相同", "");
        }

        long oldUserId = playerWorldInfo.getUserId();
        UserInfo tranUserInfo = Manager.registerManager.getUserInfo(oldUserId);
        if (tranUserInfo == null || tranUserInfo.getRoles().size() == 0) {
            return result(FAILURE, "转移角色不存在", "");
        }

        UserInfo userInfo = Manager.registerManager.getUserInfo(userId);
        if (userInfo.getRoles().size() >= 4) {
            return result(FAILURE, "角色已满", "");
        }

        //移除
        tranUserInfo.getRoles().remove(roleId);

        //增加
        player.setUserId(userId);
        playerWorldInfo.setUserId(userId);
        UserRoleInfo uRole = UserRoleInfo.initCreatePlayerToInfo(player);
        userInfo.getRoles().put(uRole.getRoleId(), uRole);

        roleBean role = Manager.playerManager.manager().makeRoleBeanByPlayer(player);
        Manager.registerManager.getDao().updateUserId(role);
        RoleUpdateLogService.getInstance().updateRoleDate(roleId);
        Manager.playerManager.manager().changeLoginUserId(player.getId(), player.getUserId());
        Manager.saveThreadManager.getOtherServerSave().deal(playerWorldInfo, DbSqlName.PLAYERWORLDINFO_UPDATE, SaveServer.UPDATE);

        Manager.biManager.get4399Script().updatePlayer(player);

        TranRoleLog log = new TranRoleLog();
        log.setRoleId(player.getId());
        log.setNewUserId(player.getUserId());
        log.setOldUserId(oldUserId);
        LogService.getInstance().execute(log);
        return result(SUCCESS, "转移角色成功", "");
    }

    @GmHelp(gmDesc = "后台模拟充值")
    public String gmRecharge(Map<String, Object> cmdMap) {
        int rechargeNum = Integer.parseInt(cmdMap.get("rechargeNum").toString());
        int rechargeTotalGold = Integer.parseInt(cmdMap.get("rechargeTotalGold").toString());
        int rechargeVipExp = Integer.parseInt(cmdMap.get("rechargeVipExp").toString());
        long roleId = Long.parseLong(cmdMap.get("roleId").toString());
        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return result(FAILURE, "玩家不存在", "");
        }
        Recharge recharge = new Recharge();
        recharge.setOrder_no("GM_ID_" + TimeUtils.Time());
        recharge.setGoods_id(0);
        recharge.setGoods_name("GM充值");
        recharge.setGoods_type("0");
        recharge.setGoods_ext("");
        recharge.setTotal_fee(rechargeNum);
        recharge.setItem_id(0);
        recharge.setGame_money(rechargeTotalGold);
        recharge.setRole_id(roleId);
        recharge.setExt_param("gm");
        recharge.setSign_type("1");
        recharge.setSign("");
        recharge.setMoney_type("CNY");
        recharge.setTotalRecharge(rechargeTotalGold);
        recharge.setTotalVipPower(rechargeVipExp);

        Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_BACKEND);
        return result(SUCCESS, String.format("玩家[%s]充值成功：%s", player.getName(), rechargeNum), "");
    }

    @GmHelp(gmDesc = "刷新充值列表")
    public String gmRefreshRechargeItemInfos(Map<String, Object> cmdMap) {
        String rechargeStr = cmdMap.get("rechargeInfos").toString();
        String md5Str = cmdMap.get("md5").toString();
//        log.info("gmRefreshRechargeItemInfos==============rechargeStr:"+rechargeStr);
//        log.info("gmRefreshRechargeItemInfos==============md5:"+md5Str);
        TreeMap<Integer, RechargeItemInfo> rechargeInfoMap = JsonUtils.parseObject(rechargeStr, new TypeReference<TreeMap<Integer, RechargeItemInfo>>() {
        });
        if (rechargeInfoMap == null || rechargeInfoMap.isEmpty()) {
            return result(FAILURE, "刷新充值列表失败", "0");
        }
        Manager.rechargeManager.setMd5(md5Str);
        Manager.rechargeManager.setRechargeItemInfoMap(rechargeInfoMap);
        Manager.rechargeManager.setRechargeItemJson(rechargeStr);
        return result(SUCCESS, "刷新充值列表成功", "1");
    }

    @GmHelp(gmDesc = "请求角色可够买商品")
    public String gmGetRoleRechargeItem(Map<String, Object> cmdMap) {
        int goods_id = Integer.parseInt(cmdMap.get("goods_id").toString());
        long roleId = Long.parseLong(cmdMap.get("roleId").toString());
        Player player = Manager.playerManager.getPlayer(roleId);
        if (player == null) {
            return result(FAILURE, "玩家不存在", "");
        }

        boolean isSuccess = false;
        String data = "";
        if (goods_id > 0) {
            RechargeItemInfo rechargeItemInfo = Manager.rechargeManager.getRechargeItemInfoMap().get(goods_id);
            if (rechargeItemInfo != null) {
                if (Manager.rechargeManager.deal().canReward(player, rechargeItemInfo)) {
                    isSuccess = true;
                }
            }
        } else {
            data = Manager.rechargeManager.deal().getThirdPayItemInfo(player);
            isSuccess = true;
        }
        return result(isSuccess, "请求角色可够买商品成功", data);
    }

    @GmHelp(gmDesc = "刷新商城命令")
    public String gmRefreshShop(Map<String, Object> cmdMap) {
        Manager.shopManager.reset();
        return result(SUCCESS, "刷新商城成功", "");
    }

    @GmHelp(gmDesc = "设置世界等级")
    public String gmSetWorldLevel(Map<String, Object> cmdMap) {
        int level = Integer.parseInt(cmdMap.get("params").toString());
        ServerParamUtil.worldLv = level;
        ServerParamUtil.saveWorldLv();
        Manager.playerHookManager.deal().worldLvChange();

        //服务器世界等级变化，通知公共服，公共服服务器分组用 世界等级 定义档次
        CrossServerMessage.G2PServerWorldLvChange.Builder msg = CrossServerMessage.G2PServerWorldLvChange.newBuilder();
        msg.setPlat(ServerConfig.getServerPlatform());
        msg.setServerId(ServerConfig.getServerId());
        msg.setServerWorldLv(level);
        MessageUtils.send_to_public(CrossServerMessage.G2PServerWorldLvChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return result(SUCCESS, "设置世界等级【" + level + "】完成", "");
    }

    @GmHelp(gmDesc = "获取商城信息")
    public String gmGetShopInfo(Map<String, Object> cmdMap) {
        int shopId = Integer.parseInt(cmdMap.getOrDefault("id", "0").toString());
        if (shopId != 0) {
            return result(SUCCESS, "", Manager.shopManager.deal().mall(shopId));
        }
        return result(SUCCESS, "", Manager.shopManager.deal().allMalls());
    }

    @GmHelp(gmDesc = "更新商品信息")
    public String gmSetShopInfo(Map<String, Object> cmdMap) {
        String shopStr = cmdMap.get("shopInfo").toString();
        ShopBean bean = JsonUtils.parseObject(shopStr, ShopBean.class);
        boolean flag = Manager.shopManager.deal().updateShop(bean);
        return result(flag, flag ? "更新商品成功:" + bean.getId() : "更新商品失败", "");
    }

    @GmHelp(gmDesc = "删除商品信息")
    public String gmDeleteShopInfo(Map<String, Object> cmdMap) {
        int shopId = Integer.parseInt(cmdMap.getOrDefault("id", "0").toString());
        boolean flag = Manager.shopManager.deal().deleteShop(shopId);
        return result(flag, flag ? "删除商品成功:" + shopId : "删除商品失败", "");
    }


    @GmHelp(gmDesc = "设置角色属性",
            parms = {@ParmDec(pramFiled = "roleId", pramDesc = "角色ID"),
                    @ParmDec(pramFiled = "attrType", pramDesc = "attrType"),
                    @ParmDec(pramFiled = "attrValue", pramDesc = "attrValue"),
            })
    public String gmSetRoleAttrOpt(Map<String, Object> cmdMap) {
        long roleId = Long.parseLong(cmdMap.get("roleId").toString());
        int attrType = Integer.parseInt(cmdMap.get("attrType").toString());
        int attrValue = Integer.parseInt(cmdMap.get("attrValue").toString());
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            return result(FAILURE, "玩家不在线", "0");
        }

        int realValue = 0;
        switch (attrType) {
            case 0://等级
                realValue = setLevel(player, attrValue);
                break;
            case 1://转职等级
                realValue = changeJob(player, attrValue);
                break;
            case 2://主线任务
                realValue = setMainTask(player, attrValue);
                break;
            default:
                break;
        }

        //实际属性值返回
        return result(SUCCESS, "设置成功", String.valueOf(realValue));
    }

    @GmHelp(gmDesc = "设置SDK评价开关")
    public String gmEvaluateState(Map<String, Object> cmdMap) {
        int type = Integer.parseInt(cmdMap.get("type").toString());
        boolean newState = Boolean.parseBoolean(cmdMap.get("state").toString());
        boolean state;
        if (type == 1 || type == 2 || type == 4 || type == 5) {//点赞
            state = ServerParamUtil.serverEvaluateData.getOrDefault(type, true);
            ServerParamUtil.serverEvaluateData.put(type, newState);
            ServerParamUtil.saveEvaluateData();
            if (state != newState) {
                for (Player player : Manager.playerManager.getOnLines()) {
                    Manager.platformevaluateManager.deal().updateEvaluate(player, type, newState);
                }
            }
            return result(SUCCESS, "设置评价(" + type + ")开关【" + newState + "】完成", "");
        } else {
            return result(FAILURE, "评价类型不存在", "");
        }
    }

    private int setLevel(Player player, int level) {
        int oldLevel = player.getLevel();
        long oldExp = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.EXP);
        int maxlevel = Global.PlayerMaxLevel;
        int nowLevel = level >= maxlevel ? maxlevel : level;
        if (nowLevel == maxlevel) {
            player.getCurrencys().set(ItemCoinType.EXP, 0);
        }

        player.setLevel(nowLevel);
        Manager.playerManager.manager().playerLevelUp(player, oldLevel, oldExp, nowLevel, oldExp, 0, ItemChangeReason.GMGet, IDConfigUtil.getLogId());
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.BASE);
        return nowLevel;
    }

    private int setStateVip(Player player, int level) {
        Manager.stateVipManager.deal().gmStateVipUp(player, level);
        return player.getStateVip().getLv();
    }

    private int changeJob(Player player, int level_changeJob) {
        int nowLv = player.getXsGrade();
        Cfg_Changejob_Bean bean = CfgManager.getCfg_Changejob_Container().getValueByKey(level_changeJob);
        if (bean == null) {
            return nowLv;
        }
        if (player.getXsGrade() == level_changeJob) {
            return nowLv;
        }

        player.setXsGrade(level_changeJob);

        Manager.newFashionManager.deal().addFashionID(player, bean.getModel_change());
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Task);

        PlayerMessage.ResGenderClassChange.Builder builder = PlayerMessage.ResGenderClassChange.newBuilder();
        builder.setGenderClass(player.getXsGrade());
        MessageUtils.send_to_player(player, PlayerMessage.ResGenderClassChange.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        return nowLv;
    }

    private int setMainTask(Player player, int taskId) {
        player.getCurMainTasks().clear();
        player.getOverMainTaskIDs().clear();
        Manager.taskManager.deal().acceptTask(player, Task.MAIN_TASK, taskId, 0, false);

        if (player.getCurMainTasks().size() < 1) {
            log.error(player.nameIdString() + "， 没有后续任务 了！");
            return 0;
        }
        MainTask mainTask = player.getCurMainTasks().get(0);
        if (taskId != mainTask.getModelId()) {
            return 0;
        }
        mainTask.finishTask(player, true);
        return taskId;
    }

    @GmHelp(gmDesc = "更新运营活动标签库")
    public String gmUpdateTagInfo(Map<String, Object> cmdMap) {
        int tagCount = Manager.activityManager.deal().loadTagInfo();
        if (tagCount > 0) {
            String tagData = JsonUtils.toJSONString(Manager.activityManager.getTagInfoList());
            for (Player player : Manager.playerManager.getOnLines()) {
                Manager.activityManager.deal().sendActivityTagInfo(player, tagData);
            }
        }
        return result(true, "加载完成", tagCount);
    }

    @GmHelp(gmDesc = "测试活动")
    public String testActivity(Map<String, Object> cmdMap) {
        int id = Integer.parseInt(cmdMap.get("params").toString());
        int result = Manager.activityManager.getConfigDao().delete(DbSqlName.ACTIVITYCONFIG_DELETE.getName(), id);
        return String.valueOf(result);
    }

    @GmHelp(gmDesc = "重新加载运营活动")
    public String reloadActivity(Map<String, Object> cmdMap) {
        Manager.activityManager.deal().load();
        return "";
    }

    @GmHelp(gmDesc = "处理运营活动")
    public String activityDeal(Map<String, Object> cmdMap) {
        String[] params = cmdMap.get("params").toString().split("&");
        int opType = Integer.parseInt(params[0]);
        int actId = 0;
        int actType = 0;
        switch (opType) {
            case 0://查询服务器内存中的活动数据
                actType = Integer.parseInt(params[1]);
                int isPre = Integer.parseInt(params[2]);
                List<Integer> list = new ArrayList<>();
                if (isPre == 1) {
                    if(actType == 0){//所有活动
                        for (Map<Integer, ActivityConfigBean> acfMap:Manager.activityManager.getPreCfgMap().values()) {
                            for (Integer id:acfMap.keySet()) {
                                list.add(id);
                            }
                        }
                    }else{
                        Map<Integer, ActivityConfigBean> preMap = Manager.activityManager.getPreCfgMap().get(actType);
                        if (preMap != null) {
                            for (Integer id : preMap.keySet()) {
                                list.add(id);
                            }
                        }
                    }
                    return result(SUCCESS, "查询运营活动完成", list);
                }else{
                    if(actType == 0){//所有活动
                        for (ActivityConfig acfg : Manager.activityManager.getActCfgMap().values()) {
                            list.add(acfg.getId());
                        }
                    }else{
                        ActivityConfig acfg = Manager.activityManager.getActCfgMap().get(actType);
                        if (acfg != null) {
                            list.add(acfg.getId());
                        }
                    }
                }
                return result(SUCCESS, "查询运营活动完成", list);
            case 1://强制删除正在运行的某个类型运营活动
                //活动类型 例如26000 10001
                actType = Integer.parseInt(params[1]);

                //删除内存正在运行活动配置、活动、活动角色的相关数据
                Manager.activityManager.deal().batchDelActivity(actType);
                return result(SUCCESS, "强制删除正在运行的某类型运营活动成功, actType="+actType, opType);
            case 2://强制删除预备活动
                actType = Integer.parseInt(params[1]);
                if (params.length < 3) {
                    Manager.activityManager.getPreCfgMap().remove(actType);
                    return result(SUCCESS, "强制删除某类型的预备活动成功, actType="+actType, opType);
                }
                actId = Integer.parseInt(params[2]);
                Map<Integer, ActivityConfigBean> preMap = Manager.activityManager.getPreCfgMap().get(actType);
                if (preMap != null) {
                    preMap.remove(actId);
                    return result(SUCCESS, "强制删除某个的预备活动成功, actType="+actType+",actId="+actId, opType);
                }
                return result(SUCCESS, "强制删除某个的预备活动失败。原因：预备队列中未找到该活动。actType="+actType+",actId="+actId, opType);
            case 3://重新加载某个运营活动
                actId = Integer.parseInt(params[1]);
                ActivityConfigBean configBean = Manager.activityManager.getConfigDao().selectById(actId);
                if (configBean == null) {
                    return result(FAILURE, "重新加载运营活动失败，数据库的活动配置为空,actId-" + actId, "");
                }
                loadActConfig(configBean);
                return result(SUCCESS, "重新加载运营活动完成", opType);
            case 4://重新加载所有运营活动
                Manager.activityManager.getActCfgMap().clear();
                List<ActivityConfigBean> configList = Manager.activityManager.getConfigDao().selectAll();
                if (configList == null) {
                    return result(FAILURE, "重新加载所有运营活动失败，数据库的活动配置为空", "");
                }

                for (ActivityConfigBean bean : configList) {
                    loadActConfig(bean);
                }
                return result(SUCCESS, "重新加载所有运营活动完成", opType);
            default:
                return result(SUCCESS, "处理运营活动完成", opType);
        }
    }

    private boolean loadActConfig(ActivityConfigBean actBean) {
        try {
            int actType = actBean.getType();
            if (actBean == null) {
                return false;
            }

            if (actType <= 0) {
                return false;
            }

            if (actBean.getIsDelete() == 1) {
                return false;
            }

            long nowTime = TimeUtils.Time();
            //活动已过期，删除活动
            if (nowTime >= actBean.getEndTime()) {
                Manager.activityManager.deal().delActConfig(actBean.getId());
                return false;
            }

            if (actBean.getState() == 0) {//预发布
                addPreMap(actBean);
            } else if (actBean.getState() == 1) {//进行活动
                //如果已经加载了同类型活动则排除不在活动时间内的。
                if (Manager.activityManager.getActCfgMap().containsKey(actType)) {
                    if (!isActiviting(actBean, nowTime)) {
                        addPreMap(actBean);
                        actBean.setState((byte) 0);
                        Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
                        return false;
                    }
                }

                IActivityScript as = Manager.activityManager.deal().getScript(actType);
                if (as == null) {
                    return false;
                }

                //注册新活动
                ActivityConfig actCfg = new ActivityConfig();

                //设置活动基础信息
                actCfg.beanToActivityBaseConfig(actBean);

                //检查解析自定义配置字段是否正确
                if (!(as.parseCustomConfig(actCfg, actBean.getCustom()))) {
                    log.error("自定义运营活动数据解析错误，actId=" + actCfg.getId());
                    return false;
                }

                Manager.activityManager.getActCfgMap().put(actType, actCfg);
            }
            return true;
        } catch (Exception e) {
            log.error(actBean, e);
        }
        return false;
    }

    private void addPreMap(ActivityConfigBean actBean) {
        Map<Integer, ActivityConfigBean> preMap = Manager.activityManager.getPreCfgMap().get(actBean.getType());
        if (preMap == null) {
            preMap = new HashMap<>();
            Manager.activityManager.getPreCfgMap().put(actBean.getType(), preMap);
        }
        preMap.put(actBean.getId(), actBean);
    }

    private boolean isActiviting(ActivityConfigBean actBean, long nowTime) {
        //增加新服活动条件 开服7天内只有新服活动才会生效
        if (TimeUtils.getOpenServerDay() <= 7) {
            if (actBean.getIsOpenServer() != 1) {
                return false;
            }
        }
        return nowTime >= actBean.getBeginTime() && nowTime <= actBean.getEndTime();
    }

    @Override
    public void P2GNoticeSynData(BackendMessage.P2GNoticeSynData messInfo) {
        String data = messInfo.getData();
//        ForbidDao dao;
//        WhiteDao whitedao;
        switch (messInfo.getType()) {//0：封禁 1：解封 2：添加白名单 3：删除白名单
            case 0:
                String[] datas = data.split("&&");
                ForbidBean forbidBean = new ForbidBean();
                forbidBean.setStr(datas[0]);
                forbidBean.setTime(Integer.parseInt(datas[1]));
                Manager.registerManager.getForbids().put(datas[0], forbidBean);
//                dao = new ForbidDao();
//                dao.delete(data);
                break;
            case 1:
                Manager.registerManager.getForbids().remove(data);
//                dao = new ForbidDao();
//                dao.delete(data);
                break;
            case 2:
                Manager.registerManager.getWhites().add(data);
//                whitedao = new WhiteDao();
//                whitedao.insert(data);
                break;
            case 3:
                Manager.registerManager.getWhites().remove(data);
//                whitedao = new WhiteDao();
//                whitedao.delete(whiteStr);
                break;
            default:
                break;
        }

    }

    @GmHelp(gmDesc = "发送多个服务器全服邮件")
    public String gmSendAllMail(Map<String, Object> cmdMap) {
        String minLv = cmdMap.get("minLv").toString();
        String maxLv = cmdMap.get("maxLv").toString();
        String career = cmdMap.get("career").toString();
        String title = cmdMap.get("title").toString();
        String content = cmdMap.get("content").toString();
        String serverIdStr = cmdMap.get("serverIdList").toString();
        String[] serverIdArr = serverIdStr.split(",");
        ReadIntegerArrayEs items = new ReadIntegerArrayEs(cmdMap.get("items").toString(), "}", ",");
        List<Item> itemList = Item.createItems(items);
        List<String> serverIdList = new ArrayList<String>();
        Collections.addAll(serverIdList, serverIdArr);
        for (Map.Entry<Long, PlayerWorldInfo> entry : Manager.playerManager.getAllPlayerWorldInfo().entrySet()) {
            PlayerWorldInfo pwi = entry.getValue();
            boolean isLevel = (pwi.getLevel() >= Integer.parseInt(minLv)) && (pwi.getLevel() <= Integer.parseInt(maxLv));
            boolean iscareer = pwi.getCareer() == Byte.parseByte(career) || (Integer.parseInt(career) == 9);
            if ((serverIdList.contains(String.valueOf(pwi.getCsid()))) && isLevel && iscareer) {
                MailManager.getInstance().sendMailToPlayer(entry.getValue().getRoleid(), 2, MessageString.System, title, content, itemList, ItemChangeReason.GM);
            }
        }
        return result(true, "发送成功", "");
    }

    @GmHelp(gmDesc = "通知游戏服开服")
    public String gmOpenServer(Map<String, Object> cmdMap) {
        boolean forceOpen = cmdMap.get("force").toString().equals("1");
        //服务器满足开服条件但未到开服时间，强制设置开服时间
        String now = TimeUtils.NowToString();

        if (forceOpen) {
            ServerConfig.setOpenTime(now);
            Manager.biManager.getScript().BiServer_op(103);
            Manager.biManager.getScript().BiServer_op(1);
            ServerParamUtil.serverOpenTime = now;
            ServerParamUtil.saveServerOpenTime();

            Manager.fallingSkyManager.deal().onOpenTimeChange();//设置开服时间后天禁令 重置到第一轮

            CrossServerMessage.G2PServerOpentimeChange.Builder msg = CrossServerMessage.G2PServerOpentimeChange.newBuilder();
            msg.setServerId(ServerConfig.getServerId());
            msg.addAllServerIdsList(ServerConfig.getServerIdList());
            msg.setServeropentime(now);
            msg.setPlat(ServerConfig.getServerPlatform());
            MessageUtils.send_to_public(CrossServerMessage.G2PServerOpentimeChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            log.error("通知游戏服进行开服处理完成");
            return result(SUCCESS, "服务器自动开服设置成功：", now);
        }

        //服务器已经开启
        if (TimeUtils.Time() >= TimeUtils.getOpenServerTime()) {
            Manager.biManager.getScript().BiServer_op(1);
            log.error("通知游戏服开服时，游戏服已经处于开放状态");
            return result(SUCCESS, "服务器已经是开服状态：", ServerConfig.getServerOpenTime());
        }

        return result(FAILURE, "", now);
    }

    @GmHelp(gmDesc = "修复充值", parms = {@ParmDec(pramFiled = "params", pramDesc = "脚本ID")})
    public String fixrecharge(Map<String, Object> cmdMap) {
        String[] configNameList = cmdMap.get("params").toString().split("&");
        int rechargeid = Integer.parseInt(configNameList[0]);
        long roleID = Long.parseLong(configNameList[1]);
        String moneyType = configNameList[2];
        Player player = Manager.playerManager.getPlayer(roleID);
        if (player == null) {
            return result(FAILURE, "玩家不在线", "0");
        }

        RechargeItemInfo cfg = Manager.rechargeManager.getRechargeItemInfoMap().get(rechargeid);
        if (cfg == null) {
            return result(FAILURE, "失败商品ID不存在", rechargeid);
        }
        Recharge recharge = new Recharge();
        recharge.setOrder_no("GM_ID_" + TimeUtils.Time());
        recharge.setGoods_id(rechargeid);
        recharge.setGoods_type(cfg.getGoods_type() + "");
        recharge.setGoods_ext("");
        recharge.setTotal_fee(Manager.rechargeManager.deal().getMoney(player, cfg, moneyType));
        recharge.setGoods_code(rechargeid + "");
        recharge.setGoods_name(cfg.getGoods_name());
        recharge.setItem_id(0);
        recharge.setGame_money(0);
        recharge.setRole_id(player.getId());
        recharge.setExt_param("gm");
        recharge.setSign_type("1");
        recharge.setSign("tiancheng==");
        recharge.setMoney_type(moneyType);
        Manager.rechargeManager.AddRecharge(null, recharge, JsonUtils.toJSONString(recharge), RechargeDefine.SRC_GM);
        return result(SUCCESS, "成功", "");
    }
}
