package com.mvc.gmcs.dataprofile;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/snap")
@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"})
public class SnapController {

    private final SnapService snapService;

    @Autowired
    public SnapController(SnapService snapService) {
        this.snapService = snapService;
    }

    @GetMapping
    public Snap getSnap() {
        return snapService.getSnap();
    }
//    MediaType.APPLICATION_JSON_VALUE
    @PutMapping(consumes="application/json")
//    public void putSnap(@RequestParam("dens1")double dens1,
//                        @RequestParam("dens2")double dens2) {
//        snapService.setSnap(new Snap(dens1,dens2));
    public void putSnap(@RequestHeader(name = "Accept") @RequestBody Snap snap) {
//        System.out.println(snap);
        snapService.setSnap(snap);
    }

//    @PostMapping(
//            path = "{snapID}/data",
//            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE
//    )
//    public void uploadSnapData(@PathVariable("snapID")UUID snapID,
//                               @RequestParam("snapData")double data) {
//        snapService.uploadSnapData(snapID, data);
//    }

}
