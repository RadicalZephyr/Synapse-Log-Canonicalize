package org.sagebionetworks.canonicalize;

import static org.junit.Assert.*;

import org.junit.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit test for simple Canonicalizer.
 */
public class CanonicalizerTest {

	Canonicalizer canon;

	@Before
	public void setup() {
		canon = new Canonicalizer();
	}

	@Test
    public void testCollateLogs() {
		List<File> list = new ArrayList<File>();

		canon.collateLogs(list);
    }
}
