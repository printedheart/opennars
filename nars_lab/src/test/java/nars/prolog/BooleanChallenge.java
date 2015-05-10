package nars.prolog;

import com.gs.collections.impl.map.mutable.primitive.ObjectDoubleHashMap;
import nars.Events.OUT;
import nars.Global;
import nars.NAR;
import nars.model.impl.Default;
import nars.nal.Task;
import nars.nal.nal1.Inheritance;
import nars.nal.nal3.SetExt;
import nars.nal.nal4.Product;
import nars.nal.term.Compound;
import nars.nal.term.Term;
import nars.narsese.InvalidInputException;
import nars.tuprolog.InvalidLibraryException;
import nars.tuprolog.InvalidTheoryException;
import nars.util.event.Reaction;
import objenome.util.random.XORShiftRandom;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nars.io.Texts.n2;
import static nars.io.Texts.n4;

/**
 * @author me
 */
public class BooleanChallenge implements Reaction {

    final float freqThresh = 0.25f; //threshold diff from 0.0 or 1.0 considered too uncertain to count as answer
    private final double complete;
    boolean failOnError = false; //exit on the first logical error
    private boolean correctFeedback = true;
    boolean ignoreCorrectProvided = false; //if true, scores will only be updated if the answer is was not provided or if it was incorrect (provided or not provided)
    float confThreshold = 0.5f; //confidence threshold for being counted as an answer
    float inputConf = 0.95f;

    public static void main(String[] args) throws InvalidTheoryException, InvalidLibraryException {
        Global.DEBUG = true;
        NAR n = new NAR(new Default(1024, 4, 3).setInternalExperience(null));

        //NAR n = new NAR(new Discretinuous());
        NARPrologMirror pl = new NARPrologMirror(n, 0.9f, 0.25f, true, true, false);
        pl.setReportAnswers(true);
        pl.setReportAssumptions(true);
        //NAR n = new CurveBagNARBuilder().build();

        //new TraceWriter(n, System.out);
        //new TextOutput(n, System.out);

        new BooleanChallenge(n, 2, 10550, 0.25f).getScore();


        System.out.println(pl.getBeliefsTheory());

        /*
        n.memory.getQuestionConcepts().forEach(x -> {

            System.out.println(x + " " + x.getQuestions().get(0).getBestSolution());
            System.out.println("  " + x.getStrongestBelief());
        });
        */
    }


    private final NAR nar;
    private final double score;
    int bits;
    String startChallenge = "<boolean --> challenge>";

    Set<Term> answerProvided = new HashSet();
    ObjectDoubleHashMap<Term> questionScores = new ObjectDoubleHashMap();



    private double totalScoreCorrect = 0, totalScoreWrong = 0, totalScoreCorrectIncludingRevised = 0;

    public StringBuilder sb = new StringBuilder();

    public BooleanChallenge(NAR n, int bits, int cycles, float mystery) {

        this.nar = n;
        n.param.outputVolume.set(100);

        this.bits = bits;

//        n.on(new Reaction() {
//
//            @Override
//            public void event(Class event, Object[] arguments) {
//                double p = Math.random();
//                if (p < inputProb) {
//                    p = Math.random();
//                    inputBoolean(bits, (p < questionRatio));
//                }
//
//
//            }
//
//        }, CycleEnd.class);

        inputAxioms();

        int N;
        switch (bits) {
            case 1: N = (2*2*2) * 3; break;
            case 2: N = (4*4*4) *3; break;
            case 3: N = (8*8*8) * 3; break;
            default:
                throw new RuntimeException("didnt count here yet");
        }
        int toAsk = (int)Math.ceil(mystery * N);


        while (answerProvided.size() < N - toAsk) {
            inputBoolean(bits, false);
        }

        n.on(this, OUT.class);

        inputAxioms();

        while (questionScores.size() < toAsk) {
            inputBoolean(bits, true);
        }



        for (int i = 0; i < cycles; i++) {

            nar.frame(1);
        }

        System.out.println(answerProvided);
        System.out.println(questionScores);



        System.out.print(nar.time() + " cycles, ");
        System.out.print(answerProvided.size() + " provided, ");
        System.out.println(questionScores.size() + " answerable");
        System.out.print(totalScoreCorrect + " correct, ");
        System.out.print((totalScoreCorrectIncludingRevised - totalScoreCorrect) + " revision, ");
        System.out.println(totalScoreWrong + " wrong, ");


        this.complete = (questionScores.sum() / (toAsk));
        System.out.print(complete + " COMPLETE, ");

        this.score = complete * ((totalScoreCorrect) / (totalScoreCorrect + totalScoreWrong));
        System.out.println(score + " SCORE");

    }

