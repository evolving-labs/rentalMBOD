/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service;

import java.util.List;
import java.util.Map;

import telespree.apps.fwk.common.Address;
import telespree.apps.fwk.common.CreditCard;

import com.telespree.apps.sprint.mbod.rental.model.HistoryType;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.model.RedeemStatus;
import com.telespree.apps.sprint.mbod.rental.model.Subscriber;
import com.telespree.apps.sprint.mbod.rental.model.SubscriberHistory;
import com.telespree.barracuda.beans.CreditCardType;

/**
 * BillingService
 * 
 * 
 * @author $Author: michael $ on $DateTime: 2012/11/29 17:34:18 $
 * @version $Revision: #9 $
 * 
 */
@Deprecated
public interface BillingService {

    /**
     * 
     * @param userId
     * @param historyNotes
     * @param deviceId
     * @param historyType
     * @return
     * @throws BillingServiceException
     */
    public void logSubscriberHistory(String userId, String historyNotes,
            String deviceId, HistoryType historyType)
            throws BillingServiceException;

    /**
     * @param userId
     * @return
     * @throws BillingServiceException
     */
    public List<SubscriberHistory> getSubscriberHistory(String userId)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param password
     * @return
     * @throws BillingServiceException
     */
    public Subscriber authenticate(String userId, String password)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param password
     * @param securityQuestionIdAnswerMap
     * @param email
     * @return
     * @throws BillingServiceException
     */
    public Subscriber register(String userId, String password,
            Map<String, String> securityQuestionIdAnswerMap, String email)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param password
     * @param securityQuestionAnswerMap
     * @param email
     * @param firstName
     * @param lastName
     * @param address
     * @param loyaltyReference
     * @return
     * @throws BillingServiceException
     */
    public Subscriber register(String userId, String password,
            Map<String, String> securityQuestionAnswerMap, String email,
            String firstName, String lastName, Address address,
            String loyaltyReference) throws BillingServiceException;

    /**
     * @param subscriber
     * @return
     * @throws BillingServiceException
     */
    public Subscriber update(Subscriber subscriber)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @return
     * @throws BillingServiceException
     */
    public List<String> retrieveSecurityQuestionIdList(String userId)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param securityQuestionIdAnswerMap
     * @param newPassword
     * @throws BillingServiceException
     */
    public void resetPassword(String userId,
            Map<String, String> securityQuestionIdAnswerMap, String newPassword)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByBalance(String userId, double amount,
            String planId, List<String> promoCodeList)
            throws BillingServiceException;

    /**
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @param creditCard
     * @param storeCreditCard
     * @param taxZipcode
     * @param useBalance
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByCreditCard(String userId, double amount,
            String planId, List<String> promoCodeList, CreditCard creditCard,
            boolean storeCreditCard, String taxZipcode, boolean useBalance)
            throws BillingServiceException;

    /**
     * @param userId
     * @param amount
     * @param planId
     * @param promoCodeList
     * @param mopCode
     * @param mopType
     * @param taxZipcode
     * @param useBalance
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByCardOnFile(String userId, double amount,
            String planId, List<String> promoCodeList, String mopCode,
            CreditCardType mopType, String taxZipcode, boolean useBalance)
            throws BillingServiceException;

    /**
     * 
     * @param userId
     * @param code
     * @return
     * @throws BillingServiceException
     */
    public RedeemStatus redeemPrepaidCard(String userId, String code)
            throws BillingServiceException;

    /**
     * 
     * @param amount
     * @param zipcode
     * @return
     * @throws BillingServiceException
     */
    public Map<String, Double> calculateTaxes(double amount, String zipcode)
            throws BillingServiceException;

}
