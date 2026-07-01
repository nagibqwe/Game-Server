package com.backend.service;

import com.backend.bean.CyAnnounce;
import com.backend.bean.Server;
import com.backend.gm.GameServerRequestUtil;
import com.backend.manager.CyAnnounceManager;
import com.backend.manager.ServerListManager;
import org.nutz.lang.util.NutMap;
import org.nutz.log.Log;
import org.nutz.log.Logs;

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
	
	private static final Log log = Logs.get();

	private int interval;

	AnnounceTimerTask(int interval) {
		this.interval = interval;
	}
	
	@Override
	public void run() {

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

			for(String sid: ids) {
				Server server = ServerListManager.getInstance().getServer(sid);
				if(server == null || server.getIsHeFu() == 1) {
					continue;
				}
				
				NutMap ret = GameServerRequestUtil.gmPublishAnnounce(server, an.getType(), an.getContent());
				StringBuilder sb = new StringBuilder();
				if( ret.getBoolean("ok")) {
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
		}	
	}

}
