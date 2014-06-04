/**
 * 
 */
package com.telespree.apps.sprint.mbod.rental;

import java.util.Comparator;

import com.telespree.barracuda.product.bean.Product;

/**
 * ProductComparator
 * 
 * TODO: Explain this class!
 * 
 * @author $Author: michael $ on $DateTime: 2014/02/11 15:47:56 $
 * @version $Revision: #1 $
 * 
 */
public class ProductComparator implements Comparator<Product> {

    @Override
    public int compare(Product p1, Product p2) {

        if (p1.getPriority() != p2.getPriority()) {
            return p1.getPriority() - p2.getPriority();
        }
        Float price1 = Float.parseFloat(p1.getPrice());
        Float price2 = Float.parseFloat(p2.getPrice());
        if (price1 > price2) {
            return 1;
        }
        if (price1 < price2) {
            return -1;
        }
        return 0;
    }

}
