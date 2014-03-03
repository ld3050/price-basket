package com.bjss.pricebasket.service;

import com.bjss.pricebasket.data.Basket;
import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Offer;

/**
 * Sums all the prices of all {@link Item}s in a {@link Basket}, then calculates
 * all applicable offers for those items and returns them as
 * {@link BasketTotals}
 * 
 * @author Leon Danser
 * 
 */
public interface BasketService {

	/**
	 * Populates a {@link BasketTotals} object with the totals for the given
	 * {@link Basket} including all applicable {@link Offer}s
	 * 
	 * @param basket
	 *            the basket to calculate totals for
	 * @return a populated BasketTotals result
	 */
	BasketTotals calculateBasketTotals(Basket basket);

}
