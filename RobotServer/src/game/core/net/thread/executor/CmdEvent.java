package game.core.net.thread.executor;

/**
 * @Desc TODO
 * @Date 2020/9/30 17:26
 * @Auth ZUncle
 */
public class CmdEvent {

    long key;
    Cmd cmd;

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public Cmd getCmd() {
        return cmd;
    }

    public void setCmd(Cmd cmd) {
        this.cmd = cmd;
    }
}
