/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.mule.DefaultMuleEvent;
import org.mule.api.MuleEvent;
import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.api.routing.AggregationContext;
import org.mule.routing.AggregationStrategy;

import com.google.common.collect.Lists;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two. The identity of an element of the list is defined by its email.
 */
public class ContactMergeAggregationStrategy implements AggregationStrategy {
	
	@Override
	public MuleEvent aggregate(AggregationContext context) throws MuleException {
		List<MuleEvent> muleEventsWithoutException = context.collectEventsWithoutExceptions();
		int muleEventsWithoutExceptionCount = muleEventsWithoutException.size();
		
		// there have to be exactly 2 sources (A and B)
		if (muleEventsWithoutExceptionCount != 2) {
			throw new IllegalArgumentException("There have to be exactly 2 sources (A and B).");
		}
		
		MuleEvent muleEvent = muleEventsWithoutException.get(0);
		MuleMessage muleMessage = muleEvent.getMessage();
		
		List<Map<String, String>> listA = getContactsList(muleEventsWithoutException, 0);
		List<Map<String, String>> listB = getContactsList(muleEventsWithoutException, 1);
		
		// events are ordered so the event index corresponds to the index of each route
		ContactMerger sfdcContactMerge = new ContactMerger();
		List<Map<String, String>> mergedContactList = sfdcContactMerge.mergeList(listA, listB);
		
		muleMessage.setPayload(mergedContactList.iterator());
		
		return new DefaultMuleEvent(muleMessage, muleEvent);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Map<String, String>> getContactsList(List<MuleEvent> events, int index) {
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		if(events.get(index).getMessage().getPayload() instanceof Iterator){
			Iterator it = (Iterator) events.get(index).getMessage().getPayload();
			list = Lists.newArrayList(it);
		} else {
			List<Map<String, String>> lst = (List<Map<String, String>>) events.get(index).getMessage().getPayload();
			list = lst;
		}
		return list;
	}

}
