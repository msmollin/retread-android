package com.treadly.Treadly.UI.TreadlyProfile.Settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.TreadlyProfileSettingsListController;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsListAdapter extends BaseExpandableListAdapter {
    private Typeface bold;
    private Context context;
    protected TreadlyProfileSettingsListController controller;
    protected TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener listener;
    private Typeface regular;
    protected List<TreadlyProfileSettingsListController.GeneralSectionTypes> generalSectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsListController.AppSectionTypes> appSectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsListController.SettingsSectionTypes> settingsSectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsListController.AboutSectionTypes> aboutSectionTypes = new ArrayList();
    protected List<TreadlyProfileSettingsListController.SettingsSections> sectionTypes = new ArrayList();
    private String appVersion = "1.1.8";

    @Override // android.widget.BaseExpandableListAdapter, android.widget.ExpandableListAdapter
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getChildId(int i, int i2) {
        return i2;
    }

    @Override // android.widget.ExpandableListAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean hasStableIds() {
        return true;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TreadlyProfileSettingsListAdapter(Context context, TreadlyProfileSettingsListController.TreadlyProfileSettingsEventListener treadlyProfileSettingsEventListener, TreadlyProfileSettingsListController treadlyProfileSettingsListController) {
        this.context = context;
        this.listener = treadlyProfileSettingsEventListener;
        this.controller = treadlyProfileSettingsListController;
        this.bold = context.getResources().getFont(R.font.montserrat_bold);
        this.regular = context.getResources().getFont(R.font.montserrat_light);
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        this.controller.updateSections();
        return this.sectionTypes.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        TreadlyProfileSettingsListController.SettingsSections settingsSections = this.sectionTypes.get(i);
        if (settingsSections == null) {
            return 0;
        }
        switch (settingsSections) {
            case general:
                this.controller.updateGeneralSectionTypes();
                return this.generalSectionTypes.size();
            case app:
                this.controller.updateAppSectionTypes();
                return this.appSectionTypes.size();
            case settings:
                this.controller.updateSettingsSectionTypes();
                return this.settingsSectionTypes.size();
            case about:
                this.controller.updateAboutSectionTypes();
                return this.aboutSectionTypes.size();
            case logout:
                return 1;
            default:
                return 0;
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        return this.sectionTypes.get(i);
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        TreadlyProfileSettingsListController.SettingsSections settingsSections = this.sectionTypes.get(i);
        if (settingsSections == null) {
            return null;
        }
        switch (settingsSections) {
            case general:
                return this.generalSectionTypes.get(i2);
            case app:
                return this.appSectionTypes.get(i2);
            case settings:
                return this.settingsSectionTypes.get(i2);
            case about:
                return this.aboutSectionTypes.get(i2);
            case logout:
                return 1;
            default:
                return null;
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_header, viewGroup, false);
            DefaultHeaderViewHolder defaultHeaderViewHolder = new DefaultHeaderViewHolder();
            defaultHeaderViewHolder.textView = (TextView) view.findViewById(R.id.header_text_view);
            defaultHeaderViewHolder.subTextView = (TextView) view.findViewById(R.id.sub_header_text_view);
            defaultHeaderViewHolder.divider = view.findViewById(R.id.settings_header_divider);
            view.setTag(defaultHeaderViewHolder);
        }
        DefaultHeaderViewHolder defaultHeaderViewHolder2 = (DefaultHeaderViewHolder) view.getTag();
        defaultHeaderViewHolder2.divider.setVisibility(0);
        ((ExpandableListView) viewGroup).expandGroup(i);
        view.setOnClickListener(null);
        TreadlyProfileSettingsListController.SettingsSections settingsSections = this.sectionTypes.get(i);
        if (settingsSections == null) {
            return view;
        }
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultHeaderViewHolder2.textView.getLayoutParams();
        layoutParams.topMargin = 14;
        switch (settingsSections) {
            case general:
                defaultHeaderViewHolder2.textView.setText(R.string.app_info_title);
                defaultHeaderViewHolder2.textView.setVisibility(0);
                defaultHeaderViewHolder2.subTextView.setVisibility(8);
                layoutParams.topMargin = 6;
                break;
            case app:
                defaultHeaderViewHolder2.textView.setText(R.string.app_title);
                defaultHeaderViewHolder2.textView.setVisibility(0);
                defaultHeaderViewHolder2.subTextView.setText(this.appVersion);
                defaultHeaderViewHolder2.subTextView.setVisibility(0);
                break;
            case settings:
                defaultHeaderViewHolder2.textView.setText(R.string.treadly_title);
                defaultHeaderViewHolder2.textView.setVisibility(this.settingsSectionTypes.size() <= 0 ? 8 : 0);
                defaultHeaderViewHolder2.subTextView.setVisibility(8);
                break;
            default:
                defaultHeaderViewHolder2.textView.setVisibility(4);
                defaultHeaderViewHolder2.subTextView.setVisibility(4);
                defaultHeaderViewHolder2.divider.setVisibility(4);
                break;
        }
        defaultHeaderViewHolder2.textView.setLayoutParams(layoutParams);
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        TreadlyProfileSettingsListController.SettingsSections settingsSections = this.sectionTypes.get(i);
        if (settingsSections == null) {
            return null;
        }
        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
            if (layoutInflater == null) {
                return null;
            }
            view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_default_row, viewGroup, false);
        }
        switch (settingsSections) {
            case general:
                return generalSectionChildView(i, i2, view);
            case app:
                return appSectionChildView(i, i2, view);
            case settings:
                return settingsSectionChildView(i, i2, view);
            case about:
                return aboutSectionChildView(i, i2, view);
            case logout:
                return logoutSectionChildView(i, i2, view);
            default:
                return null;
        }
    }

    private View generalSectionChildView(int i, int i2, View view) {
        if (view == null) {
            return null;
        }
        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) view.getTag();
        if (defaultViewHolder == null) {
            defaultViewHolder = new DefaultViewHolder();
            defaultViewHolder.textView = (TextView) view.findViewById(R.id.settings_text_view);
            defaultViewHolder.divider = view.findViewById(R.id.settings_divider);
            defaultViewHolder.subTextView = (TextView) view.findViewById(R.id.settings_subtext_view);
            view.setTag(defaultViewHolder);
        }
        defaultViewHolder.divider.setVisibility(0);
        defaultViewHolder.subTextView.setVisibility(4);
        defaultViewHolder.textView.setTypeface(this.regular);
        defaultViewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.white, null));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultViewHolder.textView.getLayoutParams();
        TreadlyProfileSettingsListController.GeneralSectionTypes generalSectionTypes = this.generalSectionTypes.get(i2);
        if (generalSectionTypes == null) {
            return view;
        }
        switch (generalSectionTypes) {
            case systemVersion:
                defaultViewHolder.textView.setText(R.string.about_title);
                break;
            case pushNotifications:
                defaultViewHolder.textView.setText(R.string.notifications_title);
                break;
            case changePassword:
                defaultViewHolder.textView.setText(R.string.change_password_title);
                break;
            case changeUsername:
                defaultViewHolder.textView.setText(R.string.change_username_title);
                break;
        }
        layoutParams.width = -2;
        defaultViewHolder.textView.setLayoutParams(layoutParams);
        return view;
    }

    /* loaded from: classes2.dex */
    private class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String str) {
            super(str);
        }

        @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }
    }

    private void stripUnderlines(TextView textView) {
        URLSpan[] uRLSpanArr;
        SpannableString spannableString = new SpannableString(textView.getText());
        for (URLSpan uRLSpan : (URLSpan[]) spannableString.getSpans(0, spannableString.length(), URLSpan.class)) {
            int spanStart = spannableString.getSpanStart(uRLSpan);
            int spanEnd = spannableString.getSpanEnd(uRLSpan);
            spannableString.removeSpan(uRLSpan);
            spannableString.setSpan(new URLSpanNoUnderline(uRLSpan.getURL()), spanStart, spanEnd, 0);
        }
        textView.setText(spannableString);
    }

    private View appSectionChildView(int i, int i2, View view) {
        if (view == null) {
            return null;
        }
        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) view.getTag();
        if (defaultViewHolder == null) {
            defaultViewHolder = new DefaultViewHolder();
            defaultViewHolder.textView = (TextView) view.findViewById(R.id.settings_text_view);
            defaultViewHolder.divider = view.findViewById(R.id.settings_divider);
            defaultViewHolder.subTextView = (TextView) view.findViewById(R.id.settings_subtext_view);
            view.setTag(defaultViewHolder);
        }
        defaultViewHolder.divider.setVisibility(0);
        defaultViewHolder.subTextView.setVisibility(4);
        defaultViewHolder.textView.setTypeface(this.regular);
        defaultViewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.white, null));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultViewHolder.textView.getLayoutParams();
        TreadlyProfileSettingsListController.AppSectionTypes appSectionTypes = this.appSectionTypes.get(i2);
        if (appSectionTypes == null) {
            return view;
        }
        switch (appSectionTypes) {
            case appSettings:
                defaultViewHolder.textView.setText(R.string.profile_settings_title);
                break;
            case faq:
                defaultViewHolder.textView.setText(R.string.faq_title);
                break;
            case whatsNew:
                defaultViewHolder.textView.setText(R.string.whats_new_title);
                break;
        }
        layoutParams.width = -2;
        defaultViewHolder.textView.setLayoutParams(layoutParams);
        return view;
    }

    @SuppressLint({"SetTextI18n"})
    private View settingsSectionChildView(int i, int i2, View view) {
        if (view == null) {
            return null;
        }
        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) view.getTag();
        if (defaultViewHolder == null) {
            defaultViewHolder = new DefaultViewHolder();
            defaultViewHolder.textView = (TextView) view.findViewById(R.id.settings_text_view);
            defaultViewHolder.divider = view.findViewById(R.id.settings_divider);
            defaultViewHolder.subTextView = (TextView) view.findViewById(R.id.settings_subtext_view);
            view.setTag(defaultViewHolder);
        }
        defaultViewHolder.divider.setVisibility(0);
        defaultViewHolder.subTextView.setVisibility(4);
        defaultViewHolder.textView.setTypeface(this.regular);
        defaultViewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.white, null));
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultViewHolder.textView.getLayoutParams();
        TreadlyProfileSettingsListController.SettingsSectionTypes settingsSectionTypes = this.settingsSectionTypes.get(i2);
        if (settingsSectionTypes == null) {
            return view;
        }
        switch (settingsSectionTypes) {
            case speakerPassword:
                defaultViewHolder.textView.setText(R.string.speaker_password_title);
                break;
            case checkUpdates:
                defaultViewHolder.textView.setText(R.string.treadmill_updates_title);
                break;
            case pairingModeType:
                defaultViewHolder.textView.setText(R.string.pairing_mode_title);
                break;
            case maintenance:
                defaultViewHolder.textView.setText(R.string.maintenance_title);
                break;
            case pairedPhones:
                defaultViewHolder.textView.setText(R.string.paired_phones_title);
                break;
            case unclaimedList:
                defaultViewHolder.textView.setText(R.string.unclaimed_activities_title);
                break;
            case singleUserMode:
                defaultViewHolder.textView.setText(R.string.single_user_mode_title);
                break;
            case updateWifiSettingsConnected:
                defaultViewHolder.textView.setText(R.string.wifi_setup_title);
                defaultViewHolder.subTextView.setVisibility(0);
                defaultViewHolder.subTextView.setText(R.string.connected);
                defaultViewHolder.subTextView.setTextColor(this.context.getResources().getColor(R.color.green_1, null));
                defaultViewHolder.subTextView.setAllCaps(true);
                break;
            case updateWifiSettingsNotConnected:
                defaultViewHolder.textView.setText(R.string.wifi_setup_title);
                defaultViewHolder.subTextView.setVisibility(0);
                defaultViewHolder.subTextView.setText(R.string.disconnected);
                defaultViewHolder.subTextView.setTextColor(this.context.getResources().getColor(R.color.red_1, null));
                defaultViewHolder.subTextView.setAllCaps(true);
                break;
            case getUpdateWifiSettingUnknown:
                defaultViewHolder.textView.setText(R.string.wifi_setup_title);
                defaultViewHolder.subTextView.setVisibility(0);
                defaultViewHolder.subTextView.setText(R.string.wifi_connecting);
                defaultViewHolder.subTextView.setTextColor(this.context.getResources().getColor(R.color.orange_1, null));
                defaultViewHolder.subTextView.setAllCaps(true);
                break;
        }
        layoutParams.width = -2;
        defaultViewHolder.textView.setLayoutParams(layoutParams);
        return view;
    }

    private View aboutSectionChildView(int i, int i2, View view) {
        if (view == null) {
            return null;
        }
        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) view.getTag();
        if (defaultViewHolder == null) {
            defaultViewHolder = new DefaultViewHolder();
            defaultViewHolder.textView = (TextView) view.findViewById(R.id.settings_text_view);
            defaultViewHolder.divider = view.findViewById(R.id.settings_divider);
            defaultViewHolder.subTextView = (TextView) view.findViewById(R.id.settings_subtext_view);
            view.setTag(defaultViewHolder);
        }
        defaultViewHolder.divider.setVisibility(4);
        defaultViewHolder.subTextView.setVisibility(4);
        defaultViewHolder.textView.setTypeface(this.regular);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultViewHolder.textView.getLayoutParams();
        TreadlyProfileSettingsListController.AboutSectionTypes aboutSectionTypes = this.aboutSectionTypes.get(i2);
        if (aboutSectionTypes == null) {
            return view;
        }
        switch (aboutSectionTypes) {
            case termsAndCondition:
                defaultViewHolder.textView.setText(R.string.terms_and_conditions_title_capitalized);
                defaultViewHolder.textView.setTextColor(ContextCompat.getColor(this.context, R.color.terms_conditions));
                break;
            case privacyPolicy:
                defaultViewHolder.textView.setText(R.string.privacy_policy_title_capitalized);
                defaultViewHolder.textView.setTextColor(ContextCompat.getColor(this.context, R.color.privacy_policy));
                break;
            case supportCenter:
                defaultViewHolder.textView.setText(R.string.support_center_title_capitalized);
                defaultViewHolder.textView.setTextColor(ContextCompat.getColor(this.context, R.color.terms_conditions));
                break;
        }
        layoutParams.width = -2;
        defaultViewHolder.textView.setLayoutParams(layoutParams);
        return view;
    }

    private View logoutSectionChildView(int i, int i2, View view) {
        if (view == null) {
            return null;
        }
        DefaultViewHolder defaultViewHolder = (DefaultViewHolder) view.getTag();
        if (defaultViewHolder == null) {
            defaultViewHolder = new DefaultViewHolder();
            defaultViewHolder.textView = (TextView) view.findViewById(R.id.settings_text_view);
            defaultViewHolder.divider = view.findViewById(R.id.settings_divider);
            defaultViewHolder.subTextView = (TextView) view.findViewById(R.id.settings_subtext_view);
            view.setTag(defaultViewHolder);
        }
        defaultViewHolder.divider.setVisibility(4);
        defaultViewHolder.subTextView.setVisibility(4);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultViewHolder.textView.getLayoutParams();
        if (i2 == 0) {
            defaultViewHolder.textView.setText(R.string.log_out_title_capitalized);
            defaultViewHolder.textView.setTypeface(this.bold);
            defaultViewHolder.textView.setTextColor(this.context.getResources().getColor(R.color.logout_color, null));
        }
        layoutParams.width = -2;
        defaultViewHolder.textView.setLayoutParams(layoutParams);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void didSelectItem(int i, int i2) {
        TreadlyProfileSettingsListController.SettingsSections settingsSections;
        if (this.listener == null || (settingsSections = this.sectionTypes.get(i)) == null) {
            return;
        }
        if (settingsSections == TreadlyProfileSettingsListController.SettingsSections.general) {
            TreadlyProfileSettingsListController.GeneralSectionTypes generalSectionTypes = this.generalSectionTypes.get(i2);
            if (generalSectionTypes == null) {
                return;
            }
            int i3 = AnonymousClass1.$SwitchMap$com$treadly$Treadly$UI$TreadlyProfile$Settings$TreadlyProfileSettingsListController$GeneralSectionTypes[generalSectionTypes.ordinal()];
            if (i3 != 7) {
                switch (i3) {
                    case 1:
                        this.listener.didPressSystemVersion();
                        return;
                    case 2:
                        this.listener.didPressGeneralRow(generalSectionTypes);
                        return;
                    case 3:
                        this.listener.didPressChangePassword();
                        return;
                    case 4:
                        this.listener.didPressChangeUsername();
                        return;
                    default:
                        return;
                }
            }
            this.listener.didPressPrivateVideoCompositionOptions();
        } else if (settingsSections == TreadlyProfileSettingsListController.SettingsSections.about) {
            if (i2 >= this.aboutSectionTypes.size()) {
                return;
            }
            this.listener.didPressAboutRow(this.aboutSectionTypes.get(i2));
        } else if (settingsSections == TreadlyProfileSettingsListController.SettingsSections.app) {
            if (i2 >= this.appSectionTypes.size()) {
                return;
            }
            this.listener.didPressAppRow(this.appSectionTypes.get(i2));
        } else if (settingsSections == TreadlyProfileSettingsListController.SettingsSections.settings) {
            TreadlyProfileSettingsListController.SettingsSectionTypes settingsSectionTypes = this.settingsSectionTypes.get(i2);
            if (settingsSectionTypes == null) {
                return;
            }
            switch (settingsSectionTypes) {
                case speakerPassword:
                    this.listener.didPressSpeakerPassword();
                    return;
                case checkUpdates:
                    this.listener.didPressCheckOta();
                    return;
                case pairingModeType:
                    this.listener.didPressPairingModeType(false);
                    return;
                case maintenance:
                    this.listener.didPressMaintenance();
                    return;
                case pairedPhones:
                    this.listener.didPressPairedPhones();
                    return;
                case unclaimedList:
                    this.listener.didPressUnclaimedList();
                    return;
                case singleUserMode:
                    this.listener.didPressSingleUserMode();
                    return;
                case updateWifiSettingsConnected:
                case updateWifiSettingsNotConnected:
                case getUpdateWifiSettingUnknown:
                    this.listener.didPressUpdateWifi();
                    return;
                case idlePauseConfig:
                    this.listener.didPressIdleConfig();
                    return;
                default:
                    this.listener.didPressSettingsRow(settingsSectionTypes);
                    return;
            }
        } else if (settingsSections == TreadlyProfileSettingsListController.SettingsSections.logout) {
            switch (i2) {
                case 0:
                    this.listener.didPressLogout();
                    return;
                case 1:
                    this.listener.didPressOldSettings();
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class DefaultViewHolder {
        View divider;
        TextView subTextView;
        TextView textView;

        private DefaultViewHolder() {
        }
    }

    /* loaded from: classes2.dex */
    private static class DefaultHeaderViewHolder {
        View divider;
        TextView subTextView;
        TextView textView;

        private DefaultHeaderViewHolder() {
        }
    }
}
