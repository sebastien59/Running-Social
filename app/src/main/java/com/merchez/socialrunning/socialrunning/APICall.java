package com.merchez.socialrunning.socialrunning;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Youssra on 21/03/2017.
 */

public class APICall {

    //GET network request
    public static ResponseBody GET(OkHttpClient client, HttpUrl url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body();
    }

    public static ResponseBody GETwithAuthorization(OkHttpClient client, HttpUrl url, String Authorization) throws IOException {
        Request request = new Request.Builder()
                .header("Authorization", "Bearer "+Authorization)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body();
    }

    //POST network request
    public static ResponseBody POST(OkHttpClient client, HttpUrl url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        return response.body();
    }

}
