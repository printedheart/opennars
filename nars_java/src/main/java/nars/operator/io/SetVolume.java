package nars.operator.io;

import nars.core.Memory;
import nars.logic.nal8.ImmediateOperation;

/**
 * Sets the global volume / noise level, =(100% - "silence level")
 */
public class SetVolume extends ImmediateOperation {
    public final int volume;

    public SetVolume(int volume) {
        super();
        this.volume = volume;
    }

    @Override
    public CharSequence name() {
        return "SetVolume(" + volume + ')';
    }

    @Override
    public void execute(Memory m) {
        m.param.noiseLevel.set(volume);
    }
}