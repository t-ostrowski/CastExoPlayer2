package com.ostro.castexoplayer2.event;

/**
 * Created by Thomas Ostrowski
 * on 17/11/2016.
 */

public class CastSessionEndedEvent {

    private long sessionRemainingTime;

    public CastSessionEndedEvent() {

    }

    public CastSessionEndedEvent(long sessionRemainingTime) {
        this.sessionRemainingTime = sessionRemainingTime;
    }

    public long getSessionRemainingTime() {
        return sessionRemainingTime;
    }

    public void setSessionRemainingTime(long sessionRemainingTime) {
        this.sessionRemainingTime = sessionRemainingTime;
    }
}
