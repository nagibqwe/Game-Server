package com.gm.project.gmtool.cyAnnounce;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.project.gmtool.manager.CyAnnounceManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

@Service
public class TaskTimerService {
    @Autowired
    private ITServerService tServerService;

    public static TaskTimerService getInstance() {
        return   (TaskTimerService) SpringUtils.getBean("taskTimerService");
    }

    /**
     * 项目启动时就开始执行循环公告的发送
     */
    @PostConstruct
    public void init(){
        List<Integer> list = new ArrayList<>(CyAnnounceManager.getInstance().getAnnounceList().keySet());
        for(Integer interval : list){
            StartAnnounceTask(interval);
        }
    }
    public void StartAnnounceTask(int key) {
        if( key < 1) {
            return;
        }
        Timer tt = new Timer("announce-timer" + key);
        tt.schedule(new AnnounceTimerTask(key), 1, key * 60 * 1000);
    }

}
