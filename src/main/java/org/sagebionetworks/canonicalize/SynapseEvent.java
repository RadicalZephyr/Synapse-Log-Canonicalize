package org.sagebionetworks.canonicalize;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SynapseEvent {

	private static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZone(DateTimeZone.UTC);

	private DateTime timeStamp;
	private String controller, methodName;

	private int latency;
	private Map<String, String> properties;

	public SynapseEvent(String line) {
		// The first three splits are limited to 2 because we only want the first match.
		// Ideally though, there shouldn't be more than one match for any of these patterns
		String[] parts = line.split("\\[.*?\\] - ", 2);
		timeStamp = dateFormatter.parseDateTime(parts[0].trim());

		String[] controllerAndRest = parts[1].trim().split("/", 2);
		controller = controllerAndRest[0];

		String[] methodAndRest = controllerAndRest[1].trim().split("\\?", 2);
		methodName = methodAndRest[0];

		// Don't limit this split, because we don't know how many params we have
		String[] params = methodAndRest[1].split("&");
		this.properties = new HashMap<String, String>(params.length);

		for (String param : params) {
			String[] keyVal = param.split("=");
			if (!keyVal[0].equalsIgnoreCase("latency")) {
				properties.put(keyVal[0], keyVal[1]);
			} else {
				latency = Integer.parseInt(keyVal[1]);
			}
		}
	}

	public static DateTimeFormatter getDateformatter() {
		return dateFormatter;
	}

	public DateTime getTimeStamp() {
		return timeStamp;
	}

	public String getController() {
		return controller;
	}

	public String getMethodName() {
		return methodName;
	}

	public int getLatency() {
		return latency;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

}
