package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.db.bean.ChatBlackListBean;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class ChatBlackListDao {

    public List<ChatBlackListBean> selectAll() {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatBlackListBean> list = session.selectList("chatBlackList.selectAll");
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public List<ChatBlackListBean> selectAllByServerId(int serverId) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatBlackListBean> list = session.selectList("chatBlackList.selectAllByServerId", serverId);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }

    public List<ChatBlackListBean> selectAllByServerIds(List<Integer> serverIds) {
        try (SqlSession session = DBFactory.PUBLIC_DB.getSessionFactory().openSession()) {
            List<ChatBlackListBean> list = session.selectList("chatBlackList.selectAllByServerIds", serverIds);
            return list;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return null;
    }
}
