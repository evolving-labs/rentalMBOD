/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental;

/**
 * MbodConstants
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2014/02/17 15:31:17 $
 * @version $Revision: #41 $
 * 
 */
public interface MbodConstants {

    public static final String APP_NAME = "rentalMBOD";

    public static final String PROPERTIES_FILE = APP_NAME + ".properties";

    public static final String CONTEXT_ROOT = APP_NAME;

    public static final String PROP_MVNO_IMAGE_LOGO = "mvno.image.logo";

    public static final String PROP_MVNO_IMAGE_LOGO_DEFAULT = "logo.jpg";

    public static final String PROP_MVNO_IMAGE_BACKGROUND = "mvno.image.background";

    public static final String PROP_MVNO_IMAGE_BACKGROUND_DEFAULT = "background.jpg";

    public static final String PROP_MVNO_NAME = "mvno.name";

    public static final String PROP_MVNO_NAME_DEFAULT = "Telespree";

    public static final String PROP_MVNO_EXTERNAL_ID = "mvno.external.id";

    public static final String PROP_MVNO_EXTERNAL_ID_DEFAULT = "sonic";

    public static final String PROP_MVNO_INTERNAL_ID = "mvno.internal.id";

    public static final String PROP_MVNO_INTERNAL_ID_DEFAULT = "sonic";

    public static final String PROP_MVNO_TITLE = "mvno.title";

    public static final String PROP_MVNO_TITLE_DEFAULT = "Broadband Rental";

    public static final String PROP_MVNO_CUSTOMERCARE = "mvno.customercare";

    public static final String PROP_MVNO_CUSTOMERCARE_DEFAULT = "800-555-5555";

    public static final String PROP_MVNO_REDIRECT_URL = "mvno.redirect.url";

    public static final String PROP_MVNO_REDIRECT_URL_DEFAULT = "http://www.google.com";

    public static final String PROP_MVNO_DAB_URL = "mvno.dab.url";

    public static final String PROP_MVNO_DAB_URL_DEFAULT = "http://sss-mbod-proxy01-app:8080/dab/grant.servlet";

    public static final String PROP_MVNO_TC_VERSION = "mvno.tc.version";

    public static final int PROP_MVNO_TC_VERSION_DEFAULT = 1;

    public static final String PROP_MVNO_SUPPORT_SUBSCRIBER = "mvno.support.subscriber";

    public static final boolean PROP_MVNO_SUPPORT_SUBSCRIBER_DEFAULT = true;

    public static final String PROP_MVNO_SUPPORT_DM = "mvno.support.dm";

    public static final boolean PROP_MVNO_SUPPORT_DM_DEFAULT = true;

    public static final String PROP_MVNO_DM_WIN_URL = "mvno.dm.win.url";

    public static final String PROP_MVNO_DM_WIN_URL_DEFAULT = "#";

    public static final String PROP_MVNO_DM_MAC_URL = "mvno.dm.mac.url";

    public static final String PROP_MVNO_DM_MAC_URL_DEFAULT = "#";

    public static final String PROP_MVNO_SUPPORT_PROMO = "mvno.support.promo";

    public static final boolean PROP_MVNO_SUPPORT_PROMO_DEFAULT = true;

    public static final String PROP_MVNO_SUPPORT_GROUP_PROMO = "mvno.support.promo.group";

    public static final boolean PROP_MVNO_SUPPORT_GROUP_PROMO_DEFAULT = false;

    public static final String PROP_MVNO_GROUP_PROMO_CODE = "mvno.promo.group.code";

    public static final String PROP_MVNO_GROUP_PROMO_CODE_DEFAULT = "T3l3spree";

    public static final String PROP_MVNO_SUPPORT_BILLTO = "mvno.support.billto";

    public static final boolean PROP_MVNO_SUPPORT_BILLTO_DEFAULT = true;

    public static final String PROP_MVNO_BILLTO_TITLE = "mvno.billto.title";

    public static final String PROP_MVNO_BILLTO_TITLE_DEFAULT = "Account";

    public static final String PROP_MVNO_SUPPORT_PAYBYBALANCE = "mvno.support.paybybalance";

    public static final boolean PROP_MVNO_SUPPORT_PAYBYBALANCE_DEFAULT = true;

    public static final String PROP_MVNO_SUPPORT_LOYALTY = "mvno.support.loyalty";

    public static final boolean PROP_MVNO_SUPPORT_LOYALTY_DEFAULT = true;

    public static final String PROP_QUALUTION_BILLING_URL = "qualution.billing.url";

    public static final String PROP_QUALUTION_BILLING_URL_DEFAULT = "http://gandalf1:8088/mockBillingBinding";

    public static final String PROP_QUALUTION_BILLING_TIMEOUT = "qualution.billing.timeout";

    public static final long PROP_QUALUTION_BILLING_TIMEOUT_DEFAULT = 20000;

    public static final String PROP_QUALUTION_BILLING_ANONYMOUS_USERID = "qualution.billing.anonymous.userid";

