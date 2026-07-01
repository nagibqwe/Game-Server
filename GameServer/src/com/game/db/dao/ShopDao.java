package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ShopBean;
import com.game.db.bean.ShopBeanExample;
import com.game.db.mapper.ShopBeanMapper;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class ShopDao extends BaseDao {
    /**
     * 查询所有
     * @return
     */
    public List<ShopBean> selectAll() {
        List<ShopBean> list = new ArrayList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ShopBeanMapper mapper = session.getMapper(ShopBeanMapper.class);
            list = mapper.selectByExample(new ShopBeanExample());
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return list;
    }

    /**
     * 删除所有数据
     */
    public void deleteAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ShopBeanMapper mapper = session.getMapper(ShopBeanMapper.class);
            mapper.deleteByExample(new ShopBeanExample());
            session.commit();
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
    }

    /**
     * 插入
     * @param bean
     * @return
     */
    public int insert(ShopBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ShopBeanMapper mapper = session.getMapper(ShopBeanMapper.class);
            int row = mapper.insert(bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 更新
     * @param bean
     * @return
     */
    public int update(ShopBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ShopBeanMapper mapper = session.getMapper(ShopBeanMapper.class);
            int row = mapper.updateByPrimaryKey(bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 删除商城数据
     * @param sellId
     * @return
     */
    public int delete(int sellId) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            ShopBeanMapper mapper = session.getMapper(ShopBeanMapper.class);
            int row = mapper.deleteByPrimaryKey(sellId);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    /**
     * 插入
     *
     * @param sqlName
     * @param bean
     * @return
     */
    @Override
    public int insert(String sqlName, BaseBean bean) {
        dblog.error("insert 本接口未实现");
        return 0;
    }

    /**
     * 更新
     *
     * @param sqlName
     * @param bean
     * @return
     */
    @Override
    public int update(String sqlName, BaseBean bean) {
        dblog.error("update 本接口未实现");
        return 0;
    }

    /**
     * 删除
     *
     * @param sqlName
     * @param o
     * @return
     */
    @Override
    public int delete(String sqlName, Object o) {
        dblog.error("delete 本接口未实现");
        return 0;
    }
}
