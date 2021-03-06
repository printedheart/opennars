//package nars.guifx;
//
//import com.gs.collections.impl.set.mutable.UnifiedSet;
//import javafx.application.Platform;
//import javafx.beans.value.ChangeListener;
//import javafx.beans.value.ObservableValue;
//import javafx.geometry.Orientation;
//import javafx.scene.Group;
//import javafx.scene.Node;
//import javafx.scene.control.Label;
//import javafx.scene.control.SplitPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.FlowPane;
//import javafx.scene.paint.Color;
//import javafx.scene.paint.PhongMaterial;
//import javafx.scene.shape.Box;
//import nars.NAR;
//import nars.bag.Bag;
//import nars.budget.Itemized;
//import nars.concept.Concept;
//import nars.event.FrameReaction;
//import nars.guifx.graph3.SpaceNet;
//import nars.guifx.graph3.Xform;
//import nars.guifx.util.ColorArray;
//import nars.link.TaskLink;
//import nars.link.TermLink;
//import nars.link.TermLinkKey;
//import nars.nar.Default;
//import nars.task.Sentence;
//import nars.task.Task;
//import nars.truth.Truth;
//
//import java.util.*;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//import static javafx.application.Platform.runLater;
//import static nars.guifx.NARfx.scrolled;
//
///**
// * Created by me on 8/10/15.
// */
//public class ConceptPane extends BorderPane implements ChangeListener {
//
//    private final Concept concept;
//    private final NAR nar;
//    private final Scatter3D tasks;
//    private final BagView<Sentence, TaskLink> taskLinkView;
//    private final BagView<TermLinkKey, TermLink> termLinkView;
//    private FrameReaction reaction;
//
//    abstract public class Scatter3D<X> extends SpaceNet {
//
//        final ColorArray ca = new ColorArray(32, Color.BLUE, Color.RED);
//
//        public Scatter3D() {
//            super();
//
//
//
//            frame();
//            setPickOnBounds(true);
//            setMouseTransparent(false);
//        }
//
//        @Override
//        public Xform getRoot() {
//            return new Xform();
//        }
//
//
//
//        public class DataPoint extends Group {
//
//            public final X x;
//            private final Box shape;
//            private PhongMaterial mat;
//            private Color color = Color.WHITE;
//
//            public DataPoint(X tl) {
//                super();
//
//                shape = new Box(0.8, 0.8, 0.8);
//                mat = new PhongMaterial(color);
//                shape.setMaterial(mat);
//                //shape.onMouseEnteredProperty()
//
//                getChildren().add(shape);
//
//                this.x = tl;
//
//                frame();
//
//                shape.setOnMouseEntered(e -> {
//                    X x = ((DataPoint)e.getSource()).x;
//                    System.out.println("enter " + x);
//                });
//                shape.setOnMouseClicked(e -> {
//                    X x = ((DataPoint)e.getSource()).x;
//                    System.out.println("click " + x);
//                });
//                shape.setOnMouseExited(e -> {
//                    X x = ((DataPoint)e.getSource()).x;
//                    System.out.println("exit " + x);
//                });
//
//            }
//
//            public void setColor(Color nextColor) {
//                this.color = nextColor;
//            }
//
//            public void frame() {
//                mat.setDiffuseColor(color);
//            }
//
//
//
//        }
//
//
//        abstract Iterable<X>[] get();
//        protected abstract void update(X tl, double[] position, double[] size, Consumer<Color> color);
//
//        //ca.get(x.getPriority())
//        //concept.getTermLinks()
//
//        final Map<X, DataPoint> linkShape = new LinkedHashMap();
//
//        final Set<X> dead = new UnifiedSet();
//        double n;
//
//        float spaceScale = 10;
//
//        public void frame() {
//
//            if (!isVisible()) return;
//
//            dead.addAll(linkShape.keySet());
//
//            n = 0;
//
//            final double d[] = new double[3];
//            final double s[] = new double[3];
//
//            List<DataPoint> toAdd = new ArrayList();
//
//
//            Iterable<X>[] collects = get();
//            if (collects != null) {
//                for (final Iterable<X> ii : collects) {
//                    ii.forEach(tl -> {
//
//                        dead.remove(tl);
//
//                        DataPoint b = linkShape.get(tl);
//                        if (b == null) {
//                            b = new DataPoint(tl);
//                            linkShape.put(tl, b);
//
//                            final DataPoint _b = b;
//                            update(tl, d, s, c -> _b.setColor(c));
//                            b.setTranslateX(d[0] * spaceScale);
//                            b.setTranslateY(d[1] * spaceScale);
//                            b.setTranslateZ(d[2] * spaceScale);
//                            b.setScaleX(s[0]);
//                            b.setScaleY(s[1]);
//                            b.setScaleZ(s[2]);
//
//                            toAdd.add(b);
//                        }
//
//                        b.frame();
//
//                    });
//                }
//            }
//
//            linkShape.keySet().removeAll(dead);
//            final Object[] deads = dead.toArray(new Object[dead.size()]);
//
//            dead.clear();
//
//            runLater(() -> {
//                getChildren().addAll(toAdd);
//                toAdd.clear();
//
//                for (Object x : deads) {
//                    DataPoint shape = linkShape.remove(x);
//                    if (shape!=null && shape.getParent()!=null) {
//                        getChildren().remove(shape);
//                    }
//                }
//            });
//
//
//
//
//
//        }
//
//
//    }
//
//    public class BagView<X, Y extends Itemized<X>> extends FlowPane implements Runnable {
//
//        final Map<X,Node> componentCache = new WeakHashMap<>();
//        private final Bag<X, Y> bag;
//        private Function<Y, Node> builder;
//        final List<Node> pending = new ArrayList();
//        final AtomicBoolean queued = new AtomicBoolean();
//
//        public BagView(Bag<X, Y> bag, Function<Y,Node> builder) {
//            super();
//            this.bag = bag;
//            this.builder = builder;
//            frame();
//        }
//
//        Node getNode(Y c) {
//            final X n = c.name();
//            Node existing = componentCache.get(n);
//            if (existing == null) {
//                componentCache.put(n, existing = builder.apply(c));
//            }
//            return existing;
//        }
//
//        public void frame() {
//            synchronized (pending) {
//                pending.clear();
//                bag.forEach(b -> {
//                    pending.add(getNode(b));
//                });
//            }
//
//            if (!getChildren().equals(pending) && queued.compareAndSet(false, true)) {
//                Platform.runLater(this);
//            }
//        }
//
//        @Override
//        public void run() {
//            synchronized (pending) {
//                getChildren().setAll(pending);
//                queued.set(false);
//            }
//
//            for (final Node n : getChildren())
//                if (n instanceof Runnable)
//                    ((Runnable) n).run();
//        }
//    }
//
//    public ConceptPane(NAR nar, Concept c) {
//
//        this.concept = c;
//        this.nar = nar;
//
//        setTop(new Label(c.toInstanceString()));
//
//        final Iterable<Task>[] taskCollects = new Iterable[] {
//                c.getBeliefs(),
//                c.getGoals(),
//                c.getQuestions(),
//                c.getQuests()
//        };
//
//        //Label termlinks = new Label("Termlinks diagram");
//        //Label tasklinks = new Label("Tasklnks diagram");
//        tasks = new Scatter3D<Task>() {
//
//
//
//            @Override
//            Iterable<Task>[] get() {
//                return taskCollects;
//            }
//
//            @Override
//            protected void update(Task tl, double[] position, double[] size, Consumer<Color> color) {
//
//                if (tl.isQuestOrQuestion()) {
//                    position[0] = -1;
//                    if (tl.getBestSolution()!=null) {
//                        position[1] = tl.getBestSolution().getConfidence();
//                    }
//                    else {
//                        position[1] = 0;
//                    }
//                }
//                else {
//                    Truth t = tl.getTruth();
//                    position[0] = t.getFrequency();
//                    position[1] = t.getConfidence();
//                }
//                final float pri = tl.getPriority();
//                position[2] = pri;
//
//                size[0] = size[1] = size[2] = 0.5f + pri;
//                color.accept(ca.get(pri));
//            }
//        };
//
//        //TilePane links = new TilePane(links.content);
//
////        Label beliefs = new Label("Beliefs diagram");
////        Label goals = new Label("Goals diagram");
////        Label questions = new Label("Questions diagram");
//        SplitPane links = new SplitPane(
//                scrolled(termLinkView = new BagView<TermLinkKey, TermLink>(c.getTermLinks(),
//                                (t) -> new ItemButton( t, (i) -> i.toString(),
//                                        (i) -> {
//
//                                        }
//
//                                )
//                        )
//                ),
//                scrolled(taskLinkView = new BagView<Sentence, TaskLink>(c.getTaskLinks(),
//                        (t) -> new ItemButton( t, (i) -> i.toString(),
//                                (i) -> {
//
//                                }
//                        )
//                ))
//        );
//        links.setOrientation(Orientation.VERTICAL);
//        //links.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
//
//
//        setCenter(new SplitPane(new BorderPane(links), tasks.content));
//
//        Label controls = new Label("Control Panel");
//        setBottom(controls);
//
//        visibleProperty().addListener(this);
//        changed(null, null, null);
//    }
//
//
//
//
//    protected void frame() {
//        tasks.frame();
//        taskLinkView.frame();
//        termLinkView.frame();
//    }
//
//    @Override
//    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
//        if (isVisible()) {
//            reaction = new FrameReaction(nar) {
//                @Override public void onFrame() {
//                    frame();
//                }
//            };
//        }
//        else {
//            if (reaction!=null) {
//                reaction.off();
//                reaction = null;
//            }
//        }
//    }
//
//
//    /* test example */
//    public static void main(String[] args) {
//        NAR n = new Default();
//        n.input("<a-->b>. <b-->c>. <c-->a>.");
//        n.input("<a --> b>!");
//        n.input("<a --> b>?");
//        n.input("<a --> b>. %0.10;0.95%");
//        n.input("<a --> b>! %0.35%");
//        n.input("<a --> b>. %0.75%");
//
//        n.frame(516);
//
//        NARfx.run((a,s) -> {
//            NARfx.newWindow(n, n.concept("<a-->b>"));
////            s.setScene(new ConsolePanel(n, n.concept("<a-->b>")),
////                    800,600);
//
//            new Thread(() -> n.loop(10)).start();
//        });
//
//
//
//
//    }
//
//}
