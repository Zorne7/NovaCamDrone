package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Feature {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Feature(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Feature feature) {
        if (feature == null) {
            return 0L;
        }
        return feature.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_Feature(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public Feature(SWIGTYPE_p_feat_t sWIGTYPE_p_feat_t) {
        this(SphinxBaseJNI.new_Feature(SWIGTYPE_p_feat_t.getCPtr(sWIGTYPE_p_feat_t)), true);
    }
}
