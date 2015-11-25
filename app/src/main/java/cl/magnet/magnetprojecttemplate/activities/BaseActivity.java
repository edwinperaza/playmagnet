package cl.magnet.magnetprojecttemplate.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.Response;

import cl.magnet.magnetprojecttemplate.R;
import cl.magnet.magnetprojecttemplate.network.AppResponseListener;

/**
 * Created by yaniv on 11/4/15.
 */

/**
 * A Base class useful for using methods common to all activities.
 */
public abstract class BaseActivity extends AppCompatActivity {

    //Receiver called when getting an Unauthorized error from server (401)
    private BroadcastReceiver mUnauthorizedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            showToast(R.string.error_unauthorized);
            startActivityClosingAllOthers(LoginActivity.class);
        }
    };

    //Receiver called when getting an Upgrade Required error from server (426)
    private BroadcastReceiver mUpgradeRequiredReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivityClosingAllOthers(UpgradeRequiredActivity.class);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        // register receivers
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mUnauthorizedReceiver, new IntentFilter(AppResponseListener.ACTION_UNAUTHORIZED));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(mUpgradeRequiredReceiver, new IntentFilter(AppResponseListener.ACTION_UPGRADE_REQUIRED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // unregister receivers
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mUnauthorizedReceiver);
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mUpgradeRequiredReceiver);
    }

    /**
     * Starts a new activity closing all other application activities that are opened. Useful
     * when logging out a user or signing in a user.
     *
     * @param cls The Activity class to start.
     */
    protected void startActivityClosingAllOthers(Class<?> cls) {
        Intent intent = new Intent(getApplicationContext(), cls);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Shows a short toast message with the text from a resource
     *
     * @param resId The resource id of the string resource to use
     */
    protected void showToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a short toast message with a specific message. If the fragment is not attach to any
     * activity, the toast will not show.
     *
     * @param message The text this toast will display.
     */
    protected void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a long toast message with the text from a resource
     *
     * @param resId The resource id of the string resource to use
     */
    protected void showLongToast(int resId) {
        Toast.makeText(getApplicationContext(), resId, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a long toast message with a specific message. If the fragment is not attach to any
     * activity, the toast will not show.
     *
     * @param message The text this toast will display.
     */
    protected void showLongToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    /**
     * Show and hide views. The visibility of the view to hide can be View.GONE or View.INVISIBLE.
     * @param viewToShow The view to show
     * @param viewToHide The view to hide
     * @param gone True to hide the view using View.GONE
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showHideView(final View viewToShow, final View viewToHide, final boolean gone) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewToShow.setVisibility(View.VISIBLE);
            viewToShow.animate()
                    .setDuration(shortAnimTime)
                    .alpha(1)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToShow.setVisibility(View.VISIBLE);
                        }
                    });

            viewToHide.setVisibility(gone ? View.GONE : View.INVISIBLE);
            viewToHide.animate()
                    .setDuration(shortAnimTime)
                    .alpha(0)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewToHide.setVisibility(gone ? View.GONE : View.INVISIBLE);
                        }
                    });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            viewToHide.setVisibility(gone ? View.GONE : View.INVISIBLE);
            viewToShow.setVisibility(View.VISIBLE);
        }
    }

}
