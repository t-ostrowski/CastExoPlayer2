package com.ostro.castexoplayer2.cast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;
import com.google.android.gms.cast.framework.media.CastMediaOptions;
import com.google.android.gms.cast.framework.media.MediaIntentReceiver;
import com.google.android.gms.cast.framework.media.NotificationOptions;
import com.ostro.castexoplayer2.ui.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Ostrowski
 * on 04/11/2016.
 */

public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {
        // Cast Coach staging : CC1AD845
        CastMediaOptions mediaOptions = new CastMediaOptions.Builder()
                .setNotificationOptions(getNotificationOptions())
                .build();

        CastOptions castOptions = new CastOptions.Builder()
                .setReceiverApplicationId("CC1AD845")
                .setCastMediaOptions(mediaOptions)
                .build();
        return castOptions;
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }

    private List<String> getButtonActions() {
        List<String> buttonActions = new ArrayList<>();
        buttonActions.add(MediaIntentReceiver.ACTION_TOGGLE_PLAYBACK);
        buttonActions.add(MediaIntentReceiver.ACTION_STOP_CASTING);
        return buttonActions;
    }

    private NotificationOptions getNotificationOptions() {
        int[] compatButtonActionsIndicies = new int[]{ 0, 1 };

        NotificationOptions notificationOptions = new NotificationOptions.Builder()
                .setActions(getButtonActions(), compatButtonActionsIndicies)
                .setTargetActivityClassName(MainActivity.class.getName())
                .build();

        return notificationOptions;
    }
}
