package org.sagebionetworks.canonicalize;

import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.junit.*;

public class SynapseEventTest {

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

		for (Entry<String, String> entry : propMap.entrySet()) {
			builder.append("&");
			builder.append(entry.getKey());
			builder.append("=");
			builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		String line = builder.toString();
		SynapseEvent event = new SynapseEvent(line);

		DateTimeFormatter dateFormatter = SynapseEvent.getDateformatter();
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
