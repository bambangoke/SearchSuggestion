package com.testo.viron.lukasz.searchsuggestiontest;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.util.Log;
import android.widget.SearchView;

import com.orm.query.Condition;
import com.orm.query.Select;
import com.testo.viron.lukasz.tables.BusStop;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lukas on 2015-05-20.
 */
public class SearchSuggestionUtils {
    public static final String TAG = SearchSuggestionUtils.class.getSimpleName();
    private static final int CHARACTERS =2;//actually, it pops down after 3 characters inserted

    private static final List<String> emptySet = new ArrayList<>();
    private SearchView search = null;
    private Context context;
    private static final int MAX_SUGGESTION_SEARCH = 10;
    private static List<BusStop> mSuggestionsList = new ArrayList<>();
    private Thread mThread;
    private static SearchSuggestionUtils instance = null;

    private void getDestinationFromSuggestion(int position) {
        if (mSuggestionsList.size() == 0 || (mSuggestionsList.size() < position))
            return;

        String selected = mSuggestionsList.get(position).busStopName;

        search.setQuery(selected, false);
    }

    public Cursor getSuggestionsCursor() {
        Log.d(TAG, " inside getSuggestionsCursor");
        // Load data from list to cursor
        String[] columns = new String[]{"_ID", "BUS_STOP_NAME"};
        Object[] temp = new Object[]{0, "default"};

        // Create a new Cursor object
        MatrixCursor cursor = new MatrixCursor(columns);


        int size = (mSuggestionsList.size() < MAX_SUGGESTION_SEARCH) ? mSuggestionsList.size() : MAX_SUGGESTION_SEARCH;
        for (int i = 0; i < size; i++) {
            temp[0] = i;
            temp[1] = mSuggestionsList.get(i).busStopName;
            // Add the  Google Place data as a row in the Cursor object
            cursor.addRow(temp);
        }

        Log.d(TAG, "suggestions size is: " + mSuggestionsList.size());

        return cursor;
    }

    /**
     * metoda wywołująca się za każdym razem gdy zmieni się tekst wpisywany w wyszukiwarce
     *
     * @param input
     */
    public void autoComplete(String input) {
        Log.d(TAG, " inside autoComplete(" + input + ")");

        final String query = input;
        if (input.length() >= CHARACTERS) {
            if (mThread != null) {
                mThread = null;
            }
            mThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    //suggestionsSet wypluwa listę podpowiedzi, na podstawie query z bazy danych
                    final List<BusStop> suggestionsSet = Select.from(BusStop.class)
                            .where(Condition.prop("BUS_STOP_NAME").like("%" + query + "%"))
                            .list();


                    ArrayList<String> suggestedBusStopNames = new ArrayList<>();
                    for (BusStop s : suggestionsSet) {
                        suggestedBusStopNames.add(s.busStopName);
                    }
                    mSuggestionsList = suggestionsSet;
//                    TripData.lastBeforeDemoBusStop = mSuggestionsList.get(0);
                    final ArrayList<String> finalSet = suggestedBusStopNames;

                    // Update the SearchView's suggestions
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Cursor cursor = getSuggestionsCursor();
                            search.setSuggestionsAdapter(new SearchSuggestionAdapter(getActivity(), cursor, finalSet));
                            Log.d(TAG, "inside autocomplete setShowDividers: ");
//                            search.setSearchableInfo(SearchManager.);
                        }
                    });
                    Log.d(TAG, "suggestions list is: " + mSuggestionsList);
                }
            });
            mThread.start();
        } else if (input.length() < CHARACTERS) {
            mSuggestionsList.clear();
            Cursor cursor = getSuggestionsCursor();
//            final ArrayList<String> finalSet = new ArrayList<>();
//            finalSet.clear();
            search.setSuggestionsAdapter(new SearchSuggestionAdapter(getActivity(), cursor, emptySet));
        }
        getActivity().onSearchRequested();

    }

    private void initSearchView() {
        final Intent intent = new Intent(context, SearchResultActivity.class);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchManager.getSearchablesInGlobalSearch();
        //dummy item for performance, spinner deleted(hidden)

        search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        //search.setQuery("", false);
        //search.clearFocus();
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                Log.d(TAG, " inside onQueryTextSubmit (" + text + ")");
                intent.putExtra("QUERY", text);
                getActivity().startActivityForResult(intent, 0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, " inside onQueryTextChange(" + newText + ")");

                autoComplete(newText);
                return true;
            }
        });
        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int i) {
                Log.d(TAG, " inside onSuggestionSelect (" + i + ")");
                getDestinationFromSuggestion(i);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int i) {
                Log.d(TAG, " inside onSuggestionClick (" + i + ")");
                getDestinationFromSuggestion(i);
                return true;
            }
        });
    }

    public void init(MainActivity mainActivity, SearchView searchview) {
        search = searchview;
        context = mainActivity;
        initSearchView();
    }

    private Activity getActivity() {
        return ((Activity) context);
    }

    private SearchSuggestionUtils() {
    }

    public static SearchSuggestionUtils getInstance() {
        if (instance == null)
            instance = new SearchSuggestionUtils();
        return instance;
    }
}
