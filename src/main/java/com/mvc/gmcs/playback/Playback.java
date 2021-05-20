package com.mvc.gmcs.playback;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Playback {
    private boolean pause;
    private boolean reset;
    private boolean equilibrate;

    public Playback(@JsonProperty("pause") boolean pause,
                    @JsonProperty("reset") boolean reset,
                    @JsonProperty("equilibrate") boolean equilibrate) {
        this.pause=pause;
        this.reset=reset;
        this.equilibrate=equilibrate;
    }
//    public Playback(boolean pause, boolean reset) {
//        this.pause=pause;
//        this.reset=reset;
//    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    public boolean isReset() {
        return reset;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isEquilibrate() {
        return equilibrate;
    }

    public void setEquilibrate(boolean equilibrate) {
        this.equilibrate = equilibrate;
    }
}
