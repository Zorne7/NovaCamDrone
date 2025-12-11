package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class FrontEnd {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected FrontEnd(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(FrontEnd frontEnd) {
        if (frontEnd == null) {
            return 0L;
        }
        return frontEnd.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_FrontEnd(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public FrontEnd(SWIGTYPE_p_fe_t sWIGTYPE_p_fe_t) {
        this(SphinxBaseJNI.new_FrontEnd(SWIGTYPE_p_fe_t.getCPtr(sWIGTYPE_p_fe_t)), true);
    }

    public int outputSize() {
        return SphinxBaseJNI.FrontEnd_outputSize(this.swigCPtr, this);
    }

    public void startUtt() {
        SphinxBaseJNI.FrontEnd_startUtt(this.swigCPtr, this);
    }

    public int processUtt(SWIGTYPE_p_int16 sWIGTYPE_p_int16, long j, SWIGTYPE_p_p_p_mfcc_t sWIGTYPE_p_p_p_mfcc_t) {
        return SphinxBaseJNI.FrontEnd_processUtt(this.swigCPtr, this, SWIGTYPE_p_int16.getCPtr(sWIGTYPE_p_int16), j, SWIGTYPE_p_p_p_mfcc_t.getCPtr(sWIGTYPE_p_p_p_mfcc_t));
    }

    public int endUtt(SWIGTYPE_p_mfcc_t sWIGTYPE_p_mfcc_t) {
        return SphinxBaseJNI.FrontEnd_endUtt(this.swigCPtr, this, SWIGTYPE_p_mfcc_t.getCPtr(sWIGTYPE_p_mfcc_t));
    }
}
