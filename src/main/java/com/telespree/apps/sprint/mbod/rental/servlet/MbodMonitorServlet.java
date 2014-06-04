package com.telespree.apps.sprint.mbod.rental.servlet;

import org.slf4j.cal10n.LocLogger;

import telespree.apps.fwk.servlet.MonitorServlet;
import telespree.apps.fwk.servlet.MonitorTask;

import com.telespree.apps.sprint.mbod.rental.MbodConstants;
import com.telespree.apps.sprint.mbod.rental.QualutionBillingMonitorTask;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * 
 * MbodMonitorServlet
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/03/27 21:25:49 $
 * @version $Revision: #1 $
 * 
 */
public class MbodMonitorServlet extends MonitorServlet implements MbodConstants {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(MbodMonitorServlet.class);

    /*
     * (non-Javadoc)
     * 
     * @see telespree.apps.fwk.servlet.MonitorServlet#getContextRoot()
     */
    @Override
    public String getContextRoot() {
        return CONTEXT_ROOT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see telespree.apps.fwk.servlet.MonitorServlet#registerMonitors()
     */
    @Override
    protected void registerMonitors() {

        log.debug("registering MBOD Monitors...");
        MonitorTask qualutionBillingMonitorTask = new QualutionBillingMonitorTask();
        registerMonitor(qualutionBillingMonitorTask);
    }

}
