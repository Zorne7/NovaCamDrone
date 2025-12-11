package edu.cmu.pocketsphinx;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

/* loaded from: classes.dex */
public class SpeechRecognizer {
    private static final float BUFFER_SIZE_SECONDS = 0.4f;
    protected static final String TAG = "SpeechRecognizer";
    private int bufferSize;
    private final Decoder decoder;
    private Thread recognizerThread;
    private final AudioRecord recorder;
    private final int sampleRate;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Collection<RecognitionListener> listeners = new HashSet();

    protected SpeechRecognizer(Config config) throws IOException {
        Decoder decoder = new Decoder(config);
        this.decoder = decoder;
        int i = (int) decoder.getConfig().getFloat("-samprate");
        this.sampleRate = i;
        this.bufferSize = Math.round(i * BUFFER_SIZE_SECONDS);
        AudioRecord audioRecord = new AudioRecord(6, i, 16, 2, this.bufferSize * 2);
        this.recorder = audioRecord;
        if (audioRecord.getState() != 0) {
            return;
        }
        audioRecord.release();
        throw new IOException("Failed to initialize recorder. Microphone might be already in use.");
    }

    public void addListener(RecognitionListener recognitionListener) {
        synchronized (this.listeners) {
            this.listeners.add(recognitionListener);
        }
    }

    public void removeListener(RecognitionListener recognitionListener) {
        synchronized (this.listeners) {
            this.listeners.remove(recognitionListener);
        }
    }

    public boolean startListening(String str) {
        if (this.recognizerThread != null) {
            return false;
        }
        Log.i(TAG, String.format("Start recognition \"%s\"", str));
        this.decoder.setSearch(str);
        RecognizerThread recognizerThread = new RecognizerThread(this);
        this.recognizerThread = recognizerThread;
        recognizerThread.start();
        return true;
    }

    public boolean startListening(String str, int i) {
        if (this.recognizerThread != null) {
            return false;
        }
        Log.i(TAG, String.format("Start recognition \"%s\"", str));
        this.decoder.setSearch(str);
        RecognizerThread recognizerThread = new RecognizerThread(i);
        this.recognizerThread = recognizerThread;
        recognizerThread.start();
        return true;
    }

    private boolean stopRecognizerThread() throws InterruptedException {
        Thread thread = this.recognizerThread;
        if (thread == null) {
            return false;
        }
        try {
            thread.interrupt();
            this.recognizerThread.join();
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
        }
        this.recognizerThread = null;
        return true;
    }

    public boolean stop() throws InterruptedException {
        boolean zStopRecognizerThread = stopRecognizerThread();
        if (zStopRecognizerThread) {
            Log.i(TAG, "Stop recognition");
            this.mainHandler.post(new ResultEvent(this.decoder.hyp(), true));
        }
        return zStopRecognizerThread;
    }

    public boolean cancel() throws InterruptedException {
        boolean zStopRecognizerThread = stopRecognizerThread();
        if (zStopRecognizerThread) {
            Log.i(TAG, "Cancel recognition");
        }
        return zStopRecognizerThread;
    }

    public Decoder getDecoder() {
        return this.decoder;
    }

    public void shutdown() {
        this.recorder.release();
    }

    public String getSearchName() {
        return this.decoder.getSearch();
    }

    public void addFsgSearch(String str, FsgModel fsgModel) {
        this.decoder.setFsg(str, fsgModel);
    }

    public void addGrammarSearch(String str, File file) {
        Log.i(TAG, String.format("Load JSGF %s", file));
        this.decoder.setJsgfFile(str, file.getPath());
    }

    public void addNgramSearch(String str, File file) {
        Log.i(TAG, String.format("Load N-gram model %s", file));
        this.decoder.setLmFile(str, file.getPath());
    }

    public void addKeyphraseSearch(String str, String str2) {
        this.decoder.setKeyphrase(str, str2);
    }

    public void addKeywordSearch(String str, File file) {
        this.decoder.setKws(str, file.getPath());
    }

    public void addAllphoneSearch(String str, File file) {
        this.decoder.setAllphoneFile(str, file.getPath());
    }

    private final class RecognizerThread extends Thread {
        private static final int NO_TIMEOUT = -1;
        private int remainingSamples;
        private int timeoutSamples;

        public RecognizerThread(int i) {
            if (i != -1) {
                this.timeoutSamples = (i * SpeechRecognizer.this.sampleRate) / 1000;
            } else {
                this.timeoutSamples = -1;
            }
            this.remainingSamples = this.timeoutSamples;
        }

