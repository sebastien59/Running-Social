package com.merchez.socialrunning.socialrunning;

import okhttp3.*;

/**
 * Created by Youssra on 21/03/2017.
 */

public class RequestBuilder {

    //Login request body
    public static RequestBody LoginBody(String email, String password) {
        return new FormBody.Builder()
                .add("action", "login")
                .add("format", "json")
                .add("email", email)
                .add("password", password)
                .build();
    }

    public static HttpUrl buildURL() {
        return new HttpUrl.Builder()
                .scheme("https") //http
                .host("www.somehostname.com")
                .addPathSegment("pathSegment")//adds "/pathSegment" at the end of hostname
                .addQueryParameter("param1", "value1") //add query parameters to the URL
                .addEncodedQueryParameter("encodedName", "encodedValue")//add encoded query parameters to the URL
                .build();
        /**
         * The return URL:
         *  https://www.somehostname.com/pathSegment?param1=value1&encodedName=encodedValue
         */
    }

}
