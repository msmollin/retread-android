package com.google.android.gms.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.dynamic.IObjectWrapper;

/* loaded from: classes.dex */
public interface IFragmentWrapper extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends com.google.android.gms.internal.stable.zzb implements IFragmentWrapper {

        /* loaded from: classes.dex */
        public static class Proxy extends com.google.android.gms.internal.stable.zza implements IFragmentWrapper {
            Proxy(IBinder iBinder) {
                super(iBinder, "com.google.android.gms.dynamic.IFragmentWrapper");
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public IObjectWrapper getActivity() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(2, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public Bundle getArguments() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(3, obtainAndWriteInterfaceToken());
                Bundle bundle = (Bundle) com.google.android.gms.internal.stable.zzc.zza(transactAndReadException, Bundle.CREATOR);
                transactAndReadException.recycle();
                return bundle;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public int getId() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(4, obtainAndWriteInterfaceToken());
                int readInt = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return readInt;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public IFragmentWrapper getParentFragment() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(5, obtainAndWriteInterfaceToken());
                IFragmentWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public IObjectWrapper getResources() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(6, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean getRetainInstance() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(7, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public String getTag() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(8, obtainAndWriteInterfaceToken());
                String readString = transactAndReadException.readString();
                transactAndReadException.recycle();
                return readString;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public IFragmentWrapper getTargetFragment() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(9, obtainAndWriteInterfaceToken());
                IFragmentWrapper asInterface = Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public int getTargetRequestCode() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(10, obtainAndWriteInterfaceToken());
                int readInt = transactAndReadException.readInt();
                transactAndReadException.recycle();
                return readInt;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean getUserVisibleHint() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(11, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public IObjectWrapper getView() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(12, obtainAndWriteInterfaceToken());
                IObjectWrapper asInterface = IObjectWrapper.Stub.asInterface(transactAndReadException.readStrongBinder());
                transactAndReadException.recycle();
                return asInterface;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isAdded() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(13, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isDetached() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(14, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isHidden() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(15, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isInLayout() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(16, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isRemoving() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(17, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isResumed() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(18, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public boolean isVisible() throws RemoteException {
                Parcel transactAndReadException = transactAndReadException(19, obtainAndWriteInterfaceToken());
                boolean zza = com.google.android.gms.internal.stable.zzc.zza(transactAndReadException);
                transactAndReadException.recycle();
                return zza;
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void registerForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iObjectWrapper);
                transactAndReadExceptionReturnVoid(20, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void setHasOptionsMenu(boolean z) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, z);
                transactAndReadExceptionReturnVoid(21, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void setMenuVisibility(boolean z) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, z);
                transactAndReadExceptionReturnVoid(22, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void setRetainInstance(boolean z) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, z);
                transactAndReadExceptionReturnVoid(23, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void setUserVisibleHint(boolean z) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, z);
                transactAndReadExceptionReturnVoid(24, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void startActivity(Intent intent) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, intent);
                transactAndReadExceptionReturnVoid(25, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void startActivityForResult(Intent intent, int i) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, intent);
                obtainAndWriteInterfaceToken.writeInt(i);
                transactAndReadExceptionReturnVoid(26, obtainAndWriteInterfaceToken);
            }

            @Override // com.google.android.gms.dynamic.IFragmentWrapper
            public void unregisterForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException {
                Parcel obtainAndWriteInterfaceToken = obtainAndWriteInterfaceToken();
                com.google.android.gms.internal.stable.zzc.zza(obtainAndWriteInterfaceToken, iObjectWrapper);
                transactAndReadExceptionReturnVoid(27, obtainAndWriteInterfaceToken);
            }
        }

        public Stub() {
            super("com.google.android.gms.dynamic.IFragmentWrapper");
        }

        public static IFragmentWrapper asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.dynamic.IFragmentWrapper");
            return queryLocalInterface instanceof IFragmentWrapper ? (IFragmentWrapper) queryLocalInterface : new Proxy(iBinder);
        }

        @Override // com.google.android.gms.internal.stable.zzb
        protected boolean dispatchTransaction(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IInterface activity;
            int id;
            boolean retainInstance;
            switch (i) {
                case 2:
                    activity = getActivity();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, activity);
                    return true;
                case 3:
                    Bundle arguments = getArguments();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zzb(parcel2, arguments);
                    return true;
                case 4:
                    id = getId();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    return true;
                case 5:
                    activity = getParentFragment();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, activity);
                    return true;
                case 6:
                    activity = getResources();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, activity);
                    return true;
                case 7:
                    retainInstance = getRetainInstance();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 8:
                    String tag = getTag();
                    parcel2.writeNoException();
                    parcel2.writeString(tag);
                    return true;
                case 9:
                    activity = getTargetFragment();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, activity);
                    return true;
                case 10:
                    id = getTargetRequestCode();
                    parcel2.writeNoException();
                    parcel2.writeInt(id);
                    return true;
                case 11:
                    retainInstance = getUserVisibleHint();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 12:
                    activity = getView();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, activity);
                    return true;
                case 13:
                    retainInstance = isAdded();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 14:
                    retainInstance = isDetached();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 15:
                    retainInstance = isHidden();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 16:
                    retainInstance = isInLayout();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 17:
                    retainInstance = isRemoving();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 18:
                    retainInstance = isResumed();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 19:
                    retainInstance = isVisible();
                    parcel2.writeNoException();
                    com.google.android.gms.internal.stable.zzc.zza(parcel2, retainInstance);
                    return true;
                case 20:
                    registerForContextMenu(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                case 21:
                    setHasOptionsMenu(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    parcel2.writeNoException();
                    return true;
                case 22:
                    setMenuVisibility(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    parcel2.writeNoException();
                    return true;
                case 23:
                    setRetainInstance(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    parcel2.writeNoException();
                    return true;
                case 24:
                    setUserVisibleHint(com.google.android.gms.internal.stable.zzc.zza(parcel));
                    parcel2.writeNoException();
                    return true;
                case 25:
                    startActivity((Intent) com.google.android.gms.internal.stable.zzc.zza(parcel, Intent.CREATOR));
                    parcel2.writeNoException();
                    return true;
                case 26:
                    startActivityForResult((Intent) com.google.android.gms.internal.stable.zzc.zza(parcel, Intent.CREATOR), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 27:
                    unregisterForContextMenu(IObjectWrapper.Stub.asInterface(parcel.readStrongBinder()));
                    parcel2.writeNoException();
                    return true;
                default:
                    return false;
            }
        }
    }

    IObjectWrapper getActivity() throws RemoteException;

    Bundle getArguments() throws RemoteException;

    int getId() throws RemoteException;

    IFragmentWrapper getParentFragment() throws RemoteException;

    IObjectWrapper getResources() throws RemoteException;

    boolean getRetainInstance() throws RemoteException;

    String getTag() throws RemoteException;

    IFragmentWrapper getTargetFragment() throws RemoteException;

    int getTargetRequestCode() throws RemoteException;

    boolean getUserVisibleHint() throws RemoteException;

    IObjectWrapper getView() throws RemoteException;

    boolean isAdded() throws RemoteException;

    boolean isDetached() throws RemoteException;

    boolean isHidden() throws RemoteException;

    boolean isInLayout() throws RemoteException;

    boolean isRemoving() throws RemoteException;

    boolean isResumed() throws RemoteException;

    boolean isVisible() throws RemoteException;

    void registerForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException;

    void setHasOptionsMenu(boolean z) throws RemoteException;

    void setMenuVisibility(boolean z) throws RemoteException;

    void setRetainInstance(boolean z) throws RemoteException;

    void setUserVisibleHint(boolean z) throws RemoteException;

    void startActivity(Intent intent) throws RemoteException;

    void startActivityForResult(Intent intent, int i) throws RemoteException;

    void unregisterForContextMenu(IObjectWrapper iObjectWrapper) throws RemoteException;
}
