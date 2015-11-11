package cl.magnet.magnetprojecttemplate.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import cl.magnet.magnetprojecttemplate.network.AppErrorListener;

/**
 * Created by yaniv on 11/4/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private BroadcastReceiver mUnauthorizedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            startActivityClosingAllOthers(LoginActivity.class);
        }
    };

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
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mUnauthorizedReceiver, new IntentFilter(AppErrorListener.ACTION_UNAUTHORIZED));
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mUpgradeRequiredReceiver, new IntentFilter(AppErrorListener.ACTION_UPGRADE_REQUIRED));
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


}
