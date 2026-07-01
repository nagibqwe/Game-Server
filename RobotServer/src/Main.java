import com.game.manager.Manager;
import com.game.server.RobotServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        new Thread(RobotServer.getInstance()).start();
        Thread runtime = new Thread(() -> {
            while (true) {
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String str = br.readLine();
                    if (str == null || str.isEmpty()) {
                        return;
                    }
                    Manager.cmd.deal().cmd(str);
                } catch (IOException e) {
                }
            }
        });
        runtime.start();

    }
}
