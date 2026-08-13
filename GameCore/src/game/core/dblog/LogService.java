/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.dblog;

import com.alibaba.druid.pool.DruidDataSource;
import game.core.dblog.bean.BaseLogBean;
import game.core.net.Config.ServerConfig;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import game.core.dblog.db.util.DBUtils;
import game.core.dblog.db.util.TableCompar;
import game.core.dblog.base.MetaData;
import game.core.dblog.task.DBLogTask;
import game.core.dblog.task.FileLogTask;
import game.core.util.ClassUtil;
import game.core.util.TimeUtils;
import static java.lang.Thread.sleep;
import java.lang.reflect.Modifier;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.DataSource;

/**
 *
 * @author Administrator
 */
public class LogService
{
    public static final Logger logger = LogManager.getLogger(LogService.class);
    //public static final LogService instance = new LogService();
    public static final String poolName = "logdbpool";
    public static volatile boolean isFile = false; //数据库连接异常时，是否存文件

    private enum Singleton
    {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        LogService manager;

        Singleton()
        {
            this.manager = new LogService();
        }

        LogService getProcessor()
        {
            return manager;
        }
    }

    //LogService
    public static LogService getInstance()
    {
        return Singleton.INSTANCE.getProcessor();
    }

    private DruidDataSource ds;
    private ThreadPoolExecutor dbexecutor;
    private ThreadPoolExecutor fileexecutor;
    private static final int dbthreads = 20;
    private static final int filethreads = 10;
    private BlockingQueue<Runnable> dbqueue = new LinkedBlockingQueue<>();
    private BlockingQueue<Runnable> filequeue = new LinkedBlockingQueue<>();
    private final static AtomicInteger count = new AtomicInteger();
    private final static AtomicLong lostCount = new AtomicLong();

