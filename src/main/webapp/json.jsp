<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<fmt:setLocale value="en_US" />
{
    "deviceState": "${deviceState}",
    "contactPhone": "${mvnoCustomerCareNumber}",
    "contactEmail": "<s:text name="contact.email" />",
    "mvnoName": "${mvnoName}",
    "mvnoInternalId": "${mvnoInternalId}",
    "plans": ${productListJson},
    "authenticated": <c:choose><c:when test="${authenticated}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "newSignup": <c:choose><c:when test="${mbodSession.newSignup}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "showTC": <c:choose><c:when test="${showTC}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "newTC": <c:choose><c:when test="${mbodSession.tcNew}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "securityQuestionId": "${mbodSession.subscriber.securityQuestionId}",
    "securityQuestionText": "${mbodSession.subscriber.securityQuestionText}",
    "securityQuestionMap": ${securityQuestionMapJson},
    "supportSubscriber": <c:choose><c:when test="${supportSubscriber}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "showWelcome": <c:choose><c:when test="${supportWelcome}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportLoyalty": <c:choose><c:when test="${supportLoyalty}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportDataMeter": <c:choose><c:when test="${supportDataMeter}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "allowPayByBalance": <c:choose><c:when test="${allowPayByBalance}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "usedBalance": <c:choose><c:when test="${mbodSession.paymentStatus.usedBalance}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "allowBillto": <c:choose><c:when test="${allowBillto}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "billtoTitle": "${billtoTitle}",
    "billtoReference": "${billtoReference}",
    "free": <c:choose><c:when test="${free}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "moptype": "${mbodSession.subscriber.moptype.displayName}",
    "mopcode": "${mbodSession.subscriber.mopcode}",
    "due": "<fmt:formatNumber type="currency" value="${due}"/>",
    "dueRaw": "${due}",
    "purchasePriceWithTax": "<fmt:formatNumber type="currency" value="${purchasePriceWithTax}"/>",
    "purchasePriceWithTaxRaw": "${purchasePriceWithTax}",
    "stillOwe": <c:choose><c:when test="${mbodSession.paymentStatus.amountStillOwe>0}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "promoCode": "${promoCode}",
    "purchaseCCLast4": "${mbodSession.paymentStatus.ccLast4Digits}",
    "purchaseCCAmount": "<fmt:formatNumber type="currency" value="${mbodSession.paymentStatus.amountChargedToCreditCard}"/>",
    "purchaseCCAmountRaw": "${mbodSession.paymentStatus.amountChargedToCreditCard}",
    "purchaseCCAuth": "${mbodSession.paymentStatus.creditCardAuthorizationCode}",
    "purchaseBalancePaid": "<fmt:formatNumber type="currency" value="${mbodSession.paymentStatus.amountChargedToBalance}"/>",
    "purchaseBalancePaidRaw": "${mbodSession.paymentStatus.amountChargedToBalance}",
    "purchaseBilled": "<fmt:formatNumber type="currency" value="${mbodSession.paymentStatus.amountBilled}"/>",
    "purchaseBilledRaw": "${mbodSession.paymentStatus.amountBilled}",
    "redirectUrl": "${mvnoRedirectUrl}",
    "rehotlineUrl": "${rehotlineUrl}",
    "error": "${errorMessageKey}",
    "subscriberBalance": "<fmt:formatNumber type="currency" minFractionDigits="2" value="${mbodSession.subscriber.accountBalance}"/>",
    "subscriberBalanceRaw": "${mbodSession.subscriber.accountBalance}",
    "subscriberLoyaltyReference": "${mbodSession.subscriber.loyaltyReference}",
    "subscriberDisplayName": "${mbodSession.subscriber.firstNameHtml}",
    "subscriberUserName": "${mbodSession.subscriber.userIdHtml}",
    "subscriberEmailAddress": "${mbodSession.subscriber.email}",
    "redeemedAmount": "<fmt:formatNumber type="currency" value="${mbodSession.redeemStatus.amountRedeemed}"/>",
    "redeemedAmountRaw": "${mbodSession.redeemStatus.amountRedeemed}",
    "cardOnFileAllowed": <c:choose><c:when test="${cardOnFileAllowed}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportAdditionalCCData": <c:choose><c:when test="${supportAdditionalCCData}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportPINRedemption": <c:choose><c:when test="${supportPINRedemption}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportPromo": <c:choose><c:when test="${supportPromo}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportGroupPromo": <c:choose><c:when test="${supportGroupPromo}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "timeoutDuration":${requestScope.timeoutDuration},
    "bufferedTimeoutDuration":${requestScope.bufferedTimeoutDuration},
    "backgroundFileName": "${backgroundFileName}",
    "logoFileName": "${logoFileName}",
    "bookmarkUrl": "${bookmarkUrl}",
    "addon": <c:choose><c:when test="${addon}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "supportPrivacyPolicy": <c:choose><c:when test="${supportPrivacyPolicy}">true</c:when><c:otherwise>false</c:otherwise></c:choose>,
    "privacyPolicyUrl": "${privacyPolicyUrl}",
    "mvnoDmWinUrl": "${mvnoDmWinUrl}",
    "mvnoDmMacUrl": "${mvnoDmMacUrl}"
}