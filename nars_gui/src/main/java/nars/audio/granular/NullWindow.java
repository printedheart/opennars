package nars.audio.granular;

/**
 * Created by me on 9/11/15.
 */
public class NullWindow implements GrainWindow {

    private int samples;

    public NullWindow(int samples) {
        super();
        this.samples = samples;
    }

    @Override
    public int getSize() {
        return samples;
    }

    @Override
    public float getFactor(int offset) {
        return 1f;
    }
}
