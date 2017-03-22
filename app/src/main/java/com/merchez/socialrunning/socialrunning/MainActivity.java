package com.merchez.socialrunning.socialrunning;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnConnect;
    private EditText edtEmail;
    private EditText edtPasse;
    private CheckBox cbRemember;
    private OkHttpClient client;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        client = new OkHttpClient();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConnect = (Button) findViewById(R.id.button_connect);
        edtEmail = (EditText) findViewById(R.id.email);
        edtPasse = (EditText) findViewById(R.id.mdp);
        cbRemember = (CheckBox) findViewById(R.id.mdp_checkbox);
        btnConnect.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        String e = edtEmail.getText().toString();
        String p = edtPasse.getText().toString();
    if(btnConnect.getId() == view.getId()){
        //action à faire quand on clique sur le bouton connexion
        attemptLogin("https://socialrunning.merchez.com/authenticate");
        Log.i("debug","bouton de connexion");
    }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loadContent() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Log.i("DEBUT", "test");
                try {
                    response = APICall.GET(client, RequestBuilder.buildURL());
                    //Parse the response string here
                    Log.d("Response", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    private void attemptLogin(String url) {
        final String email = edtEmail.getText().toString();
        final String password = edtPasse.getText().toString();

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {

                try {
                    response = APICall.POST(
                            client,
                            HttpUrl.parse(params[0]),
                            RequestBuilder.LoginBody(email, password));

                    Log.d("Response", response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(url);
    }
}
