package com.bantu.lift.user.implementer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bantu.lift.user.BuildConfig;
import com.bantu.lift.user.MainActivity;
import com.bantu.lift.user.R;
import com.bantu.lift.user.activity.SignupScreen;
import com.bantu.lift.user.constant.CommonMeathod;
import com.bantu.lift.user.constant.FunctionHelper;
import com.bantu.lift.user.modelclass.signupmodel.SignUpModelclass;
import com.bantu.lift.user.presenter.ISignUpPresenter;
import com.bantu.lift.user.retrofit.ApiUtils;
import com.bantu.lift.user.retrofit.IRestInterfaces;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.bantu.lift.user.view.ISignupView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bantu.lift.user.utils.GlobalValidation.isEmailValid;

public class SignUpPresenterImplementer implements ISignUpPresenter {
    ISignupView iSignupView;
    View view;
    private Context context;
    private SharedPreferences sharedPreferences;

    public SignUpPresenterImplementer(ISignupView context, View view, Context context1) {
        this.iSignupView = context;
        this.context = context1;
        iSignupView.OnInitView(view);
        this.view = view;
    }

    @Override
    public void signUpSendRequest(int gender1) {
        CommonMeathod.hideKeyboard(context);
      //  final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId,"");
     //   Toast.makeText(context, refreshedToken, Toast.LENGTH_SHORT).show();

        if (validationSignupCheck(gender1) == true) {
            EditText et_fullName, et_mobile, et_email, et_gender, et_work, et_homecity, et_password, et_cpassword;
            final String email;


            et_fullName = view.findViewById(R.id.et_fullName);
            et_mobile = view.findViewById(R.id.et_mobile);
            et_email = view.findViewById(R.id.et_email);
            et_work = view.findViewById(R.id.et_work);
            et_homecity = view.findViewById(R.id.et_homecity);
            et_cpassword = view.findViewById(R.id.et_cpassword);

            et_password = view.findViewById(R.id.et_password);
            email = et_email.getText().toString();
            final String fullName = et_fullName.getText().toString();
            final String mobile = et_mobile.getText().toString();
            final String gender ;
            if (gender1==1)
            {
                gender="male";
            }else {
                gender="female";
            }
            final String work = et_work.getText().toString();
            final String homecity = et_homecity.getText().toString();
            final String password = et_password.getText().toString();
            final String cnf_password = et_cpassword.getText().toString();
            FunctionHelper.showDialog(context,"Loading...");
            IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
            Call<SignUpModelclass> signInModelclassCall = iRestInterfaces.signupUser(fullName, email,mobile,gender,work,homecity,password,"CPS95ssmdeh7567njycsh","android",refreshedToken,"1");
            signInModelclassCall.enqueue(new Callback<SignUpModelclass>() {
                @Override
                public void onResponse(Call<SignUpModelclass> call, Response<SignUpModelclass> response) {
                    if (response.isSuccessful()) {
                        FunctionHelper.dismissDialog();
                        int status_val = Integer.parseInt(response.body().getErrorCode());
                        if (status_val == 0) {
                            Integer buildVersion= BuildConfig.VERSION_CODE;
                            sharedPreferences.edit().putString(SharedPreferenceConstants.userId, response.body().getData().getUserId()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.email, response.body().getData().getEmail()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.mobile, response.body().getData().getMobile()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.serviceKey, response.body().getServiceKey()).apply();
                            sharedPreferences.edit().putString(SharedPreferenceConstants.name, response.body().getData().getName()).apply();
                            sharedPreferences.edit().putInt(SharedPreferenceConstants.buildVersion,buildVersion).apply();

                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                            ((SignupScreen) context).finish();

                        } else if (status_val == 1) {
                            Toast.makeText(context, response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<SignUpModelclass> call, Throwable t) {
                    FunctionHelper.dismissDialog();

                }
            });

        }
    }

    @Override
    public void sendLoginRequest() {
        ((SignupScreen) context).finish();

    }


    @SuppressLint("NewApi")
    public boolean validationSignupCheck(int gender1) {
        boolean check = false;

        EditText et_fullName, et_mobile, et_email, et_gender, et_work, et_homecity, et_password, et_cpassword;
        final String email;


        et_fullName = view.findViewById(R.id.et_fullName);
        et_mobile = view.findViewById(R.id.et_mobile);
        et_email = view.findViewById(R.id.et_email);
        et_work = view.findViewById(R.id.et_work);
        et_homecity = view.findViewById(R.id.et_homecity);
        et_cpassword = view.findViewById(R.id.et_cpassword);

        et_password = view.findViewById(R.id.et_password);
        email = et_email.getText().toString();
        final String fullName = et_fullName.getText().toString();
        final String mobile = et_mobile.getText().toString();
        final String work = et_work.getText().toString();
        final String homecity = et_homecity.getText().toString();
        final String password = et_password.getText().toString();
        final String cnf_password = et_cpassword.getText().toString();


        if (check == false) {

            if (fullName.equals("")) {

                Toast.makeText(context, "Please enter full name", Toast.LENGTH_SHORT).show();

            }
           else if (mobile.equals("")) {

                Toast.makeText(context, "Please enter mobile number", Toast.LENGTH_SHORT).show();

            } else if (mobile.length()< 9 ) {

                Toast.makeText(context, "Phone number is not valid", Toast.LENGTH_SHORT).show();

            }else if (email.equals("")) {


                Toast.makeText(context, "Please enter email id", Toast.LENGTH_SHORT).show();

            } else if ((isEmailValid(email) == false)) {


                Toast.makeText(context, "Please enter valid email id", Toast.LENGTH_SHORT).show();

            } else if (gender1==0) {

                Toast.makeText(context, "please select gender", Toast.LENGTH_SHORT).show();

            } else if (work.equals("")) {

                Toast.makeText(context, "please enter work city", Toast.LENGTH_SHORT).show();

            } else if (homecity.equals("")) {

                Toast.makeText(context, "please enter home city", Toast.LENGTH_SHORT).show();

            } else if (password.equals("")) {

                Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show();

            } else if (password.length() < 5) {

                Toast.makeText(context, "Password must contain at least 6 characters", Toast.LENGTH_SHORT).show();

            } else if (cnf_password.equals("")) {

                Toast.makeText(context, "Enter your confirmation password", Toast.LENGTH_SHORT).show();

            } else if (!(password.equals(cnf_password))) {
                Toast.makeText(context, "password mismatch", Toast.LENGTH_SHORT).show();

            } else {

                check = true;
            }
        }
        return check;
    }
}
