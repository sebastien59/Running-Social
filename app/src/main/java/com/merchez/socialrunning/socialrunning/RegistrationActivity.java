package com.merchez.socialrunning.socialrunning;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;


public class RegistrationActivity extends Activity implements View.OnClickListener  {
    private static final int RESULT_LOAD_IMAGE =1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private GlobalState gs;

    private SeekBar zone;
    private TextView cursorText;
    private TextView firstname;
    private TextView lastname;
    private TextView email;
    private TextView password;
    private TextView passwordConf;
    private TextView birthday;
    private Button button_register;
    private ImageView addProfilPicture;
    private ImageView profilPicture;
    private String type;
    private File fileProfilPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        gs = (GlobalState) getApplication();

        zone = (SeekBar) findViewById(R.id.seekBar);
        cursorText = (TextView) findViewById(R.id.cursor);
        firstname = (TextView) findViewById(R.id.firstname);
        lastname = (TextView) findViewById(R.id.lastname);
        email = (TextView) findViewById(R.id.emailadress);
        password = (TextView) findViewById(R.id.password);
        passwordConf = (TextView) findViewById(R.id.passwordConf);
        birthday = (TextView) findViewById(R.id.birthday);
        button_register = (Button) findViewById(R.id.button_register);
        addProfilPicture = (ImageView) findViewById(R.id.addProfilPicture);
        profilPicture = (ImageView) findViewById(R.id.profilPicture);

        /*gs.hideKeyboardOnUnfocus(firstname);
        gs.hideKeyboardOnUnfocus(lastname);
        gs.hideKeyboardOnUnfocus(email);
        gs.hideKeyboardOnUnfocus(password);
        gs.hideKeyboardOnUnfocus(passwordConf);
        gs.hideKeyboardOnUnfocus(birthday);*/

        button_register.setOnClickListener(this);
        addProfilPicture.setOnClickListener(this);

        zone.setMax(99);
        zone.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onStopTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0)
            {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
            {
                cursorText.setText(Integer.toString(progress));
                /*float initial = cursorText.getX();
                cursorText.setX(initial+progress*2);
                Log.i("debug", Boolean.toString(arg2));*/
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addProfilPicture:
                verifyStoragePermissions(this);
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.button_register:
                gs.alerter("Test");

                registerRequest("http://merchez.com:3001/api/register");
                break;
        }
    }

    private void registerRequest(String url) {

        final String firstnameText = firstname.getText().toString();
        final String lastnameText = lastname.getText().toString();
        final String emailText = email.getText().toString();
        final String passwordText = password.getText().toString();
        final String birthdayText = birthday.getText().toString();
        final String zoneText = cursorText.getText().toString();

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                OkHttpClient client= new OkHttpClient();


                String response;

                try {
                    response = APICall.POST(
                            client,
                            HttpUrl.parse(params[0]),
                            RequestBuilder.RegisterBody(fileProfilPicture,
                                                        type,
                                                        firstnameText,
                                                        lastnameText,
                                                        emailText,
                                                        passwordText,
                                                        birthdayText,
                                                        zoneText));

                    Log.d("Response", response);
                    JsonNode json = JSONHelper.StringToJSON(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data != null){
            Uri selectedImage = data.getData();
            profilPicture.setImageURI(selectedImage);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};


            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filePath = cursor.getString(columnIndex);
            cursor.close();

            int rotate = 0;
            try {
                this.getContentResolver().notifyChange(selectedImage, null);
                File imageFile = new File(filePath);

                ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotate = 270;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotate = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotate = 90;
                        break;
                }

                ContentResolver cR = this.getContentResolver();
                MimeTypeMap mime = MimeTypeMap.getSingleton();
                type = cR.getType(selectedImage);

                fileProfilPicture= imageFile;

                Log.i("RotateImage", "Exif orientation: " + orientation);
                Log.i("RotateImage", "Rotate value: " + rotate);

                if(rotate != 0 && orientation !=1){
                    profilPicture.setRotation(rotate);
                }else{
                    profilPicture.setRotation(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            gs.alerter("Erreur lors de l'enregistrement");
        }
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}
