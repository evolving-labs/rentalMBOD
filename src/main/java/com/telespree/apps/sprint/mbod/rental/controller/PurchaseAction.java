/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.cal10n.LocLogger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.common.CreditCard;
import telespree.apps.fwk.exception.ApplicationException;
import telespree.apps.fwk.ism.BootstrapInfo;

import com.opensymphony.xwork2.Action;
import com.telespree.abmf.bean.PurchaseActivityBean;
import com.telespree.abmf.bean.PurchaseActivityBean.Activity;
import com.telespree.abmf.exception.DeviceServiceException;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.apps.sprint.mbod.rental.util.CryptoUtils;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.log.TelespreeLogger;
import com.telespree.barracuda.product.bean.Product;
import com.telespree.barracuda.product.exception.PromoCodeServiceException;

/**
 * PurchaseAction
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/11/04 14:54:05 $
 * @version $Revision: #47 $
 * 
 */
public class PurchaseAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(PurchaseAction.class);

    private String mop;

    private String creditCardNumber;

    private String creditCardZip;

    private String creditCardSecurityCode;

    private int creditCardExpirationMonth;

    private int creditCardExpirationYear;

    private String toStore;

    private String promo;

    private String changePromo;

    private String purchaseLoyaltyReference;

    private String ccFullName;

    private String ccAddressLine1;

    private String ccAddressLine2;

    private String ccCityTown;

    private String ccStateProvinceRegion;

    private String ccCountry;

    /*
     * (non-Javadoc)
     * 
     * @see com.opensymphony.xwork2.Action#execute()
     */
    public String execute() throws Exception {
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String cc() throws Exception {

        Subscriber subscriber = getMbodSession().getSubscriber();
        String existingLoyaltyReference = null;
        if (subscriber != null) {
            existingLoyaltyReference = subscriber.getLoyaltyReference();
        }
        if (StringUtils.hasText(purchaseLoyaltyReference)) {
            try {
                if (subscriber == null) {
                    // TODO enum and log
                    getMbodSession().setErrorMessageKey(
                            BarracudaException.DEFAULT_EVENT_KEY);
                    return Action.SUCCESS;
                }
                if (!purchaseLoyaltyReference.equals(existingLoyaltyReference)) {
                    subscriber.setLoyaltyReference(purchaseLoyaltyReference);
                    getNewBillingService().update(getMvnoExternalId(),
                            subscriber, (ApplicationConfiguration) getConfig(),
                            getSession());
                }
            } catch (BillingServiceException bse) {
                getMbodSession().getSubscriber().setLoyaltyReference(
                        existingLoyaltyReference);
            } catch (ApplicationException ae) {
                getMbodSession().getSubscriber().setLoyaltyReference(
                        existingLoyaltyReference);
            } catch (Exception ex) {
                getMbodSession().getSubscriber().setLoyaltyReference(
                        existingLoyaltyReference);
            }
        }
        if (StringUtils.hasText(mop)) {
            if ("balance".equalsIgnoreCase(mop)) {
                return payByBalance();
            }
            if ("bill".equalsIgnoreCase(mop)) {
                return bill();
            }
            return mop();
        }
        try {
            StringBuilder ccData = new StringBuilder();
            String userId = null;
            if (getMbodSession().isAuthenticated()) {
                if (subscriber == null) {
                    // TODO enum and log
                    getMbodSession().setErrorMessageKey(
                            BarracudaException.DEFAULT_EVENT_KEY);
                    return Action.SUCCESS;
                }
                userId = subscriber.getUserId();
                ccData.append(userId + "_");
            }
            if (!StringUtils.hasText(creditCardZip)) {
                // TODO enum and log
                creditCardZip = getCardDefaultZipcode();
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
            ccData.append(dateFormat.format(cal.getTime()) + "_");
            ccData.append(ccFullName + "_");
            ccData.append(ccAddressLine1 + "_");
            ccData.append(ccAddressLine2 + "_");
            ccData.append(ccCityTown + "_");
            ccData.append(ccStateProvinceRegion + "_");
            ccData.append(creditCardZip + "_");
            ccData.append(ccCountry);
            Product product = getMbodSession().getSelectedProduct();
            if (product == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            String taxZipcode = getMbodSession().getTaxZipcode();
            if (!StringUtils.hasText(taxZipcode)) {
                // TODO enum and log
                taxZipcode = creditCardZip;
            }
            double amount = getPurchasePrice();
            CreditCard creditCard = new CreditCard();
            creditCard.setNumber(creditCardNumber);
            creditCard.setExpirationMonth(creditCardExpirationMonth);
            creditCard.setExpirationYear(creditCardExpirationYear + 2000);
            creditCard.setSecurityCode(creditCardSecurityCode);
            Address address = new Address();
            address.setPostalCode(creditCardZip);
            address.setStreetName(ccAddressLine1);
            address.setStreetName2(ccAddressLine2);
            address.setCityName(ccCityTown);
            address.setStateCode(ccStateProvinceRegion);
            creditCard.setBillingAddress(address);
            boolean useBalance = false;
            if (getMbodSession().getPaymentStatus() != null) {
                useBalance = getMbodSession().getPaymentStatus()
                        .isUsedBalance();
            }
            PaymentStatus paymentStatus = null;
            String cardProcessorService = getCardProcessingService();
            if ("A".equalsIgnoreCase(cardProcessorService)) {
                log.debug("Payment being processed by Authorize.net");
                paymentStatus = getAuthorizeDotNetService().payByCreditCard(
                        userId, amount, product.getSku(), creditCard,
                        taxZipcode, (ApplicationConfiguration) getConfig(),
                        getSession());
            } else {
                log.debug("Payment being processed by Qualution");
                paymentStatus = getNewBillingService().payByCreditCard(
                        getMvnoExternalId(), userId, amount, product.getSku(),
                        getMbodSession().getPromoList(), creditCard,
                        StringUtils.hasText(toStore) ? true : false,
                        taxZipcode, useBalance, ccFullName, ccCountry,
                        (ApplicationConfiguration) getConfig(), getSession());
                if (StringUtils.hasText(toStore)) {
                    logQualutionHistoryNotes(
                            "Credit card on file updated (Card On File-"
                                    + creditCard.getLastFourDigits() + ")",
                            HistoryType.PAYBYCREDITCARD);
                }
            }
            log.debug("Final String to Encrypt: " + ccData.toString());
            String appendTag = "Encode-AES-" + getCryptoKeyVersion() + "_";
            logQualutionHistoryNotes(
                    appendTag
                            + CryptoUtils.encrypt(ccData.toString(),
                                    getCryptoKey(), getCryptoAlgorithm()),
                    HistoryType.PAYBYCREDITCARD);

            paymentStatus.setCcLast4Digits(creditCard.getLastFourDigits());
            getMbodSession().setPaymentStatus(paymentStatus);
            if (useBalance) {
                LogCCUseBalancePurchaseActivity();
            } else {
                LogCCPurchaseActivity();
            }
            purchase();
            access();
        } catch (BillingServiceException bse) {
            getMbodSession().setErrorMessageKey(bse.getDisplayKey());
        } catch (ApplicationException ae) {
            // TODO enum and log
            getMbodSession().setErrorMessageKey(
                    BarracudaException.DEFAULT_EVENT_KEY);
        }
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String guest() throws Exception {
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String mop() throws Exception {

        try {
            if (!getMbodSession().isAuthenticated()) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            double amount = getPurchasePrice();
            Product product = getMbodSession().getSelectedProduct();
            if (product == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            String taxZipcode = getMbodSession().getTaxZipcode();
            if (!StringUtils.hasText(taxZipcode)) {
                // TODO enum and log, where do we get the zip then for sold
                // device?
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            boolean useBalance = false;
            if (getMbodSession().getPaymentStatus() != null) {
                useBalance = getMbodSession().getPaymentStatus()
                        .isUsedBalance();
            }
            PaymentStatus paymentStatus = getNewBillingService()
                    .payByCardOnFile(getMvnoExternalId(),
                            subscriber.getUserId(), amount, product.getSku(),
                            getMbodSession().getPromoList(),
                            subscriber.getMopcode(), subscriber.getMoptype(),
                            taxZipcode, useBalance,
                            (ApplicationConfiguration) getConfig(),
                            getSession());
            paymentStatus.setCcLast4Digits(subscriber.getMopcode());
            getMbodSession().setPaymentStatus(paymentStatus);
            String qualutionLogString = "Purchased: " + product.getName()
                    + "\n" + "Value: " + product.getPrice() + "\n" + "Taxes: "
                    + getTaxes() + "\n" + "Device ID: " + getDeviceId() + "\n"
                    + "Loyalty Points Earned: " + product.getLoyaltyPoints()
                    + "\n" + "Charged to:\n";
            StringBuffer qualutionLogSB = new StringBuffer(qualutionLogString);
            qualutionLogSB.append("Credit Card - "
                    + paymentStatus.getAmountChargedToCreditCardString() + " "
                    + subscriber.getMoptype() + "-"
                    + paymentStatus.getCcLast4Digits());
            if (useBalance) {
                if (paymentStatus.getAmountChargedToBalance() > 0) {
                    qualutionLogSB.append("\nBalance - "
                            + paymentStatus.getAmountChargedToBalance()
                            + " account balance");
                }
            }
            logQualutionHistoryNotes(qualutionLogSB.toString(),
                    HistoryType.PAYBYCREDITCARD);
            if (useBalance) {
                LogCCUseBalancePurchaseActivity();
            } else {
                LogCCPurchaseActivity();
            }
            purchase();
            access();
        } catch (BillingServiceException bse) {
            getMbodSession().setErrorMessageKey(bse.getDisplayKey());
        } catch (ApplicationException ae) {
            // TODO enum and log
            getMbodSession().setErrorMessageKey(
                    BarracudaException.DEFAULT_EVENT_KEY);
        }
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    private String payByBalance() throws Exception {

        try {
            if (!getMbodSession().isAuthenticated()) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            Product product = getMbodSession().getSelectedProduct();
            if (product == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            if (subscriber.getAccountBalance() < getDue()) {
                PaymentStatus paymentStatus = new PaymentStatus();
                paymentStatus.setAmountStillOwe((float) (getDue() - subscriber
                        .getAccountBalance()));
                paymentStatus.setUsedBalance(true);
                getMbodSession().setPaymentStatus(paymentStatus);
                return Action.SUCCESS;
            }
            String userId = subscriber.getUserId();
            double amount = getPurchasePrice();
            PaymentStatus paymentStatus = getNewBillingService().payByBalance(
                    getMvnoExternalId(), userId, amount, product.getSku(),
                    getMbodSession().getPromoList(),
                    (ApplicationConfiguration) getConfig(), getSession());
            getMbodSession().setPaymentStatus(paymentStatus);
            String qualutionLogString = "Purchased: " + product.getName()
                    + "\n" + "Value: " + product.getPrice() + "\n" + "Taxes: "
                    + getTaxes() + "\n" + "Device ID: " + getDeviceId() + "\n"
                    + "Loyalty Points Earned: " + product.getLoyaltyPoints()
                    + "\n" + "Charged to:\n";
            StringBuffer qualutionLogSB = new StringBuffer(qualutionLogString);
            qualutionLogSB.append("Balance - "
                    + paymentStatus.getAmountChargedToBalance());
            logQualutionHistoryNotes(qualutionLogSB.toString(),
                    HistoryType.PAYBYACCOUNTBALANCE);
            LogBalancePurchaseActivity();
            if (paymentStatus.getAmountStillOwe() <= 0) {
                purchase();
                access();
            }
        } catch (BillingServiceException bse) {
            getMbodSession().setErrorMessageKey(bse.getDisplayKey());
        } catch (ApplicationException ae) {
            // TODO enum and log
            getMbodSession().setErrorMessageKey(
                    BarracudaException.DEFAULT_EVENT_KEY);
        }
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    private String bill() throws Exception {

        PaymentStatus paymentStatus = getMbodSession().getPaymentStatus();
        if (paymentStatus != null && paymentStatus.isUsedBalance()) {
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            Product product = getMbodSession().getSelectedProduct();
            if (product == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            String userId = subscriber.getUserId();
            float amountStillOwe = paymentStatus.getAmountStillOwe();
            paymentStatus = getNewBillingService().payByBalance(
                    getMvnoExternalId(), userId,
                    subscriber.getAccountBalance(), product.getSku(),
                    getMbodSession().getPromoList(),
                    (ApplicationConfiguration) getConfig(), getSession());
            paymentStatus.setUsedBalance(true);
            paymentStatus.setAmountBilled(amountStillOwe);
        } else {
            paymentStatus = new PaymentStatus();
            try {
                paymentStatus.setAmountBilled((float) getPurchasePrice());
            } catch (Exception ex) {
                // TODO log new enum and message
                log.error(ex.getMessage(), ex);
            }
        }
        getMbodSession().setPaymentStatus(paymentStatus);
        if (paymentStatus.isUsedBalance()) {
            LogBillLaterUseBalancePurchaseActivity();
        } else {
            LogBillLaterPurchaseActivity();
        }
        if (getMbodSession().getSubscriber() != null) {
            Product product = getMbodSession().getSelectedProduct();
            String qualutionLogString = "Purchased: " + product.getName()
                    + "\n" + "Value: " + product.getPrice() + "\n" + "Taxes: "
                    + getTaxes() + "\n" + "Device ID: " + getDeviceId() + "\n"
                    + "Loyalty Points Earned: " + product.getLoyaltyPoints()
                    + "\n" + "Charged to:\n";
            StringBuffer qualutionLogSB = new StringBuffer(qualutionLogString);
            qualutionLogSB.append("Bill to - "
                    + paymentStatus.getAmountBilled()
                    + " to "
                    + getRentalService().getRentalInformation(getDeviceId())
                            .getBillToNumber());
            logQualutionHistoryNotes(qualutionLogSB.toString(),
                    HistoryType.RENTAL);
        }
        purchase();
        access();
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String free() throws Exception {

        // TODO log enum and message
        Product product = getMbodSession().getSelectedProduct();
        if (product != null) {
            String qualutionLogString = "Acquired Free Plan ("
                    + product.getName() + ")\nDevice ID (" + getDeviceId()
                    + ")\nLoyalty Points Earned (" + product.getLoyaltyPoints()
                    + ").";
            logQualutionHistoryNotes(qualutionLogString, HistoryType.RENTAL);
        }
        purchase();
        access();
        return Action.SUCCESS;
    }

    /**
     * @throws Exception
     */
    private void purchase() throws Exception {

        String deviceId = getDeviceId();
        Product product = getMbodSession().getSelectedProduct();
        if (product == null || product.getId() == null) {
            log.error(MbodActionError.PURCHASE_NOPRODUCTSELECTED, deviceId);
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PURCHASE_NOPRODUCTSELECTED));
            saveSession();
            return;
        }
        if (isAddon()) {
            try {
                getDeviceService().purchaseAddOn(
                        deviceId,
                        product.getId().toString(),
                        (getAddonBundleId() != null) ? getAddonBundleId()
                                .toString() : null);
                // TODO log new enum and message
                log.info(MbodActionError.PURCHASED, deviceId, product.getId());
            } catch (DeviceServiceException dse) {
                // TODO log new enum and message
                log.error(MbodActionError.PURCHASE_DSE, deviceId,
                        product.getId(), dse.getMessage());
                getMbodSession().setErrorMessageKey(
                        BarracudaException
                                .getDisplayKey(MbodActionError.PURCHASE_DSE));
                saveSession();
            }
        } else {
            try {
                getDeviceService().purchaseService(deviceId,
                        product.getId().toString(), product.getSku());
                log.info(MbodActionError.PURCHASED, deviceId, product.getId());
            } catch (DeviceServiceException dse) {
                log.error(MbodActionError.PURCHASE_DSE, deviceId,
                        product.getId(), dse.getMessage());
                getMbodSession().setErrorMessageKey(
                        BarracudaException
                                .getDisplayKey(MbodActionError.PURCHASE_DSE));
                saveSession();
            }
        }
    }

    /**
     * @throws Exception
     */
    private void access() throws Exception {

        Product product = getMbodSession().getSelectedProduct();
        if (product == null) {
            log.error(MbodActionError.ACCESS_NOPRODUCT, getDeviceId());
            getMbodSession().setErrorMessageKey(
                    BarracudaException.DEFAULT_EVENT_KEY);
            return;
        }
        List<String> productRatingGroupList = product.getRatingGroups();
        if (CollectionUtils.isEmpty(productRatingGroupList)) {
            log.error(MbodActionError.ACCESS_NORATINGGROUP, getDeviceId(),
                    product.getId());
            getMbodSession().setErrorMessageKey(
                    BarracudaException.DEFAULT_EVENT_KEY);
            return;
        }
        for (String ratingGroup : productRatingGroupList) {
            BootstrapInfo bootstrapInfo = getSessionBootstrapInfo();
            String dabSessionId = bootstrapInfo
                    .getBootstrapHeaderValue("SessionId");
            String dabRatingGroup = ratingGroup;
            String dabPeerName = bootstrapInfo
                    .getBootstrapHeaderValue("PeerName");
            String dabRealm = bootstrapInfo.getBootstrapHeaderValue("Realm");
            String dabhost = bootstrapInfo.getBootstrapHeaderValue("DaBHost");
            String deviceId = getDeviceId();
            String url = getMvnoDabUrl() + "?deviceID=" + deviceId
                    + "&sessionID=" + dabSessionId + "&ratingGroup="
                    + dabRatingGroup + "&peer=" + dabPeerName + "&realm="
                    + dabRealm + "&dabhost=" + dabhost;
            HttpClient httpClient = new HttpClient();
            GetMethod getMethod = new GetMethod(url);
            log.info(MbodActionError.ACCESS_URL, deviceId, url);
            try {
                int status = httpClient.executeMethod(getMethod);
                log.info(MbodActionError.ACCESS_STATUS, deviceId, status, url);
            } catch (HttpException he) {
                log.error(MbodActionError.ACCESS_HTTPEXCEPTION, deviceId, url,
                        he.getMessage());
            } catch (IOException ioe) {
                log.error(MbodActionError.ACCESS_IOEXCEPTION, deviceId, url,
                        ioe.getMessage());
            } finally {
                getMethod.releaseConnection();
            }
        }
    }

    /**
     * @return the mop
     */
    public String getMop() {
        return mop;
    }

    /**
     * @param mop
     *            the mop to set
     */
    public void setMop(String mop) {
        this.mop = mop;
    }

    /**
     * @return the creditCardNumber
     */
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    /**
     * @param creditCardNumber
     *            the creditCardNumber to set
     */
    public void setCreditCardNumber(String creditCardNumber) {
        if (StringUtils.hasText(creditCardNumber)) {
            creditCardNumber = StringUtils.trimAllWhitespace(creditCardNumber);
        }
        this.creditCardNumber = creditCardNumber;
    }

    /**
     * @return the creditCardZip
     */
    public String getCreditCardZip() {
        return creditCardZip;
    }

    /**
     * @param creditCardZip
     *            the creditCardZip to set
     */
    public void setCreditCardZip(String creditCardZip) {
        this.creditCardZip = creditCardZip;
    }

    /**
     * @return the creditCardSecurityCode
     */
    public String getCreditCardSecurityCode() {
        return creditCardSecurityCode;
    }

    /**
     * @param creditCardSecurityCode
     *            the creditCardSecurityCode to set
     */
    public void setCreditCardSecurityCode(String creditCardSecurityCode) {
        this.creditCardSecurityCode = creditCardSecurityCode;
    }

    /**
     * @return the toStore
     */
    public String getToStore() {
        return toStore;
    }

    /**
     * @param toStore
     *            the toStore to set
     */
    public void setToStore(String toStore) {
        this.toStore = toStore;
    }

    /**
     * @return the creditCardExpirationMonth
     */
    public int getCreditCardExpirationMonth() {
        return creditCardExpirationMonth;
    }

    /**
     * @param creditCardExpirationMonth
     *            the creditCardExpirationMonth to set
     */
    public void setCreditCardExpirationMonth(int creditCardExpirationMonth) {
        this.creditCardExpirationMonth = creditCardExpirationMonth;
    }

    /**
     * @return the creditCardExpirationYear
     */
    public int getCreditCardExpirationYear() {
        return creditCardExpirationYear;
    }

    /**
     * @param creditCardExpirationYear
     *            the creditCardExpirationYear to set
     */
    public void setCreditCardExpirationYear(int creditCardExpirationYear) {
        this.creditCardExpirationYear = creditCardExpirationYear;
    }

    /**
     * @return
     * @throws Exception
     */
    public String redeemPromo() throws Exception {

        String code = null;
        if (StringUtils.hasText(promo)) {
            code = promo.trim();
        } else if (StringUtils.hasText(changePromo)) {
            code = changePromo.trim();
        }
        if (code == null) {
            log.error(MbodActionError.PROMOTION_NOCODE, getDeviceId());
            getMbodSession().setErrorMessageKey(
                    BarracudaException
                            .getDisplayKey(MbodActionError.PROMOTION_NOCODE));
            return Action.SUCCESS;
        }
        Product product = mapPromoCodeToProduct(code);
        if (product == null) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT, getDeviceId(), code);
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PROMOTION_NOPRODUCT));
            return Action.SUCCESS;
        }
        getMbodSession().setSelectedProduct(product);
        List<String> promoList = new ArrayList<String>();
        promoList.add(code);
        getMbodSession().setPromoList(promoList);
        log.info(MbodActionError.PROMOTION_PRODUCT_SELECTED, getDeviceId(),
                code, product.getId());
        return Action.SUCCESS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String commitPromo() throws Exception {

        String code = getMbodSession().getPromoList().get(0);
        if (isSupportGroupPromo()) {
            String groupCode = getMvnoGroupPromoCode();
            if (StringUtils.hasText(groupCode)) {
                if (groupCode.equalsIgnoreCase(code)) {
                    log.info(MbodActionError.PROMOTION_REDEEMED, getDeviceId(),
                            code);
                    return Action.SUCCESS;
                }
            }
        }
        Product product = mapPromoCodeToProduct(code);
        if (product == null) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT, getDeviceId(), code);
            getMbodSession()
                    .setErrorMessageKey(
                            BarracudaException
                                    .getDisplayKey(MbodActionError.PROMOTION_NOPRODUCT));
            return Action.SUCCESS;
        }
        commitPromoCode(code);
        purchase();
        access();
        return Action.SUCCESS;
    }

    /**
     * @return the promo
     */
    public String getPromo() {
        return promo;
    }

    /**
     * @param promo
     *            the promo to set
     */
    public void setPromo(String promo) {
        if (StringUtils.hasText(promo)) {
            this.promo = promo.toUpperCase();
        }
    }

    /**
     * @return the changePromo
     */
    public String getChangePromo() {
        return changePromo;
    }

    /**
     * @param changePromo
     *            the changePromo to set
     */
    public void setChangePromo(String changePromo) {
        if (StringUtils.hasText(changePromo)) {
            this.changePromo = changePromo.toUpperCase();
        }
    }

    /**
     * @param code
     * @return
     * @throws Exception
     */
    protected Product mapPromoCodeToProduct(String code) throws Exception {

        if (isSupportGroupPromo()) {
            String groupCode = getMvnoGroupPromoCode();
            if (StringUtils.hasText(groupCode)) {
                if (groupCode.equalsIgnoreCase(code)) {
                    List<Product> productList = getPromotionalProductList();
                    if (!CollectionUtils.isEmpty(productList)) {
                        return productList.get(0);
                    }
                }
            }
        }
        Product product = null;
        try {
            product = getPromoCodeService().getProductForPromo(
                    getMvnoInternalId(), code);
        } catch (PromoCodeServiceException pcse) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT_PCSE, getDeviceId(),
                    code, pcse.getMessage());
        } catch (Exception ex) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT_EXCEPTION,
                    getDeviceId(), code, ex.getMessage());
        }
        return product;
    }

    /**
     * @param code
     * @return
     * @throws Exception
     */
    protected void commitPromoCode(String code) throws Exception {

        try {
            getPromoCodeService().markRedeemed(code);
            log.info(MbodActionError.PROMOTION_REDEEMED, getDeviceId(), code);
            LogPromoPurchaseActivity(code);
            logQualutionHistoryNotes("Redeemed Promo Code (" + code
                    + ") for plan ("
                    + getMbodSession().getSelectedProduct().getName() + ").",
                    HistoryType.PROMOCODEREDEMPTION);
        } catch (PromoCodeServiceException pcse) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT_PCSE, getDeviceId(),
                    code, pcse.getMessage());
        } catch (Exception ex) {
            log.error(MbodActionError.PROMOTION_NOPRODUCT_EXCEPTION,
                    getDeviceId(), code, ex.getMessage());
        }
    }

    /**
     * @return the purchaseLoyaltyReference
     */
    public String getPurchaseLoyaltyReference() {
        return purchaseLoyaltyReference;
    }

    /**
     * @param purchaseLoyaltyReference
     *            the purchaseLoyaltyReference to set
     */
    public void setPurchaseLoyaltyReference(String loyaltyReference) {
        this.purchaseLoyaltyReference = loyaltyReference;
    }

    /**
     * @return
     */
    protected String getPurchaseActivityTransactionId() {

        try {
            StringBuilder sb = new StringBuilder(getDeviceId());
            sb.append("-");
            sb.append(getSession().getID());
            sb.append("-");
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            sb.append(sdf.format(Calendar.getInstance().getTime()));
            return sb.toString();
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * 
     */
    protected void LogCCPurchaseActivity() {

        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(getPurchaseActivityTransactionId());
            pab.setActivity(Activity.PAY_BY_CC);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountChargedToCreditCard());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            } else {
                pab.setUsername(getAnonymousUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 
     */
    protected void LogCCUseBalancePurchaseActivity() {

        String transactionId = getPurchaseActivityTransactionId();
        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(transactionId);
            pab.setActivity(Activity.PAY_BY_ACCT_BAL);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountChargedToBalance());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(transactionId);
            pab.setActivity(Activity.PAY_BY_CC);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountChargedToCreditCard());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            } else {
                pab.setUsername(getAnonymousUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 
     */
    protected void LogBalancePurchaseActivity() {

        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(getPurchaseActivityTransactionId());
            pab.setActivity(Activity.PAY_BY_ACCT_BAL);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountChargedToBalance());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 
     */
    protected void LogBillLaterPurchaseActivity() {

        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(getPurchaseActivityTransactionId());
            pab.setActivity(Activity.BILL_LATER);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setBillToNumber(getMbodSession().getBillToReference());
            pab.setBillToAmount(getMbodSession().getPaymentStatus()
                    .getAmountBilled());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountBilled());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            } else {
                pab.setUsername(getAnonymousUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 
     */
    protected void LogBillLaterUseBalancePurchaseActivity() {

        String transactionId = getPurchaseActivityTransactionId();
        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(transactionId);
            pab.setActivity(Activity.PAY_BY_ACCT_BAL);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountChargedToBalance());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(transactionId);
            pab.setActivity(Activity.BILL_LATER);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setBillToNumber(getMbodSession().getBillToReference());
            pab.setBillToAmount(getMbodSession().getPaymentStatus()
                    .getAmountBilled());
            pab.setAmountProcessed(getMbodSession().getPaymentStatus()
                    .getAmountBilled());
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            } else {
                pab.setUsername(getAnonymousUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    /**
     * 
     */
    protected void LogPromoPurchaseActivity(String code) {

        try {
            PurchaseActivityBean pab = new PurchaseActivityBean();
            pab.setTransactionId(getPurchaseActivityTransactionId());
            pab.setActivity(Activity.PROMO_REDEMPTION);
            pab.setSku(getMbodSession().getSelectedProduct().getSku());
            pab.setProductId(getMbodSession().getSelectedProduct().getId());
            pab.setRentalId(getMbodSession().getRentalId());
            pab.setPromoCodeUsed(code);
            Subscriber subscriber = getMbodSession().getSubscriber();
            if (subscriber != null) {
                pab.setLoyaltyNumber(subscriber.getLoyaltyReference());
                pab.setUsername(subscriber.getUserId());
            } else {
                pab.setUsername(getAnonymousUserId());
            }
            getDeviceService().addPurchaseActivity(getDeviceId(), pab);
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    public void setCcFullName(String ccFullName) {
        this.ccFullName = ccFullName;
    }

    public String getCcFullName() {
        return ccFullName;
    }

    public void setCcAddressLine1(String ccAddressLine1) {
        this.ccAddressLine1 = ccAddressLine1;
    }

    public String getCcAddressLine1() {
        return ccAddressLine1;
    }

    public void setCcAddressLine2(String ccAddressLine2) {
        this.ccAddressLine2 = ccAddressLine2;
    }

    public String getCcAddressLine2() {
        return ccAddressLine2;
    }

    public void setCcCityTown(String ccCityTown) {
        this.ccCityTown = ccCityTown;
    }

    public String getCcCityTown() {
        return ccCityTown;
    }

    public void setCcStateProvinceRegion(String ccStateProvinceRegion) {
        this.ccStateProvinceRegion = ccStateProvinceRegion;
    }

    public String getCcStateProvinceRegion() {
        return ccStateProvinceRegion;
    }

    public void setccCountry(String ccCountry) {
        this.ccCountry = ccCountry;
    }

    public String getccCountry() {
        return ccCountry;
    }

}
