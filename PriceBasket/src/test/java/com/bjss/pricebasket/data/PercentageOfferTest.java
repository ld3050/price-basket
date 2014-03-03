package com.bjss.pricebasket.data;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.bjss.pricebasket.service.mock.MockMsgService;

public class PercentageOfferTest {

	@Test
	public void testCalculateDiscountNullItem() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.3"));

		try {
			percentageOffer.calculateDiscount();
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testCalculateDiscountItemNullPrice() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.3"));

		Item item = new Item("testitem", null);
		percentageOffer.setItem(item);

		try {
			percentageOffer.calculateDiscount();
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testCalculateDiscountItemNullDiscount() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");

		Item item = new Item("testitem", new BigDecimal("3.00"));
		percentageOffer.setItem(item);

		try {
			percentageOffer.calculateDiscount();
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testCalculateDiscount() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		Item item = new Item("testitem", new BigDecimal("4.00"));
		percentageOffer.setItem(item);

		assertEquals(new BigDecimal("1.0000"),
				percentageOffer.calculateDiscount());
	}

	@Test
	public void testIsApplicableNullTallies() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		try {
			percentageOffer.isApplicable(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testIsApplicableNoExpiryOrReqItems() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();

		assertTrue(percentageOffer.isApplicable(itemTallies));
	}

	@Test
	public void testIsApplicableBeforeExpiry() {
		PercentageOffer percentageOffer = new PercentageOffer() {
			@Override
			protected Calendar getCurrentTime() {
				Calendar now = Calendar.getInstance();
				now.set(2014, 3, 1);
				return now;
			}
		};
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		Calendar expiryCalendar = Calendar.getInstance();
		expiryCalendar.set(2014, 3, 8);
		percentageOffer.setExpiryDate(expiryCalendar.getTime());

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();

		assertTrue(percentageOffer.isApplicable(itemTallies));
	}

	@Test
	public void testIsApplicableAfterExpiry() {
		PercentageOffer percentageOffer = new PercentageOffer() {
			@Override
			protected Calendar getCurrentTime() {
				Calendar now = Calendar.getInstance();
				now.set(2014, 3, 1);
				return now;
			}
		};
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		Calendar expiryCalendar = Calendar.getInstance();
		expiryCalendar.set(2014, 2, 28);
		percentageOffer.setExpiryDate(expiryCalendar.getTime());

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();

		assertFalse(percentageOffer.isApplicable(itemTallies));
	}

	@Test
	public void testIsApplicableWithRequiredItems() {
		Item requiredItem1 = new Item("reqitem", new BigDecimal("1.00"));
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));
		Map<Item, Integer> requiredItems = new HashMap<Item, Integer>();
		requiredItems.put(requiredItem1, 3);
		percentageOffer.setRequiredItems(requiredItems);

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();
		itemTallies.put(requiredItem1, 5);

		assertTrue(percentageOffer.isApplicable(itemTallies));
		// assert that the item tally has been updated
		assertEquals(new Integer(2), itemTallies.get(requiredItem1));
	}

	@Test
	public void testIsApplicableWithInsufficientRequiredItems() {
		Item requiredItem1 = new Item("reqitem", new BigDecimal("1.00"));
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));
		Map<Item, Integer> requiredItems = new HashMap<Item, Integer>();
		requiredItems.put(requiredItem1, 3);
		percentageOffer.setRequiredItems(requiredItems);

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();
		itemTallies.put(requiredItem1, 2);

		assertFalse(percentageOffer.isApplicable(itemTallies));
		// assert that the item tally has not been changed
		assertEquals(new Integer(2), itemTallies.get(requiredItem1));
	}

	@Test
	public void testIsApplicableWithExpiryAndRequiredItems() {
		Item requiredItem1 = new Item("reqitem", new BigDecimal("1.00"));
		PercentageOffer percentageOffer = new PercentageOffer() {
			@Override
			protected Calendar getCurrentTime() {
				Calendar now = Calendar.getInstance();
				now.set(2014, 3, 1);
				return now;
			}
		};
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));
		Calendar expiryCalendar = Calendar.getInstance();
		expiryCalendar.set(2014, 3, 8);

		Map<Item, Integer> requiredItems = new HashMap<Item, Integer>();
		requiredItems.put(requiredItem1, 3);
		percentageOffer.setRequiredItems(requiredItems);

		Map<Item, Integer> itemTallies = new HashMap<Item, Integer>();
		itemTallies.put(requiredItem1, 5);

		assertTrue(percentageOffer.isApplicable(itemTallies));
		// assert that the item tally has not been changed
		assertEquals(new Integer(2), itemTallies.get(requiredItem1));
	}

	@Test
	public void testBuildPrintMessageNullTotal() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		try {
			percentageOffer.buildPrintMessage(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testBuildPrintMessageNullItem() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		try {
			percentageOffer.buildPrintMessage("10p");
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testBuildPrintMessageNullDiscount() {
		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.setId("testItemOffer");

		try {
			percentageOffer.buildPrintMessage("10p");
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testBuildPrintMessage() {
		MockMsgService mockMsgService = new MockMsgService();
		mockMsgService.addMessage("percentage.offer", "%s %s%% off: -%s");
		mockMsgService.addMessage("item.name.testitem", "Test Item");

		PercentageOffer percentageOffer = new PercentageOffer();
		percentageOffer.msgService = mockMsgService;
		percentageOffer.setId("testItemOffer");
		percentageOffer.setDiscount(new BigDecimal("0.25"));

		Item testItem = new Item("testitem", new BigDecimal("1.00"));
		testItem.msgService = mockMsgService;
		percentageOffer.setItem(testItem);

		assertEquals("Test Item 25% off: -10p",
				percentageOffer.buildPrintMessage("10p"));

	}

}
