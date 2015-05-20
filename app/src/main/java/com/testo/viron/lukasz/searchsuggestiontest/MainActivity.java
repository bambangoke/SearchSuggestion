package com.testo.viron.lukasz.searchsuggestiontest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import com.testo.viron.lukasz.tables.BusStop;


public class MainActivity extends ActionBarActivity {
    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String[] stopNames = new String[]{
            "Adaś","Maciek","Ola","Pola","Jadzia","Iga", "Ala","Magda","Paulina","Arek","Michaś",
            "Dominika","Filip","Paweł","Krzyś","Mateusz","Patryk", "Łukasz","Mariusz","Max","Mietek",
            "Sebastian", "Joanna", "Gabrysia", "Ania","Grzesiek","Piotrek", "Igor","Walentyna","Jan",
            "Grażyna","Bożena","Agnieszka","Bartek","Bartłomiej","Andrzej","Ewa","Ambroży","Jerzy","Szczepan",
            "Krystyna","Paula","Barbara","Benjamin","January","Tomek","Damian","Dawid","Darek","Jarek","Krystian",
            "Kuba","Zuzanna","Kryspin","Katarzyna","Szymon","Martyna","Gosia","Henryk","Halina","Ramona","Remek","Ryszard",
            "Wanda","Zenon","Włodzimierz","Zbyszek","Janusz","Jowita","Lolek","Bolek","Lucjan","Sabina","Karina",
            "Wiesiek","Stefan","Rachela","Konstanty","Kalina"
    };
//    private static final String[] stopNames = new String[]{"Nie mam pojęcia...","Nie wiem co wpisać","Biprostal", "Czarnowiejska", "AGH", "Urzędnicza", "Kawiory", "Dworzec główny",
//            "Miasteczko Studenckie", "Okulickiego", "AWF", "Rondo Matecznego", "Salwador"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, " init busStops:");
        initBusStops();
        Log.d(TAG, " busStops initialized");

        SearchView searchview = (SearchView) findViewById(R.id.searchview);

        SearchSuggestionUtils.getInstance().init(this, searchview);
        Log.d(TAG, " searchView set-up");

    }

    private void initBusStops() {
        Log.d(TAG," init BusStops");
        BusStop.deleteAll(BusStop.class);
        for (int j = 0; j < stopNames.length; j++) {
            Log.i(TAG," saving: "+ stopNames[j]);

            BusStop a = new BusStop(stopNames[j]);
            a.save();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
