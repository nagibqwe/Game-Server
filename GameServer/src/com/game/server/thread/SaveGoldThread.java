/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.thread;

import com.game.db.DBErrorToFile;
import com.game.gold.structs.Gold;
import com.game.db.bean.GoldChange;
import com.game.db.dao.GoldChangeDao;
import com.game.db.dao.GoldDao;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author hewei@haowan123.com
 */
public class SaveGoldThread extends Thread {

    //命令执行队列
    private final LinkedBlockingQueue<GoldInfo> gold_queue = new LinkedBlockingQueue<>();

    //运行标志
    private boolean stop;

    //线程名称
    protected String threadName;

    boolean insertDB = true;

    private static final int MAX_SIZE = 10000;

    private final GoldDao dao = new GoldDao();

    private final GoldChangeDao changeDao = new GoldChangeDao();

    public SaveGoldThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop || gold_queue.size() > 0) {
            GoldInfo goldInfo = gold_queue.poll();
            if (goldInfo == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save Gold Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
                try {
                    if (gold_queue.size() > MAX_SIZE) {

                    }
                    if (insertDB) {
                        if (dao.update(goldInfo.getGold()) < 1) {
                            dao.insert(goldInfo.getGold());
                        }
                        if (goldInfo.getGoldChange() != null) {
                            changeDao.insert(goldInfo.getGoldChange());
                        }
                        insertDB = true;
                    } else {
                        DBErrorToFile.error("[" + goldInfo.goldChange + "]" + goldInfo.getGold().toString());
                    }
                } catch (Exception e) {
                    DBErrorToFile.error("Gold Exception:" + goldInfo.getGold().getServerId() + "[" + goldInfo.getGold().getReaminGold() + "]", e);
                    gold_queue.add(goldInfo);
                    insertDB = true;
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
            DBErrorToFile.error("Gold Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * 添加金币数据
     *
     * @param gold 金币数据
     * @param goldChange 是否插入
     */
    public void addGold(Gold gold, GoldChange goldChange) {
        try {
            this.gold_queue.add(new GoldInfo(gold, goldChange));
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Gold Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    private class GoldInfo {

        private final Gold gold;

        private final GoldChange goldChange;

        public GoldInfo(Gold gold, GoldChange goldChange) {
            this.gold = gold;
            this.goldChange = goldChange;
        }

        public Gold getGold() {
            return gold;
        }

        public GoldChange getGoldChange() {
            return goldChange;
        }

    }
}
