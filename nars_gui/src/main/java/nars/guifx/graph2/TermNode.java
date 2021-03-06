package nars.guifx.graph2;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextBoundsType;
import nars.NAR;
import nars.concept.Concept;
import nars.guifx.JFX;
import nars.guifx.NARfx;
import nars.term.Term;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by me on 9/5/15.
 */
public class TermNode extends Group {


    final public Map<Term, TermEdge> edge = new LinkedHashMap(8);

    /**
     * copy of termedge values for fast iteration during rendering
     */
    TermEdge[] edges = null;

    protected final Term term;
    private final Text titleBar;
    private final Polygon base;
    Concept c = null;

    private double priorityDisplayed = -1;

    /**
     * granularity for discretizing displayed scales to reduces # of updates
     */
    final static double priorityDisplayedResolution = 100;


    double minSize = 16;
    double maxSize = 64;

    /**
     * cached from last set
     */
    private double scaled;
    private double tx;
    private double ty;

    private boolean hover = false;
    private Color stroke;
    public Concept concept;
    private static TermEdge[] empty = new TermEdge[0];


    public TermNode(NAR nar, Term t) {
        super();

        this.titleBar = new Text(t.toStringCompact());
        base = JFX.newPoly(6, 2.0);


        this.term = t;
        this.c = nar.concept(t);

        randomPosition(150, 150);

        titleBar.setFill(Color.WHITE);
        titleBar.setBoundsType(TextBoundsType.VISUAL);

        titleBar.setPickOnBounds(false);
        titleBar.setMouseTransparent(true);
        titleBar.setFont(NARGraph1.nodeFont);
        titleBar.setTextAlignment(TextAlignment.CENTER);
        titleBar.setSmooth(false);
        titleBar.setManaged(false);

        base.setStrokeType(StrokeType.INSIDE);

        base.setOnMouseClicked(e -> {
            //System.out.println("click " + e.getClickCount());
            if (e.getClickCount() == 2) {
                if (c != null)
                    NARfx.newWindow(nar, c);
            }
        });

        EventHandler<MouseEvent> mouseActivity = e -> {
            if (!hover) {
                base.setStroke(Color.ORANGE);
                base.setStrokeWidth(0.05);
                hover = true;
            }
        };
        //base.setOnMouseMoved(mouseActivity);
        base.setOnMouseEntered(mouseActivity);
        base.setOnMouseExited(e -> {
            if (hover) {
                base.setStroke(null);
                base.setStrokeWidth(0);
                hover = false;
            }
        });

        setPickOnBounds(false);


        getChildren().setAll(base, titleBar);//, titleBar);


        update();

        base.setLayoutX(-0.5f);
        base.setLayoutY(-0.5f);

        /*titleBar.setScaleX(0.25f);
        titleBar.setScaleY(0.25f);*/
        titleBar.setLayoutX(-getLayoutBounds().getWidth() / (2) + 0.25);
        //titleBar.setY(-getLayoutBounds().getHeight()/2);
//            System.out.println(titleBar);
//            System.out.println(titleBar.getLayoutBounds());
//            System.out.println(titleBar.getLocalToParentTransform());
//            System.out.println(titleBar.getLocalToSceneTransform());
//            System.out.println(titleBar.getBoundsInLocal());


        //setCache(true);
        //setCacheHint(CacheHint.SPEED);
        base.setCacheHint(CacheHint.SCALE_AND_ROTATE);
        base.setCache(true);

        titleBar.setCacheHint(CacheHint.SCALE_AND_ROTATE);
        titleBar.setCache(true);


        //setCacheShape(true);

        /*double s = 1.0 / titleBar.getBoundsInLocal().getWidth();

        titleBar.setScaleX(s);
        titleBar.setScaleY(s);*/

        //getChildren().add(new Rectangle(1,1))

    }


    public void randomPosition(double bx, double by) {
        move(NARGraph1.rng.nextDouble() * bx, NARGraph1.rng.nextDouble() * by);
    }

    /**
     * NAR update thread
     */
    public void update() {

        double vertexScaling = NARGraph1.visModel.getVertexScale(c);

        if ((int) (priorityDisplayedResolution * priorityDisplayed) !=
                (int) (priorityDisplayedResolution * vertexScaling)) {

            double scale = minSize + (maxSize - minSize) * vertexScaling;
            this.scaled = scale;

            setScaleX(scale);
            setScaleY(scale);

            float conf = c != null ? c.getBeliefs().getConfidenceMax(0, 1) : 0;
            base.setFill(NARGraph1.visModel.getVertexColor(vertexScaling, conf));

            //setOpacity(0.75f + 0.25f * vertexScaling);

            this.priorityDisplayed = vertexScaling;
            //System.out.println(scale + " " + vertexScaling + " " + (int)(priorityDisplayedResolution * vertexScaling));
        }

    }


    public void getPosition(final double[] v) {
        v[0] = tx;
        v[1] = ty;
    }

    Point2D sceneCoord;// = new Point2D(0,0);

    final public TermNode move(final double x, final double y) {
        setTranslateX(this.tx = x);
        setTranslateY(this.ty = y);

        sceneCoord = null;
        return this;
    }


    final public void move(final double[] v, final double speed, final double threshold) {
        final double px = tx;
        final double py = ty;
        final double momentum = 1f - speed;
        final double nx = v[0] * speed + px * momentum;
        final double ny = v[1] * speed + py * momentum;
        final double dx = Math.abs(px - nx);
        final double dy = Math.abs(py - ny);
        if ((dx > threshold) || (dy > threshold)) {
            move(nx, ny);
        }
    }

    final public boolean move(final double[] v, final double threshold) {
        final double x = tx;
        final double y = ty;
        final double nx = v[0];
        final double ny = v[1];
        if (!((Math.abs(x - nx) < threshold) && (Math.abs(y - ny) < threshold))) {
            move(nx, ny);
            return true;
        }
        return false;
    }

    public double width() {
        return scaled; //getScaleX();
    }

    public double height() {
        return scaled; //getScaleY();
    }

    public double sx() {
        if (sceneCoord == null) sceneCoord = localToParent(0, 0);
        return sceneCoord.getX();
    }

    public double sy() {
        if (sceneCoord == null) sceneCoord = localToParent(0, 0);
        return sceneCoord.getY();
    }

    public double x() {
        return tx;
    }

    public double y() {
        return ty;
    }

    public TermEdge putEdge(Term b, TermEdge e) {
        TermEdge r = edge.put(b, e);
        if (e != r)
            edges = null;
        return r;
    }

    public TermEdge[] updateEdges() {
        final int s = edge.size();
        return edges = edge.values().toArray(new TermEdge[s]);
    }

    /**
     * untested
     */
    public void removeEdge(TermEdge e) {
        if (edge.remove(e.bSrc) != e) {
            throw new RuntimeException("wtf");
        }
        edges = null;
    }

    public TermEdge[] getEdges() {
        if (edges == null) {
            if (edge.size() > 0)
                updateEdges();
            else
                edges = empty;
        }

        return edges;
    }

}
