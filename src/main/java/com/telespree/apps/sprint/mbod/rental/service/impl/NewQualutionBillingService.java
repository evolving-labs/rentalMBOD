/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.cal10n.LocLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.common.CreditCard;
import telespree.apps.fwk.common.Device;
import telespree.apps.fwk.common.TelespreeBean;
import telespree.apps.fwk.session.TelespreeSession;
import telespree.apps.fwk.session.TelespreeSessionUtil;
import telespree.common.db.DALException;

import com.telespree.apps.sprint.mbod.rental.MbodConstants;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.MbodSession;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.RedeemStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.apps.sprint.mbod.rental.service.NewBillingService;
import com.telespree.barracuda.beans.CreditCardType;
import com.telespree.barracuda.beans.DeviceID;
import com.telespree.barracuda.billing.soap.axis2.AbstractRequestTypeChoice_type0;
import com.telespree.barracuda.billing.soap.axis2.AbstractRequestTypeSequence_type0;
import com.telespree.barracuda.billing.soap.axis2.AddSubscriberHistoryRequest;
import com.telespree.barracuda.billing.soap.axis2.AddSubscriberHistoryRequestType;
import com.telespree.barracuda.billing.soap.axis2.AddSubscriberHistoryResponse;
import com.telespree.barracuda.billing.soap.axis2.AddSubscriberHistoryResponseType;
import com.telespree.barracuda.billing.soap.axis2.AddressType;
import com.telespree.barracuda.billing.soap.axis2.BillingStub;
import com.telespree.barracuda.billing.soap.axis2.CalculateTaxRequest;
import com.telespree.barracuda.billing.soap.axis2.CalculateTaxRequestType;
import com.telespree.barracuda.billing.soap.axis2.CalculateTaxResponse;
import com.telespree.barracuda.billing.soap.axis2.CalculateTaxResponseType;
import com.telespree.barracuda.billing.soap.axis2.CreateSubscriberRequest;
import com.telespree.barracuda.billing.soap.axis2.CreateSubscriberRequestType;
import com.telespree.barracuda.billing.soap.axis2.CreateSubscriberResponse;
import com.telespree.barracuda.billing.soap.axis2.CreateSubscriberResponseType;
import com.telespree.barracuda.billing.soap.axis2.CreditCardExpirationMonthType;
import com.telespree.barracuda.billing.soap.axis2.CreditCardExpirationYearType;
import com.telespree.barracuda.billing.soap.axis2.CreditCardInfoType;
import com.telespree.barracuda.billing.soap.axis2.CreditCardNumberType;
import com.telespree.barracuda.billing.soap.axis2.CreditCardSecurityCodeType;
import com.telespree.barracuda.billing.soap.axis2.EmailAddressType;
import com.telespree.barracuda.billing.soap.axis2.ErrorCodeType;
import com.telespree.barracuda.billing.soap.axis2.ErrorMessageType;
import com.telespree.barracuda.billing.soap.axis2.GetSecurityQuestionListRequest;
import com.telespree.barracuda.billing.soap.axis2.GetSecurityQuestionListRequestType;
import com.telespree.barracuda.billing.soap.axis2.GetSecurityQuestionListResponse;
import com.telespree.barracuda.billing.soap.axis2.GetSecurityQuestionListResponseType;
import com.telespree.barracuda.billing.soap.axis2.GetSubscriberHistoryRequest;
import com.telespree.barracuda.billing.soap.axis2.GetSubscriberHistoryRequestType;
import com.telespree.barracuda.billing.soap.axis2.GetSubscriberHistoryResponse;
import com.telespree.barracuda.billing.soap.axis2.GetSubscriberHistoryResponseType;
import com.telespree.barracuda.billing.soap.axis2.HealthCheckRequest;
import com.telespree.barracuda.billing.soap.axis2.HealthCheckRequestType;
import com.telespree.barracuda.billing.soap.axis2.HealthCheckResponse;
import com.telespree.barracuda.billing.soap.axis2.LastFourDigitsType;
import com.telespree.barracuda.billing.soap.axis2.OperationStatusType;
import com.telespree.barracuda.billing.soap.axis2.PasswordType;
import com.telespree.barracuda.billing.soap.axis2.PostalCodeType;
import com.telespree.barracuda.billing.soap.axis2.PurchaseRequest;
import com.telespree.barracuda.billing.soap.axis2.PurchaseRequestType;
import com.telespree.barracuda.billing.soap.axis2.PurchaseResponse;
import com.telespree.barracuda.billing.soap.axis2.PurchaseResponseType;
import com.telespree.barracuda.billing.soap.axis2.RedeemScratchCardRequest;
import com.telespree.barracuda.billing.soap.axis2.RedeemScratchCardRequestType;
import com.telespree.barracuda.billing.soap.axis2.RedeemScratchCardResponse;
import com.telespree.barracuda.billing.soap.axis2.RedeemScratchCardResponseType;
import com.telespree.barracuda.billing.soap.axis2.ResetSubscriberPasswordRequest;
import com.telespree.barracuda.billing.soap.axis2.ResetSubscriberPasswordRequestType;
import com.telespree.barracuda.billing.soap.axis2.ResetSubscriberPasswordResponse;
import com.telespree.barracuda.billing.soap.axis2.ResetSubscriberPasswordResponseType;
import com.telespree.barracuda.billing.soap.axis2.ScratchCardInfoType;
import com.telespree.barracuda.billing.soap.axis2.SecurityAnswerType;
import com.telespree.barracuda.billing.soap.axis2.SecurityQuestionAnswerType;
import com.telespree.barracuda.billing.soap.axis2.SecurityQuestionIDType;
import com.telespree.barracuda.billing.soap.axis2.StoredCreditCardInfoType;
import com.telespree.barracuda.billing.soap.axis2.SubscriberHistoryEntryType;
import com.telespree.barracuda.billing.soap.axis2.SubscriberHistoryType;
import com.telespree.barracuda.billing.soap.axis2.SubscriberInfoType;
import com.telespree.barracuda.billing.soap.axis2.TaxType;
import com.telespree.barracuda.billing.soap.axis2.UpdateSubscriberRequest;
import com.telespree.barracuda.billing.soap.axis2.UpdateSubscriberRequestType;
import com.telespree.barracuda.billing.soap.axis2.UpdateSubscriberResponse;
import com.telespree.barracuda.billing.soap.axis2.UpdateSubscriberResponseType;
import com.telespree.barracuda.billing.soap.axis2.UserIDType;
import com.telespree.barracuda.billing.soap.axis2.VerifySubscriberRequest;
import com.telespree.barracuda.billing.soap.axis2.VerifySubscriberRequestType;
import com.telespree.barracuda.billing.soap.axis2.VerifySubscriberResponse;
import com.telespree.barracuda.billing.soap.axis2.VerifySubscriberResponseType;
import com.telespree.barracuda.exception.BarracudaException;
import com.telespree.barracuda.exception.DeviceIdException;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * NewQualutionBillingService
 * 
 * 
 * @author $Author: michael $ on $DateTime: 2013/11/04 01:08:54 $
 * @version $Revision: #13 $
 * 
 */
