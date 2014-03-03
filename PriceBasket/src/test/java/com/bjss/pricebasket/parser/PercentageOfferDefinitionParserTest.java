package com.bjss.pricebasket.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Document;

public class PercentageOfferDefinitionParserTest {

	private ParserTestUtil testUtil;
	private PercentageOfferDefinitionParser parser;
	private BeanDefinitionBuilder bean;

	@Before
	public void setUp() {
		testUtil = new ParserTestUtil(this.getClass());
		parser = new PercentageOfferDefinitionParser();
		bean = BeanDefinitionBuilder.rootBeanDefinition(parser
				.getBeanClass(null));
	}

	@Test
	public void testParseOfferWithExpiry() throws Exception {
		Document document = testUtil.getDocument("offerWithExpiry.xml");

		parser.doParse(document.getDocumentElement(), bean);

		AbstractBeanDefinition percentageOffer = bean.getBeanDefinition();

		assertEquals("testOffer", percentageOffer.getPropertyValues()
				.getPropertyValue("id").getValue());
		assertEquals("testItem1",
				((RuntimeBeanReference) percentageOffer.getPropertyValues()
						.getPropertyValue("item").getValue()).getBeanName());
		assertEquals(new BigDecimal("0.5"), percentageOffer.getPropertyValues()
				.getPropertyValue("discount").getValue());

		assertEquals(getDate("2014-03-01"), percentageOffer.getPropertyValues()
				.getPropertyValue("expiryDate").getValue());
	}

	private Date getDate(String dateString) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return dateFormat.parse(dateString);
	}

	@Test
	public void testParseOfferWithRequiredItems() throws Exception {
		Document document = testUtil.getDocument("offerWithRequiredItems.xml");

		parser.doParse(document.getDocumentElement(), bean);

		AbstractBeanDefinition percentageOffer = bean.getBeanDefinition();

		assertEquals("testOffer", percentageOffer.getPropertyValues()
				.getPropertyValue("id").getValue());
		assertEquals("testItem1",
				((RuntimeBeanReference) percentageOffer.getPropertyValues()
						.getPropertyValue("item").getValue()).getBeanName());
		assertEquals(new BigDecimal("0.5"), percentageOffer.getPropertyValues()
				.getPropertyValue("discount").getValue());

		assertEquals(getDate("2014-03-01"), percentageOffer.getPropertyValues()
				.getPropertyValue("expiryDate").getValue());

		@SuppressWarnings("unchecked")
		Map<BeanReference, Integer> requiredItems = ((Map<BeanReference, Integer>) percentageOffer
				.getPropertyValues().getPropertyValue("requiredItems")
				.getValue());

		assertEquals(2, requiredItems.size());
		Iterator<Entry<BeanReference, Integer>> itemIt = requiredItems
				.entrySet().iterator();
		Entry<BeanReference, Integer> requiredItem0 = itemIt.next();
		Entry<BeanReference, Integer> requiredItem1 = itemIt.next();

		assertEquals("testItem2", requiredItem0.getKey().getBeanName());
		assertEquals(new Integer(5), requiredItem0.getValue());
		assertEquals("testItem3", requiredItem1.getKey().getBeanName());
		assertEquals(new Integer(1), requiredItem1.getValue());
	}
	
	@Test
	public void testParseOfferWithNegativeQuantity() throws Exception {
		Document document = testUtil.getDocument("negativeQuantity.xml");

		try {
			parser.doParse(document.getDocumentElement(), bean);
			fail();
		} catch (Exception e) {
			// expected
		}
	}

}
