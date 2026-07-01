package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [拍卖]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIAuction {
	/**
	 * 日志流水号，在一个游戏区服下，每条日志必须保证全局唯一性
	 */
	private String log_id = "";
	/**
	 * 游戏事件的时间 时间格式为Z yyyy-MM-dd HH:mm:ss 时区+时间形式，如：+0700 2020-10-26 20:16:38
	 */
	private String event_time = "";
	/**
	 * 游戏大区ID
	 */
	private String zone_id = "";
	/**
	 * 游戏区服ID
	 */
	private String server_id = "";
	/**
	 * 游戏版本号,例 0.5.2.0
	 */
	private String game_version = "";
	/**
	 * 外部平台账号ID,如果接绿岸SDK,则为SDK分配的账号ID
	 */
	private String account_id = "";
	/**
	 * 游戏角色ID
	 */
	private String role_id = "";
	/**
	 * 创建的游戏角色名
	 */
	private String role_name = "";
	/**
	 * 角色性别 0:女 1:男
	 */
	private String role_sex = "";
	/**
	 * 角色职业,【字典表定义】
	 */
	private String role_prof = "";
	/**
	 * 角色当前等级
	 */
	private String role_level = "";
	/**
	 * 角色vip等级，如果没有则设为空
	 */
	private String role_vip_level = "";
	/**
	 * 角色当前战力值
	 */
	private String role_combat = "";
	/**
	 * 客户端应用ID,来源于绿岸SDK包体
	 */
	private String app_id = "";
	/**
	 * 用户渠道ID,来源于绿岸SDK包体
	 */
	private String channel_id = "";
	/**
	 * 用于入口来源ID,如:广告买量ID,值来源于绿岸SDK包体
	 */
	private String source_id = "";
	/**
	 * 游戏运行平台 1:IOS 2:安卓 3:H5 4:未知
	 */
	private String platform = "";
	/**
	 * 用于设备ID,当客户端于android或ios平台时,通过绿岸SDK获取
	 */
	private String device_id = "";
	/**
	 * 设备名称,移动设备专用。格式:品牌-型号,如 三星GT-S5830
	 */
	private String device_name = "";
	/**
	 * 设备屏幕分辨率,移动设备专用 如:480*800
	 */
	private String device_screen = "";
	/**
	 * 客户端操作系统版本号
	 */
	private String device_version = "";
	/**
	 * 玩家IP
	 */
	private String user_ip = "";
	/**
	 * 地图ID
	 */
	private String map_id = "";
	/**
	 * 目标ID
	 */
	private String target_id = "";
	/**
	 * 目标类型 1:个人 2:仙盟 3:活动
	 */
	private String target_type = "";
	/**
	 * 操作类型 1:上架 2:竞拍 3:竞拍成交 4:一口价成交 5:超时下架 6:转至世界拍卖 7:主动下架
	 */
	private String auction_op_type = "";
	/**
	 * 物品自身id
	 */
	private String item_id = "";
	/**
	 * 物品类型，详见【字典】item表中的type字段
	 */
	private String item_type = "";
	/**
	 * 物品颜色 1:白 2:绿 3:蓝 4:紫 5:橙 6:金 7:红 8:粉 9:暗金 10:幻彩
	 */
	private String item_colour = "";
	/**
	 * 物品品阶
	 */
	private String item_lev = "";
	/**
	 * 物品星级
	 */
	private String item_star = "";
	/**
	 * 上架物品唯一标识id
	 */
	private String item_no = "";
	/**
	 * 物品名称
	 */
	private String item_name = "";
	/**
	 * 物品数量
	 */
	private String item_num = "";
	/**
	 * 竞标价
	 */
	private String bid_price = "";
	/**
	 * 一口价，个别物品没有一口价，则发送0
	 */
	private String fixed_price = "";
	/**
	 * 拍品所在公会ID
	 */
	private String guild_id = "";

	public BIAuction() {}

	public BIAuction(
			String log_id,
			String event_time,
			String zone_id,
			String server_id,
			String game_version,
			String account_id,
			String role_id,
			String role_name,
			String role_sex,
			String role_prof,
			String role_level,
			String role_vip_level,
			String role_combat,
			String app_id,
			String channel_id,
			String source_id,
			String platform,
			String device_id,
			String device_name,
			String device_screen,
			String device_version,
			String user_ip,
			String map_id,
			String target_id,
			String target_type,
			String auction_op_type,
			String item_id,
			String item_type,
			String item_colour,
			String item_lev,
			String item_star,
			String item_no,
			String item_name,
			String item_num,
			String bid_price,
			String fixed_price,
			String guild_id
	) {
		setLog_id(log_id);
		setEvent_time(event_time);
		setZone_id(zone_id);
		setServer_id(server_id);
		setGame_version(game_version);
		setAccount_id(account_id);
		setRole_id(role_id);
		setRole_name(role_name);
		setRole_sex(role_sex);
		setRole_prof(role_prof);
		setRole_level(role_level);
		setRole_vip_level(role_vip_level);
		setRole_combat(role_combat);
		setApp_id(app_id);
		setChannel_id(channel_id);
		setSource_id(source_id);
		setPlatform(platform);
		setDevice_id(device_id);
		setDevice_name(device_name);
		setDevice_screen(device_screen);
		setDevice_version(device_version);
		setUser_ip(user_ip);
		setMap_id(map_id);
		setTarget_id(target_id);
		setTarget_type(target_type);
		setAuction_op_type(auction_op_type);
		setItem_id(item_id);
		setItem_type(item_type);
		setItem_colour(item_colour);
		setItem_lev(item_lev);
		setItem_star(item_star);
		setItem_no(item_no);
		setItem_name(item_name);
		setItem_num(item_num);
		setBid_price(bid_price);
		setFixed_price(fixed_price);
		setGuild_id(guild_id);
	}

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		if (log_id == null || log_id.equals(""))
			this.log_id = "";
		else
			this.log_id = log_id;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		if (event_time == null || event_time.equals(""))
			this.event_time = "";
		else
			this.event_time = event_time;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		if (zone_id == null || zone_id.equals(""))
			this.zone_id = "";
		else
			this.zone_id = zone_id;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		if (server_id == null || server_id.equals(""))
			this.server_id = "";
		else
			this.server_id = server_id;
	}

	public String getGame_version() {
		return game_version;
	}

	public void setGame_version(String game_version) {
		if (game_version == null || game_version.equals(""))
			this.game_version = "";
		else
			this.game_version = game_version;
	}

	public String getAccount_id() {
		return account_id;
	}

	public void setAccount_id(String account_id) {
		if (account_id == null || account_id.equals(""))
			this.account_id = "";
		else
			this.account_id = account_id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		if (role_id == null || role_id.equals(""))
			this.role_id = "";
		else
			this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		if (role_name == null || role_name.equals(""))
			this.role_name = "";
		else
			this.role_name = role_name;
	}

	public String getRole_sex() {
		return role_sex;
	}

	public void setRole_sex(String role_sex) {
		if (role_sex == null || role_sex.equals(""))
			this.role_sex = "";
		else
			this.role_sex = role_sex;
	}

	public String getRole_prof() {
		return role_prof;
	}

	public void setRole_prof(String role_prof) {
		if (role_prof == null || role_prof.equals(""))
			this.role_prof = "";
		else
			this.role_prof = role_prof;
	}

	public String getRole_level() {
		return role_level;
	}

	public void setRole_level(String role_level) {
		if (role_level == null || role_level.equals(""))
			this.role_level = "";
		else
			this.role_level = role_level;
	}

	public String getRole_vip_level() {
		return role_vip_level;
	}

	public void setRole_vip_level(String role_vip_level) {
		if (role_vip_level == null || role_vip_level.equals(""))
			this.role_vip_level = "";
		else
			this.role_vip_level = role_vip_level;
	}

	public String getRole_combat() {
		return role_combat;
	}

	public void setRole_combat(String role_combat) {
		if (role_combat == null || role_combat.equals(""))
			this.role_combat = "";
		else
			this.role_combat = role_combat;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		if (app_id == null || app_id.equals(""))
			this.app_id = "";
		else
			this.app_id = app_id;
	}

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		if (channel_id == null || channel_id.equals(""))
			this.channel_id = "";
		else
			this.channel_id = channel_id;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		if (source_id == null || source_id.equals(""))
			this.source_id = "";
		else
			this.source_id = source_id;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		if (platform == null || platform.equals(""))
			this.platform = "";
		else
			this.platform = platform;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		if (device_id == null || device_id.equals(""))
			this.device_id = "";
		else
			this.device_id = device_id;
	}

	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		if (device_name == null || device_name.equals(""))
			this.device_name = "";
		else
			this.device_name = device_name;
	}

	public String getDevice_screen() {
		return device_screen;
	}

	public void setDevice_screen(String device_screen) {
		if (device_screen == null || device_screen.equals(""))
			this.device_screen = "";
		else
			this.device_screen = device_screen;
	}

	public String getDevice_version() {
		return device_version;
	}

	public void setDevice_version(String device_version) {
		if (device_version == null || device_version.equals(""))
			this.device_version = "";
		else
			this.device_version = device_version;
	}

	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		if (user_ip == null || user_ip.equals(""))
			this.user_ip = "";
		else
			this.user_ip = user_ip;
	}

	public String getMap_id() {
		return map_id;
	}

	public void setMap_id(String map_id) {
		if (map_id == null || map_id.equals(""))
			this.map_id = "";
		else
			this.map_id = map_id;
	}

	public String getTarget_id() {
		return target_id;
	}

	public void setTarget_id(String target_id) {
		if (target_id == null || target_id.equals(""))
			this.target_id = "";
		else
			this.target_id = target_id;
	}

	public String getTarget_type() {
		return target_type;
	}

	public void setTarget_type(String target_type) {
		if (target_type == null || target_type.equals(""))
			this.target_type = "";
		else
			this.target_type = target_type;
	}

	public String getAuction_op_type() {
		return auction_op_type;
	}

	public void setAuction_op_type(String auction_op_type) {
		if (auction_op_type == null || auction_op_type.equals(""))
			this.auction_op_type = "";
		else
			this.auction_op_type = auction_op_type;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		if (item_id == null || item_id.equals(""))
			this.item_id = "";
		else
			this.item_id = item_id;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		if (item_type == null || item_type.equals(""))
			this.item_type = "";
		else
			this.item_type = item_type;
	}

	public String getItem_colour() {
		return item_colour;
	}

	public void setItem_colour(String item_colour) {
		if (item_colour == null || item_colour.equals(""))
			this.item_colour = "";
		else
			this.item_colour = item_colour;
	}

	public String getItem_lev() {
		return item_lev;
	}

	public void setItem_lev(String item_lev) {
		if (item_lev == null || item_lev.equals(""))
			this.item_lev = "";
		else
			this.item_lev = item_lev;
	}

	public String getItem_star() {
		return item_star;
	}

	public void setItem_star(String item_star) {
		if (item_star == null || item_star.equals(""))
			this.item_star = "";
		else
			this.item_star = item_star;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		if (item_no == null || item_no.equals(""))
			this.item_no = "";
		else
			this.item_no = item_no;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		if (item_name == null || item_name.equals(""))
			this.item_name = "";
		else
			this.item_name = item_name;
	}

	public String getItem_num() {
		return item_num;
	}

	public void setItem_num(String item_num) {
		if (item_num == null || item_num.equals(""))
			this.item_num = "";
		else
			this.item_num = item_num;
	}

	public String getBid_price() {
		return bid_price;
	}

	public void setBid_price(String bid_price) {
		if (bid_price == null || bid_price.equals(""))
			this.bid_price = "";
		else
			this.bid_price = bid_price;
	}

	public String getFixed_price() {
		return fixed_price;
	}

	public void setFixed_price(String fixed_price) {
		if (fixed_price == null || fixed_price.equals(""))
			this.fixed_price = "";
		else
			this.fixed_price = fixed_price;
	}

	public String getGuild_id() {
		return guild_id;
	}

	public void setGuild_id(String guild_id) {
		if (guild_id == null || guild_id.equals(""))
			this.guild_id = "";
		else
			this.guild_id = guild_id;
	}

}
