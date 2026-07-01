package com.game.server.thread;

import com.game.server.script.IErrorReportScript;
import com.game.server.structs.ErrorExceptionLog;
import com.game.server.structs.ErrorInfo;
import com.game.server.structs.ErrorLog;
import com.game.db.DBErrorToFile;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 错误日志监控线程
 *
 * @author admin
 */
public class ErrorLogThread extends Thread {

    private static final Logger log = LogManager.getLogger(ErrorLogThread.class);

    private boolean stop = false;

    private final String threadName;

    //命令执行队列
    private final LinkedBlockingQueue<ErrorInfo> gold_queue = new LinkedBlockingQueue<>();

    //添加标记天数
    private static int LastTickDay = 0;

    //缓存玩家的物品错误日志
    private static ConcurrentHashMap<String, ConcurrentHashMap<Integer, ErrorLog>> errorlog = new ConcurrentHashMap<>();

    //加上线程锁
    public static synchronized ConcurrentHashMap<String, ConcurrentHashMap<Integer, ErrorLog>> getErrorlog() {
        return errorlog;
    }

    //清理当前的数据
    public static void clearErrorLog(int curDay) {
        if (curDay != LastTickDay) {
            LastTickDay = curDay;
            getErrorlog().clear();
        }
    }

    public ErrorLogThread(String name) {
        this.threadName = name;
    }

    @Override
    public void run() {
        stop = false;
        while (!stop || gold_queue.size() > 0) {
            ErrorInfo errorInfo = gold_queue.poll();
            if (errorInfo == null) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    DBErrorToFile.error("Save Gold Thread " + threadName + " Wait Exception:" + e.getMessage());
                }
            } else {
//                manager.scriptManager.callMethod(ScriptEnum.ErrorLogReport, "addlog", errorInfo);
                IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ErrorLogReportBaseScript);
                if (is instanceof IErrorReportScript) {
                    ((IErrorReportScript) is).addlog(errorInfo);
                } else {
                    log.error("错误报告的实例没有找到！");
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
            log.error("Gold Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    public void pushErrorLog(String key, ErrorLog log) {
        try {
            this.gold_queue.add(new ErrorInfo(key, log));
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Gold Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

    public void pushErrorExcptionLog(String type, String errorValue) {
        try {
            ErrorExceptionLog exceptionLog = new ErrorExceptionLog();
            exceptionLog.setErrorType(type);
            exceptionLog.setErrorValue(errorValue);
            this.gold_queue.add(new ErrorInfo(type, exceptionLog));
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            DBErrorToFile.error("Gold Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }

}
