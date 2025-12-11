package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.RestrictedSuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequenceScope;

/* compiled from: ViewGroup.kt */
@Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00030\u0002H\u008a@"}, m422d2 = {"<anonymous>", "", "Lkotlin/sequences/SequenceScope;", "Landroid/view/View;"}, m423k = 3, m424mv = {1, 7, 1}, m426xi = 48)
@DebugMetadata(m430c = "androidx.core.view.ViewGroupKt$descendants$1", m431f = "ViewGroup.kt", m432i = {0, 0, 0, 0, 1, 1, 1}, m433l = {119, 121}, m434m = "invokeSuspend", m435n = {"$this$sequence", "$this$forEach$iv", "child", "index$iv", "$this$sequence", "$this$forEach$iv", "index$iv"}, m436s = {"L$0", "L$1", "L$2", "I$0", "L$0", "L$1", "I$0"})
/* loaded from: classes.dex */
final class ViewGroupKt$descendants$1 extends RestrictedSuspendLambda implements Function2<SequenceScope<? super View>, Continuation<? super Unit>, Object> {
    final /* synthetic */ ViewGroup $this_descendants;
    int I$0;
    int I$1;
    private /* synthetic */ Object L$0;
    Object L$1;
    Object L$2;
    int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    ViewGroupKt$descendants$1(ViewGroup viewGroup, Continuation<? super ViewGroupKt$descendants$1> continuation) {
        super(2, continuation);
        this.$this_descendants = viewGroup;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        ViewGroupKt$descendants$1 viewGroupKt$descendants$1 = new ViewGroupKt$descendants$1(this.$this_descendants, continuation);
        viewGroupKt$descendants$1.L$0 = obj;
        return viewGroupKt$descendants$1;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(SequenceScope<? super View> sequenceScope, Continuation<? super Unit> continuation) {
        return ((ViewGroupKt$descendants$1) create(sequenceScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0071  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0092  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0099  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x008b -> B:22:0x008d). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x0092 -> B:24:0x0094). Please report as a decompilation issue!!! */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) throws Throwable {
        SequenceScope sequenceScope;
        ViewGroup viewGroup;
        int childCount;
        int i;
        ViewGroup viewGroup2;
        View view;
        int i2;
        int i3;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i4 = this.label;
        if (i4 == 0) {
            ResultKt.throwOnFailure(obj);
            sequenceScope = (SequenceScope) this.L$0;
            viewGroup = this.$this_descendants;
            childCount = viewGroup.getChildCount();
            i = 0;
            if (i >= childCount) {
            }
        } else if (i4 == 1) {
            i3 = this.I$1;
            i2 = this.I$0;
            view = (View) this.L$2;
            viewGroup2 = (ViewGroup) this.L$1;
            SequenceScope sequenceScope2 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            sequenceScope = sequenceScope2;
            if (view instanceof ViewGroup) {
            }
        } else {
            if (i4 != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            i3 = this.I$1;
            i2 = this.I$0;
            ViewGroup viewGroup3 = (ViewGroup) this.L$1;
            SequenceScope sequenceScope3 = (SequenceScope) this.L$0;
            ResultKt.throwOnFailure(obj);
            sequenceScope = sequenceScope3;
            ViewGroup viewGroup4 = viewGroup3;
            int i5 = i3;
            viewGroup = viewGroup4;
            int i6 = i5;
            i = i2 + 1;
            childCount = i6;
            if (i >= childCount) {
                View childAt = viewGroup.getChildAt(i);
                Intrinsics.checkNotNullExpressionValue(childAt, "getChildAt(index)");
                this.L$0 = sequenceScope;
                this.L$1 = viewGroup;
                this.L$2 = childAt;
                this.I$0 = i;
                this.I$1 = childCount;
                this.label = 1;
                if (sequenceScope.yield(childAt, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                viewGroup2 = viewGroup;
                i3 = childCount;
                i2 = i;
                view = childAt;
                if (view instanceof ViewGroup) {
                    i5 = i3;
                    viewGroup = viewGroup2;
                    int i62 = i5;
                    i = i2 + 1;
                    childCount = i62;
                    if (i >= childCount) {
                    }
                } else {
                    Sequence<View> descendants = ViewGroupKt.getDescendants((ViewGroup) view);
                    this.L$0 = sequenceScope;
                    this.L$1 = viewGroup2;
                    this.L$2 = null;
                    this.I$0 = i2;
                    this.I$1 = i3;
                    this.label = 2;
                    if (sequenceScope.yieldAll(descendants, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    viewGroup3 = viewGroup2;
                    sequenceScope3 = sequenceScope;
                    sequenceScope = sequenceScope3;
                    ViewGroup viewGroup42 = viewGroup3;
                    int i52 = i3;
                    viewGroup = viewGroup42;
                    int i622 = i52;
                    i = i2 + 1;
                    childCount = i622;
                    if (i >= childCount) {
                        return Unit.INSTANCE;
                    }
                }
            }
        }
    }
}
