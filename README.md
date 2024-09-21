# TrackingApp

### Description:
**TrackingApp** is an Android application that continuously collects **SMS logs**, **Call logs**, and **GEO-location data** from a device. The data is stored locally on the phone in JSON format and the GEO-location data is periodically sent to a server for tracking purposes. The app ensures data privacy by allowing users to control permission access and sending data securely.

### Features:
- Collects **SMS** logs, including back-and-forth conversations, and stores them in JSON format.
- Captures **Call logs** with detailed information (contact name or number, type of call, and duration).
- Tracks the device's **GEO-location** and sends updates to a remote server every 2 minutes.
- Stores logs locally in the **Documents** folder on the device in JSON files (`sms_logs.json`, `call_logs.json`, `geo_location.json`).
- Permission management ensures the user grants access for SMS, Call logs, and location tracking.

### JSON Format:
1. **SMS Logs**:
   ```json
   {
     "2024-09-21": [
       {
         "sender": {
           "contact_name": "John Doe",
           "type": "received",
           "body": "Hello, this is a test message"
         }
       },
       {
         "sender": {
           "contact_name": "Me",
           "type": "sent",
           "body": "Hi, thanks for the message!"
         }
       }
     ]
   }
   ```

2. **Call Logs**:
   ```json
   {
     "2024-09-21": [
       {
         "contact_name": "Jane Doe",
         "type": "missed",
         "duration": 120
       }
     ]
   }
   ```

3. **GEO-location**:
   ```json
   {
     "latitude": 37.4219983,
     "longitude": -122.084,
     "date": "2024-09-21"
   }
   ```

### Prerequisites:
- **Android Studio** for development
- **Namecheap Hosting** for server setup to receive GEO-location data
- Required Android permissions: `READ_CALL_LOG`, `READ_SMS`, `ACCESS_FINE_LOCATION`, `WRITE_EXTERNAL_STORAGE`


### Usage:
- Upon launching, the app will begin collecting **SMS** and **Call logs**, which are saved locally in JSON files.
- GEO-location updates are sent to the server every 2 minutes.
- To view the stored data, check the `Documents` folder on the device, where the JSON files will be saved.

### Roadmap:
- **Enhance security**: Add encryption for sending data to the server.
- **Data filtering**: Send only new SMS and Call logs to reduce data size.
- **Server setup**: Create a storage system to efficiently store logs in the cloud.

### License:
This project is licensed under the MIT License - see the LICENSE file for details.
