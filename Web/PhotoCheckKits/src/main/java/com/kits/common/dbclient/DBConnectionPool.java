/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kits.common.dbclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.kits.common.utils.spring.SpringUtils;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.framework.datasource.DynamicDataSource;
import com.mysql.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 *
 * @author YHYang
 */
public final class DBConnectionPool
{
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private int MIN_CONNECTIONS_PER_PARTITION = 5;
	private int MAX_CONNECTIONS_PER_PARTITION = 20;
	private static final int PARTITION_COUNT = 1;

	//private BoneCP connectionPool;

	public DBConnectionPool(String jdbcUrl, String userName, String password, int maxConnections, int minConnections) throws SQLException,
			ClassNotFoundException
	{
		this.MAX_CONNECTIONS_PER_PARTITION = maxConnections;
		this.MIN_CONNECTIONS_PER_PARTITION = minConnections;
		this.loadDriver();
		this.initializeConnectionPool(jdbcUrl, userName, password);
	}
	private String jdbcUrl;
	private String userName;
	private String password;
	private String dataSourceName;
	private boolean isUseConnectionPool;
	public DBConnectionPool(String jdbcUrl, String userName, String password,String dataSourceName,boolean isUseConnectionPool) throws SQLException, ClassNotFoundException
	{
		this.jdbcUrl = jdbcUrl;
		this.userName = userName;
		this.password = password;
		this.dataSourceName = dataSourceName;
		this.isUseConnectionPool = isUseConnectionPool;
		if(isUseConnectionPool){
			this.loadDriver();
			this.initializeConnectionPool(jdbcUrl, userName, password);
		}
	}

	private void loadDriver() throws ClassNotFoundException
	{
		Class.forName("com.mysql.jdbc.Driver");
	}
	private DataSource connectionPool = null;
	private void initializeConnectionPool(String jdbcUrl, String userName, String password) throws SQLException
	{
		DynamicDataSource dynamicDataSource = (DynamicDataSource) SpringUtils.getBean("dynamicDataSource");
		Map<Object, DataSource> dataSourceMap =  dynamicDataSource.getResolvedDataSources();
		if(dataSourceMap.containsKey(dataSourceName)){
			DataSource dataSource = dataSourceMap.get(dataSourceName);
			this.connectionPool = dataSource;
		}else
		{
			logger.error("连接池不存在-->"+dataSourceName);
		}
	}

	public Connection getConnection() throws SQLException
	{
		Connection	connection = null;
		if(this.isUseConnectionPool){
			connection = this.connectionPool.getConnection();
		}else {
			DriverManager.registerDriver(new Driver());
			connection = DriverManager.getConnection(jdbcUrl, userName, password);
		}
		return connection;
	}

	public void closeConnection(Connection connection) throws SQLException
	{
		connection.close();
	}

	public String getDataSourceName() {
		return dataSourceName;
	}

	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
}
