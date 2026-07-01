package game.core.dblog.db.util;

import game.core.dblog.ColumnInfo;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author 赵聪慧
 * @2012-10-23 下午3:58:48
 */
public class DBUtils
{
    /**
     * 执行SQL
     *
     * @param conn
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int execture(Connection conn, String sql) throws SQLException
    {
        Statement stmt;
        if (conn == null)
            throw new SQLException("Null connection");
        if (sql == null)
        {
            throw new SQLException("Null SQL statement");
        }
        stmt = conn.createStatement();
        return stmt.executeUpdate(sql);

    }

    public static List<String> getTableName(Connection conn) throws SQLException
    {
        ResultSet tableRet = conn.getMetaData().getTables(null, null, null, null);
        List<String> tablenames = new ArrayList<>();
        while (tableRet.next())
        {
            tablenames.add(tableRet.getString("TABLE_NAME").toLowerCase());
        }
        return tablenames;
    }

    public static List<ColumnInfo> getColumnDefine(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet columns = metaData.getColumns(null, "%", tableName, "%");
        ResultSet primaryKey = metaData.getPrimaryKeys(null, "%", tableName);
        String keyName = null;
        while (primaryKey.next()) {
            keyName = primaryKey.getString(4);
        }
        List<ColumnInfo> infos = new ArrayList<>();
        while (columns.next()) {
            ColumnInfo info = new ColumnInfo();
            info.setName(columns.getString("COLUMN_NAME").toLowerCase());
            info.setType(columns.getString("TYPE_NAME").toLowerCase());
            info.setSize(columns.getInt("COLUMN_SIZE"));
            info.setNullable(columns.getBoolean("IS_NULLABLE"));
            info.setPrimary(keyName);
            infos.add(info);
        }
        return infos;
    }
}
