package com.bjss.pricebasket.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;

import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.data.PercentageOffer;

public class ItemServiceImplTest {

	@Test
	public void testGetItemNull() {
		ItemServiceImpl itemService = new ItemServiceImpl();
		try {
			itemService.getItem(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testGetItemEmpty() {
		ItemServiceImpl itemService = new ItemServiceImpl();
		assertEquals(null, itemService.getItem("apple"));
	}

	@Test
	public void testGetItem() {
		ItemServiceImpl itemService = new ItemServiceImpl();
		Item testItem = new Item("testitem", new BigDecimal("12"));
		itemService.items.put("testitem", testItem);
		assertEquals(null, itemService.getItem("apple"));
		// getItem method is case insensitive
		assertEquals(testItem, itemService.getItem("testItem"));
	}

	@Test
	public void testGetOffersNull() {
		ItemServiceImpl itemService = new ItemServiceImpl();
		try {
			itemService.getOffers(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testGetOffersEmpty() {
		ItemServiceImpl itemService = new ItemServiceImpl();
		List<Offer> offers = itemService.getOffers(new Item("testitem",
				new BigDecimal("12.00")));
		assertTrue(offers.isEmpty());
	}

	@Test
	public void testGetOffers() {
		ItemServiceImpl itemService = new ItemServiceImpl();

		Item testItem1 = new Item("testitem1", new BigDecimal("12.00"));
		Item testItem2 = new Item("testitem2", new BigDecimal("13.00"));
		Item testItem3 = new Item("testitem3", new BigDecimal("14.00"));

		PercentageOffer testOffer1 = createOffer("testOffer1", testItem1);
		PercentageOffer testOffer2 = createOffer("testOffer2", testItem1);
		PercentageOffer testOffer3 = createOffer("testOffer3", testItem2);
		itemService.offers.put("testOffer1", testOffer1);
		itemService.offers.put("testOffer2", testOffer2);
		itemService.offers.put("testOffer3", testOffer3);

		// testItem1 should have 2 matching Offers
		List<Offer> offers = itemService.getOffers(testItem1);
		assertEquals(2, offers.size());
		assertEquals(testOffer1, offers.get(0));
		assertEquals(testOffer2, offers.get(1));

		// testItem2 should have 1 matching Offer
		offers = itemService.getOffers(testItem2);
		assertEquals(1, offers.size());
		assertEquals(testOffer3, offers.get(0));

		// testItem3 should have no matching offers
		offers = itemService.getOffers(testItem3);
		assertTrue(offers.isEmpty());
	}

	private PercentageOffer createOffer(String id, Item testItem1) {
		PercentageOffer offer = new PercentageOffer();
		offer.setId(id);
		offer.setItem(testItem1);
		return offer;
	}

}
