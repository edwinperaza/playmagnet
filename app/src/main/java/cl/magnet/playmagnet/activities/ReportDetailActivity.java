package cl.magnet.playmagnet.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cl.magnet.playmagnet.R;

public class ReportDetailActivity extends AppCompatActivity {

    private ImageView mReportAcceptImageView;
    private ImageView mReportDeniedImageView;
    private TextView mReportShareTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarTitleColor));
        toolbar.setTitle("Detalle de reporte");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mReportAcceptImageView = (ImageView) findViewById(R.id.iv_report_accept);
        mReportDeniedImageView = (ImageView) findViewById(R.id.iv_report_denied);
        mReportShareTextView = (TextView) findViewById(R.id.tv_report_item_share);

        mReportAcceptImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reporte Aceptado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mReportDeniedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reporte Negado", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mReportShareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareAction();
            }
        });
    }

    public void shareAction(){
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        String invitationCode = "Información relacionada al reporte en pantalla";
        share.putExtra(Intent.EXTRA_TEXT, invitationCode);
        startActivity(Intent.createChooser(share, "Vigia App"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
