/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBFactory;
import java.util.List;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author hewei@haowan123.com
 */
public class WhiteDao {
    
    private static final Logger logger = LogManager.getLogger(WhiteDao.class);
    
    public List<String> selectAllPlatforms() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<String> list = session.selectList("white.selectAllPlatform");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    public int insertPlatform(String str){
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {	    
            int row = session.insert("white.insertPlatform", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }

    public List<String> selectAll() {
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {
            List<String> list = session.selectList("white.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    public int insert(String str){
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {	    
            int row = session.insert("white.insert", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
    
    public int delete(String str){
        try (SqlSession session = DBFactory.LOGIN_DB.getSessionFactory().openSession()) {	    
            int row = session.insert("white.delete", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
}
