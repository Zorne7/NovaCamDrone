package tv.danmaku.ijk.media.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.MediaController;
import androidx.appcompat.app.ActionBar;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes.dex */
public class AndroidMediaController extends MediaController implements IMediaController {
    private ActionBar mActionBar;
    private ArrayList<View> mShowOnceArray;

    private void initView(Context context) {
    }

    public AndroidMediaController(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context, boolean z) {
        super(context, z);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public AndroidMediaController(Context context) {
        super(context);
        this.mShowOnceArray = new ArrayList<>();
        initView(context);
    }

    public void setSupportActionBar(ActionBar actionBar) {
        this.mActionBar = actionBar;
        if (isShowing()) {
            actionBar.show();
        } else {
            actionBar.hide();
        }
    }

    @Override // android.widget.MediaController, tv.danmaku.ijk.media.widget.IMediaController
    public void show() {
        super.show();
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.show();
        }
    }

    @Override // android.widget.MediaController, tv.danmaku.ijk.media.widget.IMediaController
    public void hide() {
        super.hide();
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.hide();
        }
        Iterator<View> it = this.mShowOnceArray.iterator();
        while (it.hasNext()) {
            it.next().setVisibility(8);
        }
        this.mShowOnceArray.clear();
    }

    @Override // tv.danmaku.ijk.media.widget.IMediaController
    public void showOnce(View view) {
        this.mShowOnceArray.add(view);
        view.setVisibility(0);
        show();
    }
}
