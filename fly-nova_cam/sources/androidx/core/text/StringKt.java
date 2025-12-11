package androidx.core.text;

import android.text.TextUtils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: String.kt */
@Metadata(m421d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u000e\n\u0000\u001a\r\u0010\u0000\u001a\u00020\u0001*\u00020\u0001H\u0086\b¨\u0006\u0002"}, m422d2 = {"htmlEncode", "", "core-ktx_release"}, m423k = 2, m424mv = {1, 7, 1}, m426xi = 48)
/* loaded from: classes.dex */
public final class StringKt {
    public static final String htmlEncode(String str) {
        Intrinsics.checkNotNullParameter(str, "<this>");
        String strHtmlEncode = TextUtils.htmlEncode(str);
        Intrinsics.checkNotNullExpressionValue(strHtmlEncode, "htmlEncode(this)");
        return strHtmlEncode;
    }
}
