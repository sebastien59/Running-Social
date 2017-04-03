package com.merchez.socialrunning.socialrunning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;

public class HomeActivity extends AppCompatActivity {
    private GlobalState gs;
    private Drawer result;
    private OkHttpClient client;
    private ResponseBody response;
    private Toolbar toolbar;
    private Drawable d;
    private AccountHeader headerResult;
    private IProfile profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gs = (GlobalState) getApplication();

        client = new OkHttpClient();

        toolbar= (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        URL url = null;
        try {


            profil = new ProfileDrawerItem().withIdentifier(1)
                                            .withName(gs.prefs.getString("firstname","") + " " + gs.prefs.getString("lastname", ""))
                                            .withEmail(gs.prefs.getString("email",""))
                                            .withIcon("https://socialrunning.merchez.com/uploads/"+gs.prefs.getString("profilPicture", ""));
            SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home);
            SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_user);
            SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_create_group).withIcon(FontAwesome.Icon.faw_plus_circle);
            SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_groups).withIcon(FontAwesome.Icon.faw_users);


            headerResult = new AccountHeaderBuilder()
                    .withActivity(HomeActivity.this)
                    .withHeaderBackground(R.drawable.header)
                    .addProfiles(profil)
                    .withSelectionListEnabledForSingleProfile(false)
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                            return false;
                        }
                    })
                    .build();

            result = new DrawerBuilder()
                    .withActivity(HomeActivity.this)
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .withActionBarDrawerToggle(true)
                    .addDrawerItems(item1, item2, item3, item4)
                    .withOnDrawerItemClickListener(
                            new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    gs.alerter("COUCOU");
                                    return true;
                                }
                            }
                    ).build();



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //result.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

   /* private void attemptImage(String url) {

        new AsyncTask<String, Void, Void>() {
            @Override
            protected Void doInBackground(String... params) {
                try {
                    response = APICall.GET(
                            client,
                            HttpUrl.parse(params[0]));

                    Log.d("Response", "test test test");
                    //JsonNode json = JSONHelper.StringToJSON(response);
                    InputStream inputStream = response.byteStream();
                    Bitmap bmp = BitmapFactory.decodeStream(response.byteStream());
                    d = new BitmapDrawable(getResources(), bmp);

                    IProfile profil = new ProfileDrawerItem().withIdentifier(1).withName("Mike Penz").withEmail("mikepenz@gmail.com").withIcon("https://socialrunning.merchez.com/uploads/1490712800042.jpg");

                    //DrawerImageLoader.getInstance().setImage(R.layout.material_drawer_item_profile, , "Profil");
                    //result.updateItem(profil);

                } catch (IOException e) {
                    e.printStackTrace();
                }


                return null;
            }
        }.execute(url);
    }*/


}
