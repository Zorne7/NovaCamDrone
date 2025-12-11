package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class LogMath {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected LogMath(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(LogMath logMath) {
        if (logMath == null) {
            return 0L;
        }
        return logMath.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_LogMath(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public LogMath() {
        this(SphinxBaseJNI.new_LogMath__SWIG_0(), true);
    }

    public LogMath(SWIGTYPE_p_logmath_t sWIGTYPE_p_logmath_t) {
        this(SphinxBaseJNI.new_LogMath__SWIG_1(SWIGTYPE_p_logmath_t.getCPtr(sWIGTYPE_p_logmath_t)), true);
    }

    public double exp(int i) {
        return SphinxBaseJNI.LogMath_exp(this.swigCPtr, this, i);
    }
}
