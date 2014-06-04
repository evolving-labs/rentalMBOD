/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.model;

import java.util.List;
import java.util.Map;

import telespree.apps.fwk.common.TelespreeBean;

import com.telespree.abmf.service.DeviceState;
import com.telespree.barracuda.product.bean.Product;

/**
 * MbodSession
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/11/01 00:37:30 $
 * @version $Revision: #22 $
 * 
 */
public class MbodSession extends TelespreeBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public static final String BEAN_NAME = "MBOD_BEAN";

    private Product selectedProduct;

    private List<Product> productList;

    private Map<String, List<Product>> translatedProductListMap;

    private List<Product> promotionalProductList;

    private boolean authenticated = false;

    private Map<String, String> securityQuestionMap;

    private Subscriber subscriber;

    private String errorMessageKey;

    private List<String> promoList;

    private String taxZipcode;

    private PaymentStatus paymentStatus;

    private RedeemStatus redeemStatus;

    private DeviceState deviceState;

    private Integer tcAgreedVersion;

    private String language;

    private String billToReference;

    private boolean rentalExpired = false;

    private boolean tcNew = false;

    private Map<String, Double> selectedProductTaxMap;

    private Long rentalId;

    private boolean newSignup = false;

    /**
     * @return the securityQuestionMap
     */
    public Map<String, String> getSecurityQuestionMap() {
        return securityQuestionMap;
    }

    /**
     * @param securityQuestionMap
     *            the securityQuestionMap to set
     */
    public void setSecurityQuestionMap(Map<String, String> securityQuestionMap) {
        this.securityQuestionMap = securityQuestionMap;
    }

    /**
     * @return the authenticated
     */
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * @param authenticated
     *            the authenticated to set
     */
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    /**
     * @return the selectedProduct
     */
    public Product getSelectedProduct() {
        return selectedProduct;
    }

    /**
     * @param selectedProduct
     *            the selectedProduct to set
     */
    public void setSelectedProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    /**
     * @return the productList
     */
    public List<Product> getProductList() {
        return productList;
    }

    /**
     * @param productList
     *            the productList to set
     */
    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    /**
     * @return the subscriber
     */
    public Subscriber getSubscriber() {
        return subscriber;
    }

    /**
     * @param subscriber
     *            the subscriber to set
     */
    public void setSubscriber(Subscriber subscriber) {
        this.subscriber = subscriber;
    }

    /**
     * @return the errorMessageKey
     */
    public String getErrorMessageKey() {
        return errorMessageKey;
    }

    /**
     * @param errorMessageKey
     *            the errorMessageKey to set
     */
    public void setErrorMessageKey(String errorMessageKey) {
        this.errorMessageKey = errorMessageKey;
    }

    /**
     * @return the promoList
     */
    public List<String> getPromoList() {
        return promoList;
    }

    /**
     * @param promoList
     *            the promoList to set
     */
    public void setPromoList(List<String> promoList) {
        this.promoList = promoList;
    }

    /**
     * @return the taxZipcode
     */
    public String getTaxZipcode() {
        return taxZipcode;
    }

    /**
     * @param taxZipcode
     *            the taxZipcode to set
     */
    public void setTaxZipcode(String taxZipcode) {
        this.taxZipcode = taxZipcode;
    }

    /**
     * @return the paymentStatus
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus
     *            the paymentStatus to set
     */
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return the redeemStatus
     */
    public RedeemStatus getRedeemStatus() {
        return redeemStatus;
    }

    /**
     * @param redeemStatus
     *            the redeemStatus to set
     */
    public void setRedeemStatus(RedeemStatus redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    /**
     * @return the deviceState
     */
    public DeviceState getDeviceState() {
        return deviceState;
    }

    /**
     * @param deviceState
     *            the deviceState to set
     */
    public void setDeviceState(DeviceState deviceState) {
        this.deviceState = deviceState;
    }

    /**
     * @return the tcAgreedVersion
     */
    public Integer getTcAgreedVersion() {
        return tcAgreedVersion;
    }

    /**
     * @param tcAgreedVersion
     *            the tcAgreedVersion to set
     */
    public void setTcAgreedVersion(Integer tcAgreedVersion) {
        this.tcAgreedVersion = tcAgreedVersion;
    }

    /**
     * @return the promotionalProductList
     */
    public List<Product> getPromotionalProductList() {
        return promotionalProductList;
    }

    /**
     * @param promotionalProductList
     *            the promotionalProductList to set
     */
    public void setPromotionalProductList(List<Product> promotionalProductList) {
        this.promotionalProductList = promotionalProductList;
    }

    /**
     * 
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @return the billToReference
     */
    public String getBillToReference() {
        return billToReference;
    }

    /**
     * @param billToReference
     *            the billToReference to set
     */
    public void setBillToReference(String billToReference) {
        this.billToReference = billToReference;
    }

    /**
     * @return the rentalExpired
     */
    public boolean isRentalExpired() {
        return rentalExpired;
    }

    /**
     * @param rentalExpired
     *            the rentalExpired to set
     */
    public void setRentalExpired(boolean rentalExpired) {
        this.rentalExpired = rentalExpired;
    }

    /**
     * @return the tcNew
     */
    public boolean isTcNew() {
        return tcNew;
    }

    /**
     * @param tcNew
     *            the tcNew to set
     */
    public void setTcNew(boolean tcNew) {
        this.tcNew = tcNew;
    }

    /**
     * @return the selectedProductTaxMap
     */
    public Map<String, Double> getSelectedProductTaxMap() {
        return selectedProductTaxMap;
    }

    /**
     * @param selectedProductTaxMap
     *            the selectedProductTaxMap to set
     */
    public void setSelectedProductTaxMap(
            Map<String, Double> selectedProductTaxMap) {
        this.selectedProductTaxMap = selectedProductTaxMap;
    }

    /**
     * @return the rentalId
     */
    public Long getRentalId() {
        return rentalId;
    }

    /**
     * @param rentalId
     *            the rentalId to set
     */
    public void setRentalId(Long rentalId) {
        this.rentalId = rentalId;
    }

    /**
     * @return the newSignup
     */
    public boolean isNewSignup() {
        return newSignup;
    }

    /**
     * @param newSignup
     *            the newSignup to set
     */
    public void setNewSignup(boolean newSignup) {
        this.newSignup = newSignup;
    }

    /**
     * @return the translatedProductListMap
     */
    public Map<String, List<Product>> getTranslatedProductListMap() {
        return translatedProductListMap;
    }

    /**
     * @param translatedProductListMap
     *            the translatedProductListMap to set
     */
    public void setTranslatedProductListMap(
            Map<String, List<Product>> translatedProductListMap) {
        this.translatedProductListMap = translatedProductListMap;
    }

}
