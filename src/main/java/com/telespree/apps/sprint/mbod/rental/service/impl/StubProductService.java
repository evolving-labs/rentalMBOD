/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.telespree.barracuda.product.bean.Product;
import com.telespree.barracuda.product.exception.ProductServiceException;
import com.telespree.barracuda.product.service.IProductService;

/**
 * StubProductService
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: juan $ on $DateTime: 2012/08/26 15:26:50 $
 * @version $Revision: #6 $
 * 
 */
@Service("stubProductService")
public class StubProductService implements IProductService {

	@Override
	public List<Product> getAllProductList(String carrierName)
			throws ProductServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getPromotionalProductList(String carrierName)
			throws ProductServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getNonPromotionalProductList(String carrierName)
			throws ProductServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Product> getAddOns(Long serviceInstanceId)
			throws ProductServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
