package com.game.db.dao;

import com.game.db.DBFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class WhiteDao {
    
    private static final Logger logger = LogManager.getLogger(WhiteDao.class);
    
    public List<String> selectAllPlatforms() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<String> list = session.selectList("white.selectAllPlatform");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    public int insertPlatform(String str){
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.insert("white.insertPlatform", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }

    public List<String> selectAll() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<String> list = session.selectList("white.selectAll");
            return list;
        } catch (Exception e) {
            logger.error(e);
        }
        return null;
    }
    
    public int insert(String str){
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.insert("white.insert", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
    
    public int delete(String str){
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.insert("white.delete", str);
	    session.commit();
	    return row;
        } catch (Exception e) {
            logger.error(e);
        }	
	return 0;
    }
}
