package nars.guifx.graph2;

import automenta.vivisect.dimensionalize.IterativeLayout;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import nars.Global;
import nars.NAR;
import nars.concept.Concept;
import nars.guifx.NARfx;
import nars.guifx.Spacegraph;
import nars.guifx.demo.Animate;
import nars.term.Term;
import nars.util.data.random.XORShiftRandom;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static javafx.application.Platform.runLater;

/**
 * Created by me on 8/6/15.
 */
public class NARGraph1<V, E> extends Spacegraph {

    private Animate animator;

    private Animate updaterSlow;

    public NAR nar;
    private Timeline time;

    static final Random rng = new XORShiftRandom();
    final List<TermNode> termList = Global.newArrayList();

    int layoutPeriodMS = 75;
    int updatePeriodMS = 100;

    final AtomicBoolean
            nodeDirty = new AtomicBoolean(true),
            edgeDirty = new AtomicBoolean(true);


    final static Font nodeFont = NARfx.mono(0.5);

    public static VisModel visModel = new VisModel() {

        public Color getVertexColor(double priority, float conf) {
            // TODO color based on sub/super directionality of termlink(s) : e.getTermlinkDirectionality

            if (!Double.isFinite(conf)) {
                conf = 0;
            }

            return Color.hsb(250.0 + 75.0 * (conf),
                    0.10f + 0.85f * priority,
                    0.10f + 0.5f * priority);
        }

        public double getVertexScaleByPri(Concept c) {
            return (c != null ? c.getPriority() : 0);
        }

        public double getVertexScaleByConf(Concept c) {
            if (c.hasBeliefs()) {
                double conf = c.getBeliefs().getConfidenceMax(0, 1);
                if (Double.isFinite(conf)) return conf;
            }
            return 0;
        }

        @Override
        public double getVertexScale(Concept c) {

            return (c != null ? getVertexScaleByConf(c) : 0) * 0.75f + 0.25f;
            //return getVertexScaleByPri(c);
        }

        public Color getEdgeColor(double termMean, double taskMean) {
            // TODO color based on sub/super directionality of termlink(s) : e.getTermlinkDirectionality

            return Color.hsb(25.0 + 180.0 * (1.0 + (termMean - taskMean)),
                    0.95f,
                    Math.min(0.75f + 0.25f * (termMean + taskMean) / 2f, 1f)
                    //,0.5 * (termMean + taskMean)
            );

//            return new Color(
//                    0.5f + 0.5f * termMean,
//                    0,
//                    0.5f + 0.5f * taskMean,
//                    0.5f + 0.5f * (termMean + taskMean)/2f
//            );
        }

    };



    final Map<Term, TermNode> terms = new LinkedHashMap();


    private Consumer<NARGraph1> updater;
    private EdgeRenderer<E> edgeRenderer;




    /** assumes that 's' and 't' are already ordered */
    public final TermEdge getConceptEdgeOrdered(TermNode s, TermNode t) {
        return getEdge(s.term, t.term);
    }

    static boolean order(final Term x, final Term y) {
        final int i = x.compareTo(y);
        if (i == 0) throw new RuntimeException("order=0 but must be non-equal");
        return i < 0;
    }

    public TermEdge getEdge(Term a, Term b) {
        TermNode n = getTermNode(a);
        if (n != null) {
            return n.edge.get(b);
        }
        return null;
    }

    public final boolean addEdge(Term a, Term b, TermEdge e) {
        TermNode n = getTermNode(a);
        if (n != null) {
            return n.putEdge(b, e) == null;
        }
        return false;
    }

    public final TermNode getTermNode(final Term t) {
        return terms.get(t);
    }
    public final Consumer<NARGraph1> getUpdater() {
        return updater;
    }

    public final void setUpdater(Consumer<NARGraph1> u) {
        this.updater = u;
    }


    public void commit(final Collection<Term> active /* should probably be a set for fast .contains() */,
                        final TermNode[] x,
                        final TermEdge[] y) {
        runLater(() -> {

            if (x != null) {
                for (final TermNode tn : x)
                    terms.put(tn.term, tn);

                addNodes(x);
            }

            if (y != null) {
                for (final TermEdge te : y) {
                    addEdge(te.aSrc.term, te.bSrc.term, te);
                }
                addEdges(y);
            }

            List<TermNode> toDetach = Global.newArrayList();
            List<TermEdge> toDetachEdge = new ArrayList();

            getVertices().forEach(nn -> {
                if (!(nn instanceof TermNode)) return;

                TermNode r = (TermNode) nn;
                if (!active.contains(r)) {
                    TermNode c = terms.remove(r.term);

                    if (c != null) {
                        //c.setVisible(false);
                        toDetach.add(c);
                        if (c.edges!=null)
                            Collections.addAll(toDetachEdge, c.edges);
                    }
                }
            });

            removeNodes((Collection) toDetach);
            removeEdges((Collection)toDetachEdge);

            termList.clear();
            termList.addAll(terms.values());

            //print();

            updateNodes();

        });
    }

