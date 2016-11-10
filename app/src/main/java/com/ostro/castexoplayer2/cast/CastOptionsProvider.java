package com.ostro.castexoplayer2.cast;

import android.content.Context;

import com.google.android.gms.cast.framework.CastOptions;
import com.google.android.gms.cast.framework.OptionsProvider;
import com.google.android.gms.cast.framework.SessionProvider;

import java.util.List;

/**
 * Created by Thomas Ostrowski
 * on 04/11/2016.
 */

public class CastOptionsProvider implements OptionsProvider {

    @Override
    public CastOptions getCastOptions(Context context) {
        // Cast Coach staging : CC1AD845
        CastOptions castOptions = new CastOptions.Builder()
                .setReceiverApplicationId("CC1AD845")
                .build();
        return castOptions;
    }

    @Override
    public List<SessionProvider> getAdditionalSessionProviders(Context context) {
        return null;
    }
}
