package com.bantu.lift.user.view;

import android.view.View;

public interface INotificationView {
    public  void OnLoginSuccess();
    public  void OnLoginError();
    public  void OnInitView(View view);
}
