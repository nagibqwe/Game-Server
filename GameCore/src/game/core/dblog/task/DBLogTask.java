package game.core.dblog.task;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicInteger;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.core.dblog.bean.BaseLogBean;
import game.core.util.TimeUtils;

/**
 */
public class DBLogTask implements Runnable
{

    /**
     * Logger for this class
     */
    private static final Logger logger = LogManager.getLogger(DBLogTask.class);
    public final static AtomicInteger count = new AtomicInteger();
    private DataSource ds;
    private BaseLogBean bean;

    public DBLogTask(BaseLogBean bean, DataSource ds)
    {
        this.ds = ds;
        this.bean = bean;
    }

    @Override
    public void run()
    {
        String buildSql = "";
        String buildCreateTableSql = "";
        Connection connection = null;
        try
        {
            buildCreateTableSql = bean.buildCreateTableSql(TimeUtils.Time());
            buildSql = bean.buildSql();
            connection = ds.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.execute(buildCreateTableSql);
            createStatement.executeUpdate(buildSql);

            if (logger.isDebugEnabled())
            {
                logger.debug(buildSql);
            }
            count.getAndIncrement();
        }
        catch (SQLException e)
        {
            logger.error(buildCreateTableSql);
            logger.error(e,e);
            bean.logToFile();
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
                    logger.error(e, e);
                }
            }
        }
    }

    public BaseLogBean getBean()
    {
        return bean;
    }

    public void setBean(BaseLogBean bean)
    {
        this.bean = bean;
    }

}
