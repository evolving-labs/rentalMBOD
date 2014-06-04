/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.cal10n.LocLogger;
import org.springframework.util.CollectionUtils;

import telespree.apps.fwk.ApplicationConfiguration;

import com.opensymphony.xwork2.Action;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.log.TelespreeLogger;
import com.telespree.barracuda.product.bean.Product;
import com.telespree.rental.exception.RentalServiceException;

/**
 * ProductAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: juan $ on $DateTime: 2013/04/04 16:28:36 $
 * @version $Revision: #14 $
 * 
 */
public class ProductAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(ProductAction.class);

    private long planId;

    /**
     * @return the planId
     */
    public long getPlanId() {
        return planId;
    }

    /**
     * @param planId
     *            the planId to set
     */
    public void setPlanId(long planId) {
        this.planId = planId;
    }

    /**
     * @return
     * @throws Exception
     */
    public String selectPlan() throws Exception {

        if (planId < 0) {
            log.error(MbodActionError.PRODUCT_SELECT_PLANID_INVALID,
                    getDeviceId(), planId);
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PRODUCT_SELECT_PLANID_INVALID));
            return Action.SUCCESS;
        }
        List<Product> productList = getProductList();
        if (CollectionUtils.isEmpty(productList)) {
            log.error(MbodActionError.PRODUCT_SELECT_LIST_EMPTY, getDeviceId());
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PRODUCT_SELECT_LIST_EMPTY));
            return Action.SUCCESS;
        }
        boolean found = false;
        for (Product product : productList) {
            if (product.getId() == planId) {
                found = true;
                getMbodSession().setSelectedProduct(product);
                getMbodSession().setPromoList(null);
                getMbodSession().setPaymentStatus(null);
                log.info(MbodActionError.PRODUCT_SELECTED, getDeviceId(),
                        planId);
                try {
                    /*
                     * BillingService billingService = getBillingService();
                     * setupApplicationService(billingService);
                     */
                    Map<String, Double> taxMap = getNewBillingService().calculateTaxes(
                            getMvnoExternalId(), getPurchasePrice(), getMbodSession()
                                    .getTaxZipcode(), (ApplicationConfiguration)getConfig(), getSession());
                    if (!CollectionUtils.isEmpty(taxMap)) {
                        // TODO log enum and message
                    } else {
                        // TODO log enum and message
                    }
                    getMbodSession().setSelectedProductTaxMap(taxMap);
                } catch (BillingServiceException bse) {
                    // TODO log enum and message
                } catch (Exception ex) {
                    // TODO log enum and message
                }
                break;
            }
        }
        if (!found) {
            log.error(MbodActionError.PRODUCT_SELECT_NOMATCH, getDeviceId(),
                    planId);
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PRODUCT_SELECT_NOMATCH));
        }
        return Action.SUCCESS;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    public String execute() throws Exception {
        return Action.SUCCESS;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String tcContent() throws Exception {
        return Action.SUCCESS;
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public String tc() throws Exception {

        String deviceId = getDeviceId();
        int version = tcVersion();
        try {
            getRentalService().setTermsAndConditionsVersion(deviceId, version);
            getMbodSession().setTcAgreedVersion(version);
            getMbodSession().setTcNew(true);
            log.info(MbodActionError.PRODUCT_TC_SET, deviceId, version);
            if(getMbodSession().getSubscriber() != null) {
            	logQualutionHistoryNotes("Service terms and conditions acknowledged", HistoryType.RENTAL);
            }
        } catch (RentalServiceException rse) {
            log.error(MbodActionError.PRODUCT_TC_SET_RSE, deviceId, version,
                    rse.getMessage());
            getMbodSession().setErrorMessageKey(
                    BarracudaException
                            .getDisplayKey(MbodActionError.PRODUCT_TC_SET_RSE));
        }

        return Action.SUCCESS;
    }

}
