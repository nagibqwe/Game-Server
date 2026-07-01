/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.behavior.structs;

import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import game.core.command.ICommand;
import game.core.map.IMapObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 基础行为类实现
 *
 * @author soko <xuchangming@haowan123.com>
 */
public abstract class BaseBehavior implements ICommand {

    protected static final Logger log = LogManager.getLogger("BaseBehavior");

    private int _bs = BehaviorStatus.ACTIVITYSTATUS_UNKNOW;
    private final IMapObject _self;
    private long _offset;
    private long _leaveSet;

    private IBehaviorEvent overBack = null;

    public void setOverBack(IBehaviorEvent overBack) {
        this.overBack = overBack;
    }

    //只能在子类中实现
    protected BaseBehavior(IMapObject imo) {
        _self = imo;
    }

    public boolean Cancel() {
        Over();
        return true;
    }

    public boolean Leave() {
        SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
        return true;
    }

    public boolean IsCancel() {
        return true;
    }

    /**
     * 返回行为执行主体
     *
     * @return
     */
    public IMapObject getOwner() {
        return _self;
    }

    /**
     * 返回行为执行间隔
     *
     * @return
     */
    public long getOffset() {
        return _offset;
    }

    public boolean Do(long offset) {
        MapServer map = GameServer.getInstance().getMServer(_self.gainMapId());
        if (map == null) {
            log.error(" , 地图活怪 =" + _self + " 已经找不到可执行的线程了，地图：" + _self.gainMapId() + " line :" + _self.gainLine());
            _leaveSet += offset;
            Leave();
            return false;
        }
        if (_leaveSet > 0) {
            offset += _leaveSet;
        }
        _offset = offset;
        map.addCommand(this);
        _leaveSet = 0;
        return true;
    }

    public int GetState() {
        return _bs;
    }

    public void Update(long offset) {

        switch (_bs) {
            case BehaviorStatus.ACTIVITYSTATUS_INITIALIZED:
                init();
                break;
            case BehaviorStatus.ACTIVITYSTATUS_RUN:
                Do(offset);
                break;
            case BehaviorStatus.ACTIVITYSTATUS_CANCEL:
                Leave();
                break;
            case BehaviorStatus.ACTIVITYSTATUS_OVERED:
                Over();
                break;
            default:
                break;
        }
    }

    public void init() {
        SetState(BehaviorStatus.ACTIVITYSTATUS_RUN);
    }

    public void SetState(int value) {
        if (_bs != value) {
//            if (value == BehaviorStatus.ACTIVITYSTATUS_OVERED && this instanceof GuideSkillHitCheckBehavior) {
//                IMapObject o = getOwner();
//                if (o instanceof Entity) {
//                    ((Entity) o).changeDebugString(new Throwable(o.toString()));
//                }
//            }
            _bs = value;

            if (BehaviorStatus.ACTIVITYSTATUS_OVERED == value) {
                if (overBack != null) {
                    try {
                        overBack.BehaviorOver(this);
                    } catch (Exception e) {
                        log.error(" 执行时 _offset=" + _offset, e);
                    }
                }
            }
        }
    }

    public abstract BehaviorType getType();

    public boolean Over() {
        if (_bs != BehaviorStatus.ACTIVITYSTATUS_OVERED) {
            _bs = BehaviorStatus.ACTIVITYSTATUS_OVERED;
            if (overBack != null) {
                try {
                    overBack.BehaviorOver(this);
                } catch (Exception e) {
                    log.error(" 执行时 _offset=" + _offset, e);
                }
            }
//            if(this instanceof GuideSkillHitCheckBehavior){
//                IMapObject o = getOwner();
//                if(o instanceof Entity){
//                    ((Entity)o).changeDebugString(new Throwable(o.toString()));
//                }
//            }
            return true;
        }
        return false;
    }

    public boolean IsOver() {
        return BehaviorStatus.ACTIVITYSTATUS_OVERED == _bs;
    }

    @Override
    public void action() {
        log.error(" 执行时 _offset=" + _offset);
        Over();//结束
    }
}
