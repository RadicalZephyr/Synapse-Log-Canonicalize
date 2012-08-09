package org.sagebionetworks.usagemetrics.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.sagebionetworks.canonicalize.SynapseEvent;

public class SynapseLoggingUtils {

	private static final String DATE_PATTERN = "^(?<date>\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2},\\d{3})";
	private static final String LEVEL_PATTERN = " \\[\\w+\\] - ";
	private static final String CONTROLLER_METHOD_PATTERN = "(?<controller>\\w+)/(?<method>\\w+)";
	private static final String PROPERTIES_PATTERN = "\\?(?<properties>(?:\\w+=[\\w%.\\-*_]+&?)+)$";

	public static final Pattern LOGFILE_REGEX = Pattern.compile(DATE_PATTERN+LEVEL_PATTERN+CONTROLLER_METHOD_PATTERN+PROPERTIES_PATTERN);
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZone(DateTimeZone.UTC);

	public static SynapseEvent parseSynapseEvent(String line) throws UnsupportedEncodingException {
		Matcher matcher = LOGFILE_REGEX.matcher(line);

		DateTime timeStamp;
		String controller, methodName;
		int latency;
		HashMap<String, String> properties;

		if (matcher.matches()) {
			timeStamp = DATE_FORMATTER.parseDateTime(matcher.group("date"));
			controller = matcher.group("controller");
			methodName = matcher.group("method");

			String[] propertiesArray = matcher.group("properties").split("&");
			properties = new HashMap<String, String>();

			for (String property : propertiesArray) {
				String[] keyAndVal = property.split("=", 2);
				properties.put(keyAndVal[0], URLDecoder.decode(keyAndVal[1], "UTF-8"));
			}
			if (properties.containsKey("latency")) {
				latency = Integer.parseInt(properties.get("latency"));
				properties.remove("latency");
			} else {
				latency = -1;
			}
		} else {
			throw new IllegalArgumentException("Line does not represent a SynapseEVent.");
		}
		return new SynapseEvent(timeStamp, controller, methodName, latency, properties);
	}
}
