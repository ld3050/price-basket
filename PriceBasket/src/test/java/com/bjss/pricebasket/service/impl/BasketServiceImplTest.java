package com.bjss.pricebasket.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.bjss.pricebasket.data.Basket;
import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.data.PercentageOffer;

public class BasketServiceImplTest {

	private BasketServiceImpl basketService;
	private ItemServiceImpl itemService;
	private Item testItem1;
	private Item testItem2;
	private Item testItem3;
	private Item testItem4;

	@Before
	public void setUp() {
		basketService = new BasketServiceImpl();
		itemService = new ItemServiceImpl();
		basketService.itemService = itemService;

		testItem1 = new Item("testitem1", new BigDecimal("1.30"));
		testItem2 = new Item("testitem2", new BigDecimal("1.00"));
		testItem3 = new Item("testitem3", new BigDecimal("2.20"));
		testItem4 = new Item("testitem4", new BigDecimal("3.00"));

		addToItemService(testItem1);
		addToItemService(testItem2);
		addToItemService(testItem3);
		addToItemService(testItem4);
	}

	private void addToItemService(Item item) {
		itemService.items.put(item.getId(), item);
	}

	private void addToItemService(Offer offer) {
		itemService.offers.put(offer.getId(), offer);
	}

	@Test
	public void testNullBasket() {
		try {
			basketService.calculateBasketTotals(null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testEmptyBasket() {
		BasketTotals totals = basketService.calculateBasketTotals(new Basket());

		assertEquals(new BigDecimal("0"), totals.getSubTotal());
		assertTrue(totals.getOfferTotals().isEmpty());
		assertEquals(new BigDecimal("0"), totals.getTotal());
	}

	@Test
	public void testNoOffers() {
		Basket basket = new Basket();

		basket.addItem(testItem1);
		basket.addItem(testItem2);
		basket.addItem(testItem3);
		basket.addItem(testItem1);

		BasketTotals totals = basketService.calculateBasketTotals(basket);

		assertEquals(new BigDecimal("5.80"), totals.getSubTotal());
		assertTrue(totals.getOfferTotals().isEmpty());
		assertEquals(new BigDecimal("5.80"), totals.getTotal());
	}

	@Test
	public void testOneOffer() {
		Basket basket = new Basket();

		basket.addItem(testItem1);
		basket.addItem(testItem2);
		basket.addItem(testItem3);
		basket.addItem(testItem1);

		PercentageOffer offer = new PercentageOffer() {
			@Override
			public boolean isApplicable(Map<Item, Integer> itemTallies) {
				assertEquals(new Integer(2), itemTallies.get(testItem1));
				assertEquals(new Integer(1), itemTallies.get(testItem2));
				assertEquals(new Integer(1), itemTallies.get(testItem3));
				return super.isApplicable(itemTallies);
			}
		};

		offer.setId("testOffer1");
		offer.setDiscount(new BigDecimal("0.1"));
		offer.setItem(testItem1);
		addToItemService(offer);

		BasketTotals totals = basketService.calculateBasketTotals(basket);

		assertEquals(new BigDecimal("5.80"), totals.getSubTotal());
		assertEquals(1, totals.getOfferTotals().size());
		assertEquals(new BigDecimal("0.260"), totals.getOfferTotals().get(offer));
		assertEquals(new BigDecimal("5.540"), totals.getTotal());
	}

	@Test
	public void testMultipleOffers() {
		Basket basket = new Basket();

		basket.addItem(testItem1);
		basket.addItem(testItem2);
		basket.addItem(testItem3);
		basket.addItem(testItem1);

		// applicable to testItem1
		PercentageOffer offer1 = new PercentageOffer() {
			@Override
			public boolean isApplicable(Map<Item, Integer> itemTallies) {
				assertEquals(new Integer(2), itemTallies.get(testItem1));
				assertEquals(new Integer(1), itemTallies.get(testItem2));
				assertEquals(new Integer(1), itemTallies.get(testItem3));
				return super.isApplicable(itemTallies);
			}
		};

		offer1.setId("testOffer1");
		offer1.setDiscount(new BigDecimal("0.1"));
		offer1.setItem(testItem1);
		
		// applicable to testItem2
		PercentageOffer offer2 = new PercentageOffer() {
			@Override
			public boolean isApplicable(Map<Item, Integer> itemTallies) {
				assertEquals(new Integer(2), itemTallies.get(testItem1));
				assertEquals(new Integer(1), itemTallies.get(testItem2));
				assertEquals(new Integer(1), itemTallies.get(testItem3));
				return super.isApplicable(itemTallies);
			}
		};

		offer2.setId("testOffer2");
		offer2.setDiscount(new BigDecimal("0.2"));
		offer2.setItem(testItem2);
		
		// applicable to testItem4 which is not in the basket
		PercentageOffer offer3 = new PercentageOffer();

		offer3.setId("testOffer3");
		offer3.setDiscount(new BigDecimal("0.4"));
		offer3.setItem(testItem4);
		
		addToItemService(offer1);
		addToItemService(offer2);
		addToItemService(offer3);

		BasketTotals totals = basketService.calculateBasketTotals(basket);

		assertEquals(new BigDecimal("5.80"), totals.getSubTotal());
		assertEquals(2, totals.getOfferTotals().size());
		assertEquals(new BigDecimal("0.260"), totals.getOfferTotals().get(offer1));
		assertEquals(new BigDecimal("0.200"), totals.getOfferTotals().get(offer2));
		assertEquals(new BigDecimal("5.340"), totals.getTotal());
	}
	
}
