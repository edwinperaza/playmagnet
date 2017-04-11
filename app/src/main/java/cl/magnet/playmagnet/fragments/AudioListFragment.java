package cl.magnet.playmagnet.fragments;

import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private static final String TAG = AudioListFragment.class.getSimpleName();

    private GridView mReportListView;
    private AudioListAdapter mAudioListAdapter;
    private ArrayList<Audio> mAudioArrayList;
    private Context mContext;
    private ImageView mPlayImageView;
    private ImageView mPauseImageView;
    private ImageView mStopImageView;

    MediaPlayer mediaplayer;
    private int playbackPosition = 0;
    private int mSelectedPosition = -1;
    private int mPreviousSelectedPosition = -1;
    private boolean isMediaPaused = false;
    private int currentPos;
    private int mSelectedId = -1;
    View updateview;

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
        mReportListView = (GridView) rootView.findViewById(R.id.gv_points_exchange_grid);

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        populateAudios(rootView);
        mReportListView.setAdapter(mAudioListAdapter);


        mReportListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                Audio audio = (Audio) parent.getItemAtPosition(position);
                mSelectedPosition = position;

                if (updateview != null) {
                    updateview.setBackgroundColor(Color.WHITE);
                }

                updateview = view;
                view.setBackgroundColor(ContextCompat.getColor(mContext, R.color.tabLayoutBackground));

                if (isMediaPaused) {
                    if (mSelectedPosition == mPreviousSelectedPosition) {
                        mediaplayer.seekTo(currentPos);
                        mediaplayer.start();
                        isMediaPaused = false;
                    }
                } else if (mediaplayer.isPlaying()) {
                    if (mSelectedPosition == mPreviousSelectedPosition) {
                        mediaplayer.pause();
                        isMediaPaused = true;
                        currentPos = mediaplayer.getCurrentPosition();
                    } else {
                        mediaplayer.stop();
                        mPreviousSelectedPosition = mSelectedPosition;
                        playAudio(audio.getAudioUrl(), view);
                    }
                } else {
                    playAudio(audio.getAudioUrl(), view);
                }
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

        AppResponseListener<JSONObject> appResponseListener = new AppResponseListener<JSONObject>(getActivity().getApplicationContext()) {
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

    public void playAudio(String url, final View view) {
        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        try {
            mediaplayer.setDataSource(url);
            mediaplayer.prepare();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        mediaplayer.start();

        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                view.setBackgroundColor(Color.WHITE);
            }
        });

    }
}
