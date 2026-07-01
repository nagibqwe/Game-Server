
package com.game.structs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import game.core.util.IDConfigUtil;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public abstract class GameObject implements Serializable{
        //64位 对象在内存唯一key
	protected long id;
	
	protected GameObject(){
		this.id = IDConfigUtil.getId();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public abstract void release();
}
