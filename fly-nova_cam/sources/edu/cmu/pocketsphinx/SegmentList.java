package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class SegmentList implements Iterable<Segment> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected SegmentList(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(SegmentList segmentList) {
        if (segmentList == null) {
            return 0L;
        }
        return segmentList.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_SegmentList(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.lang.Iterable
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public Iterator<Segment> iterator2() {
        long jSegmentList_iterator = PocketSphinxJNI.SegmentList_iterator(this.swigCPtr, this);
        if (jSegmentList_iterator == 0) {
            return null;
        }
        return new SegmentIterator(jSegmentList_iterator, true);
    }
}
