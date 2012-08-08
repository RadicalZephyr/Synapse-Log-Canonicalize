package org.sagebionetworks.canonicalize;

import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SynapseEvent {

	public static final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss,SSS").withZone(DateTimeZone.UTC);

	DateTime timeStamp;
	String controller, methodName, latency;
	Map<String, String> properties;

	public SynapseEvent(String line) {
		String[] parts = line.split("\\[.*?\\]");
		timeStamp = dateFormatter.parseDateTime(parts[0].trim());
	}

}
