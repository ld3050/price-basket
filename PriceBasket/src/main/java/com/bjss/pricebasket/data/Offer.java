package com.bjss.pricebasket.data;

import java.math.BigDecimal;
import java.util.Map;

import com.bjss.pricebasket.service.BasketService;

/**
 * The Offer interface allows the {@link BasketService} to calculate the
 * discount available on Basket {@link Item}s whilst giving the flexibility of
 * different discount calculations, applicability and output formats
 * 
 * @author Leon Danser
 * 
 */
public interface Offer {

	/**
	 * Unique identifier for the Offer.
	 * 
	 * @return unique identifying String
	 */
	String getId();

	/**
	 * The {@link Item} that the offer applies to.
	 * 
	 * @return
	 */
	Item getItem();

	/**
	 * Calculates the discount amount for the given price. eg. a percentage, or
	 * a fixed value, etc.
	 * 
	 * @return The discount to be applied to the {@link Item} price.
	 */
	BigDecimal calculateDiscount();

	/**
	 * Returns true if the offer applies for the given {@link Item} counts, as
	 * well as other factors. If an offer "uses up" items in the item tally, it
	 * is responsible for modifying the counts of those items
	 * 
	 * @param itemTallies
	 *            A map containing the count of each {@link Item} in the basket
	 * @return whether the offer applies or should be ignored.
	 */
	boolean isApplicable(Map<Item, Integer> itemTallies);

	/**
	 * The message to print given a total discount value.
	 * 
	 * @param totalDiscount
	 *            the sum of all discounts applied to a basket.
	 * @return the message to display
	 */
	String buildPrintMessage(String totalDiscount);

}
