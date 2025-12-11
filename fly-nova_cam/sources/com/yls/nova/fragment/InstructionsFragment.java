package com.yls.nova.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.yls.nova.C0549R;
import com.yls.nova.activity.MainActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.utils.LocalUtil;

/* loaded from: classes.dex */
public class InstructionsFragment extends BaseFragment implements View.OnClickListener {
    private static final int MSG_UPDATE_UI = 2736;
    private BannerAdapter adapter;
    private InstructionsBroadcastReceiver broadcastReceiver;
    private ImageView btnBack;
    private ImageView btnNext;
    private ImageView btnPrevious;
    private boolean isChangeLanguage = false;
    private Handler mHandler = new Handler(new Handler.Callback() { // from class: com.yls.nova.fragment.InstructionsFragment.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (InstructionsFragment.this.getActivity() == null || message == null || message.what != InstructionsFragment.MSG_UPDATE_UI) {
                return false;
            }
            InstructionsFragment instructionsFragment = InstructionsFragment.this;
            instructionsFragment.updateCountBar(instructionsFragment.viewPager.getCurrentItem() + 1);
            return false;
        }
    });
    private TextView tvCountBar;
    private ViewPager viewPager;

    private class InstructionsBroadcastReceiver extends BroadcastReceiver {
        private InstructionsBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) throws Resources.NotFoundException {
            String action = intent.getAction();
            if (TextUtils.isEmpty(action) || context == null || InstructionsFragment.this.getActivity() == null) {
                return;
            }
            action.hashCode();
            if (action.equals(IActions.ACTION_CHANGE_LANGUAGE)) {
                InstructionsFragment.this.isChangeLanguage = true;
                InstructionsFragment.this.initPage();
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.fragment_instructions, viewGroup, false);
        this.btnPrevious = (ImageView) viewInflate.findViewById(C0549R.id.instructions_previous_btn);
        this.btnNext = (ImageView) viewInflate.findViewById(C0549R.id.instructions_next_btn);
        this.btnBack = (ImageView) viewInflate.findViewById(C0549R.id.instructions_back);
        this.viewPager = (ViewPager) viewInflate.findViewById(C0549R.id.instructions_view_pager);
        this.tvCountBar = (TextView) viewInflate.findViewById(C0549R.id.instructions_count_bar);
        this.btnPrevious.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        this.btnBack.setOnClickListener(this);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) throws Resources.NotFoundException {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            initPage();
            registerBroadcastReceiver();
        }
    }

    private void registerBroadcastReceiver() {
        this.broadcastReceiver = new InstructionsBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter(IActions.ACTION_CHANGE_LANGUAGE);
        if (Build.VERSION.SDK_INT >= 26) {
            this.mApplication.registerReceiver(this.broadcastReceiver, intentFilter, 2);
        } else {
            this.mApplication.registerReceiver(this.broadcastReceiver, intentFilter);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPage() throws Resources.NotFoundException {
        BannerAdapter bannerAdapter = new BannerAdapter(getChildFragmentManager());
        this.adapter = bannerAdapter;
        this.viewPager.setAdapter(bannerAdapter);
        updateCountBar(this.viewPager.getCurrentItem() + 1);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        if (this.broadcastReceiver != null) {
            this.mApplication.unregisterReceiver(this.broadcastReceiver);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) throws Resources.NotFoundException {
        if (view == null || getActivity() == null) {
            return;
        }
        switch (view.getId()) {
            case C0549R.id.instructions_back /* 2131296418 */:
                if (this.isChangeLanguage) {
                    startActivity(new Intent(getActivity(), (Class<?>) MainActivity.class));
                }
                getActivity().finish();
                break;
            case C0549R.id.instructions_next_btn /* 2131296436 */:
                changeViewItem(1);
                break;
            case C0549R.id.instructions_previous_btn /* 2131296437 */:
                changeViewItem(0);
                break;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateCountBar(int i) {
        BannerAdapter bannerAdapter = this.adapter;
        if (bannerAdapter != null) {
            int count = bannerAdapter.getCount();
            if (i == 1) {
                this.btnPrevious.setVisibility(4);
                this.btnNext.setVisibility(0);
            } else if (i == count) {
                this.btnNext.setVisibility(4);
                this.btnPrevious.setVisibility(0);
            } else {
                this.btnPrevious.setVisibility(0);
                this.btnNext.setVisibility(0);
            }
            String str = String.format(getString(C0549R.string.count_item_format), Integer.valueOf(i), Integer.valueOf(count));
            TextView textView = this.tvCountBar;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }

    private void changeViewItem(int i) throws Resources.NotFoundException {
        if (this.adapter != null) {
            int currentItem = this.viewPager.getCurrentItem();
            if (i == 0) {
                currentItem--;
                if (currentItem < 0) {
                    return;
                }
            } else if (i == 1 && (currentItem = currentItem + 1) > this.adapter.getCount() - 1) {
                return;
            }
            this.viewPager.setCurrentItem(currentItem);
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessageDelayed(MSG_UPDATE_UI, 100L);
            }
        }
    }

    private class BannerAdapter extends FragmentStatePagerAdapter {
        private int[] images;

        BannerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            int i = PreferencesHelper.getSharedPreferences(InstructionsFragment.this.getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(InstructionsFragment.this.getActivity()));
            if (i == 1) {
                this.images = new int[]{C0549R.mipmap.instruction_french_1, C0549R.mipmap.instruction_french_2};
                return;
            }
            if (i == 2) {
                this.images = new int[]{C0549R.mipmap.instruction_german_1, C0549R.mipmap.instruction_german_2};
                return;
            }
            if (i == 3) {
                this.images = new int[]{C0549R.mipmap.instruction_spanish_1, C0549R.mipmap.instruction_spanish_2};
                return;
            }
            if (i == 4) {
                this.images = new int[]{C0549R.mipmap.instruction_portuguese_1, C0549R.mipmap.instruction_portuguese_2};
                return;
            }
            if (i == 5) {
                this.images = new int[]{C0549R.mipmap.instruction_bulgarian_1, C0549R.mipmap.instruction_bulgarian_2};
                return;
            }
            if (i == 6) {
                this.images = new int[]{C0549R.mipmap.instruction_polish_1, C0549R.mipmap.instruction_polish_2};
                return;
            }
            if (i == 7) {
                this.images = new int[]{C0549R.mipmap.instruction_dutch_1, C0549R.mipmap.instruction_dutch_2};
                return;
            }
            if (i == 8) {
                this.images = new int[]{C0549R.mipmap.instruction_czech_1, C0549R.mipmap.instruction_czech_2};
                return;
            }
            if (i == 9) {
                this.images = new int[]{C0549R.mipmap.instruction_croatian_1, C0549R.mipmap.instruction_croatian_2};
                return;
            }
            if (i == 10) {
                this.images = new int[]{C0549R.mipmap.instruction_romanian_1, C0549R.mipmap.instruction_romanian_2};
                return;
            }
            if (i == 11) {
                this.images = new int[]{C0549R.mipmap.instruction_slovak_1, C0549R.mipmap.instruction_slovak_2};
                return;
            }
            if (i == 12) {
                this.images = new int[]{C0549R.mipmap.instruction_slovenian_1, C0549R.mipmap.instruction_slovenian_2};
                return;
            }
            if (i == 13) {
                this.images = new int[]{C0549R.mipmap.instruction_greek_1, C0549R.mipmap.instruction_greek_2};
            } else if (i == 14) {
                this.images = new int[]{C0549R.mipmap.instruction_italian_1, C0549R.mipmap.instruction_italian_2};
            } else {
                this.images = new int[]{C0549R.mipmap.instruction_english_1, C0549R.mipmap.instruction_english_2};
            }
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.images.length;
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter, androidx.viewpager.widget.PagerAdapter
        public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
            super.setPrimaryItem(viewGroup, i, obj);
            if (InstructionsFragment.this.mHandler != null) {
                InstructionsFragment.this.mHandler.sendEmptyMessage(InstructionsFragment.MSG_UPDATE_UI);
            }
        }

        @Override // androidx.fragment.app.FragmentStatePagerAdapter
        public Fragment getItem(int i) {
            if (i >= getCount()) {
                return null;
            }
            InstructionSubFragment instructionSubFragment = new InstructionSubFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(IConstants.KEY_STEP_NUMBER, i);
            bundle.putInt(IConstants.KEY_RES_ID, this.images[i]);
            instructionSubFragment.setBundle(bundle);
            return instructionSubFragment;
        }
    }
}
