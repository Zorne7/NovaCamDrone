package kotlinx.coroutines.channels;

import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.ranges.RangesKt;
import kotlinx.coroutines.AbstractTimeSource;
import kotlinx.coroutines.AbstractTimeSourceKt;
import kotlinx.coroutines.DelayKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.EventLoop_commonKt;
import kotlinx.coroutines.GlobalScope;

/* compiled from: TickerChannels.kt */
@Metadata(m421d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a/\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a/\u0010\b\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00032\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00010\u0006H\u0082@ø\u0001\u0000¢\u0006\u0002\u0010\u0007\u001a4\u0010\t\u001a\b\u0012\u0004\u0012\u00020\u00010\n2\u0006\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\u000eH\u0007\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u000f"}, m422d2 = {"fixedDelayTicker", "", "delayMillis", "", "initialDelayMillis", "channel", "Lkotlinx/coroutines/channels/SendChannel;", "(JJLkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "fixedPeriodTicker", "ticker", "Lkotlinx/coroutines/channels/ReceiveChannel;", "context", "Lkotlin/coroutines/CoroutineContext;", "mode", "Lkotlinx/coroutines/channels/TickerMode;", "kotlinx-coroutines-core"}, m423k = 2, m424mv = {1, 6, 0}, m426xi = 48)
/* loaded from: classes.dex */
public final class TickerChannelsKt {

    /* compiled from: TickerChannels.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.TickerChannelsKt", m431f = "TickerChannels.kt", m432i = {0, 0, 1, 1, 2, 2}, m433l = {106, 108, 109}, m434m = "fixedDelayTicker", m435n = {"channel", "delayMillis", "channel", "delayMillis", "channel", "delayMillis"}, m436s = {"L$0", "J$0", "L$0", "J$0", "L$0", "J$0"})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$fixedDelayTicker$1 */
    static final class C07931 extends ContinuationImpl {
        long J$0;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C07931(Continuation<? super C07931> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TickerChannelsKt.fixedDelayTicker(0L, 0L, null, this);
        }
    }

    /* compiled from: TickerChannels.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.TickerChannelsKt", m431f = "TickerChannels.kt", m432i = {0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3}, m433l = {84, 88, 94, 96}, m434m = "fixedPeriodTicker", m435n = {"channel", "delayMillis", "deadline", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs", "channel", "deadline", "delayNs"}, m436s = {"L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1", "L$0", "J$0", "J$1"})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$fixedPeriodTicker$1 */
    static final class C07941 extends ContinuationImpl {
        long J$0;
        long J$1;
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C07941(Continuation<? super C07941> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return TickerChannelsKt.fixedPeriodTicker(0L, 0L, null, this);
        }
    }

    public static /* synthetic */ ReceiveChannel ticker$default(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode, int i, Object obj) {
        if ((i & 2) != 0) {
            j2 = j;
        }
        if ((i & 4) != 0) {
            coroutineContext = EmptyCoroutineContext.INSTANCE;
        }
        if ((i & 8) != 0) {
            tickerMode = TickerMode.FIXED_PERIOD;
        }
        return ticker(j, j2, coroutineContext, tickerMode);
    }

    public static final ReceiveChannel<Unit> ticker(long j, long j2, CoroutineContext coroutineContext, TickerMode tickerMode) {
        if (j < 0) {
            throw new IllegalArgumentException(("Expected non-negative delay, but has " + j + " ms").toString());
        }
        if (j2 < 0) {
            throw new IllegalArgumentException(("Expected non-negative initial delay, but has " + j2 + " ms").toString());
        }
        return ProduceKt.produce(GlobalScope.INSTANCE, Dispatchers.getUnconfined().plus(coroutineContext), 0, new C07953(tickerMode, j, j2, null));
    }

