package com.yls.nova.libs.pullrefreshview.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RelativeLayout;
import androidx.core.view.ViewCompat;
import com.yls.nova.libs.pullrefreshview.support.impl.Refreshable;

/* loaded from: classes.dex */
public abstract class BaseHeaderView extends RelativeLayout implements Refreshable {
    public static final int LOOSENT_O_REFRESH = 2;
    public static final int NONE = 0;
    public static final int PULLING = 1;
    public static final int REFRESHING = 3;
    public static final int REFRESH_CLONE = 4;
    private boolean isLockState;
    OnRefreshListener onRefreshListener;
    private PullRefreshLayout pullRefreshLayout;
    private int scrollState;
    private int stateType;

    public interface OnRefreshListener {
        void onRefresh(BaseHeaderView baseHeaderView);
    }

    public int getLayoutType() {
        return 0;
    }

    public abstract float getSpanHeight();

    protected abstract void onStateChange(int i);

    public BaseHeaderView(Context context) {
        this(context, null);
    }

    public BaseHeaderView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BaseHeaderView(Context context, AttributeSet attributeSet, int i) {
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
        Log.i("BaseHeaderView", "" + i);
        this.stateType = i;
        if (i == 3) {
            this.isLockState = true;
            OnRefreshListener onRefreshListener = this.onRefreshListener;
            if (onRefreshListener != null) {
                onRefreshListener.onRefresh(this);
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
            if (moveY > 0.0f) {
                this.pullRefreshLayout.startMoveTo(moveY, 0.0f);
                setState(0);
            }
        }
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        this.pullRefreshLayout = pullRefreshLayout;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    public void startRefresh() {
        PullRefreshLayout pullRefreshLayout = this.pullRefreshLayout;
        if (pullRefreshLayout == null || pullRefreshLayout.getMoveY() != 0.0f) {
            return;
        }
        this.pullRefreshLayout.startMoveTo(0.0f, getSpanHeight());
        setState(3);
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    public void stopRefresh() {
        this.isLockState = false;
        setState(4);
        postDelayed(new Runnable() { // from class: com.yls.nova.libs.pullrefreshview.layout.BaseHeaderView.1
            @Override // java.lang.Runnable
            public void run() {
                BaseHeaderView.this.close();
            }
        }, 400L);
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x002f  */
    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean onScroll(float f) {
        boolean z;
        int layoutType = getLayoutType();
        if (layoutType == 16) {
            ViewCompat.setTranslationY(this, getMeasuredHeight());
        } else {
            if (layoutType == 1) {
                ViewCompat.setTranslationY(this, f);
                ViewCompat.setTranslationY(this.pullRefreshLayout.getPullView(), 0.0f);
                z = true;
                float spanHeight = getSpanHeight();
                if (this.scrollState == 1) {
                    if (f >= spanHeight) {
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

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    public void onScrollChange(int i) {
        this.scrollState = i;
    }

    @Override // com.yls.nova.libs.pullrefreshview.support.impl.Refreshable
    public boolean onStartFling(float f) {
        float spanHeight = getSpanHeight();
        if (f >= spanHeight) {
            this.pullRefreshLayout.startMoveTo(f, spanHeight);
            setState(3);
            return true;
        }
        this.pullRefreshLayout.startMoveTo(f, 0.0f);
        setState(0);
        return false;
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
    }
}
