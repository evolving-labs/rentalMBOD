/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import org.slf4j.cal10n.LocLogger;

import com.opensymphony.xwork2.Action;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * LaunchAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/12/19 19:36:38 $
 * @version $Revision: #19 $
 * 
 */
public class LaunchAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(LaunchAction.class);

    private static final String RESULT_ACTIVATE = "activate";

    private static final String RESULT_REACTIVATE = "reactivate";

    private static final String RESULT_TOPUP = "topup";

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    public String execute() throws Exception {

        if (isTopup()) {
            log.debug("It's topup.");
            getSession().setUIBasePath("topup");
            return RESULT_TOPUP;
        } else if (isReactivate()) {
            log.debug("It's reactivate.");
            getSession().setUIBasePath("reactivate");
            return RESULT_REACTIVATE;
        } else {
            log.debug("It's activate.");
            getSession().setUIBasePath("activate");
            return RESULT_ACTIVATE;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    public String usage() throws Exception {
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String device() throws Exception {
        return Action.SUCCESS;
    }

}
