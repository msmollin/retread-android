package com.bambuser.broadcaster;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.concurrent.atomic.AtomicReference;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class LocationController {
    private static final int FALLBACK_PERIOD = 120000;
    private static final int LOCATION_PERIOD = 8000;
    private static final String LOGTAG = "LocationController";
    private static final long MAX_LOCATION_AGE = 3600000;
    private final Context mContext;
    private final LocationManager mLocationManager;
    private final LocationListener mGpsLocationListener = new LocationListener() { // from class: com.bambuser.broadcaster.LocationController.3
        @Override // android.location.LocationListener
        public void onProviderEnabled(String str) {
        }

        @Override // android.location.LocationListener
        public void onStatusChanged(String str, int i, Bundle bundle) {
            switch (i) {
                case 0:
                case 1:
                    LocationController.this.startNetworkLocationListening();
                    return;
                default:
                    return;
            }
        }

        @Override // android.location.LocationListener
        public void onProviderDisabled(String str) {
            LocationController.this.startNetworkLocationListening();
        }

        @Override // android.location.LocationListener
        public void onLocationChanged(Location location) {
            if (Log.isLoggable(LocationController.LOGTAG, 3)) {
                Log.d(LocationController.LOGTAG, "Got a location update from " + location.getProvider());
            }
            if (System.currentTimeMillis() - location.getTime() <= LocationController.MAX_LOCATION_AGE) {
                LocationController.this.stopNetworkLocationListening();
                LocationController.this.mLastKnownLocation = location;
                Observer observer = LocationController.this.mLocationObserver;
                if (observer != null) {
                    observer.onLocationChanged(location.getLatitude(), location.getLongitude(), location.getAccuracy());
                    return;
                }
                return;
            }
            Log.i(LocationController.LOGTAG, "location fix is old, discarding it");
        }
    };
    private LocationListener mNetworkLocationListener = null;
    private final AtomicReference<HandlerThread> mLocationThread = new AtomicReference<>();
    private volatile Observer mLocationObserver = null;
    private volatile Location mLastKnownLocation = null;

    /* loaded from: classes.dex */
    public interface Observer {
        void onLocationChanged(double d, double d2, float f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocationController(Context context) {
        this.mContext = context.getApplicationContext();
        this.mLocationManager = (LocationManager) context.getSystemService(FirebaseAnalytics.Param.LOCATION);
    }

    public void setObserver(Observer observer) {
        this.mLocationObserver = observer;
    }

    public synchronized void start() {
        if (this.mLocationThread.get() != null) {
            return;
        }
        HandlerThread handlerThread = new HandlerThread("LocationThread");
        handlerThread.start();
        this.mLocationThread.set(handlerThread);
        if (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION") && this.mLocationManager.getProviders(true).contains("gps")) {
            this.mLocationManager.requestLocationUpdates("gps", 8000L, 1.0f, this.mGpsLocationListener, handlerThread.getLooper());
        }
        startNetworkLocationListening();
        final Handler handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(new Runnable() { // from class: com.bambuser.broadcaster.LocationController.1
            @Override // java.lang.Runnable
            public void run() {
                if (LocationController.this.mLastKnownLocation == null || LocationController.this.mLastKnownLocation.getTime() + 120000 < System.currentTimeMillis()) {
                    LocationController.this.startNetworkLocationListening();
                }
                handler.postDelayed(this, 120000L);
            }
        }, 120000L);
    }

    public void stop() {
        this.mLocationObserver = null;
        if (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION")) {
            this.mLocationManager.removeUpdates(this.mGpsLocationListener);
        }
        stopNetworkLocationListening();
        HandlerThread andSet = this.mLocationThread.getAndSet(null);
        if (andSet != null) {
            andSet.getLooper().quit();
        }
    }

    public void sendLastKnownLocation(Connection connection) {
        Location location = this.mLastKnownLocation;
        if (connection == null || location == null) {
            return;
        }
        MovinoPacket movinoPacket = new MovinoPacket(18, 23);
        movinoPacket.writeUint8(2).writeReal64(location.getLatitude());
        movinoPacket.writeUint8(2).writeReal64(location.getLongitude());
        movinoPacket.writeUint8(1).writeReal32(location.getAccuracy());
        connection.send(movinoPacket);
    }

    public Location getLastKnownLocation() {
        return this.mLastKnownLocation;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void startNetworkLocationListening() {
        HandlerThread handlerThread = this.mLocationThread.get();
        if (this.mLocationManager != null && this.mNetworkLocationListener == null && handlerThread != null) {
            if ((DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") || DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION")) && this.mLocationManager.getProviders(true).contains("network")) {
                this.mNetworkLocationListener = new LocationListener() { // from class: com.bambuser.broadcaster.LocationController.2
                    @Override // android.location.LocationListener
                    public void onProviderDisabled(String str) {
                    }

                    @Override // android.location.LocationListener
                    public void onProviderEnabled(String str) {
                    }

                    @Override // android.location.LocationListener
                    public void onStatusChanged(String str, int i, Bundle bundle) {
                    }

                    @Override // android.location.LocationListener
                    public void onLocationChanged(Location location) {
                        if (Log.isLoggable(LocationController.LOGTAG, 3)) {
                            Log.d(LocationController.LOGTAG, "Got a location update from " + location.getProvider());
                        }
                        if (System.currentTimeMillis() - location.getTime() <= LocationController.MAX_LOCATION_AGE) {
                            LocationController.this.mLastKnownLocation = location;
                            Observer observer = LocationController.this.mLocationObserver;
                            if (observer != null) {
                                observer.onLocationChanged(location.getLatitude(), location.getLongitude(), location.getAccuracy());
                                return;
                            }
                            return;
                        }
                        Log.i(LocationController.LOGTAG, "location fix is old, discarding it");
                    }
                };
                this.mLocationManager.requestLocationUpdates("network", 8000L, 1.0f, this.mNetworkLocationListener, handlerThread.getLooper());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void stopNetworkLocationListening() {
        if (this.mNetworkLocationListener != null && this.mLocationManager != null && (DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_COARSE_LOCATION") || DeviceInfoHandler.hasPermission(this.mContext, "android.permission.ACCESS_FINE_LOCATION"))) {
            this.mLocationManager.removeUpdates(this.mNetworkLocationListener);
        }
        this.mNetworkLocationListener = null;
    }
}
