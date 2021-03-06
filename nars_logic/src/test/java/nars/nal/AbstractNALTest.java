package nars.nal;

import com.google.common.collect.Lists;
import nars.NAR;
import nars.meter.TestNAR;
import nars.nar.Default;
import org.junit.Ignore;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by me on 2/10/15.
 */
@Ignore
abstract public class AbstractNALTest  {

    public static final List<Supplier<NAR>> core1 =Lists.newArrayList(
        //() -> new Default().nal(1),
        //() -> new Default().nal(2),
        () -> new Default().nal(1)
        //() -> new DefaultAlann(48)
    );
    public static final List<Supplier<NAR>> core2 =Lists.newArrayList(
            //() -> new Default().nal(1),
            //() -> new Default().nal(2),
            () -> new Default().nal(2)
            //() -> new DefaultAlann(48)
    );
    public static final List<Supplier<NAR>> core3 =Lists.newArrayList(
            //() -> new Default().nal(1),
            //() -> new Default().nal(2),
            () -> new Default().nal(3)
            //() -> new DefaultAlann(48)
    );
    public static final List<Supplier<NAR>> core6 =Lists.newArrayList(
            //() -> new Default().nal(1),
            //() -> new Default().nal(2),
            () -> new Default().nal(6)
            //() -> new DefaultAlann(48)
    );


    //final ThreadLocal<NAR> nars;
    //private final Supplier<NAR> nar;
    private final NAR the;

    protected AbstractNALTest(NAR nar) {
        this.the = nar;
    }

    protected AbstractNALTest(Supplier<NAR> nar) {
        //this.nar = nar;
        this.the = nar.get();
        //this.nars = ThreadLocal.withInitial( nar );
    }


    public final TestNAR test() {
        return new TestNAR(nar());
    }


    public final NAR nar() {
        return the;
    }

}
