package com.testo.viron.lukasz.searchsuggestiontest;


import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lukasz Marczak on 2015-05-18.
 */
public class SearchSuggestionAdapter extends CursorAdapter {

    private static final String TAG = "SearchSuggestionAdapter";

    private List<String> items;

    private TextView text;

    public SearchSuggestionAdapter(Context context, Cursor cursor, List items) {
        // super(context, cursor);
        super(context, cursor, false);


        this.items = items;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.search_suggestion_item, parent, false);

        text = (TextView) view.findViewById(R.id.search_suggestion_item_text);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Show list item data from cursor
        text.setText(items.get(cursor.getPosition()));

        Log.d(TAG, "bindView text is: " + items.get(cursor.getPosition()));

    }
}
