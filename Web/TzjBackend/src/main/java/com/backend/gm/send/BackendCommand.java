package com.backend.gm.send;

import com.backend.bean.Server;
import com.backend.gm.result.ResultHandler;
import com.backend.utils.JsonUtils;

import java.util.Map;

public class BackendCommand {

	private Map<String,Object> action;

	private int timeout = 60 * 1000;

	public BackendCommand(Map<String, Object> action) {
		this.action = action;
	}

	public BackendCommand(Map<String, Object> action, int timeout) {
		this.action = action;
		this.timeout = timeout;
	}

	public <T>  T execute(Server server, ResultHandler<T> handler) {
		String msg = JsonUtils.toJSONString(action);
	    try {
		   String result = GMSendUtil.send(msg, server.getWorldIP(), server.getWorldPort(), timeout);
		   return handler.hand(server,result);
	    } catch (Exception e) {
		   e.printStackTrace();
		   return handler.hand(server,e);
	    }
	}

}
