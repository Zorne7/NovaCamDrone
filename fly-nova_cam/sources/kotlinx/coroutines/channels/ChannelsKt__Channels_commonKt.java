package kotlinx.coroutines.channels;

import java.util.List;
import java.util.concurrent.CancellationException;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlinx.coroutines.ExceptionsKt;
import kotlinx.coroutines.channels.ReceiveChannel;
import kotlinx.coroutines.selects.SelectClause1;

/* compiled from: Channels.common.kt */
@Metadata(m421d1 = {"\u0000>\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0010 \n\u0000\u001a\u001a\u0010\u0002\u001a\u00020\u0003*\u0006\u0012\u0002\b\u00030\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0001\u001aC\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\t0\n2\u001d\u0010\u000b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0004\u0012\u0004\u0012\u0002H\b0\f¢\u0006\u0002\b\rH\u0087\b¢\u0006\u0002\u0010\u000e\u001aP\u0010\u0007\u001a\u0002H\b\"\u0004\b\u0000\u0010\t\"\u0004\b\u0001\u0010\b*\b\u0012\u0004\u0012\u0002H\t0\u00042\u001d\u0010\u000b\u001a\u0019\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\t0\u0004\u0012\u0004\u0012\u0002H\b0\f¢\u0006\u0002\b\rH\u0086\b\u0082\u0002\n\n\b\b\u0001\u0012\u0002\u0010\u0001 \u0001¢\u0006\u0002\u0010\u000f\u001a5\u0010\u0010\u001a\u00020\u0003\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\n2\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\u00030\fH\u0087Hø\u0001\u0000¢\u0006\u0002\u0010\u0012\u001a5\u0010\u0010\u001a\u00020\u0003\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u00042\u0012\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u0002H\t\u0012\u0004\u0012\u00020\u00030\fH\u0086Hø\u0001\u0000¢\u0006\u0002\u0010\u0013\u001a$\u0010\u0014\u001a\n\u0012\u0006\u0012\u0004\u0018\u0001H\t0\u0015\"\b\b\u0000\u0010\t*\u00020\u0016*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0007\u001a'\u0010\u0017\u001a\u0004\u0018\u0001H\t\"\b\b\u0000\u0010\t*\u00020\u0016*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u0018\u001a'\u0010\u0019\u001a\b\u0012\u0004\u0012\u0002H\t0\u001a\"\u0004\b\u0000\u0010\t*\b\u0012\u0004\u0012\u0002H\t0\u0004H\u0086@ø\u0001\u0000¢\u0006\u0002\u0010\u0018\"\u000e\u0010\u0000\u001a\u00020\u0001X\u0080T¢\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u001b"}, m422d2 = {"DEFAULT_CLOSE_MESSAGE", "", "cancelConsumed", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "cause", "", "consume", "R", "E", "Lkotlinx/coroutines/channels/BroadcastChannel;", "block", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "consumeEach", "action", "(Lkotlinx/coroutines/channels/BroadcastChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "onReceiveOrNull", "Lkotlinx/coroutines/selects/SelectClause1;", "", "receiveOrNull", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toList", "", "kotlinx-coroutines-core"}, m423k = 5, m424mv = {1, 6, 0}, m426xi = 48, m427xs = "kotlinx/coroutines/channels/ChannelsKt")
/* loaded from: classes.dex */
final /* synthetic */ class ChannelsKt__Channels_commonKt {

    /* compiled from: Channels.common.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 176)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", m431f = "Channels.common.kt", m432i = {0, 0}, m433l = {104}, m434m = "consumeEach", m435n = {"action", "$this$consume$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$1 */
    static final class C07481<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07481(Continuation<? super C07481> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__Channels_commonKt.consumeEach((ReceiveChannel) null, (Function1) null, this);
        }
    }

    /* compiled from: Channels.common.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 176)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", m431f = "Channels.common.kt", m432i = {0, 0}, m433l = {129}, m434m = "consumeEach", m435n = {"action", "channel$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$consumeEach$3 */
    static final class C07493<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07493(Continuation<? super C07493> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__Channels_commonKt.consumeEach((BroadcastChannel) null, (Function1) null, this);
        }
    }

    /* compiled from: Channels.common.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt", m431f = "Channels.common.kt", m432i = {0, 0}, m433l = {148}, m434m = "toList", m435n = {"$this$toList_u24lambda_u2d3", "$this$consume$iv$iv"}, m436s = {"L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__Channels_commonKt$toList$1 */
    static final class C07501<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C07501(Continuation<? super C07501> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toList(null, this);
        }
    }

    public static final <E, R> R consume(BroadcastChannel<E> broadcastChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        ReceiveChannel<E> receiveChannelOpenSubscription = broadcastChannel.openSubscription();
        try {
            return function1.invoke(receiveChannelOpenSubscription);
        } finally {
            InlineMarker.finallyStart(1);
            ReceiveChannel.DefaultImpls.cancel$default((ReceiveChannel) receiveChannelOpenSubscription, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Deprecated(level = DeprecationLevel.ERROR, message = "Deprecated in the favour of 'receiveCatching'", replaceWith = @ReplaceWith(expression = "receiveCatching().getOrNull()", imports = {}))
    public static final <E> Object receiveOrNull(ReceiveChannel<? extends E> receiveChannel, Continuation<? super E> continuation) {
        return receiveChannel.receiveOrNull(continuation);
    }

    @Deprecated(level = DeprecationLevel.ERROR, message = "Deprecated in the favour of 'onReceiveCatching'")
    public static final <E> SelectClause1<E> onReceiveOrNull(ReceiveChannel<? extends E> receiveChannel) {
        return receiveChannel.getOnReceiveOrNull();
    }

    public static final <E, R> R consume(ReceiveChannel<? extends E> receiveChannel, Function1<? super ReceiveChannel<? extends E>, ? extends R> function1) {
        try {
            R rInvoke = function1.invoke(receiveChannel);
            InlineMarker.finallyStart(1);
            ChannelsKt.cancelConsumed(receiveChannel, null);
            InlineMarker.finallyEnd(1);
            return rInvoke;
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x005a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0066 A[Catch: all -> 0x0037, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x006f), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f A[Catch: all -> 0x0037, TRY_LEAVE, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x006f), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x005b -> B:25:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object consumeEach(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) throws Throwable {
        C07481 c07481;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ChannelIterator it;
        Function1<? super E, Unit> function12;
        Object objHasNext;
        if (continuation instanceof C07481) {
            c07481 = (C07481) continuation;
            if ((c07481.label & Integer.MIN_VALUE) != 0) {
                c07481.label -= Integer.MIN_VALUE;
            } else {
                c07481 = new C07481(continuation);
            }
        }
        Object obj = c07481.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07481.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                function12 = function1;
                c07481.L$0 = function12;
                c07481.L$1 = receiveChannel2;
                c07481.L$2 = it;
                c07481.label = 1;
                objHasNext = it.hasNext(c07481);
                if (objHasNext != coroutine_suspended) {
                }
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
                throw th;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            it = (ChannelIterator) c07481.L$2;
            receiveChannel2 = (ReceiveChannel) c07481.L$1;
            Function1<? super E, Unit> function13 = (Function1) c07481.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    function13.invoke((Object) it.next());
                    function12 = function13;
                    c07481.L$0 = function12;
                    c07481.L$1 = receiveChannel2;
                    c07481.L$2 = it;
                    c07481.label = 1;
                    objHasNext = it.hasNext(c07481);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    function13 = function12;
                    obj = objHasNext;
                    if (!((Boolean) obj).booleanValue()) {
                        Unit unit = Unit.INSTANCE;
                        InlineMarker.finallyStart(1);
                        ChannelsKt.cancelConsumed(receiveChannel2, null);
                        InlineMarker.finallyEnd(1);
                        return Unit.INSTANCE;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                try {
                    throw th;
                } catch (Throwable th4) {
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    InlineMarker.finallyEnd(1);
                    throw th4;
                }
            }
        }
    }

    private static final <E> Object consumeEach$$forInline(ReceiveChannel<? extends E> receiveChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        try {
            ChannelIterator<? extends E> it = receiveChannel.iterator();
            while (true) {
                InlineMarker.mark(3);
                InlineMarker.mark(0);
                Object objHasNext = it.hasNext(null);
                InlineMarker.mark(1);
                if (!((Boolean) objHasNext).booleanValue()) {
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ChannelsKt.cancelConsumed(receiveChannel, null);
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                }
                function1.invoke(it.next());
            }
        } finally {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0065 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0066  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0071 A[Catch: all -> 0x003b, TryCatch #3 {all -> 0x003b, blocks: (B:12:0x0037, B:25:0x0069, B:27:0x0071, B:28:0x007a), top: B:45:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x007a A[Catch: all -> 0x003b, TRY_LEAVE, TryCatch #3 {all -> 0x003b, blocks: (B:12:0x0037, B:25:0x0069, B:27:0x0071, B:28:0x007a), top: B:45:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0066 -> B:25:0x0069). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object toList(ReceiveChannel<? extends E> receiveChannel, Continuation<? super List<? extends E>> continuation) throws Throwable {
        C07501 c07501;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        List list;
        ChannelIterator it;
        List list2;
        Object objHasNext;
        if (continuation instanceof C07501) {
            c07501 = (C07501) continuation;
            if ((c07501.label & Integer.MIN_VALUE) != 0) {
                c07501.label -= Integer.MIN_VALUE;
            } else {
                c07501 = new C07501(continuation);
            }
        }
        Object obj = c07501.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07501.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                List listCreateListBuilder = CollectionsKt.createListBuilder();
                list = listCreateListBuilder;
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                list2 = listCreateListBuilder;
                c07501.L$0 = list;
                c07501.L$1 = list2;
                c07501.L$2 = receiveChannel2;
                c07501.L$3 = it;
                c07501.label = 1;
                objHasNext = it.hasNext(c07501);
                if (objHasNext != coroutine_suspended) {
                }
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
                throw th;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            it = (ChannelIterator) c07501.L$3;
            ReceiveChannel<? extends E> receiveChannel3 = (ReceiveChannel) c07501.L$2;
            List list3 = (List) c07501.L$1;
            list = (List) c07501.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                List list4 = list3;
                if (!((Boolean) obj).booleanValue()) {
                    list4.add(it.next());
                    receiveChannel2 = receiveChannel3;
                    list2 = list4;
                    try {
                        c07501.L$0 = list;
                        c07501.L$1 = list2;
                        c07501.L$2 = receiveChannel2;
                        c07501.L$3 = it;
                        c07501.label = 1;
                        objHasNext = it.hasNext(c07501);
                        if (objHasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel3 = receiveChannel2;
                        obj = objHasNext;
                        list4 = list2;
                        if (!((Boolean) obj).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed(receiveChannel3, null);
                            return CollectionsKt.build(list);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            ChannelsKt.cancelConsumed(receiveChannel2, th);
                            throw th4;
                        }
                    }
                }
            } catch (Throwable th5) {
                th = th5;
                receiveChannel2 = receiveChannel3;
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0061 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0062  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006e A[Catch: all -> 0x0086, TryCatch #0 {all -> 0x0086, blocks: (B:26:0x0066, B:28:0x006e, B:29:0x0078), top: B:40:0x0066 }] */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0078 A[Catch: all -> 0x0086, TRY_LEAVE, TryCatch #0 {all -> 0x0086, blocks: (B:26:0x0066, B:28:0x006e, B:29:0x0078), top: B:40:0x0066 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x0062 -> B:14:0x0039). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E> Object consumeEach(BroadcastChannel<E> broadcastChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) throws Throwable {
        C07493 c07493;
        ReceiveChannel<E> receiveChannel;
        Throwable th;
        ReceiveChannel<E> receiveChannel2;
        ChannelIterator it;
        Object objHasNext;
        if (continuation instanceof C07493) {
            c07493 = (C07493) continuation;
            if ((c07493.label & Integer.MIN_VALUE) != 0) {
                c07493.label -= Integer.MIN_VALUE;
            } else {
                c07493 = new C07493(continuation);
            }
        }
        Object obj = c07493.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07493.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            ReceiveChannel<E> receiveChannelOpenSubscription = broadcastChannel.openSubscription();
            try {
                receiveChannel2 = receiveChannelOpenSubscription;
                it = receiveChannelOpenSubscription.iterator();
                c07493.L$0 = function1;
                c07493.L$1 = receiveChannel2;
                c07493.L$2 = it;
                c07493.label = 1;
                objHasNext = it.hasNext(c07493);
                if (objHasNext != coroutine_suspended) {
                }
            } catch (Throwable th2) {
                receiveChannel = receiveChannelOpenSubscription;
                th = th2;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            it = (ChannelIterator) c07493.L$2;
            receiveChannel = (ReceiveChannel) c07493.L$1;
            Function1<? super E, Unit> function12 = (Function1) c07493.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                C07493 c074932 = c07493;
                ReceiveChannel<E> receiveChannel3 = receiveChannel;
                function1 = function12;
                C07493 c074933 = c074932;
                try {
                    if (!((Boolean) obj).booleanValue()) {
                        function1.invoke((Object) it.next());
                        receiveChannel2 = receiveChannel3;
                        c07493 = c074933;
                        try {
                            c07493.L$0 = function1;
                            c07493.L$1 = receiveChannel2;
                            c07493.L$2 = it;
                            c07493.label = 1;
                            objHasNext = it.hasNext(c07493);
                            if (objHasNext != coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            c074932 = c07493;
                            receiveChannel3 = receiveChannel2;
                            obj = objHasNext;
                            C07493 c0749332 = c074932;
                            if (!((Boolean) obj).booleanValue()) {
                                Unit unit = Unit.INSTANCE;
                                InlineMarker.finallyStart(1);
                                ReceiveChannel.DefaultImpls.cancel$default((ReceiveChannel) receiveChannel3, (CancellationException) null, 1, (Object) null);
                                InlineMarker.finallyEnd(1);
                                return Unit.INSTANCE;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            receiveChannel = receiveChannel2;
                        }
                    }
                } catch (Throwable th4) {
                    th = th4;
                    receiveChannel = receiveChannel3;
                }
            } catch (Throwable th5) {
                th = th5;
            }
        }
        InlineMarker.finallyStart(1);
        ReceiveChannel.DefaultImpls.cancel$default((ReceiveChannel) receiveChannel, (CancellationException) null, 1, (Object) null);
        InlineMarker.finallyEnd(1);
        throw th;
    }

    public static final void cancelConsumed(ReceiveChannel<?> receiveChannel, Throwable th) {
        if (th != null) {
            CancellationException = th instanceof CancellationException ? (CancellationException) th : null;
            if (CancellationException == null) {
                CancellationException = ExceptionsKt.CancellationException("Channel was consumed, consumer had failed", th);
            }
        }
        receiveChannel.cancel(CancellationException);
    }

    private static final <E> Object consumeEach$$forInline(BroadcastChannel<E> broadcastChannel, Function1<? super E, Unit> function1, Continuation<? super Unit> continuation) {
        ReceiveChannel<E> receiveChannelOpenSubscription = broadcastChannel.openSubscription();
        try {
            ChannelIterator<E> it = receiveChannelOpenSubscription.iterator();
            while (true) {
                InlineMarker.mark(3);
                InlineMarker.mark(0);
                Object objHasNext = it.hasNext(null);
                InlineMarker.mark(1);
                if (!((Boolean) objHasNext).booleanValue()) {
                    Unit unit = Unit.INSTANCE;
                    InlineMarker.finallyStart(1);
                    ReceiveChannel.DefaultImpls.cancel$default((ReceiveChannel) receiveChannelOpenSubscription, (CancellationException) null, 1, (Object) null);
                    InlineMarker.finallyEnd(1);
                    return Unit.INSTANCE;
                }
                function1.invoke(it.next());
            }
        } catch (Throwable th) {
            InlineMarker.finallyStart(1);
            ReceiveChannel.DefaultImpls.cancel$default((ReceiveChannel) receiveChannelOpenSubscription, (CancellationException) null, 1, (Object) null);
            InlineMarker.finallyEnd(1);
            throw th;
        }
    }
}
