/*******************************************************************************
 * Copyright 2009 OmniDroid - http://code.google.com/p/omnidroid 
 *  
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0 
 *     
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 *******************************************************************************/
package edu.nyu.cs.omnidroid.ui;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.nyu.cs.omnidroid.R;
import edu.nyu.cs.omnidroid.util.AGParser;
import edu.nyu.cs.omnidroid.util.StringMap;
import edu.nyu.cs.omnidroid.util.UGParser;

/**
 * Activity used to present a list of filter types available to apply to this OmniHandler.
 * FilterTypes present the type of data that an OmniHandler can filter by. For example a
 * PhoneNumber, or an EmailAddress.
 * 
 */
public class FiltersAddType extends ListActivity {
  // Standard Menu options (Android menus require int, so no enums)
  private static final int MENU_HELP = 0;

  // Intent passed Data
  private String eventApp;
  private String eventName;

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreate(android.os.Bundle)
   */
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Initialize our AGParser
    AGParser ag = new AGParser(this);

    // Get data passed to this activity
    Intent i = getIntent();
    Bundle extras = i.getExtras();
    eventApp = extras.getString(AGParser.KEY_APPLICATION);
    eventName = extras.getString(UGParser.KEY_EVENT_TYPE);

    // Getting the list of Filters available from AppConfig
    ArrayList<String> filterTypeList = ag.readFilters(eventApp, eventName);

    // Convert filter to OmniDroid common name
    ArrayList<StringMap> contentMap = ag.readContentMap(eventApp);
    ArrayList<StringMap> filterInputs = new ArrayList<StringMap>();
    for (int cnt = 0; cnt < filterTypeList.size(); cnt++) {
      for (StringMap item : contentMap) {
        if (item.getKey().equals(filterTypeList.get(cnt))) {
          filterInputs.add(new StringMap(filterTypeList.get(cnt), item.getValue()));
        }
      }
    }

    // Setup our list view
    ArrayAdapter<StringMap> arrayAdapter = new ArrayAdapter<StringMap>(this,
        android.R.layout.simple_list_item_1, filterInputs);
    setListAdapter(arrayAdapter);
    getListView().setTextFilterEnabled(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int,
   * long)
   */
  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    StringMap sm = (StringMap) l.getAdapter().getItem(position);
    // String filterType = (StringMap)(l.getAdapter().getItem(position)).getKey();
    String filterType = sm.getKey();
    Intent i = new Intent();
    i.setClass(this.getApplicationContext(), FiltersAddData.class);
    i.putExtra(AGParser.KEY_APPLICATION, eventApp);
    i.putExtra(UGParser.KEY_EVENT_TYPE, eventName);
    // i.putExtra(UGParser.KEY_FilterType, sm.getKey());
    i.putExtra(UGParser.KEY_FILTER_TYPE, filterType);
    startActivityForResult(i, Constants.RESULT_ADD_FILTER);
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
   */
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    switch (requestCode) {
    case Constants.RESULT_ADD_FILTER:
      switch (resultCode) {
      case Constants.RESULT_SUCCESS:
        setResult(resultCode, data);
        finish();
        break;
      }
      break;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
   */
  public boolean onCreateOptionsMenu(Menu menu) {
    menu.add(0, MENU_HELP, 0, R.string.help).setIcon(android.R.drawable.ic_menu_help);
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
   */
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
    case MENU_HELP:
      help();
      return true;
    }
    return false;
  }

  /**
   * Call our Help dialog
   */
  private void help() {
    Builder help = new AlertDialog.Builder(this);
    String help_msg = this.getResources().getString(R.string.help_filters_add_type);
    help.setTitle(R.string.help);
    help.setIcon(android.R.drawable.ic_menu_help);
    help.setMessage(Html.fromHtml(help_msg));
    help.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {
      }
    });
    help.show();
  }

}