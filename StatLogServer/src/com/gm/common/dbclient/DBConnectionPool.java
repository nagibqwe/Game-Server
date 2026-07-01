/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gm.common.dbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.mysql.jdbc.Driver;

/**
 * 
 * @author YHYang
 */
public final class DBConnectionPool
{
	static {
		String[] drivers = new String[]{"com.mysql.cj.jdbc.Driver"};
		String[] var1 = drivers;
		int var2 = drivers.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			String driverClassName = var1[var3];

			try {
				Class.forName(driverClassName);
			} catch (Throwable var6) {
			}
		}

	}


	private int MIN_CONNECTIONS_PER_PARTITION = 2;
	private int MAX_CONNECTIONS_PER_PARTITION = 5;
	private static final int PARTITION_COUNT = 1;

	private BoneCP connectionPool;
	private String jdbcUrl;
	private String userName;
	private String password;
	private boolean isUseConnectionPool;
	public DBConnectionPool(String jdbcUrl, String userName, String password,boolean isUseConnectionPool) throws SQLException, ClassNotFoundException
	{
		this.jdbcUrl = jdbcUrl;
		this.userName = userName;
		this.password = password;
		this.isUseConnectionPool = isUseConnectionPool;
		if(isUseConnectionPool){
			this.initializeConnectionPool(jdbcUrl, userName, password);
		}
	}
	private void initializeConnectionPool(String jdbcUrl, String userName, String password) throws SQLException
	{
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setUsername(userName);
		config.setPassword(password);
		config.setMinConnectionsPerPartition(this.MIN_CONNECTIONS_PER_PARTITION);
		config.setMaxConnectionsPerPartition(this.MAX_CONNECTIONS_PER_PARTITION);
		config.setPartitionCount(PARTITION_COUNT);
		config.setIdleMaxAgeInSeconds(600);

		config.setIdleConnectionTestPeriodInSeconds(60);
		this.connectionPool = new BoneCP(config);

//		DruidDataSource dataSource = new DruidDataSource();
//		dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//		dataSource.setUrl(jdbcUrl);
//		dataSource.setUsername(userName);
//		dataSource.setPassword(password);
//		dataSource.setTestWhileIdle(true);
//		dataSource.setInitialSize(5);
//		dataSource.setMaxActive(15);
//	//	dataSource.setMaxIdle(10);
//		dataSource.setMinIdle(5);
//		dataSource.setMaxWait(3000);
//		dataSource.setTimeBetweenEvictionRunsMillis(27000);
//		dataSource.setMinEvictableIdleTimeMillis(30000);
//		dataSource.setLogAbandoned(true);
//		dataSource.setRemoveAbandoned(false);
//		dataSource.setRemoveAbandonedTimeout(600);
//		dataSource.setValidationQuery("SELECT NOW() FROM DUAL");
//		this.connectionPool = dataSource;
	}

	public Connection getConnection() throws SQLException
	{
		 Connection	connection = null;
		if(this.isUseConnectionPool){
			connection = this.connectionPool.getConnection();
		}else {
			if(connection == null){

				connection = DriverManager.getConnection(jdbcUrl, userName, password);
			}

		}
		return connection;
	}

	public void closeConnection(Connection connection) throws SQLException
	{
		connection.close();
	}
}
