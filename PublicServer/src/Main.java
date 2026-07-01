import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.server.MainServer;
import com.game.server.script.IGMScript;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 *
 * 启动入口
 */
public class Main {
    private static void initLogSystem() {

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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initLogSystem();
        // TODO code application logic here
        new Thread(MainServer.getInstance()).start();

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

                        if (str.equalsIgnoreCase("quit")) {
                            System.out.println("输入了QUIT 命令，需要退出！");
                            System.exit(1);
                            return;
                        }

                        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GMScript);
                        if (is instanceof IGMScript) {
                            ((IGMScript) is).dealGm(null, 0, str);
                        }
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        });

        runtime.start();
    }

    public static boolean isIDEEnvironment() {
        String val = System.getProperty("ideDebug");
        return val != null && val.equals("true");
    }
}
