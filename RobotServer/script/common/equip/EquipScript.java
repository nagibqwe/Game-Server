package common.equip;

import com.data.CfgManager;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Task_Bean;
import com.data.struct.ReadIntegerArray;
import com.game.equip.script.IEquipScript;
import com.game.equip.struct.Equip;
import com.game.equip.struct.EquipPart;
import com.game.player.structs.CellItem;
import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import game.message.EquipMessage;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EquipScript implements IEquipScript {

    private static final Logger log = LogManager.getLogger(EquipScript.class);

    @Override
    public int getId() {
        return ScriptEnum.EquipScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initBagItem(Player player, backpackMessage.ResItemInfos messInfo) {
        for (backpackMessage.ItemInfo it : messInfo.getItemInfoListList()) {
            setBagItem(player,it);
        }
    }

    @Override
    public void setBagItem(Player player, backpackMessage.ItemInfo it) {
        CellItem ci;
        if (player.getBags().containsKey(it.getItemId())) {
            ci = player.getBags().get(it.getItemId());
            ci.setNum(it.getNum());
        } else {
            if(it.getItemModelId()>2000000){
                Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(it.getItemModelId());
                if (eqBean != null) {
                    Equip eq = new Equip();
                    setEquip(eqBean, eq, it);
                    player.getBagEquips().put(it.getItemId(), eq);
                }
            }else{
                ci = new CellItem();
                ci.setItemModelId(it.getItemModelId());
                ci.setNum(it.getNum());
                ci.setGridId(it.getGridId());
                ci.setBind(it.getIsbind());
                ci.setId(it.getItemId());
                ci.setLosttime(it.getLostTime());
                player.getBags().put(it.getItemId(), ci);
            }

        }
    }

    @Override
    public void initBodyEquip(Player player, EquipMessage.ResEquipPartInfo messInfo) {
        Cfg_Equip_Bean eqBean = null;
        EquipPart eqp = null;
        Equip eq = null;
        backpackMessage.ItemInfo it = null;
        //ResEquipPartInfo服务器初始下发的所有装备部位信息都不为NULL
        for (EquipMessage.EquipPartInfo epi : messInfo.getInfosList()) {
            it = epi.getEquip();
            if (it == null || it.getItemModelId() == 0) {//该部位没有装备
                continue;
            }

            eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(it.getItemModelId());
            if (eqBean == null) {
                log.error("没有找到该装备,id=" + it.getItemModelId());
                continue;
            }

            eqp = player.getBodyEquips()[eqBean.getPart()];
            if (eqp == null) {
                eqp = new EquipPart();
                eqp.setType(eqBean.getPart());
//                eqp.setLevel(epi.getStrengthInfo().getLevel());
//                eqp.setCurrentExp(epi.getStrengthInfo().getExp());
                eq = new Equip();
                setEquip(eqBean, eq, it);

                eqp.setEquip(eq);
                player.getBodyEquips()[eqBean.getPart()] = eqp;
            } else {
                eqp = player.getBodyEquips()[eqBean.getPart()];
                eq = eqp.getEquip();
                if (eq == null) {
                    eq = new Equip();
                }
                setEquip(eqBean, eq, it);
            }
        }
        player.setEquipInit(true);
    }

    public void setEquip(Cfg_Equip_Bean eqBean, Equip eq, backpackMessage.ItemInfo it) {
        eq.setItemModelId(it.getItemModelId());
        eq.setId(it.getItemId());
        eq.setNum(it.getNum());
        eq.setBind(it.getIsbind());
        eq.setGridId(it.getGridId());
        eq.setLosttime(it.getLostTime());
        eq.setSuitId(it.getSuitId());
        eq.setQuality(eqBean.getQuality());
        eq.setGrade(eqBean.getGrade());
        eq.setStar(eqBean.getDiamond_Number());
        eq.setGender(eqBean.getGender());
        eq.setLevel(eqBean.getLevel());
        eq.setScore(eqBean.getScore());
        eq.setPart(eqBean.getPart());
    }

    @Override
    public void checkEquipWear(Player player, backpackMessage.ItemInfo it) {
        if (!player.isEquipInit()) {
            return;
        }
        if(it.getItemModelId()<=2000000){//不是装备
            return;
        }

        Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(it.getItemModelId());
        if (eqBean == null) {
            log.info("装备表中没有找到该装备,id=" + it.getItemModelId());
            return;
        }
        if(!eqBean.getGender().contains(player.getCareer())){
            return;
        }
        if(eqBean.getLevel()>player.getLevel()){
            return;
        }
        if(eqBean.getStatelevel()>player.getStateLevel()){
            return;
        }

        //和身上的装备做比较
        Equip eq = player.getBodyEquips()[eqBean.getPart()].getEquip();
        if (eq == null) {//没有装备直接穿上
            eq = new Equip();
            setEquip(eqBean,eq,it);
            player.getBodyEquips()[eq.getPart()].setEquip(eq);
            player.getBagEquips().remove(eq);
            sendReqEquipWear(player,eq);
            player.waitDoTime(1000);
            return;
        }

        if (eqBean.getScore() > eq.getScore()) {
            Equip newEq = new Equip();
            setEquip(eqBean,newEq,it);
            player.getBodyEquips()[eq.getPart()].setEquip(newEq);
            player.getBagEquips().remove(eq);
            sendReqEquipWear(player,newEq);
            player.waitDoTime(1000);
        }
    }

    @Override
    public void checkAllEquipWear(Player player) {
        if (!player.isEquipInit()) {
            return;
        }
        if (player.getBagEquips().isEmpty()) {
            return;
        }

        Equip[] tmpArray = new Equip[8];
        Equip tmpEq = null;
        //遍历背包装备，取到评分最高的
        for (Equip eq : player.getBagEquips().values()) {
            if (!eq.getGender().contains(player.getCareer()) ) {
                continue;
            }
            if (eq.getLevel() > player.getLevel()) {
                continue;
            }

            if (tmpArray[eq.getPart()] == null) {
                tmpEq = new Equip();
                tmpEq.setId(eq.getId());
                tmpEq.setScore(eq.getScore());
                tmpEq.setPart(eq.getPart());
                tmpArray[eq.getPart()] = tmpEq;
            } else if (eq.getScore() > tmpArray[eq.getPart()].getScore()) {
                tmpEq = tmpArray[eq.getPart()];
                tmpEq.setId(eq.getId());
                tmpEq.setScore(eq.getScore());
                tmpEq.setPart(eq.getPart());
            }
        }

        for (Equip eq : tmpArray) {
            if (eq == null) {
                continue;
            }
            if (player.getBodyEquips()[eq.getPart()].getEquip() == null) {//该部位没有装备，直接穿戴
                player.getBodyEquips()[eq.getPart()].setEquip(player.getBagEquips().get(eq.getId()));
                player.getBagEquips().remove(eq.getId());
                sendReqEquipWear(player,eq);
                player.waitDoTime(1000);
                continue;
            }

            if (eq.getScore() > player.getBodyEquips()[eq.getPart()].getEquip().getScore()) {
                player.getBodyEquips()[eq.getPart()].setEquip(player.getBagEquips().get(eq.getId()));
                player.getBagEquips().remove(eq.getId());
                sendReqEquipWear(player,eq);
                player.waitDoTime(1000);
            }
        }
    }

    /**
     * 执行穿戴装备任务,穿戴X阶X色X星X件装备  e.g:15_3_6_1_3
     * @return
     */
    @Override
    public int doWornEquipTask(Player player, Cfg_Task_Bean bean){
        int afterTime = 1000;
        int needNum = bean.getTarget().get(4);
        //检查背包中是否存在任务指定装备
        HashMap<Integer,Equip> parts = new HashMap<>();
        for (Equip eq : player.getBagEquips().values()) {
            if(!eq.getGender().contains(player.getCareer()) ){
                continue;
            }
            if(eq.getLevel()>player.getLevel()){
                continue;
            }
            if(eq.getQuality()<bean.getTarget().get(2)){
                continue;
            }
            if(eq.getGrade()<bean.getTarget().get(1)){
                continue;
            }
            if(eq.getStar()<bean.getTarget().get(3)){
                continue;
            }
            Equip tmpEq = parts.get(eq.getPart());
            if(tmpEq != null){
                if(eq.getScore()<tmpEq.getScore()){
                    continue;
                }
            }
            parts.put(eq.getPart(),eq);
        }

        for (Equip e : parts.values()) {
            sendReqEquipWear(player,e);
            player.waitDoTime(1000);
        }

        if(parts.isEmpty()){
            List<Integer> bodyParts = new ArrayList<>();
            for (int i = 0; i < player.getBodyEquips().length; i++) {
                if(i>=needNum){//检查操作完成次数
                    break;
                }
                Equip eq = player.getBodyEquips()[i].getEquip();
                if(eq == null){
                    continue;
                }
                if(eq.getQuality()>=bean.getTarget().get(2)){
                    continue;
                }
                if(eq.getGrade()>=bean.getTarget().get(1)){
                    continue;
                }
                if(eq.getStar()>=bean.getTarget().get(3)){
                    continue;
                }
                i++;
                bodyParts.add(eq.getPart());
            }
            gmSendEquip(player, bean.getTarget(), bodyParts);
        }
        return afterTime;
    }

    private void gmSendEquip(Player player, ReadIntegerArray con, List<Integer> bodyParts){
        int num = 0;
        HashMap<Integer,Equip> parts = new HashMap<>();
        for (Cfg_Equip_Bean bean:CfgManager.getCfg_Equip_Container().getValuees()) {
            if(num>=con.get(4)-bodyParts.size()){
                break;
            }


            if(!bean.getGender().contains(player.getCareer())){
                continue;
            }
            if(bean.getLevel()>player.getLevel()){
                continue;
            }
            if(bean.getQuality()<con.get(2)){
                continue;
            }
            if(bean.getGrade()<con.get(1)){
                continue;
            }
            if(bean.getDiamond_Number()<con.get(3)){
                continue;
            }
            Equip tmpEq = parts.get(bean.getPart());
            if(tmpEq != null){
                if(bean.getScore()>tmpEq.getScore()){
                    setEquip(bean,tmpEq);
                    num++;
                    continue;
                }
            }
            Equip eq = new Equip();
            setEquip(bean,eq);
            parts.put(eq.getPart(),eq);
            num++;
        }

        for (Equip equip:parts.values()) {
            //使用GM命令增加一件装备
            player.chatGM("&additem "+equip.getItemModelId());
            player.waitDoTime(1000);
        }
    }

    private void setEquip(Cfg_Equip_Bean eqBean, Equip eq) {
        eq.setItemModelId(eqBean.getId());
        eq.setQuality(eqBean.getQuality());
        eq.setGrade(eqBean.getGrade());
        eq.setStar(eqBean.getDiamond_Number());
        eq.setGender(eqBean.getGender());
        eq.setLevel(eqBean.getLevel());
        eq.setScore(eqBean.getScore());
        eq.setPart(eqBean.getPart());
    }

    public void sendReqEquipWear(Player player, Equip eq) {
        if(eq.getId()<=0){
            return;
        }
        log.info(player.getInfo() + "请求穿戴装备ID:" + eq.getId() + ",装备部位:" + eq.getPart() + "，装备战力:" + eq.getScore());
        EquipMessage.ReqEquipWear.Builder msg = EquipMessage.ReqEquipWear.newBuilder();
        msg.setEquipId(eq.getId());
        msg.setInherit(false);
        player.sendMsg(EquipMessage.ReqEquipWear.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
