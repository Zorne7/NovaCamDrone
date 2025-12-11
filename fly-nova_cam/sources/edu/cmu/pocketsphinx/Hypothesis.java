package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Hypothesis {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Hypothesis(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Hypothesis hypothesis) {
        if (hypothesis == null) {
            return 0L;
        }
        return hypothesis.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Hypothesis(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setHypstr(String str) {
        PocketSphinxJNI.Hypothesis_hypstr_set(this.swigCPtr, this, str);
    }

    public String getHypstr() {
        return PocketSphinxJNI.Hypothesis_hypstr_get(this.swigCPtr, this);
    }

    public void setBestScore(int i) {
        PocketSphinxJNI.Hypothesis_bestScore_set(this.swigCPtr, this, i);
    }

    public int getBestScore() {
        return PocketSphinxJNI.Hypothesis_bestScore_get(this.swigCPtr, this);
    }

    public void setProb(int i) {
        PocketSphinxJNI.Hypothesis_prob_set(this.swigCPtr, this, i);
    }

    public int getProb() {
        return PocketSphinxJNI.Hypothesis_prob_get(this.swigCPtr, this);
    }

    public Hypothesis(String str, int i, int i2) {
        this(PocketSphinxJNI.new_Hypothesis(str, i, i2), true);
    }
}
