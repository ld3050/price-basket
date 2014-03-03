package com.bjss.pricebasket.service.impl;

import java.util.Locale;

import javax.annotation.Resource;
import javax.inject.Named;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.bjss.pricebasket.service.MsgService;

/**
 * This class acts as a wrapper for the Spring injected MessageSource resource
 * bundle. This service uses the System configured locale
 * 
 * @author Leon Danser
 * 
 */
@Named
public class MsgServiceImpl implements MsgService {

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Override
	public String getMessage(String key, Object... params) {
		Locale locale = LocaleContextHolder.getLocale();
		return messageSource.getMessage(key, params, locale);
	}

}
