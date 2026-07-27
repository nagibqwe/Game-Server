package com.gm.project.gmtool.hefu.tool.tables.game;

import com.game.backpack.structs.Item;
import com.game.mail.structs.MailData;
import com.game.util.IDConfigUtil;
import com.game.util.JsonUtils;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.BaseTableHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * @author gaozhaoguang
 * @desc role
 * @date Created on 2021/1/18 21:55
 **/
public class role extends BaseTableHandler {

    private HashMap<Integer,HashMap<Long,String[]>> needChangeRoles = new HashMap<>();
    private static final Logger cnLog = LoggerFactory.getLogger("RoleChangeName");

    @Override
    public int getPriority() {
        return 200;
    }

    @Override
    public void doBefore(Object args) throws SQLException {
        WriteLog("开始处理 role 表... ... ");
        needChangeRoles.clear();
        HashSet<String> toRoleNames = new HashSet<>();
        List<List<String>> rs = task.getToServer().getDb().query("SELECT rolename FROM role",1);
        for(List<String> r : rs){
            String roleName = r.get(0);
            if (roleName == null || roleName.isEmpty()) {
                continue;
            }
            toRoleNames.add(roleName);
        }
        int cnt = 0;

        for(HefuServer server : task.getFromServer()){
            List<List<String>> rsFrom = server.getDb().query("SELECT roleid,rolename FROM role",2);
            HashMap<Long,String[]> tmp = new HashMap<>();
            needChangeRoles.put(server.getServerId(),tmp);
            for(List<String> r : rsFrom){
                String roleName = r.get(1);
                String roleId = r.get(0);
                if (roleName == null || roleName.isEmpty()) {
                    continue;
                }
                if(toRoleNames.contains(roleName)){
                    String newRoleName = "S"+server.getServerId()+"-"+roleName;
                    tmp.put(Long.parseLong(roleId),new String[]{roleName,newRoleName});
                    toRoleNames.add(newRoleName);
                    cnt++;
                }else{
                    toRoleNames.add(roleName);
                }
            }
        }

        task.setRoles(needChangeRoles);
        WriteLog("role 发现需要改名的角色数量:" + cnt);
        cnLog.info("role 发现需要改名的角色数量:" + cnt);
    }

    @Override
    public void doProcess(Object args) throws SQLException {
        for(HefuServer server : task.getFromServer()){
            processOne(server,needChangeRoles.get(server.getServerId()));
        }
        WriteLog("role 表处理完毕！");
    }

    private void processOne(HefuServer server,HashMap<Long,String[]> map)  throws SQLException{
        PreparedStatement update = server.getDb().createPreStat("update role set rolename = ? where roleid= ?");
        PreparedStatement psMail = server.getDb().createPreStat("insert into mail(mailId,type,receiveTime,sender,receiverId,isRead,hasAttachment,isAttachReceived,mailData) values(?,?,?,?,?,?,?,?,?)");
        Iterator<Map.Entry<Long, String[]>> iter = map.entrySet().iterator();
        int count = 0;
        while(iter.hasNext()){
            Map.Entry<Long,String[]> entry = (iter.next());
            update.setLong(2,entry.getKey());
            update.setString(1,entry.getValue()[1]);
            update.addBatch();
            sendEmail(psMail,entry.getKey());
            WriteLog("角色["+entry.getKey()+"]名字由: "+entry.getValue()[0]+" 改变为:"+entry.getValue()[1]);
            count++;
            if(count % 7200==0){
                update.executeBatch();
                psMail.executeBatch();
                server.getDb().commit();
                update.clearBatch();
                psMail.clearBatch();
            }
        }
        update.executeBatch();
        psMail.executeBatch();
        server.getDb().commit();
        update.clearBatch();
        psMail.clearBatch();
        update.close();
        psMail.close();
    }

    /**
     * 发送改名卡的邮件
     * @param psMail
     * @param roleID
     * @throws SQLException
     */
    private void sendEmail(PreparedStatement psMail,Long roleID) throws SQLException {

        Item item = Item.createItem(1006); //角色改名卡modelId=1006
        List<Item> attachList = new ArrayList<>();
        attachList.add(item);

        MailData data = new MailData();
        data.setType(1);
        data.setMailId(IDConfigUtil.getId());
        data.setReceiveTime(System.currentTimeMillis());
        data.setSender(task.getLangInfo().getSender());
        data.setMailTitle(task.getLangInfo().getRoleTitle());
        data.setMailContend(task.getLangInfo().getRoleContent());
        data.setReceiverId(roleID);
        data.setIsRead((byte)0);
        data.setHasAttachment((byte)1);
        data.setIsAttachReceived((byte)0);
        data.setItemList(attachList);
        data.setIsRead((byte)0);

        psMail.setLong(1, data.getMailId());
        psMail.setInt(2, data.getType());
        psMail.setLong(3, data.getReceiveTime());
        psMail.setString(4, data.getSender());
        psMail.setLong(5, data.getReceiverId());
        psMail.setByte(6, data.getIsRead());
        psMail.setByte(7, data.getHasAttachment());
        psMail.setByte(8, data.getIsAttachReceived());
        psMail.setString(9, JsonUtils.toJSONString(data));
        psMail.addBatch();
        cnLog.info("发放角色改名邮件 mailId=" + data.getMailId());
    }
}
