package nars.guifx.demo;

import nars.Global;
import nars.guifx.NARfx;
import nars.nar.Default;

import java.io.File;

/**
 * Created by me on 9/7/15.
 */
public class NARfxDefaultIterative {
    public static void main(String[] arg) {


//        Platform.runLater(new Runnable() {
//
//            @Override
//            public void run() {
//
//            }
//        });

        //Application.launch(NARfx.class, arg);

        Global.DEBUG = true;

        NARfx.newWindow(new Default(), (i) -> {
            try {
                i.nar.input(new File("/tmp/h.nal"));
            } catch (Throwable e) {
                i.nar.memory().eventError.emit(e);
                //e.printStackTrace();
            }
        });

    }
}
