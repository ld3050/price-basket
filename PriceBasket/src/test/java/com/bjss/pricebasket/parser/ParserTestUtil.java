package com.bjss.pricebasket.parser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.util.ClassUtils;
import org.w3c.dom.Document;

public class ParserTestUtil {

	private final Class<?> type;

	public ParserTestUtil(Class<?> type) {
		this.type = type;
	}

	public InputStream getFileAsInputStream(String name) {
		return type.getResourceAsStream(ClassUtils.getShortName(type) + "#"
				+ name);
	}

	public Document getDocument(String name) throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		return docBuilder.parse(getFileAsInputStream(name));
	}
}
