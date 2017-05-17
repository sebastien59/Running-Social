package com.merchez.socialrunning.socialrunning;

import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sebastien on 03/04/2017.
 */

public class AuthorizationInterceptor extends AppCompatActivity implements Interceptor {
    String token;

    public AuthorizationInterceptor(String token) {
        this.token = token;
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Bearer "+this.token).build();
        return chain.proceed(request);
    }
}

