package com.gm.project.gmtool.server.send;

import com.gm.common.utils.security.ShiroUtils;
import com.gm.project.gmtool.server.result.ResultHandler;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.system.user.domain.User;

import java.util.Map;

public class BackendCommand {

	private Map<String,Object> action;

	private int timeout = 60 * 1000;

	public BackendCommand(Map<String, Object> action) {
		User currentUser = ShiroUtils.getSysUser();
		if(currentUser!=null){
			action.put("operationuser", currentUser.getLoginName());
		}
		this.action = action;
	}

	public BackendCommand(Map<String, Object> action, int timeout) {
		User currentUser = ShiroUtils.getSysUser();
		if(currentUser!=null){
			action.put("operationuser", currentUser.getLoginName());
		}
		this.action = action;
		this.timeout = timeout;
	}

	public <T>  T execute(TServer server, ResultHandler<T> handler) {
		String msg = JsonUtils.toJSONString(action);
	    try {
		   String result = GMSendUtil.send(msg, server.getServerIP(), server.getServerPort(), timeout);
		   return handler.hand(server,result);
	    } catch (Exception e) {
		   e.printStackTrace();
		   return handler.hand(server,e);
	    }
	}

}
