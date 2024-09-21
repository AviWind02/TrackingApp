package DataCollection;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;
import android.util.Log;

import androidx.core.content.ContextCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
public class Call {

    public JSONObject getCallLogsAsJSON(Context context) {
        JSONObject callLogsByDate = new JSONObject();

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);

            if (cursor != null) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                while (cursor.moveToNext()) {
                    @SuppressLint("Range") String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                    @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                    @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE));
                    @SuppressLint("Range") String duration = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DURATION));

                    // Convert timestamp to date
                    String callDate = dateFormat.format(new Date(Long.parseLong(date)));

                    // Determine call type
                    String callType;
                    switch (Integer.parseInt(type)) {
                        case CallLog.Calls.INCOMING_TYPE:
                            callType = "in";
                            break;
                        case CallLog.Calls.OUTGOING_TYPE:
                            callType = "out";
                            break;
                        case CallLog.Calls.MISSED_TYPE:
                            callType = "missed";
                            break;
                        case CallLog.Calls.REJECTED_TYPE:
                            callType = "canceled";
                            break;
                        default:
                            callType = "unknown";
                    }

                    try {
                        // Create a JSON entry for this call
                        JSONObject callEntry = new JSONObject();
                        callEntry.put("contact_name", number);
                        callEntry.put("type", callType);
                        callEntry.put("duration", duration);

                        // Add to the respective date entry
                        if (!callLogsByDate.has(callDate)) {
                            callLogsByDate.put(callDate, new JSONArray());
                        }
                        callLogsByDate.getJSONArray(callDate).put(callEntry);
                    } catch (Exception e) {
                        Log.e("Call", "Failed to process call log", e);
                    }
                }
                cursor.close();
            }
        }

        return callLogsByDate;  // Return the JSON object
    }
}
