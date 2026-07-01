/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.thread;

import com.game.db.DBErrorToFile;
import com.game.db.bean.roleBean;
import com.game.db.dao.roleDao;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author Administrator
 */
public class SavePlayerThread extends Thread {

    //命令执行队列
    private final LinkedBlockingQueue<Long> role_queue = new LinkedBlockingQueue<>();

    private final HashMap<Long, roleBean> role_map = new HashMap<>();
    //运行标志
    private boolean stop;

    //线程名称
    protected String threadName;

    private final roleDao dao = new roleDao();

    private static final int MAX_SIZE = 10000;

    public SavePlayerThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        stop = false;
        while (!stop || role_queue.size() > 0) {
            roleBean role = null;
            synchronized (this) {
                Object o = role_queue.poll();
                if (o != null) {
                    long roleId = (Long) o;
                    role = role_map.remove(roleId);
                }
            }
            if (role == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save Role Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
                if (role_queue.size() > MAX_SIZE) {

                }
                try {
                    if (dao.update(role) == 0) {
                        if (dao.insert(role) == 0) {
                            DBErrorToFile.error("SavePlayerThread保存玩家数据出错，" + role.toString());
                        }
                    }
                } catch (Exception e) {
                    DBErrorToFile.error(e + " role id :" + role.getRoleid() + "_" + role.getRolename(), e);

                    synchronized (this) {
                        if (!role_map.containsKey(role.getRoleid())) {
                            this.role_queue.add(role.getRoleid());
                            this.role_map.put(role.getRoleid(), role);
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
            DBErrorToFile.error("Role Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    /**
     * 添加玩家数据
     *
     * @param role
     */
    public void addRole(roleBean role) {
        try {
            synchronized (this) {
                if (!role_map.containsKey(role.getRoleid())) {
                    this.role_queue.add(role.getRoleid());
                }
                this.role_map.put(role.getRoleid(), role);
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Role Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }
}
