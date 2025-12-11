package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Decoder {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Decoder(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Decoder decoder) {
        if (decoder == null) {
            return 0L;
        }
        return decoder.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                PocketSphinxJNI.delete_Decoder(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public Decoder() {
        this(PocketSphinxJNI.new_Decoder__SWIG_0(), true);
    }

    public Decoder(Config config) {
        this(PocketSphinxJNI.new_Decoder__SWIG_1(Config.getCPtr(config), config), true);
    }

    public void reinit(Config config) {
        PocketSphinxJNI.Decoder_reinit(this.swigCPtr, this, Config.getCPtr(config), config);
    }

    public void loadDict(String str, String str2, String str3) {
        PocketSphinxJNI.Decoder_loadDict(this.swigCPtr, this, str, str2, str3);
    }

    public void saveDict(String str, String str2) {
        PocketSphinxJNI.Decoder_saveDict(this.swigCPtr, this, str, str2);
    }

    public void addWord(String str, String str2, int i) {
        PocketSphinxJNI.Decoder_addWord(this.swigCPtr, this, str, str2, i);
    }

    public String lookupWord(String str) {
        return PocketSphinxJNI.Decoder_lookupWord(this.swigCPtr, this, str);
    }

    public Lattice getLattice() {
        long jDecoder_getLattice = PocketSphinxJNI.Decoder_getLattice(this.swigCPtr, this);
        if (jDecoder_getLattice == 0) {
            return null;
        }
        return new Lattice(jDecoder_getLattice, false);
    }

    public Config getConfig() {
        long jDecoder_getConfig = PocketSphinxJNI.Decoder_getConfig(this.swigCPtr, this);
        if (jDecoder_getConfig == 0) {
            return null;
        }
        return new Config(jDecoder_getConfig, true);
    }

    public static Config defaultConfig() {
        long jDecoder_defaultConfig = PocketSphinxJNI.Decoder_defaultConfig();
        if (jDecoder_defaultConfig == 0) {
            return null;
        }
        return new Config(jDecoder_defaultConfig, true);
    }

    public static Config fileConfig(String str) {
        long jDecoder_fileConfig = PocketSphinxJNI.Decoder_fileConfig(str);
        if (jDecoder_fileConfig == 0) {
            return null;
        }
        return new Config(jDecoder_fileConfig, true);
    }

    public void startStream() {
        PocketSphinxJNI.Decoder_startStream(this.swigCPtr, this);
    }

    public void startUtt() {
        PocketSphinxJNI.Decoder_startUtt(this.swigCPtr, this);
    }

    public void endUtt() {
        PocketSphinxJNI.Decoder_endUtt(this.swigCPtr, this);
    }

    public int processRaw(short[] sArr, long j, boolean z, boolean z2) {
        return PocketSphinxJNI.Decoder_processRaw(this.swigCPtr, this, sArr, j, z, z2);
    }

    public void setRawdataSize(long j) {
        PocketSphinxJNI.Decoder_setRawdataSize(this.swigCPtr, this, j);
    }

    public short[] getRawdata() {
        return PocketSphinxJNI.Decoder_getRawdata(this.swigCPtr, this);
    }

    public Hypothesis hyp() {
        long jDecoder_hyp = PocketSphinxJNI.Decoder_hyp(this.swigCPtr, this);
        if (jDecoder_hyp == 0) {
            return null;
        }
        return new Hypothesis(jDecoder_hyp, true);
    }

    public FrontEnd getFe() {
        long jDecoder_getFe = PocketSphinxJNI.Decoder_getFe(this.swigCPtr, this);
        if (jDecoder_getFe == 0) {
            return null;
        }
        return new FrontEnd(jDecoder_getFe, false);
    }

    public Feature getFeat() {
        long jDecoder_getFeat = PocketSphinxJNI.Decoder_getFeat(this.swigCPtr, this);
        if (jDecoder_getFeat == 0) {
            return null;
        }
        return new Feature(jDecoder_getFeat, false);
    }

    public boolean getInSpeech() {
        return PocketSphinxJNI.Decoder_getInSpeech(this.swigCPtr, this);
    }

    public FsgModel getFsg(String str) {
        long jDecoder_getFsg = PocketSphinxJNI.Decoder_getFsg(this.swigCPtr, this, str);
        if (jDecoder_getFsg == 0) {
            return null;
        }
        return new FsgModel(jDecoder_getFsg, false);
    }

    public void setFsg(String str, FsgModel fsgModel) {
        PocketSphinxJNI.Decoder_setFsg(this.swigCPtr, this, str, FsgModel.getCPtr(fsgModel), fsgModel);
    }

    public void setJsgfFile(String str, String str2) {
        PocketSphinxJNI.Decoder_setJsgfFile(this.swigCPtr, this, str, str2);
    }

    public void setJsgfString(String str, String str2) {
        PocketSphinxJNI.Decoder_setJsgfString(this.swigCPtr, this, str, str2);
    }

    public String getKws(String str) {
        return PocketSphinxJNI.Decoder_getKws(this.swigCPtr, this, str);
    }

    public void setKws(String str, String str2) {
        PocketSphinxJNI.Decoder_setKws(this.swigCPtr, this, str, str2);
    }

    public void setKeyphrase(String str, String str2) {
        PocketSphinxJNI.Decoder_setKeyphrase(this.swigCPtr, this, str, str2);
    }

    public void setAllphoneFile(String str, String str2) {
        PocketSphinxJNI.Decoder_setAllphoneFile(this.swigCPtr, this, str, str2);
    }

    public NGramModel getLm(String str) {
        long jDecoder_getLm = PocketSphinxJNI.Decoder_getLm(this.swigCPtr, this, str);
        if (jDecoder_getLm == 0) {
            return null;
        }
        return new NGramModel(jDecoder_getLm, true);
    }

    public void setLm(String str, NGramModel nGramModel) {
        PocketSphinxJNI.Decoder_setLm(this.swigCPtr, this, str, NGramModel.getCPtr(nGramModel), nGramModel);
    }

    public void setLmFile(String str, String str2) {
        PocketSphinxJNI.Decoder_setLmFile(this.swigCPtr, this, str, str2);
    }

    public LogMath getLogmath() {
        long jDecoder_getLogmath = PocketSphinxJNI.Decoder_getLogmath(this.swigCPtr, this);
        if (jDecoder_getLogmath == 0) {
            return null;
        }
        return new LogMath(jDecoder_getLogmath, true);
    }

    public void setSearch(String str) {
        PocketSphinxJNI.Decoder_setSearch(this.swigCPtr, this, str);
    }

    public String getSearch() {
        return PocketSphinxJNI.Decoder_getSearch(this.swigCPtr, this);
    }

    public int nFrames() {
        return PocketSphinxJNI.Decoder_nFrames(this.swigCPtr, this);
    }

    public SegmentList seg() {
        long jDecoder_seg = PocketSphinxJNI.Decoder_seg(this.swigCPtr, this);
        if (jDecoder_seg == 0) {
            return null;
        }
        return new SegmentList(jDecoder_seg, false);
    }

    public NBestList nbest() {
        long jDecoder_nbest = PocketSphinxJNI.Decoder_nbest(this.swigCPtr, this);
        if (jDecoder_nbest == 0) {
            return null;
        }
        return new NBestList(jDecoder_nbest, false);
    }
}
