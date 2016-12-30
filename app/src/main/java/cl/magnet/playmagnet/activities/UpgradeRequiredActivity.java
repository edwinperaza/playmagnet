package cl.magnet.playmagnet.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import cl.magnet.playmagnet.R;

/**
 * A screen that leads you to the app store where you have to update the app.
 */
public class UpgradeRequiredActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_required);

        Button updateButton = (Button) findViewById(R.id.upgrade_required_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String appPackageName = getPackageName();

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }

            }
        });

    }
}
