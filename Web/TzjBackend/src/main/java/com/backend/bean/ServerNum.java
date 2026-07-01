package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_servernum")
@TableIndexes(value = { @Index(fields = { "serverId" }, name = "serverId_index", unique=false),@Index(fields={"time"},name="time_index",unique=false)})
public class ServerNum {

	@Id
	@Column
	private int id;
	
	@Column
	@Comment("服务器区号")
	private int serverId;
	
	@Column
	@Comment("标记的日期")
	private String day;
	
	@Column
	@Comment("标记的小时")
	private int hour;
	
	@Column
	@Comment("标记的分钟")
	private int min;
	
	@Column
	@Comment("服务器的人数")
	private int num;
	
	@Column
	@Comment("时间")
	private int time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
}
