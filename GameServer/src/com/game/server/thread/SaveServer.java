package com.game.server.thread;

import com.game.server.DbSqlName;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import game.core.util.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import game.core.db.BaseBean;
import game.core.db.BaseDao;

public class SaveServer extends Thread {

    /**
     * 插入
     */
    public static final int INSERT = 1;
    /**
     * 保存
     */
    public static final int UPDATE = 2;
    /**
     * 插入或保存
     */
    public static final int MERGE = 3;
    /**
     * 删除
     */
    public static final int DELETE = 4;
    // 日志
    private static Logger log = LogManager.getLogger(SaveServer.class);
    // 失败日志
    private static Logger failedlog = LogManager.getLogger("SAVEFAILED");
    // 待保存队列
    private final LinkedBlockingQueue<SaveBean> beans = new LinkedBlockingQueue<>();
    // 保存Dao
    private final ConcurrentHashMap<String, BaseDao> daos = new ConcurrentHashMap<>();
    // 运行标志
    private boolean stop;
    // 插入数据库
    boolean insertDB = true;
    // 最大缓存数量
    private static int MAX_SIZE = 100000;
    // 最大错误次数
    private static int MAX_ERRORTIMES = 2;

    public SaveServer() {
        super();
    }

    public SaveServer(ThreadGroup threadGroup, String threadName) {
        super(threadGroup, threadName);
    }

    /**
     * 注册保存Dao
     *
     * @param clazz
     * @param dao
     */
    public void register(Class<? extends BaseBean> clazz, BaseDao dao) {
        daos.put(clazz.getSimpleName(), dao);
    }

    @Override
    public void run() {
        this.stop = false;
        while (!stop || beans.size() > 0) {
            SaveBean bean = null;
            try {
                bean = beans.take();
            } catch (InterruptedException e) {
                log.error(e, e);
            }
            try {
                insertDB = beans.size() <= MAX_SIZE;

                if (insertDB) {
                    deal(bean);
                } else {
                    failedlog.error("队列长度：" + beans.size() + "MAX_SIZE:" + MAX_SIZE + "beanName:" + bean.sqlName);
                    failedlog.error(JsonUtils.toJSONString(bean));
                }
            } catch (Exception e) {
                log.error(e, e);
                if (null == bean) {
                    continue;
                }

                bean.setErrorTimes(bean.getErrorTimes() + 1);
                if (bean.getErrorTimes() < MAX_ERRORTIMES) {
                    beans.add(bean);
                }
            }
        }
    }

    /**
     * 停止保存处理
     *
     * @param flag
     */
    public void stop(boolean flag) {
        stop = flag;
    }

    /**
     * 放入待处理数据
     *
     * @param bean
     * @param sqlName
     * @param dealType 1-insert 2-update 4-delete
     */
    public void deal(BaseBean bean, DbSqlName sqlName, int dealType) {
        bean.setDealTime(System.currentTimeMillis());
        beans.add(new SaveBean(bean, sqlName, dealType));
    }

    public int getQueueSize() {
        return beans.size();
    }

    /**
     * 处理数据
     */
    private void deal(SaveBean saveBean) throws Exception {
        BaseBean bean = saveBean.getBean();
        DbSqlName sqlName = saveBean.getSqlName();
        BaseDao dao = daos.get(bean.getClass().getSimpleName());
        if (dao == null) {
            log.error("数据保存错误(未找到对应的dao), Dao名字:" + bean.getClass().getSimpleName() + "," + JsonUtils.toJSONString(bean));
            throw new Exception();
        }

        switch (saveBean.getDealType()) {
            case INSERT: {
                if (dao.insert(sqlName.getName(), bean) == 0) {
                    log.error(sqlName.getName()+"数据插入错误, " + JsonUtils.toJSONString(bean));
                    throw new Exception();
                }
            }
            break;
            case UPDATE: {
                if (dao.update(sqlName.getName(), bean) == 0) {
                    if (sqlName.getName().equals(DbSqlName.SERVERPARAM_UPDATE.getName())) {
                        if (dao.insert(DbSqlName.SERVERPARAM_INSERT.getName(), bean) == 0) {
                            log.error(sqlName.getName()+"数据插入错误, " + JsonUtils.toJSONString(bean));
                            throw new Exception();
                        }
                    } else {
                        log.error(sqlName.getName()+"数据保存时，数据已经不存在了" + JsonUtils.toJSONString(bean));
                        throw new Exception();
                    }
                }
            }
            break;
            case MERGE: {
                String str = sqlName.getName().substring(0, sqlName.getName().length() - 6);
                String sql = str + "update";
                if (dao.update(sql, bean) == 0) {
                    sql = str + "insert";
                    if (dao.insert(sql, bean) == 0) {
                        log.error(sqlName.getName()+"数据MERGE错误, " + JsonUtils.toJSONString(bean));
                        throw new Exception();
                    }
                }
            }
            break;
            case DELETE: {
                Object o = bean.getWhere();
                if (o == null) {
                    if (dao.delete(sqlName.getName(), bean) == 0) {
                        log.error(sqlName.getName() + "数据删除错误, 条件:" + JsonUtils.toJSONString(o) + ", 对象：" + JsonUtils.toJSONString(bean));
                    }
                }else {
                    if (dao.delete(sqlName.getName(), o) == 0) {
                        log.error(sqlName.getName() + "数据删除错误, 条件:" + JsonUtils.toJSONString(o) + ", 对象：" + JsonUtils.toJSONString(bean));
                    }
                }
            }
            default:
                break;
        }
    }

    /**
     * 待保存的数据
     *
     *
     * @date 2014-1-15上午11:35:50
     *
     * @version 1.0.0
     */
    private class SaveBean {

        // 待保存实体
        private BaseBean bean;
        // 处理方式
        private int dealType;
        //sql的方法名
        private DbSqlName sqlName;
        // 失败次数
        private int errorTimes;

        public SaveBean(BaseBean bean, DbSqlName sqlName, int dealType) {
            this.bean = bean;
            this.sqlName = sqlName;
            this.dealType = dealType;
        }

        public BaseBean getBean() {
            return bean;
        }

        @SuppressWarnings("unused")
        public void setBean(BaseBean bean) {
            this.bean = bean;
        }

        public int getDealType() {
            return dealType;
        }

        @SuppressWarnings("unused")
        public void setDealType(int dealType) {
            this.dealType = dealType;
        }

        public DbSqlName getSqlName() {
            return sqlName;
        }

        @SuppressWarnings("unused")
        public void setSqlName(DbSqlName sqlName) {
            this.sqlName = sqlName;
        }

        public int getErrorTimes() {
            return errorTimes;
        }

        public void setErrorTimes(int errorTimes) {
            this.errorTimes = errorTimes;
        }

    }

}
