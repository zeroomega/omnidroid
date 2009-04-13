package edu.nyu.cs.omnidroid.bkgservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.OmLogger;
import edu.nyu.cs.omnidroid.util.UGParser;

public class BRService extends Service {
  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onBind(android.content.Intent)
   */
  IntentFilter Ifilter = new IntentFilter();
  BroadcastReceiver BR = new BCReceiver();

  @Override
  public IBinder onBind(Intent arg0) {
    // TODO Auto-generated method stub
    return null;
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate() {
    try {

      // Initializing the UGParser. To be deleted from this Code after Andrews Module
      UGParser ug = new UGParser(getApplicationContext());
      ug.update(getApplicationContext(), "Enabled", "True");
      ug.delete_all();
      HashMap<String, String> HM = new HashMap<String, String>();
      HM.put("InstanceName", "SMS to Email");
      HM.put("EventName", "SMS_RECEIVED");
      HM.put("EventApp", "SMS");
      HM.put("FilterType", "S_PhoneNum");
      HM.put("FilterData", "212-555-1234");
      HM.put("ActionName", "SEND_EMAIL");
      HM.put("ActionApp", "EMAIL");
      HM.put("ActionData", "ContentProvider_URI");
      HM.put("EnableInstance", "True");
      ug.writeRecord(HM);
      HM.put("InstanceName", "SMS to Email2");
      HM.put("EventName", "SMS_RECEIVED");
      HM.put("EventApp", "SMS");
      HM.put("FilterType", "S_PhoneNum");
      HM.put("FilterData", "212-555-1234");
      HM.put("ActionName", "SEND_EMAIL");
      HM.put("ActionApp", "EMAIL");
      HM.put("ActionData", "ContentProvider_URI");
      HM.put("EnableInstance", "True");
      ug.writeRecord(HM);
      /* Code demonstrating use of Appl Config parser; To be deleted */
      AGParser ag = new AGParser(getApplicationContext());
      ag.delete_all();
      ag.agwrite("Application:SMS");
      ag.agwrite("EventName:SMS_RECEIVED,RECEIVED SMS");
      ag.agwrite("Filters:S_Name,S_Ph_No,Text,Location");
      ag.agwrite("EventName:SMS_SENT,SENT SMS");
      ag.agwrite("Filters:R_Name,R_Ph_no,Text");
      ag.agwrite("ActionName:SMS_SEND,SEND SMS");
      ag.agwrite("URIFields:R_NAME,R_Ph_No,Text");
      ag.agwrite("ContentMap:");
      ag.agwrite("S_Name,SENDER NAME,STRING");
      ag.agwrite("R_Name,RECEIVER NAME,STRING ");
      ag.agwrite("S_Ph_No,SENDER PHONE NUMBER,INT");
      ag.agwrite("R_Ph_No,RECEIVER PHONE NUMBER,INT");
      ag.agwrite("Text,Text,STRING");
      ag.agwrite("Location,SMS Number,INT");

      // Getting the Events from AppConfig
      ArrayList<HashMap<String, String>> eArrayList = ag.readEvents("SMS");
      Iterator<HashMap<String, String>> i1 = eArrayList.iterator();
      while (i1.hasNext()) {
        HashMap<String, String> HM1 = i1.next();
        // Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      }

      // Getting the Actions from AppConfig
      ArrayList<HashMap<String, String>> aArrayList = ag.readActions("SMS");
      Iterator<HashMap<String, String>> i2 = aArrayList.iterator();
      while (i2.hasNext()) {
        HashMap<String, String> HM1 = i2.next();
        // Toast.makeText(getBaseContext(), HM1.toString(), 5).show();
      }

      // Getting the Filters from AppConfig
      ArrayList<String> FilterList = ag.readFilters("SMS", "SMS_RECEIVED");
      Iterator<String> i3 = FilterList.iterator();
      while (i3.hasNext()) {
        String filter = i3.next();
        // Toast.makeText(getBaseContext(), filter.toString(), 5).show();
      }

      // Getting the ContentMap from AppConfig
      ArrayList<String[]> contentmap = ag.readContentMap("SMS");
      Iterator<String[]> i4 = contentmap.iterator();
      while (i4.hasNext()) {
        String[] fieldmap = i4.next();
        // Toast.makeText(getBaseContext(),
        // fieldmap[0].toString()+fieldmap[1].toString()+fieldmap[2].toString(), 5).show();
      }

      // Code starts here. The above code above this line is temporary usage.

      /* Check the User Config to start OmniDroid */
      String Enabled = ug.readLine("Enabled");
      if (Enabled.equalsIgnoreCase("True")) {
        Toast.makeText(getBaseContext(), "Starting OmniDroid", 5).show();

        // Get the User Instances in an Arraylist from the User Config
        ArrayList<HashMap<String, String>> UCRecords = ug.readRecords();
        Iterator<HashMap<String, String>> i = UCRecords.iterator();
        while (i.hasNext()) {
          HashMap<String, String> HM1 = i.next();
          // Configure the Intent Filter with the Events if Instance in enabled
          if (HM1.get("EnableInstance").equalsIgnoreCase("True"))
            Ifilter.addAction(HM1.get("EventName"));
        }
        registerReceiver(BR, Ifilter);
      } else {
        Toast.makeText(getBaseContext(), "Stopping OmniDroid", 5).show();
        unregisterReceiver(BR);
      }
    } catch (Exception e) {
      Log.i("BRService", e.getLocalizedMessage());
      Log.i("BRService", e.toString());
      OmLogger.write(getApplicationContext(), "Not able to Enable/Diable Omnidroid");
      // Logger.write("Unable to start BroadcastReceiver");
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Service#onDestroy()
   */
  @Override
  public void onDestroy() {
    // TODO Auto-generated method stub
    super.onDestroy();

  }
}