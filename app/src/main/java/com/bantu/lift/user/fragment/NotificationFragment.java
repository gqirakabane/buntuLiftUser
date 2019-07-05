package com.bantu.lift.user.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bantu.lift.user.MainActivity;
import com.bantu.lift.user.R;
import com.bantu.lift.user.adapter.NotificationAdapter;
import com.bantu.lift.user.implementer.NotificationPresenterImplementer;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.bantu.lift.user.view.INotificationView;


public class NotificationFragment extends Fragment implements View.OnClickListener,INotificationView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView recycler_view;
    private String mParam1;
    private String mParam2;
    NotificationAdapter notificationAdapter;
    TextView noRecard;
    SharedPreferences sharedPreferences;

    NotificationPresenterImplementer notificationPresenterImplementer;
    public NotificationFragment() {
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notification_fragment, container, false);
        notificationPresenterImplementer=new NotificationPresenterImplementer(this, view, getActivity());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_view = view.findViewById(R.id.recycler_view);
        sharedPreferences = getActivity().getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);

        noRecard = view.findViewById(R.id.noRecard);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_view.setLayoutManager(llm);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Bold.ttf");

        MainActivity.text_toolbarTitle.setText("Notifications");
        MainActivity.text_toolbarTitle.setTypeface(typeface);
        MainActivity.text_toolbarTitle.setTextSize(20);
        notificationPresenterImplementer.getNotification();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.continue_map) {
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void OnLoginSuccess() {

    }

    @Override
    public void OnLoginError() {
        logoutUser();
    }

    @Override
    public void OnInitView(View view) {

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
