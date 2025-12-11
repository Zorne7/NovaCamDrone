package kotlinx.coroutines.flow;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: SafeCollector.common.kt */
@Metadata(m421d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002*\u0001\u0000\b\n\u0018\u00002\b\u0012\u0004\u0012\u00028\u00000\u0001J\u001f\u0010\u0002\u001a\u00020\u00032\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00028\u00000\u0005H\u0096@ø\u0001\u0000¢\u0006\u0002\u0010\u0006\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0007¸\u0006\u0000"}, m422d2 = {"kotlinx/coroutines/flow/internal/SafeCollector_commonKt$unsafeFlow$1", "Lkotlinx/coroutines/flow/Flow;", "collect", "", "collector", "Lkotlinx/coroutines/flow/FlowCollector;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m423k = 1, m424mv = {1, 6, 0}, m426xi = 48)
/* loaded from: classes.dex */
public final class FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7 implements Flow<Integer> {
    final /* synthetic */ int[] $this_asFlow$inlined;

    /* compiled from: SafeCollector.common.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7", m431f = "Builders.kt", m432i = {0}, m433l = {115}, m434m = "collect", m435n = {"$this$asFlow_u24lambda_u2d13"}, m436s = {"L$0"})
    /* renamed from: kotlinx.coroutines.flow.FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7$1 */
    public static final class C08131 extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        public C08131(Continuation continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7.this.collect(null, this);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Override // kotlinx.coroutines.flow.Flow
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public Object collect(FlowCollector<? super Integer> flowCollector, Continuation<? super Unit> continuation) throws Throwable {
        C08131 c08131;
        int i;
        int[] iArr;
        FlowCollector flowCollector2;
        int i2;
        if (continuation instanceof C08131) {
            c08131 = (C08131) continuation;
            if ((c08131.label & Integer.MIN_VALUE) != 0) {
                c08131.label -= Integer.MIN_VALUE;
            } else {
                c08131 = new C08131(continuation);
            }
        }
        Object obj = c08131.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c08131.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            int[] iArr2 = this.$this_asFlow$inlined;
            int length = iArr2.length;
            i = 0;
            iArr = iArr2;
            flowCollector2 = flowCollector;
            i2 = length;
        } else {
            if (i3 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            i2 = c08131.I$1;
            int i4 = c08131.I$0;
            int[] iArr3 = (int[]) c08131.L$1;
            FlowCollector flowCollector3 = (FlowCollector) c08131.L$0;
            ResultKt.throwOnFailure(obj);
            flowCollector2 = flowCollector3;
            iArr = iArr3;
            i = i4;
        }
        int[] iArr4 = iArr;
        while (i < i2) {
            int i5 = iArr4[i];
            i++;
            Integer numBoxInt = Boxing.boxInt(i5);
            c08131.L$0 = flowCollector2;
            c08131.L$1 = iArr4;
            c08131.I$0 = i;
            c08131.I$1 = i2;
            c08131.label = 1;
            if (flowCollector2.emit(numBoxInt, c08131) == coroutine_suspended) {
                return coroutine_suspended;
            }
        }
        return Unit.INSTANCE;
    }

    public FlowKt__BuildersKt$asFlow$$inlined$unsafeFlow$7(int[] iArr) {
        this.$this_asFlow$inlined = iArr;
    }
}
