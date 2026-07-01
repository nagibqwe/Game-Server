import com.game.manager.Manager;
import com.game.server.SocialServer;
import game.core.net.Config.ServerConfig;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.*;

/**
 * @Desc TODO
 * @Date 2021/6/7 17:05
 * @Auth ZUncle
 */
public class Main {

    public static void main(String[] args) {

        initLogConfig();
        initConfig();

        //TODO 启动游戏线程
        new Thread(SocialServer.getInstance()).start();

        //TODO 命令行监听线程
        Thread cmd = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String str = br.readLine();
                        System.out.println(str);
                        if (str.equalsIgnoreCase("quit")) {
                            System.out.println("输入了QUIT 命令，需要退出！");
                            System.exit(1);
                            return;
                        }
                        Manager.gmManager.deal().cmd(str);

                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        });
        if (isIDEEnvironment()) {
            cmd.start();
        }

    }

    //初始化数据库系统
    private static void initConfig() {
        try {
            String filePath = System.getProperty("user.dir") + File.separator + "config" + File.separator + "server-config.xml";
            ServerConfig.getInstance().load(filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(2);
        }
    }

    /**
     * 设置日志
     */
    private static void initLogConfig() {
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        if (isIDEEnvironment()) {
            pathBuilder.append("log4j2_debug.xml");
        } else {
            pathBuilder.append("log4j2.xml");
        }

        try {
            ConfigurationSource source = new ConfigurationSource(new FileInputStream(pathBuilder.toString()));
            Configurator.initialize(null, source);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }


    /**
     * 是否IDE  环境
     *
     * @return
     */
    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }

}
