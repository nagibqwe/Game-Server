package common.boss;

import com.game.boss.script.IBossScript;
import com.game.boss.struct.BossTypeConst;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import game.message.BossMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BossScript implements IBossScript {

    private static final Logger log = LogManager.getLogger(BossScript.class);

    @Override
    public int getId() {
        return ScriptEnum.BossBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void updateBossCount(Player player, int type, int num) {
        switch (type){
            case BossTypeConst.WORLD_BOSS:
            case BossTypeConst.SUIT_BOSS:
                player.getBossCountMap().put(type,num);
                //重置次数方便机器人测试
                if(num <= 0){
                    player.chatGM("&clearcount");
                }
                break;
        }
    }

    @Override
    public void onResOpenDreamBoss(Player player, BossMessage.ResOpenDreamBoss messInfo) {
        List<BossMessage.BossMapOlInfo> list= messInfo.getMapOlListList();
        for (BossMessage.BossMapOlInfo bmInfo:list) {
            Manager.bossManager.getNumMap().put(bmInfo.getMapModelId(), bmInfo.getNum());
        }

        updateBossCount(player, messInfo.getBossType(), messInfo.getRemainCount());
    }
    public void onResSuitGemBossPanel(Player player, BossMessage.ResSuitGemBossPanel messInfo){
        List<BossMessage.BossMapOlInfo> list= messInfo.getMapOlListList();
        for (BossMessage.BossMapOlInfo bmInfo:list) {
            Manager.bossManager.getNumMap().put(bmInfo.getMapModelId(), bmInfo.getNum());
        }
        updateBossCount(player,BossTypeConst.SUIT_BOSS, messInfo.getRemainCount());
    }

    @Override
    public void sendReqOpenDreamBoss(Player player, int type) {
        BossMessage.ReqOpenDreamBoss.Builder msg = BossMessage.ReqOpenDreamBoss.newBuilder();
        msg.setBossType(type);
        player.sendMsg(BossMessage.ReqOpenDreamBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        log.info(player.getInfo()+"请求BOSS活动数据，类型="+type);
    }

    public  void sendReqSuitGemBossPanel(Player player, int type){
        BossMessage.ReqSuitGemBossPanel.Builder  msg = BossMessage.ReqSuitGemBossPanel.newBuilder();
        msg.setType(type);
        player.sendMsg(BossMessage.ReqSuitGemBossPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        log.info(player.getInfo()+"请求精甲和玉 BOSS数据，类型="+type);
    }

}
