package com.telespree.apps.sprint.mbod.rental.controller;

import java.util.Calendar;

import org.slf4j.cal10n.LocLogger;

import telespree.apps.fwk.session.TelespreeSession;

import com.opensymphony.xwork2.Action;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * 
 * TimeoutAction
 * 
 * Action proceeding timeout screen.
 * 
 * @author $Author: michael $ on $DateTime: 2012/09/11 15:13:20 $
 * @version $Revision: #4 $
 * 
 */
public class TimeoutAction extends MbodActionSupport {

    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(TimeoutAction.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    @Override
    public String execute() throws Exception {

        if (hasSession()) {
            TelespreeSession session = getSession();
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(cal.getTimeInMillis() + 30000);
            session.setExpirationDate(cal.getTime());
            return Action.SUCCESS;
        }
        return "static";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * telespree.apps.fwk.internal.struts2.action.TelespreeActionSupport#isTimeout
     * ()
     */
    @Override
    public boolean isTimeout() {
        return true;
    }

}
