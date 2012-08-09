package org.sagebionetworks.canonicalize;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SynapseEvent {

	private static final String DATE_PATTERN = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})";
	private static final String LEVEL_PATTERN = " \\[\\w+\\] - ";
	private static final String CONTROLLER_METHOD_PATTERN = "(?<controller>\\w+)/(?<method>\\w+)";
	private static final String PROPERTIES_PATTERN = "\\?(?<properties>(?:\\w+=[\\w%.\\-*_]+&?)+)$";
	
	private static final Pattern REGEX = Pattern.compile(DATE_PATTERN+LEVEL_PATTERN+CONTROLLER_METHOD_PATTERN+PROPERTIES_PATTERN); 
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZone(DateTimeZone.UTC);


	private DateTime timeStamp;
	private String controller, methodName;

	private int latency;
	private Map<String, String> properties;

	public SynapseEvent(String line) {
		Matcher matcher = REGEX.matcher(line);
		if (matcher.matches()) {
			this.timeStamp = DATE_FORMATTER.parseDateTime(matcher.group("date"));
			this.controller = matcher.group("controller");
			this.methodName = matcher.group("method");

			String[] properties = matcher.group("properties").split("&");
			this.properties = new HashMap<String, String>();
			
			for (String property : properties) {
				String[] keyAndVal = property.split("=", 2);
				this.properties.put(keyAndVal[0], keyAndVal[1]);
			}
			if (this.properties.containsKey("latency")) { 
				this.latency = Integer.parseInt(this.properties.get("latency"));
				this.properties.remove("latency");
			}
		} else {
			throw new IllegalArgumentException("Line does not represent a SynapseEVent.");
		}
	}

	public static DateTimeFormatter getDateformatter() {
		return DATE_FORMATTER;
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
