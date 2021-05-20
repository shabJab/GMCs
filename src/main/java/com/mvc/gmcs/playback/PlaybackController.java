package com.mvc.gmcs.playback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/playback")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"})
public class PlaybackController {

    private final PlaybackService playbackService;

    @Autowired
    public PlaybackController(PlaybackService playbackService) {
        this.playbackService = playbackService;
    }

    @GetMapping
    public Playback getPlayback() {
        return playbackService.getPlayback();
    }

    @PatchMapping(consumes = "application/x-www-form-urlencoded")
    public void patchPlayback(
            @RequestParam(value = "PAUSE", required = false)Optional<Boolean> PAUSE,
            @RequestParam(value = "RESET", required = false)Optional<Boolean> RESET,
            @RequestParam(value = "EQUILIBRATE", required = false)Optional<Boolean> EQUILIBRATE) {
        PAUSE.ifPresent(playbackService::setPause);
        RESET.ifPresent(playbackService::setReset);
        EQUILIBRATE.ifPresent(playbackService::setEquilibrate);
    }

    @PutMapping(consumes="application/json")
//    public void putSnap(@RequestParam("dens1")double dens1,
//                        @RequestParam("dens2")double dens2) {
//        snapService.setSnap(new Snap(dens1,dens2));
    public void putPlayback(@RequestHeader(name = "Accept") @RequestBody Playback playback) {
//        System.out.println(snap);
//        playbackService.setPlayback(playback);
        playbackService.setPause(playback.isPause());
        playbackService.setReset(playback.isReset());
        playbackService.setEquilibrate(playback.isEquilibrate());

    }
}
