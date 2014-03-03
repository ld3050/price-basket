

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bjss.pricebasket.PriceBasketRunner;

/**
 * Main class for PriceBasket program.
 * 
 * PriceBasket takes items to be purchased as command line arguments, then sums
 * their total cost according to their pre-configured prices. It then calculates
 * any discounts applied to the purchased items and prints out a the subtotal,
 * discounts and totals. eg:
 * 
 * <pre>
 * Subtotal: œ5.40
 * Bread 50% off: -40p
 * Apples 10% off: -20p
 * Total price: œ4.80
 * </pre>
 * 
 * Invalid Items added to the Basket are ignored.
 * 
 * @author Leon Danser
 */

public class PriceBasket {

	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			printUsage();
			System.exit(0);
		}
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/applicationContext.xml");
		PriceBasketRunner runner = context.getBean(PriceBasketRunner.class);
		runner.setItems(args);
		runner.run();
		context.close();
	}

	private static void printUsage() {
		System.out.println("Usage: java PriceBasket [items]\n"
				+ "eg. java PriceBasket Apple Milk Bread");

	}

	
}
