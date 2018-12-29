package thinkhattke.gaurav.vjti;

/*
FEATURES:
- One Line Implementation
- Simple Calls To Retreive Latitude & Longitude In DOUBLE Format
- Simple Calls To Retreive Latitude & Longitude In STRING Format
- Detects GPS And Location Services Availability
- Redirects To The Location Settings If Services Found Disabled
- Returns Location Accurate Upto 10 Metres
- Returns Location Offline - Without Internet Connectivity; COURTESY - OffPay
- Returns Last Known Location In Case Of No Network Connectivity
- Customizable & Modifiable For Easy Reactive Use
- Custom Listeners & Easily Usable As A Broadcast Receiver Or Unlimited Cycle Service
USAGE:
- Initialize It Your JAVA Class Like This:
    GPSTracker gps = new GPSTracker(getApplicationContext());
- Custom Function To Handle Returns
    private void getCoordinates() {
      if (gps.canGetLocation()) {
        Double dlatitude = gps.getLatitude();
        Double dlongitude = gps.getLongitude();
        String slatitude = gps.getStringLatitude();
        String slongitude = gps.getStringLongtitude();
      }
      else {
        gps.showSettingsAlert();
      }
  }
*/



/*
MIT License
Copyright (c) 2017 Rishabh Singh
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/


import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {

    // Declarations: Datatype Declarations For Global Usage

    private final Context mContext;

    boolean isGPSEnabled = false;

    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location;

    double latitude;

    double longitude;

    String slatitude;

    String slongitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;


    // Constructor: Initializer Requiring Context As A Parameter; Usage: GPSTracker gps = new GPSTracker(getApplicationContext());

    public GPSTracker(Context context) {

        this.mContext = context;

        getLocation();

    }


    // Attempts To Retreive Current Location Statistics If GPS Connectivity Is Available. Else, Returns Last Known Coordinates.

    public Location getLocation() {

        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {

                showSettingsAlert();

            } else {

                this.canGetLocation = true;

                if (isNetworkEnabled) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {

                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {

                            latitude = location.getLatitude();

                            longitude = location.getLongitude();

                        }

                    }

                }

                if (isGPSEnabled) {

                    if (location == null) {

                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null) {

                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {

                                latitude = location.getLatitude();

                                longitude = location.getLongitude();

                            }

                        }

                    }

                }

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

        return location;

    }


    // Stops The Continuos GPS Tracing In Order To Reduce Memory Lag, If Any.

    public void stopUsingGPS() {

        if (locationManager != null){

            locationManager.removeUpdates(GPSTracker.this);

        }

    }


    // Returns TRUE If Location Services Works Fine. IMPORTANT: To Be Called Before Calling For Longitude Or Latitude, Otherwise They Might Return NULL!

    public boolean canGetLocation() {

        return this.canGetLocation;

    }


    // Returns The Current Latitude In DOUBLE Format, If boolean canGetLocation == true

    public double getLatitude() {

        if (location != null) {

            latitude = location.getLatitude();

        }

        return latitude;

    }


    // Returns The Current Longitude In DOUBLE Format, If boolean canGetLocation == true

    public double getLongitude() {

        if (location != null) {

            longitude = location.getLongitude();

        }

        return longitude;

    }


    // Returns The Current Latitude In STRING Format, If boolean canGetLocation == true

    public String getStringLatitude() {

        if (location != null) {

            slatitude = Double.toString(location.getLatitude());

        }

        return slatitude;

    }


    // Returns The Current Latitude In STRING Format, If boolean canGetLocation == true

    public String getStringLongtitude() {

        if (location != null){

            slongitude = Double.toString(location.getLongitude());

        }

        return slongitude;

    }


    // If Location Or Network Services Are Disabled, Then Asks To Enable GPS From Settings

    public void showSettingsAlert() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("GPS is settings");

        alertDialog.setMessage("GPS Not Enables. Enable It From Settings Menu?");

        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog,int which) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                mContext.startActivity(intent);

            }

        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }

        });

        alertDialog.show();

    }


    // Other Receivers; Can Be Used Accordingly, If Required.

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {

        return null;

    }

}
