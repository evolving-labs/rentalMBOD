/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental;

import org.slf4j.cal10n.LocLogger;
import org.springframework.context.ApplicationContext;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.servlet.MonitorTask;

import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.apps.sprint.mbod.rental.service.NewBillingService;
import com.telespree.barracuda.log.TelespreeLogger;
import com.telespree.barracuda.util.ApplicationContextProvider;

/**
 * QualutionBillingMonitorTask
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/03/27 21:25:49 $
 * @version $Revision: #1 $
 * 
 */
public class QualutionBillingMonitorTask extends MonitorTask {

    private static final LocLogger log = TelespreeLogger
            .getLogger(QualutionBillingMonitorTask.class);

    /*
     * (non-Javadoc)
     * 
     * @see telespree.apps.fwk.servlet.MonitorTask#check(telespree.apps.fwk.
     * ApplicationConfiguration)
     */
    @Override
    protected boolean check(ApplicationConfiguration config) {

        ApplicationContext applicationContext = ApplicationContextProvider
                .getApplicationContext();
        NewBillingService newBillingService = (NewBillingService) applicationContext
                .getBean("newQualutionBillingService");
        boolean status = false;
        if (newBillingService != null) {
            try {
                status = newBillingService.healthCheck(config, null);
            } catch (BillingServiceException bse) {
                log.error(bse.getMessage(), bse);
                status = false;
            }
        }
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * telespree.apps.fwk.servlet.MonitorTask#getMonitorName(telespree.apps.
     * fwk.ApplicationConfiguration)
     */
    @Override
    protected String getMonitorName(ApplicationConfiguration config) {
        return "Qualution Billing SOAP";
    }

}
