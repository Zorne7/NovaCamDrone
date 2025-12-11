package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Lattice {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Lattice(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Lattice lattice) {
        if (lattice == null) {
            return 0L;
        }
        return lattice.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Lattice(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public Lattice(String str) {
        this(PocketSphinxJNI.new_Lattice__SWIG_0(str), true);
    }

    public Lattice(Decoder decoder, String str) {
        this(PocketSphinxJNI.new_Lattice__SWIG_1(Decoder.getCPtr(decoder), decoder, str), true);
    }

    public void write(String str) {
        PocketSphinxJNI.Lattice_write(this.swigCPtr, this, str);
    }

    public void writeHtk(String str) {
        PocketSphinxJNI.Lattice_writeHtk(this.swigCPtr, this, str);
    }
}
