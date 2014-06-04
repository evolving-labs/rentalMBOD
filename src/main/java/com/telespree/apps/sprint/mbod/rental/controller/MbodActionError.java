/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.controller;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * MbodActionError
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: juan $ on $DateTime: 2013/10/30 16:15:57 $
 * @version $Revision: #9 $
 * 
 */
@BaseName("MbodActionError")
@LocaleData({ @Locale("en_US") })
public enum MbodActionError {
    TOPUP_DEVICESTATUS_DSE, TOPUP_DEVICESTATUS_EMPTY, TOPUP_DEVICESTATUS_AVAILABLE, TOPUP_DEVICESTATUS_CHECKEDOUT, TOPUP_DEVICESTATUS_INUSE, TOPUP_DEVICESTATUS_SUSPENDED, TOPUP_DEVICESTATUS_SOLD, TOPUP_DEVICESTATUS_UNKNOWN,
    TOPUP_RENTALINFO_RSE, TOPUP_RENTALINFO_NONE, TOPUP_RENTALINFO_FOUND, TOPUP_RENTALINFO_EXPIRED,
    PRODUCT_TC_SET, PRODUCT_TC_SET_RSE, PRODUCT_TC_GET_SESSION, PRODUCT_TC_GET, PRODUCT_TC_GET_RSE,
    PRODUCT_SELECT_PLANID_INVALID, PRODUCT_SELECT_LIST_EMPTY, PRODUCT_SELECTED, PRODUCT_SELECT_NOMATCH,
    PROMOTION_NOCODE, PROMOTION_NOPRODUCT, PROMOTION_PRODUCT_SELECTED, PROMOTION_NOPRODUCT_PCSE, PROMOTION_NOPRODUCT_EXCEPTION, PROMOTION_REDEEMED,
    PURCHASE_NOPRODUCTSELECTED, PURCHASED, PURCHASE_DSE,
    ACCESS_URL, ACCESS_STATUS, ACCESS_HTTPEXCEPTION, ACCESS_IOEXCEPTION, ACCESS_NOPRODUCT, ACCESS_NORATINGGROUP,
    PURCHASE_PURCHASETYPE_ACCOUNT_BALANCE, PURCHASE_PURCHASETYPE_PAY_LATER,
    SUBSCRIBER_WELCOME_EMAIL_FAILURE, REDEEM_PIN_INVALID
}
