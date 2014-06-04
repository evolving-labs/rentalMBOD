package com.telespree.apps.sprint.mbod.rental.controller;

import org.slf4j.cal10n.LocLogger;

import com.opensymphony.xwork2.Action;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * ErrorAction
 * 
 * Action proceeding error screen.
 * 
 * @author $Author: michael $ on $DateTime: 2012/04/26 12:43:43 $
 * @version $Revision: #2 $
 * 
 */
public class ErrorAction extends MbodActionSupport {

    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(ErrorAction.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    @Override
    public String execute() throws Exception {
        if (hasSession()) {
            return Action.SUCCESS;
        } else {
            return "static";
        }
    }

}
