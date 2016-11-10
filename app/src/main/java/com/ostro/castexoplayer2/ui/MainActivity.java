package com.ostro.castexoplayer2.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.Session;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.ostro.castexoplayer2.R;
import com.ostro.castexoplayer2.ui.player.CustomPlayerFragment;

import timber.log.Timber;

/**
 * Created by Thomas Ostrowski
 * on 02/11/2016.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TEST_URL = "http://techslides.com/demos/sample-videos/small.mp4";

    private Button btnCast;

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
        }

        @Override
        public void onSessionStartFailed(Session session, int i) {
            Timber.d("onSessionStartFailed");
        }

        @Override
        public void onSessionEnding(Session session) {
            Timber.d("onSessionEnding");
        }

        @Override
        public void onSessionEnded(Session session, int i) {
            Timber.d("onSessionEnded");
            finish();
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

        btnCast = (Button) findViewById(R.id.btn_cast);

        btnCast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRemoteMedia(1, true);
            }
        });
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

    private void loadRemoteMedia(int position, boolean autoPlay) {
        if (mCastSession == null) {
            mCastSession = mSessionManager.getCurrentCastSession();
        }
        if (mCastSession != null) {
            final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();
            if (remoteMediaClient == null) {
                Timber.d("remoteMediaClient == null");
                return;
            }
            remoteMediaClient.load(getMediaInfo(), autoPlay, position);
        } else {
            Timber.d("mCastSession == null");
        }
    }

    private MediaInfo getMediaInfo() {
        MediaMetadata movieMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        movieMetadata.putString(MediaMetadata.KEY_TITLE, "Test video");

        MediaInfo mediaInfo = new MediaInfo.Builder(getVideoUrl())
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("videos/mp4")
                .setStreamDuration(MediaInfo.UNKNOWN_DURATION)
                .setMetadata(movieMetadata)
                .build();

        return mediaInfo;
    }
}
