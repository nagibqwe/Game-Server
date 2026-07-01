package com.game.redpacket.manager;

import com.game.db.bean.redpacketBean;
import com.game.db.dao.GuildDao;
import com.game.db.dao.redpacketDao;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.redpacket.script.IRedPacketScript;
import com.game.redpacket.structs.RedPacket;
import com.game.redpacket.structs.RedPacketEnum;
import com.game.redpacket.structs.RedPacketLog;
import com.game.redpacket.timer.RedPacketTimer;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.GameServer;
import com.game.server.thread.SaveServer;
import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 红包数据管理中心
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class RedPacketManager {

    private static final Logger log = LogManager.getLogger(RedPacketManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        RedPacketManager manager;

        Singleton() {
            this.manager = new RedPacketManager();
        }

        RedPacketManager getProcessor() {
            return manager;
        }
    }

    public static RedPacketManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //总的数据列表
    private final ConcurrentHashMap<Long, RedPacket> redPacketList = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, RedPacket> getRedPacketList() {
        return redPacketList;
    }

    //世界红包
    private final ConcurrentHashMap<Integer, HashSet<Long>> groupRedpacket = new ConcurrentHashMap<>();
    //个人充值红包
    private final ConcurrentHashMap<Long, HashSet<Long>> roleRedpacket = new ConcurrentHashMap<>();
    //公会红包
    private final ConcurrentHashMap<Long, HashSet<Long>> guildRedpacket = new ConcurrentHashMap<>();

    //公会红包日志
    private final ConcurrentHashMap<Long, List<RedPacketLog>> guildRPlog = new ConcurrentHashMap<>();

    private static final GuildDao guildDao = new GuildDao();

    public static GuildDao getGuildDao() {
        return guildDao;
    }

    public ConcurrentHashMap<Integer, HashSet<Long>> getGroupRedpacket() {
        return groupRedpacket;
    }

    public ConcurrentHashMap<Long, HashSet<Long>> getRoleRedpacket() {
        return roleRedpacket;
    }

    public ConcurrentHashMap<Long, HashSet<Long>> getGuildRedpacket() {
        return guildRedpacket;
    }

    public ConcurrentHashMap<Long, List<RedPacketLog>> getGuildRPlog() {
        return guildRPlog;
    }

    public String guildRPLog(long guildId) {
        if (guildRPlog.containsKey(guildId)) {
            return JsonUtils.toJSONString(guildRPlog.get(guildId));
        }
        return "[]";
    }

    public void setGuildRPLog(long guildId, String guildRedPacket) {
        if (StringUtils.isEmpty(guildRedPacket)) {
            return;
        }

        List<RedPacketLog> rpl = JsonUtils.parseObject(guildRedPacket, new TypeReference<ArrayList<RedPacketLog>>(){});
        if (rpl == null) {
            return;
        }

        guildRPlog.put(guildId, rpl);
    }

    private final redpacketDao dao = new redpacketDao();

    //重启服务器的时候要重新加载数据
    public void initload() {
        redPacketList.clear();
        groupRedpacket.clear();
        guildRedpacket.clear();
        roleRedpacket.clear();

        List<redpacketBean> plist = dao.selectAll();
        if (plist == null || plist.isEmpty()) {
            log.info("加载红包的数据" + redPacketList.size() + "条!");
            return;
        }

        for (redpacketBean bean : plist) {
            try {
                RedPacket rpd = JsonUtils.parseObject(bean.getRedpacket(), RedPacket.class);
                if (rpd == null) {
                    continue;
                }
                redPacketList.put(bean.getRpId(), rpd);
                if (rpd.getGuildId() > 0) {
                    long guildId = rpd.getGuildId();
                    HashSet<Long> rplist = null;
                    if (guildRedpacket.containsKey(guildId)) {
                        rplist = guildRedpacket.get(guildId);
                    }

                    if (rplist == null) {
                        rplist = new HashSet<>();
                        guildRedpacket.put(guildId, rplist);
                    }

                    rplist.add(rpd.getId());
                } else {
                    long roleId = rpd.getRoleId();
                    HashSet<Long> rplist = null;
                    if (roleRedpacket.containsKey(roleId)) {
                        rplist = roleRedpacket.get(roleId);
                    }
                    if (rplist == null) {
                        rplist = new HashSet<>();
                        roleRedpacket.put(roleId, rplist);
                    }
                    rplist.add(rpd.getId());
                }

            } catch (Exception e) {
                log.error("加载数据" + bean.getRpId() + "时，解析出错了", e);
            }
        }
        log.info("加载红包的数据" + redPacketList.size() + "条!");

        GameServer.getInstance().getAssistThread().addTimerEvent(new RedPacketTimer());
    }

    /**
     * 保存红包的数据存库
     *
     * @param rpd    实例
     * @param insert 是否是插入
     */
    public void saveData(RedPacket rpd, boolean insert) {
        redpacketBean bean = new redpacketBean();
        bean.setRpId(rpd.getId());
        bean.setRedpacket(JsonUtils.toJSONString(rpd));
        bean.setRpCreateTime(rpd.getCreateTime());
        bean.setRpType(rpd.getType());
        if (insert) {
            Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.REDPACKET_UPDATE, SaveServer.MERGE);
        } else {
            Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.REDPACKET_INSERT, SaveServer.INSERT);
        }
    }

    public void dataDelete(RedPacket rpd) {
        //删除系列
        if (roleRedpacket.containsKey(rpd.getRoleId())) {
            roleRedpacket.get(rpd.getRoleId()).remove(rpd.getId());
        }

        if (guildRedpacket.containsKey(rpd.getGuildId())) {
            guildRedpacket.get(rpd.getGuildId()).remove(rpd.getId());
        }

        redpacketBean bean = new redpacketBean();
        bean.setRpId(rpd.getId());
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.REDPACKET_DELETE, SaveServer.DELETE);
    }

    public IRedPacketScript getScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RedPacketBaseScript);
        if (is instanceof IRedPacketScript) {
            return (IRedPacketScript) is;
        }else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    /**
     * 发红包
     * @param player
     * @param redPacketEnum
     */
    public void createRedpacket(Player player, RedPacketEnum redPacketEnum) {
        IRedPacketScript script = getScript();
        if(script != null){
            script.createRedpacket(player, redPacketEnum);
        }
    }

    /**
     * 添加红包
     * @param redPacket
     */
    public void addRedpacket(RedPacket redPacket){
        if(redPacket != null){
            this.redPacketList.put(redPacket.getId(), redPacket);
        }
    }

    /**
     * 添加玩家红包
     * @param player
     * @param redPacket
     */
    public void addRoleRedpacket(Player player, RedPacket redPacket){
        HashSet<Long> bs = Manager.redPacketManager.getRoleRedpacket().get(player.getId());
        if(bs == null){
            bs = new HashSet<>();
            Manager.redPacketManager.getRoleRedpacket().put(player.getId(), bs);
        }
        bs.add(redPacket.getId());
    }

    /**
     * 添加红包日志
     * @param player
     * @param redPacket
     */
    public void addRedpacketLog(Player player, RedPacket redPacket){
        RedPacketLog log = new RedPacketLog();
        log.setReason(redPacket.getReason());
        log.setSendtime(redPacket.getCreateTime());
        log.setRoleId(redPacket.getRoleId());
        log.setValue(redPacket.getTotalMoney());
        log.setItemType(redPacket.getItemType());

        Guild guild = Manager.guildsManager.getGuildById(player.getGuildId());
        if(guild != null){
            //添加到日志
            List<RedPacketLog> logs = Manager.redPacketManager.getGuildRPlog().get(player.getGuildId());
            if(logs == null){
                logs = new ArrayList<>();
                Manager.redPacketManager.getGuildRPlog().put(player.getGuildId(), logs);
            }
            logs.add(log);
        }
    }

    /**
     * 添加工会红包
     * @param guildId
     * @param redPacket
     */
    public void addGuildRedpacket(Long guildId, RedPacket redPacket){
        HashSet<Long> bs = Manager.redPacketManager.getGuildRedpacket().get(guildId);
        if(bs == null){
            bs = new HashSet<>();
            Manager.redPacketManager.getGuildRedpacket().put(guildId, bs);
        }
        bs.add(redPacket.getId());
    }

    /**
     * 清理过期红包
     */
    public void clearExpireRedpacket() {
        try{
            getScript().clearExpireRedpacket();
        }catch (Exception e){
            log.error("clearExpireRedpacket error", e);
        }
    }

    /**
     * 玩家加入公会
     * @param player
     */
    public void joinGuild(Player player) {
        try{
            getScript().joinGuild(player);
        }catch (Exception e){
            log.error("redPacket joinGuild", e);
        }
    }
}
