package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class NGramModelSetIterator implements Iterator<NGramModel> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NGramModelSetIterator(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NGramModelSetIterator nGramModelSetIterator) {
        if (nGramModelSetIterator == null) {
            return 0L;
        }
        return nGramModelSetIterator.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModelSetIterator(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void setPtr(SWIGTYPE_p_ngram_model_set_iter_t sWIGTYPE_p_ngram_model_set_iter_t) {
        SphinxBaseJNI.NGramModelSetIterator_ptr_set(this.swigCPtr, this, SWIGTYPE_p_ngram_model_set_iter_t.getCPtr(sWIGTYPE_p_ngram_model_set_iter_t));
    }

    public SWIGTYPE_p_ngram_model_set_iter_t getPtr() {
        long jNGramModelSetIterator_ptr_get = SphinxBaseJNI.NGramModelSetIterator_ptr_get(this.swigCPtr, this);
        if (jNGramModelSetIterator_ptr_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_ngram_model_set_iter_t(jNGramModelSetIterator_ptr_get, false);
    }

    public NGramModelSetIterator(SWIGTYPE_p_ngram_model_set_iter_t sWIGTYPE_p_ngram_model_set_iter_t) {
        this(SphinxBaseJNI.new_NGramModelSetIterator(SWIGTYPE_p_ngram_model_set_iter_t.getCPtr(sWIGTYPE_p_ngram_model_set_iter_t)), true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public NGramModel next() {
        long jNGramModelSetIterator_next = SphinxBaseJNI.NGramModelSetIterator_next(this.swigCPtr, this);
        if (jNGramModelSetIterator_next == 0) {
            return null;
        }
        return new NGramModel(jNGramModelSetIterator_next, true);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return SphinxBaseJNI.NGramModelSetIterator_hasNext(this.swigCPtr, this);
    }
}
