package com.yls.nova.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.yls.nova.BuildConfig;
import com.yls.nova.C0549R;
import com.yls.nova.activity.MainActivity;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.base.MainApplication;
import com.yls.nova.dialog.SelectLanguageDialog;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.LocalUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class SettingsFragment extends BaseFragment implements AdapterView.OnItemClickListener, SelectLanguageDialog.OnSelectLanguageListener {
    private String mAppVersion = "";
    private MyBroadcastReceiver mReceiver;
    private SettingsAdapter settingsAdapter;
    private ListView settingsListView;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.fragment_settings, viewGroup, false);
        ListView listView = (ListView) viewInflate.findViewById(C0549R.id.settings_list_view);
        this.settingsListView = listView;
        listView.setOnItemClickListener(this);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) throws Resources.NotFoundException {
        super.onActivityCreated(bundle);
        if (getActivity() != null) {
            initListView();
            this.mAppVersion = "V" + getVersion();
        }
    }

    private static String getVersion() {
        return BuildConfig.VERSION_NAME;
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        this.mReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(IActions.ACTION_CHANGE_LANGUAGE);
        if (Build.VERSION.SDK_INT >= 26) {
            MainApplication.getInstance().registerReceiver(this.mReceiver, intentFilter, 2);
        } else {
            MainApplication.getInstance().registerReceiver(this.mReceiver, intentFilter);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (this.mReceiver != null) {
            MainApplication.getInstance().unregisterReceiver(this.mReceiver);
        }
    }

    @Override // android.widget.AdapterView.OnItemClickListener
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        SettingsAdapter settingsAdapter = this.settingsAdapter;
        if (settingsAdapter != null) {
            String str = (String) settingsAdapter.getItem(i);
            if (TextUtils.isEmpty(str) || !str.equals(getString(C0549R.string.language_setting))) {
                return;
            }
            showSelectLanguageDialog(PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())));
        }
    }

    private void initListView() throws Resources.NotFoundException {
        int i;
        if (getActivity() != null) {
            String[] stringArray = getResources().getStringArray(C0549R.array.settings_list_google);
            ArrayList<String> arrayList = new ArrayList();
            Collections.addAll(arrayList, stringArray);
            HashMap map = new HashMap();
            for (String str : arrayList) {
                if (!TextUtils.isEmpty(str)) {
                    if (str.equals(getString(C0549R.string.right_hand_mode))) {
                        i = 0;
                    } else {
                        i = (str.equals(getString(C0549R.string.app_version)) || str.equals(getString(C0549R.string.language_setting))) ? 1 : -1;
                    }
                    if (i != -1) {
                        map.put(str, Integer.valueOf(i));
                    }
                }
            }
            SettingsAdapter settingsAdapter = new SettingsAdapter(getActivity().getApplicationContext());
            this.settingsAdapter = settingsAdapter;
            this.settingsListView.setAdapter((ListAdapter) settingsAdapter);
            this.settingsAdapter.setItemList(arrayList, map);
        }
    }

    private void showSelectLanguageDialog(int i) {
        if (getActivity() != null) {
            SelectLanguageDialog selectLanguageDialog = new SelectLanguageDialog();
            selectLanguageDialog.setLanguageFlag(i);
            selectLanguageDialog.setOnSelectLanguageListener(this);
            selectLanguageDialog.show(getActivity().getFragmentManager(), "select_language_dialog");
        }
    }

    @Override // com.yls.nova.dialog.SelectLanguageDialog.OnSelectLanguageListener
    public void onSelectLanguage(int i) {
        if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != i) {
            Locale language = AppUtils.getLanguage(i);
            if (language != null) {
                AppUtils.setLanguage(getActivity().getApplicationContext(), language);
                PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, i);
                getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                return;
            }
            return;
        }
        showShortToast(C0549R.string.language_no_change);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        private MyBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (context == null || intent == null) {
                return;
            }
            String action = intent.getAction();
            if (TextUtils.isEmpty(action)) {
                return;
            }
            action.hashCode();
            if (action.equals(IActions.ACTION_CHANGE_LANGUAGE) && SettingsFragment.this.getActivity() != null) {
                SettingsFragment.this.startActivity(new Intent(SettingsFragment.this.getActivity(), (Class<?>) MainActivity.class));
                SettingsFragment.this.getActivity().finish();
            }
        }
    }

    private class SettingsAdapter extends BaseAdapter {
        private List<String> itemList = new ArrayList();
        private Context mContext;
        private SharedPreferences sharedPreferences;
        private Map<String, Integer> typeMap;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getViewTypeCount() {
            return 3;
        }

        SettingsAdapter(Context context) {
            this.mContext = context;
            this.sharedPreferences = PreferencesHelper.getSharedPreferences(this.mContext);
        }

        void setItemList(List<String> list, Map<String, Integer> map) {
            if (list != null) {
                this.itemList = list;
            }
            if (map != null) {
                this.typeMap = map;
            }
            notifyDataSetChanged();
        }

        @Override // android.widget.BaseAdapter, android.widget.Adapter
        public int getItemViewType(int i) {
            Map<String, Integer> map;
            Integer num;
            String str = (String) getItem(i);
            if (TextUtils.isEmpty(str) || (map = this.typeMap) == null || (num = map.get(str)) == null) {
                return 2;
            }
            return num.intValue();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.itemList.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            if (i < 0 || i >= this.itemList.size()) {
                return null;
            }
            return this.itemList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            ViewHolder1 viewHolder1;
            String str = (String) getItem(i);
            int itemViewType = getItemViewType(i);
            if (itemViewType == 0) {
                if (view == null) {
                    view = LayoutInflater.from(this.mContext).inflate(C0549R.layout.item_settings_one, viewGroup, false);
                    viewHolder = new ViewHolder();
                    viewHolder.f65tv = (TextView) view.findViewById(C0549R.id.item_settings_one_tv);
                    viewHolder.tv1 = (TextView) view.findViewById(C0549R.id.item_settings_one_switch_tv1);
                    viewHolder.tv2 = (TextView) view.findViewById(C0549R.id.item_settings_one_switch_tv2);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (ViewHolder) view.getTag();
                }
                if (TextUtils.isEmpty(str)) {
                    return view;
                }
                viewHolder.f65tv.setText(str);
                if (!str.equals(SettingsFragment.this.getString(C0549R.string.right_hand_mode))) {
                    return view;
                }
                viewHolder.tv1.setText(SettingsFragment.this.getString(C0549R.string.shutdown));
                viewHolder.tv2.setText(SettingsFragment.this.getString(C0549R.string.open));
                if (this.sharedPreferences.getBoolean(IConstants.RIGHT_HAND_MODE, false)) {
                    viewHolder.tv2.setSelected(true);
                    SettingsFragment.this.switchTextView(2, viewHolder.tv2, viewHolder.tv1, null, null, null);
                } else {
                    viewHolder.tv1.setSelected(true);
                }
                viewHolder.tv1.setOnClickListener(new View.OnClickListener() { // from class: com.yls.nova.fragment.SettingsFragment.SettingsAdapter.1
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (!view2.isSelected() && SettingsAdapter.this.sharedPreferences.getBoolean(IConstants.RIGHT_HAND_MODE, false)) {
                            view2.setSelected(true);
                            viewHolder.tv2.setSelected(false);
                            PreferencesHelper.putBooleanValue(SettingsAdapter.this.mContext, IConstants.RIGHT_HAND_MODE, false);
                            SettingsFragment.this.switchTextView(0, viewHolder.tv1, viewHolder.tv2, null, null, null);
                            SettingsFragment.this.getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                        }
                    }
                });
                viewHolder.tv2.setOnClickListener(new View.OnClickListener() { // from class: com.yls.nova.fragment.SettingsFragment.SettingsAdapter.2
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        if (view2.isSelected() || SettingsAdapter.this.sharedPreferences.getBoolean(IConstants.RIGHT_HAND_MODE, false)) {
                            return;
                        }
                        view2.setSelected(true);
                        viewHolder.tv1.setSelected(false);
                        PreferencesHelper.putBooleanValue(SettingsAdapter.this.mContext, IConstants.RIGHT_HAND_MODE, true);
                        SettingsFragment.this.switchTextView(2, viewHolder.tv2, viewHolder.tv1, null, null, null);
                        SettingsFragment.this.getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    }
                });
                return view;
            }
            if (itemViewType != 1) {
                if (itemViewType != 2) {
                    return view;
                }
                if (view == null) {
                    View viewInflate = LayoutInflater.from(this.mContext).inflate(C0549R.layout.item_settings_three, viewGroup, false);
                    ViewHolder2 viewHolder2 = new ViewHolder2();
                    viewHolder2.f67tv = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_tv);
                    viewHolder2.tv1 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_tv_1);
                    viewHolder2.tv2 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_switch_tv1);
                    viewHolder2.tv3 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_switch_tv2);
                    viewHolder2.tv4 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_switch_tv3);
                    viewHolder2.tv5 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_switch_tv4);
                    viewHolder2.tv6 = (TextView) viewInflate.findViewById(C0549R.id.item_settings_three_switch_tv5);
                    viewInflate.setTag(viewHolder2);
                    return viewInflate;
                }
                return view;
            }
            if (view == null) {
                view = LayoutInflater.from(this.mContext).inflate(C0549R.layout.item_settings_two, viewGroup, false);
                viewHolder1 = new ViewHolder1();
                viewHolder1.f66tv = (TextView) view.findViewById(C0549R.id.item_settings_two_tv);
                viewHolder1.tv1 = (TextView) view.findViewById(C0549R.id.item_settings_two_tv_1);
                viewHolder1.tv2 = (TextView) view.findViewById(C0549R.id.item_settings_two_switch_tv1);
                viewHolder1.tv3 = (TextView) view.findViewById(C0549R.id.item_settings_two_switch_tv2);
                viewHolder1.icon = (ImageView) view.findViewById(C0549R.id.item_settings_two_icon);
                viewHolder1.layout = (LinearLayout) view.findViewById(C0549R.id.item_settings_two_switch_layout);
                view.setTag(viewHolder1);
            } else {
                viewHolder1 = (ViewHolder1) view.getTag();
            }
            if (TextUtils.isEmpty(str)) {
                return view;
            }
            viewHolder1.f66tv.setText(str);
            viewHolder1.icon.setVisibility(8);
            viewHolder1.tv1.setVisibility(8);
            viewHolder1.layout.setVisibility(8);
            if (!str.equals(SettingsFragment.this.getString(C0549R.string.app_version))) {
                return view;
            }
            viewHolder1.tv1.setText(SettingsFragment.this.mAppVersion);
            viewHolder1.tv1.setVisibility(0);
            return view;
        }

        private class ViewHolder {

            /* renamed from: tv */
            private TextView f65tv;
            private TextView tv1;
            private TextView tv2;

            private ViewHolder() {
            }
        }

        private class ViewHolder1 {
            private ImageView icon;
            private LinearLayout layout;

            /* renamed from: tv */
            private TextView f66tv;
            private TextView tv1;
            private TextView tv2;
            private TextView tv3;

            private ViewHolder1() {
            }
        }

        private class ViewHolder2 {

            /* renamed from: tv */
            private TextView f67tv;
            private TextView tv1;
            private TextView tv2;
            private TextView tv3;
            private TextView tv4;
            private TextView tv5;
            private TextView tv6;

            private ViewHolder2() {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void switchTextView(int i, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5) {
        if (i == 0) {
            textView.setBackgroundResource(C0549R.drawable.bg_switch_left_settings);
        } else if (i == 1) {
            textView.setBackgroundColor(-1);
        } else {
            textView.setBackgroundResource(C0549R.drawable.bg_switch_right_settings);
        }
        textView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        textView2.setTextColor(-1);
        textView2.setBackgroundColor(0);
        if (textView3 != null) {
            textView3.setTextColor(-1);
            textView3.setBackgroundColor(0);
        }
        if (textView4 != null) {
            textView4.setTextColor(-1);
            textView4.setBackgroundColor(0);
        }
        if (textView5 != null) {
            textView5.setTextColor(-1);
            textView5.setBackgroundColor(0);
        }
    }
}
