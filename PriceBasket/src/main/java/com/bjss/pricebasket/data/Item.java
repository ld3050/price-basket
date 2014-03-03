package com.bjss.pricebasket.data;

import java.math.BigDecimal;

import javax.inject.Inject;

import com.bjss.pricebasket.service.MsgService;

/**
 * A Price Basket Item. The id should be unique and the price should not be
 * null.
 * 
 * @author Leon Danser
 * 
 */
public class Item {
	private static final String ITEM_NAME_PREFIX = "item.name.";

	@Inject
	MsgService msgService;

	private String id;
	private BigDecimal price;
	
	public Item() {
	}
	
	public Item(String id, BigDecimal price) {
		this.id = id;
		this.price = price;
	}

	/**
	 * Unique identifier for the {@link Item}
	 * 
	 * @return unique identifying String
	 */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The price of the item (in local currency)
	 * 
	 * @return BigDecimal representation of the price of the item
	 */
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * The localised name for this item
	 * @return
	 */
	public String getDisplayName() {
		return msgService.getMessage(ITEM_NAME_PREFIX + id);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Item other = (Item) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", price=" + price + "]";
	}

}
