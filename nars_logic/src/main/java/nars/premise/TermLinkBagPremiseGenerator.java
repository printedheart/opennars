//package nars.premise;
//
//import nars.bag.tx.ParametricBagForgetting;
//import nars.concept.Concept;
//import nars.link.TaskLink;
//import nars.link.TermLink;
//import nars.link.TermLinkKey;
//
//import javax.annotation.Nullable;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.function.Function;
//
///**
// * Permits any premise to be selected, unfiltered, as decided by the Concept's bag
// * Includes parameter for how many re-attempts in case no termlink was
// * provided by the bag, or if the provided termlink was invalid.
// *
// * Not thread safe
// */
//public class TermLinkBagPremiseGenerator extends ParametricBagForgetting<TermLinkKey,TermLink> implements PremiseGenerator, Function<TermLink, ParametricBagForgetting.ForgetAction> {
//
//    public final AtomicInteger maxSelectionAttempts;
//    private Concept currentConcept;
//    private TaskLink currentTaskLink;
//
//
//    public TermLinkBagPremiseGenerator(AtomicInteger maxSelectionAttempts) {
//        super();
//        this.maxSelectionAttempts = maxSelectionAttempts;
//        setModel(this);
//
//    }
//
//    public long time() { return currentConcept.time(); }
//
//    /** a general condition */
//    public boolean valid(final TermLink term, final TaskLink task) {
//        return !(term.getTerm().equals(task.getTerm())
//                && !task.getBudget().isDeleted()
//        );
//    }
//
//
//    @Override
//    final public void setConcept(Concept c) {
//        this.currentConcept = c;
//    }
//
//    @Override
//    final public ForgetAction apply(final TermLink termLink) {
//
//        if (valid(termLink, currentTaskLink)) {
//            return ParametricBagForgetting.ForgetAction.SelectAndForget;
//        }
//        else {
//            return ParametricBagForgetting.ForgetAction.IgnoreAndForget;
//        }
//
//    }
//
//    @Override
//    public @Nullable TermLink[] nextTermLinks(final Concept c, final TaskLink taskLink, TermLink[] result) {
//
//
//        final int attempting = getMaxAttempts(c);
//        if (attempting == 0) return null;
//
//
//
//        this.currentConcept = c;
//        this.currentTaskLink = taskLink;
//        set(c.getMemory().termLinkForgetDurations.floatValue(), c.getMemory().time());
//
//
//        ////protected int update(BagTransaction<K, V> tx, V[] batch, int start, int stop, int maxAdditionalAttempts) {
//
//        //int n = c.getTermLinks().forgetNext(this, result, attempting);
//
//        ////onSelect(c, lastForgotten != null, n);
//
//
//        //HACK
//        result[0] = c.getTermLinks().forgetNext(c.getMemory().termLinkForgetDurations.floatValue(), c.getMemory());
//
//        return result;
//    }
//
//
////    public @Nullable TermLink nextTermLinkOLD(final Concept c, final TaskLink taskLink) {
////
////        final int attempting = getMaxAttempts(c);
////        if (attempting == 0) return null;
////
////        int r = attempting;
////
////        this.currentConcept = c;
////        this.currentTaskLink = taskLink;
////        set(c.getMemory().param.termLinkForgetDurations.floatValue(), c.getMemory().time());
////
////        while (r > 0) {
////
////            r--;
////
////            c.getTermLinks().update(this);
////            if (lastForgotten != null)
////                break;
////
////        }
////
////        onSelect(c, lastForgotten != null, attempting - r);
////
////        return lastForgotten;
////    }
//
////    /** for statistics and tuning purposes in subclasses */
////    final protected void onSelect(final Concept c, final boolean foundSomething, final int attempts) {
////
////        /*
////        int s = c.getTermLinks().size();
////        System.out.println(c + " termlinks avail=" + s +
////                    " found=" + foundSomething + " attempts=" + attempts);
////        */
////
////    }
////
//    protected int getMaxAttempts(final Concept c) {
//        int termlinks = c.getTermLinks().size();
//
//        if (termlinks == 0)
//            return 0;
//
//        if (termlinks == 1)
//            return 1;
//
//        //TODO use # of termlinks as a guesstimate
//
//        return maxSelectionAttempts.get();
//    }
//
//}
