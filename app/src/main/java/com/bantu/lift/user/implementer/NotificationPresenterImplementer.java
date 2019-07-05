package com.bantu.lift.user.implementer;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bantu.lift.user.R;
import com.bantu.lift.user.adapter.NotificationAdapter;
import com.bantu.lift.user.constant.FunctionHelper;
import com.bantu.lift.user.modelclass.NotificationModel.NotificationModelclass;
import com.bantu.lift.user.presenter.INotificationlPresenter;
import com.bantu.lift.user.retrofit.ApiUtils;
import com.bantu.lift.user.retrofit.IRestInterfaces;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.bantu.lift.user.view.INotificationView;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotificationPresenterImplementer implements INotificationlPresenter {
    RecyclerView recycler_view;
    NotificationAdapter notificationAdapter;
    INotificationView iNotificationView;
    View view;
    TextView noRecard;
    private Context context;
    private SharedPreferences sharedPreferences;
    public NotificationPresenterImplementer(INotificationView context, View view, Context context1 ) {
        this.iNotificationView = context;
        this.context = context1;
        iNotificationView.OnInitView(view);
        this.view = view;
    }
    @Override
    public void sendRequest() {
    }
    @Override
    public void getNotification() {
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        recycler_view=view.findViewById(R.id.recycler_view);
        noRecard=view.findViewById(R.id.noRecard);
        LinearLayoutManager llm = new LinearLayoutManager(context);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        sharedPreferences = context.getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        recycler_view.setLayoutManager(llm);
        FunctionHelper.showDialog(context,"Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<NotificationModelclass> signInModelclassCall = iRestInterfaces.getNotifications(sharedPreferences.getString(SharedPreferenceConstants.serviceKey,""), sharedPreferences.getString(SharedPreferenceConstants.userId,""));
        signInModelclassCall.enqueue(new Callback<NotificationModelclass>() {
            @Override
            public void onResponse(Call<NotificationModelclass> call, Response<NotificationModelclass> response) {
                if (response.isSuccessful()) {
                    Log.e("notification_list--",""+new Gson().toJson(response));
                    FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        if (response.body().getData().size()>0)
                        {
                            noRecard.setVisibility(View.GONE);
                            recycler_view.setVisibility(View.VISIBLE);
                            notificationAdapter=new NotificationAdapter(context,response.body().getData());
                            recycler_view.setAdapter(notificationAdapter);
                        }else {
                            noRecard.setVisibility(View.VISIBLE);
                            recycler_view.setVisibility(View.GONE);
                        }

                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();
                        Toast.makeText(context, "You have already login in other device", Toast.LENGTH_SHORT).show();
                   iNotificationView.OnLoginError();
                    }
                }
            }
            @Override
            public void onFailure(Call<NotificationModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    @Override
    public void sendSignUpRequest() {

    }

}

