package com.google.android.gms.internal.measurement;

import com.facebook.imageutils.TiffUtil;
import com.treadly.Treadly.Data.Managers.TreadlyEventHelper;
import java.io.IOException;

/* loaded from: classes.dex */
public final class zzkq extends zzaby<zzkq> {
    private static volatile zzkq[] zzatg;
    public Integer zzath = null;
    public zzkn[] zzati = zzkn.zzll();
    public zzks[] zzatj = zzks.zzlo();
    public Long zzatk = null;
    public Long zzatl = null;
    public Long zzatm = null;
    public Long zzatn = null;
    public Long zzato = null;
    public String zzatp = null;
    public String zzatq = null;
    public String zzatr = null;
    public String zzafn = null;
    public Integer zzats = null;
    public String zzadt = null;
    public String zzti = null;
    public String zzth = null;
    public Long zzatt = null;
    public Long zzatu = null;
    public String zzatv = null;
    public Boolean zzatw = null;
    public String zzadl = null;
    public Long zzatx = null;
    public Integer zzaty = null;
    public String zzaek = null;
    public String zzadm = null;
    public Boolean zzatz = null;
    public zzkm[] zzaua = zzkm.zzlk();
    public String zzado = null;
    public Integer zzaub = null;
    private Integer zzauc = null;
    private Integer zzaud = null;
    public String zzaue = null;
    public Long zzauf = null;
    public Long zzaug = null;
    public String zzauh = null;
    private String zzaui = null;
    public Integer zzauj = null;

    public zzkq() {
        this.zzbww = null;
        this.zzbxh = -1;
    }

