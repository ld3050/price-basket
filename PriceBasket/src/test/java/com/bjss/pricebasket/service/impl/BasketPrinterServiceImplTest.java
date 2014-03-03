package com.bjss.pricebasket.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.i18n.LocaleContextHolder;

import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.data.PercentageOffer;
import com.bjss.pricebasket.service.mock.MockMsgService;

public class BasketPrinterServiceImplTest {

	private MockMsgService mockMsgService;

	@Before
	public void setUp() {
		LocaleContextHolder.setLocale(Locale.UK);
		mockMsgService = new MockMsgService();
		mockMsgService.addMessage("no.offers.available",
				"(No offers available)");
		mockMsgService.addMessage("subtotal", "Subtotal: %s");
		mockMsgService.addMessage("total", "Total price: %s");
		mockMsgService.addMessage("minor.currency", "%sp");
	}

	@Test
	public void testWriteNullPrintStream() {
		BasketPrinterServiceImpl basketPrinterService = new BasketPrinterServiceImpl();
		BasketTotals basketTotals = new BasketTotals();
		basketTotals.setSubTotal(new BigDecimal("5.4"));
		basketTotals.setTotal(new BigDecimal("5.4"));

		try {
			basketPrinterService.write(null, basketTotals);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		}
	}

	@Test
	public void testWriteNullBasketTotal() throws IOException {
		BasketPrinterServiceImpl basketPrinterService = new BasketPrinterServiceImpl();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		try {
			basketPrinterService.write(ps, null);
			fail();
		} catch (IllegalArgumentException e) {
			// expected
		} finally {
			baos.close();
			ps.close();
		}
	}

	@Test
	public void testWriteWithNoOffers() throws IOException {
		BasketPrinterServiceImpl basketPrinterService = new BasketPrinterServiceImpl();
		basketPrinterService.msgService = mockMsgService;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		BasketTotals basketTotals = new BasketTotals();
		basketTotals.setSubTotal(new BigDecimal("5.4"));
		basketTotals.setTotal(new BigDecimal("5.4"));

		basketPrinterService.write(ps, basketTotals);
		assertEquals(
				"Subtotal: œ5.40\n(No offers available)\nTotal price: œ5.40\n",
				baos.toString(BasketPrinterServiceImpl.ENCODING));

		baos.close();
		ps.close();
	}

	@Test
	public void testWriteWithOffers() throws IOException {
		BasketPrinterServiceImpl basketPrinterService = new BasketPrinterServiceImpl();
		basketPrinterService.msgService = mockMsgService;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		BasketTotals basketTotals = new BasketTotals();
		basketTotals.setSubTotal(new BigDecimal("5.4"));
		basketTotals.setTotal(new BigDecimal("5.4"));

		Map<Offer, BigDecimal> offerTotals = new HashMap<Offer, BigDecimal>();
		PercentageOffer testOffer1 = createOffer("testOffer1",
				"Test Item1 10% off: -10p", "10p");
		PercentageOffer testOffer2 = createOffer("testOffer2",
				"Test Item2 25% off: -50p", "50p");
		offerTotals.put(testOffer1, new BigDecimal("0.1"));
		offerTotals.put(testOffer2, new BigDecimal("0.5"));
		basketTotals.setOfferTotals(offerTotals);

		basketPrinterService.write(ps, basketTotals);
		assertEquals(
				"Subtotal: œ5.40\nTest Item1 10% off: -10p\nTest Item2 25% off: -50p\nTotal price: œ5.40\n",
				baos.toString(BasketPrinterServiceImpl.ENCODING));

		baos.close();
		ps.close();
	}

	private PercentageOffer createOffer(String id, final String printMessage,
			final String expectedTotalDiscount) {
		PercentageOffer offer = new PercentageOffer() {
			@Override
			public String buildPrintMessage(String totalDiscount) {
				assertEquals(expectedTotalDiscount, totalDiscount);
				return printMessage;
			}
		};
		offer.setId(id);
		return offer;
	}

}
