package com.khc.controller;

import com.khc.dto.StateSaveReq;
import com.khc.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/state")
public class StateController {
    @Autowired
    private StateService stateService;

    @PostMapping("/save")
    public ResponseEntity<String> stateSave(@RequestBody StateSaveReq stateSaveReq) {
        String mas = stateService.stateSave(stateSaveReq);
        return new ResponseEntity<>(mas, HttpStatus.OK);
    }
}