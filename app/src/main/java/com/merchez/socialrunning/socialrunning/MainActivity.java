package com.merchez.socialrunning.socialrunning;



import android.app.Activity;
import android.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;


import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

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
    private ResponseBody response;

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

        setupUI(findViewById(R.id.activity_main));
    }

    @Override
    protected void onStart() {
        super.onStart();

        cbRemember.setChecked(gs.prefs.getBoolean("remember",true));
        edtEmail.setText(gs.prefs.getString("email",""));
        edtPasse.setText(gs.prefs.getString("password",""));

    }

    @Override
    public void onClick(View view) {
        String email = edtEmail.getText().toString();
        String password = edtPasse.getText().toString();

        switch(view.getId()){
            case R.id.button_connect:
                    if (gs.verifReseau() == true) {
                        SharedPreferences.Editor prefEdit = gs.prefs.edit();
                        if(cbRemember.isChecked()){
                            prefEdit.putString("email", email);
                            prefEdit.putString("password", password);
                            prefEdit.putBoolean("remember", true);
                        }else{
                            prefEdit.putString("email", "");
                            prefEdit.putString("password", "");
                            prefEdit.putBoolean("remember", false);
                        }
                        prefEdit.apply();
                        //action à faire quand on clique sur le bouton connexion
                        attemptLogin("https://socialrunning.merchez.com/authenticate");
                        Log.i("debug", "bouton de connexion");

                    }else{
                        DialogFragment dialog = new NetworkDialogFragment();

                        dialog.show(this.getFragmentManager(), "reseau");
                    }
                break;
            case R.id.register_link:
                Intent registrationView = new Intent(this, RegistrationActivity.class);
                startActivity(registrationView);
                break;
        }
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

                    //Log.d("Response", response.string());
                    JsonNode json = JSONHelper.StringToJSON(response.string());

                    if(json.path("token").asText() != ""){
                        SharedPreferences.Editor prefEdit = gs.prefs.edit();
                        prefEdit.putString("token", json.path("token").asText());


                        prefEdit.apply();

                        Intent homeView = new Intent(MainActivity.this, HomeActivity.class);
                        startActivity(homeView);
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


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}
