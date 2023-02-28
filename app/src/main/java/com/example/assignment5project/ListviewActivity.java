package com.example.assignment5project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ListviewActivity extends AppCompatActivity {

    private ArrayList<Wonders> items;
    private ArrayAdapter<Wonders> adapter;
    //private Wonders[] wonder;
    private WebView mySecondWebView;
    private SharedPreferences myPreferenceRef;
    private SharedPreferences.Editor myPreferenceEditor;
    private TextView textView;
    private SQLiteDatabase database;
    private DatabaseHelper databaseHelper;

    // Inserts into database
    private long insertWonders(Wonders m) {
        ContentValues values = new ContentValues();
        values.put(DatabaseTables.Wonders.COLUMN_NAME_NAME, m.getName());
        values.put(DatabaseTables.Wonders.COLUMN_NAME_COMPANY, m.getCompany());
        values.put(DatabaseTables.Wonders.COLUMN_NAME_LOCATION, m.getLocation());
        values.put(DatabaseTables.Wonders.COLUMN_NAME_CATEGORY, m.getCategory());
        values.put(DatabaseTables.Wonders.COLUMN_NAME_AUXDATA, m.getAuxdata());
        return database.insert(DatabaseTables.Wonders.TABLE_NAME, null, values);
    }

    private List<Wonders> fetchWonders() {
        myPreferenceEditor.putString("filter", "Filtered");
        myPreferenceEditor.apply();

        //  results WHERE "title" = 'My Title'
        String selection = DatabaseTables.Wonders.COLUMN_NAME_CATEGORY + " = ?";
        String[] selectionArgs = { "City" };

        Cursor cursor = database.query(
                DatabaseTables.Wonders.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        List<Wonders> wonders = new ArrayList<>();
        while (cursor.moveToNext()) {
            Wonders wonder = new Wonders(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_COMPANY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_AUXDATA))
            );
            wonders.add(wonder);
        }
        cursor.close();
        return wonders;
    }

    private List<Wonders> fetchAllWonders() {
        myPreferenceEditor.putString("filter", "Unfiltered");
        myPreferenceEditor.apply();

        Cursor cursor = database.query(
                DatabaseTables.Wonders.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
        List<Wonders> wonders = new ArrayList<>();
        while (cursor.moveToNext()) {
            Wonders wonder = new Wonders(
                    cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_COMPANY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_LOCATION)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(DatabaseTables.Wonders.COLUMN_NAME_AUXDATA))
            );
            wonders.add(wonder);
        }
        cursor.close();
        return wonders;
    }



    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        myPreferenceRef = getPreferences(MODE_PRIVATE);
        myPreferenceEditor = myPreferenceRef.edit();

        // Initialise DatabaseHelper classes
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        String fetch = myPreferenceRef.getString("filter", "No history found.");
        Log.d("fetch", fetch);


        items = new ArrayList<>();
        adapter = new ArrayAdapter<Wonders>(ListviewActivity.this, R.layout.listview2, R.id.item, items);
        textView = findViewById(R.id.recentlyViewed);
        textView.setText(myPreferenceRef.getString("recentlyVisited", "No history found."));

        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Wonders wonder = items.get(position);

            mySecondWebView = findViewById(R.id.webView2);
            WebViewClient myWebViewClient = new WebViewClient();
            mySecondWebView.setWebViewClient(myWebViewClient);

            WebSettings webSettings = mySecondWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mySecondWebView.loadUrl(wonder.getAuxdata());

            String message = "The wonder " + wonder.getName() + " is a " + wonder.getCategory() +
                    ". It is located in " + wonder.getLocation() + " and was built in " + wonder.getCompany();

            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).setDuration(7000);
            View snackbarView = snackbar.getView();
            TextView tv = snackbarView.findViewById(R.id.snackbar_text);
            tv.setMaxLines(3);
            snackbar.show();

            textView.setText(wonder.getName());
            // Store the new preference
            myPreferenceEditor.putString("recentlyVisited", wonder.getName());
            myPreferenceEditor.apply();

        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                myPreferenceEditor.clear();
                myPreferenceEditor.commit();
                Log.d("==>","You have cleared the history");
            }
        });

        Button fetchButton = findViewById(R.id.fetchFilter);
        fetchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Wonders> tmp = fetchWonders();
                adapter.clear();
                Log.d("ListviewActivity ==>", "Will fetch from database");

                for (int i = 0; i < tmp.size(); i++) {
                        Wonders m = tmp.get(i);
                        Log.d("ListviewActivity ==>", m.getCategory());
                        adapter.add(m);
                    }
                    adapter.notifyDataSetChanged();
                    return;
            }

        });

        Button allButton = findViewById(R.id.allButton);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                List<Wonders> tmp2 = fetchAllWonders();
                Log.d("ListviewActivity ==>", "Will fetch ALL wonders from database");

                for (int i = 0; i < tmp2.size(); i++) {
                    Wonders m = tmp2.get(i);
                    Log.d("ListviewActivity ==>", m.getCategory());
                    adapter.add(m);
                }
                adapter.notifyDataSetChanged();
                return;


                /*Log.d("ListviewActivity ==>", "Will insert into database");
                for (int i = 0; i < adapter.getCount(); i++) {
                    Wonders m = adapter.getItem(i);
                    Log.d("ListviewActivity ==>", m.getName());
                    insertWonders(m);
                }
                return;*/
            }
        });


        if(fetch=="No history found.") {
            new JsonTask().execute("https://wwwlab.iit.his.se/brom/kurser/mobilprog/dbservice/admin/getdataasjson.php?type=a20samul");
            myPreferenceEditor.putString("filter", "Unfiltered");
            myPreferenceEditor.apply();
        }
        else if (fetch.equals("Unfiltered")){
            List<Wonders> tempWonders = fetchAllWonders();
            adapter.clear();
            for (int i = 0; i < tempWonders.size(); i++) {
                //tempWonders.get(i); returnerar ett wonder
                Log.d("ListviewActivity ==>", "Found a wonder: " + tempWonders.get(i));
                adapter.add(tempWonders.get(i));
            }
            adapter.notifyDataSetChanged();
        }
        else if (fetch.equals("Filtered")){
            List<Wonders> tempWonders = fetchWonders();
            adapter.clear();
            for (int i = 0; i < tempWonders.size(); i++) {
                //tempWonders.get(i); returnerar ett wonder
                Log.d("ListviewActivity ==>", "Found a wonder: " + tempWonders.get(i));
                adapter.add(tempWonders.get(i));
            }
            adapter.notifyDataSetChanged();
        }
        else{
            Log.d("Something happened", "fetch:" + fetch);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String json) {
            try {
                Log.d("AsyncTask", json);
                Gson gson = new Gson();
                Wonders[] wonder = gson.fromJson(json, Wonders[].class);
                adapter.clear();
                for (int i = 0; i < wonder.length; i++) {
                    Log.d("ListviewActivity ==>", "Found a wonder: " + wonder[i]);
                    adapter.add(wonder[i]);
                    insertWonders(wonder[i]);
                }
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                Log.e("ListviewActivity ==>", "Something went wrong.");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dropdown_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home_page:
                Intent intent1 = new Intent(ListviewActivity.this, MainActivity.class);
                startActivity(intent1);
                Log.d("HOME", "Successfully launched home page");
                return true;
            case R.id.about_page:
                Intent intent = new Intent(ListviewActivity.this, About.class);
                startActivity(intent);
                Log.d("ABOUT", "About page");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}