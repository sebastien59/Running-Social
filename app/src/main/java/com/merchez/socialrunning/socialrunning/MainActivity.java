package com.merchez.socialrunning.socialrunning;



import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import com.fasterxml.jackson.databind.JsonNode;

import com.merchez.socialrunning.socialrunning.Fragments.ConnectionDialogFragment;
import com.merchez.socialrunning.socialrunning.Fragments.NetworkDialogFragment;




public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private GlobalState gs;


    private Button btnConnect;
    private EditText edtEmail;
    private EditText edtPasse;
    private CheckBox cbRemember;
    private TextView linkRegistration;

    private OkHttpClient client;
    private String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gs = (GlobalState) getApplication();
        client = new OkHttpClient();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnConnect = (Button) findViewById(R.id.button_connect);
        edtEmail = (EditText) findViewById(R.id.email);
        edtPasse = (EditText) findViewById(R.id.mdp);
        cbRemember = (CheckBox) findViewById(R.id.mdp_checkbox);
        linkRegistration = (TextView) findViewById(R.id.register_link);

        linkRegistration.setOnClickListener(this);
        btnConnect.setOnClickListener(this);

        gs.hideKeyboardOnUnfocus(edtEmail);
        gs.hideKeyboardOnUnfocus(edtPasse);
    }

    @Override
    public void onClick(View view) {
        String e = edtEmail.getText().toString();
        String p = edtPasse.getText().toString();

        switch(view.getId()){
            case R.id.button_connect:
                    if (gs.verifReseau() == true) {
                        //action à faire quand on clique sur le bouton connexion
                        attemptLogin("https://socialrunning.merchez.com/authenticate");
                        Log.i("debug", "bouton de connexion");

                    }else{
                        DialogFragment dialog = new NetworkDialogFragment();

                        dialog.show(this.getFragmentManager(), "reseau");
                    }
                break;
            case R.id.register_link:
                Intent preferenceView = new Intent(this, RegistrationActivity.class);
                startActivity(preferenceView);
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                    JsonNode json = JSONHelper.StringToJSON(response);

                    if(json.path("token").asText() != ""){
                        SharedPreferences.Editor prefEdit = gs.prefs.edit();
                        prefEdit.putString("token", json.path("token").asText());
                        prefEdit.commit();
                    }else{
                        DialogFragment dialog = new ConnectionDialogFragment();
                        dialog.show(getFragmentManager(), "Connexion");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(url);
    }


}
