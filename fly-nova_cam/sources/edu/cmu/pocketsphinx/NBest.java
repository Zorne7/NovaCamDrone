package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class NBest {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NBest(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NBest nBest) {
        if (nBest == null) {
            return 0L;
        }
        return nBest.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_NBest(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setHypstr(String str) {
        PocketSphinxJNI.NBest_hypstr_set(this.swigCPtr, this, str);
    }

    public String getHypstr() {
        return PocketSphinxJNI.NBest_hypstr_get(this.swigCPtr, this);
    }

    public void setScore(int i) {
        PocketSphinxJNI.NBest_score_set(this.swigCPtr, this, i);
    }

    public int getScore() {
        return PocketSphinxJNI.NBest_score_get(this.swigCPtr, this);
    }

    public static NBest fromIter(SWIGTYPE_p_ps_nbest_t sWIGTYPE_p_ps_nbest_t) {
        long jNBest_fromIter = PocketSphinxJNI.NBest_fromIter(SWIGTYPE_p_ps_nbest_t.getCPtr(sWIGTYPE_p_ps_nbest_t));
        if (jNBest_fromIter == 0) {
            return null;
        }
        return new NBest(jNBest_fromIter, false);
    }

    public Hypothesis hyp() {
        long jNBest_hyp = PocketSphinxJNI.NBest_hyp(this.swigCPtr, this);
        if (jNBest_hyp == 0) {
            return null;
        }
        return new Hypothesis(jNBest_hyp, true);
    }

    public NBest() {
        this(PocketSphinxJNI.new_nBest(), true);
    }
}
