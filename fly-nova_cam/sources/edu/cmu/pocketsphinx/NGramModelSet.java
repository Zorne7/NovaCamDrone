package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class NGramModelSet implements Iterable<NGramModel> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NGramModelSet(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NGramModelSet nGramModelSet) {
        if (nGramModelSet == null) {
            return 0L;
        }
        return nGramModelSet.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModelSet(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.lang.Iterable
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public Iterator<NGramModel> iterator2() {
        long jNGramModelSet_iterator = SphinxBaseJNI.NGramModelSet_iterator(this.swigCPtr, this);
        if (jNGramModelSet_iterator == 0) {
            return null;
        }
        return new NGramModelSetIterator(jNGramModelSet_iterator, true);
    }

    public NGramModelSet(Config config, LogMath logMath, String str) {
        this(SphinxBaseJNI.new_NGramModelSet(Config.getCPtr(config), config, LogMath.getCPtr(logMath), logMath, str), true);
    }

    public int count() {
        return SphinxBaseJNI.NGramModelSet_count(this.swigCPtr, this);
    }

    public NGramModel add(NGramModel nGramModel, String str, float f, boolean z) {
        long jNGramModelSet_add = SphinxBaseJNI.NGramModelSet_add(this.swigCPtr, this, NGramModel.getCPtr(nGramModel), nGramModel, str, f, z);
        if (jNGramModelSet_add == 0) {
            return null;
        }
        return new NGramModel(jNGramModelSet_add, false);
    }

    public NGramModel select(String str) {
        long jNGramModelSet_select = SphinxBaseJNI.NGramModelSet_select(this.swigCPtr, this, str);
        if (jNGramModelSet_select == 0) {
            return null;
        }
        return new NGramModel(jNGramModelSet_select, false);
    }

    public NGramModel lookup(String str) {
        long jNGramModelSet_lookup = SphinxBaseJNI.NGramModelSet_lookup(this.swigCPtr, this, str);
        if (jNGramModelSet_lookup == 0) {
            return null;
        }
        return new NGramModel(jNGramModelSet_lookup, false);
    }

    public String current() {
        return SphinxBaseJNI.NGramModelSet_current(this.swigCPtr, this);
    }
}
