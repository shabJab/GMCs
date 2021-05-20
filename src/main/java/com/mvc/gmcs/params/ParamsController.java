package com.mvc.gmcs.params;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/params")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"})
public class ParamsController {

    private final ParamsService paramsService;

    @Autowired
    public ParamsController(ParamsService paramsService) {
        this.paramsService = paramsService;
    }

    @GetMapping
    public Params getParams() {
        return paramsService.getParams();
    }

    @PutMapping(consumes = "application/json")
    public void putParams(@RequestHeader(name = "Content-type") @RequestBody Params params) {
        paramsService.setParams(params);
    }
}
