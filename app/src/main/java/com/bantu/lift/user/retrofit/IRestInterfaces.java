package com.bantu.lift.user.retrofit;

import com.bantu.lift.user.modelclass.FcmModel.FcmModelclass;
import com.bantu.lift.user.modelclass.ForgotModel.ForgotModelclass;
import com.bantu.lift.user.modelclass.GetCarTypeModel.CarTypeModelclass;
import com.bantu.lift.user.modelclass.GetPollsModel.GetPollsModelclass;
import com.bantu.lift.user.modelclass.LogoutModelclass.LogoutModelclass;
import com.bantu.lift.user.modelclass.NotificationModel.NotificationModelclass;
import com.bantu.lift.user.modelclass.RequestPollModel.RequestPollModelclass;
import com.bantu.lift.user.modelclass.signmodelclass.SignInModelclass;
import com.bantu.lift.user.modelclass.signupmodel.SignUpModelclass;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IRestInterfaces {
    @FormUrlEncoded
    @POST("login")
    Call<SignInModelclass> signInUser(@Field("emailMobile") String useremail,
                                      @Field("password") String userpassword,
                                      @Field("serviceKey") String serviceKey,
                                      @Field("deviceType") String deviceType,
                                      @Field("fcmToken") String fcmToken,
                                      @Field("userType") String userType);



    @FormUrlEncoded
    @POST("signUp")
    Call<SignUpModelclass> signupUser(@Field("name") String name,
                                      @Field("email") String email,
                                      @Field("mobile") String mobile,
                                      @Field("gender") String gender,
                                      @Field("workCity") String workCity,
                                      @Field("homeCity") String homeCity,
                                      @Field("password") String password,
                                      @Field("serviceKey") String serviceKey,
                                      @Field("deviceType") String deviceType,
                                      @Field("fcmToken") String fcmToken,
                                      @Field("userType") String userType);

    @FormUrlEncoded
    @POST("logout")
    Call<LogoutModelclass> logoutUser(@Field("serviceKey") String serviceKey,
                                      @Field("userId") String userId);
    @FormUrlEncoded
    @POST("forgotPassword")
    Call<ForgotModelclass> forgotPassword(@Field("email") String email);
    @FormUrlEncoded
    @POST("getAllCarTypes")
    Call<CarTypeModelclass> getAllCarTypes(@Field("serviceKey") String serviceKey,
                                           @Field("userId") String userId);

    @FormUrlEncoded
    @POST("getPolls")
    Call<GetPollsModelclass> getPolls(@Field("serviceKey") String serviceKey,
                                      @Field("userId") String userId,
                                      @Field("pickupLatitude") String pickupLatitude,
                                      @Field("pickupLongitude") String pickupLongitude,
                                      @Field("dropLatitude") String dropLatitude,
                                      @Field("dropLongitude") String dropLongitude,
                                      @Field("passengers") String passengers,
                                      @Field("carType") String carType,
                                      @Field("luggage") String luggage,
                                      @Field("smoking") String smoking,
                                      @Field("amount") String amount,
                                      @Field("otherPreferences") String otherPreferences,
                                      @Field("dateAndTime") String dateAndTime);

@FormUrlEncoded
    @POST("requestPoll")
    Call<RequestPollModelclass> requestPoll(@Field("serviceKey") String serviceKey,
                                            @Field("userId") String userId,
                                            @Field("pollId") String pollId,
                                            @Field("driverId") String driverId,
                                            @Field("budget") String budget,
                                            @Field("passengers") String passengers);

    @FormUrlEncoded
    @POST("getNotifications")
    Call<NotificationModelclass> getNotifications(@Field("serviceKey") String serviceKey,
                                                  @Field("userId") String userId);
    @FormUrlEncoded
    @POST("updateToken")
    Call<FcmModelclass> updateToken(@Field("fcmToken") String fcmToken,
                                    @Field("userId") String userId);
}
