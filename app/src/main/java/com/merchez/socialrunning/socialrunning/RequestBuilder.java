package com.merchez.socialrunning.socialrunning;

import android.util.Log;
import android.widget.TextView;

import java.io.File;

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

    public static RequestBody RegisterBody(File file, String mimeType, String firstname, String lastname, String email, String password, String birthday, String cursorText){
        final MediaType MEDIA_TYPE = MediaType.parse(mimeType);
        Log.i("Debug ", mimeType);
        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("avatar", file.getName(), RequestBody.create(MEDIA_TYPE, file))
                .addFormDataPart("firstname", firstname)
                .addFormDataPart("lastname", lastname)
                .addFormDataPart("email", email)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("password", password)
                .addFormDataPart("zone", cursorText)
                .build();

        return req;
    }

/*    public static HttpUrl buildURL() {
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
    //}

}
