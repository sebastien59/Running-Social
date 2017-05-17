package com.merchez.socialrunning.socialrunning.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.merchez.socialrunning.socialrunning.CreateGroupActivity;
import com.merchez.socialrunning.socialrunning.GlobalState;
import com.merchez.socialrunning.socialrunning.HomeActivity;
import com.merchez.socialrunning.socialrunning.R;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.net.URL;

public class DrawerFragment extends Fragment {
    private GlobalState gs;
    private Drawer result;
    private Toolbar toolbar;
    private Drawable d;
    private AccountHeader headerResult;
    private IProfile profil;


    public static Fragment newInstance(Context context) {
        DrawerFragment d = new DrawerFragment();

        return d;
    }

    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        gs = (GlobalState) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View  view = (View) inflater.inflate(R.layout.fragment_drawer, null);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        URL url = null;
        try {
            profil = new ProfileDrawerItem().withIdentifier(1)
                    .withName(gs.prefs.getString("firstname","") + " " + gs.prefs.getString("lastname", ""))
                    .withEmail(gs.prefs.getString("email",""))
                    .withIcon("https://socialrunning.herokuapp.com/uploads/"+gs.prefs.getString("profilPicture", ""));
            SecondaryDrawerItem item1 = new SecondaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home);
            SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_user);
            SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName(R.string.drawer_item_create_group).withIcon(FontAwesome.Icon.faw_plus_circle);
            SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName(R.string.drawer_item_groups).withIcon(FontAwesome.Icon.faw_users);


            headerResult = new AccountHeaderBuilder()
                    .withActivity(getActivity())
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
                    .withActivity(getActivity())
                    .withToolbar(toolbar)
                    .withAccountHeader(headerResult)
                    .addDrawerItems(item1, item2, item3, item4)
                    .withOnDrawerItemClickListener(
                            new Drawer.OnDrawerItemClickListener() {
                                @Override
                                public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                    gs.alerter(Integer.toString(position));
                                    switch(position){
                                        case 1: // Accueil
                                            Intent HomeView = new Intent(getActivity(), HomeActivity.class );
                                            startActivity(HomeView);
                                            break;
                                        case 2: // Mon Compte

                                            //Intent SettingsView = new Intent(this, SettingsActivity.class);
                                            //startActivity(SettingsView);
                                            break;
                                        case 3: // Creer un groupe
                                            Intent CreateGroupeView = new Intent(getActivity(), CreateGroupActivity.class );
                                            startActivity(CreateGroupeView);
                                            break;
                                        case 4: // Mes Groupes

                                            break;
                                    }
                                    return true;
                                }
                            }
                    ).build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
