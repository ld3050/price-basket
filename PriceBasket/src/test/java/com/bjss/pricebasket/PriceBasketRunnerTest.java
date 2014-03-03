package com.bjss.pricebasket;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

import java.io.PrintStream;
import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import com.bjss.pricebasket.data.Basket;
import com.bjss.pricebasket.data.BasketTotals;
import com.bjss.pricebasket.data.Item;
import com.bjss.pricebasket.service.BasketPrinterService;
import com.bjss.pricebasket.service.BasketService;
import com.bjss.pricebasket.service.ItemService;

public class PriceBasketRunnerTest {

	private PriceBasketRunner runner;
	private BasketService basketService;
	private ItemService itemService;
	private BasketPrinterService basketPrinterService;

	@Before
	public void setUp() {
		runner = new PriceBasketRunner();
		basketService = mock(BasketService.class);
		itemService = mock(ItemService.class);
		basketPrinterService = mock(BasketPrinterService.class);

		runner.basketService = basketService;
		runner.basketPrinterService = basketPrinterService;
		runner.itemService = itemService;

	}

	@Test
	public void testNullItems() {
		try {
			runner.run();
			fail();
		} catch (IllegalStateException e) {
			// expected
		}
	}

	@Test
	public void testEmptyItems() {
		runner.setItems(new String[0]);
		runner.run();

		class IsEmptyBasket extends ArgumentMatcher<Basket> {
			public boolean matches(Object b) {
				return ((Basket) b).getItems().isEmpty();
			}
		}

		verify(basketService).calculateBasketTotals(
				argThat(new IsEmptyBasket()));
		verify(basketPrinterService).write(any(PrintStream.class),
				any(BasketTotals.class));
		
	}

	@Test
	public void testWithItems() {
		runner.setItems(new String[] { "testitem1", "testitem2", "testitem3", "baditem" });

		final Item testItem1 = new Item("testitem1", new BigDecimal("1.00"));
		final Item testItem2 = new Item("testitem2", new BigDecimal("2.00"));
		final Item testItem3 = new Item("testitem3", new BigDecimal("3.00"));
		when(itemService.getItem("testitem1")).thenReturn(testItem1);
		when(itemService.getItem("testitem2")).thenReturn(testItem2);
		when(itemService.getItem("testitem3")).thenReturn(testItem3);

		runner.run();
		
		verify(itemService).getItem("testitem1");
		verify(itemService).getItem("testitem2");
		verify(itemService).getItem("testitem3");

		class IsPopulatedBasket extends ArgumentMatcher<Basket> {
			public boolean matches(Object b) {
				Basket basket = (Basket) b;
				return basket.getItems().size() == 3
						&& basket.getItems().contains(testItem1)
						&& basket.getItems().contains(testItem2)
						&& basket.getItems().contains(testItem3);
			}
		}

		verify(basketService).calculateBasketTotals(
				argThat(new IsPopulatedBasket()));
		verify(basketPrinterService).write(any(PrintStream.class),
				any(BasketTotals.class));
		
	}

}
