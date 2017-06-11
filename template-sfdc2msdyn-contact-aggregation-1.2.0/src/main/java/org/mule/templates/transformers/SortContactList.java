/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.templates.transformers;


import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import com.google.common.collect.Lists;

/**
 * This transformer will take to list as input and create a third one that will be the merge of the previous two. The identity of an element of the list is
 * defined by its email.
 * 
 * @author
 */
public class SortContactList extends AbstractMessageTransformer {

	public static Comparator<Map<String, String>> recordComparator = new Comparator<Map<String, String>>() {

		public int compare(Map<String, String> contact1, Map<String, String> contact2) {

			String key1 = buildKey(contact1);
			String key2 = buildKey(contact2);

			return key1.compareTo(key2);
		}

		private String buildKey(Map<String, String> contact) {
			StringBuilder key = new StringBuilder();

			if (StringUtils.isNotBlank(contact.get("IDInSalesforce")) && StringUtils.isNotBlank(contact.get("IDInMsDynamics"))) {
				key.append("~~");
				key.append(contact.get("IDInSalesforce"));
				key.append(contact.get("IDInMsDynamics"));
				key.append(contact.get("Email"));
			}

			if (StringUtils.isNotBlank(contact.get("IDInSalesforce")) && StringUtils.isBlank(contact.get("IDInMsDynamics"))) {
				key.append(contact.get("IDInSalesforce"));
				key.append("~");
				key.append(contact.get("Email"));
			}

			if (StringUtils.isBlank(contact.get("IDInSalesforce")) && StringUtils.isNotBlank(contact.get("IDInMsDynamics"))) {
				key.append("~");
				key.append(contact.get("IDInMsDynamics"));
				key.append(contact.get("Email"));
			}

			return key.toString();
		}

	};

	@SuppressWarnings("unchecked")
	@Override
	public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

		List<Map<String, String>> sortedContactsList = Lists.newArrayList((Iterator<Map<String, String>>) message.getPayload());

		Collections.sort(sortedContactsList, recordComparator);

		return sortedContactsList;

	}

}
