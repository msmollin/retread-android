package com.google.android.gms.signin.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.internal.stable.zza;
import com.google.android.gms.internal.stable.zzb;
import com.google.android.gms.internal.stable.zzc;
import com.google.android.gms.signin.internal.ISignInService;
import java.util.List;

/* loaded from: classes.dex */
public interface IOfflineAccessCallbacks extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends zzb implements IOfflineAccessCallbacks {

        /* loaded from: classes.dex */
        public static class Proxy extends zza implements IOfflineAccessCallbacks {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
            }

            @Override // com.google.android.gms.signin.internal.IOfflineAccessCallbacks
            public void checkServerAuthorization(String str, List<Scope> list, ISignInService iSignInService) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeTypedList(list);
                zzc.zza(obtainAndWriteInterfaceToken, iSignInService);
                transactAndReadExceptionReturnVoid(2, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.signin.internal.IOfflineAccessCallbacks
            public void uploadServerAuthCode(String str, String str2, ISignInService iSignInService) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                obtainAndWriteInterfaceToken.writeString(str2);
                zzc.zza(obtainAndWriteInterfaceToken, iSignInService);
                transactAndReadExceptionReturnVoid(3, obtainAndWriteInterfaceToken);
            }
        }

        public Stub() {
            super("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
        }

        public static IOfflineAccessCallbacks asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.signin.internal.IOfflineAccessCallbacks");
            return queryLocalInterface instanceof IOfflineAccessCallbacks ? (IOfflineAccessCallbacks) queryLocalInterface : new Proxy(iBinder);
        }

        @Override // com.google.android.gms.internal.stable.zzb
        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            switch (i) {
                case 2:
                    checkServerAuthorization(parcel.readString(), parcel.createTypedArrayList(Scope.CREATOR), ISignInService.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                case 3:
                    uploadServerAuthCode(parcel.readString(), parcel.readString(), ISignInService.Stub.asInterface(parcel.readStrongBinder()));
                    break;
                default:
                    return false;
            }
            parcel2.writeNoException();
            return true;
        }
    }

    void checkServerAuthorization(String str, List<Scope> list, ISignInService iSignInService) throws RemoteException;

    void uploadServerAuthCode(String str, String str2, ISignInService iSignInService) throws RemoteException;
}
