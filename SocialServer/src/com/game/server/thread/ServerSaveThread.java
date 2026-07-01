package com.game.server.thread;

import com.game.db.DBErrorToFile;
import com.game.db.bean.ServerParamBean;
import com.game.db.dao.ServerParamDao;
import com.game.server.struct.ServerParams;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Desc TODO
 * @Date 2021/8/9 17:18
 * @Auth ZUncle
 */
public class ServerSaveThread extends Thread {

    final LinkedBlockingQueue<ServerParams> queue = new LinkedBlockingQueue<>();

    final HashMap<ServerParams, ServerParamBean> beans = new HashMap<>();

    final ServerParamDao dao = new ServerParamDao();

    //运行标志
    private boolean stop;
    //线程名称九 零一起玩  www.9017  5.com
    protected String threadName;

    public ServerSaveThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop || queue.size() > 0) {
            ServerParams key = queue.poll();
            if (key == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save ServerParams Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
                ServerParamBean bean = beans.remove(key);
                try {
                    if (dao.insert(bean) == 0) {
                        DBErrorToFile.error("ServerParams 保存出错:" + bean);
                    }
//                    DBErrorToFile.error("ServerParams 保存完成:" + bean);
                } catch (Exception e) {
                    DBErrorToFile.error("ServerParams Exception:", e);
                    synchronized (this) {
                        if (!beans.containsKey(key)) {
                            queue.add(key);
                            beans.put(key, bean);
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
            DBErrorToFile.error("ServerParams Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * @param key
     */
    public void dealSave(ServerParams key, ServerParamBean bean) {
        try {
            synchronized (this) {
                if (!beans.containsKey(key)) {
                    queue.add(key);
                }
                beans.put(key, bean);
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("ServerParams Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

}
