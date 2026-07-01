package game.core.script;

public interface IScript {

    /**
     * 获取scriptId
     *
     * @return
     */
    int getId();

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    Object call(Object... args);
}
