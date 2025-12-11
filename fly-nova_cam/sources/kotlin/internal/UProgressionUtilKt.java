package kotlin.internal;

import kotlin.Metadata;
import kotlin.UInt;
import kotlin.ULong;
import kotlin.UnsignedKt;

/* compiled from: UProgressionUtil.kt */
@Metadata(m421d1 = {"\u0000 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\u001a*\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0001H\u0002ø\u0001\u0000¢\u0006\u0004\b\u0005\u0010\u0006\u001a*\u0010\u0000\u001a\u00020\u00072\u0006\u0010\u0002\u001a\u00020\u00072\u0006\u0010\u0003\u001a\u00020\u00072\u0006\u0010\u0004\u001a\u00020\u0007H\u0002ø\u0001\u0000¢\u0006\u0004\b\b\u0010\t\u001a*\u0010\n\u001a\u00020\u00012\u0006\u0010\u000b\u001a\u00020\u00012\u0006\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u000eH\u0001ø\u0001\u0000¢\u0006\u0004\b\u000f\u0010\u0006\u001a*\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u00072\u0006\u0010\f\u001a\u00020\u00072\u0006\u0010\r\u001a\u00020\u0010H\u0001ø\u0001\u0000¢\u0006\u0004\b\u0011\u0010\t\u0082\u0002\u0004\n\u0002\b\u0019¨\u0006\u0012"}, m422d2 = {"differenceModulo", "Lkotlin/UInt;", "a", "b", "c", "differenceModulo-WZ9TVnA", "(III)I", "Lkotlin/ULong;", "differenceModulo-sambcqE", "(JJJ)J", "getProgressionLastElement", "start", "end", "step", "", "getProgressionLastElement-Nkh28Cs", "", "getProgressionLastElement-7ftBX0g", "kotlin-stdlib"}, m423k = 2, m424mv = {1, 7, 1}, m426xi = 48)
/* loaded from: classes.dex */
public final class UProgressionUtilKt {
    /* renamed from: differenceModulo-WZ9TVnA, reason: not valid java name */
    private static final int m1770differenceModuloWZ9TVnA(int i, int i2, int i3) {
        int iM906uintRemainderJ1ME1BU = UnsignedKt.m906uintRemainderJ1ME1BU(i, i3);
        int iM906uintRemainderJ1ME1BU2 = UnsignedKt.m906uintRemainderJ1ME1BU(i2, i3);
        int iUintCompare = UnsignedKt.uintCompare(iM906uintRemainderJ1ME1BU, iM906uintRemainderJ1ME1BU2);
        int iM652constructorimpl = UInt.m652constructorimpl(iM906uintRemainderJ1ME1BU - iM906uintRemainderJ1ME1BU2);
        return iUintCompare >= 0 ? iM652constructorimpl : UInt.m652constructorimpl(iM652constructorimpl + i3);
    }

    /* renamed from: differenceModulo-sambcqE, reason: not valid java name */
    private static final long m1771differenceModulosambcqE(long j, long j2, long j3) {
        long jM908ulongRemaindereb3DHEI = UnsignedKt.m908ulongRemaindereb3DHEI(j, j3);
        long jM908ulongRemaindereb3DHEI2 = UnsignedKt.m908ulongRemaindereb3DHEI(j2, j3);
        int iUlongCompare = UnsignedKt.ulongCompare(jM908ulongRemaindereb3DHEI, jM908ulongRemaindereb3DHEI2);
        long jM730constructorimpl = ULong.m730constructorimpl(jM908ulongRemaindereb3DHEI - jM908ulongRemaindereb3DHEI2);
        return iUlongCompare >= 0 ? jM730constructorimpl : ULong.m730constructorimpl(jM730constructorimpl + j3);
    }

    /* renamed from: getProgressionLastElement-Nkh28Cs, reason: not valid java name */
    public static final int m1773getProgressionLastElementNkh28Cs(int i, int i2, int i3) {
        if (i3 > 0) {
            return UnsignedKt.uintCompare(i, i2) >= 0 ? i2 : UInt.m652constructorimpl(i2 - m1770differenceModuloWZ9TVnA(i2, i, UInt.m652constructorimpl(i3)));
        }
        if (i3 < 0) {
            return UnsignedKt.uintCompare(i, i2) <= 0 ? i2 : UInt.m652constructorimpl(i2 + m1770differenceModuloWZ9TVnA(i, i2, UInt.m652constructorimpl(-i3)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }

    /* renamed from: getProgressionLastElement-7ftBX0g, reason: not valid java name */
    public static final long m1772getProgressionLastElement7ftBX0g(long j, long j2, long j3) {
        if (j3 > 0) {
            return UnsignedKt.ulongCompare(j, j2) >= 0 ? j2 : ULong.m730constructorimpl(j2 - m1771differenceModulosambcqE(j2, j, ULong.m730constructorimpl(j3)));
        }
        if (j3 < 0) {
            return UnsignedKt.ulongCompare(j, j2) <= 0 ? j2 : ULong.m730constructorimpl(j2 + m1771differenceModulosambcqE(j, j2, ULong.m730constructorimpl(-j3)));
        }
        throw new IllegalArgumentException("Step is zero.");
    }
}
