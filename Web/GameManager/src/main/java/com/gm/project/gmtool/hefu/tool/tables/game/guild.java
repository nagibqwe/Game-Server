package com.gm.project.gmtool.hefu.tool.tables.game;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.game.backpack.structs.Item;
import com.game.mail.structs.MailData;
import com.game.util.IDConfigUtil;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author gaozhaoguang
 * @desc 帮会信息,如果有帮会名字冲突,那么就吧From的工会名字给改掉
 * @date Created on 2021/1/18 22:00
 **/
public class guild extends BaseTableHandler {

    private static final Logger cnLog = LoggerFactory.getLogger("GuildChangeName");
    //需要改变的帮会
    private HashMap<Integer,HashMap<Long,String[]>> needChangeGuilds = new HashMap<>();

    @Override
    public int getPriority() {
        return 100;
    }
    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 guild 表... ... ");
        needChangeGuilds.clear();
        HashSet<String> toNames = new HashSet<>();
        List<List<String>> rs = task.getToServer().getDb().query("SELECT guildName FROM guild", 1);
        for(List<String> r : rs){
            String guildName = r.get(0);
            if (guildName == null || guildName.isEmpty()) {
                continue;
            }
            toNames.add(guildName);
        }
        int cnt = 0;
        for(HefuServer server : task.getFromServer()){
            List<List<String>> fromRs = server.getDb().query("SELECT guildId,guildName,chairmanId FROM guild", 3);
            HashMap<Long,String[]> abc = new HashMap<>();
            needChangeGuilds.put(server.getServerId(), abc);
            for(List<String> r : fromRs){
                String guildName = r.get(1);
                if (guildName == null || guildName.isEmpty()) {
                    continue;
                }
                if(toNames.contains(guildName)){
                    String newGuildName = "S"+server.getServerId()+"-"+guildName;
                    abc.put(Long.parseLong(r.get(0)),new String[]{guildName,newGuildName,r.get(2)});
                    toNames.add(newGuildName);
                    cnt++;
                }else{
                    toNames.add(guildName);
                }
            }
        }
        WriteLog("guild 发现需要改名的帮派数量:" + cnt);
        cnLog.info("guild 发现需要改名的帮派数量:" + cnt);
    }

    @Override
    public void doProcess(Object args) throws SQLException {
       for(Integer sid:needChangeGuilds.keySet()){
           processOne(sid,needChangeGuilds.get(sid));
       }
       WriteLog("guild 表处理完毕！");
    }

    private void processOne(Integer sid,HashMap<Long,String[]> map) throws SQLException {
        for(HefuServer server : task.getFromServer()){
            if(server.getServerId() == sid){
                PreparedStatement update = server.getDb().createPreStat("update guild set guildName = ? where guildId= ?");
                PreparedStatement psMail = server.getDb().createPreStat("insert into mail(mailId,type,receiveTime,sender,receiverId,isRead,hasAttachment,isAttachReceived,mailData) values(?,?,?,?,?,?,?,?,?)");
                Iterator<Map.Entry<Long, String[]>> iter = map.entrySet().iterator();
                while(iter.hasNext()){
                    Map.Entry<Long,String[]> entry = iter.next();
                    update.setLong(2,entry.getKey());
                    update.setString(1,entry.getValue()[1]);
                    update.addBatch();
                    cnLog.info("帮会["+entry.getKey()+"]名字由: "+entry.getValue()[0]+" 改变为:"+entry.getValue()[1]);
                    //给帮主发送改名卡
                    //sendEmail(psMail,Long.parseLong(entry.getValue()[2]));
                }
                update.executeBatch();
                psMail.executeBatch();
                server.getDb().commit();
                update.clearBatch();
                psMail.clearBatch();
                update.close();
                psMail.close();
            }
        }
    }

    /**
     * 发送改名卡的邮件
     * @param psMail
     * @param roleID
     * @throws SQLException
     */
    private void sendEmail(PreparedStatement psMail, Long roleID) throws SQLException {

        Item item = Item.createItem(1006); //帮会改名卡modelId=1006
        List<Item> attachList = new ArrayList<>();
        attachList.add(item);

        MailData data = new MailData();
        data.setType(1);
        data.setMailId(IDConfigUtil.getId());
        data.setReceiveTime(System.currentTimeMillis());
        data.setSender(task.getLangInfo().getSender());
        data.setMailTitle(task.getLangInfo().getGuildTitle());
        data.setMailContend(task.getLangInfo().getGuildContent());
        data.setReceiverId(roleID);
        data.setIsRead((byte)0);
        data.setHasAttachment((byte)1);
        data.setIsAttachReceived((byte)0);
        data.setItemList(attachList);

        psMail.setLong(1, data.getMailId());
        psMail.setInt(2, data.getType());
        psMail.setLong(3, data.getReceiveTime());
        psMail.setString(4, data.getSender());
        psMail.setLong(5, data.getReceiverId());
        psMail.setByte(6, data.getIsRead());
        psMail.setByte(7, data.getHasAttachment());
        psMail.setByte(8, data.getIsAttachReceived());
        psMail.setString(9, JSON.toJSONString(data, SerializerFeature.WriteClassName));
        psMail.addBatch();
    }
}