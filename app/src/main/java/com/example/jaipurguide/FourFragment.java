package com.example.jaipurguide;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FourFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener
{
    private static View view;

    private Boolean b;
    private static final LatLng AJMERI_GATE = new LatLng(26.9176473,75.8144671d);
    private static final LatLng BIRLA_TEMPLE = new LatLng(26.891625,75.8151219);
    private static final LatLng JANTAR_MANTAR = new LatLng(26.9251924,75.8228273);
    private static final LatLng MUBARAK_MAHAL = new LatLng(26.9257424,75.8226036);
    private static final LatLng STATUE_GARDEN = new LatLng(26.9077083,75.8038556);
    private static final LatLng RAILWAY_STATION = new LatLng(26.9178386,75.7851529);
    private static final LatLng AIRPORT = new LatLng(26.8254856,75.803142);
    private static final LatLng BUS_STATION = new LatLng(26.923329,75.798231);

    private static  LatLng source;

    private GoogleMap mMap;
    ArrayList<LatLng> MarkerPoints;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    SupportMapFragment mapFragment;

    RadioButton rbRail;
    RadioButton rbBus;
    RadioButton rbAir;
    private  RadioGroup rgModes;

    LatLng origin;
    LatLng dest;
    String mode = "mode=transit";  // Travelling Mode
     NewActivity newActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isUserVisible)
    {
        super.setUserVisibleHint(isUserVisible);
        // when fragment visible to user and view is not null then enter here.
        if (isUserVisible && view != null)
        {
            startFragment();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (view != null) {
            ViewGroup viewGroupParent = (ViewGroup) view.getParent();
            if (viewGroupParent != null)
                viewGroupParent.removeView(view);
        }
        try
        {
            view = inflater.inflate(R.layout.map_fragment, container, false);

            rbRail = (RadioButton) getActivity().findViewById(R.id.railwayStation);
            rbBus = (RadioButton) getActivity().findViewById(R.id.busStand);
            rbAir = (RadioButton) getActivity().findViewById(R.id.airport);
            rgModes = (RadioGroup) view.findViewById(R.id.rg_modes);
        }
        catch (Exception e)
        {
            Log.e("error:", String.valueOf(e));
        }

         newActivity = (NewActivity) getActivity();
        String value = newActivity.getData();

        if (value.equals("ajmeriGate"))
        {
            source=AJMERI_GATE;
            Log.i("source:", String.valueOf(source));

        } else if (value.equals("birlaTemple"))
        {
            source=BIRLA_TEMPLE;
            Log.i("source:", String.valueOf(source));

        } else if (value.equals("mubarakMahal"))
        {
            source=MUBARAK_MAHAL;
            Log.i("source:", String.valueOf(source));
        } else if (value.equals("jantarMantar"))
        {
            source=JANTAR_MANTAR;
            Log.i("source:", String.valueOf(source));
        } else if (value.equals("statueGarden"))
        {
            source=STATUE_GARDEN;
            Log.i("source:", String.valueOf(source));
        }


        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        return view;
    }


    public void startFragment()
    {

        mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override public void onMapReady(GoogleMap googleMap) {
                        if (googleMap != null)
                        {
                            mMap = googleMap;
                            mMap.getUiSettings().setCompassEnabled(true);      //show compass

                            buildGoogleApiClient();

                            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                //do nothing becoz it is checked in menu activity
                            }
                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            MarkerPoints = new ArrayList<>();
                            MarkerPoints.add(source);
                            MarkerPoints.add(RAILWAY_STATION);

                            drawStartStopMarkers();
                            origin = MarkerPoints.get(0);
                            dest = MarkerPoints.get(1);

                            String url = getDirectionsUrl(origin, dest);
                            FetchUrl FetchUrl = new FetchUrl();
                            FetchUrl.execute(url);

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                            rgModes.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {

                                    MarkerPoints.clear();              //clear all markers and polylines
                                    switch (checkedId)
                                    {
                                        case R.id.railwayStation:
                                            MarkerPoints.add(source);
                                            MarkerPoints.add(RAILWAY_STATION);
                                            mMap.clear();
                                            drawStartStopMarkers();
                                            break;
                                        case R.id.busStand:
                                            MarkerPoints.add(source);
                                            MarkerPoints.add(BUS_STATION);
                                            mMap.clear();
                                            drawStartStopMarkers();
                                            break;
                                        case R.id.airport:

                                            MarkerPoints.add(source);
                                            MarkerPoints.add(AIRPORT);
                                            mMap.clear();
                                            drawStartStopMarkers();
                                            break;
                                    }

                                    origin = MarkerPoints.get(0);
                                    dest = MarkerPoints.get(1);

                                    String url = getDirectionsUrl(origin, dest);
                                    FetchUrl FetchUrl = new FetchUrl();
                                    FetchUrl.execute(url);

                                    mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
                                    mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

                                }
                            });
                        }

                    }
                });
              //  Toast.makeText(getActivity(), "Internet Connected", Toast.LENGTH_LONG).show();
            }



    private void drawStartStopMarkers()
    {
        //Log.i("markerpoints size:", String.valueOf(MarkerPoints.size()));
        for (int i = 0; i < MarkerPoints.size(); i++)
        {
            MarkerOptions options = new MarkerOptions();
            options.position(MarkerPoints.get(i));

            if (i == 0) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            } else if (i == 1) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }

            mMap.addMarker(options);                // Add new marker to the Google Map Android API V2
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest)
    {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        String sensor = "sensor=false";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;  // Building the parameters to the web service
        String output = "json";            // Output format
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;   // Building the url to the web service
        return url;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            urlConnection = (HttpURLConnection) url.openConnection();     // Creating an http connection to communicate with url
            urlConnection.connect();                                     // Connecting to url
            iStream = urlConnection.getInputStream();                     // Reading data from url

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        }
        catch (Exception e)
        {
            Log.d("Exception", e.toString());
        }
        finally
        {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    public class FetchUrl extends AsyncTask<String, Void, String>          // Fetches data from url passed
    {

        @Override
        protected String doInBackground(String... url)
        {


            String data = "";                                             // For storing data from web service

            try
            {
                data = downloadUrl(url[0]);                                // Fetching the data from web service
                Log.d("Background Task data", data.toString());
            }
            catch (Exception e)
            {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);                                    // Invokes the thread for parsing the JSON data
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>>               // Parsing the data in non-ui thread
    {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData)
        {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try
            {
                jObject = new JSONObject(jsonData[0]);
                // Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                //Log.d("ParserTask", parser.toString());

                routes = parser.parse(jObject);                      // Starts parsing data
                //Log.d("ParserTask","Executing routes");
                //Log.d("ParserTask",routes.toString());

            }
            catch (Exception e)
            {
                // Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }


        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            if (result != null)
            {
                ArrayList<LatLng> points = new ArrayList<LatLng>();
                PolylineOptions lineOptions = new PolylineOptions();

                String distance = "";
                String duration = "";

                lineOptions.width(6);
                lineOptions.color(Color.RED);

                for (int i = 0; i < result.size(); i++) {
                    List<HashMap<String, String>> path = result.get(i);        // Executes in UI thread, after the parsing process

                    for (int j = 0; j < path.size(); j++)                      // Fetching all the points in i-th route
                    {
                        HashMap<String, String> point = path.get(j);

                        if (j == 0) {
                            distance = point.get("distance");                 // Get distance from the list
                            continue;
                        } else if (j == 1) {
                            duration = point.get("duration");                  // Get duration from the list
                            continue;
                        }

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }


                    lineOptions.addAll(points);                            // Adding all the points in the route to LineOptions
                }

                //Log.i("distance",distance);
                //Log.i("distance",duration);
                int counter=4;
              //  Toast.makeText(getActivity(), "distance:" + distance+" fragment:"+ counter, Toast.LENGTH_SHORT).show();
               // Toast.makeText(getActivity(), "duration:" + duration, Toast.LENGTH_SHORT).show();


                mMap.addPolyline(lineOptions);       // Drawing polyline in the Google Map for the i-th route
                newActivity.startSlideUpAnimation(distance,duration);
            }
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle)
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this );
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {
       // Toast.makeText(getActivity(), "connection failed!", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onConnectionSuspended(int i)
    {
      //  Toast.makeText(getActivity(), "connection suspend!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(AJMERI_GATE));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        //stop location updates
        if (mGoogleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }



}