        public RecognizerThread(SpeechRecognizer speechRecognizer) {
            this(-1);
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() throws IllegalStateException {
            SpeechRecognizer.this.recorder.startRecording();
            if (SpeechRecognizer.this.recorder.getRecordingState() == 1) {
                SpeechRecognizer.this.recorder.stop();
                SpeechRecognizer.this.mainHandler.post(SpeechRecognizer.this.new OnErrorEvent(new IOException("Failed to start recording. Microphone might be already in use.")));
                return;
            }
            Log.d(SpeechRecognizer.TAG, "Starting decoding");
            SpeechRecognizer.this.decoder.startUtt();
            int i = SpeechRecognizer.this.bufferSize;
            short[] sArr = new short[i];
            boolean inSpeech = SpeechRecognizer.this.decoder.getInSpeech();
            SpeechRecognizer.this.recorder.read(sArr, 0, i);
            boolean inSpeech2 = inSpeech;
            while (!interrupted() && (this.timeoutSamples == -1 || this.remainingSamples > 0)) {
                int i2 = SpeechRecognizer.this.recorder.read(sArr, 0, i);
                if (-1 == i2) {
                    throw new RuntimeException("error reading audio buffer");
                }
                if (i2 > 0) {
                    SpeechRecognizer.this.decoder.processRaw(sArr, i2, false, false);
                    if (SpeechRecognizer.this.decoder.getInSpeech() != inSpeech2) {
                        inSpeech2 = SpeechRecognizer.this.decoder.getInSpeech();
                        SpeechRecognizer.this.mainHandler.post(SpeechRecognizer.this.new InSpeechChangeEvent(inSpeech2));
                    }
                    if (inSpeech2) {
                        this.remainingSamples = this.timeoutSamples;
                    }
                    SpeechRecognizer.this.mainHandler.post(SpeechRecognizer.this.new ResultEvent(SpeechRecognizer.this.decoder.hyp(), false));
                }
                if (this.timeoutSamples != -1) {
                    this.remainingSamples -= i2;
                }
            }
            SpeechRecognizer.this.recorder.stop();
            SpeechRecognizer.this.decoder.endUtt();
            SpeechRecognizer.this.mainHandler.removeCallbacksAndMessages(null);
            if (this.timeoutSamples == -1 || this.remainingSamples > 0) {
                return;
            }
            SpeechRecognizer.this.mainHandler.post(new TimeoutEvent());
        }
    }

    private abstract class RecognitionEvent implements Runnable {
        protected abstract void execute(RecognitionListener recognitionListener);

        private RecognitionEvent() {
        }

        @Override // java.lang.Runnable
        public void run() {
            for (RecognitionListener recognitionListener : (RecognitionListener[]) SpeechRecognizer.this.listeners.toArray(new RecognitionListener[0])) {
                execute(recognitionListener);
            }
        }
    }

    private class InSpeechChangeEvent extends RecognitionEvent {
        private final boolean state;

        InSpeechChangeEvent(boolean z) {
            super();
            this.state = z;
        }

        @Override // edu.cmu.pocketsphinx.SpeechRecognizer.RecognitionEvent
        protected void execute(RecognitionListener recognitionListener) {
            if (this.state) {
                recognitionListener.onBeginningOfSpeech();
            } else {
                recognitionListener.onEndOfSpeech();
            }
        }
    }

    private class ResultEvent extends RecognitionEvent {
        private final boolean finalResult;
        protected final Hypothesis hypothesis;

        ResultEvent(Hypothesis hypothesis, boolean z) {
            super();
            this.hypothesis = hypothesis;
            this.finalResult = z;
        }

        @Override // edu.cmu.pocketsphinx.SpeechRecognizer.RecognitionEvent
        protected void execute(RecognitionListener recognitionListener) {
            if (this.finalResult) {
                recognitionListener.onResult(this.hypothesis);
            } else {
                recognitionListener.onPartialResult(this.hypothesis);
            }
        }
    }

    private class OnErrorEvent extends RecognitionEvent {
        private final Exception exception;

        OnErrorEvent(Exception exc) {
            super();
            this.exception = exc;
        }

        @Override // edu.cmu.pocketsphinx.SpeechRecognizer.RecognitionEvent
        protected void execute(RecognitionListener recognitionListener) {
            recognitionListener.onError(this.exception);
        }
    }

    private class TimeoutEvent extends RecognitionEvent {
        private TimeoutEvent() {
            super();
        }

        @Override // edu.cmu.pocketsphinx.SpeechRecognizer.RecognitionEvent
        protected void execute(RecognitionListener recognitionListener) {
            recognitionListener.onTimeout();
        }
    }
}
