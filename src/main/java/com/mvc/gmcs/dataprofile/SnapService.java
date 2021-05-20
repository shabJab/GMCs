package com.mvc.gmcs.dataprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SnapService {

    private final SnapDBAccessService snapDBAccessService;

    @Autowired
    public SnapService(SnapDBAccessService snapDBAccessService) {
        this.snapDBAccessService = snapDBAccessService;
    }

    Snap getSnap() {
        return snapDBAccessService.getSnap();
    }
    void setSnap(Snap snap) {
        snapDBAccessService.setSnap(snap);
    }

//    public void uploadSnapData(UUID snapID, double data) {
//        // logic behind upload
//    }
}
