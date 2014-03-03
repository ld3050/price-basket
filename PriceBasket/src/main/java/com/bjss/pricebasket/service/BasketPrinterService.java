package com.bjss.pricebasket.service;

import java.io.PrintStream;

import com.bjss.pricebasket.data.BasketTotals;

/**
 * Writes the given {@link BasketTotal} object to the given {@link PrintStream}
 * 
 * @author Leon Danser
 * 
 */
public interface BasketPrinterService {

	/**
	 * Writes the {@link BasketTotal}s to the given {@link PrintStream}
	 * @param out PrintStream to be written to.
	 * @param totals a populated {@link BasketTotal} to be printed
	 */
	void write(PrintStream out, BasketTotals totals);

}
