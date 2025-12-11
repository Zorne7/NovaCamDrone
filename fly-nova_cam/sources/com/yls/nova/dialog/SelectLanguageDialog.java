package com.yls.nova.dialog;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.yls.nova.C0549R;
import com.yls.nova.base.BaseDialogFragment;

/* loaded from: classes.dex */
public class SelectLanguageDialog extends BaseDialogFragment implements View.OnClickListener {
    private RadioButton btnBulgarian;
    private RadioButton btnCroatian;
    private RadioButton btnCzech;
    private RadioButton btnDutch;
    private RadioButton btnEnglish;
    private RadioButton btnFrench;
    private RadioButton btnGerman;
    private RadioButton btnGreek;
    private RadioButton btnItalian;
    private RadioButton btnPolish;
    private RadioButton btnPortuguese;
    private RadioButton btnRomanian;
    private RadioButton btnSlovak;
    private RadioButton btnSlovenian;
    private RadioButton btnSpanish;
    private int languageFlag = 0;
    private OnSelectLanguageListener onSelectLanguageListener;

    public interface OnSelectLanguageListener {
        void onSelectLanguage(int i);
    }

    public void setLanguageFlag(int i) {
        this.languageFlag = i;
    }

    public void setOnSelectLanguageListener(OnSelectLanguageListener onSelectLanguageListener) {
        this.onSelectLanguageListener = onSelectLanguageListener;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(C0549R.layout.dialog_select_language, viewGroup, false);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(1);
        }
        this.btnEnglish = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_english);
        this.btnFrench = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_french);
        this.btnGerman = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_german);
        this.btnSpanish = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_spanish);
        this.btnPortuguese = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_portuguese);
        this.btnBulgarian = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_bulgarian);
        this.btnPolish = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_polish);
        this.btnDutch = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_dutch);
        this.btnCzech = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_czech);
        this.btnCroatian = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_croatian);
        this.btnRomanian = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_romanian);
        this.btnSlovak = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_slovak);
        this.btnSlovenian = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_slovenian);
        this.btnGreek = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_greek);
        this.btnItalian = (RadioButton) viewInflate.findViewById(C0549R.id.dialog_select_language_italian);
        this.btnEnglish.setOnClickListener(this);
        this.btnFrench.setOnClickListener(this);
        this.btnGerman.setOnClickListener(this);
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

    @Override // android.app.DialogFragment, android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if (getActivity() == null || getDialog() == null || getDialog().getWindow() == null) {
            return;
        }
        int i = this.languageFlag;
        if (i == 1) {
            this.btnFrench.setChecked(true);
        } else if (i == 2) {
            this.btnGerman.setChecked(true);
        } else if (i == 3) {
            this.btnSpanish.setChecked(true);
        } else if (i == 4) {
            this.btnPortuguese.setChecked(true);
        } else if (i == 5) {
            this.btnBulgarian.setChecked(true);
        } else if (i == 6) {
            this.btnPolish.setChecked(true);
        } else if (i == 7) {
            this.btnDutch.setChecked(true);
        } else if (i == 9) {
            this.btnCroatian.setChecked(true);
        } else if (i == 10) {
            this.btnRomanian.setChecked(true);
        } else if (i == 11) {
            this.btnSlovak.setChecked(true);
        } else if (i == 12) {
            this.btnSlovenian.setChecked(true);
        } else if (i == 13) {
            this.btnGreek.setChecked(true);
        } else if (i == 14) {
            this.btnItalian.setChecked(true);
        } else if (i == 8) {
            this.btnCzech.setChecked(true);
        } else {
            this.btnEnglish.setChecked(true);
        }
        WindowManager.LayoutParams attributes = getDialog().getWindow().getAttributes();
        attributes.width = ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION;
        attributes.height = 350;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        if (getResources().getConfiguration().orientation == 2) {
            attributes.width = (displayMetrics.heightPixels * 3) / 5;
        } else if (getResources().getConfiguration().orientation == 1) {
            attributes.width = (displayMetrics.widthPixels * 3) / 5;
        }
        attributes.height = -2;
        attributes.gravity = 17;
        getDialog().getWindow().setAttributes(attributes);
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setBackgroundDrawableResource(C0549R.drawable.bg_dialog);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == null || getActivity() == null) {
            return;
        }
        switch (view.getId()) {
            case C0549R.id.dialog_select_language_bulgarian /* 2131296374 */:
                OnSelectLanguageListener onSelectLanguageListener = this.onSelectLanguageListener;
                if (onSelectLanguageListener != null) {
                    onSelectLanguageListener.onSelectLanguage(5);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_croatian /* 2131296375 */:
                OnSelectLanguageListener onSelectLanguageListener2 = this.onSelectLanguageListener;
                if (onSelectLanguageListener2 != null) {
                    onSelectLanguageListener2.onSelectLanguage(9);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_czech /* 2131296376 */:
                OnSelectLanguageListener onSelectLanguageListener3 = this.onSelectLanguageListener;
                if (onSelectLanguageListener3 != null) {
                    onSelectLanguageListener3.onSelectLanguage(8);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_dutch /* 2131296377 */:
                OnSelectLanguageListener onSelectLanguageListener4 = this.onSelectLanguageListener;
                if (onSelectLanguageListener4 != null) {
                    onSelectLanguageListener4.onSelectLanguage(7);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_english /* 2131296378 */:
                OnSelectLanguageListener onSelectLanguageListener5 = this.onSelectLanguageListener;
                if (onSelectLanguageListener5 != null) {
                    onSelectLanguageListener5.onSelectLanguage(0);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_french /* 2131296379 */:
                OnSelectLanguageListener onSelectLanguageListener6 = this.onSelectLanguageListener;
                if (onSelectLanguageListener6 != null) {
                    onSelectLanguageListener6.onSelectLanguage(1);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_german /* 2131296380 */:
                OnSelectLanguageListener onSelectLanguageListener7 = this.onSelectLanguageListener;
                if (onSelectLanguageListener7 != null) {
                    onSelectLanguageListener7.onSelectLanguage(2);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_greek /* 2131296381 */:
                OnSelectLanguageListener onSelectLanguageListener8 = this.onSelectLanguageListener;
                if (onSelectLanguageListener8 != null) {
                    onSelectLanguageListener8.onSelectLanguage(13);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_italian /* 2131296382 */:
                OnSelectLanguageListener onSelectLanguageListener9 = this.onSelectLanguageListener;
                if (onSelectLanguageListener9 != null) {
                    onSelectLanguageListener9.onSelectLanguage(14);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_polish /* 2131296383 */:
                OnSelectLanguageListener onSelectLanguageListener10 = this.onSelectLanguageListener;
                if (onSelectLanguageListener10 != null) {
                    onSelectLanguageListener10.onSelectLanguage(6);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_portuguese /* 2131296384 */:
                OnSelectLanguageListener onSelectLanguageListener11 = this.onSelectLanguageListener;
                if (onSelectLanguageListener11 != null) {
                    onSelectLanguageListener11.onSelectLanguage(4);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_romanian /* 2131296385 */:
                OnSelectLanguageListener onSelectLanguageListener12 = this.onSelectLanguageListener;
                if (onSelectLanguageListener12 != null) {
                    onSelectLanguageListener12.onSelectLanguage(10);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_slovak /* 2131296386 */:
                OnSelectLanguageListener onSelectLanguageListener13 = this.onSelectLanguageListener;
                if (onSelectLanguageListener13 != null) {
                    onSelectLanguageListener13.onSelectLanguage(11);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_slovenian /* 2131296387 */:
                OnSelectLanguageListener onSelectLanguageListener14 = this.onSelectLanguageListener;
                if (onSelectLanguageListener14 != null) {
                    onSelectLanguageListener14.onSelectLanguage(12);
                }
                dismiss();
                break;
            case C0549R.id.dialog_select_language_spanish /* 2131296388 */:
                OnSelectLanguageListener onSelectLanguageListener15 = this.onSelectLanguageListener;
                if (onSelectLanguageListener15 != null) {
                    onSelectLanguageListener15.onSelectLanguage(3);
                }
                dismiss();
                break;
        }
    }
}
