/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.cal10n.LocLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Device;
import telespree.apps.fwk.common.TelespreeBean;
import telespree.apps.fwk.exception.ApplicationException;
import telespree.apps.fwk.internal.struts2.action.TelespreeActionSupport;
import telespree.apps.fwk.ioc.Chargeable;
import telespree.apps.fwk.ioc.ChargeableAware;
import telespree.apps.fwk.ioc.ConfigAware;
import telespree.apps.fwk.ioc.Loggable;
import telespree.apps.fwk.ioc.LoggableAware;
import telespree.apps.fwk.ioc.TelespreeSessionAware;
import telespree.apps.fwk.ioc.TelespreeSessionManager;
import telespree.apps.fwk.ioc.TelespreeSessionManagerAware;
import telespree.apps.fwk.ism.BootstrapInfo;

import com.telespree.abmf.bean.BalanceBean;
import com.telespree.abmf.exception.CarrierServiceException;
import com.telespree.abmf.exception.DeviceServiceException;
import com.telespree.abmf.service.DeviceState;
import com.telespree.abmf.service.IAccountService;
import com.telespree.abmf.service.ICarrierService;
import com.telespree.abmf.service.IDeviceService;
import com.telespree.apps.sprint.mbod.rental.MbodConstants;
import com.telespree.apps.sprint.mbod.rental.ProductComparator;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.MbodSession;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.apps.sprint.mbod.rental.service.NewBillingService;
import com.telespree.apps.sprint.mbod.rental.service.impl.AuthorizeDotNetService;
import com.telespree.apps.sprint.mbod.rental.util.CryptoUtils;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.log.TelespreeLogger;
import com.telespree.barracuda.product.bean.Product;
import com.telespree.barracuda.product.service.IProductService;
import com.telespree.barracuda.product.service.IPromoCodeService;
import com.telespree.rental.exception.RentalServiceException;
import com.telespree.rental.service.IRentalService;

/**
 * MbodActionSupport
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2014/02/17 15:31:17 $
 * @version $Revision: #77 $
 * 
 */
