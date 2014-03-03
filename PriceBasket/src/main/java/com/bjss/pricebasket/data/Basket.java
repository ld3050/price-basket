package com.bjss.pricebasket.data;

import java.util.LinkedList;
import java.util.List;

/**
 * A List of {@link Item}s
 * 
 * @author Leon Danser
 *
 */
public class Basket {

	private List<Item> items = new LinkedList<Item>();

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
}
