package com.game.log.db;

import game.core.util.TimeUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItemCountLogDao {

    private Logger logger = LogManager.getLogger(ItemCountLogDao.class);

    private DataSource dataSource;

    public ItemCountLogDao(DataSource dataSource){
        this.dataSource = dataSource;
    }

    public int replaceInto(ItemCountLog log){
        String buildSql = "";
        Connection connection = null;
        try
        {
            buildSql = buildSql(log);
            connection = dataSource.getConnection();
            Statement createStatement = connection.createStatement();
            int num = createStatement.executeUpdate(buildSql);
            if (logger.isDebugEnabled())
            {
                logger.debug(buildSql);
            }
            return num;
        }
        catch (SQLException e)
        {
            logger.error(buildSql,e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    logger.error("", e);
                }
            }
        }
        return 0;
    }

    public List<ItemCountLog> list(Date now) {
        String buildSql = "";
        String buildCreateTableSql = "";
        Connection connection = null;
        List<ItemCountLog> list = new ArrayList<>();
        try
        {
            buildCreateTableSql = buildCreateTableSql(TimeUtils.Time());
            buildSql = buildListSql(now);
            connection = dataSource.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.execute(buildCreateTableSql);
            ResultSet set = createStatement.executeQuery(buildSql);
            if (logger.isDebugEnabled())
            {
                logger.debug(buildSql);
            }
            while(set.next()){
                ItemCountLog log = new ItemCountLog();
                log.setDealTime(set.getLong("dealTime"));
                log.setConsume(set.getLong("consume"));
                log.setProduce(set.getLong("produce"));
                log.setDate(set.getDate("date"));
                log.setType(set.getInt("type"));
                log.setModelId(set.getInt("modelId"));
                log.setName(set.getString("name"));
                log.setServerId(set.getInt("serverId"));
                list.add(log);
            }
            return list;
        }
        catch (SQLException e)
        {
            logger.error(buildCreateTableSql,e);
        }
        finally
        {
            if (connection != null)
            {
                try
                {
                    connection.close();
                }
                catch (SQLException e)
                {
                    logger.error("", e);
                }
            }
        }
        return list;
    }

    private String buildListSql(Date now) {
        return "select * from itemcountlog where date = '" + DateFormatUtils.format(now,"yyyy-MM-dd")+"'";
    }

    public String buildCreateTableSql(long time) {
        return  "CREATE TABLE IF NOT EXISTS `itemcountlog` (" +
                "  `date` DATE NOT NULL COMMENT '日期'," +
                "  `modelId` INT NOT NULL COMMENT '物品id'," +
                "  `type` INT NOT NULL DEFAULT 0 COMMENT '物品类型'," +
                "  `name` VARCHAR(100) NULL COMMENT '物品名称'," +
                "  `produce` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '产生数'," +
                "  `consume` BIGINT(20) NOT NULL DEFAULT 0 COMMENT '消耗数'," +
                "  `dealTime` BIGINT(20) NULL COMMENT '最后更新时间'," +
                "  `serverId` INT NOT NULL DEFAULT 0 COMMENT '服务器id'," +
                "  PRIMARY KEY (`date`, `modelId`))" +
                "  ENGINE = INNODB," +
                "    CHARACTER SET utf8," +
                "    COLLATE utf8_general_ci," +
                "    COMMENT = '物品产销记录';";
    }

    public String buildSql(ItemCountLog log) {
        if (logger.isDebugEnabled()) {
            logger.debug("buildSql(ItemCountLog) - start");
        }
        StringBuilder buffer = new StringBuilder();
        buffer.append("replace INTO itemcountlog (date,modelId,type,name,produce,consume,dealTime,serverId) value (");
        buffer.append(dealValue(log.getDate())).append(",")
                .append(dealValue(log.getModelId())).append(",")
                .append(dealValue(log.getType())).append(",")
                .append(dealValue(log.getName())).append(",")
                .append(dealValue(log.getProduce())).append(",")
                .append(dealValue(log.getConsume())).append(",")
                .append(dealValue(log.getDealTime())).append(",")
                .append(dealValue(log.getServerId())).append(")");
        if (logger.isDebugEnabled()) {
            logger.debug("buildSql(ItemCountLog) - end");
        }
        return buffer.toString();
    }

    protected String dealValue(Object object) {
        if (object instanceof Date) {
            return "'" + DateFormatUtils.format((Date)object, "yyyy-MM-dd") + "'";
        }
        if (object instanceof List) {
            //暂不作集合类支持
        }
        if (object instanceof String) {
            //TODO 防注入处理
//			object=
        }
        return object == null ? "''" : "'" + object.toString() + "'";
    }
}
