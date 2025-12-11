package com.yls.nova.libs.pullrefreshview.support.impl;

import com.yls.nova.libs.pullrefreshview.layout.PullRefreshLayout;

/* loaded from: classes.dex */
public interface Loadable {
    boolean onScroll(float f);

    void onScrollChange(int i);

    boolean onStartFling(float f);

    void setPullRefreshLayout(PullRefreshLayout pullRefreshLayout);

    void startLoad();

    void stopLoad();
}
