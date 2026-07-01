/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gm.common.dbclient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.gm.project.gmtool.StatLogServer;
import com.sun.rowset.CachedRowSetImpl;


/**
 * 
 * @author YHYang
 */
public class DBClient {
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(StatLogServer.class);
	private DBConnectionPool connectionPool;

	public DBClient(DBServerConfig dbServerConfig) throws SQLException, ClassNotFoundException {
		this.connectionPool = new DBConnectionPool(dbServerConfig.getJdbcUrl(), dbServerConfig.getUserName(), dbServerConfig.getPassword(),dbServerConfig.isUseConnectionPool());
	}
	public int executeUpdate(String command) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		int affectedLine = 0;
		try {
			connection = this.connectionPool.getConnection();
			connection.setAutoCommit(true);
			statement = connection.createStatement();

			affectedLine = statement.executeUpdate(command);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {

				}
			}

			if (connection != null) {
				try {
					this.connectionPool.closeConnection(connection);
				} catch (Exception e) {
					logger.info(statement.toString());
				}
			}
		}
		return affectedLine;
	}

	public int executeUpdate(String command, Object... objs) throws SQLException {
		Connection connection = null;
		PreparedStatement statement = null;
		int affectedLine = 0;
		try {
			connection = this.connectionPool.getConnection();
			statement = connection.prepareStatement(command);
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] instanceof Integer)
					statement.setInt(i + 1, (Integer) objs[i]);
				if (objs[i] instanceof Boolean)
					statement.setBoolean(i + 1, (boolean) objs[i]);
				else
					statement.setString(i + 1, (objs[i] == null) ? null : objs[i].toString());
			}
			logger.info(statement.toString());
			affectedLine = statement.executeUpdate();
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {

				}
			}

			if (connection != null) {
				try {
					this.connectionPool.closeConnection(connection);
				} catch (Exception e) {

				}
			}
		}
		return affectedLine;
	}

	public CachedRowSet executeQuery(String sqlCommand) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		CachedRowSet cachedRowSet = null;
		try {
			connection = this.connectionPool.getConnection();
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlCommand);
			logger.info(sqlCommand);
			cachedRowSet = new CachedRowSetImpl();
			cachedRowSet.populate(resultSet);
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
				}
			}

			if (connection != null) {
				try {
					this.connectionPool.closeConnection(connection);
				} catch (Exception e) {
				}
			}
		}
		return cachedRowSet;
	}

	public long executeInsertAndReturnIncrementID(String command) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		long autoIncrementKey = 0;
		try {
			connection = this.connectionPool.getConnection();
			statement = connection.createStatement();
			logger.info(statement.toString());
			statement.executeUpdate(command, Statement.RETURN_GENERATED_KEYS);

			ResultSet resultSet = statement.getGeneratedKeys();

			if (resultSet.next()) {
				autoIncrementKey = resultSet.getInt(1);
			}
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (Exception e) {
				}
			}

			if (connection != null) {
				try {
					this.connectionPool.closeConnection(connection);
				} catch (Exception e) {
				}
			}
		}

		return autoIncrementKey;
	}

	public Connection getConnection() throws SQLException {
		return this.connectionPool.getConnection();
	}

	public void returnConnection(Connection connection) throws SQLException {
		this.connectionPool.closeConnection(connection);
	}
	
	public void free(ResultSet rs, PreparedStatement ps, Connection conn) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (ps != null)
			try {
				ps.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
	// 查询纪录总数
	public int qryTotalCount(String sql){
		int count = 0;

		String countSql = sql;
		CachedRowSet rs = null;
		try {
			 rs = this.executeQuery(countSql);
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			this.free(rs,null,null);
		}
		return count;
	}

	public <T> T selectOne(String sql, Class<T> clazz, Object... params) throws SQLException {

		Connection conn = this.connectionPool.getConnection();
		T results = null;
		try {

			QueryRunner qr = new QueryRunner();

			if (clazz == null) {
				results = (T) qr.query(conn, sql, new MapHandler(),params);
			} else {
				ResultSetHandler<T> rsh = new BeanHandler<T>(clazz);
				results = qr.query(conn, sql, rsh,params);
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
			//LogUtil.error(e);
		} finally {
			free(null,null,conn);
		}

		return results;
	}
	/**
	 * 查询:无反射
	 * 注：返回map
	 * */

	public <T> T selectOne(String sql){

		Connection conn = null;
		T results = null;
		try {
			conn = getConnection();
			QueryRunner qr = new QueryRunner();
			ResultSetHandler<T> rsh = (ResultSetHandler<T>) new MapHandler();
			results = qr.query(conn, sql, rsh);

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} finally {
			free(null,null,conn);

		}

		return results;
	}
	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */
	public <T> List<T> selectList(String sql,  Class<T> clazz ,Object... params) throws SQLException {

		Connection conn = null;
		List<T> results = null;
		try {
			conn = getConnection();
			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = qr.query(conn, sql, new ColumnListHandler<T>(),params);
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(conn, sql,rsh, params);
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} finally {
			free(null,null,conn);
		}

		return results;
	}

	/**
	 * 查询:有反射
	 * 注:1.基础表专用 2.T为Integer,Long,String等基础数据类型时 clazz传null
	 * */
	public <T> List<T> selectList(String sql, Class<T> clazz) throws SQLException {

		Connection conn = null;
		List<T> results = null;
		try {
			conn = getConnection();
			QueryRunner qr = new QueryRunner();
			if (clazz == null) {
				results = qr.query(conn, sql, new ColumnListHandler<T>());
			} else {
				ResultSetHandler<List<T>> rsh = new BeanListHandler<T>(clazz);
				results = qr.query(conn, sql, rsh);
			}

		} catch (SQLException e) {
			logger.error(e.toString());
			e.printStackTrace();
		} finally {
			free(null,null,conn);
		}

		return results;
	}

	public <T> List<T> selectList(String sql, Object... params) {
		Connection conn = null;
		List<T> results = null;
		try {
			conn = getConnection();

			QueryRunner qr = new QueryRunner();
			results = (List<T>) qr.query(conn, sql,new MapListHandler(), params);

		} catch (SQLException e) {
			//logger.error(e.toString());
			e.printStackTrace();
		} finally {
			free(null,null,conn);
		}

		return results;
	}

}
