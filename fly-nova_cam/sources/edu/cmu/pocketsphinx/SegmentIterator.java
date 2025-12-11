package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class SegmentIterator implements Iterator<Segment> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected SegmentIterator(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(SegmentIterator segmentIterator) {
        if (segmentIterator == null) {
            return 0L;
        }
        return segmentIterator.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_SegmentIterator(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void setPtr(SWIGTYPE_p_ps_seg_t sWIGTYPE_p_ps_seg_t) {
        PocketSphinxJNI.SegmentIterator_ptr_set(this.swigCPtr, this, SWIGTYPE_p_ps_seg_t.getCPtr(sWIGTYPE_p_ps_seg_t));
    }

    public SWIGTYPE_p_ps_seg_t getPtr() {
        long jSegmentIterator_ptr_get = PocketSphinxJNI.SegmentIterator_ptr_get(this.swigCPtr, this);
        if (jSegmentIterator_ptr_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_ps_seg_t(jSegmentIterator_ptr_get, false);
    }

    public SegmentIterator(SWIGTYPE_p_ps_seg_t sWIGTYPE_p_ps_seg_t) {
        this(PocketSphinxJNI.new_SegmentIterator(SWIGTYPE_p_ps_seg_t.getCPtr(sWIGTYPE_p_ps_seg_t)), true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Segment next() {
        long jSegmentIterator_next = PocketSphinxJNI.SegmentIterator_next(this.swigCPtr, this);
        if (jSegmentIterator_next == 0) {
            return null;
        }
        return new Segment(jSegmentIterator_next, true);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return PocketSphinxJNI.SegmentIterator_hasNext(this.swigCPtr, this);
    }
}
