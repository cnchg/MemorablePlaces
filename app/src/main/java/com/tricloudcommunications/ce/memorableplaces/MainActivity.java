package com.tricloudcommunications.ce.memorableplaces;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> locationList;
    static ArrayAdapter<String> arrayAdapter;
    static ArrayList<LatLng> locationCoords;

    ListView myListView;


    public void addNewPlace(View view){

        Intent i = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(i);

        Log.i("Button Status: ", "Button was pressed");


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myListView = (ListView) findViewById(R.id.placesListView);

        locationList = new ArrayList<String>();
        //locationList.add("Go Ahead! Add a new place");

        locationCoords = new ArrayList<LatLng>();
        //locationCoords.add(new LatLng(0,0));

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, locationList);
        myListView.setAdapter(arrayAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getApplicationContext(), MapsActivity.class);
                i.putExtra("LocationInfo", position);
                startActivity(i);

                Log.i("List Item Selceted: ", locationList.get(position));

            }
        });


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