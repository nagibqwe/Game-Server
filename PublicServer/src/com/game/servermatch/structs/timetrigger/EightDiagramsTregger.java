package com.game.servermatch.structs.timetrigger;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.eightdiagrams.manager.EightDiagramsManager;
import com.game.servermatch.structs.Trigger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by 542 on 2019/10/10.
 */
public class EightDiagramsTregger implements Trigger{

    @JsonIgnore
    private static final Logger logger = LogManager.getLogger("EightDiagramsTregger");
    @Override
    public boolean active() {
        try {
            //周期结算
            EightDiagramsManager.getInstance().deal().periodSettle();
            return true;
        } catch (Exception e) {
            logger.error(e, e);
            return false;
        }
    }
}
