/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.telespree.abmf.exception.DeviceServiceException;
import com.telespree.rental.exception.RentalServiceException;
import com.telespree.rental.service.IRentalService;
import com.telespree.rental.service.RentalInformationBean;

/**
 * StubRentalService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: juan $ on $DateTime: 2012/08/26 15:26:50 $
 * @version $Revision: #2 $
 * 
 */
@Service("stubRentalService")
public class StubRentalService implements IRentalService {

	@Override
	public void deviceCheckOut(String deviceId, Date rentedUntil,
			String zipCode, String locationId, String comments,
			String billToNumber) throws RentalServiceException,
			DeviceServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceCheckIn(String deviceId, String comments)
			throws RentalServiceException, DeviceServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getTermsAndConditionsVersion(String deviceId)
			throws RentalServiceException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTermsAndConditionsVersion(String deviceId, int version)
			throws RentalServiceException, DeviceServiceException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RentalInformationBean getRentalInformation(String deviceId)
			throws RentalServiceException {
		// TODO Auto-generated method stub
		return null;
	}


}
