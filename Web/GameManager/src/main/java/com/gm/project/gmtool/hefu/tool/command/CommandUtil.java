package com.gm.project.gmtool.hefu.tool.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/9 14:50
 */
public class CommandUtil {
    private static Logger log = LoggerFactory.getLogger(CommandUtil.class);
    /**
     * 执行命令
     * @param command
     * @return
     */
    public static Result exeCommand(String command) {
        boolean success = true;
        List<String> result = new ArrayList<>();
        List<String> error = new ArrayList<>();
        try{
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[]{"/bin/bash","-c", command});

            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String str = null;
                while((str = reader.readLine()) != null){
                    log.info("resultMsg:{}", str);
                    result.add(str);
                }
            }catch (Exception e){
                log.error("", e);
            }

            try(BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))){
                String str = null;
                while((str = errorReader.readLine()) != null){
                    log.info("resultErrorMsg:{}", str);
                    error.add(str);
                }
            }catch (Exception e){
                log.error("", e);
            }

            int exitValue = process.waitFor();
            process.destroy();
            log.info("exitValue:{}", exitValue);

            if(exitValue != 0){
                log.info("命令执行失败");
                success = false;
            }
        }catch (Exception e){
            log.error("exeCommand error:" + command, e);
            success = false;
        }
        return new Result(success, result, error);
    }

}

