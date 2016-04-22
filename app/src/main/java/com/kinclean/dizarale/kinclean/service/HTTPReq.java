package com.kinclean.dizarale.kinclean.service;

import android.util.Log;

import com.squareup.mimecraft.FormEncoding;

import java.net.URL;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dizar on 2/1/2016.
 */
public class HTTPReq {
    URL url;
    public HTTPReq(){

    }
    public HTTPReq(URL url){
        this.url=url;
    }
    public String HTTPGET(URL url){
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        }catch (Exception e){
            return "Can not Get Data";
        }
    }
    public String HTTPPOST(URL url, RequestBody data){
        try{

            OkHttpClient client = new OkHttpClient();

            /*RequestBody formBody = new FormBody.Builder()
                    .add("email", "Jurassic@Park.com")
                    .add("tel", "90301171XX")
                    .build();
*/
            Request request = new Request.Builder()
                    .url(url)
                    .post(data)
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        }catch (Exception e){
            return "Can not Get Data : "+e.toString();
        }
    }

}
