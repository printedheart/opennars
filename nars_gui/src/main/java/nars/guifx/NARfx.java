package nars.guifx;

import com.gs.collections.impl.map.mutable.primitive.IntObjectHashMap;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import nars.Audio;
import nars.Global;
import nars.NAR;
import nars.concept.Concept;
import nars.guifx.remote.VncClientApp;
import nars.guifx.terminal.LocalTerminal;
import nars.sonification.ConceptSonification;
import nars.task.Task;
import org.jewelsea.willow.browser.WebBrowser;

import java.util.Map;
import java.util.function.Consumer;

import static javafx.application.Platform.runLater;


/**
 *
 * @author me
 */
public class NARfx  {

    public static final String css = NARfx.class.getResource("narfx.css").toExternalForm();


    /** NAR instances -> GUI windows */
    public static Map<NAR, Stage> window = Global.newHashMap();

    public static final javafx.scene.control.ScrollPane scrolled(Node n) {
        return scrolled(n, true, true);
    }

    public static final javafx.scene.control.ScrollPane scrolled(Node n, boolean stretchwide, boolean stretchhigh) {
        javafx.scene.control.ScrollPane s = new javafx.scene.control.ScrollPane();
        s.setHbarPolicy(stretchwide ? javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED : javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        s.setVbarPolicy(stretchwide ? javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED : javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);

        s.setContent(n);

        if (stretchhigh) {
            s.setMaxHeight(Double.MAX_VALUE);
        }
        s.setFitToHeight(true);

        if (stretchwide) {
            s.setMaxWidth(Double.MAX_VALUE);
        }
        s.setFitToWidth(true);

        //s.autosize();
        return s;
    }

//    public void start_(Stage primaryStage) {
//        primaryStage.setTitle("Tree View Sample");
//
//        CheckBoxTreeItem<String> rootItem =
//                new CheckBoxTreeItem<String>("View Source Files");
//        rootItem.setExpanded(true);
//
//        final TreeView tree = new TreeView(rootItem);
//        tree.setEditable(true);
//
//        tree.setCellFactory(CheckBoxTreeCell.<String>forTreeView());
//        for (int i = 0; i < 8; i++) {
//            final CheckBoxTreeItem<String> checkBoxTreeItem =
//                    new CheckBoxTreeItem<String>("Sample" + (i+1));
//            rootItem.getChildren().add(checkBoxTreeItem);
//        }
//
//        tree.setRoot(rootItem);
//        tree.setShowRoot(true);
//
//        StackPane root = new StackPane();
//        root.getChildren().add(tree);
//        primaryStage.setScene(new Scene(root, 300, 250));
//        primaryStage.show();
//    }




//    static {
//        Toolkit.getToolkit().init();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Application.launch(NARfx.class);
//            }
//        }).start();
//
//    }


//    public static void run(NAR n) {
//
//        //Default d = new Default();
//
//        /*Default d = new Equalized(1024,4,5);
//        d.setCyclesPerFrame(4);
//        d.setTermLinkBagSize(96);
//        d.setTaskLinkBagSize(96);*/
//
//        Default d = new Default(); //new Equalized(1024,1,3);
//        //Default d = new Default(1024,2,3);
//
//        NAR n = new NAR(d);
//
//        NARide w = NARfx.newWindow(n);
//
//
//        for (String s : getParameters().getRaw()) {
//            try {
//                n.input(new File(s));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        try {
//            n.input(new File("/tmp/h.nal")); //temporary
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        primaryStage.setOnCloseRequest(e -> {
//            n.stop();
//        });
//
//        {
//
//
//
//
//
////            final TilePane lp = new TilePane(4,4,
//////                        new LinePlot("Total Priority", () ->
//////                            nar.memory.getActivePrioritySum(true, true, true)
//////                        , 128),
////                    new LinePlot("Concept Priority", () -> {
////                        int c = nar.memory.getControl().size();
////                        if (c == 0) return 0;
////                        else return nar.memory.getActivePrioritySum(true, false, false) / (c);
////                    }, 128),
////                    new LinePlot("Link Priority", () ->
////                            nar.memory.getActivePrioritySum(false, true, false)
////                            , 128),
////                    new LinePlot("TaskLink Priority", () ->
////                            nar.memory.getActivePrioritySum(false, false, true)
////                            , 128)
////            );
////            lp.setPrefColumns(2);
////            lp.setPrefRows(2);
////
////            new CycleReaction(w.nar) {
////
////                @Override
////                public void onCycle() {
////                    for (Object o : lp.getChildren()) {
////                        if (o instanceof LinePlot)
////                            ((LinePlot)o).update();
////                    }
////                }
////            };
////
////            lp.setOpacity(0.5f);
////            lp.setPrefSize(200,200);
////            lp.maxWidth(Double.MAX_VALUE);
////            lp.maxHeight(Double.MAX_VALUE);
////            lp.setMouseTransparent(true);
////            lp.autosize();
//
//
////                StackPane s = new StackPane(lp);
////                s.maxWidth(Double.MAX_VALUE);
////                s.maxHeight(Double.MAX_VALUE);
//
//
//            w.content.getTabs().add(new TabX("Terminal", new TerminalPane(w.nar) ));
//
//
//
////              NARGraph1 g = new NARGraph1(w.nar);
////            SubScene gs = g.newSubScene(w.content.getWidth(), w.content.getHeight());
////            gs.widthProperty().bind(w.content.widthProperty());
////            gs.heightProperty().bind(w.content.heightProperty());
////
////            AnchorPane ags = new AnchorPane(gs);
////            w.content.getTabs().add(new TabX("Graph", ags ));
//
//        }
//        //startup defaults
//        w.console(true);
//
//
//        //JFX.popup(new NodeControlPane());
//
//        /*
//        WebBrowser w = new WebBrowser();
//
//
//        primaryStage.setTitle("title");
//        primaryStage.show();
//        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
//            @Override
//            public void handle(WindowEvent event) {
//                System.exit(0);
//            }
//        });
//
//        try {
//            w.start(primaryStage);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }*/
//
//    }


    public static Stage newWindow(String title, Region n) {

        Scene scene = new Scene(n);
        scene.getStylesheets().setAll(NARfx.css, "dark.css" );

        Stage s = new Stage();
        s.setTitle(title);
        s.setScene(scene);

        n.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return s;
    }

    public static Stage newWindow(String title, Scene scene) {
        Stage s = new Stage();
        s.setTitle(title);
        return newWindow(title, scene, s);
    }

    public static Stage newWindow(String title, Scene scene, Stage stage) {
        stage.setScene(scene);
        stage.getScene().getStylesheets().setAll(NARfx.css, "dark.css" );

        //scene.getRoot().maxWidth(Double.MAX_VALUE);
        //scene.getRoot().maxHeight(Double.MAX_VALUE);
        return stage;
    }

    public static void newWindow(NAR nar) {
        newWindow(nar, (Consumer)null);
    }

    public static void newWindow(NAR nar, Consumer<NARide> ide) {

        //SizeAwareWindow wn = NARide.newWindow(nar, ni = new NARide(nar));

        run((a,b) -> {

            NARide ni = new NARide(nar);

            {
                ni.addView(new TerminalPane(nar));
                ni.addIcon(() -> {
                    return new InputPane(nar);
                });
                ni.addIcon(()-> {
                   return new ConceptSonificationPanel(nar);
                });
                //ni.addView(additional components);
            }

            /** tool registration */
            ni.addTool("Terminal (bash)", () -> new LocalTerminal());
            ni.addTool("Status", () -> new StatusPane(nar));
            ni.addTool("VNC/RDP Remote", () -> (VncClientApp.newView()));
            ni.addTool("Web Browser", () -> new WebBrowser());

            ni.addTool("HTTP Server", () -> new Pane());

            ni.addTool(new Menu("Interface..."));
            ni.addTool(new Menu("Cognition..."));
            ni.addTool(new Menu("Sensor..."));


            //Button summaryPane = new Button(":D");

//            Scene scene = new SizeAwareWindow((d) -> {
//                double w = d[0];
//                double h = d[1];
//                if ((w < 200) && (h < 200)) {
//                    /*
//                    new LinePlot(
//                        "Concepts",
//                        () -> (nar.memory.getConcepts().size()),
//                        300
//                     */
//                    return () -> summaryPane;
//                }/* else if (w < 200) {
//                    return Column;
//                } else if (h < 200) {
//                    return Row;
//                }*/
//                return () -> ni;
//            });
            Scene scene = new Scene(ni, 900, 700,
                    false, SceneAntialiasing.DISABLED);

            scene.getStylesheets().setAll(NARfx.css, "dark.css" );
            b.setScene(scene);


            b.setScene(scene);

            b.show();

            if (ide!=null)
                ide.accept(ni);

            b.setOnCloseRequest((e) -> {
                System.exit(0);
            });
        });
//        SizeAwareWindow wn = NARide.newWindow(nar, ni = new NARide(nar));
//
//        ni.resize(500,500);
//
//        Stage s = new Stage();
//        s.setScene(wn);
//        //s.sizeToScene();
//
//
//
//        s.show();
//
//        Stage removed = window.put(nar, s);
//
//        if (removed!=null)
//            removed.close();
//
//        return ni;
    }

    public static void newWindow(NAR nar, Concept c) {
        //TODO //ConceptPane wn = new ConceptPane(nar, c);
        Pane wn = new Pane();

        Stage st;
        Stage removed = window.put(nar, st = newWindow(c.toString(), wn));

        st.show();

        if (removed!=null)
            removed.close();
    }

    public static void newWindow(NAR nar, Task c) {
        TaskPane wn = new TaskPane(nar, c);

        Stage st;
        Stage removed = window.put(nar, st = newWindow(c.toString(), wn));

        st.show();

        if (removed!=null)
            removed.close();
    }

    //final static public Font monospace = new Font("Monospace", 14);

    final static int fontPrecision = 4; //how many intervals per 1.0 to round to
    static final IntObjectHashMap<Font> monoFonts = new IntObjectHashMap();

    public static Font mono(double v) {
        //[Dialog, SansSerif, Serif, Monospaced, DialogInput]
        if (v < 1) v = 1;

        final int i = (int)Math.round(v * fontPrecision);

        final double finalV = v;

        return monoFonts.getIfAbsentPut(i, () -> {
            return Font.font("Monospaced", finalV);
        });
    }

//   static void popup(Core core, Parent n) {
//        Stage st = new Stage();
//
//        st.setScene(new Scene(n));
//        st.show();
//    }
//   static void popup(Core core, Application a) {
//        Stage st = new Stage();
//
//        BorderPane root = new BorderPane();
//        st.setScene(new Scene(root));
//        try {
//            a.start(st);
//        } catch (Exception ex) {
//            Logger.getLogger(NARfx.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        st.show();
//    }
//
//    static void popupObjectView(Core core, NObject n) {
//        Stage st = new Stage();
//
//        BorderPane root = new BorderPane();
//
//        WebView v = new WebView();
//        v.getEngine().loadContent(ObjectEditPane.toHTML(n));
//
//        root.setCenter(v);
//
//        st.setTitle(n.id);
//        st.setScene(new Scene(root));
//        st.show();
//    }



    /* https://macdevign.wordpress.com/2014/03/27/running-javafx-application-instance-in-the-main-method-of-a-class/ */
    public static void run(AppLaunch appLaunch, String... sArArgs) {
        DummyApplication.appLaunch = appLaunch;
        DummyApplication.launch(DummyApplication.class, sArArgs);
    }

    // This must be public in order to instantiate successfully
    public static class DummyApplication extends Application {

        private static AppLaunch appLaunch;

        @Override
        public void start(Stage primaryStage) throws Exception {

            if (appLaunch != null) {
                appLaunch.start(this, primaryStage);
            }
        }

        @Override
        public void init() throws Exception {
            if (appLaunch != null) {
                appLaunch.init(this);
            }
        }

        @Override
        public void stop() throws Exception {
            if (appLaunch != null) {
                appLaunch.stop(this);
            }
        }
    }

    @FunctionalInterface
    public static interface AppLaunch {
        void start(Application app, Stage stage) throws Exception;
        // Remove default keyword if you need to run in Java7 and below
        default void init(Application app) throws Exception {
        }

        default void stop(Application app) throws Exception {
        }
    }

    private static class ConceptSonificationPanel extends BorderPane {

        private final Label info;
        final static int maxVoices = 4;
        private final NAR nar;
        private ConceptSonification son;

        public ConceptSonificationPanel(NAR nar) {
            super();

            this.nar = nar;
            info = new Label();
            info.setWrapText(true);
            setCenter(info);

            CheckBox b = new CheckBox("Sonify");
            setBottom(b);
            b.selectedProperty().addListener(c -> {
                if (b.isSelected()) {
                    start();
                }
                else {
                    stop();
                }
            });
        }
        protected void stop() {
            info.setText("Stopping");
            if (son!=null) {
                son.off();
                son.sound.shutDown();
                son = null;
            }
            info.setText("Silent");
        }

        protected void start() {

            try {
                son = new ConceptSonification(nar, new Audio(maxVoices)) {

                    @Override
                    public void onFrame() {
                        super.onFrame();
                        //String pp = playing.keySet().toString();
                        runLater(() -> {
                            info.setText("Sonifying..");
                            //info.setText(this.playing.toString());
                        });

                    }
                };
            } catch (Exception e) {
                e.printStackTrace();
                info.setText(e.toString());
                son = null;
            }

        }
    }
}
