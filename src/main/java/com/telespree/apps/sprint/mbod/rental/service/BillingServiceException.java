/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service;

import com.telespree.barracuda.exception.BarracudaException;

/**
 * BillingServiceException
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2012/04/10 23:24:02 $
 * @version $Revision: #1 $
 * 
 */
public class BillingServiceException extends BarracudaException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     * @param cause
     */
    public BillingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     */
    public BillingServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public BillingServiceException(Throwable cause) {
        super(cause);
    }

}