    /* compiled from: TickerChannels.kt */
    @Metadata(m421d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\b\u0012\u0004\u0012\u00020\u00010\u0002H\u008a@"}, m422d2 = {"<anonymous>", "", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.TickerChannelsKt$ticker$3", m431f = "TickerChannels.kt", m432i = {}, m433l = {72, 73}, m434m = "invokeSuspend", m435n = {}, m436s = {})
    /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$ticker$3 */
    static final class C07953 extends SuspendLambda implements Function2<ProducerScope<? super Unit>, Continuation<? super Unit>, Object> {
        final /* synthetic */ long $delayMillis;
        final /* synthetic */ long $initialDelayMillis;
        final /* synthetic */ TickerMode $mode;
        private /* synthetic */ Object L$0;
        int label;

        /* compiled from: TickerChannels.kt */
        @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
        /* renamed from: kotlinx.coroutines.channels.TickerChannelsKt$ticker$3$WhenMappings */
        public /* synthetic */ class WhenMappings {
            public static final /* synthetic */ int[] $EnumSwitchMapping$0;

            static {
                int[] iArr = new int[TickerMode.values().length];
                iArr[TickerMode.FIXED_PERIOD.ordinal()] = 1;
                iArr[TickerMode.FIXED_DELAY.ordinal()] = 2;
                $EnumSwitchMapping$0 = iArr;
            }
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C07953(TickerMode tickerMode, long j, long j2, Continuation<? super C07953> continuation) {
            super(2, continuation);
            this.$mode = tickerMode;
            this.$delayMillis = j;
            this.$initialDelayMillis = j2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07953 c07953 = new C07953(this.$mode, this.$delayMillis, this.$initialDelayMillis, continuation);
            c07953.L$0 = obj;
            return c07953;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super Unit> producerScope, Continuation<? super Unit> continuation) {
            return ((C07953) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope = (ProducerScope) this.L$0;
                int i2 = WhenMappings.$EnumSwitchMapping$0[this.$mode.ordinal()];
                if (i2 == 1) {
                    this.label = 1;
                    if (TickerChannelsKt.fixedPeriodTicker(this.$delayMillis, this.$initialDelayMillis, producerScope.getChannel(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                } else if (i2 == 2) {
                    this.label = 2;
                    if (TickerChannelsKt.fixedDelayTicker(this.$delayMillis, this.$initialDelayMillis, producerScope.getChannel(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                }
            } else {
                if (i != 1 && i != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Unit.INSTANCE;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00c6 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00d3  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00d5  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0128 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0016  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:52:0x0112 -> B:34:0x00b5). Please report as a decompilation issue!!! */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:54:0x0126 -> B:15:0x003d). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object fixedPeriodTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) throws Throwable {
        C07941 c07941;
        SendChannel sendChannel2;
        long j3;
        long j4;
        long jDelayToNanos;
        long j5;
        long j6;
        SendChannel sendChannel3;
        long j7;
        long jCoerceAtLeast;
        long jDelayNanosToMillis;
        char c;
        Unit unit;
        if (continuation instanceof C07941) {
            c07941 = (C07941) continuation;
            if ((c07941.label & Integer.MIN_VALUE) != 0) {
                c07941.label -= Integer.MIN_VALUE;
            } else {
                c07941 = new C07941(continuation);
            }
        }
        Object obj = c07941.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07941.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            AbstractTimeSource timeSource = AbstractTimeSourceKt.getTimeSource();
            Long lBoxLong = timeSource == null ? null : Boxing.boxLong(timeSource.nanoTime());
            long jNanoTime = (lBoxLong == null ? System.nanoTime() : lBoxLong.longValue()) + EventLoop_commonKt.delayToNanos(j2);
            sendChannel2 = sendChannel;
            c07941.L$0 = sendChannel2;
            c07941.J$0 = j;
            c07941.J$1 = jNanoTime;
            c07941.label = 1;
            if (DelayKt.delay(j2, c07941) == coroutine_suspended) {
                return coroutine_suspended;
            }
            j3 = jNanoTime;
            j4 = j;
        } else if (i == 1) {
            j3 = c07941.J$1;
            j4 = c07941.J$0;
            SendChannel sendChannel4 = (SendChannel) c07941.L$0;
            ResultKt.throwOnFailure(obj);
            sendChannel2 = sendChannel4;
        } else if (i == 2) {
            j6 = c07941.J$1;
            j7 = c07941.J$0;
            sendChannel3 = (SendChannel) c07941.L$0;
            ResultKt.throwOnFailure(obj);
            AbstractTimeSource timeSource2 = AbstractTimeSourceKt.getTimeSource();
            if (timeSource2 != null) {
            }
            if (lBoxLong != null) {
            }
            jCoerceAtLeast = RangesKt.coerceAtLeast(j7 - jNanoTime, 0L);
            if (jCoerceAtLeast != 0) {
            }
            jDelayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(jCoerceAtLeast);
            c07941.L$0 = sendChannel3;
            c07941.J$0 = j7;
            c07941.J$1 = j6;
            c = 4;
            c07941.label = 4;
            if (DelayKt.delay(jDelayNanosToMillis, c07941) == coroutine_suspended) {
            }
            long j8 = j6;
            j3 = j7;
            jDelayToNanos = j8;
            sendChannel2 = sendChannel3;
            long j9 = j3 + jDelayToNanos;
            unit = Unit.INSTANCE;
            c07941.L$0 = sendChannel2;
            c07941.J$0 = j9;
            c07941.J$1 = jDelayToNanos;
            c07941.label = 2;
            if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
            }
        } else if (i == 3) {
            j6 = c07941.J$1;
            j5 = c07941.J$0;
            sendChannel3 = (SendChannel) c07941.L$0;
            ResultKt.throwOnFailure(obj);
            long j10 = j6;
            j3 = j5;
            jDelayToNanos = j10;
            sendChannel2 = sendChannel3;
            long j92 = j3 + jDelayToNanos;
            unit = Unit.INSTANCE;
            c07941.L$0 = sendChannel2;
            c07941.J$0 = j92;
            c07941.J$1 = jDelayToNanos;
            c07941.label = 2;
            if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
            }
        } else if (i == 4) {
            j6 = c07941.J$1;
            j7 = c07941.J$0;
            sendChannel3 = (SendChannel) c07941.L$0;
            ResultKt.throwOnFailure(obj);
            c = 4;
            long j82 = j6;
            j3 = j7;
            jDelayToNanos = j82;
            sendChannel2 = sendChannel3;
            long j922 = j3 + jDelayToNanos;
            unit = Unit.INSTANCE;
            c07941.L$0 = sendChannel2;
            c07941.J$0 = j922;
            c07941.J$1 = jDelayToNanos;
            c07941.label = 2;
            if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
                return coroutine_suspended;
            }
            sendChannel3 = sendChannel2;
            j6 = jDelayToNanos;
            j7 = j922;
            AbstractTimeSource timeSource22 = AbstractTimeSourceKt.getTimeSource();
            Long lBoxLong2 = timeSource22 != null ? null : Boxing.boxLong(timeSource22.nanoTime());
            long jNanoTime2 = lBoxLong2 != null ? System.nanoTime() : lBoxLong2.longValue();
            jCoerceAtLeast = RangesKt.coerceAtLeast(j7 - jNanoTime2, 0L);
            if (jCoerceAtLeast != 0 && j6 != 0) {
                long j11 = j6 - ((jNanoTime2 - j7) % j6);
                j5 = jNanoTime2 + j11;
                long jDelayNanosToMillis2 = EventLoop_commonKt.delayNanosToMillis(j11);
                c07941.L$0 = sendChannel3;
                c07941.J$0 = j5;
                c07941.J$1 = j6;
                c07941.label = 3;
                if (DelayKt.delay(jDelayNanosToMillis2, c07941) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                long j102 = j6;
                j3 = j5;
                jDelayToNanos = j102;
                sendChannel2 = sendChannel3;
                long j9222 = j3 + jDelayToNanos;
                unit = Unit.INSTANCE;
                c07941.L$0 = sendChannel2;
                c07941.J$0 = j9222;
                c07941.J$1 = jDelayToNanos;
                c07941.label = 2;
                if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
                }
            } else {
                jDelayNanosToMillis = EventLoop_commonKt.delayNanosToMillis(jCoerceAtLeast);
                c07941.L$0 = sendChannel3;
                c07941.J$0 = j7;
                c07941.J$1 = j6;
                c = 4;
                c07941.label = 4;
                if (DelayKt.delay(jDelayNanosToMillis, c07941) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                long j822 = j6;
                j3 = j7;
                jDelayToNanos = j822;
                sendChannel2 = sendChannel3;
                long j92222 = j3 + jDelayToNanos;
                unit = Unit.INSTANCE;
                c07941.L$0 = sendChannel2;
                c07941.J$0 = j92222;
                c07941.J$1 = jDelayToNanos;
                c07941.label = 2;
                if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        jDelayToNanos = EventLoop_commonKt.delayToNanos(j4);
        long j922222 = j3 + jDelayToNanos;
        unit = Unit.INSTANCE;
        c07941.L$0 = sendChannel2;
        c07941.J$0 = j922222;
        c07941.J$1 = jDelayToNanos;
        c07941.label = 2;
        if (sendChannel2.send(unit, c07941) != coroutine_suspended) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0072 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:25:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0080 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x007e -> B:14:0x0035). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final Object fixedDelayTicker(long j, long j2, SendChannel<? super Unit> sendChannel, Continuation<? super Unit> continuation) throws Throwable {
        C07931 c07931;
        SendChannel<? super Unit> sendChannel2;
        if (continuation instanceof C07931) {
            c07931 = (C07931) continuation;
            if ((c07931.label & Integer.MIN_VALUE) != 0) {
                c07931.label -= Integer.MIN_VALUE;
            } else {
                c07931 = new C07931(continuation);
            }
        }
        Object obj = c07931.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07931.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            c07931.L$0 = sendChannel;
            c07931.J$0 = j;
            c07931.label = 1;
            if (DelayKt.delay(j2, c07931) == coroutine_suspended) {
                return coroutine_suspended;
            }
        } else if (i == 1) {
            j = c07931.J$0;
            sendChannel = (SendChannel) c07931.L$0;
            ResultKt.throwOnFailure(obj);
        } else if (i == 2) {
            j = c07931.J$0;
            sendChannel2 = (SendChannel) c07931.L$0;
            ResultKt.throwOnFailure(obj);
            c07931.L$0 = sendChannel2;
            c07931.J$0 = j;
            c07931.label = 3;
            if (DelayKt.delay(j, c07931) == coroutine_suspended) {
                return coroutine_suspended;
            }
            sendChannel = sendChannel2;
        } else {
            if (i != 3) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            j = c07931.J$0;
            sendChannel2 = (SendChannel) c07931.L$0;
            ResultKt.throwOnFailure(obj);
            sendChannel = sendChannel2;
        }
        Unit unit = Unit.INSTANCE;
        c07931.L$0 = sendChannel;
        c07931.J$0 = j;
        c07931.label = 2;
        if (sendChannel.send(unit, c07931) != coroutine_suspended) {
            return coroutine_suspended;
        }
        sendChannel2 = sendChannel;
        c07931.L$0 = sendChannel2;
        c07931.J$0 = j;
        c07931.label = 3;
        if (DelayKt.delay(j, c07931) == coroutine_suspended) {
        }
        sendChannel = sendChannel2;
        Unit unit2 = Unit.INSTANCE;
        c07931.L$0 = sendChannel;
        c07931.J$0 = j;
        c07931.label = 2;
        if (sendChannel.send(unit2, c07931) != coroutine_suspended) {
        }
    }
}
