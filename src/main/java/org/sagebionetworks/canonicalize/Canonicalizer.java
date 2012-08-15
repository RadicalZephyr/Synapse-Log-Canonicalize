package org.sagebionetworks.canonicalize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.sagebionetworks.logging.SynapseEvent;
import org.sagebionetworks.logging.SynapseLoggingUtils;

/**
 * Hello world!
 *
 */
public class Canonicalizer {
    public static void main( String[] args )
    {

    }

	public void collateLogs(List<File> list) {
		ArrayList<BufferedReader> streamBuffers = new ArrayList<BufferedReader>();

		for (File file : list) {
			try {
				streamBuffers.add(new BufferedReader(new FileReader(file)));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		boolean done = false;

		SortedMap<SynapseEvent, BufferedReader> fileEventMap = new TreeMap<SynapseEvent, BufferedReader>();
		for (BufferedReader stream : streamBuffers) {
			try {
				String line = stream.readLine();
				SynapseEvent event = SynapseLoggingUtils.parseSynapseEvent(line);
				fileEventMap.put(event, stream);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		while (!done) {
			SynapseEvent firstKey = fileEventMap.firstKey();
			BufferedReader rdr = fileEventMap.get(firstKey);
			enhanceEvent(firstKey);
			writeEvent(firstKey);
			fileEventMap.remove(firstKey);

			try {
				String line = rdr.readLine();
				SynapseEvent next = SynapseLoggingUtils.parseSynapseEvent(line);
				fileEventMap.put(next, rdr);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
		}
	}

	private void writeEvent(SynapseEvent firstKey) {
		// TODO Auto-generated method stub

	}

	private void enhanceEvent(SynapseEvent firstKey) {
		// TODO Auto-generated method stub

	}
}
