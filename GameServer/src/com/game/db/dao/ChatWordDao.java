package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ChatWordBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ChatWordDao {

    public List<ChatWordBean> selectAll() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatWordBean> list = session.selectList("chatWord.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public List<ChatWordBean> selectAllByServerId(int serverId) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatWordBean> list = session.selectList("chatWord.selectAllByServerId",serverId);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public List<ChatWordBean> selectAllByServerIds(List<Integer> serverIds) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatWordBean> list = session.selectList("chatWord.selectAllByServerIds",serverIds);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public int insert(ChatWordBean wordBean) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int rows = session.insert("chatWord.insert", wordBean);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int delete(int id) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            int row = session.delete("chatWord.delete", id);
            session.commit();
            return row;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
