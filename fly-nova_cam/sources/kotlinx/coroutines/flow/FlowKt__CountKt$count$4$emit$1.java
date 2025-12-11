package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlinx.coroutines.flow.FlowKt__CountKt;

/* compiled from: Count.kt */
@Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
@DebugMetadata(m430c = "kotlinx.coroutines.flow.FlowKt__CountKt$count$4", m431f = "Count.kt", m432i = {0}, m433l = {31}, m434m = "emit", m435n = {"this"}, m436s = {"L$0"})
/* loaded from: classes.dex */
final class FlowKt__CountKt$count$4$emit$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ FlowKt__CountKt.C08254<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    FlowKt__CountKt$count$4$emit$1(FlowKt__CountKt.C08254<? super T> c08254, Continuation<? super FlowKt__CountKt$count$4$emit$1> continuation) {
        super(continuation);
        this.this$0 = c08254;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.emit(null, this);
    }
}
