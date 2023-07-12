package com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.facebook.appevents.AppEventsConstants;
import com.treadly.Treadly.Data.Model.UserComment;
import com.treadly.Treadly.MainActivity;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.ActivityUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class TreadlyPublicStreamMessagingViewController {
    public CommentControllerListener commentControllerListener;
    Context context;
    ImageButton heartAnimatedButton;
    TextView heartLabel;
    public Boolean isHost;
    public boolean isStreamer;
    private TreadlyPublicStreamMessagingViewAdapter messageAdapter;
    private RecyclerView messageRecyclerView;
    private List<UserComment> messages = new ArrayList();
    private View parentView;
    public boolean readOnly;
    Button sendMessageButton;
    AppCompatEditText viewerTextField;

    /* loaded from: classes2.dex */
    public interface CommentControllerListener {
        void didEndTyping();

        void didPressLike();

        void didPressLikeCount();

        void didSendMessage(String str);

        void didStartTyping();
    }

    public TreadlyPublicStreamMessagingViewController(Context context, View view, Boolean bool) {
        this.context = context;
        this.parentView = view;
        this.isHost = bool;
        initViews();
    }

    private void initViews() {
        initRecycler();
        if (this.isHost != null && !this.isHost.booleanValue()) {
            initTextField();
            this.heartLabel = (TextView) this.parentView.findViewById(R.id.viewer_public_broadcast_heart_label);
            this.heartAnimatedButton = (ImageButton) this.parentView.findViewById(R.id.viewer_public_broadcast_heart);
        } else if (this.isHost != null && this.isHost.booleanValue()) {
            this.heartLabel = (TextView) this.parentView.findViewById(R.id.host_public_broadcast_heart_label);
            this.heartAnimatedButton = (ImageButton) this.parentView.findViewById(R.id.host_public_broadcast_heart);
        } else {
            this.heartLabel = (TextView) this.parentView.findViewById(R.id.discover_video_heart_label);
            this.heartAnimatedButton = (ImageButton) this.parentView.findViewById(R.id.discover_video_heart);
        }
        this.heartLabel.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
        this.heartLabel.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyPublicStreamMessagingViewController.this.commentControllerListener.didPressLikeCount();
            }
        });
        this.heartAnimatedButton.setBackgroundResource(R.drawable.video_heart_icon);
        this.heartAnimatedButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                TreadlyPublicStreamMessagingViewController.this.commentControllerListener.didPressLike();
            }
        });
    }

    private void initTextField() {
        this.viewerTextField = (AppCompatEditText) this.parentView.findViewById(R.id.viewer_public_broadcast_text_field);
        this.viewerTextField.addTextChangedListener(new TextWatcher() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.3
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                TreadlyPublicStreamMessagingViewController.this.commentControllerListener.didStartTyping();
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                TreadlyPublicStreamMessagingViewController.this.commentControllerListener.didEndTyping();
            }
        });
        this.sendMessageButton = (Button) this.parentView.findViewById(R.id.viewer_public_broadcast_send_comment_button);
        this.sendMessageButton.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                String obj = TreadlyPublicStreamMessagingViewController.this.viewerTextField.getText().toString();
                TreadlyPublicStreamMessagingViewController.this.viewerTextField.setText("");
                TreadlyPublicStreamMessagingViewController.this.commentControllerListener.didSendMessage(obj);
                ((InputMethodManager) ((MainActivity) TreadlyPublicStreamMessagingViewController.this.context).getSystemService("input_method")).hideSoftInputFromWindow(TreadlyPublicStreamMessagingViewController.this.viewerTextField.getWindowToken(), 0);
            }
        });
    }

    private void initRecycler() {
        if (this.isHost == null) {
            this.messageRecyclerView = (RecyclerView) this.parentView.findViewById(R.id.discover_video_comment);
        } else if (this.isHost.booleanValue()) {
            this.messageRecyclerView = (RecyclerView) this.parentView.findViewById(R.id.host_public_broadcast_comment_section);
        } else {
            this.messageRecyclerView = (RecyclerView) this.parentView.findViewById(R.id.viewer_public_broadcast_comment_section);
        }
        this.messageAdapter = new TreadlyPublicStreamMessagingViewAdapter(this.context, this.messages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        linearLayoutManager.setOrientation(1);
        this.messageRecyclerView.setLayoutManager(linearLayoutManager);
        this.messageRecyclerView.setAdapter(this.messageAdapter);
    }

    public void addMessage(UserComment userComment) {
        this.messages.add(userComment);
        this.messageAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    public void setLikes(int i) {
        if (i == -1) {
            this.heartLabel.setText(AppEventsConstants.EVENT_PARAM_VALUE_NO);
        } else {
            this.heartLabel.setText(String.format(Locale.getDefault(), "%,d", Integer.valueOf(i)));
        }
    }

    public void setIsLiked(boolean z, boolean z2) {
        if (z) {
            this.heartAnimatedButton.setBackgroundResource(R.drawable.video_heart_icon_selected);
        } else {
            this.heartAnimatedButton.setBackgroundResource(R.drawable.video_heart_icon);
        }
    }

    public void initializeMessages(List<UserComment> list) {
        this.messages = list;
        this.messageAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    private void scrollToBottom() {
        if (this.messages.isEmpty()) {
            return;
        }
        ActivityUtil.runOnUiThread((MainActivity) this.context, new Runnable() { // from class: com.treadly.Treadly.UI.TreadlyVideo.PublicStream.PublicStreamMessagingView.TreadlyPublicStreamMessagingViewController.5
            @Override // java.lang.Runnable
            public void run() {
                TreadlyPublicStreamMessagingViewController.this.messageRecyclerView.scrollToPosition(TreadlyPublicStreamMessagingViewController.this.messageAdapter.getItemCount() - 1);
            }
        });
    }

    public void clearMessages() {
        this.messages.clear();
        this.messageAdapter.notifyDataSetChanged();
        scrollToBottom();
    }
}
