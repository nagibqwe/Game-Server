package common.register;

import com.data.FunctionStart;
import com.data.Global;
import com.data.MessageString;
import com.game.bi.biqq.QQLogType;
import com.game.chat.Manager.ChatManager;
import com.game.db.bean.ForbidBean;
import com.game.db.bean.RoleLoginInfoBean;
import com.game.db.bean.roleBean;
import com.game.db.dao.ForbidDao;
import com.game.db.dao.WhiteDao;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.guild.structs.GuildSysConfig;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.marriage.struct.Marriage;
import com.game.player.structs.*;
import com.game.ranklist.handler.DeleteRoleForRankListHandler;
import com.game.register.log.RoleCreateLog;
import com.game.register.log.RoleDoLog;
import com.game.register.log.RoleLoginLog;
import com.game.register.manager.RegisterManager;
import com.game.register.script.IRegisterScript;
import com.game.register.structs.UserInfo;
import com.game.register.structs.UserRoleInfo;
import com.game.register.structs.UserState;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.thread.SaveServer;
import com.game.structs.EntityState;
import com.game.structs.GlobalType;
import com.game.thread.FriendAndRankListProcessor;
import com.game.utils.ServerParamUtil;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.json.TypeReference;
import game.core.map.Position;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.*;
import game.message.BIMessage;
import game.message.CommonMessage;
import game.message.RegisterMessage;
import game.message.RegisterMessage.RoleBaseInfo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录处理接口
 *
 * @author admin
 */
public class RegisterScript implements IScript, IRegisterScript {

    private static final Logger log = LogManager.getLogger("com.game.Register.manager.RegisterManager");
    //角色创建失败原因
    private final int createRole_SUCCESS = 0;//创建角色成功！
    private final int createRoleFailed_MaxNum = 1;//角色数已达到最大了
    private final int createRoleFailed_errorlength = 2;//名字太长
    private final int createRoleFailed_forbidden = 3;//名字还有非法字符
    private final int createRoleFailed_duplicationname = 4;//重名
    private final int createRoleFailed_errorparm = 5;//错误参数
    private final int createRoleFailed_Shielding_symbol = 6;//屏蔽标点符号
    private final int createRoleFailed_ServerMaxNum = 7;//注册人数已达上限

    private final int DeleteLevel = 110;//直接删除，不能恢复的角色转职阶位
    private final int DeleteTime = 48 * 60 * 60;//超过48小时直接删除，不能恢复的角色
    private final int MaxRoleNum = 4;//一个账号下最大角色数

    //sign错误
    private final int loginfailed_signerror = -1;

    //超时
    private final int loginfailed_timeout = -2;

    //重复登录
    private final int loginfailed_userIdReplay = -3;

    //区号不在本服
    private final int loginfailed_ServerNotExist = -4;

    //人数已经达到上限了
    private final int loginfailed_RoleNumMax = -5;

    //开服时间未到
    private final int loginfailed_OpenServerTime = -6;

