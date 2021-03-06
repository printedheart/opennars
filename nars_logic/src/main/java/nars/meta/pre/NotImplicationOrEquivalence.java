package nars.meta.pre;

import nars.Op;
import nars.meta.RuleMatch;
import nars.term.Term;


final public class NotImplicationOrEquivalence extends PreCondition1 {

    public NotImplicationOrEquivalence(final Term arg1) {
        super(arg1);
    }

    @Override
    public boolean test(final RuleMatch m, final Term arg1) {

        if (arg1 == null) return false;

        final Op o = arg1.op();
        switch (o) {
            case IMPLICATION:
            case IMPLICATION_AFTER:
            case IMPLICATION_BEFORE:
            case IMPLICATION_WHEN:
            case EQUIVALENCE:
            case EQUIVALENCE_AFTER:
            case EQUIVALENCE_WHEN:
                return false;
        }
        return true;
    }

}
