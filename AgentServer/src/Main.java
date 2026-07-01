
import com.game.server.GateServer;
import game.core.util.TimeUtils;
import java.io.File;
import java.io.FileInputStream;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Administrator
 */
public class Main {
//  初始化日志系统

    private static void initLogSystem() {
        StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
        DOMConfigurator.configureAndWatch(pathBuilder.toString() + "log4j_socket.xml");
        if (TimeUtils.isIDEEnvironment()) {
            return;
        }
        pathBuilder.append("log4j2.xml");
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
        initLogSystem();
        // TODO code application logic here
        new Thread((Runnable) GateServer.GetInstance()).start();
    }

}
