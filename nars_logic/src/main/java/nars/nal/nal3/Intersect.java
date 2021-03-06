package nars.nal.nal3;

import nars.term.Compound;
import nars.term.Term;

/**
 * Common parent class for IntersectInt and IntersectExt
 */
abstract public class Intersect extends Compound {

    public Intersect(Term[] arg) {
        super(arg);
    }

    /**
     * Check if the compound is communitative.
     * @return true for communitative
     */
    @Override
    public final boolean isCommutative() {
        return true;
    }

}
