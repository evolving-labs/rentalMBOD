/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.telespree.abmf.bean.BalanceBean;
import com.telespree.abmf.bean.PurchaseActivityBean;
import com.telespree.abmf.bean.PurchaseBean;
import com.telespree.abmf.exception.DeviceServiceException;
import com.telespree.abmf.service.DeviceState;
import com.telespree.abmf.service.IDeviceService;
import com.telespree.barracuda.oracle.model.User;

/**
 * StubDeviceService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/10/07 14:03:37 $
 * @version $Revision: #8 $
 * 
 */
@Service("stubDeviceService")
public class StubDeviceService implements IDeviceService {

    @Override
    public String getCurrentState(String deviceId)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void modifyState(String deviceId, DeviceState state)
            throws DeviceServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void modifyState(String deviceId, DeviceState state,
            boolean isAlreadyCheckedIn) throws DeviceServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void removePurchases(String deviceId) throws DeviceServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void addPurchaseActivity(String deviceId, PurchaseActivityBean pa)
            throws DeviceServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public void purchaseAddOn(String arg0, String arg1, String arg2)
            throws DeviceServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public String purchaseService(String deviceId, String productId, String sku)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BalanceBean> getAllBalances(String deviceId)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<BalanceBean> getPurchaseBalances(String deviceId)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PurchaseBean> getAllPurchases(String deviceId)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public User findUserByDeviceId(String deviceId)
            throws DeviceServiceException {
        // TODO Auto-generated method stub
        return null;
    }

}
