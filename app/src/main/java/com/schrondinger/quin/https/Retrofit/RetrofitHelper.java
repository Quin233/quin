package com.schrondinger.quin.https.Retrofit;

import com.orhanobut.logger.Logger;
import com.schrondinger.quin.BuildConfig;
import com.schrondinger.quin.Utils.Constants;
import com.schrondinger.quin.Utils.SystemUtil;
import com.schrondinger.quin.bean.Empty;
import com.schrondinger.quin.https.api.NetApi;
import com.schrondinger.quin.https.converter.JxGsonConverterFactory;
import com.schrondinger.quin.https.cookies.CookiesManager;
import com.schrondinger.quin.https.exception.ApiException;
import com.schrondinger.quin.https.exception.ErrorUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrofitHelper {
    private static RetrofitHelper instance;
    private static Retrofit retrofit;

    //API注入
    private NetApi api;

    public static RetrofitHelper getInstance(){
        synchronized (RetrofitHelper.class){
            if (instance==null)
                instance=new RetrofitHelper();
        }
        return new RetrofitHelper();
    }
    public RetrofitHelper(){}

    private <T> T configRetrofit(Class<T> service) {
        String API_BASE_URL= BuildConfig.API_BASE_URL;
        if(BuildConfig.ISTEST.equals("true")){
            API_BASE_URL=BuildConfig.API_BASE_URL_TEST;
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .client(initOkHttp())
                .addConverterFactory(JxGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(service);
    }

    /**
     * 配置OKHTTP3
     * @return
     */
    private OkHttpClient initOkHttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Logger.d(message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        File cacheFile = new File(Constants.PATH_CACHE);
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
//                if (!SystemUtil.isNetworkConnected()) {//强制使用网络
//                    request = request.newBuilder()
//                            .cacheControl(CacheControl.FORCE_NETWORK)
//                            .build();
//                }else{
                request=request.newBuilder()
                        .header("User-Agent","NBA_ANDROID_PHONE/1.0")
                        .header("Content-Type","application/stream")
                        .header("Accept","text/mobilejson")
                        .header("Connection","Keep-Alive")
                        .build();
//                }
                Response response;
                try{
                    response = chain.proceed(request);
                }catch (Exception e){
                    Logger.d("okhttp request error>>>>>>>>>>>"+e.getLocalizedMessage());
                    throw new ApiException(ErrorUtils.errorMsg(e.getLocalizedMessage()));
                }


                if (SystemUtil.isNetworkConnected()) {
                    int maxAge = 0;
                    // 有网络时, 不缓存, 最大保存时长为0
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")
                            .build();
                } else {// 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }

                return response;
            }
        };

        builder.addNetworkInterceptor(cacheInterceptor);
        builder.addInterceptor(cacheInterceptor);
        builder.cache(cache);

        //设置缓存
        builder.cookieJar(new CookiesManager());
        //设置超时
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        //错误重连
        builder.retryOnConnectionFailure(true);
        return builder.build();
    }

    public NetApi getApi(){
        return api==null?configRetrofit(NetApi.class):api;
    }
}
