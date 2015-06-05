package nars.nal.nal4;

import nars.nal.NALOperator;
import nars.nal.term.Compound1;
import nars.nal.term.Term;
import nars.util.data.id.UTF8Identifier;

/**
 * Higher efficiency 1-subterm implementation of Product
 */
public class Product1<T extends Term> extends Compound1<T> implements Product {

    public Product1(T the) {
        super(the);

        init(term);
    }

    @Override
    public NALOperator operator() {
        return NALOperator.PRODUCT;
    }

    @Override
    public Term[] terms() {
        return new Term[] { the() };
    }

    @Override
    public Term clone() {
        return Product.only(the());
    }

    @Override
    public Term clone(Term[] replaced) {
        return Product.make( replaced );
    }

    public UTF8Identifier newName() {
        return new DefaultCompoundUTF8Identifier(this);
    }

}