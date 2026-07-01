package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_code_batch")
//@TableIndexes(value={@Index(fields={"batchId"}, name="batchId_index",unique=true)})
public class CodeBatch {
	@Id
	private int id;
	@Column
	@Comment("批号")
	private int batchId;
	@Column
	@Comment("后台用户ID")
	private int userId;
	@Column
	@Comment("添加时间")
	private long time;
	@Column
	@Comment("平台名")
	private String platform;
	@Column
	@Comment("万能码标识0:普通激活码1:万能码")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isUniversal;//激活码是否为万能码
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getBatchId() {
		return batchId;
	}
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public int getIsUniversal() {
		return isUniversal;
	}
	public void setIsUniversal(int isUniversal) {
		this.isUniversal = isUniversal;
	}
}
