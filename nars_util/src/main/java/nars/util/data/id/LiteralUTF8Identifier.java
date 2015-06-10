package nars.util.data.id;

import nars.util.utf8.FastByteComparisons;
import nars.util.utf8.Utf8;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * Constant-value UTF8 identifier, populated by String or byte[] on construction
 */
public class LiteralUTF8Identifier extends UTF8Identifier {

    protected byte[] data = null;
    transient protected int hash = 0;


    /** do nothing, used by subclass */
    protected LiteralUTF8Identifier() { }

    public LiteralUTF8Identifier(byte[] b) {
        this.data = b;
    }

    public LiteralUTF8Identifier(byte[] b, int start, int stop) {
        int len = stop - start;
        this.data = new byte[len];
        System.arraycopy(b, start, data, 0, len);
    }

    public LiteralUTF8Identifier(final byte[] prefix, final byte[] suffix) {
        int plen = prefix.length;
        int slen = suffix.length;
        int len = plen + slen;
        this.data = new byte[len];
        System.arraycopy(prefix, 0, data, 0, plen);
        System.arraycopy(suffix, 0, data, plen, slen);
    }

    public LiteralUTF8Identifier(String s) {
        this(Utf8.toUtf8(s));
    }

    public LiteralUTF8Identifier(char[] s) {
        this(Utf8.toUtf8(s));
    }

    /** should set the hashCode, but this may need to call makeName */
    public int makeHash() {
        return Arrays.hashCode(data) * 31;
    }

    public boolean hasName() { return data !=null;  }

    public boolean hasHash() {
        return hash!=0;
    }


    public void invalidate() {
        data = null;
    }

    @Override
    public byte[] bytes() {
        ensureNamed();
        return data;
    }

    @Override
    public char[] chars(final boolean pretty) {
        return charsByName();
    }


    @Override
    public int charsEstimated() {
        if (hasName())
            return data.length;
        return 16;
    }

    @Override
    public int hashCode() {
        ensureHashed();
        return hash;
    }

    private void ensureHashed() {
        if (!hasHash()) {
            this.hash = makeHash();
        }
    }


    protected void ensureNamed() {
        //should not need to do anything here in the constant/static impl
    }


    @Override
    public boolean equalTo(final Identifier x) {



        //if (x instanceof UTF8Identifier) {

            UTF8Identifier u = (UTF8Identifier) x;


            if (hashCode() != u.hashCode())
                return false;

            final byte[] ub = u.bytes();
            if (Utf8.equals2(bytes(), ub)) {
                //share the same data for fast equality test next time
                data = ub;
                return true;
            }

            return false;
        //}

        /*
        //this case should be avoided, it is wasteful
        System.err.println(this + " wasteful String comparison");
        return toString().equals(x.toString());
        */
    }

//    /*8 returns true only if the hashes both exist and are different */
//    public boolean unequalHash(final UTF8Identifier u) {
//        //if (!hasHash()) return false;
//        //if (!u.hasHash()) return false;
//        return hashCode()!=u.hashCode();
//    }


    @Override
    public int compare(final Identifier o) {
        int i = Integer.compare(hashCode(), o.hashCode());
        if (i == 0) {
            if (o instanceof UTF8Identifier)
                //rare case that hash is equal, do a value comparison
                return FastByteComparisons.compare(bytes(), ((LiteralUTF8Identifier) o).bytes());
            else {
                //this case should be avoided
                System.err.println(this + " wasteful String comparison");
                return toString().compareTo(o.toString());
            }
        }

        return i;
    }



    /** writes an expanded representation to a writer (output) */
    @Override public void append(Writer output, boolean pretty) throws IOException {
        //default behavior: string representation of name
        output.write(Utf8.fromUtf8ToChars(bytes()));
    }


    @Override
    public void delete() {
        data = null;
        hash = 0;
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** string representation formed by the UTF8 bytes() */
    public String stringFromBytes() {
        return Utf8.fromUtf8(bytes());
    }

}
