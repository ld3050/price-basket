package com.bjss.pricebasket.data;

import java.math.BigDecimal;
import java.util.Map;

import com.bjss.pricebasket.service.BasketPrinterService;
import com.bjss.pricebasket.service.BasketService;

/**
 * The result of a {@link BasketService}. Contains the calculated totals of all
 * results to be printed by the {@link BasketPrinterService} so that the
 * {@link BasketPrinterService} is not responsible for performing any
 * calculations.
 * 
 * @author Leon Danser
 * 
 */
public class BasketTotals {

	private BigDecimal subTotal;

	private Map<Offer, BigDecimal> offerTotals;

	private BigDecimal total;

	/**
	 * The sum of the price of all {@link Item}s in a {@link Basket}
	 * 
	 * @return the sum of the price of all {@link Item}s as a BigDecimal
	 */
	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * The offer totals map contains the sum of all discounts for each
	 * applicable {@link Offer} in a {@link Basket}.
	 * 
	 * @return a Map of {@link Offer}s to the sum of the discounts in a basket.
	 */
	public Map<Offer, BigDecimal> getOfferTotals() {
		return offerTotals;
	}

	public void setOfferTotals(Map<Offer, BigDecimal> offerTotals) {
		this.offerTotals = offerTotals;
	}

	public BigDecimal getTotal() {
		return total;
	}

	/**
	 * The subTotal minus the sum of all values in the offerTotals Map.
	 * 
	 * @return The subTotal minus the sum of all discounts as a BigDecimal
	 */
	public void setTotal(BigDecimal total) {
		this.total = total;
	}

}