    @Override
    public int getId() {
        return ScriptEnum.RegisterBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void OnReqCreateCharacter(ChannelHandlerContext context, BIMessage.Device device, String name, int career) {

        if (ServerParamUtil.registerNumLimit != 0 && Manager.playerManager.getAllPlayerWorldInfo().size() >= ServerParamUtil.registerNumLimit) {
            sendCreatetRoleMessage(context, createRoleFailed_ServerMaxNum);
            GameServer.getInstance().setErrorLog("register role has reached limit", "注册角色数已达上限：" + ServerParamUtil.registerNumLimit);
            return;
        }

        if (context.channel().attr(SessionAttribute.USER_STATE).get() == null) {
            String str = String.format(" %s", context);
            log.error("创建角色失败 context无角色信息 Nowcontext is");
            SessionUtils.closeSession(context, str);
            return;
        }
        int userState = context.channel().attr(SessionAttribute.USER_STATE).get();
        if (userState < UserState.LOGININGSUCCESS.getValue()) {
            String str = String.format("创建角色失败,没有登录成功的账号不能选择角色 Nowcontext is %s", context);
            log.error(str);
            SessionUtils.closeSession(context, str);
            return;
        }
        context.channel().attr(SessionAttribute.USER_STATE).set(UserState.CREATEROLE.getValue());
        int serverId = context.channel().attr(SessionAttribute.SERVER_ID).get();// context.getAttribute(SessionAttribute.SERVER_ID.getValue());

        int languageType = context.channel().attr(SessionAttribute.LANGUAGE_TYPE).get();
        int sex = GlobalType.getSexByCareer(career);

        //检查参数
        if (!isCanCreateRole(context, name, career, sex, serverId)) {
            return;
        }
        UserInfo userInfo = context.channel().attr(SessionAttribute.USER_INFO).get();
        String platformName = context.channel().attr(SessionAttribute.PLATFORMNAME).get();
        String loginIP = context.channel().attr(SessionAttribute.IP).get();
        String funcelUUid = context.channel().attr(SessionAttribute.FUNCELLUUID).get();
        String code = context.channel().attr(SessionAttribute.MACHINECODE).get();
        String puserId = context.channel().attr(SessionAttribute.PLATUSERID).get();
        String pOs = context.channel().attr(SessionAttribute.CLIENTOS).get();

        boolean isTrue = createRoleSuccess(device, userInfo, platformName, serverId, loginIP, (byte) sex, (byte) career, name, languageType, context, funcelUUid, code, puserId, pOs);
        sendCreatetRoleMessage(context, isTrue ? createRole_SUCCESS : createRoleFailed_forbidden);

    }

    //发送创建角色失败消息
    private void sendCreatetRoleMessage(ChannelHandlerContext context, int reason) {
        RegisterMessage.ResCreateRoleFailed.Builder b = RegisterMessage.ResCreateRoleFailed.newBuilder();
        b.setReason(reason);
        b.setTime((int) (TimeUtils.Time() / 1000L));
        context.writeAndFlush(new SMessage(RegisterMessage.ResCreateRoleFailed.MsgID.eMsgID_VALUE, b.build().toByteArray()));
    }

    //成功创建角色
    private boolean createRoleSuccess(BIMessage.Device device, UserInfo userInfo, String platformName, int serverId, String ip, byte sex, byte carrer, String realName, int languageType, ChannelHandlerContext context, String funcelUUid, String code, String puserId, String os) {
        Player player = createPlayer(userInfo.getUserId(), platformName, serverId, ip, sex, carrer, realName, languageType, funcelUUid);
        Manager.registerManager.addRoleName(player.getId(), realName); //创建就加入新角色名，移到这里就add，下面makeRoleBean需要player.getName()
        roleBean role = Manager.playerManager.manager().makeRoleBeanByPlayer(player);
        role.setRolename(realName); //前面addName了这里可以不要这句
        if (Manager.registerManager.getDao().insert(role) < 1) {//插入数据失败了， 玩家不应该上线了
            Manager.registerManager.DelRoleName(player.getId());// hasUsedNameMap.remove(player.getId()); //insert失败则缓存中也移除
            return false;
        }
        Position position = new Position(Global.NewPlayerFirstCoordinate.get(0), Global.NewPlayerFirstCoordinate.get(1));

        player.setMaCode(code);
        player.setOs(os);
        player.setPlatUserId(puserId);
        player.changeMapModelId(Global.NewPlayerFirstMap);
        player.changeCurPos(position);
        //设置显示服务器ID编号
        player.setShowSid(ServerConfig.getShowServerId());
        Manager.playerManager.cachePlayer(player);

        RoleUpdateLogService.getInstance().createRoleSave(player);

        UserRoleInfo uRole = UserRoleInfo.initCreatePlayerToInfo(player);
        userInfo.getRoles().put(uRole.getRoleId(), uRole);

        context.channel().attr(SessionAttribute.PLAYER).set(player);
        context.channel().attr(SessionAttribute.ROLE_ID).set(player.getId());
        context.channel().attr(SessionAttribute.USER_STATE).set(UserState.ENTERGAMEING.getValue());
        log.info("创建角色成功 userId:" + userInfo.getUserId() + " roleId:" + player.getId() + " name:" + realName);

        Manager.registerManager.deal().registerSession(context);
        //加log
        writeCreateRoleLog(role, ip, player,device);
        //玩家创建成功BI
        Manager.biManager.getScript().biCreate(player, device, player.getCareer(), player.getSex(), ip);
        Manager.biManager.getScript().biRole_info(player);
        Manager.biManager.get4399Script().chatInfoTo4399(player, ChatManager.ROLE_NAME, "", realName, new ArrayList<>());
        Manager.biManager.getQQScript().log(player, QQLogType.roleCreate);

        enterGame(player, false);

        //todo 修改登录日志 记录创建角色选择服务器Id
        Manager.playerManager.manager().insertLoginData(player.getId(), player.getUserId(), player.getCreateServerId(), player.getName(), player.getLevel(), player.getCareer(), 0);

        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Certification)) {
            if (!userInfo.isCertify()) {
                Manager.mailManager.sendMailToPlayer(player.getId(), 1, MessageString.System, MessageString.Real_Name_Authentication_MailTitle, MessageString.Real_Name_Authentication_Mail);
            }
        }
        return true;
    }

    //创建角色
    private Player createPlayer(long userId, String platformName, int serverId, String ip, byte sex, byte career, String realName, int languageType, String funcelUUid) {
        long roleId = IDConfigUtil.getId();
        Player player = new Player();
        player.setId(roleId);
        player.setUserId(userId);
        player.setCareer(career);
        player.setLevel(1);
        player.setSex(sex);
        player.setCreateTime((int) (TimeUtils.Time() / 1000));
        player.setPlatformName(platformName);
        player.setUuid(funcelUUid);
        player.setName(realName);
        player.setLanguageType(languageType);
        player.setCreateServerID(serverId);
        player.setLoginIP(ip);
        Manager.goldManager.playerOnLine(player);

        player.setBagCellsNum(Global.Born_Bag_Num.get(0));
        player.setStoreCellsNum(Global.StoreCreateNum);

        //初始化玩家装备部位
        Manager.equipManager.initAllEquipParts(player);

        //这个放到最后
        Manager.playerAttAttributeManager.deal().initPlayerAttribute(player, true);

        player.setCurHp(player.getAttribute().MaxHP());
        return player;
    }

    private void writeCreateRoleLog(roleBean role, String ip, Player player,BIMessage.Device device) {
        try {
            RoleCreateLog cLog = new RoleCreateLog();
            cLog.setCreateRoleIP(ip);
            cLog.setPlatformName(role.getPlatformName());
            cLog.setRoleId(role.getRoleid());
            cLog.setRoleName(role.getRolename());
            cLog.setServerId(role.getServerId());
            cLog.setCareer(role.getCareer());
            cLog.setUserId(role.getUserId());
            cLog.setFuncellUUid(player.getUuid());
            cLog.setMachineCode(player.getMaCode());
            cLog.setPlatUserName(player.getPlatUserId());
            cLog.setOs(player.getOs());
            cLog.setClientVer(device.getAppVersion());
            LogService.getInstance().execute(cLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //判断是否能够创建角色
    private boolean isCanCreateRole(ChannelHandlerContext context, String name, int career, int sex, int serverId) {
        //检查参数
        if (name == null || StringUtils.isBlank(name) || (sex != PlayerDefine.SEX_MAN && sex != PlayerDefine.SEX_WOMAN)
                || context.channel().attr(SessionAttribute.USER_STATE).get() == null
                || context.channel().attr(SessionAttribute.PLATFORMNAME).get() == null || context.channel().attr(SessionAttribute.SERVER_ID).get() == null
                || context.channel().attr(SessionAttribute.IP).get() == null || context.channel().attr(SessionAttribute.USER_INFO).get() == null) {
            sendCreatetRoleMessage(context, createRoleFailed_errorparm);
            log.error("创建角色错误，参数错误 name:" + name + " career:" + career + " sex:" + sex + ";Nowcontext is " + context.channel());
            return false;
        }
        UserInfo userInfo = context.channel().attr(SessionAttribute.USER_INFO).get();
        int num = 0;
        int now = (int) (TimeUtils.Time() / 1000);
        for (UserRoleInfo info : userInfo.getRoles().values()) {
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(info.getRoleId());
            if (pwi == null) {
                log.error("没有找到简要信息， 是否有错误发生！");
                Player player = Manager.playerManager.getPlayerCache(info.getRoleId());
                if (player == null) {
                    continue;
                } else {
                    Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
                    pwi = Manager.playerManager.getPlayerWorldInfo(info.getRoleId());
                }

                if (pwi == null) {
                    continue;
                }
            }
            if (pwi.getCsid() != serverId) {
                continue;
            }
            if (info.getDeleteTime() != 0 && pwi.getLevel() < DeleteLevel) {
                continue;
            }
            if (info.getDeleteTime() != 0 && now - info.getDeleteTime() > DeleteTime) {
                continue;
            }
            num++;
        }
        //检查已创建角色个数
        if (num >= MaxRoleNum) {
            sendCreatetRoleMessage(context, createRoleFailed_MaxNum);
            return false;
        }
        //检查名字长度(字符长度)
        int namelength = length(name);
        if (namelength < Global.PlayerNameLimit.get(1) || namelength > Global.PlayerNameLimit.get(0)) {
            sendCreatetRoleMessage(context, createRoleFailed_errorlength);
            log.error("创建角色错误，角色名长度不对 createRoleInfo:" + name + "namelength: " + namelength + "_Nowcontext is " + context.channel());
            return false;
        }
        //屏蔽标点符号检查
        if (Utils.isContainsShielding_symbol(name)) {
            sendCreatetRoleMessage(context, createRoleFailed_Shielding_symbol);
            log.error("创建角色错误，角色名包含屏蔽标点符号 createRoleInfo:" + name + ";Nowcontext is " + context.channel());
            return false;
        }
        //屏蔽字检查
        if (Utils.isForbiddenStr(name)) {
            sendCreatetRoleMessage(context, createRoleFailed_forbidden);
            log.error("创建角色错误，角色名包含屏蔽字 createRoleInfo:" + name + ";Nowcontext is " + context.channel());
            return false;
        }

        if (name.contains("?")) {
            sendCreatetRoleMessage(context, createRoleFailed_forbidden);
            log.error("创建角色错误，角色名包含屏蔽字 createRoleInfo:" + name + ";Nowcontext is " + context.channel());
            return false;
        }

        //重名检查
        if (Manager.registerManager.isUsedName(name)) {
            sendCreatetRoleMessage(context, createRoleFailed_duplicationname);
            log.error("创建角色错误，角色名重复 createRoleInfo:" + name + ";Nowcontext is " + context.channel());
            return false;
        }

        return true;
    }

    /**
     * 得到一个字符串的长度,一个汉字或日韩文长度为2,英文字符长度为1
     */
    private int length(String s) {
        String newString;
        try {
            newString = new String(s.getBytes("gbk"), "ISO-8859-1");
            return newString.length();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.length();
    }

    @Override
    public void OnReqDeleteRole(ChannelHandlerContext context, RegisterMessage.ReqDeleteRole messInfo) {
        try {
            long deleteId = messInfo.getRoleId();
            if (context.channel().attr(SessionAttribute.USER_STATE).get() == null) {
                String str = String.format("删除角色失败 context无角色信息 roleId:%d ;Nowcontext is %s", deleteId, context.channel());
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }
            int userState = context.channel().attr(SessionAttribute.USER_STATE).get();
            if (userState < UserState.LOGININGSUCCESS.getValue()) {
                String str = String.format("删除角色失败,没有登录成功的账号不能删除 roelId:" + deleteId + ";Nowcontext is " + context.channel());
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }

            //检查该账号下是否有这个角色
            UserInfo userInfo = context.channel().attr(SessionAttribute.USER_INFO).get();
            if (userInfo == null) {
                log.error("角色选择错误 roleId:" + deleteId + ";Nowcontext is " + context.channel());
                return;
            }

            int now = (int) (TimeUtils.Time() / 1000);
            int isSuccess = 0;
            for (UserRoleInfo roleInfo : userInfo.getRoles().values()) {
                if (roleInfo.getDeleteTime() == 0 && roleInfo.getRoleId() == deleteId) {
                    Player player = Manager.playerManager.getPlayer(deleteId);
                    if (player == null) {
                        return;
                    }

                    //会长不能移除
                    if (player.isHaveGuild()) {
                        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
                        GuildMember guildMember = guild.getMembers().get(player.getId());
                        if (guildMember.getPosition() == GuildSysConfig.TYPE_MASTER || Manager.guildsManager.manager().isProxyChairMan(guildMember, guild)) {
//                            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Delete_role_prompt);
                            isSuccess = 1;
                            break;
                        } else {
                            Manager.guildsManager.manager().leaveGuild(player, 0);
                        }
                    }

                    if (player.getMarriageUid() != 0) {
                        Marriage marriage = Manager.marriageManager.getMarriageList().get(player.getMarriageUid());
                        if (marriage.getId() == Manager.marriageManager.getCurMarriageId()) {
                            isSuccess = 1;
                        }

                        if (isSuccess != 1) {
                            if (ServerParamUtil.weddingList.containsKey(marriage.getId())) {
                                isSuccess = 1;
                            }
                        }

                        if (isSuccess == 1) {
                            break;
                        }

                        Player bePlayer = Manager.marriageManager.manager().getMarraige(player, marriage);
                        Manager.marriageManager.manager().divorce(player, bePlayer, marriage, 1);
                    }

                    roleInfo.setDeleteTime(now);
                    player.setDeleteTime(now);
                    if (player.getTeamId() > 0) {
                        Manager.teamManager.OnQuitTeam(player);
                    }
                    Manager.chumManager.getScript().onReqExit(player);

                    writeDoRoleLog(roleInfo, userInfo.getUserId(), 0);
                    break;
                }
            }
            RegisterMessage.ResDeleteRoleSuccess.Builder msg = RegisterMessage.ResDeleteRoleSuccess.newBuilder();
            msg.setPlayerId(deleteId);
            msg.setRes(isSuccess);
            context.writeAndFlush(new SMessage(RegisterMessage.ResDeleteRoleSuccess.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
            if (isSuccess != 0) {
                return;
            }
            Manager.registerManager.getDao().deleteRole(deleteId);
            RoleUpdateLogService.getInstance().deleteRoleState(deleteId);
            Manager.playerManager.manager().removePlayer(deleteId);
            Manager.playerManager.manager().changeLoginDelete(deleteId, now);
            FriendAndRankListProcessor.getInstance().addCommand(new DeleteRoleForRankListHandler(deleteId));
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void OnReqLoginGame(ChannelHandlerContext context, RegisterMessage.ReqLoginGame messInfo) {
        Validate.notNull(context);
        Validate.notNull(messInfo);
        try {

            if (Manager.playerManager.getOnLinePlayerNum() > 1500) {
                sendLoginFailedMsg(context, loginfailed_RoleNumMax);
                GameServer.getInstance().setErrorLog("content len max than 1500", "客户端连接已经超过1500了， 需要玩家等待！");
                return;
            }

            log.info("角色开始登录 " + messInfo);
            if (context.channel().attr(SessionAttribute.USER_STATE).get() != null) {
                log.error("重复登录消息 Session is :" + context.channel());
                sendLoginFailedMsg(context, loginfailed_userIdReplay);
                return;
            }

            //设置登录状态
            context.channel().attr(SessionAttribute.USER_STATE).set(UserState.LOGINING.getValue());
            InetSocketAddress remoteAddress = (InetSocketAddress) context.channel().remoteAddress();
            if (remoteAddress == null) {
                sendLoginFailedMsg(context, loginfailed_userIdReplay);
                String str = String.format("getIP error. Session is :%s ;messInfo is %s", context.channel(), messInfo);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }

            String ip = remoteAddress.getAddress().getHostAddress();
            if (ip == null) {
                sendLoginFailedMsg(context, loginfailed_userIdReplay);
                String str = String.format("getIP error. Session is :%s ;messInfo is %s", context.channel(), messInfo);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }

            //判断客户端上传的服务器id是否是正确的id
            if (!ServerConfig.isRightServerId(messInfo.getServerId())) {
                sendLoginFailedMsg(context, loginfailed_ServerNotExist);
                String str = String.format("serverId error. Session is :%s ;messInfo is %s", context.channel(), messInfo);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }

            //判断开服时间是否正常,白名单可以进入
            if (TimeUtils.getOpenServerTime() > TimeUtils.Time() && !messInfo.getIsWhite()) {
                sendLoginFailedMsg(context, loginfailed_OpenServerTime);
                String str = String.format("openserver time error:%s. Session is :%s ;messInfo is %s", ServerConfig.getServerOpenTime(), context.channel(), messInfo);
                log.error(str);
                return;
            }

            //设置账号状态为登录中
            long userId = messInfo.getUserId();

            //判断sign是否正确
            if (!LoginVerifySignCal.calSign(userId, messInfo.getAccessToken(), messInfo.getMachineCode(), messInfo.getTime(), messInfo.getPlatformName())
                    .equals(messInfo.getSign())) {
                sendLoginFailedMsg(context, loginfailed_signerror);
                String str = String.format("sign error. Session is :%s ;messInfo is %s", context.channel(), messInfo);
                SessionUtils.closeSession(context, str);
                return;
            }

            //从sdk获取用户的年龄
            int userAge = -1;

            if (!ServerConfig.isTestServer() || !isPCLogin(messInfo.getPlatformName())) {
                if (!messInfo.getIsChangeRole()) {
                    int[] loginRet = loginTokenCheck(messInfo);
                    userAge = loginRet[1];
                    if (loginRet[0] != 1) {
                        sendLoginFailedMsg(context, (loginRet[0]));
                        String str = String.format("token check error. Session is :%s ;messInfo is %s", context.channel(), messInfo);
                        SessionUtils.closeSession(context, str);
                        return;
                    }
                }
            }
            messInfo.getExtensionList();
            UserInfo userInfo = Manager.registerManager.getUserInfo(userId);
            userInfo.setAge(userAge);
            userInfo.setCertify(messInfo.getIsCertify());
            HashMap<String,String> ext = new HashMap<>();
            for (CommonMessage.KeyValueInfo e : messInfo.getExtensionList()) {
                ext.put(e.getKey(),e.getValue());
            }
            userInfo.setExtension(ext);

            context.channel().attr(SessionAttribute.USER_INFO).set(userInfo);
            context.channel().attr(SessionAttribute.SERVER_ID).set(messInfo.getServerId());
            context.channel().attr(SessionAttribute.PLATFORMNAME).set(messInfo.getPlatformName());
            context.channel().attr(SessionAttribute.FUNCELLUUID).set(messInfo.getFuncelUUid());
            context.channel().attr(SessionAttribute.CLIENTOS).set(messInfo.getOs());
            context.channel().attr(SessionAttribute.MACHINECODE).set(messInfo.getMachineCode());
            context.channel().attr(SessionAttribute.PLATUSERID).set(messInfo.getPlatUserName());
            context.channel().attr(SessionAttribute.LANGUAGE_TYPE).set(messInfo.getLanguageType());
            context.channel().attr(SessionAttribute.IP).set(ip);

            //是否包含了
            String keyId = userId + "_" + messInfo.getServerId();
            ChannelHandlerContext oldS = Manager.registerManager.getUserEnterRoleID().get(keyId);
            if (oldS != null) {
                log.error("账号重复登录  userId={} oldSession={} newSession={}", userInfo, oldS.channel(), context.channel());
                replaceLogin(oldS,true);
            }
            //判断同一个区的是否有重复登录的
            for (UserRoleInfo roleInfo : userInfo.getRoles().values()) {
                if (roleInfo.getCsId() != messInfo.getServerId()) {
                    continue;
                }
                ChannelHandlerContext oldSession = Manager.registerManager.getSessionByRoleId(roleInfo.getRoleId());
                if (oldSession != null) {
                    replaceLogin(oldSession,true);
                    log.error("账号重复登录，已选择角色进入游戏 userId：" + userInfo.getUserId() + " 角色id：" + roleInfo.getRoleId() + "oldSession is :" + oldSession + ";Nowcontext is " + context);
                }
            }
            //同时只有一个连接
            context.channel().attr(SessionAttribute.USER_STATE).set(UserState.LOGININGSUCCESS.getValue());
            log.info("登录验证成功 userId:" + messInfo.getUserId() + " sessionId:" + context.channel());
            sendLoginSuccessMsg(context, userInfo, messInfo.getServerId(), messInfo.getPlatformName(), messInfo.getRoleId(), true);
        } catch (Exception e) {
            log.error(e, e);
            String str = e.toString();
            SessionUtils.closeSession(context, str);
        }
    }

    //重复登录处理
    @Override
    public void replaceLogin(ChannelHandlerContext oldS,boolean isSendMsg) {

        if(isSendMsg) {
            sendReplaceSession(oldS);
        }

        Manager.playerManager.iQuitGame().QuitGame(oldS, QuitGameDefine.RepeatLogin, false,isSendMsg);
    }

    //发送登录失败消息
    private void sendLoginFailedMsg(ChannelHandlerContext session, int reason) {
        RegisterMessage.ResLoginGameFailed.Builder b = RegisterMessage.ResLoginGameFailed.newBuilder();
        b.setCurrentTime((int) (TimeUtils.Time() / 1000));
        b.setOpenTime((int) (TimeUtils.getOpenServerTime() / 1000));
        b.setReason(reason);
        ChannelFuture cf = session.writeAndFlush(new SMessage(RegisterMessage.ResLoginGameFailed.MsgID.eMsgID_VALUE, b.build().toByteArray()));
        cf.awaitUninterruptibly(2, TimeUnit.SECONDS);
    }

    //发送登录成功消息
    public void sendLoginSuccessMsg(ChannelHandlerContext session, UserInfo userInfo, int loginServerId, String platformName, long roleId, boolean addRoleList) {
        if (userInfo == null) {
            return;
        }
        //角色列表下要发送vip等级
        Manager.goldManager.initGold(roleId, loginServerId, platformName);
        RegisterMessage.ResLoginGameSuccess.Builder b = RegisterMessage.ResLoginGameSuccess.newBuilder();
        if (addRoleList) {
            int now = (int) (TimeUtils.Time() / 1000);
            for (UserRoleInfo info : userInfo.getRoles().values()) {
                PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(info.getRoleId());
                if (pwi == null) {
                    log.error("没有找到简要信息， 是否有错误发生！");
                    Player player = Manager.playerManager.getPlayer(info.getRoleId());
                    if (player == null) {
                        log.error(info.getRoleId() + "角色不存在！");
                        continue;
                    } else {
                        log.error(player.nameIdString() + "进入重构！");
                        if (player.getGold() == null) {
                            Manager.goldManager.playerOnLine(player);
                        }
                        Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
                        pwi = Manager.playerManager.getPlayerWorldInfo(info.getRoleId());
                    }
                    if (pwi == null) {
                        continue;
                    }
                }
                if (pwi.getCsid() != loginServerId) {
                    continue;
                }
                if (info.getDeleteTime() != 0 && pwi.getLevel() < DeleteLevel) {
                    continue;
                }
                if (info.getDeleteTime() != 0 && now - info.getDeleteTime() > DeleteTime) {
                    continue;
                }

                b.addInfoList(getRoleBaseInfo(pwi, info.getDeleteTime()));
            }
        }
        b.setReallyServerId(GameServer.getInstance().getServerId());
        if (RegisterManager.iconUpdateUrl.length() < 2) {
            String url = ServerConfig.getErrorLogUrl();
            int i = url.lastIndexOf('/');
            if (i > 7) {
                url = url.substring(0, i);
            }
            RegisterManager.iconUpdateUrl = url;
        }

        b.setIconUpUrl(RegisterManager.iconUpdateUrl);
        b.setRoleId(roleId);
        b.setTimezone(Calendar.getInstance().getTimeZone().getRawOffset() / 1000);
        b.setAge(userInfo.getAge());
        session.writeAndFlush(new SMessage(RegisterMessage.ResLoginGameSuccess.MsgID.eMsgID_VALUE, b.build().toByteArray()));
    }

    private RoleBaseInfo getRoleBaseInfo(PlayerWorldInfo info, int deleteTime) {
        RoleBaseInfo.Builder msg = RoleBaseInfo.newBuilder();
        msg.setCareer(info.getCareer());
        msg.setDeleteTime(deleteTime);
        msg.setLv(info.getLevel());
        msg.setName(info.getRolename());
        msg.setRoleId(info.getRoleid());
        msg.setStateLv(info.getStateVip());
        msg.setCreateTime(info.getCreateTime());
        msg.setFight(info.getFightPower());
        msg.setFacade(MapUtils.getFacade(info));
        return msg.build();
    }

    @Override
    public void OnReqRegainRole(ChannelHandlerContext context, RegisterMessage.ReqRegainRole messInfo) {
        try {
            long regainId = messInfo.getRoleId();
            if (context.channel().attr(SessionAttribute.USER_STATE).get() == null) {
                String str = String.format("恢复角色失败 context无角色信息 roleId:%d ;Nowcontext is %s", regainId, context);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }
            int userState = context.channel().attr(SessionAttribute.USER_STATE).get();
            if (userState < UserState.LOGININGSUCCESS.getValue()) {
                String str = String.format("恢复角色失败,没有登录成功的账号不能删除 roleId:%d ;Nowcontext is %s", regainId, context);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }
            //检查该账号下是否有这个角色
            UserInfo userInfo = context.channel().attr(SessionAttribute.USER_INFO).get();
            if (userInfo == null) {
                log.error("角色选择错误 roleId:" + regainId + ";Nowcontext is " + context);
                return;
            }
            int now = (int) (TimeUtils.Time() / 1000);
            int result = -1;
            for (UserRoleInfo roleInfo : userInfo.getRoles().values()) {
                if (roleInfo.getRoleId() != regainId) {
                    continue;
                }
                PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(roleInfo.getRoleId());
                if (pwi == null) {
                    log.error("没有找到简要信息， 是否有错误发生！");
                    Player player = Manager.playerManager.getPlayer(roleInfo.getRoleId());
                    if (player == null) {
                        log.error(roleInfo.getRoleId() + "角色不存在！");
                        continue;
                    } else {
                        log.error(player.nameIdString() + "进入重构！");
                        if (player.getGold() == null) {
                            Manager.goldManager.playerOnLine(player);
                        }
                        Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
                        pwi = Manager.playerManager.getPlayerWorldInfo(roleInfo.getRoleId());
                    }

                    if (pwi == null) {
                        continue;
                    }
                }
                if (pwi.getLevel() >= DeleteLevel && roleInfo.getDeleteTime() != 0 && now - roleInfo.getDeleteTime() < DeleteTime) {
                    roleInfo.setDeleteTime(0);
                    result = 0;
                    Player player = Manager.playerManager.getPlayerCache(regainId);
                    if (player != null) {
                        player.setDeleteTime(0);
                    }
                    writeDoRoleLog(roleInfo, userInfo.getUserId(), 1);
                }
                break;
            }
            RegisterMessage.ResRegainRoleResult.Builder msg = RegisterMessage.ResRegainRoleResult.newBuilder();
            msg.setRoleId(regainId);
            msg.setResult(result);
            context.writeAndFlush(new SMessage(RegisterMessage.ResRegainRoleResult.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
            if (result != 0) {
                return;
            }
            Manager.registerManager.getDao().regainRole(regainId);
            RoleUpdateLogService.getInstance().updateRoleDate(regainId);
            Manager.playerManager.manager().changeLoginDelete(regainId, 0);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //type0表示删除角色，1表示恢复角色
    private void writeDoRoleLog(UserRoleInfo roleInfo, long userId, int type) {
        try {
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(roleInfo.getRoleId());
            if (pwi == null) {
                log.error("没有找到简要信息， 是否有错误发生！");
                Player player = Manager.playerManager.getPlayer(roleInfo.getRoleId());
                if (player == null) {
                    log.error(roleInfo.getRoleId() + "角色不存在！");
                    return;
                } else {
                    log.error(player.nameIdString() + "进入重构！");
                    if (player.getGold() == null) {
                        Manager.goldManager.playerOnLine(player);
                    }
                    Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
                    pwi = Manager.playerManager.getPlayerWorldInfo(roleInfo.getRoleId());
                }

                if (pwi == null) {
                    return;
                }
            }
            RoleDoLog dlog = new RoleDoLog();
            dlog.setLv(pwi.getLevel());
            dlog.setRoleId(roleInfo.getRoleId());
            dlog.setRoleName(pwi.getRolename());
            dlog.setServerId(roleInfo.getCsId());
            dlog.setCareer(pwi.getCareer());
            dlog.setUserId(userId);
            dlog.setType(type);
            LogService.getInstance().execute(dlog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void OnReqSelectCharacter(ChannelHandlerContext context, long roleId, boolean isReconnect) {
        try {
            if (context.channel().attr(SessionAttribute.USER_STATE).get() == null) {
                String str = String.format("选择角色失败 context无角色信息 roleId:%d ;Nowcontext is %s", roleId, context);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }
            int userState = context.channel().attr(SessionAttribute.USER_STATE).get();
            if (userState < UserState.LOGININGSUCCESS.getValue()) {
                String str = String.format("选择角色失败 没有登录成功的账号不能删除 roleId:%d ;Nowcontext is %s", roleId, context);
                log.error(str);
                SessionUtils.closeSession(context, str);
                return;
            }
            context.channel().attr(SessionAttribute.USER_STATE).set(UserState.SELECTING.getValue());
            log.info("选择角色进入游戏 roleId:" + roleId);
            //检查该账号下是否有这个角色
            UserInfo userInfo = context.channel().attr(SessionAttribute.USER_INFO).get();
            if (userInfo == null) {
                log.error("角色选择错误 roleId:" + roleId + ";Nowcontext is " + context);
                return;
            }
            UserRoleInfo uRole = userInfo.getRoles().get(roleId);
            if (uRole == null || uRole.getDeleteTime() != 0) {
                log.error("玩家所选择的角色不存在 roleId:" + roleId);
                return;
            }

            Player player = Manager.playerManager.getPlayer(roleId);
            if (player == null) {
                log.error("玩家 roleId:" + roleId + "查找不到数据了");
                return;
            }
            //注册账号
            if (TimeUtils.Time() / 1000L < player.getForbid()) {
                //角色封停中
                sendResSelectCharacterFailed(context, player.getForbid());
                Manager.playerManager.manager().removePlayer(player);
                log.info("选择角色封停中 roleId:" + roleId + " context=" + context);
                return;
            }
            log.info("选择角色成功 roleId:" + roleId + " context=" + context.channel());

            context.channel().attr(SessionAttribute.PLAYER).set(player);
            context.channel().attr(SessionAttribute.ROLE_ID).set(player.getId());
            context.channel().attr(SessionAttribute.USER_STATE).set(UserState.ENTERGAMEING.getValue());

            Manager.registerManager.deal().registerSession(context);

            enterGame(player, isReconnect);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    void sendReplaceSession(ChannelHandlerContext old) {
        if (old == null) {
            return;
        }
        long roleId = old.channel().attr(SessionAttribute.ROLE_ID).get();
        String ip = old.channel().attr(SessionAttribute.IP).get();

        RegisterMessage.ResSubstitute.Builder message = RegisterMessage.ResSubstitute.newBuilder();
        message.setNewip(ip);
        ChannelFuture cf = old.writeAndFlush(new SMessage(RegisterMessage.ResSubstitute.MsgID.eMsgID_VALUE, message.build().toByteArray()));
        cf.awaitUninterruptibly(30, TimeUnit.SECONDS);

        log.error("账号重复登录, 发送顶号消息 roleId={} IP={}", roleId, ip);
    }

    /**
     * 踢出连接
     *
     * @param session
     */
    @Override
    public void tickSession(ChannelHandlerContext session) {
        if (session == null) {
            return;
        }
        if (session.channel().hasAttr(SessionAttribute.USER_INFO) && session.channel().hasAttr(SessionAttribute.SERVER_ID)) {
            UserInfo user = session.channel().attr(SessionAttribute.USER_INFO).get();
            int serverId = session.channel().attr(SessionAttribute.SERVER_ID).get();
            String keyId = user.getUserId() + "_" + serverId;
            Manager.registerManager.getUserEnterRoleID().remove(keyId);
            log.warn("移除连接 user={} session={}", user, session.channel());
        }
        if (session.channel().hasAttr(SessionAttribute.USER_INFO) && session.channel().hasAttr(SessionAttribute.ROLE_ID)) {
            UserInfo user = session.channel().attr(SessionAttribute.USER_INFO).get();
            long roleId = session.channel().attr(SessionAttribute.ROLE_ID).get();
            UserRoleInfo role = user.getRoles().get(roleId);
            if (role != null) {
                role.setOnLine(false);
            }
            Manager.registerManager.getRole_sessions().remove(roleId);
            log.warn("移除连接 role={} session={}", role, session.channel());
        }
        if (session.channel().hasAttr(SessionAttribute.ROLE_ID)) {
            long roleId = session.channel().attr(SessionAttribute.ROLE_ID).get();
            Player player = Manager.playerManager.getPlayer(roleId);
            player.setIosession(null);
            player.dealOffLine();

            session.channel().attr(SessionAttribute.PLAYER).set(null);
            Manager.registerManager.getRole_sessions().remove(roleId);
            log.warn("移除连接 role={} session={}", roleId, session.channel());
        }
        SessionUtils.closeSession(session, "tickSession 关闭连接退出游戏！");

    }

    /**
     * 注册连接
     *
     * @param session
     */
    @Override
    public void registerSession(ChannelHandlerContext session) {
        if (session == null) {
            return;
        }
        if (session.channel().hasAttr(SessionAttribute.USER_INFO) && session.channel().hasAttr(SessionAttribute.SERVER_ID)) {
            UserInfo user = session.channel().attr(SessionAttribute.USER_INFO).get();
            int serverId = session.channel().attr(SessionAttribute.SERVER_ID).get();
            String keyId = user.getUserId() + "_" + serverId;
            ChannelHandlerContext oldS = Manager.registerManager.getUserEnterRoleID().get(keyId);
            if (oldS != null && !oldS.equals(session)) {
                log.error("账号重复登录  userId={} oldSession={} newSession={}", user, oldS.channel(), session.channel());
                replaceLogin(oldS,true);
            }
            Manager.registerManager.getUserEnterRoleID().put(keyId, session);
            log.info("注册连接 user={} session={}", user, session.channel());
        }
        if (session.channel().hasAttr(SessionAttribute.USER_INFO) && session.channel().hasAttr(SessionAttribute.ROLE_ID)) {
            UserInfo user = session.channel().attr(SessionAttribute.USER_INFO).get();
            long roleId = session.channel().attr(SessionAttribute.ROLE_ID).get();
            UserRoleInfo role = user.getRoles().get(roleId);
            if (role != null) {
                role.setOnLine(true);
            }
        }
        if (session.channel().hasAttr(SessionAttribute.ROLE_ID)) {
            String ip = session.channel().attr(SessionAttribute.IP).get();
            long roleId = session.channel().attr(SessionAttribute.ROLE_ID).get();
            int languageType = session.channel().attr(SessionAttribute.LANGUAGE_TYPE).get();

            Player player = Manager.playerManager.getPlayer(roleId);
            player.setIosession(session);
            player.setLoginIP(ip);
            player.setLanguageType(languageType);
            player.dealOnLine();

            session.channel().attr(SessionAttribute.PLAYER).set(player);

            Manager.registerManager.addLanguageType(player.getId(), languageType);
            Manager.registerManager.getRole_sessions().put(roleId, session);
            log.info("注册连接 player={} session={} sessionCount={} ", player, session.channel(), Manager.registerManager.getRole_sessions().size());
        }
    }

    /**
     * 选择角色进入游戏时 ， 此函数逻辑一定要简单，速度要快， 不能处理太多的其它信息
     *
     * @param player      玩家
     * @param isReConnect 是否是断线重连
     */
    private void enterGame(Player player, boolean isReConnect) {
        log.info("玩家【" + player.getName() + "(" + player.getId() + ")】" + " 选择角色进入游戏！");
        player.resetState();
        player.addState(EntityState.LoginGame);
        //角色进入游戏时的账号信息
        saveRoleLogin(player);

        Manager.rechargeManager.discountScript().online(player);

        //写log
        writeRoleLoginLog(player);

        //调用进入游戏脚本
        if (isReConnect) {
            Manager.playerManager.loadScript().reconnect(player);
            boolean canRetrieveRes = Manager.retrieveResManager.getScript().canRetrieveRes(player);
            Manager.biManager.getScript().biLogin(player, 2, canRetrieveRes ? 1 : 0);
        } else {
            Manager.playerManager.loadScript().EnterGameMap(player);
            boolean canRetrieveRes = Manager.retrieveResManager.getScript().canRetrieveRes(player);
            Manager.biManager.getScript().biLogin(player, 1, canRetrieveRes ? 1 : 0);
        }
        Manager.biManager.getQQScript().log(player, QQLogType.login);
    }

    @Override
    public void writeRoleLoginLog(Player player) {
        writeRoleLoginLog(player, player.getIsOnline(), player.getIsOnline() == 1);
    }

    @Override
    public void writeRoleLoginLog(Player player, int type, boolean isTrue) {
        try {
            RoleLoginLog roleLoginLog = new RoleLoginLog();
            roleLoginLog.setPlayer(player);
            roleLoginLog.setSex(player.getCareer());
            roleLoginLog.setLevel(player.getLevel());
            roleLoginLog.setType(type);
            roleLoginLog.setLoginRoleIP(player.getLoginIP());
            roleLoginLog.setFuncellUUid(player.getUuid());
            roleLoginLog.setMachineCode(player.getMaCode());
            roleLoginLog.setPlatUserName(player.getPlatUserId());
            roleLoginLog.setOs(player.getOs());
            if (isTrue) {
                roleLoginLog.setOnlineTime(0);
            } else {
                roleLoginLog.setOnlineTime(player.getAccunonlinetime());
            }

            roleLoginLog.setCreateTime(player.getCreateTime());

            LogService.getInstance().execute(roleLoginLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //角色登陆的时候记录单纯的账号及设备信息
    void saveRoleLogin(Player player) {
        RoleLoginInfoBean bean = makeRoleLoginInfoBean(player);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ROLELOGININFO_INSERT, SaveServer.MERGE);
    }

    private RoleLoginInfoBean makeRoleLoginInfoBean(Player player) {
        RoleLoginInfoBean info = new RoleLoginInfoBean();
        info.setCreateTime(TimeUtils.Time());
        info.setMaCode(player.getMaCode());
        info.setOs(player.getOs());
        info.setPlatUserId(player.getPlatUserId());
        info.setPlatformName(player.getPlatformName());
        info.setUserId(player.getUserId());
        info.setUuid(player.getUuid());
        info.setLastLoginTime(player.getLastLoginTime());
        return info;
    }

    private void sendResSelectCharacterFailed(ChannelHandlerContext context, int forbidTime) {
        RegisterMessage.ResSelectCharacterFailed.Builder msg = RegisterMessage.ResSelectCharacterFailed.newBuilder();
        msg.setForbidTime(forbidTime);
        context.writeAndFlush(new SMessage(RegisterMessage.ResSelectCharacterFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
    }

    public boolean isPCLogin(String platformName) {
        if (platformName == null) {
            return false;
        }
        return platformName.equals("PC");
    }

    //后面可能修改为多线程处理
    private int[] loginTokenCheck(RegisterMessage.ReqLoginGame messInfo) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("user_id=").append(messInfo.getFuncelUUid()).append("&");
        stringBuffer.append("token=").append(messInfo.getAccessToken()).append("&");
        /*
        stringBuffer.append("svr_id=").append(messInfo.getServerId()).append("&");
        stringBuffer.append("sign=").append(LoginVerifySignCal.calSign(
                messInfo.getAccessToken(),
                messInfo.getFuncelUUid(),
                messInfo.getServerId(),
                ServerConfig.getPrivateKey()));
         */
        stringBuffer.append("sign=").append(LoginVerifySignCal.calSignV2(
                messInfo.getAccessToken(),
                messInfo.getFuncelUUid(),
                ServerConfig.getPrivateKey()));
        String result = "";
        PrintWriter out;
        BufferedReader in;
        URLConnection connection;
        try {
            /*URL realUrl = new URL(ServerConfig.getLoginVerifyUrl());
            connection = realUrl.openConnection();
            connection.setReadTimeout(2000);
            connection.setConnectTimeout(2000);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            out = new PrintWriter(connection.getOutputStream());
            out.print(stringBuffer.toString());
            out.flush();
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            HashMap<String, Object> resultMap = JsonUtils.parseObject(result, new TypeReference<HashMap<String, Object>>() {
            });
            int state = (int) resultMap.get("state");*/
            int state = 1;
            int age = 18;
            /*if (state == 1) {
                HashMap<String, Object> inner = (HashMap<String, Object>) resultMap.get("data");
                if (inner.containsKey("age")) {
                    age = (int) inner.get("age");
                } else {
                    log.error("没有在返回的数据中找到age的节点.tokenCheck ret:" + result);
                    age = 18;
                }
                //log.error("登陆用户的年龄:"+age);
            }*/
            return new int[]{state, age};
        } catch (Exception e) {
            log.error("tokenCheck ret:" + result);
            log.error(messInfo + "token check exception:" + e);
            return new int[]{loginfailed_timeout, -1};
        }
    }

    @Override
    public void loadForbidAndWhite() {
        long start = TimeUtils.Time();
        log.info("开始加载封号和白名单列表");
        ForbidDao dao = new ForbidDao();
        List<ForbidBean> list = dao.selectAll();
        for (ForbidBean bean : list) {
            Manager.registerManager.getForbids().put(bean.getStr(), bean);
        }
        WhiteDao whitedao = new WhiteDao();
        Manager.registerManager.getWhites().addAll(whitedao.selectAll());
        long end = TimeUtils.Time();
        log.info("加载名字列表结束用时" + (end - start));
    }


    /**
     * 开服登录异常检测
     */
    @Override
    public void loginCheckException() {
         if (ServerParamUtil.isLoginCheck  <= 0){
             long curTime =  TimeUtils.Time();
             long loginCheckTime =  ServerConfig.getCheckLoginTime() * 60 * 1000;
             if (curTime >=  TimeUtils.getOpenServerTime() + loginCheckTime ){
                 if (Manager.playerManager.getPlayersCache().size() <=0){
                     //此处报警
                     log.error(BeanUtil.customThrowException("LoginCheckException"));
                 }
                 ServerParamUtil.saveLoginCheck();
             }
         }
    }

    private boolean isForbiden(String str) {
        if (!Manager.registerManager.getForbids().containsKey(str)) {
            return false;
        }
        ForbidBean bean = Manager.registerManager.getForbids().get(str);
        if (bean.getTime() == -1) {
            return true;
        }
        return (int) (TimeUtils.Time() / 1000) < bean.getTime();
    }

    private boolean isWhite(String str) {
        for (String whiteStr : Manager.registerManager.getWhites()) {
            if (whiteStr.equals(str)) {
                return true;
            }
        }
        return false;
    }
}
