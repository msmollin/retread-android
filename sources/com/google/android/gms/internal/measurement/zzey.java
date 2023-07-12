package com.google.android.gms.internal.measurement;

import android.os.IInterface;
import android.os.RemoteException;
import java.util.List;

/* loaded from: classes.dex */
public interface zzey extends IInterface {
    List<zzjx> zza(zzdz zzdzVar, boolean z) throws RemoteException;

    List<zzed> zza(String str, String str2, zzdz zzdzVar) throws RemoteException;

    List<zzjx> zza(String str, String str2, String str3, boolean z) throws RemoteException;

    List<zzjx> zza(String str, String str2, boolean z, zzdz zzdzVar) throws RemoteException;

    void zza(long j, String str, String str2, String str3) throws RemoteException;

    void zza(zzdz zzdzVar) throws RemoteException;

    void zza(zzed zzedVar, zzdz zzdzVar) throws RemoteException;

    void zza(zzeu zzeuVar, zzdz zzdzVar) throws RemoteException;

    void zza(zzeu zzeuVar, String str, String str2) throws RemoteException;

    void zza(zzjx zzjxVar, zzdz zzdzVar) throws RemoteException;

    byte[] zza(zzeu zzeuVar, String str) throws RemoteException;

    void zzb(zzdz zzdzVar) throws RemoteException;

    void zzb(zzed zzedVar) throws RemoteException;

    String zzc(zzdz zzdzVar) throws RemoteException;

    void zzd(zzdz zzdzVar) throws RemoteException;

    List<zzed> zze(String str, String str2, String str3) throws RemoteException;
}
