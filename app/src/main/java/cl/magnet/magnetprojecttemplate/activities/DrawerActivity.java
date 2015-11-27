package cl.magnet.magnetprojecttemplate.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Request;

import org.json.JSONObject;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.fragments.Section1Fragment;
import cl.magnet.magnetprojecttemplate.fragments.Section2Fragment;
import cl.magnet.magnetprojecttemplate.models.user.UserRequestManager;
import cl.magnet.magnetprojecttemplate.network.AppResponseListener;
import cl.magnet.magnetprojecttemplate.utils.PrefsManager;
import cl.magnet.magnetrestclient.VolleyManager;

/**
 * The class in charge of the drawer and its fragments.
 */
public class DrawerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Section1Fragment.OnFragmentInteractionListener,
        Section2Fragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setting floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Setting up default fragment.
        Fragment fragment = Section1Fragment.newInstance(null, null);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.mainFrame, fragment);
        ft.commit();

        Context context = getApplicationContext();

        //Setting navigation drawer greeting.
        String fullName = PrefsManager.getFullName(context);

        View headerView = navigationView.getHeaderView(0);

        TextView textView = (TextView) headerView.findViewById(R.id.drawer_hello_textView);
        textView.setText(String.format(getResources().getString(R.string.drawer_hello_user), fullName));

        //If there is no user logged in we go to the Login Activity
        //TODO: Use method UserManager.isUserLogged instead
        if(!PrefsManager.isUserLogged(context)){
            startActivityClosingAllOthers(LoginActivity.class);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();

        Fragment fragment = getFragmentAt(itemId);

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainFrame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private Fragment getFragmentAt(int itemId) {
        //Populate with respective fragments
        switch(itemId) {
            case R.id.nav_camara: return Section1Fragment.newInstance(null, null);
            case R.id.nav_gallery: return Section2Fragment.newInstance(null, null);
            case R.id.nav_logout: logout();
            case R.id.nav_slideshow:
            case R.id.nav_manage:
            case R.id.nav_share:
            case R.id.nav_send:
            default: return null;
        }
    }

    public void logout(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout_confirmation)
                    .setPositiveButton(
                            android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Context context = getApplicationContext();

                                    //We add the log ou request
                                    AppResponseListener<JSONObject> appResponseListener = new AppResponseListener<JSONObject>(context);
                                    Request request = UserRequestManager.userLogOutRequest(appResponseListener);
                                    VolleyManager.getInstance(context).addToRequestQueue(request);

                                    PrefsManager.clearPrefs(context);

                                    startActivityClosingAllOthers(LoginActivity.class);
                                }
                            })
                    .setNegativeButton(
                            android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                .show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Handle fragment interaction
    }
}
