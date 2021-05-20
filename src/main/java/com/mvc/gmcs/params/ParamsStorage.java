package com.mvc.gmcs.params;

import org.springframework.stereotype.Repository;

@Repository
public class ParamsStorage {
    private static Params PARAMS;

    static { PARAMS = new Params(108,108,0.3225, 0.3225, 1.0); }

    public Params getPARAMS() {
        return PARAMS;
    }

    public void setPARAMS(Params PARAMS) {
        ParamsStorage.PARAMS = PARAMS;
    }
}
