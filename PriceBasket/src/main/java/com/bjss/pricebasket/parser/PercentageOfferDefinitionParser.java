package com.bjss.pricebasket.parser;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.config.BeanReference;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bjss.pricebasket.data.PercentageOffer;

/**
 * Responsible for parsing custom Spring config definition for
 * {@link PercentageOffer}s.
 * 
 * eg.
 * 
 * <pre>
 * &lt;percentageOffer id="breadOffer" itemRef="bread" discount="0.5" expiryDate="2014-03-01">
 *  &lt;requiredItems>
 * 	&lt;itemRef ref="soup" quantity="2" />
 *  &lt;/requiredItems>
 * &lt;/percentageOffer>
 * </pre>
 * 
 * @author Leon Danser
 * 
 */
public class PercentageOfferDefinitionParser extends
		AbstractSingleBeanDefinitionParser {

	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String ITEM = "item";
	private static final String ID = "id";
	private static final String DISCOUNT = "discount";
	private static final String ITEM_REF = "itemRef";
	private static final String REQUIRED_ITEMS = "requiredItems";
	private static final String REF = "ref";
	private static final String QUANTITY = "quantity";
	private static final String EXPIRY_DATE = "expiryDate";

	@Override
	protected Class<PercentageOffer> getBeanClass(Element el) {
		return PercentageOffer.class;
	}

	@Override
	protected void doParse(Element element, BeanDefinitionBuilder bean) {
		bean.addPropertyValue(ID, element.getAttribute(ID));
		String discountString = element.getAttribute(DISCOUNT);
		bean.addPropertyValue(DISCOUNT, new BigDecimal(discountString));
		bean.addPropertyReference(ITEM, element.getAttribute(ITEM_REF));
		bean.addPropertyValue(REQUIRED_ITEMS, parseRequiredItems(element));
		if (element.hasAttribute(EXPIRY_DATE)) {
			Date expiryDate = parseDate(element.getAttribute(EXPIRY_DATE));
			bean.addPropertyValue(EXPIRY_DATE, expiryDate);
		}

	}

	private Date parseDate(String expiryString) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
			return dateFormat.parse(expiryString);
		} catch (ParseException e) {
			throw new RuntimeException(String.format(
					"Could not parse the date [%s]. Should use the format %s",
					expiryString, DATE_FORMAT), e);
		}
	}

	private Map<BeanReference, Integer> parseRequiredItems(Element element) {
		Map<BeanReference, Integer> itemMap = new ManagedMap<BeanReference, Integer>();
		Element listDefintion = DomUtils.getChildElementByTagName(element,
				REQUIRED_ITEMS);
		if (listDefintion != null) {
			NodeList items = listDefintion.getChildNodes();
			for (int i = 0; i < items.getLength(); i++) {
				Node node = items.item(i);
				if (ITEM_REF.equals(node.getNodeName())) {
					String ref = ((Element) node).getAttribute(REF);
					String quantityString = ((Element) node)
							.getAttribute(QUANTITY);
					RuntimeBeanReference beanRef = new RuntimeBeanReference(ref);
					int quantity = Integer.parseInt(quantityString);
					if (quantity < 0) {
						throw new IllegalArgumentException(
								String.format(
										"Quantity for requiredItem [%s] must be a positive number",
										ref));
					}
					itemMap.put(beanRef, quantity);
				}
			}
		}
		return itemMap;
	}

}
