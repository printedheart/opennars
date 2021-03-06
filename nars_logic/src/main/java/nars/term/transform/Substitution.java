package nars.term.transform;

import com.gs.collections.impl.map.mutable.UnifiedMap;
import nars.term.Compound;
import nars.term.Term;
import nars.term.Variable;

import java.util.Map;
import java.util.function.Function;

import static java.util.Arrays.copyOf;

/** holds a substitution and any metadata that can eliminate matches as early as possible */
public class Substitution<C extends Compound> implements Function<C,Term> {
    final Map<Term, Term> subs;

    public int minMatchVolume/*, maxMatchVolume*/;
    final int numSubs;

    final int numDep, numIndep, numQuery;


    /** creates a substitution of one variable; more efficient than supplying a Map */
    public Substitution(Term termFrom, Term termTo) {
        this(UnifiedMap.newWithKeysValues(termFrom, termTo));
    }

    public Substitution(final Map<Term, Term> subs) {

        this.subs = subs;

        final int numSubs = this.numSubs = subs.size();
        if (numSubs == 0) {
            throw new RuntimeException("Empty substitution");
        }

        int numDep = 0, numIndep = 0, numQuery = 0;

        int minMatchVolume = Integer.MAX_VALUE,
            maxMatchVolume = Integer.MIN_VALUE;

        for (final Map.Entry<Term,Term> e : subs.entrySet()) {

            final Term m = e.getKey();

            final int v = m.volume();
            if (minMatchVolume > v) minMatchVolume = v;
            if (maxMatchVolume < v) maxMatchVolume = v;

            if (m instanceof Variable) {
                switch (m.op()) {
                    case VAR_DEPENDENT: numDep++; break;
                    case VAR_INDEPENDENT: numIndep++; break;
                    case VAR_QUERY: numQuery++; break;
                }
            }

//        /* collapse a substitution map to each key's ultimate destination
//         *  in the case of values that are equal to other keys */
//            if (numSubs >= 2) {
//                final Term o = e.getValue(); //what the original mapping of this entry's key
//
//                Term k = o, prev = o;
//                int hops = 1;
//                while ((k = subs.getOrDefault(k, k)) != prev) {
//                    prev = k;
//                    if (hops++ == numSubs) {
//                        //cycle detected
//                        throw new RuntimeException("Cyclical substitution map: " + subs);
//                    }
//                }
//                if (!k.equals(o)) {
//                    //replace with the actual final mapping
//                    e.setValue(k);
//                }
//            }

        }

        this.numDep = numDep;
        this.numIndep = numIndep;
        this.numQuery = numQuery;

        this.minMatchVolume = minMatchVolume;
        //this.maxMatchVolume = maxMatchVolume;
    }


    /** if eliminates all conditions with regard to a specific compound */
    public final boolean impossible(final Term superterm) {

        if (superterm instanceof Compound) {
            if (superterm.impossibleSubTermOrEqualityVolume(minMatchVolume)) {
                //none of the subs could possibly fit inside or be equal to the superterm
                return true;
            }
        }

        int subsApplicable = numSubs;

        if (numDep > 0 && !superterm.hasVarDep()) {
            subsApplicable -= numDep;
            if (subsApplicable <= 0)
                return true;
        }

        if (numIndep > 0 && !superterm.hasVarIndep())
            subsApplicable -= numIndep;
            if (subsApplicable <= 0)
                return true;

        if (numQuery > 0 && !superterm.hasVarQuery())
            subsApplicable -= numQuery;
            if (subsApplicable <= 0)
                return true;


        //there exist variables that can match, and the term can theoretically equal or contain it
        return false;

    }


    final public Term get(final Term t) {
        return subs.get(t);
    }

    @Override
    public Term apply(final C t) {
        if (impossible(t))
            return t;


        final Term[] in = t.term;
        Term[] out = in;

        final int subterms = in.length;

        final int minVolumeOfMatch = minMatchVolume;


        for (int i = 0; i < subterms; i++) {
            final Term t1 = in[i];

            if (t1.volume() < minVolumeOfMatch) {
                //too small to be any of the keys or hold them in a subterm
                continue;
            }

            Term t2;
            if ((t2 = get(t1))!=null) {


                //prevents infinite recursion
                if (!t2.containsTerm(t1)) {
                    if (out == in) out = copyOf(in, subterms);
                    out[i] = t2; //t2.clone();
                }

            } else if (t1 instanceof Compound) {

                //additional constraint here?

                Term t3 = apply((C) t1);
                if ((t3 != null) && (!t3.equals(t1 /* in[i]*/))) {
                    //modification
                    if (out == in) out = copyOf(in, subterms);
                    out[i] = t3;
                }
            }
        }

        if (out == in) //nothing changed
            return t;

        Term s = t.clone(out);

        return s;
    }

    @Override
    public String toString() {
        return "Substitution{" +
                "subs=" + subs +
                ", minMatchVolume=" + minMatchVolume +
                ", numSubs=" + numSubs +
                ", numDep=" + numDep +
                ", numIndep=" + numIndep +
                ", numQuery=" + numQuery +
                '}';
    }
}
