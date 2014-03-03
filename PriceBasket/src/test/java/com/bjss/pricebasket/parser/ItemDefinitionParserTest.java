package com.bjss.pricebasket.parser;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Document;

public class ItemDefinitionParserTest {

	private ParserTestUtil testUtil;
	private ItemDefinitionParser parser;
	private BeanDefinitionBuilder bean;

	@Before
	public void setUp() {
		testUtil = new ParserTestUtil(this.getClass());
		parser = new ItemDefinitionParser();
		bean = BeanDefinitionBuilder.rootBeanDefinition(parser
				.getBeanClass(null));
	}

	@Test
	public void testParseValidItem() throws Exception {
		Document document = testUtil.getDocument("validItem.xml");

		parser.doParse(document.getDocumentElement(), bean);

		AbstractBeanDefinition item = bean.getBeanDefinition();

		assertEquals("testitem1",
				item.getPropertyValues().getPropertyValue("id").getValue());
		assertEquals(new BigDecimal("12.50"),
				item.getPropertyValues().getPropertyValue("price").getValue());
	}

}
