package com.merchez.socialrunning.socialrunning;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.merchez.socialrunning.socialrunning.Fragments.DatePickerFragment;
import com.merchez.socialrunning.socialrunning.Fragments.DrawerFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private GlobalState gs;
    private Drawer result;
    private OkHttpClient client;
    private ResponseBody response;
    private Toolbar toolbar;
    private Drawable d;
    private AccountHeader headerResult;
    private IProfile profil;

    private DrawerFragment drawer;

    private Button btnOccas;
    //ViewPager viewPager;
    //PickerAdapter adapter;

    DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        gs = (GlobalState) getApplication();

        btnOccas = (Button) findViewById(R.id.btn_occasionnel);
        btnOccas.setOnClickListener(this);

        drawer = new DrawerFragment();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_occasionnel:
                datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "createGroupeCalendar");


               break;
        }
    }




}
