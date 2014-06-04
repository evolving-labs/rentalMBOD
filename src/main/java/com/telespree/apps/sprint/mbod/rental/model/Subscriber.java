/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.model;

import java.io.Serializable;

import telespree.apps.fwk.common.Address;

import com.telespree.barracuda.beans.CreditCardType;

/**
 * Subscriber
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/01/16 16:51:47 $
 * @version $Revision: #6 $
 * 
 */
public class Subscriber implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String firstName;

    private String lastName;

    private String userId;

    private String email;

    private CreditCardType moptype;

    private String mopcode;

    private String securityQuestionId;

    private String securityQuestionText;

    private String securityAnswer;

    private Address address;

    private double accountBalance;

    private String loyaltyReference;

    private String userIdHtml;

    private String firstNameHtml;

    private String lastNameHtml;

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
        this.userId = userId;
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
     * @return the moptype
     */
    public CreditCardType getMoptype() {
        return moptype;
    }

    /**
     * @param moptype
     *            the moptype to set
     */
    public void setMoptype(CreditCardType moptype) {
        this.moptype = moptype;
    }

    /**
     * @return the mopcode
     */
    public String getMopcode() {
        return mopcode;
    }

    /**
     * @param mopcode
     *            the mopcode to set
     */
    public void setMopcode(String mopcode) {
        this.mopcode = mopcode;
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
     * @return the securityQuestionText
     */
    public String getSecurityQuestionText() {
        return securityQuestionText;
    }

    /**
     * @param securityQuestionText
     *            the securityQuestionText to set
     */
    public void setSecurityQuestionText(String securityQuestionText) {
        this.securityQuestionText = securityQuestionText;
    }

    /**
     * @return the address
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @param address
     *            the address to set
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @return the accountBalance
     */
    public double getAccountBalance() {
        return accountBalance;
    }

    /**
     * @param accountBalance
     *            the accountBalance to set
     */
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
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
     * @return the userIdHtml
     */
    public String getUserIdHtml() {
        return userIdHtml;
    }

    /**
     * @param userIdHtml
     *            the userIdHtml to set
     */
    public void setUserIdHtml(String userIdHtml) {
        this.userIdHtml = userIdHtml;
    }

    /**
     * @return the firstNameHtml
     */
    public String getFirstNameHtml() {
        return firstNameHtml;
    }

    /**
     * @param firstNameHtml
     *            the firstNameHtml to set
     */
    public void setFirstNameHtml(String firstNameHtml) {
        this.firstNameHtml = firstNameHtml;
    }

    /**
     * @return the lastNameHtml
     */
    public String getLastNameHtml() {
        return lastNameHtml;
    }

    /**
     * @param lastNameHtml
     *            the lastNameHtml to set
     */
    public void setLastNameHtml(String lastNameHtml) {
        this.lastNameHtml = lastNameHtml;
    }

}
