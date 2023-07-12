package com.treadly.Treadly.UI.TreadlyProfile.Settings.About;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.Settings.About.TreadlyProfileSettingsAboutFragment;
import com.treadly.Treadly.UI.Util.QrHelper;
import com.treadly.client.lib.sdk.Managers.TreadlyLogManager;
import com.treadly.client.lib.sdk.Model.ComponentInfo;
import com.treadly.client.lib.sdk.Model.ComponentType;
import com.treadly.client.lib.sdk.Model.VersionInfo;
import com.treadly.client.lib.sdk.TreadlyClientLib;
import com.yalantis.ucrop.view.CropImageView;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyProfileSettingsAboutListAdapter extends BaseExpandableListAdapter {
    protected List<ComponentInfo> components;
    private Context context;
    protected TreadlyProfileSettingsAboutListAdapterEventListener listener;
    private TreadlyProfileSettingsAboutFragment parent;
    protected List<TreadlyProfileSettingsAboutFragment.AboutViewSections> sectionTypes;

    /* loaded from: classes2.dex */
    protected interface TreadlyProfileSettingsAboutListAdapterEventListener {
    }

    private String getRowTitleForIndex(int i) {
        switch (i) {
            case 0:
                return "Version";
            case 1:
                return "MAC Address";
            case 2:
                return "BLE Version:";
            case 3:
                return "Serial Number:";
            case 4:
                return "Auto Pause:";
            default:
                return "";
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getChild(int i, int i2) {
        return null;
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
        return false;
    }

    public TreadlyProfileSettingsAboutListAdapter(Context context, List<TreadlyProfileSettingsAboutFragment.AboutViewSections> list, List<ComponentInfo> list2, TreadlyProfileSettingsAboutListAdapterEventListener treadlyProfileSettingsAboutListAdapterEventListener, TreadlyProfileSettingsAboutFragment treadlyProfileSettingsAboutFragment) {
        this.context = context;
        this.listener = treadlyProfileSettingsAboutListAdapterEventListener;
        this.sectionTypes = list;
        this.components = list2;
        this.parent = treadlyProfileSettingsAboutFragment;
    }

    @Override // android.widget.ExpandableListAdapter
    public int getGroupCount() {
        this.parent.updateSections();
        return this.sectionTypes.size();
    }

    @Override // android.widget.ExpandableListAdapter
    public int getChildrenCount(int i) {
        ComponentInfo componentInfo;
        if (i >= this.sectionTypes.size()) {
            return 0;
        }
        TreadlyProfileSettingsAboutFragment.AboutViewSections aboutViewSections = this.sectionTypes.get(i);
        if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.appInfo) {
            return TreadlyClientLib.shared.isAdminEnabled() ? 4 : 3;
        } else if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.qrCode) {
            return 1;
        } else {
            if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.deviceInfo) {
                return 5;
            }
            int sectionToDeviceIndex = sectionToDeviceIndex(i);
            return (sectionToDeviceIndex < this.components.size() && (componentInfo = this.components.get(sectionToDeviceIndex)) != null && componentInfo.getType() == ComponentType.bleBoard && hasIdlePauseFeature(componentInfo)) ? 4 : 3;
        }
    }

    @Override // android.widget.ExpandableListAdapter
    public Object getGroup(int i) {
        if (i >= this.sectionTypes.size()) {
            return null;
        }
        return this.sectionTypes.get(i);
    }

    @Override // android.widget.ExpandableListAdapter
    public View getGroupView(int i, boolean z, View view, ViewGroup viewGroup) {
        TreadlyProfileSettingsAboutFragment.AboutViewSections aboutViewSections;
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
            defaultHeaderViewHolder.defaultTopMarginTextView = ((ConstraintLayout.LayoutParams) defaultHeaderViewHolder.textView.getLayoutParams()).topMargin;
            view.setTag(defaultHeaderViewHolder);
        }
        DefaultHeaderViewHolder defaultHeaderViewHolder2 = (DefaultHeaderViewHolder) view.getTag();
        ((ExpandableListView) viewGroup).expandGroup(i);
        if (i < this.sectionTypes.size() && (aboutViewSections = this.sectionTypes.get(i)) != null) {
            defaultHeaderViewHolder2.textView.setVisibility(0);
            defaultHeaderViewHolder2.subTextView.setVisibility(8);
            defaultHeaderViewHolder2.textView.setText(getHeaderTitleForGroup(i));
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) defaultHeaderViewHolder2.textView.getLayoutParams();
            if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.appInfo) {
                layoutParams.topMargin = 0;
                defaultHeaderViewHolder2.textView.setLayoutParams(layoutParams);
            } else {
                layoutParams.topMargin = defaultHeaderViewHolder2.defaultTopMarginTextView;
                defaultHeaderViewHolder2.textView.setLayoutParams(layoutParams);
            }
            if (defaultHeaderViewHolder2.divider != null) {
                ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) defaultHeaderViewHolder2.divider.getLayoutParams();
                if (layoutParams2.leftMargin != 0) {
                    layoutParams2.leftMargin = 0;
                    defaultHeaderViewHolder2.divider.setLayoutParams(layoutParams2);
                }
            }
            return view;
        }
        return view;
    }

    private String getHeaderTitleForGroup(int i) {
        if (i >= this.sectionTypes.size()) {
            return "";
        }
        TreadlyProfileSettingsAboutFragment.AboutViewSections aboutViewSections = this.sectionTypes.get(i);
        if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.appInfo) {
            return this.context.getString(R.string.app_info_title);
        }
        if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.qrCode) {
            return this.context.getString(R.string.qr_code_title);
        }
        ComponentInfo componentInfo = this.components.get(sectionToDeviceIndex(i));
        switch (componentInfo.getType()) {
            case mainBoard:
                return this.context.getString(R.string.main_board);
            case watch:
                return String.format(Locale.getDefault(), "Watch (%s)", componentInfo.getId().toUpperCase());
            case motorController:
                return this.context.getString(R.string.motor_controller_board);
            case remote:
                return String.format(Locale.getDefault(), "Remote (%s)", componentInfo.getId().toUpperCase());
            default:
                return "";
        }
    }

    private int sectionToDeviceIndex(int i) {
        int i2 = i - 1;
        return hasQrCode() ? i2 - 1 : i2;
    }

    private String getRowContentForIndex(int i, ComponentInfo componentInfo) {
        switch (i) {
            case 0:
                return this.components.get(0).getVersionInfo().getVersion();
            case 1:
                return this.components.get(1).getId().toUpperCase();
            case 2:
                return this.components.get(1).getVersionInfo().getVersion();
            case 3:
                return this.parent.getSerialNumber();
            case 4:
                return this.parent.getIdleStatus();
            default:
                return "";
        }
    }

    private Bitmap generateQrCode() {
        String mainboardVersion = this.parent.getMainboardVersion();
        String deviceAddress = this.parent.getDeviceAddress();
        String deviceVersion = this.parent.getDeviceVersion();
        String serialNumber = this.parent.getSerialNumber();
        String handrailPairingString = this.parent.getHandrailPairingString();
        try {
            return QrHelper.textToImage("1.1.8|" + mainboardVersion + "|" + deviceAddress + "|" + deviceVersion + "|" + serialNumber + "|" + handrailPairingString, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION, CropImageView.DEFAULT_IMAGE_TO_CROP_BOUNDS_ANIM_DURATION);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean hasQrCode() {
        for (TreadlyProfileSettingsAboutFragment.AboutViewSections aboutViewSections : this.sectionTypes) {
            if (aboutViewSections != null && aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.qrCode) {
                return true;
            }
        }
        return false;
    }

    @Override // android.widget.ExpandableListAdapter
    public View getChildView(int i, int i2, boolean z, View view, ViewGroup viewGroup) {
        if (i >= this.sectionTypes.size()) {
            return new View(this.context);
        }
        TreadlyProfileSettingsAboutFragment.AboutViewSections aboutViewSections = this.sectionTypes.get(i);
        if (aboutViewSections == null) {
            return null;
        }
        if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.qrCode) {
            if (view == null || !(view.getTag() instanceof DefaultQrCodeHolder)) {
                LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService("layout_inflater");
                if (layoutInflater == null) {
                    return null;
                }
                view = layoutInflater.inflate(R.layout.list_view_treadly_profile_settings_qr_code, viewGroup, false);
                DefaultQrCodeHolder defaultQrCodeHolder = new DefaultQrCodeHolder();
                defaultQrCodeHolder.qrImageView = (ImageView) view.findViewById(R.id.image_view);
                view.setTag(defaultQrCodeHolder);
            }
            ((DefaultQrCodeHolder) view.getTag()).qrImageView.setImageBitmap(generateQrCode());
        } else {
            int i3 = 8;
            if (view == null || !(view.getTag() instanceof DefaultViewHolder)) {
                LayoutInflater layoutInflater2 = (LayoutInflater) this.context.getSystemService("layout_inflater");
                if (layoutInflater2 == null) {
                    return null;
                }
                view = layoutInflater2.inflate(R.layout.list_view_treadly_profile_settings_default_detail_row, viewGroup, false);
                DefaultViewHolder defaultViewHolder = new DefaultViewHolder();
                defaultViewHolder.textView = (TextView) view.findViewById(R.id.header_text_view);
                defaultViewHolder.detailTextView = (TextView) view.findViewById(R.id.detail_text_view);
                defaultViewHolder.detailArrow = (ImageView) view.findViewById(R.id.detail_arrow);
                defaultViewHolder.detailArrow.setVisibility(8);
                defaultViewHolder.divider = view.findViewById(R.id.detail_divider);
                view.setTag(defaultViewHolder);
            }
            DefaultViewHolder defaultViewHolder2 = (DefaultViewHolder) view.getTag();
            if (aboutViewSections == TreadlyProfileSettingsAboutFragment.AboutViewSections.appInfo) {
                if (i2 == 0) {
                    defaultViewHolder2.textView.setText(R.string.app_version_title_field);
                    defaultViewHolder2.detailTextView.setText("1.1.8");
                } else if (i2 == 1) {
                    defaultViewHolder2.textView.setText(R.string.sdk_version_title_field);
                    defaultViewHolder2.detailTextView.setText("1.1.8");
                } else if (i2 == 2) {
                    defaultViewHolder2.textView.setText(R.string.uuid_title_field);
                    defaultViewHolder2.detailTextView.setText(TreadlyLogManager.shared.getUuid().toUpperCase());
                } else if (i2 == 3) {
                    defaultViewHolder2.textView.setText(R.string.admin_settings_title_field);
                    defaultViewHolder2.detailTextView.setText("");
                }
                defaultViewHolder2.textView.setVisibility(0);
                defaultViewHolder2.detailTextView.setVisibility(0);
                defaultViewHolder2.divider.setVisibility(0);
            } else {
                ComponentInfo componentInfo = this.components.get(sectionToDeviceIndex(i));
                if (AnonymousClass1.$SwitchMap$com$treadly$client$lib$sdk$Model$ComponentType[componentInfo.getType().ordinal()] == 3) {
                    defaultViewHolder2.textView.setVisibility((i2 == 0 || i2 == 2) ? 8 : 0);
                    defaultViewHolder2.detailTextView.setVisibility((i2 == 0 || i2 == 2) ? 8 : 0);
                    View view2 = defaultViewHolder2.divider;
                    if (i2 != 0 && i2 != 2) {
                        i3 = 0;
                    }
                    view2.setVisibility(i3);
                } else {
                    defaultViewHolder2.textView.setVisibility(0);
                    defaultViewHolder2.detailTextView.setVisibility(0);
                }
                defaultViewHolder2.textView.setText(getRowTitleForIndex(i2));
                defaultViewHolder2.detailTextView.setText(getRowContentForIndex(i2, componentInfo));
            }
        }
        return view;
    }

    @Override // android.widget.ExpandableListAdapter
    public boolean isChildSelectable(int i, int i2) {
        return i < this.sectionTypes.size() && this.sectionTypes.get(i) == TreadlyProfileSettingsAboutFragment.AboutViewSections.appInfo && i2 == 3;
    }

    /* loaded from: classes2.dex */
    private class DefaultViewHolder {
        ImageView detailArrow;
        TextView detailTextView;
        View divider;
        TextView textView;

        private DefaultViewHolder() {
        }
    }

    /* loaded from: classes2.dex */
    private class DefaultHeaderViewHolder {
        int defaultTopMarginTextView;
        View divider;
        TextView subTextView;
        TextView textView;

        private DefaultHeaderViewHolder() {
        }
    }

    /* loaded from: classes2.dex */
    private class DefaultQrCodeHolder {
        ImageView qrImageView;

        private DefaultQrCodeHolder() {
        }
    }

    private boolean hasIdlePauseFeature(ComponentInfo componentInfo) {
        if (componentInfo == null || componentInfo.getType() != ComponentType.bleBoard) {
            return false;
        }
        return isGreaterThanOrEqual(componentInfo, new VersionInfo(3, 20, 0));
    }

    private boolean isGreaterThanOrEqual(ComponentInfo componentInfo, VersionInfo versionInfo) {
        return (componentInfo == null || versionInfo == null || (!componentInfo.getVersionInfo().isGreaterThan(versionInfo) && !componentInfo.getVersionInfo().isEqual(versionInfo))) ? false : true;
    }
}
