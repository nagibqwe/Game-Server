package com.kits;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 * 
 * @author ruoyi
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ClientLogKitsApplication
{
    public static void main(String[] args)
    {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ClientLogKitsApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  客户端日志工具启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}