package common.couplefight;

import com.game.couplefight.ICoupleManagerScript;
import com.game.script.ScriptEnum;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/10/16 15:06
 */
public class CoupleManagerScript implements ICoupleManagerScript {
    @Override
    public int getId() {
        return ScriptEnum.CouplefightScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
