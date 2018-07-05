package com.example.jaipurguide;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import static com.example.jaipurguide.Place.places;


public class MainActivity extends AppCompatActivity
{
    private SearchView searchView;
    private RecyclerView rv;

    private Boolean b;
    private EditText searchPlate;
    Intent i;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00897b")));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();
        }

        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

            if (b) {
               // Toast.makeText(this, "Internet Connected", Toast.LENGTH_LONG).show();

            } else {
               // Toast.makeText(this, "Internet Connection lost", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception caught", Toast.LENGTH_LONG).show();
        }
      //  registerReceiver(broadcastReceiver, new IntentFilter("INTERNET_LOST"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        initializeData();
        //  Toast.makeText(MainActivity.this, "adapter1: "+ adapter, Toast.LENGTH_SHORT).show();
        //   Toast.makeText(MainActivity.this, "adapter2: "+ adapter, Toast.LENGTH_SHORT).show();

        final RVAdapter adapter = new RVAdapter(places,this);
        rv.setAdapter(adapter);

        //add this addOnItemTouchListener for recyclerview
        rv.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // Toast.makeText(MainActivity.this, "onItemClick is called", Toast.LENGTH_SHORT).show();
                    //   Toast.makeText(MainActivity.this,"pos in menu:"+position,Toast.LENGTH_LONG).show();

                        i=new Intent(MainActivity.this,NewActivity.class);
                        switch(position)
                        {
                            case 0: i.putExtra("1","ajmeriGate");  break;
                            case 1: i.putExtra("1","birlaTemple"); break;
                            case 2: i.putExtra("1","mubarakMahal"); break;
                            case 3: i.putExtra("1","jantarMantar"); break;
                            case 4: i.putExtra("1","statueGarden"); break;
                            default:Toast.makeText(MainActivity.this, "nothing is selected",Toast.LENGTH_LONG).show();
                        }
                        Log.i("position", ""+position);

                        i.putExtra("pos",position);
                        startActivity(i);
                    }
                })
        );

        getMenuInflater().inflate(R.menu.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null)
        {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose()
                {
                   // Toast.makeText(MainActivity.this, "onclose is called", Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                   // Toast.makeText(MainActivity.this, "onclick is called", Toast.LENGTH_SHORT).show();
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                }
            });
            searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

                private void filter(String text) {
                    //new array list that will hold the filtered data
                    ArrayList<Place> filterdNames = new ArrayList<>();

                    //looping through existing elements
                    for (Place s : places) {
                        //if the existing elements contains the search input
                        if (s.eTitle.toLowerCase().contains(text.toLowerCase()) || s.hTitle.toLowerCase().contains(text.toLowerCase()))
                        {
                            //adding the element to filtered list
                            filterdNames.add(s);
                        }
                    }

                    //calling a method of the adapter class and passing the filtered list
                    adapter.filterList(filterdNames);
                }

                @Override
                public boolean onQueryTextSubmit(String query)
                {
                    filter(query);
                    Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
                    return false;
                }


                @Override
                public boolean onQueryTextChange(String newText) {
                    filter(newText);
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        return super.onPrepareOptionsMenu(menu);
    }

  /*  BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                b = activeNetworkInfo != null && activeNetworkInfo.isConnected();

                if (b) {
                    Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Internet Connection lost", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Exception caught", Toast.LENGTH_LONG).show();
            }
        }
    };*/

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
*/

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            else
            {
                ActivityCompat.requestPermissions(this,                         // No explanation needed, we can request the permission.
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        }
        else
        {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_LOCATION:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        Toast.makeText(this, "permission accepted", Toast.LENGTH_LONG).show();
                    }

                }
                else
                {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }






       private void initializeData()
    {
        places = new ArrayList<>();
        places.add(new Place("ajmeri gate", "अजमेरी गेट", R.drawable.ajmeri_gate));
        places.add(new Place("birla temple","बिरला मंदिर", R.drawable.birla_temple));
        places.add(new Place("jantar mantar", "जंतर मंतर", R.drawable.jantarmantar));
        places.add(new Place("mubarak mahal", "मुबारक महल", R.drawable.mubarak_mahal));
        //places.add(new Place("jantar mantar", "जंतर मंतर", R.drawable.statue_garden));
    }




    @Override
    public void onBackPressed() {
        if (!searchView.isIconified())
        {
            searchView.setIconified(true);
           // supportInvalidateOptionsMenu();
        } else {
            super.onBackPressed();
        }

       // Toast.makeText(MainActivity.this, "back key", Toast.LENGTH_SHORT).show();
        finish();
    }
}

class Place
{
    String eTitle;
    String hTitle;
    int image;
    static ArrayList<Place> places;

    Place(String  etitles, String hTitles, int photoId)
    {
        this.eTitle = etitles;
        this.hTitle = hTitles;
        this.image = photoId;
    }
}



// for recyclerview item click listener use this class in activity
class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View childView = view.findChildViewUnder(e.getX(), e.getY());
        if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

}


