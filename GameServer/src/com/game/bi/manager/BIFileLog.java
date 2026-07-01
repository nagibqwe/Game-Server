package com.game.bi.manager;

import java.io.OutputStreamWriter;

/**
 * @explain: desc
 * @time Created on 2020/2/17 11:20.
 * @author: tc
 */
public class BIFileLog {
	private String path;
	private OutputStreamWriter writer;
	private long lastBakTime;
	private String filePath;
	private String prefix; // 前缀
	private String suffix; // 后缀

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public OutputStreamWriter getWriter() {
		return writer;
	}

	public void setWriter(OutputStreamWriter writer) {
		this.writer = writer;
	}

	public long getLastBakTime() {
		return lastBakTime;
	}

	public void setLastBakTime(long lastBakTime) {
		this.lastBakTime = lastBakTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	public String toString() {
		return "BIFileLog{" +
				"path='" + path + '\'' +
				", writer=" + writer +
				", lastBakTime=" + lastBakTime +
				", filePath='" + filePath + '\'' +
				", prefix='" + prefix + '\'' +
				", suffix='" + suffix + '\'' +
				'}';
	}
}
