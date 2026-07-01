package game.core.script;

import game.core.net.server.SocketServer;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.script.ScriptException;
import java.io.IOException;

public class ScriptManager extends ScriptAbstractManager {

    private final static Logger errorLog = LogManager.getLogger(ScriptManager.class);

    private SocketServer errorServer;

    public void setErrorServer(SocketServer errorServer) {
        this.errorServer = errorServer;
    }

    private void error(String content) {
        if (errorServer != null) {
            errorServer.setErrorLog("ScriptManager", content);
        }
    }

    public void error(String key, String content) {
        if (errorServer != null) {
            errorServer.setErrorLog("ScriptManager:" + key, content);
        }
    }

    private ScriptManager() {
        load();
    }

    private enum Singleton {

        INSTANCE;
        ScriptManager processor;

        Singleton() {
            this.processor = new ScriptManager();
        }

        ScriptManager getProcessor() {
            return processor;
        }
    }

    public static ScriptManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    private void load() {
        try {
            String javaFilePath = "script";
            String classFilePath = "bin";
            String packageName = "common";
            super.initialize(javaFilePath, classFilePath, packageName, TimeUtils.isIDEEnvironment());
        } catch (ScriptException | IOException | IllegalAccessException | ClassNotFoundException | InstantiationException e) {
            errorLog.error(e, e);
            System.exit(-4);
        }
    }

}
