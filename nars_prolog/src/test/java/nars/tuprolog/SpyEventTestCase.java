package nars.tuprolog;

import junit.framework.TestCase;
import nars.tuprolog.event.SpyEvent;

public class SpyEventTestCase extends TestCase {
	
	public void testToString() throws InvalidLibraryException {
		String msg = "testConstruction";
		SpyEvent e = new SpyEvent(new Prolog(), msg);
		assertEquals(msg, e.toString());
	}

}
