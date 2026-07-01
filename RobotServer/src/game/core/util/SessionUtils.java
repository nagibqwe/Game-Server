/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.future.CloseFuture;
import org.apache.mina.core.session.IoSession;

public class SessionUtils {

    private static final Logger closelog = LogManager.getLogger("GATESESSIONCLOSE");

    public static void closeSession(IoSession session, String reason, boolean isAwait) throws InterruptedException {
        closelog.error(session + "-->close [because] " + reason, new Throwable());
        CloseFuture cf = session.closeNow();
        if(isAwait){
            cf.await();
        }
    }
}
