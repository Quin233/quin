package com.schrondinger.quin.https.api;

import com.schrondinger.quin.bean.Empty;
import com.schrondinger.quin.bean.Result;
import com.schrondinger.quin.bean.User;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetApi {
    //调试的接口
    @Headers({"Content-Type:application/json","Accept:application/json"})
    @POST("login_register/login.php")
    Observable<Result<User>> getUserInfo(@Body RequestBody body);

    //注册 检查账号是否已经存在
    @Headers({"Content-Type:application/json","Accept:application/json"})
    @POST("login_register/checkPhone.php")
    Observable<Result<Empty>> checkRegister(@Body RequestBody body);

    //注册
    @Headers({"Content-Type:application/json","Accept:application/json"})
    @POST("login_register/login.php")
    Observable<Result<User>> register(@Body RequestBody body);

}
