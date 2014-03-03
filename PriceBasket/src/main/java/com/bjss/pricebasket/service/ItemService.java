package com.bjss.pricebasket.service;

import java.util.List;

import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.data.Offer;

/**
 * Returns {@link Item}s and {@link Offer}s from the configured basket
 * catalogue.
 * 
 * @author Leon Danser
 * 
 */
public interface ItemService {

	/**
	 * An item from the catalogue that matches the given item name.
	 * 
	 * @param name
	 *            the id of the item. Name argument is case-insensitive
	 * @return
	 */
	Item getItem(String name);

	/**
	 * Return the {@link Offer}s applied to the given {@link Item}
	 * 
	 * @param item
	 * @return
	 */
	List<Offer> getOffers(Item item);

}
