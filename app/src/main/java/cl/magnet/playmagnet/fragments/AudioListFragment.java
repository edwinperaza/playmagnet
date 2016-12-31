package cl.magnet.playmagnet.fragments;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import cl.magnet.magnetrestclient.VolleyErrorHelper;
import cl.magnet.magnetrestclient.VolleyManager;
import cl.magnet.playmagnet.R;
import cl.magnet.playmagnet.adapters.AudioListAdapter;
import cl.magnet.playmagnet.models.audio.Audio;
import cl.magnet.playmagnet.models.audio.AudioRequestManager;
import cl.magnet.playmagnet.network.AppResponseListener;

public class AudioListFragment extends Fragment {
    private static final String FRAGMENT_TITLE = "fragment_title";

    private ListView mReportListView;
    private AudioListAdapter mAudioListAdapter;
    private ArrayList<Audio> mAudioArrayList;
    private Context mContext;
    private ImageView mPlayImageView;
    private ImageView mPauseImageView;
    private ImageView mStopImageView;

    MediaPlayer mediaplayer;
    private int playbackPosition=0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AudioListFragment() {
        // Required empty public constructor
    }

    public static AudioListFragment newInstance(String param1) {
        AudioListFragment fragment = new AudioListFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAudioArrayList = new ArrayList<>();
        mAudioListAdapter = new AudioListAdapter(getActivity(), mAudioArrayList);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(FRAGMENT_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_list, container, false);
        mReportListView = (ListView) rootView.findViewById(R.id.lv_report_list);

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        populateAudios(rootView);
        mReportListView.setAdapter(mAudioListAdapter);


        mReportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Audio audio = (Audio) parent.getItemAtPosition(position);

               mListener.onAudioClickListener(audio.getTitle(), audio.getAudioUrl(), audio.getComment());
                }
        });


        return rootView;
    }

    @Override
    public void onDestroy() {
        mediaplayer.stop();
        super.onDestroy();
    }

    private void populateAudios(final View rootView) {

        AppResponseListener<JSONObject> appResponseListener = new AppResponseListener<JSONObject>(getActivity().getApplicationContext()){
            @Override
            public void onResponse(JSONObject response) {
                JSONArray jsonArrayAudio = null;
                try {

                    jsonArrayAudio = response.getJSONArray("audios");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList<Audio> newListAudio = Audio.fromJsonArray(jsonArrayAudio);

                if (!newListAudio.isEmpty()) {
                    for (int i = 0; i < newListAudio.size(); i++) {
                        mAudioListAdapter.insert(newListAudio.get(i), i);
                    }

                    Snackbar.make(rootView, "Audios Actualizados", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                super.onErrorResponse(error);
                String errorVolley = VolleyErrorHelper.getMessage(error, mContext);
                Toast.makeText(mContext, errorVolley, Toast.LENGTH_SHORT).show();
            }

        };

        //We add the request
        JsonObjectRequest request = AudioRequestManager.getAudioList(appResponseListener);
        VolleyManager.getInstance(mContext).addToRequestQueue(request);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int reportId) {
//        if (mListener != null) {
//            mListener.onAudioClickListener(reportId);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onAudioClickListener(String audioTitle, String audioUrl, String audioComment);
    }
}
