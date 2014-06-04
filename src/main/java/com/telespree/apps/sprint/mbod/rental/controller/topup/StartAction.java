/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller.topup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.slf4j.cal10n.LocLogger;
import org.springframework.util.StringUtils;

import com.telespree.abmf.exception.DeviceServiceException;
import com.telespree.abmf.service.DeviceState;
import com.telespree.apps.sprint.mbod.rental.controller.MbodActionError;
import com.telespree.apps.sprint.mbod.rental.controller.MbodActionSupport;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.log.TelespreeLogger;
import com.telespree.rental.exception.RentalServiceException;
import com.telespree.rental.service.RentalInformationBean;

/**
 * StartAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/12/19 19:36:38 $
 * @version $Revision: #21 $
 * 
 */
public class StartAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(StartAction.class);

    private static final String RESULT_MOBILE = "mobile";

    private static final String RESULT_MOBILE_ERROR = "mobileError";

    private static final String RESULT_DESKTOP = "desktop";

    private static final String RESULT_DESKTOP_ERROR = "desktopError";

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    public String execute() throws Exception {

        String deviceId = getDeviceId();
        String deviceStateString = null;
        boolean isMobile = detectedMobileUser(getServletRequest().getHeader(
                "User-Agent"));
        try {
            deviceStateString = getDeviceService().getCurrentState(deviceId);
        } catch (DeviceServiceException dse) {
            log.error(MbodActionError.TOPUP_DEVICESTATUS_DSE, deviceId,
                    dse.getMessage());
            if (isMobile) {
                return RESULT_MOBILE_ERROR;
            } else {
                return RESULT_DESKTOP_ERROR;
            }
        }
        if (!StringUtils.hasText(deviceStateString)) {
            log.error(MbodActionError.TOPUP_DEVICESTATUS_EMPTY, deviceId);
            if (isMobile) {
                return RESULT_MOBILE_ERROR;
            } else {
                return RESULT_DESKTOP_ERROR;
            }
        }
        DeviceState deviceState = DeviceState.valueOf(deviceStateString);
        RentalInformationBean rib = null;
        switch (deviceState) {
        case AVAILABLE:
            log.error(MbodActionError.TOPUP_DEVICESTATUS_AVAILABLE, deviceId);
            if (isMobile) {
                return RESULT_MOBILE_ERROR;
            } else {
                return RESULT_DESKTOP_ERROR;
            }
        case CHECKEDOUT:
        case INUSE:
            if (DeviceState.CHECKEDOUT == deviceState) {
                log.info(MbodActionError.TOPUP_DEVICESTATUS_CHECKEDOUT,
                        deviceId);
            } else {
                log.info(MbodActionError.TOPUP_DEVICESTATUS_INUSE, deviceId);
            }
            try {
                rib = getRentalService().getRentalInformation(deviceId);
                if (rib == null) {
                    log.error(MbodActionError.TOPUP_RENTALINFO_NONE, deviceId);
                    if (isMobile) {
                        return RESULT_MOBILE_ERROR;
                    } else {
                        return RESULT_DESKTOP_ERROR;
                    }
                }
                Calendar now = Calendar.getInstance();
                if (now.getTime().after(rib.getRentedUntil())) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
                            "MM/dd/yyyy, hh:mm a, z");
                    String expirationTime = simpleDateFormat.format(rib
                            .getRentedUntil());
                    log.warn(MbodActionError.TOPUP_RENTALINFO_EXPIRED,
                            deviceId, expirationTime);
                    getMbodSession().setRentalExpired(true);
                    getMbodSession()
                            .setErrorMessageKey(
                                    BarracudaException
                                            .getDisplayKey(MbodActionError.TOPUP_RENTALINFO_EXPIRED));
                }
            } catch (RentalServiceException rse) {
                log.error(MbodActionError.TOPUP_RENTALINFO_RSE, deviceId,
                        rse.getMessage());
                if (isMobile) {
                    return RESULT_MOBILE_ERROR;
                } else {
                    return RESULT_DESKTOP_ERROR;
                }
            }
            break;
        case SUSPENDED:
            log.error(MbodActionError.TOPUP_DEVICESTATUS_SUSPENDED, deviceId);
            if (isMobile) {
                return RESULT_MOBILE_ERROR;
            } else {
                return RESULT_DESKTOP_ERROR;
            }
        case SOLD:
            log.warn(MbodActionError.TOPUP_DEVICESTATUS_SOLD, deviceId);
            break;
        default:
            log.error(MbodActionError.TOPUP_DEVICESTATUS_UNKNOWN, deviceId,
                    deviceState);
            if (isMobile) {
                return RESULT_MOBILE_ERROR;
            } else {
                return RESULT_DESKTOP_ERROR;
            }
        }
        getMbodSession().setDeviceState(deviceState);
        if (rib != null) {
            // TODO add billto to log
            log.info(MbodActionError.TOPUP_RENTALINFO_FOUND, deviceId,
                    rib.getZipCode());
            getMbodSession().setTaxZipcode(rib.getZipCode());
            getMbodSession().setBillToReference(rib.getBillToNumber());
            getMbodSession().setRentalId(rib.getId());
        }
        if (isMobile) {
            return RESULT_MOBILE;
        } else {
            return RESULT_DESKTOP;
        }
    }

}
