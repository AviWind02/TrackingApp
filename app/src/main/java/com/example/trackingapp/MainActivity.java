package com.example.trackingapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import DataCollection.Call;
import DataCollection.GPS;
import DataCollection.SMS;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1001; // Request code for permissions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Check and request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_CALL_LOG,
                            Manifest.permission.READ_SMS,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_CODE);
        } else {
            // Permissions are already granted
            collectAndSaveData();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                // Permissions are granted
                collectAndSaveData();
            } else {
                Log.d("MainActivity", "Permissions not granted.");
            }
        }
    }

    // Method to collect and save data
    private void collectAndSaveData() {
        // Initialize collectors
        Call callCollector = new Call();
        SMS smsCollector = new SMS();
        GPS gpsCollector = new GPS();

        // Get the JSON data
        JSONObject smsData = smsCollector.getSMSLogsAsJSON(this);  // Use JSON method for SMS logs
        JSONObject callData = callCollector.getCallLogsAsJSON(this);  // Use JSON method for call logs

        // Get geo-location and save everything to files
        gpsCollector.getLastKnownLocation(this, locationJson -> {
            // Save the JSON data to files
            saveJSONToFile("sms_logs.json", smsData);
            saveJSONToFile("call_logs.json", callData);
            saveJSONToFile("geo_location.json", locationJson);  // Geo location JSON
        });
    }

    // Method to save the collected data in JSON format to a file
    private void saveJSONToFile(String fileName, JSONObject jsonData) {
        // Create a directory for the app on external storage
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, fileName);

        try {
            if (!path.exists()) {
                path.mkdirs();  // Create the directory if it doesn't exist
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(jsonData.toString().getBytes());
            fos.close();
            Log.d("MainActivity", "Data saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            Log.e("MainActivity", "Failed to save data to file", e);
        }
    }
}
