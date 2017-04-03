package com.merchez.socialrunning.socialrunning;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sebastien on 03/04/2017.
 */

public class AuthorizationInterceptor implements Interceptor {

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJmaXJzdG5hbWUiOiJTZWJhc3RpZW4iLCJsYXN0bmFtZSI6Ik1lcmNoZXoiLCJlbWFpbCI6Im1lcmNoZXouc2ViYXN0aWVuQGdtYWlsLmNvbSIsImJpcnRoZGF5IjoiMTk5NC0wNC0xM1QwMDowMDowMC4wMDBaIiwiem9uZSI6MTAsInByb2ZpbFBpY3R1cmUiOiIxNDkwNzEyODAwMDQyLmpwZyIsImlhdCI6MTQ5MTIyNDUzMiwiZXhwIjoxNDkxMzk3MzMyfQ.l5XJbiwdpyDyEXFIxBrHryXmuHNVYs48BsL3a9fkW9I").build();
        return chain.proceed(request);
    }
}

