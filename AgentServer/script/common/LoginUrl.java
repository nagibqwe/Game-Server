package common;

import com.game.biqq.IBiQQScript;
import com.game.db.bean.*;
import com.game.db.dao.RoleLoginDao;
import com.game.db.dao.ServerNameDao;
import com.game.db.dao.UserloginDao;
import com.game.db.dao.WhiteDao;
import com.game.ipfind.manager.IPFinderManager;
import com.game.login.LoginDataBean;
import com.game.login.LoginVerify;
import com.game.login.UserCacheManager;
import com.game.login.script.ILoginScript;
import com.game.utils.MessageUtil;
import game.core.dblog.LogService;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.IDConfigUtil;
import game.core.util.LoginVerifySignCal;
import game.core.util.TimeUtils;
import game.message.LoginMessage;
import game.message.LoginMessage.ReqLogin;
import game.message.LoginMessage.roleInfo;
import game.message.LoginMessage.serverNumInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class LoginUrl implements IScript, ILoginScript {

    private static final Logger logger = LogManager.getLogger(LoginUrl.class);

    //登录验证失败原因码九  零一 起玩www.90 17 5.c  om
    private static final int verifyFailed = 1;                      //验证失败
    private static final int verifyFailed_Forbided = 2;             //验证失败，账号被屏蔽了
    private static final int verifyFailed_ForbidedMacCode = 3;      //验证失败，机器码被屏蔽了
    private static final int verifyFailed_ForbidedMac = 4;          //验证失败，mac地址屏蔽
    private static final int verifyFailed_ForbidedIMEI = 5;         //验证失败，IMEI地址屏蔽
    private static final int verifyFailed_ForbidedIP = 6;           //验证失败，ip地址屏蔽


    //验证成功发送成功消息
    private static void verifysuccess(ChannelHandlerContext iosession, LoginMessage.ReqLogin messInfo, LoginDataBean user, String platformID, boolean isWight,int region) {
        long time = TimeUtils.Time();
        String sign = LoginVerifySignCal.calSign(user.getUserId(), messInfo.getAccessToken(), messInfo.getMachineCode(), time, platformID);
        //CodedUtil.Md5(userId + private_key + messInfo.getAccessToken() + messInfo.getMachineCode() + time );
        LoginMessage.ResLoginSuccess.Builder b = LoginMessage.ResLoginSuccess.newBuilder();
        for (Integer i : user.getSIdList()) {
            b.addCreatRoleServerIDList(i);
        }
        b.setUserId(user.getUserId());
        b.setTime(time);
        b.setLastEnterSeverId(user.getLastEnterServerId());
        b.setSign(sign);
        b.setHttpPort(ServerConfig.getLoginHtttpPort());
        b.setIsWhite(isWight);
        b.setLastEnterRoleId(user.getLastEnterRoleId());
        b.setRegion(region);

        //账号已有角色查询
        HashMap<Integer, List<RoleLoginBean>> allRole = getAllLoginRoleByUserId(user.getUserId());
        for (Map.Entry<Integer, List<RoleLoginBean>> value : allRole.entrySet()) {
            serverNumInfo.Builder info = serverNumInfo.newBuilder();
            info.setServerId(value.getKey());
            info.setNum(0);
            for (RoleLoginBean role : value.getValue()) {
                roleInfo.Builder roleMsg = roleInfo.newBuilder();
                roleMsg.setRoleId(role.getRoleId());
                roleMsg.setName(role.getRoleName());
                roleMsg.setCareer(role.getCareer());
                roleMsg.setLv(role.getLv());
                roleMsg.setFight(role.getFight());
                info.addRoles(roleMsg);
            }
            b.addServerInfo(info);
        }

        ServerNameDao nameDao = new ServerNameDao();
        for (ServerNameBean bean : nameDao.selectAll()) {
            LoginMessage.serverChangeName.Builder changeName = LoginMessage.serverChangeName.newBuilder();
            changeName.setServerId(bean.getServerId());
            changeName.setChangeName(bean.getChangeName());
            b.addServerChangeName(changeName);
        }
        MessageUtil.SendMess(iosession, new SMessage(LoginMessage.ResLoginSuccess.MsgID.eMsgID_VALUE, b.build().toByteArray()));
    }

    /**
     * 根据玩家账号ID获取该账号下所有角色
     *
     * @param userId 账号ID
     * @return
     */
    private static HashMap<Integer, List<RoleLoginBean>> getAllLoginRoleByUserId(long userId) {
        int DeleteLevel = 110;
        int DeleteTime = 48 * 60 * 60;
        int now = (int) TimeUtils.TimeSec();
        //key : 服务器id   value ：该服务器下的所有角色
        HashMap<Integer, List<RoleLoginBean>> allRoleMap = new HashMap<>();
        RoleLoginDao dao = new RoleLoginDao();
        List<RoleLoginBean> allRoleList = dao.selectByUserId(userId);
        if (allRoleList != null && !allRoleList.isEmpty()) {
            for (RoleLoginBean role : allRoleList) {
                if (role.getDeleteTime() != 0 && role.getLv() < DeleteLevel) {
                    continue;
                }
                if (role.getDeleteTime() != 0 && now - role.getDeleteTime() > DeleteTime) {
                    continue;
                }
                List<RoleLoginBean> list = allRoleMap.get(role.getServerId());
                if (list == null) {
                    list = new ArrayList<>();
                    allRoleMap.put(role.getServerId(), list);
                }
                list.add(role);
            }
        }
        return allRoleMap;
    }

    //验证失败发送失败消息
    private static void verifyfailed(ChannelHandlerContext iosession, int failedResaon) {
        LoginMessage.ResLoginFailed.Builder b = LoginMessage.ResLoginFailed.newBuilder();
        b.setReason(failedResaon);
        MessageUtil.SendMess(iosession, new SMessage(LoginMessage.ResLoginFailed.MsgID.eMsgID_VALUE, b.build().toByteArray()));
    }

    @Override
    public int getId() {
        return 1;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void check(ChannelHandlerContext iosession, ReqLogin messInfo) {
        try {
            long beginTime = System.currentTimeMillis();

            //账号被屏蔽了
            if (LoginVerify.getInstance().isForbiden(messInfo.getPlatformUid())) {
                verifyfailed(iosession, verifyFailed_Forbided);
                logger.error("账号:" + messInfo.getPlatformUid() + "被屏蔽了");
                return;
            }

            //机器码
            if (messInfo.getMachineCode().isEmpty()) {
                verifyfailed(iosession, verifyFailed_ForbidedMacCode);
                logger.error("账号:" + messInfo.getPlatformUid() + " 机器唯一码为空");
                return;
            }

            //机器码
            if (!messInfo.getMachineCode().isEmpty() && LoginVerify.getInstance().isForbiden(messInfo.getMachineCode())) {
                verifyfailed(iosession, verifyFailed_ForbidedMacCode);
                logger.error("账号:" + messInfo.getPlatformUid() + " 机器唯一码被屏蔽了；machineCode:" + messInfo.getMachineCode());
                return;
            }

            //mac
            if (LoginVerify.getInstance().isForbiden(messInfo.getMac())) {
                verifyfailed(iosession, verifyFailed_ForbidedMac);
                logger.error("账号:" + messInfo.getPlatformUid() + " 机器mac被屏蔽了；machineCode:" + messInfo.getMac());
                return;
            }

            //IMEI
            if (LoginVerify.getInstance().isForbiden(messInfo.getImei())) {
                verifyfailed(iosession, verifyFailed_ForbidedIMEI);
                logger.error("账号:" + messInfo.getPlatformUid() + " 机器imei被屏蔽了；imei:" + messInfo.getImei());
                return;
            }

            logger.info("第三方渠道的设备ID:" + messInfo.getCpdid());
            if (LoginVerify.getInstance().isForbiden(messInfo.getCpdid())) {
                verifyfailed(iosession, verifyFailed_ForbidedIMEI);
                logger.error("账号:" + messInfo.getPlatformUid() + " 机器deviceId被屏蔽了；deviceId:" + messInfo.getCpdid());
                return;
            }



            String ip = "";
            InetSocketAddress remoteAddress = (InetSocketAddress) iosession.channel().remoteAddress();
            if (remoteAddress != null) {
                ip = remoteAddress.getAddress().getHostAddress();
                if (LoginVerify.getInstance().isForbiden(ip)) {
                    verifyfailed(iosession, verifyFailed_ForbidedIP);
                    logger.error("账号:" + messInfo.getPlatformUid() + " ip被屏蔽了；ip:" + ip);
                    return;
                }
            }
            int region = IPFinderManager.getInstance().deal().getRegion(ip);
            boolean isWhite = LoginVerify.getInstance().isWhite(messInfo.getPlatformUid());

            String platform = messInfo.getPlatformName();
            String userName = messInfo.getPlatformUid();
            String machineCode = messInfo.getMachineCode();
            String mac = messInfo.getMac();
            String imei = messInfo.getImei();

            LoginDataBean loginData = UserCacheManager.getInstance().get(userName);
            if (loginData == null) {
                logger.info("脚本开始验证 funcell账号:" + messInfo.getPlatformUid() + " 缓存中未读取到userId，从数据库读取游戏userId。。。");
                UserloginDao userDao = new UserloginDao();
                UserloginBean user = userDao.selectByUserName(userName);
                if (user == null) {
                    user = new UserloginBean();
                    user.setUserId(IDConfigUtil.getId());
                    user.setCreateTime(System.currentTimeMillis() / 1000);
                    user.setData("[]");
                    user.setIsDelete(0);
                    user.setLastEnterServerId(0);
                    user.setPlatformAccount(messInfo.getPlatformAccount());
                    user.setPlatformName(platform);
                    user.setUserName(userName);
                    user.setLastLoginIp(ip);
                    if (userDao.insert(user) != 1) {
                        verifyfailed(iosession, verifyFailed);
                        logger.error("账号:" + messInfo.getPlatformUid() + " 登录失败，写入数据库失败，检查数据库连接");
                        return;
                    }
                    logger.info("脚本开始验证账号:" + messInfo.getPlatformUid() + " 新生成userId" + user.getUserId());
                    //注册日志
                    addQQRegisterLog(user.getUserId(), ip);
                }
                user.setLastLoginIp(ip);
                loginData = user.toLoginDataBean();
                UserCacheManager.getInstance().put(userName, loginData);
            }
            loginData.setLastLoginIp(ip);
            verifysuccess(iosession, messInfo, loginData, platform, isWhite,region);
            logger.info("funcell账号:" + messInfo.getPlatformUid() + " userId：" + loginData.getUserId() + " 登录验证成功");

            writeLog(loginData, ip, machineCode, mac, imei);

            if (!LoginVerify.getInstance().getPlatforms().contains(platform)) {
                LoginVerify.getInstance().getPlatforms().add(platform);
                WhiteDao whitedao = new WhiteDao();
                whitedao.insertPlatform(platform);
            }
            long endTime = System.currentTimeMillis();
            if (endTime - beginTime > 300) {
                logger.error("登录验证时间过长 账号:" + messInfo.getPlatformUid() + " 验证时间：" + (endTime - beginTime));
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /**
     * 添加QQ大厅注册日志
     * @param userId
     * @param ip
     */
    private void addQQRegisterLog(long userId, String ip) {
        try{
            IScript is = ScriptManager.getInstance().GetScriptClass(3);
            if (is instanceof IBiQQScript) {
                IBiQQScript script = (IBiQQScript) is;
                script.logRegister(userId, ip);
            } else {
                logger.info("IBiQQScript not find！");
            }
        }catch (Exception e){
            logger.error("addQQRegisterLog error", e);
        }
    }

    private void writeLog(LoginDataBean user, String ip, String machineCode, String mac, String imei) {
        LogUser logUser = new LogUser();
        logUser.setCreateTime(user.getCreateTime());
        logUser.setPlatformAccount(user.getPlatformAccount());
        logUser.setPlatformName(user.getPlatformName());
        logUser.setUserId(user.getUserId());
        logUser.setUserName(user.getUserName());
        logUser.setLastLoginIp(ip);
        logUser.setImei(imei);
        logUser.setMac(mac);
        logUser.setMachineCode(machineCode);
        LogService.getInstance().execute(logUser);
    }

}
