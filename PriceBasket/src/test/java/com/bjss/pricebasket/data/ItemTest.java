package com.bjss.pricebasket.data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bjss.pricebasket.service.mock.MockMsgService;

public class ItemTest {

	@Test
	public void testDisplayName() {
		Item item = new Item();
		MockMsgService mockMsgService = new MockMsgService();
		mockMsgService.addMessage("item.name.testitem", "Test Item");
		item.msgService = mockMsgService;
		item.setId("testitem");
		assertEquals("Test Item", item.getDisplayName());
	}

}
