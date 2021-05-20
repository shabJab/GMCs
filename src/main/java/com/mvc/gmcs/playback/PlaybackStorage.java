package com.mvc.gmcs.playback;

import org.springframework.stereotype.Repository;

@Repository
public class PlaybackStorage {

    private static Playback PLAYBACK;

    static { PLAYBACK = new Playback(true,false, false); }

    public Playback getPLAYBACK() { return PLAYBACK; }

    public void setPause(boolean pause) {
        PLAYBACK.setPause(pause);
    }

    public void setReset(boolean reset) {
        PLAYBACK.setReset(reset);
    }

    public void setEquilibrate(boolean equilibrate) {
        PLAYBACK.setEquilibrate(equilibrate);
    }

    //    static { PLAYBACK = new Playback(false, false); }
//
//    public Playback getPLAYBACK() {
//        return PLAYBACK;
//    }
//
//    public void setPLAYBACK(Playback PLAYBACK) {
//        PlaybackStorage.PLAYBACK = PLAYBACK;
//    }
}
