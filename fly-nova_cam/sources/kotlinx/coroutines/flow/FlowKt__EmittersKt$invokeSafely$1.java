package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: Emitters.kt */
@Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
@DebugMetadata(m430c = "kotlinx.coroutines.flow.FlowKt__EmittersKt", m431f = "Emitters.kt", m432i = {0}, m433l = {216}, m434m = "invokeSafely$FlowKt__EmittersKt", m435n = {"cause"}, m436s = {"L$0"})
/* loaded from: classes.dex */
final class FlowKt__EmittersKt$invokeSafely$1<T> extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;

    FlowKt__EmittersKt$invokeSafely$1(Continuation<? super FlowKt__EmittersKt$invokeSafely$1> continuation) {
        super(continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return FlowKt__EmittersKt.invokeSafely$FlowKt__EmittersKt(null, null, null, this);
    }
}
