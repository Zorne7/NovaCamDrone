package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class FsgModel {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected FsgModel(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(FsgModel fsgModel) {
        if (fsgModel == null) {
            return 0L;
        }
        return fsgModel.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_FsgModel(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public FsgModel(String str, LogMath logMath, float f, int i) {
        this(SphinxBaseJNI.new_FsgModel__SWIG_0(str, LogMath.getCPtr(logMath), logMath, f, i), true);
    }

    public FsgModel(SWIGTYPE_p_fsg_model_t sWIGTYPE_p_fsg_model_t) {
        this(SphinxBaseJNI.new_FsgModel__SWIG_1(SWIGTYPE_p_fsg_model_t.getCPtr(sWIGTYPE_p_fsg_model_t)), true);
    }

    public FsgModel(String str, LogMath logMath, float f) {
        this(SphinxBaseJNI.new_FsgModel__SWIG_2(str, LogMath.getCPtr(logMath), logMath, f), true);
    }

    public int wordId(String str) {
        return SphinxBaseJNI.FsgModel_wordId(this.swigCPtr, this, str);
    }

    public int wordAdd(String str) {
        return SphinxBaseJNI.FsgModel_wordAdd(this.swigCPtr, this, str);
    }

    public void transAdd(int i, int i2, int i3, int i4) {
        SphinxBaseJNI.FsgModel_transAdd(this.swigCPtr, this, i, i2, i3, i4);
    }

    public int nullTransAdd(int i, int i2, int i3) {
        return SphinxBaseJNI.FsgModel_nullTransAdd(this.swigCPtr, this, i, i2, i3);
    }

    public int tagTransAdd(int i, int i2, int i3, int i4) {
        return SphinxBaseJNI.FsgModel_tagTransAdd(this.swigCPtr, this, i, i2, i3, i4);
    }

    public int addSilence(String str, int i, float f) {
        return SphinxBaseJNI.FsgModel_addSilence(this.swigCPtr, this, str, i, f);
    }

    public int addAlt(String str, String str2) {
        return SphinxBaseJNI.FsgModel_addAlt(this.swigCPtr, this, str, str2);
    }

    public void writefile(String str) {
        SphinxBaseJNI.FsgModel_writefile(this.swigCPtr, this, str);
    }
}
