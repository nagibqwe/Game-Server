package com.gm.project.gmtool.cyAnnounce;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.framework.web.domain.AjaxResult;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;
import com.gm.project.gmtool.manager.CyAnnounceManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.server.service.impl.TServerServiceImpl;
import com.gm.project.gmtool.utils.GameServerRequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * 循环公告的计时发送处理
 */
public class AnnounceTimerTask extends TimerTask{

	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger log = LoggerFactory.getLogger(AnnounceTimerTask.class);

	private int interval;

	AnnounceTimerTask(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void run() {

		if( interval < 1) {
			return;
		}
		long now = System.currentTimeMillis();
		int step = interval * 60 * 1000;

		List<CyAnnounce> list = new ArrayList<>(CyAnnounceManager.getInstance().getList(interval));

		for( CyAnnounce an : list) {

			if( an.getState() != 0) {
				continue;
			}

			//开始时间没到
			if( now < an.getFromTime()) {
				continue;
			}

			//检查次数
			if( an.getTotalTimes() > 0  && an.getNowTimes() >= an.getTotalTimes()) {
				an.setState(2);
				CyAnnounceManager.getInstance().updateSave(an);
				continue;
			}

			//超过了结束时间
			if(now > an.getToTime()) {
				an.setState(3);
				CyAnnounceManager.getInstance().updateSave(an);
				continue;
			}

			//下一次的时间还没到则返回
			if( now < an.getNextTimes()) {
				continue;
			}

			String sids = an.getServerIds();
			String [] ids = sids.split(",");
			//没有服务器列表也删除
			if( ids.length < 1) {
				CyAnnounceManager.getInstance().remove(an);
				continue;
			}
			TServerServiceImpl tServerService = SpringUtils.getBean("TServerServiceImpl");
			if (tServerService == null){
				System.out.println("tServerService is null");
			}

			for(String sid: ids) {

				TServer server = tServerService.selectTServerByServerId(Integer.parseInt(sid));
				if(server == null || server.getIsHeFu() == 1) {
					continue;
				}

				AjaxResult ret = GameServerRequestUtil.gmPublishAnnounce(server, an.getType(), an.getContent());
				StringBuilder sb = new StringBuilder();
				if(Boolean.valueOf(ret.get("ok").toString())) {
					sb.append(server.getServerName()).append("接收即时公告刷新成功 ！");
				} else {
					sb.append(server.getServerName()) .append("失败，原因：").append(ret.get("msg"));
				}
				log.info(sb.toString());
			}
			an.setNowTimes(an.getNowTimes() + 1);
			if (an.getNowTimes() < an.getTotalTimes()) {
				an.setNextTimes(now + step);
				an.setNextDate(format.format(new Date(an.getNextTimes())));
			}
			int num = CyAnnounceManager.getInstance().updateSave(an);
			log.info("公告id：" + an.getId() + "发送次数：" + an.getNowTimes() + "  更新结果：" + num);
			log.info("循环公告耗时:" +(System.currentTimeMillis() - now)+ "ms");
		}
	}
}
