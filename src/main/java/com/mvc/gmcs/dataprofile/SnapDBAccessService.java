package com.mvc.gmcs.dataprofile;

import com.mvc.gmcs.datastore.FakeSnapStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SnapDBAccessService {

    private final FakeSnapStorage fakeSnapStorage;

    @Autowired
    public SnapDBAccessService(FakeSnapStorage fakeSnapStorage) {
        this.fakeSnapStorage = fakeSnapStorage;
    }

    Snap getSnap() {
        return fakeSnapStorage.getSNAP();
    }
    void setSnap(Snap snap) {
        fakeSnapStorage.setSNAP(snap);
    }
}
