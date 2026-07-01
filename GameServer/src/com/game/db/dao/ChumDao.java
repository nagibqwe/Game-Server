package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ChumBean;
import com.game.db.bean.ChumBeanExample;
import com.game.db.mapper.ChumBeanMapper;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * @explain: 挚友
 * @time Created on 2019/10/22 10:47.
 * @author: tc
 */
public class ChumDao extends BaseDao {
	/**
	 * 查询所有
	 * @return
	 */
	public List<ChumBean> selectAll() {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			ChumBeanMapper mapper = session.getMapper(ChumBeanMapper.class);
			List<ChumBean> list = mapper.selectByExampleWithBLOBs(new ChumBeanExample());
			session.commit();
			return list;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
		return null;
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
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			ChumBeanMapper mapper = session.getMapper(ChumBeanMapper.class);
			int ret = mapper.insert((ChumBean) bean);
			session.commit();
			return ret;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
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
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			ChumBeanMapper mapper = session.getMapper(ChumBeanMapper.class);
			int ret = mapper.updateByPrimaryKeyWithBLOBs((ChumBean) bean);
			session.commit();
			return ret;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
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
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			ChumBeanMapper mapper = session.getMapper(ChumBeanMapper.class);
			int ret = mapper.deleteByPrimaryKey((long) o);
			session.commit();
			return ret;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
		return 0;
	}
}
