package com.bantu.lift.user.implementer;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;


import com.bantu.lift.user.constant.CommonMeathod;
import com.bantu.lift.user.presenter.ICreateLiftPresenter;
import com.bantu.lift.user.view.ICreateLiftView;

public class CreateLiftPresenterImplementer implements ICreateLiftPresenter {
    ICreateLiftView iCreateLiftPresenter;
    View view;
    private SharedPreferences sharedPreferences;
    String smoking1;

    private Context context;
    String otherPreferaces,carNumber,carName;
    public CreateLiftPresenterImplementer(ICreateLiftView context, View view, Context context1) {
        this.context = context1;
        this.iCreateLiftPresenter = context;
        iCreateLiftPresenter.OnInitView(view);
        this.view = view;
    }
    @Override
    public void sendRequest(String img, double l1, double t1, double l2, double t2, String start_date,String car_type,String luggage12,String pickAddress,String  dropAddress,String distance,String carId) {

            CommonMeathod.hideKeyboard(context);
            Log.d("data==",img);
            Log.d("data==", String.valueOf(l1));
            Log.d("data==", String.valueOf(t1));
            Log.d("data==", String.valueOf(l2));
            Log.d("data==", String.valueOf(t2));
            Log.d("data==",start_date);
            Log.d("data==",car_type);
            Log.d("data==",luggage12);
            Log.d("data==",carId);
            Log.d("data==",img);
        iCreateLiftPresenter.OnLoginSuccess();
               }

    @Override
    public void sendForgotScreenRequest() {


    }

    @Override
    public void sendSignUpRequest() {


    }

    public boolean validationLoginCheck(String img,String car_type,String luggage12) {
        return true;
    }

}
