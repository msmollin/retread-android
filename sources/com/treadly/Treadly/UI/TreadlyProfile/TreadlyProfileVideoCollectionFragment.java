package com.treadly.Treadly.UI.TreadlyProfile;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.StreamPermission;
import com.treadly.Treadly.Data.Model.UserInfo;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionAdapter;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoInfo;
import com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceVideoType;
import com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class TreadlyProfileVideoCollectionFragment extends Fragment {
    public static final String TAG = "TREADLY_VIDEO_COLLECTION_FRAGMENT";
    public TreadlyProfileVideoCollectionAdapter adapter;
    public boolean isCurrentUser;
    BottomNavigationView navView;
    public UserInfo user;
    public String userId;
    public RecyclerView videoCollectionView;
    public List<VideoServiceVideoInfo> videos = new ArrayList();

    /* JADX INFO: Access modifiers changed from: private */
    public void refresh() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_treadly_profile_video_collection, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        this.adapter = new TreadlyProfileVideoCollectionAdapter(getContext(), this.user);
        this.adapter.setClickListener(new TreadlyProfileVideoCollectionAdapter.ItemClickListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment.1
            @Override // com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionAdapter.ItemClickListener
            public void onVideoSelected(View view2, int i) {
                Uri parse = Uri.parse(TreadlyProfileVideoCollectionFragment.this.adapter.getItem(i).url);
                Uri parse2 = Uri.parse(TreadlyProfileVideoCollectionFragment.this.adapter.getItem(i).thumbnailUrl);
                boolean z = TreadlyProfileVideoCollectionFragment.this.adapter.getItem(i).permission == StreamPermission.friendsStream || TreadlyProfileVideoCollectionFragment.this.adapter.getItem(i).permission == StreamPermission.publicStream;
                TreadlyDiscoverVideoFragment treadlyDiscoverVideoFragment = new TreadlyDiscoverVideoFragment();
                treadlyDiscoverVideoFragment.uri = parse;
                treadlyDiscoverVideoFragment.isCurrentUser = TreadlyProfileVideoCollectionFragment.this.isCurrentUser;
                treadlyDiscoverVideoFragment.thumbnail = parse2;
                treadlyDiscoverVideoFragment.isPublic = z;
                treadlyDiscoverVideoFragment.archiveInfo = TreadlyProfileVideoCollectionFragment.this.adapter.getItem(i);
                treadlyDiscoverVideoFragment.deleteVideoListener = new TreadlyDiscoverVideoFragment.DeleteVideoListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment.1.1
                    @Override // com.treadly.Treadly.UI.TreadlyVideo.TreadlyDiscoverVideoFragment.DeleteVideoListener
                    public void didDeleteVideo() {
                        TreadlyProfileVideoCollectionFragment.this.getActivity().getSupportFragmentManager().popBackStack();
                        TreadlyProfileVideoCollectionFragment.this.refresh();
                    }
                };
                if (TreadlyProfileVideoCollectionFragment.this.getActivity() != null) {
                    TreadlyProfileVideoCollectionFragment.this.getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).add(R.id.activity_fragment_container_empty, treadlyDiscoverVideoFragment, TreadlyDiscoverVideoFragment.TAG).commit();
                }
            }
        });
        this.videoCollectionView = (RecyclerView) view.findViewById(R.id.video_grid_view);
        this.videoCollectionView.setHasFixedSize(true);
        this.videoCollectionView.setNestedScrollingEnabled(false);
        this.videoCollectionView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        this.videoCollectionView.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    private void searchUserVideos() {
        if (this.userId == null) {
            return;
        }
        String str = this.userId;
        if (TreadlyServiceManager.getInstance().getUserId() == null) {
            return;
        }
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId.equals(str)) {
            VideoServiceHelper.getVideoInfoList(userId, null, new VideoServiceHelper.videoInfoResultsListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment.2
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.videoInfoResultsListener
                public void onVideoInfoResponse(String str2, VideoServiceVideoInfo[] videoServiceVideoInfoArr) {
                    TreadlyProfileVideoCollectionFragment.this.videos.clear();
                    if (str2 == null && videoServiceVideoInfoArr != null) {
                        TreadlyProfileVideoCollectionFragment.this.processVideoInfoList(videoServiceVideoInfoArr);
                    }
                    TreadlyProfileVideoCollectionFragment.this.reloadData();
                }
            });
        } else {
            VideoServiceHelper.getVideoInfoList(userId, str, new VideoServiceHelper.videoInfoResultsListener() { // from class: com.treadly.Treadly.UI.TreadlyProfile.TreadlyProfileVideoCollectionFragment.3
                @Override // com.treadly.Treadly.UI.TreadlyVideo.Data.VideoServiceHelper.videoInfoResultsListener
                public void onVideoInfoResponse(String str2, VideoServiceVideoInfo[] videoServiceVideoInfoArr) {
                    TreadlyProfileVideoCollectionFragment.this.videos.clear();
                    if (str2 == null && videoServiceVideoInfoArr != null) {
                        TreadlyProfileVideoCollectionFragment.this.processVideoInfoList(videoServiceVideoInfoArr);
                    }
                    TreadlyProfileVideoCollectionFragment.this.reloadData();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processVideoInfoList(VideoServiceVideoInfo[] videoServiceVideoInfoArr) {
        String userId = TreadlyServiceManager.getInstance().getUserId();
        if (userId == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        for (VideoServiceVideoInfo videoServiceVideoInfo : videoServiceVideoInfoArr) {
            if (videoServiceVideoInfo.thumbnailUrl == null) {
                videoServiceVideoInfo.thumbnailUrl = TreadlyServiceManager.getInstance().getUserInfoById(videoServiceVideoInfo.id).avatarPath;
            }
            VideoServiceVideoType fromVideoServiceVideoType = VideoServiceVideoType.fromVideoServiceVideoType(videoServiceVideoInfo.type);
            if (fromVideoServiceVideoType == null) {
                return;
            }
            if ((fromVideoServiceVideoType != VideoServiceVideoType.live || !userId.equals(videoServiceVideoInfo.userId)) && videoServiceVideoInfo.groupId == null) {
                arrayList.add(videoServiceVideoInfo);
            }
        }
        ArrayList arrayList2 = new ArrayList(arrayList);
        Collections.sort(arrayList2);
        this.videos = arrayList2;
        this.adapter.setVideoInfos(this.videos);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reloadData() {
        this.adapter.notifyDataSetChanged();
    }
}
