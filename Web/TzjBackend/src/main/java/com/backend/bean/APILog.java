package com.backend.bean;

import java.util.Enumeration;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.mvc.Mvcs;

import com.backend.struct.StaticField;

@Table("t_api_log")
public class APILog {
	@Id
	private int id;
	/**
	 * 访问URL
	 */
	@Column
	@Comment("请求URL")
	@ColDefine(type = ColType.VARCHAR, width = 255)
	private String url;

	/**
	 * 参数
	 */
	@Column
	@Comment("参数")
	@ColDefine(type=ColType.TEXT)
	private String params;
	
	/**
	 * 操作内容
	 */
	@Column
	@Comment("结果")
	@ColDefine(type=ColType.TEXT)
	private String result;
	
	/**
	 * API类型
	 */
	@Column
	@Comment("API类型")
	private int type;
	
	/**
	 * 操作时间
	 */
	@Column
	@Comment("操作时间 ")
	private long time;
	
	/**
	 * 请求IP
	 */
	@Column
	@Comment("请求IP ")
	private String ip;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public APILog() {
		
	}
	
	public APILog(String result,int type) {
		this.url = Mvcs.getReq().getRequestURI();
		this.params = gainParams();
		this.result = result;
		this.type = type;
		this.ip = Mvcs.getReq().getHeader(StaticField.USER_REAL_IP);
		this.time = System.currentTimeMillis();
	}

	public String gainParams() {
		String params="";
		Enumeration<String> e= Mvcs.getReq().getParameterNames();
		String name;
		String value;
		while(e.hasMoreElements()){
			name=e.nextElement();
			value=Mvcs.getReq().getParameter(name);
			params+=name+"="+value+"&";
		}
		params = params.substring(0,params.length()-1);
		return params;
	}
}
