package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class NBestList implements Iterable<NBest> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NBestList(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NBestList nBestList) {
        if (nBestList == null) {
            return 0L;
        }
        return nBestList.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBestList(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.lang.Iterable
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public Iterator<NBest> iterator2() {
        long jNBestList_iterator = PocketSphinxJNI.NBestList_iterator(this.swigCPtr, this);
        if (jNBestList_iterator == 0) {
            return null;
        }
        return new NBestIterator(jNBestList_iterator, true);
    }
}
