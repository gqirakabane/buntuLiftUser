package com.bantu.lift.user.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bantu.lift.user.MainActivity;
import com.bantu.lift.user.R;
import com.bantu.lift.user.constant.CommonMeathod;
import com.bantu.lift.user.interFace.FragmentInterface;
import com.bantu.lift.user.service.GPSTracker;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
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

public class AmountMapFragment extends Fragment implements View.OnClickListener ,OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private double mParam1;
    private double mParam2;
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private double mParam3;
    private double mParam4;
    private String mParam5;
    private String mParam6;
    private GoogleMap mMap;
    String title;
    EditText et_amount;
    double lat;
    double lang1;
    private LatLngBounds bounds;
    private LatLngBounds.Builder builder;
    LinearLayout continue_map;
    GPSTracker gps;
    String distance;
    private FragmentInterface fragmentInterface;
    ArrayList markerPoints = new ArrayList();
    SharedPreferences sharedPreferences;
    SupportMapFragment mapFragment;
    public AmountMapFragment() {
    }
    public static AmountMapFragment newInstance(double param1, double param2, double l2, double t2, String pickupaddrss, String dropaddress) {
        AmountMapFragment fragment = new AmountMapFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, param1);
        args.putDouble(ARG_PARAM2, param2);
        args.putDouble(ARG_PARAM3, l2);
        args.putDouble(ARG_PARAM4, t2);
        args.putString(ARG_PARAM5, pickupaddrss);
        args.putString(ARG_PARAM6, dropaddress);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getDouble(ARG_PARAM1);
            mParam2 = getArguments().getDouble(ARG_PARAM2);
            mParam3 = getArguments().getDouble(ARG_PARAM3);
            mParam4 = getArguments().getDouble(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
            mParam6 = getArguments().getString(ARG_PARAM6);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapwithamount, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
              GPSTracker gps = new GPSTracker(getActivity());
        et_amount=view.findViewById(R.id.et_amount);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            lat = latitude;
            lang1 = longitude;
            mapFragment.getMapAsync(this);
            Log.e("data==", String.valueOf(latitude));
            Log.e("data==1", String.valueOf(longitude));

        } else {

            Log.e("UpdateHistoryService", "gps not");
        }
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        MainActivity.text_toolbarTitle.setText("Amount");
        MainActivity.text_toolbarTitle.setTypeface(typeface);
        MainActivity.text_toolbarTitle.setTextSize(20);
        continue_map=view.findViewById(R.id.continue_map);
        continue_map.setOnClickListener(this);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id==R.id.continue_map)
        {
            CommonMeathod.hideKeyboard(getActivity());
            fragmentInterface.fragmentResult(FindCarPullFragment.newInstance(mParam1, mParam2, mParam3, mParam4, et_amount.getText().toString().trim(), mParam5, mParam6,distance), "Group Details");

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(SharedPreferenceConstants.checkData,"").equalsIgnoreCase("1"))
        {
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack("dashboard", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(mParam1, mParam2);

        LatLng sydney1 = new LatLng(mParam3, mParam4);
        markerPoints.add(sydney);
        builder = new LatLngBounds.Builder();

        drawMarker(new LatLng(mParam1, mParam2), "");
        drawMarker1(new LatLng(mParam3, mParam4), "");

        bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);
        mMap.animateCamera(cu);
        String url = getDirectionsUrl(sydney, sydney1);

        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute(url);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface = (FragmentInterface) context;
    }
    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;

        // Output format
        String output = "json";

        // Building the url to the web service
        //  String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyB30y4oyqSfThUXJII35FbMFLj2lgBy5Q8";
        Log.d("data=", url);

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.connect();

            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            String data = "";

            try {
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result==", result);
            try {
                JSONObject jsonObject=new JSONObject(result);

                JSONArray jsonArray=jsonObject.getJSONArray("routes");
                for (int i = 0; i <jsonArray.length() ; i++) {
                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                    JSONArray jsonArray1=jsonObject1.getJSONArray("legs");
                    for (int j = 0; j <jsonArray1.length() ; j++) {
                        JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                        JSONObject jsonObject3=jsonObject2.getJSONObject("distance");
                        distance=jsonObject3.getString("text");
                        // Toast.makeText(getActivity(), jsonObject3.getString("text"), Toast.LENGTH_SHORT).show();

                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ParserTask parserTask = new ParserTask();


            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();
    if (result==null)
      {}else {
    for (int i = 0; i < result.size(); i++) {
        points = new ArrayList();
        lineOptions = new PolylineOptions();

        List<HashMap<String, String>> path = result.get(i);

        for (int j = 0; j < path.size(); j++) {
            HashMap<String, String> point = path.get(j);

            double lat = Double.parseDouble(point.get("lat"));
            double lng = Double.parseDouble(point.get("lng"));
            LatLng position = new LatLng(lat, lng);

            points.add(position);
        }

        lineOptions.addAll(points);
        lineOptions.color(Color.BLACK);

    }
}


            if (lineOptions != null && mMap != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines draw");
            }           //mMap.addPolyline(lineOptions);
        }
    }


    private void drawMarker(LatLng point, String text) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(text).icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_loc));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

    }

    private void drawMarker1(LatLng point, String text) {

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(point).title(text).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_green));
        mMap.addMarker(markerOptions);
        builder.include(markerOptions.getPosition());

    }
}

