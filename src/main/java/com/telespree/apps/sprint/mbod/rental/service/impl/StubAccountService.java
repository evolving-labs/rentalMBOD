/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.telespree.abmf.exception.AccountServiceException;
import com.telespree.abmf.service.IAccountService;
import com.telespree.abmf.service.OverwritePolicy;

/**
 * StubAccountService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2013/10/07 15:36:59 $
 * @version $Revision: #7 $
 * 
 */
@Service("stubAccountService")
public class StubAccountService implements IAccountService {

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.abmf.service.IAccountService#createAccount(java.lang.String
     * , java.lang.String)
     */
    public void createAccount(String accountNumber, String carrierName)
            throws AccountServiceException {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.telespree.abmf.service.IAccountService#addDevice(java.lang.String,
     * java.util.List, java.lang.String, java.lang.String)
     */
    public void addDevice(String accountNumber, List<String> deviceIds,
            String msid, String mdn, OverwritePolicy policy)
            throws AccountServiceException {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean validateDeviceCarrier(String carrierName, String deviceId)
            throws AccountServiceException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addDevice(String accountNumber, List<String> deviceIds,
            String msid, String mdn, String imsi, OverwritePolicy policy, Set<Long> services)
            throws AccountServiceException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addDeviceToCarrier(String carrierName, List<String> deviceIds,
            String msid, String mdn, String imsi, OverwritePolicy policy, Set<Long> services)
            throws AccountServiceException {
        // TODO Auto-generated method stub
        
    }

	@Override
	public void updateDeviceCarrier(String deviceId, String carrier)
			throws AccountServiceException {
		// TODO Auto-generated method stub
		
	}

}
