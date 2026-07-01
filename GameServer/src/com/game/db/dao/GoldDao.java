/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.dao;

import com.game.db.DBErrorToFile;
import com.game.db.DBFactory;
import com.game.gold.structs.Gold;
import java.util.List;
import org.apache.ibatis.session.SqlSession;

/**
 *
 * @author hewei@haowan123.com
 */
public class GoldDao {

    public List<Gold> selectAll() {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            List<Gold> list = session.selectList("game_gold.selectAll");
            return list;
        } catch (Exception e) {
            //启服没有把元宝数据加载出来关闭服务器
            DBErrorToFile.error(e);
            System.exit(-1);
        }
        return null;
    }

    public int insert(Gold gold) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.insert("game_gold.insert", gold);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }

    public int update(Gold gold) {
        try (SqlSession session = DBFactory.GAME_DB.getSessionFactory().openSession()) {
            int rows = session.update("game_gold.update", gold);
            session.commit();
            return rows;
        } catch (Exception e) {
            DBErrorToFile.error(e);
        }
        return 0;
    }
}