    public double getScore() {
        return score;
    }

    protected boolean evalAnd(int[] t) { return (t[0] & t[1]) == t[2];        }

    protected boolean evalOr(int[] t) {
        return (t[0] | t[1]) == t[2];
    }

    protected boolean evalXor(int[] t) {
        return (t[0] ^ t[1]) == t[2];
    }


    boolean started = false;

    @Override
    public void event(Class event, Object[] arguments) {
        if (!(arguments[0] instanceof Task)) return;

        Task answer = (Task) arguments[0];
        if (!answer.sentence.isJudgment())
            return;

        if (answer.isInput()) {
            //this is a repetition of something explicitly input
            return;
        }

//        if (!started) {
//            if (answer.getTerm().toString().equals(startChallenge)) {
//                started = true;
//            }
//            else {
//                return;
//            }
//        }

        Term qterm = answer.getTerm(); //for now, assume the qustion term is answer term

        if (answerProvided.contains(qterm)) {
            return;
        }

                /*
                Task question = answer.getParentTask();
                if (!question.sentence.isQuestion())
                    return;
                if (!questionScores.containsKey(question.getTerm())) {
                    //this is a response to a question it asked itself
                    return;
                }
                */

        if (answer.sentence.truth.getConfidence() < confThreshold)
            return;

        Term t = answer.getTerm();


        if (t instanceof Inheritance) {
            Term subj = ((Inheritance) t).getSubject();
            Term pred = ((Inheritance) t).getPredicate();

            if ((subj instanceof SetExt) && (((SetExt) subj).size() == 1))
                subj = ((Compound) subj).term[0];

            if (subj instanceof Product) {
                Product p = (Product) subj;
                int[] n = n(p, 3);
                if (n != null) {
                    boolean correct = false;
                    switch (pred.toString()) {
                        case "and2b":
                            correct = evalAnd(n);
                            break;
                        case "or2b":
                            correct = evalOr(n);
                            break;
                        case "xor2b":
                            correct = evalXor(n);
                            break;
                        //case "not": correct = evalNot(n); break;
                        default:
                            return;
                    }

                    float freq = answer.sentence.truth.getFrequency();
                    float conf = answer.sentence.truth.getConfidence();

                    if (freq < freqThresh) {
                        correct = !correct; //invert
                        freq = 1f - freq;
                    }

                    if (freq < (1.0 - freqThresh)) {
                        //not clear 0 or 1
                        return;
                    }

                    addScore(qterm, answer, correct, freq*conf);

                    if (!correct) {
                        System.err.println(answer.getExplanation());

                        if (failOnError)
                            System.exit(1);

                        //give correct answer
                        if (correctFeedback) {
                            //String c = getTerm(n[0], n[1], pred.toString(), not(n[2])) + ('.');
                            Term fc = nar.term(getTerm(n[0], n[1], pred.toString(), n[2]));
                            String c = "(--," + fc + ("). %1.00;" + n2(inputConf) + "%");

                            nar.input(c);

                            answerProvided.add(fc);
                            questionScores.remove(fc);
                        }

                    }
                }
            }

        }
    }



    public static int not(int i) {
        if (i == 0) return 1;
        else return 0;
    }

    public static char b(int a) {
        return (char)(a + '0');
    }

    public static boolean isNumber(String s) {
        return (s.length() == 1) && (Character.isDigit(s.charAt(0)));
    }

    public static int[] n(Product p, int count) {

        AtomicInteger i = new AtomicInteger(0);
        int[] x = new int[count];

        //extract all numbers from the product recursively, depth first
        p.recurseSubterms((t, parent) -> {
            String ts = t.toString();
            if (!ts.startsWith("b")) return; //b prefix
            ts = ts.toString().substring(1);
            if (isNumber(ts.toString())) {
                x[i.getAndIncrement()] = (Integer.parseInt(ts));
            }
        });
        if (i.get() == count)
            return x;

        return null;
    }



//    public void updateScore() {
//        double s = 0;
//        qanswered = 0;
//        for (Double q : questionScores.values()) {
//            if (q!=null) {
//                s += q;
//                qanswered++;
//            }
//        }
//        //scoreRate = s/ nar.time();
//
//    }

    void inputAxioms() {

        nar.believe("<{or,xor,and} <=> op>", inputConf);

        String a = "<{";
        for (int i = 1; i < (1 << bits); i++)
            a += "b" + i + ",";
        a += "b0} <=> number>";
        nar.believe(a, inputConf);

        nar.believe("<{even,odd} <-> parity>");
        nar.believe("<{a0,b0,b2} <-> even>");
        nar.believe("<{a1,b1,b3} <-> odd>");
        nar.believe("<b0 <-> (*,a0,a0)>");
        nar.believe("<b1 <-> (*,a0,a1)>");
        nar.believe("<b2 <-> (*,a1,a0)>");
        nar.believe("<b3 <-> (*,a1,a1)>");
    }

