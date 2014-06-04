package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.cal10n.LocLogger;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import telespree.apps.fwk.ApplicationConfiguration;
import telespree.apps.fwk.common.CreditCard;
import telespree.apps.fwk.common.Device;
import telespree.apps.fwk.session.TelespreeSession;
import telespree.apps.fwk.session.TelespreeSessionUtil;

import com.telespree.apps.sprint.mbod.rental.MbodConstants;
import com.telespree.apps.sprint.mbod.rental.model.PaymentStatus;
import com.telespree.apps.sprint.mbod.rental.service.BillingServiceException;
import com.telespree.barracuda.beans.DeviceID;
import com.telespree.barracuda.exception.DeviceIdException;
import com.telespree.barracuda.log.TelespreeLogger;

/**
 * AuthorizeDotNetService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/04/11 23:36:05 $
 * @version $Revision: #10 $
 * 
 */
@Service("authorizeDotNetService")
public class AuthorizeDotNetService implements MbodConstants {

    private static final String prodAuthorizeURL = "https://secure.authorize.net/gateway/transact.dll";

    private static final String testAuthorizeURL = "https://test.authorize.net/gateway/transact.dll";

    private static final LocLogger log = TelespreeLogger
            .getLogger(AuthorizeDotNetService.class);

    /**
     * @param userId
     * @param amount
     * @param planId
     * @param creditCard
     * @param taxZipcode
     * @param appConfiguration
     * @param telespreeSession
     * @return
     * @throws BillingServiceException
     */
    public PaymentStatus payByCreditCard(String userId, double amount,
            String planId, CreditCard creditCard, String taxZipcode,
            ApplicationConfiguration appConfiguration,
            TelespreeSession telespreeSession) throws BillingServiceException {

        String deviceId = getDeviceId(telespreeSession);
        boolean isTestEnvironment = appConfiguration.getProperty(
                PROP_MVNO_CC_ANET_ENV_IS_TEST,
                PROP_MVNO_CC_ANET_ENV_IS_TEST_DEFAULT);
        if (amount <= 0) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_AMOUNT_INVALID,
                            deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_AMOUNT_INVALID);
            log.error(AuthorizeDotNetServiceError.CC_AMOUNT_INVALID, deviceId);
            throw bse;
        }
        if (creditCard == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_NO, deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_NO);
            log.error(AuthorizeDotNetServiceError.CC_NO, deviceId);
            throw bse;
        }
        com.telespree.barracuda.beans.CreditCardType ccType = com.telespree.barracuda.beans.CreditCardType
                .getTypeByNumber(creditCard.getNumber());
        if (ccType == null) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_TYPE_INVALID,
                            deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_TYPE_INVALID);
            log.error(AuthorizeDotNetServiceError.CC_TYPE_INVALID, deviceId);
            throw bse;

        }
        if (creditCard.getBillingAddress() == null
                || !StringUtils.hasText(creditCard.getBillingAddress()
                        .getPostalCode())) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_NOBILLING, deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_NOBILLING);
            log.error(AuthorizeDotNetServiceError.CC_NOBILLING, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(creditCard.getSecurityCode())) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_NOCODE, deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_NOCODE);
            log.error(AuthorizeDotNetServiceError.CC_NOCODE, deviceId);
            throw bse;
        }
        if (!StringUtils.hasText(taxZipcode)) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_NOTAXZIPCODE,
                            deviceId));
            bse.setError(AuthorizeDotNetServiceError.CC_NOTAXZIPCODE);
            log.error(AuthorizeDotNetServiceError.CC_NOTAXZIPCODE, deviceId);
            throw bse;
        }

        try {
            URL post_url = null;
            Map<String, String> post_values = new HashMap<String, String>();
            if (isTestEnvironment) {
                post_url = new URL(testAuthorizeURL);
                post_values.put("x_login", appConfiguration.getProperty(
                        PROP_ANET_TEST_LOGIN, PROP_ANET_TEST_LOGIN_DEFAULT));
                post_values.put("x_tran_key", appConfiguration.getProperty(
                        PROP_ANET_TEST_KEY, PROP_ANET_TEST_KEY_DEFAULT));
            } else {
                post_url = new URL(prodAuthorizeURL);
                post_values.put("x_login", appConfiguration.getProperty(
                        PROP_ANET_LIVE_LOGIN, PROP_ANET_LIVE_LOGIN_DEFAULT));
                post_values.put("x_tran_key", appConfiguration.getProperty(
                        PROP_ANET_LIVE_KEY, PROP_ANET_LIVE_KEY_DEFAULT));
            }

            post_values.put("x_version", "3.1");
            post_values.put("x_delim_data", "TRUE");
            post_values.put("x_delim_char", "|");
            post_values.put("x_relay_response", "FALSE");

            post_values.put("x_type", "AUTH_CAPTURE");
            post_values.put("x_method", "CC");
            post_values.put("x_card_num", creditCard.getNumber());
            post_values.put(
                    "x_exp_date",
                    Integer.toString(creditCard.getExpirationMonth())
                            + Integer.toString(creditCard.getExpirationYear()));
            post_values.put("x_card_code", creditCard.getSecurityCode());
            post_values.put("x_amount", formatDoubleAmountToString(amount));
            post_values.put("x_description", "Golden Gate Mobile");

            // This section takes the input fields and converts them to the
            // proper format
            // for an http post. For example:
            // "x_login=username&x_tran_key=a1B2c3D4"
            StringBuffer post_string = new StringBuffer();
            Set<Entry<String, String>> entrySet = post_values.entrySet();
            for (Entry<String, String> entry : entrySet) {
                String key = URLEncoder.encode(entry.getKey(), "UTF-8");
                String value = URLEncoder.encode(entry.getValue(), "UTF-8");
                post_string.append(key + "=" + value + "&");
            }

            URLConnection connection = post_url.openConnection();
            if (connection == null) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        AuthorizeDotNetServiceError.CC_ENDPOINT_FAILURE,
                                        deviceId, post_url.toString()));
                bse.setError(AuthorizeDotNetServiceError.CC_ENDPOINT_FAILURE);
                log.error(AuthorizeDotNetServiceError.CC_ENDPOINT_FAILURE,
                        deviceId, post_url.toString());
                throw bse;
            }
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // this line is not necessarily required but fixes a bug with some
            // servers
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            // submit the post_string and close the connection
            DataOutputStream requestObject = new DataOutputStream(
                    connection.getOutputStream());
            requestObject.write(post_string.toString().getBytes());
            requestObject.flush();
            requestObject.close();

            // process and read the gateway response
            BufferedReader rawResponse = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String responseData = rawResponse.readLine();
            rawResponse.close();
            if (!StringUtils.hasText(responseData)
                    || ArrayUtils.isEmpty(responseData.split("\\|"))) {
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger
                                .getEventMessage(
                                        AuthorizeDotNetServiceError.CC_RESPONSE_INVALID,
                                        deviceId,
                                        StringUtils.hasText(responseData) ? responseData
                                                .replaceAll("\\|", ",") : null));
                bse.setError(AuthorizeDotNetServiceError.CC_RESPONSE_INVALID);
                log.error(
                        AuthorizeDotNetServiceError.CC_RESPONSE_INVALID,
                        deviceId,
                        StringUtils.hasText(responseData) ? responseData
                                .replaceAll("\\|", ",") : null);
                throw bse;
            }
            log.info(AuthorizeDotNetServiceError.CC_RESPONSE, deviceId,
                    responseData.replaceAll("\\|", ","));
            String[] responses = responseData.split("\\|");
            if (!"1".equals(responses[0])) {
                String notes = (responses.length >= 4) ? responses[3] : "";
                BillingServiceException bse = new BillingServiceException(
                        TelespreeLogger.getEventMessage(
                                AuthorizeDotNetServiceError.CC_FAILURE,
                                deviceId, responses[0], notes));
                bse.setError(AuthorizeDotNetServiceError.CC_FAILURE);
                log.error(AuthorizeDotNetServiceError.CC_FAILURE, deviceId,
                        responses[0], notes);
                throw bse;
            }
            String authcode = (responses.length >= 5) ? responses[4] : "n/a";
            PaymentStatus paymentStatus = new PaymentStatus();
            paymentStatus.setAmountChargedToCreditCard((float) amount);
            paymentStatus.setAmountBilled(0);
            paymentStatus.setAmountChargedToBalance(0);
            paymentStatus.setAmountStillOwe(0);
            paymentStatus.setCreditCardAuthorizationCode(authcode);
            paymentStatus.setTransactionTime(Calendar.getInstance());
            paymentStatus.setUsedBalance(false);
            log.info(AuthorizeDotNetServiceError.CC_SUCCESS, deviceId,
                    ccType.getDisplayName(), creditCard.getLastFourDigits(),
                    paymentStatus.getAmountChargedToCreditCard(), planId,
                    userId, paymentStatus.getCreditCardAuthorizationCode());
            return paymentStatus;
        } catch (BillingServiceException bse) {
            throw bse;
        } catch (Exception ex) {
            BillingServiceException bse = new BillingServiceException(
                    TelespreeLogger.getEventMessage(
                            AuthorizeDotNetServiceError.CC_EXCEPTION, deviceId,
                            ex.getMessage()));
            bse.setError(AuthorizeDotNetServiceError.CC_EXCEPTION);
            log.error(AuthorizeDotNetServiceError.CC_EXCEPTION, deviceId,
                    ex.getMessage());
            throw bse;
        }
    }

    /**
     * @param telespreeSession
     * @return
     */
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
     * @param amount
     * @return
     */
    protected String formatDoubleAmountToString(double amount) {
        DecimalFormat twoDForm = new DecimalFormat("#.#############");
        return twoDForm.format(amount);
    }

}
