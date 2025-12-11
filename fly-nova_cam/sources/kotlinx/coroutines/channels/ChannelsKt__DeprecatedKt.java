package kotlinx.coroutines.channels;

import com.yls.nova.libs.verticalseekbar.VerticalSeekBar;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.Deprecated;
import kotlin.DeprecationLevel;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.IndexedValue;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;

/* compiled from: Deprecated.kt */
@Metadata(m421d1 = {"\u0000 \u0001\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u001f\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010$\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\b\u0002\n\u0002\u0010!\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0010\"\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u001aJ\u0010\u0000\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\u0005\u0012\u0004\u0012\u00020\u00060\u0001j\u0002`\u00072\u001a\u0010\b\u001a\u000e\u0012\n\b\u0001\u0012\u0006\u0012\u0002\b\u00030\n0\t\"\u0006\u0012\u0002\b\u00030\nH\u0001¢\u0006\u0002\u0010\u000b\u001a!\u0010\f\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a1\u0010\u0010\u001a#\u0012\u0015\u0012\u0013\u0018\u00010\u0002¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\u0005\u0012\u0004\u0012\u00020\u00060\u0001j\u0002`\u0007*\u0006\u0012\u0002\b\u00030\nH\u0001\u001a!\u0010\u0011\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a\u001e\u0010\u0013\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0007\u001aZ\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u0010\u0015*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u0010\u0018\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00150\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a0\u0010\u001d\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001aT\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u0010 \u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a)\u0010!\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010\"\u001a\u00020\u0012H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010#\u001a+\u0010$\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010\"\u001a\u00020\u0012H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010#\u001aT\u0010%\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u0010 \u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001ai\u0010&\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u001727\u0010 \u001a3\b\u0001\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\"\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0'H\u0007ø\u0001\u0000¢\u0006\u0002\u0010(\u001aT\u0010)\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u0010 \u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a$\u0010*\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\b\b\u0000\u0010\u000e*\u00020\u001b*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\nH\u0001\u001aA\u0010+\u001a\u0002H,\"\b\b\u0000\u0010\u000e*\u00020\u001b\"\u0010\b\u0001\u0010,*\n\u0012\u0006\b\u0000\u0012\u0002H\u000e0-*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\n2\u0006\u0010.\u001a\u0002H,H\u0087@ø\u0001\u0000¢\u0006\u0002\u0010/\u001a?\u0010+\u001a\u0002H,\"\b\b\u0000\u0010\u000e*\u00020\u001b\"\u000e\b\u0001\u0010,*\b\u0012\u0004\u0012\u0002H\u000e00*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\n2\u0006\u0010.\u001a\u0002H,H\u0087@ø\u0001\u0000¢\u0006\u0002\u00101\u001a!\u00102\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a#\u00103\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a`\u00104\u001a\b\u0012\u0004\u0012\u0002H50\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172(\u00106\u001a$\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\u0010\u0012\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H50\n0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a)\u00107\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u00108\u001a\u0002H\u000eH\u0087@ø\u0001\u0000¢\u0006\u0002\u00109\u001a!\u0010:\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a)\u0010;\u001a\u00020\u0012\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u00108\u001a\u0002H\u000eH\u0087@ø\u0001\u0000¢\u0006\u0002\u00109\u001a#\u0010<\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001aZ\u0010=\u001a\b\u0012\u0004\u0012\u0002H50\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u00106\u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H50\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0001ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001ao\u0010>\u001a\b\u0012\u0004\u0012\u0002H50\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u001727\u00106\u001a3\b\u0001\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\"\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H50\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0'H\u0001ø\u0001\u0000¢\u0006\u0002\u0010(\u001au\u0010?\u001a\b\u0012\u0004\u0012\u0002H50\n\"\u0004\b\u0000\u0010\u000e\"\b\b\u0001\u00105*\u00020\u001b*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u001729\u00106\u001a5\b\u0001\u0012\u0013\u0012\u00110\u0012¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(\"\u0012\u0004\u0012\u0002H\u000e\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H50\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0'H\u0007ø\u0001\u0000¢\u0006\u0002\u0010(\u001a`\u0010@\u001a\b\u0012\u0004\u0012\u0002H50\n\"\u0004\b\u0000\u0010\u000e\"\b\b\u0001\u00105*\u00020\u001b*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172$\u00106\u001a \b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\f\u0012\n\u0012\u0006\u0012\u0004\u0018\u0001H50\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a?\u0010A\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u001a\u0010B\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0Cj\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`DH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010E\u001a?\u0010F\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u001a\u0010B\u001a\u0016\u0012\u0006\b\u0000\u0012\u0002H\u000e0Cj\n\u0012\u0006\b\u0000\u0012\u0002H\u000e`DH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010E\u001a!\u0010G\u001a\u00020\r\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a$\u0010H\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\b\b\u0000\u0010\u000e*\u00020\u001b*\n\u0012\u0006\u0012\u0004\u0018\u0001H\u000e0\nH\u0007\u001a!\u0010I\u001a\u0002H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a#\u0010J\u001a\u0004\u0018\u0001H\u000e\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a0\u0010K\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010\u001e\u001a\u00020\u00122\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001aT\u0010L\u001a\b\u0012\u0004\u0012\u0002H\u000e0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u00172\"\u0010 \u001a\u001e\b\u0001\u0012\u0004\u0012\u0002H\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\r0\u001a\u0012\u0006\u0012\u0004\u0018\u00010\u001b0\u0019H\u0007ø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a9\u0010M\u001a\u0002H,\"\u0004\b\u0000\u0010\u000e\"\u000e\b\u0001\u0010,*\b\u0012\u0004\u0012\u0002H\u000e00*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010.\u001a\u0002H,H\u0081@ø\u0001\u0000¢\u0006\u0002\u00101\u001a;\u0010N\u001a\u0002H,\"\u0004\b\u0000\u0010\u000e\"\u0010\b\u0001\u0010,*\n\u0012\u0006\b\u0000\u0012\u0002H\u000e0-*\b\u0012\u0004\u0012\u0002H\u000e0\n2\u0006\u0010.\u001a\u0002H,H\u0081@ø\u0001\u0000¢\u0006\u0002\u0010/\u001a?\u0010O\u001a\u000e\u0012\u0004\u0012\u0002H\u0015\u0012\u0004\u0012\u0002HQ0P\"\u0004\b\u0000\u0010\u0015\"\u0004\b\u0001\u0010Q*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0015\u0012\u0004\u0012\u0002HQ0R0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001aU\u0010O\u001a\u0002HS\"\u0004\b\u0000\u0010\u0015\"\u0004\b\u0001\u0010Q\"\u0018\b\u0002\u0010S*\u0012\u0012\u0006\b\u0000\u0012\u0002H\u0015\u0012\u0006\b\u0000\u0012\u0002HQ0T*\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u0015\u0012\u0004\u0012\u0002HQ0R0\n2\u0006\u0010.\u001a\u0002HSH\u0081@ø\u0001\u0000¢\u0006\u0002\u0010U\u001a'\u0010V\u001a\b\u0012\u0004\u0012\u0002H\u000e0W\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a'\u0010X\u001a\b\u0012\u0004\u0012\u0002H\u000e0Y\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0081@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a'\u0010Z\u001a\b\u0012\u0004\u0012\u0002H\u000e0[\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\nH\u0087@ø\u0001\u0000¢\u0006\u0002\u0010\u000f\u001a.\u0010\\\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u000e0]0\n\"\u0004\b\u0000\u0010\u000e*\b\u0012\u0004\u0012\u0002H\u000e0\n2\b\b\u0002\u0010\u0016\u001a\u00020\u0017H\u0007\u001a?\u0010^\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u0002H\u000e\u0012\u0004\u0012\u0002H50R0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u00105*\b\u0012\u0004\u0012\u0002H\u000e0\n2\f\u0010_\u001a\b\u0012\u0004\u0012\u0002H50\nH\u0087\u0004\u001az\u0010^\u001a\b\u0012\u0004\u0012\u0002HQ0\n\"\u0004\b\u0000\u0010\u000e\"\u0004\b\u0001\u00105\"\u0004\b\u0002\u0010Q*\b\u0012\u0004\u0012\u0002H\u000e0\n2\f\u0010_\u001a\b\u0012\u0004\u0012\u0002H50\n2\b\b\u0002\u0010\u0016\u001a\u00020\u001726\u00106\u001a2\u0012\u0013\u0012\u0011H\u000e¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(`\u0012\u0013\u0012\u0011H5¢\u0006\f\b\u0003\u0012\b\b\u0004\u0012\u0004\b\b(a\u0012\u0004\u0012\u0002HQ0\u0019H\u0001\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006b"}, m422d2 = {"consumesAll", "Lkotlin/Function1;", "", "Lkotlin/ParameterName;", "name", "cause", "", "Lkotlinx/coroutines/CompletionHandler;", "channels", "", "Lkotlinx/coroutines/channels/ReceiveChannel;", "([Lkotlinx/coroutines/channels/ReceiveChannel;)Lkotlin/jvm/functions/Function1;", "any", "", "E", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "consumes", "count", "", "distinct", "distinctBy", "K", "context", "Lkotlin/coroutines/CoroutineContext;", "selector", "Lkotlin/Function2;", "Lkotlin/coroutines/Continuation;", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function2;)Lkotlinx/coroutines/channels/ReceiveChannel;", "drop", "n", "dropWhile", "predicate", "elementAt", "index", "(Lkotlinx/coroutines/channels/ReceiveChannel;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "elementAtOrNull", "filter", "filterIndexed", "Lkotlin/Function3;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlin/coroutines/CoroutineContext;Lkotlin/jvm/functions/Function3;)Lkotlinx/coroutines/channels/ReceiveChannel;", "filterNot", "filterNotNull", "filterNotNullTo", "C", "", "destination", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Collection;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Lkotlinx/coroutines/channels/SendChannel;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Lkotlinx/coroutines/channels/SendChannel;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "first", "firstOrNull", "flatMap", "R", "transform", "indexOf", "element", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "last", "lastIndexOf", "lastOrNull", "map", "mapIndexed", "mapIndexedNotNull", "mapNotNull", "maxWith", "comparator", "Ljava/util/Comparator;", "Lkotlin/Comparator;", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Comparator;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "minWith", "none", "requireNoNulls", "single", "singleOrNull", "take", "takeWhile", "toChannel", "toCollection", "toMap", "", "V", "Lkotlin/Pair;", "M", "", "(Lkotlinx/coroutines/channels/ReceiveChannel;Ljava/util/Map;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toMutableList", "", "toMutableSet", "", "toSet", "", "withIndex", "Lkotlin/collections/IndexedValue;", "zip", "other", "a", "b", "kotlinx-coroutines-core"}, m423k = 5, m424mv = {1, 6, 0}, m426xi = 48, m427xs = "kotlinx/coroutines/channels/ChannelsKt")
/* loaded from: classes.dex */
final /* synthetic */ class ChannelsKt__DeprecatedKt {

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0}, m433l = {404}, m434m = "any", m435n = {"$this$consume$iv"}, m436s = {"L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$any$1 */
    static final class C07511<E> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C07511(Continuation<? super C07511> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.any(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {487}, m434m = "count", m435n = {"count", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$count$1 */
    static final class C07541<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07541(Continuation<? super C07541> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.count(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0}, m433l = {38}, m434m = "elementAt", m435n = {"$this$consume$iv", "index", "count"}, m436s = {"L$0", "I$0", "I$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$elementAt$1 */
    static final class C07591<E> extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07591(Continuation<? super C07591> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.elementAt(null, 0, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0}, m433l = {53}, m434m = "elementAtOrNull", m435n = {"$this$consume$iv", "index", "count"}, m436s = {"L$0", "I$0", "I$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$elementAtOrNull$1 */
    static final class C07601<E> extends ContinuationImpl {
        int I$0;
        int I$1;
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07601(Continuation<? super C07601> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.elementAtOrNull(null, 0, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {487}, m434m = "filterNotNullTo", m435n = {"destination", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNullTo$1 */
    static final class C07651<E, C extends Collection<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07651(Continuation<? super C07651> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.filterNotNullTo((ReceiveChannel) null, (Collection) null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {487, 242}, m434m = "filterNotNullTo", m435n = {"destination", "$this$consume$iv$iv", "destination", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNullTo$3 */
    static final class C07663<E, C extends SendChannel<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07663(Continuation<? super C07663> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.filterNotNullTo((ReceiveChannel) null, (SendChannel) null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {65}, m434m = "first", m435n = {"$this$consume$iv", "iterator"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$first$1 */
    static final class C07671<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07671(Continuation<? super C07671> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.first(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {75}, m434m = "firstOrNull", m435n = {"$this$consume$iv", "iterator"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$firstOrNull$1 */
    static final class C07681<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07681(Continuation<? super C07681> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.firstOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0}, m433l = {487}, m434m = "indexOf", m435n = {"element", "index", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$indexOf$1 */
    static final class C07701<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C07701(Continuation<? super C07701> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.indexOf(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 1}, m433l = {97, 100}, m434m = "last", m435n = {"$this$consume$iv", "iterator", "$this$consume$iv", "iterator", "last"}, m436s = {"L$0", "L$1", "L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$last$1 */
    static final class C07711<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07711(Continuation<? super C07711> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.last(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0, 0}, m433l = {487}, m434m = "lastIndexOf", m435n = {"element", "lastIndex", "index", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$lastIndexOf$1 */
    static final class C07721<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;
        /* synthetic */ Object result;

        C07721(Continuation<? super C07721> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.lastIndexOf(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 1}, m433l = {123, 126}, m434m = "lastOrNull", m435n = {"$this$consume$iv", "iterator", "$this$consume$iv", "iterator", "last"}, m436s = {"L$0", "L$1", "L$0", "L$1", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$lastOrNull$1 */
    static final class C07731<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07731(Continuation<? super C07731> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.lastOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0, 1, 1, 1, 1}, m433l = {420, 422}, m434m = "maxWith", m435n = {"comparator", "$this$consume$iv", "iterator", "comparator", "$this$consume$iv", "iterator", "max"}, m436s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$maxWith$1 */
    static final class C07761<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C07761(Continuation<? super C07761> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.maxWith(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 0, 1, 1, 1, 1}, m433l = {434, 436}, m434m = "minWith", m435n = {"comparator", "$this$consume$iv", "iterator", "comparator", "$this$consume$iv", "iterator", "min"}, m436s = {"L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$minWith$1 */
    static final class C07771<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;
        /* synthetic */ Object result;

        C07771(Continuation<? super C07771> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.minWith(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0}, m433l = {447}, m434m = "none", m435n = {"$this$consume$iv"}, m436s = {"L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$none$1 */
    static final class C07781<E> extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        C07781(Continuation<? super C07781> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.none(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {136, 139}, m434m = "single", m435n = {"$this$consume$iv", "iterator", "$this$consume$iv", "single"}, m436s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$single$1 */
    static final class C07801<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07801(Continuation<? super C07801> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.single(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {149, 152}, m434m = "singleOrNull", m435n = {"$this$consume$iv", "iterator", "$this$consume$iv", "single"}, m436s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$singleOrNull$1 */
    static final class C07811<E> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        int label;
        /* synthetic */ Object result;

        C07811(Continuation<? super C07811> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt__DeprecatedKt.singleOrNull(null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {487, 278}, m434m = "toChannel", m435n = {"destination", "$this$consume$iv$iv", "destination", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1", "L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toChannel$1 */
    static final class C07841<E, C extends SendChannel<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07841(Continuation<? super C07841> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toChannel(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {487}, m434m = "toCollection", m435n = {"destination", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toCollection$1 */
    static final class C07851<E, C extends Collection<? super E>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07851(Continuation<? super C07851> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toCollection(null, null, this);
        }
    }

    /* compiled from: Deprecated.kt */
    @Metadata(m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt", m431f = "Deprecated.kt", m432i = {0, 0}, m433l = {487}, m434m = "toMap", m435n = {"destination", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$toMap$2 */
    static final class C07862<K, V, M extends Map<? super K, ? super V>> extends ContinuationImpl {
        Object L$0;
        Object L$1;
        Object L$2;
        int label;
        /* synthetic */ Object result;

        C07862(Continuation<? super C07862> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return ChannelsKt.toMap(null, null, this);
        }
    }

    public static final Function1<Throwable, Unit> consumesAll(final ReceiveChannel<?>... receiveChannelArr) {
        return new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumesAll.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) throws Throwable {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable th) throws Throwable {
                ReceiveChannel<?>[] receiveChannelArr2 = receiveChannelArr;
                int length = receiveChannelArr2.length;
                Throwable th2 = null;
                int i = 0;
                while (i < length) {
                    ReceiveChannel<?> receiveChannel = receiveChannelArr2[i];
                    i++;
                    try {
                        ChannelsKt.cancelConsumed(receiveChannel, th);
                    } catch (Throwable th3) {
                        if (th2 == null) {
                            th2 = th3;
                        } else {
                            ExceptionsKt.addSuppressed(th2, th3);
                        }
                    }
                }
                if (th2 != null) {
                    throw th2;
                }
            }
        };
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0060 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0061  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006e A[Catch: all -> 0x003b, TRY_LEAVE, TryCatch #2 {all -> 0x003b, blocks: (B:12:0x0037, B:25:0x0066, B:27:0x006e, B:33:0x007e, B:34:0x0095), top: B:46:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x007e A[Catch: all -> 0x003b, TRY_ENTER, TryCatch #2 {all -> 0x003b, blocks: (B:12:0x0037, B:25:0x0066, B:27:0x006e, B:33:0x007e, B:34:0x0095), top: B:46:0x0037 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0061 -> B:25:0x0066). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object elementAt(ReceiveChannel receiveChannel, int i, Continuation continuation) throws Throwable {
        C07591 c07591;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator it;
        int i2;
        Object objHasNext;
        if (continuation instanceof C07591) {
            c07591 = (C07591) continuation;
            if ((c07591.label & Integer.MIN_VALUE) != 0) {
                c07591.label -= Integer.MIN_VALUE;
            } else {
                c07591 = new C07591(continuation);
            }
        }
        Object obj = c07591.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c07591.label;
        try {
            if (i3 == 0) {
                ResultKt.throwOnFailure(obj);
                if (i < 0) {
                    throw new IndexOutOfBoundsException("ReceiveChannel doesn't contain element at index " + i + '.');
                }
                it = receiveChannel.iterator();
                i2 = 0;
                c07591.L$0 = receiveChannel;
                c07591.L$1 = it;
                c07591.I$0 = i;
                c07591.I$1 = i2;
                c07591.label = 1;
                objHasNext = it.hasNext(c07591);
                if (objHasNext != coroutine_suspended) {
                }
            } else if (i3 == 1) {
                int i4 = c07591.I$1;
                i = c07591.I$0;
                ChannelIterator channelIterator = (ChannelIterator) c07591.L$1;
                receiveChannel2 = (ReceiveChannel) c07591.L$0;
                try {
                    ResultKt.throwOnFailure(obj);
                    if (!((Boolean) obj).booleanValue()) {
                        Object next = channelIterator.next();
                        int i5 = i4 + 1;
                        if (i == i4) {
                            ChannelsKt.cancelConsumed(receiveChannel2, null);
                            return next;
                        }
                        it = channelIterator;
                        receiveChannel = receiveChannel2;
                        i2 = i5;
                        c07591.L$0 = receiveChannel;
                        c07591.L$1 = it;
                        c07591.I$0 = i;
                        c07591.I$1 = i2;
                        c07591.label = 1;
                        objHasNext = it.hasNext(c07591);
                        if (objHasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel;
                        i4 = i2;
                        channelIterator = it;
                        obj = objHasNext;
                        if (!((Boolean) obj).booleanValue()) {
                            throw new IndexOutOfBoundsException("ReceiveChannel doesn't contain element at index " + i + '.');
                        }
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        throw th;
                    } catch (Throwable th3) {
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        throw th3;
                    }
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        } catch (Throwable th4) {
            receiveChannel2 = receiveChannel;
            th = th4;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x0066 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0074 A[Catch: all -> 0x0089, TRY_LEAVE, TryCatch #0 {all -> 0x0089, blocks: (B:27:0x006c, B:29:0x0074, B:23:0x0056, B:22:0x0050), top: B:43:0x0050 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0085 A[DONT_GENERATE] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x0067 -> B:27:0x006c). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object elementAtOrNull(ReceiveChannel receiveChannel, int i, Continuation continuation) throws Throwable {
        C07601 c07601;
        ChannelIterator it;
        int i2;
        Throwable th;
        Throwable th2;
        ReceiveChannel receiveChannel2;
        Object objHasNext;
        if (continuation instanceof C07601) {
            c07601 = (C07601) continuation;
            if ((c07601.label & Integer.MIN_VALUE) != 0) {
                c07601.label -= Integer.MIN_VALUE;
            } else {
                c07601 = new C07601(continuation);
            }
        }
        Object obj = c07601.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i3 = c07601.label;
        if (i3 == 0) {
            ResultKt.throwOnFailure(obj);
            if (i < 0) {
                ChannelsKt.cancelConsumed(receiveChannel, null);
                return null;
            }
            try {
                it = receiveChannel.iterator();
                i2 = 0;
                th = null;
                c07601.L$0 = receiveChannel;
                c07601.L$1 = it;
                c07601.I$0 = i;
                c07601.I$1 = i2;
                c07601.label = 1;
                objHasNext = it.hasNext(c07601);
                if (objHasNext != coroutine_suspended) {
                }
            } catch (Throwable th3) {
                receiveChannel2 = receiveChannel;
                th2 = th3;
                throw th2;
            }
        } else {
            if (i3 != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            int i4 = c07601.I$1;
            i = c07601.I$0;
            ChannelIterator channelIterator = (ChannelIterator) c07601.L$1;
            receiveChannel2 = (ReceiveChannel) c07601.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                Throwable th4 = null;
                i2 = i4;
                receiveChannel = receiveChannel2;
                C07601 c076012 = c07601;
                ChannelIterator channelIterator2 = channelIterator;
                if (!((Boolean) obj).booleanValue()) {
                    Object next = channelIterator2.next();
                    int i5 = i2 + 1;
                    if (i == i2) {
                        return next;
                    }
                    it = channelIterator2;
                    c07601 = c076012;
                    th = th2;
                    i2 = i5;
                    c07601.L$0 = receiveChannel;
                    c07601.L$1 = it;
                    c07601.I$0 = i;
                    c07601.I$1 = i2;
                    c07601.label = 1;
                    objHasNext = it.hasNext(c07601);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    C07601 c076013 = c07601;
                    channelIterator2 = it;
                    obj = objHasNext;
                    th4 = th;
                    c076012 = c076013;
                    if (!((Boolean) obj).booleanValue()) {
                        return null;
                    }
                }
            } catch (Throwable th5) {
                th2 = th5;
                try {
                    throw th2;
                } finally {
                    ChannelsKt.cancelConsumed(receiveChannel2, th2);
                }
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x005c A[Catch: all -> 0x0032, TRY_LEAVE, TryCatch #1 {all -> 0x0032, blocks: (B:12:0x002e, B:23:0x0054, B:25:0x005c, B:28:0x0065, B:29:0x006c), top: B:38:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0065 A[Catch: all -> 0x0032, TRY_ENTER, TryCatch #1 {all -> 0x0032, blocks: (B:12:0x002e, B:23:0x0054, B:25:0x005c, B:28:0x0065, B:29:0x006c), top: B:38:0x002e }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object first(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07671 c07671;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator channelIterator;
        if (continuation instanceof C07671) {
            c07671 = (C07671) continuation;
            if ((c07671.label & Integer.MIN_VALUE) != 0) {
                c07671.label -= Integer.MIN_VALUE;
            } else {
                c07671 = new C07671(continuation);
            }
        }
        Object obj = c07671.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07671.label;
        if (i != 0) {
            if (i == 1) {
                channelIterator = (ChannelIterator) c07671.L$1;
                receiveChannel2 = (ReceiveChannel) c07671.L$0;
                try {
                    ResultKt.throwOnFailure(obj);
                    if (((Boolean) obj).booleanValue()) {
                        throw new NoSuchElementException("ReceiveChannel is empty.");
                    }
                    Object next = channelIterator.next();
                    ChannelsKt.cancelConsumed(receiveChannel2, null);
                    return next;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        throw th;
                    } catch (Throwable th3) {
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        throw th3;
                    }
                }
            }
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        try {
            ChannelIterator it = receiveChannel.iterator();
            c07671.L$0 = receiveChannel;
            c07671.L$1 = it;
            c07671.label = 1;
            Object objHasNext = it.hasNext(c07671);
            if (objHasNext == coroutine_suspended) {
                return coroutine_suspended;
            }
            receiveChannel2 = receiveChannel;
            channelIterator = it;
            obj = objHasNext;
            if (((Boolean) obj).booleanValue()) {
            }
        } catch (Throwable th4) {
            receiveChannel2 = receiveChannel;
            th = th4;
            throw th;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object firstOrNull(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07681 c07681;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator channelIterator;
        if (continuation instanceof C07681) {
            c07681 = (C07681) continuation;
            if ((c07681.label & Integer.MIN_VALUE) != 0) {
                c07681.label -= Integer.MIN_VALUE;
            } else {
                c07681 = new C07681(continuation);
            }
        }
        Object obj = c07681.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07681.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07681.L$0 = receiveChannel;
                c07681.L$1 = it;
                c07681.label = 1;
                Object objHasNext = it.hasNext(c07681);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj = objHasNext;
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
                throw th;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            channelIterator = (ChannelIterator) c07681.L$1;
            receiveChannel2 = (ReceiveChannel) c07681.L$0;
            try {
                ResultKt.throwOnFailure(obj);
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
        if (!((Boolean) obj).booleanValue()) {
            ChannelsKt.cancelConsumed(receiveChannel2, null);
            return null;
        }
        Object next = channelIterator.next();
        ChannelsKt.cancelConsumed(receiveChannel2, null);
        return next;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0064 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0065  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0070 A[Catch: all -> 0x0039, TryCatch #1 {all -> 0x0039, blocks: (B:12:0x0035, B:25:0x0068, B:27:0x0070, B:29:0x007a, B:32:0x0084, B:21:0x0054, B:33:0x008b), top: B:44:0x0035 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008b A[Catch: all -> 0x0039, TRY_LEAVE, TryCatch #1 {all -> 0x0039, blocks: (B:12:0x0035, B:25:0x0068, B:27:0x0070, B:29:0x007a, B:32:0x0084, B:21:0x0054, B:33:0x008b), top: B:44:0x0035 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0065 -> B:25:0x0068). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object indexOf(ReceiveChannel receiveChannel, Object obj, Continuation continuation) throws Throwable {
        C07701 c07701;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator it;
        Ref.IntRef intRef;
        Object obj2;
        Object objHasNext;
        if (continuation instanceof C07701) {
            c07701 = (C07701) continuation;
            if ((c07701.label & Integer.MIN_VALUE) != 0) {
                c07701.label -= Integer.MIN_VALUE;
            } else {
                c07701 = new C07701(continuation);
            }
        }
        Object obj3 = c07701.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07701.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj3);
            Ref.IntRef intRef2 = new Ref.IntRef();
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                intRef = intRef2;
                obj2 = obj;
                c07701.L$0 = obj2;
                c07701.L$1 = intRef;
                c07701.L$2 = receiveChannel2;
                c07701.L$3 = it;
                c07701.label = 1;
                objHasNext = it.hasNext(c07701);
                if (objHasNext != coroutine_suspended) {
                }
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
            }
        } else {
            if (i != 1) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            it = (ChannelIterator) c07701.L$3;
            receiveChannel2 = (ReceiveChannel) c07701.L$2;
            intRef = (Ref.IntRef) c07701.L$1;
            Object obj4 = c07701.L$0;
            try {
                ResultKt.throwOnFailure(obj3);
                if (((Boolean) obj3).booleanValue()) {
                    if (Intrinsics.areEqual(obj4, it.next())) {
                        Integer numBoxInt = Boxing.boxInt(intRef.element);
                        ChannelsKt.cancelConsumed(receiveChannel2, null);
                        return numBoxInt;
                    }
                    intRef.element++;
                    obj2 = obj4;
                    c07701.L$0 = obj2;
                    c07701.L$1 = intRef;
                    c07701.L$2 = receiveChannel2;
                    c07701.L$3 = it;
                    c07701.label = 1;
                    objHasNext = it.hasNext(c07701);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    obj4 = obj2;
                    obj3 = objHasNext;
                    if (((Boolean) obj3).booleanValue()) {
                    }
                } else {
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed(receiveChannel2, null);
                    return Boxing.boxInt(-1);
                }
            } catch (Throwable th3) {
                th = th3;
            }
        }
        try {
            throw th;
        } catch (Throwable th4) {
            ChannelsKt.cancelConsumed(receiveChannel2, th);
            throw th4;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:35:0x0087 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0094 A[Catch: all -> 0x0038, TRY_LEAVE, TryCatch #2 {all -> 0x0038, blocks: (B:13:0x0034, B:37:0x008c, B:39:0x0094), top: B:54:0x0034 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:36:0x0088 -> B:37:0x008c). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object last(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07711 c07711;
        ReceiveChannel receiveChannel2;
        ChannelIterator channelIterator;
        Object next;
        ChannelIterator channelIterator2;
        Object objHasNext;
        if (continuation instanceof C07711) {
            c07711 = (C07711) continuation;
            if ((c07711.label & Integer.MIN_VALUE) != 0) {
                c07711.label -= Integer.MIN_VALUE;
            } else {
                c07711 = new C07711(continuation);
            }
        }
        Object obj = c07711.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07711.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07711.L$0 = receiveChannel;
                c07711.L$1 = it;
                c07711.label = 1;
                Object objHasNext2 = it.hasNext(c07711);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj = objHasNext2;
            } catch (Throwable th) {
                receiveChannel2 = receiveChannel;
                th = th;
                throw th;
            }
        } else if (i == 1) {
            channelIterator = (ChannelIterator) c07711.L$1;
            receiveChannel2 = (ReceiveChannel) c07711.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        } else if (i == 2) {
            Object obj2 = c07711.L$2;
            channelIterator2 = (ChannelIterator) c07711.L$1;
            ReceiveChannel receiveChannel3 = (ReceiveChannel) c07711.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    next = channelIterator2.next();
                    receiveChannel = receiveChannel3;
                    c07711.L$0 = receiveChannel;
                    c07711.L$1 = channelIterator2;
                    c07711.L$2 = next;
                    c07711.label = 2;
                    objHasNext = channelIterator2.hasNext(c07711);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    receiveChannel3 = receiveChannel;
                    obj2 = next;
                    obj = objHasNext;
                    if (!((Boolean) obj).booleanValue()) {
                        ChannelsKt.cancelConsumed(receiveChannel3, null);
                        return obj2;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                receiveChannel2 = receiveChannel3;
                try {
                    throw th;
                } catch (Throwable th4) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    throw th4;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (!((Boolean) obj).booleanValue()) {
            throw new NoSuchElementException("ReceiveChannel is empty.");
        }
        next = channelIterator.next();
        ReceiveChannel receiveChannel4 = receiveChannel2;
        channelIterator2 = channelIterator;
        receiveChannel = receiveChannel4;
        c07711.L$0 = receiveChannel;
        c07711.L$1 = channelIterator2;
        c07711.L$2 = next;
        c07711.label = 2;
        objHasNext = channelIterator2.hasNext(c07711);
        if (objHasNext != coroutine_suspended) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0073 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0074  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x007f A[Catch: all -> 0x003d, TryCatch #2 {all -> 0x003d, blocks: (B:12:0x0039, B:25:0x0077, B:27:0x007f, B:29:0x0089, B:30:0x008d, B:21:0x0061, B:31:0x0094), top: B:44:0x0039 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0094 A[Catch: all -> 0x003d, TRY_LEAVE, TryCatch #2 {all -> 0x003d, blocks: (B:12:0x0039, B:25:0x0077, B:27:0x007f, B:29:0x0089, B:30:0x008d, B:21:0x0061, B:31:0x0094), top: B:44:0x0039 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x0074 -> B:25:0x0077). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object lastIndexOf(ReceiveChannel receiveChannel, Object obj, Continuation continuation) throws Throwable {
        C07721 c07721;
        Ref.IntRef intRef;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator it;
        Ref.IntRef intRef2;
        Object obj2;
        Object objHasNext;
        if (continuation instanceof C07721) {
            c07721 = (C07721) continuation;
            if ((c07721.label & Integer.MIN_VALUE) != 0) {
                c07721.label -= Integer.MIN_VALUE;
            } else {
                c07721 = new C07721(continuation);
            }
        }
        Object obj3 = c07721.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07721.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj3);
            Ref.IntRef intRef3 = new Ref.IntRef();
            intRef3.element = -1;
            intRef = new Ref.IntRef();
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                intRef2 = intRef3;
                obj2 = obj;
                c07721.L$0 = obj2;
                c07721.L$1 = intRef2;
                c07721.L$2 = intRef;
                c07721.L$3 = receiveChannel2;
                c07721.L$4 = it;
                c07721.label = 1;
                objHasNext = it.hasNext(c07721);
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
            it = (ChannelIterator) c07721.L$4;
            receiveChannel2 = (ReceiveChannel) c07721.L$3;
            intRef = (Ref.IntRef) c07721.L$2;
            intRef2 = (Ref.IntRef) c07721.L$1;
            Object obj4 = c07721.L$0;
            try {
                ResultKt.throwOnFailure(obj3);
                if (((Boolean) obj3).booleanValue()) {
                    if (Intrinsics.areEqual(obj4, it.next())) {
                        intRef2.element = intRef.element;
                    }
                    intRef.element++;
                    obj2 = obj4;
                    c07721.L$0 = obj2;
                    c07721.L$1 = intRef2;
                    c07721.L$2 = intRef;
                    c07721.L$3 = receiveChannel2;
                    c07721.L$4 = it;
                    c07721.label = 1;
                    objHasNext = it.hasNext(c07721);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    obj4 = obj2;
                    obj3 = objHasNext;
                    if (((Boolean) obj3).booleanValue()) {
                    }
                } else {
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed(receiveChannel2, null);
                    return Boxing.boxInt(intRef2.element);
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
    }

    /* JADX WARN: Removed duplicated region for block: B:37:0x008b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:38:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0098 A[Catch: all -> 0x0038, TRY_LEAVE, TryCatch #3 {all -> 0x0038, blocks: (B:13:0x0034, B:39:0x0090, B:41:0x0098), top: B:57:0x0034 }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x009e  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:38:0x008c -> B:39:0x0090). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object lastOrNull(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07731 c07731;
        ReceiveChannel receiveChannel2;
        ChannelIterator channelIterator;
        Object next;
        ChannelIterator channelIterator2;
        Object objHasNext;
        if (continuation instanceof C07731) {
            c07731 = (C07731) continuation;
            if ((c07731.label & Integer.MIN_VALUE) != 0) {
                c07731.label -= Integer.MIN_VALUE;
            } else {
                c07731 = new C07731(continuation);
            }
        }
        Object obj = c07731.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07731.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07731.L$0 = receiveChannel;
                c07731.L$1 = it;
                c07731.label = 1;
                Object objHasNext2 = it.hasNext(c07731);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj = objHasNext2;
            } catch (Throwable th) {
                receiveChannel2 = receiveChannel;
                th = th;
                throw th;
            }
        } else if (i == 1) {
            channelIterator = (ChannelIterator) c07731.L$1;
            receiveChannel2 = (ReceiveChannel) c07731.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        } else {
            if (i != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Object obj2 = c07731.L$2;
            channelIterator2 = (ChannelIterator) c07731.L$1;
            ReceiveChannel receiveChannel3 = (ReceiveChannel) c07731.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    next = channelIterator2.next();
                    receiveChannel = receiveChannel3;
                    c07731.L$0 = receiveChannel;
                    c07731.L$1 = channelIterator2;
                    c07731.L$2 = next;
                    c07731.label = 2;
                    objHasNext = channelIterator2.hasNext(c07731);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    receiveChannel3 = receiveChannel;
                    obj2 = next;
                    obj = objHasNext;
                    if (!((Boolean) obj).booleanValue()) {
                        ChannelsKt.cancelConsumed(receiveChannel3, null);
                        return obj2;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                receiveChannel2 = receiveChannel3;
                try {
                    throw th;
                } catch (Throwable th4) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    throw th4;
                }
            }
        }
        if (!((Boolean) obj).booleanValue()) {
            ChannelsKt.cancelConsumed(receiveChannel2, null);
            return null;
        }
        next = channelIterator.next();
        ReceiveChannel receiveChannel4 = receiveChannel2;
        channelIterator2 = channelIterator;
        receiveChannel = receiveChannel4;
        c07731.L$0 = receiveChannel;
        c07731.L$1 = channelIterator2;
        c07731.L$2 = next;
        c07731.label = 2;
        objHasNext = channelIterator2.hasNext(c07731);
        if (objHasNext != coroutine_suspended) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x006d A[Catch: all -> 0x004b, TRY_LEAVE, TryCatch #2 {all -> 0x004b, blocks: (B:20:0x0047, B:29:0x0065, B:31:0x006d, B:41:0x0097, B:42:0x009e), top: B:53:0x0047 }] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0097 A[Catch: all -> 0x004b, TRY_ENTER, TryCatch #2 {all -> 0x004b, blocks: (B:20:0x0047, B:29:0x0065, B:31:0x006d, B:41:0x0097, B:42:0x009e), top: B:53:0x0047 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object single(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07801 c07801;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel3;
        Object obj;
        if (continuation instanceof C07801) {
            c07801 = (C07801) continuation;
            if ((c07801.label & Integer.MIN_VALUE) != 0) {
                c07801.label -= Integer.MIN_VALUE;
            } else {
                c07801 = new C07801(continuation);
            }
        }
        Object obj2 = c07801.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07801.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj2);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07801.L$0 = receiveChannel;
                c07801.L$1 = it;
                c07801.label = 1;
                Object objHasNext = it.hasNext(c07801);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj2 = objHasNext;
                if (((Boolean) obj2).booleanValue()) {
                }
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
                throw th;
            }
        } else if (i == 1) {
            channelIterator = (ChannelIterator) c07801.L$1;
            receiveChannel2 = (ReceiveChannel) c07801.L$0;
            try {
                ResultKt.throwOnFailure(obj2);
                if (((Boolean) obj2).booleanValue()) {
                    throw new NoSuchElementException("ReceiveChannel is empty.");
                }
                Object next = channelIterator.next();
                c07801.L$0 = receiveChannel2;
                c07801.L$1 = next;
                c07801.label = 2;
                Object objHasNext2 = channelIterator.hasNext(c07801);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel3 = receiveChannel2;
                obj2 = objHasNext2;
                obj = next;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        } else if (i == 2) {
            obj = c07801.L$1;
            receiveChannel3 = (ReceiveChannel) c07801.L$0;
            try {
                ResultKt.throwOnFailure(obj2);
            } catch (Throwable th4) {
                th = th4;
                receiveChannel2 = receiveChannel3;
                try {
                    throw th;
                } catch (Throwable th5) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    throw th5;
                }
            }
        } else {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        if (((Boolean) obj2).booleanValue()) {
            throw new IllegalArgumentException("ReceiveChannel has more than one element.");
        }
        ChannelsKt.cancelConsumed(receiveChannel3, null);
        return obj;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x008f  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object singleOrNull(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07811 c07811;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator channelIterator;
        ReceiveChannel receiveChannel3;
        Object obj;
        if (continuation instanceof C07811) {
            c07811 = (C07811) continuation;
            if ((c07811.label & Integer.MIN_VALUE) != 0) {
                c07811.label -= Integer.MIN_VALUE;
            } else {
                c07811 = new C07811(continuation);
            }
        }
        Object obj2 = c07811.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07811.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj2);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07811.L$0 = receiveChannel;
                c07811.L$1 = it;
                c07811.label = 1;
                Object objHasNext = it.hasNext(c07811);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj2 = objHasNext;
            } catch (Throwable th2) {
                receiveChannel2 = receiveChannel;
                th = th2;
                throw th;
            }
        } else {
            if (i != 1) {
                if (i != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                obj = c07811.L$1;
                receiveChannel3 = (ReceiveChannel) c07811.L$0;
                try {
                    ResultKt.throwOnFailure(obj2);
                    if (((Boolean) obj2).booleanValue()) {
                        ChannelsKt.cancelConsumed(receiveChannel3, null);
                        return obj;
                    }
                    ChannelsKt.cancelConsumed(receiveChannel3, null);
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    receiveChannel2 = receiveChannel3;
                    try {
                        throw th;
                    } catch (Throwable th4) {
                        ChannelsKt.cancelConsumed(receiveChannel2, th);
                        throw th4;
                    }
                }
            }
            channelIterator = (ChannelIterator) c07811.L$1;
            receiveChannel2 = (ReceiveChannel) c07811.L$0;
            try {
                ResultKt.throwOnFailure(obj2);
            } catch (Throwable th5) {
                th = th5;
                throw th;
            }
        }
        if (!((Boolean) obj2).booleanValue()) {
            ChannelsKt.cancelConsumed(receiveChannel2, null);
            return null;
        }
        Object next = channelIterator.next();
        c07811.L$0 = receiveChannel2;
        c07811.L$1 = next;
        c07811.label = 2;
        Object objHasNext2 = channelIterator.hasNext(c07811);
        if (objHasNext2 == coroutine_suspended) {
            return coroutine_suspended;
        }
        receiveChannel3 = receiveChannel2;
        obj2 = objHasNext2;
        obj = next;
        if (((Boolean) obj2).booleanValue()) {
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$drop$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 2}, m433l = {164, 169, 170}, m434m = "invokeSuspend", m435n = {"$this$produce", "remaining", "$this$produce", "$this$produce"}, m436s = {"L$0", "I$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$drop$1 */
    static final class C07571<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {

        /* renamed from: $n */
        final /* synthetic */ int f84$n;
        final /* synthetic */ ReceiveChannel<E> $this_drop;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07571(int i, ReceiveChannel<? extends E> receiveChannel, Continuation<? super C07571> continuation) {
            super(2, continuation);
            this.f84$n = i;
            this.$this_drop = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07571 c07571 = new C07571(this.f84$n, this.$this_drop, continuation);
            c07571.L$0 = obj;
            return c07571;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07571) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code restructure failed: missing block: B:26:0x0078, code lost:
        
            if (r1 == 0) goto L27;
         */
        /* JADX WARN: Removed duplicated region for block: B:25:0x0073  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0090 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:32:0x0091  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x009c  */
        /* JADX WARN: Removed duplicated region for block: B:38:0x00b0  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:21:0x0068 -> B:23:0x006b). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:36:0x00ad -> B:8:0x001c). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            int i;
            ChannelIterator<E> it;
            ProducerScope producerScope2;
            ChannelIterator<E> it2;
            ProducerScope producerScope3;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i2 = this.label;
            if (i2 == 0) {
                ResultKt.throwOnFailure(obj);
                producerScope = (ProducerScope) this.L$0;
                i = this.f84$n;
                if (!(i >= 0)) {
                    throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
                }
                if (i > 0) {
                    it = this.$this_drop.iterator();
                    producerScope2 = producerScope;
                    this.L$0 = producerScope2;
                    this.L$1 = it;
                    this.I$0 = i;
                    this.label = 1;
                    obj = it.hasNext(this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    if (((Boolean) obj).booleanValue()) {
                    }
                    producerScope = producerScope2;
                }
                it2 = this.$this_drop.iterator();
                this.L$0 = producerScope;
                this.L$1 = it2;
                this.label = 2;
                objHasNext = it2.hasNext(this);
                if (objHasNext != coroutine_suspended) {
                }
            } else if (i2 == 1) {
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (((Boolean) obj).booleanValue()) {
                    it.next();
                    i--;
                }
                producerScope = producerScope2;
                it2 = this.$this_drop.iterator();
                this.L$0 = producerScope;
                this.L$1 = it2;
                this.label = 2;
                objHasNext = it2.hasNext(this);
                if (objHasNext != coroutine_suspended) {
                }
            } else if (i2 == 2) {
                it2 = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                }
            } else {
                if (i2 != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it2 = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                producerScope = producerScope3;
                this.L$0 = producerScope;
                this.L$1 = it2;
                this.label = 2;
                objHasNext = it2.hasNext(this);
                if (objHasNext != coroutine_suspended) {
                    return coroutine_suspended;
                }
                producerScope3 = producerScope;
                obj = objHasNext;
                if (!((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope3;
                    this.L$1 = it2;
                    this.label = 3;
                    if (producerScope3.send(it2.next(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope = producerScope3;
                    this.L$0 = producerScope;
                    this.L$1 = it2;
                    this.label = 2;
                    objHasNext = it2.hasNext(this);
                    if (objHasNext != coroutine_suspended) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            }
        }
    }

    public static /* synthetic */ ReceiveChannel drop$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return drop(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel drop(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07571(i, receiveChannel, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel dropWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return dropWhile(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$dropWhile$1", m431f = "Deprecated.kt", m432i = {0, 1, 1, 2, 3, 4}, m433l = {181, 182, 183, 187, 188}, m434m = "invokeSuspend", m435n = {"$this$produce", "$this$produce", "e", "$this$produce", "$this$produce", "$this$produce"}, m436s = {"L$0", "L$0", "L$2", "L$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$dropWhile$1 */
    static final class C07581<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_dropWhile;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07581(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C07581> continuation) {
            super(2, continuation);
            this.$this_dropWhile = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07581 c07581 = new C07581(this.$this_dropWhile, this.$predicate, continuation);
            c07581.L$0 = obj;
            return c07581;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07581) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0084 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:25:0x008d  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x00af  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00c3  */
        /* JADX WARN: Removed duplicated region for block: B:39:0x00da A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00db  */
        /* JADX WARN: Removed duplicated region for block: B:43:0x00e7  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x00fb  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:28:0x00a2 -> B:16:0x0054). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:44:0x00f8 -> B:10:0x0023). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ChannelIterator<E> it;
            ProducerScope producerScope;
            ProducerScope producerScope2;
            ChannelIterator<E> it2;
            ProducerScope producerScope3;
            ChannelIterator<E> channelIterator;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope4 = (ProducerScope) this.L$0;
                it = this.$this_dropWhile.iterator();
                producerScope = producerScope4;
                this.L$0 = producerScope;
                this.L$1 = it;
                this.L$2 = null;
                this.label = 1;
                obj = it.hasNext(this);
                if (obj == coroutine_suspended) {
                }
                ProducerScope producerScope5 = producerScope;
                ChannelIterator<E> channelIterator2 = it;
                producerScope2 = producerScope5;
                if (((Boolean) obj).booleanValue()) {
                }
                it2 = this.$this_dropWhile.iterator();
                this.L$0 = producerScope2;
                this.L$1 = it2;
                this.label = 4;
                objHasNext = it2.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                }
            } else if (i == 1) {
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope52 = producerScope;
                ChannelIterator<E> channelIterator22 = it;
                producerScope2 = producerScope52;
                if (((Boolean) obj).booleanValue()) {
                }
                it2 = this.$this_dropWhile.iterator();
                this.L$0 = producerScope2;
                this.L$1 = it2;
                this.label = 4;
                objHasNext = it2.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                }
            } else if (i == 2) {
                Object obj2 = this.L$2;
                ChannelIterator<E> channelIterator3 = (ChannelIterator) this.L$1;
                ProducerScope producerScope6 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                ChannelIterator<E> channelIterator4 = channelIterator3;
                E e = obj2;
                it = channelIterator4;
                if (((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope6;
                    this.L$1 = null;
                    this.L$2 = null;
                    this.label = 3;
                    if (producerScope6.send(e, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope2 = producerScope6;
                    it2 = this.$this_dropWhile.iterator();
                    this.L$0 = producerScope2;
                    this.L$1 = it2;
                    this.label = 4;
                    objHasNext = it2.hasNext(this);
                    if (objHasNext == coroutine_suspended) {
                    }
                } else {
                    producerScope = producerScope6;
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.L$2 = null;
                    this.label = 1;
                    obj = it.hasNext(this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    ProducerScope producerScope522 = producerScope;
                    ChannelIterator<E> channelIterator222 = it;
                    producerScope2 = producerScope522;
                    if (((Boolean) obj).booleanValue()) {
                        E next = channelIterator222.next();
                        Function2<E, Continuation<? super Boolean>, Object> function2 = this.$predicate;
                        this.L$0 = producerScope2;
                        this.L$1 = channelIterator222;
                        this.L$2 = next;
                        this.label = 2;
                        Object objInvoke = function2.invoke(next, this);
                        if (objInvoke == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        channelIterator4 = channelIterator222;
                        e = next;
                        obj = objInvoke;
                        producerScope6 = producerScope2;
                        it = channelIterator4;
                        if (((Boolean) obj).booleanValue()) {
                        }
                    }
                    it2 = this.$this_dropWhile.iterator();
                    this.L$0 = producerScope2;
                    this.L$1 = it2;
                    this.label = 4;
                    objHasNext = it2.hasNext(this);
                    if (objHasNext == coroutine_suspended) {
                    }
                }
            } else if (i == 3) {
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                it2 = this.$this_dropWhile.iterator();
                this.L$0 = producerScope2;
                this.L$1 = it2;
                this.label = 4;
                objHasNext = it2.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                }
            } else if (i == 4) {
                channelIterator = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                }
            } else {
                if (i != 5) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelIterator = (ChannelIterator) this.L$1;
                producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                it2 = channelIterator;
                producerScope2 = producerScope3;
                this.L$0 = producerScope2;
                this.L$1 = it2;
                this.label = 4;
                objHasNext = it2.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                ProducerScope producerScope7 = producerScope2;
                channelIterator = it2;
                obj = objHasNext;
                producerScope3 = producerScope7;
                if (!((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope3;
                    this.L$1 = channelIterator;
                    this.label = 5;
                    if (producerScope3.send(channelIterator.next(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    it2 = channelIterator;
                    producerScope2 = producerScope3;
                    this.L$0 = producerScope2;
                    this.L$1 = it2;
                    this.label = 4;
                    objHasNext = it2.hasNext(this);
                    if (objHasNext == coroutine_suspended) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel dropWhile(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07581(receiveChannel, function2, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel filter$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.filter(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filter$1", m431f = "Deprecated.kt", m432i = {0, 1, 1, 2}, m433l = {198, 199, 199}, m434m = "invokeSuspend", m435n = {"$this$produce", "$this$produce", "e", "$this$produce"}, m436s = {"L$0", "L$0", "L$2", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filter$1 */
    static final class C07611<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_filter;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07611(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C07611> continuation) {
            super(2, continuation);
            this.$this_filter = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07611 c07611 = new C07611(this.$this_filter, this.$predicate, continuation);
            c07611.L$0 = obj;
            return c07611;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07611) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x0051, code lost:
        
            r6 = r7;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:18:0x006b  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x008c  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x009e  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00a0  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ChannelIterator<E> it;
            ProducerScope producerScope;
            ProducerScope producerScope2;
            E e;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                it = this.$this_filter.iterator();
                producerScope = producerScope3;
            } else if (i == 1) {
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    E next = it.next();
                    Function2<E, Continuation<? super Boolean>, Object> function2 = this.$predicate;
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.L$2 = next;
                    this.label = 2;
                    Object objInvoke = function2.invoke(next, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    ProducerScope producerScope4 = producerScope;
                    e = next;
                    obj = objInvoke;
                    producerScope2 = producerScope4;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            } else if (i == 2) {
                Object obj2 = this.L$2;
                ChannelIterator<E> channelIterator = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                e = obj2;
                it = channelIterator;
                if (!((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope2;
                    this.L$1 = it;
                    this.L$2 = null;
                    this.label = 3;
                    if (producerScope2.send(e, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope = producerScope2;
                }
            } else {
                if (i != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.L$2 = null;
            this.label = 1;
            obj = it.hasNext(this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    public static final <E> ReceiveChannel<E> filter(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07611(receiveChannel, function2, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel filterIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return filterIndexed(receiveChannel, coroutineContext, function3);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterIndexed$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 1, 2, 2}, m433l = {211, 212, 212}, m434m = "invokeSuspend", m435n = {"$this$produce", "index", "$this$produce", "e", "index", "$this$produce", "index"}, m436s = {"L$0", "I$0", "L$0", "L$2", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterIndexed$1 */
    static final class C07621<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function3<Integer, E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_filterIndexed;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07621(ReceiveChannel<? extends E> receiveChannel, Function3<? super Integer, ? super E, ? super Continuation<? super Boolean>, ? extends Object> function3, Continuation<? super C07621> continuation) {
            super(2, continuation);
            this.$this_filterIndexed = receiveChannel;
            this.$predicate = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07621 c07621 = new C07621(this.$this_filterIndexed, this.$predicate, continuation);
            c07621.L$0 = obj;
            return c07621;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07621) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code restructure failed: missing block: B:30:0x005b, code lost:
        
            r7 = r8;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0077  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00a0  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00b4  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00b6  */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            ChannelIterator<E> it;
            int i;
            ProducerScope producerScope2;
            E e;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i2 = this.label;
            if (i2 == 0) {
                ResultKt.throwOnFailure(obj);
                producerScope = (ProducerScope) this.L$0;
                it = this.$this_filterIndexed.iterator();
                i = 0;
            } else if (i2 == 1) {
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    E next = it.next();
                    Function3<Integer, E, Continuation<? super Boolean>, Object> function3 = this.$predicate;
                    int i3 = i + 1;
                    Integer numBoxInt = Boxing.boxInt(i);
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.L$2 = next;
                    this.I$0 = i3;
                    this.label = 2;
                    Object objInvoke = function3.invoke(numBoxInt, next, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope2 = producerScope;
                    e = next;
                    obj = objInvoke;
                    i = i3;
                    if (!((Boolean) obj).booleanValue()) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            } else if (i2 == 2) {
                i = this.I$0;
                Object obj2 = this.L$2;
                ChannelIterator<E> channelIterator = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                e = obj2;
                it = channelIterator;
                if (!((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope2;
                    this.L$1 = it;
                    this.L$2 = null;
                    this.I$0 = i;
                    this.label = 3;
                    if (producerScope2.send(e, this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope = producerScope2;
                }
            } else {
                if (i2 != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.L$2 = null;
            this.I$0 = i;
            this.label = 1;
            obj = it.hasNext(this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel filterIndexed(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07621(receiveChannel, function3, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel filterNot$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return filterNot(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u0002H\u0002H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "it"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNot$1", m431f = "Deprecated.kt", m432i = {}, m433l = {222}, m434m = "invokeSuspend", m435n = {}, m436s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNot$1 */
    static final class C07631<E> extends SuspendLambda implements Function2<E, Continuation<? super Boolean>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07631(Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C07631> continuation) {
            super(2, continuation);
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07631 c07631 = new C07631(this.$predicate, continuation);
            c07631.L$0 = obj;
            return c07631;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Continuation<? super Boolean> continuation) {
            return invoke2((C07631<E>) obj, continuation);
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(E e, Continuation<? super Boolean> continuation) {
            return ((C07631) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                Object obj2 = this.L$0;
                Function2<E, Continuation<? super Boolean>, Object> function2 = this.$predicate;
                this.label = 1;
                obj = function2.invoke(obj2, this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            return Boxing.boxBoolean(!((Boolean) obj).booleanValue());
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel filterNot(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2) {
        return ChannelsKt.filter(receiveChannel, coroutineContext, new C07631(function2, null));
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u0001H\u0002H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "", "it"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNull$1", m431f = "Deprecated.kt", m432i = {}, m433l = {}, m434m = "invokeSuspend", m435n = {}, m436s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$filterNotNull$1 */
    static final class C07641<E> extends SuspendLambda implements Function2<E, Continuation<? super Boolean>, Object> {
        /* synthetic */ Object L$0;
        int label;

        C07641(Continuation<? super C07641> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07641 c07641 = new C07641(continuation);
            c07641.L$0 = obj;
            return c07641;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Continuation<? super Boolean> continuation) {
            return invoke2((C07641<E>) obj, continuation);
        }

        /* renamed from: invoke, reason: avoid collision after fix types in other method */
        public final Object invoke2(E e, Continuation<? super Boolean> continuation) {
            return ((C07641) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            return Boxing.boxBoolean(this.L$0 != null);
        }
    }

    public static final <E> ReceiveChannel<E> filterNotNull(ReceiveChannel<? extends E> receiveChannel) {
        return filter$default(receiveChannel, null, new C07641(null), 1, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x005a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0066 A[Catch: all -> 0x0037, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:29:0x006c, B:21:0x004c, B:31:0x0071), top: B:42:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0071 A[Catch: all -> 0x0037, TRY_LEAVE, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:29:0x006c, B:21:0x004c, B:31:0x0071), top: B:42:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x005b -> B:25:0x005e). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object filterNotNullTo(ReceiveChannel receiveChannel, Collection collection, Continuation continuation) throws Throwable {
        C07651 c07651;
        ReceiveChannel receiveChannel2;
        Throwable th;
        ChannelIterator it;
        Collection collection2;
        Object objHasNext;
        if (continuation instanceof C07651) {
            c07651 = (C07651) continuation;
            if ((c07651.label & Integer.MIN_VALUE) != 0) {
                c07651.label -= Integer.MIN_VALUE;
            } else {
                c07651 = new C07651(continuation);
            }
        }
        Object obj = c07651.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07651.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                collection2 = collection;
                c07651.L$0 = collection2;
                c07651.L$1 = receiveChannel2;
                c07651.L$2 = it;
                c07651.label = 1;
                objHasNext = it.hasNext(c07651);
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
            it = (ChannelIterator) c07651.L$2;
            receiveChannel2 = (ReceiveChannel) c07651.L$1;
            Collection collection3 = (Collection) c07651.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed(receiveChannel2, null);
                    return collection3;
                }
                Object next = it.next();
                if (next != null) {
                    collection3.add(next);
                }
                collection2 = collection3;
                c07651.L$0 = collection2;
                c07651.L$1 = receiveChannel2;
                c07651.L$2 = it;
                c07651.label = 1;
                objHasNext = it.hasNext(c07651);
                if (objHasNext != coroutine_suspended) {
                    return coroutine_suspended;
                }
                collection3 = collection2;
                obj = objHasNext;
                if (!((Boolean) obj).booleanValue()) {
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
    }

    /* JADX WARN: Code restructure failed: missing block: B:51:0x0063, code lost:
    
        r9 = r0;
        r0 = r2;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007e A[Catch: all -> 0x009c, TryCatch #2 {all -> 0x009c, blocks: (B:25:0x0063, B:29:0x0076, B:31:0x007e, B:33:0x0084, B:37:0x0096, B:24:0x005f), top: B:49:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0093  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0096 A[Catch: all -> 0x009c, TRY_LEAVE, TryCatch #2 {all -> 0x009c, blocks: (B:25:0x0063, B:29:0x0076, B:31:0x007e, B:33:0x0084, B:37:0x0096, B:24:0x005f), top: B:49:0x005f }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r8v0, types: [kotlinx.coroutines.channels.SendChannel] */
    /* JADX WARN: Type inference failed for: r8v10 */
    /* JADX WARN: Type inference failed for: r8v11, types: [java.lang.Object, kotlinx.coroutines.channels.SendChannel] */
    /* JADX WARN: Type inference failed for: r8v12, types: [java.lang.Object] */
    /* JADX WARN: Type inference failed for: r8v13 */
    /* JADX WARN: Type inference failed for: r8v3 */
    /* JADX WARN: Type inference failed for: r8v7 */
    /* JADX WARN: Type inference failed for: r8v8, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object filterNotNullTo(ReceiveChannel receiveChannel, SendChannel sendChannel, Continuation continuation) throws Throwable {
        C07663 c07663;
        C07663 c076632;
        ChannelIterator channelIterator;
        ChannelIterator it;
        if (continuation instanceof C07663) {
            c07663 = (C07663) continuation;
            if ((c07663.label & Integer.MIN_VALUE) != 0) {
                c07663.label -= Integer.MIN_VALUE;
            } else {
                c07663 = new C07663(continuation);
            }
        }
        Object obj = c07663.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07663.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                it = receiveChannel.iterator();
            } catch (Throwable th) {
                sendChannel = receiveChannel;
                th = th;
                throw th;
            }
        } else {
            try {
                if (i == 1) {
                    ChannelIterator channelIterator2 = (ChannelIterator) c07663.L$2;
                    ReceiveChannel receiveChannel2 = (ReceiveChannel) c07663.L$1;
                    SendChannel sendChannel2 = (SendChannel) c07663.L$0;
                    ResultKt.throwOnFailure(obj);
                    c076632 = c07663;
                    channelIterator = channelIterator2;
                    receiveChannel = receiveChannel2;
                    sendChannel = sendChannel2;
                    C07663 c076633 = c076632;
                    if (!((Boolean) obj).booleanValue()) {
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel, null);
                        return sendChannel;
                    }
                    Object next = channelIterator.next();
                    if (next != null) {
                        c076633.L$0 = sendChannel;
                        c076633.L$1 = receiveChannel;
                        c076633.L$2 = channelIterator;
                        c076633.label = 2;
                        if (sendChannel.send(next, c076633) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        it = channelIterator;
                        c07663 = c076633;
                    }
                } else {
                    if (i != 2) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    ChannelIterator channelIterator3 = (ChannelIterator) c07663.L$2;
                    ReceiveChannel receiveChannel3 = (ReceiveChannel) c07663.L$1;
                    SendChannel sendChannel3 = (SendChannel) c07663.L$0;
                    ResultKt.throwOnFailure(obj);
                    it = channelIterator3;
                    receiveChannel = receiveChannel3;
                    sendChannel = sendChannel3;
                }
            } catch (Throwable th2) {
                th = th2;
                try {
                    throw th;
                } catch (Throwable th3) {
                    ChannelsKt.cancelConsumed(sendChannel, th);
                    throw th3;
                }
            }
        }
        c07663.L$0 = sendChannel;
        c07663.L$1 = receiveChannel;
        c07663.L$2 = it;
        c07663.label = 1;
        Object objHasNext = it.hasNext(c07663);
        if (objHasNext == coroutine_suspended) {
            return coroutine_suspended;
        }
        c076632 = c07663;
        channelIterator = it;
        obj = objHasNext;
        sendChannel = sendChannel;
        C07663 c0766332 = c076632;
        if (!((Boolean) obj).booleanValue()) {
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$take$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {254, 255}, m434m = "invokeSuspend", m435n = {"$this$produce", "remaining", "$this$produce", "remaining"}, m436s = {"L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$take$1 */
    static final class C07821<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {

        /* renamed from: $n */
        final /* synthetic */ int f85$n;
        final /* synthetic */ ReceiveChannel<E> $this_take;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07821(int i, ReceiveChannel<? extends E> receiveChannel, Continuation<? super C07821> continuation) {
            super(2, continuation);
            this.f85$n = i;
            this.$this_take = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07821 c07821 = new C07821(this.f85$n, this.$this_take, continuation);
            c07821.L$0 = obj;
            return c07821;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07821) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:22:0x005f A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0060  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x006b  */
        /* JADX WARN: Removed duplicated region for block: B:31:0x0085  */
        /* JADX WARN: Removed duplicated region for block: B:33:0x0088  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x007e -> B:7:0x001b). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            int i;
            ChannelIterator<E> it;
            ProducerScope producerScope2;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i2 = this.label;
            if (i2 == 0) {
                ResultKt.throwOnFailure(obj);
                producerScope = (ProducerScope) this.L$0;
                i = this.f85$n;
                if (i == 0) {
                    return Unit.INSTANCE;
                }
                if (!(i >= 0)) {
                    throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
                }
                it = this.$this_take.iterator();
                this.L$0 = producerScope;
                this.L$1 = it;
                this.I$0 = i;
                this.label = 1;
                objHasNext = it.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                }
            } else if (i2 == 1) {
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                }
            } else {
                if (i2 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                producerScope = producerScope2;
                i--;
                if (i == 0) {
                    return Unit.INSTANCE;
                }
                this.L$0 = producerScope;
                this.L$1 = it;
                this.I$0 = i;
                this.label = 1;
                objHasNext = it.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                producerScope2 = producerScope;
                obj = objHasNext;
                if (!((Boolean) obj).booleanValue()) {
                    this.L$0 = producerScope2;
                    this.L$1 = it;
                    this.I$0 = i;
                    this.label = 2;
                    if (producerScope2.send(it.next(), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope = producerScope2;
                    i--;
                    if (i == 0) {
                    }
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.I$0 = i;
                    this.label = 1;
                    objHasNext = it.hasNext(this);
                    if (objHasNext == coroutine_suspended) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            }
        }
    }

    public static /* synthetic */ ReceiveChannel take$default(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext, int i2, Object obj) {
        if ((i2 & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return take(receiveChannel, i, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel take(ReceiveChannel receiveChannel, int i, CoroutineContext coroutineContext) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07821(i, receiveChannel, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel takeWhile$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return takeWhile(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\f\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$takeWhile$1", m431f = "Deprecated.kt", m432i = {0, 1, 1, 2}, m433l = {269, VerticalSeekBar.ROTATION_ANGLE_CW_270, 271}, m434m = "invokeSuspend", m435n = {"$this$produce", "$this$produce", "e", "$this$produce"}, m436s = {"L$0", "L$0", "L$2", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$takeWhile$1 */
    static final class C07831<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super Boolean>, Object> $predicate;
        final /* synthetic */ ReceiveChannel<E> $this_takeWhile;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07831(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super Boolean>, ? extends Object> function2, Continuation<? super C07831> continuation) {
            super(2, continuation);
            this.$this_takeWhile = receiveChannel;
            this.$predicate = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07831 c07831 = new C07831(this.$this_takeWhile, this.$predicate, continuation);
            c07831.L$0 = obj;
            return c07831;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07831) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0065  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x0087  */
        /* JADX WARN: Removed duplicated region for block: B:26:0x008a  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00a0  */
        /* JADX WARN: Type inference failed for: r6v0, types: [java.lang.Object, kotlinx.coroutines.channels.ProducerScope] */
        /* JADX WARN: Type inference failed for: r6v3 */
        /* JADX WARN: Type inference failed for: r6v6 */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:29:0x009d -> B:13:0x004d). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ChannelIterator<E> it;
            ProducerScope producerScope;
            ?? r6;
            ChannelIterator<E> channelIterator;
            Object obj2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope2 = (ProducerScope) this.L$0;
                it = this.$this_takeWhile.iterator();
                producerScope = producerScope2;
            } else if (i == 1) {
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    E next = it.next();
                    Function2<E, Continuation<? super Boolean>, Object> function2 = this.$predicate;
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.L$2 = next;
                    this.label = 2;
                    Object objInvoke = function2.invoke(next, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    ChannelIterator<E> channelIterator2 = it;
                    obj2 = next;
                    obj = objInvoke;
                    r6 = producerScope;
                    channelIterator = channelIterator2;
                    if (((Boolean) obj).booleanValue()) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            } else if (i == 2) {
                obj2 = this.L$2;
                channelIterator = (ChannelIterator) this.L$1;
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                r6 = producerScope3;
                if (((Boolean) obj).booleanValue()) {
                    return Unit.INSTANCE;
                }
                this.L$0 = r6;
                this.L$1 = channelIterator;
                this.L$2 = null;
                this.label = 3;
                if (r6.send(obj2, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
                it = channelIterator;
                producerScope = r6;
            } else {
                if (i != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.label = 1;
            obj = it.hasNext(this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel takeWhile(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07831(receiveChannel, function2, null), 6, null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006c A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x007a A[Catch: all -> 0x0055, TryCatch #1 {all -> 0x0055, blocks: (B:13:0x0036, B:28:0x0072, B:30:0x007a, B:33:0x008d, B:18:0x0051), top: B:44:0x0024 }] */
    /* JADX WARN: Removed duplicated region for block: B:33:0x008d A[Catch: all -> 0x0055, TRY_LEAVE, TryCatch #1 {all -> 0x0055, blocks: (B:13:0x0036, B:28:0x0072, B:30:0x007a, B:33:0x008d, B:18:0x0051), top: B:44:0x0024 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r8v0, types: [C extends kotlinx.coroutines.channels.SendChannel<? super E>] */
    /* JADX WARN: Type inference failed for: r8v1 */
    /* JADX WARN: Type inference failed for: r8v2, types: [kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Type inference failed for: r8v25 */
    /* JADX WARN: Type inference failed for: r8v26 */
    /* JADX WARN: Type inference failed for: r8v4 */
    /* JADX WARN: Type inference failed for: r8v5, types: [java.lang.Object, kotlinx.coroutines.channels.ReceiveChannel] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:31:0x008a -> B:14:0x0039). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E, C extends SendChannel<? super E>> Object toChannel(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) throws Throwable {
        C07841 c07841;
        ChannelIterator<? extends E> it;
        ChannelIterator<? extends E> channelIterator;
        SendChannel sendChannel;
        SendChannel sendChannel2;
        Object objHasNext;
        if (continuation instanceof C07841) {
            c07841 = (C07841) continuation;
            if ((c07841.label & Integer.MIN_VALUE) != 0) {
                c07841.label -= Integer.MIN_VALUE;
            } else {
                c07841 = new C07841(continuation);
            }
        }
        Object obj = c07841.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07841.label;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                try {
                    it = receiveChannel.iterator();
                    sendChannel2 = c;
                    c07841.L$0 = sendChannel2;
                    c07841.L$1 = receiveChannel;
                    c07841.L$2 = it;
                    c07841.label = 1;
                    objHasNext = it.hasNext(c07841);
                    if (objHasNext == coroutine_suspended) {
                    }
                } catch (Throwable th) {
                    c = receiveChannel;
                    th = th;
                    try {
                        throw th;
                    } catch (Throwable th2) {
                        ChannelsKt.cancelConsumed(c, th);
                        throw th2;
                    }
                }
            } else if (i == 1) {
                channelIterator = (ChannelIterator) c07841.L$2;
                boolean z = (C) ((ReceiveChannel) c07841.L$1);
                sendChannel = (SendChannel) c07841.L$0;
                ResultKt.throwOnFailure(obj);
                c = z;
                if (!((Boolean) obj).booleanValue()) {
                }
            } else {
                if (i != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                channelIterator = (ChannelIterator) c07841.L$2;
                ReceiveChannel<? extends E> receiveChannel2 = (C) ((ReceiveChannel) c07841.L$1);
                sendChannel = (SendChannel) c07841.L$0;
                ResultKt.throwOnFailure(obj);
                ReceiveChannel<? extends E> receiveChannel3 = receiveChannel2;
                it = channelIterator;
                receiveChannel = receiveChannel3;
                sendChannel2 = (C) sendChannel;
                c07841.L$0 = sendChannel2;
                c07841.L$1 = receiveChannel;
                c07841.L$2 = it;
                c07841.label = 1;
                objHasNext = it.hasNext(c07841);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                SendChannel sendChannel3 = sendChannel2;
                boolean z2 = (C) receiveChannel;
                channelIterator = it;
                obj = objHasNext;
                sendChannel = sendChannel3;
                c = z2;
                if (!((Boolean) obj).booleanValue()) {
                    E next = channelIterator.next();
                    c07841.L$0 = sendChannel;
                    c07841.L$1 = (Object) c;
                    c07841.L$2 = channelIterator;
                    c07841.label = 2;
                    receiveChannel3 = c;
                    if (sendChannel.send(next, c07841) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    it = channelIterator;
                    receiveChannel = receiveChannel3;
                    sendChannel2 = (C) sendChannel;
                    c07841.L$0 = sendChannel2;
                    c07841.L$1 = receiveChannel;
                    c07841.L$2 = it;
                    c07841.label = 1;
                    objHasNext = it.hasNext(c07841);
                    if (objHasNext == coroutine_suspended) {
                    }
                } else {
                    Unit unit = Unit.INSTANCE;
                    ChannelsKt.cancelConsumed((ReceiveChannel) c, null);
                    return sendChannel;
                }
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0066 A[Catch: all -> 0x0037, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x006f), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006f A[Catch: all -> 0x0037, TRY_LEAVE, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x006f), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.Object, java.util.Collection] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x005b -> B:25:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <E, C extends Collection<? super E>> Object toCollection(ReceiveChannel<? extends E> receiveChannel, C c, Continuation<? super C> continuation) throws Throwable {
        C07851 c07851;
        ReceiveChannel<? extends E> receiveChannel2;
        Throwable th;
        ChannelIterator it;
        C c2;
        Object objHasNext;
        if (continuation instanceof C07851) {
            c07851 = (C07851) continuation;
            if ((c07851.label & Integer.MIN_VALUE) != 0) {
                c07851.label -= Integer.MIN_VALUE;
            } else {
                c07851 = new C07851(continuation);
            }
        }
        Object obj = c07851.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07851.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                c2 = c;
                c07851.L$0 = c2;
                c07851.L$1 = receiveChannel2;
                c07851.L$2 = it;
                c07851.label = 1;
                objHasNext = it.hasNext(c07851);
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
            it = (ChannelIterator) c07851.L$2;
            receiveChannel2 = (ReceiveChannel) c07851.L$1;
            Collection collection = (Collection) c07851.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                ?? r2 = collection;
                if (!((Boolean) obj).booleanValue()) {
                    r2.add(it.next());
                    c2 = r2;
                    c07851.L$0 = c2;
                    c07851.L$1 = receiveChannel2;
                    c07851.L$2 = it;
                    c07851.label = 1;
                    objHasNext = it.hasNext(c07851);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    r2 = c2;
                    obj = objHasNext;
                    if (!((Boolean) obj).booleanValue()) {
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, null);
                        return r2;
                    }
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
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x005a A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0066 A[Catch: all -> 0x0037, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x0079), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0079 A[Catch: all -> 0x0037, TRY_LEAVE, TryCatch #1 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x005e, B:27:0x0066, B:21:0x004c, B:28:0x0079), top: B:39:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Type inference failed for: r2v2 */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.lang.Object, java.util.Map] */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x005b -> B:25:0x005e). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final <K, V, M extends Map<? super K, ? super V>> Object toMap(ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel, M m, Continuation<? super M> continuation) throws Throwable {
        C07862 c07862;
        ReceiveChannel<? extends Pair<? extends K, ? extends V>> receiveChannel2;
        Throwable th;
        ChannelIterator it;
        M m2;
        Object objHasNext;
        if (continuation instanceof C07862) {
            c07862 = (C07862) continuation;
            if ((c07862.label & Integer.MIN_VALUE) != 0) {
                c07862.label -= Integer.MIN_VALUE;
            } else {
                c07862 = new C07862(continuation);
            }
        }
        Object obj = c07862.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07862.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                receiveChannel2 = receiveChannel;
                it = receiveChannel.iterator();
                m2 = m;
                c07862.L$0 = m2;
                c07862.L$1 = receiveChannel2;
                c07862.L$2 = it;
                c07862.label = 1;
                objHasNext = it.hasNext(c07862);
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
            it = (ChannelIterator) c07862.L$2;
            receiveChannel2 = (ReceiveChannel) c07862.L$1;
            Map map = (Map) c07862.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                ?? r2 = map;
                if (!((Boolean) obj).booleanValue()) {
                    Pair pair = (Pair) it.next();
                    r2.put(pair.getFirst(), pair.getSecond());
                    m2 = r2;
                    c07862.L$0 = m2;
                    c07862.L$1 = receiveChannel2;
                    c07862.L$2 = it;
                    c07862.label = 1;
                    objHasNext = it.hasNext(c07862);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    r2 = m2;
                    obj = objHasNext;
                    if (!((Boolean) obj).booleanValue()) {
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, null);
                        return r2;
                    }
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
    }

    public static /* synthetic */ ReceiveChannel flatMap$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return flatMap(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$flatMap$1", m431f = "Deprecated.kt", m432i = {0, 1, 2}, m433l = {321, 322, 322}, m434m = "invokeSuspend", m435n = {"$this$produce", "$this$produce", "$this$produce"}, m436s = {"L$0", "L$0", "L$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$flatMap$1 */
    static final class C07691<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_flatMap;
        final /* synthetic */ Function2<E, Continuation<? super ReceiveChannel<? extends R>>, Object> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07691(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super ReceiveChannel<? extends R>>, ? extends Object> function2, Continuation<? super C07691> continuation) {
            super(2, continuation);
            this.$this_flatMap = receiveChannel;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07691 c07691 = new C07691(this.$this_flatMap, this.$transform, continuation);
            c07691.L$0 = obj;
            return c07691;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C07691) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0063  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x008a A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:24:0x008b  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:22:0x0088 -> B:13:0x004b). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ChannelIterator it;
            ProducerScope producerScope;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope2 = (ProducerScope) this.L$0;
                it = this.$this_flatMap.iterator();
                producerScope = producerScope2;
            } else if (i == 1) {
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    Object next = it.next();
                    Function2<E, Continuation<? super ReceiveChannel<? extends R>>, Object> function2 = this.$transform;
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.label = 2;
                    obj = function2.invoke(next, this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    this.L$0 = producerScope;
                    this.L$1 = it;
                    this.label = 3;
                    if (ChannelsKt.toChannel((ReceiveChannel) obj, producerScope, this) == coroutine_suspended) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            } else if (i == 2) {
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                this.L$0 = producerScope;
                this.L$1 = it;
                this.label = 3;
                if (ChannelsKt.toChannel((ReceiveChannel) obj, producerScope, this) == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                it = (ChannelIterator) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.label = 1;
            obj = it.hasNext(this);
            if (obj == coroutine_suspended) {
                return coroutine_suspended;
            }
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel flatMap(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07691(receiveChannel, function2, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel map$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.map(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$map$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 2, 2}, m433l = {487, 333, 333}, m434m = "invokeSuspend", m435n = {"$this$produce", "$this$consume$iv$iv", "$this$produce", "$this$consume$iv$iv", "$this$produce", "$this$consume$iv$iv"}, m436s = {"L$0", "L$2", "L$0", "L$2", "L$0", "L$2"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$map$1 */
    static final class C07741<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_map;
        final /* synthetic */ Function2<E, Continuation<? super R>, Object> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07741(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2, Continuation<? super C07741> continuation) {
            super(2, continuation);
            this.$this_map = receiveChannel;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07741 c07741 = new C07741(this.$this_map, this.$transform, continuation);
            c07741.L$0 = obj;
            return c07741;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C07741) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:28:0x0093 A[Catch: all -> 0x00d1, TRY_LEAVE, TryCatch #0 {all -> 0x00d1, blocks: (B:8:0x0022, B:22:0x0076, B:26:0x008b, B:28:0x0093, B:36:0x00c9, B:18:0x005e, B:21:0x006e), top: B:44:0x000a }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00c2 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00c3  */
        /* JADX WARN: Removed duplicated region for block: B:36:0x00c9 A[Catch: all -> 0x00d1, TRY_ENTER, TRY_LEAVE, TryCatch #0 {all -> 0x00d1, blocks: (B:8:0x0022, B:22:0x0076, B:26:0x008b, B:28:0x0093, B:36:0x00c9, B:18:0x005e, B:21:0x006e), top: B:44:0x000a }] */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x00c3 -> B:22:0x0076). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ReceiveChannel receiveChannel;
            ProducerScope producerScope;
            Throwable th;
            Function2 function2;
            ChannelIterator it;
            ReceiveChannel receiveChannel2;
            Throwable th2;
            ProducerScope producerScope2;
            Function2 function22;
            ChannelIterator channelIterator;
            ProducerScope producerScope3;
            Throwable th3;
            ProducerScope producerScope4;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            try {
                if (i == 0) {
                    ResultKt.throwOnFailure(obj);
                    producerScope = (ProducerScope) this.L$0;
                    receiveChannel = this.$this_map;
                    th = null;
                    function2 = this.$transform;
                    it = receiveChannel.iterator();
                } else if (i == 1) {
                    it = (ChannelIterator) this.L$3;
                    receiveChannel = (ReceiveChannel) this.L$2;
                    function2 = (Function2) this.L$1;
                    producerScope4 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    th3 = null;
                    if (!((Boolean) obj).booleanValue()) {
                        Object next = it.next();
                        this.L$0 = producerScope4;
                        this.L$1 = function2;
                        this.L$2 = receiveChannel;
                        this.L$3 = it;
                        this.L$4 = producerScope4;
                        this.label = 2;
                        obj = function2.invoke(next, this);
                        if (obj == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        th2 = th3;
                        producerScope2 = producerScope4;
                        function22 = function2;
                        receiveChannel2 = receiveChannel;
                        channelIterator = it;
                        producerScope3 = producerScope2;
                        this.L$0 = producerScope2;
                        this.L$1 = function22;
                        this.L$2 = receiveChannel2;
                        this.L$3 = channelIterator;
                        this.L$4 = null;
                        this.label = 3;
                        if (producerScope3.send(obj, this) != coroutine_suspended) {
                        }
                    } else {
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel, th3);
                        return Unit.INSTANCE;
                    }
                } else if (i == 2) {
                    producerScope3 = (ProducerScope) this.L$4;
                    channelIterator = (ChannelIterator) this.L$3;
                    receiveChannel2 = (ReceiveChannel) this.L$2;
                    function22 = (Function2) this.L$1;
                    producerScope2 = (ProducerScope) this.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        th2 = null;
                        this.L$0 = producerScope2;
                        this.L$1 = function22;
                        this.L$2 = receiveChannel2;
                        this.L$3 = channelIterator;
                        this.L$4 = null;
                        this.label = 3;
                        if (producerScope3.send(obj, this) != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        it = channelIterator;
                        receiveChannel = receiveChannel2;
                        function2 = function22;
                        producerScope = producerScope2;
                        th = th2;
                    } catch (Throwable th4) {
                        th = th4;
                        receiveChannel = receiveChannel2;
                        try {
                            throw th;
                        } catch (Throwable th5) {
                            ChannelsKt.cancelConsumed(receiveChannel, th);
                            throw th5;
                        }
                    }
                } else {
                    if (i != 3) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    it = (ChannelIterator) this.L$3;
                    receiveChannel = (ReceiveChannel) this.L$2;
                    function2 = (Function2) this.L$1;
                    ProducerScope producerScope5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    producerScope = producerScope5;
                    th = null;
                }
                this.L$0 = producerScope;
                this.L$1 = function2;
                this.L$2 = receiveChannel;
                this.L$3 = it;
                this.label = 1;
                Object objHasNext = it.hasNext(this);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
                Throwable th6 = th;
                producerScope4 = producerScope;
                obj = objHasNext;
                th3 = th6;
                if (!((Boolean) obj).booleanValue()) {
                }
            } catch (Throwable th7) {
                th = th7;
            }
        }
    }

    public static final <E, R> ReceiveChannel<R> map(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super R>, ? extends Object> function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07741(receiveChannel, function2, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel mapIndexed$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.mapIndexed(receiveChannel, coroutineContext, function3);
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u0004H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "R", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$mapIndexed$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 2, 2}, m433l = {344, 345, 345}, m434m = "invokeSuspend", m435n = {"$this$produce", "index", "$this$produce", "index", "$this$produce", "index"}, m436s = {"L$0", "I$0", "L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$mapIndexed$1 */
    static final class C07751<R> extends SuspendLambda implements Function2<ProducerScope<? super R>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_mapIndexed;
        final /* synthetic */ Function3<Integer, E, Continuation<? super R>, Object> $transform;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07751(ReceiveChannel<? extends E> receiveChannel, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3, Continuation<? super C07751> continuation) {
            super(2, continuation);
            this.$this_mapIndexed = receiveChannel;
            this.$transform = function3;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07751 c07751 = new C07751(this.$this_mapIndexed, this.$transform, continuation);
            c07751.L$0 = obj;
            return c07751;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super R> producerScope, Continuation<? super Unit> continuation) {
            return ((C07751) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:19:0x0076  */
        /* JADX WARN: Removed duplicated region for block: B:25:0x00ab A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:26:0x00ac  */
        /* JADX WARN: Removed duplicated region for block: B:27:0x00af  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:26:0x00ac -> B:13:0x0059). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            ChannelIterator it;
            int i;
            ProducerScope producerScope2;
            ChannelIterator channelIterator;
            ProducerScope producerScope3;
            ProducerScope producerScope4;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i2 = this.label;
            if (i2 == 0) {
                ResultKt.throwOnFailure(obj);
                producerScope = (ProducerScope) this.L$0;
                it = this.$this_mapIndexed.iterator();
                i = 0;
            } else if (i2 == 1) {
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                producerScope4 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    Object next = it.next();
                    Function3<Integer, E, Continuation<? super R>, Object> function3 = this.$transform;
                    int i3 = i + 1;
                    Integer numBoxInt = Boxing.boxInt(i);
                    this.L$0 = producerScope4;
                    this.L$1 = it;
                    this.L$2 = producerScope4;
                    this.I$0 = i3;
                    this.label = 2;
                    obj = function3.invoke(numBoxInt, next, this);
                    if (obj == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    producerScope2 = producerScope4;
                    i = i3;
                    channelIterator = it;
                    producerScope3 = producerScope2;
                    this.L$0 = producerScope2;
                    this.L$1 = channelIterator;
                    this.L$2 = null;
                    this.I$0 = i;
                    this.label = 3;
                    if (producerScope3.send(obj, this) != coroutine_suspended) {
                    }
                } else {
                    return Unit.INSTANCE;
                }
            } else if (i2 == 2) {
                i = this.I$0;
                producerScope3 = (ProducerScope) this.L$2;
                channelIterator = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                this.L$0 = producerScope2;
                this.L$1 = channelIterator;
                this.L$2 = null;
                this.I$0 = i;
                this.label = 3;
                if (producerScope3.send(obj, this) != coroutine_suspended) {
                    return coroutine_suspended;
                }
                it = channelIterator;
                producerScope = producerScope2;
            } else {
                if (i2 != 3) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                i = this.I$0;
                it = (ChannelIterator) this.L$1;
                ProducerScope producerScope5 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                producerScope = producerScope5;
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.I$0 = i;
            this.label = 1;
            Object objHasNext = it.hasNext(this);
            if (objHasNext == coroutine_suspended) {
                return coroutine_suspended;
            }
            producerScope4 = producerScope;
            obj = objHasNext;
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    public static final <E, R> ReceiveChannel<R> mapIndexed(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function3<? super Integer, ? super E, ? super Continuation<? super R>, ? extends Object> function3) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07751(receiveChannel, function3, null), 6, null);
    }

    public static /* synthetic */ ReceiveChannel mapIndexedNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return mapIndexedNotNull(receiveChannel, coroutineContext, function3);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel mapIndexedNotNull(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function3 function3) {
        return ChannelsKt.filterNotNull(ChannelsKt.mapIndexed(receiveChannel, coroutineContext, function3));
    }

    public static /* synthetic */ ReceiveChannel mapNotNull$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return mapNotNull(receiveChannel, coroutineContext, function2);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel mapNotNull(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2) {
        return ChannelsKt.filterNotNull(ChannelsKt.map(receiveChannel, coroutineContext, function2));
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00040\u0003H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "Lkotlinx/coroutines/channels/ProducerScope;", "Lkotlin/collections/IndexedValue;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$withIndex$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1}, m433l = {370, 371}, m434m = "invokeSuspend", m435n = {"$this$produce", "index", "$this$produce", "index"}, m436s = {"L$0", "I$0", "L$0", "I$0"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$withIndex$1 */
    static final class C07871<E> extends SuspendLambda implements Function2<ProducerScope<? super IndexedValue<? extends E>>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_withIndex;
        int I$0;
        private /* synthetic */ Object L$0;
        Object L$1;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07871(ReceiveChannel<? extends E> receiveChannel, Continuation<? super C07871> continuation) {
            super(2, continuation);
            this.$this_withIndex = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07871 c07871 = new C07871(this.$this_withIndex, continuation);
            c07871.L$0 = obj;
            return c07871;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super IndexedValue<? extends E>> producerScope, Continuation<? super Unit> continuation) {
            return ((C07871) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Removed duplicated region for block: B:17:0x0064  */
        /* JADX WARN: Removed duplicated region for block: B:21:0x0085  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:20:0x0081 -> B:11:0x0044). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            ChannelIterator<E> it;
            int i;
            ProducerScope producerScope2;
            ChannelIterator<E> channelIterator;
            int i2;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i3 = this.label;
            if (i3 == 0) {
                ResultKt.throwOnFailure(obj);
                producerScope = (ProducerScope) this.L$0;
                it = this.$this_withIndex.iterator();
                i = 0;
            } else if (i3 == 1) {
                i2 = this.I$0;
                channelIterator = (ChannelIterator) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    int i4 = i2 + 1;
                    this.L$0 = producerScope2;
                    this.L$1 = channelIterator;
                    this.I$0 = i4;
                    this.label = 2;
                    if (producerScope2.send(new IndexedValue(i2, channelIterator.next()), this) == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    it = channelIterator;
                    producerScope = producerScope2;
                    i = i4;
                } else {
                    return Unit.INSTANCE;
                }
            } else {
                if (i3 != 2) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                int i5 = this.I$0;
                ChannelIterator<E> channelIterator2 = (ChannelIterator) this.L$1;
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                producerScope = producerScope3;
                i = i5;
                it = channelIterator2;
            }
            this.L$0 = producerScope;
            this.L$1 = it;
            this.I$0 = i;
            this.label = 1;
            Object objHasNext = it.hasNext(this);
            if (objHasNext == coroutine_suspended) {
                return coroutine_suspended;
            }
            producerScope2 = producerScope;
            obj = objHasNext;
            int i6 = i;
            channelIterator = it;
            i2 = i6;
            if (!((Boolean) obj).booleanValue()) {
            }
        }
    }

    public static /* synthetic */ ReceiveChannel withIndex$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return withIndex(receiveChannel, coroutineContext);
    }

    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    public static final /* synthetic */ ReceiveChannel withIndex(ReceiveChannel receiveChannel, CoroutineContext coroutineContext) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07871(receiveChannel, null), 6, null);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u0004\n\u0002\b\u0003\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u0006\u0010\u0002\u001a\u0002H\u0001H\u008a@"}, m422d2 = {"<anonymous>", "E", "it"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinct$1", m431f = "Deprecated.kt", m432i = {}, m433l = {}, m434m = "invokeSuspend", m435n = {}, m436s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinct$1 */
    static final class C07551<E> extends SuspendLambda implements Function2<E, Continuation<? super E>, Object> {
        /* synthetic */ Object L$0;
        int label;

        C07551(Continuation<? super C07551> continuation) {
            super(2, continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07551 c07551 = new C07551(continuation);
            c07551.L$0 = obj;
            return c07551;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
            return invoke((C07551<E>) obj, (Continuation<? super C07551<E>>) obj2);
        }

        public final Object invoke(E e, Continuation<? super E> continuation) {
            return ((C07551) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            return this.L$0;
        }
    }

    public static /* synthetic */ ReceiveChannel distinctBy$default(ReceiveChannel receiveChannel, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 1) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.distinctBy(receiveChannel, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00020\u0004H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "K", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinctBy$1", m431f = "Deprecated.kt", m432i = {0, 0, 1, 1, 1, 2, 2, 2}, m433l = {387, 388, 390}, m434m = "invokeSuspend", m435n = {"$this$produce", "keys", "$this$produce", "keys", "e", "$this$produce", "keys", "k"}, m436s = {"L$0", "L$1", "L$0", "L$1", "L$3", "L$0", "L$1", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$distinctBy$1 */
    static final class C07561<E> extends SuspendLambda implements Function2<ProducerScope<? super E>, Continuation<? super Unit>, Object> {
        final /* synthetic */ Function2<E, Continuation<? super K>, Object> $selector;
        final /* synthetic */ ReceiveChannel<E> $this_distinctBy;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07561(ReceiveChannel<? extends E> receiveChannel, Function2<? super E, ? super Continuation<? super K>, ? extends Object> function2, Continuation<? super C07561> continuation) {
            super(2, continuation);
            this.$this_distinctBy = receiveChannel;
            this.$selector = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07561 c07561 = new C07561(this.$this_distinctBy, this.$selector, continuation);
            c07561.L$0 = obj;
            return c07561;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super E> producerScope, Continuation<? super Unit> continuation) {
            return ((C07561) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:15:0x007b A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:18:0x0084  */
        /* JADX WARN: Removed duplicated region for block: B:24:0x00a6  */
        /* JADX WARN: Removed duplicated region for block: B:30:0x00c6  */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:23:0x00a4 -> B:29:0x00c3). Please report as a decompilation issue!!! */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:27:0x00ba -> B:28:0x00bc). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ProducerScope producerScope;
            HashSet hashSet;
            ChannelIterator<E> it;
            ProducerScope producerScope2;
            HashSet hashSet2;
            E e;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                ProducerScope producerScope3 = (ProducerScope) this.L$0;
                HashSet hashSet3 = new HashSet();
                producerScope = producerScope3;
                hashSet = hashSet3;
                it = this.$this_distinctBy.iterator();
                this.L$0 = producerScope;
                this.L$1 = hashSet;
                this.L$2 = it;
                this.L$3 = null;
                this.label = 1;
                obj = it.hasNext(this);
                if (obj == coroutine_suspended) {
                }
                if (((Boolean) obj).booleanValue()) {
                }
            } else if (i == 1) {
                it = (ChannelIterator) this.L$2;
                hashSet = (HashSet) this.L$1;
                producerScope = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                if (((Boolean) obj).booleanValue()) {
                }
            } else if (i == 2) {
                Object obj2 = this.L$3;
                ChannelIterator<E> channelIterator = (ChannelIterator) this.L$2;
                hashSet2 = (HashSet) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                e = obj2;
                it = channelIterator;
                if (!hashSet2.contains(obj)) {
                }
                hashSet = hashSet2;
                producerScope = producerScope2;
                this.L$0 = producerScope;
                this.L$1 = hashSet;
                this.L$2 = it;
                this.L$3 = null;
                this.label = 1;
                obj = it.hasNext(this);
                if (obj == coroutine_suspended) {
                }
                if (((Boolean) obj).booleanValue()) {
                }
            } else if (i == 3) {
                Object obj3 = this.L$3;
                ChannelIterator<E> channelIterator2 = (ChannelIterator) this.L$2;
                hashSet2 = (HashSet) this.L$1;
                producerScope2 = (ProducerScope) this.L$0;
                ResultKt.throwOnFailure(obj);
                hashSet2.add(obj3);
                it = channelIterator2;
                hashSet = hashSet2;
                producerScope = producerScope2;
                this.L$0 = producerScope;
                this.L$1 = hashSet;
                this.L$2 = it;
                this.L$3 = null;
                this.label = 1;
                obj = it.hasNext(this);
                if (obj == coroutine_suspended) {
                    return coroutine_suspended;
                }
                if (((Boolean) obj).booleanValue()) {
                    E next = it.next();
                    Function2<E, Continuation<? super K>, Object> function2 = this.$selector;
                    this.L$0 = producerScope;
                    this.L$1 = hashSet;
                    this.L$2 = it;
                    this.L$3 = next;
                    this.label = 2;
                    Object objInvoke = function2.invoke(next, this);
                    if (objInvoke == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    HashSet hashSet4 = hashSet;
                    e = next;
                    obj = objInvoke;
                    producerScope2 = producerScope;
                    hashSet2 = hashSet4;
                    if (!hashSet2.contains(obj)) {
                        this.L$0 = producerScope2;
                        this.L$1 = hashSet2;
                        this.L$2 = it;
                        this.L$3 = obj;
                        this.label = 3;
                        if (producerScope2.send(e, this) == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        channelIterator2 = it;
                        obj3 = obj;
                        hashSet2.add(obj3);
                        it = channelIterator2;
                    }
                    hashSet = hashSet2;
                    producerScope = producerScope2;
                    this.L$0 = producerScope;
                    this.L$1 = hashSet;
                    this.L$2 = it;
                    this.L$3 = null;
                    this.label = 1;
                    obj = it.hasNext(this);
                    if (obj == coroutine_suspended) {
                    }
                    if (((Boolean) obj).booleanValue()) {
                        return Unit.INSTANCE;
                    }
                }
            } else {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    public static final <E, K> ReceiveChannel<E> distinctBy(ReceiveChannel<? extends E> receiveChannel, CoroutineContext coroutineContext, Function2<? super E, ? super Continuation<? super K>, ? extends Object> function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumes(receiveChannel), new C07561(receiveChannel, function2, null), 6, null);
    }

    public static final <E> Object toMutableSet(ReceiveChannel<? extends E> receiveChannel, Continuation<? super Set<E>> continuation) {
        return ChannelsKt.toCollection(receiveChannel, new LinkedHashSet(), continuation);
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object any(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07511 c07511;
        if (continuation instanceof C07511) {
            c07511 = (C07511) continuation;
            if ((c07511.label & Integer.MIN_VALUE) != 0) {
                c07511.label -= Integer.MIN_VALUE;
            } else {
                c07511 = new C07511(continuation);
            }
        }
        Object objHasNext = c07511.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07511.label;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(objHasNext);
                ChannelIterator it = receiveChannel.iterator();
                c07511.L$0 = receiveChannel;
                c07511.label = 1;
                objHasNext = it.hasNext(c07511);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                receiveChannel = (ReceiveChannel) c07511.L$0;
                ResultKt.throwOnFailure(objHasNext);
            }
            ChannelsKt.cancelConsumed(receiveChannel, null);
            return objHasNext;
        } finally {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x005e A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x006a A[Catch: all -> 0x0037, TryCatch #3 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x0062, B:27:0x006a, B:28:0x0074), top: B:45:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0074 A[Catch: all -> 0x0037, TRY_LEAVE, TryCatch #3 {all -> 0x0037, blocks: (B:12:0x0033, B:25:0x0062, B:27:0x006a, B:28:0x0074), top: B:45:0x0033 }] */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:24:0x005f -> B:25:0x0062). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object count(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07541 c07541;
        ReceiveChannel receiveChannel2;
        Throwable th;
        Ref.IntRef intRef;
        ReceiveChannel receiveChannel3;
        ChannelIterator it;
        Object objHasNext;
        if (continuation instanceof C07541) {
            c07541 = (C07541) continuation;
            if ((c07541.label & Integer.MIN_VALUE) != 0) {
                c07541.label -= Integer.MIN_VALUE;
            } else {
                c07541 = new C07541(continuation);
            }
        }
        Object obj = c07541.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07541.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                intRef = new Ref.IntRef();
                receiveChannel3 = receiveChannel;
                it = receiveChannel.iterator();
                c07541.L$0 = intRef;
                c07541.L$1 = receiveChannel3;
                c07541.L$2 = it;
                c07541.label = 1;
                objHasNext = it.hasNext(c07541);
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
            it = (ChannelIterator) c07541.L$2;
            receiveChannel2 = (ReceiveChannel) c07541.L$1;
            intRef = (Ref.IntRef) c07541.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                if (!((Boolean) obj).booleanValue()) {
                    it.next();
                    intRef.element++;
                    receiveChannel3 = receiveChannel2;
                    try {
                        c07541.L$0 = intRef;
                        c07541.L$1 = receiveChannel3;
                        c07541.L$2 = it;
                        c07541.label = 1;
                        objHasNext = it.hasNext(c07541);
                        if (objHasNext != coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        receiveChannel2 = receiveChannel3;
                        obj = objHasNext;
                        if (!((Boolean) obj).booleanValue()) {
                            Unit unit = Unit.INSTANCE;
                            ChannelsKt.cancelConsumed(receiveChannel2, null);
                            return Boxing.boxInt(intRef.element);
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel2 = receiveChannel3;
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
                throw th;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x009f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ac A[Catch: all -> 0x00be, TRY_LEAVE, TryCatch #3 {all -> 0x00be, blocks: (B:40:0x00a4, B:42:0x00ac, B:36:0x008f, B:26:0x0063), top: B:61:0x0063 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x00a0 -> B:15:0x003e). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object maxWith(ReceiveChannel receiveChannel, Comparator comparator, Continuation continuation) throws Throwable {
        C07761 c07761;
        ReceiveChannel receiveChannel2;
        ChannelIterator channelIterator;
        Comparator comparator2;
        Object next;
        Comparator comparator3;
        ChannelIterator channelIterator2;
        Object objHasNext;
        if (continuation instanceof C07761) {
            c07761 = (C07761) continuation;
            if ((c07761.label & Integer.MIN_VALUE) != 0) {
                c07761.label -= Integer.MIN_VALUE;
            } else {
                c07761 = new C07761(continuation);
            }
        }
        Object obj = c07761.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07761.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07761.L$0 = comparator;
                c07761.L$1 = receiveChannel;
                c07761.L$2 = it;
                c07761.label = 1;
                Object objHasNext2 = it.hasNext(c07761);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj = objHasNext2;
                comparator2 = comparator;
            } catch (Throwable th) {
                receiveChannel2 = receiveChannel;
                th = th;
                throw th;
            }
        } else if (i == 1) {
            channelIterator = (ChannelIterator) c07761.L$2;
            receiveChannel2 = (ReceiveChannel) c07761.L$1;
            comparator2 = (Comparator) c07761.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        } else {
            if (i != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Object obj2 = c07761.L$3;
            channelIterator2 = (ChannelIterator) c07761.L$2;
            ReceiveChannel receiveChannel3 = (ReceiveChannel) c07761.L$1;
            comparator3 = (Comparator) c07761.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                C07761 c077612 = c07761;
                Object obj3 = obj2;
                receiveChannel = receiveChannel3;
                C07761 c077613 = c077612;
                if (!((Boolean) obj).booleanValue()) {
                    next = channelIterator2.next();
                    if (comparator3.compare(obj3, next) >= 0) {
                        next = obj3;
                    }
                    c07761 = c077613;
                    c07761.L$0 = comparator3;
                    c07761.L$1 = receiveChannel;
                    c07761.L$2 = channelIterator2;
                    c07761.L$3 = next;
                    c07761.label = 2;
                    objHasNext = channelIterator2.hasNext(c07761);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c077612 = c07761;
                    obj3 = next;
                    obj = objHasNext;
                    C07761 c0776132 = c077612;
                    if (!((Boolean) obj).booleanValue()) {
                        ChannelsKt.cancelConsumed(receiveChannel, null);
                        return obj3;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                receiveChannel2 = receiveChannel3;
                try {
                    throw th;
                } catch (Throwable th4) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    throw th4;
                }
            }
        }
        if (!((Boolean) obj).booleanValue()) {
            ChannelsKt.cancelConsumed(receiveChannel2, null);
            return null;
        }
        next = channelIterator.next();
        comparator3 = comparator2;
        ReceiveChannel receiveChannel4 = receiveChannel2;
        channelIterator2 = channelIterator;
        receiveChannel = receiveChannel4;
        c07761.L$0 = comparator3;
        c07761.L$1 = receiveChannel;
        c07761.L$2 = channelIterator2;
        c07761.L$3 = next;
        c07761.label = 2;
        objHasNext = channelIterator2.hasNext(c07761);
        if (objHasNext != coroutine_suspended) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:38:0x009f A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00a0  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ac A[Catch: all -> 0x00be, TRY_LEAVE, TryCatch #3 {all -> 0x00be, blocks: (B:40:0x00a4, B:42:0x00ac, B:36:0x008f, B:26:0x0063), top: B:61:0x0063 }] */
    /* JADX WARN: Removed duplicated region for block: B:47:0x00ba  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:39:0x00a0 -> B:15:0x003e). Please report as a decompilation issue!!! */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object minWith(ReceiveChannel receiveChannel, Comparator comparator, Continuation continuation) throws Throwable {
        C07771 c07771;
        ReceiveChannel receiveChannel2;
        ChannelIterator channelIterator;
        Comparator comparator2;
        Object next;
        Comparator comparator3;
        ChannelIterator channelIterator2;
        Object objHasNext;
        if (continuation instanceof C07771) {
            c07771 = (C07771) continuation;
            if ((c07771.label & Integer.MIN_VALUE) != 0) {
                c07771.label -= Integer.MIN_VALUE;
            } else {
                c07771 = new C07771(continuation);
            }
        }
        Object obj = c07771.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07771.label;
        if (i == 0) {
            ResultKt.throwOnFailure(obj);
            try {
                ChannelIterator it = receiveChannel.iterator();
                c07771.L$0 = comparator;
                c07771.L$1 = receiveChannel;
                c07771.L$2 = it;
                c07771.label = 1;
                Object objHasNext2 = it.hasNext(c07771);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                receiveChannel2 = receiveChannel;
                channelIterator = it;
                obj = objHasNext2;
                comparator2 = comparator;
            } catch (Throwable th) {
                receiveChannel2 = receiveChannel;
                th = th;
                throw th;
            }
        } else if (i == 1) {
            channelIterator = (ChannelIterator) c07771.L$2;
            receiveChannel2 = (ReceiveChannel) c07771.L$1;
            comparator2 = (Comparator) c07771.L$0;
            try {
                ResultKt.throwOnFailure(obj);
            } catch (Throwable th2) {
                th = th2;
                throw th;
            }
        } else {
            if (i != 2) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            Object obj2 = c07771.L$3;
            channelIterator2 = (ChannelIterator) c07771.L$2;
            ReceiveChannel receiveChannel3 = (ReceiveChannel) c07771.L$1;
            comparator3 = (Comparator) c07771.L$0;
            try {
                ResultKt.throwOnFailure(obj);
                C07771 c077712 = c07771;
                Object obj3 = obj2;
                receiveChannel = receiveChannel3;
                C07771 c077713 = c077712;
                if (!((Boolean) obj).booleanValue()) {
                    next = channelIterator2.next();
                    if (comparator3.compare(obj3, next) <= 0) {
                        next = obj3;
                    }
                    c07771 = c077713;
                    c07771.L$0 = comparator3;
                    c07771.L$1 = receiveChannel;
                    c07771.L$2 = channelIterator2;
                    c07771.L$3 = next;
                    c07771.label = 2;
                    objHasNext = channelIterator2.hasNext(c07771);
                    if (objHasNext != coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    c077712 = c07771;
                    obj3 = next;
                    obj = objHasNext;
                    C07771 c0777132 = c077712;
                    if (!((Boolean) obj).booleanValue()) {
                        ChannelsKt.cancelConsumed(receiveChannel, null);
                        return obj3;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                receiveChannel2 = receiveChannel3;
                try {
                    throw th;
                } catch (Throwable th4) {
                    ChannelsKt.cancelConsumed(receiveChannel2, th);
                    throw th4;
                }
            }
        }
        if (!((Boolean) obj).booleanValue()) {
            ChannelsKt.cancelConsumed(receiveChannel2, null);
            return null;
        }
        next = channelIterator.next();
        comparator3 = comparator2;
        ReceiveChannel receiveChannel4 = receiveChannel2;
        channelIterator2 = channelIterator;
        receiveChannel = receiveChannel4;
        c07771.L$0 = comparator3;
        c07771.L$1 = receiveChannel;
        c07771.L$2 = channelIterator2;
        c07771.L$3 = next;
        c07771.label = 2;
        objHasNext = channelIterator2.hasNext(c07771);
        if (objHasNext != coroutine_suspended) {
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    @Deprecated(level = DeprecationLevel.HIDDEN, message = "Binary compatibility")
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static final /* synthetic */ Object none(ReceiveChannel receiveChannel, Continuation continuation) throws Throwable {
        C07781 c07781;
        if (continuation instanceof C07781) {
            c07781 = (C07781) continuation;
            if ((c07781.label & Integer.MIN_VALUE) != 0) {
                c07781.label -= Integer.MIN_VALUE;
            } else {
                c07781 = new C07781(continuation);
            }
        }
        Object objHasNext = c07781.result;
        Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
        int i = c07781.label;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(objHasNext);
                ChannelIterator it = receiveChannel.iterator();
                c07781.L$0 = receiveChannel;
                c07781.label = 1;
                objHasNext = it.hasNext(c07781);
                if (objHasNext == coroutine_suspended) {
                    return coroutine_suspended;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                receiveChannel = (ReceiveChannel) c07781.L$0;
                ResultKt.throwOnFailure(objHasNext);
            }
            Boolean boolBoxBoolean = Boxing.boxBoolean(!((Boolean) objHasNext).booleanValue());
            ChannelsKt.cancelConsumed(receiveChannel, null);
            return boolBoxBoolean;
        } finally {
        }
    }

    /* JADX INFO: Add missing generic type declarations: [E] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\n\n\u0002\b\u0002\n\u0002\u0010\u0000\n\u0000\u0010\u0000\u001a\u0002H\u0001\"\b\b\u0000\u0010\u0001*\u00020\u00022\b\u0010\u0003\u001a\u0004\u0018\u0001H\u0001H\u008a@"}, m422d2 = {"<anonymous>", "E", "", "it"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$requireNoNulls$1", m431f = "Deprecated.kt", m432i = {}, m433l = {}, m434m = "invokeSuspend", m435n = {}, m436s = {})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$requireNoNulls$1 */
    static final class C07791<E> extends SuspendLambda implements Function2<E, Continuation<? super E>, Object> {
        final /* synthetic */ ReceiveChannel<E> $this_requireNoNulls;
        /* synthetic */ Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07791(ReceiveChannel<? extends E> receiveChannel, Continuation<? super C07791> continuation) {
            super(2, continuation);
            this.$this_requireNoNulls = receiveChannel;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07791 c07791 = new C07791(this.$this_requireNoNulls, continuation);
            c07791.L$0 = obj;
            return c07791;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // kotlin.jvm.functions.Function2
        public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
            return invoke((C07791<E>) obj, (Continuation<? super C07791<E>>) obj2);
        }

        public final Object invoke(E e, Continuation<? super E> continuation) {
            return ((C07791) create(e, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) throws Throwable {
            IntrinsicsKt.getCOROUTINE_SUSPENDED();
            if (this.label != 0) {
                throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
            ResultKt.throwOnFailure(obj);
            Object obj2 = this.L$0;
            if (obj2 != null) {
                return obj2;
            }
            throw new IllegalArgumentException("null element found in " + this.$this_requireNoNulls + '.');
        }
    }

    public static /* synthetic */ ReceiveChannel zip$default(ReceiveChannel receiveChannel, ReceiveChannel receiveChannel2, CoroutineContext coroutineContext, Function2 function2, int i, Object obj) {
        if ((i & 2) != 0) {
            coroutineContext = Dispatchers.getUnconfined();
        }
        return ChannelsKt.zip(receiveChannel, receiveChannel2, coroutineContext, function2);
    }

    /* JADX INFO: Add missing generic type declarations: [V] */
    /* compiled from: Deprecated.kt */
    @Metadata(m421d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0003\"\u0004\b\u0002\u0010\u0004*\b\u0012\u0004\u0012\u0002H\u00040\u0005H\u008a@"}, m422d2 = {"<anonymous>", "", "E", "R", "V", "Lkotlinx/coroutines/channels/ProducerScope;"}, m423k = 3, m424mv = {1, 6, 0}, m426xi = 48)
    @DebugMetadata(m430c = "kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$zip$2", m431f = "Deprecated.kt", m432i = {0, 0, 0, 1, 1, 1, 1, 2, 2, 2}, m433l = {487, 469, 471}, m434m = "invokeSuspend", m435n = {"$this$produce", "otherIterator", "$this$consume$iv$iv", "$this$produce", "otherIterator", "$this$consume$iv$iv", "element1", "$this$produce", "otherIterator", "$this$consume$iv$iv"}, m436s = {"L$0", "L$1", "L$3", "L$0", "L$1", "L$3", "L$5", "L$0", "L$1", "L$3"})
    /* renamed from: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt$zip$2 */
    static final class C07892<V> extends SuspendLambda implements Function2<ProducerScope<? super V>, Continuation<? super Unit>, Object> {
        final /* synthetic */ ReceiveChannel<R> $other;
        final /* synthetic */ ReceiveChannel<E> $this_zip;
        final /* synthetic */ Function2<E, R, V> $transform;
        private /* synthetic */ Object L$0;
        Object L$1;
        Object L$2;
        Object L$3;
        Object L$4;
        Object L$5;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        C07892(ReceiveChannel<? extends R> receiveChannel, ReceiveChannel<? extends E> receiveChannel2, Function2<? super E, ? super R, ? extends V> function2, Continuation<? super C07892> continuation) {
            super(2, continuation);
            this.$other = receiveChannel;
            this.$this_zip = receiveChannel2;
            this.$transform = function2;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            C07892 c07892 = new C07892(this.$other, this.$this_zip, this.$transform, continuation);
            c07892.L$0 = obj;
            return c07892;
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(ProducerScope<? super V> producerScope, Continuation<? super Unit> continuation) {
            return ((C07892) create(producerScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Code restructure failed: missing block: B:56:0x0091, code lost:
        
            r14 = r1;
            r1 = r6;
            r6 = r8;
            r7 = r9;
            r8 = r10;
            r9 = r11;
         */
        /* JADX WARN: Removed duplicated region for block: B:28:0x00b6 A[Catch: all -> 0x0055, TRY_LEAVE, TryCatch #1 {all -> 0x0055, blocks: (B:26:0x00ae, B:28:0x00b6, B:40:0x0105, B:13:0x004a), top: B:50:0x004a }] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x00de  */
        /* JADX WARN: Removed duplicated region for block: B:35:0x00e5 A[Catch: all -> 0x0102, TRY_LEAVE, TryCatch #0 {all -> 0x0102, blocks: (B:32:0x00d6, B:35:0x00e5), top: B:48:0x00d6 }] */
        /* JADX WARN: Removed duplicated region for block: B:40:0x0105 A[Catch: all -> 0x0055, TRY_ENTER, TRY_LEAVE, TryCatch #1 {all -> 0x0055, blocks: (B:26:0x00ae, B:28:0x00b6, B:40:0x0105, B:13:0x004a), top: B:50:0x004a }] */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object obj) throws Throwable {
            ReceiveChannel receiveChannel;
            Function2 function2;
            ProducerScope producerScope;
            Throwable th;
            ChannelIterator channelIterator;
            ChannelIterator it;
            ReceiveChannel receiveChannel2;
            ProducerScope producerScope2;
            ChannelIterator channelIterator2;
            Function2 function22;
            ReceiveChannel receiveChannel3;
            Object obj2;
            ChannelIterator channelIterator3;
            Throwable th2;
            ProducerScope producerScope3;
            ChannelIterator channelIterator4;
            Function2 function23;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            int i = this.label;
            try {
                if (i == 0) {
                    ResultKt.throwOnFailure(obj);
                    ProducerScope producerScope4 = (ProducerScope) this.L$0;
                    ChannelIterator it2 = this.$other.iterator();
                    receiveChannel = this.$this_zip;
                    function2 = this.$transform;
                    producerScope = producerScope4;
                    th = null;
                    channelIterator = it2;
                    it = receiveChannel.iterator();
                } else if (i == 1) {
                    ChannelIterator channelIterator5 = (ChannelIterator) this.L$4;
                    ReceiveChannel receiveChannel4 = (ReceiveChannel) this.L$3;
                    Function2 function24 = (Function2) this.L$2;
                    ChannelIterator channelIterator6 = (ChannelIterator) this.L$1;
                    ProducerScope producerScope5 = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    producerScope3 = producerScope5;
                    channelIterator4 = channelIterator6;
                    function23 = function24;
                    receiveChannel2 = receiveChannel4;
                    channelIterator3 = channelIterator5;
                    th2 = null;
                    if (!((Boolean) obj).booleanValue()) {
                        Object next = channelIterator3.next();
                        this.L$0 = producerScope3;
                        this.L$1 = channelIterator4;
                        this.L$2 = function23;
                        this.L$3 = receiveChannel2;
                        this.L$4 = channelIterator3;
                        this.L$5 = next;
                        this.label = 2;
                        Object objHasNext = channelIterator4.hasNext(this);
                        if (objHasNext == coroutine_suspended) {
                            return coroutine_suspended;
                        }
                        ReceiveChannel receiveChannel5 = receiveChannel2;
                        obj2 = next;
                        obj = objHasNext;
                        producerScope2 = producerScope3;
                        channelIterator2 = channelIterator4;
                        function22 = function23;
                        receiveChannel3 = receiveChannel5;
                        if (((Boolean) obj).booleanValue()) {
                        }
                    } else {
                        Unit unit = Unit.INSTANCE;
                        ChannelsKt.cancelConsumed(receiveChannel2, th2);
                        return Unit.INSTANCE;
                    }
                } else if (i == 2) {
                    Object obj3 = this.L$5;
                    channelIterator3 = (ChannelIterator) this.L$4;
                    receiveChannel2 = (ReceiveChannel) this.L$3;
                    Function2 function25 = (Function2) this.L$2;
                    ChannelIterator channelIterator7 = (ChannelIterator) this.L$1;
                    ProducerScope producerScope6 = (ProducerScope) this.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        producerScope2 = producerScope6;
                        channelIterator2 = channelIterator7;
                        function22 = function25;
                        receiveChannel3 = receiveChannel2;
                        obj2 = obj3;
                        th2 = null;
                    } catch (Throwable th3) {
                        th = th3;
                        receiveChannel = receiveChannel2;
                        try {
                            throw th;
                        } catch (Throwable th4) {
                            ChannelsKt.cancelConsumed(receiveChannel, th);
                            throw th4;
                        }
                    }
                    try {
                        if (((Boolean) obj).booleanValue()) {
                            th = th2;
                            it = channelIterator3;
                            receiveChannel = receiveChannel3;
                            function2 = function22;
                            channelIterator = channelIterator2;
                            producerScope = producerScope2;
                        } else {
                            Object objInvoke = function22.invoke(obj2, channelIterator2.next());
                            this.L$0 = producerScope2;
                            this.L$1 = channelIterator2;
                            this.L$2 = function22;
                            this.L$3 = receiveChannel3;
                            this.L$4 = channelIterator3;
                            this.L$5 = null;
                            this.label = 3;
                            if (producerScope2.send(objInvoke, this) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            th = th2;
                            it = channelIterator3;
                            receiveChannel = receiveChannel3;
                            function2 = function22;
                            channelIterator = channelIterator2;
                            producerScope = producerScope2;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        receiveChannel = receiveChannel3;
                        throw th;
                    }
                } else {
                    if (i != 3) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    it = (ChannelIterator) this.L$4;
                    receiveChannel = (ReceiveChannel) this.L$3;
                    function2 = (Function2) this.L$2;
                    channelIterator = (ChannelIterator) this.L$1;
                    producerScope = (ProducerScope) this.L$0;
                    ResultKt.throwOnFailure(obj);
                    th = null;
                }
                this.L$0 = producerScope;
                this.L$1 = channelIterator;
                this.L$2 = function2;
                this.L$3 = receiveChannel;
                this.L$4 = it;
                this.L$5 = null;
                this.label = 1;
                Object objHasNext2 = it.hasNext(this);
                if (objHasNext2 == coroutine_suspended) {
                    return coroutine_suspended;
                }
                ChannelIterator channelIterator8 = it;
                th2 = th;
                obj = objHasNext2;
                producerScope3 = producerScope;
                channelIterator4 = channelIterator;
                function23 = function2;
                receiveChannel2 = receiveChannel;
                channelIterator3 = channelIterator8;
                if (!((Boolean) obj).booleanValue()) {
                }
            } catch (Throwable th6) {
                th = th6;
            }
        }
    }

    public static final <E, R, V> ReceiveChannel<V> zip(ReceiveChannel<? extends E> receiveChannel, ReceiveChannel<? extends R> receiveChannel2, CoroutineContext coroutineContext, Function2<? super E, ? super R, ? extends V> function2) {
        return ProduceKt.produce$default(GlobalScope.INSTANCE, coroutineContext, 0, null, ChannelsKt.consumesAll(receiveChannel, receiveChannel2), new C07892(receiveChannel2, receiveChannel, function2, null), 6, null);
    }

    public static final Function1<Throwable, Unit> consumes(final ReceiveChannel<?> receiveChannel) {
        return new Function1<Throwable, Unit>() { // from class: kotlinx.coroutines.channels.ChannelsKt__DeprecatedKt.consumes.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Throwable th) {
                invoke2(th);
                return Unit.INSTANCE;
            }

            /* renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Throwable th) {
                ChannelsKt.cancelConsumed(receiveChannel, th);
            }
        };
    }
}
