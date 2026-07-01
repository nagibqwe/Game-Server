package game.core.net.thread.executor;

import com.game.player.structs.Player;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2020/9/30 18:20
 * @Auth ZUncle
 */
public class PlayerTickCmd implements Cmd {
    static Logger logger = LogManager.getLogger(PlayerTickCmd.class);

    final long time;
    final Player player;

    public PlayerTickCmd(long time, Player player) {
        this.time = time;
        this.player = player;
    }

    @Override
    public void action() {
        if (player == null) {
            return;
        }
        long start = TimeUtils.Time();
        try {
            player.sendHeartToServer(time);
            //player.timeTick(time);
        } catch (Exception e) {
            logger.info(e, e);
        }
        long offset = TimeUtils.Time() - start;
        if (offset > 200) {
            logger.error("处理时间 PlayerTickCmd  offset={}", offset);
        }
    }
}
