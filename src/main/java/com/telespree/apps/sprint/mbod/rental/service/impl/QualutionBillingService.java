/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.cal10n.LocLogger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.common.CreditCard;
import telespree.apps.fwk.common.TelespreeBean;
import telespree.apps.fwk.exception.ApplicationException;
import telespree.apps.fwk.services.ApplicationServiceSupport;
import telespree.common.DeviceIdentifier;

import com.telespree.apps.sprint.mbod.rental.MbodConstants;
import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.MbodSession;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.RedeemStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.apps.sprint.mbod.rental.service.BillingService;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
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
import com.telespree.barracuda.billing.soap.axis2.CreditCardType;
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
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * QualutionBillingService
 * 
 * 
 * @author $Author: michael $ on $DateTime: 2013/05/28 12:39:20 $
 * @version $Revision: #16 $
 * 
 */
@Scope("prototype")
@Service("qualutionBillingService")
@Deprecated
public class QualutionBillingService extends ApplicationServiceSupport
        implements BillingService, MbodConstants {

    private static final LocLogger log = TelespreeLogger
            .getLogger(QualutionBillingService.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.telespree.apps.sprint.mbod.rental.service.BillingService#
     * logSubscriberHistory(java.lang.String, java.lang.String,
     * java.lang.String,
     * com.telespree.apps.sprint.mbod.rental.model.HistoryType)
     */
    @Override
    public void logSubscriberHistory(String userId, String historyNotes,
            String deviceId, HistoryType historyType)
            throws BillingServiceException {

        Calendar historyTime = Calendar.getInstance();
        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.LOGHISTORY_USERID_EMPTY);
            log.error(QualutionBillingServiceError.LOGHISTORY_USERID_EMPTY,
                    deviceId);
            throw bse;
        }
        if (historyType == null) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty history type.");
            bse.setError(QualutionBillingServiceError.LOGHISTORY_HISTORYTYPE_EMPTY);
            log.error(
                    QualutionBillingServiceError.LOGHISTORY_HISTORYTYPE_EMPTY,
                    deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(historyNotes)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty history notes.");
            bse.setError(QualutionBillingServiceError.LOGHISTORY_HISTORYNOTES_EMPTY);
            log.error(
                    QualutionBillingServiceError.LOGHISTORY_HISTORYNOTES_EMPTY,
                    deviceId);
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
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
            addSubscriberHistoryRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                        "Qualution returned invalid AddSubscriberHistory response.");
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
                        "Qualution AddSubscriberHistory failed.");
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
                        "Qualution AddSubscriberHistory returned error code.");
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
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.LOGHISTORY_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.LOGHISTORY_REMOTE_EXCEPTION,
                    deviceId, re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
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
     * @see com.telespree.apps.sprint.mbod.rental.service.BillingService#
     * getSubscriberHistory(java.lang.String)
     */
    @Override
    public List<SubscriberHistory> getSubscriberHistory(String userId)
            throws BillingServiceException {

        ArrayList<SubscriberHistory> historyList = new ArrayList<SubscriberHistory>();
        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.GETHISTORY_USERID_EMPTY);
            log.error(QualutionBillingServiceError.GETHISTORY_USERID_EMPTY, "");
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            GetSubscriberHistoryRequestType getSubscriberHistoryRequestType = new GetSubscriberHistoryRequestType();
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
            getSubscriberHistoryRequestType.setUserID(userIDType);
            getSubscriberHistoryRequestType.setCorpID(getMvnoExternalId()); // TODO
                                                                            // no
            // config
            // here,
            // what to
            // do?
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
                        "Qualution returned invalid GetSubscriberHistory response.");
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
                        "Qualution GetSubscriberHistory failed.");
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
                        "Qualution GetSubscriberHistory returned error code.");
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
                // TODO log enum and message
            } else {
                // TODO update enum and message
                log.info(QualutionBillingServiceError.LOGHISTORY_SUCCESS, "",
                        userId);
                for (SubscriberHistoryEntryType entry : subscriberHistoryEntryTypeArray) {
                    SubscriberHistory history = new SubscriberHistory();
                    history.setDeviceId(entry.getDeviceID());
                    history.setHistoryNotes(entry.getHistoryNotes());
                    history.setHistoryType(entry.getHistoryType().toString()); // maybe
                                                                               // convert
                                                                               // to
                                                                               // enum
                    historyList.add(history);
                }
            }
            return historyList;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.GETHISTORY_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.GETHISTORY_REMOTE_EXCEPTION,
                    "", re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * authenticate (java.lang.String, java.lang.String)
     */
    public Subscriber authenticate(String userId, String password)
            throws BillingServiceException {
        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_USERID_EMPTY);
            log.error(QualutionBillingServiceError.AUTHENTICATE_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(password)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty password.");
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_PASSWORD_EMPTY);
            log.error(QualutionBillingServiceError.AUTHENTICATE_PASSWORD_EMPTY,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            VerifySubscriberRequestType verifySubscriberRequestType = new VerifySubscriberRequestType();
            verifySubscriberRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                        "Qualution returned invalid VerifySubscriber response.");
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution VerifySubscriber failed.");
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution VerifySubscriber returned error code.");
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = verifySubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution VerifySubscriber did not return subscriber information.");
                bse.setError(QualutionBillingServiceError.AUTHENTICATE_RESPONSE_NOSUB);
                log.error(
                        QualutionBillingServiceError.AUTHENTICATE_RESPONSE_NOSUB,
                        getDeviceId());
                throw bse;
            }
            log.info(QualutionBillingServiceError.AUTHENTICATE_SUCCESS,
                    getDeviceId(), userId);
            return mapSubscriberInfo(subscriberInfoType);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.AUTHENTICATE_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.AUTHENTICATE_EXCEPTION);
            log.error(QualutionBillingServiceError.AUTHENTICATE_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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
     * com.telespree.apps.sprint.mbod.rental.test.service.BillingService#register
     * (java.lang.String, java.lang.String, java.util.Map, java.lang.String)
     */
    @Override
    public Subscriber register(String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email)
            throws BillingServiceException {
        return register(userId, password, securityQuestionIdAnswerMap, email,
                null, null, null, null);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.BillingService#register
     * (java.lang.String, java.lang.String, java.util.Map, java.lang.String,
     * java.lang.String, java.lang.String, telespree.apps.fwk.common.Address,
     * java.lang.String)
     */
    @Override
    public Subscriber register(String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email,
            String firstName, String lastName, Address address,
            String loyaltyReference) throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.REGISTER_USERID_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(password)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty password.");
            bse.setError(QualutionBillingServiceError.REGISTER_PASSWORD_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_PASSWORD_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (CollectionUtils.isEmpty(securityQuestionIdAnswerMap)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty security question answer map.");
            bse.setError(QualutionBillingServiceError.REGISTER_SECURITY_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_SECURITY_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(email)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty email.");
            bse.setError(QualutionBillingServiceError.REGISTER_EMAIL_EMPTY);
            log.error(QualutionBillingServiceError.REGISTER_EMAIL_EMPTY,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            CreateSubscriberRequestType createSubscriberRequestType = new CreateSubscriberRequestType();
            createSubscriberRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                securityAnswerType.setSecurityAnswerType(value);
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
            if (StringUtils.hasText(firstName)) {
                createSubscriberRequestType.setFirstName(firstName);
            }
            if (StringUtils.hasText(lastName)) {
                createSubscriberRequestType.setLastName(lastName);
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
                        "Qualution returned invalid CreateSubscriber response.");
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution CreateSubscriber failed.");
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution CreateSubscriber returned error code.");
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.REGISTER_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = createSubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution CreateSubscriber did not return subscriber information.");
                bse.setError(QualutionBillingServiceError.REGISTER_RESPONSE_NOSUB);
                log.error(QualutionBillingServiceError.REGISTER_RESPONSE_NOSUB,
                        getDeviceId());
                throw bse;
            }
            log.info(QualutionBillingServiceError.REGISTER_SUCCESS,
                    getDeviceId(), userId);
            return mapSubscriberInfo(subscriberInfoType);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.REGISTER_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.REGISTER_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.REGISTER_EXCEPTION);
            log.error(QualutionBillingServiceError.REGISTER_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * retrieveSecurityQuestionIdList(java.lang.String)
     */
    @Override
    public List<String> retrieveSecurityQuestionIdList(String userId)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_USERID_EMPTY);
            log.error(
                    QualutionBillingServiceError.SECURITYQUESTION_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            GetSecurityQuestionListRequestType getSecurityQuestionListRequestType = new GetSecurityQuestionListRequestType();
            getSecurityQuestionListRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                        "Qualution returned invalid GetSecurityQuestionList response.");
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution GetSecurityQuestionList failed.");
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution GetSecurityQuestionList returned error code.");
                bse.setError(QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.SECURITYQUESTION_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SecurityQuestionIDType[] securityQuestionIDs = getSecurityQuestionListResponseType
                    .getSecurityQuestionID();
            if (ArrayUtils.isEmpty(securityQuestionIDs)) {
                log.info(
                        QualutionBillingServiceError.SECURITYQUESTION_SUCCESS_ZERO,
                        getDeviceId(), userId);
                return null;
            }
            List<String> questionIdList = new ArrayList<String>(
                    securityQuestionIDs.length);
            for (SecurityQuestionIDType securityQuestionIDType : securityQuestionIDs) {
                questionIdList.add(securityQuestionIDType
                        .getSecurityQuestionIDType());
            }
            log.info(QualutionBillingServiceError.SECURITYQUESTION_SUCCESS,
                    getDeviceId(), userId);
            return questionIdList;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.SECURITYQUESTION_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.SECURITYQUESTION_EXCEPTION);
            log.error(QualutionBillingServiceError.SECURITYQUESTION_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * resetPassword (java.lang.String, java.util.Map, java.lang.String)
     */
    @Override
    public void resetPassword(String userId,
            Map<String, String> securityQuestionIdAnswerMap, String newPassword)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_USERID_EMPTY);
            log.error(QualutionBillingServiceError.RESETPASSWORD_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(newPassword)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty new password.");
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_PASSWORD_EMPTY);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_PASSWORD_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (CollectionUtils.isEmpty(securityQuestionIdAnswerMap)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty security question answer map.");
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_SECURITY_EMPTY);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_SECURITY_EMPTY,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            ResetSubscriberPasswordRequestType resetSubscriberPasswordRequestType = new ResetSubscriberPasswordRequestType();
            resetSubscriberPasswordRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                securityAnswerType.setSecurityAnswerType(value);
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
                        "Qualution returned invalid ResetSubscriberPassword response.");
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_INVALID);
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution ResetSubscriberPassword failed.");
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution ResetSubscriberPassword returned error code.");
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = resetSubscriberPasswordResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution ResetSubscriberPassword did not return subscriber information.");
                bse.setError(QualutionBillingServiceError.RESETPASSWORD_RESPONSE_NOSUB);
                log.error(
                        QualutionBillingServiceError.RESETPASSWORD_RESPONSE_NOSUB,
                        getDeviceId());
                throw bse;
            }
            log.info(QualutionBillingServiceError.RESETPASSWORD_SUCCESS,
                    getDeviceId(), userId);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_REMOTE_EXCEPTION);
            log.error(
                    QualutionBillingServiceError.RESETPASSWORD_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.RESETPASSWORD_EXCEPTION);
            log.error(QualutionBillingServiceError.RESETPASSWORD_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * payByBalance (java.lang.String, double, java.lang.String, java.util.List)
     */
    @Override
    public PaymentStatus payByBalance(String userId, double amount,
            String planId, List<String> promoCodeList)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
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
        BillingStub billingStub = getBillingStub();
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType);
            getMbodSession().setSubscriber(subscriber);
            getTelespreeSessionManager().saveSession();
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
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.BillingService#payByCreditCard
     * (java.lang.String, double, java.lang.String, java.util.List,
     * telespree.apps.fwk.common.CreditCard, boolean, java.lang.String, boolean)
     */
    @Override
    public PaymentStatus payByCreditCard(String userId, double amount,
            String planId, List<String> promoCodeList, CreditCard creditCard,
            boolean storeCreditCard, String taxZipcode, boolean useBalance)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
            try {
                ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
                userId = applicationConfiguration.getProperty(
                        PROP_QUALUTION_BILLING_ANONYMOUS_USERID,
                        PROP_QUALUTION_BILLING_ANONYMOUS_USERID_DEFAULT,
                        getSession());
                if (!StringUtils.hasText(userId)) {
                    BillingServiceException bse = new BillingServiceException(
                            "Empty user ID.");
                    bse.setError(QualutionBillingServiceError.CC_ANONYMOUS_USERID_EMPTY);
                    log.error(
                            QualutionBillingServiceError.CC_ANONYMOUS_USERID_EMPTY,
                            getDeviceId());
                    throw bse;
                }
            } catch (ApplicationException ae) {
                BillingServiceException bse = new BillingServiceException(
                        ae.getMessage(), ae);
                bse.setError(QualutionBillingServiceError.CC_ANONYMOUS_USERID_EXCEPTION);
                log.error(
                        QualutionBillingServiceError.CC_ANONYMOUS_USERID_EXCEPTION,
                        getDeviceId(), (ae.getErrorCode() != null) ? ae
                                .getErrorCode().toString() : "n/a", ae
                                .getMessage());
                throw bse;
            }
        }
        if (!StringUtils.hasText(planId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty plan ID.");
            bse.setError(QualutionBillingServiceError.CC_PLANID_EMPTY);
            log.error(QualutionBillingServiceError.CC_PLANID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    "Payment amount less than or equal to zero.");
            bse.setError(QualutionBillingServiceError.CC_AMOUNT_INVALID);
            log.error(QualutionBillingServiceError.CC_AMOUNT_INVALID,
                    getDeviceId());
            throw bse;
        }
        if (creditCard == null) {
            BillingServiceException bse = new BillingServiceException(
                    "No credit card provided.");
            bse.setError(QualutionBillingServiceError.CC_NO);
            log.error(QualutionBillingServiceError.CC_NO, getDeviceId());
            throw bse;
        }
        com.telespree.barracuda.beans.CreditCardType ccType = com.telespree.barracuda.beans.CreditCardType
                .getTypeByNumber(creditCard.getNumber());
        if (ccType == null) {
            BillingServiceException bse = new BillingServiceException(
                    "Invalid credit card number.");
            bse.setError(QualutionBillingServiceError.CC_TYPE_INVALID);
            log.error(QualutionBillingServiceError.CC_TYPE_INVALID,
                    getDeviceId());
            throw bse;

        }
        if (creditCard.getBillingAddress() == null
                || !StringUtils.hasText(creditCard.getBillingAddress()
                        .getPostalCode())) {
            BillingServiceException bse = new BillingServiceException(
                    "No billing zipcode provided.");
            bse.setError(QualutionBillingServiceError.CC_NOBILLING);
            log.error(QualutionBillingServiceError.CC_NOBILLING, getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(creditCard.getSecurityCode())) {
            BillingServiceException bse = new BillingServiceException(
                    "No security code provided.");
            bse.setError(QualutionBillingServiceError.CC_NOCODE);
            log.error(QualutionBillingServiceError.CC_NOCODE, getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(taxZipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    "No tax zipcode provided.");
            bse.setError(QualutionBillingServiceError.CC_NOTAXZIPCODE);
            log.error(QualutionBillingServiceError.CC_NOTAXZIPCODE,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                creditCardInfoType.setCardType(CreditCardType.visa);
                break;
            case MASTERCARD:
                creditCardInfoType.setCardType(CreditCardType.masterCard);
                break;
            case AMEX:
                creditCardInfoType.setCardType(CreditCardType.amex);
                break;
            case DISCOVER:
                creditCardInfoType.setCardType(CreditCardType.discover);
                break;
            case DINERSCLUB:
                creditCardInfoType.setCardType(CreditCardType.dinersClub);
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
            creditCardInfoType.setCardZipCode(creditCard.getBillingAddress()
                    .getPostalCode());
            abstractRequestTypeSequence_type0.setCreditCard(creditCardInfoType);
            abstractRequestTypeSequence_type0.setToStore(storeCreditCard);
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
                        "Qualution returned invalid PurchaseByCreditCard response.");
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.CC_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution PurchaseByCreditCard failed.");
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.CC_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByCreditCard returned error code.");
                bse.setError(QualutionBillingServiceError.CC_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.CC_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = purchaseResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType != null) {
                Subscriber subscriber = mapSubscriberInfo(subscriberInfoType);
                getMbodSession().setSubscriber(subscriber);
                getTelespreeSessionManager().saveSession();
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
                        getDeviceId(), ccType.getDisplayName(),
                        creditCard.getLastFourDigits(),
                        paymentStatus.getAmountChargedToCreditCard(), planId,
                        userId, paymentStatus.getCreditCardAuthorizationCode());
            } else {
                log.info(QualutionBillingServiceError.CC_SUCCESS,
                        getDeviceId(), ccType.getDisplayName(),
                        creditCard.getLastFourDigits(),
                        paymentStatus.getAmountChargedToCreditCard(), planId,
                        userId, paymentStatus.getCreditCardAuthorizationCode());
            }
            return paymentStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.CC_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.CC_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (ApplicationException ae) {
            BillingServiceException bse = new BillingServiceException(
                    ae.getMessage(), ae);
            bse.setError(QualutionBillingServiceError.CC_APPEXCEPTION);
            log.error(QualutionBillingServiceError.CC_APPEXCEPTION,
                    getDeviceId(), (ae.getErrorCode() != null) ? ae
                            .getErrorCode().toString() : "n/a", ae.getMessage());
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.CC_EXCEPTION);
            log.error(QualutionBillingServiceError.CC_EXCEPTION, getDeviceId(),
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
     * com.telespree.apps.sprint.mbod.rental.service.BillingService#payByCardOnFile
     * (java.lang.String, double, java.lang.String, java.util.List,
     * java.lang.String, com.telespree.barracuda.beans.CreditCardType,
     * java.lang.String, boolean)
     */
    @Override
    public PaymentStatus payByCardOnFile(String userId, double amount,
            String planId, List<String> promoCodeList, String mopCode,
            com.telespree.barracuda.beans.CreditCardType mopType,
            String taxZipcode, boolean useBalance)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.MOP_USERID_EMPTY);
            log.error(QualutionBillingServiceError.MOP_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(planId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty plan ID.");
            bse.setError(QualutionBillingServiceError.MOP_PLANID_EMPTY);
            log.error(QualutionBillingServiceError.MOP_PLANID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    "Payment amount less than or equal to zero.");
            bse.setError(QualutionBillingServiceError.MOP_AMOUNT_INVALID);
            log.error(QualutionBillingServiceError.MOP_AMOUNT_INVALID,
                    getDeviceId());
            throw bse;
        }
        if (mopType == null) {
            BillingServiceException bse = new BillingServiceException(
                    "No mop type provided.");
            bse.setError(QualutionBillingServiceError.MOP_TYPE_INVALID);
            log.error(QualutionBillingServiceError.MOP_TYPE_INVALID,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(mopCode)) {
            BillingServiceException bse = new BillingServiceException(
                    "No mop code provided.");
            bse.setError(QualutionBillingServiceError.MOP_CODE_EMPTY);
            log.error(QualutionBillingServiceError.MOP_CODE_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(taxZipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    "No tax zipcode provided.");
            bse.setError(QualutionBillingServiceError.MOP_NOTAXZIPCODE);
            log.error(QualutionBillingServiceError.MOP_NOTAXZIPCODE,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            PurchaseRequestType purchaseRequestType = new PurchaseRequestType();
            purchaseRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
                storedCreditCardInfoType.setCardType(CreditCardType.visa);
                break;
            case MASTERCARD:
                storedCreditCardInfoType.setCardType(CreditCardType.masterCard);
                break;
            case AMEX:
                storedCreditCardInfoType.setCardType(CreditCardType.amex);
                break;
            case DISCOVER:
                storedCreditCardInfoType.setCardType(CreditCardType.discover);
                break;
            case DINERSCLUB:
                storedCreditCardInfoType.setCardType(CreditCardType.dinersClub);
                break;
            default:
                BillingServiceException bse = new BillingServiceException(
                        "Unknown mop type provided.");
                bse.setError(QualutionBillingServiceError.MOP_TYPE_UNKNONW);
                log.error(QualutionBillingServiceError.MOP_TYPE_UNKNONW,
                        getDeviceId());
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
                        "Qualution returned invalid PurchaseByCardOnFile response.");
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.MOP_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution PurchaseByCardOnFile failed.");
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.MOP_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution PurchaseByCardOnFile returned error code.");
                bse.setError(QualutionBillingServiceError.MOP_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.MOP_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
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
                        getDeviceId());
                throw bse;
            }
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType);
            getMbodSession().setSubscriber(subscriber);
            getTelespreeSessionManager().saveSession();
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
            log.info(QualutionBillingServiceError.MOP_SUCCESS, getDeviceId(),
                    mopType.getDisplayName(), mopCode,
                    paymentStatus.getAmountChargedToCreditCard(), planId,
                    userId, paymentStatus.getCreditCardAuthorizationCode());
            return paymentStatus;
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.MOP_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.MOP_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (ApplicationException ae) {
            BillingServiceException bse = new BillingServiceException(
                    ae.getMessage(), ae);
            bse.setError(QualutionBillingServiceError.MOP_APPEXCEPTION);
            log.error(QualutionBillingServiceError.MOP_APPEXCEPTION,
                    getDeviceId(), (ae.getErrorCode() != null) ? ae
                            .getErrorCode().toString() : "n/a", ae.getMessage());
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.MOP_EXCEPTION);
            log.error(QualutionBillingServiceError.MOP_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * redeemPrepaidCard(java.lang.String, java.lang.String)
     */
    @Override
    public RedeemStatus redeemPrepaidCard(String userId, String code)
            throws BillingServiceException {

        if (!StringUtils.hasText(userId)) {
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
        BillingStub billingStub = getBillingStub();
        try {
            RedeemScratchCardRequestType redeemScratchCardRequestType = new RedeemScratchCardRequestType();
            redeemScratchCardRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
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
            Subscriber subscriber = mapSubscriberInfo(subscriberInfoType);
            getMbodSession().setSubscriber(subscriber);
            getTelespreeSessionManager().saveSession();
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
     * @see com.telespree.apps.sprint.mbod.rental.test.service.BillingService#
     * calculateTaxes (double, java.lang.String)
     */
    public Map<String, Double> calculateTaxes(double amount, String zipcode)
            throws BillingServiceException {

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
        BillingStub billingStub = getBillingStub();
        try {
            CalculateTaxRequestType calculateTaxRequestType = new CalculateTaxRequestType();
            calculateTaxRequestType.setCorpID(getMvnoExternalId());
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
     * @return
     * @throws ApplicationException
     */
    public final MbodSession getMbodSession() throws ApplicationException {

        MbodSession mbodSession = null;
        TelespreeBean bean = getSession().getProperty(MbodSession.BEAN_NAME);
        if (bean == null) {
            mbodSession = new MbodSession();
            getSession().setProperty(MbodSession.BEAN_NAME, mbodSession);
            getTelespreeSessionManager().saveSession();
        } else {
            mbodSession = (MbodSession) bean;
        }
        return mbodSession;
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
    protected BillingStub getBillingStub() throws BillingServiceException {

        try {
            ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
            String url = applicationConfiguration.getProperty(
                    PROP_QUALUTION_BILLING_URL,
                    PROP_QUALUTION_BILLING_URL_DEFAULT, getSession());
            BillingStub billingStub = new BillingStub(url);
            billingStub
                    ._getServiceClient()
                    .getOptions()
                    .setProperty(
                            org.apache.axis2.context.MessageContextConstants.CHUNKED,
                            "false");
            billingStub._getServiceClient().getOptions()
                    .setTimeOutInMilliSeconds(getBillingStubTimeout());
            return billingStub;
        } catch (AxisFault af) {
            BillingServiceException bse = new BillingServiceException(
                    af.getMessage(), af);
            bse.setError(QualutionBillingServiceError.GENERAL_STUB_AXISFAULT);
            log.error(QualutionBillingServiceError.GENERAL_STUB_AXISFAULT,
                    getDeviceId(), af.getMessage());
            throw bse;
        } catch (ApplicationException ae) {
            BillingServiceException bse = new BillingServiceException(
                    ae.getMessage(), ae);
            bse.setError(QualutionBillingServiceError.GENERAL_STUB_APPEXCEPTION);
            log.error(QualutionBillingServiceError.GENERAL_STUB_APPEXCEPTION,
                    getDeviceId(), (ae.getErrorCode() != null) ? ae
                            .getErrorCode().toString() : "n/a", ae.getMessage());
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.GENERAL_STUB_EXCEPTION);
            log.error(QualutionBillingServiceError.GENERAL_STUB_EXCEPTION,
                    getDeviceId(), ex.getMessage());
            throw bse;
        }
    }

    /**
     * @return
     * @throws Exception
     */
    protected long getBillingStubTimeout() throws Exception {

        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfig();
        long timeout = applicationConfiguration.getProperty(
                PROP_QUALUTION_BILLING_TIMEOUT,
                PROP_QUALUTION_BILLING_TIMEOUT_DEFAULT, getSession());
        return timeout;
    }

    /**
     * @param subscriberInfoType
     * @return
     * @throws BillingServiceException
     */
    protected Subscriber mapSubscriberInfo(SubscriberInfoType subscriberInfoType)
            throws BillingServiceException {

        try {
            Subscriber subscriber = new Subscriber();
            UserIDType userIDType = subscriberInfoType.getUserID();
            if (userIDType != null) {
                subscriber.setUserId(userIDType.getUserIDType());
            }
            SecurityQuestionIDType[] securityQuestionIDTypes = subscriberInfoType
                    .getSecurityQuestionID();
            if (ArrayUtils.isNotEmpty(securityQuestionIDTypes)) {
                subscriber.setSecurityQuestionId(securityQuestionIDTypes[0]
                        .getSecurityQuestionIDType());
                Map<String, String> securityQuestionMap = getMbodSession()
                        .getSecurityQuestionMap();
                if (!CollectionUtils.isEmpty(securityQuestionMap)) {
                    subscriber.setSecurityQuestionText(securityQuestionMap
                            .get(subscriber.getSecurityQuestionId()));
                }
            }
            EmailAddressType emailAddressType = subscriberInfoType.getEmail();
            if (emailAddressType != null) {
                subscriber.setEmail(emailAddressType.getEmailAddressType());
            }
            subscriber.setFirstName(subscriberInfoType.getFirstName());
            subscriber.setLastName(subscriberInfoType.getLastName());
            StoredCreditCardInfoType[] storedCreditCardInfoTypes = subscriberInfoType
                    .getStoredCreditCard();
            if (ArrayUtils.isNotEmpty(storedCreditCardInfoTypes)) {
                LastFourDigitsType lastFourDigitsType = storedCreditCardInfoTypes[0]
                        .getLastFourDigits();
                if (lastFourDigitsType != null) {
                    subscriber.setMopcode(lastFourDigitsType
                            .getLastFourDigitsType());
                }
                CreditCardType creditCardType = storedCreditCardInfoTypes[0]
                        .getCardType();
                if (CreditCardType.amex.equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.AMEX);
                } else if (CreditCardType.visa.equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.VISA);
                } else if (CreditCardType.masterCard.equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.MASTERCARD);
                } else if (CreditCardType.discover.equals(creditCardType)) {
                    subscriber
                            .setMoptype(com.telespree.barracuda.beans.CreditCardType.DISCOVER);
                } else if (CreditCardType.dinersClub.equals(creditCardType)) {
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
        } catch (ApplicationException ae) {
            BillingServiceException bse = new BillingServiceException(
                    ae.getMessage(), ae);
            bse.setError(QualutionBillingServiceError.GENERAL_MAP_SUB);
            log.error(QualutionBillingServiceError.GENERAL_MAP_SUB,
                    getDeviceId(), (ae.getErrorCode() != null) ? ae
                            .getErrorCode().toString() : "n/a", ae.getMessage());
            throw bse;
        }
    }

    /**
     * @return
     */
    private String getDeviceId() {

        try {
            DeviceIdentifier deviceId = getDeviceIdentifier();
            return deviceId.getID();
        } catch (ApplicationException ae) {
            log.error(ae.getMessage());
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.apps.sprint.mbod.rental.service.BillingService#update(com
     * .telespree.apps.sprint.mbod.rental.model.Subscriber)
     */
    @Override
    public Subscriber update(Subscriber subscriber)
            throws BillingServiceException {

        String userId = subscriber.getUserId();
        String securityQuestionId = subscriber.getSecurityQuestionId();
        String securityAnswer = subscriber.getSecurityAnswer();
        String email = subscriber.getEmail();
        String firstName = subscriber.getFirstName();
        String lastName = subscriber.getLastName();
        String loyaltyReference = subscriber.getLoyaltyReference();
        Address address = subscriber.getAddress();
        if (!StringUtils.hasText(userId)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty user ID.");
            bse.setError(QualutionBillingServiceError.UPDATE_USERID_EMPTY);
            log.error(QualutionBillingServiceError.UPDATE_USERID_EMPTY,
                    getDeviceId());
            throw bse;
        }
        if (!StringUtils.hasText(email)) {
            BillingServiceException bse = new BillingServiceException(
                    "Empty email.");
            bse.setError(QualutionBillingServiceError.UPDATE_EMAIL_EMPTY);
            log.error(QualutionBillingServiceError.UPDATE_EMAIL_EMPTY,
                    getDeviceId());
            throw bse;
        }
        BillingStub billingStub = getBillingStub();
        try {
            UpdateSubscriberRequestType updateSubscriberRequestType = new UpdateSubscriberRequestType();
            updateSubscriberRequestType.setCorpID(getMvnoExternalId());
            UserIDType userIDType = new UserIDType();
            userIDType.setUserIDType(userId);
            updateSubscriberRequestType.setUserID(userIDType);
            if (StringUtils.hasText(securityQuestionId)
                    && StringUtils.hasText(securityAnswer)) {
                SecurityQuestionAnswerType securityQuestionAnswerType = new SecurityQuestionAnswerType();
                SecurityQuestionIDType securityQuestionIDType = new SecurityQuestionIDType();
                securityQuestionIDType
                        .setSecurityQuestionIDType(securityQuestionId);
                SecurityAnswerType securityAnswerType = new SecurityAnswerType();
                securityAnswerType.setSecurityAnswerType(securityAnswer);
                securityQuestionAnswerType
                        .setQuestionID(securityQuestionIDType);
                securityQuestionAnswerType.setAnswer(securityAnswerType);
                updateSubscriberRequestType
                        .addSecurityQuestionAnswer(securityQuestionAnswerType);
            }
            EmailAddressType emailAddressType = new EmailAddressType();
            emailAddressType.setEmailAddressType(email);
            updateSubscriberRequestType.setEmail(emailAddressType);
            if (StringUtils.hasText(firstName)) {
                updateSubscriberRequestType.setFirstName(firstName);
            }
            if (StringUtils.hasText(lastName)) {
                updateSubscriberRequestType.setLastName(lastName);
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
                        "Qualution returned invalid UpdateSubscriber response.");
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_INVALID);
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_INVALID,
                        getDeviceId());
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
                        "Qualution UpdataeSubscriber failed.");
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_FAILURE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_FAILURE,
                        getDeviceId(), statusType.getValue(),
                        errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            if (errorCodeType != null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution UpdateSubscriber returned error code.");
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_HASCODE);
                QualutionBillingServiceError qualutionBillingServiceError = QualutionBillingServiceError
                        .get(errorCodeType.getErrorCodeType());
                if (qualutionBillingServiceError != null) {
                    bse.setError(qualutionBillingServiceError);
                }
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_HASCODE,
                        getDeviceId(), errorCodeType.getErrorCodeType(),
                        errorMessageType.getErrorMessageType());
                throw bse;
            }
            SubscriberInfoType subscriberInfoType = updateSubscriberResponseType
                    .getSubscriberInfo();
            if (subscriberInfoType == null) {
                BillingServiceException bse = new BillingServiceException(
                        "Qualution UpdateSubscriber did not return subscriber information.");
                bse.setError(QualutionBillingServiceError.UPDATE_RESPONSE_NOSUB);
                log.error(QualutionBillingServiceError.UPDATE_RESPONSE_NOSUB,
                        getDeviceId());
                throw bse;
            }
            log.info(QualutionBillingServiceError.UPDATE_SUCCESS,
                    getDeviceId(), userId);
            return mapSubscriberInfo(subscriberInfoType);
        } catch (RemoteException re) {
            BillingServiceException bse = new BillingServiceException(
                    re.getMessage(), re);
            bse.setError(QualutionBillingServiceError.UPDATE_REMOTE_EXCEPTION);
            log.error(QualutionBillingServiceError.UPDATE_REMOTE_EXCEPTION,
                    getDeviceId(), re.getMessage());
            throw bse;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    ex.getMessage(), ex);
            bse.setError(QualutionBillingServiceError.UPDATE_EXCEPTION);
            log.error(QualutionBillingServiceError.UPDATE_EXCEPTION,
                    getDeviceId(), ex.getMessage());
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

}
