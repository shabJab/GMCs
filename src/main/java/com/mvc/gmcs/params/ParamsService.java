package com.mvc.gmcs.params;

import com.mvc.gmcs.dataprofile.Snap;
import com.mvc.gmcs.dataprofile.SnapDBAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ParamsService {

    private final  ParamsStorage paramsStorage;

    @Autowired
    public ParamsService(ParamsStorage paramsStorage) {
        this.paramsStorage = paramsStorage;
    }

    Params getParams() {
        return paramsStorage.getPARAMS();
    }
    void setParams(Params params) {
        paramsStorage.setPARAMS(params);
    }
}
