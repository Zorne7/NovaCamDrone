package edu.cmu.pocketsphinx;

import java.io.File;
import java.io.IOException;

/* loaded from: classes.dex */
public class SpeechRecognizerSetup {
    private final Config config;

    static {
        System.loadLibrary("pocketsphinx_jni");
    }

    public static SpeechRecognizerSetup defaultSetup() {
        return new SpeechRecognizerSetup(Decoder.defaultConfig());
    }

    public static SpeechRecognizerSetup setupFromFile(File file) {
        return new SpeechRecognizerSetup(Decoder.fileConfig(file.getPath()));
    }

    private SpeechRecognizerSetup(Config config) {
        this.config = config;
    }

    public SpeechRecognizer getRecognizer() throws IOException {
        return new SpeechRecognizer(this.config);
    }

    public SpeechRecognizerSetup setAcousticModel(File file) {
        return setString("-hmm", file.getPath());
    }

    public SpeechRecognizerSetup setDictionary(File file) {
        return setString("-dict", file.getPath());
    }

    public SpeechRecognizerSetup setSampleRate(int i) {
        return setFloat("-samprate", i);
    }

    public SpeechRecognizerSetup setRawLogDir(File file) {
        return setString("-rawlogdir", file.getPath());
    }

    public SpeechRecognizerSetup setKeywordThreshold(float f) {
        return setFloat("-kws_threshold", f);
    }

    public SpeechRecognizerSetup setBoolean(String str, boolean z) {
        this.config.setBoolean(str, z);
        return this;
    }

    public SpeechRecognizerSetup setInteger(String str, int i) {
        this.config.setInt(str, i);
        return this;
    }

    public SpeechRecognizerSetup setFloat(String str, double d) {
        this.config.setFloat(str, d);
        return this;
    }

    public SpeechRecognizerSetup setString(String str, String str2) {
        this.config.setString(str, str2);
        return this;
    }
}
