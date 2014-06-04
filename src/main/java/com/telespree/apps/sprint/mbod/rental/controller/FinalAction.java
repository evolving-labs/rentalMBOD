/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import org.slf4j.cal10n.LocLogger;

import com.opensymphony.xwork2.Action;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * FinalAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/05/11 17:35:38 $
 * @version $Revision: #2 $
 * 
 */
public class FinalAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(FinalAction.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    @Override
    public String execute() throws Exception {
        sessionComplete();
        if (isTopup()) {
            // TODO
        } else {
            // TODO
        }
        return Action.SUCCESS;
    }

}
