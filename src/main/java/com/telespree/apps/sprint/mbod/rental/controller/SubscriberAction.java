/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.cal10n.LocLogger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.exception.ApplicationException;

import com.opensymphony.xwork2.Action;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.RedeemStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * SubscriberAction
 * 
 * 
 * @author $Author: michael $ on $DateTime: 2013/12/19 19:36:38 $
 * @version $Revision: #41 $
 * 
 */
public class SubscriberAction extends MbodActionSupport {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private static final LocLogger logger = TelespreeLogger
            .getLogger(SubscriberAction.class);

    private String userId;

    private String passwordLogin;

    private String password;

    private String passwordConfirm;

    private String email;

    private String firstName;

    private String lastName;

    private String securityQuestionId;

    private String securityAnswer;

    private String passwordNew;

    private String passwordNewConfirm;

    private String loyaltyReference;

    private String pin;

    private String downloadedOS;

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
    public String login() throws Exception {

        try {
            Subscriber subscriber = getNewBillingService().authenticate(
                    getMvnoExternalId(), userId, passwordLogin,
                    (ApplicationConfiguration) getConfig(), getSession());
            getMbodSession().setSubscriber(subscriber);
            getMbodSession().setAuthenticated(true);
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
    public String signup() throws Exception {

        try {
            Map<String, String> securityQuestionAnswerMap = new HashMap<String, String>();
            securityQuestionAnswerMap.put(securityQuestionId, securityAnswer);
            Address address = new Address();
            address.setPostalCode("91301");// TODO qualution needs this, should
                                           // go to config
            Subscriber subscriber = getNewBillingService().register(
                    getMvnoExternalId(), userId, password,
                    securityQuestionAnswerMap, email, firstName, lastName,
                    address, loyaltyReference,
                    (ApplicationConfiguration) getConfig(), getSession());

            // StringBuilder for notification e-mail body
            StringBuilder sb = new StringBuilder();
            sb.append(getText("mvno.notification.signup.email.hello.text") + " " + subscriber.getUserId() + ", <br /><br />");
            sb.append(getText("mvno.notification.signup.email.text1")
                    + "<br /><br />");
            sb.append(getText("mvno.notification.signup.email.text2.username")
                    + " " + subscriber.getUserId() + "<br /><br />");
            sb.append(getText("mvno.notification.signup.email.text3")
                    + " <a href=\"" + getBookmarkUrl() + "\">"
                    + getBookmarkUrl() + "</a><br /><br />");
            sb.append(getText("mvno.notification.signup.email.text4") + " "
                    + getMvnoCustomerCareNumber());
            logger.debug("E-Mail from: "
                    + getText("mvno.notification.signup.email.from"));
            logger.debug("E-Mail Subject: "
                    + getText("mvno.notification.signup.email.subject"));
            logger.debug("E-Mail Body: \n" + sb.toString());
            try {
                logger.debug("Sending mail to: " + subscriber.getEmail());
                sendNotificationEmail(
                        getText("mvno.notification.signup.email.from"),
                        subscriber.getEmail(),
                        getText("mvno.notification.signup.email.subject"),
                        sb.toString());
            } catch (Exception ex) {
                logger.error(MbodActionError.SUBSCRIBER_WELCOME_EMAIL_FAILURE,
                        getDeviceId(), subscriber.getEmail(), ex.getMessage());
                logger.error(ex.getMessage(), ex);
            }
            // MUST set MBOD session Subscriber before
            // logQualutionHistoryNotes() is called.
            // Otherwise, method will not hit the conditional check
            // that Subscriber in session exists.
            getMbodSession().setSubscriber(subscriber);
            logQualutionHistoryNotes(subscriber.getUserId() + " subscribed.",
                    HistoryType.RENTAL);
            getMbodSession().setNewSignup(true);
            getMbodSession().setAuthenticated(true);
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
    public String forgetpassword() throws Exception {

        try {
            List<String> questionIdList = getNewBillingService()
                    .retrieveSecurityQuestionIdList(getMvnoExternalId(),
                            userId, (ApplicationConfiguration) getConfig(),
                            getSession());
            if (CollectionUtils.isEmpty(questionIdList)) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            Subscriber subscriber = new Subscriber();
            subscriber.setSecurityQuestionId(questionIdList.get(0));
            Map<String, String> map = getSecurityQuestionMap();
            if (CollectionUtils.isEmpty(map)) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            subscriber.setSecurityQuestionText(map.get(subscriber
                    .getSecurityQuestionId()));
            getMbodSession().setSubscriber(subscriber);
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
    public String resetpassword() throws Exception {

        try {
            Map<String, String> securityQuestionIdAnswerMap = new HashMap<String, String>();
            securityQuestionIdAnswerMap.put(securityQuestionId, securityAnswer);
            getNewBillingService().resetPassword(getMvnoExternalId(), userId,
                    securityQuestionIdAnswerMap, passwordNew,
                    (ApplicationConfiguration) getConfig(), getSession());
            logQualutionHistoryNotes("Subscriber reset password",
                    HistoryType.RESETPASSWORD);
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
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        if (StringUtils.hasText(userId)) {
            this.userId = userId.trim().toLowerCase();
        }
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the passwordConfirm
     */
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    /**
     * @param passwordConfirm
     *            the passwordConfirm to set
     */
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName
     *            the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName
     *            the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the securityQuestionId
     */
    public String getSecurityQuestionId() {
        return securityQuestionId;
    }

    /**
     * @param securityQuestionId
     *            the securityQuestionId to set
     */
    public void setSecurityQuestionId(String securityQuestionId) {
        this.securityQuestionId = securityQuestionId;
    }

    /**
     * @return the securityAnswer
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * @param securityAnswer
     *            the securityAnswer to set
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * @return the passwordNew
     */
    public String getPasswordNew() {
        return passwordNew;
    }

    /**
     * @param passwordNew
     *            the passwordNew to set
     */
    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    /**
     * @return the passwordNewConfirm
     */
    public String getPasswordNewConfirm() {
        return passwordNewConfirm;
    }

    /**
     * @param passwordNewConfirm
     *            the passwordNewConfirm to set
     */
    public void setPasswordNewConfirm(String passwordNewConfirm) {
        this.passwordNewConfirm = passwordNewConfirm;
    }

    /**
     * @return the passwordLogin
     */
    public String getPasswordLogin() {
        return passwordLogin;
    }

    /**
     * @param passwordLogin
     *            the passwordLogin to set
     */
    public void setPasswordLogin(String passwordLogin) {
        this.passwordLogin = passwordLogin;
    }

    /**
     * @param loyaltyReference
     */
    public void setLoyaltyReference(String loyaltyReference) {
        this.loyaltyReference = loyaltyReference;
    }

    /**
     * @return
     */
    public String getLoyaltyReference() {
        return loyaltyReference;
    }

    /**
     * @return the pin
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param pin
     *            the pin to set
     */
    public void setPin(String pin) {
        if (StringUtils.hasText(pin)) {
            this.pin = pin.trim();
        }
    }

    public void setDownloadedOS(String downloadedOs) {
        this.downloadedOS = downloadedOs;
    }

    public String getDownloadedOS() {
        return downloadedOS;
    }

    /**
     * @return
     * @throws Exception
     */
    public String redeemPIN() throws Exception {

        try {
            Subscriber subscriber = getMbodSession().getSubscriber();

            if (subscriber == null) {
                // TODO enum and log
                getMbodSession().setErrorMessageKey(
                        BarracudaException.DEFAULT_EVENT_KEY);
                return Action.SUCCESS;
            }
            RedeemStatus redeemStatus = getNewBillingService()
                    .redeemPrepaidCard(getMvnoExternalId(),
                            subscriber.getUserId(), pin,
                            (ApplicationConfiguration) getConfig(),
                            getSession());
            getMbodSession().setRedeemStatus(redeemStatus);
            logQualutionHistoryNotes("Subscriber redeemed PIN: " + pin
                    + ", type: " + redeemStatus.getAmountRedeemed()
                    + ", new balance is: "
                    + getMbodSession().getSubscriber().getAccountBalance(),
                    HistoryType.PINREDEMPTION);
            // TODO log enum and message
        } catch (BillingServiceException bse) {
            getMbodSession().setErrorMessageKey(bse.getDisplayKey());
            logger.info(MbodActionError.REDEEM_PIN_INVALID, getDeviceId(), pin);
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
    public String logDatameterDownload() throws Exception {

        Subscriber subscriber = getMbodSession().getSubscriber();
        if (subscriber != null) {
            logQualutionHistoryNotes("Requested data meter download: "
                    + downloadedOS, HistoryType.RENTAL);
        } else {
            logger.info("Not logging datameter download to qualution. No subscriber authenticated.");
        }
        return Action.SUCCESS;
    }
}