    public static final String PROP_QUALUTION_BILLING_ANONYMOUS_USERID_DEFAULT = "anonymous";

    public static final String PROP_REHOTLINE_URL = "rehotline.url";

    public static final String PROP_REHOTLINE_URL_DEFAULT = "http://www.telespree.com/soniccarrental";

    public static final String PROP_BOOKMARK_URL = "bookmark.url";

    public static final String PROP_BOOKMARK_URL_DEFAULT = "http://sonic.selfservicesetup.com";

    public static final String PROP_CC_SERVICE = "mvno.cc.service";

    public static final String PROP_CC_SERVICE_DEFAULT = "Q";

    public static final String PROP_MVNO_SUPPORT_CC_STORE = "mvno.support.cc.store";

    public static final boolean PROP_MVNO_SUPPORT_CC_STORE_DEFAULT = true;

    public static final String PROP_CRYPTO_KEY = "crypto.key";

    public static final String PROP_CRYPTO_KEY_DEFAULT = "T3lESPreeSeCreTT";

    public static final String PROP_CRYPTO_KEY_VERSION = "crypto.key.version";

    public static final String PROP_CRYPTO_KEY_VERSION_DEFAULT = "V1";

    public static final String PROP_CRYPTO_ALGORITHM = "crypto.key.algorithm";

    public static final String PROP_CRYPTO_ALGORITHM_DEFAULT = "AES";

    public static final String PROP_MVNO_SUPPORT_CC_EXTRA = "mvno.support.cc.extra";

    public static final boolean PROP_MVNO_SUPPORT_CC_EXTRA_DEFAULT = true;

    public static final String PROP_MVNO_DEFAULT_CC_ZIP = "mvno.default.cc.zip";

    public static final String PROP_MVNO_DEFAULT_CC_ZIP_DEFAULT = "94107";

    public static final String PROP_MVNO_SUPPORT_PIN_REDEMPTION = "mvno.support.pin.redemption";

    public static final boolean PROP_MVNO_SUPPORT_PIN_REDEMPTION_DEFAULT = true;

    public static final String PROP_MVNO_CC_ANET_ENV_IS_TEST = "mvno.cc.anet.env.is.test";

    public static final boolean PROP_MVNO_CC_ANET_ENV_IS_TEST_DEFAULT = true;

    public static final String PROP_ANET_LIVE_LOGIN = "anet.live.login";

    public static final String PROP_ANET_LIVE_LOGIN_DEFAULT = "3fnHh8Zr3k84";

    public static final String PROP_ANET_LIVE_KEY = "anet.live.key";

    public static final String PROP_ANET_LIVE_KEY_DEFAULT = "9Q99nA27J48dmkrq";

    public static final String PROP_ANET_TEST_LOGIN = "anet.test.login";

    public static final String PROP_ANET_TEST_LOGIN_DEFAULT = "7wBE66ub9k4M";

    public static final String PROP_ANET_TEST_KEY = "anet.test.key";

    public static final String PROP_ANET_TEST_KEY_DEFAULT = "28YL37PP3upc6teT";

    public static final String PROP_MOBILE_REGEX = "mobile.regex";

    public static final String PROP_MOBILE_REGEX_DEFAULT = "^$";

    public static final String PROP_MOBILE_REDIRECT = "mobile.redirect";

    public static final String PROP_MOBILE_REDIRECT_ERROR = "mobile.redirect.error";

    public static final String PROP_DESKTOP_REDIRECT = "desktop.redirect";

    public static final String PROP_DESKTOP_REDIRECT_ERROR = "desktop.redirect.error";

    public static final String PROP_MOBILE_REDIRECT_DEFAULT = "/appstore/sonic/web/mobilesp/";

    public static final String PROP_MOBILE_REDIRECT_ERROR_DEFAULT = "/appstore/sonic/web/mobilesp/?target=error";

    public static final String PROP_DESKTOP_REDIRECT_DEFAULT = "/appstore/sonic/web/desktopsp/";

    public static final String PROP_DESKTOP_REDIRECT_ERROR_DEFAULT = "/appstore/sonic/web/desktopsp/?target=error";

    public static final String PROP_SUPPORT_PRIVACY_POLICY = "mvno.support.privacy.policy";

    public static final boolean PROP_SUPPORT_PRIVACY_POLICY_DEFAULT = false;

    public static final String PROP_PRIVACY_POLICY_URL = "mvno.privacy.policy.url";

    public static final String PROP_PRIVACY_POLICY_URL_DEFAULT = "http://www.goldengatemobile.com/Privacy_Policy.html";

    public static final Long TERABYTES = new Long("1099511627776");

    public static final int BITMASK_SUB = 0x0001;

    public static final String PROP_MVNO_SUPPORT_WELCOME = "mvno.support.welcome";

    public static final boolean PROP_MVNO_SUPPORT_WELCOME_DEFAULT = false;

    public static final String PROP_MVNO_TC_ENFORCE = "mvno.tc.enforce";

    public static final boolean PROP_MVNO_TC_ENFORCE_DEFAULT = false;

}
