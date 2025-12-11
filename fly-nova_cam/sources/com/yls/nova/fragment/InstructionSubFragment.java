package com.yls.nova.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.yls.nova.C0549R;
import com.yls.nova.base.BaseFragment;
import com.yls.nova.tools.IActions;
import com.yls.nova.tools.IConstants;
import com.yls.nova.tools.PreferencesHelper;
import com.yls.nova.utils.AppUtils;
import com.yls.nova.utils.LocalUtil;
import java.util.Locale;

/* loaded from: classes.dex */
public class InstructionSubFragment extends BaseFragment implements View.OnClickListener {
    private ImageView btnBulgarian;
    private ImageView btnCroatian;
    private ImageView btnCzech;
    private ImageView btnDutch;
    private ImageView btnEnglish;
    private ImageView btnFrench;
    private ImageView btnGerman;
    private ImageView btnGreek;
    private ImageView btnItalian;
    private ImageView btnPolish;
    private ImageView btnPortuguese;
    private ImageView btnRomanian;
    private ImageView btnSlovak;
    private ImageView btnSlovenian;
    private ImageView btnSpanish;
    private ImageView imageView;
    private LinearLayout layoutLanguage;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.fragment_insturction_sub, viewGroup, false);
        this.layoutLanguage = (LinearLayout) viewInflate.findViewById(C0549R.id.instructions_language_layout);
        this.imageView = (ImageView) viewInflate.findViewById(C0549R.id.iv_image);
        this.btnEnglish = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_english_iv);
        this.btnGerman = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_german_iv);
        this.btnFrench = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_french_iv);
        this.btnSpanish = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_spanish_iv);
        this.btnPortuguese = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_portuguese_iv);
        this.btnBulgarian = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_bulgarian_iv);
        this.btnPolish = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_polish_iv);
        this.btnDutch = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_dutch_iv);
        this.btnCzech = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_czech_iv);
        this.btnCroatian = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_croatian_iv);
        this.btnRomanian = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_romanian_iv);
        this.btnSlovak = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_slovak_iv);
        this.btnSlovenian = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_slovenian_iv);
        this.btnGreek = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_greek_iv);
        this.btnItalian = (ImageView) viewInflate.findViewById(C0549R.id.instructions_language_italian_iv);
        this.btnEnglish.setOnClickListener(this);
        this.btnGerman.setOnClickListener(this);
        this.btnFrench.setOnClickListener(this);
        this.btnSpanish.setOnClickListener(this);
        this.btnPortuguese.setOnClickListener(this);
        this.btnBulgarian.setOnClickListener(this);
        this.btnPolish.setOnClickListener(this);
        this.btnDutch.setOnClickListener(this);
        this.btnCzech.setOnClickListener(this);
        this.btnCroatian.setOnClickListener(this);
        this.btnRomanian.setOnClickListener(this);
        this.btnSlovak.setOnClickListener(this);
        this.btnSlovenian.setOnClickListener(this);
        this.btnGreek.setOnClickListener(this);
        this.btnItalian.setOnClickListener(this);
        return viewInflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        Bundle bundle2;
        super.onActivityCreated(bundle);
        if (getActivity() == null || (bundle2 = getBundle()) == null) {
            return;
        }
        int i = bundle2.getInt(IConstants.KEY_RES_ID, -1);
        int i2 = bundle2.getInt(IConstants.KEY_STEP_NUMBER, -1);
        if (i != -1) {
            this.imageView.setImageResource(i);
        }
        if (i2 != 0) {
            this.layoutLanguage.setVisibility(8);
            return;
        }
        int i3 = PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity()));
        if (i3 == 0) {
            this.btnEnglish.setSelected(true);
            return;
        }
        if (i3 == 2) {
            this.btnGerman.setSelected(true);
            return;
        }
        if (i3 == 1) {
            this.btnFrench.setSelected(true);
            return;
        }
        if (i3 == 3) {
            this.btnSpanish.setSelected(true);
            return;
        }
        if (i3 == 5) {
            this.btnBulgarian.setSelected(true);
            return;
        }
        if (i3 == 6) {
            this.btnPolish.setSelected(true);
            return;
        }
        if (i3 == 7) {
            this.btnDutch.setSelected(true);
            return;
        }
        if (i3 == 9) {
            this.btnCroatian.setSelected(true);
            return;
        }
        if (i3 == 10) {
            this.btnRomanian.setSelected(true);
            return;
        }
        if (i3 == 11) {
            this.btnSlovak.setSelected(true);
            return;
        }
        if (i3 == 12) {
            this.btnSlovenian.setSelected(true);
            return;
        }
        if (i3 == 13) {
            this.btnGreek.setSelected(true);
            return;
        }
        if (i3 == 14) {
            this.btnItalian.setSelected(true);
        } else if (i3 == 8) {
            this.btnCzech.setSelected(true);
        } else if (i3 == 4) {
            this.btnPortuguese.setSelected(true);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        Locale language;
        Locale language2;
        Locale language3;
        Locale language4;
        Locale language5;
        Locale language6;
        Locale language7;
        Locale language8;
        Locale language9;
        Locale language10;
        Locale language11;
        Locale language12;
        Locale language13;
        Locale language14;
        Locale language15;
        switch (view.getId()) {
            case C0549R.id.instructions_language_bulgarian_iv /* 2131296420 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 5 && (language = AppUtils.getLanguage(5)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 5);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_croatian_iv /* 2131296421 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 9 && (language2 = AppUtils.getLanguage(9)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language2);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 9);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_czech_iv /* 2131296422 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 8 && (language3 = AppUtils.getLanguage(8)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language3);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 8);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_dutch_iv /* 2131296423 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 7 && (language4 = AppUtils.getLanguage(7)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language4);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 7);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_english_iv /* 2131296424 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 0 && (language5 = AppUtils.getLanguage(0)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language5);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 0);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_french_iv /* 2131296425 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 1 && (language6 = AppUtils.getLanguage(1)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language6);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 1);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_german_iv /* 2131296426 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 2 && (language7 = AppUtils.getLanguage(2)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language7);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 2);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_greek_iv /* 2131296427 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 13 && (language8 = AppUtils.getLanguage(13)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language8);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 13);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_italian_iv /* 2131296428 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 14 && (language9 = AppUtils.getLanguage(14)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language9);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 14);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_polish_iv /* 2131296430 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 6 && (language10 = AppUtils.getLanguage(6)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language10);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 6);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_portuguese_iv /* 2131296431 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 4 && (language11 = AppUtils.getLanguage(4)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language11);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 4);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_romanian_iv /* 2131296432 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 10 && (language12 = AppUtils.getLanguage(10)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language12);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 10);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_slovak_iv /* 2131296433 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 11 && (language13 = AppUtils.getLanguage(11)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language13);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 11);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_slovenian_iv /* 2131296434 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 12 && (language14 = AppUtils.getLanguage(12)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language14);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 12);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
            case C0549R.id.instructions_language_spanish_iv /* 2131296435 */:
                if (PreferencesHelper.getSharedPreferences(getActivity().getApplicationContext()).getInt(IConstants.KEY_LANGUAGE_FLAG, LocalUtil.getLocaleLanguage(getActivity())) != 3 && (language15 = AppUtils.getLanguage(3)) != null) {
                    AppUtils.setLanguage(getActivity().getApplicationContext(), language15);
                    PreferencesHelper.putIntValue(getActivity().getApplicationContext(), IConstants.KEY_LANGUAGE_FLAG, 3);
                    getActivity().sendBroadcast(new Intent(IActions.ACTION_CHANGE_LANGUAGE));
                    break;
                }
                break;
        }
    }
}
