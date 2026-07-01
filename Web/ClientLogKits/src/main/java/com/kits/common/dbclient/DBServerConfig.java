package com.kits.common.dbclient;

public class DBServerConfig
{
	private String jdbcUrl = null;
	private String userName = null;
	private String password = null;
	private String dataSourceName = null;
	private boolean isUseConnectionPool;
	public String getJdbcUrl()
	{
		return jdbcUrl;
	}
	public void setJdbcUrl(String jdbcUrl)
	{
		this.jdbcUrl = jdbcUrl;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}

	public boolean isUseConnectionPool() {
		return isUseConnectionPool;
	}

	public void setUseConnectionPool(boolean useConnectionPool) {
		isUseConnectionPool = useConnectionPool;
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
}