public abstract class MbodActionSupport extends TelespreeActionSupport
        implements MbodConstants {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger log = TelespreeLogger
            .getLogger(MbodActionSupport.class);

    private IProductService productService;

    private IPromoCodeService promoCodeService;

    private IAccountService accountService;

    private IDeviceService deviceService;

    private ICarrierService carrierService;

    private IRentalService rentalService;

    private NewBillingService newBillingService;

    private JavaMailSenderImpl mailSender;

    private AuthorizeDotNetService authorizeDotNetService;

    /*
     * (non-Javadoc)
     * 
     * @see telespree.apps.fwk.ioc.Authenticatable#isAuthenticated()
     */
    public boolean isAuthenticated() throws ApplicationException {
        return getMbodSession().isAuthenticated();
    }

    /**
     * @return
     * @throws ApplicationException
     */
    public final MbodSession getMbodSession() throws ApplicationException {

        MbodSession mbodSession = null;
        TelespreeBean bean = getSession().getProperty(MbodSession.BEAN_NAME);
        if (bean == null) {
            mbodSession = new MbodSession();
            getSession().setProperty(MbodSession.BEAN_NAME, mbodSession);
            saveSession();
        } else {
            mbodSession = (MbodSession) bean;
        }
        return mbodSession;
    }

    /**
     * 
     * @return
     * @throws ApplicationException
     */
    public boolean isTopup() throws ApplicationException {

        BootstrapInfo bootstrapInfo = getSessionBootstrapInfo();
        if (BootstrapInfo.TRIGGER_ACTION_SELFCARE
                .equalsIgnoreCase(bootstrapInfo.getTriggerAction())) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @return
     * @throws ApplicationException
     */
    public boolean isAddon() throws ApplicationException {

        BootstrapInfo bootstrapInfo = getSessionBootstrapInfo();
        if ("NEEDS_ADDON".equalsIgnoreCase(bootstrapInfo
                .getBootstrapHeaderValue("HotlineReason"))) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @throws ApplicationException
     */
    public Long getAddonBundleId() throws ApplicationException {

        BootstrapInfo bootstrapInfo = getSessionBootstrapInfo();
        try {
            Long value = Long.valueOf(bootstrapInfo
                    .getBootstrapHeaderValue("BundleId"));
            return value;
        } catch (NumberFormatException nfe) {
            // TODO log new enum
            log.error(nfe.getMessage());
        }
        return null;
    }

    /**
     * 
     * @return
     * @throws ApplicationException
     */
    public boolean isReactivate() throws ApplicationException {

        BootstrapInfo bootstrapInfo = getSessionBootstrapInfo();
        if (BootstrapInfo.TRIGGER_ACTION_ACTIVATE
                .equalsIgnoreCase(bootstrapInfo.getTriggerAction())
                && BootstrapInfo.TRIGGER_SUBACTION_REACTIVATE
                        .equalsIgnoreCase(bootstrapInfo.getTriggerSubAction())) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getLanguageSelected() throws Exception {

        String language = Locale.ENGLISH.getLanguage();
        try {
            language = getMbodSession().getLanguage();
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        }
        if (!StringUtils.hasText(language)) {
            language = Locale.ENGLISH.getLanguage();
        }
        return language;
    }

    /**
     * 
     * @param applicationService
     * @throws Exception
     */
    public void setupApplicationService(Object applicationService)
            throws Exception {

        if (applicationService instanceof ConfigAware) {
            ((ConfigAware) applicationService)
                    .setConfig((ApplicationConfiguration) getConfig());
        }
        if (applicationService instanceof TelespreeSessionAware) {
            ((TelespreeSessionAware) applicationService)
                    .setSession(getSession());
        }
        if (applicationService instanceof LoggableAware) {
            ((LoggableAware) applicationService).setLoggable((Loggable) this);
        }
        if (applicationService instanceof ChargeableAware) {
            ((ChargeableAware) applicationService)
                    .setChargeable((Chargeable) this);
        }
        if (applicationService instanceof TelespreeSessionManagerAware) {
            ((TelespreeSessionManagerAware) applicationService)
                    .setTelespreeSessionManager((TelespreeSessionManager) this);
        }
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    /*
     * public String getMbodSessionExpirationString() throws Exception {
     * 
     * String message = "N/A"; // TODO i18n if
     * (getMbodSession().getLastPurchaseSession() != null &&
     * getMbodSession().getLastPurchaseSession() .getBroadbandSession() != null)
     * { BroadbandSession broadbandSession = getMbodSession()
     * .getLastPurchaseSession().getBroadbandSession(); Calendar expiration =
     * broadbandSession.getExpirationTime(); if (expiration != null) { String
     * format = ((ApplicationConfiguration) getConfig())
     * .getTelespreeProperty(PROP_EXPIRATION_FORMAT,
     * PROP_EXPIRATION_FORMAT_DEFAULT, getSession()); SimpleDateFormat
     * simpleDateFormat = new SimpleDateFormat(format); message =
     * simpleDateFormat.format(expiration.getTime()); } } return message; }
     */

    /**
     * @return
     * @throws Exception
     */
    public String getLogoFileName() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(
                PROP_MVNO_IMAGE_LOGO, PROP_MVNO_IMAGE_LOGO_DEFAULT,
                getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getBackgroundFileName() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(
                PROP_MVNO_IMAGE_BACKGROUND, PROP_MVNO_IMAGE_BACKGROUND_DEFAULT,
                getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<Product> getProductList() throws Exception {

        List<Product> productList = getMbodSession().getProductList();
        if (CollectionUtils.isEmpty(productList)) {
            List<Product> list = null;
            if (isAddon()) {
                list = productService.getAddOns(getAddonBundleId());
            } else {
                list = productService.getAllProductList(getMvnoInternalId());
            }
            if (!CollectionUtils.isEmpty(list)) {
                for (Product product : list) {
                    int a = product.getAvailableTo() & BITMASK_SUB;
                    if (a == BITMASK_SUB) {
                        if (CollectionUtils.isEmpty(productList)) {
                            productList = new ArrayList<Product>();
                        }
                        productList.add(product);
                    }
                }
            }
            if (!CollectionUtils.isEmpty(productList)) {
                Collections.sort(productList, new ProductComparator());
            }
            getMbodSession().setProductList(productList);
            saveSession();
        }
        return productList;
    }

    /**
     * @return
     * @throws Exception
     */
    public List<Product> getPromotionalProductList() throws Exception {

        List<Product> productList = getMbodSession()
                .getPromotionalProductList();
        if (CollectionUtils.isEmpty(productList)) {
            productList = productService
                    .getPromotionalProductList(getMvnoInternalId());
            getMbodSession().setPromotionalProductList(productList);
            saveSession();
        }
        return productList;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoName() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(PROP_MVNO_NAME,
                PROP_MVNO_NAME_DEFAULT, getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoExternalId() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(
                PROP_MVNO_EXTERNAL_ID, PROP_MVNO_EXTERNAL_ID_DEFAULT,
                getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoInternalId() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(
                PROP_MVNO_INTERNAL_ID, PROP_MVNO_INTERNAL_ID_DEFAULT,
                getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoTitle() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(PROP_MVNO_TITLE,
                PROP_MVNO_TITLE_DEFAULT, getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoCustomerCareNumber() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String name = applicationConfiguration.getProperty(
                PROP_MVNO_CUSTOMERCARE, PROP_MVNO_CUSTOMERCARE_DEFAULT,
                getSession());
        return name;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoCustomerCareEmail() throws Exception {
        String email = getText("contact.email");
        return email;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoRedirectUrl() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String url = applicationConfiguration.getProperty(
                PROP_MVNO_REDIRECT_URL, PROP_MVNO_REDIRECT_URL_DEFAULT,
                getSession());
        return url;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoDabUrl() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String url = applicationConfiguration.getProperty(PROP_MVNO_DAB_URL,
                PROP_MVNO_DAB_URL_DEFAULT, getSession());
        return url;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoDmWinUrl() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String url = applicationConfiguration.getProperty(PROP_MVNO_DM_WIN_URL,
                PROP_MVNO_DM_WIN_URL_DEFAULT, getSession());
        return url;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoDmMacUrl() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String url = applicationConfiguration.getProperty(PROP_MVNO_DM_MAC_URL,
                PROP_MVNO_DM_MAC_URL_DEFAULT, getSession());
        return url;
    }

    /**
     * @return the productService
     */
    public IProductService getProductService() {
        return productService;
    }

    /**
     * @param productService
     *            the productService to set
     */
    @Autowired
    // @Qualifier("productService")
    @Qualifier("productService")
    public void setProductService(IProductService productService) {
        this.productService = productService;
    }

    /**
     * @return the promoCodeService
     */
    public IPromoCodeService getPromoCodeService() {
        return promoCodeService;
    }

    /**
     * @param promoCodeService
     *            the promoCodeService to set
     */
    @Autowired
    @Qualifier("promoCodeService")
    public void setPromoCodeService(IPromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    /**
     * @return the accountService
     */
    public IAccountService getAccountService() {
        return accountService;
    }

    /**
     * @param accountService
     *            the accountService to set
     */
    @Autowired
    @Qualifier("mailSender")
    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailSender = mailSender;
    }

    public MailSender getMailSender() {
        return mailSender;
    }

    /**
     * @param accountService
     *            the accountService to set
     */
    @Autowired
    @Qualifier("accountService")
    public void setAccountService(IAccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * @return the deviceService
     */
    public IDeviceService getDeviceService() {
        return deviceService;
    }

    /**
     * @param deviceService
     *            the deviceService to set
     */
    @Autowired
    @Qualifier("deviceService")
    public void setDeviceService(IDeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * @return the carrierService
     */
    public ICarrierService getCarrierService() {
        return carrierService;
    }

    /**
     * @param carrierService
     *            the carrierService to set
     */
    @Autowired
    @Qualifier("carrierService")
    public void setCarrierService(ICarrierService carrierService) {
        this.carrierService = carrierService;
    }

    /**
     * @return the rentalService
     */
    public IRentalService getRentalService() {
        return rentalService;
    }

    /**
     * @param rentalService
     *            the rentalService to set
     */
    @Autowired
    @Qualifier("rentalService")
    public void setRentalService(IRentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * @return the newBillingService
     */
    public NewBillingService getNewBillingService() {
        return newBillingService;
    }

    /**
     * @param newBillingService
     *            the newBillingService to set
     */
    @Autowired
    @Qualifier("newQualutionBillingService")
    public void setNewBillingService(NewBillingService newBillingService) {
        this.newBillingService = newBillingService;
    }

    @Autowired
    @Qualifier("authorizeDotNetService")
    public void setAuthorizeDotNetService(
            AuthorizeDotNetService authorizeDotNetService) {
        this.authorizeDotNetService = authorizeDotNetService;
    }

    /**
     * @return
     */
    public AuthorizeDotNetService getAuthorizeDotNetService() {
        return authorizeDotNetService;
    }

    /**
     * @return
     * @throws Exception
     */
    public int tcVersion() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        int value = applicationConfiguration.getProperty(PROP_MVNO_TC_VERSION,
                PROP_MVNO_TC_VERSION_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public int tcAgreedVersion() throws Exception {

        String deviceId = getDeviceId();
        Integer agreedVersion = getMbodSession().getTcAgreedVersion();
        if (agreedVersion != null) {
            /*
             * log.info(MbodActionError.PRODUCT_TC_GET_SESSION, deviceId,
             * agreedVersion);
             */
            return agreedVersion;
        }
        int version = 0;
        try {
            version = getRentalService().getTermsAndConditionsVersion(deviceId);
            getMbodSession().setTcAgreedVersion(version);
            saveSession();
            log.info(MbodActionError.PRODUCT_TC_GET, deviceId, version);
        } catch (RentalServiceException rse) {
            log.error(MbodActionError.PRODUCT_TC_GET_RSE, deviceId,
                    rse.getMessage());
            getMbodSession().setErrorMessageKey(
                    BarracudaException
                            .getDisplayKey(MbodActionError.PRODUCT_TC_GET_RSE));
            saveSession();
        }
        return version;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isShowTC() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_TC_ENFORCE, PROP_MVNO_TC_ENFORCE_DEFAULT,
                getSession());
        if (value) {
            return true;
        }
        DeviceState deviceState = getMbodSession().getDeviceState();
        if (deviceState == DeviceState.SOLD) {
            return false;
        }
        int latest = tcVersion();
        int agreed = tcAgreedVersion();
        if (latest > agreed) {
            return true;
        }
        return false;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportSubscriber() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_SUBSCRIBER,
                PROP_MVNO_SUPPORT_SUBSCRIBER_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportWelcome() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_WELCOME, PROP_MVNO_SUPPORT_WELCOME_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportDataMeter() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_DM, PROP_MVNO_SUPPORT_DM_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportPINRedemption() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_PIN_REDEMPTION,
                PROP_MVNO_SUPPORT_PIN_REDEMPTION_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportPrivacyPolicy() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_SUPPORT_PRIVACY_POLICY,
                PROP_SUPPORT_PRIVACY_POLICY_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getPrivacyPolicyUrl() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_PRIVACY_POLICY_URL, PROP_PRIVACY_POLICY_URL_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public Map<String, String> getSecurityQuestionMap() throws Exception {

        Map<String, String> map = getMbodSession().getSecurityQuestionMap();
        if (CollectionUtils.isEmpty(map)) {
            try {
                map = getCarrierService().getSecurityQuestions(
                        getMvnoInternalId());
            } catch (CarrierServiceException cse) {
                // TODO log enum and message
                log.error(cse.getMessage(), cse);
                throw cse;
            }
            getMbodSession().setSecurityQuestionMap(map);
            saveSession();
        }
        return map;
    }

    /**
     * @return
     * @throws Exception
     */
    public double getDue() throws Exception {

        PaymentStatus paymentStatus = getMbodSession().getPaymentStatus();
        if (paymentStatus != null) {
            return paymentStatus.getAmountStillOwe();
        }
        double due = 0;
        Product product = getMbodSession().getSelectedProduct();
        if (product != null) {
            due += Double.parseDouble(product.getPrice());
        }
        due += getTaxes();
        return due;
    }

    /**
     * @return
     * @throws Exception
     */
    public double getPurchasePrice() throws Exception {

        double due = 0;
        Product product = getMbodSession().getSelectedProduct();
        if (product != null) {
            due = Double.parseDouble(product.getPrice());
        }
        return due;
    }

    /**
     * @return
     * @throws Exception
     */
    public double getPurchasePriceWithTax() throws Exception {

        double due = 0;
        Product product = getMbodSession().getSelectedProduct();
        if (product != null) {
            due = Double.parseDouble(product.getPrice());
        }
        due += getTaxes();
        return due;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getErrorMessage() throws Exception {

        String key = getMbodSession().getErrorMessageKey();
        if (StringUtils.hasText(key)) {
            getMbodSession().setErrorMessageKey(null);
            saveSession();
        }
        return getText(key);
    }

    /**
     * @return
     * @throws Exception
     */
    public String getErrorMessageKey() throws Exception {

        String key = getMbodSession().getErrorMessageKey();
        if (StringUtils.hasText(key)) {
            getMbodSession().setErrorMessageKey(null);
            saveSession();
        }
        return key;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getDeviceId() throws Exception {

        Device device = getSessionDevice();
        String deviceId = null;
        if (device.hasESN()) {
            deviceId = device.getEsnHex();
        } else if (device.hasMeid()) {
            deviceId = device.getMeidHex();
        } else if (device.hasMacAddress()) {
            deviceId = device.getMacAddress();
        } else if (device.hasImsi()) {
            deviceId = device.getImsi();
        } else if (device.hasIccid()) {
            deviceId = device.getIccid();
        }
        return deviceId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * telespree.apps.fwk.internal.struts2.action.TelespreeActionSupport#getText
     * (java.lang.String)
     */
    @Override
    public String getText(String arg0) {

        String language = null;
        try {
            language = getLanguageSelected();
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        if (StringUtils.hasText(language)
                && !Locale.ENGLISH.getLanguage().equalsIgnoreCase(language)) {
            return super.getText(arg0 + "." + language);
        }
        return super.getText(arg0);
    }

    /**
     * @return
     * @throws Exception
     */
    public String getPromoCode() throws Exception {

        List<String> promoList = getMbodSession().getPromoList();
        if (CollectionUtils.isEmpty(promoList)) {
            return null;
        }
        return promoList.get(0);
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportPromo() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_PROMO, PROP_MVNO_SUPPORT_PROMO_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportGroupPromo() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_GROUP_PROMO,
                PROP_MVNO_SUPPORT_GROUP_PROMO_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMvnoGroupPromoCode() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String code = applicationConfiguration.getProperty(
                PROP_MVNO_GROUP_PROMO_CODE, PROP_MVNO_GROUP_PROMO_CODE_DEFAULT,
                getSession());
        return code;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isAllowBillto() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_BILLTO, PROP_MVNO_SUPPORT_BILLTO_DEFAULT,
                getSession());
        boolean hasBillTo = StringUtils.hasText(getMbodSession()
                .getBillToReference());
        boolean expired = getMbodSession().isRentalExpired();
        return value && hasBillTo && !expired;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getBilltoTitle() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_MVNO_BILLTO_TITLE, PROP_MVNO_BILLTO_TITLE_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getBilltoReference() throws Exception {
        return getMbodSession().getBillToReference();
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isAllowPayByBalance() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_PAYBYBALANCE,
                PROP_MVNO_SUPPORT_PAYBYBALANCE_DEFAULT, getSession());
        boolean hasAccountBalance = false;
        Subscriber subscriber = getMbodSession().getSubscriber();
        if (subscriber != null) {
            hasAccountBalance = (subscriber.getAccountBalance() > 0);
        }
        boolean usedBalance = false;
        PaymentStatus paymentStatus = getMbodSession().getPaymentStatus();
        if (paymentStatus != null) {
            usedBalance = paymentStatus.isUsedBalance();
        }
        return (value && hasAccountBalance && !usedBalance);
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportLoyalty() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_LOYALTY, PROP_MVNO_SUPPORT_LOYALTY_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isSupportAdditionalCCData() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_CC_EXTRA, PROP_MVNO_SUPPORT_CC_EXTRA_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isFree() throws Exception {

        boolean free = false;
        Product product = getMbodSession().getSelectedProduct();
        if (product != null) {
            try {
                float price = Float.parseFloat(product.getPrice());
                free = !(price > 0);
            } catch (Exception ex) {
                // TODO log new enum and message
                log.error(ex.getMessage(), ex);
            }
        }
        return free;
    }

    /**
     * @return
     * @throws Exception
     */
    public double getTaxes() throws Exception {

        double taxes = 0;
        Map<String, Double> taxMap = getMbodSession()
                .getSelectedProductTaxMap();
        if (!CollectionUtils.isEmpty(taxMap)) {
            for (String key : taxMap.keySet()) {
                Double tax = taxMap.get(key);
                if (tax != null) {
                    taxes += tax;
                }
            }
        }
        return taxes;
    }

    /**
     * @param historyNotes
     * @param type
     */
    public void logQualutionHistoryNotes(String historyNotes, HistoryType type) {

        try {
            if (getMbodSession().getSubscriber() != null) {
                getNewBillingService().logSubscriberHistory(
                        getMvnoExternalId(),
                        getMbodSession().getSubscriber().getUserId(),
                        historyNotes, getDeviceId(), type,
                        (ApplicationConfiguration) getConfig(), getSession());
            }
        } catch (BillingServiceException bse) {
            // TODO log enum and message
            log.error(bse.getMessage(), bse);
        } catch (ApplicationException ae) {
            // TODO log enum and message
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
    }

    public List<String> getQualutionCCDataBySubscriberId(String appendTag,
            String userId) {

        List<String> subscriberCCData = new ArrayList<String>();
        List<SubscriberHistory> qualFullHistoryList = null;
        try {
            if (getMbodSession().getSubscriber() != null) {
                qualFullHistoryList = getNewBillingService()
                        .getSubscriberHistory(getMvnoExternalId(), userId,
                                (ApplicationConfiguration) getConfig(),
                                getSession());
            }
            for (SubscriberHistory subHist : qualFullHistoryList) {
                if (subHist.getHistoryNotes().startsWith(appendTag)) {
                    subscriberCCData.add(CryptoUtils.decrypt(subHist
                            .getHistoryNotes().substring(appendTag.length()),
                            getCryptoKey(), getCryptoAlgorithm()));
                }
            }
        } catch (BillingServiceException bse) {
            log.error(bse.getMessage(), bse);
        } catch (ApplicationException ae) {
            log.error(ae.getMessage(), ae);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return subscriberCCData;
    }

    public void sendNotificationEmail(String fromSender, String toSender,
            String subject, String mailText) throws MailException,
            MessagingException {

        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg);
        helper.setFrom(fromSender);
        helper.setTo(toSender);
        helper.setSubject(subject);
        msg.setText(mailText, "UTF-8", "html");

        try {
            log.debug("Sending notification e-mail message: " + msg.toString());
            this.mailSender.send(msg);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return
     */
    public String getActiveBalanceBeanJson() {

        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEEE, MMMM dd yyyy 'at' HH:mma z");
        List<BalanceBean> purchasedBalances = null;
        try {
            purchasedBalances = getDeviceService().getPurchaseBalances(
                    getDeviceId());
            if (!CollectionUtils.isEmpty(purchasedBalances)) {
                List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
                for (BalanceBean bb : purchasedBalances) {
                    if ("active".equalsIgnoreCase(bb.getState())
                            && bb.getIncludedDataBytes() < TERABYTES) {
                        Map<String, String> balanceMap = new HashMap<String, String>();
                        balanceMap.put("description", bb.getDescription());
                        Calendar expirationDate = Calendar.getInstance();
                        if (bb.getExpirationDate() != null) {
                            expirationDate.setTimeInMillis(bb
                                    .getExpirationDate());
                            balanceMap.put("expirationDate",
                                    sdf.format(expirationDate.getTime()));
                        }
                        balanceMap.put("state", bb.getState());
                        balanceMap.put("includedData", bb.getIncludedData());
                        balanceMap.put("planName", bb.getName());
                        balanceMap.put("remainingData", bb.getRemainingData());
                        balanceMap.put("sku", bb.getSku());
                        jsonList.add(balanceMap);
                    }
                }
                if (!CollectionUtils.isEmpty(jsonList)) {
                    ObjectMapper jsonMapper = new ObjectMapper();
                    return jsonMapper.writeValueAsString(jsonList);
                }
            }
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        return "null";
    }

    /**
     * @return
     */
    public String getInactiveBalanceBeanJson() {

        SimpleDateFormat sdf = new SimpleDateFormat(
                "EEEE, MMMM dd yyyy 'at' HH:mma z");
        List<BalanceBean> purchasedBalances = null;
        try {
            purchasedBalances = getDeviceService().getPurchaseBalances(
                    getDeviceId());
            if (!CollectionUtils.isEmpty(purchasedBalances)) {
                List<Map<String, String>> jsonList = new ArrayList<Map<String, String>>();
                for (BalanceBean bb : purchasedBalances) {
                    if ("purchased".equalsIgnoreCase(bb.getState())
                            && bb.getIncludedDataBytes() < TERABYTES) {
                        Map<String, String> balanceMap = new HashMap<String, String>();
                        balanceMap.put("description", bb.getDescription());
                        balanceMap.put("state", bb.getState());
                        balanceMap.put("includedData", bb.getIncludedData());
                        balanceMap.put("planName", bb.getName());
                        balanceMap.put("remainingData", bb.getRemainingData());
                        balanceMap.put("sku", bb.getSku());
                        jsonList.add(balanceMap);
                    }
                }
                if (!CollectionUtils.isEmpty(jsonList)) {
                    ObjectMapper jsonMapper = new ObjectMapper();
                    return jsonMapper.writeValueAsString(jsonList);
                }
            }
        } catch (DeviceServiceException dse) {
            // TODO log enum and message
            log.error(dse.getMessage(), dse);
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        return "null";
    }

    /**
     * @return
     * @throws Exception
     */
    public String getRehotlineUrl() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(PROP_REHOTLINE_URL,
                PROP_REHOTLINE_URL_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getBookmarkUrl() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(PROP_BOOKMARK_URL,
                PROP_BOOKMARK_URL_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getCardDefaultZipcode() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_MVNO_DEFAULT_CC_ZIP, PROP_MVNO_DEFAULT_CC_ZIP_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getCardProcessingService() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(PROP_CC_SERVICE,
                PROP_CC_SERVICE_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getCryptoKey() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(PROP_CRYPTO_KEY,
                PROP_CRYPTO_KEY_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getCryptoKeyVersion() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_CRYPTO_KEY_VERSION, PROP_CRYPTO_KEY_VERSION_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getCryptoAlgorithm() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_CRYPTO_ALGORITHM, PROP_CRYPTO_ALGORITHM_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public boolean isCardOnFileAllowed() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        boolean value = applicationConfiguration.getProperty(
                PROP_MVNO_SUPPORT_CC_STORE, PROP_MVNO_SUPPORT_CC_STORE_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     */
    public String getDeviceState() {
        String deviceState = "activate";
        try {
            if (isTopup()) {
                deviceState = "topup";
            } else if (isReactivate()) {
                deviceState = "reactivate";
            }
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
            deviceState = "activate";
        }
        return deviceState;
    }

    /**
     * @return
     */
    public String getProductListJson() {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(getProductList());
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        return "null";
    }

    /**
     * @return
     */
    public String getSecurityQuestionMapJson() {
        try {
            ObjectMapper jsonMapper = new ObjectMapper();
            return jsonMapper.writeValueAsString(getSecurityQuestionMap());
        } catch (Exception ex) {
            // TODO log enum and message
            log.error(ex.getMessage(), ex);
        }
        return "null";
    }

    /**
     * @return
     */
    public boolean detectedMobileUser(String userAgent) {
        try {
            log.debug("User-Agent: " + userAgent);
            if (userAgent != null) {
                ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
                String userAgentRegex = applicationConfiguration.getProperty(
                        PROP_MOBILE_REGEX, PROP_MOBILE_REGEX_DEFAULT,
                        getSession());
                log.debug("User-Agent Regex: " + userAgentRegex);
                if (userAgent.toLowerCase().matches(
                        userAgentRegex.toLowerCase())) {
                    return true;
                }
            } else {
                log.error("Why User-Agent is empty? " + userAgent);
            }
        } catch (Exception ex) {
            log.error("Error checking User-Agent: ", ex);
        }
        return false;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMobileRedirect() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_MOBILE_REDIRECT, PROP_MOBILE_REDIRECT_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getMobileRedirectError() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_MOBILE_REDIRECT_ERROR, PROP_MOBILE_REDIRECT_ERROR_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getDesktopRedirect() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_DESKTOP_REDIRECT, PROP_DESKTOP_REDIRECT_DEFAULT,
                getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getDesktopRedirectError() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_DESKTOP_REDIRECT_ERROR,
                PROP_DESKTOP_REDIRECT_ERROR_DEFAULT, getSession());
        return value;
    }

    /**
     * @return
     * @throws Exception
     */
    public String getAnonymousUserId() throws Exception {
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        String value = applicationConfiguration.getProperty(
                PROP_QUALUTION_BILLING_ANONYMOUS_USERID,
                PROP_QUALUTION_BILLING_ANONYMOUS_USERID_DEFAULT, getSession());
        return value;
    }

}