    private LogService() {
        logger.info("Инициализация сервиса базы данных логов");
        try {
//            ds = new ComboPooledDataSource();
//            ds.setDriverClass(DbServerConfig.getLogDrivers());
//            ds.setJdbcUrl(DbServerConfig.getLogUrl());
//            ds.setPassword(DbServerConfig.getLogPassword());
//            ds.setUser(DbServerConfig.getLogUser());
//            ds.setInitialPoolSize(10);
//            ds.setAcquireIncrement(10);
//            ds.setMinPoolSize(10);
//            ds.setMaxPoolSize(30);
//            ds.setMaxIdleTime(60000);
//            ds.setCheckoutTimeout(2000);
//            ds.setIdleConnectionTestPeriod(60 * 10);
//            ds.setPreferredTestQuery(DbServerConfig.getLogValidationquery());
            ds = new DruidDataSource();
            ds.setDriverClassName(ServerConfig.getLogDrivers());
            ds.setUrl(ServerConfig.getLogUrl());
            ds.setUsername(ServerConfig.getLogUser());
            ds.setPassword(ServerConfig.getLogPassword());
            ds.setPoolPreparedStatements(true);//打开游标缓存
            ds.setMaxWait(60000);
            ds.setValidationQuery(ServerConfig.getLogValidationquery());
            ds.setMinIdle(10);
            ds.setMaxActive(30);
            ds.setMaxOpenPreparedStatements(20);
            ds.setTestWhileIdle(true);
            ds.setTestOnBorrow(false);
            ds.setTestOnReturn(false);
            ds.setRemoveAbandoned(true);
            ds.setRemoveAbandonedTimeout(1800);//30分中的连接不用则关闭
            ds.setLogAbandoned(true);//记录删除日志

            logger.info("Запуск пула соединений с базой данных логов завершён: " + ServerConfig.getLogUrl());
            sleep(200);//停留一下，确定日志连接池连接成功 
            checkTable();
            dbexecutor = new ThreadPoolExecutor(10, dbthreads, 0l, TimeUnit.MILLISECONDS, dbqueue);
            fileexecutor = new ThreadPoolExecutor(5, filethreads, 0l, TimeUnit.MILLISECONDS, filequeue);
            logger.info("Запуск пула потоков для логов завершён");
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
        logger.info("Инициализация сервиса базы данных логов завершена");
    }

    public final void checkTable() {
    Connection connection = null;

    try {
        Set<Class<BaseLogBean>> subClasses =
                ClassUtil.getSubClasses("com.game", BaseLogBean.class);

        connection = ds.getConnection();

        /*
         * DBUtils.getTableName() в Linux/MariaDB возвращает реальные имена
         * таблиц, обычно в нижнем регистре.
         */
        List<String> databaseTables = DBUtils.getTableName(connection);
        Set<String> normalizedTables = new HashSet<>();

        for (String name : databaseTables) {
            if (name != null) {
                normalizedTables.add(name.toLowerCase(Locale.ROOT));
            }
        }

        long currentTimeMillis = TimeUtils.Time();

        for (Class<BaseLogBean> cls : subClasses) {
            if (Modifier.isAbstract(cls.getModifiers())) {
                continue;
            }

            try {
                BaseLogBean bean = cls.newInstance();

                /*
                 * Критически важно:
                 * одно и то же имя используется для CREATE, ALTER,
                 * чтения структуры и дальнейшего сравнения.
                 */
                String buildTableName = bean
                        .buildTableName(currentTimeMillis)
                        .toLowerCase(Locale.ROOT);

                logger.info("Проверка таблицы: " + buildTableName);

                /*
                 * Создаём минимальную таблицу, если её ещё нет.
                 * Затем TableCompar добавит поля из BaseLogBean.
                 */
                if (!normalizedTables.contains(buildTableName)) {
                    String createSql =
                            "CREATE TABLE IF NOT EXISTS `" + buildTableName + "` ("
                            + "`id` BIGINT NOT NULL AUTO_INCREMENT,"
                            + "PRIMARY KEY (`id`)"
                            + ") ENGINE=InnoDB "
                            + "DEFAULT CHARACTER SET utf8mb4 "
                            + "COLLATE utf8mb4_unicode_ci";

                    logger.info("Создание таблицы логов: " + createSql);

                    try (Statement statement = connection.createStatement()) {
                        statement.executeUpdate(createSql);
                    }

                    normalizedTables.add(buildTableName);
                }

                /*
                 * Получаем фактические поля уже созданной таблицы.
                 * Здесь также используем имя в нижнем регистре.
                 */
                List<ColumnInfo> columnDefine =
                        DBUtils.getColumnDefine(connection, buildTableName);

                Iterator<ColumnInfo> iterator = columnDefine.iterator();

                while (iterator.hasNext()) {
                    ColumnInfo next = iterator.next();

                    if (next.getName().equalsIgnoreCase("id")) {
                        iterator.remove();
                    }
                }

                /*
                 * Формируем описание таблицы по Java-классу лога.
                 */
                List<ColumnInfo> codeDefine = new ArrayList<>();
HashSet<MetaData> metaDataSet = bean.getMetadata();

/*
 * Убираем повторяющиеся колонки.
 * Сравнение выполняем без учёта регистра.
 */
Set<String> codeColumnNames = new HashSet<>();

for (MetaData md : metaDataSet) {
    ColumnInfo columnInfo = md.toColumnInfo();

    if (columnInfo == null || columnInfo.getName() == null) {
        continue;
    }

    String normalizedColumnName =
            columnInfo.getName().toLowerCase(Locale.ROOT);

    if (codeColumnNames.add(normalizedColumnName)) {
        codeDefine.add(columnInfo);
    } else {
        logger.warn(
                "Пропущена повторяющаяся колонка: "
                + buildTableName
                + "."
                + columnInfo.getName()
                + ", класс="
                + cls.getName()
        );
    }
}

                /*
                 * TableCompar теперь получает имя только в нижнем регистре,
                 * поэтому ALTER TABLE обращается к реально созданной таблице.
                 */
                List<String> comparator =
                        TableCompar.getInstance().compartor(
                                buildTableName,
                                codeDefine,
                                columnDefine
                        );

                if (!comparator.isEmpty()) {
                    try (Statement statement = connection.createStatement()) {
                        for (String sql : comparator) {
                            logger.info("Обнаружено изменение: " + sql);
                            statement.addBatch(sql);
                        }

                        statement.executeBatch();
                    }
                }

                logger.info("Проверка таблицы " + buildTableName + " завершена");

            } catch (Exception e) {
                logger.error(cls.getName() + "," + e, e);
            }
        }

    } catch (SQLException e) {
        logger.error(e, e);

    } catch (IOException e) {
        logger.error(e, e);

    } catch (ClassNotFoundException e) {
        logger.error(e, e);

    } finally {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(e, e);
            }
        }
    }
}

    public void execute(BaseLogBean bean)
    {
        int dbsize = dbexecutor.getQueue().size();
        int filesize = fileexecutor.getQueue().size();
        int file = 0;
        long lostcount = 0;
        if (dbsize <= 8 * 10000)
        {
            //写数据库 十万
            file = count.get();
            dbexecutor.submit(new DBLogTask(bean, ds));
        }
        else if (filesize <= 2 * 10000)
        {
            //写文件2万
            file = count.getAndIncrement();
            fileexecutor.submit(new FileLogTask(bean));
            if (file != 0 && file % 100 == 0)
            {
                logger.info("executor(BaseLogBean) - filelogcount" + lostcount);
            }
        }
        else
        {
            //队列太长 丢掉
            lostcount = lostCount.getAndIncrement();
            logger.error("С момента запуска потеряно " + lostcount + " записей логов");
            if (lostcount != 0 && lostcount % 1000 == 0)
            {
                logger.info("executor(BaseLogBean) - потеряно логов: " + lostcount);
            }
        }

    }

    public void executeDDL(String ddl)
    {
        try
        {
            Connection connection = ds.getConnection();
            Statement createStatement = connection.createStatement();
            createStatement.execute(ddl);
            if (logger.isDebugEnabled())
            {
                logger.info(ddl);
            }
        }
        catch (SQLException e)
        {
            logger.error(e + ":" + ddl);
            isFile = true;
        }

    }

    /**
     * 日志系统关闭 系统shutdown的时候调用这里
     */
    public void shutdown()
    {
        logger.info("Выполняется закрытие системы логирования");
        List<Runnable> fileshutdownNow = fileexecutor.shutdownNow();
        List<Runnable> dbshutdownNow = dbexecutor.shutdownNow();
        if (fileshutdownNow.size() > 0)
        {
            logger.info("Сохранение оставшихся записей в очереди файловых логов, длина очереди: " + fileshutdownNow.size());
            for (int i = 0; i < fileshutdownNow.size(); i++)
            {
                Runnable runnable = fileshutdownNow.get(i);
                runnable.run();
                logger.info("Сохранение записи файлового лога №" + (i + 1) + " из " + fileshutdownNow.size() + " завершено");
            }
        }
        if (dbshutdownNow.size() > 0)
        {
            for (int i = 0; i < dbshutdownNow.size(); i++)
            {
                Runnable task = dbshutdownNow.get(i);
                task.run();
                logger.info("Сохранение записи лога в БД №" + (i + 1) + " из " + dbshutdownNow.size() + " завершено");
            }
        }

        ds.close();
        logger.info("Служба логирования закрыта");
    }

    public void test() {
        tbllog_pvp log = new tbllog_pvp();
        log.setUserId("userid1234");
        execute(log);
    }

    public DataSource getDataSource(){
        return ds;
    }

    public static void main(String args[])
    {
        new LogService().test();
    }
}
