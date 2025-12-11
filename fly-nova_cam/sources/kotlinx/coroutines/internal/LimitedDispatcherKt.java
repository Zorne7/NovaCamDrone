package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: LimitedDispatcher.kt */
@Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0002\u0010\b\n\u0000\u001a\f\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u0000¨\u0006\u0003"}, m422d2 = {"checkParallelism", "", "", "kotlinx-coroutines-core"}, m423k = 2, m424mv = {1, 6, 0}, m426xi = 48)
/* loaded from: classes.dex */
public final class LimitedDispatcherKt {
    public static final void checkParallelism(int i) {
        if (i < 1) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Expected positive parallelism level, but got ", Integer.valueOf(i)).toString());
        }
    }
}
