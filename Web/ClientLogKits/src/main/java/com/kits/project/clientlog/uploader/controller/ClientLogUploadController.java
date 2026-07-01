package com.kits.project.clientlog.uploader.controller;

import com.kits.common.utils.file.MimeTypeUtils;
import com.kits.framework.config.ProjectConfig;
import com.kits.framework.config.ServerConfig;
import com.kits.framework.web.domain.AjaxResult;
import com.kits.project.clientlog.clientlogdata.domain.Clientlog;
import com.kits.project.clientlog.clientlogdata.service.IClientlogService;
import com.kits.project.clientlog.uploader.domain.ClientLogModel;
import com.kits.project.clientlog.utils.LogFileUploadUtils;
import com.kits.project.clientlog.utils.LogFileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;

/**
 * @author gaozhaoguang
 * @desc ClientLogUploadController
 * @date Created on 2021/6/17 14:22
 **/
@Controller
public class ClientLogUploadController {

    protected final Logger logger = LoggerFactory.getLogger(ClientLogUploadController.class);

    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private IClientlogService tClientlogService;

    private void updateOrInsert(ClientLogModel m){
        Clientlog t = new Clientlog();
        t.setUuid(m.getUuid());
        t.setGame(m.getGame());
        t.setMiei(m.getMiei());
        t.setIdfa(m.getIdfa());
        t.setVersion(m.getVersion());
        t.setTime(m.getTime());
        t.setMemUsed(m.getMem_used());
        t.setMemFree(m.getMem_free());
        t.setPlayerinfo(m.getUser()+"_"+m.getPlayerid()+"_"+m.getPlayername()+";");
        t.setFilemd5("");

        Clientlog  dt = tClientlogService.selectClientlogByKey(t);
        if(dt != null){
            String str = dt.getPlayerinfo();
            if(str.indexOf(t.getPlayerinfo()) < 0){
                str = str + t.getPlayerinfo();
            }
            t.setId(dt.getId());
            t.setPlayerinfo(str);
            tClientlogService.updateClientlog(t);
        }else{
            tClientlogService.insertClientlog(t);
        }
    }
    /**
     * 通用上传请求
     */
    @PostMapping("/clientlog/upload")
    @ResponseBody
    public AjaxResult upload(ClientLogModel model) throws Exception
    {
        try
        {
            if(model == null){
                logger.error("ClientLogUploadController.upload param is empty!");
                return AjaxResult.error("param is empty!");
            }
            if(model.getGame() == null || model.getGame() == ""){
                logger.error("ClientLogUploadController.upload param is error. game is empty!");
                return AjaxResult.error("game param is empty!");
            }

            if(model.getUuid() == null || model.getUuid() == ""){
                logger.error("ClientLogUploadController.upload param is error. uuid is empty!");
                return AjaxResult.error("uuid param is empty!");
            }


            if(model.getLogPath() == null || model.getLogPath() == ""){
                logger.error("ClientLogUploadController.upload param is error. logpath is empty!");
                return AjaxResult.error("logpath param is empty!");
            }

            //整理文件名
            String fileName = LogFileUtils.uniSeparator(LogFileUtils.getLogFilePath(model.getUuid(),model.getGame(),model.getLogPath()));
            logger.info("LogFileUploadUtils.upload: start:"+fileName);
            //上传并返回新文件名称
            LogFileUploadUtils.upload(fileName, model.getStack(), MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
            logger.info("LogFileUploadUtils.upload,end");
            updateOrInsert(model);
            return AjaxResult.success("ok");
        }
        catch (Exception e)
        {
            logger.error("上传日志失败",e);
            return AjaxResult.error(e.getMessage());
        }
    }
}
