/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package nars.logic;

import nars.core.NAR;
import nars.build.Neuromorphic;
import nars.gui.NARSwing;
import nars.io.TextInput;

import java.io.File;

/**
 *
 * @author me
 */
public class NARSwingNeuromorphic {
 
    public static void main(String[] args) throws Exception {
        int ants = 6;
        
        NAR n = new NAR(
                new Neuromorphic(ants).simulationTime().setConceptBagSize(4096).setNovelTaskBagSize(100).setSubconceptBagSize(8192).setTaskLinkBagSize(50).setTermLinkBagSize(200)      
        ) {


        };
        n.param.conceptBeliefsMax.set(32);
        
        n.addInput(new TextInput(new File("/tmp/h.nal")));
        
        NARSwing.themeInvert();
        
        NARSwing s = new NARSwing(n);
        
        s.enableJMX();
        
        
        
    }
}
