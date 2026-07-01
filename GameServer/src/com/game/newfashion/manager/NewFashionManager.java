package com.game.newfashion.manager;

import com.game.manager.Manager;
import com.game.newfashion.script.INewFashionScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2020/3/23.
 */
public class NewFashionManager {

    private final static Logger log = LogManager.getLogger(NewFashionManager.class);

    public static final int BODY_TYPE     = 1;//装备时装

    public static final int WEAPON_TYPE   = 2;//武器时装

    public static final int WING_TYPE     = 3;//翅膀 背饰

    public static final int HORSE_TYPE    = 4;//坐骑

    public static final int PET_TYPE      = 5;//骑宠

    public static final int FABAO_TYPE    = 6;//法宝

    public static final int SOUL_TYPE     = 7;//魂甲

    public static final int HEAD_TYPE     = 11;// 头像

    public static final int HEAD_FRAME_TYPE     = 12;// 头像框

    public static final int BUBBLE_TYPE     = 13;// 气泡




    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        NewFashionManager manager;

        Singleton() {
            this.manager = new NewFashionManager();
        }

        NewFashionManager getProcessor() {
            return manager;
        }
    }

    public static NewFashionManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public INewFashionScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NewFasionBaseScript);
        if (is instanceof INewFashionScript) {
            return (INewFashionScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
