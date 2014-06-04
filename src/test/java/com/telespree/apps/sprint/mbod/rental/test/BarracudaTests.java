/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.util.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.telespree.abmf.service.IAccountService;
import com.telespree.abmf.service.IDeviceService;
import com.telespree.abmf.service.OverwritePolicy;
import com.telespree.barracuda.product.service.IProductService;
import com.telespree.barracuda.product.service.IPromoCodeService;
import com.telespree.rental.service.IRentalService;
import com.telespree.rental.service.RentalInformationBean;

/**
 * BarracudaTests
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/10/07 15:36:59 $
 * @version $Revision: #6 $
 * 
 */
@ContextConfiguration
@TransactionConfiguration(transactionManager = "transactionManager")
public class BarracudaTests extends AbstractTestNGSpringContextTests {

    @Autowired
    @Qualifier("productService")
    private IProductService productService;

    @Autowired
    @Qualifier("promoCodeService")
    private IPromoCodeService promoCodeService;

    @Autowired
    @Qualifier("accountService")
    private IAccountService accountService;

    @Autowired
    @Qualifier("deviceService")
    private IDeviceService deviceService;

    @Autowired
    @Qualifier("rentalService")
    private IRentalService rentalService;

    @BeforeClass
    public void init() {
        Assert.notNull(productService);
        Assert.notNull(promoCodeService);
        Assert.notNull(accountService);
        Assert.notNull(deviceService);
        Assert.notNull(rentalService);
    }

    @DataProvider(name = "device")
    public Object[][] devices() {
        return new Object[][] { { "5B4CF868" }, { "5B4CF869" } };
    }

    @DataProvider(name = "checkout")
    public Object[][] checkouts() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        return new Object[][] { new Object[] { "5B4CF868", now.getTime(),
                "94107", "sonic", "sonic" } };
    }

    @Test(dataProvider = "device", enabled = false)
    public void testDeviceCreate(String deviceId) throws Exception {
        List<String> deviceList = new ArrayList<String>();
        deviceList.add(deviceId);
        accountService.addDevice("1", deviceList, "4158170800", "4158170801", "000004158170800", OverwritePolicy.ALL, null);
    }

    @Test(dataProvider = "device"/*, dependsOnMethods = { "testDeviceCreate" }*/)
    public void testDeviceState(String deviceId) throws Exception {
        String state = deviceService.getCurrentState(deviceId);
    }

    @Test(dataProvider = "device", dependsOnMethods = { "testDeviceState" }, enabled = false)
    public void testRentalInformationBeforeCheckout(String deviceId)
            throws Exception {
        RentalInformationBean rib = rentalService
                .getRentalInformation(deviceId);
        ;
    }

    @Test(dataProvider = "checkout", dependsOnMethods = { "testDeviceState" })
    public void testCheckout(String deviceId, Date returnDate, String zipcode,
            String locationId, String comment) throws Exception {
        rentalService.deviceCheckOut(deviceId, returnDate, zipcode, locationId,
                comment, comment);
    }

    @Test(dataProvider = "device", dependsOnMethods = { "testCheckout" })
    public void testRentalInformationBeforeCheckin(String deviceId)
            throws Exception {
        RentalInformationBean rib = rentalService
                .getRentalInformation(deviceId);
        ;
    }

    @Test(dataProvider = "device", dependsOnMethods = { "testRentalInformationBeforeCheckin" }, enabled = false)
    public void testCheckin(String deviceId) throws Exception {
        rentalService.deviceCheckIn(deviceId, "sonic");
    }

    @Test(dataProvider = "device", dependsOnMethods = { "testCheckin" }, enabled = false)
    public void testRentalInformationAfterCheckin(String deviceId)
            throws Exception {
        RentalInformationBean rib = rentalService
                .getRentalInformation(deviceId);
        ;
    }

}
