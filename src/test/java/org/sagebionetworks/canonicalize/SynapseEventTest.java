package org.sagebionetworks.canonicalize;

import static org.junit.Assert.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.*;

public class SynapseEventTest {

	@Test
	public void testParseSynapseEvent() {
		String timeStamp = "2012-08-06 18:36:22,961";

		String line = timeStamp+" [TRACE] - "+"AccessRequirementController"+"/"+"getUnfulfilledAccessRequirement"+
		"?latency=6&userId=Geoff.Shannon%40sagebase.org&entityId=syn114138&request=org.sagebionetworks.authutil.ModParamHttpServletRequest%405cba727";

		SynapseEvent event = new SynapseEvent(line);


		DateTimeFormatter dateFormatter = SynapseEvent.dateFormatter;

		DateTime time = dateFormatter.parseDateTime(timeStamp);

		assertEquals(time, event.timeStamp);
	}
}
