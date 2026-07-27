package com.gm.project.gmtool.hefu.entiry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库信息
 * @Auther: gouzhongliang
 * @Date: 2021/8/23 14:17
 */
public class DBInfo {

    private String ip;
    private String port;
    private String dbname;
    private String username;
    private String password;

    private Connection conn;

    private Logger log = LoggerFactory.getLogger(DBInfo.class);

    public DBInfo(String ip, String port, String dbname, String username, String password){
        this.ip = ip;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Connection getConn() throws SQLException {
        return getConnect();
    }

    @Override
    public String toString() {
        return "DBInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", dbname='" + dbname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void execute(String sql) throws SQLException{
        Statement stat = getConnect().createStatement();
        stat.execute(sql);
        conn.commit();
        stat.close();
    }

    private Connection getConnect() throws SQLException {
        if(conn == null || conn.isClosed()){
            String url = "jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?rewriteBatchedStatements=true&useServerPrepStmts=true&cachePrepStmts=true&characterEncoding=utf8";
            Connection conn = DriverManager.getConnection(url, username, password);
            conn.setAutoCommit(false);
            this.conn = conn;
        }
        return this.conn;
    }

    public List<List<String>> query(String sql, int fieldNum) throws SQLException{
        List<List<String>> data = new ArrayList<>();
        try(Statement stat = getConnect().createStatement(); ResultSet resultSet = stat.executeQuery(sql)){
            while (resultSet.next()){
                List<String> row = new ArrayList<>(fieldNum);
                for(int i = 1; i <= fieldNum; i++){
                    row.add(resultSet.getString(i));
                }
                data.add(row);
            }
        }
        return data;
    }

    public PreparedStatement createPreStat(String sql) throws SQLException{
        return getConnect().prepareStatement(sql);
    }

    public void commit() throws SQLException {
        getConnect().commit();
    }

    public void close(){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error(e.getMessage(), e);
            }
        }
        conn = null;
    }

    public void test(){
        
    }
}
