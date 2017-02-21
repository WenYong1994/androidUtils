package com.yuyh.library.nets;

import android.text.TextUtils;

import com.yuyh.library.nets.converters.GsonConverterFactory;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * created by arvin on 16/11/21 23:06
 * email：1035407623@qq.com
 */
public abstract class BaseNet<T> {
    private T api;
    private Class<T> clazz;
    private OkHttpClient okHttpClient;
    private String token;
    private String deviceId;

    @SuppressWarnings("FieldCanBeLocal")
    private InputStream mCertificate;

    private Converter.Factory converterFactory;
    private CallAdapter.Factory rxJavaCallAdapterFactory;

    protected BaseNet() {
        converterFactory = GsonConverterFactory.create();
        rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();
        clazz = getApiClazz();
    }

    public T getApi() {
        if (api == null) {
            initHttpClient();
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(converterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            api = retrofit.create(clazz);
        }
        return api;
    }

    private void initHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request;
                    if (!TextUtils.isEmpty(token)) {
                        request = chain.request().newBuilder()
                                .addHeader("version", "1.0")
                                .addHeader("terminal", "1")
                                .addHeader("uid", "1231564651231321")
                                .addHeader("appVersion", "1.0")
                                .addHeader("Content-Type", "application/json")
                                .build();
                    } else {
                        request = chain.request().newBuilder()
                                .addHeader("version", "1.0")
                                .addHeader("terminal", "1")
                                .addHeader("uid", "1231564651231321")
                                .addHeader("appVersion", "1.0")
                                .addHeader("Content-Type", "application/json")
                                .build();
                    }

                    request = chain.request().newBuilder()
                            .addHeader("version", "1.0")
                            .addHeader("terminal", "1")
                            .addHeader("uid", "1231564651231321")
                            .addHeader("appVersion", "1.0")
                            .addHeader("Content-Type", "application/json")
                            .build();
                    Response response = chain.proceed(request);
                    dealResponse(response);
                    return response;
                }
            });
            okHttpClient = builder.build();
        }
    }

    protected void dealResponse(Response response) {
    }

    /**
     * 若需要使用https请求,请设置证书信息
     */
    private boolean isNeedHttps() {
        return false;
    }

    protected void setCertificateInputStream(InputStream certificate) {
        mCertificate = certificate;
    }

    public void setToken(String token) {
        this.token = token;
        clear();
    }

    public String getToken() {
        return token;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    private void clear() {
        okHttpClient = null;
        api = null;
    }

    protected abstract Class<T> getApiClazz();

    protected abstract String getBaseUrl();

    protected Request dealRequest(Request request) {
        return request;
    }
}
