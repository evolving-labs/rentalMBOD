/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.telespree.barracuda.product.bean.Product;
import com.telespree.barracuda.product.exception.PromoCodeServiceException;
import com.telespree.barracuda.product.service.IPromoCodeService;

/**
 * StubPromoCodeService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: juan $ on $DateTime: 2012/08/26 15:26:50 $
 * @version $Revision: #3 $
 * 
 */
@Service("stubPromoCodeService")
public class StubPromoCodeService implements IPromoCodeService {

	@Override
	public String generatePromo(String carrierName, Long productId, String sku,
			Date validUntil) throws PromoCodeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Product getProductForPromo(String carrierName, String promoCode)
			throws PromoCodeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isPromoCodeValid(String carrierName, String promoCode)
			throws PromoCodeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void markRedeemed(String promoCode) throws PromoCodeServiceException {
		// TODO Auto-generated method stub
		
	}


}
