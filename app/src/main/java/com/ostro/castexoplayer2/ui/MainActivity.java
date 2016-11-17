package com.ostro.castexoplayer2.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.ostro.castexoplayer2.R;
import com.ostro.castexoplayer2.event.CastSessionEndedEvent;
import com.ostro.castexoplayer2.event.CastSessionStartedEvent;
import com.ostro.castexoplayer2.ui.player.CustomPlayerFragment;

import org.greenrobot.eventbus.EventBus;

import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * on 02/11/2016.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TEST_URL = "http://techslides.com/demos/sample-videos/small.mp4";

    private CastContext mCastContext;
    private SessionManager mSessionManager;
    private CastSession mCastSession;
    private final SessionManagerListener mSessionManagerListener =
            new SessionManagerListenerImpl();

    private class SessionManagerListenerImpl implements SessionManagerListener {
        @Override
        public void onSessionStarting(Session session) {
            Timber.d("onSessionStarting");
        }

        @Override
        public void onSessionStarted(Session session, String s) {
            Timber.d("onSessionStarted");
            invalidateOptionsMenu();
            EventBus.getDefault().post(new CastSessionStartedEvent());
        }

        @Override
        public void onSessionStartFailed(Session session, int i) {
            Timber.d("onSessionStartFailed");
        }

        @Override
        public void onSessionEnding(Session session) {
            Timber.d("onSessionEnding");
            EventBus.getDefault().post(new CastSessionEndedEvent(session.getSessionRemainingTimeMs()));
        }

        @Override
        public void onSessionEnded(Session session, int i) {
            Timber.d("onSessionEnded");
        }

        @Override
        public void onSessionResuming(Session session, String s) {
            Timber.d("onSessionResuming");
        }

        @Override
        public void onSessionResumed(Session session, boolean b) {
            Timber.d("onSessionResumed");
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumeFailed(Session session, int i) {
            Timber.d("onSessionResumeFailed");
        }

        @Override
        public void onSessionSuspended(Session session, int i) {
            Timber.d("onSessionSuspended");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSessionManager = CastContext.getSharedInstance(this).getSessionManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCastContext = CastContext.getSharedInstance(this);

        if (savedInstanceState == null) {
            CustomPlayerFragment customPlayerFragment =
                    CustomPlayerFragment.newInstance(getVideoUrl());
            launchFragment(customPlayerFragment);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCastSession = mSessionManager.getCurrentCastSession();
        mSessionManager.addSessionManagerListener(mSessionManagerListener);
    }

    @Override
    protected void onPause() {
        mSessionManager.removeSessionManagerListener(mSessionManagerListener);
        mCastSession = null;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_cast, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(),
                                                menu,
                                                R.id.media_route_menu_item);
        return true;
    }

    private void launchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_fragment_container, fragment, "CustomPlayerFragment");
        fragmentTransaction.commit();
    }

    private String getVideoUrl() {
        return TEST_URL;
    }

    public CastSession getCastSession() {
        return mCastSession;
    }

    public SessionManager getSessionManager() {
        return mSessionManager;
    }
}