    void addScore(Term q, Task a, boolean correct, float expectation) {

        if (ignoreCorrectProvided && correct && answerProvided.contains(a.getTerm())) return;

        Term questionTerm = q;
        long questionTime = a.getParentTask() != null ? a.getParentTask().getCreationTime() : 0;
        long answerTime = a.getCreationTime();
        long time = nar.time();


        long delay = time - questionTime;


        double s;
        s = expectation; // / ( 1 + delay * delayFactor );

//        if (!questionScores.containsKey(questionTerm)) {
//            System.out.println("question not asked: " + questionTerm + " but these are: " + questionScores.keySet());
//        }

        double existingScore = questionScores.getIfAbsent(questionTerm,0);
        if (correct) {

            if (s > existingScore) {


                double ds = s - existingScore;
                totalScoreCorrectIncludingRevised += s;
                totalScoreCorrect += ds;

                questionScores.put(questionTerm, Math.min(Math.max(existingScore,s), 1f));
            }

        }
        else {

            if (-s < existingScore) {
                totalScoreWrong += s;
                questionScores.put(questionTerm, Math.max(Math.min(existingScore, -s), -1f));
            }
        }




        System.out.println("ANSWER:" + questionTime + "," + delay + ": " + a.sentence + "  " + n4(s) + "  " +
                n4(totalScoreCorrect) + "-" + n4(totalScoreWrong) + "=" + n4(totalScoreCorrect - totalScoreWrong));


    }


    int rand(int bits) {
        return (int) (XORShiftRandom.global.nextInt(1 << bits));
    }

    protected void inputBoolean(int bits, boolean question) {
        int a = rand(bits);
        int b = rand(bits);
        int y = rand(bits);
        int correct;
        String op;
        boolean allowNegation = XORShiftRandom.global.nextBoolean();
        switch (XORShiftRandom.global.nextInt(3)) {
            case 0:
                op = "and2b";
                correct = a & b;
                break;
            case 1:
                op = "or2b";
                correct = a | b;
                break;
            case 2:
            default:
                op = "xor2b";
                correct = a ^ b;
                break;
        }

        if (!question && (!allowNegation) && (correct!=y))
            y = correct;


        //String term = "<(*,(*," + b(a) + "," + b(b) + ")," + b(y) + ") --> " + op + ">";
        String term = getTerm(a, b, op, y);
        Term t;
        try {
            t = nar.term(term);
        } catch (InvalidInputException ex) {
            Logger.getLogger(BooleanChallenge.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }


        if (question) {
            //dont ask alredy answered
            if (answerProvided.contains(t)) {
                return;
            }
            //only ask once
            if (questionScores.containsKey(t))
                return;
            questionScores.put(t, 0);
        } else {
            if (questionScores.containsKey(t)) {
                //dont give answer to a question
                return;
            }
            answerProvided.add(t);
        }

        float truth = 1f;
        if (!question) {
            truth = (correct == y) ? 1.0f : 0.0f;
        }
        String ii = term +
                (question ? '?' : '.') + " %" + n2(truth) + ";" + n2(inputConf) + "%";

        //System.out.println(ii);
        //System.out.println(questionScores.keySet());
        //System.out.println(answerProvided);

        nar.input(ii);

    }


    public final String getTerm(int a, int b, String op, int y) {

        sb.setLength(0);
        sb.append("<{(*,(*,");
            sb.append('b');
            sb.append(a);
        sb.append(',');
            sb.append('b');
            sb.append(b);
        sb.append("),");
            sb.append('b');
            sb.append(y);
        sb.append(")} --> ");
        sb.append(op);
        sb.append('>');
        return sb.toString();


        //use the { } --> form and not {-- because this is the form answers will return in
        //return "<{(*," + b(a) + "," + b(b) + "," + b(y) + ")} --> " + op + ">";

        //return "<(*,(*," + b(a) + "," + b(b) + ")," + b(y) + ") --> " + op + ">";        


        //return "<(&/," + b(a) + "," + b(b) + "," + b(y) + ") --> " + op + ">";        

        //return "(&/," + op + "," + b(a) + "," + b(b) + "," + b(y) + ")";

        //return "<(*," + op + ", (*," + b(a) + "," + b(b) + ")) --> " + b(y) + ">";

        //return "<(*," + op + ", (*," + b(a) + "," + b(b) + ")) <=> " + b(y) + ">";

    }

}
