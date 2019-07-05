package com.bantu.lift.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bantu.lift.user.MainActivity;
import com.bantu.lift.user.R;
import com.bantu.lift.user.adapter.CarTypeAdapter;
import com.bantu.lift.user.adapter.SpinnerDropdownAdapter;
import com.bantu.lift.user.constant.FunctionHelper;
import com.bantu.lift.user.implementer.CreateLiftPresenterImplementer;
import com.bantu.lift.user.interFace.FragmentInterface;
import com.bantu.lift.user.modelclass.GetCarTypeModel.CarTypeModelclass;
import com.bantu.lift.user.modelclass.GetCarTypeModel.Datum;
import com.bantu.lift.user.retrofit.ApiUtils;
import com.bantu.lift.user.retrofit.IRestInterfaces;
import com.bantu.lift.user.service.GPSTracker;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.bantu.lift.user.view.ICreateLiftView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindCarPullFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, ICreateLiftView {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private double mParam1;
    private double mParam2;
    private double mParam3;
    private double mParam4;
    private String mParam5;
    private String mParam6;
    private String mParam7;
    private String mParam8;
    private GoogleMap mMap;
    String title;
    LinearLayout finishBtn;
    double lat;
    double lang1;
    RelativeLayout carType1;
    LinearLayout uploadPhoto;
    Spinner spinner_carType, spinner_luggage;
    SharedPreferences sharedPreferences;
    SpinnerDropdownAdapter spinnerDropdownAdapter, spinnerDropdownAdapter1;

    CarTypeAdapter carTypeAdapter;
    private FragmentInterface fragmentInterface;
    List<Datum> cartypeList = new ArrayList<>();
    String carId;
    List<String> luggagetypeList = new ArrayList<>();
    CreateLiftPresenterImplementer createLiftPresenterImplementer;
    SupportMapFragment mapFragment;
    String imagepath = "checkdata";
    String carType = "carType1";
    String luggage = "";
    Datum bean;
    CheckBox checkbox;
    String smoking1;
    String passanger;
    EditText et_preferanceType, et_passerger;

    public FindCarPullFragment() {
    }

    public static FindCarPullFragment newInstance(double mParam1, double mParam2, double mParam3, double mParam4, String date, String pickupaddrss, String dropaddress, String distance) {
        FindCarPullFragment fragment = new FindCarPullFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, mParam1);
        args.putDouble(ARG_PARAM2, mParam2);
        args.putDouble(ARG_PARAM3, mParam3);
        args.putDouble(ARG_PARAM4, mParam4);
        args.putString(ARG_PARAM5, date);
        args.putString(ARG_PARAM6, pickupaddrss);
        args.putString(ARG_PARAM7, dropaddress);
        args.putString(ARG_PARAM8, distance);
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
            mParam7 = getArguments().getString(ARG_PARAM7);
            mParam8 = getArguments().getString(ARG_PARAM8);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_lift_amount, container, false);
        createLiftPresenterImplementer = new CreateLiftPresenterImplementer(this, view, getActivity());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        uploadPhoto = view.findViewById(R.id.uploadPhoto);
        et_passerger = view.findViewById(R.id.et_passerger);
        et_preferanceType = view.findViewById(R.id.et_preferanceType);
        uploadPhoto.setOnClickListener(this);
        checkbox = view.findViewById(R.id.sighupCheckbox);

        GPSTracker gps = new GPSTracker(getActivity());
        spinner_carType = view.findViewById(R.id.spinner_carType);
        spinner_luggage = view.findViewById(R.id.spinner_luggage);
        finishBtn = view.findViewById(R.id.finishBtn);
        carType1 = view.findViewById(R.id.carType1);
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
        sharedPreferences = getActivity().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        luggagetypeList.clear();
        luggagetypeList.add("Luggage");
        luggagetypeList.add("Small");
        luggagetypeList.add("large");


        spinnerDropdownAdapter1 = new SpinnerDropdownAdapter(getActivity(), luggagetypeList);
        spinner_luggage.setAdapter(spinnerDropdownAdapter1);
        spinner_luggage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (luggagetypeList.get(position).equalsIgnoreCase("Luggage")) {
                    luggage = "";

                } else {
                    luggage = luggagetypeList.get(position);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        finishBtn.setOnClickListener(this);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        MainActivity.text_toolbarTitle.setText("Find Car Pool ");
        MainActivity.text_toolbarTitle.setTypeface(typeface);
        MainActivity.text_toolbarTitle.setTextSize(20);
        if (checkbox.isChecked()) {
            smoking1 = "1";
        } else {
            smoking1 = "0";
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.uploadPhoto) {
        }
        if (id == R.id.finishBtn) {

            if (et_passerger.getText().toString().equalsIgnoreCase("")) {
                passanger = "1";
            } else {
                passanger = et_passerger.getText().toString();
            }
            //  Toast.makeText(getActivity(), passanger, Toast.LENGTH_SHORT).show();
            fragmentInterface.fragmentResult(PoolNearYouFragment.newInstance(mParam1, mParam2, mParam3, mParam4, mParam5, carId, luggage, passanger, et_preferanceType.getText().toString(), smoking1), "Police Department");

            //  createLiftPresenterImplementer.sendRequest(imagepath, mParam1, mParam2, mParam3, mParam4, mParam5, carType, luggage, mParam6, mParam7,mParam8,carId);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString(SharedPreferenceConstants.checkData, "").equalsIgnoreCase("1")) {
            FragmentManager fm = getActivity()
                    .getSupportFragmentManager();
            fm.popBackStack("Group Details", FragmentManager.POP_BACK_STACK_INCLUSIVE);

        } else {
            getCarTypeList();

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng sydney = new LatLng(lat, lang1);
        MarkerOptions marker = new MarkerOptions().position(sydney);
        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.drop_loc));
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                sydney).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.addCircle(new CircleOptions()
                .center(sydney)
                .radius(5.0f)
                .strokeWidth(2f).strokeColor(0x55B5CFEB)
                .fillColor(0x55B5CFEB));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragmentInterface = (FragmentInterface) context;
    }

    @Override
    public void OnLoginSuccess() {
        fragmentInterface.fragmentResult(DashboardFragment.newInstance("", ""), "Police Department");
        //addFragment(DashboardFragment.newInstance("", ""), "Dashboard");


        FragmentManager fm = getActivity()
                .getSupportFragmentManager();
        fm.popBackStack("Group Details", FragmentManager.POP_BACK_STACK_INCLUSIVE);

    }

    @Override
    public void OnLoginError() {
        logoutUser();
    }

    @Override
    public void OnInitView(View view) {

    }


    private void addFragment(Fragment fragment, final String title1) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, title1);
        fragmentTransaction.commit();
        // drawer.closeDrawer(GravityCompat.START);
        //imgTitle.setImageResource(R.drawable.toolbarimage);
    }


    public void getCarTypeList() {
        FunctionHelper.showDialog(getActivity(), "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<CarTypeModelclass> signInModelclassCall = iRestInterfaces.getAllCarTypes(sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        signInModelclassCall.enqueue(new Callback<CarTypeModelclass>() {
            @Override
            public void onResponse(Call<CarTypeModelclass> call, Response<CarTypeModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        bean = new Datum();
                        bean.setCarTypeName("Driver/Passenger");
                        bean.setCarTypeId("");
                        cartypeList.add(bean);
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            bean = new Datum();

                            bean.setCarTypeName(response.body().getData().get(i).getCarTypeName());
                            bean.setCarTypeId(response.body().getData().get(i).getCarTypeId());
                            cartypeList.add(bean);

                        }
                        // Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        carTypeAdapter = new CarTypeAdapter(getActivity(), cartypeList);
                        spinner_carType.setAdapter(carTypeAdapter);
                        getData();
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(getActivity(), "You have already login in other device", Toast.LENGTH_SHORT).show();

                        logoutUser();
                    }
                }
            }

            @Override
            public void onFailure(Call<CarTypeModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    public void getData() {
        spinner_carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                carType = cartypeList.get(position).getCarTypeName();
                carId = cartypeList.get(position).getCarTypeId();
                if (carType.equalsIgnoreCase("Other")) {
                    carType1.setVisibility(View.VISIBLE);
                } else {
                    carType1.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void logoutUser()
    {
        String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId, "");

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SharedPreferenceConstants.email, "");
        editor.putString(SharedPreferenceConstants.name, "");
        editor.putString(SharedPreferenceConstants.serviceKey, "");
        editor.putString(SharedPreferenceConstants.userId, "");
        editor.putString(SharedPreferenceConstants.homeCity, "");
        editor.putString(SharedPreferenceConstants.workCity, "");
        editor.putString(SharedPreferenceConstants.mobile, "");
        editor.putString(SharedPreferenceConstants.checkData, "");
        editor.clear();
        editor.commit();
        sharedPreferences.edit().putString(SharedPreferenceConstants.fcmId, refreshedToken).apply();
        Intent i1 = new Intent();
        i1.setClassName("com.bantu.lift.user", "com.bantu.lift.user.activity.LoginActivity");
        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i1);
    }
}
