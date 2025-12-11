package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.flow.internal.AbortFlowException;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* JADX INFO: Add missing generic type declarations: [T] */
/* compiled from: Limit.kt */
@Metadata(m421d1 = {"\u0000\u0013\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u0019\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00028\u0000H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0005\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0006¸\u0006\u0000"}, m422d2 = {"kotlinx/coroutines/flow/FlowKt__LimitKt$collectWhile$collector$1", "Lkotlinx/coroutines/flow/FlowCollector;", "emit", "", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m423k = 1, m424mv = {1, 6, 0}, m426xi = 48)
/* renamed from: kotlinx.coroutines.flow.FlowKt__LimitKt$takeWhile$lambda-6$$inlined$collectWhile$1, reason: invalid class name */
/* loaded from: classes.dex */
public final class FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1<T> implements FlowCollector<T> {
    final /* synthetic */ Function2 $predicate$inlined;
    final /* synthetic */ FlowCollector $this_unsafeFlow$inlined;

    /* compiled from: Limit.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.flow.FlowKt__LimitKt$takeWhile$lambda-6$$inlined$collectWhile$1", m431f = "Limit.kt", m432i = {0, 0, 1}, m433l = {143, IjkMediaMeta.FF_PROFILE_H264_HIGH_444}, m434m = "emit", m435n = {"this", "value", "this"}, m436s = {"L$0", "L$1", "L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__LimitKt$takeWhile$lambda-6$$inlined$collectWhile$1$1 */
    public static final class C08451 extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public C08451(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1.this.emit(null, this);
        }
    }

    public FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1(Function2 function2, FlowCollector flowCollector) {
        this.$predicate$inlined = function2;
        this.$this_unsafeFlow$inlined = flowCollector;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0082  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Override // kotlinx.coroutines.flow.FlowCollector
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object emit(T t, Continuation<? super Unit> continuation) throws Throwable {
        C08451 c08451;
        Object obj;
        T t2;
        FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1<T> flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1;
        if (continuation instanceof C08451) {
            c08451 = (C08451) continuation;
            if ((c08451.label & Integer.MIN_VALUE) != 0) {
                c08451.label -= Integer.MIN_VALUE;
            } else {
                c08451 = new C08451(continuation);
            }
        }
        Object obj2 = c08451.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c08451.label;
        boolean z = true;
        if (i == 0) {
            ResultKt.throwOnFailure(obj2);
            Function2 function2 = this.$predicate$inlined;
            c08451.L$0 = this;
            c08451.L$1 = t;
            c08451.label = 1;
            InlineMarker.mark(6);
            Object objInvoke = function2.invoke(t, c08451);
            InlineMarker.mark(7);
            if (objInvoke == coroutine_suspended) {
                return coroutine_suspended;
            }
            obj = objInvoke;
            t2 = t;
            flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1 = this;
        } else {
            if (i != 1) {
                if (i != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1 = (FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1) c08451.L$0;
                ResultKt.throwOnFailure(obj2);
                if (z) {
                    throw new AbortFlowException(flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1);
                }
                return Unit.INSTANCE;
            }
            Object obj3 = c08451.L$1;
            FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1<T> flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$12 = (FlowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1) c08451.L$0;
            ResultKt.throwOnFailure(obj2);
            t2 = obj3;
            flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1 = flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$12;
            obj = obj2;
        }
        if (((Boolean) obj).booleanValue()) {
            FlowCollector flowCollector = flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1.$this_unsafeFlow$inlined;
            c08451.L$0 = flowKt__LimitKt$takeWhile$lambda6$$inlined$collectWhile$1;
            c08451.L$1 = null;
            c08451.label = 2;
            if (flowCollector.emit(t2, c08451) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else {
            z = false;
        }
        if (z) {
        }
    }
}
