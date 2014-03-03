package com.bjss.pricebasket.service.impl;

import static com.bjss.pricebasket.util.ValidateUtil.validateNotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.service.ItemService;

/**
 * ItemServiceImpl is ApplicationContextAware so that it can collect all Spring
 * configured Item and Offer beans.
 * 
 * @author Leon Danser
 * 
 */
@Named
public class ItemServiceImpl implements ItemService, ApplicationContextAware {

	Map<String, Item> items = new HashMap<String, Item>();
	Map<String, Offer> offers = new LinkedHashMap<String, Offer>();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		items = (Map<String, Item>) applicationContext
				.getBeansOfType(Item.class);
		offers = (Map<String, Offer>) applicationContext
				.getBeansOfType(Offer.class);
	}

	@Override
	public Item getItem(String name) {
		validateNotNull(name, "name");
		return items.get(name.toLowerCase());
	}

	@Override
	public List<Offer> getOffers(Item item) {
		validateNotNull(item, "item");
		List<Offer> offersForItem = new LinkedList<Offer>();
		for (Offer offer : offers.values()) {
			if (item.equals(offer.getItem())) {
				offersForItem.add(offer);
			}
		}
		return offersForItem;
	}

}
