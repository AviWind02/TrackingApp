package DataCollection;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GPS {

    private FusedLocationProviderClient fusedLocationClient;

    public GPS() {
        // Empty constructor
    }

    public void getLastKnownLocation(Context context, LocationResultCallback callback) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            callback.onLocationResult(convertLocationToJson(location));
                        } else {
                            Log.d("GPS", "Location is null");
                            callback.onLocationResult(null);
                        }
                    });
        }
    }

    // Method to convert location data to JSON format
    private JSONObject convertLocationToJson(Location location) {
        JSONObject locationData = new JSONObject();
        try {
            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            locationData.put("latitude", location.getLatitude());
            locationData.put("longitude", location.getLongitude());
            locationData.put("date", date);
        } catch (Exception e) {
            Log.e("GPS", "Failed to process location", e);
        }

        return locationData;
    }

    public interface LocationResultCallback {
        void onLocationResult(JSONObject locationJson);
    }
}
