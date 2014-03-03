package com.bjss.pricebasket.parser;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringConfigLoadingTest {

	@Test
	public void testLoading() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"classpath:/META-INF/applicationContext.xml");
		context.close();
	}

}
