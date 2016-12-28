package cl.magnet.vigia.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;

import org.json.JSONObject;

import cl.magnet.magnetrestclient.VolleyManager;
import cl.magnet.vigia.R;
import cl.magnet.vigia.adapters.PagerTabAdapter;
import cl.magnet.vigia.fragments.ReportListFragment;
import cl.magnet.vigia.fragments.Section1Fragment;
import cl.magnet.vigia.fragments.Section2Fragment;
import cl.magnet.vigia.models.user.UserRequestManager;
import cl.magnet.vigia.network.AppResponseListener;
import cl.magnet.vigia.utils.PrefsManager;

/**
 * The class in charge of the drawer and its fragments.
 */
public class DrawerActivity extends BaseActivity implements
        Section1Fragment.OnFragmentInteractionListener,
        Section2Fragment.OnFragmentInteractionListener,
        ReportListFragment.OnFragmentInteractionListener{

    private PagerTabAdapter mPagerTabAdapter;
    private Context mContext;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarTitleColor));

        // Create the adapter that will return a fragment for each of the two
        // primary sections of the activity.
        mPagerTabAdapter = new PagerTabAdapter(getSupportFragmentManager(), DrawerActivity.this);

        // Set up the ViewPager with the pager adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mPagerTabAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //Setting floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Launch Camera", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mContext = getApplicationContext();

        //If there is no user logged in we go to the Login Activity
        //TODO: Use method UserManager.isUserLogged instead
//        if(!PrefsManager.isUserLogged(context)){
//            startActivityClosingAllOthers(LoginActivity.class);
//        }

    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

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

    public void logout(){

        showAlertDialog(R.string.logout_confirmation,
                        android.R.string.ok,
                        android.R.string.cancel,
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
                        },
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }
        );
        
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        // Handle fragment interaction
    }

    @Override
    public void onReportClickListener(int reportId) {
    }
}
