package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Segment {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Segment(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Segment segment) {
        if (segment == null) {
            return 0L;
        }
        return segment.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Segment(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setWord(String str) {
        PocketSphinxJNI.Segment_word_set(this.swigCPtr, this, str);
    }

    public String getWord() {
        return PocketSphinxJNI.Segment_word_get(this.swigCPtr, this);
    }

    public void setAscore(int i) {
        PocketSphinxJNI.Segment_ascore_set(this.swigCPtr, this, i);
    }

    public int getAscore() {
        return PocketSphinxJNI.Segment_ascore_get(this.swigCPtr, this);
    }

    public void setLscore(int i) {
        PocketSphinxJNI.Segment_lscore_set(this.swigCPtr, this, i);
    }

    public int getLscore() {
        return PocketSphinxJNI.Segment_lscore_get(this.swigCPtr, this);
    }

    public void setLback(int i) {
        PocketSphinxJNI.Segment_lback_set(this.swigCPtr, this, i);
    }

    public int getLback() {
        return PocketSphinxJNI.Segment_lback_get(this.swigCPtr, this);
    }

    public void setProb(int i) {
        PocketSphinxJNI.Segment_prob_set(this.swigCPtr, this, i);
    }

    public int getProb() {
        return PocketSphinxJNI.Segment_prob_get(this.swigCPtr, this);
    }

    public void setStartFrame(int i) {
        PocketSphinxJNI.Segment_startFrame_set(this.swigCPtr, this, i);
    }

    public int getStartFrame() {
        return PocketSphinxJNI.Segment_startFrame_get(this.swigCPtr, this);
    }

    public void setEndFrame(int i) {
        PocketSphinxJNI.Segment_endFrame_set(this.swigCPtr, this, i);
    }

    public int getEndFrame() {
        return PocketSphinxJNI.Segment_endFrame_get(this.swigCPtr, this);
    }

    public static Segment fromIter(SWIGTYPE_p_ps_seg_t sWIGTYPE_p_ps_seg_t) {
        long jSegment_fromIter = PocketSphinxJNI.Segment_fromIter(SWIGTYPE_p_ps_seg_t.getCPtr(sWIGTYPE_p_ps_seg_t));
        if (jSegment_fromIter == 0) {
            return null;
        }
        return new Segment(jSegment_fromIter, false);
    }

    public Segment() {
        this(PocketSphinxJNI.new_segment(), true);
    }
}
