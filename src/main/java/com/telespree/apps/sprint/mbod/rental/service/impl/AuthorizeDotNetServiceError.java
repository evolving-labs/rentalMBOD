package com.telespree.apps.sprint.mbod.rental.service.impl;

import ch.qos.cal10n.BaseName;
import ch.qos.cal10n.Locale;
import ch.qos.cal10n.LocaleData;

@BaseName("AuthorizeDotNetServiceError")
@LocaleData({ @Locale("en_US") })
public enum AuthorizeDotNetServiceError {

    CC_AMOUNT_INVALID, CC_NO, CC_TYPE_INVALID, CC_NOBILLING, CC_NOCODE, CC_NOTAXZIPCODE, CC_ENDPOINT_FAILURE, CC_RESPONSE_INVALID, CC_RESPONSE, CC_FAILURE, CC_SUCCESS, CC_EXCEPTION;
}
