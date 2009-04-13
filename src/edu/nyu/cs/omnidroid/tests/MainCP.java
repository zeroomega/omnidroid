package edu.nyu.cs.omnidroid.tests;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.core.CP;

public class MainCP extends Activity {
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main2);
    Button Store;
    final EditText Adata;
    final EditText ID;
    final EditText Iname;
    Button Retrieve;
    Button GetAll;

    Store = (Button) findViewById(R.id.Store);
    Iname = (EditText) findViewById(R.id.Iname);
    Adata = (EditText) findViewById(R.id.Adata);
    Retrieve = (Button) findViewById(R.id.Retrieve);
    ID = (EditText) findViewById(R.id.ID);
    GetAll = (Button) findViewById(R.id.GetAll);
    
    Store.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String iname = Iname.getText().toString();
        String adata = Adata.getText().toString();

        if (iname.length() > 0 && adata.length() > 0) {
          ContentValues values = new ContentValues();
          values.put("i_name", iname);
          values.put("a_data", adata);
          Uri uri = getContentResolver().insert(
              Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP"), values);
          Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_SHORT).show();
          Log.d("Insert Complete", "This is a log");
          Toast.makeText(getBaseContext(), "Good Job", Toast.LENGTH_SHORT).show();

        /*  // SENDING THE INTENT
          Intent intent = new Intent(text1);
          sendBroadcast(intent);
*/
        } else

          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();

      }
    });

    Retrieve.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        String id = ID.getText().toString();
        if (id.length() > 0) {
          Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP");
          Cursor c = managedQuery(OmniURI, null, null, null, null);
          int new_id = Integer.parseInt(id);

          if (c.moveToPosition(new_id)) {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(CP._ID)) + ", "
                    + c.getString(c.getColumnIndex(CP.INSTANCE_NAME)) + ", "
                    + c.getString(c.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
          }
          Toast.makeText(getBaseContext(), "Good Job 2", Toast.LENGTH_SHORT).show();
        } else

          Toast.makeText(getBaseContext(), "No Value", Toast.LENGTH_SHORT).show();

      }
    });

    GetAll.setOnClickListener(new View.OnClickListener() {
      public void onClick(View v) {
        Uri OmniURI = Uri.parse("content://edu.nyu.cs.omnidroid.core.maincp/CP");
        Cursor c = managedQuery(OmniURI, null, null, null, null);
        if (c.moveToFirst()) {
          do {
            Toast.makeText(
                getBaseContext(),
                c.getString(c.getColumnIndex(CP._ID)) + ", "
                    + c.getString(c.getColumnIndex(CP.INSTANCE_NAME)) + ", "
                    + c.getString(c.getColumnIndex(CP.ACTION_DATA)), Toast.LENGTH_LONG).show();
          } while (c.moveToNext());
        }

      }

    });

  }

}