package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class NGramModel {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected NGramModel(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(NGramModel nGramModel) {
        if (nGramModel == null) {
            return 0L;
        }
        return nGramModel.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_NGramModel(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public static NGramModel fromIter(SWIGTYPE_p_ngram_model_set_iter_t sWIGTYPE_p_ngram_model_set_iter_t) {
        long jNGramModel_fromIter = SphinxBaseJNI.NGramModel_fromIter(SWIGTYPE_p_ngram_model_set_iter_t.getCPtr(sWIGTYPE_p_ngram_model_set_iter_t));
        if (jNGramModel_fromIter == 0) {
            return null;
        }
        return new NGramModel(jNGramModel_fromIter, false);
    }

    public NGramModel(String str) {
        this(SphinxBaseJNI.new_NGramModel__SWIG_0(str), true);
    }

    public NGramModel(Config config, LogMath logMath, String str) {
        this(SphinxBaseJNI.new_NGramModel__SWIG_1(Config.getCPtr(config), config, LogMath.getCPtr(logMath), logMath, str), true);
    }

    public void write(String str, SWIGTYPE_p_ngram_file_type_t sWIGTYPE_p_ngram_file_type_t) {
        SphinxBaseJNI.NGramModel_write(this.swigCPtr, this, str, SWIGTYPE_p_ngram_file_type_t.getCPtr(sWIGTYPE_p_ngram_file_type_t));
    }

    public SWIGTYPE_p_ngram_file_type_t strToType(String str) {
        return new SWIGTYPE_p_ngram_file_type_t(SphinxBaseJNI.NGramModel_strToType(this.swigCPtr, this, str), true);
    }

    public String typeToStr(int i) {
        return SphinxBaseJNI.NGramModel_typeToStr(this.swigCPtr, this, i);
    }

    public void casefold(int i) {
        SphinxBaseJNI.NGramModel_casefold(this.swigCPtr, this, i);
    }

    public int size() {
        return SphinxBaseJNI.NGramModel_size(this.swigCPtr, this);
    }

    public int addWord(String str, SWIGTYPE_p_float32 sWIGTYPE_p_float32) {
        return SphinxBaseJNI.NGramModel_addWord(this.swigCPtr, this, str, SWIGTYPE_p_float32.getCPtr(sWIGTYPE_p_float32));
    }

    public int prob(long j, SWIGTYPE_p_p_char sWIGTYPE_p_p_char) {
        return SphinxBaseJNI.NGramModel_prob(this.swigCPtr, this, j, SWIGTYPE_p_p_char.getCPtr(sWIGTYPE_p_p_char));
    }
}
