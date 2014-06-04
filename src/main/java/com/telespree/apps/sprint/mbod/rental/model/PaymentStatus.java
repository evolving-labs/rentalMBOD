/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.model;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 * PaymentStatus
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/10/02 01:11:25 $
 * @version $Revision: #6 $
 * 
 */
public class PaymentStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private float amountChargedToBalance = 0;

    private float amountChargedToCreditCard = 0;

    private float amountBilled = 0;

    private String creditCardAuthorizationCode;

    private float amountStillOwe = 0;

    private Calendar transactionTime;

    private String ccLast4Digits;

    private boolean usedBalance;

    /**
     * @return the amountChargedToBalance
     */
    public float getAmountChargedToBalance() {
        return amountChargedToBalance;
    }

    /**
     * @param amountChargedToBalance
     *            the amountChargedToBalance to set
     */
    public void setAmountChargedToBalance(float amountChargedToBalance) {
        this.amountChargedToBalance = amountChargedToBalance;
    }

    /**
     * @return the amountChargedToCreditCard
     */
    public float getAmountChargedToCreditCard() {
        return amountChargedToCreditCard;
    }

    /**
     * @param amountChargedToCreditCard
     *            the amountChargedToCreditCard to set
     */
    public void setAmountChargedToCreditCard(float amountChargedToCreditCard) {
        this.amountChargedToCreditCard = amountChargedToCreditCard;
    }

    /**
     * @return
     */
    public String getAmountChargedToCreditCardString() {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(amountChargedToCreditCard);
    }

    /**
     * @return the creditCardAuthorizationCode
     */
    public String getCreditCardAuthorizationCode() {
        return creditCardAuthorizationCode;
    }

    /**
     * @param creditCardAuthorizationCode
     *            the creditCardAuthorizationCode to set
     */
    public void setCreditCardAuthorizationCode(
            String creditCardAuthorizationCode) {
        this.creditCardAuthorizationCode = creditCardAuthorizationCode;
    }

    /**
     * @return the amountStillOwe
     */
    public float getAmountStillOwe() {
        return amountStillOwe;
    }

    /**
     * @param amountStillOwe
     *            the amountStillOwe to set
     */
    public void setAmountStillOwe(float amountStillOwe) {
        this.amountStillOwe = amountStillOwe;
    }

    /**
     * @return the transactionTime
     */
    public Calendar getTransactionTime() {
        return transactionTime;
    }

    /**
     * @param transactionTime
     *            the transactionTime to set
     */
    public void setTransactionTime(Calendar transactionTime) {
        this.transactionTime = transactionTime;
    }

    /**
     * @return the ccLast4Digits
     */
    public String getCcLast4Digits() {
        return ccLast4Digits;
    }

    /**
     * @param ccLast4Digits
     *            the ccLast4Digits to set
     */
    public void setCcLast4Digits(String ccLast4Digits) {
        this.ccLast4Digits = ccLast4Digits;
    }

    /**
     * @return the amountBilled
     */
    public float getAmountBilled() {
        return amountBilled;
    }

    /**
     * @param amountBilled
     *            the amountBilled to set
     */
    public void setAmountBilled(float amountBilled) {
        this.amountBilled = amountBilled;
    }

    /**
     * @return the usedBalance
     */
    public boolean isUsedBalance() {
        return usedBalance;
    }

    /**
     * @param usedBalance
     *            the usedBalance to set
     */
    public void setUsedBalance(boolean usedBalance) {
        this.usedBalance = usedBalance;
    }
}
