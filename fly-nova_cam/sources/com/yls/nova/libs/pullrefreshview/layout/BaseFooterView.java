package com.yls.nova.libs.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;
import com.yls.nova.libs.pullrefreshview.support.impl.Loadable;

/* loaded from: classes.dex */
public abstract class BaseFooterView extends RelativeLayout implements Loadable {
    public static final int LOADING = 3;
    public static final int LOAD_CLONE = 4;
    public static final int LOOSENT_O_LOAD = 2;
    public static final int NONE = 0;
    public static final int PULLING = 1;
    private boolean isLockState;
    private OnLoadListener onLoadListener;
    private PullRefreshLayout pullRefreshLayout;
    private int scrollState;
    private int stateType;

    public interface OnLoadListener {
        void onLoad(BaseFooterView baseFooterView);
    }

    public int getLayoutType() {
        return 0;
    }

    public abstract float getSpanHeight();

    protected abstract void onStateChange(int i);

    public BaseFooterView(Context context) {
        this(context, null);
    }

    public BaseFooterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaseFooterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.stateType = 0;
        this.isLockState = false;
        this.scrollState = 0;
        init();
    }

    private void init() {
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    protected boolean isLockState() {
        return this.isLockState;
    }

    private void setState(int i) {
        if (this.isLockState || this.stateType == i) {
            return;
        }
        Log.i("BaseFooterView", "" + i);
        this.stateType = i;
        if (i == 3) {
            this.isLockState = true;
            OnLoadListener onLoadListener = this.onLoadListener;
            if (onLoadListener != null) {
                onLoadListener.onLoad(this);
            }
        }
        onStateChange(i);
    }

    public int getType() {
        return this.stateType;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void close() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout != null) {
            float moveY = pullRefreshLayout.getMoveY();
            if (moveY < 0.0f) {
                this.pullRefreshLayout.startMoveTo(moveY, 0.0f);
                setState(0);
            }
        }
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public void startLoad() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout == null || pullRefreshLayout.getMoveY() != 0.0f) {
            return;
        }
        this.pullRefreshLayout.startMoveTo(0.0f, -getSpanHeight());
        setState(3);
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public void stopLoad() {
        this.isLockState = false;
        setState(4);
        postDelayed(new Runnable() { // from class: com.yls.nova.libs.pullrefreshview.layout.BaseFooterView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseFooterView.this.close();
            }
        }, 400L);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0030  */
    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onScroll(float f) {
        boolean z;
        int layoutType = getLayoutType();
        if (layoutType == 16) {
            ViewCompat.setTranslationY(this, -getMeasuredHeight());
        } else {
            if (layoutType == 1) {
                ViewCompat.setTranslationY(this, f);
                ViewCompat.setTranslationY(this.pullRefreshLayout.getPullView(), 0.0f);
                z = true;
                float spanHeight = getSpanHeight();
                if (this.scrollState == 1) {
                    if (f <= (-spanHeight)) {
                        setState(2);
                    } else {
                        setState(1);
                    }
                }
                return z;
            }
            ViewCompat.setTranslationY(this, f);
        }
        z = false;
        float spanHeight2 = getSpanHeight();
        if (this.scrollState == 1) {
        }
        return z;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public void onScrollChange(int i) {
        this.scrollState = i;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public boolean onStartFling(float f) {
        float f2 = -getSpanHeight();
        if (f <= f2) {
            this.pullRefreshLayout.startMoveTo(f, f2);
            setState(3);
            return true;
        }
        this.pullRefreshLayout.startMoveTo(f, 0.0f);
        setState(0);
        return false;
    }

    public void setOnLoadListener(OnLoadListener onLoadListener) {
        this.onLoadListener = onLoadListener;
    }
}
