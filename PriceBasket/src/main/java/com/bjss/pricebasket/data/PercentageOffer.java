package com.bjss.pricebasket.data;

import static com.bjss.pricebasket.util.ValidateUtil.validateNotNull;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;

import com.bjss.pricebasket.service.MsgService;

/**
 * An implementation of {@link Offer} that discounts an item by a specified
 * percentage. Other conditions of applicability include:
 * <ul>
 * <li>expiry date: if specified, an offer will not be applicable after a
 * certain date</li>
 * <li>required items: if specified, the required quantity of all items in the
 * required items list must be in the basket along with</li>
 * </ul>
 * 
 * @author Leon Danser
 * 
 */
public class PercentageOffer implements Offer {

	private static final String PERCENTAGE_OFFER = "percentage.offer";

	@Inject
	MsgService msgService;

	private String id;
	private BigDecimal discount;
	private Item item;
	private Date expiryDate;
	private Map<Item, Integer> requiredItems;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * The percentage discount, specified as a fraction of 1. ie. a discount of
	 * 1 is 100%
	 * 
	 * @return
	 */
	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	@Override
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	/**
	 * A Map of {@link Item}s and their quantities that are required for the
	 * offer to be applicable. eg.
	 * "Buy 2 tins of soup and get a loaf of bread for half price" will give a
	 * requiredItem map of: { Soup: 2 }
	 * 
	 * @return
	 */
	public Map<Item, Integer> getRequiredItems() {
		return requiredItems;
	}

	public void setRequiredItems(Map<Item, Integer> requiredItems) {
		this.requiredItems = requiredItems;
	}

	/**
	 * The date that the offer expires (inclusive)
	 * 
	 * @return
	 */
	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiry) {
		this.expiryDate = expiry;
	}

	/**
	 * A percentageOffer returns the discount multiplied by the {@link Item}s
	 * price
	 * 
	 * @return
	 */
	@Override
	public BigDecimal calculateDiscount() {
		if (item == null) {
			throw new IllegalStateException("[item] should not be null");
		}
		if (item.getPrice() == null) {
			throw new IllegalStateException("[item price] should not be null");
		}
		if (discount == null) {
			throw new IllegalStateException("[discount] should not be null");
		}
		return item.getPrice().multiply(discount);
	}

	/**
	 * Determine if expiryDate and requiredItems conditions are met. If the
	 * requiredItem conditions are met, the itemTallies argument will be
	 * modified to reflect the new counts.
	 * 
	 * @param itemTallies
	 *            The number of each number
	 * @return Return true if all offer conditions are met.
	 */
	@Override
	public boolean isApplicable(Map<Item, Integer> itemTallies) {
		validateNotNull(itemTallies, "itemTallies");
		boolean isBeforeExpired = isBeforeExpiryDate();
		boolean hasApplicableRequiredItems = hasApplicableRequiredItems(itemTallies);
		return isBeforeExpired && hasApplicableRequiredItems;
	}

	/**
	 * Determine if the current system time is less than or equal to the
	 * expiryDate
	 * 
	 * @return
	 */
	private boolean isBeforeExpiryDate() {
		if (expiryDate != null) {
			/*
			 * Add one day to the expiryDate so that the offer expires at
			 * midnight the following day. ie. make the expiryDate inclusive of
			 * today.
			 */
			Calendar expiryCalendar = Calendar.getInstance();
			expiryCalendar.setTime(expiryDate);
			expiryCalendar.add(Calendar.DATE, 1);
			Calendar now = getCurrentTime();
			return (now.compareTo(expiryCalendar) <= 0);
		}
		return true;
	}

	protected Calendar getCurrentTime() {
		return Calendar.getInstance();
	}

	/**
	 * Checks each requiredItem entry to see if the itemTallies meet the
	 * quantity requirements. If it does, the tally for that {@link Item} will
	 * be modified.
	 * 
	 * @param itemTallies
	 *            count of each {@link Item} in the basket
	 * @return true if the requirements for all requiredItem entries are met.
	 */
	private boolean hasApplicableRequiredItems(Map<Item, Integer> itemTallies) {
		if (requiredItems == null || requiredItems.isEmpty()) {
			return true;
		}
		for (Entry<Item, Integer> requiredItemEntry : requiredItems.entrySet()) {
			Item requiredItem = requiredItemEntry.getKey();
			Integer qty = requiredItemEntry.getValue();
			Integer itemTally = itemTallies.get(requiredItem);
			if (itemTally != null && itemTally >= qty) {
				itemTallies.put(requiredItem, itemTally - qty);
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * The message showing the item, the discount percentage and the
	 * totalDiscount. eg. "Bread 50% off: -40p"
	 * 
	 * @return a String containing the offer message
	 */
	@Override
	public String buildPrintMessage(String totalDiscount) {
		validateNotNull(totalDiscount, "totalDiscount");
		if (item == null) {
			throw new IllegalStateException("[item] should not be null");
		}
		return msgService.getMessage(PERCENTAGE_OFFER, item.getDisplayName(),
				formatDiscount(), totalDiscount);
	}

	/**
	 * Convert the discount value to a percentage of 100. eg. 0.5 becomes 50
	 * 
	 * @return
	 */
	protected String formatDiscount() {
		if (discount == null) {
			throw new IllegalStateException("[discount] should not be null");
		}
		BigDecimal displayablePercent = discount.multiply(new BigDecimal(100));
		DecimalFormat format = new DecimalFormat("#.##");
		return format.format(displayablePercent.doubleValue());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((discount == null) ? 0 : discount.hashCode());
		result = prime * result
				+ ((expiryDate == null) ? 0 : expiryDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((item == null) ? 0 : item.hashCode());
		result = prime * result
				+ ((requiredItems == null) ? 0 : requiredItems.hashCode());
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
		PercentageOffer other = (PercentageOffer) obj;
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
			return false;
		if (expiryDate == null) {
			if (other.expiryDate != null)
				return false;
		} else if (!expiryDate.equals(other.expiryDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (item == null) {
			if (other.item != null)
				return false;
		} else if (!item.equals(other.item))
			return false;
		if (requiredItems == null) {
			if (other.requiredItems != null)
				return false;
		} else if (!requiredItems.equals(other.requiredItems))
			return false;
		return true;
	}

}
