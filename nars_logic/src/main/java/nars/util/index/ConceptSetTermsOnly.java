package nars.util.index;

import nars.NAR;
import nars.concept.Concept;
import nars.term.Term;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** similar to ConceptSet except Concepts are not stored, useful as a pass-through */
abstract public class ConceptSetTermsOnly<T extends Term> extends MutableConceptMap<T> implements Iterable<T> {

    public final Set<T> values = new HashSet();


    public ConceptSetTermsOnly(NAR nar) {
        super(nar);
    }

    @Override
    public Iterator<T> iterator() {
        return values.iterator();
    }

    @Override
    public boolean include(Concept c) {
        values.add((T)c.getTerm());
        return true;
    }
    @Override
    public boolean exclude(Concept c) {
        return values.remove((T)c.getTerm());
    }
    public boolean exclude(Term t) {
        return true;
    }


    @Override
    public boolean contains(final T t) {
        if (!values.contains(t)) {
            return super.contains(t);
        }
        return true;
    }


    /** set a term to be present always in this map, even if the conept disappears */
    @Override
    public void include(T a) {
        super.include(a);
        values.add(a);
    }

    /** remove an inclusion, and/or add an exclusion */
    //TODO public void exclude(Term a) { }

    public int size() {
        return values.size();
    }


}
