package com.projects.kquicho.uwatm8;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.h6ah4i.android.widget.advrecyclerview.animator.GeneralItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.animator.SwipeDismissItemAnimator;
import com.h6ah4i.android.widget.advrecyclerview.decoration.SimpleListDividerDecorator;
import com.h6ah4i.android.widget.advrecyclerview.swipeable.RecyclerViewSwipeManager;
import com.h6ah4i.android.widget.advrecyclerview.touchguard.RecyclerViewTouchActionGuardManager;
import com.projects.kquicho.uw_api_client.Core.APIResult;
import com.projects.kquicho.uw_api_client.Core.JSONDownloader;
import com.projects.kquicho.uw_api_client.Core.UWOpenDataAPI;
import com.projects.kquicho.uw_api_client.Resources.InfoSession;
import com.projects.kquicho.uw_api_client.Resources.ResourcesParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.ListIterator;
import java.util.Locale;
import java.util.TimeZone;

public class InfoSessionsFragment extends Fragment implements JSONDownloader.onDownloadListener,
        InfoSessionAdapter.onInfoSessionClickListener{
    final String TAG = "InfoSessionsFragment";
    private final int ALERT_CHANGE_REQUEST = 1;
    public static final String SHOULD_TOGGLE = "shouldToggle";

    private ArrayList<InfoSessionData> mData = new ArrayList<>();
    private InfoSessionAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mWrappedAdapter;
    private RecyclerViewSwipeManager mRecyclerViewSwipeManager;
    private RecyclerViewTouchActionGuardManager mRecyclerViewTouchActionGuardManager;
    private ResourcesParser mParser = new ResourcesParser();
    private View mEmptyView;
    private FloatingActionButton mFab;
    private ProgressBar mProgressBar;
    private int mCurrentInfoSessionPosition;

    public InfoSessionsFragment(){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_sessions, parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mProgressBar = (ProgressBar)view.findViewById(R.id.pbLoading);
        mEmptyView = view.findViewById(R.id.empty_view);
        mRecyclerView =  (RecyclerView)view.findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        // touch guard manager  (this class is required to suppress scrolling while swipe-dismiss animation is running)
        mRecyclerViewTouchActionGuardManager = new RecyclerViewTouchActionGuardManager();
        mRecyclerViewTouchActionGuardManager.setInterceptVerticalScrollingWhileAnimationRunning(true);
        mRecyclerViewTouchActionGuardManager.setEnabled(true);

        // swipe manager
        mRecyclerViewSwipeManager = new RecyclerViewSwipeManager();

        Drawable selected = ContextCompat.getDrawable(getActivity(), R.drawable.ic_star);
        Drawable unselected = ContextCompat.getDrawable(getActivity(), R.drawable.ic_star_outline);
        mAdapter = new InfoSessionAdapter(mData, selected, unselected, InfoSessionDBHelper.getInstance(getContext()),
                getActivity(), this);

        mWrappedAdapter = mRecyclerViewSwipeManager.createWrappedAdapter(mAdapter);      // wrap for swiping

        final GeneralItemAnimator animator = new SwipeDismissItemAnimator();
        // Change animations are enabled by default since support-v7-recyclerview v22.
        // Disable the change animation in order to make turning back animation of swiped item works properly.
        animator.setSupportsChangeAnimations(false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mWrappedAdapter);
        mRecyclerView.setItemAnimator(animator);

        mRecyclerView.addItemDecoration(new SimpleListDividerDecorator(ContextCompat.getDrawable(getContext(), R.drawable.list_divider_h), true));

        mRecyclerViewTouchActionGuardManager.attachRecyclerView(mRecyclerView);
        mRecyclerViewSwipeManager.attachRecyclerView(mRecyclerView);

        mParser.setParseType(ResourcesParser.ParseType.INFOSESSIONS.ordinal());
        String url = UWOpenDataAPI.buildURL(mParser.getEndPoint());

        mProgressBar.setVisibility(View.VISIBLE);
        JSONDownloader downloader = new JSONDownloader(url);
        downloader.setOnDownloadListener(this);
        downloader.start();

        mFab = (FloatingActionButton)view.findViewById(R.id.fab);


        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.fabClick();
            }
        });
    }


    @Override
    public void onDownloadFail(String givenURL, int index) {
        Log.e(TAG, "Download failed.. url = " + givenURL);
    }

    @Override
    public void onDownloadComplete(APIResult apiResult) {
        mParser.setAPIResult(apiResult);
        mParser.parseJSON();
        ArrayList<InfoSession> infoSessions = mParser.getInfoSessions();
        ListIterator li = infoSessions.listIterator(infoSessions.size());
        InfoSessionDBHelper dbHelper = InfoSessionDBHelper.getInstance(getActivity().getApplicationContext());
        DateFormat format = new SimpleDateFormat("MMM d, yy HH:mm", Locale.CANADA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        while (li.hasPrevious()){
            InfoSession infoSession = (InfoSession)li.previous();
            Date date = null;
            try {
                date = format.parse(infoSession.getDate() + " " + infoSession.getStart_time());
            } catch (ParseException exception) {
                Log.e(TAG, "onReceive ParseException: " + exception.getMessage());
            }
           // if (date == null ||  date.getTime() < System.currentTimeMillis()) {
            if (date == null) {
                break;
            }

            boolean isAlertSet = dbHelper.checkForInfoSession(String.valueOf(infoSession.getId()));

            mData.add(new InfoSessionData(infoSession, isAlertSet, date.getTime()));

        }
        Collections.sort(mData, new CustonComparator());
        android.os.Handler handler = new android.os.Handler(getActivity().getMainLooper());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
                if(mData == null || mData.size() == 0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    mFab.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    mFab.setVisibility(View.VISIBLE);
                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        handler.post(runnable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALERT_CHANGE_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(SHOULD_TOGGLE, false)) {
                    mAdapter.toggleSetAlert(mCurrentInfoSessionPosition);
                }
            }
        }
    }

    @Override
    public void onInfoSessionClick(InfoSessionData infoSessionData, int position) {
        mCurrentInfoSessionPosition = position;
        Intent intent = new Intent(getActivity(), InfoSessionActivity.class);
        intent.putExtra(InfoSessionActivity.INFO_SESSION, infoSessionData.getInfoSession());
        intent.putExtra(InfoSessionActivity.TIME, infoSessionData.getTime());
        intent.putExtra(InfoSessionActivity.IS_ALARM_SET, infoSessionData.isAlertSet());

        startActivityForResult(intent, ALERT_CHANGE_REQUEST);

    }

    public class CustonComparator implements Comparator<InfoSessionData>{
        @Override
        public int compare(InfoSessionData o1, InfoSessionData o2) {
            return  o1.getTime()<o2.getTime()?-1:
                    o1.getTime()>o2.getTime()?1:0;
        }

    }
}

