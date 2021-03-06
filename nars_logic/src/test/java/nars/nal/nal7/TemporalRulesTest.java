package nars.nal.nal7;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by me on 6/5/15.
 */
public class TemporalRulesTest {

    @Test
    public void testAfter() {

        assertTrue("after", TemporalRules.after(1, 4, 1));

        assertFalse("concurrent (equivalent)", TemporalRules.after(4, 4, 1));
        assertFalse("before", TemporalRules.after(6, 4, 1));
        assertFalse("concurrent (by duration range)", TemporalRules.after(3, 4, 3));

    }

}
