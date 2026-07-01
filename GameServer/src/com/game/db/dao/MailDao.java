/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.Mail;
import game.core.db.BaseBean;
import game.core.db.BaseDao;
import java.util.ArrayList;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author Administrator
 */
public class MailDao extends BaseDao {

    public List<Mail> selectAll() {
        List<Mail> list = new ArrayList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            list = session.selectList("mail.selectAll");
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return list;
    }

    public List<Mail> selectByReceiverId(long receiverId) {
        List<Mail> list = new ArrayList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            list = session.selectList("mail.selectByReceiverId", receiverId);
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return list;
    }

    public List<Mail> selectByReceiveTime(long receiveTime) {
        List<Mail> list = new ArrayList<>();
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            list = session.selectList("mail.selectByReceiveTime", receiveTime);
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return list;
    }

//    public int insert(Mail mail) {
//	try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {	    
//            int row = session.insert("mail.insert", mail);
//	    session.commit();
//	    return row;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }	
//	return 0;
//    }
//    
//    public int update(Mail mail) {
//	try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {	    
//            int row = session.update("mail.update", mail);
//	    session.commit();
//	    return row;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//	return 0;
//    }
//    
//    public int deleteByMailId(long mailId) {
//	try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {	    
//            int row = session.delete("mail.deleteByMailId", mailId);
//	    session.commit();
//	    return row;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//	return 0;
//    }
//    
//    public int deleteByReceiverId(long receiverId) {
//	try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {	    
//            int row = session.delete("mail.deleteByReceiverId", receiverId);
//	    session.commit();
//	    return row;
//        } catch (Exception e) {
//            DBErrorToFile.error(e);
//        }
//	return 0;
//    }
    @Override
    public int insert(String sqlName, BaseBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.insert(sqlName, (Mail) bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    @Override
    public int update(String sqlName, BaseBean bean) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.update(sqlName, (Mail) bean);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    @Override
    public int delete(String sqlName, Object o) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int row = session.delete(sqlName, o);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

}
