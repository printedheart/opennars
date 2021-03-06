package nars.util.data.id;

import nars.util.data.Util;
import nars.util.utf8.Byted;
import nars.util.utf8.Utf8;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * Constant-value UTF8 identifier, populated by String or byte[] on construction
 */
public class LiteralUTF8Identifier extends UTF8Identifier {

    protected byte[] data = null;
    transient protected int hash = 0;


    /** do nothing, used by subclass */
    protected LiteralUTF8Identifier() { }

    public LiteralUTF8Identifier(byte[] b) {
        setData(b);
    }

    public LiteralUTF8Identifier(byte[] b, int start, int stop) {
        int len = stop - start;
        byte[] d = new byte[len];
        System.arraycopy(b, start, d, 0, len);
        setData(d);
    }

    protected void setData(final byte[] d) {
        setBytes(d);
        this.hash = makeHash();
    }

    public void rehash() {
        this.hash = makeHash();
    }

    public LiteralUTF8Identifier(final byte[] prefix, final byte[] suffix) {
        int plen = prefix.length;
        int slen = suffix.length;
        int len = plen + slen;
        byte[] d = new byte[len];
        System.arraycopy(prefix, 0, d, 0, plen);
        System.arraycopy(suffix, 0, d, plen, slen);
        setData(d);
    }
    public LiteralUTF8Identifier(final byte[] prefix, byte separator, final byte[] suffix) {
        int plen = prefix.length;
        int slen = suffix.length;
        int len = plen + slen + 1;
        final byte[] d = new byte[len];
        System.arraycopy(prefix, 0, d, 0, plen);
        d[plen] = separator;
        System.arraycopy(suffix, 0, d, plen+1, slen);
        setData(d);
    }

    public LiteralUTF8Identifier(String s) {
        this(Utf8.toUtf8(s));
    }


    final public int makeHash() {
        return (int) Util.ELFHash(data, 0);
    }

    final public boolean hasName() { return data !=null;  }

/*    public boolean hasHash() {
        return hash!=0;
    }*/


    public void invalidate() {
        data = null;
        hash = 0;
    }

    @Override
    public byte[] bytes() {
        ensureNamed();
        return data;
    }

    protected void ensureNamed() {

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
        return hash;
    }

//    private void ensureHashed() {
//        if (!hasHash()) {
//            this.hash = makeHash();
//        }
//    }




    @Override
    public boolean equals(final Object x) {
        if (this == x) return true;
        if (!(x instanceof LiteralUTF8Identifier)) return false;
        return Byted.equals(this, (Byted)x);
    }

    @Override
    public int compareTo(final Object o) {
        if (this == o ) return 0;
        final Class oc = o.getClass();
        final Class c = getClass();
        if (o.getClass() == getClass()) {
            return Byted.compare(this, (Byted)o);
        }
        return Integer.compare(oc.hashCode(), c.hashCode());
    }

//    public int compare(final UTF8Identifier o) {
//        int i = Integer.compare(hashCode(), o.hashCode());
//        if (i == 0) {
//            if (o instanceof UTF8Identifier)
//                //rare case that hash is equal, do a value comparison
//                return FastByteComparisons.compare(bytes(), ((LiteralUTF8Identifier) o).bytes());
//            else {
//                //this case should be avoided
//                System.err.println(this + " wasteful String comparison");
//                return toString().compareTo(o.toString());
//            }
//        }
//
//        return i;
//    }



    /** writes an expanded representation to a writer (output) */
    @Override public void append(Appendable output, boolean pretty) throws IOException {
        //default behavior: string representation of name
        byte[] b = bytes();
        if (b.length == 1)
            output.append((char)b[0]);
        else {
            output.append(CharBuffer.wrap(Utf8.fromUtf8ToChars(b)));
        }
    }


    @Override
    public void delete() {
        invalidate();
    }

    @Override
    public String toString() {
        return toString(true);
    }

    /** string representation formed by the UTF8 bytes() */
    public String stringFromBytes() {
        return Utf8.fromUtf8(bytes());
    }

    /** this should only be used when setting a value and the hash will be invalidated,
     * or setting an equivalent value where the hash would remain the same.
     * for all other purposes, use setData() which will do a complete update
     */
    @Override public final void setBytes(byte[] b) {
        this.data = b;
    }
}
