/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBFactory;
import com.game.db.bean.ForbidBean;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author hewei@haowan123.com
 */
public class ForbidDao {

    private static final Logger logger = LogManager.getLogger(ForbidDao.class);
    
    public List<ForbidBean> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<ForbidBean> list = session.selectList("forbid.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    public int insert(ForbidBean str){
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {	    
            int row = session.insert("forbid.insert", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
    
    public int delete(String str){
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {	    
            int row = session.update("forbid.delete", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
}
