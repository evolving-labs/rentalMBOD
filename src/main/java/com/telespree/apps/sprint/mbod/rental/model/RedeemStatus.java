/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.model;

import java.io.Serializable;
import java.util.Calendar;

/**
 * RedeemStatus
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/04/19 00:31:38 $
 * @version $Revision: #2 $
 * 
 */
public class RedeemStatus implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private float amountRedeemed;

    private String authorizationCode;

    private Calendar transactionTime;

    /**
     * @return the amountRedeemed
     */
    public float getAmountRedeemed() {
        return amountRedeemed;
    }

    /**
     * @param amountRedeemed
     *            the amountRedeemed to set
     */
    public void setAmountRedeemed(float amountRedeemed) {
        this.amountRedeemed = amountRedeemed;
    }

    /**
     * @return the authorizationCode
     */
    public String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * @param authorizationCode
     *            the authorizationCode to set
     */
    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
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

}
