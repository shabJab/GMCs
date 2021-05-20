package com.mvc.gmcs.playback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlaybackService {

    private final PlaybackStorage playbackStorage;

    @Autowired
    public PlaybackService(PlaybackStorage playbackStorage) {
        this.playbackStorage = playbackStorage;
    }
//    public PlaybackService() {}

    Playback getPlayback() { return playbackStorage.getPLAYBACK(); }
//    void setPlayback(Playback playback) {
//        setPause(playback.isPause());
//        setPause(playback.isReset());
//    }
    void setPause(Boolean pause) { this.playbackStorage.setPause(pause); }
    void setReset(Boolean reset) { this.playbackStorage.setReset(reset); }
    void setEquilibrate(Boolean equilibrate) {
        this.playbackStorage.setEquilibrate(equilibrate);
    }
//    void setPause(boolean pause) { this.playbackStorage.setPAUSE(pause); }
//    Playback_backup getPlayback() { return playbackStorage.getPLAYBACK(); }
//    void setPlayback(Playback_backup playbackBackup) { playbackStorage.setPLAYBACK(playbackBackup); }
}
