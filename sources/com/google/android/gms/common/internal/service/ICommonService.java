package com.google.android.gms.common.internal.service;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.internal.service.ICommonCallbacks;

/* loaded from: classes.dex */
public interface ICommonService extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends com.google.android.gms.internal.stable.zzb implements ICommonService {

        /* loaded from: classes.dex */
        public static class Proxy extends com.google.android.gms.internal.stable.zza implements ICommonService {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.common.internal.service.ICommonService");
            }

            @Override // com.google.android.gms.common.internal.service.ICommonService
            public void clearDefaultAccount(ICommonCallbacks iCommonCallbacks) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iCommonCallbacks);
                transactOneway(1, obtainAndWriteInterfaceToken);
            }
        }

        public Stub() {
            super("com.google.android.gms.common.internal.service.ICommonService");
        }

        public static ICommonService asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.service.ICommonService");
            return queryLocalInterface instanceof ICommonService ? (ICommonService) queryLocalInterface : new Proxy(iBinder);
        }

        @Override // com.google.android.gms.internal.stable.zzb
        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1) {
                clearDefaultAccount(ICommonCallbacks.Stub.asInterface(parcel.readStrongBinder()));
                return true;
            }
            return false;
        }
    }

    void clearDefaultAccount(ICommonCallbacks iCommonCallbacks) throws RemoteException;
}
