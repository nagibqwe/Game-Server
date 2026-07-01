package common.buff.script;

import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2020/9/21 11:20
 * @Auth ZUncle
 */
public class JinliaoBuff implements IScript, IBuffBehavior {
    /**
     * 增加buff,会在buff被增加到目标身上之前被调用
     *
     * @param buff
     * @param source 来源
     * @param target 对象
     * @return
     */
    @Override
    public int add(Buff buff, Fighter source, Fighter target) {
        return 0;
    }

    /**
     * buff 叠加
     *
     * @param buff
     * @param owner
     */
    @Override
    public void overlap(Buff buff, Fighter owner) {

    }

    /**
     * 移除buff,会在buff从目标身上移除之前被调用
     *
     * @param buff
     * @param owner buff拥有者
     * @return
     */
    @Override
    public int remove(Buff buff, Fighter owner) {
        return 0;
    }

    /**
     * buff作用 定时处理
     *
     * @param buff
     * @param attacker 来源
     * @param owner  buff拥有者
     * @return 作用数值 0 忽略 -1执行删除buff
     */
    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        return 0;
    }

    /**
     * buff 时间到时候的事件触发
     *
     * @param buff
     * @param target
     * @return
     */
    @Override
    public int timeout(Buff buff, Fighter target) {
        return 0;
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.JinliaoBuff;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
