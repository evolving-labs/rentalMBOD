<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="head.jspf"%>
    </head>
    <body>
        <form action="${cssUrl}" id="cssForm"></form>
        <s:url id="guestUrl" action="guest" includeParams="none">
            <s:param name="sessionid" value="%{id}" />
        </s:url>
        <form action="${guestUrl}" id="guestForm"></form>
        <s:url id="freeUrl" action="free" includeParams="none">
            <s:param name="sessionid" value="%{id}" />
        </s:url>
        <form action="${freeUrl}" id="freeForm"></form>
        <s:url id="commitPromoUrl" action="commitPromo" includeParams="none">
            <s:param name="sessionid" value="%{id}" />
        </s:url>
        <form action="${commitPromoUrl}" id="commitPromoForm"></form>
        <s:url id="finishUrl" action="finish" includeParams="none">
            <s:param name="sessionid" value="%{id}" />
        </s:url>
        <form action="${finishUrl}" id="finishForm"></form>
        <form action="${mvnoDmWinUrl}" id="dmWinForm"></form>
        <form action="${mvnoDmMacUrl}" id="dmMacForm"></form>
        <s:url id="languageUrl" action="language" includeParams="none">
            <s:param name="sessionid" value="%{id}" />
        </s:url>
        <form action="${languageUrl}" id="languageForm"></form>
        <div id="bg">
            <img src='<c:url value="/images/${backgroundFileName}" />' alt="">
        </div>
        <div id="overlay">
            <div id="progress">
                <p>
                    <img src="get.image?name=${loaderFileName}&amp;scope=${scopeString}" />
                </p>
            </div>
        </div>
		<div id="dialog" class="${languageSelected}">
            <%@include file="header.jspf"%>
            <div id="language-bar">
            	<a class="page-bookmark" href="${bookmarkUrl}" id="bookmark" >Bookmark</span>&nbsp;|&nbsp;
				<c:if test="${languageSupported}">
					<c:forEach var="map" items="${languageMapList}" varStatus="status">
					    <c:choose>
						    <c:when test="${map.code=='en'}">
						        <c:if test="${!(status.last && status.first)}">
		                            <a href="javascript:changeLanguage('${map.code}')">${map.text}</a>    
		                        </c:if>
						    </c:when>
						    <c:otherwise>
	                            <a href="javascript:changeLanguage('${map.code}')">${map.text}</a>
	                        </c:otherwise>
                        </c:choose>
	                    <c:if test="${status.last}">
	                       <c:if test="${!(status.first && map.code=='en')}">
                                &nbsp;|&nbsp;
                           </c:if>   
                        </c:if> 
					</c:forEach>
				</c:if>
                <a href="javascript:showDialogContact()"><s:text name="contact.link.text" /></a>
            </div>
            <div class="spacer"></div>
            <div id="plan">
                <p>
                    <span class="title-${languageSelected}">
						<c:choose>
	                        <c:when test="${addon}">
	                            <s:text name="plan.title.addon" />
	                        </c:when>
	                        <c:otherwise>
	                            <s:text name="plan.title" />
	                        </c:otherwise>
	                    </c:choose>
					</span><br />
                    <span class="text">
                        <c:choose>
	                        <c:when test="${addon}">
	                            <s:text name="plan.text.addon" />
	                        </c:when>
	                        <c:otherwise>
	                            <s:text name="plan.text">
		                            <s:param>${mvnoName}</s:param>
		                        </s:text>
	                        </c:otherwise>
	                    </c:choose>
                    </span>
                </p>
                <div class="error" id="plan-error"><p>${errorMessage}</p></div>
                <table id="plan-table" class="plan-table">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="plan.table.header.action" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.name" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.description" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.data" />
                            </th>
                            <c:if test="${supportLoyalty && !addon}">
                            <th>
                                <s:text name="plan.table.header.points" />
                            </th>
                            </c:if>
                            <th>
                                <s:text name="plan.table.header.price" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${translatedProductList}" var="product"
                            varStatus="status">
                            <c:choose>
                            <c:when test="${status.index%2!=0}">
                                <tr class="even">
                            </c:when>
                            <c:otherwise>
                                <tr>
                            </c:otherwise>
                            </c:choose>
                                <td class="center">
                                    <s:url id="selectPlanUrl" action="selectPlan" includeParams="none">
                                        <s:param name="sessionid" value="%{id}" />
                                        <s:param name="planId" value="${product.id}" />
                                    </s:url>
                                    <form action="${selectPlanUrl}" id="selectPlanForm-${product.id}"></form>
                                    <a href="javascript:selectPlan(${product.id})" class="button">
                                        <s:text name="button.buy" />
                                    </a>
                                </td>
                                <td>
                                    ${product.name}
                                </td>
                                <td>
                                    ${product.description}
                                </td>
                                <td class="data-col">
                                    ${product.includedUnits}
                                </td>
                                <c:if test="${supportLoyalty && !addon}">
                                <td>
                                    ${product.loyaltyPoints}
                                </td>
                                </c:if>
                                <td class="center">
                                    <span class="bold">
                                    <c:choose>
                                        <c:when test="${product.price>0}">
                                            &#36;${product.price}
                                        </c:when>
                                        <c:otherwise>
                                            <s:text name="plan.free" />
                                        </c:otherwise>
                                    </c:choose>
                                    </span>
                                </td>
                                <c:if test="${product.moreinfo != null}">
                                <div class="plan-tip">
                                    <span class="title">${product.name} Plan Details</span><br />
                                    <span class="text">${product.moreinfo}</span>
                                </div>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${addon}">
                	<table class="button-table">
                        <tr>
                            <td><a href="javascript:showNoAddOn()" class="button"><s:text name="button.nothanks" /></a></td>
                        </tr>
                    </table>
                </c:if>
                <c:if test="${supportPromo && !addon}">
                    <p>
                        <s:url id="redeemPromoUrl" action="redeemPromo" includeParams="none">
                            <s:param name="sessionid" value="%{id}" />
                        </s:url>
                        <form action="${redeemPromoUrl}" method="post" id="redeemPromoForm">
                            <span class="bold"><s:text name="plan.promotion" /></span>&nbsp;
                            <input type="text" id="promo" name="promo" value="" maxlength="10" size="10" />&nbsp;
                            <button type="submit"><s:text name="button.submit" /></button>
                        </form>
                    </p>
                </c:if>
				<c:if test="${supportPrivacyPolicy}">
					<div id="privacy-div">
						<a>&#169;2013 Telespree Communications | </a>
						<a class="privacy-policy-link" id="privacy-link" href="#" onclick="openPopupWindow('${privacyPolicyUrl}', 'asdf', 730, 650)" >Privacy</a>
					</div>
				</c:if>
            </div>
            <div id="tc">
                <p>
                    <span class="title-${languageSelected}"><s:text name="tc.title" /></span><br />
                    <span class="text">
                        <s:text name="tc.text">
                            <s:param>${mvnoName}</s:param>
                        </s:text>
                    </span>
                </p>
                <div class="error" id="tc-error"><p></p></div>
                <s:url id="tcContentUrl" action="tcContent" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${tcContentUrl}" id="tcContentForm"></form>
                <s:url id="tcUrl" action="tc" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <div id="tc-content"></div>
                <div id="tc-buttons">
                    <form action="${tcUrl}" id="tcForm">
                    <p class="fineprint"><input type="checkbox" name="tc-checkbox" id="tc-checkbox" value="" /><span class="superscript"><s:text name="tc.fineprint" /></span></p>
                    </form>
                    <a href="javascript:tcContinue()" class="button"><s:text name="button.agree" /></a>&nbsp;
                    <a href="javascript:tcCancel()" class="button"><s:text name="button.cancel" /></a>
                </div>
            </div>
            <div id="cc">
                <p>
                    <span class="title-${languageSelected}"><s:text name="cc.title" /></span><br />
                    <span id="cc-stillowe"><s:text name="cc.stillowe" /><br /></span>
                    <!-- <span class="bold"><s:text name="cc.price" />&nbsp;&nbsp;<span id="cc-price"><fmt:formatNumber type="currency" value="0" /></span></span><br /> -->
                    <span class="bold"><s:text name="cc.due" /></span>&nbsp;<span id="cc-amount"><fmt:formatNumber type="currency" value="0" /></span>&nbsp;<s:text name="cc.due.appendix" />
                </p>
                <div class="error" id="cc-error"><p></p></div>
                <s:url id="ccUrl" action="cc" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${ccUrl}" method="post" id="ccForm">
                    <div id="cc-mop">
                        <span class="bold"><s:text name="cc.mop" /></span>
                        <table>
                            <tr id="mop-yes-tr">
                                <td><input type="radio" name="mop" value="" id="mop-yes" /></td>
                                <td><span class="bold" id="mop-type"></span>&nbsp;<s:text name="cc.mop.text" />&nbsp;<span id="mop-code" class="bold"></span></td>
                            </tr>
                            <tr>
                                <td><input type="radio" name="mop" value="" id="mop-no" /></td>
                                <td><s:text name="cc.new" /></td>
                            </tr>
                            <tr id="mop-balance-tr">
                                <td><input type="radio" name="mop" value="balance" id="mop-balance" /></td>
                                <td><s:text name="cc.balance" />&nbsp;<span id="mop-balance-value"></span></td>
                            </tr>
                            <c:if test="${allowBillto}">
                            <tr>
                                <td><input type="radio" name="mop" value="bill" id="mop-bill" /></td>
                                <td>
                                    <s:text name="cc.bill">
			                            <s:param>${billtoTitle}</s:param>
			                            <s:param>${billtoReference}</s:param>
			                        </s:text>
                                </td>
                            </tr>
                            </c:if>
                        </table>
                    </div>
                    <div id="cc-loyalty">
                        <label for="purchaseLoyaltyReference">
                            <s:text name="cc.loyalty">
                                <s:param>${mvnoName}</s:param>
                            </s:text>
                        </label>&nbsp;<input type="text" name="purchaseLoyaltyReference" id="purchaseLoyaltyReference" value="" maxlength="16" />
                    </div>
                    <fieldset>
                        <legend><img src="<c:url value="/images/CreditCardLogos.png" />" alt='<s:text name="cc.legend" />' /></legend>
                        <table id="add-cc-table">
                        	<tr>
                                <td><label for="ccFullName"><span class="bold"><s:text name="cc.full.name" /></span></label></td>
                                <td><input type="text" name="ccFullName" id="ccFullName" value="" maxlength="50"/></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="ccAddressLine1"><span class="bold"><s:text name="cc.address.one" /></span></label></td>
                                <td><input type="text" name="ccAddressLine1" id="ccAddressLine1" value="" maxlength="50" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="ccAddressLine2"><span class="bold"><s:text name="cc.address.two" /></span></label></td>
                                <td><input type="text" name="ccAddressLine2" id="ccAddressLine2" value="" maxlength="50" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="ccCityTown"><span class="bold"><s:text name="cc.city.town" /></span></label></td>
                                <td><input type="text" name="ccCityTown" id="ccCityTown" value="" maxlength="50" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="ccStateProvinceRegion"><span class="bold"><s:text name="cc.state" /></span></label></td>
                                <td><input type="text" name="ccStateProvinceRegion" id="ccStateProvinceRegion" value="" maxlength="10" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
								<td><label for="ccCountry"><span class="bold"><s:text name="cc.country" /></span></label></td>
								<td>
								<select name="ccCountry" id="ccCountry"><option value="">Country...</option></select>
								</td>
								<td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            </tr>
                        </table>
                        <table id="cc-table">
                            <tr>
                                <td><label for="creditCardNumber"><span class="bold"><s:text name="cc.number" /></span></label></td>
                                <td><input type="text" name="creditCardNumber" id="creditCardNumber" value="" class="numeric" maxlength="16" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                            </tr>
                            <tr>    
                                <td><label for="creditCardExpiration"><span class="bold"><s:text name="cc.expiration" /></span></label></td>
                                <td>
                                    <input type="text" name="creditCardExpirationMonth" id="creditCardExpirationMonth" value="" class="numeric" maxlength="2" size="2" />&nbsp;/&nbsp;
                                    <input type="text" name="creditCardExpirationYear" id="creditCardExpirationYear" value="" class="numeric" maxlength="2" size="2" />
                                </td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="creditCardZip"><span class="bold"><s:text name="cc.zipcode" /></span></label></td>
								<td><input type="text" name="creditCardZip" id="creditCardZip" value="" maxlength="9" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr>
                                <td><label for="creditCardSecurityCode"><span class="bold"><s:text name="cc.code" /></span></label></td>
                                <td><input type="text" name="creditCardSecurityCode" id="creditCardSecurityCode" value="" class="numeric" maxlength="4" /></td>
                                <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                                <td></td>
                            </tr>
                            <tr id="cc-table-tostore">
                                <td colspan="4"><input type="checkbox" name="toStore" value="toStore" />&nbsp;<span class="bold"><s:text name="cc.tostore" /></span></td>
                            </tr>
                        </table>
                    </fieldset>
                    <table class="button-table">
                        <tr>
                            <td><button type="submit"><s:text name="button.submit" /></button></td>
                            <c:if test="${productList.size()>1}">
                            <td><button type="button" onclick="ccToChangeplan();"><s:text name="button.cancel" /></button></td>
                            </c:if>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="summary">
                <p>
                    <span class="title-${languageSelected}"><s:text name="summary.title" /></span><br />
                    <s:text name="summary.text" />
                    <span id="summary-cc">
                        <s:text name="summary.text.cc.part1" />
                        &nbsp;****<span id="summary-cc-last4" class="bold"></span>
                        &nbsp;<s:text name="summary.text.cc.part2" />
                        &nbsp;<span id="summary-cc-amount" class="bold"></span>
                        &nbsp;<s:text name="summary.text.cc.part3" />
                        &nbsp;<span id="summary-cc-auth" class="bold"></span>
                        <s:text name="summary.text.cc.part4" />
                    </span>
                    <span id="summary-balance">
                        <s:text name="summary.text.balance.part1" />
                        &nbsp;<span id="summary-balance-amount" class="bold"></span>
                        &nbsp;<s:text name="summary.text.balance.part2" />
                    </span>
                    <span id="summary-ccb">
                        <s:text name="summary.text.ccb.part1" />
                        &nbsp;<span id="summary-ccb-balance-amount" class="bold"></span>
                        &nbsp;<s:text name="summary.text.ccb.part2" />
                        &nbsp;<span id="summary-ccb-cc-amount" class="bold"></span>
                        &nbsp;<s:text name="summary.text.ccb.part3" />
                        &nbsp;****<span id="summary-ccb-last4" class="bold"></span>
                        &nbsp;<s:text name="summary.text.ccb.part4" />
                        &nbsp;<span id="summary-ccb-auth" class="bold"></span>
                        <s:text name="summary.text.ccb.part5" />
                    </span>
                    <span id="summary-bill">
                        <s:text name="summary.text.bill">
                            <s:param>${billtoTitle}</s:param>
                            <s:param>${billtoReference}</s:param>
                        </s:text>
                    </span>
                    <span id="summary-bb">
                        <s:text name="summary.text.bb.part1" />
                        &nbsp;<span id="summary-bb-balance-amount" class="bold"></span>
                        &nbsp;<s:text name="summary.text.bb.part2" />
                        &nbsp;<span id="summary-bb-bill-amount" class="bold"></span>
                        &nbsp;
                        <s:text name="summary.text.bb.part3">
                            <s:param>${billtoTitle}</s:param>
                            <s:param>${billtoReference}</s:param>
                        </s:text>
                    </span>
                    <br /><br />
                    <span id="summary-signup">
                        <s:text name="summary.text.signup" /><br />
                        <span class="bold"><s:text name="summary.text.username" /></span>&nbsp;<span id="summary-signup-username"></span><br />
                        <span class="bold"><s:text name="summary.text.email" /></span>&nbsp;<span id="summary-signup-email"></span><br /><br />
                    </span>
                    <span id="summary-loyalty">
                        <s:text name="summary.text.loyalty.part1" />&nbsp;
                        <span id="summary-loyalty-placeholder" class="bold"></span>&nbsp;
                        <s:text name="summary.text.loyalty.part2">
                            <s:param>${mvnoName}</s:param>
                        </s:text><br />
                    </span>
                    <c:choose>
                        <c:when test="${addon}">
                            <span><s:text name="summary.subtitle.addon" /></span>
                        </c:when>
                        <c:otherwise>
                            <span class="bold"><s:text name="summary.subtitle" /></span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <table id="summary-table" class="plan-table">
                    <thead>
                        <tr>
                            <th class="left">
                                <s:text name="plan.table.header.name" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.price" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.description" />
                            </th>
                            <c:if test="${supportLoyalty && !addon}">
                            <th class="left">
                                <s:text name="plan.table.header.points" />
                            </th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <c:if test="${supportLoyalty && !addon}">
                            <td></td>
                            </c:if>
                         </tr>
                    </tbody>
                </table>
                <p><span id="summary-finish"><s:text name="summary.finish" /></span></p>
                <table class="button-table">
                    <tr>
                        <td id="summary-button-finish"><button type="button" onclick="finish();"><s:text name="button.finish" /></button></td>
                        <td id="summary-button-continue"><button type="button" onclick="summaryToDatameter();"><s:text name="button.continue" /></button></td>
                    </tr>
                </table>
            </div>
            <div id="summary-promo">
                <p>
                    <span class="title-${languageSelected}"><s:text name="promo.title" /></span><br />
                    <span class="text"><s:text name="promo.text.part1" /></span>
                    &nbsp;<span id="promo-code" class="bold"></span>&nbsp;
					<span class="text"><s:text name="promo.text.part2" /></span>
                    <br /><br />
                    <span id="promo-signup">
                        <s:text name="promo.text.signup" /><br />
                        <span class="bold"><s:text name="promo.text.username" /></span>&nbsp;<span id="promo-signup-username"></span><br />
                        <span class="bold"><s:text name="promo.text.email" /></span>&nbsp;<span id="promo-signup-email"></span><br /><br />
                    </span>
                    <span id="promo-loyalty">
                        <s:text name="promo.text.loyalty.part1" />&nbsp;
                        <span id="promo-loyalty-placeholder"></span>&nbsp;
                        <s:text name="promo.text.loyalty.part2">
                            <s:param>${mvnoName}</s:param>
                        </s:text><br />
                    </span>
                    <c:choose>
                        <c:when test="${addon}">
                            <span><s:text name="promo.subtitle.addon" /></span>
                        </c:when>
                        <c:otherwise>
                            <span class="bold"><s:text name="promo.subtitle" /></span>
                        </c:otherwise>
                    </c:choose>
                </p>
                <table id="promo-table" class="plan-table">
                    <thead>
                        <tr>
                            <th class="left">
                                <s:text name="plan.table.header.name" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.price" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.description" />
                            </th>
                            <c:if test="${supportLoyalty && !addon}">
                            <th class="left">
                                <s:text name="plan.table.header.points" />
                            </th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <c:if test="${supportLoyalty && !addon}">
                            <td></td>
                            </c:if>
                         </tr>
                    </tbody>
                </table>
                <p><span id="promo-finish"><s:text name="promo.finish" /></span></p>
                <table class="button-table">
                	<tr>
                        <td id="promo-button-finish"><button type="button" onclick="finish();"><s:text name="button.finish" /></button></td>
                        <td id="promo-button-continue"><button type="button" onclick="promoToDatameter();"><s:text name="button.go" /></button></td>
                    </tr>
                </table>
            </div>
            <div id="datameter">
                <p>
                    <span class="title-${languageSelected}"><s:text name="datameter.title" /></span><br />
                    <span class="text"><s:text name="datameter.text" /></span>
                </p>
                <!-- 
                <table>
                    <tr>
                        <td><a href="${mvnoDmWinUrl}"><img src='<c:url value="/images/windows_logo_1.png" />' alt=""></a></td>
                        <td><a href="${mvnoDmMacUrl}"><img src='<c:url value="/images/99px-Mac_OS_icon.png" />' alt=""></a></td>
                    </tr>
                </table>  -->   
                <table class="button-table">
                    <tr>
                        <td><button type="button" onclick="dmWindows();"><s:text name="button.windows" /></button></td>
                        <td><button type="button" onclick="dmMac();"><s:text name="button.mac" /></button></td>
                        <td><button type="button" onclick="finish();"><s:text name="button.nothanks" /></button></td>
                    </tr>
                </table>
            </div>
            <div id="datameter-finish">
                <p>
                    <span class="title-${languageSelected}"><s:text name="datameter.finish.title" /></span>
                </p>
                <table class="button-table">
                    <tr>
                        <td><button type="button" onclick="finish();"><s:text name="button.finish" /></button></td>
                    </tr>
                </table>
            </div>
            <div id="changeplan">
                <p>
                    <span class="title-${languageSelected}"><s:text name="plan.title" /></span><br />
                    <span class="text">
                        <s:text name="plan.text">
                            <s:param>${mvnoName}</s:param>
                        </s:text>
                    </span>
                </p>
                <div class="error" id="changeplan-error"><p></p></div>
                <table id="changeplan-table" class="plan-table">
                    <thead>
                        <tr>
                            <th>
                                <s:text name="plan.table.header.action" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.name" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.description" />
                            </th>
                            <th class="left">
                                <s:text name="plan.table.header.data" />
                            </th>
                            <c:if test="${supportLoyalty && !addon}">
                            <th>
                                <s:text name="plan.table.header.points" />
                            </th>
                            </c:if>
                            <th>
                                <s:text name="plan.table.header.price" />
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${translatedProductList}" var="product"
                            varStatus="status">
                            <c:choose>
                            <c:when test="${status.index%2!=0}">
                                <tr class="even">
                            </c:when>
                            <c:otherwise>
                                <tr>
                            </c:otherwise>
                            </c:choose>
                                <td class="center">
                                    <s:url id="changePlanUrl" action="selectPlan" includeParams="none">
                                        <s:param name="sessionid" value="%{id}" />
                                        <s:param name="planId" value="${product.id}" />
                                    </s:url>
                                    <form action="${changePlanUrl}" id="changePlanForm-${product.id}"></form>
                                    <a href="javascript:changePlan(${product.id})" class="button">
                                        <s:text name="button.buy" />
                                    </a>
                                </td>
                                <td>
                                    ${product.name}
                                </td>
                                <td>
                                    ${product.description}
                                </td>
                                <td>
                                    ${product.includedUnits}
                                </td>
                                <c:if test="${supportLoyalty && !addon}">
                                <td>
                                    ${product.loyaltyPoints}
                                </td>
                                </c:if>
                                <td class="center">
                                    <span class="bold">
                                    <c:choose>
                                        <c:when test="${product.price>0}">
                                            &#36;${product.price}
                                        </c:when>
                                        <c:otherwise>
                                            <s:text name="plan.free" />
                                        </c:otherwise>
                                    </c:choose>
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <c:if test="${supportPromo}">
                    <p>
                        <s:url id="changePromoUrl" action="redeemPromo" includeParams="none">
                            <s:param name="sessionid" value="%{id}" />
                        </s:url>
                        <form action="${changePromoUrl}" method="post" id="changePromoForm">
                            <span class="bold"><s:text name="plan.promotion" /></span>&nbsp;
                            <input type="text" id="changePromo" name="changePromo" value="" maxlength="10" size="10" />&nbsp;
                            <button type="submit"><s:text name="button.submit" /></button>
                        </form>
                    </p>
                </c:if>
            </div>
        </div>
        <div id="dialog-login" class="dialog-temp ${languageSelected}">
            <div id="login">
                <p>
                    <span class="title-${languageSelected}"><s:text name="login.title" /></span><br />
                    <span class="text"><s:text name="login.text" /></span>
                </p>
                <div class="error" id="login-error"><p></p></div>
                <s:url id="loginUrl" action="login" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${loginUrl}" method="post" id="loginForm">
                    <table id="login-table">
                        <tr>
                            <td colspan="2"><label for="userId"><span class="bold"><s:text name="login.userid" /></span></label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="userId" id="userId" value="" tabindex="1" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <label for="password"><span class="bold"><s:text name="login.password" /></span></label>&nbsp;
                                <a href="javascript:forgetPassword()"><span class="fineprint"><s:text name="login.password.forgot" /></span></a>
                            </td>
                        </tr>
                        <tr>
                            <td><input type="password" name="passwordLogin" id="passwordLogin" value="" tabindex="2" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                        </tr>
                    </table>
                    <table class="button-table">
                        <tr>
                            <td><button type="submit"><s:text name="button.login" /></button></td>
                            <td><button type="button" onclick="hideLoginDialog()"><s:text name="button.cancel" /></button></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="forgetpassword">
                <p>
                    <span class="title-${languageSelected}"><s:text name="forgetpassword.title" /></span><br />
                    <span class="text"><s:text name="forgetpassword.text" /></span>
                </p>
                <div class="error" id="forgetpassword-error"><p></p></div>
                <s:url id="forgetpasswordUrl" action="forgetpassword" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${forgetpasswordUrl}" method="post" id="forgetpasswordForm">
                    <table id="forgetpassword-table">
                        <tr>
                            <td colspan="2"><label for="forgetpasswordUserId"><span class="bold"><s:text name="forgetpassword.userid" /></span></label></td>
                        </tr>
                        <tr>
                            <td><input type="text" name="userId" id="forgetpasswordUserId" value="" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                        </tr>
                    </table>
                    <table class="button-table">
                        <tr>
                            <td><button type="submit"><s:text name="button.submit" /></button></td>
                            <td><button type="button" onclick="forgetpasswordToLogin();"><s:text name="button.cancel" /></button></td>
                        </tr>
                    </table>
                </form>
            </div>
            <div id="resetpassword">
                <p>
                    <span class="title-${languageSelected}"><s:text name="resetpassword.title" /></span><br />
                    <span class="text"><s:text name="resetpassword.text" /></span>
                </p>
                <div class="error" id="resetpassword-error"><p></p></div>
                <s:url id="resetpasswordUrl" action="resetpassword" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${resetpasswordUrl}" method="post" id="resetpasswordForm">
                    <input type="hidden" name="securityQuestionId" value="" id="resetpasswordSecurityQuestionId" />
                    <input type="hidden" name="userId" value="" id="resetpasswordUserId" />
                    <table id="resetpassword-table">
                        <tr>
                            <td colspan="3"><span class="bold"><label for="securityAnswer"></label></span></td>
                        </tr>
                         <tr>
                            <td><input type="text" name="securityAnswer" id="securityAnswer" value="" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        </tr>
                        <tr>
                            <td colspan="3"><label for="passwordNew"><span class="bold"><s:text name="resetpassword.passwordnew" /></span></label></td>
                        </tr>
                         <tr>
                            <td><input type="password" name="passwordNew" id="passwordNew" value="" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td colspan="3"><label for="passwordNewConfirm"><span class="bold"><s:text name="resetpassword.passwordnewconfirm" /></span></label></td>
                        </tr>
                         <tr>
                            <td><input type="password" name="passwordNewConfirm" id="passwordNewConfirm" value="" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                    </table>
                    <table class="button-table">
                        <tr>
                            <td><button type="submit"><s:text name="button.submit" /></button></td>
                            <td><button type="button" onclick="resetpasswordToLogin();"><s:text name="button.cancel" /></button></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div id="dialog-signup" class="dialog-temp ${languageSelected}">
            <div id="signup">
                <p>
                    <span class="title-${languageSelected}"><s:text name="signup.title" /></span><br />
                    <span class="text">
						<s:text name="signup.text" >
							<s:param>${mvnoName}</s:param>
						</s:text>
					</span>
                </p>
                <div class="error" id="signup-error"><p></p></div>
                <s:url id="signupUrl" action="signup" includeParams="none">
                    <s:param name="sessionid" value="%{id}" />
                </s:url>
                <form action="${signupUrl}" method="post" id="signupForm">
                    <table id="signup-table">
                        <tr>
                            <td><label for="userId"><span class="bold"><s:text name="signup.userid" /></span></label></td>
                            <td><input type="text" name="userId" id="userId" value="" maxlength="50" tabindex="1" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                        </tr>
                        <tr>    
                            <td><label for="password"><span class="bold"><s:text name="signup.password" /></span></label></td>
                            <td><input type="password" name="password" id="password" value="" maxlength="10" tabindex="2" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>    
                            <td><label for="passwordConfirm"><span class="bold"><s:text name="signup.password.confirm" /></span></label></td>
                            <td><input type="password" name="passwordConfirm" id="passwordConfirm" value="" maxlength="10" tabindex="3" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>    
                            <td><label for="securityQuestionId"><span class="bold"><s:text name="signup.securityquestion" /></span></label></td>
                            <td colspan="3"><s:select name="securityQuestionId" tabindex="4" list="securityQuestionMap" theme="simple" /></td>
                        </tr>
                        <tr>
                            <td><label for="securityAnswer"><span class="bold"><s:text name="signup.securityanswer" /></span></label></td>
                            <td><input type="text" name="securityAnswer" id="securityAnswer" value="" maxlength="100" tabindex="5" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label for="email"><span class="bold"><s:text name="signup.email" /></span></label></td>
                            <td><input type="text" name="email" id="email" value="" tabindex="6" maxlength="50" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label for="firstName"><span class="bold"><s:text name="signup.firstname" /></span></label></td>
                            <td><input type="text" name="firstName" id="firstName" value="" tabindex="7" maxlength="25" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <tr>
                            <td><label for="lastName"><span class="bold"><s:text name="signup.lastname" /></span></label></td>
                            <td><input type="text" name="lastName" id="lastName" value="" tabindex="8" maxlength="25"/></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        <c:if test="${supportLoyalty}">
                        <tr>
                            <td>
                                <label for="loyaltyReference">
                                    <span class="bold">
                                        <s:text name="signup.loyalty" />
                                    </span>
                                </label>
                            </td>
                            <td><input type="text" name="loyaltyReference" id="loyaltyReference" value="" tabindex="9" maxlength="50" /></td>
                            <td><span class="ui-icon ui-icon-alert error-icon"></span></td>
                            <td></td>
                        </tr>
                        </c:if>
                    </table>
                    <br /><br /><br /><br /><br /><br />
                    <div id="loyaltyPopUp">
                        <h3>
                            <s:text name="signup.loyalty">
                                <s:param>${mvnoName}</s:param>
                            </s:text>
                        </h3>
                        <p>
                            <s:text name="signup.loyalty.detail">
                                <s:param>${mvnoName}</s:param>
                            </s:text>
                        </p>
                    </div>
                    <table class="button-table">
                        <tr>
                            <td><button type="submit"><s:text name="button.submit" /></button></td>
                            <td><button type="button" onclick="hideSignupDialog();"><s:text name="button.cancel" /></button></td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div id="planSelectFilter" class="dialog-temp ${languageSelected}">
		    <p>
		        <span class="title-${languageSelected}"><s:text name="filter.title" /></span><br />
                <span class="text"><s:text name="filter.text"></s:text></span>
            </p>
            <table class="button-table">
                <tr>
                    <td><button type="button" onclick="planSelectToLogin()"><s:text name="button.login" /></button></td>
                    <td><button type="button" onclick="planSelectToSignup()"><s:text name="button.signup" /></button></td>
                    <td><button type="button" onclick="planSelectToGuest()"><s:text name="button.guest" /></button></td>
                </tr>
            </table>
        </div>
        <div id="redeemPIN-auth" class="dialog-temp ${languageSelected}">
            <p>
                <span class="title-${languageSelected}"><s:text name="redeem.auth.title" /></span><br />
                <span class="text"><s:text name="redeem.auth.text" /></span>
            </p>
            <table class="button-table">
                <tr>
                    <td><button type="button" onclick="redeemToLogin()"><s:text name="button.login" /></button></td>
                    <td><button type="button" onclick="redeemToSignup()"><s:text name="button.signup" /></button></td>
                </tr>
            </table>
        </div>
        <div id="redeemPIN-status" class="dialog-temp ${languageSelected}">
            <p>
                <span class="title-${languageSelected}"><s:text name="redeem.status.title" /></span><br />
                <span id="redeemPIN-status-success">
                    <s:text name="redeem.status.success.part1" />&nbsp;
                    <span id="redeemPIN-status-amount"></span>
                    &nbsp;<s:text name="redeem.status.success.part2" />
                </span>
                <div class="error" id="redeemPIN-status-error"></div>
            </p>
            <table class="button-table">
                <tr>
                    <td id="redeemPIN-status-button-close"><button type="button" onclick="hideRedeemPINStatus()"><s:text name="button.close" /></button></td>
                    <td id="redeemPIN-status-button-tryagain"><button type="button" onclick="hideRedeemPINStatus()"><s:text name="button.tryagain" /></button></td>
                </tr>
            </table>
        </div>
        <div id="payment-confirm" class="dialog-temp ${languageSelected}">       
            <p>
                <span class="title-${languageSelected}"><s:text name="payment.confirm.title" /></span><br />
                <span id="payment-confirm-cc">
                    <s:text name="payment.confirm.cc.part1" />&nbsp;
                    <span id="payment-confirm-cc-last4" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.cc.part2" />&nbsp;
                    <span id="payment-confirm-cc-amount" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.cc.part3" /><br />
                </span>
                <span id="payment-confirm-bill">
                    <s:text name="payment.confirm.bill.part1">
                        <s:param>${billtoTitle}</s:param>
                        <s:param>${billtoReference}</s:param>
                    </s:text>&nbsp;
                    <span id="payment-confirm-bill-amount" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.bill.part2" /><br />
                </span>
                <span id="payment-confirm-balance-sufficient">
                    <s:text name="payment.confirm.balance.sufficient.part1" />&nbsp;
                    <span id="payment-confirm-balance-sufficient-amount" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.sufficient.part2" /><br />
                </span>
                <span id="payment-confirm-balance-insufficient">
                    <s:text name="payment.confirm.balance.insufficient.part1" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-balance" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.insufficient.part2" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-amount" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.insufficient.part3" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-due" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.insufficient.part4" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-last4" class="bold"></span><br />
                </span>
                <span id="payment-confirm-balance-insufficient-bill">
                    <s:text name="payment.confirm.balance.insufficient.bill.part1" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-bill-balance" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.insufficient.bill.part2" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-bill-amount" class="bold"></span>
                    &nbsp;<s:text name="payment.confirm.balance.insufficient.bill.part3" />&nbsp;
                    <span id="payment-confirm-balance-insufficient-bill-due" class="bold"></span>
                    &nbsp;
                    <s:text name="payment.confirm.balance.insufficient.bill.part4">
                        <s:param>${billtoTitle}</s:param>
                        <s:param>${billtoReference}</s:param>
                    </s:text>
                    <br />
                </span>
                <br /><s:text name="payment.confirm.text" />
            </p>
            <table class="button-table">
                <tr>
                    <td><button type="button" onclick="submitCCForm()"><s:text name="button.continue" /></button></td>
                    <td><button type="button" onclick="cancelPayment()"><s:text name="button.cancel" /></button></td>
                </tr>
            </table>
        </div>
        <div id="dialog-datameter" class="dialog-temp ${languageSelected}">
			<s:url id="logDatemeterDownloadWindowsUrl" action="logDatameterDownload" includeParams="none">
				<s:param name="sessionid" value="%{id}" />
				<s:param name="downloadedOS">Windows</s:param>
			</s:url>
			<s:url id="logDatemeterDownloadMacUrl" action="logDatameterDownload" includeParams="none">
				<s:param name="sessionid" value="%{id}" />
				<s:param name="downloadedOS">Mac</s:param>
			</s:url>
			<form action="${logDatemeterDownloadWindowsUrl}" id="logDatameterDownloadWindowsForm"></form>
			<form action="${logDatemeterDownloadMacUrl}" id="logDatameterDownloadMacForm"></form>
            <div id="dialog-datameter-content">
                <p>
                    <span class="title-${languageSelected}"><s:text name="datameter.title" /></span><br />
                    <span class="text"><s:text name="datameter.text.dialog" /></span>
                </p>
                <!-- 
                <table>
                    <tr>
                        <td><a href="${mvnoDmWinUrl}"><img src='<c:url value="/images/windows_logo_1.png" />' alt=""></a></td>
                        <td><a href="${mvnoDmMacUrl}"><img src='<c:url value="/images/99px-Mac_OS_icon.png" />' alt=""></a></td>
                    </tr>
                </table>  -->   
                <table class="button-table">
                    <tr>
                        <td><button type="button" onclick="dialogDmWindows();"><s:text name="button.windows" /></button></td>
                        <td><button type="button" onclick="dialogDmMac();"><s:text name="button.mac" /></button></td>
                        <td><button type="button" onclick="hideDatameterDialog();"><s:text name="button.nothanks" /></button></td>
                    </tr>
                </table>
            </div>
            <div id="dialog-datameter-finish">
                <p>
                    <span class="title-${languageSelected}"><s:text name="datameter.finish.title.dialog" /></span>
                </p>
                <table class="button-table">
                    <tr>
                        <td><button type="button" onclick="hideDatameterDialog();"><s:text name="button.close" /></button></td>
                    </tr>
                </table>
            </div>
        </div>
        <div id="dialog-addon-declined" class="dialog-temp ${languageSelected}">
            <p><s:text name="addon.declined" /></p>
            <table class="button-table">
                <tr>
                    <td><button type="button" onclick="finish()"><s:text name="button.finish" /></button></td>
                </tr>
            </table>
        </div>
        <div id="footer" class="footer ${languageSelected}">
			<div id="footer-content">
			    <s:url id="usageUrl" action="usage" includeParams="none">
			        <s:param name="sessionid" value="%{id}" />
			    </s:url>
				<form action="${usageUrl}" id="usageForm"></form>
				<div id="top-box-content">
					<span id="footer-no-plan"><s:text name="footer.no.plan.balance" /></span>
					<span id="footer-title" class="bold"><s:text name="footer.balance.title" />&nbsp;</span>
					<span id="footer-plan-name"></span>
					<span id="footer-expiration" class="bold">&nbsp;<s:text name="footer.expiration.text" />&nbsp;</span>
					<span id="footer-expiration-date"></span>
				</div>
				<div id="mid-box-content" class="padded-footer-content">
				    <span id="footer-additional-purchases">
					    <span id="footer-additional-purchases-title" class="bold"><s:text name="footer.additional.purchases.text" /></span><br />
					    <span id="footer-additional-purchases-list"></span>
					</span>
				</div>
				<div id="bottom-box-content" class="padded-footer-content">
				    <span id="footer-bookmark"><s:text name="footer.bookmark.text" /> <a class="page-bookmark" href="${bookmarkUrl}">${bookmarkUrl}</a></span>
				</div>
			</div>
		</div>
		<div id="dialog-ajax-error" class="dialog-temp ${languageSelected}">
            <p><s:text name="error.general" /></p>
            <table class="button-table">
                <tr>
                    <td><button type="button" onclick="hideAjaxErrorDialog();"><s:text name="button.close" /></button></td>
                </tr>
            </table>
        </div>
        <div id="dialog-contact" class="dialog-temp ${languageSelected}">
        	<span class="title"><s:text name="contact.title" /></span>
            <p><s:text name="contact.phone.text" /> ${mvnoCustomerCareNumber}</p>
            <c:if test="${!(empty mvnoCustomerCareEmail)}">
                <p><s:text name="contact.email.text" /> <s:text name="contact.email" /></p>
            </c:if>
	        <table class="button-table">
		        <tr>
		            <td><button type="button" onclick="hideDialogContact();"><s:text name="button.close" /></button></td>
		        </tr>
	        </table>
        </div>
    </body>
</html>
