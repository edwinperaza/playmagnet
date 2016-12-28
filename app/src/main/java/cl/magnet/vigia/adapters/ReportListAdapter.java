package cl.magnet.vigia.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cl.magnet.vigia.R;
import cl.magnet.vigia.models.report.Report;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class ReportListAdapter extends ArrayAdapter<Report> {

    private List<Report> mReportList;

    public ReportListAdapter(Context context, List<Report> reportList) {
        super(context, 0, reportList);
        mReportList = reportList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Report report = mReportList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_report_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setStory(report, getContext());

        return convertView;
    }

    private static class ViewHolder {
        TextView mReportTitleTextView;
        ImageView mReportThumbnailImageView;

        public ViewHolder(View view) {
            mReportTitleTextView = (TextView) view.findViewById(R.id.tv_report_item_title);
            mReportThumbnailImageView = (ImageView) view.findViewById(R.id.iv_report_list_image);
        }

        public void setStory(Report report, Context context) {
            this.mReportTitleTextView.setText(report.getTitle());
//            Picasso.with(context).load(report.getImageUrl())
//                    .fit()
//                    .centerCrop()
//                    .into(mReportThumbnailImageView);
        }
    }
}
