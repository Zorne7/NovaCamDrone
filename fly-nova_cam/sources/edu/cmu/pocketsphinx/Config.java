package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class Config {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected Config(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(Config config) {
        if (config == null) {
            return 0L;
        }
        return config.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_Config(j);
            }
            this.swigCPtr = 0L;
        }
    }

    public void setBoolean(String str, boolean z) {
        SphinxBaseJNI.Config_setBoolean(this.swigCPtr, this, str, z);
    }

    public void setInt(String str, int i) {
        SphinxBaseJNI.Config_setInt(this.swigCPtr, this, str, i);
    }

    public void setFloat(String str, double d) {
        SphinxBaseJNI.Config_setFloat(this.swigCPtr, this, str, d);
    }

    public void setString(String str, String str2) {
        SphinxBaseJNI.Config_setString(this.swigCPtr, this, str, str2);
    }

    public void setStringExtra(String str, String str2) {
        SphinxBaseJNI.Config_setStringExtra(this.swigCPtr, this, str, str2);
    }

    public boolean exists(String str) {
        return SphinxBaseJNI.Config_exists(this.swigCPtr, this, str);
    }

    public boolean getBoolean(String str) {
        return SphinxBaseJNI.Config_getBoolean(this.swigCPtr, this, str);
    }

    public int getInt(String str) {
        return SphinxBaseJNI.Config_getInt(this.swigCPtr, this, str);
    }

    public double getFloat(String str) {
        return SphinxBaseJNI.Config_getFloat(this.swigCPtr, this, str);
    }

    public String getString(String str) {
        return SphinxBaseJNI.Config_getString(this.swigCPtr, this, str);
    }
}
