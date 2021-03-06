package nars.audio.granular;

public class Granulator {

	private final float[] sourceBuffer;
    public final float sampleRate;
    private int grainSizeSamples;
	private final GrainWindow window;

//    public Granulator(SonarSample source, float grainSizeSecs, float windowSizeFactor) {
//        this(source.buf, source.rate, grainSizeSecs, windowSizeFactor);
//    }

	public Granulator(float[] sourceBuffer, float sampleRate, float grainSizeSecs, float windowSizeFactor) {
		this.sourceBuffer = sourceBuffer;
		this.grainSizeSamples = Math.round(sampleRate * grainSizeSecs);


		    this.window = new HanningWindow(Math.round(sampleRate * grainSizeSecs * windowSizeFactor));
            //this.window = new NullWindow(Math.round(sampleRate * grainSizeSecs * windowSizeFactor));

        this.sampleRate = sampleRate;
	}

    public boolean hasMoreSamples(final long[] grain, final long now) {
        final long length = grain[1];
        final long showTime = grain[2];
        return now < showTime + length + window.getSize();
    }

    public float getSample(long[] grain, long now) {
        final long startIndex = grain[0];
        final long length = grain[1];
        final long showTime = grain[2];

        final float[] sb = sourceBuffer;

        long offset = now - showTime;
        int sourceIndex = (int)((startIndex + offset + sb.length) % sb.length);
        while (sourceIndex < 0)
            sourceIndex+=sb.length;
        float sample = sb[sourceIndex];
        float ww = 0;
        if (offset <= 0) {
            ww = window.getFactor((int)offset);
        }
        if (offset > length) {
            ww = window.getFactor((int)(offset - length));
        }
        return sample * ww;
    }

    public static final boolean isFading(long[] grain, long now) {
        final long length = grain[1];
        final long showTime = grain[2];
        return now > showTime + length;
    }

	public long[] nextGrain(long[] grain, int startIndex, long fadeInTime) {
        if (grain == null) grain = new long[3];
        final int ws = window.getSize();
        grain[0] = (startIndex + ws) % sourceBuffer.length;
        grain[1] = grainSizeSamples;
        grain[2] = fadeInTime + ws;
        return grain;
	}

}
