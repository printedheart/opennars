package nars.operator.software;

import nars.core.Memory;
import nars.core.Parameters;
import nars.io.Symbols;
import nars.logic.entity.Task;
import nars.logic.entity.Term;
import nars.logic.nal7.Tense;
import nars.logic.nal8.Operation;
import nars.logic.nal8.Operator;
import nars.operator.mental.Mental;

import java.util.ArrayList;

// Usage:
// (^numericCertainty, min, max, value, term)
public class NumericCertainty extends Operator implements Mental{
    public NumericCertainty() {
        super("^numericCertainty");
    }
    
    @Override
    protected ArrayList<Task> execute(Operation operation, Term[] args, Memory memory) {
        if (args.length != 4) {
            // TODO< error report >
            return null;
        }
        
        float min = tryToConvertStringToFloat(args[0].toString());
        float max = tryToConvertStringToFloat(args[1].toString());
        float value = tryToConvertStringToFloat(args[2].toString());
        
        float clampedValue = clamp(min, max, value);
        float resultCertainty = (clampedValue-min)/(max-min);
        
        
        Term resultTerm = args[3];
        
        // NOTE< no memory output ? >
        
        ArrayList<Task> results = new ArrayList<>(1);
        results.add(memory.newTask(resultTerm, Symbols.JUDGMENT_MARK, 1.0f, resultCertainty, Parameters.DEFAULT_JUDGMENT_PRIORITY, Parameters.DEFAULT_JUDGMENT_DURABILITY, Tense.Present));
        
        return results;
    }
    
    static private float tryToConvertStringToFloat(String input) {
        return Float.parseFloat(input);
    }
    
    static private float clamp(float min, float max, float value) {
        if (value > max) {
            return max;
        }
        else if (value < min) {
            return min;
        }
        else {
            return value;
        }
    }
}
