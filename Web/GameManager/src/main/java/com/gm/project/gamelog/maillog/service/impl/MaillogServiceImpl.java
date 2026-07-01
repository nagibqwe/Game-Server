package com.gm.project.gamelog.maillog.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.manager.ItemManager;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.maillog.domain.Maillog;
import com.gm.project.gamelog.maillog.service.IMaillogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 邮件日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class MaillogServiceImpl implements IMaillogService 
{


    /**
     * 查询邮件日志列表
     * 
     * @param maillog 邮件日志
     * @return 邮件日志
     */
    @Override
    public List<Maillog> selectMaillogList(Maillog maillog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(maillog.getType() != null){
            wheresql.append(" and type = " + maillog.getType());
        }
        if(maillog.getReceiverId() != null){
            wheresql.append(" and receiverId = " + maillog.getReceiverId());
        }
        //自定义查询条件
        param.put("tableName","maillog");
        param.put("where",wheresql);
        List<Maillog> maillogList =  GameLogUtil.getLogDataList(Maillog.class,param);
        if(maillogList!=null && maillogList.size()>0){
            for(int i = 0;i<maillogList.size();i++){
                StringBuilder items = new StringBuilder("[");
                String attachment = maillogList.get(i).getAttachment();
                List<HashMap<String, Object>> array = JsonUtils.parseObject(attachment, new TypeReference<ArrayList<HashMap<String, Object>>>() {});
                if(array.size()>0) {
                    for (HashMap<String, Object> object : array) {

                        String itemName = ItemManager.getInstance().getItemName(Integer.valueOf(object.get("itemModelId").toString()));
                        items.append(itemName).append("*").append(object.get("num").toString()).append(";");
                    }
                }
                items.append("]");
                maillogList.get(i).setAttachment(items.toString());
            }
        }
        return maillogList;
    }
}
