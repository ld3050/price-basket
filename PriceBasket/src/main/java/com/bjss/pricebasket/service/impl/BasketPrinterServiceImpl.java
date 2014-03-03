package com.bjss.pricebasket.service.impl;

import static com.bjss.pricebasket.util.ValidateUtil.validateNotNull;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.i18n.LocaleContextHolder;

import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Offer;
import com.bjss.pricebasket.service.BasketPrinterService;
import com.bjss.pricebasket.service.MsgService;

/**
 * Writes a given {@link BasketTotals} object with the format:
 * 
 * <pre>
 * SUBTOTAL
 * OFFERS
 * TOTAL
 * </pre>
 * 
 * eg.
 * 
 * <pre>
 * Subtotal: œ5.40
 * Bread 50% off: -40p
 * Apples 10% off: -20p
 * Total price: œ4.80
 * </pre>
 * 
 * Uses System's configured locale to format messages and currency
 * 
 * @author Leon Danser
 * 
 */
@Named
public class BasketPrinterServiceImpl implements BasketPrinterService {

	public static final String ENCODING = "CP850";
	private static final String NO_OFFERS_AVAILABLE = "no.offers.available";
	private static final String TOTAL = "total";
	private static final String SUBTOTAL = "subtotal";
	private static final String MINOR_CURRENCY = "minor.currency";

	@Inject
	MsgService msgService;

	public void write(PrintStream out, BasketTotals totals) {
		validateNotNull(out, "out");
		validateNotNull(totals, "totals");
		OutputStreamWriter writer = null;
		try {
			writer = new OutputStreamWriter(out, ENCODING);
			String subTotalString = formatCurrency(totals.getSubTotal());
			String offersString = formatOffers(totals.getOfferTotals());
			String totalString = formatCurrency(totals.getTotal());
			writer.write(msgService.getMessage(SUBTOTAL, subTotalString) + "\n");
			// offersString responsible for appending new line
			writer.write(offersString);
			writer.write(msgService.getMessage(TOTAL, totalString) + "\n");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(
					"BasketPrinter encountered an error occurred printing to the specified print stream",
					e);
		} finally {
			closeQuietly(writer);
		}
	}

	private void closeQuietly(OutputStreamWriter writer) {
		try {
			if (writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			throw new RuntimeException(
					"BasketPrinter encountered error closing OutputStreamWriter",
					e);
		}
	}

	/**
	 * Iterates through the entries in an offerTotals map and outputs all
	 * {@link Offer}s according to its buildPrintMessage() call. Responsible for
	 * appending its own newline character
	 * 
	 * @param offerTotals
	 * @return a String containing the localised offer print messages for each
	 *         entry in the offerTotals map
	 */
	protected String formatOffers(Map<Offer, BigDecimal> offerTotals) {
		StringBuffer sb = new StringBuffer();
		if (offerTotals == null || offerTotals.isEmpty()) {
			sb.append(msgService.getMessage(NO_OFFERS_AVAILABLE)).append('\n');
		} else {
			for (Entry<Offer, BigDecimal> offerEntry : offerTotals.entrySet()) {
				Offer offer = offerEntry.getKey();
				BigDecimal totalDiscount = offerEntry.getValue();
				String formattedTotalDiscount = formatCurrency(totalDiscount);

				sb.append(offer.buildPrintMessage(formattedTotalDiscount))
						.append('\n');
			}
		}
		return sb.toString();
	}

	/**
	 * Use the currency format for the system locale to output values greater
	 * than 1. For fractions of 1, output the currency in cents/pence
	 * 
	 * @param value
	 *            the currency value to format
	 * @return the argument value as a formatted currency String including the
	 *         currency sign
	 */
	protected String formatCurrency(BigDecimal value) {
		if (BigDecimal.ONE.compareTo(value) <= 0) {
			Locale locale = LocaleContextHolder.getLocale();
			NumberFormat numberFormatter = NumberFormat
					.getCurrencyInstance(locale);
			return numberFormatter.format(value);
		} else {
			BigDecimal valueInPence = value.multiply(new BigDecimal(100));
			valueInPence = valueInPence.setScale(0, RoundingMode.HALF_UP);
			return msgService.getMessage(MINOR_CURRENCY,
					valueInPence.toString());
		}
	}

}
