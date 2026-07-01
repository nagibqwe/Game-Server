package com.game.server.thread;

import com.game.db.DBErrorToFile;
import com.game.db.dao.PlayerDao;
import com.game.player.structs.GlobalPlayerWorldInfo;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Desc TODO
 * @Date 2021/6/23 16:57
 * @Auth ZUncle
 */
public class PlayerSaveThread extends Thread {

    final LinkedBlockingQueue<Long> queue = new LinkedBlockingQueue<>();
    final HashMap<Long, GlobalPlayerWorldInfo> players = new HashMap<>();
    //数据库
    final PlayerDao dao = new PlayerDao();
    final String InsertOption = "player.insert";
    //运行标志
    private boolean stop;
    //线程名称
    protected String threadName;

    public PlayerSaveThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop || queue.size() > 0) {
            Long roleId = queue.poll();
            if (roleId == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save Player Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
                GlobalPlayerWorldInfo player = players.remove(roleId);
                try {
                    player.toDB();
                    if (dao.insert(InsertOption, player) == 0) {
                        DBErrorToFile.error("Player 保存出错:" + player);
                    }
//                    DBErrorToFile.error("Player 保存完成:" + player);
                } catch (Exception e) {
                    DBErrorToFile.error("Player Exception:", e);
                    synchronized (this) {
                        if (!players.containsKey(player.getId())) {
                            queue.add(player.getId());
                            players.put(player.getId(), player);
                        }
                    }
                }
            }
        }
    }

    public void stop(boolean flag) {
        stop = flag;
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Player Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * @param player
     */
    public void dealSave(GlobalPlayerWorldInfo player) {
        try {
            synchronized (this) {
                if (!players.containsKey(player.getId())) {
                    queue.add(player.getId());
                }
                players.put(player.getId(), player);
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Player Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }


}
