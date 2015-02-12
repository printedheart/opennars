/*
 * Inheritance.java
 *
 * Copyright (C) 2008  Pei Wang
 *
 * This file is part of Open-NARS.
 *
 * Open-NARS is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Open-NARS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Open-NARS.  If not, see <http://www.gnu.org/licenses/>.
 */
package nars.logic.nal8;

import nars.core.Memory;
import nars.io.Symbols;
import nars.logic.NALOperator;
import nars.logic.entity.Task;
import nars.logic.entity.Term;
import nars.logic.nal1.Inheritance;
import nars.logic.nal4.Product;

import java.util.Arrays;

import static nars.logic.NALOperator.COMPOUND_TERM_CLOSER;
import static nars.logic.NALOperator.COMPOUND_TERM_OPENER;

/**
 * An operation is interpreted as an Inheritance relation.
 */
public class Operation extends Inheritance {
    private Task task;
    
    
    public final static Term[] SELF_TERM_ARRAY = new Term[] { SELF };

    /**
     * Constructor with partial values, called by make
     *
     * @param n The name of the term
     * @param arg The component list of the term
     */
    protected Operation(Term argProduct, Term operator) {
        super(argProduct, operator);
    }
    
    protected Operation(Term[] t) {
        super(t);
    }
    protected Operation() {
        super(null);
    }

    
    /**
     * Clone an object
     *
     * @return A new object, to be casted into a SetExt
     */
    @Override
    public Operation clone() {        
        return new Operation(term);
    }
 
   
    /**
     * Try to make a new compound from two components. Called by the logic
     * rules.
     *
     * @param memory Reference to the memory
     * @param addSelf include SELF term at end of product terms
     * @return A compound generated or null
     */
    public static Operation make(final Operator oper, Term[] arg, boolean addSelf) {

//        if (Variables.containVar(arg)) {
//            throw new RuntimeException("Operator contains variable: " + oper + " with arguments " + Arrays.toString(arg) );
//        }
        if(addSelf && !Term.isSelf(arg[arg.length-1])) {
            Term[] arg2=new Term[arg.length+1];
            System.arraycopy(arg, 0, arg2, 0, arg.length);
            arg2[arg.length] = Term.SELF;
            arg=arg2;
        }
        
        return new Operation( new Product(arg), oper  );        
    }

    public Operator getOperator() {
        return (Operator)getPredicate();
    }
    
    @Override
    protected CharSequence makeName() {
        if(getSubject() instanceof Product && getPredicate() instanceof Operator)
            return makeName(getPredicate().name(), ((Product)getSubject()).term);
        return makeStatementName(getSubject(), NALOperator.INHERITANCE, getPredicate());
    }

    
    public static CharSequence makeName(final CharSequence op, final Term[] arg) {
        final StringBuilder nameBuilder = new StringBuilder(16) //estimate
                .append(COMPOUND_TERM_OPENER.ch).append(op);
        
        int n=0;
        for (final Term t : arg) {
            if(n==arg.length-1) {
                break;
            }
            nameBuilder.append(Symbols.ARGUMENT_SEPARATOR);
            nameBuilder.append(t.name());
            n++;
        }
        
        nameBuilder.append(COMPOUND_TERM_CLOSER.ch);
        return nameBuilder.toString();
    }
    
    
    /*public Operator getOperator() {
        return (Operator) getPredicate();
    }
    
    public Term[] getArguments() {
        return ((CompoundTerm) getSubject()).term;
    }*/

    /** stores the currently executed task, which can be accessed by Operator execution */
    public void setTask(final Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Product getArguments() {
        return (Product)getSubject();
    }

    public static Term make(Memory m, Term[] raw) {
        if (raw.length < 1) {
            //must include at least the operator as the first term in raw[]
            return null;
        }

        String operator = raw[0].name().toString();
        if (operator.charAt(0)!= NALOperator.OPERATION.ch) {
            //prepend '^' if missing
            operator = NALOperator.OPERATION.symbol + operator;
        }

        Term[] args = Arrays.copyOfRange(raw, 1, raw.length);

        Operator o = m.getOperator(operator);
        if (o == null)
            throw new RuntimeException("Unknown operator: " + o); //TODO use a 'UnknownOperatorException' so this case can be detected by callers

        return make(o, args, true);
    }

    public Term getArgument(int i) {
        return getArguments().term[i];
    }

    @Override
    public boolean isExecutable(Memory mem) {
        //don't allow ^want and ^believe to be active/have an effect,
        //which means its only used as monitor
        return getOperator().isExecutable(mem);
    }
}