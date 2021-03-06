/*
 * SetExt.java
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
package nars.nal.nal3;

import nars.Op;
import nars.Symbols;
import nars.term.Compound;
import nars.term.Term;

import java.io.IOException;

/**
 * An extensionally defined set, which contains one or more instances.
 */
public class SetExtN extends AbstractSetN implements SetExt {



    /**
     * Constructor with partial values, called by make
     * @param n The name of the term
     * @param arg The component list of the term - args must be unique and sorted
     */
    protected SetExtN(final Term... arg) {
        super(arg);
    }


    /**
     * Clone a SetExt
     * @return A new object, to be casted into a SetExt
     */
    @Override
    public SetExtN clone() {
        return new SetExtN(term);
    }
    
    @Override public Compound clone(Term[] replaced) {
        return SetExt.make(replaced);
    }

    /**
     * Get the operate of the term.
     * @return the operate of the term
     */
    @Override
    public Op op() {
        return Op.SET_EXT;
    }


    @Override
    public boolean appendTermOpener() {
        return false;
    }

    @Override
    public boolean appendOperator(Appendable p) throws IOException {
        super.appendOperator(p);
        return false;
    }

    @Override
    public void appendCloser(Appendable p) throws IOException {
        p.append(Symbols.SET_EXT_CLOSER);
    }

}

