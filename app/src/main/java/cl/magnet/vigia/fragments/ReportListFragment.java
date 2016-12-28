package cl.magnet.vigia.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import cl.magnet.vigia.R;
import cl.magnet.vigia.adapters.ReportListAdapter;
import cl.magnet.vigia.models.report.Report;

public class ReportListFragment extends Fragment {
    private static final String FRAGMENT_TITLE = "fragment_title";

    private ListView mReportListView;
    private ReportListAdapter mReportListAdapter;
    private ArrayList<Report> mReportArrayList;
    private Context mContext;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReportListFragment() {
        // Required empty public constructor
    }

    public static ReportListFragment newInstance(String param1) {
        ReportListFragment fragment = new ReportListFragment();
        Bundle args = new Bundle();
        args.putString(FRAGMENT_TITLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReportArrayList = new ArrayList<>();
        mReportListAdapter = new ReportListAdapter(getActivity(), mReportArrayList);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(FRAGMENT_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_report_list, container, false);
        mReportListView = (ListView) rootView.findViewById(R.id.lv_report_list);
        mReportListView.setAdapter(mReportListAdapter);
        populateReports();
        return rootView;
    }

    private void populateReports () {
        for (int i = 0; i < 10; i++) {
            Report report = new Report(i, "Reporte Nro: " + String.valueOf(i), "");
            mReportListAdapter.add(report);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int reportId) {
        if (mListener != null) {
            mListener.onReportClickListener(reportId);
        }
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
        void onReportClickListener(int reportId);
    }
}
