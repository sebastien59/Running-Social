package com.merchez.socialrunning.socialrunning;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.merchez.socialrunning.socialrunning.Fragments.DatePickerFragment;
import com.merchez.socialrunning.socialrunning.Fragments.DrawerFragment;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
    private Button btnHebdo;

    private CheckBox cbLundi;
    private CheckBox cbMardi;
    private CheckBox cbMercredi;
    private CheckBox cbJeudi;
    private CheckBox cbVendredi;
    private CheckBox cbSamedi;
    private CheckBox cbDimanche;
    private TextView textView_list;

    private List<CalendarDay> dates;
    private ArrayList<String> datesListStr = new ArrayList<String>();

    DatePickerFragment datePickerFragment;
    private List<LatLng> lstLatLngs;

    private Button btnCrea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        gs = (GlobalState) getApplication();

        btnOccas = (Button) findViewById(R.id.btn_occasionnel);
        btnOccas.setOnClickListener(this);

        btnHebdo = (Button) findViewById(R.id.btn_Hebdomadaire);
        btnHebdo.setOnClickListener(this);

        btnCrea = (Button) findViewById(R.id.creaBtn);
        btnCrea.setOnClickListener(this);

        drawer = new DrawerFragment();
        lstLatLngs = new ArrayList<LatLng>();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.creaBtn:
                Intent mapsView = new Intent(this, MapsActivity.class);
                startActivityForResult(mapsView, 1);
                break;
            case R.id.btn_occasionnel:
                datePickerFragment = new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(), "createGroupeCalendar");

                cbLundi = (CheckBox) findViewById(R.id.checkBox_lundi);
                cbMardi = (CheckBox) findViewById(R.id.checkBox_mardi);
                cbMercredi = (CheckBox) findViewById(R.id.checkBox_mercredi);
                cbJeudi = (CheckBox) findViewById(R.id.checkBox_jeudi);
                cbVendredi = (CheckBox) findViewById(R.id.checkBox_vendredi);
                cbSamedi = (CheckBox) findViewById(R.id.checkBox_samedi);
                cbDimanche = (CheckBox) findViewById(R.id.checkBox_dimanche);

                cbLundi.setVisibility(View.INVISIBLE);
                cbMardi.setVisibility(View.INVISIBLE);
                cbMercredi.setVisibility(View.INVISIBLE);
                cbJeudi.setVisibility(View.INVISIBLE);
                cbVendredi.setVisibility(View.INVISIBLE);
                cbSamedi.setVisibility(View.INVISIBLE);
                cbDimanche.setVisibility(View.INVISIBLE);

               break;
            case R.id.btn_Hebdomadaire:
                cbLundi = (CheckBox) findViewById(R.id.checkBox_lundi);
                cbMardi = (CheckBox) findViewById(R.id.checkBox_mardi);
                cbMercredi = (CheckBox) findViewById(R.id.checkBox_mercredi);
                cbJeudi = (CheckBox) findViewById(R.id.checkBox_jeudi);
                cbVendredi = (CheckBox) findViewById(R.id.checkBox_vendredi);
                cbSamedi = (CheckBox) findViewById(R.id.checkBox_samedi);
                cbDimanche = (CheckBox) findViewById(R.id.checkBox_dimanche);

                cbLundi.setVisibility(View.VISIBLE);
                cbMardi.setVisibility(View.VISIBLE);
                cbMercredi.setVisibility(View.VISIBLE);
                cbJeudi.setVisibility(View.VISIBLE);
                cbVendredi.setVisibility(View.VISIBLE);
                cbSamedi.setVisibility(View.VISIBLE);
                cbDimanche.setVisibility(View.VISIBLE);

                textView_list = (TextView) findViewById(R.id.dateList);
                textView_list.setText("");
                break;
        }
    }


    public void setDates(List<CalendarDay> dates) {
        this.dates = dates;

        String dateString = "";
        for (CalendarDay day : dates) {

            if(day.getDay() <10) dateString+="0";
            dateString+=day.getDay()+"/";

            if(day.getMonth() <10) dateString+="0";
            dateString+=day.getMonth()+"/";

            dateString+=day.getYear();

            if(dates.indexOf(day)%2 == 1)
                dateString+= "  \n";
            else
                dateString+= "       ";
            datesListStr.add(dateString);
        }

        textView_list = (TextView) findViewById(R.id.dateList);
        textView_list.setText(dateString);
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  ArrayList<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Debug", "Reception");
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("points");
                result = result.replace("[","").replace("]","").replace("lat/lng:","");
                List<String> myList = new ArrayList<String>(Arrays.asList(result.split(",  ")));

                for(String s : myList){
                    Log.i("deb", s);
                    String[] part = s.split(",");
                    Log.i("deb", part[0].replace("(",""));
                    Log.i("deb", part[1].replace(")",""));

                    Float Lat = Float.parseFloat(part[0].replace("(","").replace(" ", ""));
                    Float Lng = Float.parseFloat(part[1].replace(")","").replace(" ", ""));
                    lstLatLngs.add(new LatLng(Lat,Lng));
                }
                gs.alerter("Parcours validé !");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivityResult
}
