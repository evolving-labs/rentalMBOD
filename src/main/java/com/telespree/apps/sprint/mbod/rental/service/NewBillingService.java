/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service;

import java.util.List;
import java.util.Map;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.common.CreditCard;
import telespree.apps.fwk.session.TelespreeSession;

import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.RedeemStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.barracuda.beans.CreditCardType;

/**
 * NewBillingService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/09/27 23:08:07 $
 * @version $Revision: #4 $
 * 
 */
public interface NewBillingService {

    /**
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public boolean healthCheck(ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param historyNotes
     * @param deviceId
     * @param historyType
     * @param appConfiguration
     * @param telespreeSession
     * @throws BillingServiceException
     */
    public void logSubscriberHistory(String corpId, String userId,
            String historyNotes, String deviceId, HistoryType historyType,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public List<SubscriberHistory> getSubscriberHistory(String corpId,
            String userId, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param password
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public Subscriber authenticate(String corpId, String userId,
            String password, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param password
     * @param securityQuestionIdAnswerMap
     * @param email
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public Subscriber register(String corpId, String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param password
     * @param securityQuestionIdAnswerMap
     * @param email
     * @param firstName
     * @param lastName
     * @param address
     * @param loyaltyReference
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public Subscriber register(String corpId, String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email,
            String firstName, String lastName, Address address,
            String loyaltyReference, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param subscriber
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public Subscriber update(String corpId, Subscriber subscriber,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public List<String> retrieveSecurityQuestionIdList(String corpId,
            String userId, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param securityQuestionIdAnswerMap
     * @param newPassword
     * @param appConfiguration
     * @param telespreeSession
     * @throws BillingServiceException
     */
    public void resetPassword(String corpId, String userId,
            Map<String, String> securityQuestionIdAnswerMap,
            String newPassword, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByBalance(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @param creditCard
     * @param storeCreditCard
     * @param taxZipcode
     * @param useBalance
     * @param cardHolderName
     * @param cardHolderCountry
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByCreditCard(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            CreditCard creditCard, boolean storeCreditCard, String taxZipcode,
            boolean useBalance, String cardHolderName,
            String cardHolderCountry,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @param mopCode
     * @param mopType
     * @param taxZipcode
     * @param useBalance
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByCardOnFile(String corpId, String userId,
            double amount, String planId, List<String> promoCodeList,
            String mopCode, CreditCardType mopType, String taxZipcode,
            boolean useBalance, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param userId
     * @param code
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public RedeemStatus redeemPrepaidCard(String corpId, String userId,
            String code, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

    /**
     * @param corpId
     * @param amount
     * @param zipcode
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public Map<String, Double> calculateTaxes(String corpId, double amount,
            String zipcode, ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException;

}
