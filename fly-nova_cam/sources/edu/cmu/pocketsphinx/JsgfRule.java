package edu.cmu.pocketsphinx;

/* loaded from: classes.dex */
public class JsgfRule {
    protected boolean swigCMemOwn;
    private long swigCPtr;

    protected JsgfRule(long j, boolean z) {
        this.swigCMemOwn = z;
        this.swigCPtr = j;
    }

    protected static long getCPtr(JsgfRule jsgfRule) {
        if (jsgfRule == null) {
            return 0L;
        }
        return jsgfRule.swigCPtr;
    }

    protected void finalize() {
        delete();
    }

    public synchronized void delete() {
        long j = this.swigCPtr;
        if (j != 0) {
            if (this.swigCMemOwn) {
                this.swigCMemOwn = false;
                SphinxBaseJNI.delete_JsgfRule(j);
            }
            this.swigCPtr = 0L;
        }
    }

    private JsgfRule() {
        this(SphinxBaseJNI.new_JsgfRule(), true);
    }

    public static JsgfRule fromIter(SWIGTYPE_p_jsgf_rule_iter_t sWIGTYPE_p_jsgf_rule_iter_t) {
        long jJsgfRule_fromIter = SphinxBaseJNI.JsgfRule_fromIter(SWIGTYPE_p_jsgf_rule_iter_t.getCPtr(sWIGTYPE_p_jsgf_rule_iter_t));
        if (jJsgfRule_fromIter == 0) {
            return null;
        }
        return new JsgfRule(jJsgfRule_fromIter, false);
    }

    public String getName() {
        return SphinxBaseJNI.JsgfRule_getName(this.swigCPtr, this);
    }

    public boolean isPublic() {
        return SphinxBaseJNI.JsgfRule_isPublic(this.swigCPtr, this);
    }
}