    public static zzkq[] zzln() {
        if (zzatg == null) {
            synchronized (zzacc.zzbxg) {
                if (zzatg == null) {
                    zzatg = new zzkq[0];
                }
            }
        }
        return zzatg;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof zzkq) {
            zzkq zzkqVar = (zzkq) obj;
            if (this.zzath == null) {
                if (zzkqVar.zzath != null) {
                    return false;
                }
            } else if (!this.zzath.equals(zzkqVar.zzath)) {
                return false;
            }
            if (zzacc.equals(this.zzati, zzkqVar.zzati) && zzacc.equals(this.zzatj, zzkqVar.zzatj)) {
                if (this.zzatk == null) {
                    if (zzkqVar.zzatk != null) {
                        return false;
                    }
                } else if (!this.zzatk.equals(zzkqVar.zzatk)) {
                    return false;
                }
                if (this.zzatl == null) {
                    if (zzkqVar.zzatl != null) {
                        return false;
                    }
                } else if (!this.zzatl.equals(zzkqVar.zzatl)) {
                    return false;
                }
                if (this.zzatm == null) {
                    if (zzkqVar.zzatm != null) {
                        return false;
                    }
                } else if (!this.zzatm.equals(zzkqVar.zzatm)) {
                    return false;
                }
                if (this.zzatn == null) {
                    if (zzkqVar.zzatn != null) {
                        return false;
                    }
                } else if (!this.zzatn.equals(zzkqVar.zzatn)) {
                    return false;
                }
                if (this.zzato == null) {
                    if (zzkqVar.zzato != null) {
                        return false;
                    }
                } else if (!this.zzato.equals(zzkqVar.zzato)) {
                    return false;
                }
                if (this.zzatp == null) {
                    if (zzkqVar.zzatp != null) {
                        return false;
                    }
                } else if (!this.zzatp.equals(zzkqVar.zzatp)) {
                    return false;
                }
                if (this.zzatq == null) {
                    if (zzkqVar.zzatq != null) {
                        return false;
                    }
                } else if (!this.zzatq.equals(zzkqVar.zzatq)) {
                    return false;
                }
                if (this.zzatr == null) {
                    if (zzkqVar.zzatr != null) {
                        return false;
                    }
                } else if (!this.zzatr.equals(zzkqVar.zzatr)) {
                    return false;
                }
                if (this.zzafn == null) {
                    if (zzkqVar.zzafn != null) {
                        return false;
                    }
                } else if (!this.zzafn.equals(zzkqVar.zzafn)) {
                    return false;
                }
                if (this.zzats == null) {
                    if (zzkqVar.zzats != null) {
                        return false;
                    }
                } else if (!this.zzats.equals(zzkqVar.zzats)) {
                    return false;
                }
                if (this.zzadt == null) {
                    if (zzkqVar.zzadt != null) {
                        return false;
                    }
                } else if (!this.zzadt.equals(zzkqVar.zzadt)) {
                    return false;
                }
                if (this.zzti == null) {
                    if (zzkqVar.zzti != null) {
                        return false;
                    }
                } else if (!this.zzti.equals(zzkqVar.zzti)) {
                    return false;
                }
                if (this.zzth == null) {
                    if (zzkqVar.zzth != null) {
                        return false;
                    }
                } else if (!this.zzth.equals(zzkqVar.zzth)) {
                    return false;
                }
                if (this.zzatt == null) {
                    if (zzkqVar.zzatt != null) {
                        return false;
                    }
                } else if (!this.zzatt.equals(zzkqVar.zzatt)) {
                    return false;
                }
                if (this.zzatu == null) {
                    if (zzkqVar.zzatu != null) {
                        return false;
                    }
                } else if (!this.zzatu.equals(zzkqVar.zzatu)) {
                    return false;
                }
                if (this.zzatv == null) {
                    if (zzkqVar.zzatv != null) {
                        return false;
                    }
                } else if (!this.zzatv.equals(zzkqVar.zzatv)) {
                    return false;
                }
                if (this.zzatw == null) {
                    if (zzkqVar.zzatw != null) {
                        return false;
                    }
                } else if (!this.zzatw.equals(zzkqVar.zzatw)) {
                    return false;
                }
                if (this.zzadl == null) {
                    if (zzkqVar.zzadl != null) {
                        return false;
                    }
                } else if (!this.zzadl.equals(zzkqVar.zzadl)) {
                    return false;
                }
                if (this.zzatx == null) {
                    if (zzkqVar.zzatx != null) {
                        return false;
                    }
                } else if (!this.zzatx.equals(zzkqVar.zzatx)) {
                    return false;
                }
                if (this.zzaty == null) {
                    if (zzkqVar.zzaty != null) {
                        return false;
                    }
                } else if (!this.zzaty.equals(zzkqVar.zzaty)) {
                    return false;
                }
                if (this.zzaek == null) {
                    if (zzkqVar.zzaek != null) {
                        return false;
                    }
                } else if (!this.zzaek.equals(zzkqVar.zzaek)) {
                    return false;
                }
                if (this.zzadm == null) {
                    if (zzkqVar.zzadm != null) {
                        return false;
                    }
                } else if (!this.zzadm.equals(zzkqVar.zzadm)) {
                    return false;
                }
                if (this.zzatz == null) {
                    if (zzkqVar.zzatz != null) {
                        return false;
                    }
                } else if (!this.zzatz.equals(zzkqVar.zzatz)) {
                    return false;
                }
                if (zzacc.equals(this.zzaua, zzkqVar.zzaua)) {
                    if (this.zzado == null) {
                        if (zzkqVar.zzado != null) {
                            return false;
                        }
                    } else if (!this.zzado.equals(zzkqVar.zzado)) {
                        return false;
                    }
                    if (this.zzaub == null) {
                        if (zzkqVar.zzaub != null) {
                            return false;
                        }
                    } else if (!this.zzaub.equals(zzkqVar.zzaub)) {
                        return false;
                    }
                    if (this.zzauc == null) {
                        if (zzkqVar.zzauc != null) {
                            return false;
                        }
                    } else if (!this.zzauc.equals(zzkqVar.zzauc)) {
                        return false;
                    }
                    if (this.zzaud == null) {
                        if (zzkqVar.zzaud != null) {
                            return false;
                        }
                    } else if (!this.zzaud.equals(zzkqVar.zzaud)) {
                        return false;
                    }
                    if (this.zzaue == null) {
                        if (zzkqVar.zzaue != null) {
                            return false;
                        }
                    } else if (!this.zzaue.equals(zzkqVar.zzaue)) {
                        return false;
                    }
                    if (this.zzauf == null) {
                        if (zzkqVar.zzauf != null) {
                            return false;
                        }
                    } else if (!this.zzauf.equals(zzkqVar.zzauf)) {
                        return false;
                    }
                    if (this.zzaug == null) {
                        if (zzkqVar.zzaug != null) {
                            return false;
                        }
                    } else if (!this.zzaug.equals(zzkqVar.zzaug)) {
                        return false;
                    }
                    if (this.zzauh == null) {
                        if (zzkqVar.zzauh != null) {
                            return false;
                        }
                    } else if (!this.zzauh.equals(zzkqVar.zzauh)) {
                        return false;
                    }
                    if (this.zzaui == null) {
                        if (zzkqVar.zzaui != null) {
                            return false;
                        }
                    } else if (!this.zzaui.equals(zzkqVar.zzaui)) {
                        return false;
                    }
                    if (this.zzauj == null) {
                        if (zzkqVar.zzauj != null) {
                            return false;
                        }
                    } else if (!this.zzauj.equals(zzkqVar.zzauj)) {
                        return false;
                    }
                    return (this.zzbww == null || this.zzbww.isEmpty()) ? zzkqVar.zzbww == null || zzkqVar.zzbww.isEmpty() : this.zzbww.equals(zzkqVar.zzbww);
                }
                return false;
            }
            return false;
        }
        return false;
    }

    public final int hashCode() {
        int i = 0;
        int hashCode = (((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((getClass().getName().hashCode() + 527) * 31) + (this.zzath == null ? 0 : this.zzath.hashCode())) * 31) + zzacc.hashCode(this.zzati)) * 31) + zzacc.hashCode(this.zzatj)) * 31) + (this.zzatk == null ? 0 : this.zzatk.hashCode())) * 31) + (this.zzatl == null ? 0 : this.zzatl.hashCode())) * 31) + (this.zzatm == null ? 0 : this.zzatm.hashCode())) * 31) + (this.zzatn == null ? 0 : this.zzatn.hashCode())) * 31) + (this.zzato == null ? 0 : this.zzato.hashCode())) * 31) + (this.zzatp == null ? 0 : this.zzatp.hashCode())) * 31) + (this.zzatq == null ? 0 : this.zzatq.hashCode())) * 31) + (this.zzatr == null ? 0 : this.zzatr.hashCode())) * 31) + (this.zzafn == null ? 0 : this.zzafn.hashCode())) * 31) + (this.zzats == null ? 0 : this.zzats.hashCode())) * 31) + (this.zzadt == null ? 0 : this.zzadt.hashCode())) * 31) + (this.zzti == null ? 0 : this.zzti.hashCode())) * 31) + (this.zzth == null ? 0 : this.zzth.hashCode())) * 31) + (this.zzatt == null ? 0 : this.zzatt.hashCode())) * 31) + (this.zzatu == null ? 0 : this.zzatu.hashCode())) * 31) + (this.zzatv == null ? 0 : this.zzatv.hashCode())) * 31) + (this.zzatw == null ? 0 : this.zzatw.hashCode())) * 31) + (this.zzadl == null ? 0 : this.zzadl.hashCode())) * 31) + (this.zzatx == null ? 0 : this.zzatx.hashCode())) * 31) + (this.zzaty == null ? 0 : this.zzaty.hashCode())) * 31) + (this.zzaek == null ? 0 : this.zzaek.hashCode())) * 31) + (this.zzadm == null ? 0 : this.zzadm.hashCode())) * 31) + (this.zzatz == null ? 0 : this.zzatz.hashCode())) * 31) + zzacc.hashCode(this.zzaua)) * 31) + (this.zzado == null ? 0 : this.zzado.hashCode())) * 31) + (this.zzaub == null ? 0 : this.zzaub.hashCode())) * 31) + (this.zzauc == null ? 0 : this.zzauc.hashCode())) * 31) + (this.zzaud == null ? 0 : this.zzaud.hashCode())) * 31) + (this.zzaue == null ? 0 : this.zzaue.hashCode())) * 31) + (this.zzauf == null ? 0 : this.zzauf.hashCode())) * 31) + (this.zzaug == null ? 0 : this.zzaug.hashCode())) * 31) + (this.zzauh == null ? 0 : this.zzauh.hashCode())) * 31) + (this.zzaui == null ? 0 : this.zzaui.hashCode())) * 31) + (this.zzauj == null ? 0 : this.zzauj.hashCode())) * 31;
        if (this.zzbww != null && !this.zzbww.isEmpty()) {
            i = this.zzbww.hashCode();
        }
        return hashCode + i;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final int zza() {
        int zza = super.zza();
        if (this.zzath != null) {
            zza += zzabw.zzf(1, this.zzath.intValue());
        }
        if (this.zzati != null && this.zzati.length > 0) {
            int i = zza;
            for (int i2 = 0; i2 < this.zzati.length; i2++) {
                zzkn zzknVar = this.zzati[i2];
                if (zzknVar != null) {
                    i += zzabw.zzb(2, zzknVar);
                }
            }
            zza = i;
        }
        if (this.zzatj != null && this.zzatj.length > 0) {
            int i3 = zza;
            for (int i4 = 0; i4 < this.zzatj.length; i4++) {
                zzks zzksVar = this.zzatj[i4];
                if (zzksVar != null) {
                    i3 += zzabw.zzb(3, zzksVar);
                }
            }
            zza = i3;
        }
        if (this.zzatk != null) {
            zza += zzabw.zzc(4, this.zzatk.longValue());
        }
        if (this.zzatl != null) {
            zza += zzabw.zzc(5, this.zzatl.longValue());
        }
        if (this.zzatm != null) {
            zza += zzabw.zzc(6, this.zzatm.longValue());
        }
        if (this.zzato != null) {
            zza += zzabw.zzc(7, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            zza += zzabw.zzc(8, this.zzatp);
        }
        if (this.zzatq != null) {
            zza += zzabw.zzc(9, this.zzatq);
        }
        if (this.zzatr != null) {
            zza += zzabw.zzc(10, this.zzatr);
        }
        if (this.zzafn != null) {
            zza += zzabw.zzc(11, this.zzafn);
        }
        if (this.zzats != null) {
            zza += zzabw.zzf(12, this.zzats.intValue());
        }
        if (this.zzadt != null) {
            zza += zzabw.zzc(13, this.zzadt);
        }
        if (this.zzti != null) {
            zza += zzabw.zzc(14, this.zzti);
        }
        if (this.zzth != null) {
            zza += zzabw.zzc(16, this.zzth);
        }
        if (this.zzatt != null) {
            zza += zzabw.zzc(17, this.zzatt.longValue());
        }
        if (this.zzatu != null) {
            zza += zzabw.zzc(18, this.zzatu.longValue());
        }
        if (this.zzatv != null) {
            zza += zzabw.zzc(19, this.zzatv);
        }
        if (this.zzatw != null) {
            this.zzatw.booleanValue();
            zza += zzabw.zzaq(20) + 1;
        }
        if (this.zzadl != null) {
            zza += zzabw.zzc(21, this.zzadl);
        }
        if (this.zzatx != null) {
            zza += zzabw.zzc(22, this.zzatx.longValue());
        }
        if (this.zzaty != null) {
            zza += zzabw.zzf(23, this.zzaty.intValue());
        }
        if (this.zzaek != null) {
            zza += zzabw.zzc(24, this.zzaek);
        }
        if (this.zzadm != null) {
            zza += zzabw.zzc(25, this.zzadm);
        }
        if (this.zzatn != null) {
            zza += zzabw.zzc(26, this.zzatn.longValue());
        }
        if (this.zzatz != null) {
            this.zzatz.booleanValue();
            zza += zzabw.zzaq(28) + 1;
        }
        if (this.zzaua != null && this.zzaua.length > 0) {
            for (int i5 = 0; i5 < this.zzaua.length; i5++) {
                zzkm zzkmVar = this.zzaua[i5];
                if (zzkmVar != null) {
                    zza += zzabw.zzb(29, zzkmVar);
                }
            }
        }
        if (this.zzado != null) {
            zza += zzabw.zzc(30, this.zzado);
        }
        if (this.zzaub != null) {
            zza += zzabw.zzf(31, this.zzaub.intValue());
        }
        if (this.zzauc != null) {
            zza += zzabw.zzf(32, this.zzauc.intValue());
        }
        if (this.zzaud != null) {
            zza += zzabw.zzf(33, this.zzaud.intValue());
        }
        if (this.zzaue != null) {
            zza += zzabw.zzc(34, this.zzaue);
        }
        if (this.zzauf != null) {
            zza += zzabw.zzc(35, this.zzauf.longValue());
        }
        if (this.zzaug != null) {
            zza += zzabw.zzc(36, this.zzaug.longValue());
        }
        if (this.zzauh != null) {
            zza += zzabw.zzc(37, this.zzauh);
        }
        if (this.zzaui != null) {
            zza += zzabw.zzc(38, this.zzaui);
        }
        return this.zzauj != null ? zza + zzabw.zzf(39, this.zzauj.intValue()) : zza;
    }

    @Override // com.google.android.gms.internal.measurement.zzaby, com.google.android.gms.internal.measurement.zzace
    public final void zza(zzabw zzabwVar) throws IOException {
        if (this.zzath != null) {
            zzabwVar.zze(1, this.zzath.intValue());
        }
        if (this.zzati != null && this.zzati.length > 0) {
            for (int i = 0; i < this.zzati.length; i++) {
                zzkn zzknVar = this.zzati[i];
                if (zzknVar != null) {
                    zzabwVar.zza(2, zzknVar);
                }
            }
        }
        if (this.zzatj != null && this.zzatj.length > 0) {
            for (int i2 = 0; i2 < this.zzatj.length; i2++) {
                zzks zzksVar = this.zzatj[i2];
                if (zzksVar != null) {
                    zzabwVar.zza(3, zzksVar);
                }
            }
        }
        if (this.zzatk != null) {
            zzabwVar.zzb(4, this.zzatk.longValue());
        }
        if (this.zzatl != null) {
            zzabwVar.zzb(5, this.zzatl.longValue());
        }
        if (this.zzatm != null) {
            zzabwVar.zzb(6, this.zzatm.longValue());
        }
        if (this.zzato != null) {
            zzabwVar.zzb(7, this.zzato.longValue());
        }
        if (this.zzatp != null) {
            zzabwVar.zzb(8, this.zzatp);
        }
        if (this.zzatq != null) {
            zzabwVar.zzb(9, this.zzatq);
        }
        if (this.zzatr != null) {
            zzabwVar.zzb(10, this.zzatr);
        }
        if (this.zzafn != null) {
            zzabwVar.zzb(11, this.zzafn);
        }
        if (this.zzats != null) {
            zzabwVar.zze(12, this.zzats.intValue());
        }
        if (this.zzadt != null) {
            zzabwVar.zzb(13, this.zzadt);
        }
        if (this.zzti != null) {
            zzabwVar.zzb(14, this.zzti);
        }
        if (this.zzth != null) {
            zzabwVar.zzb(16, this.zzth);
        }
        if (this.zzatt != null) {
            zzabwVar.zzb(17, this.zzatt.longValue());
        }
        if (this.zzatu != null) {
            zzabwVar.zzb(18, this.zzatu.longValue());
        }
        if (this.zzatv != null) {
            zzabwVar.zzb(19, this.zzatv);
        }
        if (this.zzatw != null) {
            zzabwVar.zza(20, this.zzatw.booleanValue());
        }
        if (this.zzadl != null) {
            zzabwVar.zzb(21, this.zzadl);
        }
        if (this.zzatx != null) {
            zzabwVar.zzb(22, this.zzatx.longValue());
        }
        if (this.zzaty != null) {
            zzabwVar.zze(23, this.zzaty.intValue());
        }
        if (this.zzaek != null) {
            zzabwVar.zzb(24, this.zzaek);
        }
        if (this.zzadm != null) {
            zzabwVar.zzb(25, this.zzadm);
        }
        if (this.zzatn != null) {
            zzabwVar.zzb(26, this.zzatn.longValue());
        }
        if (this.zzatz != null) {
            zzabwVar.zza(28, this.zzatz.booleanValue());
        }
        if (this.zzaua != null && this.zzaua.length > 0) {
            for (int i3 = 0; i3 < this.zzaua.length; i3++) {
                zzkm zzkmVar = this.zzaua[i3];
                if (zzkmVar != null) {
                    zzabwVar.zza(29, zzkmVar);
                }
            }
        }
        if (this.zzado != null) {
            zzabwVar.zzb(30, this.zzado);
        }
        if (this.zzaub != null) {
            zzabwVar.zze(31, this.zzaub.intValue());
        }
        if (this.zzauc != null) {
            zzabwVar.zze(32, this.zzauc.intValue());
        }
        if (this.zzaud != null) {
            zzabwVar.zze(33, this.zzaud.intValue());
        }
        if (this.zzaue != null) {
            zzabwVar.zzb(34, this.zzaue);
        }
        if (this.zzauf != null) {
            zzabwVar.zzb(35, this.zzauf.longValue());
        }
        if (this.zzaug != null) {
            zzabwVar.zzb(36, this.zzaug.longValue());
        }
        if (this.zzauh != null) {
            zzabwVar.zzb(37, this.zzauh);
        }
        if (this.zzaui != null) {
            zzabwVar.zzb(38, this.zzaui);
        }
        if (this.zzauj != null) {
            zzabwVar.zze(39, this.zzauj.intValue());
        }
        super.zza(zzabwVar);
    }

    @Override // com.google.android.gms.internal.measurement.zzace
    public final /* synthetic */ zzace zzb(zzabv zzabvVar) throws IOException {
        while (true) {
            int zzuw = zzabvVar.zzuw();
            switch (zzuw) {
                case 0:
                    return this;
                case 8:
                    this.zzath = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case 18:
                    int zzb = zzach.zzb(zzabvVar, 18);
                    int length = this.zzati == null ? 0 : this.zzati.length;
                    zzkn[] zzknVarArr = new zzkn[zzb + length];
                    if (length != 0) {
                        System.arraycopy(this.zzati, 0, zzknVarArr, 0, length);
                    }
                    while (length < zzknVarArr.length - 1) {
                        zzknVarArr[length] = new zzkn();
                        zzabvVar.zza(zzknVarArr[length]);
                        zzabvVar.zzuw();
                        length++;
                    }
                    zzknVarArr[length] = new zzkn();
                    zzabvVar.zza(zzknVarArr[length]);
                    this.zzati = zzknVarArr;
                    break;
                case 26:
                    int zzb2 = zzach.zzb(zzabvVar, 26);
                    int length2 = this.zzatj == null ? 0 : this.zzatj.length;
                    zzks[] zzksVarArr = new zzks[zzb2 + length2];
                    if (length2 != 0) {
                        System.arraycopy(this.zzatj, 0, zzksVarArr, 0, length2);
                    }
                    while (length2 < zzksVarArr.length - 1) {
                        zzksVarArr[length2] = new zzks();
                        zzabvVar.zza(zzksVarArr[length2]);
                        zzabvVar.zzuw();
                        length2++;
                    }
                    zzksVarArr[length2] = new zzks();
                    zzabvVar.zza(zzksVarArr[length2]);
                    this.zzatj = zzksVarArr;
                    break;
                case 32:
                    this.zzatk = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 40:
                    this.zzatl = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 48:
                    this.zzatm = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 56:
                    this.zzato = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 66:
                    this.zzatp = zzabvVar.readString();
                    break;
                case 74:
                    this.zzatq = zzabvVar.readString();
                    break;
                case 82:
                    this.zzatr = zzabvVar.readString();
                    break;
                case 90:
                    this.zzafn = zzabvVar.readString();
                    break;
                case 96:
                    this.zzats = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case 106:
                    this.zzadt = zzabvVar.readString();
                    break;
                case 114:
                    this.zzti = zzabvVar.readString();
                    break;
                case 130:
                    this.zzth = zzabvVar.readString();
                    break;
                case 136:
                    this.zzatt = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 144:
                    this.zzatu = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 154:
                    this.zzatv = zzabvVar.readString();
                    break;
                case 160:
                    this.zzatw = Boolean.valueOf(zzabvVar.zzux());
                    break;
                case 170:
                    this.zzadl = zzabvVar.readString();
                    break;
                case 176:
                    this.zzatx = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 184:
                    this.zzaty = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case 194:
                    this.zzaek = zzabvVar.readString();
                    break;
                case TreadlyEventHelper.MESSAGE_ID_USER_VIDEO_BROADCAST_INVITE_REQUEST /* 202 */:
                    this.zzadm = zzabvVar.readString();
                    break;
                case 208:
                    this.zzatn = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 224:
                    this.zzatz = Boolean.valueOf(zzabvVar.zzux());
                    break;
                case 234:
                    int zzb3 = zzach.zzb(zzabvVar, 234);
                    int length3 = this.zzaua == null ? 0 : this.zzaua.length;
                    zzkm[] zzkmVarArr = new zzkm[zzb3 + length3];
                    if (length3 != 0) {
                        System.arraycopy(this.zzaua, 0, zzkmVarArr, 0, length3);
                    }
                    while (length3 < zzkmVarArr.length - 1) {
                        zzkmVarArr[length3] = new zzkm();
                        zzabvVar.zza(zzkmVarArr[length3]);
                        zzabvVar.zzuw();
                        length3++;
                    }
                    zzkmVarArr[length3] = new zzkm();
                    zzabvVar.zza(zzkmVarArr[length3]);
                    this.zzaua = zzkmVarArr;
                    break;
                case 242:
                    this.zzado = zzabvVar.readString();
                    break;
                case 248:
                    this.zzaub = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case 256:
                    this.zzauc = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case 264:
                    this.zzaud = Integer.valueOf(zzabvVar.zzuy());
                    break;
                case TiffUtil.TIFF_TAG_ORIENTATION /* 274 */:
                    this.zzaue = zzabvVar.readString();
                    break;
                case 280:
                    this.zzauf = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 288:
                    this.zzaug = Long.valueOf(zzabvVar.zzuz());
                    break;
                case 298:
                    this.zzauh = zzabvVar.readString();
                    break;
                case 306:
                    this.zzaui = zzabvVar.readString();
                    break;
                case 312:
                    this.zzauj = Integer.valueOf(zzabvVar.zzuy());
                    break;
                default:
                    if (super.zza(zzabvVar, zzuw)) {
                        break;
                    } else {
                        return this;
                    }
            }
        }
    }
}
