package com.bjss.pricebasket.parser;

import java.math.BigDecimal;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

import com.bjss.pricebasket.data.Item;

/**
 * Responsible for parsing custom Spring config definition for {@link Item}s.
 * 
 * eg. <item id="apple" price="1.00" />
 * 
 * @author Leon Danser
 * 
 */
public class ItemDefinitionParser extends AbstractSingleBeanDefinitionParser {

	private static final String ID = "id";
	private static final String PRICE = "price";

	@Override
	protected Class<Item> getBeanClass(Element el) {
		return Item.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		bean.addPropertyValue(ID, element.getAttribute(ID).toLowerCase());
		String priceString = element.getAttribute(PRICE);
		bean.addPropertyValue(PRICE, new BigDecimal(priceString));
	}

}
