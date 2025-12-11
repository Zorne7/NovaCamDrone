package edu.cmu.pocketsphinx;

import java.util.Iterator;

/* loaded from: classes.dex */
public class Jsgf implements Iterable<JsgfRule> {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Jsgf(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Jsgf jsgf) {
        if (jsgf == null) {
            return 0L;
        }
        return jsgf.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_Jsgf(j);
            }
            this.swigCPtr = 0L;
        }
    }

    @Override // java.lang.Iterable
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    public Iterator<JsgfRule> iterator2() {
        long jJsgf_iterator = SphinxBaseJNI.Jsgf_iterator(this.swigCPtr, this);
        if (jJsgf_iterator == 0) {
            return null;
        }
        return new JsgfIterator(jJsgf_iterator, true);
    }

    public Jsgf(String str) {
        this(SphinxBaseJNI.new_Jsgf(str), true);
    }

    public String name() {
        return SphinxBaseJNI.Jsgf_name(this.swigCPtr, this);
    }

    public JsgfRule getRule(String str) {
        long jJsgf_getRule = SphinxBaseJNI.Jsgf_getRule(this.swigCPtr, this, str);
        if (jJsgf_getRule == 0) {
            return null;
        }
        return new JsgfRule(jJsgf_getRule, false);
    }

    public FsgModel buildFsg(JsgfRule jsgfRule, LogMath logMath, float f) {
        long jJsgf_buildFsg = SphinxBaseJNI.Jsgf_buildFsg(this.swigCPtr, this, JsgfRule.getCPtr(jsgfRule), jsgfRule, LogMath.getCPtr(logMath), logMath, f);
        if (jJsgf_buildFsg == 0) {
            return null;
        }
        return new FsgModel(jJsgf_buildFsg, false);
    }
}
