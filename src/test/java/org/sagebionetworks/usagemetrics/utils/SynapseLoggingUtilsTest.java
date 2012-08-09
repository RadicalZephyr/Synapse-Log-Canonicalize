package org.sagebionetworks.usagemetrics.utils;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.sagebionetworks.canonicalize.SynapseEvent;

public class SynapseLoggingUtilsTest {

	@Test
	public void testParseSynapseEvent() throws UnsupportedEncodingException {
		String timeStamp = "2012-08-06 18:36:22,961";

		String controller = "AccessRequirementController";
		String method = "getUnfulfilledAccessRequirement";

		StringBuilder builder = new StringBuilder(String.format(
				"%s [TRACE] - %s/%s?", timeStamp, controller, method));

		String latencyLabel = "latency";
		int latencyValue = 6;
		builder.append(String.format("%s=%d", latencyLabel, latencyValue));

		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("userId", "Geoff.Shannon@sagebase.org");
		propMap.put("entityId", "syn114138");
		propMap.put("request",
				"org.sagebionetworks.authutil.ModParamHttpServletRequest@5cba727");
		propMap.put("header", "header=%7Bsessiontoken%3D%5B0BMBOTJ7Wvvhz0Y4d1RMnw00%5D%2C+content-type%3D%5Bapplication%2Fjson%5D%2C+accept%3D%5Bapplication%2Fjson%5D%2C+content-length%3D%5B92%5D%2C+host%3D%5Blocalhost%3A8080%5D%2C+connection%3D%5BKeep-Alive%5D%7D");
		for (Entry<String, String> entry : propMap.entrySet()) {
			builder.append("&");
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		String line = builder.toString();
		SynapseEvent event = SynapseLoggingUtils.parseSynapseEvent(line);

		DateTimeFormatter dateFormatter = SynapseLoggingUtils.DATE_FORMATTER;
		DateTime time = dateFormatter.parseDateTime(timeStamp);

		assertEquals(time, event.getTimeStamp());
		assertEquals(latencyValue, event.getLatency());
		assertEquals(controller, event.getController());
		assertEquals(method, event.getMethodName());

		for (String key : propMap.keySet()) {
			assertEquals(propMap.get(key), URLDecoder.decode(event.getProperties().get(key), "UTF-8"));
		}
	}

}
