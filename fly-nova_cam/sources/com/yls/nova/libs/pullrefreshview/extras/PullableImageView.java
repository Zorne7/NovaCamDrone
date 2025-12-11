package com.yls.nova.libs.pullrefreshview.extras;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatImageView;
import com.yls.nova.libs.pullrefreshview.support.impl.Pullable;

/* loaded from: classes.dex */
public class PullableImageView extends AppCompatImageView implements Pullable {
    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Pullable
    public boolean isGetBottom() {
        return true;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Pullable
    public boolean isGetTop() {
        return true;
    }

    public PullableImageView(Context context) {
        super(context);
    }

    public PullableImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PullableImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
