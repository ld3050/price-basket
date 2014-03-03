package com.bjss.pricebasket.service.impl;

import static com.bjss.pricebasket.util.ValidateUtil.validateNotNull;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.bjss.pricebasket.data.Basket;
import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.service.BasketService;
import com.bjss.pricebasket.service.ItemService;

/**
 * @see BasketService
 * @author Leon
 *
 */
@Named
public class BasketServiceImpl implements BasketService {

	@Inject
	ItemService itemService;

	@Override
	public BasketTotals calculateBasketTotals(Basket basket) {
		validateNotNull(basket, "basket");
		BasketTotals totals = new BasketTotals();

		// A map that stores how many of each item there is in the basket.
		// Used to calculate offerTotals.
		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();

		BigDecimal subTotal = calculateSubTotal(basket, itemTallies);
		Map<Offer, BigDecimal> offerTotals = calculateOfferTotals(basket,
				itemTallies);
		BigDecimal total = calculateTotal(subTotal, offerTotals);

		totals.setSubTotal(subTotal);
		totals.setOfferTotals(offerTotals);
		totals.setTotal(total);
		return totals;
	}

	/**
	 * Subtracts total of all discounts from the subtotal
	 * @param subTotal
	 * @param offerTotals
	 * @return
	 */
	private BigDecimal calculateTotal(BigDecimal subTotal,
			Map<Offer, BigDecimal> offerTotals) {
		BigDecimal discounts = BigDecimal.ZERO;
		for (BigDecimal total : offerTotals.values()) {
			discounts = discounts.add(total);
		}
		return subTotal.subtract(discounts);
	}

	/**
	 * Builds a map of {@link Offer}s that are applicable to the given
	 * {@link Basket} along with the total discount gained by those Offers.
	 * 
	 * @param basket
	 * @param itemTallies
	 *            the item counts from the calculateSubTotal call. Used to
	 *            calculate the applicability of certain {@link Offer}s
	 * @return
	 */
	private Map<Offer, BigDecimal> calculateOfferTotals(Basket basket,
			Map<Item, Integer> itemTallies) {
		Map<Offer, BigDecimal> offerTotals = new HashMap<Offer, BigDecimal>();
		for (Item item : basket.getItems()) {
			List<Offer> offers = itemService.getOffers(item);
			for (Offer offer : offers) {
				if (offer.isApplicable(itemTallies)) {
					BigDecimal offerDiscount = offer.calculateDiscount();
					BigDecimal totalOfferDiscount = offerTotals.get(offer);
					if (totalOfferDiscount == null) {
						offerTotals.put(offer, offerDiscount);
					} else {
						offerTotals.put(offer,
								totalOfferDiscount.add(offerDiscount));
					}
				}
			}
		}
		return offerTotals;
	}

	/**
	 * Iterates through all {@link Item}s in the {@link Basket}, summing their
	 * price and also keep a track of the count of each Item.
	 * 
	 * @param basket
	 * @param itemTallies
	 *            a map that stores how many of each item there is in the
	 *            basket.
	 * @return the subtotal for the {@link Basket}
	 */
	private BigDecimal calculateSubTotal(Basket basket,
			Map<Item, Integer> itemTallies) {
		BigDecimal subtotal = BigDecimal.ZERO;
		for (Item item : basket.getItems()) {
			subtotal = subtotal.add(item.getPrice());
			Integer tally = itemTallies.get(item);
			if (tally == null) {
				itemTallies.put(item, 1);
			} else {
				itemTallies.put(item, tally + 1);
			}
		}
		return subtotal;
	}

}
