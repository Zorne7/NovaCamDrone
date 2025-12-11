package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class NBestIterator implements Iterator<NBest> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NBestIterator(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NBestIterator nBestIterator) {
        if (nBestIterator == null) {
            return 0L;
        }
        return nBestIterator.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBestIterator(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void setPtr(SWIGTYPE_p_ps_nbest_t sWIGTYPE_p_ps_nbest_t) {
        PocketSphinxJNI.NBestIterator_ptr_set(this.swigCPtr, this, SWIGTYPE_p_ps_nbest_t.getCPtr(sWIGTYPE_p_ps_nbest_t));
    }

    public SWIGTYPE_p_ps_nbest_t getPtr() {
        long jNBestIterator_ptr_get = PocketSphinxJNI.NBestIterator_ptr_get(this.swigCPtr, this);
        if (jNBestIterator_ptr_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_ps_nbest_t(jNBestIterator_ptr_get, false);
    }

    public NBestIterator(SWIGTYPE_p_ps_nbest_t sWIGTYPE_p_ps_nbest_t) {
        this(PocketSphinxJNI.new_NBestIterator(SWIGTYPE_p_ps_nbest_t.getCPtr(sWIGTYPE_p_ps_nbest_t)), true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public NBest next() {
        long jNBestIterator_next = PocketSphinxJNI.NBestIterator_next(this.swigCPtr, this);
        if (jNBestIterator_next == 0) {
            return null;
        }
        return new NBest(jNBestIterator_next, true);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return PocketSphinxJNI.NBestIterator_hasNext(this.swigCPtr, this);
    }
}
