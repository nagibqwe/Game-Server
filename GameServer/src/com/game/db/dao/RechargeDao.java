package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.RechargeBean;
import com.game.db.bean.RechargeBeanExample;
import com.game.db.mapper.RechargeBeanMapper;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import org.apache.ibatis.session.SqlSession;

import java.util.HashMap;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/21 11:30.
 * @author: tc
 */
public class RechargeDao extends BaseDao {
	/**
	 * 查询所有
	 * @return
	 */
	public List<RechargeBean> selectAll() {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			RechargeBeanMapper mapper = session.getMapper(RechargeBeanMapper.class);
			List<RechargeBean> list = mapper.selectByExampleWithBLOBs(new RechargeBeanExample());
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
			RechargeBeanMapper mapper = session.getMapper(RechargeBeanMapper.class);
			int ret = mapper.insert((RechargeBean) bean);
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
			RechargeBeanMapper mapper = session.getMapper(RechargeBeanMapper.class);
			int ret = mapper.updateByPrimaryKeyWithBLOBs((RechargeBean) bean);
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
			RechargeBeanMapper mapper = session.getMapper(RechargeBeanMapper.class);
			int ret = mapper.deleteByPrimaryKey((String) o);

			session.commit();
			return ret;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
		return 0;
	}
	public long getRoleTimeRecharge(long roleId, long beginTime, long endTime) {
		try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("role_id", roleId);
			map.put("begin",  beginTime );
			map.put("end",  endTime);
			RechargeBeanMapper mapper = session.getMapper(RechargeBeanMapper.class);
			Long rows = mapper.selectRechargeByRoleId(map);
			session.commit();
			if (rows == null) {
				return 0;
			}
			return  rows;
		} catch (Exception e) {
			DBErrorToFile.error(e);
		}
		return 0;
	}
}
