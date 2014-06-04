package com.telespree.apps.sprint.mbod.rental;

import java.util.HashMap;

import telespree.apps.fwk.ApplicationErrors;
import telespree.apps.fwk.CoreErrors;
import telespree.common.eventlogging.EventID;

/**
 * MbodErrors
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/03/21 16:12:57 $
 * @version $Revision: #1 $
 * 
 */
public class MbodErrors implements ApplicationErrors {

    private static MbodErrors instance = new MbodErrors();

    private static HashMap<EventID, String> map;

    /**
     * 
     * @return
     */
    public static ApplicationErrors getInstance() {
        return instance;
    }

    /**
     * 
     */
    private static void initializeMapping() {

        map = new HashMap<EventID, String>();

        /*
         * map.put(PmbsEvents.ERROR_USER_DATACARD_INPUT_LIMIT_MDN.getEventID(),
         * "html.error.mdn.retry.limit");
         */

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * telespree.apps.fwk.ApplicationErrors#getResourceMapping(telespree.common
     * .eventlogging.EventID)
     */
    public synchronized String getResourceMapping(EventID event) {

        if (event == null) {
            return CoreErrors.KEY_UNKNOWN;
        }
        if (map == null) {
            initializeMapping();
        }
        String key = map.get(event);
        if (key == null) {
            return CoreErrors.KEY_UNKNOWN;
        }
        return key;
    }

}
