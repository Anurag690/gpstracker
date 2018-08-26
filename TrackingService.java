
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class TrackingService extends Service {
    private static final String TAG = "MARCIGPS";
    private static final String URL = "http://192.168.1.27:4000/";
    private static final String TRACK_EVENT = "tracking";
    private Socket mSocket = null;
    LocationListener [] locationListeners = new LocationListener[] {
        new LocationListener(LocationManager.GPS_PROVIDER),
        new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    LocationManager locationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent notificationIntent = new Intent(this, TrackingService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("BushireDriverApp")
                .setContentText("GPS is running")
                .setSmallIcon(R.drawable.src_assets_drawablehdpi_boarding_dropping)
                .setContentIntent(pendingIntent)
                .setTicker("BushireDriverApp")
                .setPriority(Notification.PRIORITY_HIGH)
                .build();
        startForeground(101, notification);

        initializeLocationManager();
    }

    private void initializeLocationManager() {
        Log.i(TAG, "initializelocationmanager");
        if(locationManager == null) {
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location lastLocation;

        public LocationListener(String provider) {
            lastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, location.toString());
            lastLocation.set(location);
            try {
                mSocket.emit(TRACK_EVENT, lastLocation);
            }catch (Exception err) {
                Log.i(TAG, err.toString());
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            Log.i("GPS", "onStatusChanged: " + s);
        }

        @Override
        public void onProviderEnabled(String s) {
            Log.i("GPS", "onProviderEnabled: " + s);
        }

        @Override
        public void onProviderDisabled(String s) {
            Log.i("GPS", "onProviderDisabled: " + s);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        try {
            createSocket();
            try{
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        LOCATION_INTERVAL,
                        LOCATION_DISTANCE,
                        locationListeners[1]
                );
            }catch (Exception err) {
                Log.i(TAG, err.toString());
            }
            try {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        LOCATION_INTERVAL,
                        LOCATION_DISTANCE,
                        locationListeners[0]
                );
            }catch (Exception err) {
                Log.i(TAG, err.toString());
            }
            mSocket.emit("connection", "conn1");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        return START_STICKY;
    }

    public void createSocket() throws URISyntaxException {
        String token = "";
        IO.Options options = new IO.Options();
        options.query = "token="+token;
        mSocket = IO.socket(URL, options).connect();
    }

    // @Override
    // public void onDestroy() {
    //     super.onDestroy();
    //     Log.i(TAG, "onDestroy");
    //     if(locationManager != null) {
    //         for(int i = 0; i<locationListeners.length; i++) {
    //             try {
    //                 locationManager.removeUpdates(locationListeners[i]);
    //             }catch (Exception e) {
    //                 Log.i(TAG, e.toString());
    //             }
    //         }
    //     }
    //     mSocket.disconnect();
    //     Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    // }
}