package com.bjss.pricebasket.parser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Registers the parsers that handle initialisation of custom Bean Definition
 * Parsers
 * 
 * @author Leon Danser
 * 
 */
public class NamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("item", new ItemDefinitionParser());
		registerBeanDefinitionParser("percentageOffer",
				new PercentageOfferDefinitionParser());
	}

}
