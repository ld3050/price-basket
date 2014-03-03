package com.bjss.pricebasket;

import javax.inject.Inject;
import javax.inject.Named;

import com.bjss.pricebasket.data.Basket;
import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.service.BasketPrinterService;
import com.bjss.pricebasket.service.BasketService;
import com.bjss.pricebasket.service.ItemService;

/**
 * Gets {@Item} for each item in items Array, adds them to a
 * {@link Basket}, calculates the Totals and writes it to System.out
 */
@Named
public class PriceBasketRunner {

	@Inject
	BasketService basketService;

	@Inject
	ItemService itemService;

	@Inject
	BasketPrinterService basketPrinterService;
	
	private String[] items;
	
	public void run() {
		if (items == null) {
			throw new IllegalStateException("items array should not be null");
		}
		Basket basket = new Basket();
		for (String itemName : items) {
			Item item = itemService.getItem(itemName);
			if (item == null) {
				continue; // skip invalid items
			}
			basket.addItem(item);
		}

		BasketTotals basketTotals = basketService.calculateBasketTotals(basket);
		basketPrinterService.write(System.out, basketTotals);
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	
}
