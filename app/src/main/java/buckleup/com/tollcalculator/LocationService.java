package buckleup.com.tollcalculator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.Manifest;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Permission;

/**
 * Created by root on 16/3/18.
 */

public class LocationService extends Service {
    static boolean isconnected = false;
    static String finlat;
    static String finlon;
    public String phonenumber = null;
    String uid ="";
    //public String password = null;

    public static final String BROADCAST_ACTION = "Hello World";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;

    //Intent intent;
    int counter = 0;


    //Dummy DAtaset

    double lat[]={18.500897,
            18.500772,
            18.500615,
            18.500425,
            18.500312,
            18.500199,
            18.500062,
            18.499911,
            18.499781,
            18.499664,
            18.499544,
            18.499417,
            18.499278,
            18.499135,
            18.499018,
            18.498907,
            18.498761,
            18.498624,
            18.498483,
            18.498345,
            18.498227,
            18.498094,
            18.497987,
            18.497845,
            18.497712,
            18.497591,
            18.497457,
            18.497318,
            18.497172,
            18.497033,
            18.496899,
            18.496765,
            18.496637,
            18.496494,
            18.496353,
            18.496221,
            18.496093,
            18.495974,
            18.495862,
            18.495736,
            18.495594,
            18.495453,
            18.495311,
            18.495166,
            18.495027,
            18.494901,
            18.494775,
            18.494626,
            18.494495};
    double lon[] = {73.939241,
            73.940178,
            73.941113,
            73.942027,
            73.942967,
            73.943907,
            73.944844,
            73.945778,
            73.946716,
            73.947657,
            73.948599,
            73.949536,
            73.950472,
            73.951409,
            73.952351,
            73.953292,
            73.954225,
            73.955162,
            73.956101,
            73.957036,
            73.957977,
            73.958915,
            73.959857,
            73.960791,
            73.961731,
            73.962671,
            73.963608,
            73.964546,
            73.965481,
            73.966418,
            73.967354,
            73.968294,
            73.969231,
            73.970168,
            73.971103,
            73.972042,
            73.972981,
            73.973919,
            73.974863,
            73.975798,
            73.976737,
            73.977671,
            73.978607,
            73.979545,
            73.980483,
            73.981422,
            73.982361,
            73.983294,
            73.984233};

    double slat[] = {18.495453
            ,18.495989
            ,18.496538
            ,18.497084
            ,18.497636
            ,18.498214
            ,18.49885
            ,18.49937
            ,18.500003
            ,18.500747,
            18.500897,
            18.500772,
            18.500615,
            18.500425,
            18.500312,
            18.500199,
            18.500062,
            18.499911,
            18.499781,
            18.499664,
            18.499544,
            18.499417,
            18.499278,
            18.499135,
            18.499018,
            18.498907,
            18.498761,
            18.498624,
            18.498483,
            18.498345,
            18.498227,
            18.498094,
            18.497987,
            18.497845,
            18.497712,
            18.497591,
            18.497457,
            18.497318,
            18.497172,
            18.497033,
            18.496899,
            18.496765,
            18.496637,
            18.496494,
            18.496353,
            18.496221,
            18.496093,
            18.495974,
            18.495862,
            18.495736,
            18.495594,
            18.495453,
            18.495311,
            18.495166,
            18.495027,
            18.494901,
            18.494775,
            18.494626,
            18.494495
    };

    double slon[] = {73.944862
            ,73.944103
            ,73.943351
            ,73.942596
            ,73.941848
            ,73.941124
            ,73.940453
            ,73.939707
            ,73.939033
            ,73.938538,
            73.939241,
            73.940178,
            73.941113,
            73.942027,
            73.942967,
            73.943907,
            73.944844,
            73.945778,
            73.946716,
            73.947657,
            73.948599,
            73.949536,
            73.950472,
            73.951409,
            73.952351,
            73.953292,
            73.954225,
            73.955162,
            73.956101,
            73.957036,
            73.957977,
            73.958915,
            73.959857,
            73.960791,
            73.961731,
            73.962671,
            73.963608,
            73.964546,
            73.965481,
            73.966418,
            73.967354,
            73.968294,
            73.969231,
            73.970168,
            73.971103,
            73.972042,
            73.972981,
            73.973919,
            73.974863,
            73.975798,
            73.976737,
            73.977671,
            73.978607,
            73.979545,
            73.980483,
            73.981422,
            73.982361,
            73.983294,
            73.984233
    };

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist/0.62137);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        while(true) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                break;
            }
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);*/

        int ref;
        int flg=0;
        for(int i = 0 ;i < slat.length; i++)
        {
            for(int j = 0 ;j < lat.length; j++)
            {
                double dist = distance(lat[j],lon[j],slat[i],slon[i]);
                if( flg == 0 && dist <= 0.07 )
                {
                    ref = j;
                    if(distance(slat[i+1],slon[i+1],lat[j+1],lon[j+1])<=0.07 || distance(slat[i+1],slon[i+1],lat[j+2],lon[j+2])<=0.07 || distance(slat[i+1],slon[i+1],lat[j+3],lon[j+3])<=0.07){
                        flg=1;
                        Log.d("Found","lat lon : "+lat[ref]+" "+lon[ref]);
                        break;
                    }
                   // reflon = j;

                }


            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        //intent = new Intent(BROADCAST_ACTION);
    }

    @Override
    public void onStart(Intent intent, int startId) throws SecurityException
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }



    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("STOP_SERVICE", "DONE");
        locationManager.removeUpdates(listener);
    }

    public class MyLocationListener implements LocationListener
    {

        public void onLocationChanged( final Location loc)
        {
            Log.i("********", "Location changed");
            Log.i("lat ",""+loc.getLatitude());
            Log.i("lon ",""+loc.getLongitude());
            isconnected = true;
            if(isBetterLocation(loc, previousBestLocation)) {

                loc.getLatitude();
                loc.getLongitude();
            }
        }

        public void onProviderDisabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT ).show();
        }


        public void onProviderEnabled(String provider)
        {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }

    }

}
