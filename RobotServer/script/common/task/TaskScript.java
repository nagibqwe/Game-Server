package common.task;

import com.game.player.structs.Player;
import com.game.script.ScriptEnum;
import com.game.task.script.ITaskScript;
import game.message.taskMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskScript implements ITaskScript {

    private static final Logger log = LogManager.getLogger(TaskScript.class);

    @Override
    public int getId() {
        return ScriptEnum.TaskBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void reqChangeTaskState(Player player, int type, int modelId) {
        taskMessage.ReqChangeTaskState.Builder msg = taskMessage.ReqChangeTaskState.newBuilder();
        msg.setType(type);
        msg.setModelId(modelId);
        player.sendMsg(taskMessage.ReqChangeTaskState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
