package com.yuyh.library.nets.converters;

import android.util.Log;

import com.google.gson.Gson;
import com.yuyh.library.nets.entities.ResultEntity;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * created by arvin on 16/10/24 17:24
 */
public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type adapter;

    public GsonResponseBodyConverter(Gson gson, Type adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        try {
            ResultEntity resultModel = gson.fromJson(response, ResultEntity.class);
            if (resultModel.getStatus() == 200) {
                if (resultModel.getData() != null) {
                    return gson.fromJson(resultModel.getData(), adapter);
                }
                return null;
            }else if(resultModel.getStatus()==405){//设备注册
                 return gson.fromJson(response, adapter);
            }else if(resultModel.getStatus()==401){//重新登陆
                 return gson.fromJson(response, adapter);
            }else {
                return gson.fromJson(response, adapter);
            }

        }catch (Exception e){
            Log.e("","");

        } finally{
            value.close();
        }
        return null;
    }
}
