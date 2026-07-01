package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.MagicDirMoveBehavior;
import com.game.behavior.structs.type.MagicMoveBehavior;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.SkillMagic;
import com.game.structs.Entity;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.FightMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by soko(xysoko@qq.com) on 2018/9/29. copyright 巨匠@雨墨
 */
public class MagicBehaviorScript implements IScript, IBehavior {
    private static final Logger log = LogManager.getLogger(MagicBehaviorScript.class);

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        if (behavior instanceof  MagicDirMoveBehavior){
            return onCancel((MagicDirMoveBehavior) behavior);
        }

        if( behavior instanceof MagicMoveBehavior){
            return onCancel((MagicMoveBehavior) behavior);
        }

        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {
        if (behavior instanceof  MagicDirMoveBehavior){
            onAction((MagicDirMoveBehavior) behavior);
        }

        if( behavior instanceof MagicMoveBehavior){
            onAction((MagicMoveBehavior) behavior);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.MagicBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //==================DirMoveEvent ============ begin=====================================//

    private boolean onCancel(MagicDirMoveBehavior magicDirMoveBehavior){
        onAction(magicDirMoveBehavior);
        magicDirMoveBehavior.Over();
        return true;
    }

    private void onAction(MagicDirMoveBehavior behavior) {

        onAction((MagicMoveBehavior)behavior);

        //检查是否跑到了目标点，应该结束了
        float dis = Utils.getDistance(behavior.getOwner().gainCurPos(), behavior.getDefer().gainCurPos());
        if( dis < 0.5f){
            log.error("已经中到目标，是否要移除呢！");
        }

        if(behavior.getTargetPos().compare(behavior.getDefer().gainCurPos())){
            return;
        }

        behavior.setDirPos(Utils.getDir(behavior.getOwner().gainCurPos(), behavior.getDefer().gainCurPos()));
        behavior.setXy(behavior.getDefer().gainCurPos().ceilX(), behavior.getDefer().gainCurPos().ceilY());
        SkillMagic magic = (SkillMagic)behavior.getOwner();
        //发送最新的移动路径
        FightMessage.ResPlaySkillObject.Builder msg = FightMessage.ResPlaySkillObject.newBuilder();
        msg.setPosX(magic.gainX());
        msg.setPosY(magic.gainY());
        msg.setID(magic.getId());
        msg.setOwnerID(magic.getOwnerId());
        msg.setVisualDef(behavior.getSkillVisalName());
        msg.setMoveSpeed(behavior.getSpeed());
        msg.setMpveAddSpeed(behavior.getaSpeed());
        msg.addMovePosList(MapUtils.getPos(magic.gainCurPos()));
        msg.addMovePosList(MapUtils.getPos(behavior.getDefer().gainCurPos()));
        MessageUtils.send_to_roundPlayer(magic, FightMessage.ResPlaySkillObject.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //==================MoveEvent ============ begin=====================================//
    private boolean onCancel(MagicMoveBehavior magicMoveBehavior){
        onAction(magicMoveBehavior);
        magicMoveBehavior.Over();
        return true;
    }



    private void onAction(MagicMoveBehavior behavior) {
        //300是为了与客户端同步误差，所以做的一个偏移值， 目的是让服务器坐标跑得更快一些
        float t = (TimeUtils.Time() - behavior.getBeginTime() + 300) / 1000.0f;
        double s = behavior.getSpeed() * t + 0.5d * behavior.getaSpeed() * Math.pow(t, 2);

        float curlen = (float) (s - behavior.getLastDis());
        if( curlen < 0){
            log.error(" 下一步的时间是：" + t + " 走的距离是:" + s );
            return ;
        }

        //设置最后一步的距离值
        behavior.setLastDis((float)s);

        Position pos = Utils.getPosByDir(behavior.getOwner().gainCurPos(), behavior.getDirPos(), curlen);

        Entity entity = (Entity)behavior.getOwner();
        entity.changeCurPos(pos, true);

//        log.error(entity.nameIdString() + " ,移动更新！");
    }
}
