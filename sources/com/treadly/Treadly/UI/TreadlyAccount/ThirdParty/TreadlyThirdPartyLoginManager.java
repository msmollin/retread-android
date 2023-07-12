package com.treadly.Treadly.UI.TreadlyAccount.ThirdParty;

import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter;
import com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener;
import com.treadly.Treadly.Data.Managers.TreadlyServiceManager;
import com.treadly.Treadly.Data.Model.UserTokenInfo;

/* loaded from: classes2.dex */
public class TreadlyThirdPartyLoginManager {
    private void signUpFacebookUser(String str, TreadlyServiceResponseEventListener treadlyServiceResponseEventListener) {
        TreadlyServiceManager.getInstance().authenticateWithFacebook(str, new TreadlyServiceResponseEventAdapter() { // from class: com.treadly.Treadly.UI.TreadlyAccount.ThirdParty.TreadlyThirdPartyLoginManager.1
            @Override // com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventAdapter, com.treadly.Treadly.Data.Listeners.TreadlyServiceResponseEventListener
            public void onUserTokenInfoProfile(String str2, UserTokenInfo userTokenInfo, boolean z, boolean z2) {
            }
        });
    }
}
