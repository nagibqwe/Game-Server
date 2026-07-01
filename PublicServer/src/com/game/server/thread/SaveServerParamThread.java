package com.game.server.thread;

import com.game.db.DBErrorToFile;
import com.game.db.bean.ServerParamBean;
import com.game.db.dao.ServerParamDao;
import game.core.util.JsonUtils;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 服务器参数的保存系统
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class SaveServerParamThread extends Thread {

    //数据库
    private final ServerParamDao dao = new ServerParamDao();

    //命令执行队列
    private final LinkedBlockingQueue<ServerParamInfo> serverParam_queue = new LinkedBlockingQueue<>();
    //运行标志
    private boolean stop;
    //线程名称
    protected String threadName;

    boolean insertDB = true;

//    private static int MAX_SIZE = 1000;
    public static int ServerParam_UPDATE = 0;
    public static int ServerParam_INSERT = 1;

    public SaveServerParamThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop || serverParam_queue.size() > 0) {
            ServerParamInfo serverParamInfo = serverParam_queue.poll();
            if (serverParamInfo == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save serverParam Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
                try {
                    if (insertDB) {                   
                        if (dao.update(serverParamInfo.getServerParam()) == 0) {
                            if (dao.insert(serverParamInfo.getServerParam()) == 0) {

                            }
                        }
                    } else {
                        DBErrorToFile.error(String.format("ServerParam数据执行步骤[4]，ServerParam_Key[%s]，ServerID[%d]", serverParamInfo.getServerParam().getParamkey(), serverParamInfo.getServerParam().getServerid()));
                    }
                } catch (Exception e) {
                    DBErrorToFile.error(String.format("ServerParam数据执行步骤[8]，ServerParam_Key[%s]，ServerID[%d]，ServerParam_Value[%s]", serverParamInfo.getServerParam().getParamkey(), serverParamInfo.getServerParam().getServerid(), serverParamInfo.getServerParam().getParamvalue()));
                    DBErrorToFile.error("serverParam Exception:", e);
                    serverParam_queue.add(serverParamInfo);
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
            DBErrorToFile.error("ServerParam Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * 处理ServerParam
     *
     * @param serverParam
     * @param deal
     */
    public void dealServerParam(ServerParamBean serverParam, int deal) {
        try {
            this.serverParam_queue.add(new ServerParamInfo(serverParam, deal));
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("ServerParam Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    private class ServerParamInfo {

        private final ServerParamBean serverParam;
        private final int deal;

        public ServerParamInfo(ServerParamBean serverParam, int deal) {
            this.serverParam = serverParam;
            this.deal = deal;
        }

        public ServerParamBean getServerParam() {
            return serverParam;
        }

        public int getDeal() {
            return deal;
        }

        @Override
        public String toString() {
            return JsonUtils.toJSONString(this);
        }
    }
}
