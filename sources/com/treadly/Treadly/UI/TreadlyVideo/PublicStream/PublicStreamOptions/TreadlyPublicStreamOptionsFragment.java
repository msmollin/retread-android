package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SwitchCompat;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.WorkoutInfo;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyConnect.TreadlyConnectFragment;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamHost.TreadlyPublicStreamHostFragment;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamOptionsFragment extends BaseFragment {
    static final String TAG = "TreadlyPublicStreamOptionsFragment";
    private EditText descriptionEditText;
    private TextView joinTextView;
    private TextView viewTextView;
    private StreamPermission joinPermission = StreamPermission.publicStream;
    private StreamPermission viewPermission = StreamPermission.publicStream;
    private boolean allowComments = true;

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        hideBottomNavigation();
        return layoutInflater.inflate(R.layout.fragment_treadly_public_stream_options, viewGroup, false);
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.descriptionEditText = (EditText) view.findViewById(R.id.public_stream_options_edit_text);
        this.joinTextView = (TextView) view.findViewById(R.id.public_stream_options_join_permission);
        this.viewTextView = (TextView) view.findViewById(R.id.public_stream_options_view_permission);
        SwitchCompat switchCompat = (SwitchCompat) view.findViewById(R.id.public_stream_options_allow_comments_switch);
        ((Button) view.findViewById(R.id.public_stream_options_start_button)).setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                TreadlyPublicStreamOptionsFragment.this.createWorkout();
            }
        });
        this.joinTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PopupMenu joinPermissionsPopUpMenu = TreadlyPublicStreamOptionsFragment.this.getJoinPermissionsPopUpMenu(TreadlyPublicStreamOptionsFragment.this.joinTextView);
                if (joinPermissionsPopUpMenu != null) {
                    joinPermissionsPopUpMenu.inflate(R.menu.public_stream_options_permission_menu);
                    joinPermissionsPopUpMenu.show();
                }
            }
        });
        this.viewTextView.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PopupMenu viewPermissionsPopUpMenu = TreadlyPublicStreamOptionsFragment.this.getViewPermissionsPopUpMenu(TreadlyPublicStreamOptionsFragment.this.viewTextView);
                if (viewPermissionsPopUpMenu != null) {
                    viewPermissionsPopUpMenu.inflate(R.menu.public_stream_options_permission_menu);
                    viewPermissionsPopUpMenu.show();
                }
            }
        });
        switchCompat.setChecked(true);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.4
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                TreadlyPublicStreamOptionsFragment.this.allowComments = z;
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        hideBottomNavigation();
    }

    @Override // com.treadly.Treadly.UI.Util.BaseClasses.BaseFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        showBottomNavigation();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PopupMenu getJoinPermissionsPopUpMenu(View view) {
        if (getContext() == null) {
            return null;
        }
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.5
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.friends_stream) {
                    TreadlyPublicStreamOptionsFragment.this.joinPermission = StreamPermission.friendsStream;
                    TreadlyPublicStreamOptionsFragment.this.joinTextView.setText(R.string.friends_title);
                    return true;
                } else if (itemId == R.id.private_stream) {
                    TreadlyPublicStreamOptionsFragment.this.joinPermission = StreamPermission.privateStream;
                    TreadlyPublicStreamOptionsFragment.this.joinTextView.setText(R.string.private_title);
                    return true;
                } else if (itemId != R.id.public_stream) {
                    return false;
                } else {
                    TreadlyPublicStreamOptionsFragment.this.joinPermission = StreamPermission.publicStream;
                    TreadlyPublicStreamOptionsFragment.this.joinTextView.setText(R.string.public_title);
                    return true;
                }
            }
        });
        return popupMenu;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public PopupMenu getViewPermissionsPopUpMenu(View view) {
        if (getContext() == null) {
            return null;
        }
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.6
            @Override // androidx.appcompat.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.friends_stream) {
                    TreadlyPublicStreamOptionsFragment.this.viewPermission = StreamPermission.friendsStream;
                    TreadlyPublicStreamOptionsFragment.this.viewTextView.setText(R.string.friends_title);
                    return true;
                } else if (itemId == R.id.private_stream) {
                    TreadlyPublicStreamOptionsFragment.this.viewPermission = StreamPermission.privateStream;
                    TreadlyPublicStreamOptionsFragment.this.viewTextView.setText(R.string.private_title);
                    return true;
                } else if (itemId != R.id.public_stream) {
                    return false;
                } else {
                    TreadlyPublicStreamOptionsFragment.this.viewPermission = StreamPermission.publicStream;
                    TreadlyPublicStreamOptionsFragment.this.viewTextView.setText(R.string.public_title);
                    return true;
                }
            }
        });
        return popupMenu;
    }

    public void createWorkout() {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null || getContext() == null) {
            return;
        }
        WorkoutInfo workoutInfo = new WorkoutInfo(userId, this.descriptionEditText.getText().toString(), this.joinPermission, this.viewPermission, this.allowComments, true);
        showLoading();
        VideoServiceHelper.createWorkoutInfo(workoutInfo, "", new VideoServiceHelper.VideoWorkoutInfoListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamOptions.TreadlyPublicStreamOptionsFragment.7
            @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.VideoWorkoutInfoListener
            public void onResponse(String str, Integer num) {
                TreadlyPublicStreamOptionsFragment.this.dismissLoading();
                if (str == null && num != null) {
                    TreadlyPublicStreamOptionsFragment.this.toPublicStreamHostFragment(num);
                } else {
                    new AlertDialog.Builder(TreadlyPublicStreamOptionsFragment.this.getContext()).setTitle("Error").setMessage("Error setting public stream options").setNeutralButton("Dismiss", (DialogInterface.OnClickListener) null).show();
                }
            }
        });
    }

    public void toPublicStreamHostFragment(Integer num) {
        TreadlyPublicStreamHostFragment treadlyPublicStreamHostFragment = new TreadlyPublicStreamHostFragment();
        treadlyPublicStreamHostFragment.userId = TreadlyServiceManager.getInstance().getUserId();
        treadlyPublicStreamHostFragment.workoutId = num;
        treadlyPublicStreamHostFragment.scheduleId = null;
        treadlyPublicStreamHostFragment.streamPermission = this.joinPermission;
        treadlyPublicStreamHostFragment.allowComments = this.allowComments;
        MainActivity mainActivity = (MainActivity) getContext();
        if (mainActivity == null) {
            return;
        }
        mainActivity.getSupportFragmentManager().popBackStack(TreadlyConnectFragment.TAG, 1);
        mainActivity.getSupportFragmentManager().beginTransaction().addToBackStack(TreadlyConnectFragment.TAG).add(R.id.activity_fragment_container_empty, treadlyPublicStreamHostFragment, TreadlyPublicStreamHostFragment.TAG).commit();
    }
}
