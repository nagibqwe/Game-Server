package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户菜单
 */
@Table("t_menu")
public class Menu {

	@Id
	@Column
	@Comment("菜单ID")
	private int menuId;
	
	@Column
	@Comment("菜单名")
	private String menuName;
	
	@Column
	@Comment("菜单等级")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("3")
	private int level;
	
	@Column
	@Comment("父级菜单ID")
	private int parentId;
	
	@Column
	@Comment("菜单别名")
	private String alias;
	
	@Column
	@Comment("菜单url路径")
	private String urlPath;
	
	@Column
	@Comment("菜单描述")
	@ColDefine(type=ColType.TEXT)
	private String description;
	
	@Column
	@Comment("菜单生效标识 0:生效1:无效")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isDeleted;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getUrlPath() {
		return urlPath;
	}
	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
	
}
