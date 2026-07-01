package common.bi;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Item_Bean;
import com.data.bean.Cfg_State_power_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.bi4399.*;
import com.game.bi.inter.IBi4399Script;
import com.game.bi.manager.BI4399DictDefine;
import com.game.chat.Manager.ChatManager;
import com.game.count.structs.VariantType;
import com.game.db.DBErrorToFile;
import com.game.db.bean.ShopBean;
import com.game.equip.struct.EquipDefine;
import com.game.guild.structs.Guild;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.QuitGameDefine;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.shop.structs.ShopDefine;
import game.core.dblog.ColumnInfo;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @explain: desc
 * @time Created on 2020/3/26 20:40.
 * @author: tc
 */
public class Bi4399Script implements IBi4399Script {

	private final Logger bi4399Log = LogManager.getLogger("BI4399Log");

	private final Logger biNetLog = LogManager.getLogger("BILog4");

	/**
	 * 获取scriptId
	 *
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.BI4399Script;
	}

	/**
	 * 调用脚本
	 *
	 * @param args 参数
	 * @return
	 */
	@Override
	public Object call(Object... args) {
		return null;
	}

	/**
	 * 创建角色
	 *
	 * @param player
	 */
	@Override
	public void createPlayer(Player player) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
		tbllog_player tbllogPlayer = new tbllog_player(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
				player.getBi().getOs(),
				player.getId(),
				player.getName(),
				player.getBi().getCpUserId(), // account_name
				player.getBi().getCpUserName(), // user_name
				"",
				player.getCareer(),
				player.getSex(),
				player.getCreateTime(),
				player.getLoginIP(),
				player.getBi().getCpdid(),
				player.getLevel(),
				player.getVipLv(),
				player.getStateVip().getLv(),
				player.getCurrencys().get(ItemCoinType.EXP),
				player.gainGuildName(),
				player.getFightPoint(),
				(int) player.getCurrencys().get(ItemCoinType.GemCoin),
				(int) player.getCurrencys().get(ItemCoinType.BindGemCoin),
				(int) player.getCurrencys().get(ItemCoinType.GoldCoin),
				0,
                (int) (Manager.countManager.getVariant(player, VariantType.RechargeMoney) / 100),
                (int) (player.getFirstRechargeTime() / 1000),
                (int) (player.getLastRechargeTime() / 1000),
				(int) (player.getLastLoginTime() / 1000),
				(int) (TimeUtils.Time() / 1000)
		);
		RoleUpdateLogService.getInstance().createTblLogPlayer(tbllogPlayer);
	}

	/**
	 * 更新角色
	 *
	 * @param player
	 */
	@Override
	public void updatePlayer(Player player) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;

		long point = 0;
		if (player.isOnline()) {
			point = player.getFightPoint();
		} else {
			PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
			if (playerWorldInfo != null) {
				point = playerWorldInfo.getFightPower();
			}
		}
		tbllog_player tbllogPlayer = new tbllog_player(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
				player.getBi().getOs(),
				player.getId(),
				player.getName(),
				player.getBi().getCpUserId(), // account_name
				player.getBi().getCpUserName(), // user_name
				"",
				player.getCareer(),
				player.getSex(),
				player.getCreateTime(),
				player.getLoginIP(),
				player.getBi().getCpdid(),
				player.getLevel(),
				player.getVipLv(),
				player.getStateVip().getLv(),
				player.getCurrencys().get(ItemCoinType.EXP),
				player.gainGuildName(),
				point,
				(int) player.getCurrencys().get(ItemCoinType.GemCoin),
				(int) player.getCurrencys().get(ItemCoinType.BindGemCoin),
				(int) player.getCurrencys().get(ItemCoinType.GoldCoin),
				0,
                (int) (Manager.countManager.getVariant(player, VariantType.RechargeMoney) / 100),
                (int) (player.getFirstRechargeTime() / 1000),
                (int) (player.getLastRechargeTime() / 1000),
				(int) (player.getLastLoginTime() / 1000),
				(int) (TimeUtils.Time() / 1000)
		);
		RoleUpdateLogService.getInstance().updateTblLogPlayer(tbllogPlayer);
	}

	@Override
	public void createGuild(Player player, Guild guild) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
	    tbllog_guild guildLog = new tbllog_guild(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            guild.getId(),
            guild.getName(),
            guild.getLevel(),
            0,
            0,
            guild.getMembers().size(),
            guild.getChairMan().getId(),
            guild.getChairMan().getPlayerWorldInfo().getRolename(),
            guild.getChairMan().gainPower(),
            guild.getChairMan().getPlayerWorldInfo().getStateVip(),
            guild.getNotice(),
            guild.gainGuildPower(),
				(int)guild.getExp(),
            guild.getCreateTime()
        );
        RoleUpdateLogService.getInstance().createTblLogGuild(guildLog);
    }

    @Override
    public void updateGuild(Guild guild) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
	    Player player = Manager.playerManager.getPlayer(guild.getChairMan().getId());
	    if (player == null) {
	        return;
        }
        tbllog_guild guildLog = new tbllog_guild(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            guild.getId(),
            guild.getName(),
            guild.getLevel(),
            0,
            0,
            guild.getMembers().size(),
            guild.getChairMan().getId(),
            guild.getChairMan().getPlayerWorldInfo().getRolename(),
            guild.getChairMan().gainPower(),
            guild.getChairMan().getPlayerWorldInfo().getStateVip(),
            guild.getNotice(),
            guild.gainGuildPower(),
				(int)guild.getExp(),
            guild.getCreateTime()
        );
        RoleUpdateLogService.getInstance().updateTblLogGuild(guildLog);
    }

	public void chatBiTo4399(Player player, int channel, String msg, int type, long target_role_id) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
        tbllog_chat chatLog = new tbllog_chat(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),
			player.getBi().getCpUserId(), // account_name
			player.getId(),
            player.getName(),
            player.getLevel(),
            player.getLoginIP(),
            player.getBi().getCpdid(),
            channel,
            splitMsg(msg, new ArrayList<>()),
            type,
            target_role_id,
            (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(chatLog);
    }

    public void goldBiTo4399(Player player, int money_type, int amount, int money_remain, int opt, int action_1) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
        tbllog_gold goldLog = new tbllog_gold(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            player.getId(),
			player.getBi().getCpUserId(), // account_name
            player.getLevel(),
            player.getCareer(),
            money_type,
            amount,
            money_remain,
            0,
            opt,
            action_1,
            action_1,
            0,
            (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(goldLog);
    }

    public void loginBiTo4399(Player player) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
        tbllog_login loginLog = new tbllog_login(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            player.getId(),
			player.getBi().getCpUserId(), // account_name
            player.getLevel(),
            player.getLoginIP(),
            player.gainMapModelId(),
				player.getBi().getCpdid(),
            player.getBi().getApp_version(),
            player.getBi().getOs(),
            player.getBi().getOs_version(),
            player.getBi().getCpdevice_name(),
            player.getBi().getScreen(),
            player.getBi().getMerchant(),
            player.getBi().getNet_type(),
            (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(loginLog);
    }

    public void onlineBiTo4399() {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
        Map<String, OnlineInfo> onlineMap = new HashMap<>();
        String platform, device, key;
        for (Player player : Manager.playerManager.getOnLines()) {
            platform = 	player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId();
            device = player.getBi().getOs();
            key = platform + "&" + device;
            OnlineInfo onlineInfo = onlineMap.get(key);
            if (onlineInfo == null) {
                onlineInfo = new OnlineInfo();
                onlineInfo.setPlatform(platform);
                onlineInfo.setDevice(device);
                onlineMap.put(key, onlineInfo);
            }
            onlineInfo.getRoleIds().add(player.getId());
            onlineInfo.getDevices().add(player.getBi().getCpdid());
            onlineInfo.getIps().add(player.getLoginIP());
        }
        int now = (int) (TimeUtils.Time() / 1000);
        for (OnlineInfo info : onlineMap.values()) {
            tbllog_online onlineLog = new tbllog_online(
                    info.getPlatform(),
                    info.getDevice(),
                    info.getRoleIds().size(),
                    info.getDevices().size(),
                    info.getIps().size(),
                    now
            );
            LogService.getInstance().execute(onlineLog);
        }
    }

    public void payBiTo4399(Player player, int pay_type, String order_id, float pay_money, int pay_gold) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
	    tbllog_pay payLog = new tbllog_pay(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            player.getId(),
			player.getBi().getCpUserId(), // account_name
            player.getLoginIP(),
            player.getLevel(),
            pay_type,
            order_id,
            pay_money / 100.0f,
            pay_gold,
				player.getBi().getCpdid(),
            player.getBi().getApp_version(),
            (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(payLog);
    }

    public void quitBiTo4399(Player player, int reason, String msg) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
        int now = (int) (TimeUtils.Time() / 1000);
	    tbllog_quit quitLog = new tbllog_quit(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            player.getId(),
			player.getBi().getCpUserId(), // account_name
            player.getLastLoginLevel(),
            player.getLevel(),
            player.getLoginIP(),
            (int) (player.getLastLoginTime() / 1000),
            now,
            (int) (now - player.getLastLoginTime() / 1000),
            player.gainMapModelId(),
            reason,
            msg,
				player.getBi().getCpdid(),
            player.getBi().getApp_version()
        );
        LogService.getInstance().execute(quitLog);
    }

    public void roleBiTo4399(Player player) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
	    tbllog_role roleLog = new tbllog_role(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
            player.getBi().getOs(),//device
            player.getId(),
            player.getName(),
			player.getBi().getCpUserId(), // account_name
            player.getLoginIP(),
            player.getCareer(),
            player.getSex(),
            player.getBi().getCpdid(),
            player.getBi().getApp_version(),
            (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(roleLog);
    }

 	@Override
	public void levelupBiTo4399(Player player,int oldLevel,long oldExp,long curExp){
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;

		tbllog_level_up level_upLog = new  tbllog_level_up(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
				player.getBi().getOs(),//device
				player.getId(),
				player.getBi().getCpUserId(), // account_name
				player.getName(),
				oldLevel,
				player.getLevel(),
				oldExp,
				curExp,
				(int) (TimeUtils.Time() / 1000)
				);
		LogService.getInstance().execute(level_upLog);
	}

 	@Override
    public void shopBuyTo4399(Player player, ShopBean shop, int num, int money) {
        if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
            return;
        tbllog_shop shopLog = new tbllog_shop(
                player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
                player.getBi().getOs(),//device
                player.getId(),
                player.getBi().getCpUserId(), // account_name
                shop.getShopid(),
                player.getLevel(),
                player.getCareer(),
                shop.getCurrencyid(),
                money,
                shop.getShopid(),
                shop.getLabelid(),
                shop.getItemid(),
                num,
                (int) (TimeUtils.Time() / 1000)
        );
        LogService.getInstance().execute(shopLog);
    }


	@Override
	public void itemBiTo4399(Player player, int action_id, int item_id, int oldNum, int afterNum) {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
			return;
		int opt;
		if (afterNum > oldNum) {
			opt = 1;
		} else if (afterNum < oldNum) {
			opt = -1;
		} else {
			opt = 0;
		}
		int item_num = Math.abs(afterNum - oldNum);
		tbllog_items itemsLog = new tbllog_items(
				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
				player.getBi().getOs(),//device
				player.getId(),
				player.getBi().getCpUserId(), // account_name
				player.getLevel(),
				opt,
				action_id,
				item_id,
				item_num,
				0,
				(int) (TimeUtils.Time() / 1000)
		);
		LogService.getInstance().execute(itemsLog);
	}

	@Override
	public void createDictTo4399() {
		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog()) {
			return;
		}

		Connection connection = null;
		try {
			connection = RoleUpdateLogService.getInstance().getDs().getConnection();
			for (String tableName : BI4399DictDefine.tables) {
				if (tableName.equals(BI4399DictDefine.dict_value_type)) {
					dictValue(connection, BI4399DictDefine.dict_value_type);
				} else if (tableName.equals(BI4399DictDefine.dict_item)) {
					dictItem(connection, BI4399DictDefine.dict_item);
				} else if (tableName.equals(BI4399DictDefine.dict_prof)) {
					dictProf(connection, BI4399DictDefine.dict_prof);
				} else if (tableName.equals(BI4399DictDefine.dict_color)) {
					dictColor(connection, BI4399DictDefine.dict_color);
				} else if (tableName.equals(BI4399DictDefine.dict_chat_channel)) {
					dictChatChannel(connection, BI4399DictDefine.dict_chat_channel);
				} else if (tableName.equals(BI4399DictDefine.dict_grade)) {
					dictGrade(connection, BI4399DictDefine.dict_grade);
				} else if (tableName.equals(BI4399DictDefine.dict_reason)) {
					dictReason(connection, BI4399DictDefine.dict_reason);
				} else if (tableName.equals(BI4399DictDefine.dict_shop_type)) {
					dictShopType(connection, BI4399DictDefine.dict_shop_type);
				} else if (tableName.equals(BI4399DictDefine.dict_action)) {
					dictAction(connection, BI4399DictDefine.dict_action);
				}
			}
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					DBErrorToFile.error(e, e);
				}
			}
		}
	}

	public void chatInfoTo4399(Player player, int channelId, String toPlatuserId, String msg, List<String> params) {
//		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog()) {
//			return;
//		}
//		List<String> list = new ArrayList<>();
//		list.add("mhfwz");
//		list.add(ServerConfig.getServerId() + "");
//		list.add(player.getBi().getCpUserId());
//		list.add(player.getName());
//		list.add(player.getLoginIP());
//		list.add(player.getBi().getCpdid());
//
//		StringBuffer sb = new StringBuffer();
//		for (int i = 0; i < list.size(); i++) {
//			if (i == (list.size() - 1)) {
//				sb.append(list.get(i));
//			} else {
//				sb.append(list.get(i)).append("|");
//			}
//		}
//
//		sb.append("<<<").append(player.getId()).append("<<<");
//		sb.append(channelId).append("/").append(toPlatuserId).append(">>>");
//		sb.append(splitMsg(msg, params)).append("\r\n");
//
//		if (ServerConfig.getBi4399Config().getBi4399OpenNet() == 0)
//			return;
//
//		if (ServerConfig.getBi4399Config().getBi4399OpenNet() == 1) {
//			bi4399Log.info(sb.toString());
//			return;
//		}
//
//		if (ServerConfig.getBi4399Config().getBi4399OpenNet() == 2) {
//			biNetLog.info(sb.toString());
//			return;
//		}
//
//		biNetLog.info(sb.toString());
//		bi4399Log.info(sb.toString());
	}

	private void dictValue(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("value_type_id", "tinyint", 4, true, "", "值类型ID"));
			parm.add(ColumnInfo.createColumnInfo("value_type_name", "varchar", 200, true, "", "值类型名称"));
			parm.add(ColumnInfo.createColumnInfo("stores", "tinyint", 4, true, "", "是否在运营支撑内展示，可不填，默认均为0"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));
			for (int i = 1; i < ItemCoinType.ItemCoinMaxId; i++) {
				Cfg_Item_Bean bean = CfgManager.getCfg_Item_Container().getValueByKey(i);
				if (bean != null) {
					int temp = 1;
					insertStmt.setInt(temp++, i);
					insertStmt.setString(temp++, bean.getName());
					insertStmt.setInt(temp++, 0);
					insertStmt.addBatch();
				}
			}
			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictItem(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("item_id", "int", 11, true, "", "道具ID"));
			parm.add(ColumnInfo.createColumnInfo("item_name", "varchar", 200, true, "", "道具名称"));
			parm.add(ColumnInfo.createColumnInfo("quality", "int", 11, true, "", "品质"));
			parm.add(ColumnInfo.createColumnInfo("level_req", "int", 11, true, "", "使用等级要求"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));
			for (Cfg_Item_Bean bean : CfgManager.getCfg_Item_Container().getValuees()) {
				int temp = 1;
				insertStmt.setInt(temp++, bean.getId());
				insertStmt.setString(temp++, bean.getName());
				insertStmt.setInt(temp++, bean.getColor());
				insertStmt.setInt(temp++, bean.getLevel());
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictProf(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("prof_id", "int", 11, true, "", "职业id"));
			parm.add(ColumnInfo.createColumnInfo("prof_name", "varchar", 10, true, "", "职业名称"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));
			for (int i = 0; i < PlayerDefine.CAREER_DESC.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, PlayerDefine.CAREER_DESC[i]);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictColor(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("color_id", "tinyint", 4, true, "", "颜色id"));
			parm.add(ColumnInfo.createColumnInfo("color_name", "varchar", 10, true, "", "颜色名"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));
			for (int i = 1; i <= EquipDefine.QualityDesc.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, EquipDefine.QualityDesc[i - 1]);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictChatChannel(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("channel_id", "int", 11, true, "", "频道id"));
			parm.add(ColumnInfo.createColumnInfo("channel_name", "varchar", 10, true, "", "频道名称"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));
			for (int i = 0; i < ChatManager.CHATCHANNEL_DESC.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, ChatManager.CHATCHANNEL_DESC[i]);
				insertStmt.addBatch();
			}

			for (int i = 0; i < ChatManager.OTHERCHANNEL_DESC.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i + 100);
				insertStmt.setString(temp++, ChatManager.OTHERCHANNEL_DESC[i]);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictGrade(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("grade_id", "int", 11, true, "", "段位ID"));
			parm.add(ColumnInfo.createColumnInfo("grade_name", "varchar", 10, true, "", "段位名称"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));

			for (Cfg_State_power_Bean bean : CfgManager.getCfg_State_power_Container().getValuees()) {
				int temp = 1;
				insertStmt.setInt(temp++, bean.getId());
				insertStmt.setString(temp++, bean.getName());
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictReason(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("reason_id", "int", 11, true, "", "退出原因ID"));
			parm.add(ColumnInfo.createColumnInfo("msg", "varchar", 20, true, "", "退出原因描述"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));

			for (int i = 0; i < QuitGameDefine.QuitGameDesc.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, QuitGameDefine.QuitGameDesc[i]);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictShopType(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("shop_type_id", "tinyint", 4, true, "", "商城类型ID"));
			parm.add(ColumnInfo.createColumnInfo("shop_type_name", "varchar", 10, true, "", "商城类型名称"));
			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));

			for (int i = 1; i <= ShopDefine.ShopDesc.length; i++) {
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, ShopDefine.ShopDesc[i - 1]);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private void dictAction(Connection connection, String tableName) {
		try {
			Statement createStatement = connection.createStatement();
			createStatement.execute(deleteTable(tableName));
			List<ColumnInfo> parm = new ArrayList<>();
			parm.add(ColumnInfo.createColumnInfo("action_id", "int", 11, true, "", "行为ID"));
			parm.add(ColumnInfo.createColumnInfo("action_name", "varchar", 50, true, "", "行为名称"));
			parm.add(ColumnInfo.createColumnInfo("action_type_id", "int", 11, true, "", "行为类型ID"));
			parm.add(ColumnInfo.createColumnInfo("level_req", "int", 11, true, "", "等级要求"));

			createStatement.execute(buildTable(tableName, parm));
			PreparedStatement insertStmt = connection.prepareStatement(buildInsert(tableName, parm));

			int i = 0;
			for (String str : ItemChangeReason.AllDescString.getValue()) {
				i++;
				int temp = 1;
				insertStmt.setInt(temp++, i);
				insertStmt.setString(temp++, str);
				insertStmt.setInt(temp++, 0);
				insertStmt.setInt(temp++, 0);
				insertStmt.addBatch();
			}

			insertStmt.executeBatch();
		} catch (Exception e) {
			DBErrorToFile.error(e, e);
		}
	}

	private String deleteTable(String tableName) {
		StringBuilder ddl = new StringBuilder();
		ddl.append("DROP TABLE IF EXISTS `").append(tableName).append("`;");
		return ddl.toString();
	}

	private String buildTable(String tableName, List<ColumnInfo> buildFields) {
		StringBuilder ddl = new StringBuilder();
		ddl.append("CREATE TABLE `").append(tableName).append("` (");
		for (ColumnInfo info : buildFields) {
			ddl.append("\r\n").append(info.toDDL()).append(",");
		}
		String str = ddl.substring(0, ddl.length() - 1);
		str = str + "\r\n" + ") ENGINE=MyISAM DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;";
		return str;
	}

	private String buildInsert(String tableName, List<ColumnInfo> buildFields) {
		StringBuilder ddl = new StringBuilder();
		ddl.append("insert into ").append(tableName).append(" set");
		for (ColumnInfo info : buildFields) {
			ddl.append("\r\n `").append(info.getName()).append("`=?,");
		}
		return ddl.substring(0, ddl.length() - 1);
	}

	private String splitMsg(String msg, List<String> params) {
		String specStr = "<t=0>";
		String specStr1 = "</t>";
		StringBuilder sb = new StringBuilder();

		if (!msg.contains(specStr1)) {
			if (params.size() != 0) {
				sb.append("--");
				for (String str : params) {
					sb.append(str).append("--");
				}
			}
			return sb.append(msg).toString();
		}
		String[] strs = msg.split(specStr1);
		for (int i = 0; i < strs.length; i++) {
			if (strs[i].length() <= specStr.length()) {
				continue;
			}

			if (strs[i].substring(0, 5).equals(specStr)) {
				String temp = strs[i].substring(5);
				int i1 = temp.indexOf(',');
				int i2 = temp.indexOf(',', i1 + 1);
				temp = temp.substring(i2 + 1, temp.length() - 1);
				sb.append(temp);
			}
		}
		return sb.toString();
	}

//	public void pvpTo4399(Player player, int action_type, int pvp_type) {
//		if (!ServerConfig.getBi4399Config().isOpen4399SqlLog())
//			return;
//		tbllog_pvp pvpLog = new tbllog_pvp(
//				player.getBi().getCpplatformId() + "_" + player.getBi().getCpgameId(),
//				player.getBi().getOs(),//device
//				player.getId(),
//				player.getBi().getCpUserId(), // account_name
//				player.getLevel(),
//				player.getCareer(),
//				pvp_type,
//				pvp_id,
//				continuous,
//				begin_time,
//				end_time,
//				time_duration,
//				dim_power,
//				game_id,
//				status,
//				result,
//				(int) (TimeUtils.Time() / 1000)
//		);
//		LogService.getInstance().execute(pvpLog);
//	}
}