@Service("newQualutionBillingService")
public class NewQualutionBillingService implements NewBillingService,
        MbodConstants {

    private static final LocLogger log = TelespreeLogger
            .getLogger(NewQualutionBillingService.class);

    private long billingStubTimeout;

    private String billingStubUrl;

    private static final String ISO_8859_1 = "ISO-8859-1";

    private static final String UTF_8 = "UTF-8";

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#healthCheck
     * (telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public boolean healthCheck(ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        boolean status = false;
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            HealthCheckRequestType healthCheckRequestType = new HealthCheckRequestType();
            HealthCheckRequest healthCheckRequest = new HealthCheckRequest();
            healthCheckRequest.setHealthCheckRequest(healthCheckRequestType);
            HealthCheckResponse healthCheckResponse = billingStub
                    .healthCheck(healthCheckRequest);
            if (healthCheckResponse == null
                    || healthCheckResponse.getHealthCheckResponse() == null
                    || healthCheckResponse.getHealthCheckResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventCode(
                                        QualutionBillingServiceError.HEALTHCHECK_RESPONSE_INVALID,
                                        (Object[]) null));
                bse.setError(QualutionBillingServiceError.HEALTHCHECK_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.HEALTHCHECK_RESPONSE_INVALID,
                        (Object[]) null);
                throw bse;
            }
            OperationStatusType statusType = healthCheckResponse
                    .getHealthCheckResponse().getStatus();
            if (OperationStatusType.ok.equals(statusType)) {
                status = true;
            }
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.HEALTHCHECK_REMOTE_EXCEPTION,
                            (Object[]) null, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.HEALTHCHECK_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.HEALTHCHECK_REMOTE_EXCEPTION,
                    (Object[]) null, re.getMessage());
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.HEALTHCHECK_EXCEPTION,
                            (Object[]) null, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.HEALTHCHECK_EXCEPTION);
            log.error(QualutionBillingServiceError.HEALTHCHECK_EXCEPTION,
                    (Object[]) null, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
        return status;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * logSubscriberHistory(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String,
     * com.telespree.apps.sprint.mbod.rental.model.HistoryType,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public void logSubscriberHistory(String corpId, String userId,
            String historyNotes, String deviceId, HistoryType historyType,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        Calendar historyTime = Calendar.getInstance();
        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.LOGHISTORY_CORPID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.LOGHISTORY_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.LOGHISTORY_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.LOGHISTORY_USERID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.LOGHISTORY_USERID_EMPTY);
            log.error(QualutionBillingServiceError.LOGHISTORY_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (historyType == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.LOGHISTORY_HISTORYTYPE_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.LOGHISTORY_HISTORYTYPE_EMPTY);
            log.error(
                    QualutionBillingServiceError.LOGHISTORY_HISTORYTYPE_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(historyNotes)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.LOGHISTORY_HISTORYNOTES_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.LOGHISTORY_HISTORYNOTES_EMPTY);
            log.error(
                    QualutionBillingServiceError.LOGHISTORY_HISTORYNOTES_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            AddSubscriberHistoryRequestType addSubscriberHistoryRequestType = new AddSubscriberHistoryRequestType();
            SubscriberHistoryEntryType subscriberHistoryEntryType = new SubscriberHistoryEntryType();
            subscriberHistoryEntryType.setHistoryNotes(historyNotes);
            switch (historyType) {
            case RENTAL:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.rental);
                break;
            case PAYBYCREDITCARD:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.payByCreditCard);
                break;
            case PROMOCODEREDEMPTION:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.promoCodeRedemption);
                break;
            case PINREDEMPTION:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.pinRedemption);
                break;
            case PAYBYACCOUNTBALANCE:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.payByAccountBalance);
                break;
            case UPDATEPROFILE:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.updateProfile);
                break;
            case RESETPASSWORD:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.resetPassword);
                break;
            default:
                subscriberHistoryEntryType
                        .setHistoryType(SubscriberHistoryType.rental);
            }
            subscriberHistoryEntryType.setHistoryTime(historyTime);
            subscriberHistoryEntryType.setDeviceID(deviceId);
            addSubscriberHistoryRequestType
                    .addSubscriberHistoryEntry(subscriberHistoryEntryType);
            addSubscriberHistoryRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            addSubscriberHistoryRequestType.setUserID(userIDType);
            AddSubscriberHistoryRequest addSubscriberHistoryRequest = new AddSubscriberHistoryRequest();
            addSubscriberHistoryRequest
                    .setAddSubscriberHistoryRequest(addSubscriberHistoryRequestType);
            AddSubscriberHistoryResponse addSubscriberHistoryResponse = billingStub
                    .addSubscriberHistory(addSubscriberHistoryRequest);
            if (addSubscriberHistoryResponse == null
                    || addSubscriberHistoryResponse
                            .getAddSubscriberHistoryResponse() == null
                    || addSubscriberHistoryResponse
                            .getAddSubscriberHistoryResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.LOGHISTORY_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            AddSubscriberHistoryResponseType addSubscriberHistoryResponseType = addSubscriberHistoryResponse
                    .getAddSubscriberHistoryResponse();
            OperationStatusType statusType = addSubscriberHistoryResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = addSubscriberHistoryResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = addSubscriberHistoryResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.LOGHISTORY_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.LOGHISTORY_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.LOGHISTORY_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            log.info(QualutionBillingServiceError.LOGHISTORY_SUCCESS, deviceId,
                    userId);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.LOGHISTORY_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.LOGHISTORY_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.LOGHISTORY_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.LOGHISTORY_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.LOGHISTORY_EXCEPTION);
            log.error(QualutionBillingServiceError.LOGHISTORY_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * getSubscriberHistory(java.lang.String, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public List<SubscriberHistory> getSubscriberHistory(String corpId,
            String userId, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        ArrayList<SubscriberHistory> historyList = new ArrayList<SubscriberHistory>();
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.GETHISTORY_CORPID_EMPTY,
                                    ""));
            bse.setError(QualutionBillingServiceError.GETHISTORY_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.GETHISTORY_CORPID_EMPTY, "");
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.GETHISTORY_USERID_EMPTY,
                                    ""));
            bse.setError(QualutionBillingServiceError.GETHISTORY_USERID_EMPTY);
            log.error(QualutionBillingServiceError.GETHISTORY_USERID_EMPTY, "");
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            GetSubscriberHistoryRequestType getSubscriberHistoryRequestType = new GetSubscriberHistoryRequestType();
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            getSubscriberHistoryRequestType.setUserID(userIDType);
            getSubscriberHistoryRequestType.setCorpID(corpId);
            GetSubscriberHistoryRequest getSubscriberHistoryRequest = new GetSubscriberHistoryRequest();
            getSubscriberHistoryRequest
                    .setGetSubscriberHistoryRequest(getSubscriberHistoryRequestType);
            GetSubscriberHistoryResponse getSubscriberHistoryResponse = billingStub
                    .getSubscriberHistory(getSubscriberHistoryRequest);
            if (getSubscriberHistoryResponse == null
                    || getSubscriberHistoryResponse
                            .getGetSubscriberHistoryResponse() == null
                    || getSubscriberHistoryResponse
                            .getGetSubscriberHistoryResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.GETHISTORY_RESPONSE_INVALID,
                                        ""));
                bse.setError(QualutionBillingServiceError.GETHISTORY_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.GETHISTORY_RESPONSE_INVALID,
                        "");
                throw bse;
            }
            GetSubscriberHistoryResponseType getSubscriberHistoryResponseType = getSubscriberHistoryResponse
                    .getGetSubscriberHistoryResponse();
            OperationStatusType statusType = getSubscriberHistoryResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = getSubscriberHistoryResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = getSubscriberHistoryResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.GETHISTORY_RESPONSE_FAILURE,
                                        "", statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.GETHISTORY_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.GETHISTORY_RESPONSE_FAILURE,
                        "", statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.GETHISTORY_RESPONSE_HASCODE,
                                        "", errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.GETHISTORY_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.GETHISTORY_RESPONSE_HASCODE,
                        "", errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberHistoryEntryType[] subscriberHistoryEntryTypeArray = getSubscriberHistoryResponseType
                    .getSubscriberHistoryEntry();
            if (ArrayUtils.isEmpty(subscriberHistoryEntryTypeArray)) {
                log.info(QualutionBillingServiceError.GETHISTORY_NOHISTORY, "",
                        userId);
            } else {
                log.info(QualutionBillingServiceError.GETHISTORY_SUCCESS, "",
                        userId);
                for (SubscriberHistoryEntryType entry : subscriberHistoryEntryTypeArray) {
                    SubscriberHistory history = new SubscriberHistory();
                    history.setDeviceId(entry.getDeviceID());
                    history.setHistoryNotes(entry.getHistoryNotes());
                    history.setHistoryType(entry.getHistoryType().toString());
                    history.setHistoryTime(entry.getHistoryTime());
                    history.setUserId(userId);
                    historyList.add(history);
                }
            }
            return historyList;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GETHISTORY_REMOTE_EXCEPTION,
                            "", re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.GETHISTORY_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.GETHISTORY_REMOTE_EXCEPTION,
                    "", re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GETHISTORY_EXCEPTION,
                            "", ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.GETHISTORY_EXCEPTION);
            log.error(QualutionBillingServiceError.GETHISTORY_EXCEPTION, "",
                    ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#authenticate
     * (java.lang.String, java.lang.String, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public Subscriber authenticate(String corpId, String userId,
            String password, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.AUTHENTICATE_CORPID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.AUTHENTICATE_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.AUTHENTICATE_USERID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_USERID_EMPTY);
            log.error(QualutionBillingServiceError.AUTHENTICATE_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(password)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.AUTHENTICATE_PASSWORD_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_PASSWORD_EMPTY);
            log.error(QualutionBillingServiceError.AUTHENTICATE_PASSWORD_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            VerifySubscriberRequestType verifySubscriberRequestType = new VerifySubscriberRequestType();
            verifySubscriberRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            PasswordType passwordType = new PasswordType();
            passwordType.setPasswordType(password);
            verifySubscriberRequestType.setUserID(userIDType);
            verifySubscriberRequestType.setPassword(passwordType);
            VerifySubscriberRequest verifySubscriberRequest = new VerifySubscriberRequest();
            verifySubscriberRequest
                    .setVerifySubscriberRequest(verifySubscriberRequestType);
            VerifySubscriberResponse verifySubscriberResponse = billingStub
                    .verifySubscriber(verifySubscriberRequest);
            if (verifySubscriberResponse == null
                    || verifySubscriberResponse.getVerifySubscriberResponse() == null
                    || verifySubscriberResponse.getVerifySubscriberResponse()
                            .getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            VerifySubscriberResponseType verifySubscriberResponseType = verifySubscriberResponse
                    .getVerifySubscriberResponse();
            OperationStatusType statusType = verifySubscriberResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = verifySubscriberResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = verifySubscriberResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = verifySubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_NOSUB,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_NOSUB);
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_NOSUB,
                        deviceId);
                throw bse;
            }
            log.info(QualutionBillingServiceError.AUTHENTICATE_SUCCESS,
                    deviceId, userId);
            return mapSubscriberInfo(subscriberInfoType, telespreeSession);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.AUTHENTICATE_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.AUTHENTICATE_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.AUTHENTICATE_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_EXCEPTION);
            log.error(QualutionBillingServiceError.AUTHENTICATE_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#register
     * (java.lang.String, java.lang.String, java.lang.String, java.util.Map,
     * java.lang.String, telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public Subscriber register(String corpId, String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {
        return register(corpId, userId, password, securityQuestionIdAnswerMap,
                email, null, null, null, null, appConfiguration,
                telespreeSession);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#register
     * (java.lang.String, java.lang.String, java.lang.String, java.util.Map,
     * java.lang.String, java.lang.String, java.lang.String,
     * telespree.apps.fwk.common.Address, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public Subscriber register(String corpId, String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email,
            String firstName, String lastName, Address address,
            String loyaltyReference, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String encFirstName = encodeToCharSet(firstName, ISO_8859_1);
        String encLastName = encodeToCharSet(lastName, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.REGISTER_CORPID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.REGISTER_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.REGISTER_USERID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.REGISTER_USERID_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(password)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.REGISTER_PASSWORD_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.REGISTER_PASSWORD_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_PASSWORD_EMPTY,
                    deviceId);
            throw bse;
        }
        if (CollectionUtils.isEmpty(securityQuestionIdAnswerMap)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.REGISTER_SECURITY_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.REGISTER_SECURITY_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_SECURITY_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(email)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.REGISTER_EMAIL_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.REGISTER_EMAIL_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_EMAIL_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            CreateSubscriberRequestType createSubscriberRequestType = new CreateSubscriberRequestType();
            createSubscriberRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            PasswordType passwordType = new PasswordType();
            passwordType.setPasswordType(password);
            createSubscriberRequestType.setUserID(userIDType);
            createSubscriberRequestType.setPassword(passwordType);
            for (String key : securityQuestionIdAnswerMap.keySet()) {
                String value = securityQuestionIdAnswerMap.get(key);
                SecurityQuestionAnswerType securityQuestionAnswerType = new SecurityQuestionAnswerType();
                SecurityQuestionIDType securityQuestionIDType = new SecurityQuestionIDType();
                securityQuestionIDType.setSecurityQuestionIDType(key);
                SecurityAnswerType securityAnswerType = new SecurityAnswerType();
                securityAnswerType.setSecurityAnswerType(encodeToCharSet(value,
                        ISO_8859_1));
                securityQuestionAnswerType
                        .setQuestionID(securityQuestionIDType);
                securityQuestionAnswerType.setAnswer(securityAnswerType);
                createSubscriberRequestType
                        .addSecurityQuestionAnswer(securityQuestionAnswerType);
                break; // only send first one for now
            }
            EmailAddressType emailAddressType = new EmailAddressType();
            emailAddressType.setEmailAddressType(email);
            createSubscriberRequestType.setEmail(emailAddressType);
            if (StringUtils.hasText(encFirstName)) {
                createSubscriberRequestType.setFirstName(encFirstName);
            }
            if (StringUtils.hasText(encLastName)) {
                createSubscriberRequestType.setLastName(encLastName);
            }
            if (StringUtils.hasText(loyaltyReference)) {
                createSubscriberRequestType
                        .setLoyaltyReference(loyaltyReference);
            }
            if (address != null && StringUtils.hasText(address.getPostalCode())) {
                AddressType addressType = new AddressType();
                PostalCodeType postalCodeType = new PostalCodeType();
                postalCodeType.setPostalCodeType(address.getPostalCode());
                addressType.setPostalCode(postalCodeType);
                createSubscriberRequestType.setAddress(addressType);
            }
            CreateSubscriberRequest createSubscriberRequest = new CreateSubscriberRequest();
            createSubscriberRequest
                    .setCreateSubscriberRequest(createSubscriberRequestType);
            CreateSubscriberResponse createSubscriberResponse = billingStub
                    .createSubscriber(createSubscriberRequest);
            if (createSubscriberResponse == null
                    || createSubscriberResponse.getCreateSubscriberResponse() == null
                    || createSubscriberResponse.getCreateSubscriberResponse()
                            .getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.REGISTER_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            CreateSubscriberResponseType createSubscriberResponseType = createSubscriberResponse
                    .getCreateSubscriberResponse();
            OperationStatusType statusType = createSubscriberResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = createSubscriberResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = createSubscriberResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.REGISTER_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.REGISTER_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = createSubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.REGISTER_RESPONSE_NOSUB,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_NOSUB);
                log.error(QualutionBillingServiceError.REGISTER_RESPONSE_NOSUB,
                        deviceId);
                throw bse;
            }
            log.info(QualutionBillingServiceError.REGISTER_SUCCESS, deviceId,
                    userId);
            return mapSubscriberInfo(subscriberInfoType, telespreeSession);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.REGISTER_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.REGISTER_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.REGISTER_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.REGISTER_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.REGISTER_EXCEPTION);
            log.error(QualutionBillingServiceError.REGISTER_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#update
     * (java.lang.String,
     * com.telespree.apps.sprint.mbod.rental.model.Subscriber,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public Subscriber update(String corpId, Subscriber subscriber,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String userId = subscriber.getUserId();
        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String securityQuestionId = subscriber.getSecurityQuestionId();
        String securityAnswer = subscriber.getSecurityAnswer();
        String email = subscriber.getEmail();
        String firstName = subscriber.getFirstName();
        String lastName = subscriber.getLastName();
        String encFirstName = encodeToCharSet(firstName, ISO_8859_1);
        String encLastName = encodeToCharSet(lastName, ISO_8859_1);
        String loyaltyReference = subscriber.getLoyaltyReference();
        Address address = subscriber.getAddress();
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.UPDATE_CORPID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.UPDATE_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.UPDATE_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.UPDATE_USERID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.UPDATE_USERID_EMPTY);
            log.error(QualutionBillingServiceError.UPDATE_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(email)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.UPDATE_EMAIL_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.UPDATE_EMAIL_EMPTY);
            log.error(QualutionBillingServiceError.UPDATE_EMAIL_EMPTY, deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            UpdateSubscriberRequestType updateSubscriberRequestType = new UpdateSubscriberRequestType();
            updateSubscriberRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            updateSubscriberRequestType.setUserID(userIDType);
            if (StringUtils.hasText(securityQuestionId)
                    && StringUtils.hasText(securityAnswer)) {
                SecurityQuestionAnswerType securityQuestionAnswerType = new SecurityQuestionAnswerType();
                SecurityQuestionIDType securityQuestionIDType = new SecurityQuestionIDType();
                securityQuestionIDType
                        .setSecurityQuestionIDType(securityQuestionId);
                SecurityAnswerType securityAnswerType = new SecurityAnswerType();
                securityAnswerType.setSecurityAnswerType(encodeToCharSet(
                        securityAnswer, ISO_8859_1));
                securityQuestionAnswerType
                        .setQuestionID(securityQuestionIDType);
                securityQuestionAnswerType.setAnswer(securityAnswerType);
                updateSubscriberRequestType
                        .addSecurityQuestionAnswer(securityQuestionAnswerType);
            }
            EmailAddressType emailAddressType = new EmailAddressType();
            emailAddressType.setEmailAddressType(email);
            updateSubscriberRequestType.setEmail(emailAddressType);
            if (StringUtils.hasText(encFirstName)) {
                updateSubscriberRequestType.setFirstName(encFirstName);
            }
            if (StringUtils.hasText(encLastName)) {
                updateSubscriberRequestType.setLastName(encLastName);
            }
            if (StringUtils.hasText(loyaltyReference)) {
                updateSubscriberRequestType
                        .setLoyaltyReference(loyaltyReference);
            }
            if (address != null && StringUtils.hasText(address.getPostalCode())) {
                AddressType addressType = new AddressType();
                PostalCodeType postalCodeType = new PostalCodeType();
                postalCodeType.setPostalCodeType(address.getPostalCode());
                addressType.setPostalCode(postalCodeType);
                updateSubscriberRequestType.setAddress(addressType);
            }
            UpdateSubscriberRequest updateSubscriberRequest = new UpdateSubscriberRequest();
            updateSubscriberRequest
                    .setUpdateSubscriberRequest(updateSubscriberRequestType);
            UpdateSubscriberResponse updateSubscriberResponse = billingStub
                    .updateSubscriber(updateSubscriberRequest);
            if (updateSubscriberResponse == null
                    || updateSubscriberResponse.getUpdateSubscriberResponse() == null
                    || updateSubscriberResponse.getUpdateSubscriberResponse()
                            .getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.UPDATE_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            UpdateSubscriberResponseType updateSubscriberResponseType = updateSubscriberResponse
                    .getUpdateSubscriberResponse();
            OperationStatusType statusType = updateSubscriberResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = updateSubscriberResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = updateSubscriberResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.UPDATE_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.UPDATE_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = updateSubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.UPDATE_RESPONSE_NOSUB,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_NOSUB);
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_NOSUB,
                        deviceId);
                throw bse;
            }
            log.info(QualutionBillingServiceError.UPDATE_SUCCESS, deviceId,
                    userId);
            return mapSubscriberInfo(subscriberInfoType, telespreeSession);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.UPDATE_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.UPDATE_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.UPDATE_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.UPDATE_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.UPDATE_EXCEPTION);
            log.error(QualutionBillingServiceError.UPDATE_EXCEPTION, deviceId,
                    ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * retrieveSecurityQuestionIdList(java.lang.String, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public List<String> retrieveSecurityQuestionIdList(String corpId,
            String userId, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.SECURITYQUESTION_CORPID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_CORPID_EMPTY);
            log.error(
                    QualutionBillingServiceError.SECURITYQUESTION_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.SECURITYQUESTION_USERID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_USERID_EMPTY);
            log.error(
                    QualutionBillingServiceError.SECURITYQUESTION_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            GetSecurityQuestionListRequestType getSecurityQuestionListRequestType = new GetSecurityQuestionListRequestType();
            getSecurityQuestionListRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            getSecurityQuestionListRequestType.setUserID(userIDType);
            GetSecurityQuestionListRequest getSecurityQuestionListRequest = new GetSecurityQuestionListRequest();
            getSecurityQuestionListRequest
                    .setGetSecurityQuestionListRequest(getSecurityQuestionListRequestType);
            GetSecurityQuestionListResponse getSecurityQuestionListResponse = billingStub
                    .getSecurityQuestionList(getSecurityQuestionListRequest);
            if (getSecurityQuestionListResponse == null
                    || getSecurityQuestionListResponse
                            .getGetSecurityQuestionListResponse() == null
                    || getSecurityQuestionListResponse
                            .getGetSecurityQuestionListResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            GetSecurityQuestionListResponseType getSecurityQuestionListResponseType = getSecurityQuestionListResponse
                    .getGetSecurityQuestionListResponse();
            OperationStatusType statusType = getSecurityQuestionListResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = getSecurityQuestionListResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = getSecurityQuestionListResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SecurityQuestionIDType[] securityQuestionIDs = getSecurityQuestionListResponseType
                    .getSecurityQuestionID();
            if (ArrayUtils.isEmpty(securityQuestionIDs)) {
                log.info(
                        QualutionBillingServiceError.SECURITYQUESTION_SUCCESS_ZERO,
                        deviceId, userId);
                return null;
            }
            List<String> questionIdList = new ArrayList<String>(
                    securityQuestionIDs.length);
            for (SecurityQuestionIDType securityQuestionIDType : securityQuestionIDs) {
                questionIdList.add(securityQuestionIDType
                        .getSecurityQuestionIDType());
            }
            log.info(QualutionBillingServiceError.SECURITYQUESTION_SUCCESS,
                    deviceId, userId);
            return questionIdList;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.SECURITYQUESTION_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.SECURITYQUESTION_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.SECURITYQUESTION_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_EXCEPTION);
            log.error(QualutionBillingServiceError.SECURITYQUESTION_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#resetPassword
     * (java.lang.String, java.lang.String, java.util.Map, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public void resetPassword(String corpId, String userId,
            Map<String, String> securityQuestionIdAnswerMap,
            String newPassword, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.RESETPASSWORD_CORPID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.RESETPASSWORD_CORPID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.RESETPASSWORD_USERID_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_USERID_EMPTY);
            log.error(QualutionBillingServiceError.RESETPASSWORD_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(newPassword)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.RESETPASSWORD_PASSWORD_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_PASSWORD_EMPTY);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_PASSWORD_EMPTY,
                    deviceId);
            throw bse;
        }
        if (CollectionUtils.isEmpty(securityQuestionIdAnswerMap)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.RESETPASSWORD_SECURITY_EMPTY,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_SECURITY_EMPTY);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_SECURITY_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            ResetSubscriberPasswordRequestType resetSubscriberPasswordRequestType = new ResetSubscriberPasswordRequestType();
            resetSubscriberPasswordRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            PasswordType passwordType = new PasswordType();
            passwordType.setPasswordType(newPassword);
            resetSubscriberPasswordRequestType.setUserID(userIDType);
            resetSubscriberPasswordRequestType.setNewPassword(passwordType);
            for (String key : securityQuestionIdAnswerMap.keySet()) {
                String value = securityQuestionIdAnswerMap.get(key);
                SecurityQuestionAnswerType securityQuestionAnswerType = new SecurityQuestionAnswerType();
                SecurityQuestionIDType securityQuestionIDType = new SecurityQuestionIDType();
                securityQuestionIDType.setSecurityQuestionIDType(key);
                SecurityAnswerType securityAnswerType = new SecurityAnswerType();
                securityAnswerType.setSecurityAnswerType(encodeToCharSet(value,
                        ISO_8859_1));
                securityQuestionAnswerType
                        .setQuestionID(securityQuestionIDType);
                securityQuestionAnswerType.setAnswer(securityAnswerType);
                resetSubscriberPasswordRequestType
                        .addSecurityQuestionAnswer(securityQuestionAnswerType);
                break; // only send first one for now
            }
            ResetSubscriberPasswordRequest resetSubscriberPsswordRequest = new ResetSubscriberPasswordRequest();
            resetSubscriberPsswordRequest
                    .setResetSubscriberPasswordRequest(resetSubscriberPasswordRequestType);
            ResetSubscriberPasswordResponse resetSubscriberPasswordResponse = billingStub
                    .resetSubscriberPassword(resetSubscriberPsswordRequest);
            if (resetSubscriberPasswordResponse == null
                    || resetSubscriberPasswordResponse
                            .getResetSubscriberPasswordResponse() == null
                    || resetSubscriberPasswordResponse
                            .getResetSubscriberPasswordResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventCode(
                                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            ResetSubscriberPasswordResponseType resetSubscriberPasswordResponseType = resetSubscriberPasswordResponse
                    .getResetSubscriberPasswordResponse();
            OperationStatusType statusType = resetSubscriberPasswordResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = resetSubscriberPasswordResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = resetSubscriberPasswordResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = resetSubscriberPasswordResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_NOSUB,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_NOSUB);
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_NOSUB,
                        deviceId);
                throw bse;
            }
            log.info(QualutionBillingServiceError.RESETPASSWORD_SUCCESS,
                    deviceId, userId);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.RESETPASSWORD_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.RESETPASSWORD_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_EXCEPTION);
            log.error(QualutionBillingServiceError.RESETPASSWORD_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.NewBillingService#payByBalance
     * (java.lang.String, java.lang.String, double, java.lang.String,
     * java.util.List, telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public PaymentStatus payByBalance(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty corp ID.");
            // TODO set enum and log
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            // TODO set enum and log
            throw bse;
        }
        if (!StringUtils.hasText(planId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty plan ID.");
            // TODO set enum and log
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    "Payment amount less than or equal to zero.");
            // TODO set enum and log
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            purchaseRequestType.setUserID(userIDType);
            purchaseRequestType.setPlanID(planId);
            if (!CollectionUtils.isEmpty(promoCodeList)) {
                String[] promoCodeArray = (String[]) promoCodeList.toArray();
                purchaseRequestType.setPromoteCode(promoCodeArray);
            }
            purchaseRequestType.setAmount((float) amount);
            purchaseRequestType.setUseBalance(true);
            PurchaseRequest purchaseRequest = new PurchaseRequest();
            purchaseRequest.setPurchaseRequest(purchaseRequestType);
            PurchaseResponse purchaseResponse = billingStub
                    .purchase(purchaseRequest);
            if (purchaseResponse == null
                    || purchaseResponse.getPurchaseResponse() == null
                    || purchaseResponse.getPurchaseResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution returned invalid PurchaseByBalance response.");
                // TODO set enum and log
                throw bse;
            }
            PurchaseResponseType purchaseResponseType = purchaseResponse
                    .getPurchaseResponse();
            OperationStatusType statusType = purchaseResponseType.getStatus();
            ErrorCodeType errorCodeType = purchaseResponseType.getErrorCode();
            ErrorMessageType errorMessageType = purchaseResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByBalance failed.");
                // TODO set enum and log
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByBalance returned error code.");
                // TODO set enum and log
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = purchaseResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByBalance did not return subscriber information.");
                // TODO set enum and log
                throw bse;
            }
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType,
                    telespreeSession);
            getMbodSession(telespreeSession).setSubscriber(subscriber);
            saveTelespreeSession(telespreeSession);
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setAmountChargedToBalance(purchaseResponseType
                    .getAmountChargedToBalance());
            paymentStatus.setAmountChargedToCreditCard(purchaseResponseType
                    .getAmountChargedToCreditCard());
            paymentStatus.setAmountStillOwe(purchaseResponseType
                    .getAmountStillOwe());
            paymentStatus.setCreditCardAuthorizationCode(purchaseResponseType
                    .getCreditCardAuthorizationCode());
            paymentStatus.setTransactionTime(purchaseResponseType
                    .getTransactionTime());
            return paymentStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            // TODO set enum and log
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            // TODO set enum and log
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * payByCreditCard(java.lang.String, java.lang.String, double,
     * java.lang.String, java.util.List, telespree.apps.fwk.common.CreditCard,
     * boolean, java.lang.String, boolean, java.lang.String, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public PaymentStatus payByCreditCard(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            CreditCard creditCard, boolean storeCreditCard, String taxZipcode,
            boolean useBalance, String cardHolderName,
            String cardHolderCountry,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_CORPID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.CC_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.CC_CORPID_EMPTY, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            try {
                encUserId = appConfiguration.getProperty(
                        PROP_QUALUTION_BILLING_ANONYMOUS_USERID,
                        PROP_QUALUTION_BILLING_ANONYMOUS_USERID_DEFAULT,
                        telespreeSession);
                log.debug("Guest purchase made by anonymous user: " + encUserId
                        + " using MVNO ID: " + corpId);
                if (!StringUtils.hasText(encUserId)) {
                    BillingServiceException bse = new BillingServiceException(
                            TelespreeLogger
                                    .getEventMessage(
                                            QualutionBillingServiceError.CC_ANONYMOUS_USERID_EMPTY,
                                            deviceId));
                    bse.setError(QualutionBillingServiceError.CC_ANONYMOUS_USERID_EMPTY);
                    log.error(
                            QualutionBillingServiceError.CC_ANONYMOUS_USERID_EMPTY,
                            deviceId);
                    throw bse;
                }
            } catch (Exception ex) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger.getEventMessage(
                                QualutionBillingServiceError.CC_ANONYMOUS_USERID_EXCEPTION,
                                deviceId, ex.getMessage()), ex);
                bse.setError(QualutionBillingServiceError.CC_ANONYMOUS_USERID_EXCEPTION);
                log.error(
                        QualutionBillingServiceError.CC_ANONYMOUS_USERID_EXCEPTION,
                        deviceId, ex.getMessage());
                throw bse;
            }
        }
        if (!StringUtils.hasText(planId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_PLANID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.CC_PLANID_EMPTY);
            log.error(QualutionBillingServiceError.CC_PLANID_EMPTY, deviceId);
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_AMOUNT_INVALID,
                            deviceId));
            bse.setError(QualutionBillingServiceError.CC_AMOUNT_INVALID);
            log.error(QualutionBillingServiceError.CC_AMOUNT_INVALID, deviceId);
            throw bse;
        }
        if (creditCard == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_NO, deviceId));
            bse.setError(QualutionBillingServiceError.CC_NO);
            log.error(QualutionBillingServiceError.CC_NO, deviceId);
            throw bse;
        }
        com.telespree.barracuda.beans.CreditCardType ccType = com.telespree.barracuda.beans.CreditCardType
                .getTypeByNumber(creditCard.getNumber());
        if (ccType == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_TYPE_INVALID,
                            deviceId));
            bse.setError(QualutionBillingServiceError.CC_TYPE_INVALID);
            log.error(QualutionBillingServiceError.CC_TYPE_INVALID, deviceId);
            throw bse;

        }
        if (creditCard.getBillingAddress() == null
                || !StringUtils.hasText(creditCard.getBillingAddress()
                        .getPostalCode())) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger
                            .getEventMessage(
                                    QualutionBillingServiceError.CC_NOBILLING,
                                    deviceId));
            bse.setError(QualutionBillingServiceError.CC_NOBILLING);
            log.error(QualutionBillingServiceError.CC_NOBILLING, deviceId);
            throw bse;
        }
        Address address = creditCard.getBillingAddress();
        if (!StringUtils.hasText(creditCard.getSecurityCode())) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_NOCODE, deviceId));
            bse.setError(QualutionBillingServiceError.CC_NOCODE);
            log.error(QualutionBillingServiceError.CC_NOCODE, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(taxZipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_NOTAXZIPCODE,
                            deviceId));
            bse.setError(QualutionBillingServiceError.CC_NOTAXZIPCODE);
            log.error(QualutionBillingServiceError.CC_NOTAXZIPCODE, deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            purchaseRequestType.setUserID(userIDType);
            purchaseRequestType.setPlanID(planId);
            if (!CollectionUtils.isEmpty(promoCodeList)) {
                String[] promoCodeArray = (String[]) promoCodeList.toArray();
                purchaseRequestType.setPromoteCode(promoCodeArray);
            }
            purchaseRequestType.setAmount((float) amount);
            PostalCodeType taxPostalCodeType = new PostalCodeType();
            taxPostalCodeType.setPostalCodeType(taxZipcode);
            purchaseRequestType.setTaxZipCode(taxPostalCodeType);
            purchaseRequestType.setUseBalance(useBalance);
            AbstractRequestTypeChoice_type0 abstractRequestTypeChoice_type0 = new AbstractRequestTypeChoice_type0();
            AbstractRequestTypeSequence_type0 abstractRequestTypeSequence_type0 = new AbstractRequestTypeSequence_type0();
            CreditCardInfoType creditCardInfoType = new CreditCardInfoType();
            switch (ccType) {
            case VISA:
                creditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.visa);
                break;
            case MASTERCARD:
                creditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.masterCard);
                break;
            case AMEX:
                creditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.amex);
                break;
            case DISCOVER:
                creditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.discover);
                break;
            case DINERSCLUB:
                creditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.dinersClub);
                break;
            }
            CreditCardNumberType creditCardNumberType = new CreditCardNumberType();
            creditCardNumberType
                    .setCreditCardNumberType(creditCard.getNumber());
            creditCardInfoType.setCardNumber(creditCardNumberType);
            CreditCardExpirationMonthType creditCardExpirationMonthType = new CreditCardExpirationMonthType();
            creditCardExpirationMonthType
                    .setCreditCardExpirationMonthType(creditCard
                            .getExpirationMonth());
            CreditCardExpirationYearType creditCardExpirationYearType = new CreditCardExpirationYearType();
            creditCardExpirationYearType
                    .setCreditCardExpirationYearType(creditCard
                            .getExpirationYear());
            creditCardInfoType
                    .setCardExpirationMonth(creditCardExpirationMonthType);
            creditCardInfoType
                    .setCardExpirationYear(creditCardExpirationYearType);
            CreditCardSecurityCodeType creditCardSecurityCodeType = new CreditCardSecurityCodeType();
            creditCardSecurityCodeType.setCreditCardSecurityCodeType(creditCard
                    .getSecurityCode());
            creditCardInfoType.setCardSecurityCode(creditCardSecurityCodeType);
            creditCardInfoType.setCardZipCode(StringUtils
                    .trimWhitespace(address.getPostalCode()));
            abstractRequestTypeSequence_type0.setCreditCard(creditCardInfoType);
            abstractRequestTypeSequence_type0.setToStore(storeCreditCard);
            if (StringUtils.hasText(cardHolderName)) {
                abstractRequestTypeSequence_type0.setCCHolderName(StringUtils
                        .trimWhitespace(cardHolderName));
            }
            AddressType addressType = new AddressType();
            if (StringUtils.hasText(address.getStreetName())) {
                addressType.setLine1(StringUtils.trimWhitespace(address
                        .getStreetName()));
            }
            if (StringUtils.hasText(address.getStreetName2())) {
                addressType.setLine2(StringUtils.trimWhitespace(address
                        .getStreetName2()));
            }
            if (StringUtils.hasText(address.getCityName())) {
                addressType.setCity(StringUtils.trimWhitespace(address
                        .getCityName()));
            }
            if (StringUtils.hasText(address.getStateCode())) {
                addressType.setState(StringUtils.trimWhitespace(address
                        .getStateCode()));
            }
            if (StringUtils.hasText(cardHolderCountry)) {
                addressType.setCountry(StringUtils
                        .trimWhitespace(cardHolderCountry));
            }
            abstractRequestTypeSequence_type0.setCCAddress(addressType);
            abstractRequestTypeChoice_type0
                    .setAbstractRequestTypeSequence_type0(abstractRequestTypeSequence_type0);
            purchaseRequestType
                    .setAbstractRequestTypeChoice_type0(abstractRequestTypeChoice_type0);
            PurchaseRequest purchaseRequest = new PurchaseRequest();
            purchaseRequest.setPurchaseRequest(purchaseRequestType);
            PurchaseResponse purchaseResponse = billingStub
                    .purchase(purchaseRequest);
            if (purchaseResponse == null
                    || purchaseResponse.getPurchaseResponse() == null
                    || purchaseResponse.getPurchaseResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.CC_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.CC_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            PurchaseResponseType purchaseResponseType = purchaseResponse
                    .getPurchaseResponse();
            OperationStatusType statusType = purchaseResponseType.getStatus();
            ErrorCodeType errorCodeType = purchaseResponseType.getErrorCode();
            ErrorMessageType errorMessageType = purchaseResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.CC_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.CC_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.CC_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.CC_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = purchaseResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType != null) {
                Subscriber subscriber = mapSubscriberInfo(subscriberInfoType,
                        telespreeSession);
                getMbodSession(telespreeSession).setSubscriber(subscriber);
                saveTelespreeSession(telespreeSession);
            }
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setAmountChargedToBalance(purchaseResponseType
                    .getAmountChargedToBalance());
            paymentStatus.setAmountChargedToCreditCard(purchaseResponseType
                    .getAmountChargedToCreditCard());
            paymentStatus.setAmountStillOwe(purchaseResponseType
                    .getAmountStillOwe());
            paymentStatus.setCreditCardAuthorizationCode(purchaseResponseType
                    .getCreditCardAuthorizationCode());
            paymentStatus.setTransactionTime(purchaseResponseType
                    .getTransactionTime());
            if (subscriberInfoType != null) {
                log.info(QualutionBillingServiceError.CC_SUCCESS_HASSUB,
                        deviceId, ccType.getDisplayName(),
                        creditCard.getLastFourDigits(),
                        paymentStatus.getAmountChargedToCreditCard(), planId,
                        userId, paymentStatus.getCreditCardAuthorizationCode());
            } else {
                log.info(QualutionBillingServiceError.CC_SUCCESS, deviceId,
                        ccType.getDisplayName(),
                        creditCard.getLastFourDigits(),
                        paymentStatus.getAmountChargedToCreditCard(), planId,
                        userId, paymentStatus.getCreditCardAuthorizationCode());
            }
            return paymentStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.CC_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.CC_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CC_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.CC_EXCEPTION);
            log.error(QualutionBillingServiceError.CC_EXCEPTION, deviceId,
                    ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * payByCardOnFile(java.lang.String, java.lang.String, double,
     * java.lang.String, java.util.List, java.lang.String,
     * com.telespree.barracuda.beans.CreditCardType, java.lang.String, boolean,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public PaymentStatus payByCardOnFile(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            String mopCode, CreditCardType mopType, String taxZipcode,
            boolean useBalance, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_CORPID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_CORPID_EMPTY);
            log.error(QualutionBillingServiceError.MOP_CORPID_EMPTY, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_USERID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_USERID_EMPTY);
            log.error(QualutionBillingServiceError.MOP_USERID_EMPTY, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(planId)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_PLANID_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_PLANID_EMPTY);
            log.error(QualutionBillingServiceError.MOP_PLANID_EMPTY, deviceId);
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_AMOUNT_INVALID,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_AMOUNT_INVALID);
            log.error(QualutionBillingServiceError.MOP_AMOUNT_INVALID, deviceId);
            throw bse;
        }
        if (mopType == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_TYPE_INVALID,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_TYPE_INVALID);
            log.error(QualutionBillingServiceError.MOP_TYPE_INVALID, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(mopCode)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_CODE_EMPTY,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_CODE_EMPTY);
            log.error(QualutionBillingServiceError.MOP_CODE_EMPTY, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(taxZipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_NOTAXZIPCODE,
                            deviceId));
            bse.setError(QualutionBillingServiceError.MOP_NOTAXZIPCODE);
            log.error(QualutionBillingServiceError.MOP_NOTAXZIPCODE, deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            purchaseRequestType.setUserID(userIDType);
            purchaseRequestType.setPlanID(planId);
            if (!CollectionUtils.isEmpty(promoCodeList)) {
                String[] promoCodeArray = (String[]) promoCodeList.toArray();
                purchaseRequestType.setPromoteCode(promoCodeArray);
            }
            purchaseRequestType.setAmount((float) amount);
            PostalCodeType taxPostalCodeType = new PostalCodeType();
            taxPostalCodeType.setPostalCodeType(taxZipcode);
            purchaseRequestType.setTaxZipCode(taxPostalCodeType);
            purchaseRequestType.setUseBalance(useBalance);
            AbstractRequestTypeChoice_type0 abstractRequestTypeChoice_type0 = new AbstractRequestTypeChoice_type0();
            StoredCreditCardInfoType storedCreditCardInfoType = new StoredCreditCardInfoType();
            switch (mopType) {
            case VISA:
                storedCreditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.visa);
                break;
            case MASTERCARD:
                storedCreditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.masterCard);
                break;
            case AMEX:
                storedCreditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.amex);
                break;
            case DISCOVER:
                storedCreditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.discover);
                break;
            case DINERSCLUB:
                storedCreditCardInfoType
                        .setCardType(com.telespree.barracuda.billing.soap.axis2.CreditCardType.dinersClub);
                break;
            default:
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger.getEventMessage(
                                QualutionBillingServiceError.MOP_TYPE_UNKNONW,
                                deviceId));
                bse.setError(QualutionBillingServiceError.MOP_TYPE_UNKNONW);
                log.error(QualutionBillingServiceError.MOP_TYPE_UNKNONW,
                        deviceId);
                throw bse;
            }
            LastFourDigitsType lastFourDigitsType = new LastFourDigitsType();
            lastFourDigitsType.setLastFourDigitsType(mopCode);
            storedCreditCardInfoType.setLastFourDigits(lastFourDigitsType);
            abstractRequestTypeChoice_type0
                    .setStoredCreditCard(storedCreditCardInfoType);
            purchaseRequestType
                    .setAbstractRequestTypeChoice_type0(abstractRequestTypeChoice_type0);
            PurchaseRequest purchaseRequest = new PurchaseRequest();
            purchaseRequest.setPurchaseRequest(purchaseRequestType);
            PurchaseResponse purchaseResponse = billingStub
                    .purchase(purchaseRequest);
            if (purchaseResponse == null
                    || purchaseResponse.getPurchaseResponse() == null
                    || purchaseResponse.getPurchaseResponse().getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.MOP_RESPONSE_INVALID,
                                        deviceId));
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.MOP_RESPONSE_INVALID,
                        deviceId);
                throw bse;
            }
            PurchaseResponseType purchaseResponseType = purchaseResponse
                    .getPurchaseResponse();
            OperationStatusType statusType = purchaseResponseType.getStatus();
            ErrorCodeType errorCodeType = purchaseResponseType.getErrorCode();
            ErrorMessageType errorMessageType = purchaseResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.MOP_RESPONSE_FAILURE,
                                        deviceId, statusType.getValue(),
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.MOP_RESPONSE_FAILURE,
                        deviceId, statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        QualutionBillingServiceError.MOP_RESPONSE_HASCODE,
                                        deviceId,
                                        errorCodeType.getErrorCodeType(),
                                        errorMessageType.getErrorMessageType()));
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.MOP_RESPONSE_HASCODE,
                        deviceId, errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = purchaseResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByCardOnFile did not return subscriber information.");
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_NOSUB);
                log.error(QualutionBillingServiceError.MOP_RESPONSE_NOSUB,
                        deviceId);
                throw bse;
            }
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType,
                    telespreeSession);
            getMbodSession(telespreeSession).setSubscriber(subscriber);
            saveTelespreeSession(telespreeSession);
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setAmountChargedToBalance(purchaseResponseType
                    .getAmountChargedToBalance());
            paymentStatus.setAmountChargedToCreditCard(purchaseResponseType
                    .getAmountChargedToCreditCard());
            paymentStatus.setAmountStillOwe(purchaseResponseType
                    .getAmountStillOwe());
            paymentStatus.setCreditCardAuthorizationCode(purchaseResponseType
                    .getCreditCardAuthorizationCode());
            paymentStatus.setTransactionTime(purchaseResponseType
                    .getTransactionTime());
            log.info(QualutionBillingServiceError.MOP_SUCCESS, deviceId,
                    mopType.getDisplayName(), mopCode,
                    paymentStatus.getAmountChargedToCreditCard(), planId,
                    userId, paymentStatus.getCreditCardAuthorizationCode());
            return paymentStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_REMOTE_EXCEPTION,
                            deviceId, re.getMessage()), re);
            bse.setError(QualutionBillingServiceError.MOP_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.MOP_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.MOP_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.MOP_EXCEPTION);
            log.error(QualutionBillingServiceError.MOP_EXCEPTION, deviceId,
                    ex.getMessage());
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * redeemPrepaidCard(java.lang.String, java.lang.String, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public RedeemStatus redeemPrepaidCard(String corpId, String userId,
            String code, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String encUserId = encodeToCharSet(userId, ISO_8859_1);
        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty corp ID.");
            // TODO set enum and log
            throw bse;
        }
        if (!StringUtils.hasText(encUserId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            // TODO set enum and log
            throw bse;
        }
        if (!StringUtils.hasText(code)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty prepaid code.");
            // TODO set enum and log
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            RedeemScratchCardRequestType redeemScratchCardRequestType = new RedeemScratchCardRequestType();
            redeemScratchCardRequestType.setCorpID(corpId);
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(encUserId);
            redeemScratchCardRequestType.setUserID(userIDType);
            ScratchCardInfoType scratchCardInfoType = new ScratchCardInfoType();
            scratchCardInfoType.setPIN(code);
            redeemScratchCardRequestType.setScratchCard(scratchCardInfoType);
            RedeemScratchCardRequest redeemScratchCardRequest = new RedeemScratchCardRequest();
            redeemScratchCardRequest
                    .setRedeemScratchCardRequest(redeemScratchCardRequestType);
            RedeemScratchCardResponse redeemScratchCardResponse = billingStub
                    .redeemScratchCard(redeemScratchCardRequest);
            if (redeemScratchCardResponse == null
                    || redeemScratchCardResponse.getRedeemScratchCardResponse() == null
                    || redeemScratchCardResponse.getRedeemScratchCardResponse()
                            .getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution returned invalid RedeemScratchCard response.");
                // TODO set enum and log
                throw bse;
            }
            RedeemScratchCardResponseType redeemScratchCardResponseType = redeemScratchCardResponse
                    .getRedeemScratchCardResponse();
            OperationStatusType statusType = redeemScratchCardResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = redeemScratchCardResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = redeemScratchCardResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution RedeemScratchCard failed.");
                // TODO set enum and log
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution RedeemScratchCard returned error code.");
                // TODO set enum and log
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = redeemScratchCardResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution RedeemScratchCard did not return subscriber information.");
                // TODO set enum and log
                throw bse;
            }
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType,
                    telespreeSession);
            getMbodSession(telespreeSession).setSubscriber(subscriber);
            saveTelespreeSession(telespreeSession);
            RedeemStatus redeemStatus = new RedeemStatus();
            redeemStatus.setAmountRedeemed(redeemScratchCardResponseType
                    .getAmountRedeemed());
            if (ArrayUtils.isNotEmpty(redeemScratchCardResponseType
                    .getAuthorizationCode())) {
                redeemStatus.setAuthorizationCode(redeemScratchCardResponseType
                        .getAuthorizationCode()[0]);
            }
            redeemStatus.setTransactionTime(redeemScratchCardResponseType
                    .getTransactionTime());
            return redeemStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            // TODO set enum and log
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            // TODO set enum and log
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.NewBillingService#
     * calculateTaxes(java.lang.String, double, java.lang.String,
     * telespree.apps.fwk.ApplicationConfiguration,
     * telespree.apps.fwk.session.TelespreeSession)
     */
    @Override
    public Map<String, Double> calculateTaxes(String corpId, double amount,
            String zipcode, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String deviceId = getDeviceId(telespreeSession);
        if (!StringUtils.hasText(corpId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty corp ID.");
            // TODO set enum and log
            throw bse;
        }
        if (!StringUtils.hasText(zipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty zip code.");
            // TODO set enum and log
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    "Price less than or equal to zero.");
            // TODO set enum and log
            throw bse;
        }
        BillingStub billingStub = getBillingStub(appConfiguration,
                telespreeSession);
        try {
            CalculateTaxRequestType calculateTaxRequestType = new CalculateTaxRequestType();
            calculateTaxRequestType.setCorpID(corpId);
            calculateTaxRequestType.setAmount((float) amount);
            PostalCodeType postalCodeType = new PostalCodeType();
            postalCodeType.setPostalCodeType(zipcode);
            calculateTaxRequestType.setZipCode(postalCodeType);
            CalculateTaxRequest calculateTaxRequest = new CalculateTaxRequest();
            calculateTaxRequest.setCalculateTaxRequest(calculateTaxRequestType);
            CalculateTaxResponse calculateTaxResponse = billingStub
                    .calculateTax(calculateTaxRequest);
            if (calculateTaxResponse == null
                    || calculateTaxResponse.getCalculateTaxResponse() == null
                    || calculateTaxResponse.getCalculateTaxResponse()
                            .getStatus() == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution returned invalid CalculateTax response.");
                // TODO set enum and log
                throw bse;
            }
            CalculateTaxResponseType calculateTaxResponseType = calculateTaxResponse
                    .getCalculateTaxResponse();
            OperationStatusType statusType = calculateTaxResponseType
                    .getStatus();
            ErrorCodeType errorCodeType = calculateTaxResponseType
                    .getErrorCode();
            ErrorMessageType errorMessageType = calculateTaxResponseType
                    .getErrorMessage();
            if (!OperationStatusType.ok.equals(statusType)) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution CalculateTax failed.");
                // TODO set enum and log
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution CalculateTax returned error code.");
                // TODO set enum and log
                throw bse;
            }
            TaxType[] taxes = calculateTaxResponseType.getTax();
            if (ArrayUtils.isNotEmpty(taxes)) {
                Map<String, Double> taxMap = new LinkedHashMap<String, Double>();
                for (TaxType tax : taxes) {
                    taxMap.put(tax.getTaxName(),
                            Double.valueOf(tax.getTaxAmount()));
                }
                return taxMap;
            }
            return null;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            // TODO set enum and log
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            // TODO set enum and log
            throw bse;
        } finally {
            try {
                billingStub._getServiceClient().cleanup();
                billingStub.cleanup();
            } catch (AxisFault af) {
                log.error(af.getMessage(), af);
            }
        }
    }

    /**
     * @param telespreeSession
     * @throws BarracudaException
     */
    // TODO move to core support
    protected void saveTelespreeSession(TelespreeSession telespreeSession)
            throws BarracudaException {

        try {
            TelespreeSessionUtil.saveTelespreeSession(telespreeSession);
        } catch (DALException dale) {
            // TODO move enum, code, and message to core
            BarracudaException be = new BarracudaException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CORE_SESSION_UPDATE_DAL,
                            "", telespreeSession.getID(), dale.getMessage()),
                    dale);
            be.setError(QualutionBillingServiceError.CORE_SESSION_UPDATE_DAL);
            log.error(QualutionBillingServiceError.CORE_SESSION_UPDATE_DAL, "",
                    telespreeSession.getID(), dale.getMessage());
            throw be;
        } catch (Throwable t) {
            // TODO move enum, code, and message to core
            BarracudaException be = new BarracudaException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.CORE_SESSION_UPDATE_T,
                            "", telespreeSession.getID(), t.getMessage()), t);
            be.setError(QualutionBillingServiceError.CORE_SESSION_UPDATE_T);
            log.error(QualutionBillingServiceError.CORE_SESSION_UPDATE_T, "",
                    telespreeSession.getID(), t.getMessage());
            throw be;
        }
    }

    /**
     * @param telespreeSession
     * @return
     */
    // TODO move to core support
    protected String getDeviceId(TelespreeSession telespreeSession) {

        Device device = TelespreeSessionUtil.getSessionDevice(telespreeSession);
        DeviceID deviceID = null;
        if (device != null) {
            try {
                if (device.hasESN()) {
                    deviceID = DeviceID.getDeviceId(device.getEsnDecimal());
                } else if (device.hasMeid()) {
                    deviceID = DeviceID.getDeviceId(device.getMeidDecimal());
                } else if (device.hasMacAddress()) {
                    deviceID = DeviceID.getDeviceId(device.getMacAddress());
                } else if (device.hasImsi()) {
                    deviceID = DeviceID.getDeviceId(device.getImsi());
                } else if (device.hasIccid()) {
                    deviceID = DeviceID.getDeviceId(device.getIccid());
                }
            } catch (DeviceIdException die) {
                log.error(die.getMessage(), die);
            }
        }
        if (deviceID == null) {
            return null;
        }
        return deviceID.getId();
    }

    /**
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     * @throws BarracudaException
     */
    protected MbodSession getMbodSession(TelespreeSession telespreeSession)
            throws BillingServiceException, BarracudaException {

        if (telespreeSession == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GENERAL_MBODSESSION,
                            ""));
            bse.setError(QualutionBillingServiceError.GENERAL_MBODSESSION);
            log.error(QualutionBillingServiceError.GENERAL_MBODSESSION, "");
            throw bse;
        }
        MbodSession mbodSession = null;
        TelespreeBean bean = telespreeSession
                .getProperty(MbodSession.BEAN_NAME);
        if (bean == null) {
            mbodSession = new MbodSession();
            telespreeSession.setProperty(MbodSession.BEAN_NAME, mbodSession);
            saveTelespreeSession(telespreeSession);
        } else {
            mbodSession = (MbodSession) bean;
        }
        return mbodSession;
    }

    /**
     * @param subscriberInfoType
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    protected Subscriber mapSubscriberInfo(
            SubscriberInfoType subscriberInfoType,
            TelespreeSession telespreeSession) throws BillingServiceException {

        try {
            Subscriber subscriber = new Subscriber();
            UserIDType userIDType = subscriberInfoType.getUserID();
            if (userIDType != null) {
                subscriber.setUserId(encodeFromCharSetToCharSet(
                        userIDType.getUserIDType(), ISO_8859_1, UTF_8));
                subscriber
                        .setUserIdHtml(StringEscapeUtils
                                .escapeHtml(encodeFromCharSetToCharSet(
                                        userIDType.getUserIDType(), ISO_8859_1,
                                        UTF_8)));
            }
            SecurityQuestionIDType[] securityQuestionIDTypes = subscriberInfoType
                    .getSecurityQuestionID();
            if (ArrayUtils.isNotEmpty(securityQuestionIDTypes)) {
                subscriber.setSecurityQuestionId(securityQuestionIDTypes[0]
                        .getSecurityQuestionIDType());
                Map<String, String> securityQuestionMap = getMbodSession(
                        telespreeSession).getSecurityQuestionMap();
                if (!CollectionUtils.isEmpty(securityQuestionMap)) {
                    subscriber.setSecurityQuestionText(securityQuestionMap
                            .get(subscriber.getSecurityQuestionId()));
                }
            }
            EmailAddressType emailAddressType = subscriberInfoType.getEmail();
            if (emailAddressType != null) {
                subscriber.setEmail(emailAddressType.getEmailAddressType());
            }
            subscriber.setFirstName(encodeFromCharSetToCharSet(
                    subscriberInfoType.getFirstName(), ISO_8859_1, UTF_8));
            subscriber.setFirstNameHtml(StringEscapeUtils
                    .escapeHtml(encodeFromCharSetToCharSet(
                            subscriberInfoType.getFirstName(), ISO_8859_1,
                            UTF_8)));
            subscriber.setLastName(encodeFromCharSetToCharSet(
                    subscriberInfoType.getLastName(), ISO_8859_1, UTF_8));
            subscriber
                    .setLastNameHtml(StringEscapeUtils
                            .escapeHtml(encodeFromCharSetToCharSet(
                                    subscriberInfoType.getLastName(),
                                    ISO_8859_1, UTF_8)));
            StoredCreditCardInfoType[] storedCreditCardInfoTypes = subscriberInfoType
                    .getStoredCreditCard();
            if (ArrayUtils.isNotEmpty(storedCreditCardInfoTypes)) {
                LastFourDigitsType lastFourDigitsType = storedCreditCardInfoTypes[0]
                        .getLastFourDigits();
                if (lastFourDigitsType != null) {
                    subscriber.setMopcode(lastFourDigitsType
                            .getLastFourDigitsType());
                }
                com.telespree.barracuda.billing.soap.axis2.CreditCardType creditCardType = storedCreditCardInfoTypes[0]
                        .getCardType();
                if (com.telespree.barracuda.billing.soap.axis2.CreditCardType.amex
                        .equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.AMEX);
                } else if (com.telespree.barracuda.billing.soap.axis2.CreditCardType.visa
                        .equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.VISA);
                } else if (com.telespree.barracuda.billing.soap.axis2.CreditCardType.masterCard
                        .equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.MASTERCARD);
                } else if (com.telespree.barracuda.billing.soap.axis2.CreditCardType.discover
                        .equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.DISCOVER);
                } else if (com.telespree.barracuda.billing.soap.axis2.CreditCardType.dinersClub
                        .equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.DINERSCLUB);
                }
            }
            AddressType addressType = subscriberInfoType.getAddress();
            if (addressType != null) {
                PostalCodeType postalCodeType = addressType.getPostalCode();
                if (postalCodeType != null) {
                    Address address = new Address();
                    address.setPostalCode(postalCodeType.getPostalCodeType());
                    subscriber.setAddress(address);
                }
            }
            subscriber.setLoyaltyReference(subscriberInfoType
                    .getLoyaltyReference());
            subscriber.setAccountBalance(subscriberInfoType.getBalance());
            return subscriber;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (BarracudaException be) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GENERAL_MAP_SUB,
                            getDeviceId(telespreeSession),
                            TelespreeLogger.getEventCode(be.getError(), "", ""),
                            be.getMessage()), be);
            bse.setError(QualutionBillingServiceError.GENERAL_MAP_SUB);
            log.error(QualutionBillingServiceError.GENERAL_MAP_SUB,
                    getDeviceId(telespreeSession),
                    TelespreeLogger.getEventCode(be.getError(), "", ""),
                    be.getMessage());
            throw bse;
        } catch (Throwable t) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GENERAL_MAP_SUB_T,
                            getDeviceId(telespreeSession), t.getMessage()), t);
            bse.setError(QualutionBillingServiceError.GENERAL_MAP_SUB_T);
            log.error(QualutionBillingServiceError.GENERAL_MAP_SUB_T,
                    getDeviceId(telespreeSession), t.getMessage());
            throw bse;
        }
    }

    /**
     * @param applicationConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    protected BillingStub getBillingStub(
            ApplicationConfiguration applicationConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String deviceId = getDeviceId(telespreeSession);
        try {
            String url = getBillingStubUrl();
            if (applicationConfiguration != null) {
                url = applicationConfiguration.getProperty(
                        PROP_QUALUTION_BILLING_URL, getBillingStubUrl(),
                        telespreeSession);
            }
            BillingStub billingStub = new BillingStub(url);
            billingStub
                    ._getServiceClient()
                    .getOptions()
                    .setProperty(
                            org.apache.axis2.context.MessageContextConstants.CHUNKED,
                            "false");
            billingStub
                    ._getServiceClient()
                    .getOptions()
                    .setTimeOutInMilliSeconds(
                            getBillingStubTimeout(applicationConfiguration,
                                    telespreeSession));
            return billingStub;
        } catch (AxisFault af) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GENERAL_STUB_AXISFAULT,
                            deviceId, af.getMessage()), af);
            bse.setError(QualutionBillingServiceError.GENERAL_STUB_AXISFAULT);
            log.error(QualutionBillingServiceError.GENERAL_STUB_AXISFAULT,
                    deviceId, af.getMessage());
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            QualutionBillingServiceError.GENERAL_STUB_EXCEPTION,
                            deviceId, ex.getMessage()), ex);
            bse.setError(QualutionBillingServiceError.GENERAL_STUB_EXCEPTION);
            log.error(QualutionBillingServiceError.GENERAL_STUB_EXCEPTION,
                    deviceId, ex.getMessage());
            throw bse;
        }
    }

    /**
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws Exception
     */
    protected long getBillingStubTimeout(
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws Exception {

        long timeout = getBillingStubTimeout();
        if (appConfiguration != null) {
            timeout = appConfiguration.getProperty(
                    PROP_QUALUTION_BILLING_TIMEOUT, getBillingStubTimeout(),
                    telespreeSession);
        }
        return timeout;
    }

    /**
     * @return the billingStubTimeout
     */
    public long getBillingStubTimeout() {
        return billingStubTimeout;
    }

    /**
     * @param billingStubTimeout
     *            the billingStubTimeout to set
     */
    @Value("#{mbodProperties['billingStubTimeout']}")
    public void setBillingStubTimeout(long billingStubTimeout) {
        this.billingStubTimeout = billingStubTimeout;
    }

    /**
     * @return the billingStubUrl
     */
    public String getBillingStubUrl() {
        return billingStubUrl;
    }

    /**
     * @param billingStubUrl
     *            the billingStubUrl to set
     */
    @Value("#{mbodProperties['billingStubUrl']}")
    public void setBillingStubUrl(String billingStubUrl) {
        this.billingStubUrl = billingStubUrl;
    }

    /**
     * @param toEncode
     * @param toCharSet
     * @return
     */
    private String encodeToCharSet(String toEncode, String toCharSet) {

        if (!StringUtils.hasText(toEncode) || !StringUtils.hasText(toCharSet)) {
            return null;
        }
        byte[] bytes = toEncode.getBytes();
        String encString = null;
        try {
            encString = new String(bytes, toCharSet);
        } catch (UnsupportedEncodingException uee) {
            // TODO log enum and message
            log.error(uee.getMessage(), uee);
        }
        return encString;
    }

    /**
     * @param toDecode
     * @param fromCharSet
     * @param toCharSet
     * @return
     */
    private String encodeFromCharSetToCharSet(String toDecode,
            String fromCharSet, String toCharSet) {

        if (!StringUtils.hasText(toDecode) || !StringUtils.hasText(fromCharSet)
                || !StringUtils.hasText(toCharSet)) {
            return null;
        }
        String decString = null;
        try {
            decString = new String(toDecode.getBytes(fromCharSet), toCharSet);
            if (StringUtils.hasText(decString)) {
                if (decString.contains("�")) {
                    decString = new String(toDecode.toLowerCase().getBytes(
                            fromCharSet), toCharSet);
                }
            }
        } catch (UnsupportedEncodingException uee) {
            // TODO log enum and message
            log.error(uee.getMessage(), uee);
        }
        return decString;
    }

}