    public final void updateGraph() {


        if (!isVisible()) {
            return;
        }

        Consumer<NARGraph1> u = getUpdater();
        if (u!=null)
            u.accept(this);
        /*else
            System.err.println(this + " has no updater");*/
    }


    @FunctionalInterface
    public interface PreallocatedResultFunction<X, Y> {
        public void apply(X x, Y setResultHereAndReturnIt);
    }

    @FunctionalInterface
    public interface PairConsumer<A, B> {
        public void accept(A a, B b);
    }

    ;

    IterativeLayout<TermNode, TermEdge> layout = null;


    protected final void layoutNodes() {
        IterativeLayout<TermNode, TermEdge> l;
        if ((l = getLayout()) != null) {
            l.run(this, 1);
        }
        else {
            System.err.println(this + " has no layout");
        }
    }

    private final IterativeLayout<TermNode, TermEdge> getLayout() {
        return layout;
    }

    public void setLayout(IterativeLayout<TermNode, TermEdge> layout) {
        this.layout = layout;
    }


    protected void updateNodes() {
        if (termList != null)
            termList.forEach(n -> {
                if (n != null) n.update();
            });
    }

    final List<Object /*TermEdge*/> removable = Global.newArrayList();

    final Color FADEOUT = Color.BLACK;
    //new Color(0,0,0,0.5);

    public interface EdgeRenderer<E> extends Consumer<E> {
        /** called before any update begins */
        public void reset(NARGraph1 g);
    }

    public EdgeRenderer<E> getEdgeRenderer() {
        return edgeRenderer;
    }

    public void setEdgeRenderer(EdgeRenderer<E> r) {
        this.edgeRenderer = r;
    }

    protected void renderEdges() {

        EdgeRenderer er = getEdgeRenderer();
        if (er == null) {
            System.err.println(this + " has no edge renderer");
        }
        er.reset(this);


        Collections.addAll(removable, edges.getChildren());

        for (int i = 0, termListSize = termList.size(); i < termListSize; i++) {
            final TermNode n = termList.get(i);
            for (final TermEdge e : n.getEdges()) {
                er.accept(e);
                removable.remove(e);
            }
        }

        removable.forEach(x -> {
            edges.getChildren().remove(x);
        });

        removable.clear();

    }

    public final AtomicBoolean conceptsChanged = new AtomicBoolean(true);
    final Consumer<Concept> ifConceptsChanged = c -> {
        this.conceptsChanged.set(true);
    };

    public NARGraph1(NAR n) {
        super();


        this.nar = n
                //.stdout()
                //.stdoutTrace()
//                .input("<a --> b>. %1.00;0.7%", //$0.9;0.75;0.2$
//                        "<b --> c>. %1.00;0.7%")
                .onConceptActive(ifConceptsChanged)
                .onConceptForget(ifConceptsChanged)
                .onEachNthFrame(this::updateGraph, 1);
                /*.forEachCycle(() -> {
                    double[] dd = new double[4];
                    nar.memory.getControl().conceptPriorityHistogram(dd);
                    System.out.println( Arrays.toString(dd) );

                    System.out.println(
                            nar.memory.getActivePrioritySum(true, false, false) +
                            " " +
                            nar.memory.getActivePrioritySum(false, true, false) +
                            " " +
                            nar.memory.getActivePrioritySum(false, false, true)  );

                })*/


        visibleProperty().addListener(v -> {
            checkVisibility();
        });

        runLater(() -> {
            checkVisibility();
        });

    }

    protected void checkVisibility() {
        if (isVisible())
            start();
        else
            stop();
    }

    protected void start() {
        synchronized (nar) {
            if (this.animator == null) {

                this.animator = new Animate(layoutPeriodMS, a -> {
                    if (!termList.isEmpty()) {
                        layoutNodes();
                        renderEdges();
                    }
                });

                /*this.updaterSlow = new Animate(updatePeriodMS, a -> {
                    if (!termList.isEmpty()) {
                        layoutNodes();
                        renderEdges();
                    }
                });*/
                animator.start();
                //updaterSlow.start();
            }
        }
    }

    protected void stop() {
        synchronized (nar) {
            if (this.animator != null) {
                animator.stop();
                animator = null;
            }
        }
    }

    //    private class TermEdgeConsumer implements Consumer<TermEdge> {
//        private final Consumer<TermNode> updateFunc;
//        private final TermNode nodeToQuery;
//
//        public TermEdgeConsumer(Consumer<TermNode> updateFunc, TermNode nodeToQuery) {
//            this.updateFunc = updateFunc;
//            this.nodeToQuery = nodeToQuery;
//        }
//
//        @Override
//        public void accept(TermEdge te) {
//            if (te.isVisible())
//                updateFunc.accept(te.otherNode(nodeToQuery));
//        }
//    }
}
