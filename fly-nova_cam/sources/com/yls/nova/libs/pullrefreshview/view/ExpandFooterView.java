package com.yls.nova.libs.pullrefreshview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.yls.nova.C0549R;
import com.yls.nova.libs.pullrefreshview.layout.BaseFooterView;
import com.yls.nova.libs.pullrefreshview.layout.PullRefreshLayout;
import com.yls.nova.libs.pullrefreshview.utils.AnimUtil;

/* loaded from: classes.dex */
public class ExpandFooterView extends BaseFooterView {
    private int layoutType;
    private View loadBox;
    private View progress;
    private int state;
    private View stateImg;

    public ExpandFooterView(Context context) {
        this(context, null);
    }

    public ExpandFooterView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ExpandFooterView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.state = 0;
        this.layoutType = 1;
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(C0549R.layout.view_footer_expand, (ViewGroup) this, true);
        this.progress = findViewById(C0549R.id.progress);
        this.stateImg = findViewById(C0549R.id.state);
        this.loadBox = findViewById(C0549R.id.load_box);
        setLayoutParams(new ViewGroup.LayoutParams(-1, 350));
    }

    @Override // com.yls.nova.libs.pullrefreshview.layout.BaseFooterView, com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout) {
        super.setPullRefreshLayout(pullRefreshLayout);
        pullRefreshLayout.setMaxDistance(350);
    }

    @Override // com.yls.nova.libs.pullrefreshview.layout.BaseFooterView
    protected void onStateChange(int i) {
        this.state = i;
        ObjectAnimator.clearAllAnimations();
        this.stateImg.setVisibility(4);
        this.progress.setVisibility(0);
        ViewHelper.setAlpha(this.progress, 1.0f);
        if (i == 3) {
            View view = this.progress;
            AnimUtil.startRotation(view, 359.99f + ViewHelper.getRotation(view), 500L, 0L, -1);
        } else {
            if (i != 4) {
                return;
            }
            AnimUtil.startShow(this.stateImg, 0.1f, 400L, 200L);
            AnimUtil.startHide(this.progress);
        }
    }

    @Override // com.yls.nova.libs.pullrefreshview.layout.BaseFooterView
    public float getSpanHeight() {
        return this.loadBox.getHeight();
    }

    @Override // com.yls.nova.libs.pullrefreshview.layout.BaseFooterView
    public int getLayoutType() {
        return this.layoutType;
    }

    @Override // com.yls.nova.libs.pullrefreshview.layout.BaseFooterView, com.yls.nova.libs.pullrefreshview.support.impl.Loadable
    public boolean onScroll(float f) {
        boolean zOnScroll = super.onScroll(f);
        if (!isLockState()) {
            ViewHelper.setRotation(this.progress, ((f * f) * 48.0f) / 31250.0f);
        }
        return zOnScroll;
    }
}
