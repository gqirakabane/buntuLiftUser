package com.bantu.lift.user.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bantu.lift.user.MainActivity;
import com.bantu.lift.user.R;
import com.bantu.lift.user.adapter.PoolnearYouAdapter;
import com.bantu.lift.user.constant.FunctionHelper;
import com.bantu.lift.user.interFace.AdapterCallback;
import com.bantu.lift.user.modelclass.GetPollsModel.Datum;
import com.bantu.lift.user.modelclass.GetPollsModel.GetPollsModelclass;
import com.bantu.lift.user.modelclass.RequestPollModel.RequestPollModelclass;
import com.bantu.lift.user.retrofit.ApiUtils;
import com.bantu.lift.user.retrofit.IRestInterfaces;
import com.bantu.lift.user.utils.SharedPreferenceConstants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoolNearYouFragment extends Fragment implements View.OnClickListener ,AdapterCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";
    private static final String ARG_PARAM6 = "param6";
    private static final String ARG_PARAM7 = "param7";
    private static final String ARG_PARAM8 = "param8";
    private static final String ARG_PARAM9 = "param9";
    private static final String ARG_PARAM10 = "param10";
    private double mParam1;
    private double mParam2;
    private double mParam3;
    private double mParam4;
    private String mParam5;
    private String mParam6;
    private String mParam7;
    private String mParam8;
    private String mParam9;
    private String mParam10;
    LinearLayout continue_map;
    SharedPreferences sharedPreferences;
    TextView date_time;
    RecyclerView recycler_view;
    TextView noRecard;
    TextInputLayout txtInputpickUp, txtInputdropcity;
    PoolnearYouAdapter poolnearYouAdapter;
    List<Datum> data;

    public PoolNearYouFragment() {
    }

    public static PoolNearYouFragment newInstance(double mParam1, double mParam2, double mParam3, double mParam4, String amount, String carId, String luggage, String passenger,String preferanceType,String smoking) {
        PoolNearYouFragment fragment = new PoolNearYouFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_PARAM1, mParam1);
        args.putDouble(ARG_PARAM2, mParam2);
        args.putDouble(ARG_PARAM3, mParam3);
        args.putDouble(ARG_PARAM4, mParam4);
        args.putString(ARG_PARAM5, amount);
        args.putString(ARG_PARAM6, carId);
        args.putString(ARG_PARAM7, luggage);
        args.putString(ARG_PARAM8, passenger);
        args.putString(ARG_PARAM9, preferanceType);
        args.putString(ARG_PARAM10, smoking);
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
            mParam9 = getArguments().getString(ARG_PARAM9);
            mParam10 = getArguments().getString(ARG_PARAM10);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.poolnearyou_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        date_time = view.findViewById(R.id.date_time);
        recycler_view = view.findViewById(R.id.recycler_view);
        noRecard = view.findViewById(R.id.noRecard);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");
        MainActivity.text_toolbarTitle.setText("Pools Near You");
        MainActivity.text_toolbarTitle.setTypeface(typeface);
        MainActivity.text_toolbarTitle.setTextSize(20);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        getPoll();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
        String strDate = sdf.format(c.getTime());
        date_time.setText(strDate);
        sharedPreferences.edit().putString(SharedPreferenceConstants.checkData, "1").apply();

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.continue_map) {

        }

    }
    public void getPoll()
    {
        String start_date;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        start_date = sdf.format(c.getTime());
       // Log.d("data==",start_date);
        FunctionHelper.showDialog(getActivity(),"Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<GetPollsModelclass> signInModelclassCall = iRestInterfaces.getPolls(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""),String.valueOf(mParam1),String.valueOf(mParam2),String.valueOf(mParam3),String.valueOf(mParam4),mParam8,mParam6,mParam7,mParam10,mParam5,mParam9,start_date);
        signInModelclassCall.enqueue(new Callback<GetPollsModelclass>() {
            @Override
            public void onResponse(Call<GetPollsModelclass> call, Response<GetPollsModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        data=response.body().getData();
                        if (data.size()>0)
                        {
                            noRecard.setVisibility(View.GONE);
                            recycler_view.setVisibility(View.VISIBLE);
                            poolnearYouAdapter = new PoolnearYouAdapter(getContext(),data,PoolNearYouFragment.this);
                            recycler_view.setAdapter(poolnearYouAdapter);
                        }else {
                            noRecard.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                            //Toast.makeText(getActivity(), "There is no record of a trip for your search", Toast.LENGTH_SHORT).show();
                        }
                       

                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetPollsModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    @Override
    public void onItemClicked(String pollId, String requestId,int position) {
        requestData(pollId,requestId,position);
    }


    public  void requestData(String pollId, String driverId, final int position){
        FunctionHelper.showDialog(getActivity(),"Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        if (mParam5.equalsIgnoreCase(""))
            mParam5="0";
        Call<RequestPollModelclass> signInModelclassCall = iRestInterfaces.requestPoll(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""),pollId,driverId,mParam5,mParam8);
        signInModelclassCall.enqueue(new Callback<RequestPollModelclass>() {
            @Override
            public void onResponse(Call<RequestPollModelclass> call, Response<RequestPollModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                        int a = Integer.parseInt(data.get(position).getStatus());
                        Log.e("LoginUser==come====", String.valueOf(a));
                        Datum bean = data.get(position);
                        bean.setStatus("1");
                        data.set(position, bean);
                        poolnearYouAdapter.notifyDataSetChanged();
                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();

                        Toast.makeText(getActivity(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<RequestPollModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }
}

