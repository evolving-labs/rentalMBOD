/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.HashMap;
import java.util.Map;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

/**
 * QualutionBillingServiceError
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/03/27 21:25:49 $
 * @version $Revision: #8 $
 * 
 */
@BaseName("QualutionBillingServiceError")
@LocaleData({ @Locale("en_US") })
public enum QualutionBillingServiceError {
    
    AUTHENTICATE_CORPID_EMPTY, AUTHENTICATE_USERID_EMPTY, AUTHENTICATE_PASSWORD_EMPTY, AUTHENTICATE_RESPONSE_INVALID, AUTHENTICATE_RESPONSE_FAILURE, AUTHENTICATE_RESPONSE_HASCODE, AUTHENTICATE_RESPONSE_NOSUB, AUTHENTICATE_REMOTE_EXCEPTION, AUTHENTICATE_EXCEPTION, AUTHENTICATE_SUCCESS, 
    REGISTER_CORPID_EMPTY, REGISTER_USERID_EMPTY, REGISTER_USERID_TAKEN, REGISTER_PASSWORD_EMPTY, REGISTER_SECURITY_EMPTY, REGISTER_EMAIL_EMPTY, REGISTER_RESPONSE_INVALID, REGISTER_RESPONSE_FAILURE, REGISTER_RESPONSE_HASCODE, REGISTER_RESPONSE_NOSUB, REGISTER_REMOTE_EXCEPTION, REGISTER_EXCEPTION, REGISTER_SUCCESS, 
    GENERAL_MAP_SUB, GENERAL_MAP_SUB_T, GENERAL_STUB_AXISFAULT, GENERAL_STUB_APPEXCEPTION, GENERAL_STUB_EXCEPTION, GENERAL_MBODSESSION, 
    SECURITYQUESTION_CORPID_EMPTY, SECURITYQUESTION_USERID_EMPTY, SECURITYQUESTION_RESPONSE_INVALID, SECURITYQUESTION_RESPONSE_FAILURE, SECURITYQUESTION_RESPONSE_HASCODE, SECURITYQUESTION_REMOTE_EXCEPTION, SECURITYQUESTION_EXCEPTION, SECURITYQUESTION_SUCCESS, SECURITYQUESTION_SUCCESS_ZERO, 
    RESETPASSWORD_CORPID_EMPTY, RESETPASSWORD_USERID_EMPTY, RESETPASSWORD_PASSWORD_EMPTY, RESETPASSWORD_SECURITY_EMPTY, RESETPASSWORD_RESPONSE_INVALID, RESETPASSWORD_RESPONSE_FAILURE, RESETPASSWORD_RESPONSE_HASCODE, RESETPASSWORD_RESPONSE_NOSUB, RESETPASSWORD_REMOTE_EXCEPTION, RESETPASSWORD_EXCEPTION, RESETPASSWORD_SUCCESS, 
    CC_CORPID_EMPTY, CC_ANONYMOUS_USERID_EMPTY, CC_ANONYMOUS_USERID_EXCEPTION, CC_PLANID_EMPTY, CC_AMOUNT_INVALID, CC_NO, CC_TYPE_INVALID, CC_NOBILLING, CC_NOCODE, CC_NOTAXZIPCODE, CC_RESPONSE_INVALID, CC_RESPONSE_FAILURE, CC_RESPONSE_HASCODE, CC_SUCCESS, CC_SUCCESS_HASSUB, CC_REMOTE_EXCEPTION, CC_APPEXCEPTION, CC_EXCEPTION, 
    MOP_CORPID_EMPTY, MOP_USERID_EMPTY, MOP_PLANID_EMPTY, MOP_AMOUNT_INVALID, MOP_TYPE_INVALID, MOP_TYPE_UNKNONW, MOP_CODE_EMPTY, MOP_NOTAXZIPCODE, MOP_RESPONSE_INVALID, MOP_RESPONSE_FAILURE, MOP_RESPONSE_HASCODE, MOP_SUCCESS, MOP_RESPONSE_NOSUB, MOP_REMOTE_EXCEPTION, MOP_APPEXCEPTION, MOP_EXCEPTION,
    UPDATE_CORPID_EMPTY, UPDATE_USERID_EMPTY, UPDATE_SECURITY_QUESTION_EMPTY, UPDATE_SECURITY_ANSWER_EMPTY, UPDATE_EMAIL_EMPTY, UPDATE_RESPONSE_INVALID, UPDATE_RESPONSE_FAILURE, UPDATE_RESPONSE_HASCODE, UPDATE_RESPONSE_NOSUB, UPDATE_SUCCESS, UPDATE_REMOTE_EXCEPTION, UPDATE_EXCEPTION,
    LOGHISTORY_CORPID_EMPTY, LOGHISTORY_USERID_EMPTY, LOGHISTORY_HISTORYTYPE_EMPTY, LOGHISTORY_HISTORYNOTES_EMPTY, LOGHISTORY_RESPONSE_INVALID, LOGHISTORY_RESPONSE_FAILURE, LOGHISTORY_RESPONSE_HASCODE, LOGHISTORY_SUCCESS, LOGHISTORY_REMOTE_EXCEPTION, LOGHISTORY_EXCEPTION,
    GETHISTORY_RESPONSE_HASCODE, GETHISTORY_RESPONSE_FAILURE, GETHISTORY_CORPID_EMPTY, GETHISTORY_USERID_EMPTY, GETHISTORY_RESPONSE_INVALID, GETHISTORY_REMOTE_EXCEPTION, GETHISTORY_EXCEPTION, GETHISTORY_SUCCESS, GETHISTORY_NOHISTORY,
    HEALTHCHECK_RESPONSE_INVALID, HEALTHCHECK_REMOTE_EXCEPTION, HEALTHCHECK_EXCEPTION,
    CORE_SESSION_UPDATE_DAL, CORE_SESSION_UPDATE_T;
    
    
    private static Map<String, QualutionBillingServiceError> lookup = new HashMap<String, QualutionBillingServiceError>();
    static {
        lookup.put("1529", QualutionBillingServiceError.REGISTER_USERID_TAKEN);
    }

    /**
     * @param code
     * @return
     */
    public static QualutionBillingServiceError get(String code) {
        return lookup.get(code);
    }
}
