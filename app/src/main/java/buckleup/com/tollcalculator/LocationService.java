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
//Toll dataset

    double tolllat[]={
            18.495594,
            18.488307
    };

    double tolllon[]={
            73.976737,
            74.080526
    };

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
            18.494495,
            18.494366,
            18.494235,
            18.494112,
            18.493966,
            18.493844,
            18.493718,
            18.493604,
            18.493468,
            18.493346,
            18.493215,
            18.493036,
            18.492901,
            18.492773,
            18.492645,
            18.492519,
            18.492393,
            18.492263,
            18.492127,
            18.491989,
            18.491857,
            18.491729,
            18.491608,
            18.491482,
            18.491358,
            18.491221,
            18.491095,
            18.490941,
            18.490787,
            18.490659,
            18.490534,
            18.490412,
            18.490287,
            18.490141,
            18.490003,
            18.489881,
            18.489743,
            18.489621,
            18.489493,
            18.489358,
            18.489244,
            18.489128,
            18.489021,
            18.489008,
            18.489005,
            18.488996,
            18.488981,
            18.488956,
            18.488925,
            18.488917,
            18.488911,
            18.488909,
            18.488904,
            18.488901,
            18.488895,
            18.488888,
            18.488877,
            18.488871,
            18.488862,
            18.488853,
            18.488847,
            18.488837,
            18.488822,
            18.488805,
            18.488783,
            18.488779,
            18.488783,
            18.488789,
            18.488796,
            18.488801,
            18.488795,
            18.488792,
            18.488772,
            18.488741,
            18.488705,
            18.488673,
            18.488641,
            18.488618,
            18.488594,
            18.488577,
            18.488566,
            18.488544,
            18.488536,
            18.488522,
            18.488511,
            18.488496,
            18.488504,
            18.488506,
            18.488498,
            18.488492,
            18.488478,
            18.488462,
            18.488451,
            18.488446,
            18.488429,
            18.488396,
            18.488381,
            18.488368,
            18.488356,
            18.488351,
            18.488341,
            18.488323,
            18.488307,
            18.488295,
            18.488285,
            18.488271,
            18.488261,
            18.488251,
            18.488241,
            18.488226,
            18.488215,
            18.488201,
            18.488191,
            18.488176,
            18.488163,
            18.488151,
            18.488138,
            18.488125,
            18.488113,
            18.488102,
            18.488087,
            18.488077,
            18.488065,
            18.488048,
            18.488032,
            18.488021,
            18.488007,
            18.487991,
            18.487977,
            18.487962,
            18.487949,
            18.487936,
            18.487922,
            18.487906,
            18.487889,
            18.487868,
            18.487853,
            18.487837,
            18.487818,
            18.487805,
            18.487794,
            18.487774,
            18.487751,
            18.487741,
            18.487728,
            18.487705,
            18.487686,
            18.487666,
            18.487659,
            18.487651,
            18.487628,
            18.487597,
            18.487579
    };
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
            73.984233,
            73.985171,
            73.986111,
            73.987047,
            73.987985,
            73.988923,
            73.989863,
            73.990802,
            73.991741,
            73.992681,
            73.993621,
            73.994545,
            73.995485,
            73.996422,
            73.997362,
            73.998303,
            73.999241,
            74.000178,
            74.001117,
            74.002054,
            74.002992,
            74.003931,
            74.004869,
            74.005807,
            74.006747,
            74.007686,
            74.008623,
            74.009559,
            74.010494,
            74.011431,
            74.012369,
            74.013311,
            74.014248,
            74.015182,
            74.016123,
            74.017061,
            74.017999,
            74.018937,
            74.019876,
            74.020814,
            74.021753,
            74.022694,
            74.023636,
            74.024586,
            74.025533,
            74.026479,
            74.027428,
            74.028375,
            74.029325,
            74.030271,
            74.031222,
            74.032167,
            74.033117,
            74.034069,
            74.035017,
            74.035967,
            74.036911,
            74.037861,
            74.038811,
            74.039758,
            74.040704,
            74.041655,
            74.042601,
            74.043551,
            74.044499,
            74.045446,
            74.046396,
            74.047345,
            74.048293,
            74.049239,
            74.050189,
            74.051136,
            74.052083,
            74.053031,
            74.053981,
            74.054926,
            74.055875,
            74.056819,
            74.057771,
            74.058717,
            74.059669,
            74.060615,
            74.061561,
            74.062511,
            74.063458,
            74.064407,
            74.065356,
            74.066303,
            74.067251,
            74.068201,
            74.069148,
            74.070094,
            74.071044,
            74.071993,
            74.072939,
            74.073888,
            74.074835,
            74.075784,
            74.076734,
            74.077679,
            74.078631,
            74.079576,
            74.080526,
            74.081475,
            74.082422,
            74.083371,
            74.084321,
            74.085265,
            74.086214,
            74.087164,
            74.088111,
            74.089058,
            74.090009,
            74.090951,
            74.091902,
            74.092852,
            74.093799,
            74.094749,
            74.095696,
            74.096643,
            74.097593,
            74.098541,
            74.099488,
            74.100437,
            74.101386,
            74.102332,
            74.103281,
            74.104324,
            74.105179,
            74.106125,
            74.107073,
            74.108021,
            74.108971,
            74.109918,
            74.110865,
            74.111813,
            74.112763,
            74.113711,
            74.114661,
            74.115606,
            74.116555,
            74.117503,
            74.118452,
            74.119399,
            74.120348,
            74.121296,
            74.122244,
            74.123191,
            74.124141,
            74.125088,
            74.126034,
            74.126985,
            74.127931
    };

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
            18.494495,
            18.494366,
            18.494235,
            18.494112,
            18.493966,
            18.493844,
            18.493718,
            18.493604,
            18.493468,
            18.493346,
            18.493215,
            18.493036,
            18.492901,
            18.492773,
            18.492645,
            18.492519,
            18.492393,
            18.492263,
            18.492127,
            18.491989,
            18.491857,
            18.491729,
            18.491608,
            18.491482,
            18.491358,
            18.491221,
            18.491095,
            18.490941,
            18.490787,
            18.490659,
            18.490534,
            18.490412,
            18.490287,
            18.490141,
            18.490003,
            18.489881,
            18.489743,
            18.489621,
            18.489493,
            18.489358,
            18.489244,
            18.489128,
            18.489021,
            18.489008,
            18.489005,
            18.488996,
            18.488981,
            18.488956,
            18.488925,
            18.488917,
            18.488911,
            18.488909,
            18.488904,
            18.488901,
            18.488895,
            18.488888,
            18.488877,
            18.488871,
            18.488862,
            18.488853,
            18.488847,
            18.488837,
            18.488822,
            18.488805,
            18.488783,
            18.488779,
            18.488783,
            18.488789,
            18.488796,
            18.488801,
            18.488795,
            18.488792,
            18.488772,
            18.488741,
            18.488705,
            18.488673,
            18.488641,
            18.488618,
            18.488594,
            18.488577,
            18.488566,
            18.488544,
            18.488536,
            18.488522,
            18.488511,
            18.488496,
            18.488504,
            18.488506,
            18.488498,
            18.488492,
            18.488478,
            18.488462,
            18.488451,
            18.488446,
            18.488429,
            18.488396,
            18.488381,
            18.488368,
            18.488356,
            18.488351,
            18.488341,
            18.488323,
            18.488307,
            18.488295,
            18.488285,
            18.488271,
            18.488261,
            18.488251,
            18.488241,
            18.488226,
            18.488215,
            18.488201,
            18.488191,
            18.488176,
            18.488163,
            18.488151,
            18.488138,
            18.488125,
            18.488113,
            18.488102,
            18.488087,
            18.488077,
            18.488065,
            18.488048,
            18.488032,
            18.488021,
            18.488007,
            18.487991,
            18.487977,
            18.487962,
            18.487949,
            18.487936,
            18.487922,
            18.487906,
            18.487889,
            18.487868,
            18.487853,
            18.487837,
            18.487818,
            18.487805,
            18.487794,
            18.487774,
            18.487751,
            18.487741,
            18.487728,
            18.487705,
            18.487686,
            18.487666,
            18.487659,
            18.487651,
            18.487628,
            18.487597,
            18.487579, //Toll road ends here
            18.487552,
            18.487528,
            18.487499,
            18.48748,
            18.487451,
            18.48742,
            18.487401,
            18.487378,
            18.487361,
            18.487343



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
            73.984233,
            73.985171,
            73.986111,
            73.987047,
            73.987985,
            73.988923,
            73.989863,
            73.990802,
            73.991741,
            73.992681,
            73.993621,
            73.994545,
            73.995485,
            73.996422,
            73.997362,
            73.998303,
            73.999241,
            74.000178,
            74.001117,
            74.002054,
            74.002992,
            74.003931,
            74.004869,
            74.005807,
            74.006747,
            74.007686,
            74.008623,
            74.009559,
            74.010494,
            74.011431,
            74.012369,
            74.013311,
            74.014248,
            74.015182,
            74.016123,
            74.017061,
            74.017999,
            74.018937,
            74.019876,
            74.020814,
            74.021753,
            74.022694,
            74.023636,
            74.024586,
            74.025533,
            74.026479,
            74.027428,
            74.028375,
            74.029325,
            74.030271,
            74.031222,
            74.032167,
            74.033117,
            74.034069,
            74.035017,
            74.035967,
            74.036911,
            74.037861,
            74.038811,
            74.039758,
            74.040704,
            74.041655,
            74.042601,
            74.043551,
            74.044499,
            74.045446,
            74.046396,
            74.047345,
            74.048293,
            74.049239,
            74.050189,
            74.051136,
            74.052083,
            74.053031,
            74.053981,
            74.054926,
            74.055875,
            74.056819,
            74.057771,
            74.058717,
            74.059669,
            74.060615,
            74.061561,
            74.062511,
            74.063458,
            74.064407,
            74.065356,
            74.066303,
            74.067251,
            74.068201,
            74.069148,
            74.070094,
            74.071044,
            74.071993,
            74.072939,
            74.073888,
            74.074835,
            74.075784,
            74.076734,
            74.077679,
            74.078631,
            74.079576,
            74.080526,
            74.081475,
            74.082422,
            74.083371,
            74.084321,
            74.085265,
            74.086214,
            74.087164,
            74.088111,
            74.089058,
            74.090009,
            74.090951,
            74.091902,
            74.092852,
            74.093799,
            74.094749,
            74.095696,
            74.096643,
            74.097593,
            74.098541,
            74.099488,
            74.100437,
            74.101386,
            74.102332,
            74.103281,
            74.104324,
            74.105179,
            74.106125,
            74.107073,
            74.108021,
            74.108971,
            74.109918,
            74.110865,
            74.111813,
            74.112763,
            74.113711,
            74.114661,
            74.115606,
            74.116555,
            74.117503,
            74.118452,
            74.119399,
            74.120348,
            74.121296,
            74.122244,
            74.123191,
            74.124141,
            74.125088,
            74.126034,
            74.126985,
            74.127931, //Toll road ends here
            74.12888,
            74.129828,
            74.130774,
            74.131723,
            74.132671,
            74.133619,
            74.134565,
            74.135514,
            74.136463,
            74.13741
    };
    double latmid[] = {18.506462,
            18.505594,
            18.504695,
            18.503798,
            18.502901,
            18.500205,
            18.501107,//start below
            18.500199,
            18.500062,
            18.499911,
            18.499781,
            18.499664,
            18.499544,
            18.499417,//end below
            18.50038,
            18.501276,
            18.502174,
            18.503068,
            18.503964,
            18.504152
    };

    double lonmid[] = {
            73.94436,
            73.944164,
            73.944108,
            73.944121,
            73.944064,
            73.94408,
            73.944133,
            73.943907,
            73.944844,
            73.945778,
            73.946716,
            73.947657,
            73.948599,
            73.949536,
            73.949168,
            73.949254,
            73.949328,
            73.949417,
            73.949503,
            73.950357
    };
    int ref = 0;
    int sref = 0;
    int tmpsref = sref;
    int tmpref = ref;

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


        //Calculating the start point

        int flg = 0;
        /*for(int i = 0 ;i < latmid.length; i++)
        {
            for(int j = 0 ;j < lat.length; j++)
            {
                double dist = distance(lat[j],lon[j],latmid[i],lonmid[i]);
                if( flg == 0 && dist <= 0.07 )
                {
                    ref = j;
                    sref = i;
                    if(distance(latmid[i+1],lonmid[i+1],lat[j+1],lon[j+1])<=0.07 || distance(latmid[i+1],lonmid[i+1],lat[j+2],lon[j+2])<=0.07 || distance(latmid[i+1],lonmid[i+1],lat[j+3],lon[j+3])<=0.07){
                        flg=1;
                        Log.d("Found","lat lon : "+lat[ref]+" "+lon[ref]);
                        Toast.makeText(this,"lat lon : "+lat[ref]+" "+lon[ref],Toast.LENGTH_LONG).show();
                        break;
                    }
                   // reflon = j;
                }
            }
            if(flg==1)
                break;
        }

        //Calculating the end point

        for(int i = tmpsref;i < latmid.length;i++)
        {
            for(int j = tmpref;j< lat.length;j++)
            {
                double dist = distance(lat[j],lon[j],latmid[i],lonmid[i]);

                if(dist <= 0.07)
                {
                    tmpsref = i;
                    if(j+1==lat.length)
                        tmpref = j;
                    else
                        tmpref = j+1;
                    Log.d("Getting point","lat lon : "+lat[tmpref]+" "+lon[tmpref]+" "+tmpref+" "+tmpsref+" "+latmid[tmpsref]+" "+lonmid[tmpsref]);

                    break;
                }

            }

        }*/

        for(int i = 0 ;i < slat.length; i++)
        {
            for(int j = 0 ;j < lat.length; j++)
            {
                double dist = distance(lat[j],lon[j],slat[i],slon[i]);
                if( flg == 0 && dist <= 0.07 )
                {
                    ref = j;
                    sref = i;
                    if(distance(slat[i+1],slon[i+1],lat[j+1],lon[j+1])<=0.07 || distance(slat[i+1],slon[i+1],lat[j+2],lon[j+2])<=0.07 || distance(slat[i+1],slon[i+1],lat[j+3],lon[j+3])<=0.07){
                        flg=1;
                        Log.d("Found","lat lon : "+lat[ref]+" "+lon[ref]);
                        Toast.makeText(this,"lat lon : "+lat[ref]+" "+lon[ref],Toast.LENGTH_LONG).show();
                        break;
                    }
                    // reflon = j;
                }
            }
            if(flg==1)
                break;
        }

        //Calculating the end point

        for(int i = tmpsref;i < slat.length;i++)
        {
            for(int j = tmpref;j< lat.length;j++)
            {
                double dist = distance(lat[j],lon[j],slat[i],slon[i]);

                if(dist <= 0.07)
                {
                    tmpsref = i;
                    if(j+1==lat.length)
                        tmpref = j;
                    else
                        tmpref = j+1;
                    Log.d("Getting point","lat lon : "+lat[tmpref]+" "+lon[tmpref]+" "+tmpref+" "+tmpsref+" "+slat[tmpsref]+" "+slon[tmpsref]);

                    break;
                }

            }

        }

        Log.d("Toll Exit","lat lon : "+lat[tmpref]+" "+lon[tmpref]);
        Toast.makeText(this,"lat lon : "+lat[tmpref]+" "+lon[tmpref],Toast.LENGTH_LONG).show();


       Log.d("Toll cost","Total cost = "+tollcalc());



        return super.onStartCommand(intent, flags, startId);
    }

    //Calculate Toll
    public double tollcalc()
    {
       double toll = 0;
        for(int i=0;i<slat.length;i++)
        {
            for(int j=0;j<tolllat.length;j++)
            {
                double dist = distance(slat[i],slon[i],tolllat[j],tolllon[j]);
                if(dist <= 1.0) {
                    int k;
                    for (k = 0; k < lat.length; k++)
                    {
                        if(lat[k]==tolllat[j])
                        {
                            break;
                        }
                    }
                    toll = k-ref;
                    toll*=0.1*2;

                    Log.d("Toll k ","Toll "+k+" "+ref);
                }
            }
        }

        return toll;
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
