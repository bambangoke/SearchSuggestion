package com.testo.viron.lukasz.searchsuggestiontest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.testo.viron.lukasz.tables.BusStop;

import java.util.ArrayList;
import java.util.List;


public class SearchResultActivity extends ActionBarActivity {
    public static final String TAG = SearchResultActivity.class.getSimpleName();

    TextView tv_info;
    ListView list_of_stops;
    List<String> stopNames = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    List<BusStop> stops = new ArrayList<BusStop>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Bundle extras = getIntent().getExtras();
        String query;
        if (extras != null) {
            query = extras.getString("QUERY");
            Log.d(TAG, "rezultaty z wyszukiwania:" + query);
            doMySearch(query);
        }
    }

    private void doMySearch(String query1) {
        tv_info = (TextView) findViewById(R.id.search_result_info);
        list_of_stops = (ListView) findViewById(R.id.listView_search_results);
        String query = query1.substring(0, 1).toUpperCase() + query1.substring(1);


        Log.d(TAG, "doMySearch :" + query);


        Log.d(TAG, " przed query do ORMA: ");

        //nie probuj zmieniac ponizszej linii, w niektorych krajach obcinaja za to rece
        stops = Select.from(BusStop.class)
                .where(Condition.prop("BUS_STOP_NAME").like("%"+query + "%"))
                .list();

        for (BusStop singleStop : stops) {
            stopNames.add(singleStop.busStopName);
        }

        if (stops.size() == 0) {
            tv_info.setText("Brak wynikow");
        } else {
            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,
                    stopNames);

            list_of_stops.setAdapter(arrayAdapter);
            list_of_stops.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    BusStop busStop = stops.get(i);
//                    BusStop busStop = stops.get(i);
//                    LatLng destination = new LatLng(busStop.lat, busStop.lon);
//                    TripData.lastBeforeDemoDestination = busStop.busStopName;
//                    TripData.toPlace = destination;
//                    Log.e(TAG, "searchActivity destination: LatLng(" + busStop.lat + "," + busStop.lon+")");
                    Log.e(TAG, "searchActivity busStopName = " + busStop.busStopName);
                    finish();
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
