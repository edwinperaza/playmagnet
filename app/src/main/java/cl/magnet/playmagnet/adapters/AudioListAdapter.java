package cl.magnet.playmagnet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cl.magnet.playmagnet.R;
import cl.magnet.playmagnet.models.audio.Audio;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class AudioListAdapter extends ArrayAdapter<Audio> {

    private List<Audio> mAudioList;

    public AudioListAdapter(Context context, List<Audio> audioList) {
        super(context, 0, audioList);
        mAudioList = audioList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Audio audio = mAudioList.get(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_audio_item, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.setStory(audio, getContext());

        return convertView;
    }

    private static class ViewHolder {
        TextView mReportTitleTextView;
        ImageView mReportThumbnailImageView;

        public ViewHolder(View view) {
            mReportTitleTextView = (TextView) view.findViewById(R.id.tv_report_item_title);
//            mReportThumbnailImageView = (ImageView) view.findViewById(R.id.iv_report_list_image);
        }

        public void setStory(Audio audio, Context context) {
            this.mReportTitleTextView.setText(audio.getTitle());
//            Picasso.with(context).load(audio.getAudioUrl())
//                    .fit()
//                    .centerCrop()
//                    .into(mReportThumbnailImageView);
        }
    }
}
