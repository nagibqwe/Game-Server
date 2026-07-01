import com.game.db.dao.ConfigDbDao;
import com.game.server.GameServer;
import com.game.server.handler.ConsoleHandler;
import game.core.db.bean.ConfigDbBean;
import game.core.net.Config.ServerConfig;
import game.core.util.JsonUtils;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * @author Administrator
 */
public class Main {
    /**
     * 初始化日志系统
     */
    private static void initLogSystem() {
        // 设置BI属性配置
        System.setProperty("biLogDir", ServerConfig.getBiConfig().getBiBakDir());
        System.setProperty("biSrvID", String.valueOf(ServerConfig.getServerId()));
        System.setProperty("sysLogHost", ServerConfig.getBiConfig().getSysLogHost());
        System.setProperty("sysLogPort", ServerConfig.getBiConfig().getSysLogPort());
        System.setProperty("sysLogProtocol", ServerConfig.getBiConfig().getSysLogProtocol());

        // 设置4399BI属性配置
        System.setProperty("bi4399Dir", ServerConfig.getBi4399Config().getBi4399Dir());
        System.setProperty("bi4399Host", ServerConfig.getBi4399Config().getBi4399Host());
        System.setProperty("bi4399Port", ServerConfig.getBi4399Config().getBi4399Port());
        System.setProperty("bi4399Protocol", ServerConfig.getBi4399Config().getBi4399Protocol());

        String property = System.getProperty("user.dir");
        System.out.println("程序启动主目录：" + property);
        StringBuilder pathBuilder = new StringBuilder(property + File.separator + "config" + File.separator);
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            initConfig();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            System.out.println(e.getMessage());
        }

        initLogSystem();

        // TODO code application logic here
        new Thread(GameServer.getInstance(), "GameServer Start").start();

        if (!isIDEEnvironment()) {
            return;
        }

        Thread runtime = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String str = br.readLine();
                        System.out.println(str);
                        ConsoleHandler handler = new ConsoleHandler();
                        handler.setCmdStr(str);
                        GameServer.getInstance().getMainThread().addCommand(handler);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "cmd thread");

        runtime.start();
    }

    /**
     * 初始化配置
     *
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static void initConfig() throws ParserConfigurationException, IOException, SAXException {
        String filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "config-db.xml";
        // 连接DB数据库
        File xmlFile = new File(filePath);
        if (xmlFile.exists()) {
            if (ServerConfig.getInstance().loadConfigDB(filePath)) {
                ConfigDbDao configDao = new ConfigDbDao();
                ConfigDbBean bean = configDao.select(ServerConfig.getServerId(), ServerConfig.getLsId());
                ServerConfig.getInstance().setGameServerConfigInfo(bean);
                return;
            }
        }
        filePath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "server-config.xml";
        ServerConfig.getInstance().load(filePath);
    }

    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }
}
