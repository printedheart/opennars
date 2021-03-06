package nars.concept;

import nars.NAR;
import nars.io.out.TextOutput;
import nars.nar.experimental.Equalized;
import org.junit.Test;

/**
 * Created by me on 9/9/15.
 */
public class ActivationTest {

    @Test
    public void testDerivedBudgets() {
        NAR n= new Equalized(10, 1, 3);

        TextOutput.out(n);


        n.input("$0.1$ <a --> b>.");
        n.input("$0.1$ <b --> a>.");
        n.frame(15);


        for (Concept concept : n.memory.concepts) {
            System.out.println(concept);
        }
    }
}
