package com.kits.common.dbclient;

import java.util.HashMap;
import java.util.Map;

public class DBCluster
{
	private DBServerConfig managerDB;
	private DBServerConfig accountDB;
	private DBServerConfig purchaseDB;

	// areaId --- gameDB
	private Map<Integer, DBServerConfig> gameDBMap = new HashMap<>();
	private Map<Integer, DBServerConfig> logDBMap = new HashMap<>();

	// 与服务器同步时间
	private long syc_with_server_time;

	public DBCluster()
	{
		this.syc_with_server_time = System.currentTimeMillis();
	}

	public long getSyctTime()
	{
		return syc_with_server_time;
	}

	public void setSycTime(long sycTime)
	{
		this.syc_with_server_time = sycTime;
	}

	public void putGameDb(int areaId, DBServerConfig config)
	{
		gameDBMap.put(areaId, config);
	}

	public void putLogDb(int areaId, DBServerConfig config)
	{
		logDBMap.put(areaId, config);
	}

	public DBServerConfig getGameDB(int areaId)
	{
		return gameDBMap.get(areaId);
	}

	public DBServerConfig getLogDB(int areaId)
	{
		return logDBMap.get(areaId);
	}

	public DBServerConfig getManagerDB()
	{
		return managerDB;
	}
	public void setManagerDB(DBServerConfig managerDB)
	{
		this.managerDB = managerDB;
	}
	public DBServerConfig getAccountDB()
	{
		return accountDB;
	}
	public void setAccountDB(DBServerConfig accountDB)
	{
		this.accountDB = accountDB;
	}
	public DBServerConfig getPurchaseDB()
	{
		return purchaseDB;
	}
	public void setPurchaseDB(DBServerConfig purchaseDB)
	{
		this.purchaseDB = purchaseDB;
	}
	public Map<Integer, DBServerConfig> getGameDBMap()
	{
		return gameDBMap;
	}
	public void setGameDBMap(Map<Integer, DBServerConfig> gameDBMap)
	{
		this.gameDBMap = gameDBMap;
	}
	public Map<Integer, DBServerConfig> getLogDBMap()
	{
		return logDBMap;
	}
	public void setLogDBMap(Map<Integer, DBServerConfig> logDBMap)
	{
		this.logDBMap = logDBMap;
	}

}
