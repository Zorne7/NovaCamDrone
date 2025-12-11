package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class JsgfIterator implements Iterator<JsgfRule> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected JsgfIterator(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(JsgfIterator jsgfIterator) {
        if (jsgfIterator == null) {
            return 0L;
        }
        return jsgfIterator.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_JsgfIterator(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public void setPtr(SWIGTYPE_p_jsgf_rule_iter_t sWIGTYPE_p_jsgf_rule_iter_t) {
        SphinxBaseJNI.JsgfIterator_ptr_set(this.swigCPtr, this, SWIGTYPE_p_jsgf_rule_iter_t.getCPtr(sWIGTYPE_p_jsgf_rule_iter_t));
    }

    public SWIGTYPE_p_jsgf_rule_iter_t getPtr() {
        long jJsgfIterator_ptr_get = SphinxBaseJNI.JsgfIterator_ptr_get(this.swigCPtr, this);
        if (jJsgfIterator_ptr_get == 0) {
            return null;
        }
        return new SWIGTYPE_p_jsgf_rule_iter_t(jJsgfIterator_ptr_get, false);
    }

    public JsgfIterator(SWIGTYPE_p_jsgf_rule_iter_t sWIGTYPE_p_jsgf_rule_iter_t) {
        this(SphinxBaseJNI.new_JsgfIterator(SWIGTYPE_p_jsgf_rule_iter_t.getCPtr(sWIGTYPE_p_jsgf_rule_iter_t)), true);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public JsgfRule next() {
        long jJsgfIterator_next = SphinxBaseJNI.JsgfIterator_next(this.swigCPtr, this);
        if (jJsgfIterator_next == 0) {
            return null;
        }
        return new JsgfRule(jJsgfIterator_next, true);
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return SphinxBaseJNI.JsgfIterator_hasNext(this.swigCPtr, this);
    }
}
