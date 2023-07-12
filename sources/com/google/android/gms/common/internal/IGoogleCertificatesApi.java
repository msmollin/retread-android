package com.google.android.gms.common.internal;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.common.GoogleCertificatesQuery;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public interface IGoogleCertificatesApi extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends com.google.android.gms.internal.stable.zzb implements IGoogleCertificatesApi {

        /* loaded from: classes.dex */
        public static class Proxy extends com.google.android.gms.internal.stable.zza implements IGoogleCertificatesApi {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
            }

            @Override // com.google.android.gms.common.internal.IGoogleCertificatesApi
            public IObjectWrapper getGoogleCertificates() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(1, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.common.internal.IGoogleCertificatesApi
            public IObjectWrapper getGoogleReleaseCertificates() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.common.internal.IGoogleCertificatesApi
            public boolean isGoogleOrPlatformSigned(GoogleCertificatesQuery googleCertificatesQuery, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, googleCertificatesQuery);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken);
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.common.internal.IGoogleCertificatesApi
            public boolean isGoogleReleaseSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken);
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.common.internal.IGoogleCertificatesApi
            public boolean isGoogleSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                obtainAndWriteInterfaceToken.writeString(str);
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iObjectWrapper);
                Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken);
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }
        }

        public Stub() {
            super("com.google.android.gms.common.internal.IGoogleCertificatesApi");
        }

        public static IGoogleCertificatesApi asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGoogleCertificatesApi");
            return queryLocalInterface instanceof IGoogleCertificatesApi ? (IGoogleCertificatesApi) queryLocalInterface : new Proxy(iBinder);
        }

        @Override // com.google.android.gms.internal.stable.zzb
        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IObjectWrapper googleCertificates;
            boolean isGoogleReleaseSigned;
            switch (i) {
                case 1:
                    googleCertificates = getGoogleCertificates();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, googleCertificates);
                    return true;
                case 2:
                    googleCertificates = getGoogleReleaseCertificates();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, googleCertificates);
                    return true;
                case 3:
                    isGoogleReleaseSigned = isGoogleReleaseSigned(parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, isGoogleReleaseSigned);
                    return true;
                case 4:
                    isGoogleReleaseSigned = isGoogleSigned(parcel.readString(), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, isGoogleReleaseSigned);
                    return true;
                case 5:
                    isGoogleReleaseSigned = isGoogleOrPlatformSigned((GoogleCertificatesQuery) com.google.android.gms.internal.stable.zzc.zza(parcel, GoogleCertificatesQuery.CREATOR), IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, isGoogleReleaseSigned);
                    return true;
                default:
                    return false;
            }
        }
    }

    IObjectWrapper getGoogleCertificates() throws RemoteException;

    IObjectWrapper getGoogleReleaseCertificates() throws RemoteException;

    boolean isGoogleOrPlatformSigned(GoogleCertificatesQuery googleCertificatesQuery, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean isGoogleReleaseSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException;

    boolean isGoogleSigned(String str, IObjectWrapper iObjectWrapper) throws RemoteException;
}
