package com.yls.nova.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import com.yls.nova.C0549R;
import com.yls.nova.activity.BrowseFileActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.libs.HorizontalListView;
import com.yls.nova.tools.IConstants;
import com.yls.nova.utils.AppUtils;

/* loaded from: classes.dex */
public class BrowseSelectFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private HorizontalListView listView;
    private String whichDir = IConstants.VIEW_FRONT;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.fragment_browse_select, viewGroup, false);
        ImageView imageView = (ImageView) viewInflate.findViewById(C0549R.id.browse_select_back);
        HorizontalListView horizontalListView = (HorizontalListView) viewInflate.findViewById(C0549R.id.browse_select_list_view);
        this.listView = horizontalListView;
        horizontalListView.setOnItemClickListener(this);
        imageView.setOnClickListener(new View.OnClickListener() { // from class: com.yls.nova.fragment.BrowseSelectFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (BrowseSelectFragment.this.getActivity() != null) {
                    BrowseSelectFragment.this.getActivity().setResult(-1);
                    BrowseSelectFragment.this.getActivity().onBackPressed();
                }
            }
        });
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            Bundle bundle2 = getBundle();
            if (bundle2 != null) {
                this.whichDir = bundle2.getString(IConstants.KEY_DIR_TYPE);
            }
            this.listView.setAdapter((ListAdapter) new BroseSelectAdapter());
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        switchFragment(i);
    }

    private class BroseSelectAdapter extends BaseAdapter {
        private int[] resIDs;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        private BroseSelectAdapter() {
            this.resIDs = new int[]{C0549R.mipmap.ic_local_picture, C0549R.mipmap.ic_local_video};
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.resIDs.length;
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return Integer.valueOf(this.resIDs[i]);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = new ImageView(BrowseSelectFragment.this.getActivity().getApplicationContext());
                view.setPadding(2, 2, 2, 2);
                view.setLayoutParams(new ViewGroup.LayoutParams(AppUtils.dip2px(BrowseSelectFragment.this.getContext(), 150.0f), AppUtils.dip2px(BrowseSelectFragment.this.getContext(), 150.0f)));
            }
            ((ImageView) view).setImageResource(this.resIDs[i]);
            return view;
        }
    }

    private void switchFragment(int i) {
        String string;
        if (getActivity() == null) {
            return;
        }
        if (i == 0) {
            string = getString(C0549R.string.local_mode_picture);
        } else {
            string = i != 1 ? null : getString(C0549R.string.local_mode_video);
        }
        BaseFragment browseFileFragment = (BaseFragment) getActivity().getSupportFragmentManager().findFragmentByTag(IConstants.FRAGMENT_TAG_BROWSE_FILE);
        if (browseFileFragment == null) {
            browseFileFragment = new BrowseFileFragment();
        }
        if (TextUtils.isEmpty(string)) {
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(IConstants.KEY_FILE_NAME, string);
        bundle.putString(IConstants.KEY_DIR_TYPE, this.whichDir);
        browseFileFragment.setBundle(bundle);
        ((BrowseFileActivity) getActivity()).changeFragment(C0549R.id.browse_file_frame_layout, browseFileFragment, IConstants.FRAGMENT_TAG_BROWSE_FILE, true);
    }
}
