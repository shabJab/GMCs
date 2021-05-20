package com.mvc.gmcs.datastore;

import com.mvc.gmcs.dataprofile.Snap;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeSnapStorage {

//    private static List<Snap> SNAPS = new ArrayList<>();
    private static Snap SNAP;

//    static {
//        SNAPS.add(new Snap(UUID.randomUUID(), "FirstSnap", 0));
//        SNAPS.add(new Snap(UUID.randomUUID(), "SecondSnap", 0));
//        SNAPS.add(new Snap(UUID.randomUUID(), "ThirdSnap", 0));
//    }
    static { SNAP = new Snap(); }

    public Snap getSNAP() {
        return SNAP;
    }

    public void setSNAP(Snap SNAP) {
        FakeSnapStorage.SNAP = SNAP;
    }


//    public List<Snap> getSnaps() {
//        return SNAPS;
//    }
}
