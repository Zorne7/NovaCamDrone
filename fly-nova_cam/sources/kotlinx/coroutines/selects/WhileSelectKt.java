package kotlinx.coroutines.selects;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;

/* compiled from: WhileSelect.kt */
@Metadata(m421d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a2\u0010\u0000\u001a\u00020\u00012\u001f\b\u0004\u0010\u0002\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0004\u0012\u00020\u00010\u0003¢\u0006\u0002\b\u0006H\u0087Hø\u0001\u0000¢\u0006\u0002\u0010\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\b"}, m422d2 = {"whileSelect", "", "builder", "Lkotlin/Function1;", "Lkotlinx/coroutines/selects/SelectBuilder;", "", "Lkotlin/ExtensionFunctionType;", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, m423k = 2, m424mv = {1, 6, 0}, m426xi = 48)
/* loaded from: classes.dex */
public final class WhileSelectKt {

    /* compiled from: WhileSelect.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 176)
    @DebugMetadata(m430c = "kotlinx.coroutines.selects.WhileSelectKt", m431f = "WhileSelect.kt", m432i = {0}, m433l = {37}, m434m = "whileSelect", m435n = {"builder"}, m436s = {"L$0"})
    /* renamed from: kotlinx.coroutines.selects.WhileSelectKt$whileSelect$1 */
    static final class C09341 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C09341(Continuation<? super C09341> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return WhileSelectKt.whileSelect(null, this);
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(9:11|26|(2:29|30)|15|31|16|20|(1:22)|(1:24)(9:25|26|(0)|15|31|16|20|(0)|(0)(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0049, code lost:
    
        r4 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x004a, code lost:
    
        r2.handleBuilderException(r4);
     */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0057  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x005d -> B:26:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object whileSelect(Function1<? super SelectBuilder<? super Boolean>, Unit> function1, Continuation<? super Unit> continuation) throws Throwable {
        C09341 c09341;
        Object result;
        if (continuation instanceof C09341) {
            c09341 = (C09341) continuation;
            if ((c09341.label & Integer.MIN_VALUE) != 0) {
                c09341.label -= Integer.MIN_VALUE;
            } else {
                c09341 = new C09341(continuation);
            }
        }
        Object obj = c09341.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c09341.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            c09341.L$0 = function1;
            c09341.label = 1;
            C09341 c093412 = c09341;
            SelectBuilderImpl selectBuilderImpl = new SelectBuilderImpl(c093412);
            function1.invoke(selectBuilderImpl);
            result = selectBuilderImpl.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            }
            if (result == coroutine_suspended) {
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            function1 = (Function1) c09341.L$0;
            ResultKt.throwOnFailure(obj);
            if (!((Boolean) obj).booleanValue()) {
                return Unit.INSTANCE;
            }
            c09341.L$0 = function1;
            c09341.label = 1;
            C09341 c0934122 = c09341;
            SelectBuilderImpl selectBuilderImpl2 = new SelectBuilderImpl(c0934122);
            function1.invoke(selectBuilderImpl2);
            result = selectBuilderImpl2.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(c0934122);
            }
            if (result == coroutine_suspended) {
                return coroutine_suspended;
            }
            obj = result;
            if (!((Boolean) obj).booleanValue()) {
            }
            c09341.L$0 = function1;
            c09341.label = 1;
            C09341 c09341222 = c09341;
            SelectBuilderImpl selectBuilderImpl22 = new SelectBuilderImpl(c09341222);
            function1.invoke(selectBuilderImpl22);
            result = selectBuilderImpl22.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            }
            if (result == coroutine_suspended) {
            }
        }
    }

    private static final Object whileSelect$$forInline(Function1<? super SelectBuilder<? super Boolean>, Unit> function1, Continuation<? super Unit> continuation) {
        Object result;
        do {
            InlineMarker.mark(0);
            SelectBuilderImpl selectBuilderImpl = new SelectBuilderImpl(continuation);
            try {
                function1.invoke(selectBuilderImpl);
            } catch (Throwable th) {
                selectBuilderImpl.handleBuilderException(th);
            }
            result = selectBuilderImpl.getResult();
            if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
                DebugProbesKt.probeCoroutineSuspended(continuation);
            }
            InlineMarker.mark(1);
        } while (((Boolean) result).booleanValue());
        return Unit.INSTANCE;
    }
}
